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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Auth;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.form.KontoForm;
import ch.dbs.form.UserForm;
import ch.dbs.form.UserInfo;

public final class PrepareUserAddAction extends Action {
 
    
    public ActionForward execute(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    	UserForm uf = (UserForm) form;
    	Countries countriesInstance = new Countries();
    	Konto kontoInstance = new Konto();
    	UserForm ufLoginAction = (UserForm) rq.getAttribute("userform"); // nach Login
    	if (ufLoginAction != null) {
    		uf = ufLoginAction;	
    	}
    	String forward = "failure";
    	Text cn = new Text();
    	Auth auth = new Auth();
    	
    	
    	if (auth.isLogin(rq)) {
    	// bereits eingeloggt => direkt zu modifykontousers.do
    		forward = "adduser";    		
    		UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
    		
    		List<Konto> allPossKontos = kontoInstance.getAllAllowedKontosAndSelectActive(ui, cn.getConnection());

            ArrayList<KontoForm> lkf = new ArrayList<KontoForm>();
            for (int i=0;i<allPossKontos.size();i++){
            	KontoForm kf = new KontoForm();
            	kf.setKonto((Konto)allPossKontos.get(i));
            	lkf.add(kf);
            }
            
            List <Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
            
            AbstractBenutzer b = new AbstractBenutzer(uf);
            uf.setUser(b);
            
            uf .setAddFromBestellformEmail(true); // steuert die korrekte Überschrift in modifykontousers.jsp
            
            ui.setKontos(allPossKontos);
            ui.setCountries(allPossCountries);
            rq.setAttribute("ui", ui);
    		
    	} else {  
    	// nicht eingeloggt => zu LoginAction	
	    	forward = "login";    	
    	}
    	
    	rq.setAttribute("userform", uf);
    	cn.close();
    	return mp.findForward(forward);     	

    }            
    
    
    
}
