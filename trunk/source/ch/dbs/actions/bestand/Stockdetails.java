package ch.dbs.actions.bestand;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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



/**
 * Class to show stock details. One does not need to be logged in,
 * in order to view the information card of a holding.
 */
public class Stockdetails extends Action {

    /**
     * Presents all the details for a given holding/stock.
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";

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
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.stockinfo", "login.do");
            rq.setAttribute("errormessage", em);
            return mp.findForward(forward);
        }

        forward = "success";

        // get Konto from holding
        final Konto k = holdings.get(0).getHolding().getKonto();

        // set DaiaParam into request, if we have an DID in the Konto
        if (k.getDid() != null) {
            final DaiaParam dp = new DaiaParam(k.getDid(), k.getConnection());
            k.close();
            // set linkout dependent on protocol
            dp.setLinkout(dp, of);
            rq.setAttribute("daiaparam", dp);
            // redirect to linkout directly
            if (dp.isRedirect()) { forward = "redirect"; }
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
        if (kid == null && hoid == null && stid == null) { return holdings; }

        // return holdings upon the most specific identifier, ignoring
        // less specific identifiers. The order is: stid, hoid, kid
        if (stid != null && !"".equals(stid)) {
            // with a stockid, internal holdings have to be visible
            bestand = new Bestand(Long.valueOf(stid), true, cn.getConnection());
            // to avoid NullPointerException if internal of holding = true
            if (bestand.getId() != null) { holdings.add(bestand); }
        } else if (hoid != null && !"".equals(hoid)) {
            // internal holdings are not visible
            holdings = bestand.getAllBestandForHoid(Long.valueOf(hoid), false, cn.getConnection());
        } else if (kid != null && !"".equals(kid)) {
            // we only have a kid
            holdings = bestand.getAllKontoBestand(Long.valueOf(kid), false, cn.getConnection());
        }

        cn.close();
        return holdings;
    }

}
