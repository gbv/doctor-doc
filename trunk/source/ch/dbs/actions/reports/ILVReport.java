//  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//  Contact: info@doctor-doc.com

package ch.dbs.actions.reports;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.actions.bestellung.BestellformAction;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.IlvReportForm;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

import com.sun.mail.smtp.SMTPAddressFailedException;

/**
 * Erstellt PDF-Reports
 *
 * @author Pascal Steiner
 *
 */
public final class ILVReport extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(ILVReport.class);

    /**
     * ILV-Report Formular Daten vorbereiten
     */
    public ActionForward prepareFormIlv(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) fm;
        final Auth auth = new Auth();
        // Make sure method is only accessible when user is logged in
        String forward = "failure";
        if (auth.isLogin(rq)) {
            forward = "success";

            final Text cn = new Text();

            try {

                // this is a small hack, because the ILV report is being
                // composed on the JSP, where we can't exchange the country
                // codes (CH / DE / US) against the full name of the country
                final Countries country = new Countries(pageForm.getKonto().getLand(), cn.getConnection());
                pageForm.getKonto().setLand(country.getCountryname());

                rq.setAttribute("orderform", pageForm);
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("uebersicht");
                rq.setAttribute("ActiveMenus", mf);

            } catch (final Exception e) {
                forward = "failure";

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                LOG.error("journalorderdetail: " + e.toString());

            } finally {
                cn.close();
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }

    /**
     * Erstelt ein PDF- Report (ILV-Bestellung)
     */
    public ActionForward PDF(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                final IlvReportForm ilvf = (IlvReportForm) fm;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                OutputStream out = null;

                // set ILV form number from wildcard mapping
                ilvf.setIlvformnr(getIlvNumber(mp.getPath()));

                // prepare output stream
                rp.setContentType("application/pdf");
                rp.setHeader("Content-Disposition", "attachment;filename=ilv.pdf");
                // run report, depending on ILV form number
                try {
                    out = rp.getOutputStream();
                    runReport(ilvf, ui, out);
                } catch (final Exception e) {
                    LOG.error("OutputStream failed: " + e.toString());
                } finally {
                    // send report to browser
                    try {
                        out.flush();
                        out.close();
                    } catch (final IOException e) {
                        LOG.error("orderspdf: " + e.toString());
                    }
                    forward = null;
                }

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    /**
     * Prepare the Mail with attached ilv-pdf (ILV-Bestellung) for the preparemail.jsp
     */
    public ActionForward Email(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final IlvReportForm ilvf = (IlvReportForm) fm;
                final Lieferanten l = new Lieferanten();
                ilvf.setTo(l.getLieferantFromLid(ilvf.getLid(), l.getConnection()).getEmailILL());

                // default Subject & Mailtext
                final Text subject = new Text(l.getConnection(), new Texttyp("ILV Mailsubject", l.getConnection()), ui
                        .getKonto().getId());
                final Text text = new Text(l.getConnection(), new Texttyp("ILV Mailtext", l.getConnection()), ui
                        .getKonto().getId());
                if (subject.getInhalt() != null) {
                    ilvf.setSubject(subject.getInhalt());
                }
                if (text.getInhalt() != null) {
                    ilvf.setMailtext(text.getInhalt());
                }

                l.close();

                rq.setAttribute("IlvReportForm", ilvf);
                // set ILV form number back into request
                rq.setAttribute("ilvformnr", getIlvNumber(mp.getPath()));
                // set ILV form number back into request
                rq.setAttribute("ilvformnr", getIlvNumber(mp.getPath()));
                forward = "preparemail";

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    /**
     * Send the mail with attached ilv-pdf (ILV-Bestellung)
     */
    public ActionForward sendIlvMail(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                final IlvReportForm ilvf = (IlvReportForm) fm;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                try {
                    // prepare attachement
                    DataSource aAttachment = null;
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    // create PDF report using the appropriate ILV form number
                    runReport(ilvf, ui, baos);

                    aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");

                    // send Mail
                    final InternetAddress to[] = new InternetAddress[2];
                    to[0] = new InternetAddress(ilvf.getTo());
                    to[1] = new InternetAddress(ui.getKonto().getBibliotheksmail());
                    final String fileAttachment = "ilv.pdf";
                    final MHelper mh = new MHelper();
                    final Session session = Session.getDefaultInstance(mh.getProperties());

                    // Define message
                    final MimeMessage message = mh.getMimeMessage(session);
                    message.setSubject(ilvf.getSubject());

                    // set reply to address
                    message.setReplyTo(new InternetAddress[] { new InternetAddress(ui.getKonto().getBibliotheksmail()) });

                    // create the message part 
                    MimeBodyPart messageBodyPart = new MimeBodyPart();

                    //fill message
                    messageBodyPart.setText(composeMailBody(ui, ilvf));
                    final Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // Part two is attachment
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(aAttachment));
                    messageBodyPart.setFileName(fileAttachment);
                    multipart.addBodyPart(messageBodyPart);

                    // Put parts in message
                    message.setContent(multipart);
                    message.saveChanges();

                    // Send the message
                    mh.sendMessage(session, message, to);
                    //                    Transport.send(message);

                    forward = "success";
                    final String content = "ilvmail.confirmation";
                    final String link = "listkontobestellungen.do?method=overview";
                    final ch.dbs.form.Message mes = new ch.dbs.form.Message(content, link);
                    rq.setAttribute("message", mes);
                    rq.setAttribute("IlvReportForm", ilvf);

                } catch (final JRException e1) {
                    final ErrorMessage em = new ErrorMessage("error.createilvreport",
                            "listkontobestellungen.do?method=overview");
                    rq.setAttribute("errormessage", em);
                } catch (final SMTPAddressFailedException e) {
                    final ErrorMessage em = new ErrorMessage("errors.email", e.getMessage(),
                            "listkontobestellungen.do?method=overview");
                    rq.setAttribute("errormessage", em);
                } catch (final AuthenticationFailedException e) {
                    final ErrorMessage em = new ErrorMessage("error.mailserverconnection", e.getMessage(),
                            "listkontobestellungen.do?method=overview");
                    rq.setAttribute("errormessage", em);
                    //SMTPSendFailedException
                } catch (final MessagingException e) {
                    final ErrorMessage em = new ErrorMessage("error.sendmail", e.getMessage(),
                            "listkontobestellungen.do?method=overview");
                    rq.setAttribute("errormessage", em);
                } catch (final Exception e) {
                    final ErrorMessage em = new ErrorMessage("error.sendmail", e.getMessage(),
                            "listkontobestellungen.do?method=overview");
                    rq.setAttribute("errormessage", em);
                }

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    private void runReport(final IlvReportForm ilvf, final UserInfo ui, final OutputStream out) throws JRException {

        InputStream reportStream;
        Map<String, Object> values;

        switch (ilvf.getIlvformnr()) {
        case 0:
            values = reportMainz(ilvf, ui);
            reportStream = new BufferedInputStream(this.getServlet().getServletContext()
                    .getResourceAsStream("/reports/ILV-Form_0.jasper"));
            break;
        case 1:
            values = reportCharite(ilvf, ui);
            reportStream = new BufferedInputStream(this.getServlet().getServletContext()
                    .getResourceAsStream("/reports/ILV-Form_1.jasper"));
            break;
        default:
            values = reportMainz(ilvf, ui); // default case for illegal values
            reportStream = new BufferedInputStream(this.getServlet().getServletContext()
                    .getResourceAsStream("/reports/ILV-Form_0.jasper"));
            break;
        }

        final Collection<Map<String, ?>> al = new ArrayList<Map<String, ?>>();
        final HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("Fake", "Daten damit Report nicht leer wird..");
        al.add(hm);
        final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);

        JasperRunManager.runReportToPdfStream(reportStream, out, values, ds);
    }

    private Map<String, Object> reportMainz(final IlvReportForm ilvf, final UserInfo ui) {

        final Map<String, Object> result = new ConcurrentHashMap<String, Object>();

        final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
        tf.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));
        final Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));

        // Labels vorbereiten
        result.put("reporttitle", ilvf.getReporttitle() + " " + tf.format(cal.getTime(), ui.getKonto().getTimezone()));
        result.put("labelfrom", ilvf.getLabelfrom());
        result.put("labelto", ilvf.getLabelto());
        result.put("labelsignatur", ilvf.getLabelsignatur());
        result.put("labeljournaltitel", ilvf.getLabeljournaltitel());
        result.put("labelcustomer", ilvf.getLabelcustomer());
        result.put("labelname", ilvf.getLabelname());
        result.put("labellibrarycard", ilvf.getLabellibrarycard());
        result.put("labelissn", ilvf.getLabelissn());
        result.put("labelpmid", ilvf.getLabelpmid());
        result.put("labelyear", ilvf.getLabelyear());
        result.put("labelvolumevintage", ilvf.getLabelvolumevintage());
        result.put("labelbooklet", ilvf.getLabelbooklet());
        result.put("labelclinicinstitutedepartment", ilvf.getLabelclinicinstitutedepartment());
        result.put("labelphone", ilvf.getLabelphone());
        result.put("labelfax", ilvf.getLabelfax());
        result.put("labelsendto", ilvf.getLabelsendto());
        result.put("labelpages", ilvf.getLabelpages());
        result.put("labelauthorofessay", ilvf.getLabelauthorofessay());
        result.put("labeltitleofessay", ilvf.getLabeltitleofessay());
        result.put("labelendorsementsofdeliveringlibrary", ilvf.getLabelendorsementsofdeliveringlibrary());
        result.put("labelnotesfromrequestinglibrary", ilvf.getLabelnotesfromrequestinglibrary());

        // Values abfüllen
        if (ui.getKonto().getIsil() != null) {
            result.put("isil", ui.getKonto().getIsil());
        } else {
            result.put("isil", "");
        }
        result.put("from", ui.getKonto().getBibliotheksname()); // cannot be null
        result.put("to", ilvf.getLieferant());
        result.put("signatur", ilvf.getSignatur());
        result.put("journaltitel", ilvf.getJournaltitel());
        result.put("name", ilvf.getName());
        result.put("issn", ilvf.getIssn());
        result.put("pmid", ilvf.getPmid());
        result.put("librarycard", ilvf.getLibrarycard());
        result.put("year", ilvf.getYear());
        result.put("volumevintage", ilvf.getVolumevintage());
        result.put("booklet", ilvf.getBooklet());
        result.put("phone", ilvf.getPhone());
        if (ui.getKonto().getTelefon() != null) {
            result.put("phonekonto", ui.getKonto().getTelefon());
        } else {
            result.put("phonekonto", "");
        }
        if (ui.getKonto().getFax_extern() != null) {
            result.put("fax", ui.getKonto().getFax_extern());
        } else {
            result.put("fax", "");
        }
        result.put("adresse", ilvf.getPost());
        result.put("clinicinstitutedepartment", ilvf.getClinicinstitutedepartment());
        result.put("pages", ilvf.getPages());
        result.put("authorofessay", ilvf.getAuthorofessay());
        result.put("titleofessay", ilvf.getTitleofessay());
        result.put("notesfromrequestinglibrary", ilvf.getNotesfromrequestinglibrary());
        result.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName() + ": "
                + ReadSystemConfigurations.getServerWelcomepage());

        return result;
    }

    private Map<String, Object> reportCharite(final IlvReportForm ilvf, final UserInfo ui) {

        final Map<String, Object> result = new ConcurrentHashMap<String, Object>();

        final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
        tf.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));
        final Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));

        // Labels vorbereiten
        result.put("reporttitle", ilvf.getReporttitle() + " " + tf.format(cal.getTime(), ui.getKonto().getTimezone()));
        result.put("labelfrom", ilvf.getLabelfrom());
        result.put("labelto", ilvf.getLabelto());
        result.put("labelsignatur", ilvf.getLabelsignatur());
        result.put("labeljournaltitel", ilvf.getLabeljournaltitel());
        result.put("labelcustomer", ilvf.getLabelcustomer());
        result.put("labelname", ilvf.getLabelname());
        result.put("labelissn", ilvf.getLabelissn());
        result.put("labelpmid", ilvf.getLabelpmid());
        result.put("labelyear", ilvf.getLabelyear());
        result.put("labelvolumevintage", ilvf.getLabelvolumevintage());
        result.put("labelbooklet", ilvf.getLabelbooklet());
        result.put("labelclinicinstitutedepartment", ilvf.getLabelclinicinstitutedepartment());
        result.put("labelphone", ilvf.getLabelphone());
        result.put("labelfax", ilvf.getLabelfax());
        result.put("labelsendto", ilvf.getLabelsendto());
        result.put("labelpages", ilvf.getLabelpages());
        result.put("labelauthorofessay", ilvf.getLabelauthorofessay());
        result.put("labeltitleofessay", ilvf.getLabeltitleofessay());
        result.put("labelendorsementsofdeliveringlibrary", ilvf.getLabelendorsementsofdeliveringlibrary());
        result.put("labelnotesfromrequestinglibrary", ilvf.getLabelnotesfromrequestinglibrary());

        // Values abfüllen
        if (ui.getKonto().getIsil() != null) {
            result.put("isil", ui.getKonto().getIsil());
        } else {
            result.put("isil", "");
        }
        result.put("from", ui.getKonto().getBibliotheksname()); // cannot be null
        result.put("to", ilvf.getLieferant());
        result.put("signatur", ilvf.getSignatur());
        result.put("journaltitel", ilvf.getJournaltitel());
        result.put("name", ilvf.getName());
        result.put("issn", ilvf.getIssn());
        result.put("pmid", ilvf.getPmid());
        result.put("year", ilvf.getYear());
        result.put("volumevintage", ilvf.getVolumevintage());
        result.put("booklet", ilvf.getBooklet());
        result.put("phone", ilvf.getPhone());
        if (ui.getKonto().getTelefon() != null) {
            result.put("phonekonto", ui.getKonto().getTelefon());
        } else {
            result.put("phonekonto", "");
        }
        if (ui.getKonto().getFax_extern() != null) {
            result.put("fax", ui.getKonto().getFax_extern());
        } else {
            result.put("fax", "");
        }
        result.put("adresse", ilvf.getPost());
        result.put("clinicinstitutedepartment", ilvf.getClinicinstitutedepartment());
        result.put("pages", ilvf.getPages());
        result.put("authorofessay", ilvf.getAuthorofessay());
        result.put("titleofessay", ilvf.getTitleofessay());
        result.put("notesfromrequestinglibrary", ilvf.getNotesfromrequestinglibrary());
        result.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName() + ": "
                + ReadSystemConfigurations.getServerWelcomepage());

        return result;
    }

    /**
     * Get the ILV form number from the wildcard mapping. Defaults to 0, if
     * mapping is invalid.
     */
    private int getIlvNumber(final String path) {

        int number = 0;

        if (path != null && path.contains("-")) {
            try {
                number = Integer.valueOf(path.substring(path.lastIndexOf("-") + 1, path.length()));
            } catch (final NumberFormatException e) {
                LOG.error(e.toString());
            }
        }

        return number;
    }

    private String composeMailBody(final UserInfo ui, final IlvReportForm ilvf) {

        // current date and time
        final Date d = new Date();
        final ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        final String date = sdf.format(d, ui.getKonto().getTimezone());

        final StringBuffer result = new StringBuffer(320);
        result.append("Please reply to: ");
        result.append(ui.getKonto().getDbsmail());
        result.append("\n-----\n\n");

        // append customized text
        result.append(ilvf.getMailtext());

        // signature
        result.append("\n\n");
        result.append(ui.getKonto().getBibliotheksname());
        result.append('\n');
        result.append(ui.getKonto().getAdresse());
        if (ui.getKonto().getAdressenzusatz() != null && !"".equals(ui.getKonto().getAdressenzusatz())) {
            result.append('\n');
            result.append(ui.getKonto().getAdressenzusatz());
        }
        result.append('\n');
        result.append(ui.getKonto().getLand());
        result.append('-');
        result.append(ui.getKonto().getPLZ());
        result.append(' ');
        result.append(ui.getKonto().getOrt());
        result.append("\n\nPhone:\t"); // phone is a required field
        result.append(ui.getKonto().getTelefon());
        if (ui.getKonto().getFax_extern() != null && !"".equals(ui.getKonto().getFax_extern())) {
            result.append("\nFax:\t");
            result.append(ui.getKonto().getFax_extern());
        }

        result.append("\n\n-----\n");

        // order details
        if (ilvf.getAuthorofessay() != null && !"".equals(ilvf.getAuthorofessay())) {
            result.append("AUTHOR: ");
            result.append(ilvf.getAuthorofessay());
            result.append('\n');
        }
        if (ilvf.getTitleofessay() != null && !"".equals(ilvf.getTitleofessay())) {
            result.append("TITLE OF ARTICLE: ");
            result.append(ilvf.getTitleofessay());
            result.append('\n');
        }
        if (ilvf.getJournaltitel() != null && !"".equals(ilvf.getJournaltitel())) {
            result.append("JOURNAL: ");
            result.append(ilvf.getJournaltitel());
            result.append('\n');
        }
        if (ilvf.getIssn() != null && !"".equals(ilvf.getIssn())) {
            result.append("ISSN: ");
            result.append(ilvf.getIssn());
            result.append('\n');
        }
        if (ilvf.getYear() != null && !"".equals(ilvf.getYear())) {
            result.append("YEAR: ");
            result.append(ilvf.getYear());
            result.append('\n');
        }
        if (ilvf.getVolumevintage() != null && !"".equals(ilvf.getVolumevintage())) {
            result.append("VOLUME: ");
            result.append(ilvf.getVolumevintage());
            result.append('\n');
        }
        if (ilvf.getBooklet() != null && !"".equals(ilvf.getBooklet())) {
            result.append("ISSUE: ");
            result.append(ilvf.getBooklet());
            result.append('\n');
        }
        if (ilvf.getPages() != null && !"".equals(ilvf.getPages())) {
            result.append("PAGES: ");
            result.append(ilvf.getPages());
            result.append('\n');
        }
        if (ilvf.getPmid() != null && !"".equals(ilvf.getPmid())) {
            result.append("PMID: ");
            result.append(ilvf.getPmid());
            result.append('\n');
            result.append("PMID-URI: ");
            result.append("http://www.ncbi.nlm.nih.gov/pubmed/");
            final BestellformAction ba = new BestellformAction();
            result.append(ba.extractPmid(ilvf.getPmid()));
        }

        result.append("\n\n-----\nOrder date: ");
        result.append(date);
        result.append("\nBrought to you by ");
        result.append(ReadSystemConfigurations.getApplicationName());
        result.append(": ");
        result.append(ReadSystemConfigurations.getServerWelcomepage());
        result.append('\n');

        return result.toString();
    }
}
