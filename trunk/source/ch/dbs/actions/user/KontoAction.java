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

package ch.dbs.actions.user;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.*;
import ch.dbs.admin.KontoAdmin;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Fax;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.entity.VKontoBenutzer;
import ch.dbs.form.*;

public final class KontoAction extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(KontoAction.class);
	
    /**
     * Hinzufügen eines neuen Benutzers vorbereiten
     */
    public ActionForward prepareNewKonto(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

        String forward = "failure";
        Text cn = new Text();

        KontoForm kf = new KontoForm();
        Countries countriesInstance = new Countries();
        
            List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());            
            kf.setCountries(allPossCountries);
            forward = "success";
            rq.setAttribute("kontoform", kf);          
            
      cn.close();
      
      //Navigation: Tab Neues Konto aktiv schalten
      ActiveMenusForm mf = new ActiveMenusForm();
      mf.setActivemenu("newkonto");
      rq.setAttribute("ActiveMenus", mf);
      
      return mp.findForward(forward);
    }
 
    /**
     * neues Konto anlegen
     */
    public ActionForward addNewKonto(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
	    	ActiveMenusForm mf = new ActiveMenusForm();
	        mf.setActivemenu("newkonto");
	        rq.setAttribute("ActiveMenus", mf);
        
            String forward = "success";
            KontoForm kf = (KontoForm)fm;
            Konto k = new Konto(kf); //Konto erstellen und mit Values aus KontoForm abfuellen
            Check check = new Check();
            Text cn = new Text();
            Countries countriesInstance = new Countries();
         // GBV-Thread-Management
        	ThreadedWebcontent gbvthread = new ThreadedWebcontent();
    		ExecutorService executor = Executors.newCachedThreadPool();
    		Future<String> gbvcontent = null;
    		
    		if (ReadSystemConfigurations.isAllowRegisterLibraryAccounts()) {
    			
    			// ggf. Leerschläge entfernen
    			if (k.getBibliotheksmail()!=null) k.setBibliotheksmail(k.getBibliotheksmail().trim());
    			if (k.getDbsmail()!=null) k.setDbsmail(k.getDbsmail().trim());
    			
            //Angaben rudimentaer Validieren
            if (check.isMinLength(k.getBibliotheksname(), 1) && check.isMinLength(k.getAdresse(), 1) &&
            		check.isMinLength(k.getPLZ(), 4) && check.isMinLength(k.getOrt(), 1) &&
            		check.isMinLength(k.getLand(), 2) && check.isMinLength(k.getTelefon(), 4) &&
                	check.isEmail(k.getBibliotheksmail()) && check.isEmail(k.getDbsmail()) ) {
            	
            // mit einem neuen Thread ist hier die Timeout-Kontrolle besser
            if (kf.getGbvbenutzername()!=null && !kf.getGbvbenutzername().equals("")) {
               	String gbvlink = "http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" +
               	kf.getGbvbenutzername() + "&PASSWORD=" + kf.getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2FCOPY%3FPPN%3D185280552%26LANGCODE%3DDU";
               	gbvthread.setLink(gbvlink);
               	gbvcontent = executor.submit(gbvthread);
              }
            
            boolean checkgbv = true; // grundsätzlich auf true stellen. Wird erst geprüft, wenn tatsächlich ein Benutzername eingegeben wird
            // falls beim GBV ein Benutzername angegeben wurde
            if (kf.getGbvbenutzername()!=null && !kf.getGbvbenutzername().equals("")) {
            	try {
            	checkgbv = false;
            	String gbvanswer = "";
            	gbvanswer = gbvcontent.get(3, TimeUnit.SECONDS);
            	if (gbvanswer.contains("Abmelden")) {
            		checkgbv = true; 
            	} else {
            		forward = "subitofailed"; // Pfad i.O.
                	kf.setMessage("error.gbv_values");
                    List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());                
                    kf.setCountries(allPossCountries);
                    rq.setAttribute("kontoform", kf);
            	}
            	} catch (TimeoutException e) {
            		log.info("GBV-Timeout: " + e.toString());
    				forward = "subitofailed"; // Pfad i.O.
                	kf.setMessage("error.gbv_timeout");
                    List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());                
                    kf.setCountries(allPossCountries);
                    rq.setAttribute("kontoform", kf);
    			} catch (Exception e) {
    				log.error("GBV-Thread failed in modifykonto: " + e.toString());                		
            	} finally {
            		// ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
	    			gbvcontent.cancel(true);
            	}
            	
            }

            if (checkgbv == true) {
            		
                	kf.setKonto(k); // Konto in Kontoform schreiben
//                	kf.setIsil(correctIsil(kf.getIsil())); // Leerschläge und Umlaute aus ISIL entfernen sollte seit Methodenumzug nicht mehr nötig sein
                	rq.getSession().setAttribute("kontoform", kf); // Konto in Kontoform in Session legen
            	
            } // benötigt keine separate else-Schlaufe, da der Negativfall schon oben behandelt wurde
            
            } else {
            	forward = "subitofailed";
            	kf.setMessage("error.values");
            	
                List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());              
                kf.setCountries(allPossCountries);
            	
            	rq.setAttribute("kontoform", kf);            	
            }
            
		    } else {
		    	forward = "subitofailed";
		    	kf.setMessage("error.berechtigung");
		    	
		        List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());              
		        kf.setCountries(allPossCountries);
		    	
		    	rq.setAttribute("kontoform", kf);            	
		    }

        cn.close();   
        return mp.findForward(forward);
    }
    
    /**
     * Neuen Bibliothekar anlegen und mit Konto verknüpfen
     */
    public ActionForward addNewBibliothekar(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Check check = new Check();
    	Bibliothekar u = new Bibliothekar();
        UserForm uf = (UserForm)fm;
        Text cn = new Text();
        Auth auth = new Auth();
        
        ActiveMenusForm mf = new ActiveMenusForm();
    	mf.setActivemenu("newkonto");
    	rq.setAttribute("ActiveMenus", mf);
        
    	if (auth.isKontoform(rq) && // Seite kann nur aufgerufen werden, wenn vorher erfolgreich ein Konto angelegt wurde und kein userinfo im rq ist...
    		ReadSystemConfigurations.isAllowRegisterLibraryAccounts()) {
    	      
        KontoForm kf = (KontoForm)rq.getSession().getAttribute("kontoform");	 // Konto aus Kontoform holen  
        Konto k =new Konto(kf);
        
        if (kf.getEzbid().contains("bibid=")) { // Prüfung, ob EZB-ID vorhanden
				String ezb = kf.getEzbid().substring(
						kf.getEzbid().indexOf("bibid=") + 6);
				if (ezb.contains("&")) { // falls EZB-ID mitten im String
					k.setEzbid(ezb.substring(0, ezb.indexOf("&")));
				} else { // falls EZB-ID am Ende des Strings
					k.setEzbid(ezb);
				}
			} else { // falls keine gültige URL angegeben wurde
				k.setEzbid("");
			}
        
        // ggf. Leerschläge entfernen
        if (uf.getEmail()!=null) uf.setEmail(uf.getEmail().trim());
        
        //TODO: Korrekte Fehlermeldunen an Benutzer ausgeben. Minimale länge wird nicht als korrekter Fehler zurückgemeldet
        if (check.isMinLength(uf.getName(), 2) && check.isMinLength(uf.getVorname(), 2) &&
        	check.isEmail(uf.getEmail()) && check.isMinLength(uf.getPassword(), 6)){
        
        u.setAnrede(uf.getAnrede());
        if (uf.getVorname() !=null) {u.setVorname(uf.getVorname().trim());} else {u.setVorname(uf.getVorname());}
        if (uf.getName() !=null) {u.setName(uf.getName().trim());} else {u.setName(uf.getName());}
        if (uf.getEmail() !=null) {u.setEmail(uf.getEmail().trim());} else {u.setEmail(uf.getEmail());}
        if (uf.getTelefonnrg() !=null) {u.setTelefonnrg(uf.getTelefonnrg().trim());} else {u.setTelefonnrg(uf.getTelefonnrg());}
        if (uf.getTelefonnrp() !=null) {u.setTelefonnrp(uf.getTelefonnrp().trim());} else {u.setTelefonnrp(uf.getTelefonnrp());}
        if (uf.getInstitut() !=null) {u.setInstitut(uf.getInstitut().trim());} else {u.setInstitut(uf.getInstitut());}
        if (uf.getAbteilung() !=null) {u.setAbteilung(uf.getAbteilung().trim());} else {u.setAbteilung(uf.getAbteilung());}
        if (uf.getAdresse() !=null) {u.setAdresse(uf.getAdresse().trim());} else {u.setAdresse(uf.getAdresse());}
        if (uf.getAdresszusatz() !=null) {u.setAdresszusatz(uf.getAdresszusatz().trim());} else {u.setAdresszusatz(uf.getAdresszusatz());}
        if (uf.getPlz() !=null) {u.setPlz(uf.getPlz().trim());} else {u.setPlz(uf.getPlz());}
        if (uf.getOrt() !=null) {u.setOrt(uf.getOrt().trim());} else {u.setOrt(uf.getOrt());}
        u.setLand(uf.getLand());
        u.setLoginopt(true); // Login erlaubt
        u.setKontostatus(true); // Userkonto aktiv
        Encrypt e = new Encrypt();
        u.setPassword(e.makeSHA(uf.getPassword()));
        u.setUserbestellung(true); // Bestellungen bei Subito erlaubt
        u.setGbvbestellung(true); // Bestellungen beim GBV erlaubt
        u.setKontovalidation(uf.getKontovalidation());
        u.setRechte(2); // Berechtigung Bibliothekar

        		//        		 Konto abspeichern und wieder holen wegen KID 
        		k.save(cn.getConnection());
        		AbstractBenutzer b = new AbstractBenutzer();
                b.saveNewUser(u, cn.getConnection());
                VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();
                vKontoBenutzer.setKontoBibliothekar(u, k, cn.getConnection());
                forward = "weiter";          	
            	
////            	Nachricht über neues Konto abschicken
                StringBuffer message = new StringBuffer("There has been registered a new library account in " + ReadSystemConfigurations.getApplicationName() + "\012\012");
                message.append("Library account: " + k.getBibliotheksname() + "\012");
                message.append("City: " + k.getOrt() + "\012");
                message.append("Country: " + k.getLand() + "\012");
                message.append("Library email: " + k.getBibliotheksmail() + "\012");
                message.append("Type of account (0 = free | 1 = 1 year enhanced | 2 = 1 year faxserver | 3 = 3 months faxserver): " + k.getKontotyp() + "\012");
                message.append("Librarian: " + u.getVorname() + "\040" + u.getName() + "\012");
                message.append("Librarian email: " + u.getEmail() + "\012");
                String[] to = new String[1];
                to[0]= ReadSystemConfigurations.getSystemEmail();
                MHelper mh = new MHelper();
                mh.sendMail(to, "New library account!", message.toString());
                
////        	Bestätigungsemail mit Angaben zu den nächsten Schritten und Möglichkeiten
                StringBuffer mg = new StringBuffer("Dear\040");
                if (u.getAnrede().equals("Frau")) mg.append("Ms\040" + u.getVorname() + "\040" + u.getName() + "\012\012");
                if (u.getAnrede().equals("Herr")) mg.append("Mr\040" + u.getVorname() + "\040" + u.getName() + "\012\012");
                mg.append("Welcome at " + ReadSystemConfigurations.getApplicationName() + "!\012\012");
                mg.append("To use the IP-based oderform for your patrons within your institution, send us your IP. We'll activate this function for your account at " + ReadSystemConfigurations.getApplicationName() + ".\012\012");
                mg.append("This link will show you your IP: http://www.whatismyip.com (if your institution uses an IP-range instead of a single IP, ask your IT)\012\012");
                mg.append("Check out the How-To to use " + ReadSystemConfigurations.getApplicationName() + " as a linkresolver (in connection with the services of EZB/ZDB): http://www.doctor-doc.com/version1.0/howto_openurl.do\012\012");
                mg.append("You team consists of several librarians and you want each one of them to have their own ID + PW? Create their accounts as normal patrons and contact us which addresses should be granted as librarians.\012\012");
                if (k.getKontotyp()!=0) mg.append("Thank you for choosing the option \"Fax to PDF\". We'll register you for this service and we will contact you with the details as soon as possible.\012\012");
                if (k.getKontotyp()!=0 && k.getLand().equals("Deutschland")) {
                	mg.append("-------------------\012");
                	mg.append("GILT FÜR DEUTSCHLAND:\012");
                	mg.append("Aufgrund der geltenden Bestimmungen des deutschen Gesetzes für Telekommunikation bezüglich der Ortsnetzrufnummern, die von der Bundesnetzagentur (http://www.bundesnetzagentur.de/media/archive/11497.pdf) verwaltet werden, ");
                	mg.append("benötigen wir eine schriftliche Bescheinigung, dass Sie tatsächlich den Sitz im selben Ortsnetzbereich mit der von uns vergebenen Faxnummer haben.\012");
                	mg.append("Bitte senden Sie uns, deshalb per Email eine Bescheinigung des \"Firmensitzes\", entweder in Form einer:\012");
                	mg.append("- Kopie einer Rechnung für Wasser, Strom, Gas usw.\012");
                	mg.append("- Kopie des Handelsregisterauszugs/Gewerbeanmeldung\012");
                	mg.append("Bitte entschuldigen Sie diesen Zusatzaufwand, aber leider verlangt die deutsche Gesetzgebung diese Überprüfung.\012");
                	mg.append("-------------------\012\012");
                	
                }
                mg.append("We hope you enjoy using " + ReadSystemConfigurations.getApplicationName() + "!\012\012");
                mg.append("Get in contact with us if you have any questions!\012\012");
                mg.append("Best regards\012");
                mg.append("Your team " + ReadSystemConfigurations.getApplicationName() + "\012");
                String[] sendto = new String[1];
                sendto[0]=u.getEmail();
                MHelper mailh = new MHelper();
                mailh.sendMail(sendto, "Your account at " + ReadSystemConfigurations.getApplicationName(), mg.toString());
                
//              Kontoform in Session leeren
                rq.getSession().setAttribute("kontoform", null);
                
                LoginForm lf = new LoginForm();                
                lf.setEmail(uf.getEmail());
                lf.setPassword(uf.getPassword());
                
                rq.setAttribute("loginform", lf); // um Kunde nach Registration gleich einzuloggen
        
        } else {
    		ErrorMessage em = new ErrorMessage("error.values");
            rq.setAttribute("errormessage", em);
            forward = "userfailed";
            rq.setAttribute("kontoform", kf);
            rq.setAttribute("userform", uf);
    	}
    	
    	} else {
    		ErrorMessage em = new ErrorMessage("error.berechtigung");
    		em.setLink("anmeldungkonto.do");
            rq.setAttribute("errormessage", em);
    	}
    	
    	cn.close();
    	return mp.findForward(forward);
    }
    
    /**
     * ändern eines Kontos vorbereiten
     */
    public ActionForward prepareModifyKonto(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    		String forward = "failure";
        	
    		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");    		
            KontoForm kf = (KontoForm)fm;
            Konto k = new Konto();
            Text cn = new Text();
            Countries countriesInstance = new Countries();
            Auth auth = new Auth();
            
            if (auth.isLogin(rq)) {
            
    		if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
    			
    		k = new Konto(ui.getKonto().getId(),cn.getConnection());
    		
    		forward = "success";
    		
    		if (auth.isAdmin(rq)) {
    			kf.setFaxusername(k.getFaxusername());
    			kf.setFaxpassword(k.getFaxpassword());
    			kf.setGbvrequesterid(k.getGbvrequesterid());
    		}
    		
    		//TODO: folgender Quelltext mittels Konto abhandeln
            kf.setKid(k.getId());
            if (k.getBibliotheksname() !=null) {kf.setBiblioname(k.getBibliotheksname().trim());} else {kf.setBiblioname(k.getBibliotheksname());}
            if (k.getIsil() !=null) {kf.setIsil(k.getIsil().trim());} else {kf.setIsil(k.getIsil());}          
            if (k.getAdresse() !=null) {kf.setAdresse(k.getAdresse().trim());} else {kf.setAdresse(k.getAdresse());}
            if (k.getAdressenzusatz() !=null) {kf.setAdressenzusatz(k.getAdressenzusatz().trim());} else {kf.setAdressenzusatz(k.getAdressenzusatz());}
            if (k.getPLZ() !=null) {kf.setPLZ(k.getPLZ().trim());} else {kf.setPLZ(k.getPLZ());}
            if (k.getOrt() !=null) {kf.setOrt(k.getOrt().trim());} else {kf.setOrt(k.getOrt());}
            kf.setLand(k.getLand());
            if (k.getTelefon() !=null) {kf.setTelefon(k.getTelefon().trim());} else {kf.setTelefon(k.getTelefon());}
            kf.setFaxno(k.getFaxno()); // Bibliothekar nur Leserecht!
            if (k.getFax_extern()!=null) {kf.setFax_extern(k.getFax_extern().trim());} else {kf.setFax_extern(k.getFax_extern());} // Bibliothekar Schreibrecht!
            if (k.getBibliotheksmail() !=null) {kf.setBibliotheksmail(k.getBibliotheksmail().trim());} else {kf.setBibliotheksmail(k.getBibliotheksmail());}
            if (k.getDbsmail() !=null) {kf.setDbsmail(k.getDbsmail().trim());} else {kf.setDbsmail(k.getDbsmail());}
            kf.setGbvbenutzername(k.getGbvbenutzername());
			kf.setGbvpasswort(k.getGbvpasswort());
            kf.setPopfaxend(k.getPopfaxend());
            kf.setEzbid(k.getEzbid());
            kf.setZdb(k.isZdb());
            
            // Kontoeinstellungen
            kf.setBilling(k.getBilling());
            kf.setBillingtype(k.getBillingtype());
            kf.setAccounting_rhythmvalue(k.getAccounting_rhythmvalue());
            kf.setAccounting_rhythmday(k.getAccounting_rhythmday());
            kf.setAccounting_rhythmtimeout(k.getAccounting_rhythmtimeout());
            kf.setThreshold_value(k.getThreshold_value()); // Billingschwellwert?
            kf.setMaxordersu(k.getMaxordersu());
            kf.setMaxordersutotal(k.getMaxordersutotal());
            kf.setMaxordersj(k.getMaxordersj());
            kf.setOrderlimits(k.getOrderlimits());
            kf.setUserlogin(k.isUserlogin());
            kf.setUserbestellung(k.isUserbestellung()); // SUBITO
            kf.setGbvbestellung(k.isGbvbestellung()); // GBV
            kf.setKontostatus(k.isKontostatus()); // Bibliothekar nur Leserecht!
            kf.setKontotyp(k.getKontotyp()); // Bibliothekar nur Leserecht!
            kf.setDefault_deloptions(k.getDefault_deloptions());
            kf.setEdatum(k.getEdatum()); // Alle nur Leserecht!
            kf.setGtc(k.getGtc()); // Alle nur Leserecht!
            kf.setGtcdate(k.getGtcdate()); // Alle nur Leserecht!
            
    		} else {
                ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
                rq.setAttribute("errormessage", em);
    		}
		    } else {
		        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
		        rq.setAttribute("errormessage", em);
			}
            
            List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());                
            kf.setCountries(allPossCountries);	
            
            	
                
            	rq.setAttribute("kontoform", kf);

        cn.close();
        return mp.findForward(forward);
    }
    
    
    /**
     * Kontoangaben ändern
     */
    public ActionForward modifyKonto(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    		String forward = "failure";
        	
    		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");    		
            KontoForm kf = (KontoForm)fm;
            Konto k = new Konto();
            Check check = new Check();
            Auth auth = new Auth();
            Text cn = new Text();
            Countries countriesInstance = new Countries();
         // GBV-Thread-Management
        	ThreadedWebcontent gbvthread = new ThreadedWebcontent();
    		ExecutorService executor = Executors.newCachedThreadPool();
    		Future<String> gbvcontent = null;
            
    		if (auth.isLogin(rq)) {
    		if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
    			
    			// ggf. Leerschläge entfernen
    			if (kf.getBibliotheksmail()!=null) kf.setBibliotheksmail(kf.getBibliotheksmail().trim());
    			if (kf.getDbsmail()!=null) kf.setDbsmail(kf.getDbsmail().trim());
    			
                if (check.isMinLength(kf.getBiblioname(), 1) && check.isMinLength(kf.getAdresse(), 1) &&
                		check.isMinLength(kf.getPLZ(), 4) && check.isMinLength(kf.getOrt(), 1) &&
                		check.isMinLength(kf.getLand(), 2) && check.isMinLength(kf.getTelefon(), 4) &&
                    	check.isEmail(kf.getBibliotheksmail()) && check.isEmail(kf.getDbsmail()) ) {
                
                // mit einem neuen Thread ist hier die Timeout-Kontrolle besser
                if (kf.getGbvbenutzername()!=null && !kf.getGbvbenutzername().equals("")) {
                	String gbvlink = "http://gso.gbv.de/login/FORM/REQUEST?DBS_ID=2.1&DB=2.1&USER_KEY=" +
                	kf.getGbvbenutzername() + "&PASSWORD=" + kf.getGbvpasswort() + "&REDIRECT=http%3A%2F%2Fgso.gbv.de%2Frequest%2FFORCETT%3DHTML%2FDB%3D2.1%2FFORM%2FCOPY%3FPPN%3D185280552%26LANGCODE%3DDU";
                	gbvthread.setLink(gbvlink);
                	gbvcontent = executor.submit(gbvthread);
                }
                
                boolean checkgbv = true; // grundsätzlich auf true stellen. Wird erst geprüft, wenn tatsächlich ein Benutzername eingegeben wird
                // falls beim GBV ein Benutzername angegeben wurde
                if (kf.getGbvbenutzername()!=null && !kf.getGbvbenutzername().equals("")) {
                	try {
                	checkgbv = false;
                	String gbvanswer = "";
                	gbvanswer = gbvcontent.get(3, TimeUnit.SECONDS);
                	if (gbvanswer.contains("Abmelden")) {
                		checkgbv = true; 
                	} else {
                		forward = "subitofailed"; // Pfad i.O.
                    	kf.setMessage("error.gbv_values");
                        List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());                
                        kf.setCountries(allPossCountries);
                	}
                	} catch (TimeoutException e) {
                		log.info("GBV-Timeout: " + e.toString());
        				forward = "subitofailed"; // Pfad i.O.
                    	kf.setMessage("error.gbv_timeout");
                        List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());                
                        kf.setCountries(allPossCountries);
        			} catch (Exception e) {
        				log.error("GBV-Thread failed in modifykonto: " + e.toString());                		
                	} finally {
                		// ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
    	    			gbvcontent.cancel(true);
                	}
                	
                }
                
                if (checkgbv == true) {
    			
    			forward = "success";

    			k = new Konto(ui.getKonto().getId(),cn.getConnection());
    			
    			if (auth.isAdmin(rq)) {
        			if (kf.getFaxno().equals("")){k.setFaxno(null);}else{k.setFaxno(kf.getFaxno());} // Faxno == null => Deaktivierung von Faxserver-Cronjob
        			k.setFaxusername(kf.getFaxusername()); // nur Admin
        			k.setFaxpassword(kf.getFaxpassword()); // nur Admin
        			k.setGbvrequesterid(kf.getGbvrequesterid()); // nur Admin
    			}

    			if (kf.getGbvbenutzername()==null || kf.getGbvbenutzername().equals("")){k.setGbvbenutzername(null);}else{k.setGbvbenutzername(kf.getGbvbenutzername().trim());} // null wegen Anzeigelogik
    			if (kf.getGbvpasswort()==null || kf.getGbvpasswort().equals("")){k.setGbvpasswort(null);}else{k.setGbvpasswort(kf.getGbvpasswort().trim());} // null wegen Anzeigelogik
    			if (kf.getBiblioname() !=null) {k.setBibliotheksname(kf.getBiblioname().trim());} else {k.setBibliotheksname(kf.getBiblioname());}
//    			if (kf.getIsil() !=null) {k.setIsil(correctIsil(kf.getIsil().trim()));} else {k.setIsil(kf.getIsil());}// Sollte nicht mehr nötig sein da bereits in Formbean abgehandelt
    			k.setIsil(kf.getIsil());
    			if (kf.getAdresse() !=null) {k.setAdresse(kf.getAdresse().trim());} else {k.setAdresse(kf.getAdresse());}
    			if (kf.getAdressenzusatz() !=null) {k.setAdressenzusatz(kf.getAdressenzusatz().trim());} else {k.setAdressenzusatz(kf.getAdressenzusatz());}
    			if (kf.getPLZ() !=null) {k.setPLZ(kf.getPLZ().trim());} else {k.setPLZ(kf.getPLZ());}
    			if (kf.getOrt() !=null) {k.setOrt(kf.getOrt().trim());} else {k.setOrt(kf.getOrt());}
    			k.setLand(kf.getLand());
    			if (kf.getTelefon() !=null) {k.setTelefon(kf.getTelefon().trim());} else {k.setTelefon(kf.getTelefon());}
    			if (kf.getFax_extern() !=null) {k.setFax_extern(kf.getFax_extern().trim());} else {k.setFax_extern(kf.getFax_extern());}
    			if (kf.getBibliotheksmail() !=null) {k.setBibliotheksmail(kf.getBibliotheksmail().trim());} else {k.setBibliotheksmail(kf.getBibliotheksmail());}
    			if (kf.getDbsmail() !=null) {k.setDbsmail(kf.getDbsmail().trim());} else {k.setDbsmail(kf.getDbsmail());}
    			k.setEzbid(kf.getEzbid());
    			k.setZdb(kf.isZdb());
    			k.setUserlogin(kf.isUserlogin());
    			k.setUserbestellung(kf.isUserbestellung()); // SUBITO
    			k.setGbvbestellung(kf.isGbvbestellung()); // GBV
    			k.setOrderlimits(kf.getOrderlimits());
    			k.setMaxordersu(kf.getMaxordersu());
    			k.setMaxordersutotal(kf.getMaxordersutotal());
    			k.setMaxordersj(kf.getMaxordersj());
    			
    			k.setDefault_deloptions(kf.getDefault_deloptions());

    			k.update(cn.getConnection());
    			k.setCn(cn.getConnection());
		
    			// veränderte Kontos in Session legen, damit neue Angaben angezeigt werden. Z.B. kontochange
    			ui.setKonto(k);
    			List<Konto> kontolist = ui.getKonto().getLoginKontos(ui.getBenutzer(), cn.getConnection());            	
            	ui.setKontoanz(kontolist.size()); // Anzahl Kontos im UserInfo hinterlegen
            	ui.setKontos(kontolist);    			
    			rq.getSession().setAttribute("userinfo", ui);
    			
                } // benötigt keine separate else-Schlaufe, da der Negativfall schon oben behandelt wurde
    			
                } else {
                	forward = "missingvalues";
                	kf.setMessage("error.values");
                    List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());             
                    kf.setCountries(allPossCountries);
                }
            
    		} else {
                ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
                rq.setAttribute("errormessage", em);
    		}
		    } else {
		        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
		        rq.setAttribute("errormessage", em);
			}
            
            	
                
            	rq.setAttribute("kontoform", kf);

        cn.close();   
        return mp.findForward(forward);
    }
    
    /**
     * Sich eine Faxlieferung erneut per Mail zustellen lassen
     */
    public ActionForward resendfaxtomail(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	ReSendFaxForm fileForm = (ReSendFaxForm)fm;
    	String name = fileForm.getFile().getFileName();
    	
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) {
//    		Wird ein Fax-to Mail File übertragen (PDF)?
            if (name.contains(".pdf")){
            	String popfaxid = name.substring(0, name.indexOf(".pdf"));
            	
            	Fax f = new Fax();
            	//Wird ein Eintrag der Popfaxid in der DB gefunden und kann auch gelöscht werden?
            	if (f.deletePopfaxId(f.getConnection(), popfaxid)==0){ 
            		ErrorMessage em = new ErrorMessage("error.resendfax", "searchfree.do");
                    rq.setAttribute("errormessage", em);
            	} else {
            		Message m = new Message("message.resendfax", fileForm.getFile().getFileName(), "searchfree.do");
                    rq.setAttribute("message", m);
                	forward = "success";
            	}            	
            // Kein PDF!	
            } else {
            	ErrorMessage em = new ErrorMessage("error.faxfile", "searchfree.do");
                rq.setAttribute("errormessage", em);
            }
        // Nicht Administrator!
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}
    	return mp.findForward(forward);
    }
    
    
    /**
     * Auflistung Kontos fuer Admins zum Kontos deaktivieren oder reaktivieren
     */
    public ActionForward listKontos(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {

    	if (auth.isAdmin(rq)) {
    		KontoForm kf = new KontoForm();
    		Konto k = new Konto();
    		kf.setKontos(k.getAllKontos(k.getConnection()));
    		k.close();
    		rq.setAttribute("kontoform", kf);
    		if (kf.getKontos()==null){
    			ErrorMessage em = new ErrorMessage("error.missingaccounts", "searchfree.do");
                rq.setAttribute("errormessage", em);
    		} else{
    			forward = "success";
    		}
    		
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}
    	
    	
    	return mp.findForward(forward);
    }

    /**
     * Admins können einen Kontotypen ändern Beim Wechsel auf den typ Gratis wird das Kontoablaufdatum auf null gesetzt
     * 
     */
    public ActionForward changeKontoTyp(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) {
    		try {
    			KontoForm kf = (KontoForm) fm;
    			Konto k = new Konto();   		
    			k = new Konto(Long.valueOf(kf.getKid()), cn.getConnection());
    			k.setKontotyp(kf.getKontotyp());
    			if (k.getKontotyp()==0) k.setExpdate(null); // Gratiskontos laufen nicht ab!
    			k.update(cn.getConnection());
				
    			Message em = new Message("message.changekontotyp", k.getBibliotheksname(), "kontoadmin.do?method=listKontos");
	            rq.setAttribute("message", em);
	            forward = "success";
			} catch (Exception e) {
				log.error("changeKonto" + e.toString());
				ErrorMessage em = new ErrorMessage("error.changetype", e.getMessage(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("errormessage", em);
			} finally {
				cn.close();
			}
    		
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}

    	return mp.findForward(forward);
    }
    
    /**
     * Admins können Kontostatus ändern
     */
    public ActionForward changeKontoState(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) {
    		try {
    			KontoForm kf = (KontoForm) fm;
				Konto k = new Konto();
				k = new Konto(Long.valueOf(kf.getKid()), cn.getConnection());
				k.setKontostatus(kf.isKontostatus());
				k.update(cn.getConnection());

				Message em = new Message("message.changekontostatus", k.getBibliotheksname(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("message", em);
				forward = "success";

			} catch (Exception e) {
				log.error("changeKontoState: " + e.toString());
				ErrorMessage em = new ErrorMessage("error.changestate", e.getMessage(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("errormessage", em);
			} finally {
				cn.close();
			}
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}

    	return mp.findForward(forward);
    }
    
    /**
     * Admins können Expdate setzen
     */
    public ActionForward setExpDate(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) {
    		try {
    			KontoForm kf = (KontoForm) fm;
				Konto k = new Konto();
				k = new Konto(Long.valueOf(kf.getKid()), cn.getConnection());
				k.setExpdate(kf.getExpdate());
				k.update(cn.getConnection());

				Message em = new Message("message.changeexpdate", k.getBibliotheksname(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("message", em);
				forward = "success";

			} catch (Exception e) {
				log.error("setExpdate: " + e.toString());
				ErrorMessage em = new ErrorMessage("error.timeperiode", e.getMessage(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("errormessage", em);
			} finally {
				cn.close();
			}
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}

    	return mp.findForward(forward);
    }
    
    /**
     * Admins können ExpDateServer setzen
     */
    public ActionForward setExpDateServer(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
    	
    	if (auth.isLogin(rq)) {
    	if (auth.isAdmin(rq)) {
    		try {
    			KontoForm kf = (KontoForm) fm;
				Konto k = new Konto();
				k = new Konto(Long.valueOf(kf.getKid()), cn.getConnection());
				k.setPopfaxend(kf.getPopfaxend());
				k.update(cn.getConnection());

				Message em = new Message("message.changeexpdatefax", k.getBibliotheksname(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("message", em);
				forward = "success";

			} catch (Exception e) {
				log.error("setExpDate: " + e.toString());
				ErrorMessage em = new ErrorMessage("error.timeperiodefax", e.getMessage(), "kontoadmin.do?method=listKontos");
				rq.setAttribute("errormessage", em);
			} finally {
				cn.close();
			}
    	} else {
            ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
            rq.setAttribute("errormessage", em);
		}
	    } else {
	        ErrorMessage em = new ErrorMessage("error.timeout", "searchfree.do");
	        rq.setAttribute("errormessage", em);
		}

    	return mp.findForward(forward);
    }
    
    /**
     * Admins können Rechnungen vorbereiten
     */
    public ActionForward prepareBillingText(ActionMapping mp, ActionForm fm,
			HttpServletRequest rq, HttpServletResponse rp) {

		String forward = "failure";
		Text cn = new Text();
		Auth auth = new Auth();

		// Benutzer eingeloggt?
		if (auth.isLogin(rq)) {

			if (auth.isAdmin(rq)) {
				try {
					BillingForm bf = (BillingForm) fm;
					Konto k = new Konto();
					k = new Konto(bf.getKontoid(), cn.getConnection());

					// Rechnung vorbereiten
					KontoAdmin ka = new KontoAdmin();
					bf = ka.prepareBillingText(k, cn.getConnection(), null, bf);
					bf.setKonto(k);

					rq.setAttribute("billingform", bf);

					forward = "success";

				} catch (Exception e) {
					log.error("prepareBilling: " + e.toString());
					ErrorMessage em = new ErrorMessage(
							"error.preparebilling", e.getMessage(), "kontoadmin.do?method=listKontos");
					rq.setAttribute("errormessage", em);
				} finally {
					cn.close();
				}
			} else {
				ErrorMessage em = new ErrorMessage(
						"error.berechtigung",
						"searchfree.do");
				rq.setAttribute("errormessage", em);
			}
		}

		return mp.findForward(forward);
	}
    
    /**
	 * Admins können Rechnungen versenden
	 */
    public ActionForward sendBill(ActionMapping mp, ActionForm fm,
			HttpServletRequest rq, HttpServletResponse rp) {

		String forward = "failure";
		Text cn = new Text();
		Auth auth = new Auth();
		// Benutzer eingeloggt?
		if (auth.isLogin(rq)) {

			if (auth.isAdmin(rq)) {
				try {
					BillingForm bf = (BillingForm) fm;
					Konto k = new Konto();
					MHelper mh = new MHelper();

					k = new Konto(bf.getKontoid(), cn.getConnection());

					String[] to = new String[1];
					to[0] = k.getBibliotheksmail();

					// Rechnung speichern
					KontoAdmin ka = new KontoAdmin();
					bf = ka.prepareBillingText(k, cn.getConnection(), null, bf);
					bf.getBill().save(cn.getConnection());

					// Mail versenden
					mh.sendMail(to,
							"Rechnung für ihr Konto auf doctor-doc.com", bf
									.getBillingtext());

					// Meldung verfassen
					Message m = new Message("message.sendbill", k.getBibliotheksname() + "\n\n" + 
						bf.getBillingtext(), "listbillings.do?method=listBillings&kid="+k.getId());
					rq.setAttribute("message", m);

					forward = "success";

				} catch (Exception e) {
					log.error("sendBilling: " + e.toString());
					ErrorMessage em = new ErrorMessage(
							"error.sendbilling", e.getMessage(), "kontoadmin.do?method=listKontos");
					rq.setAttribute("errormessage", em);
				} finally {
					cn.close();
				}
			} else {
				ErrorMessage em = new ErrorMessage(
						"error.berechtigung",
						"searchfree.do");
				rq.setAttribute("errormessage", em);
			}
		}

		else {
			ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
			ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
			rq.setAttribute("errormessage", em);
		}

		return mp.findForward(forward);
	}
    
}
