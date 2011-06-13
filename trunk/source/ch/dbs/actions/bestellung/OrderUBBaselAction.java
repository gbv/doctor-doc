package ch.dbs.actions.bestellung;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ch.dbs.entity.DaiaParam;
import ch.dbs.form.OrderForm;

/**
 * Prepares a POST method to order over SFX at UB Basel.
 */
public class OrderUBBaselAction extends DispatchAction {


    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";

        final OrderForm ofjo = (OrderForm) form;
        final DaiaParam dp = new DaiaParam();

        // set parameters for UB Basel
        dp.setLinkout("http://www.ub.unibas.ch/cgi-bin/sfx_dod_m.pl");
        dp.setMapAuthors("Author");
        dp.setMapAtitle("Article");
        dp.setMapJournal("Journal");
        dp.setMapIssn("ISSN");
        dp.setMapDate("Year");
        dp.setMapVolume("Volumee");
        dp.setMapIssue("Issue");
        dp.setMapPages("Pages");
        dp.setMapPmid("meduid");

        // Identification
        dp.setFree1("Source");
        dp.setFree1Value("doctor-doc");
        // TODO: get Values from Konto
        dp.setFree2("uid");
        dp.setFree2Value("");
        dp.setFree3("pwd");
        dp.setFree3Value("");

        // additional parameters
        dp.setFree4("action");
        dp.setFree4Value("submit");
        dp.setFree5("pickup");
        dp.setFree5Value("EMAIL - E-Mail");
        dp.setFree6("sfxurl");
        dp.setFree6Value("#sfxurl");
        dp.setFree7("legal");
        dp.setFree7Value("on");
        dp.setFree8("B1");
        dp.setFree8Value("Bestellung abschicken");

        rq.setAttribute("ofjo", ofjo);
        rq.setAttribute("daiaparam", dp);

        forward = "redirect";

        return mp.findForward(forward);
    }

}
