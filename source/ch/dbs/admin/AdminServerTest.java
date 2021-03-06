//  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
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

package ch.dbs.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Auth;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.Message;
import enums.Result;

public final class AdminServerTest extends Action {

    /**
     * Class for Admins to test any methods on the server. Local behaviour may
     * differ from the behaviour on a remote server...
     *
     * Path: test.do
     *
     * @author Markus Fischer
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = Result.FAILURE.getValue();
        final Auth auth = new Auth();
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu(Result.LOGIN.getValue());
        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);

        if (auth.isAdmin(rq)) {
            forward = Result.SUCCESS.getValue();
            final Message m = new Message();
            m.setMessage("message.admintest");
            m.setLink("test.do");
            final StringBuffer message = new StringBuffer();
            // Testcode hier platzieren:

            // Ende Testcode

            m.setSystemMessage(message.toString()); // place your output here
            rq.setAttribute("message", m);

        } else {
            final ErrorMessage m = new ErrorMessage("error.berechtigung");
            m.setLink("login.do");
            rq.setAttribute(Result.ERRORMESSAGE.getValue(), m);
        }

        return mp.findForward(forward);
    }

}
