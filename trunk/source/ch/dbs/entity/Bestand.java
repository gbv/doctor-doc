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

import ch.dbs.form.HoldingForm;
import ch.dbs.form.OrderForm;

/**
 * Grundlegende Klasse um Bestandesangaben abzubilden und in die Datenbank zu schreiben 
 * <p/>
 * @author Markus Fischer
 */

public class Bestand extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(Bestand.class);
	
	private Holding holding;
	private String startyear;
	private String startvolume;
	private String startissue;
	private String endyear;
	private String endvolume;
	private String endissue;
	private int suppl; // 0 = keine Suppl. | 1 = inkl. Suppl. | 2 = nur Suppl.
	private boolean eissue;
	private Text standort;
	private String shelfmark; // Notation, Büchergestellnummer etc.
	private String bemerkungen = "";
	private boolean internal;
	
	
	
	  public Bestand() {
		  this.setHolding(new Holding());
		  this.setStandort(new Text());
	  }
	  
	  public Bestand(HoldingForm hf) {
	    	this.setHolding(hf.getHolding());
	    	this.setStartyear(hf.getStartyear());
	    	this.setStartvolume(hf.getStartvolume());
	    	this.setStartissue(hf.getStartissue());
	    	this.setEndyear(hf.getEndyear());
	    	this.setEndvolume(hf.getEndvolume());
	    	this.setEndissue(hf.getEndissue());
	    	this.setSuppl(hf.getSuppl());
	    	this.setEissue(hf.isEissue());
	    	this.setStandort(hf.getStandort());
	    	this.setShelfmark(hf.getShelfmark());
	    	this.setBemerkungen(hf.getBemerkungen());
	    	this.setInternal(hf.isInternal());
	  }
	  
	  public Bestand(Bestand be) {
	    	this.setHolding(be.getHolding());
	    	this.setStartyear(be.getStartyear());
	    	this.setStartvolume(be.getStartvolume());
	    	this.setStartissue(be.getStartissue());
	    	this.setEndyear(be.getEndyear());
	    	this.setEndvolume(be.getEndvolume());
	    	this.setEndissue(be.getEndissue());
	    	this.setSuppl(be.getSuppl());
	    	this.setEissue(be.isEissue());
	    	this.setStandort(be.getStandort());
	    	this.setShelfmark(be.getShelfmark());
	    	this.setBemerkungen(be.getBemerkungen());
	    	this.setInternal(be.isInternal());
	  }
	  
	  /**
	   * Erstellt einen Bestand anhand einer Verbindung und der ID
	   * 
	   * @param Long stid
	   * @param Connection cn
	   * @return Bestand bestand
	   */
	  public Bestand (Long stid, Connection cn){

		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  try {
	          pstmt = cn.prepareStatement("SELECT * FROM stock WHERE STID = ?");
	          pstmt.setLong(1, stid);
	          rs = pstmt.executeQuery();

	          while (rs.next()) {
	             this.setRsValues(cn, rs);
	          }

	      } catch (Exception e) {
	    	  log.error("Bestand (Long stid, Connection cn): " + e.toString());
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
	  
	  private void setRsValues(Connection cn, ResultSet rs) throws Exception{
		  	this.setId(rs.getLong("STID"));
		  	this.setHolding(new Holding(rs.getLong("HOID"), cn));
		  	this.setStartyear(rs.getString("startyear"));
	    	this.setStartvolume(rs.getString("startvolume"));
	    	this.setStartissue(rs.getString("startissue"));
	    	this.setEndyear(rs.getString("endyear"));
	    	this.setEndvolume(rs.getString("endvolume"));
	    	this.setEndissue(rs.getString("endissue"));
	    	this.setSuppl(rs.getInt("suppl"));
	    	this.setEissue(rs.getBoolean("eissue"));
	    	this.setStandort(new Text(cn, rs.getLong("standort")));
	    	this.setShelfmark(rs.getString("shelfmark"));
	    	this.setBemerkungen(rs.getString("bemerkungen"));
	    	this.setInternal(rs.getBoolean("internal"));
	  }
	  
	  /**
	   * Erstellt einen Bestand aus einem ResultSet
	   * 
	   * @param cn Connection
	   * @param rs ResultSet
	   */
	  public Bestand (Connection cn, ResultSet rs){
		  
		  try {
			  
			this.setId(rs.getLong("STID"));
			this.setHolding(new Holding(rs.getLong("HOID"), cn));
			this.setStartyear(rs.getString("startyear"));
			this.setStartvolume(rs.getString("startvolume"));
			this.setStartissue(rs.getString("startissue"));
			this.setEndyear(rs.getString("endyear"));
			this.setEndvolume(rs.getString("endvolume"));
			this.setEndissue(rs.getString("endissue"));
			this.setSuppl(rs.getInt("suppl"));
			this.setEissue(rs.getBoolean("eissue"));
			this.setStandort(new Text(cn, rs.getLong("standort")));
			this.setShelfmark(rs.getString("shelfmark"));
			this.setBemerkungen(rs.getString("bemerkungen"));
			this.setInternal(rs.getBoolean("internal"));
			
		} catch (SQLException e) {
			log.error("Bestand(Connection cn, ResultSet rs): " + e.toString());
		}      
	  }
	  
	    /*
	     * Setzt die Werte im Preparestatement der Methoden save()
	     */
	    private PreparedStatement setBestandValues(PreparedStatement pstmt, Bestand be, Connection cn) throws Exception{
	        
	        pstmt.setString(1, be.getHolding().getId().toString());
	        pstmt.setString(2, be.getStartyear());
	        pstmt.setString(3, be.getStartvolume());
	        pstmt.setString(4, be.getStartissue());
	        pstmt.setString(5, be.getEndyear());
	        pstmt.setString(6, be.getEndvolume());
	        pstmt.setString(7, be.getEndissue());
	        pstmt.setInt(8, be.getSuppl());
	        pstmt.setBoolean(9, be.isEissue());
	        pstmt.setLong(10, be.getStandort().getId());
	        pstmt.setString(11, be.getShelfmark());
	        pstmt.setString(12, be.getBemerkungen());
	        pstmt.setBoolean(13, be.isInternal());
	        
	        return pstmt;
	    }
	  
	    /**
	     * Speichert einen neuen Bestand in der Datenbank
	     * 
	     * @param Bestand be
	     * @param Connection cn 
	     */
	    public void save(Bestand be, Connection cn){
	    	
	    	// falls noch kein Holding zum Bestand existiert zuerst ein Holding abspeichern
	    	if (be.getHolding().getId()==null) {
	    		Holding h = new Holding();
	    		h = h.save(be.getHolding(), cn);
	    		be.setHolding(h);
	    	}
	                
	        PreparedStatement pstmt = null;
	    	try {
	            pstmt = setBestandValues(cn.prepareStatement( "INSERT INTO `stock` (`HOID` , " +
	            "`startyear` , `startvolume` , `startissue` , `endyear` , `endvolume` , `endissue` , `suppl` , `eissue` , " +
	            "`standort` , `shelfmark` , `bemerkungen` , `internal`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), be, cn);
	            
	            pstmt.executeUpdate();
	            
	        } catch (Exception e) {
	        	log.error("save(Bestand be, Connection cn)" + e.toString());
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
		   * Liefert alle Bestände anhand einer einzelnen ISSN unter
		   * Berücksichtigung aller verwandten ISSNs
		   * @param OrderForm pageForm
		   * @param boolean internal
		   * @param Connection cn
		   * 
		   * @return ArrayList<Bestand> listBestand
		   */
		  public ArrayList<Bestand> getAllBestandForIssn(OrderForm pageForm, boolean internal, Connection cn){
		      ArrayList<Bestand> listBestand = new ArrayList<Bestand>();
		      
		      String sqlQuery = "SELECT stock.* FROM stock JOIN holdings ON stock.HOID = holdings.HOID JOIN issn AS b " +
		      "ON holdings.issn = b.issn JOIN issn AS a ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier " +
		      "WHERE a.issn = ? AND internal <= ? AND ((startyear < ? AND (endyear > ? OR endyear = '')) OR (startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND " +
		      "(startvolume < ? OR startvolume = '') AND (endvolume > ? OR endvolume = '')) OR (startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND (startvolume <= ? OR startvolume = '') AND (endvolume >= ? OR endvolume = '') AND (startissue <= ? OR startissue = '') AND (endissue >= ? OR endissue = '')))";
		      
		      PreparedStatement pstmt = null;
		      ResultSet rs = null;
		      try {
		          pstmt = cn.prepareStatement(sqlQuery);
		          pstmt.setString(1, pageForm.getIssn());		          
		          pstmt.setBoolean(2, internal);		          
		          pstmt.setString(3, pageForm.getJahr());
		          pstmt.setString(4, pageForm.getJahr());
		          pstmt.setString(5, pageForm.getJahr());
		          pstmt.setString(6, pageForm.getJahr());
		          pstmt.setString(7, pageForm.getJahr());		          
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(8, "99999999999");} else {pstmt.setString(8, pageForm.getJahrgang());}
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(9, "1");} else {pstmt.setString(9, pageForm.getJahrgang());}
		          pstmt.setString(10, pageForm.getJahr());
		          pstmt.setString(11, pageForm.getJahr());
		          pstmt.setString(12, pageForm.getJahr());
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(13, "99999999999");} else {pstmt.setString(13, pageForm.getJahrgang());}
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(14, "1");} else {pstmt.setString(14, pageForm.getJahrgang());}
		          if (pageForm.getHeft().equals("")) {pstmt.setString(15, "99999999999");} else {pstmt.setString(15, pageForm.getHeft());}
		          if (pageForm.getHeft().equals("")) {pstmt.setString(16, "1");} else {pstmt.setString(16, pageForm.getHeft());}

		          rs = pstmt.executeQuery();

		          while (rs.next()) {
		              Bestand be = new Bestand(cn, rs);
		              listBestand.add(be);
		          }

		      } catch (Exception e) {
		    	  log.error("ArrayList<Bestand> getAllBestandForIssn(OrderForm pageForm, boolean internal, Connection cn): " + e.toString());
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
		      
		      return listBestand;  
		  }

		  /**
		   * Liefert alle Bestände eines Kontos anhand einer einzelnen ISSN unter
		   * Berücksichtigung aller verwandten ISSNs
		   * @param Long kid
		   * @param OrderForm pageForm
		   * @param boolean internal
		   * @param Connection cn
		   * 
		   * @return ArrayList<Bestand> listBestand
		   */
		  public ArrayList<Bestand> getKontoBestandForIssn(Long kid, OrderForm pageForm, boolean internal, Connection cn){
		      ArrayList<Bestand> listBestand = new ArrayList<Bestand>();
		      
		      String sqlQuery = "SELECT stock.* FROM stock JOIN holdings ON stock.HOID = holdings.HOID JOIN issn AS b " +
		      "ON holdings.issn = b.issn JOIN issn AS a ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier " +
		      "WHERE holdings.KID = ? AND a.issn = ? AND internal <= ? AND ((startyear < ? AND (endyear > ? OR endyear = '')) OR (startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND " +
		      "(startvolume < ? OR startvolume = '') AND (endvolume > ? OR endvolume = '')) OR (startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND (startvolume <= ? OR startvolume = '') AND (endvolume >= ? OR endvolume = '') AND (startissue <= ? OR startissue = '') AND (endissue >= ? OR endissue = '')))";
		      
		      PreparedStatement pstmt = null;
		      ResultSet rs = null;
		      try {
		          pstmt = cn.prepareStatement(sqlQuery);
		          pstmt.setLong(1, kid);
		          pstmt.setString(2, pageForm.getIssn());		          
		          pstmt.setBoolean(3, internal);		          
		          pstmt.setString(4, pageForm.getJahr());
		          pstmt.setString(5, pageForm.getJahr());
		          pstmt.setString(6, pageForm.getJahr());
		          pstmt.setString(7, pageForm.getJahr());
		          pstmt.setString(8, pageForm.getJahr());		          
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(9, "99999999999");} else {pstmt.setString(9, pageForm.getJahrgang());}
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(10, "1");} else {pstmt.setString(10, pageForm.getJahrgang());}
		          pstmt.setString(11, pageForm.getJahr());
		          pstmt.setString(12, pageForm.getJahr());
		          pstmt.setString(13, pageForm.getJahr());
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(14, "99999999999");} else {pstmt.setString(14, pageForm.getJahrgang());}
		          if (pageForm.getJahrgang().equals("")) {pstmt.setString(15, "1");} else {pstmt.setString(15, pageForm.getJahrgang());}
		          if (pageForm.getHeft().equals("")) {pstmt.setString(16, "99999999999");} else {pstmt.setString(16, pageForm.getHeft());}
		          if (pageForm.getHeft().equals("")) {pstmt.setString(17, "1");} else {pstmt.setString(17, pageForm.getHeft());}

		          rs = pstmt.executeQuery();

		          while (rs.next()) {
		              Bestand be = new Bestand(cn, rs);
		              listBestand.add(be);
		          }

		      } catch (Exception e) {
		    	  log.error("ArrayList<Bestand> getKontoBestandForIssn(Long kid, OrderForm pageForm, boolean internal, Connection cn): " + e.toString());
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
		      
		      return listBestand;  
		  }
	    
	  /**
	   * Liefert alle Bestände anhand einer Standort-ID
	   * 
	   * @param tid Long
	   * @param cn Connection
	   * 
	   * @return List mit Bestand
	   */
	  public ArrayList<Bestand> getAllBestandForStandortId(Long tid, Connection cn){
	      ArrayList<Bestand> sl = new ArrayList<Bestand>();
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	          pstmt = cn.prepareStatement("SELECT * FROM stock WHERE standort=? ORDER BY standort");
	          pstmt.setString(1, tid.toString());
	          rs = pstmt.executeQuery();

	          while (rs.next()) {
	              Bestand be = new Bestand(cn, rs);
	              sl.add(be);
	          }

	      } catch (Exception e) {
	    	  log.error("getAllBestandForStandortId(Long tid, Connection cn): " + e.toString());
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
	   * Liefert alle Bestände anhand einer Holding-Liste für ein Orderform
	   * @param ArrayList<Holding> holdings
	   * @param OrderForm pageForm
	   * @param boolean internal
	   * @param Connection cn
	   * 
	   * @return ArrayList<Bestand> listBestand
	   */
	  public ArrayList<Bestand> getAllBestandForHoldings(ArrayList<String> hoids, OrderForm pageForm, boolean internal, Connection cn){
	      ArrayList<Bestand> listBestand = new ArrayList<Bestand>();
	      
	      if (hoids.size()>0) {
	      
	      StringBuffer sqlQuery = new StringBuffer("SELECT * FROM `stock` WHERE internal <= ? AND ((startyear < ? AND (endyear > ? OR endyear = '')) OR " + 
	      "(startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND (startvolume < ? OR startvolume = '') AND (endvolume > ? OR endvolume = '')) OR " + 
	      "(startyear <= ? AND (startyear = ? OR endyear = ? OR endyear = '') AND (startvolume <= ? OR startvolume = '') AND (endvolume >= ? OR endvolume = '') AND (startissue <= ? OR startissue = '') AND (endissue >= ? OR endissue = ''))) AND (HOID = ?");
	      
	      for (int i=1;i<hoids.size();i++) { // nur ausführen, wenn holdings > 1
	    	  sqlQuery.append(" OR HOID = ?");
	      }
	      sqlQuery.append(")");
	      
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	          pstmt = cn.prepareStatement(sqlQuery.toString());
	          pstmt.setBoolean(1, internal);
	          pstmt.setString(2, pageForm.getJahr());
	          pstmt.setString(3, pageForm.getJahr());
	          pstmt.setString(4, pageForm.getJahr());
	          pstmt.setString(5, pageForm.getJahr());
	          pstmt.setString(6, pageForm.getJahr());
	          if (pageForm.getJahrgang().equals("")) {pstmt.setString(7, "99999999999");} else {pstmt.setString(7, pageForm.getJahrgang());}
	          if (pageForm.getJahrgang().equals("")) {pstmt.setString(8, "1");} else {pstmt.setString(8, pageForm.getJahrgang());}
	          pstmt.setString(9, pageForm.getJahr());
	          pstmt.setString(10, pageForm.getJahr());
	          pstmt.setString(11, pageForm.getJahr());
	          if (pageForm.getJahrgang().equals("")) {pstmt.setString(12, "99999999999");} else {pstmt.setString(12, pageForm.getJahrgang());}
	          if (pageForm.getJahrgang().equals("")) {pstmt.setString(13, "1");} else {pstmt.setString(13, pageForm.getJahrgang());}
	          if (pageForm.getHeft().equals("")) {pstmt.setString(14, "99999999999");} else {pstmt.setString(14, pageForm.getHeft());}
	          if (pageForm.getHeft().equals("")) {pstmt.setString(15, "1");} else {pstmt.setString(15, pageForm.getHeft());}
	          
	          for (int i=0;i<hoids.size();i++) {
	        	  pstmt.setString(16+i, hoids.get(i));
	          }

	          rs = pstmt.executeQuery();

	          while (rs.next()) {
	              Bestand be = new Bestand(cn, rs);
	              listBestand.add(be);
	          }

	      } catch (Exception e) {
	    	  log.error("ArrayList<Bestand> getAllBestandForHoldings: " + e.toString());
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
	      
	      return listBestand;  
	  }
	  
	  /**
	   * Liefert alle Bestände anhand einer HOID und einer Standort-ID, die nicht auf dem selben Gestell / die selbe Notation haben
	   * 
	   * @param hoid Long
	   * @param tid Long
	   * @param gestell String
	   * @param cn Connection
	   * 
	   * @return ArrayList<Bestand> listBestand
	   */
	  public ArrayList<Bestand> getAllBestandOnDifferentShelves(Long hoid, Long tid, String gestell, Connection cn){
	      ArrayList<Bestand> listBestand = new ArrayList<Bestand>();
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	          pstmt = cn.prepareStatement("SELECT * FROM stock WHERE HOID=? AND standort=? AND NOT shelfmark=? ORDER BY shelfmark");
	          pstmt.setString(1, hoid.toString());
	          pstmt.setString(2, tid.toString());
	          pstmt.setString(3, gestell);
	          rs = pstmt.executeQuery();

	          while (rs.next()) {
	              Bestand be = new Bestand(cn, rs);
	              listBestand.add(be);
	          }

	      } catch (Exception e) {
	    	  log.error("getAllBestandOnDifferentShelves(Long hoid, Long tid, Connection cn): " + e.toString());
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
	      
	      return listBestand;  
	  }

	

	public Holding getHolding() {
		return holding;
	}

	public void setHolding(Holding holding) {
		this.holding = holding;
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

	public Text getStandort() {
		return standort;
	}

	public void setStandort(Text standort) {
		this.standort = standort;
	}

	public String getShelfmark() {
		return shelfmark;
	}

	public void setShelfmark(String shelfmark) {
		this.shelfmark = shelfmark;
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

	public boolean isEissue() {
		return eissue;
	}

	public void setEissue(boolean eissue) {
		this.eissue = eissue;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}
	  
	  

}


