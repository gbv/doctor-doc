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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Vector;

import ch.dbs.login.Gtc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.Encrypt;
import util.MHelper;
import util.PasswordGenerator;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.entity.VKontoBenutzer;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.KontoForm;
import ch.dbs.form.LoginForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.OrderStatistikForm;
import ch.dbs.form.OverviewForm;
import ch.dbs.form.SearchesForm;
import ch.dbs.form.UserForm;
import ch.dbs.form.UserInfo;

public final class UserAction extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(UserAction.class);
	

    public ActionForward stati(ActionMapping mp, ActionForm form,
                              HttpServletRequest rq, HttpServletResponse rp) {
    	
     String forward = "failure";
     Auth auth = new Auth();

//    Make sure user is logged in
      if (auth.isLogin(rq)) {
    	  
    	  forward = "success";

          ActiveMenusForm mf = new ActiveMenusForm();
          mf.setActivemenu("uebersicht");
          rq.setAttribute("ActiveMenus", mf);
          
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
     *  Übersicht Zeigt Bestellungen an. Kann durch Benutzer festgelegt werden, nach welchem 
     *  Feld er ab- und aufsteigend sortieren will
     */
    public ActionForward overview(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        
        OverviewForm of = (OverviewForm) fm;        
        OrderStatistikForm osf = new OrderStatistikForm();
        Bestellungen b = new Bestellungen();
        
        Check check = new Check();
        Auth auth = new Auth();
        Text cn = new Text();
        
        if (auth.isLogin(rq)) {
            forward = "success";
            UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
            
// hier wird geprüft, ob Suchkriterien eingegeben wurden, oder die Sortierung einer Suche vorliegt 
        if (!checkIfInputIsNotEmpty(of) && !of.isS()) {
        	
        	// Auflistung möglicher Bestellstati um Statuswechsel dem User / Bibliothekar / Admin anzubieten
        	// wird auch für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
            of.setStatitexts(cn.getAllTextPlusKontoTexts(new Texttyp("Status", cn.getConnection()), ui.getKonto().getId(), cn.getConnection()));
            
            of = check.checkDateRegion(of, 1, ui.getKonto().getTimezone()); // angegebener Zeitraum prüfen, resp. Defaultbereich von 1 Monat zusammenstellen
            of = check.checkSortOrderValues(of); // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
            of = check.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of); //Check, damit nur gültige Sortierkriterien daherkommen
            of = check.checkOrdersSortCriterias(of); // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden

            // Benutzer dürfen nur ihre eigenen Bestellungen sehen
            if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)){
            	if (of.getFilter()==null) {
            		of.setBestellungen(b.getAllUserOrders(ui.getBenutzer(), of.getSort(), of.getSortorder(), 
            					of.getFromdate(), of.getTodate(), cn.getConnection()));
            	} else {
            		of.setBestellungen(b.getAllUserOrdersPerStatus(ui.getBenutzer(), of.getFilter(), of.getSort(), 
            					of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection()));
            	}
            }
//            	 Bibliothekare dürfen nur Bestellungen ihrers Kontos sehen
            	else {
            		if (of.getFilter()==null) {
            			of.setBestellungen(b.getOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), cn.getConnection()));
            			osf.setAuflistung(b.countOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), cn.getConnection()));
            		} else {            			
            			of.setBestellungen(b.getOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection()));
            			osf.setAuflistung(b.countOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(), of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection()));
            		}
                }

            // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
            Date d = new Date(); // aktuelles Datum setzen
            ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
            String datum = fmt.format(d, ui.getKonto().getTimezone());
            int year_now = Integer.parseInt(datum);
            int year_start = 2007;
            
            ArrayList<Integer> years = new ArrayList<Integer>();
            year_now++;
            for (int j=0;year_start<year_now;j++){
            	years.add(j,year_start);
            	year_start++;
            }
            of.setYears(years);
            
            // Suchfelder bestimmen
            TreeMap<String, String> result = composeSortedLocalisedOrderSearchFields(rq);            
            rq.setAttribute("sortedSearchFields", result);
            
            rq.setAttribute("overviewform", of);
            rq.setAttribute("orderstatistikform", osf);
            
            cn.close();
        
    	} else { // Umleitung auf Suche
    	forward = "search";
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
     * Wechselt den Status einer Bestellung, unter Berücksichtigung allfällig bestehender Sortierung und Filterung
     */
    public ActionForward changestat(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	OverviewForm of = (OverviewForm) form;
    	OrderState orderstate = new OrderState();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isLogin(rq)) {
        	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
        	
        	Text t = new Text();
        	
            try {            	
            	Text status = new Text(t.getConnection(), of.getTid());
            	Bestellungen b = new Bestellungen(t.getConnection(), of.getBid());
            	if (b!=null && status!=null){
            		orderstate.changeOrderState(b, ui.getKonto().getTimezone(), status, null, ui.getBenutzer().getEmail(), t.getConnection());  
            		forward = "success";
                	rq.setAttribute("overviewform", of);
            	}
            	
            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("changestat: " + e.toString());

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
        return mp.findForward(forward);
    }
    
    /**
     * Wechselt den Status einer Bestellung, unter Berücksichtigung allfällig bestehender Sortierung und Filterung
     */
    public ActionForward changenotes(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	OverviewForm of = (OverviewForm) form;
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isLogin(rq)) {       
        	
        	Text cn = new Text();
        	
            try {
            	Bestellungen b = new Bestellungen(cn.getConnection(), of.getBid());
            	b.setNotizen(of.getNotizen());
            	if (b!=null){ 
            		b.update(cn.getConnection());
            	}
            	forward = "success";
            	rq.setAttribute("overviewform", of);

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("changenotes: " + e.toString());

            } finally {
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
     * Konto wechseln
     */
    public ActionForward changekonto(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

//    Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
      KontoForm kf = (KontoForm) form;
      OrderForm pageForm = new OrderForm(kf);
      String forward = "failure";
      Auth auth = new Auth();
      if (auth.isLogin(rq)) {
          UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
          Konto k = new Konto();
          ui.setKonto(new Konto(kf.getKid(), k.getConnection()));
          k.close();
          rq.getSession().setAttribute("userinfo", ui);
          
          if (auth.isUserlogin(rq) && auth.isUserAccount(rq)) {
        	  forward = "success";
          } else {
        	  rq.getSession().setAttribute("userinfo", null); // userinfo aus Session löschen
        	  ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
              rq.setAttribute("errormessage", em);
          }

          ActiveMenusForm mf = new ActiveMenusForm();
          mf.setActivemenu("suchenbestellen");
          rq.setAttribute("ActiveMenus", mf);
          
      } else {
    	  ActiveMenusForm mf = new ActiveMenusForm();
          mf.setActivemenu("login");
          rq.setAttribute("ActiveMenus", mf);
          ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
          rq.setAttribute("errormessage", em);
      }
      
      if (kf.isResolver()==true) {
      	// hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
      	rq.setAttribute("orderform", pageForm); // Übergabe in jedem Fall
      	if (forward.equals("success") && auth.isBenutzer(rq)) forward="order"; // Die Bestellberechtigung wird in der Methode prepare geprüft!
      	if (forward.equals("success") && !auth.isBenutzer(rq)) forward="checkavailability"; // Bibliothekar oder Admin auf Checkavailability
      }
      if (kf.getKundenemail()!=null && !kf.getKundenemail().equals("")) {
    	// Übergabe von Userangaben von Link aus Bestellform-Email
    	if (forward.equals("success") && !auth.isBenutzer(rq)) forward = "adduser";
    	UserForm uf = new UserForm(kf);
    	rq.setAttribute("userform", uf);
      }
      
      return mp.findForward(forward);
    }
    
    /**
     * Benutzer setzen wenn Logininfos Login bei verschiedenen konten zulassen
     */
    public ActionForward setuser(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

    	LoginForm authuserlist = (LoginForm)rq.getSession().getAttribute("authuserlist");
    	UserInfo ui = new UserInfo();
    	LoginForm lf = (LoginForm) form; // Infos mit welchem Benutzer gearbeitet werden soll
      
    	String forward = "failure";
    	ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("login");
        
        Auth auth = new Auth();
        Text cn = new Text();        
    	
    	//	Ueberprüfung ob Auswahl aus LoginForm tatsächlich authorisierte Benutzer sind
    	for (UserInfo authlist: authuserlist.getUserinfolist()){
    		if (lf.getUserid().equals(authlist.getBenutzer().getId())){
    			
    			//Benutzer und Kontos in ui setzen
    			ui.setBenutzer(authlist.getBenutzer());
    			ui.setKontos(authlist.getKontos());
    			rq.getSession().setAttribute("userinfo", ui);
    			
    			//Bei nur einem Konto dieses gleich setzen
    			if (ui.getKontos().size()==1){
    				ui.setKonto(ui.getKontos().get(0));
    				// Last-Login Datum beim Benutzer hinterlegen
    	        	AbstractBenutzer u = new AbstractBenutzer();
    	        	u = ui.getBenutzer();
    	        	u.updateLastuse(u, ui.getKonto(), cn.getConnection());   				
    				
    				Gtc g = new Gtc();
    				if (g.isAccepted(ui.getBenutzer(), cn.getConnection())){
    					forward = "success";
    					mf.setActivemenu("suchenbestellen");
    				} else {
    					forward = "gtc";
    				}
    			}
    			//Falls Benutzer unter mehreren Kontos arbeiten darf weiterleitung zur Kontoauswahl
    			if (ui.getKontos().size()>1){
    				// Last-Login Datum beim Benutzer hinterlegen
    	        	AbstractBenutzer u = new AbstractBenutzer();
    	        	u = ui.getBenutzer();
    	        	u.updateLastuse(u, ui.getKontos().get(0), cn.getConnection());
    				forward = "kontochoose";
    			}
    		}
    	}
    	
    	cn.close();
    	
    	// Fehlermeldung bereitstellen falls mittels URL-hacking versucht wurde zu manipulieren
    	if (forward.equals("failure")){
    		rq.getSession().setAttribute("userinfo", null);
    		ErrorMessage em = new ErrorMessage("error.hack", "login.do");
            rq.setAttribute("errormessage", em);
            log.info("setuser: prevented URL-hacking! " + ui.getBenutzer().getEmail());
    	}
      
      // Angaben vom Linkresolver
      if (lf.isResolver()==true) {
      	// hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
      	rq.setAttribute("orderform", lf); // Übergabe in jedem Fall
      	if (forward.equals("success") && auth.isBenutzer(rq)) forward="order"; // Die Bestellberechtigung wird in der Methode prepare geprüft!
      	if (forward.equals("success") && !auth.isBenutzer(rq)) forward="checkavailability"; // Bibliothekar oder Admin auf Checkavailability
      }
      
      rq.setAttribute("ActiveMenus", mf);
      return mp.findForward(forward);
    }
    
    /**
     * Dient dazu beim Aufruf von prepareAddUser ab journalorder.jsp, alle Bestellangaben zu behalten...
     */
    public ActionForward keepOrderDetails(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
        	forward = "success";
        	OrderForm pageForm = (OrderForm) form;
        	rq.getSession().setAttribute("ofjo", pageForm);
        	
        	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
        	ui.setKeepordervalues(true); // damit kann in prepareAddUser kontrolliert werden, dass odervalues dabei sind!
        	rq.getSession().setAttribute("userinfo", ui);
          
      } else {    	  
    	  if (auth.isLogin(rq)) {
          ErrorMessage em = new ErrorMessage("error.berechtigung");
          rq.setAttribute("errormessage", em);
    	  } else {
    		  ActiveMenusForm mf = new ActiveMenusForm();
              mf.setActivemenu("login");
              rq.setAttribute("ActiveMenus", mf);
              ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
              rq.setAttribute("errormessage", em);  
    	  }
      }
      return mp.findForward(forward);
    }

    
    /**
     * Hinzufuegen eines neuen Benutzers vorbereiten
     */
    public ActionForward prepareAddUser(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

        String forward = "failure";
        ErrorMessage em = new ErrorMessage();
        Text cn = new Text();
        Countries countriesInstance = new Countries();
        Konto kontoInstance = new Konto();
        Auth auth = new Auth();
        
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
      	    UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");

      	    if (ui.isKeepordervalues()==true) { // hier wird festegestellt, ob keepOrderDetails durchlaufen wurde
      	        	ui.setKeepordervalues(false);
      	        	ui.setKeepordervalues2(false);
      	        	rq.getSession().setAttribute("userinfo", ui); // keepordervalues aus Session löschen, damit nicht etwas hängen bleibt...
      	        	ui.setKeepordervalues2(true);
      	    } else {
      	    	rq.getSession().setAttribute("ofjo", null); // ggf. orderform unterdrücken, welches noch in der Session hängt...
      	    }
      	    
            List<Konto> allPossKontos = kontoInstance.getAllAllowedKontosAndSelectActive(ui, cn.getConnection());

            ArrayList<KontoForm> lkf = new ArrayList<KontoForm>();
            for (int i=0;i<allPossKontos.size();i++){
            	KontoForm kf = new KontoForm();
            	kf.setKonto((Konto)allPossKontos.get(i));
            	lkf.add(kf);
            }
            
            List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
            
            ui.setKontos(allPossKontos);
            ui.setCountries(allPossCountries);
            forward = "success";
            rq.setAttribute("ui", ui);
           
      } else {    	  
    	  if (auth.isLogin(rq)) {
          em = new ErrorMessage("error.berechtigung");
    	  } else {
    		  ActiveMenusForm mf = new ActiveMenusForm();
              mf.setActivemenu("login");
              rq.setAttribute("ActiveMenus", mf);
              em = new ErrorMessage("error.timeout", "login.do");
    	  }
      }
      rq.setAttribute("errormessage", em); // unterdrückt falsche Fehlermeldungen aus Bestellform...
      cn.close();
      return mp.findForward(forward);
    }
    
    
    /**
     * Benutzereinstellungen ändern
     */
    public ActionForward changeuserdetails(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

        String forward = "failure";
        ErrorMessage em = new ErrorMessage();
        Countries countriesInstance = new Countries();
        Text cn = new Text();
        Auth auth = new Auth();
        VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();
        
        if (auth.isLogin(rq)) {
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
      	    UserForm uf = (UserForm)form;
      	    UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
      	    AbstractBenutzer u = new AbstractBenutzer();
            u = u.getUser(uf.getBid(), cn.getConnection());

            // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
            if (u!=null && vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), u.getId(), cn.getConnection())) {

            uf.setUser(u);
            
            // selektiert alle Konten unter denen ein Kunde angehängt ist
            List<Konto> kontolist = ui.getKonto().getKontosForBenutzer(u, cn.getConnection());
            List<Konto> allPossKontos = ui.getKonto().getLoginKontos(ui.getBenutzer(), cn.getConnection());
            for (int i=0;i<kontolist.size();i++){
                Konto k = (Konto)kontolist.get(i);
                for (int y=0;y<allPossKontos.size();y++){
                	Konto uik = (Konto)allPossKontos.get(y);
                	if (uik.getId().longValue()==k.getId().longValue()){
                		uik.setSelected(true);
                		allPossKontos.set(y, uik);
                	}
                }
            }
            ArrayList<KontoForm> lkf = new ArrayList<KontoForm>();
            for (int i=0;i<allPossKontos.size();i++){
            	KontoForm kf = new KontoForm();
            	kf.setKonto((Konto)allPossKontos.get(i));
            	lkf.add(kf);
            }
            
            List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());            
            ui.setKontos(allPossKontos);
            ui.setCountries(allPossCountries);
            forward = "success";
            rq.setAttribute("ui", ui);
            rq.setAttribute("userform", uf);
            
       } else {
    	  forward = "failure";
          em = new ErrorMessage("error.hack");
          em.setLink("searchfree.do?activemenu=suchenbestellen");
          log.info("changeuserdetails: prevented URL-hacking! " + ui.getBenutzer().getEmail());
       }
            
      } else {
    	  em = new ErrorMessage("error.berechtigung");
    	  em.setLink("searchfree.do?activemenu=suchenbestellen");
      }
          
      } else {
    	  ActiveMenusForm mf = new ActiveMenusForm();
          mf.setActivemenu("login");
          rq.setAttribute("ActiveMenus", mf);
          em = new ErrorMessage("error.timeout", "login.do");
      }
      rq.setAttribute("errormessage", em); // unterdrückt falsche Fehlermeldungen aus Bestellform...
      cn.close();
      return mp.findForward(forward);
    }
    
    /**
     * Generiert ein neues Passwort und teilt dem Benutzer dieses Mail mit
     */
    public ActionForward pwreset(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	LoginForm lf = (LoginForm) form;
        String forward = "failure";
        Text cn = new Text();
        MessageResources messageResources = getResources(rq);
        ErrorMessage em = new ErrorMessage();
        AbstractBenutzer u = new AbstractBenutzer();
        u = u.getUserFromEmail(lf.getEmail(), cn.getConnection());
        //Check ob der Benutzer berechtigt ist sein Passwort zurückzusetzen
        if (u != null) {
			if (u.isLoginopt()|| u.getRechte()>1) {
				// Passwort erzeugen
				PasswordGenerator p = new PasswordGenerator(8);
				String pw = p.getRandomString();
				// Passwort codieren und in DB speichern
				Encrypt e = new Encrypt();
				u.setPassword(e.makeSHA(pw));
				Konto tz = new Konto(); // we need this for setting a default timezone
				u.updateUser(u, tz, cn.getConnection());
				// Benutzer per Mail das neue Passwort mitteilen
				String[] recipients = new String[1];
				recipients[0] = u.getEmail();
				StringBuffer msg = new StringBuffer();
				msg.append(messageResources.getMessage("resend.email.intro"));
				msg.append("\012\012");
				msg.append(pw);
				msg.append("\012\012");
				msg.append(messageResources.getMessage("resend.email.greetings"));
				msg.append("\012");
				msg.append(messageResources.getMessage("resend.email.team"));
				msg.append("\040");
				msg.append(ReadSystemConfigurations.getApplicationName());
				MHelper m = new MHelper();
				m.sendMail(recipients, messageResources.getMessage("resend.email.subject") + "\040" + ReadSystemConfigurations.getApplicationName(),
						msg.toString());
				forward = "success";
				rq.setAttribute("message", new Message("message.pwreset", "login.do"));

			} else {
				em.setError("error.pwreset");
				em.setLink("login.do");
				rq.setAttribute("errormessage", em);
				log.warn(lf.getEmail() + " ist registriert und hat versucht sich unberechtigterweise eine Loginberechtigung per Email zu schicken!");
			}
		} else {
			em.setError("error.unknown_email");
			em.setLink("login.do");
			rq.setAttribute("errormessage", em);
			log.warn(lf.getEmail() + " (unbekannt) hat versucht sich unberechtigterweise eine Loginberechtigung per Email zu schicken!");
		}
        cn.close();
        return mp.findForward(forward);
    }
    
    /**
	 * Uebersicht Benutzer des Kontos
	 */
    public ActionForward showkontousers(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        UserForm uf = new UserForm();
        Auth auth = new Auth();
        
        if (auth.isLogin(rq)) {
            forward = "success";
            UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            // Bei URL-hacking: User sehen nur sich selber
            if (auth.isBenutzer(rq)) {
            	ArrayList<AbstractBenutzer> ul = new ArrayList<AbstractBenutzer>();
            	ul.add(ui.getBenutzer());
            	uf.setUsers(ul);
            }
            // Bibliothekare sehen alle Benutzer eines Kontos
            if (auth.isBibliothekar(rq)||auth.isAdmin(rq)) {
            	Text t = new Text();
            	uf.setUsers(ui.getBenutzer().getKontoUser(ui.getKonto(), t.getConnection()));
            	t.close();
            }
            rq.setAttribute("userform", uf);
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
     * neuer User anlegen abschliessen, User bearbeiten
     */
    public ActionForward modifykontousers(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text cn = new Text();
        Check check = new Check();
        Auth auth = new Auth();
        VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();
        
        if (auth.isLogin(rq)) {
        	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
        	UserForm uf = (UserForm)fm;
        	
        	// ggf. Leerschläge entfernen
        	if (uf.getEmail()!=null) uf.setEmail(uf.getEmail().trim());
        	
        	boolean name = check.isMinLength(uf.getName(), 2);
        	boolean vorname = check.isMinLength(uf.getVorname(), 2);
        	boolean email = check.isEmail(uf.getEmail());
        	boolean kont = uf.getKontos()!=null;
        	boolean land = check.isMinLength(uf.getLand(), 2);
        	
        	//Test, ob alle Sollangaben gemacht wurden
        	if (name && vorname && email && kont && land){
        		
        		String[] kontos = (String[]) uf.getKontos();
        		
                forward = "success";
                AbstractBenutzer u = new Benutzer();
                
                if (uf.getBid()!=null){
                	u = u.getUser(uf.getBid(), cn.getConnection());
                }
                           
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
                u.setLoginopt(uf.getLoginopt());
                u.setKontostatus(uf.isKontostatus());
                Encrypt e = new Encrypt();
                u.setPassword(e.makeSHA(uf.getPassword()));
                u.setUserbestellung(uf.getUserbestellung()); // SUBITO
                u.setGbvbestellung(uf.isGbvbestellung()); // GBV
                u.setKontovalidation(uf.getKontovalidation());
                u.setBilling(uf.getBilling());
                u.setGtc(uf.getGtc());
                u.setGtcdate(uf.getGtcdate());
                
                if (uf.getBid()==null) {
                	Date d = new Date(); 
            		ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String datum = fmt.format(d, ui.getKonto().getTimezone());
                    u.setDatum(datum);
                	uf.setBid(u.saveNewUser(u, ui.getKonto(), cn.getConnection()));
                // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
                } else if (vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), uf.getBid(), cn.getConnection())) {
                	u.setId(uf.getBid());
                	u.updateUser(u, ui.getKonto(), cn.getConnection());
                } else {
                	ErrorMessage em = new ErrorMessage();
                	forward = "failure";
                    em = new ErrorMessage("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute("errormessage", em);
                    log.info("modifykontousers: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
                
                // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
                if (u.getId()==null || vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), u.getId(), cn.getConnection())) {
                
                if (u.getId()!=null) vKontoBenutzer.deleteAllKontoEntries(u, cn.getConnection());
                for (int i=0;i<kontos.length;i++){                	
                     Konto k = new Konto(Long.parseLong(kontos[i]),cn.getConnection());
                     vKontoBenutzer.setKontoUser(u, k, cn.getConnection());
                    }
                
            	AbstractBenutzer b = new AbstractBenutzer(uf);
            	uf.setUser(b);
                
                if (uf.isKeepordervalues2()==true && (rq.getSession().getAttribute("ofjo")!=null)) { // d.h. hier wurde ein User angelegt aus der journalorder.jsp => mit Bestellangaben wieder direkt zurück
                	uf.setKeepordervalues2(false);
                	forward = "returntojournalorder";
                	OrderForm pageForm = new OrderForm();
                	pageForm = (OrderForm) rq.getSession().getAttribute("ofjo");
                	pageForm.setForuser(uf.getBid().toString()); // Vorselektion neu angelegter User
                	pageForm.setUid(uf.getBid().toString()); // Vorselektion neu angelegter User
                	if (pageForm.getUid()==null) pageForm.setUid("0"); // sonst kracht es auf jsp
                	rq.setAttribute("ofjo", pageForm);
                	
                	if (pageForm.getOrigin()!=null) {
                		forward = "returntojournalsave";
                	}
                	rq.getSession().setAttribute("ofjo", null);
                }
                rq.setAttribute("userform", uf);
              }
                
        	} else {
        		ErrorMessage em = new ErrorMessage();
        		if (!vorname) em.setError("error.vorname");
        		if (!name) em.setError("error.name");
        		if (!email) em.setError("error.mail");
        		if (!kont) em.setError("error.kontos");
        		if (!land) em.setError("error.land");
                rq.setAttribute("errormessage", em);
            	AbstractBenutzer b = new AbstractBenutzer(uf);
            	uf.setUser(b);
                rq.setAttribute("userform", uf);
                forward = "missing";
        	}
        } else {
            ErrorMessage em = new ErrorMessage("error.berechtigung");
            rq.setAttribute("errormessage", em);
        }

        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        cn.close();
        Message m = new Message("message.modifyuser");
        m.setLink("listkontousers.do?method=showkontousers&activemenu=bibliokunden");
        rq.setAttribute("message", m);
        return mp.findForward(forward);
    }
    /**
     * User aus Konto löschen,oder gänzlich wenn er keinem Konto mehr angehört
     * 
     */
    public ActionForward deleteKontousers(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Text cn = new Text();
        Auth auth = new Auth();
        VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();
        
        if (auth.isLogin(rq)) {
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
        	Benutzer u = new Benutzer();
            UserForm uf = (UserForm)fm; 
            UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            Konto k = ui.getKonto();
        if (vKontoBenutzer.isUserFromKonto(k.getId(), uf.getBid(), cn.getConnection())) {
            forward = "success";
            
            if (uf.getBid()!=null){
            	u.setId(uf.getBid());
            	VKontoBenutzer vkb = new VKontoBenutzer();
            	vkb.deleteSingleKontoEntry(u, k, cn.getConnection());
            } 
            
            List<Konto> rkv = ui.getKonto().getLoginKontos(u, cn.getConnection());
            AbstractBenutzer b = new AbstractBenutzer();
            if (rkv.isEmpty())b.deleteUser(u, cn.getConnection());
            
            rq.setAttribute("userform", uf);
        } else {
        	ErrorMessage em = new ErrorMessage();
            em = new ErrorMessage("error.hack");
            em.setLink("searchfree.do?activemenu=suchenbestellen");
            rq.setAttribute("errormessage", em);
            log.info("modifykontousers: prevented URL-hacking! " + ui.getBenutzer().getEmail());        	
        }
        
        } else {
            ErrorMessage em = new ErrorMessage("error.berechtigung");
            rq.setAttribute("errormessage", em);
        }

        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        cn.close();
        
        Message m = new Message("message.deleteuser");
        m.setLink("listkontousers.do?method=showkontousers&activemenu=bibliokunden");
        rq.setAttribute("message", m);
        
        return mp.findForward(forward);
    }
    
	/**
	 * Bereitet die Suche nach Bestellungen vor
	 */
	public ActionForward prepareSearch(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		Auth auth = new Auth();
		Text cn = new Text();
		
		if (auth.isLogin(rq)) {
		
		OverviewForm of = (OverviewForm) fm;
		OrderStatistikForm osf = new OrderStatistikForm();
		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq) ) {
            forward = "success";
            
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
            
            Check check = new Check();
            of = check.checkDateRegion(of, 3, ui.getKonto().getTimezone()); // angegebener Zeitraum prüfen, resp. Defaultbereich von 3 Monaten zusammenstellen

            // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
            Date d = new Date(); // aktuelles Datum setzen
            ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
            String datum = fmt.format(d, ui.getKonto().getTimezone());
            int year_now = Integer.parseInt(datum);
            int year_start = 2007;
            
            ArrayList<Integer> years = new ArrayList<Integer>();
            year_now++;
            for (int j=0;year_start<year_now;j++){
            	years.add(j,year_start);
            	year_start++;
            }
            of.setYears(years);
            
            // Suchfelder bestimmen
            TreeMap<String, String> result = composeSortedLocalisedOrderSearchFields(rq);            
            rq.setAttribute("sortedSearchFields", result);
            
            Texttyp tty = new Texttyp();
        	long id = 2; // Bestellstati
        	tty.setId(id);
            of.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
            id = 7; // Waehrungen
            tty.setId(id);
            of.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
            cn.close();
            
            rq.setAttribute("overviewform", of);
            rq.setAttribute("orderstatistikform", osf);
            
        } else {
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
	 * Suche nach Bestellungen
	 */
	public ActionForward search(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		String forward = "failure";
		Auth auth = new Auth();
		Bestellungen b = new Bestellungen();
		
		if (auth.isLogin(rq)) {
		
		OverviewForm of = (OverviewForm) fm;
		OrderStatistikForm osf = new OrderStatistikForm();
		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
		Check check = new Check();
		Text cn = new Text();
		
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq) ) {
            forward = "result";
            
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
            
         // wird für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
            of.setStatitexts(cn.getAllTextPlusKontoTexts(new Texttyp("Status", cn.getConnection()), ui.getKonto().getId(), cn.getConnection()));

            of = check.checkDateRegion(of, 3, ui.getKonto().getTimezone()); // angegebener Zeitraum prüfen, resp. Defaultbereich von 3 Monaten zusammenstellen
            of = check.checkSortOrderValues(of); // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
            of = check.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of); //Check, damit nur gültige Sortierkriterien daherkommen
            of = check.checkOrdersSortCriterias(of); // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden

            // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
            Date d = new Date(); // aktuelles Datum setzen
            ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
            String datum = fmt.format(d, ui.getKonto().getTimezone());
            int year_now = Integer.parseInt(datum);
            int year_start = 2007;
            
            ArrayList<Integer> years = new ArrayList<Integer>();
            year_now++;
            for (int j=0;year_start<year_now;j++){
            	years.add(j,year_start);
            	year_start++;
            }
            of.setYears(years);
            
//          Suchfelder bestimmen
            TreeMap<String, String> result = composeSortedLocalisedOrderSearchFields(rq);            
            rq.setAttribute("sortedSearchFields", result);
            
            Texttyp t = new Texttyp();
        	long id = 2; // Bestellstati
        	t.setId(id);
            of.setStatitexts(cn.getAllTextPlusKontoTexts(t, ui.getKonto().getId(), cn.getConnection()));
            id = 7; // Waehrungen
            t.setId(id);
            of.setWaehrungen(cn.getAllTextPlusKontoTexts(t, ui.getKonto().getId(), cn.getConnection()));
            
            String date_from = of.getYfrom() + "-" + of.getMfrom() + "-" + of.getDfrom() + " 00:00:00";
            String date_to = of.getYto() + "-" + of.getMto() + "-" + of.getDto() + " 24:00:00";
            
            ArrayList<SearchesForm> searches = new ArrayList<SearchesForm>();
            
            if (of.isS()) { // Suchkriterien aus Session holen
            	searches = ui.getSearches();
            }
            
            // hier wird ein Array aus den Suchbedingungen aus der JSP zusammengestellt, falls etwas eingegeben wurde...
            // überschreibt Session, falls eine neue Suche eingegeben wurde
            if (checkIfInputIsNotEmpty(of)) {
            	if (of.getInput1() != null && !of.getInput1().equals("")) searches.add(getSearchForm(of.getValue1(), of.getCondition1(), of.getInput1(),of.getBoolean1()));
            	if (of.getInput2() != null && !of.getInput2().equals("")) searches.add(getSearchForm(of.getValue2(), of.getCondition2(), of.getInput2(),of.getBoolean2()));
            }
            
            if (searches.size() != 0) { // Suche, falls etwas im searchesForm liegt...
            	
//            	 Suchkriterien in userinfo und Session legen, damit Sortierung funktioniert
            	ui.setSearches(searches);
            	of.setS(true); // Variable Suche auf true setzen
            	rq.getSession().setAttribute("userinfo", ui);
            	
            	PreparedStatement pstmt = null;        
            try {
            	pstmt = composeSearchLogic(searches, ui.getKonto(), of.getSort(), of.getSortorder(), date_from, date_to, cn.getConnection());
            	of.setBestellungen(b.searchOrdersPerKonto(pstmt));
            } catch (Exception e) { // Fehler aus Methode abfangen
                // zusätzliche Ausgabe von Fehlermeldung, falls versucht wurde Bestellungen nach Kunde > 3 Monate zu suchen (Datenschutz)
                forward = "failure";
                ErrorMessage em = new ErrorMessage("error.system", "searchorder.do?method=prepareSearch");
                rq.setAttribute("errormessage", em);
                log.error("search: " + e.toString());
            } finally {
            	if (pstmt != null) {
            		try {
            			pstmt.close();
            		} catch (SQLException e) {
            			log.error("search: " + e.toString());
            		}
            	}
            }
            PreparedStatement pstmtb = null;
            try {
    		pstmtb = composeCountSearchLogic(searches, ui.getKonto(), of.getSort(), of.getSortorder(), date_from, date_to, cn.getConnection()); 
            osf.setAuflistung(b.countSearchOrdersPerKonto(pstmtb));
            } catch (Exception e) {
                log.error("composeCountSearchLogic: " + e.toString());
            } finally {
            	if (pstmtb != null) {
            		try {
            			pstmtb.close();
            		} catch (SQLException e) {
            			log.error("composeCountSearchLogic: " + e.toString());
            		}
            	}
            }
            } else {
            	of.setS(false); // Suchvariable auf false setzen
            }
            
            cn.close();
            
            rq.setAttribute("overviewform", of);
            rq.setAttribute("orderstatistikform", osf);
            
        } else {
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
     * Stellt den MYSQL für die Suche zusammen / Suchlogik
     */
    public PreparedStatement composeSearchLogic(ArrayList<SearchesForm> searches, Konto k, String sort, String sortorder, String date_from, String date_to, Connection cn) throws Exception {
    	
    	Bestellungen b = new Bestellungen();
    	
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND ( "); // letzte Klammer wichtig: sonst kann mit OR Bestellungen anderer Kontos ausgelesen werden...!!!
    	
    	for (int i=0;i<searches.size();i++){
    		
    		SearchesForm sf = new SearchesForm();
    		sf = (SearchesForm) searches.get(i);
    		
    		sf = searchMapping(sf); // ggf. Suchwerte in Datenbankwerte übersetzen...
    		
    		// Suche zusammenstellen
    		if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
    			sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()) + "AGAINST (? IN BOOLEAN MODE) ");
    		} else {
    			sql.append("`" + composeSearchLogicTable(sf.getField(), sf.getCondition()) + "`\040" + 
  			  	composeSearchLogicCondition(sf.getCondition()) + "\040" + "?" + "\040");
    		}   		
    		
    		if (i+1<searches.size()) sql.append(composeSearchLogicBoolean(sf.getBool()) + "\040"); // Boolsche-Verknüpfung anhängen solange noch weiter Abfragen kommen...
    		
    	}
    	
    	sql.append(b.sortOrder(") AND orderdate >= '" + date_from + "' AND orderdate <= '" + date_to + "' ORDER BY ", sort, sortorder)); // erste Klammer wichtig: sonst kann mit OR Bestellungen anderer Kontos ausgelesen werden...!!!
    	
    	PreparedStatement pstmt = cn.prepareStatement(sql.toString());
    	pstmt.setString(1, k.getId().toString());
    	
    	boolean stop = false; // bricht die Suche ab, falls nach Name || Vorname ausserhalb des erlaubten Datumbereiches (3 Monate) gesucht wird...
    	for (int i=0;i<searches.size() && !stop;i++){
    		
    		SearchesForm sf = new SearchesForm();
    		sf = (SearchesForm) searches.get(i);
    		
//    		 Kontrolle, ob mit Name || Vorname || Email || Systembemerkungen ausserhalb des erlaubten Datumbereiches (3 Monate) gesucht wird...
    		if (composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("name") || 
    			composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("vorname") ||
    			composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("mail") || 
    			composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("systembemerkung")) {
    			if (checkAnonymize(date_from, k.getTimezone())) {
    				stop = true;
    				pstmt = null;
    			}
    			if (pstmt==null) throw new Exception("Datenschutz: unerlaubte Suchperiode (max. 3 Monate) bei Name, Vorname, Email, Bemerkungen");
    		}
    		
    		String truncation = ""; // Normalerweise keine Trunkierung
    		if (sf.getCondition().contains("contains")) truncation = "%"; // Trunkierung für LIKE
    		
    		if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
    		pstmt.setString(2+i, composeSearchInBooleanMode(sf.getInput())); // Suche für IN BOOLEAN MODE zusammenstellen
    		} else {
    		pstmt.setString(2+i, truncation + sf.getInput() + truncation);	
    		}
    	}
    	
    	return pstmt;
    		
    }
    
    /**
     * Stellt den MYSQL für die Suche zusammen / Suchlogik
     */
    private PreparedStatement composeCountSearchLogic(ArrayList<SearchesForm> searches, Konto k, String sort, String sortorder, String date_from, String date_to, Connection cn) throws Exception {
    	Bestellungen b = new Bestellungen();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND ( ");
    	
    	for (int i=0;i<searches.size();i++){
    		
    		SearchesForm sf = new SearchesForm();
    		sf = (SearchesForm) searches.get(i);
    		
    		sf = searchMapping(sf); // ggf. Suchwerte in Datenbankwerte übersetzen...
    		
    		// Suche zusammenstellen
    		if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
    			sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()) + "AGAINST (? IN BOOLEAN MODE) "); // Achtung ist immer als "contains" trunkiert...
    		} else {
    			sql.append("`" + composeSearchLogicTable(sf.getField(), sf.getCondition()) + "`\040" + 
  			  	composeSearchLogicCondition(sf.getCondition()) + "\040" + "?" + "\040");
    		}   		
    		
    		if (i+1<searches.size()) sql.append(composeSearchLogicBoolean(sf.getBool()) + "\040"); // Boolsche-Verknüpfung anhängen solange noch weiter Abfragen kommen...
    		
    	}
    	
    	sql.append(b.sortOrder(") AND orderdate >= '" + date_from + "' AND orderdate <= '" + date_to + "' ORDER BY ", sort, sortorder));
    	
    	PreparedStatement pstmt = cn.prepareStatement(sql.toString());
    	pstmt.setString(1, k.getId().toString());
    	
    	for (int i=0;i<searches.size();i++){
    		
    		SearchesForm sf = new SearchesForm();
    		sf = (SearchesForm) searches.get(i);
    		
    		String truncation = ""; // Normalerweise keine Trunkierung
    		if (sf.getCondition().contains("contains")) truncation = "%"; // Trunkierung für LIKE
    		
    		if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
    		pstmt.setString(2+i, composeSearchInBooleanMode(sf.getInput())); // Suche für IN BOOLEAN MODE zusammenstellen
    		} else {
    		pstmt.setString(2+i, truncation + sf.getInput() + truncation);	
    		}
    	}
    	
    	return pstmt;
    		
    }
    
    /**
     * Übersetzt die Suchfelder in die korrekten MYSQL Table-Felder / Suchlogik
     */
    private String composeSearchLogicTable(String field, String condition) {
    	
    	String table = "";
    	
    	if (checkIfSearchFieldIsValid(field)) {
    	
    	// Suche in einzelnen Feldern
    	if (field.equals("searchorders.artikeltitel")) table = "artikeltitel";
    	if (field.equals("searchorders.author")) table = "autor";
    	if (field.equals("searchorders.bemerkungen")) table = "systembemerkung";
    	if (field.equals("searchorders.delformat")) table = "fileformat";
    	if (field.equals("searchorders.heft")) table = "heft";
    	if (field.equals("searchorders.notizen")) table = "notizen";
    	if (field.equals("searchorders.issn")) table = "issn";
    	if (field.equals("searchorders.jahr")) table = "jahr";
    	if (field.equals("searchorders.jahrgang")) table = "jahrgang";
    	if (field.equals("searchorders.gender")) table = "anrede";
    	if (field.equals("searchorders.email")) table = "mail";
    	if (field.equals("searchorders.institut")) table = "institut";
    	if (field.equals("searchorders.department")) table = "abteilung";
    	if (field.equals("searchorders.name")) table = "name";
    	if (field.equals("searchorders.vorname")) table = "vorname";
    	if (field.equals("searchorders.supplier")) table = "bestellquelle";
    	if (field.equals("searchorders.deliveryway")) table = "deloptions";
    	if (field.equals("searchorders.prio")) table = "orderpriority";
    	if (field.equals("searchorders.seiten")) table = "seiten";
    	if (field.equals("searchorders.subitonr")) table = "subitonr";
    	if (field.equals("searchorders.gbvnr")) table = "gbvnr";
    	if (field.equals("searchorders.internenr")) table = "internenr";
    	if (field.equals("searchorders.zeitschrift")) table = "zeitschrift";
    	if (field.equals("searchorders.doi")) table = "doi";
    	if (field.equals("searchorders.pmid")) table = "pmid";
    	if (field.equals("searchorders.isbn")) table = "isbn";
    	if (field.equals("searchorders.typ")) table = "mediatype";
    	if (field.equals("searchorders.verlag")) table = "verlag";
    	if (field.equals("searchorders.buchkapitel")) table = "buchkapitel";
    	if (field.equals("searchorders.buchtitel")) table = "buchtitel";
    	
    	} else { // Suche nach allen Feldern
    		table = "MATCH (artikeltitel,autor,fileformat,heft,notizen,issn,heft,jahr,bestellquelle," +
    				"jahrgang,bestellquelle,deloptions,seiten,subitonr,gbvnr,internenr,zeitschrift,doi,pmid,isbn,mediatype,verlag,buchkapitel,buchtitel) ";
    		if (condition.equals("contains not") || condition.equals("is not")) { // hier wird die Suche von AND zu AND NOT umgekehrt
    			table = "NOT MATCH (artikeltitel,autor,fileformat,heft,notizen,issn,heft,jahr,bestellquelle," +
				"jahrgang,bestellquelle,deloptions,seiten,subitonr,gbvnr,internenr,zeitschrift,doi,pmid,isbn,mediatype,verlag,buchkapitel,buchtitel) ";
    		}
    	}

        return table;
    }
    
    /**
     * Übersetzt die Bedingungen in die korrekten MYSQL-Begriffe / Suchlogik
     */
    private String composeSearchLogicCondition(String condition) {
    	
    	String expression = "";
    	
    	if (condition.equals("contains") || condition.equals("contains not") || condition.equals("is") || 
    		condition.equals("is not") || condition.equals("higher") || condition.equals("smaller") ) {
    		
    	if (condition.equals("contains")) expression = "LIKE";
    	if (condition.equals("contains not")) expression = "NOT LIKE";
    	if (condition.equals("is")) expression = "=";
    	if (condition.equals("is not")) expression = "!=";
    	if (condition.equals("higher")) expression = ">";
    	if (condition.equals("smaller")) expression = "<";
    	
    	} else {
    		expression = "LIKE";
    	}

        return expression;
    }
    
    /**
     * Übersetzt die Verknüpfung in die korrekten MYSQL-Begriffe / Suchlogik
     */
    private String composeSearchLogicBoolean(String bool) {
    	
    	String expression = "";
    	
    	if (bool.equals("and") || bool.equals("and not") || bool.equals("or") ) {
    		
    	if (bool.equals("and")) expression = "AND";
    	if (bool.equals("and not")) expression = "AND NOT";
    	if (bool.equals("or")) expression = "OR";
    	} else {
    		expression = "AND";
    	}

        return expression;
    }
    
    /**
     * Liefert eine sprachabhängig sortierte Liste an Suchfeldern für den Select
     * @param HttpServletRequest rq
     * @return TreeMap<String, String>
     */
    private TreeMap<String, String> composeSortedLocalisedOrderSearchFields(HttpServletRequest rq) {
    
    Vector<String> v = prepareOrderSearchFields();
    TreeMap<String, String> result = new TreeMap<String, String>();
    Locale locale = getLocale(rq);
    MessageResources msgs = getResources(rq);
    String key = null;
    String value = null;
    for( int i = 0; i < v.size(); i++ ) {
    value = "searchorders." + (String) v.elementAt(i);
    key = msgs.getMessage(locale, value);
    result.put(key, value);
    }
    
    return result;
    }
    
    /**
     * Ersetzt Anzeige Werte in die korrekten in der Datenbank gespeicherten Werte
     */
    private SearchesForm searchMapping(SearchesForm sf) {
    	
    	//    	 Mapping bei Prio => express = urgent
		if (sf.getField().equals("Priorität") && sf.getInput().equalsIgnoreCase("express")) sf.setInput("urgent");
		if (sf.getField().equals("Lieferant") && sf.getInput().equalsIgnoreCase("k.A.")) sf.setInput("0");

        return sf;
    }
    
    /**
     * Legt die durchsuchbaren Felder im Select fest.
     * Enthält den hinteren Teil der Einträge im Properties-File
     * z.B. all => searchorders.all=...
     */
    private Vector<String> prepareOrderSearchFields() {
    	
    	Vector<String> v = new Vector<String>();

//        v.add("all"); // hardcodiert in JSP damit dieser Parameter in jeder Sprache zuoberst steht
        v.add("department");
        v.add("gender");
        v.add("artikeltitel");
        v.add("author");
        v.add("bemerkungen");
        v.add("buchkapitel");
        v.add("buchtitel");
        v.add("delformat");
        v.add("doi");
        v.add("email");
        v.add("gbvnr");
        v.add("heft");
        v.add("institut");
        v.add("internenr");
        v.add("notizen");
        v.add("isbn");
        v.add("issn");
        v.add("jahr");
        v.add("jahrgang");
        v.add("supplier");
        v.add("deliveryway");
        v.add("name");
        v.add("pmid");
        v.add("prio");
        v.add("seiten");
        v.add("subitonr");
        v.add("typ");
        v.add("verlag");
        v.add("vorname");
        v.add("zeitschrift");

        return v;
    }
    
    /**
     * setzt die korrekten Plus- oder Minus-Zeichen vor die Begriffe für "IN BOOLEAN MODE"
     */
    private String composeSearchInBooleanMode(String input) {
    	
    	if (input!=null) {
    		
    		Check check = new Check();
    		ArrayList<String> words = new ArrayList<String>();
    		
    		words = check.getAlphanumericWordCharacters(input); // nur Buchstaben inkl. Umlaute und Zahlen zugelassen. Keine Sonderzeichen wie ";.-?!" etc.
    		StringBuffer buf = new StringBuffer();
    		buf.append("");
    		
    		for (int i=0;i<words.size();i++) {
    			if (words.get(i).toString().length()>3) { // Ausschluss von Leerzeichen und Wörten kürzer als drei Buchstaben (min. 4 Buchstaben)
    				buf.append("+" + words.get(i) + "*"); // Jedem Wort ein Plus-Zeichen voranstellen, und Trunkierung anhängen
    				if (i+1<words.size()) buf.append("\040"); // ggf. ein Leerschlag dazwischen setzen
    			}
    		}
    		
    		input = buf.toString().trim(); // ggf. überschüssigen Leerschlag entfernen...
    	
    	}

        return input;
    }
    
    /**
     * Prüft ob Suchwerte eingegeben wurden
     */
    private boolean checkIfInputIsNotEmpty(OverviewForm of) {
    	
    	boolean check = false;
    	
    	if ((of.getInput1()!=null && !of.getInput1().equals("")) || 
    		(of.getInput2()!=null && !of.getInput2().equals(""))) {
    	check = true;	
    	}
    	
        return check;
    }
    
    /**
     * Prüft ob aus der Anzeige zulässige Suchfelder übermittelt werden
     * @param String input
     * @return boolean
     */
    private boolean checkIfSearchFieldIsValid(String input) {
    	
    	boolean check = false;
    	
    	if (input!=null && !input.equals("searchorders.all")) {
    		
    	Vector<String> v = prepareOrderSearchFields();
    	
    	for (int i=0;i<v.size();i++) {
    		String compare = "searchorders." + (String) v.elementAt(i);
    		if (input.equals(compare)) {
    			check = true;
    			break;
    		}
    	}    	
    	}
    	
        return check;
    }
    
    /**
     * Prüft, ob bei einer Suche der Datenschutz (3 Monate) verletzt wird...
     * <p></p>
     * @param Strinf date_from
     * @return true/false
     */
	public boolean checkAnonymize(String date_from, String timezone){ 
		boolean check = false;
		
		if (date_from!=null && ReadSystemConfigurations.isAnonymizationActivated()) {
		Bestellungen b = new Bestellungen();
		Calendar cal = b.stringFromMysqlToCal(date_from);
		Calendar limit = Calendar.getInstance();
		limit.setTimeZone(TimeZone.getTimeZone(timezone));
		limit.add(Calendar.MONTH, -ReadSystemConfigurations.getAnonymizationAfterMonths());
		limit.add(Calendar.DAY_OF_MONTH, -1);
		if (cal.before(limit)) {
			check = true;
		}
		}
        	
		return check;
	} 
	
	/**
	 * stellt ein SearchForm zusammen
	 */
	public SearchesForm getSearchForm(String field, String condition, String input, String bool) {
		
		SearchesForm sf = new SearchesForm();
		sf.setField(field);
		sf.setCondition(condition);
		sf.setInput(input);
		sf.setBool(bool);		
     
        return sf;
    } 

    
}
