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

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Markus Fischer
 */
public class Countries extends AbstractIdEntity {

  private static final SimpleLogger LOG = new SimpleLogger(Countries.class);

private String rowid;
private String countryid;
private String locale;
private String countrycode;
private String countryname;
private String phoneprefix;


public Countries() {

}

/**
 * Listet alle aktivierten Länder auf (in DB mit locale 'de' versehen)
 * <p></p>
 * @return a {@link Countries}
 */
public List <Countries> getAllActivatedCountries(Connection cn) {

    ArrayList <Countries> cl = new ArrayList<Countries>();
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        pstmt = cn.prepareStatement("SELECT * FROM `iso_countries` WHERE `locale` ='de' ORDER BY `countryName` ASC");
        rs = pstmt.executeQuery();
        while (rs.next()) {
            cl.add(getCountries(rs));
        }

    } catch (Exception e) {
      LOG.error("getAllActivatedCountries: " + e.toString());
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
    return cl;

}

/*
 * Füllt ein Countries-Objekt mit einer Zeile aus der Datenbank
 */
private Countries getCountries(ResultSet rs) throws Exception {
    Countries country = new Countries();
    country.setRowid(rs.getString("rowId"));
    country.setCountryid(rs.getString("countryId"));
    country.setLocale(rs.getString("locale"));
    country.setCountrycode(rs.getString("countryCode"));
    country.setCountryname(rs.getString("countryName"));
    country.setPhoneprefix(rs.getString("phonePrefix"));

    return country;
}


public String getCountrycode() {
  return countrycode;
}
public void setCountrycode(String countrycode) {
  this.countrycode = countrycode;
}
public String getCountryid() {
  return countryid;
}
public void setCountryid(String countryid) {
  this.countryid = countryid;
}
public String getCountryname() {
  return countryname;
}
public void setCountryname(String countryname) {
  this.countryname = countryname;
}
public String getLocale() {
  return locale;
}
public void setLocale(String locale) {
  this.locale = locale;
}
public String getPhoneprefix() {
  return phoneprefix;
}
public void setPhoneprefix(String phoneprefix) {
  this.phoneprefix = phoneprefix;
}
public String getRowid() {
  return rowid;
}
public void setRowid(String rowid) {
  this.rowid = rowid;
}




}
