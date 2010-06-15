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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.grlea.log.SimpleLogger;

import ch.dbs.form.OrderForm;
import ch.dbs.form.OrderStatistikForm;
import ch.dbs.form.PreisWaehrungForm;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 */
public class Bestellungen extends AbstractIdEntity {
	
	private static final SimpleLogger log = new SimpleLogger(Bestellungen.class);

  private AbstractBenutzer benutzer;
  private Konto konto;
  private Lieferanten lieferant; // neue Verknüpfung zu Tabelle Lieferanten
  private String bestellquelle; // dient lediglich noch der Führung des doppelten Eintrages in der DB
  private String priority;
  private String fileformat;
  private String deloptions;
  private String autor;
  private String artikeltitel;
  private String jahr;
  private String jahrgang;
  private String heft;
  private String seiten;
  private String issn;
  private String subitonr;
  private String gbvnr;
  private String trackingnr = ""; // eindeutige Nr. in der Kommunikation zwischen Bestellsystemen. Wird vor dem Abspeichern der Bestellung benötigt...
  private String interne_bestellnr = ""; // falls eine Bibliothek ein eigenes Nummersystem führt
  private String sigel;
  private String bibliothek;
  private String systembemerkung;
  private String notizen;
  private String zeitschrift;
  private String statustext;
  private String statusdate;
  private String orderdate;
  private boolean erledigt;
  private String preisvorkomma;
  private String preisnachkomma;
  private String waehrung;
  private boolean preisdefault;
  private BigDecimal kaufpreis;
  
  private String doi = ""; // Digital  Object Identifier
  private String pmid = ""; // Pubmed-ID
  private String isbn = "";
  private String mediatype; // book oder journal
  private String verlag = ""; // Buchverlag
  private String kapitel = "";
  private String buchtitel = "";
  
  public Bestellungen() { }

  public Bestellungen(AbstractBenutzer user, Konto k) { 
      this.setBenutzer(user);
      this.setKonto(k);
  }
  
  public Bestellungen(OrderForm of, AbstractBenutzer user, Konto k) {
	  
	  ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date d = new Date();
      String datum = fmt.format(d, k.getTimezone());
	  
	  this.setKonto(k);
	  this.setBenutzer(user);
	  this.setLieferant(of.getLieferant());
	  if (of.getMediatype()!=null) {this.setMediatype(of.getMediatype());} else {this.setMediatype("");}
	  if (of.getPrio()!=null) {this.setPriority(of.getPrio());} else {this.setPriority("normal");}
	  if (of.getDeloptions()!=null) {this.setDeloptions(of.getDeloptions());} else {this.setDeloptions("");}
	  if (of.getFileformat()!=null) {this.setFileformat(of.getFileformat());} else {this.setFileformat("");}
	  if (of.getZeitschriftentitel()!=null) {this.setZeitschrift(of.getZeitschriftentitel());} else {this.setZeitschrift("");}
	  if (of.getAuthor()!=null) {this.setAutor(of.getAuthor());} else {this.setAutor("");}
	  if (of.getArtikeltitel()!=null) {this.setArtikeltitel(of.getArtikeltitel());} else {this.setArtikeltitel("");}
	  if (of.getJahr()!=null) {this.setJahr(of.getJahr());} else {this.setJahr("");}
	  if (of.getJahrgang()!=null) {this.setJahrgang(of.getJahrgang());} else {this.setJahrgang("");}
	  if (of.getHeft()!=null) {this.setHeft(of.getHeft());} else {this.setHeft("");}
	  if (of.getSeiten()!=null) {this.setSeiten(of.getSeiten());} else {this.setSeiten("");}
	  this.setIssn(of.getIssn()); // darf offenbar null sein
	  if (of.getIsbn()!=null) {this.setIsbn(of.getIsbn());} else {this.setIsbn("");}
	  if (of.getKapitel()!=null) {this.setKapitel(of.getKapitel());} else {this.setKapitel("");}
	  if (of.getBuchtitel()!=null) {this.setBuchtitel(of.getBuchtitel());} else {this.setBuchtitel("");}
	  if (of.getVerlag()!=null) {this.setVerlag(of.getVerlag());} else {this.setVerlag("");}
	  if (of.getDoi()!=null) {this.setDoi(of.getDoi());} else {this.setDoi("");}
	  if (of.getPmid()!=null) {this.setPmid(of.getPmid());} else {this.setPmid("");}
	  if (of.getSubitonr()!=null) {this.setSubitonr(of.getSubitonr());} else {this.setSubitonr("");} // darf zwar null sein, war aber praktisch nie
	  this.setGbvnr(of.getGbvnr());
	  if (of.getTrackingnr()!=null) {this.setTrackingnr(of.getTrackingnr());} else {this.setTrackingnr("");}
	  if (of.getInterne_bestellnr()!=null) {this.setInterne_bestellnr(of.getInterne_bestellnr());} else {this.setInterne_bestellnr("");}
	  this.setSigel(of.getSigel());
	  this.setBibliothek(of.getBibliothek());
	  this.setBestellquelle(of.getBestellquelle());
	  this.setStatustext(of.getStatus());
	  this.setStatusdate(datum);
	  this.setOrderdate(datum);
	  this.setErledigt(of.isErledigt());
	  if (of.getAnmerkungen()!=null) {this.setSystembemerkung(of.getAnmerkungen());} else {this.setSystembemerkung("");}
	  if (of.getNotizen()!=null) {this.setNotizen(of.getNotizen());} else {this.setNotizen("");}
	  this.setKaufpreis(of.getKaufpreis());
	  this.setWaehrung(of.getWaehrung());	  
  }
  
  
  public Bestellungen(Connection cn, Long id){

	  Bestellungen b = new Bestellungen();
	  
	  if (id!=null) {
	        
		  	PreparedStatement pstmt = null;
		  	ResultSet rs = null;
	        try {
	            pstmt = cn.prepareStatement("SELECT * FROM bestellungen b INNER JOIN konto k USING(KID)INNER JOIN benutzer u USING(UID)INNER JOIN lieferanten l USING(LID)WHERE b.bid=?");
	            pstmt.setLong(1, id);
	            rs = pstmt.executeQuery();

	            while (rs.next()) {
	                getBestellung(rs);
	                if (checkAnonymize(this)) b = anonymize(this);
	            }

	        } catch (Exception e) {
	        	log.error("Bestellungen(Connection cn, Long id): " + e.toString());
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
  
  /**
   * Holt bei einem Ill-Request die betreffende Bestellung um den Status nachzufahren
   * 
   * @param String trackingnr
   * @param String gbvnr
   * @return Bestellungen b
   */
  public Bestellungen(Connection cn, String trackingnr, String gbvnr){

	  Bestellungen b = new Bestellungen();
	        
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  try {
	       pstmt = cn.prepareStatement("SELECT * FROM bestellungen WHERE trackingnr=? AND gbvnr=?");
	       pstmt.setString(1, trackingnr);
	       pstmt.setString(2, gbvnr);
	       
	       rs = pstmt.executeQuery();

	            while (rs.next()) {
	                getBestellung(rs);
	            }

	        } catch (Exception e) {
	        	log.error("Bestellungen(Connection cn, String trackingnr, String gbvnr): " + e.toString());
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
   * 
   * @param vorkomma
   * @param nachkomma
   * @return
   */
  public BigDecimal stringToBigDecimal (String vorkomma, String nachkomma){
  	
  	BigDecimal bd = null;
      
  	if (vorkomma != null && nachkomma != null && 
  		(!vorkomma.equals("") || !nachkomma.equals("")) && // mind. ein Feld muss ausgefüllt sein
  		vorkomma.matches("[0-9]*") && nachkomma.matches("[0-9]*")) {
  	
  	bd = new BigDecimal("0.00");
  	
  	if (!vorkomma.equals("")) bd = new BigDecimal(vorkomma + ".00");
  	if (!nachkomma.equals("")) bd = bd.add(new BigDecimal(nachkomma).movePointLeft(nachkomma.length())); // exp = 1 => Leerstring  
      	
  	}
  	
      return bd;
  }
  
  /**
   * Speichert die Bestellung in der DB ab (hinterlegt die id im Bestellobjekt)
   * @param cn
   */
  public void save(Connection cn){
		
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;		
		try {
       		pstmt = setOrderValues(cn.prepareStatement( "INSERT INTO `bestellungen` (`KID` , " +
                   "`UID` , `LID` , `orderpriority` , `deloptions` , `fileformat` , `heft` , `seiten` , `issn` , `biblionr` , `bibliothek` , " +
                   "`autor` , `artikeltitel` , `jahrgang` , `zeitschrift` , `jahr` , `subitonr` , `gbvnr` , `trackingnr` , `internenr` , `systembemerkung` , `notizen` , `kaufpreis` , `waehrung` , " +
                   "`doi` , `pmid` , `isbn` , `mediatype` , `verlag` , `buchkapitel` , `buchtitel` , " +
                   "`orderdate` , `statedate` , `state`, `bestellquelle`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),this);
       		pstmt.executeUpdate();
           
           	// ID der gerade gespeicherten Bestellung ermitteln und in der bestellung hinterlegen
       		rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
       		if (rs.next()) {
       			this.setId(rs.getLong("LAST_INSERT_ID()"));
       		}
       	} catch (Exception e) {
       		log.error("save(Connection cn)" + e.toString());
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
  
  public void update(Connection cn){
	  
	  PreparedStatement pstmt = null;
	  
	  try {
          pstmt = setOrderValues(cn.prepareStatement( "UPDATE `bestellungen` SET KID=? , " +
                  "UID=?, LID=?, orderpriority=?, deloptions=?, fileformat=?, heft=?, seiten=?, issn=?, biblionr=?, bibliothek=? , " +
                  "autor=?, artikeltitel=?, jahrgang=?, zeitschrift=?, jahr=?, subitonr=?, gbvnr=?, trackingnr=?, internenr=?, systembemerkung=?, notizen=?, kaufpreis=?, waehrung=?, " +
                  "doi=?, pmid=?, isbn=?, mediatype=?, verlag=?, buchkapitel=?, buchtitel=?, orderdate=?, statedate=?, state=?, bestellquelle=? " +
                  "WHERE `BID` = " + this.getId()),this);            

          pstmt.executeUpdate();
          
      } catch (Exception e) {
    	  StringBuffer bf = new StringBuffer();
    	  bf.append("In Bestellung.update(Connection cn) trat folgender Fehler auf:\n");
    	  bf.append(e);
    	  bf.append("\n");
    	  bf.append("BID:\040" + this.getId() + "\n");
    	  bf.append("KID:\040" + this.getKonto().getId() + "\n");
    	  bf.append("UID:\040" + this.getBenutzer().getId() + "\n");
    	  bf.append("LID:\040" + this.getLieferant().getId() + "\n");
    	  bf.append("orderpriority:\040" + this.getPriority() + "\n");
    	  bf.append("deloptions:\040" + this.getDeloptions() + "\n");
    	  bf.append("fileformat:\040" + this.getFileformat() + "\n");
    	  bf.append("heft:\040" + this.getHeft() + "\n");
    	  bf.append("seiten:\040" + this.getSeiten() + "\n");
    	  bf.append("issn:\040" + this.getIssn() + "\n");
    	  bf.append("biblionr:\040" + this.getSigel() + "\n");
    	  bf.append("bibliothek:\040" + this.getBibliothek() + "\n");
    	  bf.append("autor:\040" + this.getAutor() + "\n");
    	  bf.append("artikeltitel:\040" + this.getArtikeltitel() + "\n");
    	  bf.append("jahrgang:\040" + this.getJahrgang() + "\n");
    	  bf.append("zeitschrift:\040" + this.getZeitschrift() + "\n");
    	  bf.append("jahr:\040" + this.getJahr() + "\n");
    	  bf.append("subitonr:\040" + this.getSubitonr() + "\n");
    	  bf.append("gbvnr:\040" + this.getGbvnr() + "\n");
    	  bf.append("trackingnr:\040" + this.getTrackingnr() + "\n");
    	  bf.append("internenr:\040" + this.getInterne_bestellnr() + "\n");
    	  bf.append("systembemerkungen:\040" + this.getSystembemerkung() + "\n");
    	  bf.append("notizen:\040" + this.getNotizen() + "\n");
    	  bf.append("kaufpreis:\040" + this.getKaufpreis() + "\n");
    	  bf.append("waehrung:\040" + this.getWaehrung() + "\n");
    	  bf.append("doi:\040" + this.getDoi() + "\n");
    	  bf.append("BID:\040" + this.getId() + "\n");
    	  bf.append("PMID:\040" + this.getPmid() + "\n");
    	  bf.append("isbn:\040" + this.getIsbn() + "\n");
    	  bf.append("mediatype:\040" + this.getMediatype() + "\n");
    	  bf.append("verlag:\040" + this.getVerlag() + "\n");
    	  bf.append("buchkapitel:\040" + this.getKapitel() + "\n");
    	  bf.append("buchtitel:\040" + this.getBuchtitel() + "\n");
    	  bf.append("orderdate:\040" + this.getOrderdate() + "\n");
    	  bf.append("statedate:\040" + this.getStatusdate() + "\n");
    	  bf.append("state:\040" + this.getStatustext() + "\n");
    	  bf.append("bestellquelle:\040" + this.getBestellquelle() + "\n");
		  log.error(bf.toString());
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
   * Löscht eine Bestellung aus der DB
   * 
   * @param Bestellungen b
   */
  public boolean deleteBestellung(Bestellungen b, Connection cn){
              
  	boolean success = false;
  	
  	  PreparedStatement pstmt = null;
      try {
          pstmt = cn.prepareStatement( "DELETE FROM `bestellungen` WHERE `BID` =?");           
          pstmt.setString(1, b.getId().toString());
          pstmt.executeUpdate();
          
          success = true;
          
      } catch (Exception e) {
    	  log.error("deleteBestellung(Bestellungen b, Connection cn): " + e.toString());
      } finally {
    	if (pstmt != null) {
    		try {
    			pstmt.close();
    		} catch (SQLException e) {
    			System.out.println(e);
    		}
    	}
    }
      
      return success;
  }
  
  /**
   * Sucht anhand eines Kontos und beliebigen Suchkriterien {@link ch.dbs.entity.Benutzer} Bestellungen heraus
   * @param PreparedStatememt pstmt
   * @return
   */
     public List<Bestellungen> searchOrdersPerKonto(PreparedStatement pstmt){
         ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
         ResultSet rs = null;
         try {
         	rs = pstmt.executeQuery();
         	while (rs.next()) {
         		Bestellungen b = new Bestellungen(rs);
                 if (checkAnonymize(b)) b = anonymize(b);
                 bl.add(b);
         	}

         } catch (Exception e) {
        	 log.error("searchOrdersPerKonto(PreparedStatement pstmt)" + e.toString());  	    
         } finally {
        	if (rs != null) {
        		try {
        			rs.close();
        		} catch (SQLException e) {
        			System.out.println(e);
        		}
        	}
        }
         
         return bl;
     }
     
     /**
      * Sucht anhand eines {@link ch.dbs.entity.Benutzer} seine Bestellungen heraus
      * <p></p>
      * @param the user {@link ch.dbs.entity.Benutzer}
      * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
      */
     public List<Bestellungen> getAllUserOrders(AbstractBenutzer u, String sort, String sortorder, String date_from, String date_to, Connection cn){
         ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try {
         	String sql = "SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE b.uid=? AND orderdate >= ? AND orderdate <= ? ORDER BY ";
         	sql = sortOrder(sql, sort, sortorder);
             pstmt = cn.prepareStatement(sql);
             pstmt.setString(1, u.getId().toString());
             pstmt.setString(2, date_from);
             pstmt.setString(3, date_to);
             rs = pstmt.executeQuery();
             long bid = 0;
             while (rs.next()) {
             	Bestellungen b = new Bestellungen();
             	if (bid!=rs.getLong("b.bid")){ //Bestellung abfüllen
             		bid=rs.getLong("b.bid");
                     b.setStatusdate(rs.getString("statedate"));
                     b.setOrderdate(rs.getString("orderdate"));
                     b.setStatustext(rs.getString("state"));
                     b = getBestellung(b, rs, cn);
                     if (checkAnonymize(b)) b = anonymize(b);
                     bl.add(b);
             	} else { //Bestellung bereits abgefüllt, nur noch korrekter Status setzen
             		b= (Bestellungen) bl.get(bl.size()-1);
             		b.setStatustext(rs.getString("state"));
             		b.setStatusdate(rs.getString("statedate"));
             		if (checkAnonymize(b)) b = anonymize(b);
             		bl.set(bl.size()-1, b);
             	}
             	
             }

         } catch (Exception e) {
        	 log.error("getAllUserOrders(AbstractBenutzer u, String sort, String sortorder, String date_from, String date_to, Connection cn): " + e.toString());
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
         return bl;
     } 
     
     /**
      * Sucht anhand eines {@link ch.dbs.entity.Benutzer} seine Bestellungen mit einem bestimmten Status heraus
      * <p></p>
      * @param the user {@link ch.dbs.entity.Benutzer}
      * @param String status
      * @param String sort
      * @param String sortorder
      * @param String date_from Anfang Datumsbereich
      * @param String date_to Ende Datumsbereich
      * @param boolean subitocheck (true => nur Subitobestellungen)
      * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
      */
     public List<Bestellungen> getAllUserOrdersPerStatus(AbstractBenutzer u, String status, String sort, String sortorder, String date_from, String date_to, boolean subitocheck, Connection cn){
         ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         try {
         	String mysql = "";
         	if (subitocheck == false) {
         		mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE b.uid=? AND state=? AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
         		if (status.equals("offen")) mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE b.uid=? AND NOT state='erledigt' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
         	}
         	if (subitocheck == true) {
         		mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE b.uid=? AND state=? AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
         		if (status.equals("offen")) mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE b.uid=? AND NOT state='erledigt' AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
         	}
             pstmt = cn.prepareStatement(mysql);
             pstmt.setString(1, u.getId().toString());
             if (!status.equals("offen")) {
            	 pstmt.setString(2, status);
            	 pstmt.setString(3, date_from);
            	 pstmt.setString(4, date_to);
             } else { // status.equals("offen") => kein Status setzen, das bereits in MySQL mit "AND NOT erledigt" hardcodiert.
            	 pstmt.setString(2, date_from);
            	 pstmt.setString(3, date_to);
             }
             rs = pstmt.executeQuery();
             long bid = 0;
             while (rs.next()) {
             	Bestellungen b = new Bestellungen();
             	if (bid!=rs.getLong("b.bid")){ //Bestellung abfüllen
             		bid=rs.getLong("b.bid");
                     b.setStatusdate(rs.getString("statedate"));
                     b.setOrderdate(rs.getString("orderdate"));
                     b.setStatustext(rs.getString("state"));
                     b = getBestellung(b, rs, cn);
                     if (checkAnonymize(b)) b = anonymize(b);
                     bl.add(b);
             	} else { //Bestellung bereits abgefüllt, nur noch korrekter Status setzen
             		b= (Bestellungen) bl.get(bl.size()-1);
             		b.setStatustext(rs.getString("state"));
             		b.setStatusdate(rs.getString("statedate"));
             		if (checkAnonymize(b)) b = anonymize(b);
             		bl.set(bl.size()-1, b);
             	}
             	
             }

         } catch (Exception e) {
        	 log.error("getAllUserOrdersPerStatus(AbstractBenutzer u, String status, String sort, String sortorder, String date_from, String date_to, boolean subitocheck, Connection cn): " + e.toString());
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
         return bl;
     } 
     
     /**
      * Sucht anhand eines Kontos {@link ch.dbs.entity.Benutzer} alle Bestellungen heraus
      * @param k Konto
      * @param sort Sortierkriterium
      * @param String date_from Anfang Datumsbereich
      * @param String date_to Ende Datumsbereich
      * @param sortorder Aufsteigend/Absteigend
      * @return
      */
        public List<Bestellungen> getOrdersPerKonto(Konto k, String sort, String sortorder, String date_from, String date_to, Connection cn){
            ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
            	String sql = "SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ORDER BY ";
            	sql = sortOrder(sql, sort, sortorder);
            	pstmt = cn.prepareStatement(sql);
            	pstmt.setString(1, k.getId().toString());
            	pstmt.setString(2, date_from);
           	 	pstmt.setString(3, date_to);
            	rs = pstmt.executeQuery();
            	while (rs.next()) {
            		Bestellungen b = new Bestellungen(rs);
                    if (checkAnonymize(b)) b = anonymize(b);
                    bl.add(b);
            	}

            } catch (Exception e) {
            	log.error("getOrdersPerKonto(Konto k, String sort, String sortorder, String date_from, String date_to, Connection cn): " + e.toString());
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
            return bl;
        }
        
        /**
         * Sucht anhand eines Kontos {@link ch.dbs.entity.Benutzer} alle offenen Bestellungen mit einem bestimmten Status heraus
         * @param long KID
         * @param String status
         * @param sort Sortierkriterium
         * @param sortorder Aufsteigend/Absteigend
         * @param String date_from Anfang Datumsbereich
         * @param String date_to Ende Datumsbereich
         * @param boolean subitocheck (true => nur Subitobestellungen)
         * @return
         */
           public List<Bestellungen> getOrdersPerKontoPerStatus(long KID, String status, String sort, String sortorder, String date_from, String date_to, boolean subitocheck, Connection cn){
               ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
               PreparedStatement pstmt = null;
               ResultSet rs = null;
               try {
            	String mysql = "";
            	if (subitocheck == false) {
            		mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND state=? AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
            		if (status.equals("offen")) mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND NOT state='erledigt' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
            	}
            	if (subitocheck == true) {
            		mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND state=? AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
            		if (status.equals("offen")) mysql = sortOrder("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND NOT state='erledigt' AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
            	}
            	
               	pstmt = cn.prepareStatement(mysql);
               	pstmt.setString(1, String.valueOf(KID));
               	if (!status.equals("offen")) {
               		pstmt.setString(2, status);
               		pstmt.setString(3, date_from);
               		pstmt.setString(4, date_to);
               	} else { // status.equals("offen") => kein Status setzen, das bereits in MySQL mit "AND NOT erledigt" hardcodiert.
            	 pstmt.setString(2, date_from);
            	 pstmt.setString(3, date_to);               		
               	}
               	rs = pstmt.executeQuery();
               	while (rs.next()) {
               		Bestellungen b = new Bestellungen();
                    b.setStatusdate(rs.getString("statedate"));
                    b.setOrderdate(rs.getString("orderdate"));
                    b.setStatustext(rs.getString("state"));
                    b = getBestellung(b, rs, cn);
                    if (checkAnonymize(b)) b = anonymize(b);
                    bl.add(b);                   
                       
               	}

               } catch (Exception e) {
            	   log.error("getOrdersPerKontoPerStatus(long KID, String status, String sort, String sortorder, String date_from, String date_to, boolean subitocheck, Connection cn): " + e.toString());
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
               return bl;
           }
           
     /**
     * Sucht anhand seiner Werte eine seine Bestellungen heraus
     * <p></p>
     * @param the user {@link ch.dbs.entity.Bestellungen}
     * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
     */
    public Bestellungen getOrderSimpleWay(Bestellungen b, Connection cn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
    	try {
			pstmt = cn.prepareStatement("SELECT * FROM bestellungen WHERE uid=? and kid=? "
							+ "and heft=? and seiten=? and issn=? and jahr=? and jahrgang=? and orderdate=?");
			pstmt.setString(1, b.getBenutzer().getId().toString());
			pstmt.setString(2, b.getKonto().getId().toString());
			if (b.getHeft() != null) {
				pstmt.setString(3, b.getHeft());
			} else {
				pstmt.setString(3, "");
			}
			if (b.getSeiten() != null) {
				pstmt.setString(4, b.getSeiten());
			} else {
				pstmt.setString(4, "");
			}
			pstmt.setString(5, b.getIssn()); // in DB kann ISSN null sein
			if (b.getJahr() != null) {
				pstmt.setString(6, b.getJahr());
			} else {
				pstmt.setString(6, "");
			}
			if (b.getJahrgang() != null) {
				pstmt.setString(7, b.getJahrgang());
			} else {
				pstmt.setString(7, "");
			}
			pstmt.setString(8, b.getOrderdate());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				b = getBestellung(b, rs, cn);
			}

		} catch (Exception e) {
			log.error("getOrderSimpleWay(Bestellungen b, Connection cn)" + e.toString());
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
		return b;
	}
           
           /**
            * Sucht anhand der uid und kid alle Bestellungen heraus
            * <p></p>
            * Der Rückgabewert ist die Anzahl Bestellungen pro User im laufenden Kalenderjahr
            * <p></p>
            * @param Long uid, kid
            * @return int anzahl
            */
           public int countOrdersPerUser(String uid, Konto k, Connection cn){
               int anzahl = 0;
               PreparedStatement pstmt = null;
               ResultSet rs = null;
               try {
             	  Calendar cal = Calendar.getInstance();
             	  cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
             	  String datum = String.format("%1$tY-01-01 00:00:00", cal); // Kalenderjahr berechnen
               	// SQL ausführen
                   pstmt = cn.prepareStatement("SELECT count(bid) FROM `bestellungen` WHERE `KID` = ? AND `UID` = ? AND `orderdate` >= ?");
                   pstmt.setString(1, k.getId().toString());
                   pstmt.setString(2, uid);
                   pstmt.setString(3, datum);
                   rs = pstmt.executeQuery();
                   while (rs.next()) {
                   anzahl = rs.getInt("count(bid)");
                   }

               } catch (Exception e) {
            	   log.error("countOrdersPerUser(String uid, Long kid, Connection cn): " + e.toString());
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
               return anzahl;
           }
           
           /**
       	 * Zählt anhand eines Kontos {@link ch.dbs.entity.Benutzer} alle
       	 * Bestellungen ohne bestimmten Status
       	 * 
       	 * @param k
       	 *            Konto
       	 * @param sort
       	 *            Sortierkriterium
       	 * @param sortorder
       	 *            Aufsteigend/Absteigend
       	 * @param String date_from Anfang Datumsbereich
       	 * @param String date_to Ende Datumsbereich
       	 * @return
       	 */
        public List<OrderStatistikForm> countOrdersPerKonto(Konto k, String sort,
			String sortorder, String date_from, String date_to, Connection cn) {
		ArrayList<OrderStatistikForm> auflistung = new ArrayList<OrderStatistikForm>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = cn.prepareStatement(sortOrder("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder));
			pstmt.setString(1, k.getId().toString());
			pstmt.setString(2, date_from);
       	 	pstmt.setString(3, date_to);
			rs = pstmt.executeQuery();

			int total = 0;
			while (rs.next()) {
				OrderStatistikForm osf = new OrderStatistikForm();
				osf.setAnzahl(rs.getInt("count(bid)"));
				total = total + osf.getAnzahl();
				osf.setTotal(total);
				auflistung.add(osf);
			}

		} catch (Exception e) {
			log.error("countOrdersPerKonto(Konto k, String sort, String sortorder, String date_from, String date_to, Connection cn): " + e.toString());
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
		return auflistung;
	}
        
        /**
         * Zählt anhand eines Kontos {@link ch.dbs.entity.Konto} alle Bestellungen mit einem bestimmten Status
         * @param long KID
         * @param String status
         * @param sort Sortierkriterium
         * @param sortorder Aufsteigend/Absteigend
         * @param String date_from Anfang Datumsbereich
         * @param String date_to Ende Datumsbereich
         * @param boolean subitocheck (true => nur Subitobestellungen)
         * @return
         */
       public List<OrderStatistikForm> countOrdersPerKontoPerStatus(long KID,
			String status, String sort, String sortorder, String date_from,
			String date_to, boolean subitocheck, Connection cn) {
		ArrayList<OrderStatistikForm> auflistung = new ArrayList<OrderStatistikForm>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String mysql = "";
			if (subitocheck == false) {
				mysql = sortOrder("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND state=? AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
				if (status.equals("offen"))
					mysql = sortOrder("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND NOT state='erledigt' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
			}
			if (subitocheck == true) {
				mysql = sortOrder("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND state=? AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
				if (status.equals("offen"))
					mysql = sortOrder("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND NOT state='erledigt' AND NOT subitonr='' AND orderdate >= ? AND orderdate <= ? ORDER BY ", sort, sortorder);
			}

			pstmt = cn.prepareStatement(mysql);
			pstmt.setString(1, String.valueOf(KID));
			if (!status.equals("offen")) {
				pstmt.setString(2, status);
				pstmt.setString(3, date_from);
	       	 	pstmt.setString(4, date_to);
			} else { // status.equals("offen") => kein Status setzen, das bereits in MySQL mit "AND NOT erledigt" hardcodiert.
				pstmt.setString(2, date_from);
	       	 	pstmt.setString(3, date_to);
			}
			rs = pstmt.executeQuery();
			int total = 0;
			while (rs.next()) {
				OrderStatistikForm osf = new OrderStatistikForm();
				osf.setAnzahl(rs.getInt("count(bid)"));
				total = total + osf.getAnzahl();
				osf.setTotal(total);
				auflistung.add(osf);
			}

		} catch (Exception e) {
			log.error("countOrdersPerKontoPerStatus(long KID, String status, String sort, String sortorder, String date_from, String date_to, boolean subitocheck, Connection cn): " + e.toString());
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
		return auflistung;
	}
           
           /**
            * Sucht anhand der kid alle Bestellungen für das laufende Kalenderjahr heraus
            * <p></p>
            * <p></p>
            * @param Long kid
            * @return int anzahl
            */
           public int allOrdersThisYearForKonto(Konto k, Connection cn){
               int anzahl = 0;
               PreparedStatement pstmt = null;
               ResultSet rs = null;
               try {
             	  Calendar cal = Calendar.getInstance();
             	  cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
             	  String datum = String.format("%1$tY-01-01 00:00:00", cal); // Kalenderjahr berechnen
               	// SQL ausführen
                   pstmt = cn.prepareStatement("SELECT count(bid) FROM `bestellungen` WHERE `KID` = ? AND `orderdate` >= ?");
                   pstmt.setString(1, k.getId().toString());
                   pstmt.setString(2, datum);
                   rs = pstmt.executeQuery();
                   while (rs.next()) {
                   anzahl = rs.getInt("count(bid)");
                   }

               } catch (Exception e) {
            	   log.error("allOrdersThisYearForKonto(Long kid, Connection cn): " + e.toString());
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
               return anzahl;
           }
     
     /**
      * Stellt die korrekte Sortierung her
      * Für die Sortierung nach Buchtitel oder Zeitschrift müssen in der Übersicht die beiden Felder zusammengezogen werden...
      * @param String sql
      * @return
      */
        public String sortOrder(String sql, String sort, String sortorder){
      	  
      	  if (sort.equals("zeitschrift") || sort.equals("buchtitel")) {
      		  sql = sql + "CONCAT( zeitschrift, buchtitel ) " + sortorder;
      		  } else {
      			  if (sort.equals("artikeltitel") || sort.equals("kapitel")) {
      				  sql = sql + "CONCAT( artikeltitel, buchkapitel ) " + sortorder;
      				  } else {
      					  sql = sql + sort + "\040" + sortorder;
      					  }
      			  }
      	  
            
            return sql;
        }
     
     /**
      * Prüft, ob eine Bestellung anonymisiert werden muss
      * <p></p>
      * @param Bestellungen b
      * @return true/false
      */
 	public boolean checkAnonymize(Bestellungen b){ 
 		boolean check = false;
 		
 		if (b.getBenutzer()!=null && ReadSystemConfigurations.isAnonymizationActivated()) {
 		Calendar cal = stringFromMysqlToCal(b.getOrderdate());
 		Calendar limit = Calendar.getInstance();
 		limit.setTimeZone(TimeZone.getTimeZone(ReadSystemConfigurations.getSystemTimezone())); // takes SystemTimezone due to pratical reasons
 		limit.add(Calendar.MONTH, -ReadSystemConfigurations.getAnonymizationAfterMonths());
 		limit.add(Calendar.DAY_OF_MONTH, -1);
 		if (cal.before(limit)) {
 			check = true;
 		}
 		}
         	
 		return check;
 	}
     
     /**
      * Anonymisiert Bestellungen für die Ausgabe
      * <p></p>
      * @param Bestellungen b
      * @return Bestellungen b
      */
 	private Bestellungen anonymize(Bestellungen b){ 
 		
 		if (b.getBenutzer()!=null && ReadSystemConfigurations.isAnonymizationActivated()) {

 			AbstractBenutzer anon = b.getBenutzer();
 			anon.setName("anonymized");
 			anon.setVorname("");
 			anon.setEmail("");
 			b.setSystembemerkung("");
// 			b.setNotizen(""); // vorläufig deaktiviert, für Suche sinnvoll, Kunden hinterlegen wichtige Infos...
 			b.setBenutzer(anon);
 			
 		}
         	
 		return b;
 	}
 	
    /**
     * Konvertiert einen Datums-String aus MYSQL in ein Calendar-Objekt
     * <p></p>
     * @param String datum
     * @return cal cal
     */
	public Calendar stringFromMysqlToCal(String datum){ // 2007-11-01 08:56:07.0
        
        Date dateParsed = new Date();
        Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(ReadSystemConfigurations.getSystemTimezone()));
        
        try {
        ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        dateParsed = sdf.parse(datum);
        } catch (ParseException e) {
        	log.error("stringFromMysqlToCal(String datum):\012" + e.toString());
        }
        
		cal.setTime(dateParsed);
		
		return cal;
	}
	
    /**
     * Entfernt aus einem Datums-String mit Milliseconds aus MYSQL die Milliseconds.
     * Seit MySQL-Connector 5.0 kommen jeweils Milliseconds mit.
     * <p></p>
     * @param String datum
     * @return String datum
     */
	public String removeMilliseconds(String datum){ // 2007-11-01 08:56:07.0

        try {        	
        	if (datum.contains(".")) {
        		datum = datum.substring(0, datum.indexOf("."));
        	}
        } catch (Exception e) {
        	log.error("removeMilliseconds(String datum):\012" + e.toString());
        }
		
		return datum;
	}
  
  /*
   * Erstellt ein Bestellungsobjekt aus einer Zeile aus der Datenbank
   */
  private Bestellungen(ResultSet rs){
	  try {
		getBestellung(rs);
	} catch (Exception e) {
		log.error("Bestellungen(ResultSet rs: " + e.toString());
	}
  }
  
  private void getBestellung(ResultSet rs) throws Exception{
	  this.setId(rs.getLong("BID"));
	  
//		Falls vorname nicht im rs ist kein Benutzer abfüllen
	      try {	rs.findColumn("vorname");
	      		AbstractBenutzer b = new AbstractBenutzer();
	        	this.setBenutzer(b.getUser(rs));    	} 
	        catch (SQLException se) { log.ludicrous("getBestellung(ResultSet rs) Pos. 1: " + se.toString()); }	  
	      //Falls biblioname nicht im rs is kein Konto abfüllen
	      try {	rs.findColumn("biblioname");
	      		this.setKonto(new Konto(rs));    	} 
	      catch (SQLException se) { log.ludicrous("getBestellung(ResultSet rs) Pos. 2: " + se.toString()); }
	    //Falls CH nicht im rs ist kein Lieferant abfüllen
	      try {	rs.findColumn("CH");
	      		this.setLieferant(new Lieferanten(rs));    	} 
	      catch (SQLException se) {
	    	  log.ludicrous("getBestellung(ResultSet rs) Pos. 3: " + se.toString());
	    	  Lieferanten l = new Lieferanten();
	    	  l.setLid(Long.valueOf(1));
	    	  l.setName("k.A.");
	    	  this.setLieferant(l);
	    	  }
	      this.setBestellquelle(rs.getString("bestellquelle"));
	      if (this.getBestellquelle()==null || this.getBestellquelle().equals("") || this.getBestellquelle().equals("0")) this.setBestellquelle("k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
	      this.setPriority(rs.getString("orderpriority"));
	      this.setFileformat(rs.getString("fileformat")); 
	      this.setDeloptions(rs.getString("deloptions"));
	      this.setAutor(rs.getString("autor"));
	      this.setArtikeltitel(rs.getString("artikeltitel"));
	      this.setJahr(rs.getString("jahr"));
	      this.setJahrgang(rs.getString("jahrgang"));
	      this.setHeft(rs.getString("heft"));
	      this.setSeiten(rs.getString("seiten"));
	      this.setIssn(rs.getString("issn"));
	      this.setSubitonr(rs.getString("subitonr"));
	      this.setGbvnr(rs.getString("gbvnr"));
	      this.setTrackingnr(rs.getString("trackingnr"));
	      this.setInterne_bestellnr(rs.getString("internenr"));
	      this.setSigel(rs.getString("biblionr"));
	      this.setBibliothek(rs.getString("bibliothek"));
	      this.setSystembemerkung(rs.getString("systembemerkung"));
	      this.setNotizen(rs.getString("notizen"));        
	      this.setZeitschrift(rs.getString("zeitschrift"));
	      this.setStatustext(rs.getString("state"));
	      this.setStatusdate(removeMilliseconds(rs.getString("statedate")));
	      this.setOrderdate(removeMilliseconds(rs.getString("orderdate")));
	      this.setErledigt((rs.getBoolean("erledigt")));
	      this.setKaufpreis(rs.getBigDecimal("kaufpreis")); // Achtung kann NULL sein...
	      this.setWaehrung(rs.getString("waehrung"));
	      this.setDoi(rs.getString("doi"));
	      this.setPmid(rs.getString("pmid"));
	      this.setIsbn(rs.getString("isbn"));
	      this.setMediatype(rs.getString("mediatype"));
	      this.setVerlag(rs.getString("verlag"));
	      this.setKapitel(rs.getString("buchkapitel"));
	      this.setBuchtitel(rs.getString("buchtitel"));
  }
  
  /*
   * Füllt ein Bestellungsobjekt mit einer Zeile aus der Datenbank
   */
  private Bestellungen getBestellung(Bestellungen b, ResultSet rs, Connection cn) throws Exception{

      b.setId(rs.getLong("BID"));
      Lieferanten lieferantenInstance = new Lieferanten();
      AbstractBenutzer ab = new AbstractBenutzer();
      
      try {
      	rs.findColumn("biblioname");// Braucht es nicht ist ein Beispiel
      	b.setBenutzer(ab.getUser(rs));
      	b.setKonto(new Konto(rs));
      	} 
      catch (SQLException se) {
      		//einfach nix machen ;-)
    	  log.error("getBestellungen(Bestellungen b, ResultSet rs, Connection cn): " + se.toString());
      	}
      b.setZeitschrift(rs.getString("zeitschrift"));
      b.setAutor(rs.getString("autor"));
      b.setArtikeltitel(rs.getString("artikeltitel"));
      b.setJahr(rs.getString("jahr"));
      b.setJahrgang(rs.getString("jahrgang"));
      b.setHeft(rs.getString("heft"));
      b.setSeiten(rs.getString("seiten"));
      b.setIssn(rs.getString("issn"));
      b.setSubitonr(rs.getString("subitonr"));
      b.setGbvnr(rs.getString("gbvnr"));
      b.setTrackingnr(rs.getString("trackingnr"));
      b.setInterne_bestellnr(rs.getString("internenr"));
      b.setSigel(rs.getString("biblionr"));
      b.setBibliothek(rs.getString("bibliothek"));
      b.setSystembemerkung(rs.getString("systembemerkung"));
      b.setNotizen(rs.getString("notizen"));
      if (rs.getString("kaufpreis")!=null) b.setKaufpreis(rs.getBigDecimal("kaufpreis")); // Null-Werte nicht als 0.0 abfüllen...!
      b.setWaehrung(rs.getString("waehrung"));
      b.setDoi(rs.getString("doi"));
      b.setPmid(rs.getString("pmid"));
      b.setIsbn(rs.getString("isbn"));
      b.setMediatype(rs.getString("mediatype"));
      b.setVerlag(rs.getString("verlag"));
      b.setKapitel(rs.getString("buchkapitel"));
      b.setBuchtitel(rs.getString("buchtitel"));
      b.setPriority(rs.getString("orderpriority"));
      b.setDeloptions(rs.getString("deloptions"));
      b.setFileformat(rs.getString("fileformat"));
      
      b.setLieferant(lieferantenInstance.getLieferantFromLid(rs.getString("lid"), cn));
      b.setBestellquelle(rs.getString("bestellquelle"));
      if (b.getBestellquelle()==null || b.getBestellquelle().equals("") || b.getBestellquelle().equals("0")) b.setBestellquelle("k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
      b.setStatustext(rs.getString("state"));
      b.setStatusdate(removeMilliseconds(rs.getString("statedate")));
      b.setOrderdate(removeMilliseconds(rs.getString("orderdate")));

      return b;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Konto heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countOrdersPerKonto(Long kid, String date_from, String date_to, Connection cn){

      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
          pstmt = cn.prepareStatement("SELECT BID, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY BID ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          
          while (rs.next()) {
				OrderStatistikForm osf = new OrderStatistikForm();
				anzahl = rs.getInt("anzahl");
				total = total + anzahl;
				osf.setAnzahl(anzahl);
				list.add(osf);
			}

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countOrdersPerKonto(Long kid, String date_from, String date_to, Connection cn)" + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen und Kosten pro Lieferant heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countLieferantPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, bestellquelle, waehrung, bestellquelle, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY bestellquelle ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          anzahl = rs.getInt("anzahl");          
          total = total + anzahl;
          label = rs.getString("bestellquelle");
          if (label == null || label.equals("") || label.equals("0")) label = "k.A.";
          osf.setLabel(label);
          osf.setAnzahl(anzahl);
          osf.setPreiswaehrung(costsPerField(kid, date_from, date_to, "bestellquelle", label, cn));
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countLieferantPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl kostenpflichtigen und gratis Bestellungen heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countGratisKostenpflichtigPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();

      int gratis = 0;
      int kostenpflichtig = 0;
      int unknown = 0;
      int totalbestellungen = 0;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT kaufpreis, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY kaufpreis ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          if (rs.getString("kaufpreis") !=null) { // null = k.A.
       	   
       	   if (rs.getFloat("kaufpreis") != 0.0) { // 0.0 = gratis
       		   int x = rs.getInt("anzahl");
       		   kostenpflichtig = kostenpflichtig + x;
       		   totalbestellungen = totalbestellungen + x;
       		   
       	   } else {
       		   int x = rs.getInt("anzahl");
       		   gratis = gratis + x;
       		   totalbestellungen = totalbestellungen + x;            		   
       	   }
       	   
          } else { // k.A.
       	   unknown = rs.getInt("anzahl");         	   
       	   totalbestellungen = totalbestellungen + unknown;            	   
          }
          
          }
          
          OrderStatistikForm ok = new OrderStatistikForm();
          	ok.setLabel("kostenpflichtig");
     		ok.setAnzahl(kostenpflichtig);
     		list.add(ok);
     		
     	   OrderStatistikForm og = new OrderStatistikForm();
     		og.setLabel("gratis");
     		og.setAnzahl(gratis);
     		list.add(og);
     		
     	   OrderStatistikForm oka = new OrderStatistikForm();
     		oka.setLabel("k.A.");
     		oka.setAnzahl(unknown);
     		list.add(oka);
     		
     	   OrderStatistikForm osf = new OrderStatistikForm();
     		osf.setLabel("Total");
     		osf.setAnzahl(totalbestellungen);
     		list.add(osf);

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen 

      } catch (Exception e) {
    	  log.error("countGratisKostenpflichtigPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Berechnet die Kosten in den einzelnen Waehrungen anhand der kid und eines Zeitraumes
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm sumGratisKostenpflichtigPerKonto(Long kid, String date_from, String date_to, Connection cn){
      
	  OrderStatistikForm cost = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, waehrung FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung ORDER BY summe DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          if (rs.getString("waehrung") !=null) { // null = k.A.
        	   OrderStatistikForm osf = new OrderStatistikForm();       	   
        	   osf.setLabel(rs.getString("waehrung"));
               NumberFormat nf = NumberFormat.getInstance();
               nf.setMinimumFractionDigits(2);
               nf.setMaximumFractionDigits(2);
               osf.setPreis(nf.format(rs.getFloat("summe")));
        	   list.add(osf);       	   
          } else {
        	// k.A. => nichts zu berechnen...
          }          
          }

          cost.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen 

      } catch (Exception e) {
    	  log.error("sumGratisKostenpflichtigPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return cost;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Lieferart heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countLieferartPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT deloptions, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY deloptions ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          anzahl = rs.getInt("anzahl");
          total = total + anzahl;
          label = rs.getString("deloptions");
          osf.setLabel(label);
          osf.setAnzahl(anzahl);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countLieferartPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Medientyp heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countMediatypePerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT mediatype, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY mediatype ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          anzahl = rs.getInt("anzahl");
          total = total + anzahl;
          label = rs.getString("mediatype");
          osf.setLabel(label);
          osf.setAnzahl(anzahl);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countMediatypePerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Fileformat heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countFileformatPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT fileformat, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY fileformat ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          anzahl = rs.getInt("anzahl");
          total = total + anzahl;
          label = rs.getString("fileformat");
          //TODO: leere Angaben abfangen...
          osf.setLabel(label);
          osf.setAnzahl(anzahl);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countFileformatPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Priorität heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countPriorityPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      int unknown = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT orderpriority, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY orderpriority ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          anzahl = rs.getInt("anzahl");
          total = total + anzahl;
          label = rs.getString("orderpriority");
          	if (!label.equals("") && !label.equals("0")) {
          		osf.setLabel(label);
          		osf.setAnzahl(anzahl);
          		list.add(osf);
          		} else {
          			unknown = unknown + anzahl;
          		}
          }
          
          if (unknown > 0) {
          OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekannter Prio als "k.A." aufgeführt...
          osf.setLabel("k.A.");
          osf.setAnzahl(unknown);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countPriorityPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Gender heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countGenderPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int bestellungen = 0;
      int totalbestellungen = 0;
      int total = 0;
      int unknown = 0;
      int unknownbestellungen = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT anrede, UID, COUNT(*) AS anzahl FROM ( SELECT anrede, UID, COUNT(*) AS z FROM ( SELECT anrede, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x GROUP BY anrede ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label = "";
          if (rs.getString("anrede").equals("Herr")) label = "Mann";
          if (rs.getString("anrede").equals("Frau")) label = "Frau";
          anzahl = rs.getInt("anzahl");
          bestellungen = countRowsPerFeld(kid, date_from, date_to, "anrede", rs.getString("anrede"), cn);
          total = total + anzahl;
          totalbestellungen = totalbestellungen + bestellungen;
          	if (!label.equals("") && !label.equals("0")) {
          		osf.setLabel(label);
          		osf.setAnzahl(anzahl);
          		osf.setAnzahl_two(bestellungen);
          		osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, date_from, date_to, "anrede", rs.getString("anrede"), cn));
          		list.add(osf);
          		} else {
          			unknown = unknown + anzahl;
          			unknownbestellungen = unknownbestellungen + bestellungen;
          		}
          }
          
          if (unknown > 0) {
          OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekanntem Gender als "k.A." aufgeführt...
          osf.setLabel("k.A.");
          osf.setAnzahl(unknown);
          osf.setAnzahl_two(unknownbestellungen);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);
          os.setTotal_two(totalbestellungen);

      } catch (Exception e) {
    	  log.error("countGenderPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Institution heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countInstPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int bestellungen = 0;
      int totalbestellungen = 0;
      int total = 0;
      int unknown = 0;
      int unknownbestellungen = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT institut, UID, COUNT(*) AS anzahl FROM ( SELECT institut, UID, COUNT(*) AS z FROM ( SELECT institut, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x GROUP BY institut ORDER BY anzahl DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label = "";
          label = rs.getString("institut");
          anzahl = rs.getInt("anzahl");
          bestellungen = countRowsPerFeld(kid, date_from, date_to, "institut", rs.getString("institut"), cn);
          total = total + anzahl;
          totalbestellungen = totalbestellungen + bestellungen;
          	if (!label.equals("") && !label.equals("0")) {
          		osf.setLabel(label);
          		osf.setAnzahl(anzahl);
          		osf.setAnzahl_two(bestellungen);
          		osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, date_from, date_to, "institut", label, cn));
          		list.add(osf);
          		} else {
          			unknown = unknown + anzahl;
          			unknownbestellungen = unknownbestellungen + bestellungen;
          		}
          }
          
          if (unknown > 0) {
          OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekanntem Institut als "k.A." aufgeführt...
          osf.setLabel("k.A.");
          osf.setAnzahl(unknown);
          osf.setAnzahl_two(unknownbestellungen);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);
          os.setTotal_two(totalbestellungen);

      } catch (Exception e) {
    	  log.error("countInstPerKonto(Long kid, String date_from, String date_to, Connection cn)" + e.toString());
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
      return os;
  }
  
  /**
	 * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
	 * Abteilung heraus
	 * <p>
	 * </p>
	 * Der Rückgabewert ist ein OrderStatistikForm
	 * <p>
	 * </p>
	 * 
	 * @param Long
	 *            kid String date_from String date_to Connection cn
	 * @return OrderStatistikForm os
	 */
 public OrderStatistikForm countAbteilungPerKonto(Long kid, String date_from, String date_to, Connection cn){
     OrderStatistikForm os = new OrderStatistikForm();
     ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
     int anzahl = 0;
     int bestellungen = 0;
     int totalbestellungen = 0;
     int total = 0;
     int unknown = 0;
     int unknownbestellungen = 0;
     PreparedStatement pstmt = null;
     ResultSet rs = null;
     try {
     	// SQL ausführen
         pstmt = cn.prepareStatement("SELECT abteilung, UID, COUNT(*) AS anzahl FROM ( SELECT abteilung, UID, COUNT(*) AS z FROM ( SELECT abteilung, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x GROUP BY abteilung ORDER BY anzahl DESC");
         pstmt.setString(1, kid.toString());
         pstmt.setString(2, date_from);
         pstmt.setString(3, date_to);

         rs = pstmt.executeQuery();
         while (rs.next()) {
         OrderStatistikForm osf = new OrderStatistikForm();
         String label = "";
         label = rs.getString("abteilung");
         anzahl = rs.getInt("anzahl");
         bestellungen = countRowsPerFeld(kid, date_from, date_to, "abteilung", rs.getString("abteilung"), cn);
         total = total + anzahl;
         totalbestellungen = totalbestellungen + bestellungen;
         	if (!label.equals("") && !label.equals("0")) {
         		osf.setLabel(label);
         		osf.setAnzahl(anzahl);
         		osf.setAnzahl_two(bestellungen);
         		osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, date_from, date_to, "abteilung", label, cn));
         		list.add(osf);
         		} else {
         			unknown = unknown + anzahl;
         			unknownbestellungen = unknownbestellungen + bestellungen;
         		}
         }
         
         if (unknown > 0) {
         OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekannter Abteilung als "k.A." aufgeführt...
         osf.setLabel("k.A.");
         osf.setAnzahl(unknown);
         osf.setAnzahl_two(unknownbestellungen);
         list.add(osf);
         }

         os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
         os.setTotal(total);
         os.setTotal_two(totalbestellungen);

     } catch (Exception e) {
    	 log.error("countAbteilungPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
     return os;
 }
 
 /**
  * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro PLZ heraus
  * <p></p>
  * Der Rückgabewert ist ein OrderStatistikForm
  * <p></p>
  * @param Long kid String date_from String date_to Connection cn
  * @return OrderStatistikForm os
  */
 public OrderStatistikForm countPLZPerKonto(Long kid, String date_from, String date_to, Connection cn){
     OrderStatistikForm os = new OrderStatistikForm();
     ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
     int anzahl = 0;
     int bestellungen = 0;
     int totalbestellungen = 0;
     int total = 0;
     int unknown = 0;
     int unknownbestellungen = 0;
     PreparedStatement pstmt = null;
     ResultSet rs = null;
     try {
     	// SQL ausführen
         pstmt = cn.prepareStatement("SELECT plz, ort, UID, COUNT(*) AS anzahl FROM ( SELECT plz, ort, UID, COUNT(*) AS z FROM ( SELECT u.plz, u.ort, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x GROUP BY plz ORDER BY plz ASC");
         pstmt.setString(1, kid.toString());
         pstmt.setString(2, date_from);
         pstmt.setString(3, date_to);

         rs = pstmt.executeQuery();
         while (rs.next()) {
         OrderStatistikForm osf = new OrderStatistikForm();
         String label = "";
         String label_two = "";
         label = rs.getString("plz");
         label_two = rs.getString("ort");
         anzahl = rs.getInt("anzahl");
         bestellungen = countRowsPerFeld(kid, date_from, date_to, "plz", rs.getString("plz"), cn);
         total = total + anzahl;
         totalbestellungen = totalbestellungen + bestellungen;
         	if (!label.equals("") && !label.equals("0")) {
         		osf.setLabel(label);
         		osf.setLabel_two(label_two);
         		osf.setAnzahl(anzahl);
         		osf.setAnzahl_two(bestellungen);
         		list.add(osf);
         		} else {
         			unknown = unknown + anzahl;
         			unknownbestellungen = unknownbestellungen + bestellungen;
         		}
         }
         
         if (unknown > 0) {
         OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekannter PLZ als "k.A." aufgeführt...
         osf.setLabel("k.A.");
         osf.setAnzahl(unknown);
         osf.setAnzahl_two(unknownbestellungen);
         list.add(osf);
         }

         os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
         os.setTotal(total);
         os.setTotal_two(totalbestellungen);

     } catch (Exception e) {
    	 log.error("countPLZPerKonto(Long kid, String date_from, String date_to, Connection cn)" + e.toString());
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
     return os;
 }
 
 /**
  * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Land heraus
  * <p></p>
  * Der Rückgabewert ist ein OrderStatistikForm
  * <p></p>
  * @param Long kid String date_from String date_to Connection cn
  * @return OrderStatistikForm os
  */
 public OrderStatistikForm countLandPerKonto(Long kid, String date_from, String date_to, Connection cn){
     OrderStatistikForm os = new OrderStatistikForm();
     ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
     int anzahl = 0;
     int bestellungen = 0;
     int totalbestellungen = 0;
     int total = 0;
     int unknown = 0;
     int unknownbestellungen = 0;
     PreparedStatement pstmt = null;
     ResultSet rs = null;
     try {
     	// SQL ausführen
         pstmt = cn.prepareStatement("SELECT land, UID, COUNT(*) AS anzahl FROM ( SELECT land, UID, COUNT(*) AS z FROM ( SELECT u.land, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x GROUP BY land ORDER BY anzahl DESC");
         pstmt.setString(1, kid.toString());
         pstmt.setString(2, date_from);
         pstmt.setString(3, date_to);

         rs = pstmt.executeQuery();
         while (rs.next()) {
         OrderStatistikForm osf = new OrderStatistikForm();
         String label = "";
         label = rs.getString("land");
         anzahl = rs.getInt("anzahl");
         bestellungen = countRowsPerFeld(kid, date_from, date_to, "land", rs.getString("land"), cn);
         total = total + anzahl;
         totalbestellungen = totalbestellungen + bestellungen;
         	if (!label.equals("") && !label.equals("0")) {
         		osf.setLabel(label);
         		osf.setAnzahl(anzahl);
         		osf.setAnzahl_two(bestellungen);
         		list.add(osf);
         		} else {
         			unknown = unknown + anzahl;
         			unknownbestellungen = unknownbestellungen + bestellungen;
         		}
         }
         
         if (unknown > 0) {
         OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekanntem Land als "k.A." aufgeführt...
         osf.setLabel("k.A.");
         osf.setAnzahl(unknown);
         osf.setAnzahl_two(unknownbestellungen);
         list.add(osf);
         }

         os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
         os.setTotal(total);
         os.setTotal_two(totalbestellungen);

     } catch (Exception e) {
    	 log.error("countLandPerKonto(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
     return os;
 }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro Zeitschrift heraus
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countISSNPerKonto(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      int andere = 0;
      
      int user = 0;
      int usertotal = 0;
      
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          	pstmt = cn.prepareStatement("SELECT issn, zeitschrift, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY issn ORDER BY anzahl DESC");
          	pstmt.setString(1, kid.toString());
            pstmt.setString(2, date_from);
            pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          String label_two ="";
          anzahl = rs.getInt("anzahl");
          label = rs.getString("issn");
          label_two = rs.getString("zeitschrift");
          total = total + anzahl;
          user = countRowsUIDPerISSN(kid, date_from, date_to, label, cn);
          usertotal = usertotal + user;
          if (anzahl > 1 && !label.equals("") ) { // nur Treffer, falls mehr als 1 Bestellung und ISSN nicht ""
          osf.setLabel(label);
          osf.setLabel_two(label_two);
          osf.setAnzahl(anzahl);
          osf.setAnzahl_two(user); // Anzahl User pro ISSN
          osf.setPreiswaehrung(costsPerField(kid, date_from, date_to, "issn", label, cn));
          list.add(osf);
          		} else {
       	   andere = andere + anzahl;
          		}
          }
          
          OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit nur 1 Treffert als "andere" aufgeführt...
          osf.setLabel("andere");
          osf.setAnzahl(andere);
          list.add(osf);

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countISSNPerKonto(Long kid, String date_from, String date_to, Connection cn)" + e.toString());
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
      return os;
  }
  
  /**
   * Holt die Kosten in den verschiedenen Währungen der Bestellungen pro gewünschtem Feld
   * <p></p>
   * Der Rückgabewert eine PreisWaehrungForm
   * <p></p>
   * @param Long kid String date_from String date_to String feldbezeichnung String wert Connection cn
   * @return PreisWaehrungForm
   */
  private PreisWaehrungForm costsPerField(Long kid, String date_from, String date_to, String feldbezeichnung, String wert, Connection cn){
      PreisWaehrungForm pw = new PreisWaehrungForm();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, waehrung, " + feldbezeichnung + " FROM bestellungen WHERE KID=? AND " + feldbezeichnung + " = ? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, wert);
          pstmt.setString(3, date_from);
          pstmt.setString(4, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  
        	  if (rs.getString("summe")!=null && !rs.getString("summe").equals("") && !rs.getString("summe").equals("0.00")) {
        		  PreisWaehrungForm pwf = new PreisWaehrungForm();
        		  
        		  pwf.setPreis(rs.getString("summe"));
            	  pwf.setWaehrung(rs.getString("waehrung"));
        	  
        		  if (pwf.getWaehrung().equals("CHF")) {
        			  pw.setChf(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("EUR")) {
        			  pw.setEur(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("USD")) {
        			  pw.setUsd(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("GBP")) {
        			  pw.setGbp(pwf);
        		  }        	  
        	  
        	  }
          }         

      } catch (Exception e) {
		  StringBuffer bf = new StringBuffer();
		  bf.append("In PreisWaehrungForm costsPerField(Long kid, String date_from, String date_to, String feldbezeichnung, String wert, Connection cn) trat folgender Fehler auf:\n\n");
		  bf.append(e);
		  bf.append("\n\n");
		  bf.append("KID:\040"+kid+"\n");
		  bf.append("date_from:\040"+date_from+"\n");
		  bf.append("date_to:\040"+date_to+"\n");
		  bf.append("Feldbezeichnung:\040"+feldbezeichnung+"\n");
		  bf.append("Wert:\040"+wert+"\n");
		  log.error(bf.toString());
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
      return pw;
  }
  
  /**
   * Holt die Kosten in den verschiedenen Währungen der Bestellungen pro gewünschtem Feld in Verknüpfung mit der benutzer-Tabelle
   * <p></p>
   * Der Rückgabewert eine PreisWaehrungForm
   * <p></p>
   * @param Long kid String date_from String date_to String feldbezeichnung String wert Connection cn
   * @return PreisWaehrungForm
   */
  private PreisWaehrungForm costsPerFieldInnerJoin(Long kid, String date_from, String date_to, String feldbezeichnung, String wert, Connection cn){
      PreisWaehrungForm pw = new PreisWaehrungForm();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT waehrung, sum( kaufpreis ) AS summe FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u ) ON (b.UID=u.UID) WHERE u." + feldbezeichnung + " = ? AND KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung");
          pstmt.setString(1, wert);
          pstmt.setString(2, kid.toString());
          pstmt.setString(3, date_from);
          pstmt.setString(4, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
        	  
        	  if (rs.getString("summe")!=null && !rs.getString("summe").equals("") && !rs.getString("summe").equals("0.00")) {
        		  PreisWaehrungForm pwf = new PreisWaehrungForm();
        		  
        		  pwf.setPreis(rs.getString("summe"));
            	  pwf.setWaehrung(rs.getString("waehrung"));
        	  
        		  if (pwf.getWaehrung().equals("CHF")) {
        			  pw.setChf(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("EUR")) {
        			  pw.setEur(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("USD")) {
        			  pw.setUsd(pwf);
        		  }
        		  if (pwf.getWaehrung().equals("GBP")) {
        			  pw.setGbp(pwf);
        		  }        	  
        	  
        	  }
          }         

      } catch (Exception e) {
		  StringBuffer bf = new StringBuffer();
		  bf.append("In PreisWaehrungForm costsPerFieldInnerJoin(Long kid, String date_from, String date_to, String feldbezeichnung, String wert, Connection cn) trat folgender Fehler auf:\n\n");
		  bf.append(e);
		  bf.append("\n\n");
		  bf.append("KID:\040"+kid+"\n");
		  bf.append("date_from:\040"+date_from+"\n");
		  bf.append("date_to:\040"+date_to+"\n");
		  bf.append("Feldbezeichnung:\040"+feldbezeichnung+"\n");
		  bf.append("Wert:\040"+wert+"\n");
		  log.error(bf.toString());
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
      return pw;
  }
  
  /**
   * Zählt anhand der kid, eines Zeitraumes die Anzahl Kunden, welche Artikel
   * bestellt haben
   * <p></p>
   * Der Rückgabewert ist die Anzahl Reihen
   * <p></p>
   * @param Long kid String date_from String date_to
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countRowsUID(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
   	   pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT UID, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY UID ) AS temp");
   	   pstmt.setString(1, kid.toString());
       pstmt.setString(2, date_from);
       pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          anzahl = rs.getInt("COUNT(*)");
          osf.setAnzahl(anzahl);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen

      } catch (Exception e) {
    	  log.error("countRowsUID(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  
  /**
   * Zählt anhand der kid, eines Zeitraumes und einer ISSN die Anzahl Kunden, welche Artikel von einer bestimmten
   * Zeitschrift bestellt haben
   * <p></p>
   * Der Rückgabewert ist die Anzahl Reihen
   * <p></p>
   * @param Long kid String date_from String date_to String issn Connection cn
   * @return int anzahl
   */
  public int countRowsUIDPerISSN(Long kid, String date_from, String date_to, String issn, Connection cn){
      int anzahl = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT UID, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND issn = ? AND orderdate >= ? AND orderdate <= ? GROUP BY UID ) AS temp");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, issn);
          pstmt.setString(3, date_from);
          pstmt.setString(4, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          anzahl = rs.getInt("COUNT(*)");
          }

      } catch (Exception e) {
		  StringBuffer bf = new StringBuffer();
		  bf.append("In countRowsUIDPerISSN(Long kid, String date_from, String date_to, String issn, Connection cn) trat folgender Fehler auf:\n\n");
		  bf.append(e);
		  bf.append("\n\n");
		  bf.append("KID:\040"+kid+"\n");
		  bf.append("date_from:\040"+date_from+"\n");
		  bf.append("date_to:\040"+date_to+"\n");
		  bf.append("ISSN:\040"+issn+"\n");
		  log.error(bf.toString());
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
      return anzahl;
  }
  
  /**
   * Zählt anhand der kid, eines Zeitraumes und eines anzugebenden Feldes der Tabelle Benutzer die Bestellungen
   * <p></p>
   * Der Rückgabewert ist die Anzahl Reihen
   * <p></p>
   * @param Long kid String date_from String date_to String benutzerfeld String inhalt Connection cn
   * @return int anzahl
   */
  public int countRowsPerFeld(Long kid, String date_from, String date_to, String benutzerfeld, String inhalt, Connection cn){
      int anzahl = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT BID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.KID=? AND u." + benutzerfeld + "=? AND orderdate >= ? AND orderdate <= ? ) AS temp");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, inhalt);
          pstmt.setString(3, date_from);
          pstmt.setString(4, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          anzahl = rs.getInt("COUNT(*)");
          }

      } catch (Exception e) {
		  StringBuffer bf = new StringBuffer();
		  bf.append("In countRowsPerFeld(Long kid, String date_from, String date_to, String benutzerfeld, String inhalt, Connection cn) trat folgender Fehler auf:\n\n");
		  bf.append(e);
		  bf.append("\n\n");
		  bf.append("KID:\040"+kid+"\n");
		  bf.append("date_from:\040"+date_from+"\n");
		  bf.append("date_to:\040"+date_to+"\n");
		  bf.append("Feldbezeichnung:\040"+benutzerfeld+"\n");
		  bf.append("Wert:\040"+inhalt+"\n");
		  log.error(bf.toString());
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
      return anzahl;
  }
  
  /**
   * Sucht anhand der kid und eines Zeitraumes die Jahresbereiche der bestellten Zeitschriften
   * <p></p>
   * Der Rückgabewert ist ein OrderStatistikForm
   * <p></p>
   * @param Long kid String date_from String date_to Connection cn
   * @return OrderStatistikForm os
   */
  public OrderStatistikForm countOrderYears(Long kid, String date_from, String date_to, Connection cn){
      OrderStatistikForm os = new OrderStatistikForm();
      ArrayList <OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
      int anzahl = 0;
      int total = 0;
      int unknown = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      try {
      	// SQL ausführen
          pstmt = cn.prepareStatement("SELECT jahr, COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY jahr ORDER BY jahr DESC");
          pstmt.setString(1, kid.toString());
          pstmt.setString(2, date_from);
          pstmt.setString(3, date_to);

          rs = pstmt.executeQuery();
          while (rs.next()) {
          OrderStatistikForm osf = new OrderStatistikForm();
          String label ="";
          label = rs.getString("jahr");
          anzahl = rs.getInt("anzahl");
          total = total + anzahl;
          	if (!label.equals("") && !label.equals("0")) {
          		osf.setLabel(label);
          		osf.setAnzahl(anzahl);
          		list.add(osf);
          		} else {
          			unknown = unknown + anzahl;
          		}
          }
          
          if (unknown > 0) {
          OrderStatistikForm osf = new OrderStatistikForm(); // hier werden Bestellungen mit unbekanntem Jahr als "k.A." aufgeführt...
          osf.setLabel("k.A.");
          osf.setAnzahl(unknown);
          list.add(osf);
          }

          os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
          os.setTotal(total);

      } catch (Exception e) {
    	  log.error("countOrderYears(Long kid, String date_from, String date_to, Connection cn): " + e.toString());
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
      return os;
  }
  
  /**
 	 * Zählt anhand eines Kontos {@link ch.dbs.entity.Benutzer} die Anzahl Treffer
 	 * einer Suche
 	 * 
 	 * @param pstmt
 	 * @return
 	 */
        public List<OrderStatistikForm> countSearchOrdersPerKonto(PreparedStatement pstmt){
            ArrayList<OrderStatistikForm> auflistung = new ArrayList<OrderStatistikForm>();
            ResultSet rs = null;
            try {
         	rs = pstmt.executeQuery();        	   

             int total = 0;
             while (rs.next()) {
             	OrderStatistikForm osf = new OrderStatistikForm();
             	osf.setAnzahl(rs.getInt("count(bid)"));
             	total = total +  osf.getAnzahl();
             	osf.setTotal(total);
             	auflistung.add(osf);
             }

            } catch (Exception e) {
            	log.error("countSearchOrdersPerKonto(PreparedStatement pstmt): " + e.toString());
        } finally {
        	if (rs != null) {
        		try {
        			rs.close();
        		} catch (SQLException e) {
        			System.out.println(e);
        		}
        	}
        }
            return auflistung;
        }

public BigDecimal getKaufpreis() {
	return kaufpreis;
}

public void setKaufpreis(BigDecimal kaufpreis) {
	this.kaufpreis = kaufpreis;
}

public String getPreisnachkomma() {
	return preisnachkomma;
}

public void setPreisnachkomma(String preisnachkomma) {
	this.preisnachkomma = preisnachkomma;
}

public String getPreisvorkomma() {
	return preisvorkomma;
}

public void setPreisvorkomma(String preisvorkomma) {
	this.preisvorkomma = preisvorkomma;
}

public String getFileformat() {
    return fileformat;
}

public void setFileformat(String fileformat) {
    this.fileformat = fileformat;
}

public Lieferanten getLieferant() {
	return lieferant;
}

public void setLieferant(Lieferanten lieferant) {
	this.lieferant = lieferant;
}

public String getDeloptions() {
	return deloptions;
}

public void setDeloptions(String deloptions) {
	this.deloptions = deloptions;
}

public String getPriority() {
    return priority;
}

public void setPriority(String priority) {
    this.priority = priority;
}

public String getArtikeltitel() {
        return artikeltitel;
    }

    public void setArtikeltitel(String artikeltitel) {
        this.artikeltitel = artikeltitel;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getBibliothek() {
        return bibliothek;
    }

    public void setBibliothek(String bibliothek) {
        this.bibliothek = bibliothek;
    }

    public String getSigel() {
        return sigel;
    }

    public void setSigel(String sigel) {
        this.sigel = sigel;
    }

    public String getBestellquelle() {
		return bestellquelle;
	}

	public void setBestellquelle(String bestellquelle) {
		this.bestellquelle = bestellquelle;
	}

	public String getHeft() {
        return heft;
    }

    public void setHeft(String heft) {
        this.heft = heft;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }

    public String getJahrgang() {
        return jahrgang;
    }

    public void setJahrgang(String jahrgang) {
        this.jahrgang = jahrgang;
    }

    public String getSeiten() {
        return seiten;
    }

    public void setSeiten(String seiten) {
        this.seiten = seiten;
    }

    public String getSubitonr() {
        return subitonr;
    }

    public void setSubitonr(String subitonr) {
        this.subitonr = subitonr;
    }

    public String getZeitschrift() {
        return zeitschrift;
    }

    public void setZeitschrift(String zeitschrift) {
        this.zeitschrift = zeitschrift;
    }

    public AbstractBenutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(AbstractBenutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
    }

    public String getSystembemerkung() {
        return systembemerkung;
    }

    public void setSystembemerkung(String systembemerkung) {
        this.systembemerkung = systembemerkung;
    }

	public String getStatusdate() {
		return statusdate;
	}

	public void setStatusdate(String statusdate) {
		this.statusdate = statusdate;
	}

	public String getStatustext() {
		return statustext;
	}

	public void setStatustext(String statustext) {
		this.statustext = statustext;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getNotizen() {
		return notizen;
	}

	public void setNotizen(String notizen) {
		this.notizen = notizen;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public String getBuchtitel() {
		return buchtitel;
	}

	public void setBuchtitel(String buchtitel) {
		this.buchtitel = buchtitel;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getKapitel() {
		return kapitel;
	}

	public void setKapitel(String kapitel) {
		this.kapitel = kapitel;
	}

	public String getMediatype() {
		return mediatype;
	}

	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getVerlag() {
		return verlag;
	}

	public void setVerlag(String verlag) {
		this.verlag = verlag;
	}

    public String getGbvnr() {
		return gbvnr;
	}

	public void setGbvnr(String gbvnr) {
		this.gbvnr = gbvnr;
	}

	public String getInterne_bestellnr() {
		return interne_bestellnr;
	}

	public void setInterne_bestellnr(String interne_bestellnr) {
		this.interne_bestellnr = interne_bestellnr;
	}

	public String getTrackingnr() {
		return trackingnr;
	}

	public void setTrackingnr(String trackingnr) {
		this.trackingnr = trackingnr;
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public boolean isPreisdefault() {
		return preisdefault;
	}

	public void setPreisdefault(boolean preisdefault) {
		this.preisdefault = preisdefault;
	}

	private PreparedStatement setOrderValues(PreparedStatement ps, Bestellungen b) throws Exception{
        ps.setString(1, b.getKonto().getId().toString());
        ps.setString(2, b.getBenutzer().getId().toString());
        if (b.getLieferant()==null || b.getLieferant().getLid()==null || b.getLieferant().getLid().toString().equals("")
         || b.getLieferant().getLid().toString().equals("0")) {ps.setString(3, "1");}else{ps.setString(3, b.getLieferant().getLid().toString());}
        if (b.getPriority()==null || b.getPriority().equals("")){ps.setString(4, "normal");}else{ps.setString(4, b.getPriority());}
        if (b.getDeloptions()==null){ps.setString(5, "");}else{ps.setString(5, b.getDeloptions());}
        if (b.getFileformat()==null){ps.setString(6, "");}else{ps.setString(6, b.getFileformat());}
        if (b.getHeft()==null){ps.setString(7, "");}else{ps.setString(7, b.getHeft());}
        if (b.getSeiten()==null){ps.setString(8, "");}else{ps.setString(8, b.getSeiten());}
        ps.setString(9, b.getIssn());
        ps.setString(10, b.getSigel());
        ps.setString(11, b.getBibliothek());
        if (b.getAutor()==null){ps.setString(12, "");}else{ps.setString(12, b.getAutor());}
        if (b.getArtikeltitel()==null){ps.setString(13, "");}else{ps.setString(13, b.getArtikeltitel());}
        if (b.getJahrgang()==null){ps.setString(14, "");}else{ps.setString(14, b.getJahrgang());}
        if (b.getZeitschrift()==null){ps.setString(15, "");}else{ps.setString(15, b.getZeitschrift());}
        if (b.getJahr()==null){ps.setString(16, "");}else{ps.setString(16, b.getJahr());}
        if (b.getSubitonr()==null){ps.setString(17, "");}else{ps.setString(17, b.getSubitonr());} // dürfte null sein, war es aber nie...
        if (b.getGbvnr()!=null && b.getGbvnr().equals("")){ps.setString(18, null);}else{ps.setString(18, b.getGbvnr());} // falls keine Angaben => null setzen (Anzeige)
        if (b.getTrackingnr()==null){ps.setString(19, "");}else{ps.setString(19, b.getTrackingnr());}
        if (b.getInterne_bestellnr()==null){ps.setString(20, "");}else{ps.setString(20, b.getInterne_bestellnr());}
        if (b.getSystembemerkung()==null){ps.setString(21, "");}else{ps.setString(21, b.getSystembemerkung());}
        if (b.getNotizen()==null){ps.setString(22, "");}else{ps.setString(22, b.getNotizen());}
        if (b.getKaufpreis()!=null) {ps.setBigDecimal(23, b.getKaufpreis());} else {ps.setString(23, null);} // keine Preisangaben als Null setzen
        if (b.getWaehrung()!=null) {ps.setString(24, b.getWaehrung());} else {
        	if (b.getKaufpreis()==null) {ps.setString(24, null);} else {ps.setString(24, "EUR");} // um Kombination Kaufpreis!=null && Waherung==null zu verhindern
        }
        if (b.getDoi()==null){ps.setString(25, "");} else {ps.setString(25, b.getDoi());}
        if (b.getDoi()==null){ps.setString(26, "");} else {ps.setString(26, b.getPmid());}
        if (b.getDoi()==null){ps.setString(27, "");} else {ps.setString(27, b.getIsbn());}        
        ps.setString(28, b.getMediatype());
        if (b.getDoi()==null){ps.setString(29, "");} else {ps.setString(29, b.getVerlag());}
        if (b.getDoi()==null){ps.setString(30, "");} else {ps.setString(30, b.getKapitel());}
        if (b.getDoi()==null){ps.setString(31, "");} else {ps.setString(31, b.getBuchtitel());}
        ps.setString(32, b.getOrderdate());
        ps.setString(33, b.getStatusdate());
        ps.setString(34, b.getStatustext());
        
        if (b.getBestellquelle()==null || b.getBestellquelle().equals("") || b.getBestellquelle().equals("0")){
        	ps.setString(35, "k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
        } else {
        	ps.setString(35, b.getBestellquelle());
        }
        
        return ps;
    }


    
}
