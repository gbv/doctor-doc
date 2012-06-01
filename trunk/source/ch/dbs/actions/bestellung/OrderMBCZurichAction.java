//  Copyright (C) 2011  Markus Fischer, Pascal Steiner
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

package ch.dbs.actions.bestellung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import util.Auth;
import ch.dbs.entity.DaiaParam;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import enums.Result;

/**
 * Prepares a POST method to order over SFX at UB Basel.
 */
public class OrderMBCZurichAction extends DispatchAction {

    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final Auth auth = new Auth();
        String forward = Result.FAILURE.getValue();

        if (auth.isLogin(rq)) {

            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                final OrderForm ofjo = (OrderForm) form;
                final DaiaParam dp = new DaiaParam();

                // set parameters for UB Basel
                dp.setLinkout("http://www.hbz.unizh.ch/docdel/docdel_usb.php");
                dp.setMapAuthors("Author");
                dp.setMapAtitle("Article");
                dp.setMapJournal("Journal");
                dp.setMapIssn("ISSN");
                dp.setMapDate("Year");
                dp.setMapVolume("Volume");
                dp.setMapIssue("Issue");
                dp.setMapPages("Pages");
                dp.setMapPmid("meduid");

                // Identification
                dp.setFree1("sid");
                dp.setFree1Value("doctor-doc");
                dp.setFree2("UserName");
                dp.setFree2Value("sfxuser");

                // get Values from Konto
                dp.setFree3("Name");
                dp.setFree3Value(ui.getBenutzer().getName() + " " + ui.getBenutzer().getVorname());
                dp.setFree4("Usernum");
                if (ui.getKonto().getIdsid() != null) {
                    dp.setFree4Value(ui.getKonto().getIdsid());
                } else {
                    dp.setFree4Value("");
                }
                dp.setFree5("Institut");
                dp.setFree5Value(ui.getKonto().getBibliotheksname());
                dp.setFree6("Strasse");
                dp.setFree6Value(ui.getKonto().getAdresse() + " " + ui.getKonto().getAdressenzusatz());
                dp.setFree7("Ort");
                dp.setFree7Value(ui.getKonto().getPLZ() + " " + ui.getKonto().getOrt());
                dp.setFree8("Telefon");
                dp.setFree8Value(ui.getKonto().getTelefon());
                dp.setFree9("Useremail");
                dp.setFree9Value(ui.getKonto().getDbsmail());

                rq.setAttribute("ofjo", ofjo);
                rq.setAttribute("daiaparam", dp);

                forward = "redirect";

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
        }

        return mp.findForward(forward);
    }

}
