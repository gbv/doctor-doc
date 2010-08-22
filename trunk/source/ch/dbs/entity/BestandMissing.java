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

import org.grlea.log.SimpleLogger;

/**
 * Grundlegende Klasse um Bestandeslücken abzubilden und in die Datenbank zu schreiben
 * <p/>
 * @author Markus Fischer
 */

public class BestandMissing extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(BestandMissing.class);

    private Bestand bestand;
    private String startyear;
    private String startvolume;
    private String startissue;
    private String endyear;
    private String endvolume;
    private String endissue;
    private int suppl;
    private String bemerkungen = "";

    public BestandMissing() {

    }

    /**
     * Erstellt ein BestandMissing aus einem ResultSet
     *
     * @param cn Connection
     * @param rs ResultSet
     */
    public BestandMissing(Connection cn, ResultSet rs) {

        try {

            this.setId(rs.getLong("NLID"));
            this.setBestand(new Bestand(rs.getLong("STID"), cn));
            this.setStartyear(rs.getString("startyear"));
            this.setStartvolume(rs.getString("startvolume"));
            this.setStartissue(rs.getString("startissue"));
            this.setEndyear(rs.getString("endyear"));
            this.setEndvolume(rs.getString("endvolume"));
            this.setEndissue(rs.getString("endissue"));
            this.setSuppl(rs.getInt("suppl"));
            this.setBemerkungen(rs.getString("bemerkungen"));

        } catch (SQLException e) {
            LOG.error("BestandMissing (Connection cn, ResultSet rs)" + e.toString());
        }
    }

    /*
     * Setzt die Werte im Preparestatement der Methoden save()
     */
    private PreparedStatement setBestandMissingValues(PreparedStatement pstmt, BestandMissing be, Connection cn)
    throws Exception {

        pstmt.setLong(1, be.getBestand().getId());
        pstmt.setString(2, be.getStartyear());
        pstmt.setString(3, be.getStartvolume());
        pstmt.setString(4, be.getStartissue());
        pstmt.setString(5, be.getEndyear());
        pstmt.setString(6, be.getEndvolume());
        pstmt.setString(7, be.getEndissue());
        pstmt.setInt(8, be.getSuppl());
        pstmt.setString(9, be.getBemerkungen());

        return pstmt;
    }

    /**
     * Speichert ein neues BestandMissing in der Datenbank
     *
     * @param BestandMissing bm
     * @param Connection cn
     */
    public void save(BestandMissing bm, Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = setBestandMissingValues(cn.prepareStatement("INSERT INTO `stock_missing` (`STID` , "
                    + "`startyear` , `startvolume` , `startissue` , `endyear` , `endvolume` , `endissue` , "
                    + "`suppl` , `bemerkungen`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"), bm, cn);

            pstmt.executeUpdate();

        } catch (Exception e) {
            LOG.error("save(BestandMissing bm, Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }



    public Bestand getBestand() {
        return bestand;
    }

    public void setBestand(Bestand bestand) {
        this.bestand = bestand;
    }

    public String getStartvolume() {
        return startvolume;
    }

    public void setStartvolume(String startvolume) {
        this.startvolume = startvolume;
    }

    public String getStartissue() {
        return startissue;
    }

    public void setStartissue(String startissue) {
        this.startissue = startissue;
    }

    public String getStartyear() {
        return startyear;
    }

    public void setStartyear(String startyear) {
        this.startyear = startyear;
    }

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(String endyear) {
        this.endyear = endyear;
    }

    public String getEndvolume() {
        return endvolume;
    }

    public void setEndvolume(String endvolume) {
        this.endvolume = endvolume;
    }

    public String getEndissue() {
        return endissue;
    }

    public void setEndissue(String endissue) {
        this.endissue = endissue;
    }

    public int getSuppl() {
        return suppl;
    }

    public void setSuppl(int suppl) {
        this.suppl = suppl;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }



}


