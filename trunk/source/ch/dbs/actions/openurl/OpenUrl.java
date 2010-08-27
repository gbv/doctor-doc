//  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
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

package ch.dbs.actions.openurl;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.grlea.log.SimpleLogger;

import util.CodeUrl;
import util.Decoder;
import util.SpecialCharacters;

public class OpenUrl {

    private static final SimpleLogger LOG = new SimpleLogger(OpenUrl.class);
    private static final String PM_ENDTAG = "</Item>";

    /**
     * Klasse um OpenURL anzuwenden
     * @author Markus Fischer
     */

    /**
     * identifiziert die OpenURL-Identifiers aus einem Request
     * verwendet die Standardmethoden von Java
     */
    public ContextObject readOpenUrlFromRequest(final HttpServletRequest rq) {

        final ContextObject co = new ContextObject();
        ConcurrentHashMap<String, String> params = new ConcurrentHashMap<String, String>();
        params = getOpenUrlParameters(rq);

        final ArrayList<String> uriSchemas = new ArrayList<String>();

        for (final Map.Entry<String, String> pairs : params.entrySet()) {

            final String key = pairs.getKey();
            final String value = pairs.getValue();

            try {

                // Parameter aus OpenURL Version 1.0
                if ("rft_val_fmt".equals(key)) {
                    co.setRft_val_fmt(value);
                    continue;
                }
                if ("rfr_id".equals(key)) {
                    co.setRfr_id(value);
                    continue;
                }
                if ("rft.genre".equals(key)) {
                    co.setRft_genre(value);
                    continue;
                }
                if ("rft.atitle".equals(key)) {
                    co.setRft_atitle(value);
                    continue;
                }
                if ("rft.btitle".equals(key)) {
                    co.setRft_btitle(value);
                    continue;
                }
                if ("rft.series".equals(key)) {
                    co.setRft_series(value);
                    continue;
                }
                if ("rft.pub".equals(key)) {
                    co.setRft_pub(value);
                    continue;
                }
                if ("rft.place".equals(key)) {
                    co.setRft_place(value);
                    continue;
                }
                if ("rft.edition".equals(key)) {
                    co.setRft_edition(value);
                    continue;
                }
                if ("rft.title".equals(key)) {
                    co.setRft_title(value);
                    continue;
                }
                if ("rft.jtitle".equals(key)) {
                    co.setRft_jtitle(value);
                    continue;
                }
                if ("rft.stitle".equals(key)) {
                    co.setRft_stitle(value);
                    continue;
                }
                if ("rft.issn".equals(key)) {
                    co.setRft_issn(value);
                    continue;
                }
                if ("rft.issn1".equals(key)) {
                    co.setRft_issn1(value);
                    continue;
                }
                if ("rft.eissn".equals(key)) {
                    co.setRft_eissn(value);
                    continue;
                }
                if ("rft.eissn1".equals(key)) {
                    co.setRft_eissn1(value);
                    continue;
                }
                if ("rft.isbn".equals(key)) {
                    co.setRft_isbn(value);
                    continue;
                }
                if ("rft.date".equals(key)) {
                    co.setRft_date(value);
                    continue;
                }
                if ("rft.volume".equals(key)) {
                    co.setRft_volume(value);
                    continue;
                }
                if ("rft.part".equals(key)) {
                    co.setRft_part(value);
                    continue;
                }
                if ("rft.issue".equals(key)) {
                    co.setRft_issue(value);
                    continue;
                }
                if ("rft.spage".equals(key)) {
                    co.setRft_spage(value);
                    continue;
                }
                if ("rft.epage".equals(key)) {
                    co.setRft_epage(value);
                    continue;
                }
                if ("rft.pages".equals(key)) {
                    co.setRft_pages(value);
                    continue;
                }
                if ("rft.tpages".equals(key)) {
                    co.setRft_tpages(value);
                    continue;
                }
                if ("rft.author".equals(key)) {
                    co.setRft_author(value);
                    continue;
                }
                if ("rft.au".equals(key)) {
                    co.setRft_au(value);
                    continue;
                }
                if ("rft.aucorp".equals(key)) {
                    co.setRft_aucorp(value);
                    continue;
                }
                if ("rft.auinit".equals(key)) {
                    co.setRft_auinit(value);
                    continue;
                }
                if ("rft.auinit1".equals(key)) {
                    co.setRft_auinit1(value);
                    continue;
                }
                if ("rft.auinitm".equals(key)) {
                    co.setRft_auinitm(value);
                    continue;
                }
                if ("rft.ausuffix".equals(key)) {
                    co.setRft_ausuffix(value);
                    continue;
                }
                if ("rft.aulast".equals(key)) {
                    co.setRft_aulast(value);
                    continue;
                }
                if ("rft.aufirst".equals(key)) {
                    co.setRft_aufirst(value);
                    continue;
                }
                if ("rft.sici".equals(key)) {
                    co.setRft_sici(value);
                    continue;
                }
                if ("rft.bici".equals(key)) {
                    co.setRft_bici(value);
                    continue;
                }
                if ("rft.coden".equals(key)) {
                    co.setRft_coden(value);
                    continue;
                }
                if ("rft.artnum".equals(key)) {
                    co.setRft_artnum(value);
                    continue;
                }
                // RFTs Dublin Core, z.B. in Blogs:
                if ("rft.creator".equals(key)) {
                    co.setRft_creator(value);
                    continue;
                }
                if ("rft.publisher".equals(key)) {
                    co.setRft_publisher(value);
                    continue;
                }
                if ("rft.type".equals(key)) {
                    co.setRft_type(value);
                    continue;
                }
                if ("rft.subject".equals(key)) {
                    co.setRft_subject(value);
                    continue;
                }
                if ("rft.format".equals(key)) {
                    co.setRft_format(value);
                    continue;
                }
                if ("rft.language".equals(key)) {
                    co.setRft_language(value);
                    continue;
                }
                if ("rft.source".equals(key)) {
                    co.setRft_source(value);
                    continue;
                }
                if ("rft.identifier".equals(key)) {
                    co.setRft_identifier(value);
                    continue;
                }
                if ("rft.description".equals(key)) {
                    co.setRft_description(value);
                    continue;
                }
                if ("rft.relation".equals(key)) {
                    co.setRft_relation(value);
                    continue;
                }
                if ("rft.coverage".equals(key)) {
                    co.setRft_coverage(value);
                    continue;
                }
                if ("rft.rights".equals(key)) {
                    co.setRft_rights(value);
                    continue;
                }

                //         Parameter aus OpenURL 0.1.
                if ("id".equals(key) && !value.startsWith("doi:")) { // kommt bei BIOONE und Inforama vor: id=doi:
                    co.setRfr_id(value);
                    continue;
                }
                if ("sid".equals(key)) {
                    co.setRfr_id(value); // überschreibt &id=
                    continue;
                }
                if ("genre".equals(key)) {
                    co.setRft_genre(value);
                    continue;
                }
                if ("atitle".equals(key)) {
                    co.setRft_atitle(value);
                    continue;
                }
                if ("btitle".equals(key)) {
                    co.setRft_btitle(value);
                    continue;
                }
                if ("series".equals(key)) {
                    co.setRft_series(value);
                    continue;
                }
                if ("pub".equals(key)) {
                    co.setRft_pub(value);
                    continue;
                }
                if ("place".equals(key)) {
                    co.setRft_place(value);
                    continue;
                }
                if ("edition".equals(key)) {
                    co.setRft_edition(value);
                    continue;
                }
                if ("title".equals(key)) {
                    co.setRft_title(value);
                    continue;
                }
                if ("jtitle".equals(key)) {
                    co.setRft_jtitle(value);
                    continue;
                }
                if ("stitle".equals(key)) {
                    co.setRft_stitle(value);
                    continue;
                }
                if ("issn".equals(key)) {
                    co.setRft_issn(value);
                    continue;
                }
                if ("issn1".equals(key)) {
                    co.setRft_issn1(value); // kann es manchmal geben, als ISSN mit Bindestrich
                    continue;
                }
                if ("eissn".equals(key)) {
                    co.setRft_eissn(value);
                    continue;
                }
                if ("eissn1".equals(key)) {
                    co.setRft_eissn1(value);
                    continue;
                }
                if ("isbn".equals(key)) {
                    co.setRft_isbn(value);
                    continue;
                }
                if ("date".equals(key)) {
                    co.setRft_date(value);
                    continue;
                }
                if ("volume".equals(key)) {
                    co.setRft_volume(value);
                    continue;
                }
                if ("part".equals(key)) {
                    co.setRft_part(value);
                    continue;
                }
                if ("issue".equals(key)) {
                    co.setRft_issue(value);
                    continue;
                }
                if ("spage".equals(key)) {
                    co.setRft_spage(value);
                    continue;
                }
                if ("epage".equals(key)) {
                    co.setRft_epage(value);
                    continue;
                }
                if ("pages".equals(key)) {
                    co.setRft_pages(value);
                    continue;
                }
                if ("tpages".equals(key)) {
                    co.setRft_tpages(value);
                    continue;
                }
                if ("author".equals(key)) {
                    co.setRft_author(value);
                    continue;
                }
                if ("au".equals(key)) {
                    co.setRft_au(value);
                    continue;
                }
                if ("aucorp".equals(key)) {
                    co.setRft_aucorp(value);
                    continue;
                }
                if ("auinit".equals(key)) {
                    co.setRft_auinit(value);
                    continue;
                }
                if ("auinit1".equals(key)) {
                    co.setRft_auinit1(value);
                    continue;
                }
                if ("auinitm".equals(key)) {
                    co.setRft_auinitm(value);
                    continue;
                }
                if ("ausuffix".equals(key)) {
                    co.setRft_ausuffix(value);
                    continue;
                }
                if ("aulast".equals(key)) {
                    co.setRft_aulast(value);
                    continue;
                }
                if ("aufirst".equals(key)) {
                    co.setRft_aufirst(value);
                    continue;
                }
                if ("sici".equals(key)) {
                    co.setRft_sici(value); // doppelt?
                    continue;
                }
                if ("bici".equals(key)) {
                    co.setRft_bici(value);
                    continue;
                }
                if ("coden".equals(key)) {
                    co.setRft_coden(value);
                    continue;
                }
                if ("artnum".equals(key)) {
                    co.setRft_artnum(value);
                    continue;
                }
                // RFTs Dublin Core, z.B. in Blogs:
                if ("creator".equals(key)) {
                    co.setRft_creator(value);
                    continue;
                }
                if ("publisher".equals(key)) {
                    co.setRft_publisher(value);
                    continue;
                }
                if ("type".equals(key)) {
                    co.setRft_type(value);
                    continue;
                }
                if ("subject".equals(key)) {
                    co.setRft_subject(value);
                    continue;
                }
                if ("format".equals(key)) {
                    co.setRft_format(value);
                    continue;
                }
                if ("language".equals(key)) {
                    co.setRft_language(value);
                    continue;
                }
                if ("source".equals(key)) {
                    co.setRft_source(value);
                    continue;
                }
                if ("identifier".equals(key)) {
                    co.setRft_identifier(value);
                    continue;
                }
                if ("description".equals(key)) {
                    co.setRft_description(value);
                    continue;
                }
                if ("relation".equals(key)) {
                    co.setRft_relation(value);
                    continue;
                }
                if ("coverage".equals(key)) {
                    co.setRft_coverage(value);
                    continue;
                }
                if ("rights".equals(key)) {
                    co.setRft_rights(value);
                    continue;
                }


                // hier muss aus Kompatibilitätsgründen das jeweilige URI-Schema hinzugefügt werden
                // http://www.info-uri.info/registry/
                if ("rft_id".equals(key)) {
                    uriSchemas.add(value);
                    continue;
                }
                if ("pmid".equals(key)) {
                    if (value.length() > 0) { uriSchemas.add("info:pmid/" + value); }
                    continue;
                }
                if ("id".equals(key) && value.startsWith("doi:")) { // kommt bei BIOONE und Inforama vor
                    if (value.length() > 9) {
                        uriSchemas.add("info:doi/" + value.substring(value.indexOf("doi:") + 4));
                    }
                    continue;
                }
                if ("doi".equals(key)) {
                    if (value.length() > 9) { uriSchemas.add("info:doi/" + value); }
                    continue;
                }
                if ("sici".equals(key)) {
                    if (value.length() > 10) { uriSchemas.add("info:sici/" + value); }
                    continue;
                }
                if ("lccn".equals(key)) {
                    if (value.length() > 0) { uriSchemas.add("info:lccn/" + value); }
                    continue;
                }

            } catch (final Exception e) {
                LOG.error("readOpenUrlFromRequest: " + key + "\040value:\040" + value + "\040" + e.toString());
            }

        }

        if (!uriSchemas.isEmpty()) { co.setRft_id(uriSchemas); }

        return co;
    }



    /**
     * identifiziert die OpenURL-Identifiers aus einer Webseite (z.B. OCoins / Worlcat etc.)
     * verwendet selbstgefertigte Stringanalysen
     *
     */
    public ContextObject readOpenUrlFromString(final String content) {

        final ContextObject co = new ContextObject();

        try {

            String openURL = content;

            final SpecialCharacters specialCharacters = new SpecialCharacters();
            openURL = specialCharacters.replace(openURL); // Entfernen von &amp; und co.

            // Achtung: OpenURL nicht vor if (OpenURL...contains(">") decodieren,
            // sonst interferiert rtf.sici (enthält <...>)!
            // Grosschreibung
            if (openURL.contains("ver=Z39.88-2004")
                    // falls content von einer Webseite stammt...
                    && openURL.substring(openURL.indexOf("ver=Z39.88-2004")).contains(">")) {
                openURL = openURL.substring(openURL.indexOf("ver=Z39.88-2004"), openURL.indexOf('>', openURL
                        .indexOf("ver=Z39.88-2004"))); // ...String mit OpenUrl suchen und verkürzen
            }
            // Kleinschreibung
            if (openURL.contains("ver=z39.88-2004")
                    // falls content von einer Webseite stammt...
                    && openURL.substring(openURL.indexOf("ver=z39.88-2004")).contains(">")) {
                openURL = openURL.substring(openURL.indexOf("ver=z39.88-2004"), openURL.indexOf('>', openURL
                        .indexOf("ver=z39.88-2004"))); // ...String mit OpenUrl suchen und verkürzen
            }

            // OpenURL Version 1.0 (neben rft.genre etc. gibt es dasselbe nochmals mit rfr.genre)
            if (openURL.contains("&rft.")) {

                if (openURL.contains("rft_val_fmt=")) {
                    co.setRft_val_fmt(getOpenUrlIdentifiersVersion1_0("rft_val_fmt=", openURL));
                }
                if (openURL.contains("rfr_id=")) {
                    co.setRfr_id(getOpenUrlIdentifiersVersion1_0("rfr_id=", openURL));
                }
                if (openURL.contains("rft.genre=")) {
                    co.setRft_genre(getOpenUrlIdentifiersVersion1_0("rft.genre=", openURL));
                }
                if (openURL.contains("rft.atitle=")) {
                    co.setRft_atitle(getOpenUrlIdentifiersVersion1_0("rft.atitle=", openURL));
                }
                if (openURL.contains("rft.btitle=")) {
                    co.setRft_btitle(getOpenUrlIdentifiersVersion1_0("rft.btitle=", openURL));
                }
                if (openURL.contains("rft.series=")) {
                    co.setRft_series(getOpenUrlIdentifiersVersion1_0("rft.series=", openURL));
                }
                if (openURL.contains("rft.pub=")) {
                    co.setRft_pub(getOpenUrlIdentifiersVersion1_0("rft.pub=", openURL));
                }
                if (openURL.contains("rft.place=")) {
                    co.setRft_place(getOpenUrlIdentifiersVersion1_0("rft.place=", openURL));
                }
                if (openURL.contains("rft.edition=")) {
                    co.setRft_edition(getOpenUrlIdentifiersVersion1_0("rft.edition=", openURL));
                }
                if (openURL.contains("rft.title=")) {
                    co.setRft_title(getOpenUrlIdentifiersVersion1_0("rft.title=", openURL));
                }
                if (openURL.contains("rft.jtitle=")) {
                    co.setRft_jtitle(getOpenUrlIdentifiersVersion1_0("rft.jtitle=", openURL));
                }
                if (openURL.contains("rft.stitle=")) {
                    co.setRft_stitle(getOpenUrlIdentifiersVersion1_0("rft.stitle=", openURL));
                }
                if (openURL.contains("rft.issn=")) {
                    co.setRft_issn(getOpenUrlIdentifiersVersion1_0("rft.issn=", openURL));
                }
                if (openURL.contains("rft.issn1=")) {
                    co.setRft_issn1(getOpenUrlIdentifiersVersion1_0("rft.issn1=", openURL));
                }
                if (openURL.contains("rft.eissn=")) {
                    co.setRft_eissn(getOpenUrlIdentifiersVersion1_0("rft.eissn=", openURL));
                }
                if (openURL.contains("rft.eissn1=")) {
                    co.setRft_eissn1(getOpenUrlIdentifiersVersion1_0("rft.eissn1=", openURL));
                }
                if (openURL.contains("rft.isbn=")) {
                    co.setRft_isbn(getOpenUrlIdentifiersVersion1_0("rft.isbn=", openURL));
                }
                if (openURL.contains("rft.date=")) {
                    co.setRft_date(getOpenUrlIdentifiersVersion1_0("rft.date=", openURL));
                }
                if (openURL.contains("rft.volume=")) {
                    co.setRft_volume(getOpenUrlIdentifiersVersion1_0("rft.volume=", openURL));
                }
                if (openURL.contains("rft.part=")) {
                    co.setRft_part(getOpenUrlIdentifiersVersion1_0("rft.part=", openURL));
                }
                if (openURL.contains("rft.issue=")) {
                    co.setRft_issue(getOpenUrlIdentifiersVersion1_0("rft.issue=", openURL));
                }
                if (openURL.contains("rft.spage=")) {
                    co.setRft_spage(getOpenUrlIdentifiersVersion1_0("rft.spage=", openURL));
                }
                if (openURL.contains("rft.epage=")) {
                    co.setRft_epage(getOpenUrlIdentifiersVersion1_0("rft.epage=", openURL));
                }
                if (openURL.contains("rft.pages=")) {
                    co.setRft_pages(getOpenUrlIdentifiersVersion1_0("rft.pages=", openURL));
                }
                if (openURL.contains("rft.tpages=")) {
                    co.setRft_tpages(getOpenUrlIdentifiersVersion1_0("rft.tpages=", openURL));
                }
                if (openURL.contains("rft.author=")) {
                    co.setRft_author(getOpenUrlIdentifiersVersion1_0("rft.author=", openURL));
                }
                if (openURL.contains("rft.au=")) {
                    co.setRft_au(getOpenUrlIdentifiersVersion1_0("rft.au=", openURL));
                }
                if (openURL.contains("rft.aucorp=")) {
                    co.setRft_aucorp(getOpenUrlIdentifiersVersion1_0("rft.aucorp=", openURL));
                }
                if (openURL.contains("rft.auinit=")) {
                    co.setRft_auinit(getOpenUrlIdentifiersVersion1_0("rft.auinit=", openURL));
                }
                if (openURL.contains("rft.auinit1=")) {
                    co.setRft_auinit1(getOpenUrlIdentifiersVersion1_0("rft.auinit1=", openURL));
                }
                if (openURL.contains("rft.auinitm=")) {
                    co.setRft_auinitm(getOpenUrlIdentifiersVersion1_0("rft.auinitm=", openURL));
                }
                if (openURL.contains("rft.ausuffix=")) {
                    co.setRft_ausuffix(getOpenUrlIdentifiersVersion1_0("rft.ausuffix=", openURL));
                }
                if (openURL.contains("rft.aulast=")) {
                    co.setRft_aulast(getOpenUrlIdentifiersVersion1_0("rft.aulast=", openURL));
                }
                if (openURL.contains("rft.aufirst=")) {
                    co.setRft_aufirst(getOpenUrlIdentifiersVersion1_0("rft.aufirst=", openURL));
                }
                if (openURL.contains("rft.sici=")) {
                    co.setRft_sici(getOpenUrlIdentifiersVersion1_0("rft.sici=", openURL));
                }
                if (openURL.contains("rft.bici=")) {
                    co.setRft_bici(getOpenUrlIdentifiersVersion1_0("rft.bici=", openURL));
                }
                if (openURL.contains("rft.coden=")) {
                    co.setRft_coden(getOpenUrlIdentifiersVersion1_0("rft.coden=", openURL));
                }
                if (openURL.contains("rft.artnum=")) {
                    co.setRft_artnum(getOpenUrlIdentifiersVersion1_0("rft.artnum=", openURL));
                }
                // RFTs Dublin Core, z.B. in Blogs:
                if (openURL.contains("rft.creator=")) {
                    co.setRft_creator(getOpenUrlIdentifiersVersion1_0("rft.creator=", openURL));
                }
                if (openURL.contains("rft.publisher=")) {
                    co.setRft_publisher(getOpenUrlIdentifiersVersion1_0("rft.publisher=", openURL));
                }
                if (openURL.contains("rft.type=")) {
                    co.setRft_type(getOpenUrlIdentifiersVersion1_0("rft.type=", openURL));
                }
                if (openURL.contains("rft.subject=")) {
                    co.setRft_subject(getOpenUrlIdentifiersVersion1_0("rft.subject=", openURL));
                }
                if (openURL.contains("rft.format=")) {
                    co.setRft_format(getOpenUrlIdentifiersVersion1_0("rft.format=", openURL));
                }
                if (openURL.contains("rft.language=")) {
                    co.setRft_language(getOpenUrlIdentifiersVersion1_0("rft.language=", openURL));
                }
                if (openURL.contains("rft.source=")) {
                    co.setRft_source(getOpenUrlIdentifiersVersion1_0("rft.source=", openURL));
                }
                if (openURL.contains("rft.identifier=")) {
                    co.setRft_identifier(getOpenUrlIdentifiersVersion1_0("rft.identifier=", openURL));
                }
                if (openURL.contains("rft.description=")) {
                    co.setRft_description(getOpenUrlIdentifiersVersion1_0("rft.description=", openURL));
                }
                if (openURL.contains("rft.relation=")) {
                    co.setRft_relation(getOpenUrlIdentifiersVersion1_0("rft.relation=", openURL));
                }
                if (openURL.contains("rft.coverage=")) {
                    co.setRft_coverage(getOpenUrlIdentifiersVersion1_0("rft.coverage=", openURL));
                }
                if (openURL.contains("rft.rights=")) {
                    co.setRft_rights(getOpenUrlIdentifiersVersion1_0("rft.rights=", openURL));
                }

                final ArrayList<String> id = new ArrayList<String>();

                while (openURL.contains("rft_id=")) { // kann mehrere rft_id enthalten
                    id.add(getOpenUrlIdentifiersVersion1_0("rft_id=", openURL));
                    openURL = openURL.substring(openURL.indexOf("rft_id=") + 6);
                }

                if (!id.isEmpty()) { co.setRft_id(id); }

            } else {

                //         OpenURL Version 0.1
                // kommt nicht vor in OpenURL != Version 1.0
                if (openURL.contains("rft_val_fmt=")) {
                    co.setRft_val_fmt(getOpenUrlIdentifiersVersion0_1("rft_val_fmt=", openURL));
                }
                // mit &! sonst nicht eindeutig! etwas fragwürdig, ob das zuverlässig ist
                if (openURL.contains("&id=")) {
                    co.setRfr_id(getOpenUrlIdentifiersVersion0_1("&id=", openURL));
                }
                // überschreibt &id=
                if (openURL.contains("sid=")) {
                    co.setRfr_id(getOpenUrlIdentifiersVersion0_1("sid=", openURL));
                }
                if (openURL.contains("genre=")) {
                    co.setRft_genre(getOpenUrlIdentifiersVersion0_1("genre=", openURL));
                }
                if (openURL.contains("atitle=")) {
                    co.setRft_atitle(getOpenUrlIdentifiersVersion0_1("atitle=", openURL));
                }
                if (openURL.contains("btitle=")) {
                    co.setRft_btitle(getOpenUrlIdentifiersVersion0_1("btitle=", openURL));
                }
                if (openURL.contains("series=")) {
                    co.setRft_series(getOpenUrlIdentifiersVersion0_1("series=", openURL));
                }
                if (openURL.contains("pub=")) {
                    co.setRft_pub(getOpenUrlIdentifiersVersion0_1("pub=", openURL));
                }
                if (openURL.contains("place=")) {
                    co.setRft_place(getOpenUrlIdentifiersVersion0_1("place=", openURL));
                }
                if (openURL.contains("edition=")) {
                    co.setRft_edition(getOpenUrlIdentifiersVersion0_1("edition=", openURL));
                }
                // mit &! sonst nicht eindeutig! etwas fragwürdig, ob das zuverlässig ist
                if (openURL.contains("&title=")) {
                    co.setRft_title(getOpenUrlIdentifiersVersion0_1("&title=", openURL));
                }
                if (openURL.contains("jtitle=")) {
                    co.setRft_jtitle(getOpenUrlIdentifiersVersion0_1("jtitle=", openURL));
                }
                if (openURL.contains("stitle=")) {
                    co.setRft_stitle(getOpenUrlIdentifiersVersion0_1("stitle=", openURL));
                }
                if (openURL.contains("issn=")) {
                    co.setRft_issn(getOpenUrlIdentifiersVersion0_1("issn=", openURL));
                }
                // kann es manchmal geben, als ISSN mit Bindestrich
                if (openURL.contains("issn1=")) {
                    co.setRft_issn1(getOpenUrlIdentifiersVersion0_1("issn1=", openURL));
                }
                if (openURL.contains("eissn=")) {
                    co.setRft_eissn(getOpenUrlIdentifiersVersion0_1("eissn=", openURL));
                }
                if (openURL.contains("eissn1=")) {
                    co.setRft_eissn1(getOpenUrlIdentifiersVersion0_1("eissn1=", openURL));
                }
                if (openURL.contains("isbn=")) {
                    co.setRft_isbn(getOpenUrlIdentifiersVersion0_1("isbn=", openURL));
                }
                if (openURL.contains("date=")) {
                    co.setRft_date(getOpenUrlIdentifiersVersion0_1("date=", openURL));
                }
                if (openURL.contains("volume=")) {
                    co.setRft_volume(getOpenUrlIdentifiersVersion0_1("volume=", openURL));
                }
                if (openURL.contains("part=")) {
                    co.setRft_part(getOpenUrlIdentifiersVersion0_1("part=", openURL));
                }
                if (openURL.contains("issue=")) {
                    co.setRft_issue(getOpenUrlIdentifiersVersion0_1("issue=", openURL));
                }
                if (openURL.contains("spage=")) {
                    co.setRft_spage(getOpenUrlIdentifiersVersion0_1("spage=", openURL));
                }
                if (openURL.contains("epage=")) {
                    co.setRft_epage(getOpenUrlIdentifiersVersion0_1("epage=", openURL));
                }
                if (openURL.contains("pages=")) {
                    co.setRft_pages(getOpenUrlIdentifiersVersion0_1("pages=", openURL));
                }
                if (openURL.contains("tpages=")) {
                    co.setRft_tpages(getOpenUrlIdentifiersVersion0_1("tpages=", openURL));
                }
                if (openURL.contains("author=")) {
                    co.setRft_author(getOpenUrlIdentifiersVersion0_1("author=", openURL));
                }
                // mit &, sonst nicht eindeutig!
                if (openURL.contains("&au=")) {
                    co.setRft_au(getOpenUrlIdentifiersVersion0_1("&au=", openURL));
                }
                if (openURL.contains("aucorp=")) {
                    co.setRft_aucorp(getOpenUrlIdentifiersVersion0_1("aucorp=", openURL));
                }
                if (openURL.contains("auinit=")) {
                    co.setRft_auinit(getOpenUrlIdentifiersVersion0_1("auinit=", openURL));
                }
                if (openURL.contains("auinit1=")) {
                    co.setRft_auinit1(getOpenUrlIdentifiersVersion0_1("auinit1=", openURL));
                }
                if (openURL.contains("auinitm=")) {
                    co.setRft_auinitm(getOpenUrlIdentifiersVersion0_1("auinitm=", openURL));
                }
                if (openURL.contains("ausuffix=")) {
                    co.setRft_ausuffix(getOpenUrlIdentifiersVersion0_1("ausuffix=", openURL));
                }
                if (openURL.contains("aulast=")) {
                    co.setRft_aulast(getOpenUrlIdentifiersVersion0_1("aulast=", openURL));
                }
                if (openURL.contains("aufirst=")) {
                    co.setRft_aufirst(getOpenUrlIdentifiersVersion0_1("aufirst=", openURL));
                }
                // doppelt?
                if (openURL.contains("sici=")) {
                    co.setRft_sici(getOpenUrlIdentifiersVersion0_1("sici=", openURL));
                }
                if (openURL.contains("bici=")) {
                    co.setRft_bici(getOpenUrlIdentifiersVersion0_1("bici=", openURL));
                }
                if (openURL.contains("coden=")) {
                    co.setRft_coden(getOpenUrlIdentifiersVersion0_1("coden=", openURL));
                }
                if (openURL.contains("artnum=")) {
                    co.setRft_artnum(getOpenUrlIdentifiersVersion0_1("artnum=", openURL));
                }
                // RFTs Dublin Core, z.B. in Blogs:
                if (openURL.contains("creator=")) {
                    co.setRft_creator(getOpenUrlIdentifiersVersion0_1("creator=", openURL));
                }
                if (openURL.contains("publisher=")) {
                    co.setRft_publisher(getOpenUrlIdentifiersVersion0_1("publisher=", openURL));
                }
                if (openURL.contains("type=")) {
                    co.setRft_type(getOpenUrlIdentifiersVersion0_1("type=", openURL));
                }
                if (openURL.contains("subject=")) {
                    co.setRft_subject(getOpenUrlIdentifiersVersion0_1("subject=", openURL));
                }
                if (openURL.contains("format=")) {
                    co.setRft_format(getOpenUrlIdentifiersVersion0_1("format=", openURL));
                }
                if (openURL.contains("language=")) {
                    co.setRft_language(getOpenUrlIdentifiersVersion0_1("language=", openURL));
                }
                if (openURL.contains("source=")) {
                    co.setRft_source(getOpenUrlIdentifiersVersion0_1("source=", openURL));
                }
                if (openURL.contains("identifier=")) {
                    co.setRft_identifier(getOpenUrlIdentifiersVersion0_1("identifier=", openURL));
                }
                if (openURL.contains("description=")) {
                    co.setRft_description(getOpenUrlIdentifiersVersion0_1("description=", openURL));
                }
                if (openURL.contains("relation=")) {
                    co.setRft_relation(getOpenUrlIdentifiersVersion0_1("relation=", openURL));
                }
                if (openURL.contains("coverage=")) {
                    co.setRft_coverage(getOpenUrlIdentifiersVersion0_1("coverage=", openURL));
                }
                if (openURL.contains("rights=")) {
                    co.setRft_rights(getOpenUrlIdentifiersVersion0_1("rights=", openURL));
                }

                final ArrayList<String> id = new ArrayList<String>();
                // hier muss aus Kompatibilitätsgründen das jeweilige URI-Schema hinzugefügt werden
                // http://www.info-uri.info/registry/
                if (openURL.contains("pmid:")) {
                    final String reg = "info:pmid/" + getOpenUrlIdentifiersVersion0_1("pmid:", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }
                if (openURL.contains("doi:")) {
                    final String reg = "info:doi/" + getOpenUrlIdentifiersVersion0_1("doi:", openURL);
                    if (reg.length() > 9) { id.add(reg); }
                }
                if (openURL.contains("sici:")) {
                    final String reg = "info:sici/" + getOpenUrlIdentifiersVersion0_1("sici:", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }
                if (openURL.contains("lccn:")) {
                    final String reg = "info:lccn/" + getOpenUrlIdentifiersVersion0_1("lccn:", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }
                if (openURL.contains("pmid=")) {
                    final String reg = "info:pmid/" + getOpenUrlIdentifiersVersion0_1("pmid=", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }
                if (openURL.contains("doi=")) {
                    final String reg = "info:doi/" + getOpenUrlIdentifiersVersion0_1("doi=", openURL);
                    if (reg.length() > 9) { id.add(reg); }
                }
                if (openURL.contains("sici=")) {
                    final String reg = "info:sici/" + getOpenUrlIdentifiersVersion0_1("sici=", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }
                if (openURL.contains("lccn=")) {
                    final String reg = "info:lccn/" + getOpenUrlIdentifiersVersion0_1("lccn=", openURL);
                    if (reg.length() > 10) { id.add(reg); }
                }

                if (!id.isEmpty()) { co.setRft_id(id); }


            }


        } catch (final Exception e) {
            LOG.error("readOpenUrl: " + "\012" + content);
        }

        return co;
    }

    /**
     * Stellt aus einem ContextObject den entsprechenden OpenURL-String zusammen
     */
    public String composeOpenUrl(final ContextObject co) {

        final StringBuffer openURL = new StringBuffer(800);

        // hier werden die Identifiers gesetzt
        if (co.getRft_val_fmt() != null && !co.getRft_val_fmt().equals("")) {
            openURL.append("&rft_val_fmt=");
            openURL.append(co.getRft_val_fmt());
        }
        if (co.getRfr_id() != null && !co.getRfr_id().equals("")) {
            openURL.append("&rfr_id=");
            openURL.append(co.getRfr_id());
        }
        if (co.getRft_genre() != null && !co.getRft_genre().equals("")) {
            openURL.append("&rft.genre=");
            openURL.append(co.getRft_genre());
        }
        if (co.getRft_atitle() != null && !co.getRft_atitle().equals("")) {
            openURL.append("&rft.atitle=");
            openURL.append(co.getRft_atitle());
        }
        if (co.getRft_btitle() != null && !co.getRft_btitle().equals("")) {
            openURL.append("&rft.btitle=");
            openURL.append(co.getRft_btitle());
        }
        if (co.getRft_series() != null && !co.getRft_series().equals("")) {
            openURL.append("&rft.series=");
            openURL.append(co.getRft_series());
        }
        if (co.getRft_pub() != null && !co.getRft_pub().equals("")) {
            openURL.append("&rft.pub=");
            openURL.append(co.getRft_pub());
        }
        if (co.getRft_place() != null && !co.getRft_place().equals("")) {
            openURL.append("&rft.place=");
            openURL.append(co.getRft_place());
        }
        if (co.getRft_edition() != null && !co.getRft_edition().equals("")) {
            openURL.append("&rft.edition=");
            openURL.append(co.getRft_edition());
        }
        if (co.getRft_title() != null && !co.getRft_title().equals("")) {
            openURL.append("&rft.title=");
            openURL.append(co.getRft_title());
        }
        if (co.getRft_jtitle() != null && !co.getRft_jtitle().equals("")) {
            openURL.append("&rft.jtitle=");
            openURL.append(co.getRft_jtitle());
        }
        if (co.getRft_stitle() != null && !co.getRft_stitle().equals("")) {
            openURL.append("&rft.stitle=");
            openURL.append(co.getRft_stitle());
        }
        if (co.getRft_issn() != null && !co.getRft_issn().equals("")) {
            openURL.append("&rft.issn=");
            openURL.append(co.getRft_issn());
        }
        if (co.getRft_eissn() != null && !co.getRft_eissn().equals("")) {
            openURL.append("&rft.eissn=");
            openURL.append(co.getRft_eissn());
        }
        if (co.getRft_isbn() != null && !co.getRft_isbn().equals("")) {
            openURL.append("&rft.isbn=");
            openURL.append(co.getRft_isbn());
        }
        if (co.getRft_date() != null && !co.getRft_date().equals("")) {
            openURL.append("&rft.date=");
            openURL.append(co.getRft_date());
        }
        if (co.getRft_volume() != null && !co.getRft_volume().equals("")) {
            openURL.append("&rft.volume=");
            openURL.append(co.getRft_volume());
        }
        if (co.getRft_part() != null && !co.getRft_part().equals("")) {
            openURL.append("&rft.part=");
            openURL.append(co.getRft_part());
        }
        if (co.getRft_issue() != null && !co.getRft_issue().equals("")) {
            openURL.append("&rft.issue=");
            openURL.append(co.getRft_issue());
        }
        if (co.getRft_spage() != null && !co.getRft_spage().equals("")) {
            openURL.append("&rft.spage=");
            openURL.append(co.getRft_spage());
        }
        if (co.getRft_epage() != null && !co.getRft_epage().equals("")) {
            openURL.append("&rft.epage=");
            openURL.append(co.getRft_epage());
        }
        if (co.getRft_pages() != null && !co.getRft_pages().equals("")) {
            openURL.append("&rft.pages=");
            openURL.append(co.getRft_pages());
        }
        if (co.getRft_tpages() != null && !co.getRft_tpages().equals("")) {
            openURL.append("&rft.tpages=");
            openURL.append(co.getRft_tpages());
        }
        if (co.getRft_author() != null && !co.getRft_author().equals("")) {
            openURL.append("&rft.author=");
            openURL.append(co.getRft_author());
        }
        if (co.getRft_au() != null && !co.getRft_au().equals("")) {
            openURL.append("&rft.au=");
            openURL.append(co.getRft_au());
        }
        if (co.getRft_aucorp() != null && !co.getRft_aucorp().equals("")) {
            openURL.append("&rft.aucorp=");
            openURL.append(co.getRft_aucorp());
        }
        if (co.getRft_auinit() != null && !co.getRft_auinit().equals("")) {
            openURL.append("&rft.auinit=");
            openURL.append(co.getRft_auinit());
        }
        if (co.getRft_auinit1() != null && !co.getRft_auinit1().equals("")) {
            openURL.append("&rft.auinit1=");
            openURL.append(co.getRft_auinit1());
        }
        if (co.getRft_auinitm() != null && !co.getRft_auinitm().equals("")) {
            openURL.append("&rft.auinitm=");
            openURL.append(co.getRft_auinitm());
        }
        if (co.getRft_ausuffix() != null && !co.getRft_ausuffix().equals("")) {
            openURL.append("&rft.ausuffix=");
            openURL.append(co.getRft_ausuffix());
        }
        if (co.getRft_aulast() != null && !co.getRft_aulast().equals("")) {
            openURL.append("&rft.aulast=");
            openURL.append(co.getRft_aulast());
        }
        if (co.getRft_aufirst() != null && !co.getRft_aufirst().equals("")) {
            openURL.append("&rft.aufirst=");
            openURL.append(co.getRft_aufirst());
        }
        if (co.getRft_sici() != null && !co.getRft_sici().equals("")) {
            openURL.append("&rft.sici=");
            openURL.append(co.getRft_sici());
        }
        if (co.getRft_bici() != null && !co.getRft_bici().equals("")) {
            openURL.append("&rft.bici=");
            openURL.append(co.getRft_bici());
        }
        if (co.getRft_coden() != null && !co.getRft_coden().equals("")) {
            openURL.append("&rft.coden=");
            openURL.append(co.getRft_coden());
        }
        if (co.getRft_artnum() != null && !co.getRft_artnum().equals("")) {
            openURL.append("&rft.artnum=");
            openURL.append(co.getRft_artnum());
        }

        // RFTs Dublin Core, z.B. in Blogs:
        if (co.getRft_creator() != null && !co.getRft_creator().equals("")) {
            openURL.append("&rft.creator=");
            openURL.append(co.getRft_creator());
        }
        if (co.getRft_publisher() != null && !co.getRft_publisher().equals("")) {
            openURL.append("&rft.publisher=");
            openURL.append(co.getRft_publisher());
        }
        if (co.getRft_type() != null && !co.getRft_type().equals("")) {
            openURL.append("&rft.type=");
            openURL.append(co.getRft_type());
        }
        if (co.getRft_subject() != null && !co.getRft_subject().equals("")) {
            openURL.append("&rft.subject=");
            openURL.append(co.getRft_subject());
        }
        if (co.getRft_format() != null && !co.getRft_format().equals("")) {
            openURL.append("&rft.format=");
            openURL.append(co.getRft_format());
        }
        if (co.getRft_language() != null && !co.getRft_language().equals("")) {
            openURL.append("&rft.language=");
            openURL.append(co.getRft_language());
        }
        if (co.getRft_source() != null && !co.getRft_source().equals("")) {
            openURL.append("&rft.source=");
            openURL.append(co.getRft_source());
        }
        if (co.getRft_identifier() != null && !co.getRft_identifier().equals("")) {
            openURL.append("&rft.identifier=");
            openURL.append(co.getRft_identifier());
        }
        if (co.getRft_description() != null && !co.getRft_description().equals("")) {
            openURL.append("&rft.description=");
            openURL.append(co.getRft_description());
        }
        if (co.getRft_relation() != null && !co.getRft_relation().equals("")) {
            openURL.append("&rft.relation=");
            openURL.append(co.getRft_relation());
        }
        if (co.getRft_coverage() != null && !co.getRft_coverage().equals("")) {
            openURL.append("&rft.coverage=");
            openURL.append(co.getRft_coverage());
        }
        if (co.getRft_rights() != null && !co.getRft_rights().equals("")) {
            openURL.append("&rft.rights=");
            openURL.append(co.getRft_rights());
        }

        if (co.getRft_id() != null) {
            for (int i = 0; i < co.getRft_id().size(); i++) { // kann mehrere rft_id enthalten
                openURL.append("&rft_id=");
                openURL.append(co.getRft_id().get(i).toString());
            }
        }

        String output = openURL.toString();

        if (output == null) { output = ""; }

        return output;
    }


    /**
     * liest den Inhalt einer Pubmed-Anzeige im XML-Format und liefert ein ContextObject zurück
     */
    public ContextObject readXmlPubmed(String content) {

        // http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=3966282

        final ContextObject co = new ContextObject();
        final SpecialCharacters specialCharacters = new SpecialCharacters();
        content = specialCharacters.replace(content); // Entfernen von &amp; und co.

        try { // notwendig, da nicht kontrolliert wird, ob XML valide ist!

            // wird bei Umwandlung zu "Artikel", (Journal Article, Randomized Controlled Trial etc.)
            if (content.contains("<Item Name=\"PubType\"")) {
                co.setRft_genre(getXmlTag("<Item Name=\"PubType\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"PubDate\"")) {
                co.setRft_date(getXmlTag("<Item Name=\"PubDate\"", PM_ENDTAG, content));
            }
            // nur falls Datum nicht schon gesetzt
            if (content.contains("<Item Name=\"EPubDate\"")
                    && (co.getRft_date() == null || co.getRft_date().equals(""))) {
                co.setRft_date(getXmlTag("<Item Name=\"EPubDate\"", PM_ENDTAG, content));
            }
            // unter der Annahme, dass es sich um einen Artikel handelt
            if (content.contains("<Item Name=\"FullJournalName\"")) {
                co.setRft_jtitle(getXmlTag("<Item Name=\"FullJournalName\"", PM_ENDTAG, content));
            }
            // unter der Annahme, dass es sich um einen Artikel handelt
            if (content.contains("<Item Name=\"Source\"")) {
                co.setRft_stitle(getXmlTag("<Item Name=\"Source\"", PM_ENDTAG, content));
            }
            // nur der erste Author wird abgefüllt
            if (content.contains("<Item Name=\"Author\"")) {
                co.setRft_author(getXmlTag("<Item Name=\"Author\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"CollectiveName\"")) {
                co.setRft_aucorp(getXmlTag("<Item Name=\"CollectiveName\"", PM_ENDTAG, content));
            }
            // unter der Annahme, dass es sich um einen Artikel handelt
            if (content.contains("<Item Name=\"Title\"")) {
                co.setRft_atitle(getXmlTag("<Item Name=\"Title\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"Volume\"")) {
                co.setRft_volume(getXmlTag("<Item Name=\"Volume\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"Issue\"")) {
                co.setRft_issue(getXmlTag("<Item Name=\"Issue\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"Pages\"")) {
                co.setRft_pages(getXmlTag("<Item Name=\"Pages\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"PubStatus\" Type=\"String\">aheadofprint")
                    && (co.getRft_pages() == null || co.getRft_pages().equals(""))) {
                co.setRft_pages(getXmlTag("<Item Name=\"PubStatus\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"ISSN\"")) {
                co.setRft_issn(getXmlTag("<Item Name=\"ISSN\"", PM_ENDTAG, content));
            }
            // nur abfüllen, falls ISSN nicht schon gesetzt
            if (content.contains("<Item Name=\"ESSN\"") && (co.getRft_issn() == null || co.getRft_issn().equals(""))) {
                co.setRft_eissn(getXmlTag("<Item Name=\"ESSN\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"Lang\"")) {
                co.setRft_language(getXmlTag("<Item Name=\"Lang\"", PM_ENDTAG, content));
            }

            final ArrayList<String> id = new ArrayList<String>();

            if (content.contains("<Item Name=\"doi\"")) {
                id.add("info:doi/" + getXmlTag("<Item Name=\"doi\"", PM_ENDTAG, content));
            }
            if (content.contains("<Item Name=\"pubmed\"")) {
                id.add("info:pmid/" + getXmlTag("<Item Name=\"pubmed\"", PM_ENDTAG, content));
            }

            co.setRft_id(id);

        } catch (final Exception e) {
            LOG.error("readXmlPubmed/getXmlTag:\012" + content);
        }

        return co;
    }

    /**
     * holt aus dem Request alle OpenURL-Parameter
     */
    public ConcurrentHashMap<String, String> getOpenUrlParameters(final HttpServletRequest rq) {

        final ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<String, String>();


        @SuppressWarnings("unchecked")
        final
        Map<String, String[]> paramMap = rq.getParameterMap();

        for (final Map.Entry<String, String[]> pairs : paramMap.entrySet()) {

            final String key = pairs.getKey();
            final String[] values = pairs.getValue();

            if ("rft_id".equals(key)) { // rft_id enthält nocheinmal unterschiedliche Identifier nach dem info: Scheme
                // rft_id wird jeweils separat abgelegt und nicht aneinander gehängt
                for (int z = 0; z < values.length; z++) {
                    hm.put(key, getOpenUrlValue(values[z]));
                }
            } else { // hier werden Mehrfachparameter (z.B. mehrere Autoren) aneinander gehängt
                final StringBuffer buf = new StringBuffer();
                for (int z = 0; z < values.length; z++) {
                    if (z == 0) { buf.append(values[z]); } else {
                        buf.append("\040;\040");
                        buf.append(values[z]);
                    }
                }
                hm.put(key, getOpenUrlValue(buf.toString()));
            }

        }

        return hm;
    }

    /**
     * extrahiert aus dem content den Inhalt eines RFTs, RFRs oder RFEs (Standard-Version)
     */
    private String getOpenUrlIdentifiersVersion1_0(final String rft, final String content) {

        String output = "";
        final CodeUrl codeUrl = new CodeUrl();

        // Delimiter ist das nächste &rft (Referent), &rfe (Referring Entity), &rfr (Referrer)
        if (content.substring(content.indexOf(rft)).contains("&rf")) {
            output = content.substring(content.indexOf(rft)
                    + rft.length(), content.indexOf("&rf", content.indexOf(rft)));
        } else {
            // Delimiter ist das " am Ende des URLs in HTML
            if (content.substring(content.indexOf(rft)).contains("\"")) {
                output = content.substring(content.indexOf(rft)
                        + rft.length(), content.indexOf('"', content.indexOf(rft)));
            } else {
                // Delimiter ist Space oder Umbruch
                if (content.substring(content.indexOf(rft)).contains("\040")
                        || content.substring(content.indexOf(rft)).contains("\012")) {
                    if (content.substring(content.indexOf(rft)).contains("\040")) {
                        output = content.substring(content.indexOf(rft)
                                + rft.length(), content.indexOf('\040', content.indexOf(rft)));
                    } else {
                        output = content.substring(content.indexOf(rft)
                                + rft.length(), content.indexOf('\012', content.indexOf(rft)));
                    }
                } else {
                    // kein Delimiter. String bis ans Ende
                    output = content.substring(content.indexOf(rft) + rft.length());
                }
            }
        }

        output = codeUrl.decode(output); // RFTs sind URL-codiert

        return output;
    }

    /**
     * extrahiert aus dem content den Inhalt die nicht standardisierten RFTs (ohne RFT-Bezeichnung)
     */
    private String getOpenUrlIdentifiersVersion0_1(final String rft, final String content) {

        String output = "";
        final CodeUrl codeUrl = new CodeUrl();

        // Delimiter ist das nächste &
        if (content.substring(content.indexOf(rft) + 1).contains("&")) {
            output = content.substring(content.indexOf(rft)
                    + rft.length(), content.indexOf("&", content.indexOf(rft) + 1));
        } else {
            // Delimiter ist das " am Ende des URLs in HTML
            if (content.substring(content.indexOf(rft)).contains("\"")) {
                output = content.substring(content.indexOf(rft)
                        + rft.length(), content.indexOf("\"", content.indexOf(rft)));
            } else {
                // Delimiter ist Space oder Umbruch
                if (content.substring(content.indexOf(rft)).contains("\040")
                        || content.substring(content.indexOf(rft)).contains("\012")) {
                    if (content.substring(content.indexOf(rft)).contains("\040")) {
                        output = content.substring(content.indexOf(rft)
                                + rft.length(), content.indexOf("\040", content.indexOf(rft)));
                    } else {
                        output = content.substring(content.indexOf(rft)
                                + rft.length(), content.indexOf("\012", content.indexOf(rft)));
                    }
                } else {
                    // kein Delimiter. String bis ans Ende
                    output = content.substring(content.indexOf(rft) + rft.length());
                }
            }
        }

        output = codeUrl.decode(output); // RFTs sind URL-codiert

        return output;
    }

    /**
     * extrahiert aus dem XML-Content den Inhalt eines bestimmten Tags
     */
    private String getXmlTag(final String starttag, final String endtag, final String content) {

        String output = "";

        output = content.substring(content.indexOf('>', content.indexOf(starttag)) + 1,
                content.indexOf(endtag, content.indexOf(starttag)));

        return output;
    }

    private String getOpenUrlValue(String input) {

        try {

            if (decode(input)) { input = Decoder.utf8Convert(input); }

        } catch (final Exception e) {
            LOG.error("private boolean decode (String input): " + e.toString());
        }

        return input;
    }

    /**
     * OpenURL-request sometimes are UTF-8 encoded, sometimes ISO-8895-1
     * Here will, check if we need to decode a string or not
     */
    private boolean decode(final String input) {

        boolean check = false;

        try {

            if (input.length() > Decoder.utf8Convert(input).length()) { check = true; }

        } catch (final Exception e) {
            LOG.error("private boolean decode (String input): " + e.toString());
        }

        return check;
    }


}
