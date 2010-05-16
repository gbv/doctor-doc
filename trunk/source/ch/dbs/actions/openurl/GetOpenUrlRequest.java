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

package ch.dbs.actions.openurl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.grlea.log.SimpleLogger;

import util.Auth;
import ch.dbs.actions.bestellung.BestellformAction;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

/**
 * Methode um OpenUrl-Requests zu empfangen
 * 
 * @author Markus Fischer
 */
public final class GetOpenUrlRequest extends Action {
	
	private static final SimpleLogger log = new SimpleLogger(GetOpenUrlRequest.class);

	/**
	 * empfängt OpenURL-Requests und stellt ein OrderForm her
	 */
	public ActionForward execute(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

		Text requester = new Text();
		Auth auth = new Auth();
		String forward = "failure";
		OrderForm of = (OrderForm) form;
		BestellformAction bestellFormActionInstance = new BestellformAction();
		of.setResolver(true); // markiert, dass Angaben bereits aufgelöst wurden
		ContextObject co = new ContextObject();
		ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
		OpenUrl openUrlInstance = new OpenUrl();
		
		String query = rq.getQueryString();
		
		try {
			
			// es gibt drei mögliche Zugriffsformen, ohne eingeloggt zu sein. Priorisiert wie folgt:
			// 1. Kontokennung
	        // 2. IP-basiert
			// 3. Broker-Kennung (z.B. Careum Explorer)
			
			requester = auth.grantAccess(rq);
			
			// Nicht eingeloggt. IP-basiert, Kontokennung oder Brokerkennung
	        if (requester!=null && requester.getInhalt()!=null) {		
		
		forward = "success";

			rq.setAttribute("ip", requester); // Text mit Konto in Request setzen
			of.setBibliothek(requester.getKonto().getBibliotheksname());		

			if (query !=null && (query.contains("sid=Entrez:PubMed&id=pmid:") || // Übergabe direkt von Pubmed...
								   query.contains("sid=google&id=pmid:")) ) { // ...oder von Google Scholar
				String pmid = "";
				if (query.contains("sid=Entrez:PubMed&id=pmid:")) {
					pmid = query.substring(query.indexOf("sid=Entrez:PubMed&id=pmid:"));
					of = bestellFormActionInstance.resolvePmid(bestellFormActionInstance.extractPmid(pmid));
					of.setRfr_id("Entrez:PubMed");
				}
				if (query.contains("sid=google&id=pmid:")) {
					pmid = query.substring(query.indexOf("sid=google&id=pmid:"));
					of = bestellFormActionInstance.resolvePmid(bestellFormActionInstance.extractPmid(pmid));
					of.setRfr_id("Google Scholar");
				}
				
			} else { // Übergabe aus Linkresolver
				if (query != null) {
					co = openUrlInstance.readOpenUrlFromRequest(rq);
					of = convertOpenUrlInstance.makeOrderform(co);
					// nur bei Übergabe über OpenURL Rfr_id abfüllen. Nicht bei WorldCat! Deshalb hier nachträglich...
					if (co.getRfr_id()!=null && !co.getRfr_id().equals("")) of.setRfr_id(co.getRfr_id());
					
					// Falls Doi vorhanden aber wichtige Angaben fehlen...
					if (of.getDoi()!=null && !of.getDoi().equals("") && 
						of.getMediatype().equals("Artikel") && bestellFormActionInstance.areArticleValuesMissing(of)) {
						
						OrderForm of_doi = new OrderForm();
						of_doi = bestellFormActionInstance.resolveDoi(bestellFormActionInstance.extractDoi(of.getDoi()));
						if (of.getIssn().equals("") && !of_doi.getIssn().equals("")) of.setIssn(of_doi.getIssn());
						if (of.getZeitschriftentitel().equals("") && !of_doi.getZeitschriftentitel().equals("")) of.setZeitschriftentitel(of_doi.getZeitschriftentitel());
						if (of.getJahr().equals("") && !of_doi.getJahr().equals("")) of.setJahr(of_doi.getJahr());
						if (of.getJahrgang().equals("") && !of_doi.getJahrgang().equals("")) of.setJahrgang(of_doi.getJahrgang());
						if (of.getHeft().equals("") && !of_doi.getHeft().equals("")) of.setHeft(of_doi.getHeft());
						if (of.getSeiten().equals("") && !of_doi.getSeiten().equals("")) of.setSeiten(of_doi.getSeiten());
						if (of.getAuthor().equals("") && !of_doi.getAuthor().equals("")) of.setAuthor(of_doi.getAuthor());
						
					}
					
				}
			}
			
			 if (of.getDeloptions() == null || // Defaultwert deloptions
	                    (!of.getDeloptions().equals("post") && !of.getDeloptions().equals("fax to pdf") && !of.getDeloptions().equals("urgent")) ) {
	                 		of.setDeloptions("fax to pdf");
	                 	}
			
            // Cookie auslesen
            Cookie cookies[] = rq.getCookies();
            
            if (cookies == null) {
            	log.ludicrous("Kein Cookie gesetzt!");
            } else {

				for (int i = 0; i < cookies.length; i++) {

					if (cookies[i].getName()!=null && cookies[i].getName().equals("doctordoc-bestellform")) {
						String cookietext = cookies[i].getValue();
						 if (cookietext!=null && cookietext.contains("---")) {
							 try {
							 of.setKundenvorname(cookietext.substring(0, cookietext.indexOf("---")));
							 cookietext = cookietext.substring(cookietext.indexOf("---")+3);
							 of.setKundenname(cookietext.substring(0, cookietext.indexOf("---")));
							 cookietext = cookietext.substring(cookietext.indexOf("---")+3);
							 of.setKundenmail(cookietext);
							 } catch (Exception e) { // 
								 log.error("Fehler beim Cookie auslesen!: " + e.toString());
								 System.out.println("Fehler beim Cookie auslesen!: " + e.toString());
							 }
						 }
						
					}
				}
			}
            
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("bestellform");
            rq.setAttribute("ActiveMenus", mf);
            
	        } else {        	
	        	
	            // Falls User eingeloggt
	            if (auth.isLogin(rq)) {
	            	
	        		forward = "success";
	        		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
	        		

	    			if (query != null && (query.contains("sid=Entrez:PubMed&id=pmid:") || // Übergabe direkt von Pubmed...
					   query.contains("sid=google&id=pmid:")) ) { // ...oder von Google Scholar
	    				String pmid = "";
	    				if (query.contains("sid=Entrez:PubMed&id=pmid:")) {
	    					pmid = query.substring(query.indexOf("sid=Entrez:PubMed&id=pmid:"));
	    					of = bestellFormActionInstance.resolvePmid(bestellFormActionInstance.extractPmid(pmid));
	    					of.setRfr_id("Entrez:PubMed");
	    				}
	    				if (query.contains("sid=google&id=pmid:")) {
	    					pmid = query.substring(query.indexOf("sid=google&id=pmid:"));
	    					of = bestellFormActionInstance.resolvePmid(bestellFormActionInstance.extractPmid(pmid));
	    					of.setRfr_id("Google Scholar");
	    				}
	    				
	    			} else { // Übergabe aus Linkresolver
	    				if (query != null) {
	    					co = openUrlInstance.readOpenUrlFromRequest(rq);
	    					of = convertOpenUrlInstance.makeOrderform(co);
	    					// nur bei Übergabe über OpenURL Rfr_id abfüllen. Nicht bei WorldCat! Deshalb hier nachträglich...
	    					if (co.getRfr_id()!=null && !co.getRfr_id().equals("")) of.setRfr_id(co.getRfr_id());
	    					
	    					// Falls Doi vorhanden aber wichtige Angaben fehlen...
	    					if (of.getDoi()!=null && !of.getDoi().equals("") && 
	    						of.getMediatype().equals("Artikel") && bestellFormActionInstance.areArticleValuesMissing(of)) {
	    						
	    						OrderForm of_doi = new OrderForm();
	    						of_doi = bestellFormActionInstance.resolveDoi(bestellFormActionInstance.extractDoi(of.getDoi()));
	    						if (of.getIssn().equals("") && !of_doi.getIssn().equals("")) of.setIssn(of_doi.getIssn());
	    						if (of.getZeitschriftentitel().equals("") && !of_doi.getZeitschriftentitel().equals("")) of.setZeitschriftentitel(of_doi.getZeitschriftentitel());
	    						if (of.getJahr().equals("") && !of_doi.getJahr().equals("")) of.setJahr(of_doi.getJahr());
	    						if (of.getJahrgang().equals("") && !of_doi.getJahrgang().equals("")) of.setJahrgang(of_doi.getJahrgang());
	    						if (of.getHeft().equals("") && !of_doi.getHeft().equals("")) of.setHeft(of_doi.getHeft());
	    						if (of.getSeiten().equals("") && !of_doi.getSeiten().equals("")) of.setSeiten(of_doi.getSeiten());
	    						if (of.getAuthor().equals("") && !of_doi.getAuthor().equals("")) of.setAuthor(of_doi.getAuthor());
	    						
	    					}
	    					
	    				}
	    			}
	    			
	    			 if (of.getDeloptions() == null || // Defaultwert deloptions
	    	                    (!of.getDeloptions().equals("post") && !of.getDeloptions().equals("fax to pdf") && !of.getDeloptions().equals("urgent")) ) {
	    	                 		of.setDeloptions("fax to pdf");
	    	                 	}
	    			 
	                 of.setKundenvorname(ui.getBenutzer().getVorname());
	                 of.setKundenname(ui.getBenutzer().getName());
	                 of.setKundenmail(ui.getBenutzer().getEmail());
	    			 
	                 ActiveMenusForm mf = new ActiveMenusForm();
	                 mf.setActivemenu("suchenbestellen");
	                 rq.setAttribute("ActiveMenus", mf);
	    			 
	            	
	            } else {
	                ErrorMessage em = new ErrorMessage("error.ip", "login.do");
	                rq.setAttribute("errormessage", em);
	                ActiveMenusForm mf = new ActiveMenusForm();
	                mf.setActivemenu("bestellform");
	                rq.setAttribute("ActiveMenus", mf);
	            }
	        }

		
		} catch(Exception e){
			log.error("execute: " + query + "\040" + e.toString());
    	} finally {
//    		requester.close(); // es wird nie eine Verbindung geöffnet
    	}

		rq.setAttribute("ofjo", of);
		
        return mp.findForward(forward);
    }
	

}
