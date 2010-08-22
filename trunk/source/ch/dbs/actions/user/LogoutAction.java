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

package ch.dbs.actions.user;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.grlea.log.SimpleLogger;

/**
 *
 * @author Pascal Steiner
 */
public class LogoutAction extends Action {

    private static final SimpleLogger LOG = new SimpleLogger(LogoutAction.class);

    /**
     * Logout. Zerstört das Sessionbean
     */
    public ActionForward execute(ActionMapping mp,
            ActionForm form, HttpServletRequest rq,
            HttpServletResponse rp) {

        try {
            Object loc = rq.getSession().getAttribute("org.apache.struts.action.LOCALE"); // get existing Locale
            rq.getSession().invalidate();
            if (loc != null) {
                // restore Locale (maybe manually choosen)
                rq.getSession().setAttribute("org.apache.struts.action.LOCALE", new Locale(loc.toString()));
            }
        } catch (Exception e) {
            LOG.error("execute: " + e.toString());
        }


        return mp.findForward("success");
    }
}
