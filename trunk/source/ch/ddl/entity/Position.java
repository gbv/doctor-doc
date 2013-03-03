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

package ch.ddl.entity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.AbstractIdEntity;
import ch.dbs.entity.Konto;
import ch.dbs.interf.OrderHandler;

/**
 * Handelt die Datenbankverbindungen/Daten der einzelnen Rechnungspositionen
 * <p></p>
 * 
 * @author Pascal Steiner
 */
public class Position extends AbstractIdEntity implements OrderHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(Position.class);
    
    private AbstractBenutzer benutzer; // Endkunde / Bibliothekskunde
    private Konto konto; // Bibliothekskonto
    
    // Lieferinfos
    private String priority = ""; // Normal, Express
    private String deloptions = ""; // Online, Email, Postweg, Fax, Fax to PDF
    private String fileformat = ""; // HTML, PDF, Papierkopie,...
    private Date orderdate; // Datum der Bestellung
    
    // Produktinfos
    private String mediatype = ""; // Artikel, Teilkopie Buch oder Buch
    private String autor = "";
    private String zeitschrift_verlag = ""; // Zeitschrift / Verlag
    private String heft = ""; //
    private String jahrgang = "";
    private String jahr = "";
    private String titel = ""; // Artikeltiel / Buchtitel
    private String kapitel = "";
    private String seiten = "";
    
    // Preis
    private String waehrung = "";
    private String preis = "";
    
    //Debug
    // Erroremailadresse in properties File umstellen
    //  private String ERRORMAILADRESS = "info@doctor-doc.com";
    
    public Position() {
    }
    
    public Position(final AbstractBenutzer user, final Konto k) {
        this.setBenutzer(user);
        this.setKonto(k);
    }
    
    public Position(final Long id, final Connection cn) {
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM ddl_positionen p "
                    + "INNER JOIN konto k USING(KID)INNER JOIN benutzer u USING(UID)WHERE p.pid=?");
            pstmt.setLong(1, id);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                getPositionen(rs, cn);
            }
            
        } catch (final Exception e) {
            LOG.error("Positionen(Connection cn, Long id): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        
    }
    
    /**
     * Speichert die Bestellung in der DB ab (hinterlegt die id im
     * Bestellobjekt)
     * 
     * @param cn
     * @author Pascal Steiner
     */
    public void save(final Connection cn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setPositionenValues(cn.prepareStatement("INSERT INTO `ddl_positionen` (`UID` , "
                    + "`KID` , `priority` , `deloptions` , `fileformat` , `orderdate` , `mediatype` , `autor` , "
                    + "`zeitschrift_verlag` , `heft` , `jahrgang` , `jahr` , `titel` , `kapitel` , `seiten` , "
                    + "`waehrung` , `preis`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));
            pstmt.executeUpdate();
            
            // ID der gerade gespeicherten Bestellung ermitteln und in der bestellung hinterlegen
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                this.setId(rs.getLong("LAST_INSERT_ID()"));
            }
        } catch (final Exception e) {
            LOG.error("save(Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }
    
    /**
     * Update this Object in the DB
     * 
     * @param cn
     * @author Pascal Steiner
     */
    public void update(final Connection cn) {
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = setPositionenValues(cn.prepareStatement("UPDATE `ddl_positionen` SET UID=? , "
                    + "KID=?, priority=?, deloptions=?, fileformat=?, orderdate=?, mediatype=?, autor=?, "
                    + "zeitschrift_verlag=?, heft=?, jahrgang=? , jahr=?, titel=?, kapitel=?, seiten=?, "
                    + "waehrung=?, preis=? WHERE `pid` = " + this.getId()));
            
            pstmt.executeUpdate();
            
        } catch (final Exception e) {
            LOG.error("update(Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
    }
    
    /**
     * Löscht dieses Objekt, ohne weitere Prüfungen.
     * 
     * @return Rückmeldung o das Objekt gelöscht werden konnte
     * @author Pascal Steiner
     */
    public boolean deleteSelf(final Connection cn) {
        
        boolean success = false;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `ddl_positionen` WHERE `pid` = ?");
            pstmt.setLong(1, this.getId());
            pstmt.executeUpdate();
            
            success = true;
            
        } catch (final SQLException e) {
            LOG.error("deleteSelf(Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        
        return success;
    }
    
    /**
     * Füllt dieses Objekt mit den Daten einer Zeile eines ResultSets ab
     * 
     * @param rs
     * @author Pascal Steiner
     */
    private void getPositionen(final ResultSet rs, final Connection cn) {
        try {
            this.setId(rs.getLong("pid"));
            // Benutzer abfüllen
            final AbstractBenutzer b = new AbstractBenutzer();
            try {
                rs.findColumn("vorname");
                this.setBenutzer(b.getUser(rs, cn));
            } catch (final SQLException se) {
                LOG.error("getPositionen/ResultSet rs: " + se.toString());
                try {
                    this.setBenutzer(b.getUser(rs.getLong("UID"), this.getConnection()));
                } finally {
                    this.close();
                }
            }
            // Konto abfüllen
            try {
                rs.findColumn("biblioname");
                this.setKonto(new Konto(rs));
            } catch (final SQLException se) {
                LOG.error("getPositionen/ResultSet rs: " + se.toString());
                try {
                    this.setKonto(new Konto(rs.getLong("KID"), this.getConnection()));
                } finally {
                    this.close();
                }
            } catch (final Exception e) {
                LOG.error("getPositionen(rs)" + e.toString());
            }
            this.setPriority(rs.getString("priority"));
            this.setDeloptions(rs.getString("deloptions"));
            this.setFileformat(rs.getString("fileformat"));
            this.setOrderdate(rs.getDate("orderdate"));
            if (rs.getString("mediatype") != null) {
                this.setMediatype(rs.getString("mediatype"));
            } else {
                this.setMediatype("");
            }
            if (rs.getString("autor") != null) {
                this.setAutor(rs.getString("autor"));
            } else {
                this.setAutor("");
            }
            if (rs.getString("zeitschrift_verlag") != null) {
                this.setZeitschrift_verlag(rs.getString("zeitschrift_verlag"));
            } else {
                this.setZeitschrift_verlag("");
            }
            if (rs.getString("heft") != null) {
                this.setHeft(rs.getString("heft"));
            } else {
                this.setHeft("");
            }
            if (rs.getString("jahrgang") != null) {
                this.setJahrgang(rs.getString("jahrgang"));
            } else {
                this.setJahrgang("");
            }
            if (rs.getString("jahr") != null) {
                this.setJahr(rs.getString("jahr"));
            } else {
                this.setJahr("");
            }
            if (rs.getString("titel") != null) {
                this.setTitel(rs.getString("titel"));
            } else {
                this.setTitel("");
            }
            if (rs.getString("kapitel") != null) {
                this.setKapitel(rs.getString("kapitel"));
            } else {
                this.setKapitel("");
            }
            if (rs.getString("seiten") != null) {
                this.setSeiten(rs.getString("seiten"));
            } else {
                this.setSeiten("");
            }
            this.setWaehrung(rs.getString("waehrung"));
            this.setPreis(rs.getString("preis"));
            
        } catch (final SQLException e) {
            LOG.error("getPositionen(rs): " + e.toString());
        }
        
    }
    
    public AbstractBenutzer getBenutzer() {
        return benutzer;
    }
    
    public void setBenutzer(final AbstractBenutzer benutzer) {
        this.benutzer = benutzer;
    }
    
    public Konto getKonto() {
        return konto;
    }
    
    public void setKonto(final Konto konto) {
        this.konto = konto;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(final String priority) {
        this.priority = priority;
    }
    
    public String getDeloptions() {
        return deloptions;
    }
    
    public void setDeloptions(final String deloptions) {
        this.deloptions = deloptions;
    }
    
    public String getFileformat() {
        return fileformat;
    }
    
    public void setFileformat(final String fileformat) {
        this.fileformat = fileformat;
    }
    
    public Date getOrderdate() {
        return orderdate;
    }
    
    public void setOrderdate(final Date orderdate) {
        this.orderdate = orderdate;
    }
    
    public String getMediatype() {
        return mediatype;
    }
    
    public void setMediatype(final String mediatype) {
        this.mediatype = mediatype;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(final String autor) {
        this.autor = autor;
    }
    
    public String getZeitschrift_verlag() {
        return zeitschrift_verlag;
    }
    
    public void setZeitschrift_verlag(final String zeitschrift_verlag) {
        this.zeitschrift_verlag = zeitschrift_verlag;
    }
    
    public String getHeft() {
        return heft;
    }
    
    public void setHeft(final String heft) {
        this.heft = heft;
    }
    
    public String getJahrgang() {
        return jahrgang;
    }
    
    public void setJahrgang(final String jahrgang) {
        this.jahrgang = jahrgang;
    }
    
    public String getJahr() {
        return jahr;
    }
    
    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }
    
    public String getTitel() {
        return titel;
    }
    
    public void setTitel(final String titel) {
        this.titel = titel;
    }
    
    public String getKapitel() {
        return kapitel;
    }
    
    public void setKapitel(final String kapitel) {
        this.kapitel = kapitel;
    }
    
    public String getSeiten() {
        return seiten;
    }
    
    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }
    
    public String getWaehrung() {
        return waehrung;
    }
    
    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }
    
    public String getPreis() {
        return preis;
    }
    
    public void setPreis(final String preis) {
        this.preis = preis;
    }
    
    /**
     * ResultSet vorbereiten
     * 
     * @param ps
     * @return
     * @throws Exception
     * @author Pascal Steiner
     */
    private PreparedStatement setPositionenValues(final PreparedStatement ps) throws Exception {
        
        ps.setLong(1, this.getBenutzer().getId());
        ps.setLong(2, this.getKonto().getId());
        if (this.getPriority() == null) {
            ps.setString(3, "normal");
        } else {
            ps.setString(3, this.getPriority());
        }
        if (this.getDeloptions() == null) {
            ps.setString(4, "");
        } else {
            ps.setString(4, this.getDeloptions());
        }
        if (this.getFileformat() == null) {
            ps.setString(5, "");
        } else {
            ps.setString(5, this.getFileformat());
        }
        ps.setDate(6, this.getOrderdate());
        if (this.getMediatype() == null) {
            ps.setString(7, "");
        } else {
            ps.setString(7, this.getMediatype());
        }
        if (this.getAutor() == null) {
            ps.setString(8, "");
        } else {
            ps.setString(8, this.getAutor());
        }
        if (this.getZeitschrift_verlag() == null) {
            ps.setString(9, "");
        } else {
            ps.setString(9, this.getZeitschrift_verlag());
        }
        if (this.getHeft() == null) {
            ps.setString(10, "");
        } else {
            ps.setString(10, this.getHeft());
        }
        if (this.getJahrgang() == null) {
            ps.setString(11, "");
        } else {
            ps.setString(11, this.getJahrgang());
        }
        if (this.getJahr() == null) {
            ps.setString(12, "");
        } else {
            ps.setString(12, this.getJahr());
        }
        if (this.getTitel() == null) {
            ps.setString(13, "");
        } else {
            ps.setString(13, this.getTitel());
        }
        if (this.getKapitel() == null) {
            ps.setString(14, "");
        } else {
            ps.setString(14, this.getKapitel());
        }
        if (this.getSeiten() == null) {
            ps.setString(15, "");
        } else {
            ps.setString(15, this.getSeiten());
        }
        if (this.getWaehrung() == null) {
            ps.setString(16, "");
        } else {
            ps.setString(16, this.getWaehrung());
        }
        if (this.getPreis() == null) {
            ps.setString(17, "");
        } else {
            ps.setString(17, this.getPreis());
        }
        
        return ps;
    }
    
}
