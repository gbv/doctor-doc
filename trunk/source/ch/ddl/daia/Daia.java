package ch.ddl.daia;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.grlea.log.SimpleLogger;

import util.IPChecker;
import ch.dbs.actions.bestand.Stock;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Text;
import ch.dbs.form.OrderForm;

public class Daia extends Action {

    private static final SimpleLogger LOG = new SimpleLogger(Daia.class);

    /**
     * Interface to query holdings in D-D using OpenURL requests. The response
     * will be in DAIA/XML.
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final OrderForm ofjo = (OrderForm) form;

        String output = "";
        //    String outputformat = rq.getParameter("format");

        final ConvertOpenUrl openurlConv = new ConvertOpenUrl();
        final OpenUrl openurl = new OpenUrl();

        final ContextObject co = openurl.readOpenUrlFromRequest(rq);
        ofjo.completeOrderForm(ofjo, openurlConv.makeOrderform(co));

        // Parameters for indirect use in a metacatalogue (e.g. Vufind)
        final String daiaIP = rq.getParameter("ip"); // requesting IP transmitted in the URL
        boolean showInternal = false; // optional parameter, that will cause to show holdings that are marked as "internal"
        if (rq.getParameter("in") != null && rq.getParameter("in").equals("1")) { showInternal = true; }

        final Stock stock = new Stock();
        List<Bestand> bestaende = new ArrayList<Bestand>();

        // Default message, only used if we have no holdings
        String msgBestand = "No holdings found";

        // check for ISSN or journal title
        if (isSearchable(ofjo)) {

            if (daiaIP == null) {
                bestaende = stock.checkGeneralStockAvailability(ofjo, false);
            } else { // IP based ckeck for a given account (Konto)
                final Text cn = new Text();
                // get Text with account (Konto)
                final IPChecker ipck = new IPChecker();
                final Text tip = ipck.contains(daiaIP, cn.getConnection());

                // Only run the availability checks if we have an account (Konto)
                if (tip.getKonto() != null && tip.getKonto().getId() != null) {
                    bestaende = stock.checkStockAvailabilityForIP(ofjo, tip, showInternal, cn.getConnection());
                } else {
                    // TODO: rename: The location is unknown
                    msgBestand = "Your location is unknown";
                }
                cn.close();
            }

            // mostly requests coming from Vufind
        } else if (daiaIP != null && !daiaIP.equals("")) {
            msgBestand = "Unknown";
        }

        final DaiaXMLResponse xml = new DaiaXMLResponse();

        if (!bestaende.isEmpty()) { // output for found holdings
            output =  xml.listHoldings(bestaende, ofjo.getRfr_id());
        } else { // output for no holdings
            output = xml.noHoldings(msgBestand, ofjo.getRfr_id());
        }

        // TODO: optionally convert to JSON

        try {

            rp.setContentType("text/xml");
            // set a max-age header (in seconds) to make it possible to cache the content
            rp.setHeader("Cache-Control", "max-age=86400");
            rp.flushBuffer();
            final PrintWriter pw = rp.getWriter();
            // use writer to render text

            pw.write(output);
            pw.flush();
            pw.close();

            return null;

        } catch (final IOException e) {
            LOG.error(e.toString());
        }

        return mp.findForward("failure"); // when we get here, there is an error
    }

    /**
     * Checks if we have an ISSN or at least a journal title (may be extended).
     *
     */
    private boolean isSearchable(final OrderForm ofjo) {
        boolean check = false;

        if (ofjo.getIssn() != null && !"".equals(ofjo.getIssn())
                || ofjo.getZeitschriftentitel() != null && !"".equals(ofjo.getZeitschriftentitel())) {

            check = true;
        }

        return check;
    }

}
