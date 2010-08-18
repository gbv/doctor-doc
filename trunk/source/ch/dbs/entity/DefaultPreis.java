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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.grlea.log.SimpleLogger;

import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 * @author Markus Fischer
 */
public class DefaultPreis extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(DefaultPreis.class);

  private Long tid_waehrung;
  private Long lid;
  private String bezeichnung;
  private String waehrung;
  private String deloptions;
  private Long kid;
  private BigDecimal preis = new BigDecimal("0.00");
  private String vorkomma;
  private String nachkomma;
  private Lieferanten l = null;
  private Text waehrungstext = null;
  
  
  public DefaultPreis() { }
  
  public DefaultPreis(OrderForm of, UserInfo ui){
	  Lieferanten lieferantenInstance = new Lieferanten();
      this.l = lieferantenInstance.getLieferantFromLid(of.getLid(), this.getConnection());
	  this.setLid(l.getLid());
	  this.setBezeichnung(l.getName());
	  this.waehrungstext = new Text(this.getConnection(), of.getWaehrung());
	  this.setTid_waehrung(waehrungstext.getId());
	  this.setWaehrung(waehrungstext.getInhalt());
	  this.setDeloptions(of.getDeloptions());
	  this.setKid(ui.getKonto().getId());
	  if (of.getKaufpreis() != null) this.setPreis(of.getKaufpreis());
	  this.close();
  }
  
  /**
   * Falls Defaultpreis bereits besteht wird dieser veraendert, ansonsten wird ein neuer erstellt
   */
  public void saveOrUpdate(Connection cn){
	  
	  this.setId(getDefaultPreisID(l.getLid(), kid, this.getConnection()));
	  
	  if (this.getId() != null) { // hier wird ein bestehender Default-Preis geupdated      
			this.update(this.getConnection());
		} else { // hier wird ein neuer Default-Preis geschrieben
			this.save(this.getConnection());
		}
  }
  
  /**
   * Speichert ein neues Objekt
   * @param cn
   */
  public void save(Connection cn){
	  
	  PreparedStatement pstmt = null;	  
	  try {
          pstmt = cn.prepareStatement("INSERT INTO `default_preise` (`lid`, `bezeichnung`, `TID_waehrung`, `waehrung`, `deloptions`, `KID`, `preis`) VALUES (?, ?, ?, ?, ?, ?, ?)");
          pstmt.setLong(1, l.getLid());
          pstmt.setString(2, l.getName());
          pstmt.setLong(3, tid_waehrung);
          pstmt.setString(4, waehrung);
          pstmt.setString(5, deloptions);
          pstmt.setLong(6, kid);
          pstmt.setBigDecimal(7, preis);
          
          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("save(): " + e.toString() + "\012" + pstmt.toString());
      } 
      finally{
    	  try{
              pstmt.close();
    	  } catch (SQLException e) {
    		  log.error("save(): " + e.toString());
    	  }
      }
	  
  }
  
  /**
   * Veraendert bestehender DefaultPreis
   * 
   * @param cn
   */
  public void update(Connection cn){
	  
	  PreparedStatement pstmt = null;	  
	  try {
          pstmt = cn.prepareStatement("UPDATE `default_preise` SET `lid`=?, `bezeichnung`=?, " + 
          										"`TID_waehrung`=?, `waehrung`=?, `deloptions`=?, `KID`=?, `preis`=? WHERE `DPID`=?");
          pstmt.setLong(1, l.getLid());
          pstmt.setString(2, l.getName());
          pstmt.setLong(3, waehrungstext.getId());
          pstmt.setString(4, waehrungstext.getInhalt());
          pstmt.setString(5, deloptions);
          pstmt.setLong(6, kid);
          pstmt.setBigDecimal(7, preis);
          pstmt.setLong(8, getId());

          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("update(): " + e.toString() + "\012" + pstmt.toString());
      } finally{
    	  try{
              pstmt.close();
    	  } catch (SQLException e) {
    		  log.error("update(): " + e.toString());
    	  }
      }
  }
  
  /**
   * Liefert die DPID eines Eintrages in der Tabelle default_preise anhand der TID_bezeichnung und der KID
   * 
   * @param Text lieferant, Long kid
   */
  private Long getDefaultPreisID(Long lid, Long kid, Connection cn){
      Long dpid = null;
              
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT DPID FROM `default_preise` WHERE `lid`=? AND `KID`=?");
          pstmt.setString(1, lid.toString());
          pstmt.setString(2, kid.toString());

          rs = pstmt.executeQuery();
          
          while (rs.next()) {
              dpid = rs.getLong("DPID"); 
           }
          
      } catch (Exception e) {
    	  log.error("getDefaultPreisID(Long lid, Long kid, Connection cn): " + e.toString());
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
      return dpid;
  }
  
  /**
   * Liefert den Defaultpreis eines Lieferanten anhand der KID und des Strings des Lieferanten
   * 
   * @param String lieferant Long kid
   */
  public DefaultPreis getDefaultPreis(String lieferant, Long kid, Connection cn){
      DefaultPreis dp = new DefaultPreis();
              
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT * FROM `default_preise` WHERE `KID`=? AND `bezeichnung`=?");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, lieferant);

          rs = pstmt.executeQuery();
          
          while (rs.next()) {
          	dp = getDefaultPreis(rs);
          	OrderForm pageForm = new OrderForm();
          	pageForm.setKaufpreis(dp.getPreis());
          	pageForm = pageForm.bigDecimalToString(pageForm);
          	dp.setVorkomma(pageForm.getPreisvorkomma());
          	dp.setNachkomma(pageForm.getPreisnachkomma());
           }
          
      } catch (Exception e) {
    	  log.error("getDefaultPreis(String lieferant, Long kid, Connection cn)" + e.toString());
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
      return dp;
  }
  
  /**
   * Liefert alle Einträge eines Kontos aus der Tabelle default_preise anhand der KID
   * 
   * @param Long kid
   */
  public List<DefaultPreis> getAllKontoDefaultPreise(Long kid, Connection cn){
      ArrayList<DefaultPreis> list = new ArrayList<DefaultPreis>();
      DefaultPreis dp;
              
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT * FROM `default_preise` WHERE `KID`=?");
          pstmt.setString(1, kid.toString());

          rs = pstmt.executeQuery();
          
          while (rs.next()) {
          	dp = getDefaultPreis(rs);
          	OrderForm pageForm = new OrderForm();
          	pageForm.setKaufpreis(dp.getPreis());
          	pageForm = pageForm.bigDecimalToString(pageForm);
          	dp.setVorkomma(pageForm.getPreisvorkomma());
          	dp.setNachkomma(pageForm.getPreisnachkomma());
              list.add(dp);
           }
          
      } catch (Exception e) {
    	  log.error("getAllKontoDefaultPreise(): " + e.toString());
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
  
  /*
   * Füllt ein Defaultpreis-Objekt mit einer Zeile aus der Datenbank
   */
  private DefaultPreis getDefaultPreis(ResultSet rs) throws Exception{
      DefaultPreis dp = new DefaultPreis();
      dp.setId(rs.getLong("DPID"));
      dp.setLid(rs.getLong("lid"));
      dp.setBezeichnung(rs.getString("bezeichnung"));
      dp.setTid_waehrung(rs.getLong("TID_waehrung"));
      dp.setWaehrung(rs.getString("waehrung"));
      dp.setDeloptions(rs.getString("deloptions"));
      dp.setKid(rs.getLong("KID"));
      dp.setPreis(rs.getBigDecimal("preis"));

      return dp;
  }


public String getBezeichnung() {
	return bezeichnung;
}


public void setBezeichnung(String bezeichnung) {
	this.bezeichnung = bezeichnung;
}


public String getDeloptions() {
	return deloptions;
}


public void setDeloptions(String deloptions) {
	this.deloptions = deloptions;
}


public Long getKid() {
	return kid;
}


public void setKid(Long kid) {
	this.kid = kid;
}

public Long getLid() {
	return lid;
}

public void setLid(Long lid) {
	this.lid = lid;
}

public Long getTid_waehrung() {
	return tid_waehrung;
}


public void setTid_waehrung(Long tid_waehrung) {
	this.tid_waehrung = tid_waehrung;
}


public String getWaehrung() {
	return waehrung;
}


public void setWaehrung(String waehrung) {
	this.waehrung = waehrung;
}

public BigDecimal getPreis() {
	return preis;
}

public void setPreis(BigDecimal preis) {
	this.preis = preis;
}

public String getNachkomma() {
	return nachkomma;
}


public void setNachkomma(String nachkomma) {
	this.nachkomma = nachkomma;
}


public String getVorkomma() {
	return vorkomma;
}


public void setVorkomma(String vorkomma) {
	this.vorkomma = vorkomma;
}

public Lieferanten getL() {
	return l;
}

public void setL(Lieferanten l) {
	this.l = l;
}

public Text getWaehrungstext() {
	return waehrungstext;
}

public void setWaehrungstext(Text waehrungstext) {
	this.waehrungstext = waehrungstext;
}

 
}
