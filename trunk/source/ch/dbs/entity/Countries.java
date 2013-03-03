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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ReadSystemConfigurations;

/**
 * Abstract base class for entities having a {@link Long} unique identifier,
 * this provides the base functionality for them. <p></p>
 * 
 * @author Markus Fischer
 */
public class Countries extends AbstractIdEntity {
    
    private static final Logger LOG = LoggerFactory.getLogger(Countries.class);
    // defines which locale of iso_countries shall be used
    // you may define your own set of localized countries in the MySQL table
    private static final String LOCALE = ReadSystemConfigurations.getLocale();
    
    private String rowid;
    private String countryid;
    private String locale;
    private String countrycode;
    private String countryname;
    private String phoneprefix;
    
    public Countries() {
        
    }
    
    /**
     * Gets all Countries for the given locale specified in
     * SystemConfiguartion.properties <p></p>
     * 
     * @return a {@link Countries}
     */
    public List<Countries> getAllCountries(final Connection cn) {
        
        final ArrayList<Countries> cl = new ArrayList<Countries>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `iso_countries` WHERE `locale` =? "
                    + "ORDER BY `countryName` ASC");
            pstmt.setString(1, LOCALE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cl.add(getCountries(rs));
            }
            
        } catch (final Exception e) {
            LOG.error("getAllActivatedCountries: " + e.toString());
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
        return cl;
        
    }
    
    /**
     * Gets a Country from a countryCode and for the given locale specified in
     * SystemConfiguartion.properties <p></p>
     * 
     * @return a {@link Countries}
     */
    public Countries(final String code, final Connection cn) {
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `iso_countries` WHERE `locale` =? "
                    + "AND `countryCode` =? ORDER BY `countryName` ASC");
            pstmt.setString(1, LOCALE);
            pstmt.setString(2, code);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setRsValues(rs);
            }
            
        } catch (final Exception e) {
            LOG.error("Countries getCountry: " + e.toString());
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
    
    /*
     * Fills a Countries object with a row of the database
     */
    private Countries getCountries(final ResultSet rs) throws Exception {
        final Countries country = new Countries();
        country.setRowid(rs.getString("rowId"));
        country.setCountryid(rs.getString("countryId"));
        country.setLocale(rs.getString("locale"));
        country.setCountrycode(rs.getString("countryCode"));
        country.setCountryname(rs.getString("countryName"));
        country.setPhoneprefix(rs.getString("phonePrefix"));
        
        return country;
    }
    
    /*
     * Fills a Countries object with a row of the database
     */
    private void setRsValues(final ResultSet rs) throws Exception {
        this.setRowid(rs.getString("rowId"));
        this.setCountryid(rs.getString("countryId"));
        this.setLocale(rs.getString("locale"));
        this.setCountrycode(rs.getString("countryCode"));
        this.setCountryname(rs.getString("countryName"));
        this.setPhoneprefix(rs.getString("phonePrefix"));
    }
    
    public String getCountrycode() {
        return countrycode;
    }
    
    public void setCountrycode(final String countrycode) {
        this.countrycode = countrycode;
    }
    
    public String getCountryid() {
        return countryid;
    }
    
    public void setCountryid(final String countryid) {
        this.countryid = countryid;
    }
    
    public String getCountryname() {
        return countryname;
    }
    
    public void setCountryname(final String countryname) {
        this.countryname = countryname;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(final String locale) {
        this.locale = locale;
    }
    
    public String getPhoneprefix() {
        return phoneprefix;
    }
    
    public void setPhoneprefix(final String phoneprefix) {
        this.phoneprefix = phoneprefix;
    }
    
    public String getRowid() {
        return rowid;
    }
    
    public void setRowid(final String rowid) {
        this.rowid = rowid;
    }
    
}
