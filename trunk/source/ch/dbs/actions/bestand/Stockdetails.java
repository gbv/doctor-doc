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

package ch.dbs.actions.bestand;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Auth;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.DaiaParam;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OrderForm;
import enums.Result;

/**
 * Class to show stock details. One does not need to be logged in,
 * in order to view the information card of a holding.
 */
public class Stockdetails extends Action {

    /**
     * Presents all the details for a given holding/stock.
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = Result.FAILURE.getValue();

        // read article values, if request comes from within D-D
        final OrderForm of = (OrderForm) form;

        // read from OpenURL
        final OpenUrl openURL = new OpenUrl();
        final ConvertOpenUrl convert = new ConvertOpenUrl();
        final ContextObject co = openURL.readOpenUrlFromRequest(rq);
        final OrderForm ofOpenURL = convert.makeOrderform(co);

        // combine OrderForms
        of.completeOrderForm(of, ofOpenURL);

        // get holding parameters
        final String kid = rq.getParameter("library");
        final String hoid = rq.getParameter("holding");
        final String stid = rq.getParameter("stock");

        final List<Bestand> holdings = getHoldings(kid, hoid, stid);

        // make sure we found some holdings
        if (holdings.isEmpty()) {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu(Result.LOGIN.getValue());
            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
            final ErrorMessage em = new ErrorMessage("error.stockinfo", "login.do");
            rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            return mp.findForward(forward);
        }

        forward = Result.SUCCESS.getValue();

        // get Konto from holding
        final Konto k = holdings.get(0).getHolding().getKonto();

        // if we have an DID in the Konto, and the request is for a stock ID, we will
        // redirect to the defined order form (external / internal)
        if (k.getDid() != null && stid != null) {

            try {
                final DaiaParam dp = new DaiaParam(k.getDid(), k.getConnection());

                // additional check if the request comes out of the IP range of the account

                // get Konto from IP
                final Auth auth = new Auth();
                final Text t = auth.grantAccess(rq);

                // use IP based order form if applicable
                if (useIPBasedForm(k, t) && dp.isIp_overrides()) {
                    rq.setAttribute("ofjo", of);
                    forward = "redirectIP";
                    // redirect to order form defined in dp
                } else {
                    // set linkout dependent on protocol
                    dp.setLinkout(dp, of);
                    rq.setAttribute("daiaparam", dp);
                    // redirect to linkout...
                    if (dp.isRedirect()) {
                        // ...using POST method
                        if (dp.isPost()) {
                            rq.setAttribute("ofjo", of);
                            forward = "redirectpost";
                            // ...using GET method
                        } else {
                            forward = "redirect";
                        }
                    }
                }
            } finally {
                k.close();
            }
        }

        // in any case set parameters into request
        rq.setAttribute("konto", k);
        rq.setAttribute("holdings", holdings);
        rq.setAttribute("orderform", of);

        return mp.findForward(forward);
    }

    /**
     * Gets a list with the holdings.
     *
     * @param String kid
     * @param String hoid
     * @param String stid
     * @return List<Bestand> holdings
     */
    private List<Bestand> getHoldings(final String kid, final String hoid, final String stid) {

        List<Bestand> holdings = new ArrayList<Bestand>();
        Bestand bestand = new Bestand();
        final Text cn = new Text();

        // we need at least one parameter
        if (kid == null && hoid == null && stid == null) {
            return holdings;
        }

        try {
            // return holdings upon the most specific identifier, ignoring
            // less specific identifiers. The order is: stid, hoid, kid
            if (stid != null && !"".equals(stid)) {
                // with a stockid, internal holdings have to be visible
                bestand = new Bestand(Long.valueOf(stid), true, cn.getConnection());
                // to avoid NullPointerException if internal of holding = true
                if (bestand.getId() != null) {
                    holdings.add(bestand);
                }
            } else if (hoid != null && !"".equals(hoid)) {
                // internal holdings are not visible
                holdings = bestand.getAllBestandForHoid(Long.valueOf(hoid), false, cn.getConnection());
            } else if (kid != null && !"".equals(kid)) {
                // we only have a kid
                holdings = bestand.getAllKontoBestand(Long.valueOf(kid), false, cn.getConnection());
            }

        } finally {
            cn.close();
        }

        return holdings;
    }

    private boolean useIPBasedForm(final Konto k, final Text t) {
        boolean result = false;

        if (t.getKonto() != null && k.getId().equals(t.getKonto().getId())) {
            result = true;
        }

        return result;
    }

}
