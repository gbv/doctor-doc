package ch.ddl.daia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.grlea.log.SimpleLogger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestand;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Erstellt auf eine Verfügbarkeitsanfrage per OpenURL eine DAIA-Response in XML
 * DAIA = Document Availability Information API
 * http://ws.gbv.de/daia/daia.xsd.htm
 * <p/>
 * @author Markus Fischer
 */
public class DaiaXMLResponse {

    private static final SimpleLogger LOG = new SimpleLogger(DaiaXMLResponse.class);
    private static final String UTF8 = "UTF-8";
    private static final String CDATA = "CDATA";

    public String listHoldings(final ArrayList<Bestand> bestaende, final String rfr_id) {

        String xml = "";

        try {

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            // XERCES
            final OutputFormat of = new OutputFormat("XML", UTF8, true);
            of.setIndent(1);
            of.setIndenting(true);
            //    of.setDoctype(null,"daia.dtd");
            final XMLSerializer serializer = new XMLSerializer(out, of);

            // SAX2.0 ContentHandler
            final ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            //    hd.processingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"http://ws.gbv.de/daia/daia.xsl\"");

            final AttributesImpl atts = new AttributesImpl();

            final Date d = new Date(); // aktuelles Datum erstellen
            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());

            // ROOT tag
            atts.addAttribute("", "", "timestamp", CDATA, datum);
            atts.addAttribute("", "", "version", CDATA, "0.5");
            hd.startElement("", "", "daia", atts);

            // Institution tag
            atts.clear();
            // TODO: Bestellpfad einrichten
            atts.addAttribute("", "", "href", CDATA, ReadSystemConfigurations.getServerInstallation() + "/daia.do");
            hd.startElement("", "", "institution", atts);
            String text = "Register " + ReadSystemConfigurations.getApplicationName();
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "institution");

            if (rfr_id != null && !rfr_id.equals("")) {
                // Message tag (hier ggf. benutzt für rfr_id einer Anfrage über OpenURL)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(rfr_id);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");
            }

            final String urn = "urn:x-domain:" + ReadSystemConfigurations.getServerInstallation() + ":";

            for (final Bestand b : bestaende) {
                // Document tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, urn + "holding:" + b.getHolding().getId().toString()); // Holding-ID
                hd.startElement("", "", "document", atts);

                // Message tag (hier benutzt für Titel)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(b.getHolding().getTitel());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Item tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, urn + "stock:" + b.getId().toString()); // Stock-ID
                hd.startElement("", "", "item", atts);

                // Message tag (hier benutzt für Feld 'Bemerkungen' eines Bestandes)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(b.getBemerkungen());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Label tag (Signatur = Shelfmark)
                atts.clear();
                hd.startElement("", "", "label", atts);
                text = StringEscapeUtils.escapeXml(b.getShelfmark());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "label");

                // Department tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, urn + "library:" + b.getHolding().getKid().toString());
                hd.startElement("", "", "department", atts);
                text = b.getHolding().getKonto().getBibliotheksname() + "\040"
                + b.getHolding().getKonto().getOrt() + "\040"
                + b.getHolding().getKonto().getLand();
                text = StringEscapeUtils.escapeXml(text);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "department");

                // Storage tag
                atts.clear();
                hd.startElement("", "", "storage", atts);
                // Zusatztag location
                atts.clear();
                hd.startElement("", "", "location", atts);
                text = StringEscapeUtils.escapeXml(b.getStandort().getInhalt());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "location");
                //      // Zusatztag shelfmark
                //      atts.clear();
                //      hd.startElement("", "", "shelfmark", atts);
                //      text = StringEscapeUtils.escapeXml(bestaende.get(i).getShelfmark());
                //      hd.characters(text.toCharArray(), 0, text.length());
                //      hd.endElement("", "", "shelfmark");
                //      // Zusatztag eissue
                //      atts.clear();
                //      hd.startElement("", "", "eissue", atts);
                //      if (bestaende.get(i).isEissue()) {text="true"; } else {text="false"; };
                //      hd.characters(text.toCharArray(), 0, text.length());
                //      hd.endElement("", "", "eissue");
                hd.endElement("", "", "storage");

                // Available tag
                atts.clear();
                atts.addAttribute("", "", "service", CDATA, "interloan");
                hd.startElement("", "", "available", atts);

                //      // Limitation tag (z.B. für Liefermöglichkeiten: Fax, Post, Email...)
                //      String[] deliveryways = {"Fax", "Post", "Email", "Express"};
                //      String[] costs = {"8", "8", "8", "16"};
                //      String waehrung = "CHF";
                //      for (int y=0;y<deliveryways.length;y++) {
                //        atts.clear();
                //        atts.addAttribute("", "", "id", CDATA, deliveryways[y]);
                //        hd.startElement("", "", "limitation", atts);
                //        // Zusatztag costs
                //        atts.clear();
                //        atts.addAttribute("", "", "id", CDATA, waehrung);
                //        hd.startElement("", "", "costs", atts);
                //        hd.characters(costs[y].toCharArray(), 0, costs[y].length());
                //        hd.endElement("", "", "costs");
                //        hd.endElement("", "", "limitation");
                //      }

                hd.endElement("", "", "available");
                hd.endElement("", "", "item");
                hd.endElement("", "", "document");

            }

            hd.endElement("", "", "daia");
            hd.endDocument();

            xml = new String(out.toByteArray(), UTF8);
            out.close();

        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final SAXException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        }

        return xml;
    }

    public String noHoldings(final String msg, final String rfr_id) {

        String xml = "";

        try {

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            // XERCES
            final OutputFormat of = new OutputFormat("XML", UTF8, true);
            of.setIndent(1);
            of.setIndenting(true);
            //    of.setDoctype(null, "daia.dtd");
            final XMLSerializer serializer = new XMLSerializer(out, of);

            // SAX2.0 ContentHandler
            final  ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            //    hd.processingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"http://ws.gbv.de/daia/daia.xsl\"");

            final AttributesImpl atts = new AttributesImpl();

            final Date d = new Date(); // aktuelles Datum erstellen
            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());

            // ROOT tag
            atts.addAttribute("", "", "timestamp", CDATA, datum);
            atts.addAttribute("", "", "version", CDATA, "0.5");
            hd.startElement("", "", "daia", atts);

            // Institution tag
            atts.clear();
            atts.addAttribute("", "", "href", CDATA, "http://www.doctor-doc.com/");
            hd.startElement("", "", "institution", atts);
            String text = "Register Doctor-Doc";
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "institution");

            if (rfr_id != null && !rfr_id.equals("")) {
                // Message tag (hier ggf. benutzt für rfr_id einer Anfrage über OpenURL)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(rfr_id);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");
            }


            // Document tag
            atts.clear();
            atts.addAttribute("", "", "id", CDATA, "0"); // Holding-ID
            hd.startElement("", "", "document", atts);

            // Item tag
            atts.clear();
            atts.addAttribute("", "", "id", CDATA, "0"); // Stock-ID
            hd.startElement("", "", "item", atts);

            // Message tag
            atts.clear();
            atts.addAttribute("", "", "lang", CDATA, "de");
            hd.startElement("", "", "message", atts);
            text = StringEscapeUtils.escapeXml("No holdings found");
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "message");

            // Department tag
            atts.clear();
            atts.addAttribute("", "", "id", CDATA, "0");
            hd.startElement("", "", "department", atts);
            text = StringEscapeUtils.escapeXml(msg);
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "department");

            // Storage tag
            atts.clear();
            hd.startElement("", "", "storage", atts);
            // Zusatztag location
            atts.clear();
            hd.startElement("", "", "location", atts);
            text = StringEscapeUtils.escapeXml("");
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "location");
            hd.endElement("", "", "storage");

            // Unavailable tag
            atts.clear();
            atts.addAttribute("", "", "service", CDATA, "interloan");
            hd.startElement("", "", "unavailable", atts);


            hd.endElement("", "", "unavailable");
            hd.endElement("", "", "item");


            hd.endElement("", "", "document");



            hd.endElement("", "", "daia");
            hd.endDocument();

            xml = new String(out.toByteArray(), UTF8);
            out.close();

        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final SAXException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        }

        return xml;
    }


}