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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
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

        // get ILV form number from wildcard mapping
        int ilvformnr = getIlvNumber(mp.getPath());
        System.out.println("Coming from ILV-Form: " + ilvformnr);

        // Ist der Benutzer als Bibliothekar angemeldet?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                // Klassen vorbereiten
                final IlvReportForm ilvf = (IlvReportForm) fm;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                OutputStream out = null;
                final Konto k = ui.getKonto();

                final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
                tf.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
                final Calendar cal = new GregorianCalendar();
                cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));

                // Labels vorbereiten
                final Map<String, Object> values = new ConcurrentHashMap<String, Object>();
                values.put("reporttitle", ilvf.getReporttitle() + " "
                        + tf.format(cal.getTime(), ui.getKonto().getTimezone()));
                values.put("labelfrom", ilvf.getLabelfrom());
                values.put("labelto", ilvf.getLabelto());
                values.put("labelsignatur", ilvf.getLabelsignatur());
                values.put("labeljournaltitel", ilvf.getLabeljournaltitel());
                values.put("labelcustomer", ilvf.getLabelcustomer());
                values.put("labelname", ilvf.getLabelname());
                values.put("labellibrarycard", ilvf.getLabellibrarycard());
                values.put("labelissn", ilvf.getLabelissn());
                values.put("labelpmid", ilvf.getLabelpmid());
                values.put("labelyear", ilvf.getLabelyear());
                values.put("labelvolumevintage", ilvf.getLabelvolumevintage());
                values.put("labelbooklet", ilvf.getLabelbooklet());
                values.put("labelclinicinstitutedepartment", ilvf.getLabelclinicinstitutedepartment());
                values.put("labelphone", ilvf.getLabelphone());
                values.put("labelfax", ilvf.getLabelfax());
                values.put("labelsendto", ilvf.getLabelsendto());
                values.put("labelpages", ilvf.getLabelpages());
                values.put("labelauthorofessay", ilvf.getLabelauthorofessay());
                values.put("labeltitleofessay", ilvf.getLabeltitleofessay());
                values.put("labelendorsementsofdeliveringlibrary", ilvf.getLabelendorsementsofdeliveringlibrary());
                values.put("labelnotesfromrequestinglibrary", ilvf.getLabelnotesfromrequestinglibrary());

                // Values abfüllen
                if (k.getIsil() != null) {
                    values.put("isil", k.getIsil());
                } else {
                    values.put("isil", "");
                }
                values.put("from", k.getBibliotheksname()); // cannot be null
                values.put("to", ilvf.getLieferant());
                values.put("signatur", ilvf.getSignatur());
                values.put("journaltitel", ilvf.getJournaltitel());
                values.put("name", ilvf.getName());
                values.put("issn", ilvf.getIssn());
                values.put("pmid", ilvf.getPmid());
                values.put("librarycard", ilvf.getLibrarycard());
                values.put("year", ilvf.getYear());
                values.put("volumevintage", ilvf.getVolumevintage());
                values.put("booklet", ilvf.getBooklet());
                values.put("phone", ilvf.getPhone());
                if (k.getTelefon() != null) {
                    values.put("phonekonto", k.getTelefon());
                } else {
                    values.put("phonekonto", "");
                }
                if (k.getFax_extern() != null) {
                    values.put("fax", k.getFax_extern());
                } else {
                    values.put("fax", "");
                }
                values.put("adresse", ilvf.getPost());
                values.put("clinicinstitutedepartment", ilvf.getClinicinstitutedepartment());
                values.put("pages", ilvf.getPages());
                values.put("authorofessay", ilvf.getAuthorofessay());
                values.put("titleofessay", ilvf.getTitleofessay());
                values.put("notesfromrequestinglibrary", ilvf.getNotesfromrequestinglibrary());
                values.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName()
                        + ": " + ReadSystemConfigurations.getServerWelcomepage());

                //Reportauswahl, Verbindung zum Report aufbauen
                // JasperReports need absolute paths!
                //                final BufferedInputStream reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form.jasper"));
                final InputStream reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form.jasper"));

                //Ausgabestream vorbereiten
                rp.setContentType("application/pdf"); //Angabe, damit der Browser weiss wie den Stream behandeln
                rp.setHeader("Content-Disposition","attachment;filename=ilv.pdf");
                try {
                    out = rp.getOutputStream();
                    //Daten dem Report übergeben
                    final Collection<Map<String, ?>> al = new ArrayList<Map<String, ?>>();
                    final HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("Fake", "Daten damit Report nicht leer wird..");
                    al.add(hm);
                    final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);

                    JasperRunManager.runReportToPdfStream(reportStream, out, values, ds);
                } catch (final Exception e) {
                    // ServletOutputStream konnte nicht erstellt werden
                    e.printStackTrace();
                    LOG.error("ILV-Report konnte nicht erstellt werden: " + e.toString());
                } finally {
                    // Report an den Browser senden
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
     * Prepare the Mail with attached ilv-pdf (ILV-Bestellung)
     */
    public ActionForward mail(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // get ILV form number from wildcard mapping
        int ilvformnr = getIlvNumber(mp.getPath());
        System.out.println("Coming from ILV-Form: " + ilvformnr);

        // Ist der Benutzer als Bibliothekar angemeldet?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

            	rq.setAttribute("IlvReportForm", fm);
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
     * Prepare the Mail with attached ilv-pdf (ILV-Bestellung)
     */
    public ActionForward sendIlvMail(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

            	rq.setAttribute("IlvReportForm", fm);
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
