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
     * Schnittstelle um Bestände in D-D über OpenURL abzufragen und Antworten
     * per XML im DAIA-Format zu erhalten
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final OrderForm ofjo = (OrderForm) form;

        String output = "";
        //    String outputformat = rq.getParameter("format");

        final ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        final OpenUrl openUrlInstance = new OpenUrl();

        final ContextObject co = openUrlInstance.readOpenUrlFromRequest(rq);
        ofjo.completeOrderForm(ofjo, convertOpenUrlInstance.makeOrderform(co));

        // Parameter für indirekte Nutzung über einen Metakatalog (z.B. Vufind)
        final String daiaIP = rq.getParameter("ip"); // in der URL übermittelte Originalanfrage-IP
        boolean showInternal = false; // opt. Parameter um auch Bestände anzuzeigen, die mit "intern" gekennzeichnet sind
        if (rq.getParameter("in") != null && rq.getParameter("in").equals("1")) { showInternal = true; }

        final Stock stock = new Stock();
        List<Bestand> bestaende = new ArrayList<Bestand>();
        String msgBestand = ""; // msgBestand ist nicht DAIA-standardkonform. Wird nur bei Abfrage DAIA + IP verwendet

        if (ofjo.getIssn() != null && !ofjo.getIssn().equals("")) {

            if (daiaIP == null) {
                bestaende = stock.checkGeneralStockAvailability(ofjo, false);
            } else { // Bestände für ein bestimmtes Konto prüfen (IP-basiert)
                msgBestand = "No holdings found";
                final Text cn = new Text();
                // Text mit Konto anhand IP holen
                final IPChecker ipck = new IPChecker();
                final Text tip = ipck.contains(daiaIP, cn.getConnection());

                if (tip.getKonto() != null && tip.getKonto().getId() != null) { // Nur prüfen, falls Konto vorhanden
                    bestaende = stock.checkStockAvailabilityForIP(ofjo, tip, showInternal, cn.getConnection());
                } else {
                    msgBestand = "Your location is unknown";
                }
                cn.close();
            }

        } else if (daiaIP != null && !daiaIP.equals("")) { // msgBestand ist
            msgBestand = "Missing ISSN";
        }

        final DaiaXMLResponse xml = new DaiaXMLResponse();

        if (!bestaende.isEmpty()) { // es gibt Bestände
            output =  xml.listHoldings(bestaende, ofjo.getRfr_id());
        } else { // es gibt keine Bestände
            output = xml.noHoldings(msgBestand, ofjo.getRfr_id());
        }

        //    if (isJson(outputformat)) {
        //      // TODO: umwandeln
        //    }

        try {

            rp.setContentType("text/xml");
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

        return mp.findForward("failure"); // geht ggf. auf Fehlerseite
    }

    //  private boolean isJson(String outputformat) {
    //    boolean check = false;
    //
    //    if (outputformat != null && !outputformat.equals("")
    //      && outputformat.equalsIgnoreCase("json")) {
    //      check = true;
    //    }
    //
    //    return check;
    //  }

}
