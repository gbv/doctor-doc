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
        //        k.setDid(Long.valueOf("1"));

        // set DaiaParam into request, if we have an DID in the Konto
        if (k.getDid() != null) {
            final DaiaParam dp = new DaiaParam(k.getDid(), k.getConnection());
            k.close();
            // set linkout dependent on protocol
            dp.setLinkout(createLinkout(dp, of));
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

    private String createLinkout(final DaiaParam dp, final OrderForm of) {

        final StringBuffer linkout = new StringBuffer(dp.getBaseurl());

        linkout.append(dp.getFirstParam());

        if ("openurl".equals(dp.getProtocol())) {
            linkoutOpenURL(linkout, dp, of);
        } else if ("custom".equals(dp.getProtocol())) {
            linkoutCustom(linkout, dp, of);
        } else { // default value "internal"
            linkoutInternal(linkout, dp, of);
        }

        return linkout.toString();
    }

    private void linkoutOpenURL(final StringBuffer linkout, final DaiaParam dp, final OrderForm of) {

        // create a new OpenURL object
        final ContextObject co = new ConvertOpenUrl().makeContextObject(of);
        final String openurl = new OpenUrl().composeOpenUrl(co);
        linkout.append(openurl);

    }

    private void linkoutCustom(final StringBuffer linkout, final DaiaParam dp, final OrderForm of) {

        if (of.getMediatype() != null && !"".equals(of.getMediatype())
                && dp.getMapMediatype() != null) {
            linkout.append(dp.getMapMediatype());
            linkout.append('=');
            linkout.append(of.getMediatype());
        }
        if (of.getJahr() != null && !"".equals(of.getJahr())
                && dp.getMapDate() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapDate());
            linkout.append('=');
            linkout.append(of.getJahr());
        }
        if (of.getJahrgang() != null && !"".equals(of.getJahrgang())
                && dp.getMapVolume() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapVolume());
            linkout.append('=');
            linkout.append(of.getJahrgang());
        }
        if (of.getHeft() != null && !"".equals(of.getHeft())
                && dp.getMapIssue() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapIssue());
            linkout.append('=');
            linkout.append(of.getHeft());
        }
        if (of.getSeiten() != null && !"".equals(of.getSeiten())
                && dp.getMapPages() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapPages());
            linkout.append('=');
            linkout.append(of.getSeiten());
        }
        if (of.getSeiten() != null && !"".equals(of.getIssn())
                && dp.getMapIssn() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapIssn());
            linkout.append('=');
            linkout.append(of.getIssn());
        }
        if (of.getSeiten() != null && !"".equals(of.getIsbn())
                && dp.getMapIsbn() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapIsbn());
            linkout.append('=');
            linkout.append(of.getIsbn());
        }
        if (of.getZeitschriftentitel() != null && !"".equals(of.getZeitschriftentitel())
                && dp.getMapJournal() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapJournal());
            linkout.append('=');
            linkout.append(of.getZeitschriftentitel());
        }
        if (of.getArtikeltitel() !=  null && !"".equals(of.getArtikeltitel())
                && dp.getMapAtitle() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapAtitle());
            linkout.append('=');
            linkout.append(of.getArtikeltitel());
        }
        if (of.getAuthor() != null && !"".equals(of.getAuthor())
                && dp.getMapAuthors() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapAuthors());
            linkout.append('=');
            linkout.append(of.getAuthor());
        }
        if (of.getBuchtitel() != null && !"".equals(of.getBuchtitel())
                && dp.getMapBtitle() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapBtitle());
            linkout.append('=');
            linkout.append(of.getBuchtitel());
        }
        if (of.getKapitel() != null && !"".equals(of.getKapitel())
                && dp.getMapChapter() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapChapter());
            linkout.append('=');
            linkout.append(of.getKapitel());
        }
        if (of.getVerlag() != null && !"".equals(of.getVerlag())
                && dp.getMapPublisher() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapPublisher());
            linkout.append('=');
            linkout.append(of.getVerlag());
        }
        if (of.getPmid() != null && !"".equals(of.getPmid())
                && dp.getMapPmid() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapPmid());
            linkout.append('=');
            linkout.append(of.getPmid());
        }
        if (of.getDoi() != null && !"".equals(of.getDoi())
                && dp.getMapDoi() != null) {
            appendParam(linkout);
            linkout.append(dp.getMapDoi());
            linkout.append('=');
            linkout.append(of.getDoi());
        }

    }

    private void linkoutInternal(final StringBuffer linkout, final DaiaParam dp, final OrderForm of) {

        linkout.append("mediatype=");
        linkout.append(of.getMediatype());
        if (of.getJahr() != null && !"".equals(of.getJahr())) {
            linkout.append("&jahr=");
            linkout.append(of.getJahr());
        }
        if (of.getJahrgang() != null && !"".equals(of.getJahrgang())) {
            linkout.append("&jahrgang=");
            linkout.append(of.getJahrgang());
        }
        if (of.getHeft() != null && !"".equals(of.getHeft())) {
            linkout.append("&heft=");
            linkout.append(of.getHeft());
        }
        if (of.getSeiten() != null && !"".equals(of.getSeiten())) {
            linkout.append("&seiten=");
            linkout.append(of.getSeiten());
        }
        if (of.getIssn() != null && !"".equals(of.getIssn())) {
            linkout.append("&issn=");
            linkout.append(of.getIssn());
        }
        if (of.getIsbn() != null && !"".equals(of.getIsbn())) {
            linkout.append("&isbn=");
            linkout.append(of.getIsbn());
        }
        if (of.getZeitschriftentitel() != null && !"".equals(of.getZeitschriftentitel())) {
            linkout.append("&zeitschriftentitel=");
            linkout.append(of.getZeitschriftentitel());
        }
        if (of.getArtikeltitel() != null && !"".equals(of.getArtikeltitel())) {
            linkout.append("&artikeltitel=");
            linkout.append(of.getArtikeltitel());
        }
        if (of.getAuthor() != null && !"".equals(of.getAuthor())) {
            linkout.append("&author=");
            linkout.append(of.getAuthor());
        }
        if (of.getBuchtitel() != null && !"".equals(of.getBuchtitel())) {
            linkout.append("&buchtitel=");
            linkout.append(of.getBuchtitel());
        }
        if (of.getKapitel() != null && !"".equals(of.getKapitel())) {
            linkout.append("&kapitel=");
            linkout.append(of.getKapitel());
        }
        if (of.getVerlag() != null && !"".equals(of.getVerlag())) {
            linkout.append("&verlag=");
            linkout.append(of.getVerlag());
        }
        if (of.getPmid() != null && !"".equals(of.getPmid())) {
            linkout.append("&pmid=");
            linkout.append(of.getPmid());
        }
        if (of.getDoi() != null && !"".equals(of.getDoi())) {
            linkout.append("&doi=");
            linkout.append(of.getDoi());
        }

    }

    private void appendParam(final StringBuffer buf) {

        // check to see if we already have an ? or an & at the end
        if (buf.length() > 0 && (buf.charAt(buf.length() - 1) != '?'
            || buf.charAt(buf.length() - 1) != '&')) {
            buf.append('&'); // if not, append
        }

    }

}
