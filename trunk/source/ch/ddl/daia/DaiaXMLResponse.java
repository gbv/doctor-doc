package ch.ddl.daia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.grlea.log.SimpleLogger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestand;

/**
 * Gives a DAIA response in XML to a OpenURL request. DAIA = Document
 * Availability Information API http://ws.gbv.de/daia/daia.xsd.htm
 * <p/>
 * 
 * @author Markus Fischer
 */
public class DaiaXMLResponse {

    private static final SimpleLogger LOG = new SimpleLogger(DaiaXMLResponse.class);
    private static final String UTF8 = "UTF-8";
    private static final String CDATA = "CDATA";
    private static final String URL = ReadSystemConfigurations.getServerInstallation() + "/stockinfo.do?";
    private static final String URN = "urn:x-domain:" + URL;

    public String listHoldings(final List<Bestand> bestaende, final String rfr_id) {

        String xml = "";
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            final StreamResult streamResult = new StreamResult(out);
            final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

            final TransformerHandler hd = tf.newTransformerHandler();
            final Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, UTF8);
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            //            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
            hd.setResult(streamResult);

            // SAX2.0 ContentHandler
            //            final ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.processingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"jsp/import/xsl/daia.xsl\"");

            final AttributesImpl atts = new AttributesImpl();

            final Date d = new Date();
            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());

            // ROOT tag
            atts.addAttribute("", "", "xmlns", CDATA, "http://ws.gbv.de/daia/");
            atts.addAttribute("", "", "version", CDATA, "0.5");
            atts.addAttribute("", "", "xmlns:xsi", CDATA, "http://www.w3.org/2001/XMLSchema-instance");
            atts.addAttribute("", "", "xsi:schemaLocation", CDATA,
                    "http://ws.gbv.de/daia/ http://ws.gbv.de/daia/daia.xsd");
            atts.addAttribute("", "", "timestamp", CDATA, datum);
            hd.startElement("", "", "daia", atts);

            // Institution tag
            atts.clear();
            atts.addAttribute("", "", "href", CDATA, ReadSystemConfigurations.getServerInstallation() + "/daia.do");
            hd.startElement("", "", "institution", atts);
            String text = ReadSystemConfigurations.getApplicationName();
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "institution");

            if (rfr_id != null && !rfr_id.equals("")) {
                // Message tag (used for the rfr_id of a OpenURL request)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = rfr_id;
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");
            }

            for (final Bestand b : bestaende) {
                // Document tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "holding=" + b.getHolding().getId().toString()); // Holding-ID
                atts.addAttribute("", "", "href", CDATA, URL + "holding=" + b.getHolding().getId().toString()); // URL to holding
                hd.startElement("", "", "document", atts);

                // Message tag (used for journal title)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = b.getHolding().getTitel();
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Item tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "stock=" + b.getId().toString()); // Stock-ID
                atts.addAttribute("", "", "href", CDATA, URL + "stock=" + b.getId().toString()); // URL to stock
                hd.startElement("", "", "item", atts);

                // Message tag (used for 'remarks' of a holding)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = b.getBemerkungen();
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");

                // Label tag (call number / Shelfmark)
                atts.clear();
                hd.startElement("", "", "label", atts);
                text = b.getShelfmark();
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "label");

                // Department tag
                atts.clear();
                atts.addAttribute("", "", "id", CDATA, URN + "library=" + b.getHolding().getKid().toString());
                atts.addAttribute("", "", "href", CDATA, URL + "library=" + b.getHolding().getKid().toString()); // URL to all holdings of library
                hd.startElement("", "", "department", atts);
                text = b.getHolding().getKonto().getBibliotheksname() + "\040" + b.getHolding().getKonto().getOrt()
                        + "\040" + b.getHolding().getKonto().getLand();
                //                text = StringEscapeUtils.escapeXml(text);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "department");

                // Storage tag
                atts.clear();
                hd.startElement("", "", "storage", atts);
                text = b.getStandort().getInhalt();
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "storage");

                // Available tag
                atts.clear();
                // default service presentation
                text = "presentation";
                // if we have a DID set service to interloan
                if (b.getHolding().getKonto().getDid() != null) {
                    text = "interloan";
                }
                atts.addAttribute("", "", "service", CDATA, text);
                hd.startElement("", "", "available", atts);

                // Limitation tag (for countries: CH, DE, AT...)
                // for now we use only one configuration: the country of the library
                // TODO: allow multiple, configurable countries
                atts.clear();
                hd.startElement("", "", "limitation", atts);
                text = "Country: " + b.getHolding().getKonto().getLand();
                //                text = StringEscapeUtils.escapeXml(text);
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "limitation");

                hd.endElement("", "", "available");
                hd.endElement("", "", "item");
                hd.endElement("", "", "document");

            }

            hd.endElement("", "", "daia");
            hd.endDocument();

            xml = new String(out.toByteArray(), UTF8);

        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final SAXException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        } finally {
            try {
                out.close();
            } catch (final IOException e) {
                LOG.error(e.toString());
            }
        }

        return xml;
    }

    public String noHoldings(final String msg, final String rfr_id) {

        String xml = "";
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            final StreamResult streamResult = new StreamResult(out);
            final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

            final TransformerHandler hd = tf.newTransformerHandler();
            final Transformer serializer = hd.getTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            //            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");

            hd.setResult(streamResult);

            // SAX2.0 ContentHandler
            //            final  ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();
            hd.processingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"jsp/import/xsl/daia.xsl\"");

            final AttributesImpl atts = new AttributesImpl();

            final Date d = new Date();
            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());

            // ROOT tag
            atts.addAttribute("", "", "xmlns", CDATA, "http://ws.gbv.de/daia/");
            atts.addAttribute("", "", "version", CDATA, "0.5");
            atts.addAttribute("", "", "xmlns:xsi", CDATA, "http://www.w3.org/2001/XMLSchema-instance");
            atts.addAttribute("", "", "xsi:schemaLocation", CDATA,
                    "http://ws.gbv.de/daia/ http://ws.gbv.de/daia/daia.xsd");
            atts.addAttribute("", "", "timestamp", CDATA, datum);
            hd.startElement("", "", "daia", atts);

            // Institution tag
            atts.clear();
            atts.addAttribute("", "", "href", CDATA, ReadSystemConfigurations.getServerInstallation() + "/daia.do");
            hd.startElement("", "", "institution", atts);
            String text = ReadSystemConfigurations.getApplicationName();
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "institution");

            // used for DAIA-Driver in Vufind: setting ID back in response from rfr_id
            // this has to be the first message element!
            if (rfr_id != null && !rfr_id.equals("")) {
                // Message tag (used for the rfr_id of a OpenURL request)
                atts.clear();
                atts.addAttribute("", "", "lang", CDATA, "de");
                hd.startElement("", "", "message", atts);
                text = rfr_id;
                hd.characters(text.toCharArray(), 0, text.length());
                hd.endElement("", "", "message");
            }

            // official/additional message tag
            atts.clear();
            atts.addAttribute("", "", "lang", CDATA, "de");
            hd.startElement("", "", "message", atts);
            text = msg;
            hd.characters(text.toCharArray(), 0, text.length());
            hd.endElement("", "", "message");

            hd.endElement("", "", "daia");
            hd.endDocument();

            xml = new String(out.toByteArray(), UTF8);

        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final SAXException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        } finally {
            try {
                out.close();
            } catch (final IOException e) {
                LOG.error(e.toString());
            }
        }

        return xml;
    }

}
