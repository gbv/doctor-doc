//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package ch.dbs.actions.reports;

import java.io.IOException;
import java.io.InputStream;
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
	
	private static final SimpleLogger log = new SimpleLogger(OrderReports.class);
	
	/**
     * Erstelt ein PDF- Report wie die aktuelle Sicht der Bestellungen
     * inklusive Filterkriterien (Status) und Sortierfolge (Feld, Auf- oder Absteigend
     */
    public ActionForward orderspdf(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Auth auth = new Auth();
    	
    	// Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
    	if (auth.isLogin(rq)) {
			if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
				
				// Klassen vorbereiten
				OverviewForm of = (OverviewForm) fm; //Parameter für Einschraenkungen				
				UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
				ServletOutputStream servletOutputStream = null;
				Text cn = new Text();
				
				// wird für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
	            of.setStatitexts(cn.getAllTextPlusKontoTexts(new Texttyp("Status", cn.getConnection()), ui.getKonto().getId(), cn.getConnection()));
				
				//Eingaben Testen und Notfalls korrigieren mit defaultwerten
				Check c = new Check();
				of = c.checkDateRegion(of, 4, ui.getKonto().getTimezone());
				of = c.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of);
				of = c.checkOrdersSortCriterias(of);
				of = c.checkSortOrderValues(of);
				
				ArrayList<SearchesForm> searches = new ArrayList<SearchesForm>();
				
				if (of.isS()) { // Suchkriterien aus Session holen, falls Anzeige von einer Suche stammt
	            	searches = ui.getSearches();
	            }
				
				// Daten holen
				Bestellungen b = new Bestellungen();
				try {				
//					Reportdaten vorbereiten
					List<Bestellungen> orders = null;
					
					if (searches.size() != 0) { // hier liegt Liste aus Suche vor...
						UserAction userActionInstance = new UserAction();
						PreparedStatement pstmt = null;
						pstmt = userActionInstance.composeSearchLogic(searches, ui.getKonto(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), cn.getConnection());
						orders = b.searchOrdersPerKonto(pstmt);
				        	if (pstmt != null) {
				        		try {
				        			pstmt.close();
				        		} catch (SQLException e) {
				        			log.error("orderspdf: " + e.toString());
				        		}
				        	}
					} else {
						// normale Liste...
						if (of.getFilter()==null) {
							orders = b.getOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), cn.getConnection());
						} else {
							orders = b.getOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection());
						}
						
					}			
					
				    Collection<ConcurrentHashMap<String, String>> al = new ArrayList<ConcurrentHashMap<String, String>>();
				    ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy HH:mm");
				    tf.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));
				    for (Bestellungen order : orders) {
						ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<String, String>();
					    hm.put("orderdate", order.getOrderdate());
//					    if (order.getBestellquelle()!=null)if (order.getBestellquelle().equals("0")) order.setBestellquelle("k. A.");
					    hm.put("bestellquelle", order.getBestellquelle());
					    hm.put("typ", order.getMediatype());
					    hm.put("state", order.getStatustext());
					    hm.put("format", order.getDeloptions());
     			        hm.put("kunde", order.getBenutzer().getVorname() + " " + order.getBenutzer().getName());
					    hm.put("artikeltitel", order.getArtikeltitel() + order.getKapitel());
					    String z = order.getZeitschrift() + order.getBuchtitel();
					    if ((order.getIssn() != null && !order.getIssn().equals("")) || (order.getIsbn() != null && !order.getIsbn().equals(""))) z += ". - " + order.getIssn() + order.getIsbn();
					    z += " | ";
					    if (order.getJahr() != null && !order.getJahr().equals("")) z += order.getJahr();
					    if (order.getJahrgang() != null && !order.getJahrgang().equals("")) z += ";" + order.getJahrgang();
					    if (order.getHeft() != null && !order.getHeft().equals("")) z += "(" + order.getHeft() + ")";
					    if (order.getSeiten() != null && !order.getSeiten().equals("")) z += ":" + order.getSeiten();
					    hm.put("zeitschrift", z);
					    
					    // Hier werden die Notizen so aufbereitet, dass Bestellnummern immer am Anfang stehen
					    // und keine unnötigen Zeilenumbrüche enthalten.
					    String numbers = "";
					    if (order.getSubitonr()!= null){
					    	if (!order.getSubitonr().equals("")) numbers = order.getSubitonr();
					    }
					    if (order.getGbvnr()!= null){
					    	if (!order.getGbvnr().equals("")) {
					    		if (!numbers.equals("")) {
					    			numbers = numbers + "\nGBV-Nr.: " + order.getGbvnr(); // bereits Bestellnummer vorhanden
					    		} else {numbers = "GBV-Nr.: " + order.getGbvnr();} // keine Bestellnummer vorhanden
					    	}
					    }
					    if (order.getInterne_bestellnr()!= null){
					    	if (!order.getInterne_bestellnr().equals("")) {
					    		if (!numbers.equals("")) {
					    			numbers = numbers + "\nInterne Nr.: " + order.getInterne_bestellnr(); // bereits Bestellnummer vorhanden
					    		} else {numbers = "Interne Nr.: " + order.getInterne_bestellnr();} // keine Bestellnummer vorhanden
					    	}
					    }					    
					    if (!numbers.equals("")) order.setNotizen(numbers + "\n" + order.getNotizen());					    
					    
					    hm.put("notes", order.getNotizen());
					    al.add(hm);
					}
				    
					//Parameter abfüllen
					ConcurrentHashMap<String,String> param = new ConcurrentHashMap<String, String>();
					Date from = new SimpleDateFormat("yyyy-MM-dd").parse(of.getFromdate());
					Date to = new SimpleDateFormat("yyyy-MM-dd").parse(of.getTodate());
					param.put("from", new SimpleDateFormat("dd.MM.yyyy").format(from));
					param.put("to", new SimpleDateFormat("dd.MM.yyyy").format(to));
					Calendar cal = new GregorianCalendar();
		        	cal.setTimeZone(TimeZone.getTimeZone(ui.getKonto().getTimezone()));		        	
					param.put("today", tf.format(cal.getTime(), ui.getKonto().getTimezone()));

					//Reportauswahl, Verbindung zum Report aufbauen
					if (of.getReport()==null) of.setReport("reports/Orders.jasper");
					InputStream reportStream = this.getServlet().getServletContext().getResourceAsStream(of.getReport());

					//Ausgabestream vorbereiten	
					rp.setContentType("application/pdf"); //Angabe, damit der Browser weiss wie den Stream behandeln
					servletOutputStream = rp.getOutputStream();
					
					//Daten zusammenstellen und abfüllen
					JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);
					JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream,
			                  param, ds);
					
				} catch (Exception e) {
					// ServletOutputStream konnte nicht erstellt werden
					log.error("orderspdf: " + e.toString());
				} 
				
				finally {
				// Report an den Browser senden
					try {
						servletOutputStream.flush();
						servletOutputStream.close();
					} catch (IOException e) {
						log.error("orderspdf: " + e.toString());
					}			        
					cn.close();
					forward=null;
				}

			} else {
				ErrorMessage em = new ErrorMessage(
						"error.berechtigung",
						"login.do");
				rq.setAttribute("errormessage", em);
			}
		} else {
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage(
					"error.timeout", "login.do");
			rq.setAttribute("errormessage", em);
		}

		return mp.findForward(forward);
	}
    
	/**
     * Bestellungen nach Lieferant gruppiert
     */
    public ActionForward orderSourcePdf(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = null;
    	OverviewForm of = (OverviewForm) fm;
    	of.setReport("reports/AllOrdersOrdersource.jasper");
    	of.setSort("bestellquelle");
    	fm = of;
    	orderspdf(mp, fm, rq, rp);	
    	

		return mp.findForward(forward);
	}
}
