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

import org.grlea.log.SimpleLogger;

import util.Http;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.form.OrderForm;
import enums.Connect;

public class DOI {

    private static final SimpleLogger LOG = new SimpleLogger(DOI.class);

    /**
     * Extracts the DOI out of a string.
     */
    public String extractDoi(String doi) {

        if (doi != null && !doi.equals("")) {
            try {

                doi = doi.trim().toLowerCase();
                if (doi.contains("doi:")) {
                    doi = doi.substring(doi.indexOf("doi:") + 4);
                } // ggf. Text "DOI:" entfernen
                if (doi.contains("dx.doi.org/")) {
                    doi = doi.substring(doi.indexOf("dx.doi.org/") + 11);
                } // verschiedene Formen der Angaben entfernen ( dx.doi.org/... , http://dx.doi.org/...)
                if (doi.contains("doi/")) {
                    doi = doi.substring(doi.indexOf("doi/") + 4);
                } // ggf. Text "DOI/" entfernen

            } catch (final Exception e) {
                LOG.error("extractDoi: " + doi + "\040" + e.toString());
            }
        }

        return doi;
    }

    /**
     * Gets the meta data from a DOI.
     */
    public OrderForm resolveDoi(final String doi) {

        // http://generator.ocoins.info/ [Eingabe: 10.1002/hec.1381 ]

        OrderForm of = new OrderForm();
        final ConvertOpenUrl openurlConv = new ConvertOpenUrl();
        final OpenUrl openurl = new OpenUrl();
        final Http http = new Http();
        final String link = "http://generator.ocoins.info/?doi=" + doi;
        // String link = "http://generator.ocoins.info/crossref?handle=" + doi;
        String content = "";

        try {

            content = http.getContent(link, Connect.TIMEOUT_2.getValue(), Connect.RETRYS_2.getValue());

            content = content.replaceAll("&amp;amp;", "&amp;"); // falsche Doppelkodierung korrigieren...

            // Sicherstellen, dass die Anfrage aufgelöst wurde und vom OCoinS-Generator selber stammt (Ausschluss von
            // direkter Weiterleitung)
            if (!content.contains("DOI Resolution Error")
                    && content.contains("rfr_id=info%3Asid%2Focoins.info%3Agenerator")) {

                final ContextObject co = openurl.readOpenUrlFromString(content);
                of = openurlConv.makeOrderform(co);

            } else {
                // use CrossRef public resolver
                LOG.warn("Resolving DOI failed, using OCoinS-Generator and DOI: " + doi);
                of = resolveCrossRef(doi);
            }

        } catch (final Exception e) {
            LOG.error("resolveDoi: " + doi + "\040" + e.toString());
        }

        return of;
    }

    /**
     * Uses the CrossRef public resolver to resolve a DOI in the rare cases of a book.
     */
    private OrderForm resolveCrossRef(final String doi) {

        OrderForm of = null;
        final OpenUrl openurl = new OpenUrl();
        String content = "";

        final Http http = new Http();
        final String link = "http://www.crossref.org/guestquery?queryType=doi&restype=xsl_xml&doi=" + doi;

        content = http.getContent(link, Connect.TIMEOUT_2.getValue(), Connect.RETRYS_1.getValue());

        of = openurl.readXMLCrossRef(content);

        return of;
    }

}
