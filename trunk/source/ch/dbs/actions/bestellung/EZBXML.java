//  Copyright (C) 2012  Markus Fischer
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
import java.util.ArrayList;
import java.util.List;

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

import util.CodeUrl;
import util.Http;
import ch.dbs.form.JournalDetails;
import enums.Connect;

/**
 * This class reads answers from the normal EZB UI searched with the parameter
 * xmloutput=1 to get XML.
 */
public class EZBXML {

    private static final Logger LOG = LoggerFactory.getLogger(EZBXML.class);

    public List<JournalDetails> searchByTitle(final String jtitle, final String bibid) {

        final Http http = new Http();
        final CodeUrl coder = new CodeUrl();

        final StringBuffer link = new StringBuffer(
                "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?xmloutput=1&colors=7&lang=de&jq_type1=KT&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&offset=-1&hits_per_page=30&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=");
        link.append(bibid);
        link.append("&jq_term1=");
        link.append(coder.encode(jtitle, "ISO-8859-1"));

        String content = http.getContent(link.toString(), Connect.TIMEOUT_2.getValue(), Connect.TRIES_2.getValue(),
                null);

        // if we have > 30 hits, try a more concise search using: &jq_type1=KS (title starts with) instead of &jq_type1=KT (words in title)
        if (content != null && content.contains("<search_count>")) {

            final int x = Integer.parseInt(content.substring(content.indexOf("<search_count>") + 14,
                    content.indexOf("</search_count>")));
            if (x > 30) {
                final StringBuffer link2 = new StringBuffer(
                        "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?xmloutput=1&colors=7&lang=de&jq_type1=KS&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&offset=-1&hits_per_page=30&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=");
                link2.append(bibid);
                link2.append("&jq_term1=");
                link2.append(coder.encode(jtitle, "ISO-8859-1"));
                content = http.getContent(link2.toString(), Connect.TIMEOUT_2.getValue(), Connect.TRIES_2.getValue(),
                        null);
            }

        }

        final List<String> jourids = getJourids(content);

        return searchByJourids(jourids, bibid);

    }

    public List<JournalDetails> searchByIssn(final String issn, final String bibid) {

        final Http http = new Http();

        final StringBuffer link = new StringBuffer(
                "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?xmloutput=1&colors=5&lang=de&jq_type1=KT&jq_term1=&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&jq_bool4=AND&jq_not4=+&jq_type4=IS&offset=-1&hits_per_page=50&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=");
        link.append(bibid);
        link.append("&jq_term4=");
        link.append(issn);

        final String content = http.getContent(link.toString(), Connect.TIMEOUT_2.getValue(),
                Connect.TRIES_2.getValue(), null);

        final List<String> jourids = getJourids(content);

        return searchByJourids(jourids, bibid);
    }

    public List<JournalDetails> searchByJourids(final List<String> jourids, final String bibid) {

        final List<JournalDetails> list = new ArrayList<JournalDetails>();
        final Http http = new Http();

        final StringBuffer link = new StringBuffer(
                "http://rzblx1.uni-regensburg.de/ezeit/detail.phtml?xmloutput=1&colors=7&lang=de&bibid=");
        link.append(bibid);
        link.append("&jour_id=");

        final StringBuffer infoLink = new StringBuffer(
                "http://ezb.uni-regensburg.de/ezeit/detail.phtml?colors=7&lang=de&bibid=");
        infoLink.append(bibid);
        infoLink.append("&jour_id=");

        try {

            for (final String jourid : jourids) {

                final JournalDetails jd = new JournalDetails();

                final String content = http.getContent(link.toString() + jourid, Connect.TIMEOUT_1.getValue(),
                        Connect.TRIES_1.getValue(), null);

                if (content != null) {

                    final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                    domFactory.setNamespaceAware(true);
                    final DocumentBuilder builder = domFactory.newDocumentBuilder();
                    final Document doc = builder.parse(new InputSource(new StringReader(content)));

                    final XPathFactory factory = XPathFactory.newInstance();
                    final XPath xpath = factory.newXPath();

                    final XPathExpression exprJournal = xpath.compile("//journal");
                    final XPathExpression exprPissns = xpath.compile("//journal/detail/P_ISSNs");
                    final XPathExpression exprEissns = xpath.compile("//journal/detail/E_ISSNs");
                    final NodeList resultJournal = (NodeList) exprJournal.evaluate(doc, XPathConstants.NODESET);

                    for (int i = 0; i < resultJournal.getLength(); i++) {
                        final Node firstResultNode = resultJournal.item(i);
                        final Element journal = (Element) firstResultNode;

                        // Title
                        String title = getValue(journal.getElementsByTagName("title"));
                        if (title != null) {
                            title = Jsoup.clean(title, Whitelist.none());
                            title = Jsoup.parse(title).text();
                        }

                        jd.setZeitschriftentitel(title);

                        // P-ISSNs
                        final NodeList resultPissns = (NodeList) exprPissns.evaluate(doc, XPathConstants.NODESET);

                        // get first pissn
                        for (int z = 0; z < resultPissns.getLength(); z++) {
                            final Node firstPissnsNode = resultPissns.item(i);
                            final Element pissnElement = (Element) firstPissnsNode;
                            final String pissn = getValue(pissnElement.getElementsByTagName("P_ISSN"));
                            jd.setIssn(pissn);
                        }

                        // try to get Eissn if we have no Pissn
                        if (jd.getIssn() == null) {

                            // E-ISSNs
                            final NodeList resultEissns = (NodeList) exprEissns.evaluate(doc, XPathConstants.NODESET);

                            // get first eissn
                            for (int z = 0; z < resultEissns.getLength(); z++) {
                                final Node firstEissnsNode = resultEissns.item(i);
                                final Element eissnElement = (Element) firstEissnsNode;
                                final String eissn = getValue(eissnElement.getElementsByTagName("E_ISSN"));
                                jd.setIssn(eissn);
                            }
                        }

                        // add info link
                        jd.setLink(infoLink.toString() + jourid);

                        list.add(jd);
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

        return list;
    }

    private List<String> getJourids(final String content) {

        final List<String> result = new ArrayList<String>();

        try {

            if (content != null) {

                final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setNamespaceAware(true);
                final DocumentBuilder builder = domFactory.newDocumentBuilder();
                final Document doc = builder.parse(new InputSource(new StringReader(content)));

                final XPathFactory factory = XPathFactory.newInstance();
                final XPath xpath = factory.newXPath();

                final XPathExpression exprJournals = xpath.compile("//journals/journal");
                final NodeList journals = (NodeList) exprJournals.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < journals.getLength(); i++) {
                    final Node firstResultNode = journals.item(i);
                    final Element journal = (Element) firstResultNode;

                    final String id = journal.getAttribute("jourid");

                    if (id != null) {
                        result.add(id);
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

        return result;
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
