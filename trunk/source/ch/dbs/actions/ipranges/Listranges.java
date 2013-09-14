//  Copyright (C) 2013  Markus Fischer
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

package ch.dbs.actions.ipranges;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Auth;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.UserInfo;
import enums.Result;
import enums.TextType;

public class Listranges extends Action {

    /**
     * Gets all IP ranges for an account.
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
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

        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

        // set active menu to account
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("konto");
        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);

        final List<Text> ranges = new ArrayList<Text>();

        final Text cn = new Text();

        try {
            // get all IP4 and IP6 addresses for account
            ranges.addAll(cn.getAllKontoText(TextType.IP4, ui.getKonto().getId(), cn.getConnection()));
            ranges.addAll(cn.getAllKontoText(TextType.IP6, ui.getKonto().getId(), cn.getConnection()));
        } finally {
            cn.close();
        }

        rq.setAttribute("ranges", ranges);

        return mp.findForward(Result.SUCCESS.getValue());
    }

}
