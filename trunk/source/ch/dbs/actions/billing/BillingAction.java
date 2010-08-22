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
    public ActionForward listBillings(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
      String forward = "failure";
      Auth auth = new Auth();

      // Ist der Benutzer noch angemeldet oder Session Timeout
      if (auth.isLogin(rq)) {

        // Darf der Benutzer Rechnungen sehen? NUR SEINE EIGENEN... ;-)
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

          // Rechnungen zum Konto auflisten
          KontoForm kf = (KontoForm) fm;
          Billing b = new Billing();

          BillingForm bf = new BillingForm();
          Konto k = new Konto(kf.getKid(), b.getConnection());
          bf.setKonto(k);
          bf.setBillings(b.getBillings(k, b.getConnection()));

          rq.setAttribute("billingform", bf);

          b.close();

          forward = "success";

        } else {
            ErrorMessage em = new ErrorMessage(
                        "error.berechtigung", "searchfree.do");
                rq.setAttribute("errormessage", em);
              }

      } else {
        ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
        ErrorMessage em = new ErrorMessage(
                "error.timeout", "login.do");
        rq.setAttribute("errormessage", em);
      }

      return mp.findForward(forward);
    }


    /**
     * Admins können Paydate setzen
     */
    public ActionForward setPayDate(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

      String forward = "failure";
      Auth auth = new Auth();

      // Benutzer eingeloggt
      if (auth.isLogin(rq)) {

      // Benutzer ein Admin?
      if (auth.isAdmin(rq)) {

          BillingForm bf = (BillingForm) fm;
          Billing cn = new Billing();
          Billing b = new Billing(bf.getBillid(), cn.getConnection());
          b.setZahlungseingang(bf.getZahlungseingang());
          b.update(cn.getConnection());

          cn.close();

          Message em = new Message("message.setpaydate", b.getKonto().getBibliotheksname() + "\n\n"
              + b.getRechnungsgrund().getInhalt(), "kontoadmin.do?method=listKontos");
          rq.setAttribute("message", em);
          forward = "success";

      } else {
        ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do");
        rq.setAttribute("errormessage", em);
      }

    } else {
      ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
      ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
      rq.setAttribute("errormessage", em);
    }


      return mp.findForward(forward);
    }
}
