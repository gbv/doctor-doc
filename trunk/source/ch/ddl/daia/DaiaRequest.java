package ch.ddl.daia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.grlea.log.SimpleLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import util.ReadSystemConfigurations;
import ch.dbs.entity.Bestand;

/**
 * Übersetzt eine DAIA-XML-Response in ein ArrayList<Bestand> DAIA = Document
 * Availability Information API http://ws.gbv.de/daia/daia.xsd.htm
 * <p/>
 *
 * @author Markus Fischer
 */
public class DaiaRequest {

    private static final SimpleLogger LOG = new SimpleLogger(DaiaRequest.class);

    public List<Bestand> get(String openurl) {

        final List<Bestand> bestaende = new ArrayList<Bestand>();
        // TODO: ggf. DAIA-ID mitschicken um kontospezifische Ausgaben zu erhalten
        openurl = ReadSystemConfigurations.getDaiaHost() + "?" + openurl;
        //    openurl = "http://localhost:8080/daia.do?" + openurl;

        try {

            final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            final Document doc = docBuilder.parse(openurl);

            // normalize text representation
            doc.getDocumentElement().normalize();
            //      System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            final NodeList listOfInstitution = doc.getElementsByTagName("institution");
            final Element institutionElement = (Element) listOfInstitution.item(0);
            final String institution = institutionElement.getAttribute("href");
            System.out.println("Orderlink: " + institution);

            final NodeList listOfHoldings = doc.getElementsByTagName("document");
            final int totalHoldings = listOfHoldings.getLength();
            System.out.println("Total no of holdings: " + totalHoldings);

            for (int s = 0; s < listOfHoldings.getLength(); s++) {

                final Node firstHoldingNode = listOfHoldings.item(s);
                if (firstHoldingNode.getNodeType() == Node.ELEMENT_NODE) {

                    final Element firstHoldingElement = (Element) firstHoldingNode;
                    final String hoid = firstHoldingElement.getAttribute("id");
                    //                    System.out.println("Holding-URN: " + hoid);

                    // -------
                    final NodeList titelList = firstHoldingElement.getElementsByTagName("message");
                    final Element titelElement = (Element) titelList.item(0);
                    final NodeList textTitelList = titelElement.getChildNodes();
                    String titel = "";
                    if (textTitelList.getLength() > 0) {
                        titel = StringEscapeUtils.unescapeXml(((Node) textTitelList.item(0)).getNodeValue().trim());
                    }
                    //                    System.out.println("Titel: " + titel);


                    final NodeList listOfItems = firstHoldingElement.getElementsByTagName("item");
                    final int totalItems = listOfItems.getLength();
                    System.out.println("Total no of items: " + totalItems);

                    for (int n = 0; n < listOfItems.getLength(); n++) {

                        final Bestand bestand = new Bestand();
                        bestand.getHolding().setId(resolveUrn(hoid));
                        bestand.getHolding().setTitel(titel);

                        final Node firstItemNode = listOfItems.item(n);
                        if (firstItemNode.getNodeType() == Node.ELEMENT_NODE) {

                            String value = "";

                            final Element firstItemElement = (Element) firstItemNode;
                            value = firstItemElement.getAttribute("id");
                            //                            System.out.println("Stock-URN: " + value);
                            bestand.setId(resolveUrn(value));

                            // -------
                            final NodeList bemerkungenList = firstItemElement.getElementsByTagName("message");
                            if (bemerkungenList.getLength() > 0) {
                                final Element bemerkungenElement = (Element) bemerkungenList.item(0);
                                final NodeList textBemerkungenList = bemerkungenElement.getChildNodes();
                                if (textBemerkungenList.getLength() > 0) {
                                    value = StringEscapeUtils.unescapeXml(((Node) textBemerkungenList.item(0))
                                            .getNodeValue().trim());
                                } else {
                                    value = "";
                                }
                                //                                System.out.println("Bemerkungen: " + value);
                                bestand.setBemerkungen(value);
                            }

                            final NodeList shelfmarkList = firstItemElement.getElementsByTagName("label");
                            if (shelfmarkList.getLength() > 0) {
                                final Element shelfmarkElement = (Element) shelfmarkList.item(0);
                                final NodeList textShelfmarkList = shelfmarkElement.getChildNodes();
                                if (textShelfmarkList.getLength() > 0) {
                                    value = StringEscapeUtils.unescapeXml(((Node) textShelfmarkList.item(0))
                                            .getNodeValue().trim());
                                } else {
                                    value = "";
                                }
                                //                                System.out.println("Gestell: " + value);
                                bestand.setShelfmark(value);
                            }

                            // -------
                            final NodeList kontoList = firstItemElement.getElementsByTagName("department");
                            if (kontoList.getLength() > 0) {
                                final Element kontoElement = (Element) kontoList.item(0);
                                value = kontoElement.getAttribute("id");
                                final Long kid = resolveUrn(value);
                                //                                System.out.println("Konto-URN: " + value);
                                bestand.getHolding().setKid(kid);
                                bestand.getHolding().getKonto().setId(kid);

                                final NodeList textKontoList = kontoElement.getChildNodes();
                                if (textKontoList.getLength() > 0) {
                                    value = StringEscapeUtils.unescapeXml(((Node) textKontoList.item(0)).getNodeValue()
                                            .trim());
                                } else {
                                    value = "";
                                }
                                //                                System.out.println("Konto: " + value);
                                bestand.getHolding().getKonto().setBibliotheksname(value);
                            }

                            // ----
                            final NodeList storageList = firstItemElement.getElementsByTagName("storage");
                            if (storageList.getLength() > 0) {
                                final Element storageElement = (Element) storageList.item(0);
                                final NodeList textStorageList = storageElement.getChildNodes();
                                if (textStorageList.getLength() > 0) {
                                    value = ((Node) textStorageList.item(0)).getNodeValue().trim();
                                } else {
                                    value = "";
                                }
                                //                                System.out.println("Storage: " + value);
                            }

                            // ----
                            final NodeList standortList = firstItemElement.getElementsByTagName("location");
                            if (standortList.getLength() > 0) {
                                final Element standortElement = (Element) standortList.item(0);
                                final NodeList textStandortList = standortElement.getChildNodes();
                                if (textStandortList.getLength() > 0) {
                                    value = StringEscapeUtils.unescapeXml(((Node) textStandortList.item(0))
                                            .getNodeValue().trim());
                                } else {
                                    value = "";
                                }
                                //                                System.out.println("Standort: " + value);
                                bestand.getStandort().setInhalt(value);
                            }

                            // ------
                            final NodeList availableList = firstItemElement.getElementsByTagName("available");

                            if (availableList.getLength() > 0) { // nur zurückgeben, falls verfügbar

                                final Element availableElement = (Element) availableList.item(0);
                                //                                System.out.println("Available: " + availableElement.getAttribute("service"));

                                // ------
                                final NodeList availableLimitationList = availableElement.getElementsByTagName("limitation");
                                final int totalLimitations = availableLimitationList.getLength();
                                System.out.println("Total no of limitations: " + totalLimitations);

                                // for (int m = 0; m < availableLimitationList.getLength(); m++) {
                                //   String deliveryway = "";
                                //   Element availableLimitationElement = (Element) availableLimitationList.item(m);
                                //   deliveryway = availableLimitationElement.getAttribute("id");
                                //   System.out.println("Lieferart: " + deliveryway);
                                //   NodeList costsList = availableLimitationElement.getElementsByTagName("costs");
                                //   Element costsElement = (Element) costsList.item(0);
                                //   String waehrung = costsElement.getAttribute("id");
                                //   NodeList textCostsList = costsElement.getChildNodes();
                                //   if (textCostsList.getLength() > 0) {
                                //        value = ((Node) textCostsList.item(0)).getNodeValue().trim();
                                //   } else {
                                //        value = "";
                                //          }
                                //        System.out.println(waehrung + ": " + value);
                                //   }

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

        return bestaende;

    }

    private Long resolveUrn(final String urn) {
        Long id = null;

        if (urn.contains(":")) {
            try {
                id = Long.valueOf(urn.substring(urn.lastIndexOf(':') + 1));
            } catch (final Exception e) {
                LOG.error("resolveUrn(String urn): " + e.toString());
            }
        }

        return id;
    }


}
