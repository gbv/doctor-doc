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

package ch.dbs.cronjobs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.FaxHelper;
import ch.dbs.entity.Konto;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.Message;


/**
 * Popfax Cronjob-Handling
 * {@link ch.dbs.form.UserInfo}
 * 
 * @author Pascal Steiner
 */
public final class PopFax extends Action {
    
	/**
	 * Ceckt für alle Kontos welche Popfax aboniert haben, ob ein neuer Fax eingetroffen ist. 
	 * Der Fax wird dann per Mail an die im Konto konfigurierte Adresse gesendet.
	 */
	public ActionForward execute(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
        
		Konto kto = new Konto();
		List<Konto> kontos = kto.getFaxserverKontos();
    	FaxHelper fh = new FaxHelper();
    	for (Konto k : kontos) {
//    		System.out.println("Zur zeit wird dieses Konto bearbeitet: " + k.getBibliotheksname());
    		fh.retrieveIncomingFaxList(k);
    	}
    	// Verhindert endlose Fehlermeldungen in catalina.out
    	ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("login");
        rq.setAttribute("ActiveMenus", mf);
        
    	Message msg = new Message("success.heading");
    	rq.setAttribute("message", msg);
    	
    	return mp.findForward("success");
    }
    
    
}