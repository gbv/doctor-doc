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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletOutputStream;
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
import util.RemoveNullValuesFromObject;
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
    public ActionForward ilv(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                // Klassen vorbereiten
                final IlvReportForm ilvf = (IlvReportForm) fm;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                ServletOutputStream out = null;
                final RemoveNullValuesFromObject nullValues = new RemoveNullValuesFromObject();
                final Konto k = (Konto) nullValues.remove(ui.getKonto());

                final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
                tf.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
                final Calendar cal = new GregorianCalendar();
                cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));

                // Labels vorbereiten
                final ConcurrentHashMap<String, String> values = new ConcurrentHashMap<String, String>();
                values.put("reporttitle", ilvf.getReporttitle() + " "
                        + tf.format(cal.getTime(), ui.getKonto().getTimezone()));
                values.put("labelfrom", ilvf.getLabelfrom());
                values.put("labelto", ilvf.getLabelto());
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
                values.put("isil", k.getIsil());
                values.put("from", k.getBibliotheksname());
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
                values.put("phonekonto", k.getTelefon());
                values.put("fax", k.getFax_extern());
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
                final BufferedInputStream reportStream = new BufferedInputStream(this.getServlet().getServletContext().getResourceAsStream("/reports/ILV-Form.jasper"));

                //Ausgabestream vorbereiten
                rp.setContentType("application/pdf"); //Angabe, damit der Browser weiss wie den Stream behandeln
                try {
                    out = rp.getOutputStream();
                    //Daten dem Report übergeben
                    final ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
                    final HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("Fake", "Daten damit Report nicht leer wird..");
                    al.add(hm);
                    final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);

                    JasperRunManager.runReportToPdfStream(reportStream, out, values, ds);
                } catch (final Exception e) {
                    // ServletOutputStream konnte nicht erstellt werden
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

}
