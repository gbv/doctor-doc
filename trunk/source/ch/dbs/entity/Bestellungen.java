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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.form.OrderForm;
import ch.dbs.form.OrderStatistikForm;
import ch.dbs.form.OverviewForm;
import ch.dbs.form.PreisWaehrungForm;
import enums.TextType;

/**
 * Abstract base class for entities having a {@link Long} unique identifier,
 * this provides the base functionality for them. <p></p>
 */
public class Bestellungen extends AbstractIdEntity {

    private static final Logger LOG = LoggerFactory.getLogger(Bestellungen.class);

    private AbstractBenutzer benutzer;
    private Konto konto;
    private Lieferanten lieferant;
    private String bestellquelle; // needed for table 'bestellungen' in the database. Maybe replaced in the future...
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
    private String trackingnr = ""; // unique No. between ordering systems
    private String interne_bestellnr = ""; // for internal numbering systems of a library
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

    private String signatur = "";

    public Bestellungen() {

    }

    public Bestellungen(final AbstractBenutzer user, final Konto k) {
        this.setBenutzer(user);
        this.setKonto(k);
    }

    public Bestellungen(final OrderForm of, final AbstractBenutzer user, final Konto k) {

        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date d = new Date();
        final String datum = fmt.format(d, k.getTimezone());

        this.setKonto(k);
        this.setBenutzer(user);
        this.setLieferant(of.getLieferant());
        if (of.getMediatype() != null) {
            this.setMediatype(of.getMediatype());
        } else {
            this.setMediatype("");
        }
        if (of.getPrio() != null) {
            this.setPriority(of.getPrio());
        } else {
            this.setPriority("normal");
        }
        if (of.getDeloptions() != null) {
            this.setDeloptions(of.getDeloptions());
        } else {
            this.setDeloptions("");
        }
        if (of.getFileformat() != null) {
            this.setFileformat(of.getFileformat());
        } else {
            this.setFileformat("");
        }
        if (of.getZeitschriftentitel() != null) {
            this.setZeitschrift(of.getZeitschriftentitel());
        } else {
            this.setZeitschrift("");
        }
        if (of.getAuthor() != null) {
            this.setAutor(of.getAuthor());
        } else {
            this.setAutor("");
        }
        if (of.getArtikeltitel() != null) {
            this.setArtikeltitel(of.getArtikeltitel());
        } else {
            this.setArtikeltitel("");
        }
        if (of.getJahr() != null) {
            this.setJahr(of.getJahr());
        } else {
            this.setJahr("");
        }
        if (of.getJahrgang() != null) {
            this.setJahrgang(of.getJahrgang());
        } else {
            this.setJahrgang("");
        }
        if (of.getHeft() != null) {
            this.setHeft(of.getHeft());
        } else {
            this.setHeft("");
        }
        if (of.getSeiten() != null) {
            this.setSeiten(of.getSeiten());
        } else {
            this.setSeiten("");
        }
        this.setIssn(of.getIssn()); // darf offenbar null sein
        if (of.getIsbn() != null) {
            this.setIsbn(of.getIsbn());
        } else {
            this.setIsbn("");
        }
        if (of.getKapitel() != null) {
            this.setKapitel(of.getKapitel());
        } else {
            this.setKapitel("");
        }
        if (of.getBuchtitel() != null) {
            this.setBuchtitel(of.getBuchtitel());
        } else {
            this.setBuchtitel("");
        }
        if (of.getVerlag() != null) {
            this.setVerlag(of.getVerlag());
        } else {
            this.setVerlag("");
        }
        if (of.getDoi() != null) {
            this.setDoi(of.getDoi());
        } else {
            this.setDoi("");
        }
        if (of.getPmid() != null) {
            this.setPmid(of.getPmid());
        } else {
            this.setPmid("");
        }
        if (of.getSubitonr() != null) {
            this.setSubitonr(of.getSubitonr());
        } else {
            this.setSubitonr(""); // darf zwar null sein, war aber praktischerweise nie
        }
        this.setGbvnr(of.getGbvnr());
        if (of.getTrackingnr() != null) {
            this.setTrackingnr(of.getTrackingnr());
        } else {
            this.setTrackingnr("");
        }
        if (of.getInterne_bestellnr() != null) {
            this.setInterne_bestellnr(of.getInterne_bestellnr());
        } else {
            this.setInterne_bestellnr("");
        }
        this.setSigel(of.getSigel());
        this.setBibliothek(of.getBibliothek());
        this.setBestellquelle(of.getBestellquelle());
        this.setStatustext(of.getStatus());
        this.setStatusdate(datum);
        this.setOrderdate(datum);
        this.setErledigt(of.isErledigt());
        if (of.getAnmerkungen() != null) {
            this.setSystembemerkung(of.getAnmerkungen());
        } else {
            this.setSystembemerkung("");
        }
        if (of.getNotizen() != null) {
            this.setNotizen(of.getNotizen());
        } else {
            this.setNotizen("");
        }
        this.setKaufpreis(of.getKaufpreis());
        this.setWaehrung(of.getWaehrung());
        this.setSignatur(of.getSignatur());
    }

    public Bestellungen(final Connection cn, final Long id) {

        if (id != null) {

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = cn
                        .prepareStatement("SELECT * FROM bestellungen AS b INNER JOIN konto AS k USING(KID) INNER JOIN "
                                + "benutzer AS u USING(UID) WHERE b.bid=?");
                pstmt.setLong(1, id);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    getBestellung(rs, cn);
                    if (checkAnonymize(this)) {
                        anonymize(this);
                    }
                }

            } catch (final Exception e) {
                LOG.error("Bestellungen(Connection cn, Long id): " + e.toString());
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
    }

    /**
     * Holt bei einem Ill-Request die betreffende Bestellung um den Status
     * nachzufahren
     * 
     * @param String trackingnr
     * @param String gbvnr
     * @return Bestellungen b
     */
    public Bestellungen(final Connection cn, final String trackingNumber, final String gbvNumber) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellungen WHERE trackingnr=? AND gbvnr=?");
            pstmt.setString(1, trackingNumber);
            pstmt.setString(2, gbvNumber);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                getBestellung(rs, cn);
            }

        } catch (final Exception e) {
            LOG.error("Bestellungen(Connection cn, String trackingnr, String gbvnr): " + e.toString());
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
     */
    public void save(final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setOrderValues(
                    cn.prepareStatement("INSERT INTO `bestellungen` (`KID` , "
                            + "`UID` , `LID` , `orderpriority` , `deloptions` , `fileformat` , `heft` , `seiten` , `issn` , "
                            + "`biblionr` , `bibliothek` , `autor` , `artikeltitel` , `jahrgang` , `zeitschrift` , `jahr` , "
                            + "`subitonr` , `gbvnr` , `trackingnr` , `internenr` , `systembemerkung` , `notizen` , "
                            + "`kaufpreis` , `waehrung` , `doi` , `pmid` , `isbn` , `mediatype` , `verlag` , `buchkapitel` , "
                            + "`buchtitel` , `orderdate` , `statedate` , `state`, `bestellquelle`, `signatur`) VALUES (?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),
                    this);
            pstmt.executeUpdate();

            // ID der gerade gespeicherten Bestellung ermitteln und in der bestellung hinterlegen
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                this.setId(rs.getLong("LAST_INSERT_ID()"));
            }
        } catch (final Exception e) {
            LOG.error("save(Connection cn)" + e.toString());
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

    public void update(final Connection cn) {

        PreparedStatement pstmt = null;

        try {
            pstmt = setOrderValues(cn.prepareStatement("UPDATE `bestellungen` SET KID=? , "
                    + "UID=?, LID=?, orderpriority=?, deloptions=?, fileformat=?, heft=?, seiten=?, issn=?, "
                    + "biblionr=?, bibliothek=? , autor=?, artikeltitel=?, jahrgang=?, zeitschrift=?, jahr=?, "
                    + "subitonr=?, gbvnr=?, trackingnr=?, internenr=?, systembemerkung=?, notizen=?, kaufpreis=?, "
                    + "waehrung=?, doi=?, pmid=?, isbn=?, mediatype=?, verlag=?, buchkapitel=?, buchtitel=?, "
                    + "orderdate=?, statedate=?, state=?, bestellquelle=?, signatur=? WHERE `BID` = " + this.getId()),
                    this);

            pstmt.executeUpdate();

        } catch (final Exception e) {
            final StringBuffer bf = new StringBuffer(2400);
            bf.append("In Bestellung.update(Connection cn) trat folgender Fehler auf:\n");
            bf.append(e);
            bf.append("\nBID:\040");
            bf.append(this.getId());
            bf.append("\nKID:\040");
            bf.append(this.getKonto().getId());
            bf.append("\nUID:\040");
            bf.append(this.getBenutzer().getId());
            bf.append("\nLID:\040");
            bf.append(this.getLieferant().getId());
            bf.append("\norderpriority:\040");
            bf.append(this.getPriority());
            bf.append("\ndeloptions:\040");
            bf.append(this.getDeloptions());
            bf.append("\nfileformat:\040");
            bf.append(this.getFileformat());
            bf.append("\nheft:\040");
            bf.append(this.getHeft());
            bf.append("\nseiten:\040");
            bf.append(this.getSeiten());
            bf.append("\nissn:\040");
            bf.append(this.getIssn());
            bf.append("\nbiblionr:\040");
            bf.append(this.getSigel());
            bf.append("\nbibliothek:\040");
            bf.append(this.getBibliothek());
            bf.append("\nautor:\040");
            bf.append(this.getAutor());
            bf.append("\nartikeltitel:\040");
            bf.append(this.getArtikeltitel());
            bf.append("\njahrgang:\040");
            bf.append(this.getJahrgang());
            bf.append("\nzeitschrift:\040");
            bf.append(this.getZeitschrift());
            bf.append("\njahr:\040");
            bf.append(this.getJahr());
            bf.append("\nsubitonr:\040");
            bf.append(this.getSubitonr());
            bf.append("\ngbvnr:\040");
            bf.append(this.getGbvnr());
            bf.append("\ntrackingnr:\040");
            bf.append(this.getTrackingnr());
            bf.append("\ninternenr:\040");
            bf.append(this.getInterne_bestellnr());
            bf.append("\nsystembemerkungen:\040");
            bf.append(this.getSystembemerkung());
            bf.append("\nnotizen:\040");
            bf.append(this.getNotizen());
            bf.append("\nkaufpreis:\040");
            bf.append(this.getKaufpreis());
            bf.append("\nwaehrung:\040");
            bf.append(this.getWaehrung());
            bf.append("\ndoi:\040");
            bf.append(this.getDoi());
            bf.append("\nBID:\040");
            bf.append(this.getId());
            bf.append("\nPMID:\040");
            bf.append(this.getPmid());
            bf.append("\nisbn:\040");
            bf.append(this.getIsbn());
            bf.append("\nmediatype:\040");
            bf.append(this.getMediatype());
            bf.append("\nverlag:\040");
            bf.append(this.getVerlag());
            bf.append("\nbuchkapitel:\040");
            bf.append(this.getKapitel());
            bf.append("\nbuchtitel:\040");
            bf.append(this.getBuchtitel());
            bf.append("\norderdate:\040");
            bf.append(this.getOrderdate());
            bf.append("\nstatedate:\040");
            bf.append(this.getStatusdate());
            bf.append("\nstate:\040");
            bf.append(this.getStatustext());
            bf.append("\nbestellquelle:\040");
            bf.append(this.getBestellquelle());
            bf.append("\nSignatur:\040");
            bf.append(this.getSignatur());
            LOG.error(bf.toString());
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
     * Deletes an order and all it's stati from the DB
     * 
     * @param Bestellungen b
     */
    public boolean deleteOrder(final Bestellungen b, final Connection cn) {

        boolean success = false;

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `bestellungen` WHERE `BID` =?");
            pstmt.setLong(1, b.getId());
            pstmt.executeUpdate();

            success = true;

            deleteStati(b, cn);

        } catch (final Exception e) {
            LOG.error("deleteBestellung(Bestellungen b, Connection cn): " + e.toString());
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
     * Deletes stati from the DB
     * 
     * @param Bestellungen b
     */
    private boolean deleteStati(final Bestellungen b, final Connection cn) {

        boolean success = false;

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `bestellstatus` WHERE `BID` =?");
            pstmt.setLong(1, b.getId());
            pstmt.executeUpdate();

            success = true;

        } catch (final Exception e) {
            LOG.error("deleteStati(final Bestellungen b, final Connection cn): " + e.toString());
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
     * Sucht anhand eines Kontos und beliebigen Suchkriterien
     * {@link ch.dbs.entity.Benutzer} Bestellungen heraus
     * 
     * @param PreparedStatememt pstmt
     * @return
     */
    public List<Bestellungen> searchOrdersPerKonto(final PreparedStatement pstmt, final Connection cn) {
        final ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Bestellungen b = new Bestellungen(rs, cn);
                if (checkAnonymize(b)) {
                    b = anonymize(b);
                }
                bl.add(b);
            }

        } catch (final Exception e) {
            LOG.error("searchOrdersPerKonto(PreparedStatement pstmt)" + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }

        return bl;
    }

    /**
     * Sucht anhand eines {@link ch.dbs.entity.Benutzer} seine Bestellungen
     * heraus <p></p>
     * 
     * @param the user {@link ch.dbs.entity.Benutzer}
     * @param OverviewForm of
     * @param Connection cn
     * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
     */
    public List<Bestellungen> getAllUserOrders(final AbstractBenutzer u, final OverviewForm of, final Connection cn) {
        final ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final StringBuffer sql = new StringBuffer(256);
            sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON `b`.`UID` = `u`.`UID` "
                    + "INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `b`.`UID` = ? AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? "
                    + "ORDER BY ");
            sortOrder(sql, of.getSort(), of.getSortorder());
            sql.append(" LIMIT ");
            sql.append(of.getPage());
            sql.append(", ");
            sql.append(of.getHits());
            pstmt = cn.prepareStatement(sql.toString());
            pstmt.setLong(1, u.getId());
            pstmt.setString(2, of.getFromdate());
            pstmt.setString(3, of.getTodate());
            rs = pstmt.executeQuery();
            long bid = 0;
            while (rs.next()) {
                Bestellungen b = new Bestellungen();
                if (bid != rs.getLong("b.bid")) { //Bestellung abfüllen
                    bid = rs.getLong("b.bid");
                    b.setStatusdate(rs.getString("statedate"));
                    b.setOrderdate(rs.getString("orderdate"));
                    b.setStatustext(rs.getString("state"));
                    b = getBestellung(b, rs, cn);
                    if (checkAnonymize(b)) {
                        b = anonymize(b);
                    }
                    bl.add(b);
                } else { //Bestellung bereits abgefüllt, nur noch korrekter Status setzen
                    b = bl.get(bl.size() - 1);
                    b.setStatustext(rs.getString("state"));
                    b.setStatusdate(rs.getString("statedate"));
                    if (checkAnonymize(b)) {
                        b = anonymize(b);
                    }
                    bl.set(bl.size() - 1, b);
                }

            }

        } catch (final Exception e) {
            LOG.error("getAllUserOrders(final AbstractBenutzer u, final OverviewForm of, final Connection cn: "
                    + e.toString());
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
        return bl;
    }

    /**
     * Sucht anhand eines {@link ch.dbs.entity.Benutzer} seine Bestellungen mit
     * einem bestimmten Status heraus <p></p>
     * 
     * @param the user {@link ch.dbs.entity.Benutzer}
     * @param OberviewForm of
     * @param boolean subitocheck (true => nur Subitobestellungen)
     * @param Connection cn
     * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
     */
    public List<Bestellungen> getAllUserOrdersPerStatus(final AbstractBenutzer u, final OverviewForm of,
            final boolean subitocheck, final Connection cn) {
        final ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final StringBuffer sql = new StringBuffer(256);
            if (subitocheck) {
                if ("offen".equals(of.getFilter())) {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON "
                            + "`b`.`UID` = `u`.`UID` INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `b`.`UID` = ? "
                            + "AND NOT `b`.`state` = 'erledigt' AND NOT `b`.`subitonr` = '' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? "
                            + "ORDER BY ");
                } else {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON `b`.`UID` = `u`.`UID` "
                            + "INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `b`.`UID` = ? AND `b`.`state` = ? "
                            + "AND NOT `b`.`subitonr` = '' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? ORDER BY ");
                }
            } else {
                if ("offen".equals(of.getFilter())) {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON "
                            + "`b`.`UID` = `u`.`UID` INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `b`.`UID` = ? "
                            + "AND NOT `b`.`state` = 'erledigt' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? ORDER BY ");
                } else {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON `b`.`UID` = `u`.`UID` "
                            + "INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `b`.`UID` = ? AND `b`.`state` = ? AND `b`.`orderdate` >= ? "
                            + "AND `b`.`orderdate` <= ? ORDER BY ");
                }
            }
            sortOrder(sql, of.getSort(), of.getSortorder());
            sql.append(" LIMIT ");
            sql.append(of.getPage());
            sql.append(", ");
            sql.append(of.getHits());
            pstmt = cn.prepareStatement(sql.toString());

            pstmt.setLong(1, u.getId());
            if (!"offen".equals(of.getFilter())) {
                pstmt.setString(2, of.getFilter());
                pstmt.setString(3, of.getFromdate());
                pstmt.setString(4, of.getTodate());
            } else { // for status.equals("offen") => dont't set state, this is a pseudo state.
                pstmt.setString(2, of.getFromdate());
                pstmt.setString(3, of.getTodate());
            }
            rs = pstmt.executeQuery();
            long bid = 0;
            while (rs.next()) {
                Bestellungen b = new Bestellungen();
                if (bid != rs.getLong("b.bid")) { //Bestellung abfüllen
                    bid = rs.getLong("b.bid");
                    b.setStatusdate(rs.getString("statedate"));
                    b.setOrderdate(rs.getString("orderdate"));
                    b.setStatustext(rs.getString("state"));
                    b = getBestellung(b, rs, cn);
                    if (checkAnonymize(b)) {
                        b = anonymize(b);
                    }
                    bl.add(b);
                } else { //Bestellung bereits abgefüllt, nur noch korrekter Status setzen
                    b = bl.get(bl.size() - 1);
                    b.setStatustext(rs.getString("state"));
                    b.setStatusdate(rs.getString("statedate"));
                    if (checkAnonymize(b)) {
                        b = anonymize(b);
                    }
                    bl.set(bl.size() - 1, b);
                }

            }

        } catch (final Exception e) {
            LOG.error("getAllUserOrdersPerStatus(final AbstractBenutzer u, final OverviewForm of, final boolean subitocheck, final Connection cn): "
                    + e.toString());
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
        return bl;
    }

    /**
     * Sucht anhand eines Kontos {@link ch.dbs.entity.Benutzer} alle
     * Bestellungen heraus
     * 
     * @param k Konto
     * @param OverviewForm of
     * @param Connecton cn
     * @return
     */
    public List<Bestellungen> getOrdersPerKonto(final Konto k, final OverviewForm of, final Connection cn) {
        final ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final StringBuffer sql = new StringBuffer(256);
            sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON `b`.`UID` = `u`.`UID` "
                    + "INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `k`.`KID` = ? AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? "
                    + "ORDER BY ");
            sortOrder(sql, of.getSort(), of.getSortorder());
            sql.append(" LIMIT ");
            sql.append(of.getPage());
            sql.append(", ");
            sql.append(of.getHits());
            pstmt = cn.prepareStatement(sql.toString());
            pstmt.setLong(1, k.getId());
            pstmt.setString(2, of.getFromdate());
            pstmt.setString(3, of.getTodate());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Bestellungen b = new Bestellungen(rs, cn);
                if (checkAnonymize(b)) {
                    b = anonymize(b);
                }
                bl.add(b);
            }

        } catch (final Exception e) {
            LOG.error("getOrdersPerKonto(final Konto k, final OverviewForm of, final Connection cn): " + e.toString());
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
        return bl;
    }

    /**
     * Sucht anhand eines Kontos alle offenen Bestellungen mit einem bestimmten
     * Status heraus
     * 
     * @param long KID
     * @param OverviewForm of
     * @param boolean subitocheck (true => nur Subitobestellungen)
     * @param Connection cn
     * @return
     */
    public List<Bestellungen> getOrdersPerKontoPerStatus(final long KID, final OverviewForm of,
            final boolean subitocheck, final Connection cn) {
        final ArrayList<Bestellungen> bl = new ArrayList<Bestellungen>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final StringBuffer sql = new StringBuffer(256);
            if (subitocheck) {
                if ("offen".equals(of.getFilter())) {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON "
                            + "`b`.`UID` = `u`.`UID` INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `k`.`KID` = ? AND NOT "
                            + "`b`.`state` = 'erledigt' AND NOT `b`.`subitonr` = '' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? ORDER BY ");
                } else {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON "
                            + "`b`.`UID` = `u`.`UID` INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `k`.`KID` = ? AND `b`.`state` = ? "
                            + "AND NOT `b`.`subitonr` = '' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? ORDER BY ");
                }
            } else {
                if ("offen".equals(of.getFilter())) {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON "
                            + "`b`.`UID` = `u`.`UID` INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `k`.`KID` = ? AND NOT "
                            + "`b`.`state` = 'erledigt' AND `b`.`orderdate` >= ? AND `b`.`orderdate` <= ? ORDER BY ");
                } else {
                    sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN `benutzer` AS u ON `b`.`UID` = `u`.`UID` "
                            + "INNER JOIN `konto` AS k ON `b`.`KID` = `k`.`KID` WHERE `k`.`KID` = ? AND `b`.`state` = ? AND `b`.`orderdate` >= ? "
                            + "AND `b`.`orderdate` <= ? ORDER BY ");
                }
            }
            sortOrder(sql, of.getSort(), of.getSortorder());
            sql.append(" LIMIT ");
            sql.append(of.getPage());
            sql.append(", ");
            sql.append(of.getHits());
            pstmt = cn.prepareStatement(sql.toString());

            pstmt.setLong(1, KID);
            if (!"offen".equals(of.getFilter())) {
                pstmt.setString(2, of.getFilter());
                pstmt.setString(3, of.getFromdate());
                pstmt.setString(4, of.getTodate());
            } else { // for status.equals("offen") => dont't set state, this is a pseudo state.
                pstmt.setString(2, of.getFromdate());
                pstmt.setString(3, of.getTodate());
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Bestellungen b = new Bestellungen();
                b.setStatusdate(rs.getString("statedate"));
                b.setOrderdate(rs.getString("orderdate"));
                b.setStatustext(rs.getString("state"));
                b = getBestellung(b, rs, cn);
                if (checkAnonymize(b)) {
                    b = anonymize(b);
                }
                bl.add(b);

            }

        } catch (final Exception e) {
            LOG.error("getOrdersPerKontoPerStatus(final long KID, final OverviewForm of, final boolean subitocheck, final Connection cn): "
                    + e.toString());
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
        return bl;
    }

    /**
     * Sucht anhand seiner Werte eine seine Bestellungen heraus <p></p>
     * 
     * @param the user {@link ch.dbs.entity.Bestellungen}
     * @return a {@link List} with his {@link ch.dbs.entity.Bestellungen}
     */
    public Bestellungen getOrderSimpleWay(Bestellungen b, final Connection cn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellungen WHERE uid=? and kid=? "
                    + "and heft=? and seiten=? and issn=? and jahr=? and jahrgang=? and orderdate=?");
            pstmt.setLong(1, b.getBenutzer().getId());
            pstmt.setLong(2, b.getKonto().getId());
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

        } catch (final Exception e) {
            LOG.error("getOrderSimpleWay(Bestellungen b, Connection cn)" + e.toString());
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
        return b;
    }

    /**
     * Sucht anhand der uid und kid alle Bestellungen heraus. Der Rückgabewert
     * ist die Anzahl Bestellungen pro User im laufenden Kalenderjahr <p></p>
     * 
     * @param Long uid, kid
     * @return int anzahl
     */
    public int countOrdersPerUser(final String uid, final Konto k, final Connection cn) {
        int anzahl = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
            final String datum = String.format("%1$tY-01-01 00:00:00", cal); // Kalenderjahr berechnen
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT count(bid) FROM `bestellungen` WHERE `KID` = ? AND `UID` = ? AND `orderdate` >= ?");
            pstmt.setLong(1, k.getId());
            pstmt.setString(2, uid);
            pstmt.setString(3, datum);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                anzahl = rs.getInt("count(bid)");
            }

        } catch (final Exception e) {
            LOG.error("countOrdersPerUser(String uid, Long kid, Connection cn): " + e.toString());
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
        return anzahl;
    }

    /**
     * Sucht anhand der kid alle Bestellungen für das laufende Kalenderjahr
     * heraus. <p></p>
     * 
     * @param Long kid
     * @return int anzahl
     */
    public int allOrdersThisYearForKonto(final Konto k, final Connection cn) {
        int anzahl = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone(k.getTimezone()));
            final String datum = String.format("%1$tY-01-01 00:00:00", cal); // Kalenderjahr berechnen
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT count(bid) FROM `bestellungen` WHERE `KID` = ? AND `orderdate` >= ?");
            pstmt.setLong(1, k.getId());
            pstmt.setString(2, datum);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                anzahl = rs.getInt("count(bid)");
            }

        } catch (final Exception e) {
            LOG.error("allOrdersThisYearForKonto(Long kid, Connection cn): " + e.toString());
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
        return anzahl;
    }

    /**
     * Stellt die korrekte Sortierung her Für die Sortierung nach Buchtitel oder
     * Zeitschrift müssen in der Übersicht die beiden Felder zusammengezogen
     * werden...
     * 
     * @param StringBuffer sql
     * @param String sort
     * @paramString sortorder
     */
    public void sortOrder(final StringBuffer sql, final String sort, final String sortorder) {

        if ("zeitschrift".equals(sort) || "buchtitel".equals(sort)) {
            sql.append("CONCAT( zeitschrift, buchtitel ) ");
            sql.append(sortorder.toUpperCase());
        } else {
            if ("artikeltitel".equals(sort) || "kapitel".equals(sort)) {
                sql.append("CONCAT( artikeltitel, buchkapitel ) ");
                sql.append(sortorder.toUpperCase());
            } else {
                sql.append(sort);
                sql.append('\040');
                sql.append(sortorder.toUpperCase());
            }
        }
    }

    /**
     * Prüft, ob eine Bestellung anonymisiert werden muss <p></p>
     * 
     * @param Bestellungen b
     * @return true/false
     */
    public boolean checkAnonymize(final Bestellungen b) {
        boolean check = false;

        if (b.getBenutzer() != null && ReadSystemConfigurations.isAnonymizationActivated()) {
            final Calendar cal = stringFromMysqlToCal(b.getOrderdate());
            final Calendar limit = Calendar.getInstance();
            // takes SystemTimezone due to pratical reasons
            limit.setTimeZone(TimeZone.getTimeZone(ReadSystemConfigurations.getSystemTimezone()));
            limit.add(Calendar.MONTH, -ReadSystemConfigurations.getAnonymizationAfterMonths());
            limit.add(Calendar.DAY_OF_MONTH, -1);
            if (cal.before(limit)) {
                check = true;
            }
        }

        return check;
    }

    /**
     * Anonymisiert Bestellungen für die Ausgabe <p></p>
     * 
     * @param Bestellungen b
     * @return Bestellungen b
     */
    private Bestellungen anonymize(final Bestellungen b) {

        if (b.getBenutzer() != null && ReadSystemConfigurations.isAnonymizationActivated()) {

            final AbstractBenutzer anon = b.getBenutzer();
            anon.setName("anonymized");
            anon.setVorname("");
            anon.setEmail("");
            b.setSystembemerkung("");
            b.setBenutzer(anon);

        }

        return b;
    }

    /**
     * Konvertiert einen Datums-String aus MYSQL in ein Calendar-Objekt <p></p>
     * 
     * @param String datum
     * @return cal cal
     */
    public Calendar stringFromMysqlToCal(final String datum) { // 2007-11-01 08:56:07.0

        Date dateParsed = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(ReadSystemConfigurations.getSystemTimezone()));

        try {
            final ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateParsed = sdf.parse(datum);
        } catch (final ParseException e) {
            LOG.error("stringFromMysqlToCal(String datum):\012" + e.toString());
        }

        cal.setTime(dateParsed);

        return cal;
    }

    /**
     * Entfernt aus einem Datums-String mit Milliseconds aus MYSQL die
     * Milliseconds. Seit MySQL-Connector 5.0 kommen jeweils Milliseconds mit.
     * <p></p>
     * 
     * @param String datum
     * @return String datum
     */
    protected String removeMilliseconds(final String datum) { // 2007-11-01 08:56:07.0

        try {
            if (datum.contains(".")) {
                return datum.substring(0, datum.indexOf('.'));
            }
        } catch (final Exception e) {
            LOG.error("removeMilliseconds(String datum):\012" + e.toString());
        }

        return datum;
    }

    /*
     * Erstellt ein Bestellungsobjekt aus einer Zeile aus der Datenbank
     */
    private Bestellungen(final ResultSet rs, final Connection cn) {
        try {
            getBestellung(rs, cn);
        } catch (final Exception e) {
            LOG.error("Bestellungen(ResultSet rs: " + e.toString());
        }
    }

    private void getBestellung(final ResultSet rs, final Connection cn) throws Exception {
        this.setId(rs.getLong("BID"));
        final Lieferanten supplier = new Lieferanten();
        //    Falls vorname nicht im rs ist kein Benutzer abfüllen
        try {
            rs.findColumn("vorname");
            final AbstractBenutzer b = new AbstractBenutzer();
            // rs may be ambigous for Ort and PLZ: get User from UID
            this.setBenutzer(b.getUser(rs.getLong("UID"), cn));
        } catch (final SQLException se) {
            LOG.debug("getBestellung(ResultSet rs) Pos. 1: " + se.toString());
        }
        // Falls biblioname nicht im rs is kein Konto abfüllen
        try {
            rs.findColumn("biblioname");
            this.setKonto(new Konto(rs));
        } catch (final SQLException se) {
            LOG.debug("getBestellung(ResultSet rs) Pos. 2: " + se.toString());
        }
        this.setLieferant(supplier.getLieferantFromLid(rs.getLong("lid"), cn));
        if (this.getLieferant().getLid() == null) {
            this.getLieferant().setLid(Long.valueOf(1));
            this.getLieferant().setName("k.A.");
        }
        this.setBestellquelle(rs.getString("bestellquelle"));
        if (this.getBestellquelle() == null || this.getBestellquelle().equals("")
                || this.getBestellquelle().equals("0")) {
            this.setBestellquelle("k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
        }
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
        this.setErledigt(rs.getBoolean("erledigt"));
        this.setKaufpreis(rs.getBigDecimal("kaufpreis")); // Achtung kann NULL sein...
        this.setWaehrung(rs.getString("waehrung"));
        this.setDoi(rs.getString("doi"));
        this.setPmid(rs.getString("pmid"));
        this.setIsbn(rs.getString("isbn"));
        this.setMediatype(rs.getString("mediatype"));
        this.setVerlag(rs.getString("verlag"));
        this.setKapitel(rs.getString("buchkapitel"));
        this.setBuchtitel(rs.getString("buchtitel"));
        this.setSignatur(rs.getString("signatur"));

    }

    /*
     * Füllt ein Bestellungsobjekt mit einer Zeile aus der Datenbank
     */
    private Bestellungen getBestellung(final Bestellungen b, final ResultSet rs, final Connection cn) throws Exception {

        b.setId(rs.getLong("BID"));
        final Lieferanten supplier = new Lieferanten();
        final AbstractBenutzer ab = new AbstractBenutzer();

        try {
            // rs may be ambigous for Ort and PLZ: get User from UID
            b.setBenutzer(ab.getUser(rs.getLong("UID"), cn));
            // Falls biblioname nicht im rs is kein Konto abfüllen
            rs.findColumn("biblioname");
            b.setKonto(new Konto(rs));
        } catch (final SQLException se) {
            //einfach nix machen ;-)
            LOG.debug("getBestellungen(Bestellungen b, ResultSet rs, Connection cn): " + se.toString());
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
        // Null-Werte nicht als 0.0 abfüllen...!
        if (rs.getString("kaufpreis") != null) {
            b.setKaufpreis(rs.getBigDecimal("kaufpreis"));
        }
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

        b.setLieferant(supplier.getLieferantFromLid(rs.getLong("lid"), cn));
        b.setBestellquelle(rs.getString("bestellquelle"));
        if (b.getBestellquelle() == null || b.getBestellquelle().equals("") || b.getBestellquelle().equals("0")) {
            b.setBestellquelle("k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
        }
        b.setStatustext(rs.getString("state"));
        b.setStatusdate(removeMilliseconds(rs.getString("statedate")));
        b.setOrderdate(removeMilliseconds(rs.getString("orderdate")));
        b.setSignatur(rs.getString("signatur"));
        return b;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Konto heraus. Der Rückgabewert ist ein OrderStatistikForm. <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countOrdersPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {

        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT BID, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY BID ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                count = rs.getInt("anzahl");
                total = total + count;
                osf.setAnzahl(count);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countOrdersPerKonto(Long kid, String dateFrom, String dateTo, Connection cn)" + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen und
     * Kosten pro Lieferant heraus. Der Rückgabewert ist ein OrderStatistikForm
     * <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countLieferantPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, bestellquelle, waehrung, bestellquelle, "
                    + "COUNT(*) AS anzahl FROM bestellungen WHERE KID=? AND orderdate >= ? AND orderdate <= ? "
                    + "GROUP BY bestellquelle ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                count = rs.getInt("anzahl");
                total = total + count;
                label = rs.getString("bestellquelle");
                if (label == null || label.equals("") || label.equals("0")) {
                    label = "k.A.";
                }
                osf.setLabel(label);
                osf.setAnzahl(count);
                osf.setPreiswaehrung(costsPerField(kid, dateFrom, dateTo, "bestellquelle", label, cn));
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countLieferantPerKonto(Long kid, String dateFrom, String dateTo, " + "Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl kostenpflichtigen
     * und gratis Bestellungen heraus. Der Rückgabewert ist ein
     * OrderStatistikForm. <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countGratisKostenpflichtigPerKonto(final Long kid, final String dateFrom,
            final String dateTo, final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();

        int free = 0;
        int pay = 0;
        int unknown = 0;
        int totalOrders = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT kaufpreis, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY kaufpreis ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("kaufpreis") != null) { // null = k.A.

                    if (rs.getFloat("kaufpreis") != 0.0) { // 0.0 = gratis
                        final int x = rs.getInt("anzahl");
                        pay = pay + x;
                        totalOrders = totalOrders + x;

                    } else {
                        final int x = rs.getInt("anzahl");
                        free = free + x;
                        totalOrders = totalOrders + x;
                    }

                } else { // k.A.
                    unknown = rs.getInt("anzahl");
                    totalOrders = totalOrders + unknown;
                }

            }

            final OrderStatistikForm ok = new OrderStatistikForm();
            ok.setLabel("stats.toPay");
            ok.setAnzahl(pay);
            list.add(ok);

            final OrderStatistikForm og = new OrderStatistikForm();
            og.setLabel("stats.free");
            og.setAnzahl(free);
            list.add(og);

            final OrderStatistikForm oka = new OrderStatistikForm();
            oka.setLabel("stats.notSpecified");
            oka.setAnzahl(unknown);
            list.add(oka);

            final OrderStatistikForm osf = new OrderStatistikForm();
            osf.setLabel("stats.total");
            osf.setAnzahl(totalOrders);
            list.add(osf);

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen

        } catch (final Exception e) {
            LOG.error("countGratisKostenpflichtigPerKonto(Long kid, "
                    + "String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return os;
    }

    /**
     * Berechnet die Kosten in den einzelnen Waehrungen anhand der kid und eines
     * Zeitraumes. Der Rückgabewert ist ein OrderStatistikForm. <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm sumGratisKostenpflichtigPerKonto(final Long kid, final String dateFrom,
            final String dateTo, final Connection cn) {

        final OrderStatistikForm cost = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, waehrung FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung ORDER BY summe DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("waehrung") != null) { // null = k.A.
                    final OrderStatistikForm osf = new OrderStatistikForm();
                    osf.setLabel(rs.getString("waehrung"));
                    final NumberFormat nf = NumberFormat.getInstance();
                    nf.setMinimumFractionDigits(2);
                    nf.setMaximumFractionDigits(2);
                    osf.setPreis(nf.format(rs.getFloat("summe")));
                    list.add(osf);
                }
            }

            cost.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen

        } catch (final Exception e) {
            LOG.error("sumGratisKostenpflichtigPerKonto(Long kid, "
                    + "String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return cost;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Lieferart heraus. Der Rückgabewert ist ein OrderStatistikForm. <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countLieferartPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT deloptions, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY deloptions ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                count = rs.getInt("anzahl");
                total = total + count;
                label = rs.getString("deloptions");
                osf.setLabel(label);
                osf.setAnzahl(count);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countLieferartPerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Medientyp heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countMediatypePerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT mediatype, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY mediatype ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                count = rs.getInt("anzahl");
                total = total + count;
                label = rs.getString("mediatype");
                osf.setLabel(label);
                osf.setAnzahl(count);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countMediatypePerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Fileformat heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countFileformatPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int countl = 0;
        int total = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT fileformat, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY fileformat ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                countl = rs.getInt("anzahl");
                total = total + countl;
                label = rs.getString("fileformat");
                osf.setLabel(label);
                osf.setAnzahl(countl);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countFileformatPerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Priorität heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countPriorityPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        int unknown = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT orderpriority, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY orderpriority ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                count = rs.getInt("anzahl");
                total = total + count;
                label = rs.getString("orderpriority");
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekannter Prio als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countPriorityPerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Gender heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countGenderPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT anrede, UID, COUNT(*) AS anzahl FROM ( "
                            + "SELECT anrede, UID, COUNT(*) AS z FROM ( SELECT anrede, u.UID FROM `bestellungen` AS b "
                            + "INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) "
                            + "AS temp GROUP by UID ) AS x GROUP BY anrede ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                if (rs.getString("anrede").equals("Herr")) {
                    label = "stats.male";
                }
                if (rs.getString("anrede").equals("Frau")) {
                    label = "stats.female";
                }
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "anrede", rs.getString("anrede"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "anrede",
                            rs.getString("anrede"), cn));
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekanntem Gender als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("stats.notSpecified");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "anrede", "", cn));
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countGenderPerKonto(Long kid, String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Institution heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countInstPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT institut, UID, COUNT(*) AS anzahl "
                            + "FROM ( SELECT institut, UID, COUNT(*) AS z FROM ( SELECT institut, u.UID FROM `bestellungen` "
                            + "AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp "
                            + "GROUP by UID ) AS x GROUP BY institut ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                label = rs.getString("institut");
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "institut", rs.getString("institut"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "institut", label, cn));
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekanntem Institut als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "institut", "", cn));
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countInstPerKonto(Long kid, String dateFrom, String dateTo, Connection cn)" + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Abteilung heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countAbteilungPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT abteilung, UID, COUNT(*) AS anzahl "
                            + "FROM ( SELECT abteilung, UID, COUNT(*) AS z FROM "
                            + "( SELECT abteilung, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) "
                            + "ON ( b.UID = u.UID ) WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x "
                            + "GROUP BY abteilung ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                label = rs.getString("abteilung");
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "abteilung", rs.getString("abteilung"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "abteilung", label, cn));
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekannter Abteilung als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "abteilung", "", cn));
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countAbteilungPerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Calculates the number of orders per category for a given time range.
     * 
     * @param Long kid
     * @param String dateFrom
     * @param String dateTo
     * @param Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countCategoriesPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT category, UID, COUNT(*) AS anzahl "
                            + "FROM ( SELECT category, UID, COUNT(*) AS z FROM "
                            + "( SELECT category, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) "
                            + "ON ( b.UID = u.UID ) WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x "
                            + "GROUP BY category ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                final Text category = new Text(cn, rs.getLong("category"), TextType.USER_CATEGORY);
                final String label = category.getInhalt();
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "category", rs.getString("category"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (label != null && !"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "category", category.getId()
                            .toString(), cn));
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // we declare orders with unknown category as "k.A."...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                osf.setPreiswaehrung(costsPerFieldInnerJoin(kid, dateFrom, dateTo, "category", "0", cn));
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countCategoriesPerKonto(Long kid, " + "String dateFrom, String dateTo, Connection cn): "
                    + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro PLZ
     * heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countPLZPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT plz, ort, UID, COUNT(*) AS anzahl "
                    + "FROM ( SELECT plz, ort, UID, COUNT(*) AS z "
                    + "FROM ( SELECT u.plz, u.ort, u.UID FROM `bestellungen` AS b "
                    + "INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) "
                    + "WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp "
                    + "GROUP by UID ) AS x GROUP BY plz ORDER BY plz ASC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                String labelTwo = "";
                label = rs.getString("plz");
                labelTwo = rs.getString("ort");
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "plz", rs.getString("plz"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setLabel_two(labelTwo);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekannter PLZ als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countPLZPerKonto(Long kid, String dateFrom, String dateTo, Connection cn)" + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Land heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countLandPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int orders = 0;
        int totalOrders = 0;
        int total = 0;
        int unknown = 0;
        int unknownOrders = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn
                    .prepareStatement("SELECT land, UID, COUNT(*) AS anzahl "
                            + "FROM ( SELECT land, UID, COUNT(*) AS z "
                            + "FROM ( SELECT u.land, u.UID FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) "
                            + "ON ( b.UID = u.UID ) WHERE b.kid=? AND orderdate >= ? AND orderdate <= ? ) AS temp GROUP by UID ) AS x "
                            + "GROUP BY land ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                label = rs.getString("land");
                count = rs.getInt("anzahl");
                orders = countRowsPerFeld(kid, dateFrom, dateTo, "land", rs.getString("land"), cn);
                total = total + count;
                totalOrders = totalOrders + orders;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(orders);
                    list.add(osf);
                } else {
                    unknown = unknown + count;
                    unknownOrders = unknownOrders + orders;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekanntem Land als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                osf.setAnzahl_two(unknownOrders);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);
            os.setTotal_two(totalOrders);

        } catch (final Exception e) {
            LOG.error("countLandPerKonto(Long kid, String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return os;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Anzahl Bestellungen pro
     * Zeitschrift heraus. Der Rückgabewert ist ein OrderStatistikForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countISSNPerKonto(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int count = 0;
        int total = 0;
        int others = 0;

        int user = 0;
        int usertotal = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT issn, zeitschrift, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY issn ORDER BY anzahl DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                String labelTwo = "";
                count = rs.getInt("anzahl");
                label = rs.getString("issn");
                labelTwo = rs.getString("zeitschrift");
                total = total + count;
                user = countRowsUIDPerISSN(kid, dateFrom, dateTo, label, cn);
                usertotal = usertotal + user;
                if (count > 1 && !"".equals(label)) { // nur Treffer, falls mehr als 1 Bestellung und ISSN nicht ""
                    osf.setLabel(label);
                    osf.setLabel_two(labelTwo);
                    osf.setAnzahl(count);
                    osf.setAnzahl_two(user); // Anzahl User pro ISSN
                    osf.setPreiswaehrung(costsPerField(kid, dateFrom, dateTo, "issn", label, cn));
                    list.add(osf);
                } else {
                    others = others + count;
                }
            }

            // hier werden Bestellungen mit nur 1 Treffert als "andere" aufgeführt...
            final OrderStatistikForm osf = new OrderStatistikForm();
            osf.setLabel("andere");
            osf.setAnzahl(others);
            list.add(osf);

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countISSNPerKonto(Long kid, String dateFrom, String dateTo, Connection cn)" + e.toString());
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
        return os;
    }

    /**
     * Holt die Kosten in den verschiedenen Währungen der Bestellungen pro
     * gewünschtem Feld. Der Rückgabewert eine PreisWaehrungForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo String feldbezeichnung
     * String wert Connection cn
     * @return PreisWaehrungForm
     */
    private PreisWaehrungForm costsPerField(final Long kid, final String dateFrom, final String dateTo,
            final String feldbezeichnung, final String wert, final Connection cn) {
        final PreisWaehrungForm pw = new PreisWaehrungForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT sum( kaufpreis ) AS summe, waehrung, " + feldbezeichnung
                    + " FROM bestellungen WHERE KID=? AND " + feldbezeichnung
                    + " = ? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung");
            pstmt.setLong(1, kid);
            pstmt.setString(2, wert);
            pstmt.setString(3, dateFrom);
            pstmt.setString(4, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {

                if (rs.getString("summe") != null && !rs.getString("summe").equals("")
                        && !rs.getString("summe").equals("0.00")) {

                    final PreisWaehrungForm pwf = new PreisWaehrungForm();
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

        } catch (final Exception e) {
            final StringBuffer bf = new StringBuffer(400);
            bf.append("In PreisWaehrungForm costsPerField(Long kid, String dateFrom, String dateTo, "
                    + "String feldbezeichnung, String wert, Connection cn) trat folgender Fehler auf:\n\n");
            bf.append(e);
            bf.append("\n\nKID:\040");
            bf.append(kid);
            bf.append("\ndateFrom:\040");
            bf.append(dateFrom);
            bf.append("\ndateTo:\040");
            bf.append(dateTo);
            bf.append("\nFeldbezeichnung:\040");
            bf.append(feldbezeichnung);
            bf.append("\nWert:\040");
            bf.append(wert);
            LOG.error(bf.toString());
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
        return pw;
    }

    /**
     * Holt die Kosten in den verschiedenen Währungen der Bestellungen pro
     * gewünschtem Feld in Verknüpfung mit der benutzer-Tabelle. Der
     * Rückgabewert eine PreisWaehrungForm <p></p>
     * 
     * @param Long kid String dateFrom String dateTo String feldbezeichnung
     * String wert Connection cn
     * @return PreisWaehrungForm
     */
    private PreisWaehrungForm costsPerFieldInnerJoin(final Long kid, final String dateFrom, final String dateTo,
            final String feldbezeichnung, final String wert, final Connection cn) {
        final PreisWaehrungForm pw = new PreisWaehrungForm();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT waehrung, sum( kaufpreis ) AS summe FROM `bestellungen` AS b "
                    + "INNER JOIN (`benutzer` AS u ) ON (b.UID=u.UID) WHERE u." + feldbezeichnung
                    + " = ? AND KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY waehrung");
            pstmt.setString(1, wert);
            pstmt.setLong(2, kid);
            pstmt.setString(3, dateFrom);
            pstmt.setString(4, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {

                if (rs.getString("summe") != null && !rs.getString("summe").equals("")
                        && !rs.getString("summe").equals("0.00")) {

                    final PreisWaehrungForm pwf = new PreisWaehrungForm();
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

        } catch (final Exception e) {
            final StringBuffer bf = new StringBuffer(400);
            bf.append("In PreisWaehrungForm costsPerFieldInnerJoin(Long kid, String dateFrom, String dateTo, "
                    + "String feldbezeichnung, String wert, Connection cn) trat folgender Fehler auf:\n\n");
            bf.append(e);
            bf.append("\n\nKID:\040");
            bf.append(kid);
            bf.append("\ndateFrom:\040");
            bf.append(dateFrom);
            bf.append("\ndateTo:\040");
            bf.append(dateTo);
            bf.append("\nFeldbezeichnung:\040");
            bf.append(feldbezeichnung);
            bf.append("\nWert:\040");
            bf.append(wert);
            LOG.error(bf.toString());
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
        return pw;
    }

    /**
     * Zählt anhand der kid, eines Zeitraumes die Anzahl Kunden, welche Artikel
     * bestellt haben. Der Rückgabewert ist die Anzahl Reihen <p></p>
     * 
     * @param Long kid String dateFrom String dateTo
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countRowsUID(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int anzahl = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT UID, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY UID ) AS temp");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                anzahl = rs.getInt("COUNT(*)");
                osf.setAnzahl(anzahl);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen

        } catch (final Exception e) {
            LOG.error("countRowsUID(Long kid, String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return os;
    }

    /**
     * Zählt anhand der kid, eines Zeitraumes und einer ISSN die Anzahl Kunden,
     * welche Artikel von einer bestimmten Zeitschrift bestellt haben. Der
     * Rückgabewert ist die Anzahl Reihen <p></p>
     * 
     * @param Long kid String dateFrom String dateTo String issn Connection cn
     * @return int anzahl
     */
    public int countRowsUIDPerISSN(final Long kid, final String dateFrom, final String dateTo, final String iss,
            final Connection cn) {
        int anzahl = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT UID, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND issn = ? AND orderdate >= ? AND orderdate <= ? GROUP BY UID ) AS temp");
            pstmt.setLong(1, kid);
            pstmt.setString(2, iss);
            pstmt.setString(3, dateFrom);
            pstmt.setString(4, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                anzahl = rs.getInt("COUNT(*)");
            }

        } catch (final Exception e) {
            final StringBuffer bf = new StringBuffer(300);
            bf.append("In countRowsUIDPerISSN(Long kid, String dateFrom, String dateTo, "
                    + "String issn, Connection cn) trat folgender Fehler auf:\n\n");
            bf.append(e);
            bf.append("\n\nKID:\040");
            bf.append(kid);
            bf.append("\ndateFrom:\040");
            bf.append(dateFrom);
            bf.append("\ndateTo:\040");
            bf.append(dateTo);
            bf.append("\nISSN:\040");
            bf.append(iss);
            LOG.error(bf.toString());
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
        return anzahl;
    }

    /**
     * Zählt anhand der kid, eines Zeitraumes und eines anzugebenden Feldes der
     * Tabelle Benutzer die Bestellungen. Der Rückgabewert ist die Anzahl Reihen
     * <p></p>
     * 
     * @param Long kid String dateFrom String dateTo String benutzerfeld String
     * inhalt Connection cn
     * @return int anzahl
     */
    public int countRowsPerFeld(final Long kid, final String dateFrom, final String dateTo, final String benutzerfeld,
            final String inhalt, final Connection cn) {
        int anzahl = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT COUNT(*) FROM ( SELECT BID FROM `bestellungen` AS b "
                    + "INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) WHERE b.KID=? AND u." + benutzerfeld + "=? "
                    + "AND orderdate >= ? AND orderdate <= ? ) AS temp");
            pstmt.setLong(1, kid);
            pstmt.setString(2, inhalt);
            pstmt.setString(3, dateFrom);
            pstmt.setString(4, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                anzahl = rs.getInt("COUNT(*)");
            }

        } catch (final Exception e) {
            final StringBuffer bf = new StringBuffer(400);
            bf.append("In countRowsPerFeld(Long kid, String dateFrom, String dateTo, "
                    + "String benutzerfeld, String inhalt, Connection cn) trat folgender Fehler auf:\n\n");
            bf.append(e);
            bf.append("\n\nKID:\040");
            bf.append(kid);
            bf.append("\ndateFrom:\040");
            bf.append(dateFrom);
            bf.append("\ndateTo:\040");
            bf.append(dateTo);
            bf.append("\nFeldbezeichnung:\040");
            bf.append(benutzerfeld);
            bf.append("\nWert:\040");
            bf.append(inhalt);
            LOG.error(bf.toString());
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
        return anzahl;
    }

    /**
     * Sucht anhand der kid und eines Zeitraumes die Jahresbereiche der
     * bestellten Zeitschriften. Der Rückgabewert ist ein OrderStatistikForm
     * <p></p>
     * 
     * @param Long kid String dateFrom String dateTo Connection cn
     * @return OrderStatistikForm os
     */
    public OrderStatistikForm countOrderYears(final Long kid, final String dateFrom, final String dateTo,
            final Connection cn) {
        final OrderStatistikForm os = new OrderStatistikForm();
        final ArrayList<OrderStatistikForm> list = new ArrayList<OrderStatistikForm>();
        int anzahl = 0;
        int total = 0;
        int unknown = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // SQL ausführen
            pstmt = cn.prepareStatement("SELECT jahr, COUNT(*) AS anzahl FROM bestellungen "
                    + "WHERE KID=? AND orderdate >= ? AND orderdate <= ? GROUP BY jahr ORDER BY jahr DESC");
            pstmt.setLong(1, kid);
            pstmt.setString(2, dateFrom);
            pstmt.setString(3, dateTo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                String label = "";
                label = rs.getString("jahr");
                anzahl = rs.getInt("anzahl");
                total = total + anzahl;
                if (!"".equals(label) && !"0".equals(label)) {
                    osf.setLabel(label);
                    osf.setAnzahl(anzahl);
                    list.add(osf);
                } else {
                    unknown = unknown + anzahl;
                }
            }

            if (unknown > 0) {
                // hier werden Bestellungen mit unbekanntem Jahr als "k.A." aufgeführt...
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setLabel("k.A.");
                osf.setAnzahl(unknown);
                list.add(osf);
            }

            os.setStatistik(list); // ...OrderStatistikForm für Rückgabe abfüllen
            os.setTotal(total);

        } catch (final Exception e) {
            LOG.error("countOrderYears(Long kid, String dateFrom, String dateTo, Connection cn): " + e.toString());
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
        return os;
    }

    /**
     * Zählt anhand eines Kontos {@link ch.dbs.entity.Benutzer} die Anzahl
     * Treffer einer Suche
     * 
     * @param pstmt
     * @return
     */
    public List<OrderStatistikForm> countSearchOrdersPerKonto(final PreparedStatement pstmt) {
        final ArrayList<OrderStatistikForm> auflistung = new ArrayList<OrderStatistikForm>();
        ResultSet rs = null;
        try {
            rs = pstmt.executeQuery();

            int total = 0;
            while (rs.next()) {
                final OrderStatistikForm osf = new OrderStatistikForm();
                osf.setAnzahl(rs.getInt("count(bid)"));
                total = total + osf.getAnzahl();
                osf.setTotal(total);
                auflistung.add(osf);
            }

        } catch (final Exception e) {
            LOG.error("countSearchOrdersPerKonto(PreparedStatement pstmt): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return auflistung;
    }

    public BigDecimal getKaufpreis() {
        return kaufpreis;
    }

    public void setKaufpreis(final BigDecimal kaufpreis) {
        this.kaufpreis = kaufpreis;
    }

    public String getPreisnachkomma() {
        return preisnachkomma;
    }

    public void setPreisnachkomma(final String preisnachkomma) {
        this.preisnachkomma = preisnachkomma;
    }

    public String getPreisvorkomma() {
        return preisvorkomma;
    }

    public void setPreisvorkomma(final String preisvorkomma) {
        this.preisvorkomma = preisvorkomma;
    }

    public String getFileformat() {
        return fileformat;
    }

    public void setFileformat(final String fileformat) {
        this.fileformat = fileformat;
    }

    public Lieferanten getLieferant() {
        return lieferant;
    }

    public void setLieferant(final Lieferanten lieferant) {
        this.lieferant = lieferant;
    }

    public String getDeloptions() {
        return deloptions;
    }

    public void setDeloptions(final String deloptions) {
        this.deloptions = deloptions;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getArtikeltitel() {
        return artikeltitel;
    }

    public void setArtikeltitel(final String artikeltitel) {
        this.artikeltitel = artikeltitel;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(final String autor) {
        this.autor = autor;
    }

    public String getBibliothek() {
        return bibliothek;
    }

    public void setBibliothek(final String bibliothek) {
        this.bibliothek = bibliothek;
    }

    public String getSigel() {
        return sigel;
    }

    public void setSigel(final String sigel) {
        this.sigel = sigel;
    }

    public String getBestellquelle() {
        return bestellquelle;
    }

    public void setBestellquelle(final String bestellquelle) {
        this.bestellquelle = bestellquelle;
    }

    public String getHeft() {
        return heft;
    }

    public void setHeft(final String heft) {
        this.heft = heft;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(final String issn) {
        this.issn = issn;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }

    public String getJahrgang() {
        return jahrgang;
    }

    public void setJahrgang(final String jahrgang) {
        this.jahrgang = jahrgang;
    }

    public String getSeiten() {
        return seiten;
    }

    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }

    public String getSubitonr() {
        return subitonr;
    }

    public void setSubitonr(final String subitonr) {
        this.subitonr = subitonr;
    }

    public String getZeitschrift() {
        return zeitschrift;
    }

    public void setZeitschrift(final String zeitschrift) {
        this.zeitschrift = zeitschrift;
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

    public String getSystembemerkung() {
        return systembemerkung;
    }

    public void setSystembemerkung(final String systembemerkung) {
        this.systembemerkung = systembemerkung;
    }

    public String getStatusdate() {
        return statusdate;
    }

    public void setStatusdate(final String statusdate) {
        this.statusdate = statusdate;
    }

    public String getStatustext() {
        return statustext;
    }

    public void setStatustext(final String statustext) {
        this.statustext = statustext;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(final String orderdate) {
        this.orderdate = orderdate;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(final String notizen) {
        this.notizen = notizen;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }

    public String getBuchtitel() {
        return buchtitel;
    }

    public void setBuchtitel(final String buchtitel) {
        this.buchtitel = buchtitel;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(final String doi) {
        this.doi = doi;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public String getKapitel() {
        return kapitel;
    }

    public void setKapitel(final String kapitel) {
        this.kapitel = kapitel;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(final String mediatype) {
        this.mediatype = mediatype;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(final String pmid) {
        this.pmid = pmid;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(final String verlag) {
        this.verlag = verlag;
    }

    public String getGbvnr() {
        return gbvnr;
    }

    public void setGbvnr(final String gbvnr) {
        this.gbvnr = gbvnr;
    }

    public String getInterne_bestellnr() {
        return interne_bestellnr;
    }

    public void setInterne_bestellnr(final String interne_bestellnr) {
        this.interne_bestellnr = interne_bestellnr;
    }

    public String getTrackingnr() {
        return trackingnr;
    }

    public void setTrackingnr(final String trackingnr) {
        this.trackingnr = trackingnr;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(final boolean erledigt) {
        this.erledigt = erledigt;
    }

    public boolean isPreisdefault() {
        return preisdefault;
    }

    public void setPreisdefault(final boolean preisdefault) {
        this.preisdefault = preisdefault;
    }

    public String getSignatur() {
        return signatur;
    }

    public void setSignatur(final String signatur) {
        this.signatur = signatur;
    }

    private PreparedStatement setOrderValues(final PreparedStatement ps, final Bestellungen b) throws Exception {
        ps.setLong(1, b.getKonto().getId());
        ps.setLong(2, b.getBenutzer().getId());
        if (b.getLieferant() == null || b.getLieferant().getLid() == null
                || "".equals(b.getLieferant().getLid().toString()) || "0".equals(b.getLieferant().getLid().toString())) {
            ps.setString(3, "1");
        } else {
            ps.setLong(3, b.getLieferant().getLid());
        }
        if (b.getPriority() == null || "".equals(b.getPriority())) {
            ps.setString(4, "normal");
        } else {
            ps.setString(4, b.getPriority());
        }
        if (b.getDeloptions() == null) {
            ps.setString(5, "");
        } else {
            ps.setString(5, b.getDeloptions());
        }
        if (b.getFileformat() == null) {
            ps.setString(6, "");
        } else {
            ps.setString(6, b.getFileformat());
        }
        if (b.getHeft() == null) {
            ps.setString(7, "");
        } else {
            ps.setString(7, b.getHeft());
        }
        if (b.getSeiten() == null) {
            ps.setString(8, "");
        } else {
            ps.setString(8, b.getSeiten());
        }
        ps.setString(9, b.getIssn());
        ps.setString(10, b.getSigel());
        ps.setString(11, b.getBibliothek());
        if (b.getAutor() == null) {
            ps.setString(12, "");
        } else {
            ps.setString(12, b.getAutor());
        }
        if (b.getArtikeltitel() == null) {
            ps.setString(13, "");
        } else {
            ps.setString(13, b.getArtikeltitel());
        }
        if (b.getJahrgang() == null) {
            ps.setString(14, "");
        } else {
            ps.setString(14, b.getJahrgang());
        }
        if (b.getZeitschrift() == null) {
            ps.setString(15, "");
        } else {
            ps.setString(15, b.getZeitschrift());
        }
        if (b.getJahr() == null) {
            ps.setString(16, "");
        } else {
            ps.setString(16, b.getJahr());
        }
        if (b.getSubitonr() == null) {
            ps.setString(17, "");
        } else {
            ps.setString(17, b.getSubitonr());
        }
        if (b.getGbvnr() != null && b.getGbvnr().equals("")) {
            ps.setString(18, null); // falls keine Angaben => null setzen (Anzeige)
        } else {
            ps.setString(18, b.getGbvnr());
        }
        if (b.getTrackingnr() == null) {
            ps.setString(19, "");
        } else {
            ps.setString(19, b.getTrackingnr());
        }
        if (b.getInterne_bestellnr() == null) {
            ps.setString(20, "");
        } else {
            ps.setString(20, b.getInterne_bestellnr());
        }
        if (b.getSystembemerkung() == null) {
            ps.setString(21, "");
        } else {
            ps.setString(21, b.getSystembemerkung());
        }
        if (b.getNotizen() == null) {
            ps.setString(22, "");
        } else {
            ps.setString(22, b.getNotizen());
        }
        if (b.getKaufpreis() != null) {
            ps.setBigDecimal(23, b.getKaufpreis());
        } else {
            ps.setString(23, null); // keine Preisangaben als Null setzen
        }
        if (b.getWaehrung() != null) {
            ps.setString(24, b.getWaehrung());
        } else {
            if (b.getKaufpreis() == null) {
                ps.setString(24, null);
            } else {
                ps.setString(24, "EUR"); // um Kombination Kaufpreis!= null && Waherung==null zu verhindern
            }
        }
        if (b.getDoi() == null) {
            ps.setString(25, "");
        } else {
            ps.setString(25, b.getDoi());
        }
        if (b.getDoi() == null) {
            ps.setString(26, "");
        } else {
            ps.setString(26, b.getPmid());
        }
        if (b.getDoi() == null) {
            ps.setString(27, "");
        } else {
            ps.setString(27, b.getIsbn());
        }
        ps.setString(28, b.getMediatype());
        if (b.getDoi() == null) {
            ps.setString(29, "");
        } else {
            ps.setString(29, b.getVerlag());
        }
        if (b.getDoi() == null) {
            ps.setString(30, "");
        } else {
            ps.setString(30, b.getKapitel());
        }
        if (b.getDoi() == null) {
            ps.setString(31, "");
        } else {
            ps.setString(31, b.getBuchtitel());
        }
        ps.setString(32, b.getOrderdate());
        ps.setString(33, b.getStatusdate());
        ps.setString(34, b.getStatustext());

        if (b.getBestellquelle() == null || b.getBestellquelle().equals("") || b.getBestellquelle().equals("0")) {
            ps.setString(35, "k.A."); // Wenn keine Lieferantenangaben gemacht wurden den Eintrag auf "k.A." setzen
        } else {
            ps.setString(35, b.getBestellquelle());
        }
        ps.setString(36, b.getSignatur());
        return ps;
    }

}
