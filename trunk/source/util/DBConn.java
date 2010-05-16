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

package util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.grlea.log.SimpleLogger;

/**
 * DBConn stellt Methoden zum Kommunizieren mit dem DB-Server zur Verfügung
 * DBConn erstellt eine Verbindung zum MySQL-Server, und gibt diese zurück.
 * <p></p>
 * @author Pascal Steiner
 */
public class DBConn extends AbstractReadSystemConfigurations {
	
	private static final SimpleLogger log = new SimpleLogger(DBConn.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String SERVER = "jdbc:mysql://localhost/" + DATABASE_NAME + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false"; // z.B. 127.0.0.1 oder localhost
    private Connection cn = null;
    
    /**
     * Stellt die Verbindung zur MySql DB her und speichert die Verbindung im Objekt
     * @return liefert ein Verbindungsobjekt zurück
     */
    private Connection connect() {
        // Treiber laden
        try {
            Class.forName(DRIVER);
            cn = DriverManager.getConnection(SERVER, DATABASE_USER, DATABASE_PASSWORD);
//            System.out.println("Verbunden mit " + SERVER + " Verbindungsobjekt: " + cn );
        }
        catch(Exception e) {
        	log.error("connect: " + e.toString());
        }
        return cn;
    }
    
    /**
     * Speichert ein File in der Datenbank
     * 
     * @param file ein File übergeben
     * @param id des Datensatzes, welcher das File hinterlegt bekommen soll
     * @param table (Tabelle)
     * @param field (Feld, in dem die Datei gespeichert wird)
     */
    public void saveFile(File fl, String id, String table, String field) throws Exception {
        
        Class.forName(DRIVER);
        Connection        dbCon = DriverManager.getConnection( SERVER, DATABASE_USER, DATABASE_PASSWORD );
        
        FileInputStream   fis   = new FileInputStream( fl );
        PreparedStatement pstmt = dbCon.prepareStatement(
                                  "update " + table + " set " + field + " = ? where did = " + id );
        pstmt.setBinaryStream( 1, fis, (int)fl.length() );
        pstmt.executeUpdate();
        pstmt.close();
        dbCon.close();
        fis.close(); 
    }
    
  /**
   * Schliesst die Datenbankverbindung wieder
   */ 
   public void close(){
           try {
        	   if (cn!=null) {
//        	   System.out.println("Verbindungsobjekt " + cn + " wurde geschlossen");
        	   cn.close();
        	   }
               
               }
           catch( Exception e ) {
        	   log.error("close: " + e.toString());
               }
   }

   /**
    * Diese Methode stellt eine Einzelverbindung (nicht gepoolt) zur Datenbank her. Sie sollte nur ausnahmsweise direkt
    * aufgerufen werden. => getConnection verwenden
    * 
    * @return Connection
    */
	public Connection getSingleConnection() {
		if (cn==null){
			this.connect();
		} else {
			try {
				if (cn.isClosed()){
					this.connect();
				}
			} catch (SQLException e) {
				log.error("getSingleConnection: " + e.toString());
			}
		}
		return cn;
	}
	
	   /**
	    * Stellt die Verbindung zur DB her wenn die Verbindung noch nicht besteht.<p>
	    * Berücksichtigt Einstellungen unter SystemConfigurations für PooledConnections (true / false)</p>
	    * 
	    * @return Connection
	    */
	public Connection getConnection() {
		
		try {		
		if (cn==null || cn.isClosed()) {
		if (DATABASE_POOLED_CONNECTIONS) {
			cn = getPooledConnection();
		} else {
			cn = getSingleConnection();
		}
		}
		} catch (SQLException e) {
			log.error("getConnection: " + e.toString());
        	cn = getSingleConnection();
        }
		
		return cn;
	}
	
	   /**
	    * Diese Methode stellt eine Verbindung zur Datenbank über Pooled Connections her. Sie sollte nur ausnahmsweise direkt
	    * aufgerufen werden. => getConnection verwenden
	    * 
	    * @return Connection
	    */
	public Connection getPooledConnection() {
		
		if (cn==null){
			try {
	            InitialContext ic = new InitialContext();
	            DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/pooledDS");
	            cn = ds.getConnection();
//	            PooledDataSource pds = (PooledDataSource) ds; 
//	            System.err.println("num_connections: " + pds.getNumConnectionsDefaultUser());
//	            System.err.println("num_busy_connections: " + pds.getNumBusyConnectionsDefaultUser());
//	            System.err.println("num_idle_connections: " + pds.getNumIdleConnectionsDefaultUser());
//	            System.err.println();
	            } catch (Exception e) { // alternativer Versuch, falls PooledConnection versagt
	            	log.error("getPooledConnection: " + e.toString());
	            	cn = getSingleConnection();
	            	// Control to see if it works on remote server
	            	MHelper mh = new MHelper();
	            	mh.sendErrorMail("Failure in getPooledConnection!!!", e.toString());
	            }
		} else {
			try {
				if (cn.isClosed()){
					try {
			            InitialContext ic = new InitialContext();
			            DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/pooledDS");
			            cn = ds.getConnection();
//			            PooledDataSource pds = (PooledDataSource) ds; 
//			            System.err.println("num_connections: " + pds.getNumConnectionsDefaultUser());
//			            System.err.println("num_busy_connections: " + pds.getNumBusyConnectionsDefaultUser());
//			            System.err.println("num_idle_connections: " + pds.getNumIdleConnectionsDefaultUser());
//			            System.err.println();
			            } catch (Exception e) { // alternativer Versuch, falls PooledConnection versagt
			            	log.error("getPooledConnection: " + e.toString());
			            	cn = getSingleConnection();
			            	// Control to see if it works on remote server
			            	MHelper mh = new MHelper();
			            	mh.sendErrorMail("Failure in getPooledConnection!!!", e.toString());
			            }
				}
			} catch (SQLException e) {
				log.error("getPooledConnection: " + e.toString());
			}
		}		
		
		return cn;
	}

public void setCn(Connection cn) {
	this.cn = cn;
}
	
	
       
}
   
    
    
     


