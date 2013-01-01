//  Copyright (C) 2012  Markus Fischer
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

package ch.dbs.actions.maintenance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import util.Auth;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.MaintenanceForm;
import ch.dbs.form.Message;
import ch.dbs.form.UserInfo;
import enums.Result;

/**
 * List the possible options for the maintenance of a given account.
 */
public class BulkOperations extends DispatchAction {
    
    public ActionForward deleteorders(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
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
        
        final MaintenanceForm operation = (MaintenanceForm) form;
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final Text cn = new Text();
        
        try {
            
            if (operation.isConfirmed()) {
                // execute operation
                final int deleted = operation.deleteOrders(ui, cn.getConnection());
                
                // set result from DELETE back into form
                operation.setNumerOfRecords(deleted);
                
                final Message msg = new Message();
                msg.setMessage("maintenance.numberOfRecordsDeleted");
                msg.setSystemMessage(String.valueOf(operation.getNumerOfRecords()));
                msg.setLink("maintenance.do");
                rq.setAttribute("message", msg);
                
                forward = Result.SUCCESS.getValue();
            } else {
                // force confirmation of operation
                forward = "confirm";
                
                // get number of records involved
                operation.setNumerOfRecords(operation.countDeleteOrders(ui, cn.getConnection()));
                
                operation.setMethod("deleteorders");
                rq.setAttribute("operation", operation);
            }
            
        } finally {
            cn.close();
        }
        
        // navigation: set 'account/konto' tab as active
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
        
        return mp.findForward(forward);
    }
    
    public ActionForward deleteusernoroders(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
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
        
        final MaintenanceForm operation = (MaintenanceForm) form;
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final Text cn = new Text();
        
        try {
            
            if (operation.isConfirmed()) {
                // execute operation
                final int deleted = operation.deleteUserNoOrders(ui, cn.getConnection());
                
                // set result from DELETE back into form
                operation.setNumerOfRecords(deleted);
                
                final Message msg = new Message();
                msg.setMessage("maintenance.numberOfRecordsDeleted");
                msg.setSystemMessage(String.valueOf(operation.getNumerOfRecords()));
                msg.setLink("maintenance.do");
                rq.setAttribute("message", msg);
                
                forward = Result.SUCCESS.getValue();
            } else {
                // force confirmation of operation
                forward = "confirm";
                
                // get number of records involved
                operation.setNumerOfRecords(operation.countDeleteUserNoOrders(ui, cn.getConnection()));
                
                operation.setMethod("deleteusernoroders");
                rq.setAttribute("operation", operation);
            }
            
        } finally {
            cn.close();
        }
        
        // navigation: set 'account/konto' tab as active
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
        
        return mp.findForward(forward);
    }
    
}
