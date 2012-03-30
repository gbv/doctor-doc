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

package ch.dbs.actions.billing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import util.Auth;
import ch.dbs.entity.Billing;
import ch.dbs.entity.Konto;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.BillingForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.KontoForm;
import ch.dbs.form.Message;

/**
 * Handelt Rechnungsinformationen für Konten sowie für Admins
 *
 * @author Pascal Steiner
 *
 */
public final class BillingAction extends DispatchAction {

    /**
     * Listet die Rechnungen zu einem Konto sortiert nach Offenen Rechnungen, Rechnungsdatum
     * auf
     */
    public ActionForward listBillings(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        String forward = "failure";
        final Auth auth = new Auth();
        final Billing b = new Billing();

        try {

            // Ist der Benutzer noch angemeldet oder Session Timeout
            if (auth.isLogin(rq)) {

                // Darf der Benutzer Rechnungen sehen? NUR SEINE EIGENEN... ;-)
                if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                    // Rechnungen zum Konto auflisten
                    final KontoForm kf = (KontoForm) fm;

                    final BillingForm bf = new BillingForm();
                    final Konto k = new Konto(kf.getKid(), b.getConnection());
                    bf.setKonto(k);
                    bf.setBillings(b.getBillings(k, b.getConnection()));

                    rq.setAttribute("billingform", bf);

                    forward = "success";

                } else {
                    final ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
                    rq.setAttribute("errormessage", em);
                }

            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute("errormessage", em);
            }

        } finally {
            b.close();
        }

        return mp.findForward(forward);
    }

    /**
     * Admins können Paydate setzen
     */
    public ActionForward setPayDate(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();
        final Billing cn = new Billing();

        try {

            // Benutzer eingeloggt
            if (auth.isLogin(rq)) {

                // Benutzer ein Admin?
                if (auth.isAdmin(rq)) {

                    final BillingForm bf = (BillingForm) fm;
                    final Billing b = new Billing(bf.getBillid(), cn.getConnection());
                    b.setZahlungseingang(bf.getZahlungseingang());
                    b.update(cn.getConnection());

                    final Message em = new Message("message.setpaydate", b.getKonto().getBibliotheksname() + "\n\n"
                            + b.getRechnungsgrund().getInhalt(), "kontoadmin.do?method=listKontos");
                    rq.setAttribute("message", em);
                    forward = "success";

                } else {
                    final ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
                    rq.setAttribute("errormessage", em);
                }

            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute("errormessage", em);
            }

        } finally {
            cn.close();
        }

        return mp.findForward(forward);
    }
}
