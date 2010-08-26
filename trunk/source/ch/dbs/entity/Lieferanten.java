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
import java.util.ArrayList;

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Markus Fischer
 */
public class Lieferanten extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Lieferanten.class);

    private Long lid;
    private Long kid;
    private String sigel;
    private String name;
    private boolean land_d;
    private boolean land_a;
    private boolean land_ch;
    private boolean land_allgemein;


    public Lieferanten() {

    }

    public Lieferanten(final AbstractBenutzer user, final Konto k) {

    }

    public Lieferanten(final ResultSet rs) throws Exception {

        this.setLid(rs.getLong("LID"));
        this.setKid(rs.getLong("kid"));
        this.setSigel(rs.getString("siegel"));
        this.setName(rs.getString("lieferant"));
        this.setLand_a(rs.getBoolean("A"));
        this.setLand_d(rs.getBoolean("D"));
        this.setLand_ch(rs.getBoolean("CH"));
        this.setLand_allgemein(rs.getBoolean("allgemein"));
    }

    public ArrayList<Lieferanten> getListForKontoAndCountry(String land, final Long kId, final Connection cn) {

        if ("Schweiz".equals(land)) { land = "CH"; }
        if ("Deutschland".equals(land)) { land = "D"; }
        if ("Österreich".equals(land)) { land = "A"; }
        // if we couldn't set a country code, use a default
        if (!"CH".equals(land) && !"D".equals(land) && !"A".equals(land)) { land = "CH"; }

        final ArrayList<Lieferanten> list = new ArrayList<Lieferanten>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM lieferanten WHERE `kid`=? OR `allgemein`='1' OR `"
                    + land + "`='1' ORDER BY siegel ASC, lieferant ASC");
            pstmt.setLong(1, kId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(getLieferanten(rs));
            }

        } catch (final Exception e) {
            LOG.error("getListForKontoAndCountry(): " + e.toString());
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

        return list;

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
                l = getLieferanten(rs);
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
                l = getLieferanten(rs);
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

    private Lieferanten getLieferanten(final ResultSet rs) throws Exception {

        final Lieferanten l = new Lieferanten();

        try {

            l.setLid(rs.getLong("LID"));
            l.setKid(rs.getLong("kid"));
            l.setSigel(rs.getString("siegel"));
            l.setName(rs.getString("lieferant"));
            l.setLand_a(rs.getBoolean("A"));
            l.setLand_d(rs.getBoolean("D"));
            l.setLand_ch(rs.getBoolean("CH"));
            l.setLand_allgemein(rs.getBoolean("allgemein"));

        } catch (final Exception e) {
            LOG.error("getLieferanten(ResultSet rs): " + e.toString());
        }

        return l;
    }


    public Long getKid() {
        return kid;
    }


    public void setKid(final Long kid) {
        this.kid = kid;
    }


    public boolean isLand_a() {
        return land_a;
    }


    public void setLand_a(final boolean land_a) {
        this.land_a = land_a;
    }


    public boolean isLand_allgemein() {
        return land_allgemein;
    }


    public void setLand_allgemein(final boolean land_allgemein) {
        this.land_allgemein = land_allgemein;
    }


    public boolean isLand_ch() {
        return land_ch;
    }


    public void setLand_ch(final boolean land_ch) {
        this.land_ch = land_ch;
    }


    public boolean isLand_d() {
        return land_d;
    }


    public void setLand_d(final boolean land_d) {
        this.land_d = land_d;
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




}
