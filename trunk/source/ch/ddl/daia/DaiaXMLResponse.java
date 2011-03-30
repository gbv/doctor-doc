package ch.ddl.daia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
 * Gives a DAIA response in XML to a OpenURL request.
 * DAIA = Document Availability Information API
 * http://ws.gbv.de/daia/daia.xsd.htm
 * <p/>
 * @author Markus Fischer
 */
public class DaiaXMLResponse {

    private static final SimpleLogger LOG = new SimpleLogger(DaiaXMLResponse.class);
    private static final String UTF8 = "UTF-8";
    private static final String CDATA = "CDATA";
    private static final String URN = "urn:x-domain:" + ReadSystemConfigurations.getServerInstallation() + "/stockinfo.do:";

    public String listHoldings(final List<Bestand> bestaende, final String rfr_id) {

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

            final Date d = new Date();
            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());

            // ROOT tag
            atts.addAttribute("", "", "timestamp", CDATA, datum);
            atts.addAttribute("", "", "version", CDATA, "0.5");
            hd.startElement("", "", "daia", atts);

            // Institution tag
            atts.clear();
            atts.addAttribute("", "", "href", CDATA, ReadSystemConfigurations.getServerInstallation() + "/daia.do");
            hd.startElement("", "", "institution", atts);
            String text = "Register " + ReadSystemConfigurations.getApplicationName();
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "institution");

            if (rfr_id != null && !rfr_id.equals("")) {
                // Message tag (used for the rfr_id of a OpenURL request)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(rfr_id);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");
            }

            for (final Bestand b : bestaende) {
                // Document tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "holding:" + b.getHolding().getId().toString()); // Holding-ID
                hd.startElement("", "", "document", atts);

                // Message tag (used for journal title)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(b.getHolding().getTitel());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Item tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "stock:" + b.getId().toString()); // Stock-ID
                hd.startElement("", "", "item", atts);

                // Message tag (used for 'remarks' of a holding)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = StringEscapeUtils.escapeXml(b.getBemerkungen());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Label tag (call number / Shelfmark)
                atts.clear();
                hd.startElement("", "", "label", atts);
                text = StringEscapeUtils.escapeXml(b.getShelfmark());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "label");

                // Department tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "library:" + b.getHolding().getKid().toString());
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
                // additional tag location
                atts.clear();
                hd.startElement("", "", "location", atts);
                text = StringEscapeUtils.escapeXml(b.getStandort().getInhalt());
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "location");
                hd.endElement("", "", "storage");

                // Available tag
                atts.clear();
                atts.addAttribute("", "", "service", CDATA, "interloan");
                hd.startElement("", "", "available", atts);

                // Limitation tag for ILL indicating true/false
                // needs a DID (DAIAParam) in the konto
                text = "false";
                if (b.getHolding().getKonto().getDid() != null)  { text = "true"; }
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, "ILL");
                hd.startElement("", "", "limitation", atts);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "limitation");

                // Limitation tag (for countries: CH, DE, AT...)
                // for now we use only one configuration: the country of the library
                // TODO: allow multiple, configurable countries
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, "country");
                hd.startElement("", "", "limitation", atts);
                text = b.getHolding().getKonto().getLand();
                text = StringEscapeUtils.escapeXml(text);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "limitation");

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

            final Date d = new Date();
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
                // Message tag (used for the rfr_id of a OpenURL request)
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
            // additional tag location
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
