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

/**
 * Prepares a POST method to order over SFX at UB Basel.
 */
public class OrderUBBaselAction extends DispatchAction {

    private static final String ACTIVEMENUS = "ActiveMenus";
    private static final String ERRORMESSAGE = "errormessage";


    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final Auth auth = new Auth();
        String forward = "failure";

        if (auth.isLogin(rq)) {

            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                final OrderForm ofjo = (OrderForm) form;
                final DaiaParam dp = new DaiaParam();

                // set parameters for UB Basel
                dp.setLinkout("http://www.ub.unibas.ch/cgi-bin/sfx_dod_m.pl");
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
                dp.setFree1("Source");
                dp.setFree1Value("doctor-doc");
                // TODO: get Values from Konto
                dp.setFree2("uid");
                if (ui.getKonto().getIdsid() != null) {
                    dp.setFree2Value(ui.getKonto().getIdsid());
                } else {
                    dp.setFree2Value("");
                }
                dp.setFree3("pwd");
                if (ui.getKonto().getIdspasswort() != null) {
                    dp.setFree3Value(ui.getKonto().getIdspasswort());
                } else {
                    dp.setFree3Value("");
                }
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

                // set reference into remarks due to indication bug (missing issue) at UB Basel
                dp.setFree9("bemerkung");
                dp.setFree9Value(dp.combineReference(ofjo));

                rq.setAttribute("ofjo", ofjo);
                rq.setAttribute("daiaparam", dp);

                forward = "redirect";

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        return mp.findForward(forward);
    }

}
