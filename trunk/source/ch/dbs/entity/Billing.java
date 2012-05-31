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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.grlea.log.SimpleLogger;

import util.DBConn;
import ch.dbs.form.BillingForm;

/**
 * Abstract base class for entities having a {@link Long} unique identifier,
 * this provides the base functionality for them. <p></p>
 *
 * @author Pascal Steiner
 */
public class Billing extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Billing.class);

    private AbstractBenutzer user;
    private Konto konto;
    private Text rechnungsgrund;
    private double betrag;
    private String waehrung;
    private String rechnungsnummer;
    private Date rechnungsdatum;
    private Date zahlungseingang;
    private boolean storniert;

    public Billing() {
    }

    /**
     * Erstellt für ein Konto ein Rechnungsobjekt
     * @param k Konto
     * @param invoiceReason
     * @param betr
     * @param waehr
     */
    public Billing(final Konto k, final Text invoiceReason, final double betr, final String waehr) {
        this.konto = k;
        this.rechnungsgrund = invoiceReason;
        this.rechnungsnummer = generateBillingNumber(k);
        this.betrag = betr;
        this.waehrung = waehr;
        this.rechnungsdatum = new Date(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * Erstellt aus einem Resultset eine Billing Objekt
     *
     * @param rs ResultSet
     * @return Billing
     */
    public Billing(final Long id, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `billing` WHERE ID = ?");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(rs, cn);
            }

        } catch (final SQLException e) {
            LOG.error("Billing(Long, Connection): " + e.toString());
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
     * Erstellt aus einem Resultset eine Billing Objekt
     *
     * @param rs ResultSet
     * @param Connection
     * @return Billing
     */
    private Billing(final ResultSet rs, final Connection cn) {
        this.setRsValues(rs, cn);
    }

    private void setRsValues(final ResultSet rs, final Connection cn) {
        try {
            final AbstractBenutzer b = new AbstractBenutzer();
            this.setId(rs.getLong("ID"));
            this.setUser(b.getUser(rs.getLong("UID"), cn));
            this.setKonto(new Konto(rs.getLong("KID"), cn));
            this.setRechnungsgrund(new Text(cn, rs.getLong("rechnungsgrund")));
            this.setBetrag(rs.getDouble("betrag"));
            this.setWaehrung(rs.getString("waehrung"));
            this.setRechnungsnummer(rs.getString("rechnungsnummer"));
            this.setRechnungsdatum(rs.getDate("rechnungsdatum"));
            this.setZahlungseingang(rs.getDate("zahlungseingang"));
            this.setStorniert(rs.getBoolean("storniert"));
        } catch (final SQLException e) {
            LOG.error("RsValues(ResultSet rs): " + e.toString());
        }
    }

    /**
     * Erstellt aus einem BillingForm ein Billing Objekt
     *
     * @param rs
     * @return Billing
     */
    public Billing getBilling(final BillingForm bf) {

        final Billing b = new Billing();

        try {
            final AbstractBenutzer ab = new AbstractBenutzer();
            b.setId(bf.getBillid());
            b.setUser(ab.getUser(bf.getUserid(), b.getConnection()));
            b.setKonto(new Konto(bf.getKontoid(), b.getConnection()));
            b.setRechnungsgrund(new Text(b.getConnection(), bf.getRechnungsgrundid()));
            b.setBetrag(bf.getBetrag());
            b.setWaehrung(bf.getWaehrung());
            b.setRechnungsnummer(bf.getRechnungsnummer());
            b.setRechnungsdatum(bf.getRechnungsdatum());
            b.setZahlungseingang(bf.getZahlungseingang());
        } catch (final Exception e) {
            LOG.error("getBilling(BillingForm bf): " + e.toString());
        } finally {
            b.close();
        }
        return b;
    }

    /**
     * Generiert eine Rechnungsnummer anhand des Kontos und dem Datum sowie aus der Anzahl bereits
     * erstellter Rechnungen für dieses Konto
     *
     * @author Pascal Steiner
     * @param Konto k
     * @return String Rechnungsnummer
     */
    private String generateBillingNumber(final Konto k) {
        final StringBuffer billNr = new StringBuffer();
        //aktuelles Jahr zum String erstellen
        final Date d = new Date(Calendar.getInstance().getTimeInMillis());
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-");
        billNr.append(fmt.format(d));
        //Kundennummer zur Rechnungsummer hinzufügen
        billNr.append(k.getId().toString());
        billNr.append('-');
        //Gesamtanzahl Rechnungen welche an Konto erstellt wurden ermitteln und Kontorechnungsnummer erstellen
        billNr.append(countBillings(k));
        return billNr.toString();
    }

    /**
     * Speichert / Veraendert dieses Rechnungsobjekt in der Datenbank
     */
    public void save(final Connection cn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Neue Rechnungen abspeichern
            if (this.getId() == null) {
                pstmt = setBillingValues(cn
                        .prepareStatement("INSERT INTO `billing` (`UID` , `KID` , `rechnungsgrund` , `betrag` , `waehrung` , "
                                + "`rechnungsnummer` , `rechnungsdatum`, `zahlungseingang`) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"));
                pstmt.execute();

                // ID im objekt abspeichern
                pstmt = cn.prepareStatement("SELECT LAST_INSERT_ID()");
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    this.setId(rs.getLong("LAST_INSERT_ID()"));
                }
            } else { // bestehende Rechnung veraendern
                pstmt = setBillingValues(cn
                        .prepareStatement("UPDATE `billing` SET `UID`=? , `KID`=? , `rechnungsgrund`=? , "
                                + "`betrag`=? , `waehrung`=? , `rechnungsnummer`=? , `rechnungsdatum`=?, "
                                + "`zahlungseingang`=? WHERE `ID`=" + this.getId()));

                pstmt.execute();
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

    public List<Billing> getBillings(final Konto k, final Connection cn) {

        final ArrayList<Billing> bl = new ArrayList<Billing>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `billing` WHERE KID=? "
                    + "ORDER BY `zahlungseingang`, `rechnungsdatum`");
            pstmt.setLong(1, k.getId());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                bl.add(new Billing(rs, cn));
            }
        } catch (final Exception e) {
            LOG.error("getBillings(Konto k, Connection cn): " + e.toString());
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

    public void update(final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = setBillingValues(cn.prepareStatement("UPDATE `billing` SET UID=? , "
                    + "KID=?, rechnungsgrund=?, betrag=?, waehrung=?, rechnungsnummer=?, rechnungsdatum=?, "
                    + "zahlungseingang=? WHERE `ID` = " + this.getId()));

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
     * Liefert die letzte offene Rechnung zu einem Konto
     * @param Konto
     * @param Connection
     * @return Billing oder null wenn Konto noch keine offenen Rechnungen hat
     */
    public Billing getLastBilling(final Konto k, final Connection cn) {
        Billing b = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // TODO: NOW() berücksichtigt nur Serverzeit und funktioniert nicht im Zusammenhang mit anderen Zeitzonen.
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `konto` \n" + "INNER JOIN billing USING ( kid ) \n"
                    + "INNER JOIN text on (TID=rechnungsgrund)\n" + "WHERE billing.zahlungseingang IS NULL \n"
                    + "and konto.kontostatus = 1\n" + "and expdate>=now()\n" + "and inhalt != 'Dienstleistung' \n"
                    + "and konto.KID=? \n" + "ORDER BY konto.`biblioname` ASC, \n" + "billing.id DESC \n" + "limit 1");
            pstmt.setLong(1, k.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                b = new Billing(rs, cn);
            }

        } catch (final Exception e) {
            LOG.error("getLastBilling(Konto k, Connection cn): " + e.toString());
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
     * Zählt die Rechnungen zum Konto
     * @param k
     * @return
     */
    private int countBillings(final Konto k) {
        final DBConn cn = new DBConn();
        int z = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.getConnection().prepareStatement("SELECT count(kid) as anzahl FROM `billing` WHERE kid=?");
            pstmt.setLong(1, k.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                z = rs.getInt("anzahl");
            }

        } catch (final Exception e) {
            LOG.error("countBillings(Konto k): " + e.toString());
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
            cn.close();
        }
        return z;
    }

    /**
     * Statement vorbereiten
     * @param pstmt
     * @return pstmt
     */
    private PreparedStatement setBillingValues(final PreparedStatement pstmt) {
        try {
            if (this.getUser() != null) {
                pstmt.setLong(1, this.getUser().getId());
            } else {
                pstmt.setString(1, null);
            }
            if (this.getKonto() != null) {
                pstmt.setLong(2, this.getKonto().getId());
            } else {
                pstmt.setString(2, null);
            }
            if (this.getRechnungsgrund() != null) {
                pstmt.setLong(3, this.getRechnungsgrund().getId());
            } else {
                pstmt.setString(3, null);
            }
            pstmt.setDouble(4, this.getBetrag());
            pstmt.setString(5, this.getWaehrung());
            pstmt.setString(6, this.getRechnungsnummer());
            pstmt.setDate(7, this.getRechnungsdatum());
            if (this.getZahlungseingang() != null) {
                pstmt.setDate(8, this.getZahlungseingang());
            } else {
                pstmt.setString(8, null);
            }
        } catch (final Exception e) {
            LOG.error("setBillingValues(PreparedStatement pstmt): " + e.toString());
        }
        return pstmt;
    }

    public AbstractBenutzer getUser() {
        return user;
    }

    public void setUser(final AbstractBenutzer user) {
        this.user = user;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(final Konto konto) {
        this.konto = konto;
    }

    public Text getRechnungsgrund() {
        return rechnungsgrund;
    }

    public void setRechnungsgrund(final Text rechnungsgrund) {
        this.rechnungsgrund = rechnungsgrund;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(final double betrag) {
        this.betrag = betrag;
    }

    public String getRechnungsnummer() {
        if (rechnungsnummer == null) {
            rechnungsnummer = generateBillingNumber(this.konto);
        }
        return rechnungsnummer;
    }

    public void setRechnungsnummer(final String rechnungsnummer) {
        this.rechnungsnummer = rechnungsnummer;
    }

    public Date getRechnungsdatum() {
        return rechnungsdatum;
    }

    public void setRechnungsdatum(final Date rechnungsdatum) {
        this.rechnungsdatum = rechnungsdatum;
    }

    public Date getZahlungseingang() {
        return zahlungseingang;
    }

    public void setZahlungseingang(final Date zahlungseingang) {
        this.zahlungseingang = zahlungseingang;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public void setStorniert(final boolean storniert) {
        this.storniert = storniert;
    }

}
