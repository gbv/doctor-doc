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

package ch.dbs.actions.bestellung;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.CodeString;
import util.CodeUrl;
import util.Http;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.BestellParam;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

/**
 * BestellformAction prüft ip-basierte Zugriffe und erlaubt Kundenbestellungen innerhalb einer Institution
 * z.Hd. der betreffenden Bibliothek 
 * 
 * @author Markus Fischer
 */
public final class BestellformAction extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(BestellformAction.class);
    
	/**
	 * Prüft IP und ordnet den Request der betreffenden Bibliothek zu, ergänzt Angaben anhand PMID und DOI
	 */
	public ActionForward validate(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		Text t = new Text();
		Text cn = new Text();
		Auth auth = new Auth();
		String forward = "failure";
		OrderForm of = (OrderForm) fm;
		BestellParam bp = new BestellParam();
		Countries countriesInstance = new Countries();
		
        if (rq.getAttribute("ofjo")!=null) {
        	of = (OrderForm) rq.getAttribute("ofjo"); // Übergabe aus checkAvailability von getOpenUrlRequest und nach Kunde neu erstellen...
        }
        
     // es gibt drei mögliche Zugriffsformen, ohne eingeloggt zu sein. Priorisiert wie folgt:
		// 1. Kontokennung (überschreibt IP-basiert)
        // 2. IP-basiert (überschreibt Broker-Kennung)
		// 3. Broker-Kennung (z.B. Careum Explorer)
        
        if (of.getKkid()==null) t = auth.grantAccess(rq);
		
		// Nicht eingeloggt. IP-basiert, Kontokennung oder Brokerkennung
        if (((t!=null && t.getInhalt()!=null) || (of.getKkid()!=null || of.getBkid()!=null)) && !auth.isLogin(rq)) {
            forward = "success";
            
            String kkid = of.getKkid(); // wird benötigt, damit kkid bei resolvePmid nicht überschrieben wird...
            String bkid = of.getBkid();
          
            if (of.getMediatype() == null || // Defaultformular Artikel
               (!of.getMediatype().equals("Artikel") && !of.getMediatype().equals("Teilkopie Buch") && !of.getMediatype().equals("Buch")) ) {
            		of.setMediatype("Artikel");
            	}
            
            // hier erfolgt die Auflösung von PMID oder DOI
            if (of.isResolve()==true && of.getPmid()!=null && !of.getPmid().equals("") && of.getMediatype().equals("Artikel")) {
            	of = resolvePmid(extractPmid(of.getPmid()));
            } else {
            	if (of.isResolve()==true && of.getDoi()!=null && !of.getDoi().equals("") && of.getMediatype().equals("Artikel")) {
            		of = resolveDoi(extractDoi(of.getDoi()));
            		if (of.getDoi()==null || of.getDoi().equals("")) of = (OrderForm) fm; // falls Auflösung nicht geklappt hat...
            	}            	
            }
            
            // muss nach resolvePmid stehen, das sonst der Bibliotheksname überschrieben wird...
            if (t!=null && t.getInhalt()!=null) {
            	rq.setAttribute("ip", t); // Text mit Konto in Request setzen
    			of.setBibliothek(t.getKonto().getBibliotheksname());
    			if (t.getTexttyp().getId()==11) of.setBkid(t.getInhalt());
    			if (t.getTexttyp().getId()==12) of.setKkid(t.getInhalt());
            } else {            
            if (kkid!=null) { // Kontokennung
    			t = new Text(cn.getConnection(), Long.valueOf(12), kkid); // Text mit Kontokennung
    			if (t!=null && t.getInhalt()!=null) { // ungültige kkid abfangen!
    				rq.setAttribute("ip", t); // Text mit Konto in Request setzen
        			of.setBibliothek(t.getKonto().getBibliotheksname());
        			of.setKkid(kkid);        			
        		} else { // ungültige kkid
        			forward = "failure";
        			ErrorMessage em = new ErrorMessage("error.kkid", "login.do");
                    rq.setAttribute("errormessage", em);
                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("bestellform");
                    rq.setAttribute("ActiveMenus", mf);
        		}
    		}
    		if (bkid!=null) { // Brokerkennung
    			t = new Text(cn.getConnection(), Long.valueOf(11), bkid); // Text mit Brokerkennung
    			if (t!=null && t.getInhalt()!=null) { // ungültige bkid abfangen!
    			if (t.getKonto().getId()!=null) { // Brokerkennung ist EINEM Konto zugewiesen
    				rq.setAttribute("ip", t); // Text mit Konto in Request setzen
    				of.setBibliothek(t.getKonto().getBibliotheksname());
    				of.setBkid(bkid);
    			} else { // Borkerkennung ist offen
    				// TODO: Prüfungen wer was liefern kann und Anzeige
    			}
    			} else { // ungültige bkid
    				forward = "failure"; 
    				ErrorMessage em = new ErrorMessage("error.bkid", "login.do");
                    rq.setAttribute("errormessage", em);
                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("bestellform");
                    rq.setAttribute("ActiveMenus", mf);
    			}
    		}
            }
            
            // zugehörige Bestellformular-Parameter holen
            // Änderungen in diesem Abschnitt müssen in save() wiederholt werden
            if (t!=null && t.getInhalt()!=null) {            	
            	bp = new BestellParam(t, cn.getConnection());
            	// Länderauswahl setzen
            	List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());            
                of.setCountries(allPossCountries);
                if (of.getRadiobutton().equals("")) of.setRadiobutton(bp.getOption_value1()); // Default Option1
            }
            
            if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // Defaultwert deloptions
                 		if (bp.isLieferart()==false) {of.setDeloptions("email");} else {of.setDeloptions(bp.getLieferart_value1());}
                 	}
            
            // Cookie auslesen
            Cookie cookies[] = rq.getCookies();
            
            if (cookies == null) {
            	log.ludicrous("kein Cookie gesetzt!");
            } else {
				CodeString codeString = new CodeString();

				for (int i = 0; i < cookies.length; i++) {

					if (cookies[i].getName()!=null && cookies[i].getName().equals("doctordoc-bestellform")) {
						String cookietext = codeString.decodeString(cookies[i].getValue());
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
            
            // für Get-Methode in PrepareLogin of URL-codieren
            of = of.encodeOrderForm(of);
            
            rq.setAttribute("bestellparam", bp);
            rq.setAttribute("orderform", of);
        } else {        	
        	
            // Falls User eingeloggt
            if (auth.isLogin(rq)) {
            	
                forward = "success";
                UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
              
                if (of.getMediatype() == null || // Defaultformular Artikel
                   (!of.getMediatype().equals("Artikel") && !of.getMediatype().equals("Teilkopie Buch") && !of.getMediatype().equals("Buch")) ) {
                		of.setMediatype("Artikel");
                	}
                
//              hier erfolgt die Auflösung von PMID oder DOI
                if (of.isResolver()==false && of.getPmid()!=null && !of.getPmid().equals("") && of.getMediatype().equals("Artikel")) {
                	of = resolvePmid(extractPmid(of.getPmid()));
                } else {
                	if (of.isResolver()==false && of.getDoi()!=null && !of.getDoi().equals("") && of.getMediatype().equals("Artikel")) {
                		of = resolveDoi(extractDoi(of.getDoi()));
                		if (of.getDoi()==null || of.getDoi().equals("")) of = (OrderForm) fm; // falls Auflösung nicht geklappt hat...
                	}            	
                }
                
                bp = new BestellParam(ui.getKonto(), cn.getConnection()); // das für eingeloggt wird nicht über eine Textverknüpfung abgehandelt
                
             // Länderauswahl setzen
             // zugehörige Bestellformular-Parameter holen
                if (bp!=null && bp.getId()!=null) {
                	// Länderauswahl setzen
                	List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());           
                    of.setCountries(allPossCountries);
                    if (of.getRadiobutton().equals("")) of.setRadiobutton(bp.getOption_value1()); // Default Option1
                 // Angaben für parametriertes Bestellformular
                    of.setKundeninstitution(ui.getBenutzer().getInstitut());
                    of.setKundenabteilung(ui.getBenutzer().getAbteilung());
                    of.setKundenadresse(ui.getBenutzer().getAdresse() + "\012" + ui.getBenutzer().getAdresszusatz() + "\012" + ui.getBenutzer().getPlz() + "\040" + ui.getBenutzer().getOrt());
                    of.setKundenstrasse(ui.getBenutzer().getAdresse() + "\040" + ui.getBenutzer().getAdresszusatz());
                    of.setKundenplz(ui.getBenutzer().getPlz());
                    of.setKundenort(ui.getBenutzer().getOrt());
                    of.setKundenland(ui.getBenutzer().getLand());
                    if (ui.getBenutzer().getTelefonnrg()!=null && !ui.getBenutzer().getTelefonnrg().equals("")) {
                    	of.setKundentelefon(ui.getBenutzer().getTelefonnrg());
                    } else {
                    if (ui.getBenutzer().getTelefonnrp()!=null && !ui.getBenutzer().getTelefonnrp().equals("")) {
                        of.setKundentelefon(ui.getBenutzer().getTelefonnrp());
                        }                	
                    }
                }
                
                if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // Defaultwert alle anderen Situationen deloptions
                	if (bp.isLieferart()==false) {of.setDeloptions("email");} else {of.setDeloptions(bp.getLieferart_value1());}
             	}
                
//                of.setBibliothek(t.getKonto().getBibliotheksname());
                of.setKundenvorname(ui.getBenutzer().getVorname());
                of.setKundenname(ui.getBenutzer().getName());
                of.setKundenmail(ui.getBenutzer().getEmail());
                
             // für Get-Methode in PrepareLogin of URL-codieren
                of = of.encodeOrderForm(of);
                
                rq.setAttribute("bestellparam", bp);
                rq.setAttribute("orderform", of);
                
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
        
        cn.close();
        
        return mp.findForward(forward);
    }

	
	/**
	 * Prüft Angaben und schickt Email mit Bestellangaben an Bibliothek und an User
	 */
	public ActionForward sendOrder(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		Text t = new Text();
		Text cn = new Text();
		Auth auth = new Auth();
		String forward = "failure";
		OrderForm of = (OrderForm) fm;
		BestellParam bp = new BestellParam();
		Countries countriesInstance = new Countries();
		ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        
	     // es gibt drei mögliche Zugriffsformen, ohne eingeloggt zu sein. Priorisiert wie folgt:
		// 1. Kontokennung (überschreibt IP-basiert)
        // 2. IP-basiert (überschreibt Broker-Kennung)
		// 3. Broker-Kennung (z.B. Careum Explorer)
		
		if (of.getKkid()==null && !auth.isLogin(rq)) t = auth.grantAccess(rq);
		
		// Eingeloggt. IP-basiert, Kontokennung oder Brokerkennung
		if (((t!=null && t.getInhalt()!=null) || (of.getKkid()!=null || of.getBkid()!=null)) || auth.isLogin(rq)) {
			
			if (t!=null && t.getInhalt()!=null) {
    			of.setBibliothek(t.getKonto().getBibliotheksname());
            } else {            
            if (of.getKkid()!=null) { // Kontokennung
    			t = new Text(cn.getConnection(), Long.valueOf(12), of.getKkid()); // Text mit Kontokennung
    			of.setBibliothek(t.getKonto().getBibliotheksname());
    		}
    		if (of.getBkid()!=null) { // Brokerkennung
    			t = new Text(cn.getConnection(), Long.valueOf(11), of.getBkid()); // Text mit Brokerkennung
    			if (t.getKonto().getId()!=null) { // Brokerkennung ist EINEM Konto zugewiesen
    				of.setBibliothek(t.getKonto().getBibliotheksname());
    			} else { // Borkerkennung ist offen
    				// TODO: Prüfungen wer was liefern kann und Anzeige
    			}
    		}
            }
			
			// zugehörige Bestellformular-Parameter holen
            if (!auth.isLogin(rq) && t!=null && t.getInhalt()!=null) {            	
            	bp = new BestellParam(t, cn.getConnection());
            	// Länderauswahl setzen
            	List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());           
                of.setCountries(allPossCountries);
            } else {
            	if (auth.isLogin(rq)) {
            		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            		bp = new BestellParam(ui.getKonto(), cn.getConnection());
            		// Länderauswahl setzen
                	List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());           
                    of.setCountries(allPossCountries);
            	}
            }
        	
        try {
        	// allenfalls Leerschläge aus Email entfernen
        	if (of.getKundenmail()!=null) of.setKundenmail(of.getKundenmail().trim());
        	Message message = getMessageForMissingBestellParams(of, bp);
        	if (message.getMessage()==null) {
        		
        		of.setKundenmail(extractEmail(of.getKundenmail())); // ungültige Zeichen entfernen
        		CodeString codeString = new CodeString();
        		// Cookie Base64 codiert um besseren Datenschutz zu gewährleisten
        		Cookie cookie = new Cookie( "doctordoc-bestellform", codeString.encodeString(of.getKundenvorname() + "---" + of.getKundenname() + "---" + of.getKundenmail()));
        		cookie.setMaxAge(-1); //nur während aktueller Session gültig
        		cookie.setVersion(1);
        		try { // falls Eingabe ungültige, Nicht-ASCII-Zeichen enthielt, klappt das Versenden, und das Cookie-Setzen wird übersprungen.
						rp.addCookie(cookie);
					} catch (Exception e) {
						log.error("Setting Cookie: " + e.toString());
					}
        	
            forward = "success";
            
        	// aktuelles Datum setzen
            Date d = new Date();
            ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String date = sdf.format(d);
            
            MHelper mh = new MHelper();
			String[] to = new String[2];
			
			AbstractBenutzer u = new AbstractBenutzer(); // Für aus dbs anhand der Email und dem Konto geholter Benutzer
            
			if (auth.isLogin(rq)) {
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            	to[0] = ui.getKonto().getDbsmail();
            	u = getUserFromBestellformEmail(ui.getKonto(), of.getKundenmail(), cn.getConnection());
            } else { // IP-basiert, Konto- und Borkerkennungen
        		to[0] = t.getKonto().getDbsmail();
        		of.setBibliothek(t.getKonto().getBibliotheksname());
        		u = getUserFromBestellformEmail(t.getKonto(), of.getKundenmail(), cn.getConnection());
            }
			
			if (u.getId()!=null) of.setForuser(u.getId().toString());  // Vorselektion bei Abspeichern in JSP, falls User bekannt
			
			to[1] = of.getKundenmail();
			StringBuffer m = new StringBuffer();
			m.append("Vorname: " + of.getKundenvorname() + "\n");
			m.append("Name: " + of.getKundenname() + "\n");
			m.append("Email: " + of.getKundenmail() + "\n");
			
			// parametrisierbarer Teil
			if (of.getFreitxt1_inhalt()!=null && !of.getFreitxt1_inhalt().equals("")) m.append(of.getFreitxt1_label()+ ": " + of.getFreitxt1_inhalt() + "\n");
			
			if (of.getKundeninstitution()!=null && !of.getKundeninstitution().equals("")) { // ggf. Angaben aus Formular
				m.append("Institution: " + of.getKundeninstitution() + "\n");
			} else { // ggf. Angaben aus dbs
				if (u.getInstitut()!=null && !u.getInstitut().equals("")) m.append("Institution: " + u.getInstitut() + "\n");
			}
			
			if (of.getKundenabteilung()!=null && !of.getKundenabteilung().equals("")) { // ggf. Angaben aus Formular
				m.append("Abteilung: " + of.getKundenabteilung() + "\n");
			} else { // ggf. Angaben aus dbs
				if (u.getAbteilung()!=null && !u.getAbteilung().equals("")) m.append("Abteilung: " + u.getAbteilung() + "\n");
			}
			
			if (of.getFreitxt2_inhalt()!=null && !of.getFreitxt2_inhalt().equals("")) m.append(of.getFreitxt2_label()+ ": " + of.getFreitxt2_inhalt() + "\n");
			if (of.getKundenadresse()!=null && !of.getKundenadresse().equals("")) m.append("Lieferadresse: " + of.getKundenadresse() + "\n");
			if (of.getKundenstrasse()!=null && !of.getKundenstrasse().equals("")) m.append("Strasse: " + of.getKundenstrasse() + "\n");
			if (of.getKundenplz()!=null && !of.getKundenplz().equals("")) m.append("PLZ: " + of.getKundenplz() + "\n");
			if (of.getKundenort()!=null && !of.getKundenort().equals("")) m.append("Ort: " + of.getKundenort() + "\n");
			if (of.getKundenland()!=null && !of.getKundenland().equals("0")) m.append("Land: " + of.getKundenland() + "\n");
			
			if (of.getKundentelefon()!=null && !of.getKundentelefon().equals("")) { // ggf. Angaben aus Formular
				m.append("Telefon: " + of.getKundentelefon() + "\n");
			} else { // ggf. Angaben aus dbs
				if (u.getTelefonnrg()!=null && !u.getTelefonnrg().equals("")) m.append("Telefon G: " + u.getTelefonnrg() + "\n");
				if (u.getTelefonnrp()!=null && !u.getTelefonnrp().equals("")) m.append("Telefon P: " + u.getTelefonnrp() + "\n");
			}
			
			if (of.getKundenbenutzernr()!=null && !of.getKundenbenutzernr().equals("")) m.append("Benutzernummer: " + of.getKundenbenutzernr() + "\n");
			if (of.getFreitxt3_inhalt()!=null && !of.getFreitxt3_inhalt().equals("")) m.append(of.getFreitxt3_label()+ ": " + of.getFreitxt3_inhalt() + "\n");
			if (of.getRadiobutton()!=null && !of.getRadiobutton().equals("")) m.append(of.getRadiobutton_name()+ ": " + of.getRadiobutton() + "\n");
			
			m.append("\n");
			
			if (of.getDeloptions() != null && !of.getDeloptions().equals("")) m.append("Gewünschte Lieferart: " + of.getDeloptions().toUpperCase() + "\n");
			if (of.getPrio()!=null && of.getPrio().equals("urgent")) m.append("Priorität: EILT" + "\n");
			
			m.append("-----" + "\n");
			
			if (of.getMediatype().equals("Artikel")) {
				if (of.getRfr_id()!=null && !of.getRfr_id().equals("")) m.append("DATENBANK: " + of.getRfr_id() + "\n"); ;
				m.append("PUBLIKATIONSART: Zeitschriftenartikel " + of.getGenre() + "\n");
				m.append("VERFASSER: " + of.getAuthor() + "\n");
				m.append("ARTIKELTITEL: " + of.getArtikeltitel() + "\n");
				m.append("ZEITSCHRIFT: " + of.getZeitschriftentitel() + "\n");
				m.append("ISSN: " + of.getIssn() + "\n");
				m.append("JAHR: " + of.getJahr() + "\n");
				m.append("JAHRGANG: " + of.getJahrgang() + "\n");
				m.append("HEFT: " + of.getHeft() + "\n");
				m.append("SEITEN: " + of.getSeiten() + "\n");

				// Falls nur ISSN und kein Zeitschriftentitel vorhanden...
				if (of.getIssn() != null && !of.getIssn().equals("") && 
				   (of.getZeitschriftentitel()==null || of.getZeitschriftentitel().equals("")) ) {
					
//					 EZB-Link vorbereiten
		            String bibid = "AAAAA";
		            if (t.getKonto().getEzbid()!=null && !t.getKonto().getEzbid().equals("")) bibid = t.getKonto().getEzbid();
		            String link = "http://rzblx1.uni-regensburg.de/ezeit/searchres.phtml?bibid=" + bibid + "&colors=7&lang=de&jq_type1=KT&jq_term1=&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&jq_bool4=AND&jq_not4=+&jq_type4=IS&offset=-1&hits_per_page=50&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=4&jq_term4=";
					
					m.append("Link zur EZB: " + link + of.getIssn() + "\n"); // Link zur EZB mitschicken
				}
				
				if (of.getDoi() != null && !of.getDoi().equals("")) {
					m.append("DOI: " + of.getDoi() + "\n");
					if (!extractDoi(of.getDoi()).contains("http://")) {
						m.append("DOI-URI: " + "http://dx.doi.org/" + extractDoi(of.getDoi()) + "\n");
					} else {
						m.append("DOI-URI: " + extractDoi(of.getDoi()) + "\n");
					}
				}
				if (of.getPmid() != null && !of.getPmid().equals("")) {
					m.append("PMID: " + of.getPmid() + "\n");
					m.append("PMID-URI: " + "http://www.ncbi.nlm.nih.gov/pubmed/" + extractPmid(of.getPmid()) + "\n");
				}
				m.append("\n");
			}
			
			if (of.getMediatype().equals("Teilkopie Buch") || of.getMediatype().equals("Buch")) {
				if (of.getRfr_id()!=null && !of.getRfr_id().equals("")) m.append("DATENBANK: " + of.getRfr_id() + "\n"); ;
				if (of.getMediatype().equals("Teilkopie Buch")) {m.append("PUBLIKATIONSART: Buchausschnitt " + of.getGenre() + "\n");} else {m.append("PUBLIKATIONSART: Buch komplett " + of.getGenre() + "\n");}
				m.append("VERFASSER: " + of.getAuthor() + "\n");
				if (of.getMediatype().equals("Teilkopie Buch")) m.append("KAPITEL: " + of.getKapitel() + "\n");
				m.append("BUCHTITEL: " + of.getBuchtitel() + "\n");
				m.append("VERLAG: " + of.getVerlag() + "\n");
				m.append("ISBN: " + of.getIsbn() + "\n");
				if (!of.getIssn().equals("")) m.append("ISSN: " + of.getIssn() + "\n");  // Buchserie mit ISSN
				m.append("JAHR: " + of.getJahr() + "\n");
				if (!of.getJahrgang().equals("")) m.append("Jahrgang: " + of.getJahrgang() + "\n"); // Buchserie mit Zählung
				if (of.getMediatype().equals("Teilkopie Buch")) m.append("SEITEN: " + of.getSeiten() + "\n");
				
				if (of.getDoi() != null && !of.getDoi().equals("")) {
					m.append("DOI: " + of.getDoi() + "\n");
					if (!extractDoi(of.getDoi()).contains("http://")) {
						m.append("DOI-URI: " + "http://dx.doi.org/" + extractDoi(of.getDoi()) + "\n");
					} else {
						m.append("DOI-URI: " + extractDoi(of.getDoi()) + "\n");
					}
				}
				m.append("\n");
				
				}
			
			if (of.getNotizen() != null && !of.getNotizen().equals("")) {
			m.append("Anmerkungen des Kunden/der Kundin: " + of.getNotizen() + "\n");
			}
			
			m.append("-----" + "\n");
			m.append("Bestelldatum: " + date + "\n");
			m.append("Brought to you by ");
			m.append(ReadSystemConfigurations.getApplicationName() + ": " + ReadSystemConfigurations.getServerWelcomepage() + "\n");
			
			// direkten Login-Link für Bibliothekare zusammenstellen			
			String loginlink = "http://www.doctor-doc.com/version1.0/pl.do?" + convertOpenUrlInstance.makeGetMethodString(of) + "&foruser=" + of.getForuser();
			
			String adduserlink = "";
			if (u.getId()==null) { // User unbekannt => Übernahme-Link für Bibliothekare
				adduserlink = "http://www.doctor-doc.com/version1.0/add.do?" + createUrlParamsForAddUser(of);
			}
			
			String prio = "3"; // Email-Priorität 3 = normal
			if (of.getPrio()!=null && of.getPrio().equals("urgent")) prio = "1"; // hoch
						
			if (of.getMediatype().equals("Artikel")) {
				String[] toemail = new String[1];
				
				// Mail an Kunde schicken, ReplyTo = Bibliothek
				toemail[0] = to[1]; // Kundenmail
				mh.sendMailReplyTo(toemail,"Artikelbestellung aus: " + of.getZeitschriftentitel() + "\040" +
				of.getJahr() + ";" + of.getJahrgang() + "(" + of.getHeft() + "):" + of.getSeiten(), m.toString(), to[0]);				
				
				// Mail an Bibliothek schicken, ReplyTo = Kunde
				toemail[0] = to[0]; // Bibliomail
				if (u.getId()!=null) { // Kunde bekannt
					mh.sendMailReplyTo(toemail,"Artikelbestellung aus: " + of.getZeitschriftentitel() + "\040" +
					of.getJahr() + ";" + of.getJahrgang() + "(" + of.getHeft() + "):" + of.getSeiten(), m.toString() + 
					"\012Bestellangaben nach " + ReadSystemConfigurations.getApplicationName() + " übernehmen:\012"
					+ loginlink, of.getKundenmail(), prio);
				} else { // Kunde unbekannt
					mh.sendMailReplyTo(toemail,"Artikelbestellung aus: " + of.getZeitschriftentitel() + "\040" +
					of.getJahr() + ";" + of.getJahrgang() + "(" + of.getHeft() + "):" + of.getSeiten(), m.toString() + 
					"\012Unbekannte Email! Kundenangaben nach " + ReadSystemConfigurations.getApplicationName()
					+ " übernehmen:\012" + adduserlink + "\012" +
					"\012Bestellangaben nach " + ReadSystemConfigurations.getApplicationName() + " übernehmen:\012" + loginlink, of.getKundenmail(), prio);
				}
			}
			
			if (of.getMediatype().equals("Teilkopie Buch")) {
				String[] toemail = new String[1];
				
//				 Mail an Kunde schicken, ReplyTo = Bibliothek
				toemail[0] = to[1]; // Kundenmail
				mh.sendMailReplyTo(toemail,"Bestellung Buchausschnitt aus: " + of.getBuchtitel() + "\040" +
				of.getJahr() + ":" + of.getSeiten(), m.toString(), to[0]);	
				
				// Mail an Bibliothek schicken, ReplyTo = Kunde
				toemail[0] = to[0]; // Bibliomail
				if (u.getId()!=null) { // Kunde bekannt
					mh.sendMailReplyTo(toemail,"Bestellung Buchausschnitt aus: " + of.getBuchtitel() + "\040" +
					of.getJahr() + ":" + of.getSeiten(), m.toString(), of.getKundenmail(), prio);
				} else { // Kunde unbekannt
					mh.sendMailReplyTo(toemail,"Bestellung Buchausschnitt aus: " + of.getBuchtitel() + "\040" +
					of.getJahr() + ":" + of.getSeiten(), m.toString() + 
					"\012Unbekannte Email! Kundenangaben nach " + ReadSystemConfigurations.getApplicationName() + " übernehmen:\012" + 
					adduserlink, of.getKundenmail(), prio);
				}
			}
			
			if (of.getMediatype().equals("Buch")) {
				String[] toemail = new String[1];
				
//				 Mail an Kunde schicken, ReplyTo = Bibliothek
				toemail[0] = to[1]; // Kundenmail
				mh.sendMailReplyTo(toemail,"Bestellung Buch komplett: " + of.getBuchtitel() + "\040" +
				of.getJahr(), m.toString(), to[0]);
				
				// Mail an Bibliothek schicken, ReplyTo = Kunde
				toemail[0] = to[0]; // Bibliomail
				if (u.getId()!=null) { // Kunde bekannt
					mh.sendMailReplyTo(toemail,"Bestellung Buch komplett: " + of.getBuchtitel() + "\040" +
					of.getJahr(), m.toString(), of.getKundenmail(), prio);
				} else { // Kunde unbekannt
					mh.sendMailReplyTo(toemail,"Bestellung Buch komplett: " + of.getBuchtitel() + "\040" +
					of.getJahr(), m.toString() + "\012Unbekannte Email! Kundenangaben nach " + ReadSystemConfigurations.getApplicationName() + " übernehmen:\012" + 
					adduserlink, of.getKundenmail(), prio);
				}
			}
			
        	} else {        		
        		forward = "missingvalues";
        		rq.setAttribute("messagemissing", message);
        	}
        	
        	// für Get-Methode in PrepareLogin of URL-codieren
            of = of.encodeOrderForm(of);

            rq.setAttribute("orderform", of);
            
        } catch (Exception e) {
        	forward = "failure";
        	ErrorMessage em = new ErrorMessage("error.send", "login.do");
            rq.setAttribute("errormessage", em);
            // Severe error
            MHelper mh = new MHelper();
	        mh.sendErrorMail(e.toString(), "Bestellformular - Fehler beim Versenden der Bestellung");
        }
        
        if (auth.isLogin(rq)) {
	        ActiveMenusForm mf = new ActiveMenusForm();
	        mf.setActivemenu("suchenbestellen");
	        rq.setAttribute("ActiveMenus", mf);
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("bestellform");
            rq.setAttribute("ActiveMenus", mf);        	
        }


        } else {
        	ErrorMessage em = new ErrorMessage("error.ip", "login.do");
            rq.setAttribute("errormessage", em);
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("bestellform");
            rq.setAttribute("ActiveMenus", mf);
        }
        
        cn.close();
        return mp.findForward(forward);
    }
	
	/**
	 * Bereitet die Bestellformular-Konfiguration vor
	 */
	public ActionForward prepareConfigure(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
		Text cn = new Text();
		Text ip = new Text();
		Auth auth = new Auth();
		BestellParam ipbasiert = new BestellParam();
		
		if (auth.isLogin(rq)) {
		if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
		
		try {
		forward = "success";
		
		ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute("ActiveMenus", mf);
        
      
        boolean hasIP = cn.hasIP(cn.getConnection(), ui.getKonto());

        if (hasIP) {
	        ip.setId(Long.valueOf(0));
        	ip.setTexttyp(new Texttyp(Long.valueOf(9), cn.getConnection()));
	        ip.setKonto(ui.getKonto());
	        ipbasiert = new BestellParam(ip, cn.getConnection());
        }
        
        BestellParam eingeloggt = new BestellParam(ui.getKonto(), cn.getConnection());
        
        List<Text> kkid = cn.getAllKontoText(new Texttyp(Long.valueOf(12), cn.getConnection()), ui.getKonto().getId(), cn.getConnection());
        List<Text> bkid = cn.getAllKontoText(new Texttyp(Long.valueOf(11), cn.getConnection()), ui.getKonto().getId(), cn.getConnection());
        
        if (eingeloggt!=null && eingeloggt.getId()!=null) {
        	rq.setAttribute("eingeloggt", eingeloggt.getId()); // allenfalls vorhandene BestellParam-ID in Request     	
        } else {
        	rq.setAttribute("eingeloggt", "0"); // 0 als ID
        }
        
        if (hasIP) { // IP hinterlegt
        	if (ipbasiert!=null && ipbasiert.getId()!=null) {
        		rq.setAttribute("ipbasiert", ipbasiert.getId()); // allenfalls vorhandene BestellParam-ID in Request
        	} else {
        		rq.setAttribute("ipbasiert", "-1"); // -1 als ID
        	}
        }
        
        if (kkid.size()>0) rq.setAttribute("kkid", kkid);
        if (bkid.size()>0) rq.setAttribute("bkid", bkid);
		
		} catch(Exception e) {
			log.error("BestellformAction - prepareConfigure: " + e.toString());
        } finally {
        	cn.close();
        }
		
		} else { // keine Berechtigung
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);			
		}
		} else { // nicht eingeloggt
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);			
		}

		return mp.findForward(forward);
    }
	
	/**
	 * ändert und erstellt angepasste Bestellformulare
	 */
	public ActionForward modify(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
		Text cn = new Text();
		Auth auth = new Auth();
		
		BestellParam bp = (BestellParam) fm;
		
		if (auth.isLogin(rq)) {
		if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
		if (checkPermission(ui, bp, cn.getConnection())) { // Prüfung auf URL-Hacking			
		
		try {
		forward = "success";
		
		ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute("ActiveMenus", mf);
        
        BestellParam custom = new BestellParam();
        custom.setKid(ui.getKonto().getId());
        
        if (bp.getId()>0) { // bestehendes BestellParam (eingeloggt oder IP-basiert)
        	custom = new BestellParam(bp.getId(), cn.getConnection());
        }
        if (bp.getId()==0) {
        	custom.setTyid(Long.valueOf(13));
        	custom.setKennung("Bestellformular eingeloggt");        	
        }
        if (bp.getId()==-1) {
        	custom.setTyid(Long.valueOf(9)); // IP
        }
        if (bp.getId()==-2) { // Konto-Kennung
        	custom = new BestellParam(bp.getKennung(), ui.getKonto().getId(), cn.getConnection());
        	if (custom.getId()==null) {
        	custom.setTyid(Long.valueOf(12));
        	custom.setKid(ui.getKonto().getId());
        	custom.setKennung(bp.getKennung());
        	custom.setId(bp.getId());
        	}
        }
        if (bp.getId()==-3) { // Borker-Kennung
        	custom = new BestellParam(bp.getKennung(), ui.getKonto().getId(), cn.getConnection());
        	if (custom.getId()==null) {
        	custom.setTyid(Long.valueOf(11));
        	custom.setKid(ui.getKonto().getId());
        	custom.setKennung(bp.getKennung());
        	custom.setId(bp.getId());
        	}
        }
        
        rq.setAttribute("bestellform", custom);
        
		
		} catch(Exception e) {
			log.error("modify: " + e.toString());
        } finally {
        	cn.close();
        }
		
		} else { // URL-Hacking
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.hack", "searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);
            log.info("modify: prevented URL-hacking!");
		}
		} else { // keine Berechtigung
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);			
		}
		} else { // nicht eingeloggt
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);			
		}

		cn.close(); // wird benötigt
		return mp.findForward(forward);
    }
	
	/**
	 * speichert neue und bestehende Bestellformulare
	 */
	public ActionForward save(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
		Text cn = new Text();
		Auth auth = new Auth();
		
		BestellParam bp = (BestellParam) fm;
		Countries countriesInstance = new Countries();
		
		if (auth.isLogin(rq)) {
		if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
		if (checkPermission(ui, bp, cn.getConnection())) { // Prüfung auf URL-Hacking
			
		try {
			forward = "success";
				
			ActiveMenusForm mf = new ActiveMenusForm();
		    mf.setActivemenu("konto");		
		    rq.setAttribute("ActiveMenus", mf);
		    
		    bp = checkBPLogic(bp); // logische Prüfungen und setzt abhängige Werte
		    
		    if (bp.getMessage()==null) { // keine Fehlermedlungen
			
		    forward = "bestellform";
		    OrderForm of = new OrderForm();
		    	
	        if (bp.getId()<=0) { // negative ID => save
	        	bp.setId(bp.save(bp, cn.getConnection()));
	        } else { // positive ID => update
	        	bp.update(bp, cn.getConnection());
	        }	        
	        bp.setBack(true); // Flag für "Back" auf Bestellform
		    bp.setLink_back("bfconfigure.do?method=modify&id=" + bp.getId());
		    
            	// analog wie in validate()
	        	// Länderauswahl setzen
            	List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());            
                of.setCountries(allPossCountries);
                if (of.getRadiobutton().equals("")) of.setRadiobutton(bp.getOption_value1()); // Default Option1
            
            if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // Defaultwert deloptions
                 		if (bp.isLieferart()==false) {of.setDeloptions("email");} else {of.setDeloptions(bp.getLieferart_value1());}
                 	}
	        
            rq.setAttribute("orderform", of);
            rq.setAttribute("bestellparam", bp);
	        
		    } else { // Fehlermeldung vorhanden
		    	forward = "success"; // auf bestellformconfigure
		    	rq.setAttribute("message", bp.getMessage());
		    	rq.setAttribute("bestellform", bp);
		    }
		
		} catch(Exception e) {
			log.error("save: " + e.toString());
        } finally {
        	cn.close();
        }
		
		} else { // URL-Hacking
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.hack", "searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);
            log.info("save: prevented URL-hacking!");
		}
		} else { // keine Berechtigung
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);			
		}
		} else { // nicht eingeloggt
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);			
		}

		cn.close(); // wird benötigt
		return mp.findForward(forward);
    }
	
	
	/**
	 * Bereitet den Redirect mit SessionID für die British Library vor. Notwendig für Suche in Subsets (z.B. nur Journals)
	 */
	public ActionForward redirect(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		
		try {
		forward = "success";		
		OrderForm pageForm = (OrderForm) fm;
		String link = "";
		
		String sessionid = getSessionIdBl();
		
		if (!sessionid.equals("")) { // erlaubt Suche in Subset Journals
				link = "http://catalogue.bl.uk/F/" + sessionid + "?func=find-b&request=" + pageForm.getIssn() + "&find_code=ISSN&adjacent=Y&image.x=41&image.y=13";
		} else { // Suche durch alles, auch einzelne Artikel etc.
				link = "http://catalogue.bl.uk/F/?func=find-b&request=" + pageForm.getIssn() + "&find_code=ISSN&adjacent=Y&image.x=37&image.y=9";
		}
		
		pageForm.setLink(link);
		
		rq.setAttribute("orderform", pageForm);
		
		} catch(Exception e) {
			log.error("redirect: " + e.toString());
        }

		
		return mp.findForward(forward);
    }
	
	/**
	 * Extrahiert aus einem String die DOI
	 */
	public String extractDoi(String doi) {
		
		if (doi!=null && !doi.equals("")) {
		try {
			
		doi = doi.trim().toLowerCase();
		if (doi.contains("doi:")) doi = doi.substring(doi.indexOf("doi:")+4); // ggf. Text "DOI:" entfernen
		if (doi.contains("dx.doi.org/")) doi = doi.substring(doi.indexOf("dx.doi.org/")+11); // verschiedene Formen der Angaben entfernen ( dx.doi.org/... , http://dx.doi.org/...)
		if (doi.contains("doi/")) doi = doi.substring(doi.indexOf("doi/")+4); // ggf. Text "DOI/" entfernen
		
		} catch(Exception e) {
			log.error("extractDoi: " + doi + "\040" + e.toString());
        }
		}
		
        return doi;
    }
	
	/**
	 * Extrahiert aus einem String die PMID (Pubmed-ID
	 */
	public String extractPmid(String pmid) {
		
		if (pmid!=null && !pmid.equals("")) {
		
		try {
		
	  	Matcher w = Pattern.compile("[0-9]+").matcher(pmid);
    	if (w.find()) {
    		pmid = pmid.substring(w.start(), w.end());
    	}
    	
		} catch(Exception e) {
			log.error("extractPmid: " + pmid + "\040" + e.toString());
        }
		}        
        
        return pmid;
    }
	
	/**
	 * holt anhand einer Doi alle Artikelangaben
	 */
	public OrderForm resolveDoi(String doi) {
		
		// http://generator.ocoins.info/ [Eingabe: 10.1002/hec.1381 ]
		
		OrderForm of = new OrderForm();
		ContextObject co = new ContextObject();
		ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
		OpenUrl openUrlInstance = new OpenUrl();
		Http http = new Http();
		String link = "http://generator.ocoins.info/?doi=" + doi;
//		String link = "http://generator.ocoins.info/crossref?handle=" + doi;
		String content = "";
		
		try {

        content = http.getWebcontent(link, 2000, 2);
        
        content = content.replaceAll("&amp;amp;", "&amp;"); // falsche Doppelkodierung korrigieren...
        
        // Sicherstellen, dass die Anfrage aufgelöst wurde und vom OCoinS-Generator selber stammt (Ausschluss von direkter Weiterleitung)
        if (!content.contains("DOI Resolution Error") && content.contains("rfr_id=info%3Asid%2Focoins.info%3Agenerator")) {
        
        co = openUrlInstance.readOpenUrlFromString(content);
        of = convertOpenUrlInstance.makeOrderform(co);
        
        }
        
		} catch(Exception e) {
			log.error("resolveDoi: " + doi + "\040" + e.toString());
        }
		
        return of;
    }
	
	/**
	 * holt anhand einer PMID alle Artikelangaben
	 */
	public OrderForm resolvePmid(String pmid) {

		// automatisches Vervollständigen der Artikelangaben anhand der PMID (Pubmed-ID):
		// http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=3966282
		
		OrderForm of = new OrderForm();
		ContextObject co = new ContextObject();
		ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
		OpenUrl openUrlInstance = new OpenUrl();
		Http http = new Http();
		String link = "http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=" + pmid;
		// empfohlener Link wäre:
//		String link = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmode=xml&id=" + pmid;
		// TODO: Pubmed XML-Auswertung umstellen
		String content = "";
		
		try {

        content = http.getWebcontent(link, 2000, 2);
        co = openUrlInstance.readXmlPubmed(content);
        of = convertOpenUrlInstance.makeOrderform(co);
        
		} catch(Exception e) {
			log.error("resolvePmid: " + pmid + "\040" + e.toString());
        }
		
        return of;
    }
	
	/**
	 * holt die PMID anhand von Artikelangaben
	 * 
	 * @param OrderForm og
	 * @return String pmid
	 */
	public String getPmid(OrderForm of) {
		
		String pmid ="";
		Http http = new Http();
		
		try {
			
		String content = http.getWebcontent(composePubmedlinkToPmid(of), 2000, 2);
		
		if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
			pmid = content.substring(content.indexOf("<Id>")+4, content.indexOf("</Id>"));
		}
        
		} catch(Exception e) {
			log.error("getPmid(of): " + e.toString());
        }
		
        return pmid;
    }
	
	/**
	 * holt die PMID aus dem Webcontent
	 * 
	 * @param String webcontent
	 * @return String pmid
	 */
	public String getPmid(String content) {
		
		String pmid ="";
		
		try {
		
		if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
			pmid = content.substring(content.indexOf("<Id>")+4, content.indexOf("</Id>"));
		}
        
		} catch(Exception e) {
			log.error("getPmid(String): " + e.toString() + "\012" + content);
        }
		
        return pmid;
    }
	
	/**
	 * stellt den Suchlink zusammen um die PMID anhand von Artikelangaben zu holen
	 */
    public String composePubmedlinkToPmid(OrderForm pageForm){
    	
    	ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        
    	// http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=58[VI]+AND+383[PG]+AND+6[IP]+AND+2007[DP]+AND+0940-2993[TA] 
    	StringBuffer link = new StringBuffer();
    	link.append("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=");
    	link.append(pageForm.getIssn()+"[TA]");
    	if (pageForm.getJahrgang()!=null && !pageForm.getJahrgang().equals("")) link.append("+AND+"+pageForm.getJahrgang()+"[VI]");
    	if (pageForm.getHeft()!=null && !pageForm.getHeft().equals("")) link.append("+AND+"+pageForm.getHeft()+"[IP]");
    	if (pageForm.getSeiten()!=null && !pageForm.getSeiten().equals("")) link.append("+AND+"+convertOpenUrlInstance.extractSpage(pageForm.getSeiten())+"[PG]");
    	if (pageForm.getJahr()!=null && !pageForm.getJahr().equals("")) link.append("+AND+"+pageForm.getJahr()+"[DP]");
        
        return link.toString();
    }
	
	/**
	 * prüft, ob wichtige of-Werte fehlen (ISSN, Zeitschriftentitel, Author, Jahr, Jahrgang, Heft, Seitenzahlen)
	 */
	public boolean areArticleValuesMissing(OrderForm of) {
		
		boolean check = false;		
		
		try {
			
			if ( of.getMediatype().equals("Artikel") && // um zu verhindern, dass bei eine Übergabe aus OpenURL auch Bücher über Pubmed etc. geprüft werden
				(of.getIssn().equals("") || of.getZeitschriftentitel().equals("") ||
				 of.getAuthor().equals("") || of.getJahr().equals("") || of.getArtikeltitel().equals("") ||
				 of.getJahrgang().equals("") || of.getHeft().equals("") || of.getSeiten().equals("")) ) {
				check = true;
			}

				
			} catch(Exception e){
				log.error("areArticleValuesMissing: " + e.toString());
    		
    	}
		
		return check;
	}
	
	/**
	 * prüft, ob of-Werte bei Mussfeldern bei einem allfällig vorliegenden BestellParam fehlen,
	 * und gibt ggf. eine Message mit der entsprechenden Fehlermeldung zurück
	 */
	public Message getMessageForMissingBestellParams(OrderForm of, BestellParam bp) {
		
		Message m = new Message();
		Check ck = new Check();
		
		try {
			
				if (!ck.isMinLength(of.getKundenvorname(), 1)) m.setMessage("error.vorname"); // auf jeden Fall Mussfeld
				if (!ck.isMinLength(of.getKundenname(), 1)) m.setMessage("error.name"); // auf jeden Fall Mussfeld
        		if (!ck.isEmail(of.getKundenmail()) ) m.setMessage("error.mail"); // auf jeden Fall Mussfeld
        	if (bp!=null && bp.getId()!=null) {
				if (bp.isInst_required()==true && !ck.isMinLength(of.getKundeninstitution(), 1)) m.setMessage("error.institution");
				if (bp.isAbt_required()==true && !ck.isMinLength(of.getKundenabteilung(), 1)) m.setMessage("error.abteilung");
				if (bp.isFreitxt1_required()==true && !ck.isMinLength(of.getFreitxt1_inhalt(), 1)) m.setMessage("error.values");
				if (bp.isFreitxt2_required()==true && !ck.isMinLength(of.getFreitxt2_inhalt(), 1)) m.setMessage("error.values");
				if (bp.isFreitxt3_required()==true && !ck.isMinLength(of.getFreitxt3_inhalt(), 1)) m.setMessage("error.values");
				if (bp.isAdr_required()==true && !ck.isMinLength(of.getKundenadresse(), 1)) m.setMessage("error.adresse");
				if (bp.isStr_required()==true && !ck.isMinLength(of.getKundenstrasse(), 1)) m.setMessage("error.strasse");
				if (bp.isPlz_required()==true && !ck.isMinLength(of.getKundenplz(), 1)) m.setMessage("error.plz");
				if (bp.isOrt_required()==true && !ck.isMinLength(of.getKundenort(), 1)) m.setMessage("error.ort");
				if (bp.isLand_required()==true && !ck.isMinLength(of.getKundenland(), 2)) m.setMessage("error.land");
				if (bp.isTelefon_required()==true && !ck.isMinLength(of.getKundentelefon(), 1)) m.setMessage("error.telefon");
				if (bp.isBenutzernr_required()==true && !ck.isMinLength(of.getKundenbenutzernr(), 1)) m.setMessage("error.benutzernummer");
				if (bp.isGebuehren()==true && of.getGebuehren()==null) m.setMessage("error.fees"); // muss "on" sein
				if (bp.isAgb()==true && of.getAgb()==null) m.setMessage("error.agb"); // muss "on" sein
			}
				
			} catch(Exception e){
				log.error("areBestellParamMissing: " + e.toString());    		
			}
		
		return m;
	}
	
	/**
	 * Sucht anhand der im Bestellformular eingegebenen Email den zugehörigen Benutzer des betreffenden Kontos zu holen
	 */
	public AbstractBenutzer getUserFromBestellformEmail(Konto k, String email, Connection cn) {
		
		AbstractBenutzer u = new AbstractBenutzer();
		
		try {
			
			ArrayList<AbstractBenutzer> list = new ArrayList<AbstractBenutzer>();
			list = u.getUserListFromEmailAndKonto(k, email, cn);
			
			if (list.size()>0) u = list.get(0); // es wird der erste Benutzer zurückgegeben
				
			} catch(Exception e){
				log.error("getUserFromBestellformEmail: " + email + "\040" + e.toString());    		
    	}
		
		return u;
	}
	
	private String createUrlParamsForAddUser (OrderForm of) {
		StringBuffer urlParam = new StringBuffer();
		CodeUrl urlCoder = new CodeUrl();
		
		if (of.getKundenmail()!=null && !of.getKundenmail().equals("")) urlParam.append("email=" + of.getKundenmail());
		if (of.getKundenname()!=null && !of.getKundenname().equals("")) urlParam.append("&name=" + urlCoder.encode(of.getKundenname()));
		if (of.getKundenvorname()!=null && !of.getKundenvorname().equals("")) urlParam.append("&vorname=" + urlCoder.encode(of.getKundenvorname()));
		if (of.getKundeninstitution()!=null && !of.getKundeninstitution().equals("")) urlParam.append("&institut=" + urlCoder.encode(of.getKundeninstitution()));
		if (of.getKundenabteilung()!=null && !of.getKundenabteilung().equals("")) urlParam.append("&abteilung=" + urlCoder.encode(of.getKundenabteilung()));
		if (of.getKundenadresse()!=null && !of.getKundenadresse().equals("")) urlParam.append("&adresse=" + urlCoder.encode(of.getKundenadresse()));
		if (of.getKundenstrasse()!=null && !of.getKundenstrasse().equals("")) urlParam.append("&adresse=" + urlCoder.encode(of.getKundenstrasse()));
		if (of.getKundentelefon()!=null && !of.getKundentelefon().equals("")) urlParam.append("&telefonnrg=" + of.getKundentelefon());
		if (of.getKundenplz()!=null && !of.getKundenplz().equals("")) urlParam.append("&plz=" + urlCoder.encode(of.getKundenplz()));
		if (of.getKundenort()!=null && !of.getKundenort().equals("")) urlParam.append("&ort=" + urlCoder.encode(of.getKundenort()));
		if (of.getKundenland()!=null && !of.getKundenland().equals("")) urlParam.append("&land=" + urlCoder.encode(of.getKundenland()));
		
		return urlParam.toString();
	}
	
	/**
	 * extrahiert mit einem Regex die Email aus einem String
	 */
	private String extractEmail(String email) {
		String extractedEmail = email;		
		try {
			Pattern p = Pattern.compile("[A-Za-z0-9._-]+@[A-Za-z0-9][A-Za-z0-9.-]{0,61}[A-Za-z0-9]\\.[A-Za-z.]{2,6}");
		  	Matcher m = p.matcher(email);
		  	
		  	if (m.find()) {
		  		extractedEmail = email.substring(m.start(), m.end());
		  	}			
			
		} catch (Exception e) {
			log.error("extractEmail(String email): " + email + "\040" + e.toString());			
		}
		
		
		return extractedEmail;
	}
	
	/**
	 * holt anhand eine SessionID der British Library
	 */
	private String getSessionIdBl() {
		
		// reduziert die Treffer z.B. bei Long-term Dietary Cadmium Intake and Postmenopausal Endometrial Cancer Incidence
		// von 47 bei Suche ohne Sessionid auf die relevanten 2

		String sessionid = "";
		Http http = new Http();
		String link = "http://catalogue.bl.uk/F/?func=file&file_name=find-b";
		
		try {
		
		String content = http.getWebcontent(link, 2000, 2);
		
		if (content.contains("title=\"Catalogue subset search\"")) {
			
			link = content.substring(content.lastIndexOf("http://", content.indexOf("title=\"Catalogue subset search\"")), content.lastIndexOf("\"", content.indexOf("title=\"Catalogue subset search\"")));
			content = http.getWebcontent(link, 2000, 2);
			
			if (content.contains("Serials and periodicals")) {
				
				link = content.substring(content.lastIndexOf("http://", content.indexOf("Serials and periodicals")), content.lastIndexOf("\"", content.indexOf("Serials and periodicals")));
				content = http.getWebcontent(link, 2000, 2);
				
				if (content.contains("action=\"http://catalogue.bl.uk:80/F/")) {
					
					sessionid = content.substring(content.indexOf("action=\"http://catalogue.bl.uk:80/F/")+36, content.indexOf("\"", content.indexOf("action=\"")+8));
					
				}
			}
			
		}
		
		} catch(Exception e) {
			log.error("getSessionIdBl: " + e.toString());
        }
       
		
        return sessionid;
    }
	
	/**
	 * Prüft auf URL-Hacking bei modify (Bestellformular)
	 */
	private boolean checkPermission(UserInfo ui, BestellParam bp, Connection cn) {
		
		boolean check = false;
		Text t = new Text();
		
		try {
			
			if (bp!=null && bp.getId()!=null) {
				
				if (bp.getId()==0) {
					check = true; // Bestellformular eingeloggt
					return check;
				}
				if (bp.getId()==-1) { // neues Bestellformulat IP-basiert
					if (t.hasIP(cn, ui.getKonto())) {
						check = true;
					}
					return check;
				}
				if (bp.getId()==-2 && bp.getKennung()!=null) { // Konto-Kennung
					t = new Text(cn, bp.getKennung());
					if (t.getKonto().getId()!=null && t.getKonto().getId().equals(ui.getKonto().getId())) {
						check = true;
						return check;
					}					
				}
				if (bp.getId()==-3 && bp.getKennung()!=null) { // Broker-Kennung
					t = new Text(cn, bp.getKennung());
					if (t.getKonto().getId()!=null && t.getKonto().getId().equals(ui.getKonto().getId())) {
						check = true;
						return check;
					}					
				}
				BestellParam bp_compare = new BestellParam(bp.getId(), cn);
				// Prüfung, ob die ID zum Konto gehört! (URL-Hacking)
				if (bp_compare.getKid()!=null && bp_compare.getKid().equals(ui.getKonto().getId())) {
					check = true;
					return check;
				}				
			}			
				
			} catch(Exception e){
				log.error("checkPermission(UserInfo ui, Long id): " + e.toString());    		
			}
		
		return check;
	}
	
	/**
	 * Prüft Eingaben bei einem BestellParam auf die logischen Abhängigkeiten,
	 * setzt automatisch abhängige Werte und gibt bei fehlenden Werten ggf. 
	 * eine Fehlermeldung aus
	 */
	private BestellParam checkBPLogic(BestellParam bp) {
		
		// serielle Ausgabe um ggf. auf der jsp mehrsprachige Fehlermeldungen zu triggern
		
		try {
			
			Message m = new Message();
			Check ck = new Check();
			
			if (ck.isMinLength(bp.getLieferart_value1(), 1) ||
				ck.isMinLength(bp.getLieferart_value2(), 1) ||
				ck.isMinLength(bp.getLieferart_value3(), 1)) {
				
				if (ck.isMinLength(bp.getLieferart_value1(), 1)) {
					bp.setLieferart(true); // gültige Eingaben erfolgt				
				} else {
					m.setMessage("bestellformconfigure.deliveryway");
					bp.setMessage(m);		
				}
				
			}
			
			if (bp.isFreitxt1()==true && !ck.isMinLength(bp.getFreitxt1_name(), 1)) {
				m.setMessage("bestellformconfigure.frei1");
				bp.setMessage(m);				
			} else {
				if (!bp.isFreitxt1() && ck.isMinLength(bp.getFreitxt1_name(), 1)) {
					bp.setFreitxt1_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
				}
			}
			if (bp.isFreitxt2()==true && !ck.isMinLength(bp.getFreitxt2_name(), 1)) {
				m.setMessage("bestellformconfigure.frei2");
				bp.setMessage(m);				
			} else {
				if (!bp.isFreitxt2() && ck.isMinLength(bp.getFreitxt2_name(), 1)) {
					bp.setFreitxt2_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
				}
			}
			if (bp.isFreitxt3()==true && !ck.isMinLength(bp.getFreitxt3_name(), 1)) {
				m.setMessage("bestellformconfigure.frei3");
				bp.setMessage(m);				
			} else {
				if (!bp.isFreitxt3() && ck.isMinLength(bp.getFreitxt3_name(), 1)) {
					bp.setFreitxt3_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
				}
			}
			
			if (ck.isMinLength(bp.getOption_value1(), 1) ||
				ck.isMinLength(bp.getOption_value2(), 1) ||
				ck.isMinLength(bp.getOption_value3(), 1)) {
					
					if (ck.isMinLength(bp.getOption_value1(), 1)) {
						bp.setOption(true); // gültige Eingaben erfolgt				
					} else {
						m.setMessage("bestellformconfigure.option");
						bp.setMessage(m);				
					}
					
				} else {
					bp.setOption_name("");
					bp.setOption_comment("");
					bp.setOption_linkout("");
					bp.setOption_linkoutname("");
				}
			
			if (bp.isGebuehren()==true) {
				if (!ck.isMinLength(bp.getLink_gebuehren(), 1)) {
					m.setMessage("bestellformconfigure.fee");
					bp.setMessage(m);
				} else {
					if (!ck.isUrl(bp.getLink_gebuehren())) {
						m.setMessage("bestellformconfigure.fee_link");
						bp.setMessage(m);
					}					
				}				
			} else {
				if (!bp.isGebuehren() && ck.isMinLength(bp.getLink_gebuehren(), 1)) {
					bp.setLink_gebuehren(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
				}
			}
			if (bp.isAgb()==true) {
				if (!ck.isMinLength(bp.getLink_agb(), 1)) {
					m.setMessage("bestellformconfigure.agb");
					bp.setMessage(m);
				} else {
					if (!ck.isUrl(bp.getLink_agb())) {
						m.setMessage("bestellformconfigure.agb_link");
						bp.setMessage(m);
					}					
				}				
			} else {
				if (!bp.isAgb() && ck.isMinLength(bp.getLink_agb(), 1)) {
					bp.setLink_agb(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
				}
			}		
			
				
			} catch(Exception e){
				log.error("checkBPLogic(BestellParam bp): " + e.toString());    		
			}
		
		return bp;
	}

}
