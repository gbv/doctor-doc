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

import java.sql.Connection;

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
public class Supplier extends DispatchAction {
    
    public ActionForward edit(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        // if activated on system level, access will be restricted to paid only
        if (auth.isPaidOnly(rq)) {
            return mp.findForward(Result.ERROR_PAID_ONLY.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        
        final String sid = rq.getParameter("sid");
        
        final Text cn = new Text();
        
        try {
            
            // make sure sid is not null and is editable
            if (editable(Long.valueOf(sid), ui, cn.getConnection())) {
                
                Lieferanten sup = new Lieferanten();
                
                sup = sup.getLieferantFromLid(Long.valueOf(sid), cn.getConnection());
                
                rq.setAttribute("supplier", sup);
                
                // navigation: set 'account/konto' tab as active
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("konto");
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                
                forward = "edit";
                
            } else {
                final ErrorMessage m = new ErrorMessage("error.berechtigung");
                m.setLink("listsuppliers.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), m);
            }
            
        } finally {
            cn.close();
        }
        
        return mp.findForward(forward);
    }
    
    public ActionForward create(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        // if activated on system level, access will be restricted to paid only
        if (auth.isPaidOnly(rq)) {
            return mp.findForward(Result.ERROR_PAID_ONLY.getValue());
        }
        
        final Lieferanten sup = new Lieferanten();
        rq.setAttribute("supplier", sup);
        
        // navigation: set 'account/konto' tab as active
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
        
        return mp.findForward("create");
    }
    
    public ActionForward save(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        // if activated on system level, access will be restricted to paid only
        if (auth.isPaidOnly(rq)) {
            return mp.findForward(Result.ERROR_PAID_ONLY.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final SupplierForm sf = (SupplierForm) form;
        
        final Text cn = new Text();
        
        try {
            
            // update
            if (sf.getLid() != null) {
                
                // make sure lid is editable
                if (editable(sf.getLid(), ui, cn.getConnection())) {
                    
                    Lieferanten sup = new Lieferanten();
                    sup = sup.getLieferantFromLid(sf.getLid(), cn.getConnection());
                    
                    // set Lieferanten values from SupplierForm
                    sup.setFormValues(sup, sf, ui);
                    
                    sup.update(sup, cn.getConnection());
                    
                    rq.setAttribute("supplier", sup);
                    
                    // navigation: set 'account/konto' tab as active
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("konto");
                    rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                    
                    forward = Result.SUCCESS.getValue();
                    
                } else {
                    final ErrorMessage m = new ErrorMessage("error.berechtigung");
                    m.setLink("listsuppliers.do");
                    rq.setAttribute(Result.ERRORMESSAGE.getValue(), m);
                }
                
            } else {
                // save new supplier
                final Lieferanten sup = new Lieferanten();
                
                // set Lieferanten values from SupplierForm
                sup.setFormValues(sup, sf, ui);
                
                sup.save(sup, cn.getConnection());
                
                forward = Result.SUCCESS.getValue();
            }
            
        } finally {
            cn.close();
        }
        
        return mp.findForward(forward);
    }
    
    public ActionForward delete(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        // if activated on system level, access will be restricted to paid only
        if (auth.isPaidOnly(rq)) {
            return mp.findForward(Result.ERROR_PAID_ONLY.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        
        final String sid = rq.getParameter("sid");
        
        final Text cn = new Text();
        
        try {
            
            // make sure sid is not null and is editable
            if (deleteable(sid, ui, cn.getConnection())) {
                
                // delete supplier
                final Lieferanten sup = new Lieferanten();
                sup.delete(sid, cn.getConnection());
                
                // navigation: set 'account/konto' tab as active
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("konto");
                rq.setAttribute("activemenu", mf);
                
                forward = Result.SUCCESS.getValue();
                
            } else {
                final ErrorMessage m = new ErrorMessage("error.berechtigung");
                m.setLink("listsuppliers.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), m);
            }
            
        } finally {
            cn.close();
        }
        
        return mp.findForward(forward);
    }
    
    private boolean editable(final Long sid, final UserInfo ui, final Connection cn) {
        
        boolean result = false;
        
        if (sid != null) {
            
            // get original supplier from lid
            Lieferanten l = new Lieferanten();
            l = l.getLieferantFromLid(sid, cn);
            
            // make sure we got an supplier back...
            if (l.getLid() != null) {
                
                // we have a matching KID => individual supplier
                if ((l.getKid() != null && l.getKid().equals(ui.getKonto().getId()))
                // suppliers from the own country
                        || ui.getKonto().getLand().equals(l.getCountryCode())) {
                    result = true;
                }
            }
        }
        
        return result;
    }
    
    private boolean deleteable(final String sid, final UserInfo ui, final Connection cn) {
        
        boolean result = false;
        
        if (sid != null) {
            
            // get original supplier from lid
            Lieferanten l = new Lieferanten();
            l = l.getLieferantFromLid(Long.valueOf(sid), cn);
            
            // we have a matching KID => individual supplier
            if (l.getKid() != null && l.getKid().equals(ui.getKonto().getId())) {
                result = true;
            }
        }
        
        return result;
    }
    
}
