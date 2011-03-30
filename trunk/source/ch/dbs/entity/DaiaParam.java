//  Copyright (C) 2011  Markus Fischer, Pascal Steiner
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

package ch.dbs.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.grlea.log.SimpleLogger;

/**
 * Class to transmit DAIA requests to an (external) order form.
 * <p/>
 * @author Markus Fischer
 */
public class DaiaParam extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(DaiaParam.class);

    private Long kid;
    private String baseurl;
    private String linkAGB;
    private String linkFees;
    private String firstParam = "?";
    private String protocol = "internal"; // internal / openurl / custom
    private boolean redirect;
    private String mapMediatype;
    private String mapAuthors;
    private String mapAtitle;
    private String mapBtitle;
    private String mapChapter;
    private String mapJournal;
    private String mapPublisher;
    private String mapIssn;
    private String mapIsbn;
    private String mapDate;
    private String mapVolume;
    private String mapIssue;
    private String mapPages;
    private String mapPmid;
    private String mapDoi;
    private String limitations;

    // linkout composed upon baseurl and protocol
    private String linkout;

    public DaiaParam() {

    }

    public DaiaParam(final Long did, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellform_daia WHERE did=?");
            pstmt.setLong(1, did);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                getRsValues(rs);
            }

        } catch (final Exception e) {
            LOG.error("DaiaParam(final Long did, final Connection cn): " + e.toString());
        }  finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("DaiaParam(final Long did, final Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("DaiaParam(final Long did, final Connection cn): " + e.toString());
                }
            }
        }

    }

    private void getRsValues(final ResultSet rs) throws Exception {

        this.setId(rs.getLong("DID"));
        this.setKid(rs.getLong("KID"));
        this.setBaseurl(rs.getString("baseurl"));
        this.setLinkAGB(rs.getString("link_agb"));
        this.setLinkFees(rs.getString("link_fees"));
        this.setFirstParam(rs.getString("first_param"));
        this.setProtocol(rs.getString("protocol"));
        this.setRedirect(rs.getBoolean("redirect"));
        this.setMapMediatype(rs.getString("map_mediatype"));
        this.setMapAuthors(rs.getString("map_authors"));
        this.setMapAtitle(rs.getString("map_atitle"));
        this.setMapBtitle(rs.getString("map_btitle"));
        this.setMapChapter(rs.getString("map_chapter"));
        this.setMapJournal(rs.getString("map_journal"));
        this.setMapPublisher(rs.getString("map_publisher"));
        this.setMapIssn(rs.getString("map_issn"));
        this.setMapIsbn(rs.getString("map_isbn"));
        this.setMapDate(rs.getString("map_date"));
        this.setMapVolume(rs.getString("map_volume"));
        this.setMapIssue(rs.getString("map_issue"));
        this.setMapPages(rs.getString("map_pages"));
        this.setMapPmid(rs.getString("map_pmid"));
        this.setMapDoi(rs.getString("map_doi"));
        this.setLimitations(rs.getString("limitations"));

    }


    public Long getKid() {
        return kid;
    }
    public void setKid(final Long kid) {
        this.kid = kid;
    }
    public String getBaseurl() {
        return baseurl;
    }
    public void setBaseurl(final String baseurl) {
        this.baseurl = baseurl;
    }
    public String getLinkAGB() {
        return linkAGB;
    }
    public void setLinkAGB(final String linkAGB) {
        this.linkAGB = linkAGB;
    }
    public String getLinkFees() {
        return linkFees;
    }
    public void setLinkFees(final String linkFees) {
        this.linkFees = linkFees;
    }
    public String getFirstParam() {
        return firstParam;
    }
    public void setFirstParam(final String firstParam) {
        this.firstParam = firstParam;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }
    public boolean isRedirect() {
        return redirect;
    }
    public void setRedirect(final boolean redirect) {
        this.redirect = redirect;
    }
    public String getMapMediatype() {
        return mapMediatype;
    }
    public void setMapMediatype(final String mapMediatype) {
        this.mapMediatype = mapMediatype;
    }
    public String getMapAuthors() {
        return mapAuthors;
    }
    public void setMapAuthors(final String mapAuthors) {
        this.mapAuthors = mapAuthors;
    }
    public String getMapAtitle() {
        return mapAtitle;
    }
    public void setMapAtitle(final String mapAtitle) {
        this.mapAtitle = mapAtitle;
    }
    public String getMapBtitle() {
        return mapBtitle;
    }
    public void setMapBtitle(final String mapBtitle) {
        this.mapBtitle = mapBtitle;
    }
    public String getMapChapter() {
        return mapChapter;
    }
    public void setMapChapter(final String mapChapter) {
        this.mapChapter = mapChapter;
    }
    public String getMapJournal() {
        return mapJournal;
    }
    public void setMapJournal(final String mapJournal) {
        this.mapJournal = mapJournal;
    }
    public String getMapPublisher() {
        return mapPublisher;
    }
    public void setMapPublisher(final String mapPublisher) {
        this.mapPublisher = mapPublisher;
    }
    public String getMapIssn() {
        return mapIssn;
    }
    public void setMapIssn(final String mapIssn) {
        this.mapIssn = mapIssn;
    }
    public String getMapIsbn() {
        return mapIsbn;
    }
    public void setMapIsbn(final String mapIsbn) {
        this.mapIsbn = mapIsbn;
    }
    public String getMapDate() {
        return mapDate;
    }
    public void setMapDate(final String mapDate) {
        this.mapDate = mapDate;
    }
    public String getMapVolume() {
        return mapVolume;
    }
    public void setMapVolume(final String mapVolume) {
        this.mapVolume = mapVolume;
    }
    public String getMapIssue() {
        return mapIssue;
    }
    public void setMapIssue(final String mapIssue) {
        this.mapIssue = mapIssue;
    }
    public String getMapPages() {
        return mapPages;
    }
    public void setMapPages(final String mapPages) {
        this.mapPages = mapPages;
    }
    public String getMapPmid() {
        return mapPmid;
    }
    public void setMapPmid(final String mapPmid) {
        this.mapPmid = mapPmid;
    }
    public String getMapDoi() {
        return mapDoi;
    }
    public void setMapDoi(final String mapDoi) {
        this.mapDoi = mapDoi;
    }
    public String getLinkout() {
        return linkout;
    }
    public void setLinkout(final String linkout) {
        this.linkout = linkout;
    }
    public String getLimitations() {
        return limitations;
    }
    public void setLimitations(final String limitations) {
        this.limitations = limitations;
    }

}
