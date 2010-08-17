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

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 * @author Markus Fischer
 */
public class Holding extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(Holding.class);
	
	private Konto konto;
	private Long kid;
	private String titel = "";
	private String coden;
	private String verlag = "";
	private String ort = "";
	private String issn;
	private String zdbid;
	

  public Holding() {
	  this.setKonto(new Konto());
  }
	
  public Holding(Konto k) {
      this.setKonto(k);
  }
  
  /**
   * Erstellt ein Holding aus einem ResultSet
   * 
   * @param cn Connection
   * @param rs ResultSet
   */
  public Holding (Connection cn, ResultSet rs){
	  
	  try {
		this.setId(rs.getLong("HOID"));
	    this.setKid(rs.getLong("KID"));
	    this.setKonto(new Konto(this.getKid(), cn));
	    this.setTitel(rs.getString("titel"));
	    this.setCoden(rs.getString("coden"));
	    this.setVerlag(rs.getString("verlag"));
	    this.setOrt(rs.getString("ort"));
	    this.setIssn(rs.getString("issn"));
	    this.setZdbid(rs.getString("zdbid"));
	} catch (SQLException e) {
		log.error("Holding(Connection cn, ResultSet rs: " + e.toString());
	}
  }
  
  private void setRsValues(Connection cn, ResultSet rs) throws Exception{
	  	this.setId(rs.getLong("HOID"));
	  	this.setKid(rs.getLong("KID"));
	  	this.setKonto(new Konto(this.getKid(), cn));
	  	this.setTitel(rs.getString("titel"));
	    this.setCoden(rs.getString("coden"));
	    this.setVerlag(rs.getString("verlag"));
	    this.setOrt(rs.getString("ort"));
	    this.setIssn(rs.getString("issn"));
	    this.setZdbid(rs.getString("zdbid"));	  
  }
  
  private Holding setRsstValues(Connection cn, ResultSet rs) throws Exception{
	  Holding ho = new Holding();
	  	ho.setId(rs.getLong("HOID"));
	  	ho.setKid(rs.getLong("KID"));
	  	ho.setKonto(new Konto(ho.getKid(), cn));
	  	ho.setTitel(rs.getString("titel"));
	    ho.setCoden(rs.getString("coden"));
	    ho.setVerlag(rs.getString("verlag"));
	    ho.setOrt(rs.getString("ort"));
	    ho.setIssn(rs.getString("issn"));
	    ho.setZdbid(rs.getString("zdbid"));
	   return ho;
}
  
  /**
   * Erstellt ein Holding anhand einer Verbindung und der ID
   * 
   * @param Long hoid
   * @param Connection cn
   * @return Holding holding
   */
  public Holding (Long hoid, Connection cn){

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE HOID = ?");
          pstmt.setLong(1, hoid);
          rs = pstmt.executeQuery();

          while (rs.next()) {
             this.setRsValues(cn, rs);
          }

      } catch (Exception e) {
    	  log.error("Holding (Long hoid, Connection cn): " + e.toString());
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
   * Holt ein Holding eines Kontos anhand einer Verbindung und dem Identifier (ISSN oder ZDB-ID)
   * 
   * @param Long kid
   * @param String identwert
   * @param Connection cn
   * @return Holding holding
   */
  public Holding getHolding (Long kid, String identifier, Connection cn){
	  
	  Holding ho = new Holding();

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE KID = ? AND (issn = ? OR coden = ? OR zdbid = ?)");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, identifier);
          pstmt.setString(3, identifier);
          pstmt.setString(4, identifier);
          rs = pstmt.executeQuery();

          while (rs.next()) {
             ho = setRsstValues(cn, rs);
          }

      } catch (Exception e) {
    	  log.error("getHolding (Long kid, String identifier, Connection cn): " + e.toString());
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
      
      return ho;
  }
  
  /**
   * Gets a Holding from an existing Holding, ignoring the ID. This is useful to reduce the number of
   * identical Holdings for an account.
   * 
   * @param Holding h
   * @param Connection cn
   * @return Holding holding
   */
  public Holding (Holding h, Connection cn){

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  
	  StringBuffer sql = new StringBuffer();
	  sql.append("SELECT * FROM holdings WHERE KID = ? AND titel = ? AND coden ");
	  if (h.getCoden()==null||h.getCoden().equals("")) {sql.append("IS ? ");} else {sql.append("= ? ");}
	  sql.append("AND verlag = ? AND ort = ? AND issn = ? AND zdbid ");
	  if (h.getZdbid()==null||h.getZdbid().equals("")) {sql.append("IS ?");} else {sql.append("= ?");}
	  
	  try {
          pstmt = cn.prepareStatement(sql.toString());
          pstmt.setLong(1, h.getKid());
          pstmt.setString(2, h.getTitel());
          if (h.getCoden()==null || h.getCoden().equals("")) {pstmt.setString(3, null);} else {pstmt.setString(3, h.getCoden());}
          pstmt.setString(4, h.getVerlag());
          pstmt.setString(5, h.getOrt());
          pstmt.setString(6, h.getIssn());
          if (h.getZdbid()==null || h.getZdbid().equals("")) {pstmt.setString(7, null);} else {pstmt.setString(7, h.getZdbid());}
          rs = pstmt.executeQuery();

          if (rs.next()) { // just take the first one
             setRsValues(cn, rs);
          }

      } catch (Exception e) {
    	  log.error("Holding (Holding h, Connection cn): " + e.toString());
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
   * Holt alle Holdings anhand einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID) und einer Verbindung 
   * 
   * @param ArrayList<String> identifier
   * @param Connection cn
   * @return ArrayList<Holding> holdings
   */
  public ArrayList<Holding> getAllHoldings (ArrayList<String> identifier, Connection cn){
	  
	  ArrayList<Holding> list = new ArrayList<Holding>();
	  
	  if (identifier.size()>0) {
	  
	  StringBuffer sqlQuery = new StringBuffer("SELECT * FROM holdings WHERE issn = ? OR coden = ? OR zdbid = ?");
	  
	  for (int i=1;i<identifier.size();i++) { // nur ausführen falls length > 1
		  sqlQuery.append(" OR issn = ? OR coden = ? OR zdbid = ?");
	  }

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement(sqlQuery.toString());
          int pos = 1;
          for (String ident : identifier) {
        	  pstmt.setString(pos, ident);
              pstmt.setString(pos+1, ident);
              pstmt.setString(pos+2, ident);
              pos = pos+3;
    	  }

          rs = pstmt.executeQuery();

          while (rs.next()) {
        	  Holding ho = new Holding();
        	  ho = setRsstValues(cn, rs);
        	  list.add(ho);
          }

      } catch (Exception e) {
    	  log.error("getAllHoldings (ArrayList<String> identifier, Connection cn): " + e.toString());
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
      
      return list;
  }
  
  /**
   * Gets all holdings for a given library from its KID and a connection
   * 
   * @param Long kid
   * @param Connection cn
   * @return ArrayList<Holding> holdings
   */
  public ArrayList<Holding> getAllHoldingsForKonto (Long kid, Connection cn){
	  
	  ArrayList<Holding> list = new ArrayList<Holding>();
	  
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE KID = ?");
          pstmt.setLong(1, kid);

          rs = pstmt.executeQuery();

          while (rs.next()) {
        	  Holding ho = setRsstValues(cn, rs);
        	  list.add(ho);
          }

      } catch (Exception e) {
    	  log.error("getAllHoldingsForKonto (Long kid, Connection cn): " + e.toString());
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
   * Gets all holdings for a given library from its KID, a list of all related 
   * identifieres (ISSN, Coden or ZDB-ID) and a connection
   * 
   * @param ArrayList<String> identifier
   * @param Long kid
   * @param Connection cn
   * @return ArrayList<Holding> holdings
   */
  public ArrayList<Holding> getAllHoldingsForKonto (ArrayList<String> identifier, Long kid, Connection cn){
	  
	  ArrayList<Holding> list = new ArrayList<Holding>();
	  
	  if (identifier.size()>0) {
	  
	  StringBuffer sqlQuery = new StringBuffer("SELECT * FROM holdings WHERE KID = ? AND (issn = ? OR coden = ? OR zdbid = ?");
	  
	  for (int i=1;i<identifier.size();i++) { // nur ausführen falls length > 1
		  sqlQuery.append(" OR issn = ? OR coden = ? OR zdbid = ?");
	  }
	  
	  sqlQuery.append(")");

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement(sqlQuery.toString());
          	  pstmt.setLong(1, kid);
          int pos = 2;
          for (String ident : identifier) {
        	  pstmt.setString(pos, ident);
              pstmt.setString(pos+1, ident);
              pstmt.setString(pos+2, ident);
              pos = pos+3;
    	  }

          rs = pstmt.executeQuery();

          while (rs.next()) {
        	  Holding ho = new Holding();
        	  ho = setRsstValues(cn, rs);
        	  list.add(ho);
          }

      } catch (Exception e) {
    	  log.error("getAllHoldingsForKonto (ArrayList<String> identifier, Long kid, Connection cn): " + e.toString());
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
      
      return list;
  }
  
  /**
   * Holt alle HOIDs anhand einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID) und einer Verbindung 
   * 
   * @param ArrayList<String> identifier
   * @param Connection cn
   * @return ArrayList<String> HOIDs
   */
  public ArrayList<String> getAllHOIDs(ArrayList<String> identifier, Connection cn){
	  
	  ArrayList<String> list = new ArrayList<String>();
	  
	  if (identifier.size()>0) {
	  
	  StringBuffer sqlQuery = new StringBuffer("SELECT HOID FROM holdings WHERE issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
	  
	  for (int i=1;i<identifier.size();i++) { // nur ausführen falls length > 1
		  sqlQuery.append(" OR issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
	  }

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement(sqlQuery.toString());
          int pos = 1;
          for (String ident : identifier) {
        	  pstmt.setString(pos, ident);
              pstmt.setString(pos+1, ident);
              pstmt.setString(pos+2, ident);
              pos = pos+3;
    	  }

          rs = pstmt.executeQuery();

          while (rs.next()) {
        	  list.add(rs.getString("HOID"));
          }

      } catch (Exception e) {
    	  log.error("ArrayList<String> getAllHOIDs(ArrayList<String> identifier, Connection cn): " + e.toString());
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
      
      return list;
  }
  
  /**
   * Holt alle HOIDs eines spezifischen Kontos anhand der KID, 
   * einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID) 
   * und einer Verbindung
   * 
   * @param ArrayList<String> identifier
   * @param Long kid
   * @param Connection cn
   * @return ArrayList<String> HOIDs
   */
  public ArrayList<String> getAllHOIDsForKonto(ArrayList<String> identifier, Long kid, Connection cn){
	  
	  ArrayList<String> list = new ArrayList<String>();
	  
	  if (identifier.size()>0) {
	  
	  StringBuffer sqlQuery = new StringBuffer("SELECT HOID FROM holdings WHERE KID LIKE ? AND (issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
	  
	  for (int i=1;i<identifier.size();i++) { // nur ausführen falls length > 1
		  sqlQuery.append(" OR issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
	  }
	  
	  sqlQuery.append(")");

	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement(sqlQuery.toString());
          	  pstmt.setLong(1, kid);
          int pos = 2;
          for (String ident : identifier) {
        	  pstmt.setString(pos, ident);
              pstmt.setString(pos+1, ident);
              pstmt.setString(pos+2, ident);
              pos = pos+3;
    	  }

          rs = pstmt.executeQuery();

          while (rs.next()) {
        	  list.add(rs.getString("HOID"));
          }

      } catch (Exception e) {
    	  log.error("ArrayList<String> getAllHOIDsForKonto(ArrayList<String> identifier, Long kid, Connection cn): " + e.toString());
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
      
      return list;
  }
  
  /**
   * Prüft, ob bei einem Konto bereits ein betreffendes Holding besteht und
   * gibt es zurück. Falls noch kein Holding vorhanden ist wird ein neues erstellt.
   * 
   * @param Long kid
   * @param String ident
   * @param String identwert
   * @param Connection cn
   * @return Holding holding
   */
  public Holding createHolding (Konto k, String identdescrip, String ident, Connection cn){
	  
	  Holding ho = new Holding();

	  try {
		  ho = getHolding(k.getId(), ident, cn);
		  
		  if (ho.getId()==null) {
			  ho.setKid(k.getId());
			  ho.setKonto(k);
			  if (identdescrip.equals("issn")) ho.setIssn(ident);
			  if (identdescrip.equals("zdbid")) ho.setZdbid(ident);
			  if (identdescrip.equals("coden")) ho.setCoden(ident);
		  }  
         
      } catch (Exception e) {
    	  log.error("createHolding (Konto k, String identdescrip, String ident, Connection cn): " + e.toString());
      }
      
      return ho;
  }
  
  /**
   * Speichert ein neues Holding in der Datenbank und gibt es mit der ID zurück
   * 
   * @param Holding h
   * @param Connection cn
   * @return Holding h
   */
  public Holding save(Holding h, Connection cn){
              
      PreparedStatement pstmt = null;
      ResultSet rs = null;
	  try {
          pstmt = cn.prepareStatement( "INSERT INTO `holdings` (`KID` , " +
          "`titel` , `coden` , `verlag` , `ort` , `issn` , `zdbid`) VALUES (?, ?, ?, ?, ?, ?, ?)");
          
          pstmt.setLong(1, h.getKid());
          pstmt.setString(2, h.getTitel());
          pstmt.setString(3, h.getCoden());
          pstmt.setString(4, h.getVerlag());
          pstmt.setString(5, h.getOrt());
          pstmt.setString(6, h.getIssn());
          pstmt.setString(7, h.getZdbid());
          
          pstmt.executeUpdate();
          
       // ID des gerade gespeicherten Holdings ermitteln und im Holding hinterlegen
     		rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
     		if (rs.next()) {
     			h.setId(rs.getLong("LAST_INSERT_ID()"));
     		} else {
     			log.error("Didn't get an ID back at: save(Holding h, Connection cn)!");     			
     		}
          
      } catch (Exception e) {
    	  log.error("Holding save(Holding h, Connection cn)" + e.toString());
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
      return h;
  }
  
  /**
   * Deletes holdings from a given account that do not have any referenced stock.
   * 
   * @param Konto k
   * @param Connection cn 
   */
  public void purgeNotUsedKontoHoldings(Konto k, Connection cn){

      PreparedStatement pstmt = null;
  	try {  
  		  pstmt = cn.prepareStatement( "DELETE a FROM holdings AS a LEFT OUTER JOIN stock AS b ON a.HOID = b.HOID WHERE a.KID=? AND b.STID IS null");	            
          pstmt.setLong(1, k.getId());	            
          pstmt.executeUpdate();
          
      } catch (Exception e) {
      	log.error("purgeNotUsedKontoHoldings(Konto k, Connection cn)" + e.toString());
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


public Konto getKonto() {
	return konto;
}


public void setKonto(Konto konto) {
	this.konto = konto;
}


public Long getKid() {
	return kid;
}

public void setKid(Long kid) {
	this.kid = kid;
}

public String getTitel() {
	return titel;
}

public void setTitel(String titel) {
	this.titel = titel;
}

public String getCoden() {
	return coden;
}

public void setCoden(String coden) {
	this.coden = coden;
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

public String getIssn() {
	return issn;
}


public void setIssn(String issn) {
	this.issn = issn;
}


public String getZdbid() {
	return zdbid;
}


public void setZdbid(String zdbid) {
	this.zdbid = zdbid;
}
  
  


}
