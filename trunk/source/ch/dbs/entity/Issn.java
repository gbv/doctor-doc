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
public class Issn extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Issn.class);

    private String identifier;
    private String identifier_id;
    private String issn;
    private boolean eissn;
    private boolean lissn;
    private String coden;
    private String titel = "";
    private String verlag = "";
    private String ort = "";
    private String sprache = "";


    public Issn() {

    }

    /**
     * Erstellt ein ISSN-Objekt anhand einer Verbindung und der ID
     *
     * @param Long iid
     * @param Connection cn
     * @return ISSN issn
     */
    public Issn(Long iid, Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM issn WHERE IID = ?");
            pstmt.setLong(1, iid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(cn, rs);
            }

        } catch (Exception e) {
            LOG.error("Issn (Long iid, Connection cn): " + e.toString());
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
    }

    /**
     * Erstellt eine dublettenbereinigte ArrayList aller verwandten ISSN-Objekte
     * anhand einer Verbindung und einer bekannten ISSN
     *
     * @param String issn
     * @param Connection cn
     * @return ArrayList<ISSN> issn
     */
    public ArrayList<String> getAllIssnsFromOneIssn(String iss, Connection cn) {

        ArrayList<String> is = new ArrayList<String>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT DISTINCT b.issn FROM `issn` AS a JOIN issn AS b "
                    + "ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier WHERE a.issn = ?");
            pstmt.setString(1, iss);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                is.add(rs.getString("issn"));
            }

        } catch (Exception e) {
            LOG.error("getAllIssnsFromOneIssn(String issn, Connection cn): " + e.toString());
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
        return is;
    }

    /**
     * Creates a deduped ArrayList with all related ISSN numbers from
     * a connection and an identifier_id
     *
     * @param String ident_id
     * @param Connection cn
     * @return ArrayList<ISSN> issn
     */
    public ArrayList<String> getAllIssnsFromOneIdentifierID(String ident_id, Connection cn) {

        ArrayList<String> iss = new ArrayList<String>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT DISTINCT b.issn FROM `issn` AS a JOIN issn AS b "
                    + "ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier WHERE a.identifier_id = ?");
            pstmt.setString(1, ident_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                iss.add(rs.getString("issn"));
            }

        } catch (Exception e) {
            LOG.error("getAllIssnsFromOneEzbid(String ident_id, Connection cn): " + e.toString());
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
        return iss;
    }

    /**
     * Erstellt ein ISSN-Objekt aus einem ResultSet
     *
     * @param cn Connection
     * @param rs ResultSet
     */
    public Issn(Connection cn, ResultSet rs) {

        try {

            this.setId(rs.getLong("IID"));
            this.setIdentifier(rs.getString("identifier"));
            this.setIdentifier_id(rs.getString("identifier_id"));
            this.setIssn(rs.getString("issn"));
            this.setEissn(rs.getBoolean("eissn"));
            this.setLissn(rs.getBoolean("lissn"));
            this.setCoden(rs.getString("coden"));
            this.setTitel(rs.getString("titel"));
            this.setVerlag(rs.getString("verlag"));
            this.setOrt(rs.getString("ort"));
            this.setSprache(rs.getString("sprache"));

        } catch (SQLException e) {
            LOG.error("Issn (Connection cn, ResultSet rs): " + e.toString());
        }
    }

    private void setRsValues(Connection cn, ResultSet rs) throws Exception {
        this.setId(rs.getLong("IID"));
        this.setIdentifier(rs.getString("identifier"));
        this.setIdentifier_id(rs.getString("identifier_id"));
        this.setIssn(rs.getString("issn"));
        this.setEissn(rs.getBoolean("eissn"));
        this.setLissn(rs.getBoolean("lissn"));
        this.setCoden(rs.getString("coden"));
        this.setTitel(rs.getString("titel"));
        this.setVerlag(rs.getString("verlag"));
        this.setOrt(rs.getString("ort"));
        this.setSprache(rs.getString("sprache"));
    }


    public String getIssn() {
        return issn;
    }


    public void setIssn(String issn) {
        this.issn = issn;
    }


    public boolean isEissn() {
        return eissn;
    }


    public void setEissn(boolean eissn) {
        this.eissn = eissn;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier_id() {
        return identifier_id;
    }

    public void setIdentifier_id(String identifier_id) {
        this.identifier_id = identifier_id;
    }

    public boolean isLissn() {
        return lissn;
    }

    public void setLissn(boolean lissn) {
        this.lissn = lissn;
    }

    public String getCoden() {
        return coden;
    }

    public void setCoden(String coden) {
        this.coden = coden;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(String verlag) {
        this.verlag = verlag;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }



}
