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

import java.util.List;


/**
 * Klasse um ein ContextObject im Zusammenhang mit OpenURL zu erstellen.
 * Bildet die Grundlage um per OpenURL kommunizieren zu können.
 * <p/>
 * @author Markus Fischer
 */
public class ContextObject {


    public ContextObject() {

    }

    //  mögliche Angaben für rft Referent-Infos (the cited article) und und Referrer rfr (the database)
    //  [ Angaben zu Referring Entity rfe (the citing article)  sind beschränkt relevant]

    private String rft_val_fmt = ""; // contains e.g.: "journal" / "book" z.B. rft_val_fmt=info:ofi/fmt:kev:mtx:journal
    private String rfr_id = "";    // Referrer (woher stammt die Anfrage)
    private String rft_genre = "";   // Genre of the document. Legitimate values for the "genre" element are:
    // for Journals:
    // (1) "journal": for a serial publication issued in successive parts
    // (2) "issue": for one instance of the serial publication
    // (3) "article": for a document published in a journal.
    // (4) "conference": for a record of a conference that includes one or more conference papers
    //      and that is published as an issue of a journal or serial publication
    // (5) "proceeding": for a single conference presentation published in a journal or serial publication
    // (6) "preprint": for an individual paper or report published in paper or electronically
    //      prior to its publication in a journal or serial
    // (7) "unknown": use when the genre is unknown.
    //  for Books
    // (1) "book": a publication that is complete in one part or a designated finite number
    //      of parts, often identified with an ISBN.
    // (2) "bookitem": a defined section of a book, usually with a separate title or number.
    // (3) "proceeding": a conference paper or proceeding published in a conference publication.
    // (4) "conference": a publication bundling the proceedings of a conference.
    // (5) "report": report or technical report is a published document that is
    //      issued by an organization, agency or government body.
    // (6) "document": general document type to be used when available data elements do
    //      not allow determination of a more specific document type, i.e. when one has
    //      only author and title but no publication information.
    // (7) "unknown": use when the genre of the document is unknown.
    private String rft_atitle = ""; // Artikeltitel
    private String rft_btitle = ""; // Identifikator für Buchtitel
    private String rft_title = "";   // Zeitschriftentitel [Kompatibilität zu OpenURL Version 0.1]
    private String rft_jtitle = ""; // Zeitschriftentitel
    private String rft_stitle = ""; // abgekürzter Zeitschriftentitel
    private String rft_series = ""; // The title of a series in which the book or document
    //                                 was issued. There may also be an ISSN associated with the series.
    private String rft_place = "";  // Identifikator für Ort
    private String rft_edition = ""; // Identifikator für Ausgabe
    private String rft_pub = "";    // Identifikator für Publisher
    private String rft_issn = "";   // Print-ISSN
    private String rft_issn1 = "";   // Print-ISSN mit Bindestrich
    private String rft_eissn = "";   // E-ISSN
    private String rft_eissn1 = ""; // E-ISSN mit Bindestrich
    private String rft_isbn = "";   // ISBN e.g. "057117678X" or "1-878067-73-7"
    private String rft_date = "";   // Jahr in ISO 8601 form YYYY, YYYY-MM or YYYY-MM-DD
    private String rft_volume = ""; // Jahrgang
    private String rft_part = "";   // Part can be a special subdivision of a volume or it can be the
    //                                 highest level division of the journal. Parts are often designated
    //                                 with letters or names, e.g. "B", "Supplement"
    private String rft_issue = "";   // Heft
    private String rft_spage = "";  // start page
    private String rft_epage = "";   // end page
    private String rft_pages = "";   // Start and end pages in the form "startpage-endpage". This field can also
    //                                  be used for an unstructured pagination statement when data relating to
    //                                  pagination cannot be interpreted as a start-end pair, i.e. "A7, C4-9", "1-3, 6"
    private String rft_tpages = ""; // Total pages. Total pages is the largest recorded number of pages, if this can
    //                                 be determined. I.e., "ix, 392 p." would be recorded as "392" in tpages. This
    //                                 element is usually available only for monographs (books and printed reports).
    //                                 In some cases, tpages may not be numeric, i.e. "F36"
    private String rft_author = ""; // The author's full name, i.e. "Smith, Fred M", "Harry S. Truman"
    private String rft_au = "";     // kein offizieller Identifikator für Author. The author's full name,
    //                                 i.e. "Smith, Fred M", "Harry S. Truman"
    private String rft_auinit = ""; // First author's first and middle initials.
    private String rft_auinit1 = ""; // First author's first initial.
    private String rft_auinitm = ""; // First author's middle initial.
    private String rft_ausuffix = ""; // First author's name suffix. Qualifiers on an author's name such as "Jr.",
    //                                  "III" are entered here. i.e. Smith, Fred Jr. is recorded as "ausuffux=jr"
    private String rft_aucorp = ""; // Organization / corporation that is the author or creator, i.e. "MellonFoundation"
    private String rft_aufirst = ""; // Initialen Vorname Autor
    private String rft_aulast = ""; // Nachname Autor
    private String rft_sici = "";   // Serial Item and Contribution Identifier (SICI)
    private String rft_bici = "";   // Book Item and Component Identifier (BICI). BICI is a draft NISO standard.
    private String rft_coden = "";   // Journal-Code
    private String rft_artnum = ""; // Article number assigned by the publisher. Article numbers are often
    // generated for publications that do not have usable pagination, in particular
    // electronic journal articles, e.g. "unifi000000090". If article numbers are identifiers that follow
    // a URI Scheme such as "info:doi/" . The information should be provided in the Identifier Descriptor
    // of the ContextObject, not in this "artnum" element. Likewise, if articles are identified by means
    // of a registered URI Scheme such as the http scheme, the information should be provided in the
    // Identifier Descriptor of the ContextObject

    private List<String> rft_id;  // enthält uri-infos wie pmid, doi, lccn etc.: // http://www.info-uri.info/registry/

    // zusätzliche RFTs für Dublin Core, z.B in Blogss:
    private String rft_creator = ""; // kein offizieller Identifikator für Author.
    private String rft_publisher = "";  // kein offizieller Identifikator für Publisher
    private String rft_type = "";    // kein offizieller Identifikator für genre (blogPost...)
    private String rft_subject = "";    // kein offizieller Identifikator für Schlagwörter / Tags...
    private String rft_format = "";    // kein offizieller Identifikator für "Format" (text...)
    private String rft_language = "";    // kein offizieller Identifikator für Sprache...
    private String rft_source = "";    // kein offizieller Identifikator für Quelle in Klartext (z.B. Biblionik...)
    private String rft_identifier = "";    // kein offizieller Identifikator für URL
    private String rft_description = "";    // kein offizieller Identifikator für Beschreibung...
    private String rft_relation = "";    // kein offizieller für ?
    private String rft_coverage = "";    // kein offizieller für ?
    private String rft_rights = "";    // kein offizieller für Rechte?



    public String getRft_artnum() {
        return rft_artnum;
    }
    public void setRft_artnum(String rft_artnum) {
        this.rft_artnum = rft_artnum;
    }
    public String getRfr_id() {
        return rfr_id;
    }
    public void setRfr_id(String rfr_id) {
        this.rfr_id = rfr_id;
    }
    public String getRft_atitle() {
        return rft_atitle;
    }
    public void setRft_atitle(String rft_atitle) {
        this.rft_atitle = rft_atitle;
    }
    public String getRft_aucorp() {
        return rft_aucorp;
    }
    public void setRft_aucorp(String rft_aucorp) {
        this.rft_aucorp = rft_aucorp;
    }
    public String getRft_aufirst() {
        return rft_aufirst;
    }
    public void setRft_aufirst(String rft_aufirst) {
        this.rft_aufirst = rft_aufirst;
    }
    public String getRft_aulast() {
        return rft_aulast;
    }
    public void setRft_aulast(String rft_aulast) {
        this.rft_aulast = rft_aulast;
    }
    public String getRft_author() {
        return rft_author;
    }
    public void setRft_author(String rft_author) {
        this.rft_author = rft_author;
    }
    public String getRft_date() {
        return rft_date;
    }
    public void setRft_date(String rft_date) {
        this.rft_date = rft_date;
    }
    public String getRft_eissn() {
        return rft_eissn;
    }
    public void setRft_eissn(String rft_eissn) {
        this.rft_eissn = rft_eissn;
    }
    public String getRft_epage() {
        return rft_epage;
    }
    public void setRft_epage(String rft_epage) {
        this.rft_epage = rft_epage;
    }
    public String getRft_genre() {
        return rft_genre;
    }
    public void setRft_genre(String rft_genre) {
        this.rft_genre = rft_genre;
    }
    public String getRft_isbn() {
        return rft_isbn;
    }
    public void setRft_isbn(String rft_isbn) {
        this.rft_isbn = rft_isbn;
    }
    public String getRft_issn() {
        return rft_issn;
    }
    public void setRft_issn(String rft_issn) {
        this.rft_issn = rft_issn;
    }
    public String getRft_eissn1() {
        return rft_eissn1;
    }
    public void setRft_eissn1(String rft_eissn1) {
        this.rft_eissn1 = rft_eissn1;
    }
    public String getRft_issn1() {
        return rft_issn1;
    }
    public void setRft_issn1(String rft_issn1) {
        this.rft_issn1 = rft_issn1;
    }
    public String getRft_issue() {
        return rft_issue;
    }
    public void setRft_issue(String rft_issue) {
        this.rft_issue = rft_issue;
    }
    public String getRft_jtitle() {
        return rft_jtitle;
    }
    public void setRft_jtitle(String rft_jtitle) {
        this.rft_jtitle = rft_jtitle;
    }
    public String getRft_pages() {
        return rft_pages;
    }
    public void setRft_pages(String rft_pages) {
        this.rft_pages = rft_pages;
    }
    public String getRft_part() {
        return rft_part;
    }
    public void setRft_part(String rft_part) {
        this.rft_part = rft_part;
    }
    public String getRft_spage() {
        return rft_spage;
    }
    public void setRft_spage(String rft_spage) {
        this.rft_spage = rft_spage;
    }
    public String getRft_stitle() {
        return rft_stitle;
    }
    public void setRft_stitle(String rft_stitle) {
        this.rft_stitle = rft_stitle;
    }
    public String getRft_title() {
        return rft_title;
    }
    public void setRft_title(String rft_title) {
        this.rft_title = rft_title;
    }
    public String getRft_volume() {
        return rft_volume;
    }
    public void setRft_volume(String rft_volume) {
        this.rft_volume = rft_volume;
    }

    public String getRft_btitle() {
        return rft_btitle;
    }
    public void setRft_btitle(String rft_btitle) {
        this.rft_btitle = rft_btitle;
    }
    public String getRft_pub() {
        return rft_pub;
    }
    public void setRft_pub(String rft_pub) {
        this.rft_pub = rft_pub;
    }
    public List<String> getRft_id() {
        return rft_id;
    }
    public void setRft_id(List<String> rft_id) {
        this.rft_id = rft_id;
    }
    public String getRft_val_fmt() {
        return rft_val_fmt;
    }
    public void setRft_val_fmt(String rft_val_fmt) {
        this.rft_val_fmt = rft_val_fmt;
    }
    public String getRft_au() {
        return rft_au;
    }
    public void setRft_au(String rft_au) {
        this.rft_au = rft_au;
    }
    public String getRft_auinit() {
        return rft_auinit;
    }
    public void setRft_auinit(String rft_auinit) {
        this.rft_auinit = rft_auinit;
    }
    public String getRft_bici() {
        return rft_bici;
    }
    public void setRft_bici(String rft_bici) {
        this.rft_bici = rft_bici;
    }
    public String getRft_coden() {
        return rft_coden;
    }
    public void setRft_coden(String rft_coden) {
        this.rft_coden = rft_coden;
    }
    public String getRft_place() {
        return rft_place;
    }
    public void setRft_place(String rft_place) {
        this.rft_place = rft_place;
    }
    public String getRft_sici() {
        return rft_sici;
    }
    public void setRft_sici(String rft_sici) {
        this.rft_sici = rft_sici;
    }
    public String getRft_tpages() {
        return rft_tpages;
    }
    public void setRft_tpages(String rft_tpages) {
        this.rft_tpages = rft_tpages;
    }

    public String getRft_auinit1() {
        return rft_auinit1;
    }
    public void setRft_auinit1(String rft_auinit1) {
        this.rft_auinit1 = rft_auinit1;
    }
    public String getRft_auinitm() {
        return rft_auinitm;
    }
    public void setRft_auinitm(String rft_auinitm) {
        this.rft_auinitm = rft_auinitm;
    }
    public String getRft_ausuffix() {
        return rft_ausuffix;
    }
    public void setRft_ausuffix(String rft_ausuffix) {
        this.rft_ausuffix = rft_ausuffix;
    }
    public String getRft_edition() {
        return rft_edition;
    }
    public void setRft_edition(String rft_edition) {
        this.rft_edition = rft_edition;
    }
    public String getRft_series() {
        return rft_series;
    }
    public void setRft_series(String rft_series) {
        this.rft_series = rft_series;
    }
    public String getRft_coverage() {
        return rft_coverage;
    }
    public void setRft_coverage(String rft_coverage) {
        this.rft_coverage = rft_coverage;
    }
    public String getRft_creator() {
        return rft_creator;
    }
    public void setRft_creator(String rft_creator) {
        this.rft_creator = rft_creator;
    }
    public String getRft_description() {
        return rft_description;
    }
    public void setRft_description(String rft_description) {
        this.rft_description = rft_description;
    }
    public String getRft_format() {
        return rft_format;
    }
    public void setRft_format(String rft_format) {
        this.rft_format = rft_format;
    }
    public String getRft_identifier() {
        return rft_identifier;
    }
    public void setRft_identifier(String rft_identifier) {
        this.rft_identifier = rft_identifier;
    }
    public String getRft_language() {
        return rft_language;
    }
    public void setRft_language(String rft_language) {
        this.rft_language = rft_language;
    }
    public String getRft_publisher() {
        return rft_publisher;
    }
    public void setRft_publisher(String rft_publisher) {
        this.rft_publisher = rft_publisher;
    }
    public String getRft_relation() {
        return rft_relation;
    }
    public void setRft_relation(String rft_relation) {
        this.rft_relation = rft_relation;
    }
    public String getRft_rights() {
        return rft_rights;
    }
    public void setRft_rights(String rft_rights) {
        this.rft_rights = rft_rights;
    }
    public String getRft_source() {
        return rft_source;
    }
    public void setRft_source(String rft_source) {
        this.rft_source = rft_source;
    }
    public String getRft_subject() {
        return rft_subject;
    }
    public void setRft_subject(String rft_subject) {
        this.rft_subject = rft_subject;
    }
    public String getRft_type() {
        return rft_type;
    }
    public void setRft_type(String rft_type) {
        this.rft_type = rft_type;
    }





}
