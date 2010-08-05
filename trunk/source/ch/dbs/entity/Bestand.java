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
import java.util.HashSet;

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
	     * Sets the values for the PreparedStatement. Leaves the ID blank and lets
	     * MySQL autoincrement the STID
	     */
	    private PreparedStatement setBestandValuesAutoincrement(PreparedStatement pstmt, Bestand be, Connection cn) throws Exception{
	        
	        pstmt.setLong(1, be.getHolding().getId());
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
	    
	    /*
	     * Sets the values for the PreparedStatement. Specifies the ID and sets
	     * a given STID for MySQL
	     */
	    private PreparedStatement setAllBestandValues(PreparedStatement pstmt, Bestand be, Connection cn) throws Exception{
	        
	        pstmt.setLong(1, be.getId());
	    	pstmt.setLong(2, be.getHolding().getId());
	        pstmt.setString(3, be.getStartyear());
	        pstmt.setString(4, be.getStartvolume());
	        pstmt.setString(5, be.getStartissue());
	        pstmt.setString(6, be.getEndyear());
	        pstmt.setString(7, be.getEndvolume());
	        pstmt.setString(8, be.getEndissue());
	        pstmt.setInt(9, be.getSuppl());
	        pstmt.setBoolean(10, be.isEissue());
	        pstmt.setLong(11, be.getStandort().getId());
	        pstmt.setString(12, be.getShelfmark());
	        pstmt.setString(13, be.getBemerkungen());
	        pstmt.setBoolean(14, be.isInternal());
	        
	        return pstmt;
	    }
	  
	    /**
	     * Saves a new Bestand in the database. Will use the autoincremented ID.
	     * 
	     * @param Bestand be
	     * @param Connection cn 
	     */
	    public void saveWithoutID(Bestand be, Connection cn){
	    	
	    	// if there is no Holding specified, save one first
	    	if (be.getHolding().getId()==null) {
	    		Holding h = new Holding();
	    		h = h.save(be.getHolding(), cn);
	    		be.setHolding(h);
	    	}
	                
	        PreparedStatement pstmt = null;
	    	try {
	            pstmt = setBestandValuesAutoincrement(cn.prepareStatement( "INSERT INTO `stock` (`HOID` , " +
	            "`startyear` , `startvolume` , `startissue` , `endyear` , `endvolume` , `endissue` , `suppl` , `eissue` , " +
	            "`standort` , `shelfmark` , `bemerkungen` , `internal`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), be, cn);
	            
	            pstmt.executeUpdate();
	            
	        } catch (Exception e) {
	        	log.error("saveWithoutID(Bestand be, Connection cn)" + e.toString());
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
	     * Saves a new Bestand in the database. Will use the ID secified.
	     * 
	     * @param Bestand be
	     * @param Connection cn 
	     */
	    public void saveWithID(Bestand be, Connection cn){
	    	
	    	// if there is no Holding specified, save one first
	    	if (be.getHolding().getId()==null) {
	    		Holding h = new Holding();
	    		h = h.save(be.getHolding(), cn);
	    		be.setHolding(h);
	    	}
	                
	        PreparedStatement pstmt = null;
	    	try {
	            pstmt = setAllBestandValues(cn.prepareStatement( "INSERT INTO `stock` (`STID` , `HOID` , " +
	            "`startyear` , `startvolume` , `startissue` , `endyear` , `endvolume` , `endissue` , `suppl` , `eissue` , " +
	            "`standort` , `shelfmark` , `bemerkungen` , `internal`) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), be, cn);
	            
	            pstmt.executeUpdate();
	            
	        } catch (Exception e) {
	        	log.error("saveWithID(Bestand be, Connection cn)" + e.toString());
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
	     * Deletes all Stock from a given account. But does not touch the corresponding holdings.
	     * 
	     * @param Konto k
	     * @param Connection cn 
	     */
	    public void deleteAllKontoBestand(Konto k, Connection cn){
	        
	        PreparedStatement pstmt = null;
	    	try {
	            pstmt = cn.prepareStatement( "DELETE a FROM stock AS a INNER JOIN holdings AS b ON a.HOID = b.HOID WHERE b.KID=?;");	            
	            pstmt.setLong(1, k.getId());	            
	            pstmt.executeUpdate();
	            
	        } catch (Exception e) {
	        	log.error("deleteAllKontoBestand(Konto k, Connection cn)" + e.toString());
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
		   * Gets all "Bestand" from a kid
		   * 
		   * @param Long kid
		   * @param Connection cn
		   * 
		   * @return ArrayList<Bestand> listBestand
		   */
		  public ArrayList<Bestand> getAllKontoBestand(Long kid, Connection cn){
		      ArrayList<Bestand> listBestand = new ArrayList<Bestand>();
		      
		      PreparedStatement pstmt = null;
		      ResultSet rs = null;
		      try {
		          pstmt = cn.prepareStatement("SELECT a.* FROM stock AS a JOIN holdings AS b ON a.HOID = b.HOID WHERE b.KID = ?");
		          pstmt.setLong(1, kid);
		          rs = pstmt.executeQuery();

		          while (rs.next()) {
		              Bestand be = new Bestand(cn, rs);
		              listBestand.add(be);
		          }

		      } catch (Exception e) {
		    	  log.error("ArrayList<Bestand> getAllKontoBestand(Long kid, Connection cn): " + e.toString());
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
	   * Gets all Bestand from a list of holdings for a OrderForm
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
	    	  
	      int searchMode = getSearchMode(pageForm);
	      
	      if (searchMode>0) { // only proceed if there has been found a search modus (at least an ISSN)
	      
	      StringBuffer sqlQuery = new StringBuffer(getSQL(searchMode));
	      
	      for (int i=1;i<hoids.size();i++) { // only append if there are several holdings
	    	  sqlQuery.append(" OR HOID = ?");
	      }
	      sqlQuery.append(")"); // close SQL Syntax with a parenthesis
	      
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	          pstmt = cn.prepareStatement(sqlQuery.toString());
	          
	          int positionHoid = 2;
	          
	          switch (searchMode)
	          {
	          case 1: // everything without a year and at least an ISSN
	        	  pstmt.setBoolean(1, internal);
	        	  break;
	          case 2: // ISSN, Year
	        	  pstmt.setBoolean(1, internal);
	        	  pstmt.setString(2, pageForm.getJahr());
		          pstmt.setString(3, pageForm.getJahr());
		          positionHoid = 4;
	        	  break;
	          case 3: // ISSN, Year, Volume
	        	  pstmt.setBoolean(1, internal);
	        	  pstmt.setString(2, pageForm.getJahr());
		          pstmt.setString(3, pageForm.getJahr());
		          pstmt.setString(4, pageForm.getJahrgang());
		          pstmt.setString(5, pageForm.getJahrgang());
		          positionHoid = 6;
	        	  break;
	          case 4: // ISSN, Year, Volume, Issue
	        	  pstmt.setBoolean(1, internal);
	        	  pstmt.setString(2, pageForm.getJahr());
		          pstmt.setString(3, pageForm.getJahr());
		          pstmt.setString(4, pageForm.getJahrgang());
	        	  pstmt.setString(5, pageForm.getJahrgang());
		          pstmt.setString(6, pageForm.getHeft());
		          pstmt.setString(7, pageForm.getHeft());
		          pstmt.setString(8, pageForm.getJahr());
		          pstmt.setString(9, pageForm.getJahrgang());
	        	  pstmt.setString(10, pageForm.getJahrgang());
		          pstmt.setString(11, pageForm.getHeft());
		          pstmt.setString(12, pageForm.getJahr());
		          pstmt.setString(13, pageForm.getJahr());
		          pstmt.setString(14, pageForm.getJahrgang());
	        	  pstmt.setString(15, pageForm.getJahrgang());
		          pstmt.setString(16, pageForm.getHeft());
		          pstmt.setString(17, pageForm.getJahr());
		          positionHoid = 18;
	        	  break;
	          case 5: // ISSN, Year, Issue
	        	  pstmt.setBoolean(1, internal);
	        	  pstmt.setString(2, pageForm.getJahr());
		          pstmt.setString(3, pageForm.getJahr());
		          pstmt.setString(4, pageForm.getHeft());
		          pstmt.setString(5, pageForm.getHeft());
		          pstmt.setString(6, pageForm.getJahr());
		          pstmt.setString(7, pageForm.getHeft());
		          pstmt.setString(8, pageForm.getJahr());
		          pstmt.setString(9, pageForm.getJahr());
		          pstmt.setString(10, pageForm.getHeft());
		          pstmt.setString(11, pageForm.getJahr());
		          positionHoid = 12;
	        	  break;
	          }
	          
	          for (int i=0;i<hoids.size();i++) {
	        	  pstmt.setString(positionHoid+i, hoids.get(i));
	          }

	          rs = pstmt.executeQuery();
	          
	          // Controls that only one matching stock per location will be shown
	          HashSet<Long> uniqueSetLocationID = new HashSet<Long>();

	          while (rs.next()) {
	              Bestand be = new Bestand(cn, rs);	              
	              if(!uniqueSetLocationID.contains(be.getStandort().getId())) {
	            	  listBestand.add(be); // add only one stock for a given location
	            	  uniqueSetLocationID.add(be.getStandort().getId());
	              }
	              
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
	      }
	      
	      return listBestand;  
	  }
	
	  /**
	   * Gets the correct SQL syntax for a given search mode
	   * @param int mode
	   * 
	   * @return String sql
	   */
	private String getSQL(int mode) {
		String sql = "";
		
		switch (mode)
		{
		case 1: // everything without a year and at least an ISSN
			sql = "SELECT * FROM `stock` WHERE internal <= ? AND (HOID = ?";
			break;
		case 2: // ISSN, Year
			sql = "SELECT * FROM `stock` WHERE internal <= ? AND (startyear <= ? AND (endyear >= ? OR endyear = '')) AND (HOID = ?";
			break;
		case 3: // ISSN, Year, Volume
			sql = "SELECT * FROM `stock` WHERE internal <= ? AND (startyear <= ? AND (endyear >= ? OR endyear = '') AND (startvolume <= ? OR startvolume = '') AND (endvolume >= ? OR endvolume = '')) AND (HOID = ?";
			break;
		case 4: // ISSN, Year, Volume, Issue
			sql = "SELECT * FROM `stock` WHERE internal <= ? AND ( (startyear = ? AND ((endyear = ? AND (startvolume <=? OR startvolume = '') AND (endvolume >=? OR endvolume = '') AND (startissue <= ? OR startissue = '') AND (endissue >= ? OR endissue = '')) OR ((endyear > ? OR endyear = '') AND (startvolume <=? OR startvolume = '') AND (endvolume >=? OR endvolume = '') AND (startissue <= ? OR startissue = ''))) ) OR (startyear < ? AND ((endyear = ? AND (startvolume <=? OR startvolume = '') AND (endvolume >=? OR endvolume = '') AND (endissue >= ? OR endissue = '')) OR (endyear > ? OR endyear = '')) ) ) AND (HOID = ?";
			break;
		case 5: // ISSN, Year, Issue
			sql = "SELECT * FROM `stock` WHERE internal <= ? AND ( (startyear = ? AND ((endyear = ? AND (startissue <= ? OR startissue = '') AND (endissue >= ? OR endissue = '')) OR ((endyear > ? OR endyear = '') AND (startissue <= ? OR startissue = ''))) ) OR (startyear < ? AND ((endyear = ? AND (endissue >= ? OR endissue = '')) OR (endyear > ? OR endyear = '')) ) ) AND (HOID = ?";
			break;
		}	
		
		return sql;
	}
	
	  /**
	   * Evaluates the orderform to find the appropriate search mode
	   * @param OrderForm of
	   * 
	   * @return int mode
	   */
	private int getSearchMode(OrderForm of) {
		int mode = 0;
		
		if (!of.getIssn().equals("")) { // cases with an ISSN!
			
			if (!of.getJahr().equals("") && of.getJahrgang().equals("") && of.getHeft().equals("")) { // ISSN, Year
				mode = 2;
			} else if (!of.getJahr().equals("") && !of.getJahrgang().equals("") && of.getHeft().equals("")) { // ISSN, Year, Volume
				mode = 3;
			} else if (!of.getJahr().equals("") && !of.getJahrgang().equals("") && !of.getHeft().equals("")) { // ISSN, Year, Volume, Issue
				mode = 4;
			} else if (!of.getJahr().equals("") && of.getJahrgang().equals("") && !of.getHeft().equals("")) { // ISSN, Year, Issue
				mode = 5;
			} else { // everything else without a year and at least an ISSN
				mode = 1;
			}			
		}	
		
		return mode;
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


