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
public class EzbObject extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(EzbObject.class);

private String ezbid;
private String zdbid;
private ArrayList<String> issn;

public EzbObject() {
    
}

/**
 * Erstellt ein EZB-Objekt anhand einer Verbindung und der ID
 * 
 * @param Long eid
 * @param Connection cn
 * @return EzbObject eo
 */
public EzbObject (Long eid, Connection cn){

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
        pstmt = cn.prepareStatement("SELECT * FROM ezb_id WHERE EID = ?");
        pstmt.setLong(1, eid);
        rs = pstmt.executeQuery();

        while (rs.next()) {
           this.setRsValues(cn, rs);
        }

    } catch (Exception e) {
    	log.error("EzbObject (Long eid, Connection cn)" + e.toString());
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
 * Erstellt ein EZB-Objekt anhand einer Verbindung und der ezbid
 * 
 * @param String ezbid
 * @param Connection cn
 * @return EzbObject eo
 */
public EzbObject (String ezbid, Connection cn){

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
        pstmt = cn.prepareStatement("SELECT * FROM ezb_id WHERE ezb_id = ?");
        pstmt.setString(1, ezbid);
        rs = pstmt.executeQuery();

        while (rs.next()) {
           this.setRsValues(cn, rs);
        }

    } catch (Exception e) {
    	log.error("EzbObject (String ezbid, Connection cn)" + e.toString());
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
 * Erstellt ein EZB-Objekt anhand einer Verbindung und der zdbid
 * 
 * @param String zdbid
 * @param Connection cn
 * @return EzbObject eo
 */
public EzbObject getEzbObjectFromZdbid (String zdbid, Connection cn){
	
		EzbObject eo = new EzbObject();

	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
        pstmt = cn.prepareStatement("SELECT * FROM ezb_id WHERE zdb_id = ?");
        pstmt.setString(1, zdbid);
        rs = pstmt.executeQuery();

        while (rs.next()) {
        	eo = new EzbObject(cn, rs);
        }

    } catch (Exception e) {
    	log.error("getEzbObjectFromZdbid (String zdbid, Connection cn): " + e.toString());
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
    
    return eo;
}

public EzbObject (Connection cn, ResultSet rs) {
	Issn issnInstance = new Issn();
	
	try {
	  	this.setId(rs.getLong("EID"));
	  	this.setIssn(issnInstance.getAllIssnsFromOneEzbid(rs.getString("ezb_id"), cn));
	  	this.setEzbid(rs.getString("ezb_id"));
	  	this.setZdbid(rs.getString("zdb_id"));
	} catch (Exception e) {
		log.error("EzbObject (Connection cn, ResultSet rs)" + e.toString());
	    }
}

private void setRsValues(Connection cn, ResultSet rs) throws Exception{
	Issn issnInstance = new Issn();
  	this.setId(rs.getLong("EID"));
  	this.setIssn(issnInstance.getAllIssnsFromOneEzbid(rs.getString("ezb_id"), cn));
  	this.setEzbid(rs.getString("ezb_id"));
  	this.setZdbid(rs.getString("zdb_id"));
}

public String getEzbid() {
	return ezbid;
}

public void setEzbid(String ezbid) {
	this.ezbid = ezbid;
}

public String getZdbid() {
	return zdbid;
}

public void setZdbid(String zdbid) {
	this.zdbid = zdbid;
}

public ArrayList<String> getIssn() {
	return issn;
}

public void setIssn(ArrayList<String> issn) {
	this.issn = issn;
}


 
}
