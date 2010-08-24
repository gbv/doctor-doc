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

    public Lieferanten(AbstractBenutzer user, Konto k) {

    }

    public Lieferanten(ResultSet rs) throws Exception {

        this.setLid(rs.getLong("LID"));
        this.setKid(rs.getLong("kid"));
        this.setSigel(rs.getString("siegel"));
        this.setName(rs.getString("lieferant"));
        this.setLand_a(rs.getBoolean("A"));
        this.setLand_d(rs.getBoolean("D"));
        this.setLand_ch(rs.getBoolean("CH"));
        this.setLand_allgemein(rs.getBoolean("allgemein"));
    }

    public ArrayList<Lieferanten> getListForKontoAndCountry(String land, Long kId, Connection cn) {

        if (land != null) {
            if (land.equals("Schweiz")) { land = "CH"; }
            if (land.equals("Deutschland")) { land = "D"; }
            if (land.equals("Österreich")) { land = "A"; }
            if (!land.equals("CH") && !land.equals("D") && !land.equals("A")) { land = "CH"; } // falls kein Ländercode
        } else {
            land = "CH"; // falls kein Ländercode
        }

        ArrayList<Lieferanten> list = new ArrayList<Lieferanten>();

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

        } catch (Exception e) {
            LOG.error("getListForKontoAndCountry(): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

        return list;

    }

    public Lieferanten getLieferantFromName(String lName, Connection cn) {

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

        } catch (Exception e) {
            LOG.error("getLieferantFromName(): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

        return l;

    }

    public Lieferanten getLieferantFromLid(String lId, Connection cn) {

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

        } catch (Exception e) {
            LOG.error("getLieferantFromLid: " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

        return l;

    }

    private Lieferanten getLieferanten(ResultSet rs) throws Exception {

        Lieferanten l = new Lieferanten();

        try {

            l.setLid(rs.getLong("LID"));
            l.setKid(rs.getLong("kid"));
            l.setSigel(rs.getString("siegel"));
            l.setName(rs.getString("lieferant"));
            l.setLand_a(rs.getBoolean("A"));
            l.setLand_d(rs.getBoolean("D"));
            l.setLand_ch(rs.getBoolean("CH"));
            l.setLand_allgemein(rs.getBoolean("allgemein"));

        } catch (Exception e) {
            LOG.error("getLieferanten(ResultSet rs): " + e.toString());
        }

        return l;
    }


    public Long getKid() {
        return kid;
    }


    public void setKid(Long kid) {
        this.kid = kid;
    }


    public boolean isLand_a() {
        return land_a;
    }


    public void setLand_a(boolean land_a) {
        this.land_a = land_a;
    }


    public boolean isLand_allgemein() {
        return land_allgemein;
    }


    public void setLand_allgemein(boolean land_allgemein) {
        this.land_allgemein = land_allgemein;
    }


    public boolean isLand_ch() {
        return land_ch;
    }


    public void setLand_ch(boolean land_ch) {
        this.land_ch = land_ch;
    }


    public boolean isLand_d() {
        return land_d;
    }


    public void setLand_d(boolean land_d) {
        this.land_d = land_d;
    }


    public Long getLid() {
        return lid;
    }


    public void setLid(Long lid) {
        this.lid = lid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSigel() {
        return sigel;
    }


    public void setSigel(String sigel) {
        this.sigel = sigel;
    }




}
