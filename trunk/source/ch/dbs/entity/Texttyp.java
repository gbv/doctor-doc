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
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Pascal Steiner
 */
public class Texttyp extends AbstractIdEntity {

  private static final SimpleLogger LOG = new SimpleLogger(Texttyp.class);

  private Konto konto;
  private String inhalt;


  public Texttyp() { }

  /**
   * Erstellt einen Texttyp anhand seiner ID
   * @param cn
   * @param id
   */
  public Texttyp(Long id, Connection cn) {
      PreparedStatement pstmt = null;
      ResultSet rs = null;
    try {
          pstmt = cn.prepareStatement("SELECT * FROM `texttyp` WHERE `TYID`=?");
          pstmt.setLong(1, id);
          rs = pstmt.executeQuery();
          while (rs.next()) {
              this.setId(rs.getLong("TYID"));
              this.setKonto(new Konto(rs.getLong("KID"), cn));
              this.setInhalt(rs.getString("inhalt"));
          }

      } catch (Exception e) {
        LOG.error("Texttyp(Connection cn, Long id): " + e.toString());
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
   * Erstellt einen Texttyp anhand seines Inhaltes
   * @param cn
   * @param inhalt
   */
  public Texttyp(String inhalt, Connection cn) {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        pstmt = cn.prepareStatement("SELECT * FROM `texttyp` WHERE `inhalt`=?");
        pstmt.setString(1, inhalt);
        rs = pstmt.executeQuery();
        while (rs.next()) {
              this.setId(rs.getLong("TYID"));
              this.setKonto(new Konto(rs.getLong("KID"), cn));
              this.setInhalt(rs.getString("inhalt"));
        }
    } catch (Exception e) {
      LOG.error("Texttyp(Connection cn, Long id): " + e.toString());
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



public String getInhalt() {
    return inhalt;
}

public void setInhalt(String inhalt) {
    this.inhalt = inhalt;
}

public Konto getKonto() {
    return konto;
}

public void setKonto(Konto konto) {
    this.konto = konto;
}


}
