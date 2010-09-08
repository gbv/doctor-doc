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
import ch.dbs.entity.Holding;
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
        final String hoid = rq.getParameter("holding");
        final String stid = rq.getParameter("stock");

        final List<Bestand> holdings = getHoldings(hoid, stid);

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

    private List<Bestand> getHoldings(final String hoid, final String stid) {

        List<Bestand> holdings = new ArrayList<Bestand>();
        Bestand bestand = new Bestand();
        final Text cn = new Text();

        // we need at least one parameter
        if (hoid == null && stid == null) { return holdings; }

        // we have a stock-id
        if (stid != null) {
            // internal holdings are not visible
            bestand = new Bestand(Long.valueOf(stid), false, cn.getConnection());
            holdings.add(bestand);
        } else {
            // we only have a holding-id
            // internal holdings are not visible
            final Holding hold = new Holding(Long.valueOf(hoid), cn.getConnection());
            holdings = bestand.getAllBestandForHolding(hold, false, cn.getConnection());
        }

        cn.close();
        return holdings;
    }

}
