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
import util.RemoveNullvaluesFrom;
import util.ThreadSafeSimpleDateFormat;
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
	
	private static final SimpleLogger log = new SimpleLogger(ILVReport.class);
	
	/**
	 * ILV-Report Formular Daten vorbereiten 
	 */
	public ActionForward prepareFormIlv(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		OrderForm pageForm = (OrderForm) fm;
        Auth auth = new Auth();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        if (auth.isLogin(rq)) {
            forward = "success";
            
            Text cn = new Text();
            
            try {

                rq.setAttribute("orderform", pageForm);
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("uebersicht");
                rq.setAttribute("ActiveMenus", mf);


            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("journalorderdetail: " + e.toString());
                
            } finally{
            	cn.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
	}
	
	/**
     * Erstelt ein PDF- Report (ILV-Bestellung)
     */
    public ActionForward ilv(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Auth auth = new Auth();
    	
    	// Ist der Benutzer als Bibliothekar angemeldet? Ist das Konto berechtigt Stats anzuzeigen?
    	if (auth.isLogin(rq)) {
			if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
				
				// Klassen vorbereiten
				IlvReportForm ilvf = (IlvReportForm) fm; 		
				UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
				ServletOutputStream servletOutputStream = null;
				RemoveNullvaluesFrom r = new RemoveNullvaluesFrom();
				Konto k = (Konto) r.simplyGettterSetterMethods(ui.getKonto());
				
				ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
			    tf.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
				Calendar cal = new GregorianCalendar();
	        	cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
	        	
				// Labels vorbereiten
				ConcurrentHashMap<String, String> values = new ConcurrentHashMap<String, String>();
				values.put("reporttitle", ilvf.getReporttitle() + " " + tf.format(cal.getTime(), ui.getKonto().getTimezone()));
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
				values.put("footer", "Brought to you by " + ReadSystemConfigurations.getApplicationName() + ": " + ReadSystemConfigurations.getServerWelcomepage());

				//Reportauswahl, Verbindung zum Report aufbauen
				InputStream reportStream = this.getServlet().getServletContext().getResourceAsStream("reports/ILV-Form.jasper");
				
				//Ausgabestream vorbereiten	
				rp.setContentType("application/pdf"); //Angabe, damit der Browser weiss wie den Stream behandeln
				try {
					servletOutputStream = rp.getOutputStream();					
					//Daten dem Report übergeben
					ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
				    HashMap<String,String> hm = new HashMap<String, String>();
				    hm.put("Fake", "Daten damit Report nicht leer wird..");
				    al.add(hm);
				    JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);

//					JRMapCollectionDataSource ds = new JRMapCollectionDataSource(new ArrayList()); // Leerer Report!
					JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, values, ds);
				} catch (Exception e) {
					// ServletOutputStream konnte nicht erstellt werden
					log.error("ILV-Report konnte nicht erstellt werden: " + e.toString());
				}
				finally {
					// Report an den Browser senden
						try {
							servletOutputStream.flush();
							servletOutputStream.close();
						} catch (IOException e) {
							log.error("orderspdf: " + e.toString());
						}			        
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
    
}
