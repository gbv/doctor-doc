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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.grlea.log.SimpleLogger;

import util.Http;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.form.OrderForm;

public class Pubmed {

    private static final SimpleLogger LOG = new SimpleLogger(Pubmed.class);
    private static final int TIMEOUT = 2000;
    private static final int RETRYS = 2;

    /**
     * Extracts the PMID out of a string.
     */
    public String extractPmid(String pmid) {

        if (pmid != null && !pmid.equals("")) {
            try {
                final Matcher w = Pattern.compile("[0-9]+").matcher(pmid);
                if (w.find()) {
                    pmid = pmid.substring(w.start(), w.end());
                }
            } catch (final Exception e) {
                LOG.error("extractPmid: " + pmid + "\040" + e.toString());
            }
        }

        return pmid;
    }

    /**
     * Gets from a PMID all article details.
     */
    public OrderForm resolvePmid(final String pmid) {

        OrderForm of = new OrderForm();
        final ConvertOpenUrl openurlConv = new ConvertOpenUrl();
        final OpenUrl openurl = new OpenUrl();
        final Http http = new Http();
        final String link = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id="
                + pmid;
        String content = "";

        try {
            content = http.getWebcontent(link, TIMEOUT, RETRYS);
            final ContextObject co = openurl.readXmlPubmed(content);
            of = openurlConv.makeOrderform(co);
        } catch (final Exception e) {
            LOG.error("resolvePmid: " + pmid + "\040" + e.toString());
        }

        return of;
    }

    /**
     * Gets the PMID from the article details.
     *
     * @param OrderForm of
     * @return String pmid
     */
    public String getPmid(final OrderForm of) {

        String pmid = "";
        final Http http = new Http();

        try {

            final String content = http.getWebcontent(composePubmedlinkToPmid(of), TIMEOUT, RETRYS);

            if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
                pmid = content.substring(content.indexOf("<Id>") + 4, content.indexOf("</Id>"));
            }

        } catch (final Exception e) {
            LOG.error("getPmid(of): " + e.toString());
        }

        return pmid;
    }

    /**
     * Extracts the PMID from the web content.
     *
     * @param String content
     * @return String pmid
     */
    public String getPmid(final String content) {

        String pmid = "";

        try {

            if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
                pmid = content.substring(content.indexOf("<Id>") + 4, content.indexOf("</Id>"));
            }

        } catch (final Exception e) {
            LOG.error("getPmid(String): " + e.toString() + "\012" + content);
        }

        return pmid;
    }

    /**
     * Creates the search link to get the PMID from the article details.
     */
    public String composePubmedlinkToPmid(final OrderForm pageForm) {

        final ConvertOpenUrl openurlConv = new ConvertOpenUrl();

        final StringBuffer link = new StringBuffer(128);
        link.append("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=");
        link.append(pageForm.getIssn());
        link.append("[TA]");
        if (pageForm.getJahrgang() != null && !"".equals(pageForm.getJahrgang())) {
            link.append("+AND+");
            link.append(pageForm.getJahrgang());
            link.append("[VI]");
        }
        if (pageForm.getHeft() != null && !"".equals(pageForm.getHeft())) {
            link.append("+AND+");
            link.append(pageForm.getHeft());
            link.append("[IP]");
        }
        if (pageForm.getSeiten() != null && !"".equals(pageForm.getSeiten())) {
            link.append("+AND+");
            link.append(openurlConv.extractSpage(pageForm.getSeiten()));
            link.append("[PG]");
        }
        if (pageForm.getJahr() != null && !"".equals(pageForm.getJahr())) {
            link.append("+AND+");
            link.append(pageForm.getJahr());
            link.append("[DP]");
        }

        return link.toString();
    }

}