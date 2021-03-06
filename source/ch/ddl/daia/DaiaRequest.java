//  Copyright (C) 2013  Markus Fischer
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//  Contact: info@doctor-doc.com

package ch.ddl.daia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import util.ReadSystemConfigurations;
import ch.dbs.entity.Bestand;

/**
 * Converts a DAIA-XML-Response into a List<Bestand> DAIA = Document
 * Availability Information API see http://ws.gbv.de/daia/daia.xsd.htm <p></p>
 * 
 * @author Markus Fischer
 */
public class DaiaRequest {

    private static final Logger LOG = LoggerFactory.getLogger(DaiaRequest.class);

    public List<Bestand> get(final String openurl) {

        final List<Bestand> bestaende = new ArrayList<Bestand>();

        // TODO: use cached Threadpool with timeouts

        for (final String host : ReadSystemConfigurations.getDaiaHosts()) {

            final String url = host + "?" + openurl;

            try {

                final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                final Document doc = docBuilder.parse(url);

                // normalize text representation
                doc.getDocumentElement().normalize();

                // contains the baseUrl of the server installation, needed
                // to order at the register beeing searched
                final NodeList listOfInstitution = doc.getElementsByTagName("institution");
                final Element institutionElement = (Element) listOfInstitution.item(0);
                final String baseUrl = extractBaseUrl(institutionElement.getAttribute("href"));
                //            System.out.println("BaseURL orderlink: " + baseUrl);

                // Get a list of all holdings
                final NodeList listOfHoldings = doc.getElementsByTagName("document");
                //                final int totalHoldings = listOfHoldings.getLength();
                //                System.out.println("Total no of holdings: " + totalHoldings);

                // process each holding
                for (int s = 0; s < listOfHoldings.getLength(); s++) {

                    final Node firstHoldingNode = listOfHoldings.item(s);
                    if (firstHoldingNode.getNodeType() == Node.ELEMENT_NODE) {

                        // URN and holding ID
                        final Element firstHoldingElement = (Element) firstHoldingNode;
                        final Long hoid = urnExtractID(firstHoldingElement.getAttribute("id"));
                        //                    System.out.println("Holding-URN: " + hoid);

                        // Title of journal
                        final NodeList titelList = firstHoldingElement.getElementsByTagName("message");
                        final Element titelElement = (Element) titelList.item(0);
                        final NodeList textTitelList = titelElement.getChildNodes();
                        String titel = "";
                        if (textTitelList.getLength() > 0) {
                            titel = StringEscapeUtils.unescapeXml(textTitelList.item(0).getNodeValue().trim());
                        }

                        // List of stock elements for one holding
                        final NodeList listOfItems = firstHoldingElement.getElementsByTagName("item");
                        //                        final int totalItems = listOfItems.getLength();
                        //                        System.out.println("Total no of items: " + totalItems);

                        // process each stock element
                        for (int n = 0; n < listOfItems.getLength(); n++) {

                            final Bestand bestand = new Bestand();
                            bestand.getHolding().setId(hoid);
                            bestand.getHolding().setTitel(titel);
                            bestand.getHolding().setBaseurl(baseUrl);

                            final Node firstItemNode = listOfItems.item(n);
                            if (firstItemNode.getNodeType() == Node.ELEMENT_NODE) {

                                String value = "";

                                // URN and stock ID
                                final Element firstItemElement = (Element) firstItemNode;
                                value = firstItemElement.getAttribute("id");
                                //                                System.out.println("Stock-URN: " + value);
                                bestand.setId(urnExtractID(value));

                                // Remarks
                                final NodeList bemerkungenList = firstItemElement.getElementsByTagName("message");
                                if (bemerkungenList.getLength() > 0) {
                                    final Element bemerkungenElement = (Element) bemerkungenList.item(0);
                                    final NodeList textBemerkungenList = bemerkungenElement.getChildNodes();
                                    if (textBemerkungenList.getLength() > 0) {
                                        value = StringEscapeUtils.unescapeXml(textBemerkungenList.item(0)
                                                .getNodeValue().trim());
                                    } else {
                                        value = "";
                                    }
                                    bestand.setBemerkungen(value);
                                }

                                // shelfmark or callnumber
                                final NodeList shelfmarkList = firstItemElement.getElementsByTagName("label");
                                if (shelfmarkList.getLength() > 0) {
                                    final Element shelfmarkElement = (Element) shelfmarkList.item(0);
                                    final NodeList textShelfmarkList = shelfmarkElement.getChildNodes();
                                    if (textShelfmarkList.getLength() > 0) {
                                        value = StringEscapeUtils.unescapeXml(textShelfmarkList.item(0).getNodeValue()
                                                .trim());
                                    } else {
                                        value = "";
                                    }
                                    bestand.setShelfmark(value);
                                }

                                // URN and account ID
                                final NodeList kontoList = firstItemElement.getElementsByTagName("department");
                                if (kontoList.getLength() > 0) {
                                    final Element kontoElement = (Element) kontoList.item(0);
                                    value = kontoElement.getAttribute("id");
                                    final Long kid = urnExtractID(value);
                                    //                                    System.out.println("Konto-URN: " + value);
                                    bestand.getHolding().setKid(kid);
                                    bestand.getHolding().getKonto().setId(kid);

                                    final NodeList textKontoList = kontoElement.getChildNodes();
                                    if (textKontoList.getLength() > 0) {
                                        value = StringEscapeUtils.unescapeXml(textKontoList.item(0).getNodeValue()
                                                .trim());
                                    } else {
                                        value = "";
                                    }
                                    bestand.getHolding().getKonto().setBibliotheksname(value);
                                }

                                // ----
                                final NodeList storageList = firstItemElement.getElementsByTagName("storage");
                                if (storageList.getLength() > 0) {
                                    final Element storageElement = (Element) storageList.item(0);
                                    final NodeList textStorageList = storageElement.getChildNodes();
                                    if (textStorageList.getLength() > 0) {
                                        value = textStorageList.item(0).getNodeValue().trim();
                                        value = StringEscapeUtils.unescapeXml(textStorageList.item(0).getNodeValue()
                                                .trim());
                                    } else {
                                        value = "";
                                    }
                                    bestand.getStandort().setInhalt(value);
                                }

                                // Read available tag and limitations
                                final NodeList availableList = firstItemElement.getElementsByTagName("available");

                                if (availableList.getLength() > 0) {

                                    // Limitations
                                    final Element availableElement = (Element) availableList.item(0);
                                    final NodeList availableLimitationList = availableElement
                                            .getElementsByTagName("limitation");
                                    value = "";
                                    // there may be other limitations than country...
                                    for (int m = 0; m < availableLimitationList.getLength(); m++) {
                                        final Element countryElement = (Element) availableLimitationList.item(m);
                                        final NodeList textCountryList = countryElement.getChildNodes();
                                        if (textCountryList.getLength() > 0
                                                && textCountryList.item(0).getNodeValue().startsWith("Country: ")) {
                                            value = textCountryList.item(0).getNodeValue().substring(9).trim();
                                        }
                                    }

                                    bestand.getHolding().getKonto().setLand(value);

                                    bestaende.add(bestand);
                                }
                            }
                        }
                    }
                }

            } catch (final SAXParseException e) {
                LOG.error(e.toString());

            } catch (final SAXException e) {
                LOG.error(e.toString());

            } catch (final IOException e) {
                LOG.error(e.toString());
            } catch (final ParserConfigurationException e) {
                LOG.error(e.toString());
            }

        }

        return bestaende;

    }

    /**
     * Extracts the ID out of an URN
     * 
     * @param String urn
     * @return Long id
     */
    private Long urnExtractID(final String urn) {
        Long id = null;

        if (urn.contains("=")) {
            try {
                id = Long.valueOf(urn.substring(urn.lastIndexOf('=') + 1));
            } catch (final Exception e) {
                LOG.error("resolveUrn(final String urn): " + e.toString());
            }
        }

        return id;
    }

    /**
     * Extracts the BaseUrl out of the DAIA-URL
     * 
     * @param String url
     * @return String BaseUrl
     */
    private String extractBaseUrl(final String url) {
        String baseUrl = "";

        if (url.contains("/daia.do")) {
            try {
                baseUrl = url.substring(0, url.indexOf("/daia.do"));
            } catch (final Exception e) {
                LOG.error("extractBaseUrl(final String url): " + e.toString());
            }
        }

        return baseUrl;
    }

}
