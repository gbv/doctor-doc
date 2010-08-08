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
import java.util.ArrayList;
import java.util.List;

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 * @author Pascal Steiner
 */
public class Text extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(Text.class);

  private Konto konto;
  private Texttyp texttyp;
  private String inhalt;
  
  
  public Text() { }

  /**
   * Erstellt einen Text anhand seines Inhaltes
   * @param cn
   * @param inhalt
   */
  public Text (Connection cn, String inhalt){
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `inhalt`=?");
          pstmt.setString(1, inhalt);
          rs = pstmt.executeQuery();
          while (rs.next()) {
              this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"), cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
              this.setInhalt(rs.getString("inhalt"));
          }
          
      } catch (Exception e) {
    	  log.error("Text(Connection cn, String inhalt): " + e.toString());
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
   * Erstellt einen Text anhand eines Texttyps und dem Inhalt
   * 
   * @param cn Connection
   * @param typ Texttyp
   * @param inhalt String
   */
  public Text(Connection cn, Texttyp typ, String inhalt){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TYID`=? AND `inhalt`=?");
          pstmt.setLong(1, typ.getId());
          pstmt.setString(2, inhalt);
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }
       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Texttyp typ, String inhalt): " + e.toString());
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
   * Gets a Text from a Texttype, a KID and the content
   * 
   * @param cn Connection
   * @param Texttyp typ
   * @param Long KID
   * @param inhalt String
   */
  public Text(Connection cn, Texttyp typ, Long kid, String inhalt){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TYID`=? AND `KID`=? AND `inhalt`=?");
          pstmt.setLong(1, typ.getId());
          pstmt.setLong(2, kid);
          pstmt.setString(3, inhalt);
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }
       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Texttyp typ, Long kid, String inhalt): " + e.toString());
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
   * Erstellt einen Text anhand eines Texttyps und einer KID
   * 
   * @param cn Connection
   * @param typ Texttyp
   * @param kid long
   */
  public Text(Connection cn, Texttyp typ, long kid){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TYID`=? AND `KID`=?");
          pstmt.setLong(1, typ.getId());
          pstmt.setLong(2, kid);
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }
       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Texttyp typ, long kid): " + e.toString());
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
   * Erstellt einen Text anhand eines Texttyp-ID und dem Inhalt
   * 
   * @param cn Connection
   * @param tyid Long
   * @param inhalt String
   */
  public Text(Connection cn, Long tyid, String inhalt){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TYID`=? AND `inhalt`=?");
          pstmt.setString(1, tyid.toString());
          pstmt.setString(2, inhalt);
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Long tyid, String inhalt): " + e.toString());
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
   * Liefert eine Liste aller Texte anhand eines Texttypes <p></p>
   * 
   * @param typ Texttyp
   * @param cn
   * @return List mit Textobjekten
   */
  public List<Text> getText(Texttyp typ, Connection cn){
  	ArrayList<Text> tl = new ArrayList<Text>();
  	
  	PreparedStatement pstmt = null;
  	ResultSet rs = null;
  	try {
          pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? ORDER BY inhalt");
          pstmt.setLong(1, typ.getId());
          rs = pstmt.executeQuery();

          while (rs.next()) {
              Text text = new Text(cn, rs);
              tl.add(text);
          }
          
      } catch (Exception e) {
    	  log.error("getText(Texttyp typ, Connection cn): " + e.toString());
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
  	return tl;
  }
  
  /**
   * Erstellt ein Text aus einem ResultSet
   * 
   * @param cn Connection
   * @param rs ResultSet
   */
  public Text (Connection cn, ResultSet rs){
	  
	  try {
		this.setId(rs.getLong("TID"));
		this.setInhalt(rs.getString("inhalt"));
	    this.setKonto(new Konto(rs.getLong("KID"), cn));
	    this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
	} catch (SQLException e) {
		log.error("Text(Connection cn, ResultSet rs): " + e.toString());
	}      
  }
  
  /**
   * Erstellt einen Text anhand seiner ID
   * 
   * @param cn Connection
   * @param id Long
   */
  public Text(Connection cn, Long id){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TID`=?");
          pstmt.setString(1, id.toString());
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Long id): " + e.toString());
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
   * Erstellt einen Text anhand seiner ID und einer KID
   * 
   * @param cn Connection
   * @param id Long
   * @param kid Long
   */
  public Text(Connection cn, Long id, Long kid){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TID`=? AND `KID`=?");
          pstmt.setString(1, id.toString());
          pstmt.setString(2, kid.toString());
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Long id, Long kid): " + e.toString());
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
   * Erstellt einen Text anhand seiner ID, einer KID und einer TYID
   * 
   * @param cn Connection
   * @param id Long
   * @param kid Long
   * @param tyid Long
   */
  public Text(Connection cn, Long id, Long kid, Long tyid){
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `TID`=? AND `KID`=? AND `TYID`=?");
          pstmt.setString(1, id.toString());
          pstmt.setString(2, kid.toString());
          pstmt.setString(3, tyid.toString());
          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  this.setId(rs.getLong("TID"));
              this.setKonto(new Konto(rs.getLong("KID"),cn));
              this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              this.setInhalt(rs.getString("inhalt"));
          }       
      } catch (Exception e) {
    	  log.error("Text(Connection cn, Long id, Long kid, Long tyid): " + e.toString());
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
   * Speichert einen neuen Text
   * 
   * @param cn Connection
   * @param t Text
   */
  public void saveNewText(Connection cn, Text t){
      PreparedStatement pstmt = null;
	  try {
          pstmt = cn.prepareStatement( "INSERT INTO `text` (`KID` , `TYID`, `inhalt`) VALUES (?, ?, ?)");
          pstmt.setLong(1, t.getKonto().getId());
          pstmt.setLong(2, t.getTexttyp().getId());
          pstmt.setString(3, t.getInhalt());
          
          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("saveNewText(Connection cn, Text t): " + e.toString());
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
   * prüft, ob es IP-Einträge unter einem Konto gibt
   * 
   * @param cn Connection
   * @param k Konto
   */
  public boolean hasIP(Connection cn, Konto k){
	  boolean check = false;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE `KID`=? AND `TYID`=?");
          pstmt.setLong(1, k.getId());
          pstmt.setString(2, "9");
          
          rs = pstmt.executeQuery();
          
          if (rs.next()) {
        	  check = true;
          }
          
      } catch (Exception e) {
    	  log.error("hasIP(Connection cn, Konto k): " + e.toString());
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
      return check;
  }
  
  /**
   * holt alle Texte mit Bereichen oder Wildcards anhand der ersten beiden Oktetten einer IP
   * 
   * @param ip String 
   * @param cn Connection
   * @return list ArrayList<Text>
   */
  public ArrayList<Text> possibleIPRanges(String ip, Connection cn){
	  ArrayList<Text> list = new ArrayList<Text>();
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
      try {    	  
    	  String ip_part = ip.substring(0, ip.indexOf(".", ip.indexOf(".")+1)+1);    	  
          pstmt = cn.prepareStatement( "SELECT * FROM `text` WHERE  `inhalt` LIKE ? AND `TYID`=? AND (`inhalt` LIKE '%.*%' OR `inhalt` LIKE '%-%')");
          pstmt.setString(1, ip_part + "%");
          pstmt.setString(2, "9");
          
          rs = pstmt.executeQuery();
          
          while (rs.next()) {
        	  Text txt = new Text();
        	  txt.setId(rs.getLong("TID"));
              txt.setKonto(new Konto(rs.getLong("KID"),cn));
              txt.setTexttyp(new Texttyp(rs.getLong("TYID"), cn ));
              txt.setInhalt(rs.getString("inhalt"));
              list.add(txt);
          }
          
      } catch (Exception e) {
    	  log.error("possibleIPRanges(Connection cn, String ip): " + e.toString());
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
  
  /**
   * Verändert einen Text
   * 
   * @param cn Connection
   * @param t Text
   */
  public void updateText(Connection cn, Text t){
      PreparedStatement pstmt = null;
	  try {
          pstmt = cn.prepareStatement( "UPDATE `text` SET `KID` = ?, `TYID` = ?, `inhalt` = ? WHERE `TID` = ?");
          pstmt.setLong(1, t.getKonto().getId());
          pstmt.setLong(2, t.getTexttyp().getId());
          pstmt.setString(3, t.getInhalt());
          pstmt.setLong(4, t.getId());
          
          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("updateText(Connection cn, Text t): " + e.toString());
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
   * Löscht einen Text
   * 
   * @param cn Connection
   * @param t Text
   */
  public void deleteText(Connection cn, Text t){
      PreparedStatement pstmt = null;
	  try {
          pstmt = cn.prepareStatement( "DELETE FROM `text` WHERE `TID` = ?");
          pstmt.setLong(1, t.getId());
          
          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  log.error("deleteText(Connection cn, Text t)" + e.toString());
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
   * Liefert alle Texte eines Kontos eines bestimmten Texttypes
   * 
   * @return List mit Text
   */
  public List<Text> getAllKontoText(Texttyp t, Long kid, Connection cn){
      ArrayList<Text> sl = new ArrayList<Text>();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND KID=? ORDER BY inhalt");
          pstmt.setLong(1, t.getId());
          pstmt.setString(2, kid.toString());
          rs = pstmt.executeQuery();

          while (rs.next()) {
              Text text = new Text(cn, rs);
              sl.add(text);
          }

      } catch (Exception e) {
    	  log.error("etAllKontoText(Texttyp t, Long kid, Connection cn): " + e.toString());
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
      
      return sl;  
  }
  
  /**
   * Liefert generell alle Texte anhand eines Texttypes plus alle kontospezifischen Texte dieses Texttypes
   * @return ArrayList<Text> mit Stati-Texten
   */
  public List<Text> getAllTextPlusKontoTexts(Texttyp t, Long kid, Connection cn){
      ArrayList<Text> sl = new ArrayList<Text>();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND (KID IS null OR KID=?) ORDER BY inhalt");
          pstmt.setLong(1, t.getId());
          pstmt.setString(2, kid.toString());
          rs = pstmt.executeQuery();

          while (rs.next()) {
              Text text = new Text(cn, rs);
              sl.add(text);
          }

      } catch (Exception e) {
    	  log.error("getAllTextPlusKontoTexts(Texttyp t, Long kid, Connection cn): " + e.toString());
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
      
      return sl;  
  }

public Texttyp getTexttyp() {
    return texttyp;
}

public void setTexttyp(Texttyp texttyp) {
    this.texttyp = texttyp;
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
