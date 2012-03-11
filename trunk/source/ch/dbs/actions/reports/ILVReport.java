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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
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

import com.sun.mail.smtp.SMTPAddressFailedException;

import util.Auth;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.IlvReportForm;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

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
    public ActionForward prepareFormIlv(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

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
    public ActionForward PDF(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

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
                rp.setHeader("Content-Disposition","attachment;filename=ilv.pdf");
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
                final ErrorMessage em = new ErrorMessage(
                        "error.berechtigung",
                        "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage(
                    "error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }
    
    /**
     * Prepare the Mail with attached ilv-pdf (ILV-Bestellung) for the preparemail.jsp
     */
    public ActionForward Email(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
            	
            	final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            	IlvReportForm ilvf = (IlvReportForm) fm;
            	Lieferanten l = new Lieferanten();
            	ilvf.setTo(l.getLieferantFromName(ilvf.getLieferant(), l.getConnection()).getEmailILL());
            	
            	// default Subject & Mailtext
            	Text subject = new Text(l.getConnection(), new Texttyp("ILV Mailsubject", l.getConnection()), ui.getKonto().getId());
            	Text text = new Text(l.getConnection(), new Texttyp("ILV Mailtext", l.getConnection()), ui.getKonto().getId());
            	if (subject.getInhalt() != null){
            		ilvf.setSubject(subject.getInhalt());
            		}
            	if (text.getInhalt() != null){
            		ilvf.setSubject(text.getInhalt());
            		}
            	
            	l.close();
            	
            	rq.setAttribute("IlvReportForm", ilvf);
            	// set ILV form number back into request
            	rq.setAttribute("ilvformnr", getIlvNumber(mp.getPath()));
            	// set ILV form number back into request
            	rq.setAttribute("ilvformnr", getIlvNumber(mp.getPath()));
                forward = "preparemail";                

            } else {
                final ErrorMessage em = new ErrorMessage(
                        "error.berechtigung",
                        "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage(
                    "error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }
    
    /**
     * Send the mail with attached ilv-pdf (ILV-Bestellung)
     */
    public ActionForward sendIlvMail(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

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
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    // create PDF report using the appropriate ILV form number
                    runReport(ilvf, ui, baos);

                    aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf"); 

                    // send Mail
                    InternetAddress to[] = new InternetAddress[2];
                    to[0] = new InternetAddress(ilvf.getTo());
                    to[1] = new InternetAddress(ui.getKonto().getBibliotheksmail());
                    String fileAttachment = "ilv.pdf";                
                    MHelper mh = new MHelper();
//                    Session session = mh.getSession();
                 // Get the default Session object.
                    Session session = Session.getDefaultInstance(mh.getProperties());

                    // Define message
                    MimeMessage message = mh.getMimeMessage(session);
//                    message.addRecipients(Message.RecipientType.TO, ilvf.getTo());
//                    message.addRecipients(Message.RecipientType.BCC, "mail@test.ch");
                    message.setSubject(ilvf.getSubject());

                    // create the message part 
                    MimeBodyPart messageBodyPart = new MimeBodyPart();

                    //fill message
                    messageBodyPart.setText(ilvf.getMailtext());
                    Multipart multipart = new MimeMultipart();
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
                    ch.dbs.form.Message mes = new ch.dbs.form.Message(content, link);
                	rq.setAttribute("message", mes);
	                
				} catch (JRException e1) {
					//TODO: set correct error
					final ErrorMessage em = new ErrorMessage("error.createilvreport", "listkontobestellungen.do?method=overview");
	                rq.setAttribute("errormessage", em); 
				} catch (SMTPAddressFailedException e) {
					final ErrorMessage em = new ErrorMessage("errors.email", "listkontobestellungen.do?method=overview");
	                rq.setAttribute("errormessage", em); 
				} catch (AuthenticationFailedException e) {
					final ErrorMessage em = new ErrorMessage("error.mailserverconnection", "listkontobestellungen.do?method=overview");
	                rq.setAttribute("errormessage", em); 
				} catch (MessagingException e) {
					final ErrorMessage em = new ErrorMessage("error.sendmail", "listkontobestellungen.do?method=overview");
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
    
    private void runReport(IlvReportForm ilvf, UserInfo ui, OutputStream out) throws JRException {

        InputStream reportStream;
        Map<String, Object> values;

        switch (ilvf.getIlvformnr()) {
            case 0:  values = reportMainz(ilvf, ui);
                     reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form_0.jasper"));
                     break;
            case 1:  values = reportCharite(ilvf, ui);
                     reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form_1.jasper"));
                     break;
            default: values = reportMainz(ilvf, ui); // default case for illegal values
                     reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form_0.jasper"));
                     break;
        }
        
        final Collection<Map<String, ?>> al = new ArrayList<Map<String, ?>>();
        final HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("Fake", "Daten damit Report nicht leer wird..");
        al.add(hm);
        final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);
        
        JasperRunManager.runReportToPdfStream(reportStream, out, values, ds);     
    }

    private Map<String, Object> reportMainz(IlvReportForm ilvf, UserInfo ui) {

        Map<String, Object> result = new ConcurrentHashMap<String, Object>();
        
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
        result.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName()
                + ": " + ReadSystemConfigurations.getServerWelcomepage());

        return result;
    }

    private Map<String, Object> reportCharite(IlvReportForm ilvf, UserInfo ui) {

        Map<String, Object> result = new ConcurrentHashMap<String, Object>();
        
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
        result.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName()
                + ": " + ReadSystemConfigurations.getServerWelcomepage());

        return result;
    }
    
    /**
     * Get the ILV form number from the wildcard mapping. Defaults to 0, if
     * mapping is invalid.
     */
    private int getIlvNumber(String path) {
        
        int number = 0;

        if (path != null && path.contains("-")) {            
            try {
                number = Integer.valueOf(path.substring(path.lastIndexOf("-") + 1, path.length()));
            } catch (NumberFormatException e) {
                LOG.error(e.toString());
            }
        }

        return number;
    }

}
