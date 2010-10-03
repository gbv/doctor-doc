package ch.dbs.actions.bestand;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ch.dbs.entity.Bestand;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OrderForm;



public class Stockdetails extends Action {

    /**
     * Presents all the details to a given holding/stock
     *
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";

        // get all article values
        final OrderForm of = (OrderForm) form;

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

        rq.setAttribute("konto", holdings.get(0).getHolding().getKonto());
        rq.setAttribute("holdings", holdings);
        rq.setAttribute("orderform", of);

        return mp.findForward(forward);
    }

    private List<Bestand> getHoldings(final String kid, final String hoid, final String stid) {

        List<Bestand> holdings = new ArrayList<Bestand>();
        Bestand bestand = new Bestand();
        final Text cn = new Text();

        // we need at least one parameter
        if (kid == null && hoid == null && stid == null) { return holdings; }

        // return holdings upon the most specific identifier, ignoring
        // less specific identifiers. The order is: stid, hoid, kid
        if (stid != null) {
            // internal holdings are not visible
            bestand = new Bestand(Long.valueOf(stid), false, cn.getConnection());
            holdings.add(bestand);
        } else if (hoid != null) {
            // internal holdings are not visible
            holdings = bestand.getAllBestandForHoid(Long.valueOf(hoid), false, cn.getConnection());
        } else {
            // we only have a kid
            holdings = bestand.getAllKontoBestand(Long.valueOf(kid), false, cn.getConnection());
        }

        cn.close();
        return holdings;
    }

}
