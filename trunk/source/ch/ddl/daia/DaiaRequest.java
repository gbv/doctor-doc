package ch.ddl.daia;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.grlea.log.SimpleLogger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
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
public class DaiaRequest extends ReadSystemConfigurations {

	private static final SimpleLogger log = new SimpleLogger(DaiaRequest.class);

	public ArrayList<Bestand> get(String openurl) {

		ArrayList<Bestand> bestaende = new ArrayList<Bestand>();
		// TODO: ggf. DAIA-ID mitschicken um kontospezifische Ausgaben zu erhalten 
		 openurl = getDaiaHost() + "?" + openurl;
//		openurl = "http://localhost:8080/daia.do?" + openurl;
		
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(openurl);

			// normalize text representation
			doc.getDocumentElement().normalize();
//			System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());
			
			NodeList listOfInstitution = doc.getElementsByTagName("institution");
			Element institutionElement = (Element) listOfInstitution.item(0);
			String institution = institutionElement.getAttribute("href");
			System.out.println("Orderlink: " + institution);

			NodeList listOfHoldings = doc.getElementsByTagName("document");
			int totalHoldings = listOfHoldings.getLength();
			System.out.println("Total no of holdings: " + totalHoldings);

			for (int s=0; s<listOfHoldings.getLength();s++) {

				Node firstHoldingNode = listOfHoldings.item(s);
				if (firstHoldingNode.getNodeType() == Node.ELEMENT_NODE) {					

					Element firstHoldingElement = (Element) firstHoldingNode;
					String hoid = firstHoldingElement.getAttribute("id");
					System.out.println("Holding-URN: " + hoid);	
					
					// -------
					NodeList titelList = firstHoldingElement.getElementsByTagName("message");
					Element titelElement = (Element) titelList.item(0);
					NodeList textTitelList = titelElement.getChildNodes();
					String titel = "";
					if (textTitelList.getLength()>0) titel = StringEscapeUtils.unescapeXml(((Node) textTitelList.item(0)).getNodeValue().trim());
					System.out.println("Titel: " + titel);
					
					
					NodeList listOfItems = firstHoldingElement.getElementsByTagName("item");
					int totalItems = listOfItems.getLength();
					System.out.println("Total no of items: " + totalItems);
					
					for (int n=0;n<listOfItems.getLength();n++) {
						
						Bestand bestand = new Bestand();
						bestand.getHolding().setId(resolveUrn(hoid));
						bestand.getHolding().setTitel(titel);
						
						Node firstItemNode = listOfItems.item(n);
						if (firstItemNode.getNodeType() == Node.ELEMENT_NODE) {
							
							String value = "";
							
							Element firstItemElement = (Element) firstItemNode;
							value = firstItemElement.getAttribute("id");
							System.out.println("Stock-URN: " + value);
							bestand.setId(resolveUrn(value));
							
							// -------
							NodeList bemerkungenList = firstItemElement.getElementsByTagName("message");
							if (bemerkungenList.getLength()>0) {
							Element bemerkungenElement = (Element) bemerkungenList.item(0);
							NodeList textBemerkungenList = bemerkungenElement.getChildNodes();
							if (textBemerkungenList.getLength()>0) {value = StringEscapeUtils.unescapeXml(((Node) textBemerkungenList.item(0)).getNodeValue().trim());} else {value="";}
							System.out.println("Bemerkungen: " + value);
							bestand.setBemerkungen(value);
							}
							
							NodeList shelfmarkList = firstItemElement.getElementsByTagName("label");
							if (shelfmarkList.getLength()>0) {
							Element shelfmarkElement = (Element) shelfmarkList.item(0);
							NodeList textShelfmarkList = shelfmarkElement.getChildNodes();
							if (textShelfmarkList.getLength()>0) {value=StringEscapeUtils.unescapeXml(((Node) textShelfmarkList.item(0)).getNodeValue().trim());} else {value="";}
							System.out.println("Gestell: " + value);
							bestand.setShelfmark(value);
							}
							
							// -------
							NodeList kontoList = firstItemElement.getElementsByTagName("department");
							if (kontoList.getLength()>0) {
							Element kontoElement = (Element) kontoList.item(0);
							value = kontoElement.getAttribute("id");
							Long kid = resolveUrn(value);
							System.out.println("Konto-URN: " + value);
							bestand.getHolding().setKid(kid);
							bestand.getHolding().getKonto().setId(kid);

							NodeList textKontoList = kontoElement.getChildNodes();
							if (textKontoList.getLength()>0) {value=StringEscapeUtils.unescapeXml(((Node) textKontoList.item(0)).getNodeValue().trim());} else {value="";}
							System.out.println("Konto: " + value);
							bestand.getHolding().getKonto().setBibliotheksname(value);
							}
							
							// ----
							NodeList storageList = firstItemElement.getElementsByTagName("storage");
							if (storageList.getLength()>0) {
							Element storageElement = (Element) storageList.item(0);
							NodeList textStorageList = storageElement.getChildNodes();
							if (textStorageList.getLength()>0) {value=((Node) textStorageList.item(0)).getNodeValue().trim();} else {value="";}
							System.out.println("Storage: " + value);
							}
							
							// ----
							NodeList standortList = firstItemElement.getElementsByTagName("location");
							if (standortList.getLength()>0) {
							Element standortElement = (Element) standortList.item(0);
							NodeList textStandortList = standortElement.getChildNodes();
							if (textStandortList.getLength()>0) {value=StringEscapeUtils.unescapeXml(((Node) textStandortList.item(0)).getNodeValue().trim());} else {value="";}
							System.out.println("Standort: " + value);
							bestand.getStandort().setInhalt(value);
							}
							
							// ------
							NodeList availableList = firstItemElement.getElementsByTagName("available");
							
							if (availableList.getLength()>0) { // nur zurückgeben, falls verfügbar
							
							Element availableElement = (Element) availableList.item(0);					
							System.out.println("Available: " + availableElement.getAttribute("service"));
							
							// ------							
							NodeList availableLimitationList = availableElement.getElementsByTagName("limitation");
							int totalLimitations = availableLimitationList.getLength();
							System.out.println("Total no of limitations: " + totalLimitations);
							
//							for (int m=0;m<availableLimitationList.getLength();m++) {
//								String deliveryway = "";
//								Element availableLimitationElement = (Element) availableLimitationList.item(m);
//								deliveryway = availableLimitationElement.getAttribute("id");
//								System.out.println("Lieferart: " + deliveryway);
//								
//								NodeList costsList = availableLimitationElement.getElementsByTagName("costs");
//								Element costsElement = (Element) costsList.item(0);
//								String waehrung = costsElement.getAttribute("id");
//								NodeList textCostsList = costsElement.getChildNodes();
//								if (textCostsList.getLength()>0) {value=((Node) textCostsList.item(0)).getNodeValue().trim();} else {value="";}
//								System.out.println(waehrung + ": " + value);
//								
//							}
							
							bestaende.add(bestand);							
							}
							
						}						
					}
				}
			}

		} catch (SAXParseException e) {
			log.error(e.toString());

		} catch (SAXException e) {
			log.error(e.toString());

		} catch (Throwable t) {
			log.error(t.toString());
		}

		return bestaende;
		
	}
	
	private Long resolveUrn(String urn) {
		Long id = null;
		
		if (urn.contains(":")) {
			try {				
				id = Long.valueOf(urn.substring(urn.lastIndexOf(":")+1));				
			} catch (Exception e) {
				log.error("resolveUrn(String urn): " + e.toString());
			}
		}
		
		return id;
	}

	
}
