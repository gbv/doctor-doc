//  Copyright (C) 2012  Markus Fischer, Pascal Steiner
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//  Contact: info@doctor-doc.com

package ch.dbs.actions.suppliers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import util.Auth;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.SupplierForm;
import ch.dbs.form.UserInfo;
import enums.Result;

/**
 * Prepares the list of suppliers for a given account to be edited and
 * configured.
 */
public class Settings extends DispatchAction {
    
    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // if activated on system level, access will be restricted to paid only
        if (auth.isPaidOnly(rq)) {
            return mp.findForward(Result.ERROR_PAID_ONLY.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final SupplierForm sf = (SupplierForm) form;
        
        // catching session timeouts
        if (auth.isLogin(rq)) {
            // access restricted to librarians and admins only
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                
                final Lieferanten sup = new Lieferanten();
                final Text cn = new Text();
                
                try {
                    
                    final List<SupplierForm> privSuppliers = sup.getPrivates(ui.getKonto().getId(), cn.getConnection());
                    
                    // we do not have any private suppliers...
                    if (privSuppliers.isEmpty()) {
                        // ...there is only one logical display option
                        sf.setShowprivsuppliers(false);
                        sf.setShowpubsuppliers(true);
                    }
                    
                    // update account with new settings
                    sf.updateAccount(sf, ui.getKonto(), cn.getConnection());
                    
                    // set back updated UserInfo / account into session
                    ui.getKonto().setShowprivsuppliers(sf.isShowprivsuppliers());
                    ui.getKonto().setShowpubsuppliers(sf.isShowpubsuppliers());
                    rq.getSession().setAttribute("userinfo", ui);
                    
                    // trigger update message
                    sf.setChangedsettings(true);
                    
                    rq.setAttribute("form", sf);
                    
                    // navigation: set 'account/konto' tab as active
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("konto");
                    rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                    
                    forward = Result.SUCCESS.getValue();
                    
                } finally {
                    cn.close();
                }
            } else {
                final ErrorMessage m = new ErrorMessage("error.berechtigung");
                m.setLink("searchfree.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), m);
            }
            
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu(Result.LOGIN.getValue());
            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
        }
        
        return mp.findForward(forward);
    }
    
}
