//  Copyright (C) 2011  Markus Fischer
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

package ch.dbs.actions.bestellung;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.dbs.form.EZBDataOnline;
import ch.dbs.form.EZBDataPrint;
import ch.dbs.form.EZBForm;
import ch.dbs.form.EZBReference;
import enums.JOPState;

public class EZBJOP {
    
    private static final Logger LOG = LoggerFactory.getLogger(EZBJOP.class);
    
    /**
     * This class uses the official EZB/ZDB API from
     * http://services.dnb.de/fize-service/gvr/full.xml.
     */
    public EZBForm read(final String content) {
        
        final EZBForm ezbform = new EZBForm();
        
        try {
            
            if (content != null) {
                
                final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                final DocumentBuilder builder = domFactory.newDocumentBuilder();
                final Document doc = builder.parse(new InputSource(new StringReader(content)));
                
                final XPathFactory factory = XPathFactory.newInstance();
                final XPath xpath = factory.newXPath();
                
                // references electronic data
                //            final XPathExpression exprRefE = xpath.compile("//ElectronicData/References/Reference");
                //            final NodeList resultListRefE = (NodeList) exprRefE.evaluate(doc, XPathConstants.NODESET);
                //
                //            for (int i = 0; i < resultListRefE.getLength(); i++) {
                //                final Node firstResultNode = resultListRefE.item(i);
                //                final Element result = (Element) firstResultNode;
                //
                //                final EZBReference ref = new EZBReference();
                //
                //                // EZB URLs
                //                final String url = getValue(result.getElementsByTagName("URL"));
                //                ref.setUrl(url);
                //
                //                // Label for URLs
                //                final String label = getValue(result.getElementsByTagName("Label"));
                //                ref.setLabel(label);
                //
                //                ezbform.getReferencesonline().add(ref);
                //            }
                
                // electronic data
                final XPathExpression exprE = xpath.compile("//ElectronicData/ResultList/Result");
                final NodeList resultListE = (NodeList) exprE.evaluate(doc, XPathConstants.NODESET);
                
                for (int i = 0; i < resultListE.getLength(); i++) {
                    final Node firstResultNode = resultListE.item(i);
                    final Element result = (Element) firstResultNode;
                    
                    final EZBDataOnline online = new EZBDataOnline();
                    
                    // state
                    online.setState(Integer.valueOf(result.getAttribute("state")));
                    // 0 free accessible
                    if (online.getState() == JOPState.FREE.getValue()) {
                        online.setAmpel("green");
                        online.setComment("availresult.free");
                        // 1 partially free accesible
                    } else if (online.getState() == JOPState.FREE_PARTIALLY.getValue()) {
                        online.setAmpel("green");
                        online.setComment("availresult.partially_free");
                        // 2 licensed ; 3 partially licensed
                    } else if (online.getState() == JOPState.LICENSED.getValue()
                            || online.getState() == JOPState.LICENSED_PARTIALLY.getValue()) {
                        online.setAmpel("yellow");
                        online.setComment("availresult.abonniert");
                        // journal not online for periode
                    } else if (online.getState() == JOPState.OUTSIDE_PERIOD.getValue()) {
                        online.setAmpel("red");
                        online.setComment("availresult.timeperiode");
                        // not indexed
                    } else if (online.getState() == JOPState.NO_HITS.getValue()) {
                        online.setAmpel("red");
                        online.setComment("availresult.nohits");
                    } else {
                        online.setAmpel("red");
                        online.setComment("availresult.not_licensed");
                    }
                    
                    // title
                    String title = getValue(result.getElementsByTagName("Title"));
                    if (title != null) {
                        title = Jsoup.clean(title, Whitelist.none());
                        online.setTitle(Jsoup.parse(title).text());
                    }
                    
                    online.setUrl(getValue(result.getElementsByTagName("AccessURL")));
                    online.setLevel(getValue(result.getElementsByTagName("AccessLevel")));
                    online.setReadme(getValue(result.getElementsByTagName("ReadmeURL")));
                    // National licenses etc.
                    online.setAdditional(getValue(result.getElementsByTagName("Additional")));
                    
                    ezbform.getOnline().add(online);
                }
                
                // Title not found
                if (resultListE.getLength() == 0) {
                    final EZBDataOnline online = new EZBDataOnline();
                    online.setAmpel("red");
                    online.setComment("availresult.nohits");
                    online.setState(JOPState.NO_HITS.getValue());
                    
                    ezbform.getOnline().add(online);
                }
                
                // references print data
                final XPathExpression exprRefP = xpath.compile("//PrintData/References/Reference");
                final NodeList resultListRefP = (NodeList) exprRefP.evaluate(doc, XPathConstants.NODESET);
                
                final EZBReference ref = new EZBReference();
                
                for (int i = 0; i < resultListRefP.getLength(); i++) {
                    final Node firstResultNode = resultListRefP.item(i);
                    final Element result = (Element) firstResultNode;
                    
                    // EZB URLs
                    ref.setUrl(getValue(result.getElementsByTagName("URL")));
                    
                    // Label for URLs
                    //                final String label = getValue(result.getElementsByTagName("Label"));
                    ref.setLabel("availresult.link_title_print");
                    
                    ezbform.getReferencesprint().add(ref);
                }
                
                // print data
                final XPathExpression exprP = xpath.compile("//PrintData/ResultList/Result");
                final NodeList resultListP = (NodeList) exprP.evaluate(doc, XPathConstants.NODESET);
                
                for (int i = 0; i < resultListP.getLength(); i++) {
                    final Node firstResultNode = resultListP.item(i);
                    final Element result = (Element) firstResultNode;
                    
                    final EZBDataPrint print = new EZBDataPrint();
                    
                    // state
                    print.setState(Integer.valueOf(result.getAttribute("state")));
                    
                    // title
                    String title = getValue(result.getElementsByTagName("Title"));
                    if (title != null) {
                        title = Jsoup.clean(title, Whitelist.none());
                        print.setTitle(Jsoup.parse(title).text());
                    }
                    
                    print.setLocation(getValue(result.getElementsByTagName("Location")));
                    print.setCallnr(getValue(result.getElementsByTagName("Signature")));
                    print.setCoverage(getValue(result.getElementsByTagName("Period")));
                    
                    // set previous extracted URL and label
                    print.setInfo(ref);
                    
                    // in stock ; partially in stock
                    if (print.getState() == JOPState.LICENSED.getValue()
                            || print.getState() == JOPState.LICENSED_PARTIALLY.getValue()) {
                        print.setAmpel("yellow");
                        print.setComment("availresult.print");
                        // only return if existing in Print
                        ezbform.getPrint().add(print);
                    }
                }
            }
            
        } catch (final XPathExpressionException e) {
            LOG.error(e.toString());
        } catch (final SAXParseException e) {
            LOG.error(e.toString());
        } catch (final SAXException e) {
            LOG.error(e.toString());
        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final ParserConfigurationException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        }
        
        return ezbform;
    }
    
    private String getValue(final NodeList list) {
        String result = null;
        
        final Element listElement = (Element) list.item(0);
        if (listElement != null) {
            final NodeList textList = listElement.getChildNodes();
            if (textList.getLength() > 0) {
                result = StringEscapeUtils.unescapeXml(textList.item(0).getNodeValue());
            }
        }
        
        return result;
    }
    
}
