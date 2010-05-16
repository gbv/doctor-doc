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

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import util.Auth;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import ch.dbs.login.Gtc;

public final class GtcAction extends DispatchAction {
 
	/**
     * Prüft ob AGB in einer bestimmten Version angenommen wurden
     * 
     * @author Markus Fischer
     */
    public ActionForward accept(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	OrderForm pageForm = (OrderForm) form; // falls Artikelangaben aus Linkresolver vorhanden
    	
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
        // Sicherstellen, dass die Action nur von Benutzern nach Prüfung aufgerufen wird    
        if (auth.isLogin(rq)) {
        	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            
            Gtc gtc = new Gtc();
            Text t = gtc.getCurrentGtc(cn.getConnection()); // Text der aktuellen Version
            
            Date d = new Date(); // aktuelles Datum setzen
            ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datum = fmt.format(d);
            
            ui.getBenutzer().setGtc(t.getInhalt());
            ui.getBenutzer().setGtcdate(datum);
            AbstractBenutzer b = new AbstractBenutzer();
            b.updateUser(ui.getBenutzer(), cn.getConnection());
            rq.getSession().setAttribute("userinfo", ui); // userinfo in Request aktualisieren
            
        	if (ui.getKonto()!=null){ // Prüfung ob mehrere Konti vorhanden
        		forward = "success";
        		
        	} else {
        		forward = "konto";
        	}
            

        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        if (pageForm.isResolver()==true) {
        	// hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
        	rq.setAttribute("orderform", pageForm); // Übergabe in jedem Fall
        	if (forward.equals("success") && auth.isBenutzer(rq)) forward="order"; // Die Bestellberechtigung wird in der Methode prepare geprüft!
        	if (forward.equals("success") && !auth.isBenutzer(rq)) forward="checkavailability"; // Bibliothekar oder Admin auf Checkavailability
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        cn.close();
        return mp.findForward(forward);
    }
    
    public ActionForward decline(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	String forward = "failure";
    	Auth auth = new Auth();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        
        if (auth.isLogin(rq)) {
            forward = "decline";
           
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }            
    
    
    
}
