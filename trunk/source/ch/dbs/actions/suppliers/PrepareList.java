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


/**
 * Prepares the list of suppliers for a given account to be edited and configured.
 */
public class PrepareList extends DispatchAction {

    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final SupplierForm sf = (SupplierForm) form;

        // catching session timeouts
        if (auth.isLogin(rq)) {
            // restrict editing suppliers to librarians and admins only
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {


                final Lieferanten sup = new Lieferanten();
                final Text cn = new Text();

                final List<SupplierForm> privSuppliers = sup.getPrivates(ui.getKonto().getId(), cn.getConnection());
                final List<SupplierForm> pubSuppliers = sup.getPublics(ui.getKonto().getLand(), cn.getConnection());

                rq.setAttribute("pubsuppliers", pubSuppliers);
                rq.setAttribute("privsuppliers", privSuppliers);
                rq.setAttribute("conf", sf);

                // TODO: control that in any case we have at least the general suppliers

                // we do have changed settings for this account
                if (sf.isChangedsettings()) {
                    if (privSuppliers.isEmpty() && !sf.isShowpubsuppliers()) {
                        sf.setShowpubsuppliers(true);
                    }
                    sf.updateAccount(sf, ui.getKonto(), cn.getConnection());
                    // TODO: get back updated account and set it into ui in session
                }

                forward = "success";

                cn.close();
            }  else {
                final ErrorMessage m = new ErrorMessage("error.berechtigung");
                m.setLink("searchfree.do");
                rq.setAttribute("errormessage", m);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

}
