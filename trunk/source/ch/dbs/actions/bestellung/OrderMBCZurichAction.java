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
public class OrderMBCZurichAction extends DispatchAction {

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