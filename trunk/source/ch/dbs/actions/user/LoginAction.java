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

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Encrypt;
import util.ReadSystemConfigurations;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.LoginForm;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserForm;
import ch.dbs.form.UserInfo;
import ch.dbs.login.Gtc;


/**
 * LoginAction checkt die eingegebenen Logininformationen auf ihre Gültigkeit
 * und erstellt bei Erfolg (User existiert in der DB) das Bean userinfo 
 * {@link ch.dbs.form.UserInfo}
 * 
 * @author Pascal Steiner
 */
public final class LoginAction extends Action {
	
	private static final SimpleLogger log = new SimpleLogger(LoginAction.class);
	
    public ActionForward execute(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        LoginForm lf = (LoginForm) fm;
        OrderForm pageForm = new OrderForm(lf); // falls Artikelangaben aus Linkresolver vorhanden
        
        String forward = "failure";
        Text cn = new Text();
        Auth auth = new Auth();
        
        // User mittels Logininfos aus der DB heraussuchen
        Encrypt e = new Encrypt();        
        AbstractBenutzer u = new AbstractBenutzer();
        ArrayList<UserInfo> uil = u.login(lf.getEmail(), e.makeSHA(lf.getPassword()), cn.getConnection());
        
        // Wurde nur ein User mit den Logininfos gefunden, Bean userinfo erstellen
        if (uil.size()==1){

        	u = uil.get(0).getBenutzer();
        	// Last-Login Datum beim Benutzer hinterlegen
        	u.updateLastuse(u, cn.getConnection());
  
//        	 Prüfung, ob GTC (General Terms and Conditions) in der aktuellen Version akzeptiert wurden
        	Gtc gtc = new Gtc();
            Text t = gtc.getCurrentGtc(cn.getConnection()); // Text der aktuellen Version
        	if (ReadSystemConfigurations.isGTC() &&
        	   (u.getGtc() == null || !u.getGtc().equals(t.getInhalt())) ) {
            	forward = "gtc"; // falls ein User die GTC noch nicht akzeptiert hat              	
            } else {
            	forward = "success";
            }
        	       	
        	// Veraenderter Benutzer wieder in UserInfo und Session speichern. Ab hier keine Veränderungen mehr am UserInfo!
        	if (u.getClass().isInstance(new Bibliothekar())||u.getClass().isInstance(new Benutzer())){
        		// Damit die Auswahl bei nur einem konto nicht dargestellt wird (Gilt nicht für Admins)
        		if (uil.get(0).getKontos().size()==1){
        			uil.get(0).setKontoanz(1); 
        		}
        	}
        	uil.get(0).setBenutzer(u);
        	rq.getSession().setAttribute("userinfo", uil.get(0)); // userinfo in Session schreiben
        	
        	if (auth.isAdmin(rq)) forward = "success"; // Admin braucht keine GTC-Prüfung
        	
        	// Check ob sich der Benutzer an mehr als an 1 Konto anmelden kann und Werte in UserInfo schreiben
        	if (!forward.equals("gtc")) {
				if (uil.get(0).getKontos().size() == 1) {
					uil.get(0).setKonto(uil.get(0).getKontos().get(0));
					forward = "success";
				} else { // Sind mehr als ein Konto im UserInfo, muss das Konto vom Benutzer ausgewählt werden
					forward = "konto";
				}
			}

        }
        
        // Benutzerauswahl falls mehrere berechtigte Benutzer mit denselben Loginangaben gefunden wurden
        if (uil.size()>1){
        	forward = "chooseuser";
        	lf.setUserinfolist(uil);
        	rq.getSession().setAttribute("authuserlist", lf);
        }
        
        // Keine gültgen Logininformationen
        if (uil.size()==0){
        	log.info("Wrong Login-Credentials!: "  + lf.getEmail());
        	ErrorMessage em = new ErrorMessage();
            if (lf.isResolver()==false) {
            	em.setError("error.username_pw");
            	em.setLink("login.do");
            	} else {
            	em.setError("error.username_pw_linkresolver");
            	em.setLink("login.do");
            	}
            rq.setAttribute("errormessage", em);
        }        
        
        // Angaben aus Linkresolver
        if (lf.isResolver()==true) {
        	// hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
        	rq.setAttribute("orderform", pageForm); // Übergabe in jedem Fall
        	if (forward.equals("success") && auth.isBenutzer(rq)) forward="order"; // Die Bestellberechtigung wird in der Methode prepare geprüft!
        	if (forward.equals("success") && !auth.isBenutzer(rq)) forward="checkavailability"; // Bibliothekar oder Admin auf Checkavailability
        }
        // Übernahme von Userangaben aus Link aus Bestellform-Email
        if (lf.getKundenemail()!=null && !lf.getKundenemail().equals("")) {
        	UserForm uf = new UserForm(lf);
        	rq.setAttribute("userform", uf);
        	if (forward.equals("success") && !auth.isBenutzer(rq)) forward="adduser"; // Bibliothekar oder Admin auf Checkavailability
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        cn.close();
        
        return mp.findForward(forward);
    }
    

    
}