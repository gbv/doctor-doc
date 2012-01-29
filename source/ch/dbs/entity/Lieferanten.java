//  Copyright (C) 2005 - 2012  Markus Fischer, Pascal Steiner
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
import java.util.ArrayList;
import java.util.List;

import org.grlea.log.SimpleLogger;

import ch.dbs.form.SupplierForm;


public class Lieferanten extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Lieferanten.class);

    private Long lid;
    private Long kid;
    private String sigel;
    private String name;
    private String emailILL;
    private String countryCode;
    private boolean land_allgemein;


    public Lieferanten() {

    }

    public Lieferanten(final ResultSet rs) throws Exception {

        this.setLid(rs.getLong("LID"));
        this.setKid(rs.getLong("kid"));
        this.setSigel(rs.getString("siegel"));
        this.setName(rs.getString("lieferant"));
        this.setEmailILL(rs.getString("emailILL"));
        this.setCountryCode(rs.getString("countryCode"));
        this.setLand_allgemein(rs.getBoolean("allgemein"));
    }

    /**
     * Gets all private Lieferanten for a given account. These suppliers may be editable.
     */
    public List<SupplierForm> getPrivates(final Long kId, final Connection cn) {

        final List<SupplierForm> result = new ArrayList<SupplierForm>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid`=? ORDER BY siegel ASC, lieferant ASC");
            pstmt.setLong(1, kId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Lieferanten l = new Lieferanten(rs);
                final SupplierForm sf = new SupplierForm(l);
                result.add(sf);
            }

        } catch (final Exception e) {
            LOG.error("List<SupplierForm> getPrivates(final Long kId, final Connection cn): " + e.toString());
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

        return result;
    }

    /**
     * Gets all public Lieferanten for a given country and all general Lieferanten. These suppliers should not be editable.
     */
    public List<SupplierForm> getPublics(final String land, final Connection cn) {

        final List<SupplierForm> result = new ArrayList<SupplierForm>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid` IS NULL AND (`allgemein`='1' OR "
                    + "`countryCode`=?) ORDER BY siegel ASC, lieferant ASC");

            pstmt.setString(1, land);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Lieferanten l = new Lieferanten(rs);
                final SupplierForm sf = new SupplierForm(l);
                result.add(sf);
            }

        } catch (final Exception e) {
            LOG.error("List<SupplierForm> getPublics(final String land, final Connection cn): " + e.toString());
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

        return result;
    }

    /**
     * Gets all private and public Lieferanten for a given account and country.
     */
    public List<Lieferanten> getAll(final String land, final Long kId, final Connection cn) {

        final List<Lieferanten> result = new ArrayList<Lieferanten>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid`=? OR `allgemein`='1' OR "
                    + "`countryCode`=? ORDER BY siegel ASC, lieferant ASC");
            pstmt.setLong(1, kId);
            pstmt.setString(2, land);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new Lieferanten(rs));
            }

        } catch (final Exception e) {
            LOG.error("getAll(): " + e.toString());
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

        return result;
    }

    public Lieferanten getLieferantFromName(final String lName, final Connection cn) {

        Lieferanten l = new Lieferanten();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `lieferant`=?");
            pstmt.setString(1, lName);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                l = new Lieferanten(rs);
            }

        } catch (final Exception e) {
            LOG.error("getLieferantFromName(): " + e.toString());
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

        return l;

    }

    public Lieferanten getLieferantFromLid(final String lId, final Connection cn) {

        Lieferanten l = new Lieferanten();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `LID`=?");
            pstmt.setLong(1, Long.valueOf(lId));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                l = new Lieferanten(rs);
            }

        } catch (final Exception e) {
            LOG.error("getLieferantFromLid: " + e.toString());
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

        return l;

    }


    public Long getKid() {
        return kid;
    }
    public void setKid(final Long kid) {
        this.kid = kid;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }
    public boolean isLand_allgemein() {
        return land_allgemein;
    }
    public void setLand_allgemein(final boolean land_allgemein) {
        this.land_allgemein = land_allgemein;
    }
    public Long getLid() {
        return lid;
    }
    public void setLid(final Long lid) {
        this.lid = lid;
    }
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getSigel() {
        return sigel;
    }
    public void setSigel(final String sigel) {
        this.sigel = sigel;
    }
    public String getEmailILL() {
        return emailILL;
    }
    public void setEmailILL(final String emailILL) {
        this.emailILL = emailILL;
    }

}
