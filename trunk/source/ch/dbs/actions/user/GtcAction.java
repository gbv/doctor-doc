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
import enums.Result;

public final class GtcAction extends DispatchAction {

    /**
     * Prüft ob AGB in einer bestimmten Version angenommen wurden
     *
     * @author Markus Fischer
     */
    public ActionForward accept(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form; // falls Artikelangaben aus Linkresolver vorhanden

        String forward = Result.FAILURE.getValue();
        final Text cn = new Text();
        final Auth auth = new Auth();

        try {

            // Sicherstellen, dass die Action nur von Benutzern nach Prüfung aufgerufen wird
            if (auth.isLogin(rq)) {
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                final Gtc gtc = new Gtc();
                final Text t = gtc.getCurrentGtc(cn.getConnection()); // Text der aktuellen Version

                final Date d = new Date(); // aktuelles Datum setzen
                final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String datum = fmt.format(d, ui.getKonto().getTimezone());

                ui.getBenutzer().setGtc(t.getInhalt());
                ui.getBenutzer().setGtcdate(datum);
                final AbstractBenutzer b = new AbstractBenutzer();
                b.updateUser(ui.getBenutzer(), ui.getKonto(), cn.getConnection());
                rq.getSession().setAttribute("userinfo", ui); // userinfo in Request aktualisieren

                if (ui.getKonto() != null) { // Prüfung ob mehrere Konti vorhanden
                    forward = Result.SUCCESS.getValue();

                } else {
                    forward = "konto";
                }

            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(Result.LOGIN.getValue());
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }

            if (pageForm.isResolver()) {
                // hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
                rq.setAttribute("orderform", pageForm); // Übergabe in jedem Fall
                // Die Bestellberechtigung wird in der Methode prepare geprüft!
                if (Result.SUCCESS.getValue().equals(forward) && auth.isBenutzer(rq)) {
                    forward = "order";
                }
                // Bibliothekar oder Admin auf Checkavailability
                if (Result.SUCCESS.getValue().equals(forward) && !auth.isBenutzer(rq)) {
                    forward = "checkavailability";
                }
            }

            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);

        } finally {
            cn.close();
        }

        return mp.findForward(forward);
    }

    public ActionForward decline(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = Result.FAILURE.getValue();
        final Auth auth = new Auth();
        // Make sure method is only accessible when user is logged in

        if (auth.isLogin(rq)) {
            forward = "decline";

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
