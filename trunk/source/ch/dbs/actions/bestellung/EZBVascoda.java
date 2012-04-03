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
import org.grlea.log.SimpleLogger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ch.dbs.form.EZBDataOnline;
import ch.dbs.form.EZBForm;

public class EZBVascoda {

    private static final SimpleLogger LOG = new SimpleLogger(EZBVascoda.class);

    /**
     * This class uses the EZB API from
     * http://ezb.uni-regensburg.de/ezeit/vascoda/openURL?pid=format%3Dxml. This
     * API differs from the EZB/ZDB API (http://services.dnb.de). It brings
     * back no print information and other information for electronic holdings.
     * It seems to be more stable.
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

                // issns
                final XPathExpression exprRefE = xpath.compile("//OpenURLResponse");
                final NodeList resultListRefE = (NodeList) exprRefE.evaluate(doc, XPathConstants.NODESET);

                String title = null;
                String levelAvailable = null;

                for (int i = 0; i < resultListRefE.getLength(); i++) {
                    final Node firstResultNode = resultListRefE.item(i);
                    final Element result = (Element) firstResultNode;

                    // First ISSN
                    //                    final String issn = getValue(result.getElementsByTagName("issn"));
                    //                    System.out.println(issn);

                    // title
                    // unfortunately this will bring back the title sent by OpenURL, unless if not
                    // specified in the OpenURL request. It then brings back the title form the EZB...!
                    title = getValue(result.getElementsByTagName("title"));
                    if (title != null) {
                        title = Jsoup.clean(title, Whitelist.basic());
                        title = Jsoup.parse(title).text();
                    }

                    // this is the overall level of the best match and not the level of each individual result
                    final NodeList levelNode = result.getElementsByTagName("available");
                    final Element levelElement = (Element) levelNode.item(0);
                    if (levelElement != null) {
                        levelAvailable = levelElement.getAttribute("level");
                    }

                }

                // electronic data
                final XPathExpression exprE = xpath.compile("//OpenURLResponse/OpenURLResult/Resultlist/Result");
                final NodeList resultListE = (NodeList) exprE.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < resultListE.getLength(); i++) {
                    final Node firstResultNode = resultListE.item(i);
                    final Element result = (Element) firstResultNode;

                    final NodeList state = result.getElementsByTagName("access");
                    final Element stateElement = (Element) state.item(0);
                    int color = 0;
                    if (stateElement != null) {
                        color = Integer.valueOf(stateElement.getAttribute("color"));
                    }

                    final EZBDataOnline online = new EZBDataOnline();

                    // state
                    // 1 free accessible
                    if (color == 1) {
                        online.setAmpel("green");
                        online.setComment("availresult.free");
                        online.setState(0); // translate state to EZB/ZDB-API
                        // 2 licensed ; 3 partially licensed
                    } else if (color == 2 || color == 3) {
                        online.setAmpel("yellow");
                        online.setComment("availresult.abonniert");
                        online.setState(2); // translate state to EZB/ZDB-API
                        // not licensed
                    } else if (color == 4) {
                        online.setAmpel("red");
                        online.setComment("availresult.not_licensed");
                        online.setState(4); // translate state to EZB/ZDB-API
                    } else {
                        online.setAmpel("red");
                        online.setComment("availresult.not_licensed");
                        online.setState(4); // translate state to EZB/ZDB-API
                    }

                    // LinkToArticle not always present
                    String url = getValue(result.getElementsByTagName("LinkToArticle"));
                    // LinkToJournal always present
                    if (url == null) {
                        url = getValue(result.getElementsByTagName("LinkToJournal"));
                    }
                    online.setUrl(url);

                    // try to get level from link
                    String levelLinkToArticle = null;
                    final NodeList levelNode = result.getElementsByTagName("LinkToArticle");
                    final Element levelElement = (Element) levelNode.item(0);
                    if (levelElement != null) {
                        levelLinkToArticle = levelElement.getAttribute("level");
                    }

                    if (levelLinkToArticle != null) {
                        online.setLevel(levelLinkToArticle); // specific level of each result
                    } else {
                        online.setLevel(levelAvailable); // overall level of best match
                    }

                    if (title != null) {
                        online.setTitle(title);
                    } else {
                        online.setTitle(url);
                    }
                    online.setReadme(getValue(result.getElementsByTagName("LinkToReadme")));

                    ezbform.getOnline().add(online);
                }

                // Title not found
                if (resultListE.getLength() == 0) {
                    final EZBDataOnline online = new EZBDataOnline();
                    online.setAmpel("red");
                    online.setComment("availresult.nohits");
                    online.setState(10); // translate state to EZB/ZDB-API

                    ezbform.getOnline().add(online);
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
