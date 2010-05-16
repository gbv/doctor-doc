//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package ch.dbs.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.grlea.log.SimpleLogger;

import util.DBConn;
import util.ThreadSafeSimpleDateFormat;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 * @author Markus Fischer
 */
public class Fax extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(Fax.class);

  private String kid;
  private String popfaxid;
  private String from;
  private String popfaxdate;
  private String pages;
  private String state;
  private String statedate;
  
  
  public Fax() { }
  
  /**
   * Sucht einen Fax anhand der popfaxid
   * <p></p>
   * @param the popfaxid 
   * @return a {@link Fax}
   */
  public Fax getFax(String popfaxid, String kid){
      DBConn cn = new DBConn();
      Fax f = new Fax();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.getConnection().prepareStatement("SELECT * FROM fax WHERE popfaxid = ? AND KID = ?");
          pstmt.setString(1, popfaxid);
          pstmt.setString(2, kid);
          rs = pstmt.executeQuery();
          
          while (rs.next()) {
              f = getFax(rs);
          }

      } catch (Exception e) {
    	  log.error("getFax(): " + e.toString());
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
    	cn.close();
    }
      
      return f;
  }  
  
  /**
   * Erstellt einen neuen Datenbankeintrag dieses Fax Objecktes
   * @param cn
   */
  public void saveNewFax(){
	  Connection cn = getConnection();
	  saveNewFax(cn);
	  close();
  }
  /**
   * Erstellt einen neuen Datenbankeintrag dieses Fax Objecktes
   * @param cn
   */
  private void saveNewFax(Connection cn){
	  PreparedStatement pstmt = null;
	  try {
          pstmt = setFaxValues(cn.prepareStatement( "INSERT INTO `fax` (`KID` , " +
                  "`popfaxid` , `fromnr` , `popfaxdate` , `pages` , `state` , `statedate`) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?)"), this);
          
          Date d = new Date(); // aktuelles Datum setzen
          ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String datum = fmt.format(d);
          pstmt.setString(7, datum);

          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("saveNewFax(Connection cn): " + e.toString());
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
  
  /**
   * Speichert Angaben zu den Verbindungsversuchen zum Faxserver in der Datenbank
   * 
   * @param Fax f 
   */
  public void saveFaxRunStati(Fax f){
      DBConn cn = new DBConn();      
      PreparedStatement pstmt = null;
      try {
      	pstmt = cn.getConnection().prepareStatement( "INSERT INTO `faxrun` (`KID` , `state`, `statedate`) VALUES (?, ?, ?)");
      	pstmt.setString(1, f.getKid());
      	pstmt.setString(2, f.getState());
      	
      	Date d = new Date();
          ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String datum = sdf.format(d);

          pstmt.setString(3, datum);

          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("saveFaxRunStati(): " + e.toString());
      } finally {
    	if (pstmt != null) {
    		try {
    			pstmt.close();
    		} catch (SQLException e) {
    			System.out.println(e);
    		}
    	}
    	cn.close();
    }
  }
  
  /**
   * Löscht alle Einträge in der DB der entsprechenden popfaxid.
   * 
   * @param cn
   * @param popfaxid
   * @return rowsdeleted Anzahl gelöschter Einträge (Zeilen)
   */
  public int deletePopfaxId(Connection cn, String popfaxid){
	  
	  int rowsdeleted = 0;
	  PreparedStatement pstmt = null;
	  try {
          pstmt = cn.prepareStatement("DELETE FROM fax WHERE popfaxid = ?");
          pstmt.setString(1, popfaxid);

          rowsdeleted = pstmt.executeUpdate();

      } catch (Exception e) {
    	  log.error("deletePopfaxId(Connection cn, String popfaxid): " + e.toString());
      } finally {
    	if (pstmt != null) {
    		try {
    			pstmt.close();
    		} catch (SQLException e) {
    			System.out.println(e);
    		}
    	}
    }
      
      return rowsdeleted;	  
  }
  
  /*
   * Füllt ein Faxobjekt mit einer Zeile aus der Datenbank
   */
  private Fax getFax(ResultSet rs) throws Exception{
      Fax f = new Fax();
      f.setId(Long.getLong("", rs.getLong("FID")));
      f.setKid(rs.getString("KID"));
      f.setPopfaxid(rs.getString("popfaxid"));
      f.setFrom(rs.getString("fromnr"));
      f.setPopfaxdate(rs.getString("popfaxdate"));
      f.setPages(rs.getString("pages"));
      f.setState(rs.getString("state"));
      f.setStatedate(rs.getString("statedate"));

      return f;
  }


public String getFrom() {
	return from;
}


public void setFrom(String from) {
	this.from = from;
}


public String getKid() {
	return kid;
}


public void setKid(String kid) {
	this.kid = kid;
}

public String getPages() {
	return pages;
}


public void setPages(String pages) {
	this.pages = pages;
}


public String getPopfaxdate() {
	return popfaxdate;
}


public void setPopfaxdate(String popfaxdate) {
	this.popfaxdate = popfaxdate;
}


public String getPopfaxid() {
	return popfaxid;
}


public void setPopfaxid(String popfaxid) {
	this.popfaxid = popfaxid;
}


public String getState() {
	return state;
}


public void setState(String state) {
	this.state = state;
}


public String getStatedate() {
	return statedate;
}


public void setStatedate(String statedate) {
	this.statedate = statedate;
}

/*
 * Setzt die Werte im Preparestatement der Methode saveNewFax()
 */
private PreparedStatement setFaxValues(PreparedStatement pstmt, Fax f) throws Exception{
	
    
    if (f.getKid()!=null){pstmt.setString(1, f.getKid());} else {pstmt.setString(1, "");}
    if (f.getPopfaxid()!=null){pstmt.setString(2, f.getPopfaxid());} else {pstmt.setString(2, "");}
    if (f.getFrom()!=null){pstmt.setString(3, f.getFrom());} else {pstmt.setString(3, "");}
    if (f.getPopfaxdate()!=null){pstmt.setString(4, f.getPopfaxdate());} else {pstmt.setString(4, "");}
    if (f.getPages()!=null){pstmt.setString(5, f.getPages());} else {pstmt.setString(5, "");}
    if (f.getState()!=null){pstmt.setString(6, f.getState());} else {pstmt.setString(6, "");}
    if (f.getStatedate()!=null){pstmt.setString(7, f.getStatedate());} else {pstmt.setString(7, "");}
    
    return pstmt;
}
 
}
