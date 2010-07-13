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

package ch.dbs.actions.bestand;


import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.ZDBIDObject;
import ch.dbs.entity.Holding;
import ch.dbs.entity.Issn;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.HoldingForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

public class Stock extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(Stock.class);
	
    /**
     * bereitet die Bestandesverwaltung vor
     * @author Markus Fischer
     */
    public ActionForward prepare(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
    	long id = 10; // Standorte
    	ty.setId(id);
    	Auth auth = new Auth();
        
    	if (auth.isLogin(rq)) {
        if (auth.isAdmin(rq)) { // momentan nur Admins zugänglich
            forward = "success";
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hf = (HoldingForm) fm;
            
            hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
            
            rq.setAttribute("holdingform", hf);
 
         
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute("errormessage", em);
        }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        t.close();
        return mp.findForward(forward);
    }
    
    /**
     * speichert eine Bestandesangabe ab
     */
    public ActionForward save(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
    	long id = 10; // Standorte
    	ty.setId(id);
    	Auth auth = new Auth();
        
        if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) { // momentan nur Admins zugänglich
            forward = "success";
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hof = (HoldingForm) fm;
            Holding hol = new Holding();
            
            // Prüft, ob es beim aktuellen Konto schon eine Holding-Angabe gibt
            // falls ja, wird das Holding aus der DB geholt
            // falls nein, wird ein neues Holding in der DB gespeichert
            hof.setHolding(hol.createHolding(ui.getKonto(), hof.getIdentifierdescription(), hof.getIdentifier(), t.getConnection()));
            
            // Standort in hof legen
            Text txt = new Text(t.getConnection(), Long.valueOf(hof.getStandortid()), ui.getKonto().getId(), ty.getId());
            hof.setStandort(txt);
            
         // legt einen Standort als Default in die Session (ui)
            if (hof.isStandardplace()==true) {
            	ui.setDefaultstandortid(Long.valueOf(hof.getStandortid()));
            	rq.getSession().setAttribute("userinfo", ui);
            }
            
            ArrayList<Message> list = new ArrayList<Message>();
            list = checkStock(hof, t.getConnection());
            
            if (list.size()==0) { // nur speichern, falls keine Message vorhanden            
            Bestand be = new Bestand(hof);
            be.save(be, t.getConnection());           
            
            HoldingForm hf = new HoldingForm(); // neues leeres hf        
            hf.setMessage("Bestand erfolgreich abgespeichert! Neue Eingabe:");
            hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
            hf.setIdentifier(hof.getIdentifier());
            hf.setIdentifierdescription(hof.getIdentifierdescription());
            hf.setZeitschrift(hof.getZeitschrift());
            
            rq.setAttribute("holdingform", hf);
            
            } else { // vor dem Abspeichern eine Meldung ausgeben
            
            rq.setAttribute("holdingform", hof); // Eingaben behalten
            rq.setAttribute("message", list); // Meldungen mitschicken
            forward = "message"; // TODO: Pfad oder Anzeige regeln
            }
 
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute("errormessage", em);
        } 
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        t.close();
        return mp.findForward(forward);
    }
    
    /**
     * Verwaltung der Standorte
     */
    public ActionForward listStandorte(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
    	long id = 10; // Standorte
    	ty.setId(id);
    	Auth auth = new Auth();
        
        if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) { // momentan nur Admins zugänglich
            forward = "success";
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hf = (HoldingForm) fm;
            
            // neuer Standort wurde eingegeben...
            if (hf.getStandortid()!=null && !hf.getStandortid().equals("")) {
            	t.setInhalt(hf.getStandortid());
            	t.setKonto(ui.getKonto());
            	t.setTexttyp(ty);
            	t.saveNewText(t.getConnection(), t);
            }
            
            hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));         
            
            rq.setAttribute("holdingform", hf);
 
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute("errormessage", em);
        } 
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        t.close();
        return mp.findForward(forward);
    }
    
    /**
     * Bestandesübersicht
     */
    public ActionForward listBestand(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Auth auth = new Auth();
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        if (auth.isLogin(rq)) {
        if (auth.isAdmin(rq)) { // momentan nur Admins zugänglich
            forward = "success";
            
            //TODO: code...
 
         
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute("errormessage", em);
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
     * Bereitet das Ändern eines konkreten Standortes vor und verhindert URL-hacking
     */
    public ActionForward changeStandort(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
    	long id = 10; // Standorte
    	ty.setId(id);
    	Auth auth = new Auth();
        
        if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) { // momentan nur Admins zugänglich
        	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hf = (HoldingForm) fm;
            
            // Sicherstellen, dass nur Texte aus dem aktuellen Konto und 
            // aus dem Bereich 10 = Standorte geändert und gelöscht werden können
            Text txt = new Text(t.getConnection(), hf.getStid(), ui.getKonto().getId(), ty.getId());
        if (txt.getId() !=null) {
        	forward = "success";
        	
        if (hf.isMod()==false && hf.isDel()==false) { // Vorbereiten um ein Standort zu ändern
            hf.setMod(true); // markiert, dass Standort geändert wird
            ArrayList<Text> sl = new ArrayList<Text>();
            sl.add(txt);
            hf.setStandorte(sl);
        } else {
        	if (hf.isMod()==true) { // Hier wird ein bestehender Text geupdated
        		txt.setInhalt(hf.getStandortid());
        		txt.updateText(t.getConnection(), txt);
        		hf.setMod(false); // zurück zur Liste
        		hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
        	} else {
        		if (hf.isDel()==true) { // Hier wird ein bestehender Text gelöscht
        			// Kontrolle ob es noch verknüpfte Bestände gibt
        			Bestand bestandInstance = new Bestand();
        			ArrayList<Bestand> sl = new ArrayList<Bestand>(bestandInstance.getAllBestandForStandortId(txt.getId(), t.getConnection()));
        			if (sl==null || sl.size()==0) {
        			txt.deleteText(t.getConnection(), txt);
        			hf.setMod(false);
        			hf.setDel(false);
        			} else {
        				// es gibt noch verknüpfte Standorte!
        				hf.setMessage("Standort nicht löschbar! Es existieren noch verknüpfte Bestände!");
        			}
        			hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
        		}
        	}
        	
        }
        
        rq.setAttribute("holdingform", hf);
        
        } else { // URL-hacking
        	ErrorMessage em = new ErrorMessage("error.hack", "login.do");
            rq.setAttribute("errormessage", em);
            log.info("changeStandort: prevented URL-hacking! " + ui.getBenutzer().getEmail());
        }
 
         
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute("errormessage", em);
        }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        t.close();
        return mp.findForward(forward);
    }
    
    /**
     * Liefert eine kontoübergreifende Liste aller Bestände
     */
    public ArrayList<Bestand> checkGeneralStockAvailability (OrderForm pageForm, boolean internal) {
    	
    	ArrayList<Bestand> bestaende = new ArrayList<Bestand>();
    	
    	if (pageForm.getIssn()!=null && !pageForm.getIssn().equals("")) {
    		Text cn = new Text();
    		
    		ArrayList<String> setIssn = new ArrayList<String>();
    		
    		setIssn = getRelatedIssn(pageForm.getIssn(), cn.getConnection());
    		ArrayList<String> hoids = new ArrayList<String>();
    		
    		Holding ho = new Holding();
    		hoids = ho.getAllHOIDs(setIssn, cn.getConnection());

    		Bestand be = new Bestand();
    		bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn.getConnection());
    		    		
    		cn.close();
    	}
    	
    	return bestaende;
    	
    }
    
    /**
     * Liefert eine kontospezifische Liste aller Bestände anhand einer IP
     */
    public ArrayList<Bestand> checkStockAvailabilityForIP (OrderForm pageForm, Text tip, boolean internal, Connection cn) {
    	
    	ArrayList<Bestand> bestaende = new ArrayList<Bestand>();
    		
    		Bestand be = new Bestand();
    		Holding ho = new Holding();
    		
    		if (tip.getKonto()!=null && tip.getKonto().getId()!=null) { // Nur prüfen, falls Konto vorhanden
    		
    		ArrayList<String> setIssn = new ArrayList<String>();
    		setIssn = getRelatedIssn(pageForm.getIssn(), cn);
    		ArrayList<String> hoids = new ArrayList<String>();
    		
    		hoids = ho.getAllHOIDsForKonto(setIssn, tip.getKonto().getId(), cn);
    		
    		bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn);
    		}
    	
    	return bestaende;
    	
    }
    
    /**
     * Führt Bestandeskontrollen vor dem Abspeichern durch
     */
    private ArrayList<Message> checkStock(HoldingForm hf, Connection cn) {
    	
    	ArrayList<Message> list = new ArrayList<Message>();
    	
        // check for same place, but different shelve 
    	Message shelve = new Message();
    	if (hf.getHolding().getId()!=null) { // Prüfung nur falls ID vorhanden
    	shelve = checkStockSamePlaceDifferentShelve(hf, cn);
    	if (shelve.getMessage()!=null) {    		
    		shelve.setMessage("Von dieser Zeitschrift, gibt es am selben Standort auf einem anderen Gestell, oder unter einer anderen Notation bereits einen Eintrag!");
    		list.add(shelve);
    	}
    	}
    	
        // check for related ISSNs
    	ArrayList<String> relatedIssns = new ArrayList<String>();
    	relatedIssns = checkStockRelatedIssn(hf, cn);
    	if (relatedIssns.size()>1) {  
    		Message m = new Message();
    		m.setMessage("Es existieren Bestände mit 'verwandten' ISSN-Nummern!");
    		list.add(m);
    	}
    	

        // double stock
        
        return list;
    }
    
    /**
     * Prüft nach Bestand am gleichen Ort, aber auf einem anderen Gestell, resp. unter anderer Notation
     */
    private Message checkStockSamePlaceDifferentShelve(HoldingForm hf, Connection cn) {
    	
    	Message m = new Message();
    	Bestand bestandInstance = new Bestand();
    	
        // check for same place, but different shelve
    	ArrayList<Bestand> sl = new ArrayList<Bestand>();
    	sl = bestandInstance.getAllBestandOnDifferentShelves(hf.getHolding().getId(), hf.getStandort().getId(), hf.getShelfmark(), cn);
    	if (sl.size()>0) {
    		m.setMessage("Von dieser Zeitschrift, gibt es am selben Standort auf einem anderen Gestell, oder unter einer anderen Notation bereits einen Eintrag!");
    	}
        
        return m;
    }
    
    /**
     * Prüft nach Bestand mit verwandten ISSNs
     */
    private ArrayList<String>checkStockRelatedIssn(HoldingForm hf, Connection cn) {
    	
    	// check for related ISSNs
    	ArrayList<String> issn = new ArrayList<String>();
    	issn = getRelatedIssn(hf, cn);
    	
        
        return issn;
    }
    
    /**
     * holt anhand einer ISSN eine TreeSet<String> Liste aller 'verwandten' ISSNs um ein Mapping zu machen
     */
    private ArrayList<String> getRelatedIssn(String issn, Connection cn) {
    	
    	ArrayList<String> issns = new ArrayList<String>();
    	Issn issnInstance = new Issn();
		
    	issns = issnInstance.getAllIssnsFromOneIssn(issn, cn);
    	
    	if (issns.size()==0) issns.add(issn); // falls kein Treffer gefunden wurde, ursprüngliche ISSN zurückgeben

        return issns;
    }
    
    /**
     * holt anhand einem HoldingForm die nach Dubletten bereinigte Liste aller 'verwandten' ISSNs um ein Mapping zu machen
     */
    private ArrayList<String> getRelatedIssn(HoldingForm hf, Connection cn) {
    	
    	ArrayList<String> issns = new ArrayList<String>();
    	Issn issnInstance = new Issn();
    	
    	if (hf.getHolding().getIssn()!=null) { // anhand ISSN
    		issns = issnInstance.getAllIssnsFromOneIssn(hf.getHolding().getIssn(), cn);
    		if (issns.size()==0) issns.add(hf.getHolding().getIssn()); // falls kein Treffer gefunden wurde, ursprüngliche ISSN zurückgeben
    	} else { // TODO: Strategie überlegen für diesen Fall!?
			if (hf.getHolding().getZdbid()!=null) { // anhand zdbid
				ZDBIDObject zo = new ZDBIDObject();
				zo = zo.getZdbidObjectFromZdbid(hf.getHolding().getZdbid(), cn);
				if (zo!=null && zo.getIdentifier_id()!=null) {
					issns = issnInstance.getAllIssnsFromOneIdentifierID(zo.getIdentifier_id(), cn);
				}
			}
		}
        
        return issns;
    }


}
