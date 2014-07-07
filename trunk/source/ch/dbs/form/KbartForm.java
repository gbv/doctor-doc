//  Copyright (C) 2014  Markus Fischer
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

package ch.dbs.form;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.Bestand;
import ch.dbs.entity.Issn;

public final class KbartForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    // properties outside the original KBART format
    private boolean eissue;
    private boolean internal;
    private String sublocation;
    private String shelf;

    // properties of the original KBART format
    private String publication_title;
    private String print_identifier;
    private String online_identifier;
    private String date_first_issue_online;
    private String num_first_vol_online;
    private String num_first_issue_online;
    private String date_last_issue_online;
    private String num_last_vol_online;
    private String num_last_issue_online;
    private String title_url;
    private String first_author;
    private String title_id;
    private String embargo_info;
    private String coverage_depth;
    private String coverage_notes;
    private String publisher_name;


    public KbartForm() {

    }

    /**
     * Converts a List<Bestand> to a List<KbartForm> with additional ISSN mapping.
     * This method will therefore create a 1:n relation.
     * 
     * @param List<Bestand>
     * @param Connection
     * @return List<KbartForm>
     */
    public List<KbartForm> createKbartForms(final List<Bestand> stocks, final Connection cn) {
        final List<KbartForm> result = new ArrayList<KbartForm>();

        for (final Bestand stock : stocks) {
            // if we have an ISSN, add all with all ISSN mappings
            if (!isEmpty(stock.getHolding().getIssn())) {
                // get all related ISSNs for this stock
                final List<String> issns = getRelatedIssn(stock.getHolding().getIssn(), cn);

                for (final String issn : issns) {
                    final KbartForm kbart = new KbartForm(stock);
                    // use found ISSNs
                    if (stock.isEissue()) {
                        kbart.setOnline_identifier(issn);
                    } else {
                        kbart.setPrint_identifier(issn);
                    }
                    result.add(kbart);
                }
            } else {
                // no ISSN: add as is
                final KbartForm kbart = new KbartForm(stock);
                result.add(kbart);
            }
        }

        return result;
    }

    /**
     * Converts a single Bestand to a KbartForm.
     */
    private KbartForm(final Bestand bestand) {
        this.setPublication_title(bestand.getHolding().getTitel());
        // set ISSN as Print- or Online-Identifier according to isEissue
        if (bestand.isEissue()) {
            // use ISSN if present...
            if (!isEmpty(bestand.getHolding().getIssn())) {
                this.setOnline_identifier(bestand.getHolding().getIssn());
            } else if (!isEmpty(bestand.getHolding().getZdbid())) {
                // ...use ZDBID if no ISSN present
                this.setOnline_identifier(bestand.getHolding().getZdbid());
            }
        } else {
            // use ISSN if present...
            if (!isEmpty(bestand.getHolding().getIssn())) {
                this.setPrint_identifier(bestand.getHolding().getIssn());
            } else if (!isEmpty(bestand.getHolding().getZdbid())) {
                // ...use ZDBID if no ISSN present
                this.setPrint_identifier(bestand.getHolding().getZdbid());
            }
        }
        this.setDate_first_issue_online(bestand.getStartyear());
        this.setNum_first_issue_online(bestand.getStartissue());
        this.setNum_first_vol_online(bestand.getStartvolume());
        this.setDate_last_issue_online(bestand.getEndyear());
        this.setNum_last_issue_online(bestand.getEndissue());
        this.setNum_last_vol_online(bestand.getEndvolume());
        // title_url not present
        // use title_id for DAIA-linking over stockid
        this.setTitle_id(bestand.getId().toString());
        // embargo_info not present
        // coverage_depth not present
        this.setCoverage_notes(bestand.getBemerkungen());
        // set publisher and place
        final StringBuffer publisher = new StringBuffer(bestand.getHolding().getOrt());
        // place already set: we need a separation character
        if (publisher.length() > 0) {
            publisher.append(" : ");
        }
        publisher.append(bestand.getHolding().getVerlag());
        this.setPublisher_name(publisher.toString());

        // set non KBART properties
        this.setSublocation(bestand.getStandort().getInhalt());
        this.setShelf(bestand.getShelfmark());
        this.setEissue(bestand.isEissue());
        this.setInternal(bestand.isInternal());
    }


    private List<String> getRelatedIssn(final String issn, final Connection cn) {

        final Issn is = new Issn();

        final List<String> issns = is.getAllIssnsFromOneIssn(issn, cn);

        if (issns.isEmpty()) {
            issns.add(issn);
        } // if there has been no hit, return the ISSN from the input

        return issns;
    }

    public boolean isEissue() {
        return eissue;
    }
    public void setEissue(final boolean eissue) {
        this.eissue = eissue;
    }
    public boolean isInternal() {
        return internal;
    }
    public void setInternal(final boolean internal) {
        this.internal = internal;
    }
    public String getSublocation() {
        return sublocation;
    }
    public void setSublocation(final String sublocation) {
        this.sublocation = sublocation;
    }
    public String getShelf() {
        return shelf;
    }
    public void setShelf(final String shelf) {
        this.shelf = shelf;
    }
    public String getPublication_title() {
        return publication_title;
    }
    public void setPublication_title(final String publication_title) {
        this.publication_title = publication_title;
    }
    public String getPrint_identifier() {
        return print_identifier;
    }
    public void setPrint_identifier(final String print_identifier) {
        this.print_identifier = print_identifier;
    }
    public String getOnline_identifier() {
        return online_identifier;
    }
    public void setOnline_identifier(final String online_identifier) {
        this.online_identifier = online_identifier;
    }
    public String getDate_first_issue_online() {
        return date_first_issue_online;
    }
    public void setDate_first_issue_online(final String date_first_issue_online) {
        this.date_first_issue_online = date_first_issue_online;
    }
    public String getNum_first_vol_online() {
        return num_first_vol_online;
    }
    public void setNum_first_vol_online(final String num_first_vol_online) {
        this.num_first_vol_online = num_first_vol_online;
    }
    public String getNum_first_issue_online() {
        return num_first_issue_online;
    }
    public void setNum_first_issue_online(final String num_first_issue_online) {
        this.num_first_issue_online = num_first_issue_online;
    }
    public String getDate_last_issue_online() {
        return date_last_issue_online;
    }
    public void setDate_last_issue_online(final String date_last_issue_online) {
        this.date_last_issue_online = date_last_issue_online;
    }
    public String getNum_last_vol_online() {
        return num_last_vol_online;
    }
    public void setNum_last_vol_online(final String num_last_vol_online) {
        this.num_last_vol_online = num_last_vol_online;
    }
    public String getNum_last_issue_online() {
        return num_last_issue_online;
    }
    public void setNum_last_issue_online(final String num_last_issue_online) {
        this.num_last_issue_online = num_last_issue_online;
    }
    public String getTitle_url() {
        return title_url;
    }
    public void setTitle_url(final String title_url) {
        this.title_url = title_url;
    }
    public String getFirst_author() {
        return first_author;
    }
    public void setFirst_author(final String first_author) {
        this.first_author = first_author;
    }
    public String getTitle_id() {
        return title_id;
    }
    public void setTitle_id(final String title_id) {
        this.title_id = title_id;
    }
    public String getEmbargo_info() {
        return embargo_info;
    }
    public void setEmbargo_info(final String embargo_info) {
        this.embargo_info = embargo_info;
    }
    public String getCoverage_depth() {
        return coverage_depth;
    }
    public void setCoverage_depth(final String coverage_depth) {
        this.coverage_depth = coverage_depth;
    }
    public String getCoverage_notes() {
        return coverage_notes;
    }
    public void setCoverage_notes(final String coverage_notes) {
        this.coverage_notes = coverage_notes;
    }
    public String getPublisher_name() {
        return publisher_name;
    }
    public void setPublisher_name(final String publisher_name) {
        this.publisher_name = publisher_name;
    }

    private boolean isEmpty(final String input) {

        if (input == null || input.trim().length() == 0) {
            return true;
        }
        return false;
    }

}
