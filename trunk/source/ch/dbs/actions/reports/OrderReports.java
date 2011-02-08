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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import util.Check;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.actions.user.UserAction;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OverviewForm;
import ch.dbs.form.SearchesForm;
import ch.dbs.form.UserInfo;

/**
 * Erstellt PDF-Reports
 *
 * @author Pascal Steiner
 *
 */
public final class OrderReports extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(OrderReports.class);

    /**
     * Erstelt ein PDF- Report wie die aktuelle Sicht der Bestellungen
     * inklusive Filterkriterien (Status) und Sortierfolge (Feld, Auf- oder Absteigend
     */
    public ActionForward orderspdf(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                // Klassen vorbereiten
                OverviewForm of = (OverviewForm) fm; //Parameter für Einschraenkungen
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                ServletOutputStream out = null;
                final Text cn = new Text();

                // wird für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
                of.setStatitexts(cn.getAllTextPlusKontoTexts(new Texttyp("Status", cn.getConnection()),
                        ui.getKonto().getId(), cn.getConnection()));

                //Eingaben Testen und Notfalls korrigieren mit defaultwerten
                final Check c = new Check();
                of = c.checkDateRegion(of, 4, ui.getKonto().getTimezone());
                of = c.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of);
                of = c.checkOrdersSortCriterias(of);
                of = c.checkSortOrderValues(of);

                List<SearchesForm> searches = new ArrayList<SearchesForm>();

                if (of.isS()) { // Suchkriterien aus Session holen, falls Anzeige von einer Suche stammt
                    searches = ui.getSearches();
                }

                // Daten holen
                final Bestellungen b = new Bestellungen();
                try {
                    //          Reportdaten vorbereiten
                    List<Bestellungen> orders = null;

                    if (!searches.isEmpty()) { // hier liegt Liste aus Suche vor...
                        final UserAction uaInstance = new UserAction();
                        PreparedStatement pstmt = null;
                        pstmt = uaInstance.composeSearchLogic(searches, ui.getKonto(),
                                of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), cn.getConnection());
                        orders = b.searchOrdersPerKonto(pstmt, cn.getConnection());
                        if (pstmt != null) {
                            try {
                                pstmt.close();
                            } catch (final SQLException e) {
                                LOG.error("orderspdf: " + e.toString());
                            }
                        }
                    } else {
                        // normale Liste...
                        if (of.getFilter() == null) {
                            orders = b.getOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(),
                                    of.getFromdate(), of.getTodate(), cn.getConnection());
                        } else {
                            orders = b.getOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(), of.getSort(),
                                    of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection());
                        }

                    }

                    final Collection<ConcurrentHashMap<String, String>> al =
                        new ArrayList<ConcurrentHashMap<String, String>>();

                    final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy HH:mm");
                    tf.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));
                    for (final Bestellungen order : orders) {
                        final ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<String, String>();
                        hm.put("orderdate", order.getOrderdate());
                        // if (order.getBestellquelle() != null)if (order.getBestellquelle().equals("0"))
                        // order.setBestellquelle("k. A.");
                        hm.put("bestellquelle", order.getBestellquelle());
                        hm.put("typ", order.getMediatype());
                        hm.put("state", order.getStatustext());
                        hm.put("format", order.getDeloptions());
                        hm.put("kunde", order.getBenutzer().getVorname() + " " + order.getBenutzer().getName());
                        hm.put("artikeltitel", order.getArtikeltitel() + order.getKapitel());
                        final StringBuffer bf = new StringBuffer();
                        bf.append(order.getZeitschrift());
                        bf.append(order.getBuchtitel());
                        if ((order.getIssn() != null && !order.getIssn().equals(""))
                                || (order.getIsbn() != null && !order.getIsbn().equals(""))) {
                            bf.append(". - ");
                            bf.append(order.getIssn());
                            bf.append(order.getIsbn());
                        }
                        bf.append(" | ");
                        if (order.getJahr() != null && !order.getJahr().equals("")) { bf.append(order.getJahr()); }
                        if (order.getJahrgang() != null && !order.getJahrgang().equals("")) {
                            bf.append(';');
                            bf.append(order.getJahrgang());
                        }
                        if (order.getHeft() != null && !order.getHeft().equals("")) {
                            bf.append('(');
                            bf.append(order.getHeft());
                            bf.append(')');
                        }
                        if (order.getSeiten() != null && !order.getSeiten().equals("")) {
                            bf.append(':');
                            bf.append(order.getSeiten());
                        }
                        hm.put("zeitschrift", bf.toString());

                        // Hier werden die Notizen so aufbereitet, dass Bestellnummern immer am Anfang stehen
                        // und keine unnötigen Zeilenumbrüche enthalten.
                        final StringBuffer numbers = new StringBuffer();
                        if (order.getSubitonr() != null && !order.getSubitonr().equals("")) {
                            numbers.append(order.getSubitonr());
                        }
                        if (order.getGbvnr() != null && !order.getGbvnr().equals("")) {
                            if (numbers.length() > 0) {
                                // bereits Bestellnummer vorhanden
                                numbers.append("\nGBV-Nr.: ");
                                numbers.append(order.getGbvnr());
                            } else {
                                // keine Bestellnummer vorhanden
                                numbers.append("GBV-Nr.: ");
                                numbers.append(order.getGbvnr());
                            }
                        }
                        if (order.getInterne_bestellnr() != null && !order.getInterne_bestellnr().equals("")) {
                            if (numbers.length() > 0) {
                                // bereits Bestellnummer vorhanden
                                numbers.append("\nInterne Nr.: ");
                                numbers.append(order.getInterne_bestellnr());
                            } else {
                                // keine Bestellnummer vorhanden
                                numbers.append("Interne Nr.: ");
                                numbers.append(order.getInterne_bestellnr());
                            }
                        }
                        if (numbers.length() > 0) { order.setNotizen(numbers.toString() + "\n" + order.getNotizen()); }

                        hm.put("notes", order.getNotizen());
                        al.add(hm);
                    }

                    //Parameter abfüllen
                    final ConcurrentHashMap<String, String> param = new ConcurrentHashMap<String, String>();
                    final Date from = new SimpleDateFormat("yyyy-MM-dd").parse(of.getFromdate());
                    final Date to = new SimpleDateFormat("yyyy-MM-dd").parse(of.getTodate());
                    param.put("from", new SimpleDateFormat("dd.MM.yyyy").format(from));
                    param.put("to", new SimpleDateFormat("dd.MM.yyyy").format(to));
                    final Calendar cal = new GregorianCalendar();
                    cal.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));
                    param.put("today", tf.format(cal.getTime(), ui.getKonto().getTimezone()));

                    //Reportauswahl, Verbindung zum Report aufbauen
                    if (of.getReport() == null) { of.setReport("reports/Orders.jasper"); }
                    final BufferedInputStream reportStream = new BufferedInputStream(this.getServlet().getServletContext()
                            .getResourceAsStream(of.getReport()));

                    //Ausgabestream vorbereiten
                    rp.setContentType("application/pdf"); //Angabe, damit der Browser weiss wie den Stream behandeln
                    out = rp.getOutputStream();

                    //Daten zusammenstellen und abfüllen
                    final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);
                    JasperRunManager.runReportToPdfStream(reportStream, out,
                            param, ds);

                } catch (final Exception e) {
                    // ServletOutputStream konnte nicht erstellt werden
                    LOG.error("orderspdf: " + e.toString());
                } finally {
                    // Report an den Browser senden
                    try {
                        out.flush();
                        out.close();
                    } catch (final IOException e) {
                        LOG.error("orderspdf: " + e.toString());
                    }
                    cn.close();
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
     * Bestellungen nach Lieferant gruppiert
     */
    public ActionForward orderSourcePdf(final ActionMapping mp, ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final OverviewForm of = (OverviewForm) fm;
        of.setReport("reports/AllOrdersOrdersource.jasper");
        of.setSort("bestellquelle");
        fm = of;
        orderspdf(mp, fm, rq, rp);


        return mp.findForward(null);
    }
}
