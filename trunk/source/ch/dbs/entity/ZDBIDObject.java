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

package ch.dbs.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Markus Fischer
 */
public class ZDBIDObject extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(ZDBIDObject.class);

    private String identifier;
    private String identifier_id;
    private String zdbid;
    private List<String> issn;

    public ZDBIDObject() {

    }

    /**
     * Creates a ZDBID-Object from a connection and and ID
     *
     * @param Long zid
     * @param Connection cn
     * @return ZDBIDObject zo
     */
    public ZDBIDObject(final Long zid, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM zdb_id WHERE ZID = ?");
            pstmt.setLong(1, zid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("ZDBIDObject (Long zid, Connection cn)" + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }

    /**
     * Creates a ZDBID-Object from a connection, an identifier and an identifier_id
     *
     * @param String ident
     * @param String ident_id
     * @param Connection cn
     * @return ZDBIDObject zo
     */
    public ZDBIDObject(final String ident, final String ident_id, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM zdb_id WHERE identifier = ? AND identifier_id = ?");
            pstmt.setString(1, ident);
            pstmt.setString(2, ident_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("ZDBIDObject (String ident, String ident_id, Connection cn)" + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }

    /**
     * Creates a ZDBID-Object from a connection and a zdbid
     *
     * @param String zdbid
     * @param Connection cn
     * @return ZDBIDObject zo
     */
    public ZDBIDObject getZdbidObjectFromZdbid(final String zdbId, final Connection cn) {

        ZDBIDObject zo = new ZDBIDObject();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM zdb_id WHERE zdbid = ?");
            pstmt.setString(1, zdbId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                zo = new ZDBIDObject(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("getZdbidObjectFromZdbid (String zdbid, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }

        return zo;
    }

    public ZDBIDObject(final Connection cn, final ResultSet rs) {
        final Issn issnInstance = new Issn();

        try {
            this.setId(rs.getLong("ZID"));
            this.setIssn(issnInstance.getAllIssnsFromOneIdentifierID(rs.getString("identifier_id"), cn));
            this.setIdentifier(rs.getString("identifier"));
            this.setIdentifier_id(rs.getString("identifier_id"));
            this.setZdbid(rs.getString("zdbid"));
        } catch (final Exception e) {
            LOG.error("ZDBIDObject (Connection cn, ResultSet rs)" + e.toString());
        }
    }

    private void setRsValues(final Connection cn, final ResultSet rs) throws Exception {
        final Issn issnInstance = new Issn();
        this.setId(rs.getLong("ZID"));
        this.setIssn(issnInstance.getAllIssnsFromOneIdentifierID(rs.getString("identifier_id"), cn));
        this.setIdentifier(rs.getString("identifier"));
        this.setIdentifier_id(rs.getString("identifier_id"));
        this.setZdbid(rs.getString("zdbid"));
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier_id() {
        return identifier_id;
    }

    public void setIdentifier_id(final String identifierId) {
        identifier_id = identifierId;
    }

    public String getZdbid() {
        return zdbid;
    }

    public void setZdbid(final String zdbid) {
        this.zdbid = zdbid;
    }

    public List<String> getIssn() {
        return issn;
    }

    public void setIssn(final List<String> issn) {
        this.issn = issn;
    }

}
