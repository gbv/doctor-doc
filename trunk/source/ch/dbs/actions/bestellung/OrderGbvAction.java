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


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;


import util.Auth;
import util.Check;
import util.CodeUrl;
import util.Http;
import util.MyException;
import util.SpecialCharacters;
import ch.dbs.actions.illformat.IllHandler;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.GbvSruForm;
import ch.dbs.form.IllForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

/**
 * Klasse um Metainformation wie PPN zu bestimmen und Bestellungen beim GBV durchzuführen
 * 
 * @author Markus Fischer
 */
public final class OrderGbvAction extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(OrderGbvAction.class);
    
	/**
	 * löst eine Bestellung beim GBV aus
	 */
	public ActionForward order(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		OrderForm of = (OrderForm) fm;
	    OrderState orderstate = new OrderState();
		OrderAction orderActionInstance = new OrderAction();
		IllHandler illHandlerInstance = new IllHandler();
		Lieferanten lieferantenInstance = new Lieferanten();
		UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
		Text t = new Text();
		Check ck = new Check();
		Auth auth = new Auth();
		// URL Test-System des GBV
//		String baseurl = "http://cbst.gbv.de:8080/cgi-bin/vuefl_0702/vuefl_recv_data.pl";
		// URL Produktiv-System des GBV
		String baseurl = "http://cbs4.gbv.de:8080/cgi-bin/vuefl/vuefl_recv_data.pl";
        
         if (auth.isLogin(rq)) { // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        	 
        	 // Fileformat für Statistik aus gemachten Angaben schliessen
        	 if (of.getMediatype().equals("Buch")) { of.setFileformat("Papierkopie");        		 
        	 } else {
        		 if (of.getDeloptions().equals("post")) of.setFileformat("Papierkopie");
        		 if (of.getDeloptions().equals("fax")) of.setFileformat("Papierkopie");
        		 if (of.getDeloptions().equals("fax to pdf")) of.setFileformat("PDF");
        	 }
        
         if (!of.getSubmit().equals("GBV-Suche")) {
        	 
         if (auth.isUserAccount(of.getForuser(), t.getConnection())) { // Bestellkunde darf nicht "deaktiviert" sein
         if (auth.isMaxordersj(rq, t.getConnection())) { // Kontrolle max. Bestellungen aller Kunden pro Kalenderjahr auf einem Konto
         if (auth.isMaxordersutotal(of.getForuser(), rq, t.getConnection())) { // Kontrolle max. Bestellungen eines Kunden pro Kalenderjahr
            
            try {
            	
            // *** Bestelltyp Copy
            	if (of.getMediatype().equals("Artikel") || of.getMediatype().equals("Teilkopie Buch")) {
            		
            	if (of.getPpn()==null) { // noch keine PPN vorhanden, d.h. choosehits.do noch nicht durchlaufen
            			
            		GbvSruForm gsf = new GbvSruForm();
    				ArrayList<GbvSruForm> matches = new ArrayList<GbvSruForm>();
            		
    			// *** hier gibt es zwei Chancen ein GBV-Bestellobjekt zu erhalten: ZDB-ID und ISSN vorhanden
    				if (ck.isMinLength(of.getZdbid(), 2) && ck.isMinLength(of.getIssn(), 8)) {
    					try { // allfällige SRU-Fehler-Codes abfangen
            			matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("zdb",of.getZdbid(), 1)); // 1. Chance anhand der vorhandenen ZDB-ID
            			of.setGbvfield("zdb");
            			of.setGbvsearch(of.getZdbid());
    					} catch (MyException e) {
    						log.info("GBV-Message: " + e.getMessage());
    						Message m = new Message();
    						m.setMessage(e.getMessage());
    						rq.setAttribute("gbvmessage", m);
    					}
            			gsf = getGbvOrderObject(matches, of, t.getConnection());
            			if (gsf.getPpn_003AT()==null || gbvIsEjournal(gsf)) { // ...ist das Bestellobjekt da und kein E-Journal?
            				of.setZdbid(orderActionInstance.getZdbidFromIssn(of.getIssn(), t.getConnection())); // 2. Chance anhand der vorhandenen ISSN
            				if (of.getZdbid()!=null && !of.getZdbid().equals("")) { // hier kann es sein dass dbs keine zdbid liefert => ggf. Suche anhand ISSN
            				try { // allfällige SRU-Fehler-Codes abfangen
            				matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("zdb", of.getZdbid(), 1));
            				of.setGbvfield("zdb");
                			of.setGbvsearch(of.getZdbid());
                			gsf = getGbvOrderObject(matches, of, t.getConnection());
            				} catch (MyException e) {
            					log.info("GBV-Message: " + e.getMessage());
        						Message m = new Message();
        						m.setMessage(e.getMessage());
        						rq.setAttribute("gbvmessage", m);
        					}
            				}
            				if (gsf.getPpn_003AT()==null || gbvIsEjournal(gsf)) { // ...ist das Bestellobjekt da und kein E-Journal?
            					try { // allfällige SRU-Fehler-Codes abfangen
            					matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("iss", of.getIssn(), 1)); // 3. anhand der ISSN eine GBV-Suche machen
            					of.setGbvfield("iss");
                    			of.setGbvsearch(of.getIssn());
                    			gsf = getGbvOrderObject(matches, of, t.getConnection()); 
            					} catch (MyException e) {
            						log.info("GBV-Message: " + e.getMessage());
            						Message m = new Message();
            						m.setMessage(e.getMessage());
            						rq.setAttribute("gbvmessage", m);
            					}
            				}      				
            			}            			
        		// *** normaler Versuch GBV-Bestellobjekt zu erhalten
    				} else {
            			if (ck.isMinLength(of.getIssn(), 2)) { // es handelt sich um eine Zeitschrift
            			of.setZdbid(orderActionInstance.getZdbidFromIssn(of.getIssn(), t.getConnection()));
            			if (of.getZdbid()!=null && !of.getZdbid().equals("")) { // hier kann es sein dass dbs keine zdbid liefert => ggf. Suche anhand ISSN
            				try { // allfällige SRU-Fehler-Codes abfangen
            				matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("zdb", of.getZdbid(), 1));
            				of.setGbvfield("zdb");
                			of.setGbvsearch(of.getZdbid());
                			gsf = getGbvOrderObject(matches, of, t.getConnection());
	            			} catch (MyException e) {
	            				log.info("GBV-Message: " + e.getMessage());
	    						Message m = new Message();
	    						m.setMessage(e.getMessage());
	    						rq.setAttribute("gbvmessage", m);
	    					}
            				}
            			if (gsf.getPpn_003AT()==null || gbvIsEjournal(gsf)) { // ...ist das Bestellobjekt da und kein E-Journal?
            					try { // allfällige SRU-Fehler-Codes abfangen
            						matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("iss", of.getIssn(), 1));
            						of.setGbvfield("iss");
                        			of.setGbvsearch(of.getIssn());
                        			gsf = getGbvOrderObject(matches, of, t.getConnection());
            					} catch (MyException e) {
            						log.info("GBV-Message: " + e.getMessage());
    	    						Message m = new Message();
    	    						m.setMessage(e.getMessage());
    	    						rq.setAttribute("gbvmessage", m);
    	    					}
            				}
        					if (matches.size()==0 && ck.isMinLength(of.getZeitschriftentitel(), 2)) { // falls Bestimmung anhand ISSN fehlgeschlagen ist => Titelsuche
        						try { // allfällige SRU-Fehler-Codes abfangen	
        						// erster Versuch eng, als Phrase
                    			matches = getGbvMatches(getGbvSrucontentSearch("zti", of.getZeitschriftentitel(), 1));
                    			of.setGbvfield("zti");
                    			of.setGbvsearch(of.getZeitschriftentitel());
                    			// zweiter Versuch als Stichwort-Suche
                    			if (matches.size()==0) {
                    				matches = getGbvMatches(getGbvSrucontentSearch("sha", of.getZeitschriftentitel(), 1));
                    				of.setGbvfield("sha");
                        			of.setGbvsearch(of.getZeitschriftentitel());
                    			}
	        					} catch (MyException e) {
	        						log.info("GBV-Message: " + e.getMessage());
		    						Message m = new Message();
		    						m.setMessage(e.getMessage());
		    						rq.setAttribute("gbvmessage", m);
		    					}
	        					gsf = getGbvOrderObject(matches, of, t.getConnection());
        					}
            			} else {            				
            				if (of.getMediatype().equals("Teilkopie Buch")) {
            				if (ck.isMinLength(of.getIsbn(), 2)) { // Versuch anhand der ISBN GBV-Bestellobjekt zu erhalten
            					try { // allfällige SRU-Fehler-Codes abfangen
            					matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("isb",of.getIsbn(), 1));
            					of.setGbvfield("isb");
                    			of.setGbvsearch(of.getIsbn());
                    			gsf = getGbvOrderObject(matches, of, t.getConnection());
	            				} catch (MyException e) {
	            					log.info("GBV-Message: " + e.getMessage());
		    						Message m = new Message();
		    						m.setMessage(e.getMessage());
		    						rq.setAttribute("gbvmessage", m);
		    					}
                    		} else { // Versuch anhand des Titels ein GBV-Bestellobjekt zu erhalten
                    			try { // allfällige SRU-Fehler-Codes abfangen
                    			// erster Versuch eng, als Phrase
                    			matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("tit", of.getBuchtitel(), 1));
                    			of.setGbvfield("tit");
                    			of.setGbvsearch(of.getBuchtitel());
                    			// zweiter Versuch als Stichwort-Suche
                    			if (matches.size()==0) {
                    				matches = getGbvMatches(getGbvSrucontentSearch("tit", of.getBuchtitel(), 1));
                    				of.setGbvfield("tit");
                        			of.setGbvsearch(of.getBuchtitel());
                    			}
                    			// dritter Versuch über alle Wörter
                    			if (matches.size()==0) {
                    				matches = getGbvMatches(getGbvSrucontentSearch("all", of.getBuchtitel(), 1));
                    				of.setGbvfield("all");
                        			of.setGbvsearch(of.getBuchtitel());
                    			}
                    			} catch (MyException e) {
                    				log.info("GBV-Message: " + e.getMessage());
		    						Message m = new Message();
		    						m.setMessage(e.getMessage());
		    						rq.setAttribute("gbvmessage", m);
		    					}
                    			gsf = getGbvOrderObject(matches, of, t.getConnection());
                    		}
            				} else { // Titelsuche für Zeitschriften ohne ISSN
            					if (ck.isMinLength(of.getZeitschriftentitel(), 2)) {
            						try { // allfällige SRU-Fehler-Codes abfangen
            						// erster Versuch eng, als Phrase
                        			matches = getGbvMatches(getGbvSrucontentSearch("zti", of.getZeitschriftentitel(), 1));
                        			of.setGbvfield("zti");
                        			of.setGbvsearch(of.getZeitschriftentitel());
                        			// zweiter Versuch als Stichwort-Suche
                        			if (matches.size()==0) {
                        				matches = getGbvMatches(getGbvSrucontentSearch("sha", of.getZeitschriftentitel(), 1));
                        				of.setGbvfield("sha");
                            			of.setGbvsearch(of.getZeitschriftentitel());
                        			}
            						} catch (MyException e) {
            							log.info("GBV-Message: " + e.getMessage());
    		    						Message m = new Message();
    		    						m.setMessage(e.getMessage());
    		    						rq.setAttribute("gbvmessage", m);
    		    					}
            						gsf = getGbvOrderObject(matches, of, t.getConnection());
            					}            					
            				}            				
            			}
            		}
            					
    				// *** ein Bestellobjekt erhalten und nicht ein E-Journal => Bestellverarbeitung
            				if (gsf.getPpn_003AT()!=null && !gbvIsEjournal(gsf) &&
            					of.getMediatype().equals("Artikel")) { // bei Teilkopie Buch immer zuerst zur Trefferauswahl!
            					
            					if (of.isManuell()) { // manuelle Bestellung
            					forward = "redirect";
//            					of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
//            							  	ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
//            							  	"COPY%3FPPN%3D" + gsf.getPpn_003AT() + "%26LANGCODE%3DDU");
            					// bei Artikeln kann per OpenURL die Datenübergeben übergeben werden (bessere Übernahme der Angaben)
            					of.setLink("http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK&genre=article&issn=" + of.getIssn() +"&date=" + of.getJahr() +
            							   "&volume=" + of.getJahrgang() + "&issue=" + of.getHeft() + "&pages=" + of.getSeiten() + "&title=" + of.getZeitschriftentitel() +
            							   "&atitle=" + of.getArtikeltitel() + "&aulast=" + of.getAuthor());
            					
            					} else { // *** automatische Bestellung
            						of.setPpn(gsf.getPpn_003AT());
            						IllForm gbv = new IllForm();
            						gbv = illHandlerInstance.prepareGbvIllRequest(of, ui.getKonto(), ui);
            						String gbvanswer = illHandlerInstance.sendIllRequest(gbv, baseurl);
            						String return_value = illHandlerInstance.readGbvIllAnswer(gbvanswer);
            						forward = "ordersuccess";
            						if (gbvIsOrdernumber(gbvanswer)) { // autom. Bestellung erfolgreich
            							AbstractBenutzer kunde = new AbstractBenutzer();
            							kunde = kunde.getUser(new Long(of.getForuser()), t.getConnection());
            							of.setLieferant(lieferantenInstance.getLieferantFromName("GBV - Gemeinsamer Bibliotheksverbund", t.getConnection()));
            	                        of.setBestellquelle("GBV - Gemeinsamer Bibliotheksverbund"); // doppelter Eintrag um Sortieren und Suche zu ermöglichen
            	                        of.setStatus("bestellt");
            	                        of.setKaufpreis(new BigDecimal("0.00"));
            	                        of.setWaehrung("EUR");
            	                        of.setTrackingnr(gbv.getTransaction_group_qualifier());
            	                        of.setGbvnr(return_value);
            							Bestellungen b = new Bestellungen(of, kunde, ui.getKonto());
            							if (of.getBid()==null) {
            								b.save(t.getConnection());
            							} else {
            								b.setId(of.getBid());
            								b.update(t.getConnection());
            							}
            							if (b.getId()!=null) orderstate.setNewOrderState(b, ui.getKonto(), new Text(t.getConnection(),"bestellt"), null, ui.getBenutzer().getEmail(), t.getConnection()); // Status bestellt setzen wenn Bestellung gültige ID hat

            						} else {
            							ErrorMessage em = new ErrorMessage();
                                        em.setError("error.gbvorder");
                                        em.setError_specific(return_value);
                                        em.setLink("searchfree.do?activemenu=suchenbestellen");
                                        rq.setAttribute("errormessage", em);
                                        // Für die manuelle Bestellung
                                        of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
                							  		ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
                							  		"COPY%3FPPN%3D" + of.getPpn() + "%26LANGCODE%3DDU");                                        
                                        
                                        log.info("Failure GBV-Order: " + ui.getKonto().getBibliotheksname() + "\012" + return_value + "\012" + gbvanswer);
            						}
            					}
            		// *** keine (0), keine eindeutigen (>1) oder nur ein E-Journal (1) erhalten
            				} else {          					
            					if (matches.size()>0) { // Teilkopie oder E-Journal kann 1 sein
            						// Pfad auf Trefferauswahl
            						forward = "trefferauswahl";
            						of.setTreffer_total(matches.get(0).getTreffer_total());
            						int start = matches.get(0).getStart_record();
            						int max = matches.get(0).getMaximum_record();
            						if (of.getTreffer_total()-(start+max-1)>0) of.setForwrd(start+max);
            						if (start-max>0) of.setBack(start-max);
            						rq.setAttribute("matches", matches);          						
            					} else {
            					// Pfad auf Suche
            					forward = "search";
            					}
            				}
            				
            	// *** PPN bereits vorhanden => Bestellverarbeitung
            	} else {
            		
            		if (of.isManuell()) { // manuelle Bestellung
    					forward = "redirect";
    					if (!of.getMediatype().equals("Artikel")) { // Bei Büchern und Teilkopien...
    					of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
    							  	ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
    							  	"COPY%3FPPN%3D" + of.getPpn() + "%26LANGCODE%3DDU");
    					} else { // bei Artikeln
    						// bei Artikeln kann per OpenURL die Datenübergeben übergeben werden (bessere Übernahme der Angaben)
        				of.setLink("http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK&genre=article&issn=" + of.getIssn() +"&date=" + of.getJahr() +
        						   "&volume=" + of.getJahrgang() + "&issue=" + of.getHeft() + "&pages=" + of.getSeiten() + "&title=" + of.getZeitschriftentitel() +
        						   "&atitle=" + of.getArtikeltitel() + "&aulast=" + of.getAuthor());    						
    					}
    					} else { // *** automatische Bestellung
    						IllForm gbv = new IllForm();
    						gbv = illHandlerInstance.prepareGbvIllRequest(of, ui.getKonto(), ui);
    						String gbvanswer = illHandlerInstance.sendIllRequest(gbv, baseurl);
    						String return_value = illHandlerInstance.readGbvIllAnswer(gbvanswer);
    						forward = "ordersuccess";
    						if (gbvIsOrdernumber(gbvanswer)) { // autom. Bestellung erfolgreich
    							AbstractBenutzer kunde = new AbstractBenutzer();
    							kunde = kunde.getUser(new Long(of.getForuser()), t.getConnection());
    							of.setLieferant(lieferantenInstance.getLieferantFromName("GBV - Gemeinsamer Bibliotheksverbund", t.getConnection()));
    	                        of.setBestellquelle("GBV - Gemeinsamer Bibliotheksverbund"); // doppelter Eintrag um Sortieren und Suche zu ermöglichen
    	                        of.setStatus("bestellt");
    	                        of.setKaufpreis(new BigDecimal("0.00"));
    	                        of.setWaehrung("EUR");
    	                        of.setTrackingnr(gbv.getTransaction_group_qualifier());
    	                        of.setGbvnr(return_value);
    							Bestellungen b = new Bestellungen(of, kunde, ui.getKonto());
    							if (of.getBid()==null) {
    								b.save(t.getConnection());
    							} else {
    								b.setId(of.getBid());
    								b.update(t.getConnection());
    							}
    							if (b.getId()!=null) orderstate.setNewOrderState(b, ui.getKonto(), new Text(t.getConnection(),"bestellt"), null, ui.getBenutzer().getEmail(), t.getConnection()); // Status bestellt setzen wenn Bestellung gültige ID hat

    						} else {
    							ErrorMessage em = new ErrorMessage();
    							em.setError("error.gbvorder");
                                em.setError_specific(return_value);
                                em.setLink("searchfree.do?activemenu=suchenbestellen");
                                rq.setAttribute("errormessage", em);                             
                                // Für die manuelle Bestellung
                                of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
        							  		ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
        							  		"COPY%3FPPN%3D" + of.getPpn() + "%26LANGCODE%3DDU");
                                
                                log.info("Failure GBV-Order: " + ui.getKonto().getBibliotheksname() + "\012" + return_value + "\012" + gbvanswer);
    						}
    					}
            		
            	}
            		
            	// *** Bestelltyp Loan
            	} else {
            		
            		if (of.getPpn()==null) { // noch keine PPN vorhanden, d.h. choosehits.do noch nicht durchlaufen
            			
            			GbvSruForm gsf = new GbvSruForm();
        				ArrayList<GbvSruForm> matches = new ArrayList<GbvSruForm>();
                		
        				if (ck.isMinLength(of.getIsbn(), 2)) { // Versuch anhand der ISBN GBV-Bestellobjekt zu erhalten
        					try { // allfällige SRU-Fehler-Codes abfangen
        					matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("isb",of.getIsbn(), 1));
        					of.setGbvfield("isb");
                			of.setGbvsearch(of.getIsbn());
	        				} catch (MyException e) {
	        					log.info("GBV-Message: " + e.getMessage());
	    						Message m = new Message();
	    						m.setMessage(e.getMessage());
	    						rq.setAttribute("gbvmessage", m);
	    					}
                			gsf = getGbvOrderObject(matches, of, t.getConnection());         			
                		} else { // Versuch anhand des Titels ein GBV-Bestellobjekt zu erhalten
                			try { // allfällige SRU-Fehler-Codes abfangen
                			// erster Versuch eng, als Phrase
                			matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("tit", of.getBuchtitel(), 1));
                			of.setGbvfield("tit");
                			of.setGbvsearch(of.getBuchtitel());
                			// zweiter Versuch als Stichwort-Suche
                			if (matches.size()==0) {
                				matches = getGbvMatches(getGbvSrucontentSearch("tit", of.getBuchtitel(), 1));
                				of.setGbvfield("tit");
                    			of.setGbvsearch(of.getBuchtitel());
                			}
                			// dritter Versuch über alle Wörter
                			if (matches.size()==0) {
                				matches = getGbvMatches(getGbvSrucontentSearch("all", of.getBuchtitel(), 1));
                				of.setGbvfield("all");
                    			of.setGbvsearch(of.getBuchtitel());
                			}
                			} catch (MyException e) {
                				log.info("GBV-Message: " + e.getMessage());
        						Message m = new Message();
        						m.setMessage(e.getMessage());
        						rq.setAttribute("gbvmessage", m);
        					}
            				gsf = getGbvOrderObject(matches, of, t.getConnection());
                		}
                					
        			// *** ein Loan-Bestellobjekt erhalten => Bestellverarbeitung		
        				if (gsf.getPpn_003AT()!=null) {
                					
                					if (of.isManuell()) { // manuelle Bestellung
                					forward = "redirect";
                					of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
                							  	ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
                							  	"LOAN%3FPPN%3D" + gsf.getPpn_003AT() + "%26LANGCODE%3DDU");
                					} else { // *** Trefferauswahl vor automatischer Bestellung bei Büchern
                						// Hier wird auf jeden Fall Choosehits durchlaufen, da die Angaben sonst zu ungenau sind!!!
                						// Pfad auf Trefferauswahl
                						forward = "trefferauswahl";
                						of.setTreffer_total(matches.get(0).getTreffer_total());
                						int start = matches.get(0).getStart_record();
                						int max = matches.get(0).getMaximum_record();
                						if (of.getTreffer_total()-(start+max-1)>0) of.setForwrd(start+max);
                						if (start-max>0) of.setBack(start-max);
                						rq.setAttribute("matches", matches);
                					}
                	// *** keine (0), keine eindeutigen (>1)		
        				} else {          					
                					if (matches.size()>1) {
                						// Pfad auf Trefferauswahl
                						forward = "trefferauswahl";
                						of.setTreffer_total(matches.get(0).getTreffer_total());
                						int start = matches.get(0).getStart_record();
                						int max = matches.get(0).getMaximum_record();
                						if (of.getTreffer_total()-(start+max-1)>0) of.setForwrd(start+max);
                						if (start-max>0) of.setBack(start-max);
                						rq.setAttribute("matches", matches);            						
                					} else {
                					// Pfad auf Suche
                					forward = "search";
                					}
                				}
        			// *** PPN vorhanden => Bestellverarbeitung Loan
            		} else {
            			
            			if (of.isManuell()) { // manuelle Bestellung
        					forward = "redirect";
        					of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
        							  	ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
        							  	"LOAN%3FPPN%3D" + of.getPpn() + "%26LANGCODE%3DDU");
        					} else { // *** automatische Bestellung
        						IllForm gbv = new IllForm();
        						gbv = illHandlerInstance.prepareGbvIllRequest(of, ui.getKonto(), ui);
        						String gbvanswer = illHandlerInstance.sendIllRequest(gbv, baseurl);
        						String return_value = illHandlerInstance.readGbvIllAnswer(gbvanswer);
        						forward = "ordersuccess";
        						if (gbvIsOrdernumber(gbvanswer)) { // autom. Bestellung erfolgreich
        							AbstractBenutzer kunde = new AbstractBenutzer();
        							kunde = kunde.getUser(new Long(of.getForuser()), t.getConnection());
        							of.setLieferant(lieferantenInstance.getLieferantFromName("GBV - Gemeinsamer Bibliotheksverbund", t.getConnection()));
        	                        of.setBestellquelle("GBV - Gemeinsamer Bibliotheksverbund"); // doppelter Eintrag um Sortieren und Suche zu ermöglichen
        	                        of.setStatus("bestellt");
        	                        of.setKaufpreis(new BigDecimal("0.00"));
        	                        of.setWaehrung("EUR");
        	                        of.setTrackingnr(gbv.getTransaction_group_qualifier());
        	                        of.setGbvnr(return_value);
        							Bestellungen b = new Bestellungen(of, kunde, ui.getKonto());
        							if (of.getBid()==null) {
        								b.save(t.getConnection());
        							} else {
        								b.setId(of.getBid());
        								b.update(t.getConnection());
        							}
        							if (b.getId()!=null) orderstate.setNewOrderState(b, ui.getKonto(), new Text(t.getConnection(),"bestellt"), null, ui.getBenutzer().getEmail(), t.getConnection()); // Status bestellt setzen wenn Bestellung gültige ID hat

        						} else {
        							ErrorMessage em = new ErrorMessage();
        							em.setError("error.gbvorder");
                                    em.setError_specific(return_value);
                                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                                    rq.setAttribute("errormessage", em);
                                    // Für die manuelle Bestellung
                                    of.setLink("http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" + ui.getKonto().getGbvbenutzername() + "&PASSWORD=" +
            							  		ui.getKonto().getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2F" +
            							  		"COPY%3FPPN%3D" + of.getPpn() + "%26LANGCODE%3DDU");
                                    
                                    log.info("Failure GBV-Order: " + ui.getKonto().getBibliotheksname() + "\012" + return_value + "\012" + gbvanswer);
        						}
        					}
            			
            		}
            		
            	}

            } catch (Exception e) {
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("order: " + e.toString());
            }
        
         } else {
         	of.setIssn(null); // naja, unterdrücken von "manuell bestellen"...
         	rq.setAttribute("orderform", of);
 			ErrorMessage em = new ErrorMessage("error.maxorder", "searchfree.do");
 			rq.setAttribute("errormessage", em);
         }
         } else {
         	of.setIssn(null); // naja, unterdrücken von "manuell bestellen"...
         	rq.setAttribute("orderform", of);
 			ErrorMessage em = new ErrorMessage("error.maxorderyear", "searchfree.do");
 			rq.setAttribute("errormessage", em);
         }
         } else {
         	of.setIssn(null); // naja, unterdrücken von "manuell bestellen"...
         	rq.setAttribute("orderform", of);
 			ErrorMessage em = new ErrorMessage("error.inactive", "searchfree.do");
 			rq.setAttribute("errormessage", em);
         }
         
         } else { // GBV-Suche
        	 forward = "search";
         }
         
         
         } else {
        	 ActiveMenusForm mf = new ActiveMenusForm();
             mf.setActivemenu("login");
             rq.setAttribute("ActiveMenus", mf);
             ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
             rq.setAttribute("errormessage", em);
        }
        
        of.setSubmit("GBV");
        //TODO: saubere Preisverwaltung
        of.setPreisvorkomma("0");
        of.setPreisnachkomma("00");
        of.setWaehrung("EUR");
        
     // für Get-Methode in PrepareLogin of URL-codieren
        of = of.encodeOrderForm(of);
        
        rq.setAttribute("orderform", of);
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        t.close();
        
        return mp.findForward(forward);

    }
	
	/**
	 * führt eine GBV-Suche aus und holt die Treffer
	 */
	public ActionForward search(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		OrderForm of = (OrderForm) fm;
		Auth auth = new Auth();

        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird        
        if (auth.isLogin(rq)) {
            
            try {
            	
            	Check ck = new Check();
            	int start_record = 1;
            	if (of.getBack()!=0) start_record = of.getBack(); // in der Anzeige wurde zurück geblättert
            	if (of.getForwrd()!=0) start_record = of.getForwrd(); // in der Anzeige wurde vorwärts geblättert
            	of.setBack(0); // zurücksetzen für korrkete Anzeige
            	of.setForwrd(0); // // zurücksetzen für korrkete Anzeige
            	
            	if (ck.isMinLength(of.getGbvsearch(), 1) && ck.isMinLength(of.getGbvfield(), 3) ) { // Eingabe erfolgt
            		
            		try { // allfällige SRU-Fehler-Codes abfangen
            		// erste Suche eng als Phrase
            		ArrayList<GbvSruForm> matches = getGbvMatches(getGbvSrucontentSearchAsPhrase(of.getGbvfield(), of.getGbvsearch(), start_record));
            		// zweite Suche als Stichwörter
            		if (matches.size()==0)matches = getGbvMatches(getGbvSrucontentSearch(of.getGbvfield(), of.getGbvsearch(), start_record));
            		
            		if (matches.size()>0) {
						// Pfad auf Trefferauswahl
						forward = "trefferauswahl";
						of.setTreffer_total(matches.get(0).getTreffer_total());
						int start = matches.get(0).getStart_record();
						int max = matches.get(0).getMaximum_record();
						if (of.getTreffer_total()-(start+max-1)>0) of.setForwrd(start+max);
						if (start-max>0) of.setBack(start-max);
						rq.setAttribute("matches", matches);            						
					} else {
					// Pfad auf Suche
					forward = "search";
					Message m = new Message();
					m.setMessage("Keine Treffer!");
					rq.setAttribute("gbvmessage", m);
					}
            		
            		} catch (MyException e) {
            			log.info("GBV-Message: " + e.getMessage());
            			forward = "search";
						Message m = new Message();
						m.setMessage(e.getMessage());
						rq.setAttribute("gbvmessage", m);
					}
            		
            	} else { // keine Eingabe erfolgt => zurück zur Suche
            		forward = "search";
            		Message m = new Message();
					m.setMessage("Keine Treffer!");
					rq.setAttribute("gbvmessage", m);
            	}

            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("search: " + e.toString());
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        of.setSubmit("GBV");
        rq.setAttribute("orderform", of);
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        return mp.findForward(forward);

    }
	
	/**
	 * validiert die Mussfelder
	 */
	public ActionForward validate(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		OrderForm of = (OrderForm) fm;
		Auth auth = new Auth();

        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird        
        if (auth.isLogin(rq)) {
            
        	Texttyp t = new Texttyp();
            try {
            	
            	if (!of.getSubmit().equals("GBV-Suche")) {
            	
            	if (!gbvOrderValues(of)) {
            	
            	forward = "missingvalues";
                Message m = new Message("Fehlende Mussfelder oder zuwenig Angaben für eine Bestellung.");
                rq.setAttribute("gbvmessage", m);
            	
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
                
                if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                	List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                	AbstractBenutzer b = ui.getBenutzer();
                	kontouser.add(b);
                	of.setKontouser(kontouser);
                } else {
                	of.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), t.getConnection()));
                }
                
            	} else {
            		forward = "success";
            	}
            	
            	} else { // zur GBV-Suche
            		forward = "search";            		
            	}
            	
            	of.setSubmit("GBV");
                rq.setAttribute("orderform", of);

            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("validate: " + e.toString());

            } finally {
            	t.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        return mp.findForward(forward);

    }
	
	
    /**
     * Ändert den angezeigten Medientyp für eine GBV-Bestellung.
     * 
     */
    public ActionForward changeMediatypeGbv(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	String forward = "failure";
    	OrderForm of = (OrderForm) form;
    	Auth auth = new Auth();

        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird        
        if (auth.isLogin(rq)) {
        	
            forward = "success";
            Texttyp t = new Texttyp();
            try {
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
                
                if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                	List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                	AbstractBenutzer b = ui.getBenutzer();
                	kontouser.add(b);
                	of.setKontouser(kontouser);
                } else {
                	of.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), t.getConnection()));
                }
                
              
                if (of.getMediatype()==null) of.setMediatype("Artikel"); // Defaultwert Artikel
                if (of.getMediatype().equals("Buch")) {
                	of.setDeloptions("post"); // logische Konsequenz
                	of.setFileformat("Papierkopie"); // logische Konsequenz
                }                

                if (ui.getKonto().getIsil()==null || ui.getKonto().getGbvrequesterid()==null) {of.setManuell(true);} else {of.setManuell(false);} // ISIL und requester-id werden für autom. Bestellung benötigt
                of.setSubmit("GBV");
                of.setMaximum_cost("8");

                rq.setAttribute("orderform", of);

            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("changeMediaType: " + e.toString());
            } finally {
            	t.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        return mp.findForward(forward);
    }
    
    /**
     * Holt ein bestellbares GBV-Objekt (Zeitschrift, Buch, Film etc.)
     * 
     * @param OrderForm of
     * @return GbvSruForm
     */
    private GbvSruForm getGbvOrderObject(ArrayList<GbvSruForm> matches, OrderForm of, Connection cn){
    	
    	GbvSruForm gsf = new GbvSruForm();
    	
    	try {
    		
    		if (of.getMediatype().equals("Artikel")) {    			
    				if (matches.size()==1) {
    					gsf = (GbvSruForm) matches.get(0);    					
    					// prüft, ob der Treffer ein E-Journal ist und holt ggf. die betreffende Print-Ausgabe
    					if (gbvIsEjournalRedirectable(gsf)) {    						
    						matches = getGbvMatches(getGbvSrucontentSearchAsPhrase("zdb",gsf.getVerknuepfung_zdbid_horizontal(), 1));
    						if (matches.size()==1) gsf = (GbvSruForm) matches.get(0); // falls kein Print-J vorhanden, wird E-J zurückgegeben. Es folgt aber eine erneute Prüfung...  						
    					}
    				}
    			return gsf;
    		}
    		if (of.getMediatype().equals("Teilkopie Buch")) {
    			if (matches.size()==1) {
					gsf = (GbvSruForm) matches.get(0);
				}
    			return gsf;
    		}
    		if (of.getMediatype().equals("Buch")) {
    			if (matches.size()==1) {
					gsf = (GbvSruForm) matches.get(0);
				}
    			return gsf;
    		}
    	
    	} catch(Exception e){
    		log.error("getGbvOrderObject(OrderForm of): " + e.getMessage());
        }
    	
        return gsf;
    }
    
    /**
     * Liest die Treffer aus einem SRU-content und gibt sie in einem Array als Objekte zurück.
     * 
     * @param String content
     * @return ArrayList<GbvSruForm>
     */
    private static ArrayList<GbvSruForm> getGbvMatches(String content) throws MyException {
    	
    	ArrayList<GbvSruForm> matches = new ArrayList<GbvSruForm>();
    	int treffer = 0;
    	int start_record = 0;
    	int maximum_record = 0;
    	
    	try {
    	
    	if (content.contains("<srw:numberOfRecords>")) {
    		// kracht bewusst, wenn <srw:numberOfRecords></srw:numberOfRecords> gar keine Trefferangaben enthält. Siehe catch
    		treffer = Integer.valueOf(content.substring(content.indexOf("<srw:numberOfRecords>")+21, content.indexOf("</srw:numberOfRecords>")));
    		start_record = Integer.valueOf(content.substring(content.indexOf("<startRecord>")+13, content.indexOf("</startRecord>")));
    		maximum_record = Integer.valueOf(content.substring(content.indexOf("<maximumRecords>")+16, content.indexOf("</maximumRecords>")));
    		
    		while (content.contains("<srw:record>")) {
    			
    			GbvSruForm gsf = readSruRecord(content.substring(content.indexOf("<srw:record>"), content.indexOf("</srw:record>")));
        		gsf.setTreffer_total(treffer);
        		gsf.setStart_record(start_record);
        		gsf.setMaximum_record(maximum_record);
        		// rechne
        		int i = matches.size();
        		gsf.setRecord_number(start_record+i);
        		matches.add(gsf);
        		content = content.substring(content.indexOf("</srw:record>")+12);
    		}    		
    		
    	}
    	
    	} catch(Exception e){
    		log.error("getGbvMatches: " + e.getMessage() + "\012" + content);
	        
	        // Hier wird ein allfälliger SRU-Fehler-Code gelesen um auf der JSP angezeigt zu werden
	        // z.B. "GBV: System temporarily unavailable" (s. Liste: http://www.loc.gov/standards/sru/resources/diagnostics-list.html)
	        if (content!=null && content.contains("<diag:message>")) {	        	
	        	String error = getSruErrorCode(content);
	        	throw new MyException("GBV:\040" + error);
	        }
	        
        }
    	

        return matches;
    }
    
    /**
     * Liest einen einzelnen Record (nur wichtigere Felder) aus einem SRU-content und gibt ihn als GbvSruRecordForm zurück.
     * 
     * @param String content
     * @return GbvSruForm
     */
    private static GbvSruForm readSruRecord(String content){
    	
    	GbvSruForm record = new GbvSruForm();
    	ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
    	
    	try {
    		
    		content = content.replaceAll("\012", ""); // Umbrüche entfernen
    	
    	if (content.contains("<datafield tag=\"003@\">")) record.setPpn_003AT(getSruDatafield("003@", content));
//    	if (content.contains("<datafield tag=\"002@\">")) record.setTyp_002AT(getSruDatafield("002@", content)); // wird unten erledigt
//    	if (content.contains("<datafield tag=\"011@\">")) record.setErscheinungsjahr_011AT(getSruDatafield("011@", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"019@\">")) record.setErscheinungsland_019AT(getSruDatafield("019@", content));
    	if (content.contains("<datafield tag=\"031@\">")) record.setErscheinungsverlauf_031AT(getSruDatafield("031@", content));
//    	if (content.contains("<datafield tag=\"031A\">")) record.setUmfang_031A(getSruDatafield("031A", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"031N\">")) record.setErscheinungsverlauf_normiert_031N(getSruDatafield("031N", content));
    	if (content.contains("<datafield tag=\"032@\">")) record.setAusgabe_032AT(getSruDatafield("032@", content));
    	if (content.contains("<datafield tag=\"021V\">")) record.setBand_021V_multipel(getSruDatafield("021V", content));
    	if (content.contains("<datafield tag=\"034K\">")) record.setBegleitmaterial_034K(getSruDatafield("034K", content));
    	if (content.contains("<datafield tag=\"006T\">")) record.setCip_num_db_006T_multipel(getSruDatafield("006T", content));
    	if (content.contains("<datafield tag=\"006G\">")) record.setDnb_num_006G(getSruDatafield("006G", content));
    	if (content.contains("<datafield tag=\"004V\">")) record.setDoi_004V_multipel(getSruDatafield("004V", content));
    	if (content.contains("<datafield tag=\"004L\">")) record.setEan_004L_multipel(getSruDatafield("004L", content));
    	if (content.contains("<datafield tag=\"046Q\">")) record.setEnthaltene_werke_046Q(getSruDatafield("046Q", content));
    	if (content.contains("<datafield tag=\"034I\">")) record.setFormat_034I(getSruDatafield("034I", content));
    	if (content.contains("<datafield tag=\"037A\">")) record.setFussnoten_unaufgegliedert_037A_multipel(getSruDatafield("037A", content));
    	if (content.contains("<datafield tag=\"028F\">")) record.setGefeierte_personen_028F(getSruDatafield("028F", content));
//    	if (content.contains("<datafield tag=\"036C\">")) record.setGesamtheit_abteilungen_vorlage_036C(getSruDatafield("036C", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"036D\">")) record.setGesamtheit_ansetzungsform_036D(getSruDatafield("036D", content));
    	if (content.contains("<datafield tag=\"036F\">")) record.setGesamtheit_ansetzungsform_036F_multipel(getSruDatafield("036F", content));
    	if (content.contains("<datafield tag=\"036E\">")) record.setGesamtheit_vorlage_036E_multipel(getSruDatafield("036E", content));
//    	if (content.contains("<datafield tag=\"021A\">")) record.setHauptsachtitel_021A(getSruDatafield("021A", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"021B\">")) record.setHauptsachtitel_alternativ_021B(getSruDatafield("021B", content));
    	if (content.contains("<datafield tag=\"007E\">")) record.setHochschulschriften_num_007E(getSruDatafield("007E", content));
//    	if (content.contains("<datafield tag=\"037C\">")) record.setHochschulschriftenvermerk_037C_multipel(getSruDatafield("037C", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"034M\">")) record.setIllustration_034M(getSruDatafield("034M", content));
    	if (content.contains("<datafield tag=\"028E\">")) record.setInterpreten_028E_multipel(getSruDatafield("028E", content));
    	if (content.contains("<datafield tag=\"004A\">")) record.setIsbn_004A_multipel(getSruDatafield("004A", content));
    	if (content.contains("<datafield tag=\"004F\">")) record.setIsmn_004F(getSruDatafield("004F", content));
    	if (content.contains("<datafield tag=\"005A\">")) record.setIssn_005A_multipel(convertOpenUrlInstance.normalizeIssn(getSruDatafield("005A", content)));
    	if (content.contains("<datafield tag=\"029E\">")) record.setKoerperschaft_als_interpret_029E_multipel(getSruDatafield("029E", content));
//    	if (content.contains("<datafield tag=\"029A\">")) record.setKoerperschaft_erste_029A(getSruDatafield("029A", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"029K\">")) record.setKoerperschaft_sonstige_029K_multipel(getSruDatafield("029K", content));
    	if (content.contains("<datafield tag=\"029F\">")) record.setKoerperschaft_zweite_029F(getSruDatafield("029F", content));
    	if (content.contains("<datafield tag=\"030F\">")) record.setKongresse_030F(getSruDatafield("030F", content));
    	if (content.contains("<datafield tag=\"006A\">")) record.setLoc_num_006A(getSruDatafield("006A", content));
    	if (content.contains("<datafield tag=\"016H\">")) record.setMaterialbenennung_allg_016H(getSruDatafield("016H", content));
    	if (content.contains("<datafield tag=\"034D\">")) record.setMaterialbenennung_spezifisch_034D(getSruDatafield("034D", content));
//    	if (content.contains("<datafield tag=\"033A\">")) record.setOrt_verlag_033A_multipel(getSruDatafield("033A", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"032B\">")) record.setReprintvermerk_032B(getSruDatafield("032B", content));
    	if (content.contains("<datafield tag=\"010@\">")) record.setSprache_010AT(getSruDatafield("010@", content));
    	if (content.contains("<datafield tag=\"021C\">")) record.setUnterreihe_021C_multipel(getSruDatafield("021C", content));
//    	if (content.contains("<datafield tag=\"028A\">")) record.setVerfasser_erster_028A(getSruDatafield("028A", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"028B\">")) record.setVerfasser_zweiter_und_weitere_028B(getSruDatafield("028B", content));
//    	if (content.contains("<datafield tag=\"039B\">")) record.setVerknuepfung_groessere_einheit_039B_multipel(getSruDatafield("039B", content)); // wird unten erledigt
//    	if (content.contains("<datafield tag=\"039D\">")) record.setVerknuepfung_horizontal_039D_multipel(getSruDatafield("039D", content)); // wird unten erledigt
    	if (content.contains("<datafield tag=\"039C\">")) record.setVerknuepfung_kleinere_einheit_039C_multipel(getSruDatafield("039C", content));
    	if (content.contains("<datafield tag=\"006Z\">")) record.setZdbid_006Z_multipel(getSruDatafield("006Z", content));
    	if (content.contains("<datafield tag=\"021N\">")) record.setZusaetze_und_verfasser_021N(getSruDatafield("021N", content));
    	if (content.contains("<datafield tag=\"021M\">")) record.setZusatzwerk_021M_multipel(getSruDatafield("021M", content));
    	if (content.contains("<datafield tag=\"021G\">")) record.setRezensierteswerk_021G_multipel(getSruDatafield("021G", content));
    	
    	// hier folgen wichtige Subfields    	
    	
    	// kann mehrere Verweise enthalten... 
    	// grundsätzlich wird der letzte Verweise genommen, in der GbvOrderAction, aber die manuelle Wahl gegeben bei anzahl039D > 1
    	if (content.contains("<datafield tag=\"039D\">")) {
    		String work = content; // Kopie für String Verkürzung
    		while (work.contains("<datafield tag=\"039D\">")) {
    		String content_copy = work.substring(work.indexOf("<datafield tag=\"039D\">")+22, work.indexOf("</datafield>", work.indexOf("<datafield tag=\"039D\">")));
    		String titel = getSruSubfield("a", content_copy);
    		String vortext = getSruSubfield("c", content_copy);
    		String ident = getSruSubfield("C", content_copy); // Identifier für Querverweis, normalerweise ZDB
    		String zdbid = getSruSubfield("6", content_copy); // in der Doku eigentlich Code 7, empirisch aber Code 6
    		String titel_2 = getSruSubfield("8", content_copy); // kommt vermutlich nicht bis selten vor
    		String ppn = getSruSubfield("9", content_copy);
    		String text = "";
    		if(!vortext.equals("")) text = vortext + "\040";
    		text = text + titel + titel_2;
    		record.setVerknuepfung_horizontal_039D_multipel(text);
    		if (!ppn.equals("")) record.setVerknuepfung_ppn_horizontal(ppn);
    		if (!zdbid.equals("") && ident.equals("ZDB")) record.setVerknuepfung_zdbid_horizontal(zdbid);
    		if (record.getVerknuepfung_horizontal_039D_multipel().equals("")) record.setVerknuepfung_horizontal_039D_multipel(null);  // Anzeigelogik
    		work = work.substring(work.indexOf("<datafield tag=\"039D\">")+17); // Verkürzung
    		record.setAnzahl_039D(record.getAnzahl_039D()+1);
    		}
    	}
    	if (content.contains("<datafield tag=\"039B\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"039B\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"039B\">")));
    		String text_sammelw = getSruSubfield("a", content_copy); // fortlaufende Sammelwerke
    		String zdbid = getSruSubfield("7", content_copy);
    		String vortext = getSruSubfield("c", content_copy);
    		String text_unselbst = getSruSubfield("8", content_copy); // unselbständige Werke
    		String ppn = getSruSubfield("9", content_copy);
    		String text = "";
    		if(!vortext.equals("")) text = vortext + "\040";
    		text = text + text_sammelw + text_unselbst;
    		record.setVerknuepfung_groessere_einheit_039B_multipel(text);
    		if (!ppn.equals("")) record.setVerknuepfung_ppn_groesser(ppn);
    		if (!zdbid.equals("")) record.setVerknuepfung_zdbid_groesser(zdbid);
    		if (record.getVerknuepfung_groessere_einheit_039B_multipel().equals("")) record.setVerknuepfung_groessere_einheit_039B_multipel(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"031A\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"031A\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"031A\">")));
    		String band = getSruSubfield("d", content_copy);
    		String jahr = getSruSubfield("j", content_copy);
    		String heft = getSruSubfield("e", content_copy);
//    		String sonderheft = getSruSubfield("f", content_copy);
    		String seiten = getSruSubfield("h", content_copy);
    		String text = jahr;
    		if (!band.equals("")) {
    			if (!jahr.equals("")) {text = text + ";" + band;} else {text = text + band;}
    		}
    		if (!heft.equals("")) text = text + "(" + heft + ")";
    		if (!seiten.equals("")) text = text + ":" + seiten;
    		record.setUmfang_031A(text);
    		if (record.getUmfang_031A().equals("")) record.setUmfang_031A(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"036C\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"036C\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"036C\">")));
    		String sachtitel = getSruSubfield("a", content_copy);
    		String zusatz = getSruSubfield("y", content_copy);
    		String band = getSruSubfield("l", content_copy);
    		if (!zusatz.equals("") && !sachtitel.equals("")) sachtitel = sachtitel + "\040:\040" + zusatz;
    		if (!band.equals("")) record.setBandzaehlung(band + ":\040");
    		record.setGesamtheit_abteilungen_vorlage_036C(sachtitel);
    		if (record.getGesamtheit_abteilungen_vorlage_036C().equals("")) record.setGesamtheit_abteilungen_vorlage_036C(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"021A\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"021A\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"021A\">")));
    		String titel = getSruSubfield("a", content_copy);
    		String untertitel = getSruSubfield("d", content_copy);
    		String anmerkungen = getSruSubfield("h", content_copy);
    		if (!untertitel.equals("") && !titel.equals("")) titel = titel + "\040:\040" + untertitel;
    		if (!anmerkungen.equals("") && !(titel).equals("")) titel = titel + "\040" + anmerkungen;
    		record.setHauptsachtitel_021A(titel);
    		if (record.getHauptsachtitel_021A().equals("")) record.setHauptsachtitel_021A(null); // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"021G\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"021G\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"021G\">")));
    		String titel = getSruSubfield("a", content_copy);
    		String verfasser = getSruSubfield("g", content_copy); // ...des Originals
    		String ausgabebezeichnung = getSruSubfield("d", content_copy);
    		String rezensor = getSruSubfield("h", content_copy); // rezensierende Person
    		String rezension = verfasser + ",\040" + titel + "\040:\040" + ausgabebezeichnung + "\040/\040" + rezensor;
    		record.setRezensierteswerk_021G_multipel(rezension);
    		if (record.getRezensierteswerk_021G_multipel().equals("")) record.setRezensierteswerk_021G_multipel(null); // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"033A\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"033A\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"033A\">")));
    		String ort = getSruSubfield("p", content_copy);
    		String verlag = getSruSubfield("n", content_copy);
    		if (!verlag.equals("") && !ort.equals("")) verlag = "\040:\040" + verlag;
    		record.setOrt_verlag_033A_multipel(ort + verlag);
    		if (record.getOrt_verlag_033A_multipel().equals("")) record.setOrt_verlag_033A_multipel(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"011@\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"011@\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"011@\">")));
    		String jahr = getSruSubfield("a", content_copy);
    		String serie = getSruSubfield("n", content_copy);
    		if (!serie.equals("")) jahr = serie; // falls Verlauf vorhanden wird Verlauf genommen, sonst Jahr
    		record.setErscheinungsjahr_011AT(jahr);
    		if (record.getErscheinungsjahr_011AT().equals("")) record.setErscheinungsjahr_011AT(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"028A\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"028A\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"028A\">")));
    		String name = "";
    		String nachname = getSruSubfield("a", content_copy);
    		String praefix = getSruSubfield("c", content_copy);
    		String vorname = getSruSubfield("d", content_copy);
    		String zusatz_vorname = getSruSubfield("e", content_copy);
    		String zusatz_name = getSruSubfield("f", content_copy);
    		String lebensdaten = getSruSubfield("h", content_copy);
    		String ident = getSruSubfield("p", content_copy); // weitere identifizierende Angaben
    		if (!praefix.equals("")) name = praefix + "\040";
    		if (!zusatz_vorname.equals("")) name = name + zusatz_vorname + "\040";
    		if (!vorname.equals("")) name = name + vorname + "\040";
    		if (!zusatz_name.equals("")) name = name + zusatz_name + "\040";
    		if (!nachname.equals("")) name = name + nachname;
    		if (!ident.equals("")) name = name + "\040" + ident;
    		if (!lebensdaten.equals("")) name = name + "\040" + lebensdaten;    		
    		record.setVerfasser_erster_028A(name);
    		if (record.getVerfasser_erster_028A().equals("")) record.setVerfasser_erster_028A(null); // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"029A\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"029A\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"029A\">")));
    		String koerperschaft = getSruSubfield("8", content_copy);
    		record.setKoerperschaft_erste_029A(koerperschaft);
    		if (record.getKoerperschaft_erste_029A().equals("")) record.setKoerperschaft_erste_029A(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"037C\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"037C\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"037C\">")));
    		String ort = getSruSubfield("b", content_copy);
    		String vermerk = getSruSubfield("a", content_copy);
    		if (!vermerk.equals("") && !ort.equals("")) vermerk = ",\040" + vermerk;
    		record.setHochschulschriftenvermerk_037C_multipel(ort + vermerk);
    		if (record.getHochschulschriftenvermerk_037C_multipel().equals("")) record.setHochschulschriftenvermerk_037C_multipel(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"002@\">")) {
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"002@\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"002@\">")));
    		String code = getSruSubfield("0", content_copy);
    		code = resolveGbvMediatype(code);
    		record.setTyp_002AT(code);
    		if (record.getTyp_002AT().equals("")) record.setTyp_002AT(null);  // Anzeigelogik
    	}
    	if (content.contains("<datafield tag=\"009P\"")) {
    		String content_verk = content;
    		while (content_verk.contains("<datafield tag=\"009P\"")) {
    		String content_copy = content_verk.substring(content_verk.indexOf("<datafield tag=\"009P\"")+22, content_verk.indexOf("</datafield>", content_verk.indexOf("<datafield tag=\"009P\"")));
    		String license = getSruSubfield("S", content_copy);
    		if (license.equals("1") && content_copy.contains("<subfield code=\"a\">")) { // 1 == frei zugänglich ; 0 == kostenpflichtig
    		String url = getSruSubfield("a", content_copy);
//    		String urn = getSruSubfield("g", content_copy); // momentan nicht aktiviert, da Auflösung unklar...
    		record.setLink_009P_multiple(url);
    		}
    		content_verk = content_verk.substring(content_verk.indexOf("<datafield tag=\"009P\"")+20);
    		}
    	}
    	
    	
    	} catch(Exception e){
    		log.error("readSruRecord(String content): " + e.getMessage() + "\012" + content);
        }    	

        return record;
    }
    
    /**
     * Liest ein einzelnes Datafield aus einem SRU-record und gibt ihn als data zurück.
     * 
     * @param String tag String content
     * @return String data
     */
    private static String getSruDatafield(String tag, String content){
    	
    	StringBuffer buf = new StringBuffer();
    	
    	try {
    		
    		while (content.contains("<datafield tag=\"" + tag + "\">")) { // ein Tag kann mehrmals vorkommen
    		
    		String content_copy = content.substring(content.indexOf("<datafield tag=\"" + tag + "\">")+22, content.indexOf("</datafield>", content.indexOf("<datafield tag=\"" + tag + "\">")));
    		
    		while (content_copy.contains("<subfield code=\"")) {
    		
	    		if (buf.length()>0) buf.append("\040|\040"); // Trennzeichen, falls schon Inhalt vorhanden    		
	    		buf.append(content_copy.substring(content_copy.indexOf("<subfield code=\"")+19, content_copy.indexOf("</subfield>", content_copy.indexOf("<subfield code=\""))));
	    		content_copy = content_copy.substring(content_copy.indexOf("<subfield code=\"")+17);  // Stringverkürzung für nächsten Treffer  		
    		}
    		
    		content = content.substring(content.indexOf("<datafield tag=\"" + tag + "\">")+21); // Stringverkürzung für nächsten Treffer
    		
    		}
    	
    	} catch(Exception e){
    		log.error("getSruDatafield(String content): " + e.getMessage() + "\012" + content);
        }
    	
    	if (buf.length()==0) buf.append(""); // nicht null zurückgeben

        return deleteAT(buf.toString());
    }
    
    /**
     * Liest ein einzelnes Subfield aus einem SRU-record und gibt ihn als data zurück.
     * 
     * @param String code String content
     * @return String data
     */
    private static String getSruSubfield(String code, String content){
    	
    	String data = "";
    	
    	try {
    		
    		if (content.contains("<subfield code=\"" + code + "\">")) {
    		
    		data = content.substring(content.indexOf("<subfield code=\"" + code + "\">")+19, content.indexOf("</subfield>", content.indexOf("<subfield code=\"" + code + "\">")));
    		
    		}
    	
    	} catch(Exception e){
    		log.error("getSruSubfield(String code, String content): " + code + "\040" + e.getMessage() + "\012" + content);
        }

        return deleteAT(data);
    }
    
    /**
     * Liest Error aus SRU-Content aus
     * 
     * @param String content
     * @return String error
     */
    private static String getSruErrorCode(String content){
    	
    	String error = "";
    	
    	try {
    		
    		if (content.contains("<diag:message>")) {
    		
    		error = content.substring(content.indexOf("<diag:message>")+14, content.indexOf("</diag:message>", content.indexOf("<diag:message>")));
    		
    		}
    	
    	} catch(Exception e){
    		log.error("getSruErrorCode(String content): " + e.getMessage() + "\012" + content);
        }

        return error;
    }    
    
    /**
     * Holt den GBV-Webcontent anhand einer Phrasen-Suche ("Wort 1 Wort2...") über die SRU-Schnittstelle.
     * @param String gbvfield
     * @param String gbvsearchterm
     * @param String start_record
     * 
     * @return String content
     * 
     */
    private String getGbvSrucontentSearchAsPhrase(String gbvfield, String gbvsearchterm, int start_record){
    	Http http = new Http();
    	CodeUrl codeUrl = new CodeUrl();
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	String link = "http://gso.gbv.de/sru/DB=2.1/?version=1.1&operation=searchRetrieve&query=pica." + gbvfield.toLowerCase() + "%3D%22" +
    	codeUrl.encodeLatin1(gbvsearchterm) + "%22&recordSchema=pica&sortKeys=YOP%2Cpica%2C0%2C%2C&maximumRecords=10&startRecord=" + String.valueOf(start_record);

        return specialCharacters.replace(convertStringFromLatin1ToUTF8(http.getWebcontent(link, 3000, 2)));
    	
    }
    
    /**
     * Holt den GBV-Webcontent anhand einer Stichwort-Suche (Wort 2 Wort1...) über die SRU-Schnittstelle.
     * @param String gbvfield
     * @param String gbvsearchterm
     * @param String start_record
     * 
     * @return String content
     * 
     */
    private String getGbvSrucontentSearch(String gbvfield, String gbvsearchterm, int start_record){
    	Http http = new Http();
    	CodeUrl codeUrl = new CodeUrl();
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	String link = "http://gso.gbv.de/sru/DB=2.1/?version=1.1&operation=searchRetrieve&query=pica." + gbvfield.toLowerCase() + "%3D" +
    	codeUrl.encodeLatin1(gbvsearchterm) + "&recordSchema=pica&sortKeys=YOP%2Cpica%2C0%2C%2C&maximumRecords=10&startRecord=" + String.valueOf(start_record);

        return specialCharacters.replace(convertStringFromLatin1ToUTF8(http.getWebcontent(link, 3000, 2)));

    }
    
    /**
     * löscht alle @-Zeichen eines Strings
     * 
     */
    private static String deleteAT(String input){
    	String output = input;
    	
    	Check ck = new Check();
    	
    	if (ck.isMinLength(input, 1)) {
    		
    		output = output.replaceAll("@", "");
    		
    	}    	

        return output;
    }
    
    /**
     * löst den codierten Medientyp aus dem SRU-Result des GBV auf
     * 
     */
    private static String resolveGbvMediatype(String input){
    	String output = input;
    	
    	Check ck = new Check();
    	
    	if (ck.isMinLength(input, 3)) { // Typ kann bis 6-Stellen haben, 3 sind obligatorisch
    		
    		if (input.length()>3) input = input.substring(0, 3);
    		
    		String one = input.substring(0, 1);
    		String two = input.substring(1, 2);
    		String three = input.substring(2);
    		
    		if (one.equals("A")) {
    			output = decodePositionTwo(two);
    			String status = decodePositionThree(three);
    			if (!status.equals("")) output = output + "\040-\040" + status;
    		} else {
    			output = decodePositionOne(one);
    			String status = decodePositionThree(three);
    			if (!status.equals("")) output = output + "\040-\040" + status;
    		}
    		
    	}   	

        return output;
    }
    
    /**
     * decodiert Position 1
     * 
     */
    private static String decodePositionOne(String input){
    	String output = "";
    	
//		Position 1 (physikalische Form)
//		Bei Buchmaterialien ist Position 1 von 0500 immer A. Insgesamt sind folgende Codierungen vorgesehen:
//
//		A Druckschrift
//		B Audiovisuelles Material (Bildtonträger, auch Stummfilme; Tonbildreihen)
//		C Blindenschriftträger
//		E Mikroform
//		G Tonträger
//		H Handschriftliches Material
//		K Kartenmaterial	
//		M Noten
//		O Elektronische Ressource im Fernzugriff
//		S Elektronische Ressource auf Datenträger
//		V Objekt (z. B. Spiele, Skulpturen, Gemälde)
//		Z Materialkombination
    	
    	if (input.equals("A")) output = "Druckschrift";
    	if (input.equals("B")) output = "Multimedia";
    	if (input.equals("C")) output = "Blindenschrift";
    	if (input.equals("E")) output = "Mikroform";
    	if (input.equals("G")) output = "Tonträger";
    	if (input.equals("H")) output = "Handschrift";
    	if (input.equals("K")) output = "Kartenmaterial";     	
    	if (input.equals("O")) output = "Elektronische Ressource Online";
    	if (input.equals("S")) output = "Elektronische Ressource auf Datenträger";
    	if (input.equals("V")) output = "Objekt (Spiele, Skulpturen, Gemälde etc,)";
    	if (input.equals("Z")) output = "Materialkombination";

        return output;
    }
    
    /**
     * decodiert Position 2
     * 
     */
    private static String decodePositionTwo(String input){
    	String output = "";
    	
//		Position 2 (bibliographische Erscheinungsform)
//		Nicht alle Codierungen kommen auch bei allen Materialarten vor; auf eine detaillierte Zuordnung wird hier verzichtet. Folgende Codierungen sind vorgesehen:
//
//		a Monographie (unabhängig ob Stück einer Reihe)
//		b Zeitschrift/Zeitung
//		c Gesamtaufnahme eines mehrbändigen begrenzten Werkes
//		d Schriftenreihe
//		e Abteilung (unselbstständig) Entfällt zukünftig
//		E Abteilung (selbstständig) Nur aus Fremddaten
//		f Teil eines mehrbändigen begrenzten Werkes ohne bzw. mit nicht zitierfähigem Stücktitel
//		F Teil eines mehrbändigen begrenzten Werkes mit zitierfähigem Stücktitel
//		h Abteilung (Zeitschriften und Reihen) Nur aus Fremddaten
//		j Enthaltenes/beigefügtes Werk
//		o Unselbstständiges Werk (nur OLC) Nicht mehr verwendet
//		s Unselbstständiges Werk (Aufsatz, Rezension)
//		v Bandsatz bei Zeitschriften/zeitschriftenartigen Reihen
//		z Keine Angabe
    	
    	if (input.equals("a")) output = "Buch";
    	if (input.equals("b")) output = "Zeitschrift (Print)";
    	if (input.equals("c")) output = "mehrbändiges Gesamtwerk";
    	if (input.equals("d")) output = "Schriftenreihe";
    	if (input.equals("e")) output = "Abteilung"; // ?
    	if (input.equals("E")) output = "Abteilung"; // ?
    	if (input.equals("f")) output = "Band eines Gesamtwerkes";
    	if (input.equals("F")) output = "Band eines Gesamtwerkes";
    	if (input.equals("h")) output = "Aufsatz";
    	if (input.equals("j")) output = "Enthaltenes/beigefügtes Werk";
    	if (input.equals("o")) output = "unselbstständiges Werk"; // ?
    	if (input.equals("s")) output = "Aufsatz";
    	if (input.equals("v")) output = "Zeitschrift gebunden";
    	if (input.equals("z")) output = "keine Angabe";    	

        return output;
    }
    
    /**
     * decodiert Position 3
     * 
     */
    private static String decodePositionThree(String input){
    	String output = "";
    	
//		Position 3 (Status der Beschreibung)
//		Nicht alle Codierungen kommen auch bei allen Materialarten vor; auf eine detaillierte Zuordnung wird hier verzichtet. Folgende Codierungen sind vorgesehen:
//			
//		u Autopsie
//		a Erwerbungsdatensatz
//		v Bibliographisch vollständig (Satzsperre)
//		c CIP-Aufnahme
//		k Lösch-Status (zur Nutzung gesperrt)
//		n Konvertierte Altdaten
//		r Retrospektives Katalogisat		
//		x Fremddatum
//		y Vorläufigkeitsstatus
//		B Offline eingespieltes Novum, wahrscheinlich dublett
//		N Zunächst verdeckt eingespieltes Novum
//		X Inhalt oder Struktur ist zu überprüfen
    	
    	if (input.equals("k")) output = "keine Ausleihe/Kopiebestellung";
    	if (input.equals("y")) output = "Vorläufigkeitsstatus";
//    	if (input.equals("B")) output = "keine Ausleihe/Kopiebestellung"; // scheint nicht immer gesperrt zu sein...

        return output;
    }
    
    private String convertStringFromLatin1ToUTF8(String stringForconversion) {
		try {
			String stringToConvert = stringForconversion;
			byte[] convertStringToByte = stringToConvert.getBytes("ISO-8859-1");
			return new String(convertStringToByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("convertStringFromLatintoUTF8: " + stringForconversion + "\040" + e.getMessage());
			return stringForconversion;
		}
	}
    
    /**
     * Wandelt eine e-ZDB-ID in eine p-ZDB-ID um. Optimiert für die Abfrage von ausserhalb OrderGbvAction. 
     * 
     */
    public static String getPrintZdbidIgnoreMultipleHits(String content) {
    	String p_zdbid = null;

		try {
			ArrayList<GbvSruForm> matches = getGbvMatches(content);
			
			if (matches.size()>0) { // nur falls nicht keine Treffer! Sonst kracht es...
				GbvSruForm gsf = (GbvSruForm) matches.get(0);
			// bringt aus ggf. mehreren Umleitungen die letztmögliche
				if (gbvIsEjournalRedirectableIgnoreMultipleHits(gsf)) {    						
					p_zdbid = gsf.getVerknuepfung_zdbid_horizontal();						
				}
			}
			
		} catch (Exception e) {
			log.error("getPrintZdbid(String zdbid): " + e.toString() + "\012" + content);			
		}
		
		return p_zdbid;
	}
    /**
     * Prüft, ob alle Mussangaben für eine GBV-Bestellung gemacht wurden,
     * abhängig vom gewählten Medientyp
     * @param OrderForm of
     * @return boolean check
     */
    private boolean gbvOrderValues(OrderForm of){
    	
    	boolean check = false;
    	Check ck = new Check();
    	
    	// falls manuelle Bestellung gelten weniger scharfe Mussfelder
    	if (of.isManuell() && 
    		((of.getMediatype().equals("Artikel") && ck.isMinLength(of.getIssn(), 8) || ck.isMinLength(of.getZeitschriftentitel(), 1)) ||
    		 (of.getMediatype().equals("Teilkopie Buch") && ck.isMinLength(of.getIsbn(), 8) || ck.isMinLength(of.getBuchtitel(), 1)) ||
    		 (of.getMediatype().equals("Buch") && ck.isMinLength(of.getIsbn(), 8) || ck.isMinLength(of.getBuchtitel(), 1)) )) {
    			check = true;
    			return check;
    	}
    	
    	// Check bei Medientyp Artikel
    	if (!of.isManuell() && of.getMediatype().equals("Artikel") && !of.getForuser().equals("0") && 
    			ck.containsOnlyNumbers(of.getJahr()) && ck.isExactLength(of.getJahr(), 4) &&
    		(ck.isMinLength(of.getJahrgang(), 1) || ck.isMinLength(of.getHeft(), 1)) &&
    		ck.isMinLength(of.getSeiten(), 1) && 
    		(ck.isMinLength(of.getIssn(), 8) || ck.isMinLength(of.getZeitschriftentitel(), 1))) { // Entweder Angabe einer ISSN oder eines Zeitschriftentitels
    			check = true;
    			return check;
    	}
    	// Check bei Medientyp Teilkopie Buch
    	if (!of.isManuell() && of.getMediatype().equals("Teilkopie Buch") && !of.getForuser().equals("0") && 
    		(ck.isMinLength(of.getIsbn(), 4) || ck.isMinLength(of.getBuchtitel(), 1)) && // Entweder Angabe einer ISBN oder eines Buchtitels
    		ck.isMinLength(of.getSeiten(), 1)) {
        		check = true;
        		return check;
        }
    	// Check bei Medientyp Buch
    	if (!of.isManuell() && of.getMediatype().equals("Buch") && !of.getForuser().equals("0") && 
        		(ck.isMinLength(of.getIsbn(), 4) || ck.isMinLength(of.getBuchtitel(), 1)) ) { // Entweder Angabe einer ISBN oder eines Buchtitels
            	check = true;
            	return check;
        }
    	
    	
    	return check;    	
    }

  /**
  * Prüft, ob es sich um ein Print- oder E-Journal handelt
  * und ob, ZDB-ID-Angaben über eine Paralleldruckausgabe vorliegen
  * @param GbvSruForm srf
  * @return boolean check
  */
 private boolean gbvIsEjournalRedirectable(GbvSruForm gsf){
 	
 	boolean check = false;
 	
 	if (gsf.getVerknuepfung_horizontal_039D_multipel()!=null && gsf.getAnzahl_039D()<2 &&
 		    (gsf.getVerknuepfung_horizontal_039D_multipel().contains("Druckausg.") ||  
			(gsf.getMaterialbenennung_spezifisch_034D()!=null && gsf.getMaterialbenennung_spezifisch_034D().contains("Online-Ressource"))) &&
			 gsf.getVerknuepfung_zdbid_horizontal()!=null && !gsf.getVerknuepfung_zdbid_horizontal().equals("") ){
 		
 		check = true;
 		
 	}    	
 	
 	return check;    	
 }
 
 /**
  * Optimiert für Checkavailability
  * Prüft, ob es sich um ein Print- oder E-Journal handelt
  * und ob, ZDB-ID-Angaben über eine Paralleldruckausgabe vorliegen
  * @param GbvSruForm srf
  * @return boolean check
  */
 private static boolean gbvIsEjournalRedirectableIgnoreMultipleHits(GbvSruForm gsf){
 	
 	boolean check = false;
 	
 	if (gsf.getVerknuepfung_horizontal_039D_multipel()!=null &&
 		    (gsf.getVerknuepfung_horizontal_039D_multipel().contains("Druckausg.") ||  
			(gsf.getMaterialbenennung_spezifisch_034D()!=null && gsf.getMaterialbenennung_spezifisch_034D().contains("Online-Ressource"))) &&
			 gsf.getVerknuepfung_zdbid_horizontal()!=null && !gsf.getVerknuepfung_zdbid_horizontal().equals("") ){
 		
 		check = true;
 		
 	}    	
 	
 	return check;    	
 }
 
 /**
  * Prüft, ob es sich um ein Print- oder E-Journal handelt
  * @param GbvSruForm srf
  * @return boolean check
  */
 private boolean gbvIsEjournal(GbvSruForm gsf){
 	
 	boolean check = false;
 	
 	if ( (gsf.getVerknuepfung_horizontal_039D_multipel()!=null && gsf.getVerknuepfung_horizontal_039D_multipel().contains("Druckausg.")) ||  
 		 (gsf.getMaterialbenennung_spezifisch_034D()!=null && gsf.getMaterialbenennung_spezifisch_034D().contains("Online-Ressource")) ) {
 		
 		check = true;
 		
 	}    	
 	
 	return check;    	
 }
    
    
  /**
  * Prüft, ob es sich bei der GBV-Antwort um den Status OK handelt und ob eine Bestellnummer vorliegt
  * @param String gbvanswer
  * @return boolean check
  */
 private boolean gbvIsOrdernumber(String gbvanswer){
 	
 	boolean check = false;
 	
 	if (gbvanswer!=null && gbvanswer.contains("<status>OK</status>") && gbvanswer.contains("<return_value>")) {
 		
 		try {
 		
 		gbvanswer = gbvanswer.substring(gbvanswer.indexOf("<return_value>")+14, gbvanswer.indexOf("</return_value>", gbvanswer.indexOf("<return_value>")));
 		
 		Pattern p = Pattern.compile("A[0-9]*");
 		Matcher m = p.matcher(gbvanswer);      	  
 		if (m.find()) {
 		check = true; 
 	  	} 
 		} catch (Exception e) {
 			log.error("Check - gbvIsOrdernumber): " + e.toString() + "\012" + gbvanswer);
 		}
 	}
 	
 	return check;    	
 }

}
