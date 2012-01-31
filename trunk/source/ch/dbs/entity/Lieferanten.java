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
import ch.dbs.form.UserInfo;


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
     * Gets all private Lieferanten for a given account. This is a wrapper method, executing the
     * appropriate method depending on the settings for an account.
     */
    public List<Lieferanten> getLieferanten(final UserInfo ui, final Connection cn) {

        List<Lieferanten> result = new ArrayList<Lieferanten>();

        // three cases:

        if (ui.getKonto().isShowprivsuppliers() && ui.getKonto().isShowpubsuppliers()) {
            // show private and public suppliers
            result = getAll(ui.getKonto().getLand(), ui.getKonto().getId(), cn);
        } else if (ui.getKonto().isShowprivsuppliers() && !ui.getKonto().isShowpubsuppliers()) {
            // show private suppliers
            result = getPrivate(ui.getKonto().getId(), cn);
        } else {
            // show public suppliers
            result = getPublic(ui.getKonto().getLand(), cn);
        }

        return result;
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
     * Gets all private Lieferanten for a given account. These suppliers may be editable.
     */
    private List<Lieferanten> getPrivate(final Long kId, final Connection cn) {

        final List<Lieferanten> result = new ArrayList<Lieferanten>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid`=? ORDER BY siegel ASC, lieferant ASC");
            pstmt.setLong(1, kId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Lieferanten l = new Lieferanten(rs);
                result.add(l);
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
    private List<Lieferanten> getPublic(final String land, final Connection cn) {

        final List<Lieferanten> result = new ArrayList<Lieferanten>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid` IS NULL AND (`allgemein`='1' OR "
                    + "`countryCode`=?) ORDER BY siegel ASC, lieferant ASC");

            pstmt.setString(1, land);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Lieferanten l = new Lieferanten(rs);
                result.add(l);
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
    private List<Lieferanten> getAll(final String land, final Long kId, final Connection cn) {

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

    public void update(final Lieferanten l, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `lieferanten` SET `siegel` = ? , `lieferant` = ? , `emailILL` = ? , "
                    + "`countryCode` = ? , `allgemein` = ? , `KID` = ? WHERE `lieferanten`.`LID` =?");
            pstmt.setString(1, l.getSigel());
            pstmt.setString(2, l.getName());
            pstmt.setString(3, l.getEmailILL());
            pstmt.setString(4, l.getCountryCode());
            pstmt.setBoolean(5, l.isLand_allgemein());
            if (l.getKid() != null) {
                pstmt.setLong(6, l.getKid());
            } else {
                pstmt.setString(6, null);
            }
            pstmt.setLong(7, l.getLid());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("update(final Lieferanten l, final Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }


    public void save(final Lieferanten l, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("INSERT INTO `lieferanten` (`siegel` , `lieferant` , `emailILL` , "
                    + "`countryCode` , `allgemein` , `KID`) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, l.getSigel());
            pstmt.setString(2, l.getName());
            pstmt.setString(3, l.getEmailILL());
            pstmt.setString(4, l.getCountryCode());
            pstmt.setBoolean(5, l.isLand_allgemein());
            if (l.getKid() != null) {
                pstmt.setLong(6, l.getKid());
            } else {
                pstmt.setString(6, null);
            }
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("save(final Lieferanten l, final Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }


    public void delete(final String sid, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `lieferanten` WHERE `LID` =?");
            pstmt.setString(1, sid);
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("delete(final String sid, final Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }

    public void setFormValues(final Lieferanten sup, final SupplierForm sf, final UserInfo ui) {

        if (!isEmpty(sf.getKid())) {
            sup.setKid(sf.getKid());
        } else {
            sup.setKid(null);
        }
        if (!isEmpty(sf.getSigel())) {
            sup.setSigel(sf.getSigel());
        } else {
            sup.setSigel("");
        }
        if (!isEmpty(sf.getName())) {
            sup.setName(sf.getName());
        } else {
            sup.setName("k.A.");
        }
        if (!isEmpty(sf.getEmailILL())) {
            sup.setEmailILL(sf.getEmailILL());
        } else {
            sup.setEmailILL("");
        }
        // choose between individual suppliers and suppliers for the country of the account
        if (sf.isIndividual()) {
            sup.setKid(ui.getKonto().getId());
            sup.setLand_allgemein(false);
            sup.setCountryCode("");
        } else {
            sup.setCountryCode(ui.getKonto().getLand());
            sup.setLand_allgemein(false);
            sup.setKid(null);
        }

    }

    private boolean isEmpty(final String input) {

        boolean result = false;

        if (input == null || "".equals(input.trim())) {
            result = true;
        }

        return result;
    }

    private boolean isEmpty(final Long input) {

        boolean result = false;

        if (input == null || 0 == input) {
            result = true;
        }

        return result;
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
