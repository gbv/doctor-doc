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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.grlea.log.SimpleLogger;

import util.DBConn;
import util.ReadSystemConfigurations;
import ch.dbs.form.KontoForm;
import ch.dbs.form.UserInfo;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Pascal Steiner
 */
public class Konto extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Konto.class);

    private String bibliotheksname;
    private String isil; // International Standard Identifier for Libraries and Related Organizations (must be null)
    private String Adresse;
    private String Adressenzusatz;
    private String PLZ;
    private String Ort;
    private String Land;
    private String timezone = ReadSystemConfigurations.getSystemTimezone();
    private String faxno; // DD-Faxservernummer, nur durch Admin editierbar!
    private String faxusername;
    private String faxpassword;
    private String popfaxend;
    private String fax_extern; // externe Faxnummer, editierbar durch Kunde
    private String Telefon;
    private String Bibliotheksmail; // Bibliothekskontakt
    private String dbsmail; // This is the email that receives ILL deliveries
    private String dbsmailpw;
    private String gbvbenutzername;
    private String gbvpasswort;
    private String gbvrequesterid; // ID jeder einzelnen Bibliothek beim GBV
    private String ezbid;
    private String instlogolink; // Link to a logo/image on a remote server
    // gibt an, ob die Bibliothek seinen Bestand in der ZDB eingepflegt hat (Verfügbarkeitsprüfung)
    private boolean zdb = false;
    // Globale Einstellung, von wem die Rechnungen beglichen werden soll. Diese Einstellung kann durch den
    // Wert welcher beim User hinterlegt ist überschrieben werden.
    private Text billing;
    // Globale Einstellung wie die Rechnung an den Kunden geschickt werden soll. Verweis auf die Tabelle
    // Text mit dem Texttyp Billingtype
    private Text billingtype;
    // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll in Fr.-
    private int accounting_rhythmvalue = 0;
    // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll. (in Tagen)
    private int accounting_rhythmday = 0;
    // Wird die Schwelle im Accounting_rhytm nicht erreicht, wird nach .... trotzdem eine Rechnung
    // gestellt. Feld leer oder 0 = kein Schwellwert
    private int accounting_rhythmtimeout = 0;
    private int threshold_value = 0; /* Verrechnungsschwellwert Sammelrechnungen in Tagen */
    private int maxordersu = 0; // Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
    private int maxordersutotal = 0; // Begrenzung möglicher Bestellungen durch einen Benutzer pro Jahr
    private int maxordersj = 0; // Legt die maximale Artikelanzahl eines Kontos pro Jahr fest
    // Boolean. Bei True gelten die Beschrnkungen in maxordersj, maxordersutotal und maxordersu
    private int orderlimits = 0;
    private boolean userlogin = false; // Dürfen sich "Nichtbibliothekare" einloggen
    private boolean userbestellung = false; // Dürfen "Nichtbibliothekare" bei SUBITO Bestellungen tätigen?
    private boolean gbvbestellung = false; // Dürfen "Nichtbibliothekare" beim GBV Bestellungen tätigen?
    private boolean selected = false; // Benutzer hat rechte bei diesem Konto angemeldet (Useraction.changeuserdetails)
    private boolean kontostatus; // Aktiv / Inaktiv
    // Konto Basic = 0 / 1 Jahr Konto Enhanced = 1 /
    // 1 Jahr Konto Enhanced plus Fax = 2 / 3 Monate Konto Enhanced plus Fax = 3
    private int kontotyp;
    private String default_deloptions = "post"; // Konto-Default Einstellung für deloptions
    private java.sql.Date paydate; //Zahlungseingangsdatum.
    private java.sql.Date expdate; // Konto Ablaufdatum. Automatische zurückstufung Kontotyp = 0 falls date>expdate
    private Date edatum; // Erstellungsdatum des kontos
    private String gtc; // enthält GTC-Version (General Terms and Conditions)
    private String gtcdate; // Datum der Annahme durch User



    public Konto() {

    }

    public Konto(KontoForm kf) {
        if (kf.getBiblioname() != null) { this.bibliotheksname = kf.getBiblioname().trim(); }
        if (kf.getIsil() != null && !kf.getIsil().equals("")) {
            this.isil = kf.getIsil().trim();
        } else {
            this.isil = null; // kann null sein
        }
        if (kf.getAdresse() != null) { this.Adresse = kf.getAdresse().trim(); }
        if (kf.getAdressenzusatz() != null) { this.Adressenzusatz = kf.getAdressenzusatz().trim(); }
        if (kf.getPLZ() != null) { this.PLZ = kf.getPLZ().trim(); }
        if (kf.getOrt() != null) { this.Ort = kf.getOrt().trim(); }
        if (kf.getLand() != null) { this.Land = kf.getLand(); }
        if (kf.getTimezone() != null) { this.timezone = kf.getTimezone(); }
        if (kf.getFaxno() != null) { this.faxno = kf.getFaxno(); } // DD-Faxservernummer, nur durch Admin editierbar!
        if (kf.getFaxusername() != null) { this.faxusername = kf.getFaxusername(); }
        if (kf.getFaxpassword() != null) { this.faxpassword = kf.getFaxpassword(); }
        if (kf.getPopfaxend() != null) { this.popfaxend = kf.getPopfaxend(); }
        // externe Faxnummer, editierbar durch Kunde
        if (kf.getFax_extern() != null) { this.fax_extern = kf.getFax_extern().trim(); }
        if (kf.getTelefon() != null) { this.Telefon = kf.getTelefon().trim(); }
        // Bibliothekskontakt
        if (kf.getBibliotheksmail() != null) { this.Bibliotheksmail = kf.getBibliotheksmail().trim(); }
        // This is the email that receives ILL deliveries
        if (kf.getDbsmail() != null) { this.dbsmail = kf.getDbsmail().trim(); }
        if (kf.getDbsmailpw() != null) { this.dbsmailpw = kf.getDbsmailpw(); }
        this.gbvbenutzername = kf.getGbvbenutzername();
        this.gbvpasswort = kf.getGbvpasswort();
        this.gbvrequesterid = kf.getGbvrequesterid();
        if (kf.getEzbid() != null) { this.ezbid = kf.getEzbid(); }
        this.instlogolink = kf.getInstlogolink();
        this.zdb = kf.isZdb(); // ZDB-Teilnehmer
        if (kf.getBilling() != null) { this.billing = kf.getBilling(); }
        if (kf.getBillingtype() != null) { this.billingtype = kf.getBillingtype(); }
        this.accounting_rhythmvalue = kf.getAccounting_rhythmvalue();
        this.accounting_rhythmday = kf.getAccounting_rhythmday();
        this.accounting_rhythmtimeout = kf.getAccounting_rhythmtimeout();
        /* Verrechnungsschwellwert Sammelrechnungen in Tagen */
        this.threshold_value = kf.getThreshold_value();
        this.maxordersu = kf.getMaxordersu(); // Begrenzung möglicher Bestellungen durch einen Benutzer
        this.maxordersutotal = kf.getMaxordersutotal(); // Begrenzung durch einen Benutzer pro Jahr
        this.maxordersj = kf.getMaxordersj(); // Legt die maximale Artikelanzahl eines Kontos pro Jahr fest
        // Bei True gelten die Beschränkungen in maxordersj, maxordersutotal und maxordersu
        this.orderlimits = kf.getOrderlimits();
        this.userlogin = kf.isUserlogin(); // Drfen sich "Nichtbibliothekare" einloggen
        this.userbestellung = kf.isUserbestellung(); // Dürfen "Nichtbibliothekare" bei SUBITO Bestellungen tätigen?
        this.gbvbestellung = kf.isGbvbestellung(); // Dürfen "Nichtbibliothekare" beim GBV Bestellungen tätigen?
        this.selected = kf.isSelected(); // (Useraction.changeuserdetails)
        this.kontostatus = kf.isKontostatus(); // Aktiv / Inaktiv
        this.kontotyp = kf.getKontotyp();
        this.default_deloptions = kf.getDefault_deloptions();
        //      this.Date edatum; // Erstellungsdatum des kontos
        //      this.gtc; // enthält GTC-Version (General Terms and Conditions)
        //      this.gtcdate; // Datum der Annahme durch User
    }

    /*
     * Füllt ein Kontoobjekt mit einer Zeile aus der Datenbank
     */
    public Konto(ResultSet rs) throws Exception {

        this.setRsValues(rs);
    }


    private void setRsValues(ResultSet rs) throws Exception {
        this.setId(rs.getLong("KID"));
        this.setBibliotheksname(rs.getString("biblioname"));
        this.setIsil(rs.getString("isil"));
        this.setAdresse(rs.getString("adresse"));
        this.setAdressenzusatz(rs.getString("adresszusatz"));
        try {  rs.findColumn("k.plz");
        this.setPLZ(rs.getString("k.plz"));
        this.setOrt(rs.getString("k.ort"));
        } catch (SQLException se) {
            this.setPLZ(rs.getString("plz"));
            this.setOrt(rs.getString("ort"));
        }
        this.setLand(rs.getString("land"));
        if (rs.getString("timezone") != null) { this.setTimezone(rs.getString("timezone")); }
        this.setTelefon(rs.getString("telefon"));
        this.setFaxno(rs.getString("faxno"));
        this.setFaxusername(rs.getString("faxusername"));
        this.setFaxpassword(rs.getString("faxpassword"));
        this.setPopfaxend(rs.getString("popfaxend"));
        this.setFax_extern(rs.getString("fax2"));
        this.setBibliotheksmail(rs.getString("bibliomail")); // Bibliothekskontakt
        this.setDbsmail(rs.getString("dbsmail")); // This is the email that receives ILL deliveries
        this.setDbsmailpw(rs.getString("dbsmailpw"));
        this.setGbvbenutzername(rs.getString("gbvbn"));
        this.setGbvpasswort(rs.getString("gbvpw"));
        this.setGbvrequesterid(rs.getString("gbv_requester_id"));
        this.setEzbid(rs.getString("ezbid"));
        this.setInstlogolink(rs.getString("instlogolink"));
        this.setZdb(rs.getBoolean("zdb"));
        this.setAccounting_rhythmvalue(rs.getInt("accounting_rhythmvalue"));
        this.setAccounting_rhythmtimeout(rs.getInt("accounting_rhythmtimeout"));
        this.setThreshold_value(rs.getInt("billingschwellwert"));
        this.setMaxordersu(rs.getInt("maxordersu"));
        this.setMaxordersutotal(rs.getInt("maxordersutotal"));
        this.setMaxordersj(rs.getInt("maxordersj"));
        this.setOrderlimits(rs.getInt("orderlimits"));
        this.setUserlogin(rs.getBoolean("userlogin"));
        this.setUserbestellung(rs.getBoolean("userbestellung"));
        this.setGbvbestellung(rs.getBoolean("gbvbestellung"));
        this.setKontostatus(rs.getBoolean("kontostatus"));
        this.setKontotyp(rs.getInt("kontotyp"));
        this.setDefault_deloptions(rs.getString("default_deloptions"));
        this.setPaydate(rs.getDate("paydate"));
        this.setExpdate(rs.getDate("expdate"));
        this.setEdatum(rs.getDate("edatum"));
        this.setGtc(rs.getString("gtc"));
        this.setGtcdate(rs.getString("gtcdate"));
    }


    /**
     * Erstellt ein Kontoobjekt anhand einer Verbindung und der ID
     *
     * @param Long kid
     * @param Connection cn
     * @return
     */
    public Konto(Long kid, Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM konto WHERE KID = ?");
            pstmt.setLong(1, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(rs);
            }

        } catch (Exception e) {
            LOG.error("Konto (Long kid, Connection cn): " + e.toString());
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
     * Listet alle Kontos auf, sortiert nach Bibliotheksnamen
     * @return Kontolist
     */
    public ArrayList<Konto> getAllKontos(Connection cn) {

        ArrayList<Konto> kontos = new ArrayList<Konto>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `konto` order by `kontotyp` desc, `biblioname` asc");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kontos.add(new Konto(rs));
            }

        } catch (Exception e) {
            LOG.error("Konto (Long kid, Connection cn): " + e.toString());
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
        return kontos;
    }

    /**
     * Speichert diese Konto und hinterlegt die ID<p></p>
     *
     * @param cn
     */
    public Long save(Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Konto kontoInstance = new Konto();
            pstmt = kontoInstance.setKontoValues(cn.prepareStatement("INSERT INTO `konto` (`biblioname` , `isil` , "
                    + "`adresse` , `adresszusatz` , `plz` , `ort` , `land` , `timezone` , `telefon` , `faxno` , "
                    + "`faxusername` , `faxpassword` , `popfaxend` , `fax2` , `bibliomail` , `dbsmail` , "
                    + "`dbsmailpw` , `gbvbn` , `gbvpw` , `gbv_requester_id` , `ezbid` , `instlogolink` , `zdb` , "
                    + "`billing` , `billingtype` , `accounting_rhythmvalue` , `accounting_rhythmday` , "
                    + "`accounting_rhythmtimeout` , `billingschwellwert` , `maxordersu` ,`maxordersutotal`, "
                    + "`maxordersj` , `orderlimits` , `userlogin` , `userbestellung` , `gbvbestellung` , "
                    + "`kontostatus` , `kontotyp` , `default_deloptions` , `paydate`, `expdate`,`edatum`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())"), this);

            pstmt.executeUpdate();

            // ID des gerade gespeicherten Konto ermitteln und hinterlegen
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                this.setId(rs.getLong("LAST_INSERT_ID()"));
            }

        } catch (Exception e) {
            LOG.error("save(Connection cn): " + e.toString());
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

        return this.getId();
    }

    /**
     * Modifiziert dieses Konto<p></p>
     *
     * @param cn
     */
    public void update(Connection cn) {
        PreparedStatement pstmt = null;
        try {
            pstmt = setKontoValues(cn.prepareStatement("UPDATE `konto` SET biblioname=?, isil=?, "
                    + "adresse=?, adresszusatz=?, plz=?, ort=?, land=?, timezone=?, telefon=?, faxno=?, "
                    + "faxusername=? , faxpassword=?, popfaxend=?, fax2=?, bibliomail=?, dbsmail=?, dbsmailpw=?, "
                    + "gbvbn=?, gbvpw=?, gbv_requester_id=?, ezbid=?, instlogolink=?, zdb=?, billing=?, "
                    + "billingtype=?, accounting_rhythmvalue=?, accounting_rhythmday=?, accounting_rhythmtimeout=?, "
                    + "billingschwellwert=?, maxordersu=?, maxordersutotal=?, maxordersj=?, orderlimits=?, "
                    + "userlogin=?, userbestellung=?, gbvbestellung=?, kontostatus=?, kontotyp=?, "
                    + "default_deloptions=?, paydate=?, expdate=? "
                    + "WHERE `KID` = " + this.getId()), this);

            pstmt.executeUpdate();

        } catch (Exception e) {
            LOG.error("update(): " + e.toString());
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
     * Löscht dieses Kontoobjekt, ohne weitere Prüfungen.
     * Vorsicht vor Benutzern, Kontoverknüpfungen, Texten,usw...
     *
     * @return Rückmeldung o das Konto gelöscht werden konnte
     */
    public boolean deleteSelf(Connection cn) {

        boolean success = false;

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `konto` WHERE `KID` = ?");
            pstmt.setLong(1, this.getId());
            pstmt.executeUpdate();

            success = true;

        } catch (SQLException e) {
            LOG.error("deleteSelf(Connection cn): " + e.toString());
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

    /*
     * holt alle für einen User erlaubte Kontos und selecktiert dasjenige unter dem er eingeloggt ist
     */
    public List<Konto> getAllAllowedKontosAndSelectActive(UserInfo ui, Connection cn) {
        List<Konto> allPossKontos = ui.getKonto().getLoginKontos(ui.getBenutzer(), cn);
        try {
            int y = 0;
            for (Konto uik : allPossKontos) {
                if (uik.getId().equals(ui.getKonto().getId())) { uik.setSelected(true); } // selektiert das aktive Konto
                allPossKontos.set(y, uik);
                y++;
            }
        } catch (Exception e) {
            LOG.error("etAllAllowedKontosAndSelectActive(UserInfo ui, Connection cn): " + e.toString());
        }

        return allPossKontos;
    }

    /**
     * Sucht Kontos welche in expiredays ablaufen werden
     * <p></p>
     * @param the expiredays
     * @return a {@link Konto}
     */
    public ArrayList<Konto> getExpireKontos(int expiredays) {
        DBConn cn = new DBConn();
        ArrayList<Konto> kl = new ArrayList<Konto>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.getConnection().prepareStatement("SELECT * FROM konto WHERE expiredate < ?");
            pstmt.setInt(1, expiredays);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kl.add(new Konto(rs));
            }

        } catch (Exception e) {
            LOG.error("getExpireKontos(int expiredays): " + e.toString());
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
            cn.close();
        }

        return kl;
    }

    /**
     * Sucht alle Faxserver-Konten heraus
     * <p></p>

     * @return a {@link Konto}
     */
    public List<Konto> getFaxserverKontos() {
        DBConn cn = new DBConn();
        ArrayList<Konto> kl = new ArrayList<Konto>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.getConnection().prepareStatement("SELECT * FROM konto WHERE `faxno` is not null");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kl.add(new Konto(rs));
            }

        } catch (Exception e) {
            LOG.error("getFaxServerKontos(): " + e.toString());
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
            cn.close();
        }

        return kl;
    }

    /**
     * Listet alle Kontos auf, bei welchen der Benutzer hinterlegt ist
     *
     * @param AbstractBenutzer
     * @return ArrayList<Konto> kl
     */
    public List<Konto> getKontosForBenutzer(AbstractBenutzer u, Connection cn) {
        ArrayList<Konto> kl = new ArrayList<Konto>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement(
            "SELECT * FROM `konto` AS k INNER JOIN (`v_konto_benutzer` AS vkb ) ON (k.KID=vkb.KID) WHERE vkb.UID = ?");
            pstmt.setLong(1, u.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kl.add(new Konto(rs));
            }

        } catch (Exception e) {
            LOG.error("getKontosForBenutzer(): " + e.toString());
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
        return kl;
    }

    /**
     * Listet alle Kontos auf, bei welchem ein Benutzer sich anmelden kann
     *
     * @param AbstractBenutzer
     * @return
     */
    public List<Konto> getLoginKontos(AbstractBenutzer u, Connection cn) {
        ArrayList<Konto> kl = new ArrayList<Konto>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = null;
            // Nur an alle Kontos anmelden, wenn auch Loginoption == true
            if (u.isLoginopt() || u.getClass().isInstance(new Bibliothekar())
                    || u.getClass().isInstance(new Administrator())) {
                pstmt = cn.prepareStatement("SELECT * FROM `konto` AS k INNER JOIN (`v_konto_benutzer` AS vkb ) "
                        + "ON (k.KID=vkb.KID) WHERE vkb.UID = ?");
            } else { // Sonst nur Kontos, welche Benutzerlogin zulassen
                pstmt = cn.prepareStatement("SELECT * FROM `konto` AS k INNER JOIN (`v_konto_benutzer` AS vkb ) "
                        + "ON (k.KID=vkb.KID) WHERE vkb.UID = ? AND k.userlogin = 1");
            }
            pstmt.setLong(1, u.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kl.add(new Konto(rs));
            }

        } catch (Exception e) {
            LOG.error("getLoginKontos(): " + e.toString());
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
        return kl;
    }

    /*
     * Setzt die Werte im Preparestatement der Methoden updateKonto() sowie saveNewKonto()
     */
    private PreparedStatement setKontoValues(PreparedStatement pstmt, Konto k) throws Exception {

        if (k.getBibliotheksname() != null) {
            pstmt.setString(1, k.getBibliotheksname());
        } else {
            pstmt.setString(1, "");
        }
        pstmt.setString(2, k.getIsil());
        if (k.getAdresse() != null) {
            pstmt.setString(3, k.getAdresse());
        } else {
            pstmt.setString(3, "");
        }
        if (k.getAdressenzusatz() != null) {
            pstmt.setString(4, k.getAdressenzusatz());
        } else {
            pstmt.setString(4, "");
        }
        if (k.getPLZ() != null) {
            pstmt.setString(5, k.getPLZ());
        } else {
            pstmt.setString(5, "");
        }
        if (k.getOrt() != null) {
            pstmt.setString(6, k.getOrt());
        } else {
            pstmt.setString(6, "");
        }
        if (k.getLand() != null) {
            pstmt.setString(7, k.getLand());
        } else {
            pstmt.setString(7, "");
        }
        if (k.getTimezone() != null) {
            pstmt.setString(8, k.getTimezone());
        } else {
            pstmt.setString(8, ReadSystemConfigurations.getSystemTimezone());
        }
        if (k.getTelefon() != null) {
            pstmt.setString(9, k.getTelefon());
        } else {
            pstmt.setString(9, "");
        }
        if (k.getFaxno() != null) {
            pstmt.setString(10, k.getFaxno());
        } else {
            pstmt.setString(10, null);
        } // falls nicht vorhanden => Null belassen, wegen Anzeige in jsp!
        if (k.getFaxusername() != null) {
            pstmt.setString(11, k.getFaxusername());
        } else {
            pstmt.setString(11, "");
        }
        if (k.getFaxpassword() != null) {
            pstmt.setString(12, k.getFaxpassword());
        } else {
            pstmt.setString(12, "");
        }
        if (k.getPopfaxend() != null) {
            pstmt.setString(13, k.getPopfaxend());
        } else {
            pstmt.setString(13, null);
        }
        if (k.getFax_extern() != null && !k.getFax_extern().equals("")) {
            pstmt.setString(14, k.getFax_extern());
        } else {
            pstmt.setString(14, null);
        }
        if (k.getBibliotheksmail() != null) {
            pstmt.setString(15, k.getBibliotheksmail());
        } else {
            pstmt.setString(15, "");
        }
        if (k.getDbsmail() != null) {
            pstmt.setString(16, k.getDbsmail());
        } else {
            pstmt.setString(16, "");
        }
        if (k.getDbsmailpw() != null) {
            pstmt.setString(17, k.getDbsmailpw());
        } else {
            pstmt.setString(17, "");
        }
        if (k.getGbvbenutzername() == null || k.getGbvbenutzername().equals("")) {
            pstmt.setString(18, null);
        } else {
            pstmt.setString(18, k.getGbvbenutzername());
        }
        if (k.getGbvpasswort() == null || k.getGbvpasswort().equals("")) {
            pstmt.setString(19, null);
        } else {
            pstmt.setString(19, k.getGbvpasswort());
        }
        if (k.getGbvrequesterid() == null || k.getGbvrequesterid().equals("")) {
            pstmt.setString(20, null);
        } else {
            pstmt.setString(20, k.getGbvrequesterid());
        }
        if (k.getEzbid() != null) {
            pstmt.setString(21, k.getEzbid());
        } else {
            pstmt.setString(21, "");
        }
        if (k.getInstlogolink() == null || k.getInstlogolink().equals("")) {
            pstmt.setString(22, null);
        } else {
            pstmt.setString(22, k.getInstlogolink());
        } // set null if empty
        if (!k.isZdb()) {
            pstmt.setString(23, "0");
        } else {
            pstmt.setString(23, "1");
        }
        if (k.getBilling() != null) {
            pstmt.setString(24, k.getBilling().toString());
        } else {
            pstmt.setString(24, "");
        }
        if (k.getBillingtype() != null) {
            pstmt.setString(25, k.getBillingtype().toString());
        } else {
            pstmt.setString(25, "");
        }
        if (k.getAccounting_rhythmvalue() == 0) {
            pstmt.setString(26, "0");
        } else {
            pstmt.setString(26, "1");
        }
        if (k.getAccounting_rhythmday() == 0) {
            pstmt.setString(27, "0");
        } else {
            pstmt.setString(27, "1");
        }
        if (k.getAccounting_rhythmtimeout() == 0) {
            pstmt.setString(28, "0");
        } else {
            pstmt.setString(28, "1");
        }
        if (k.getThreshold_value() == 0) {
            pstmt.setString(29, "0");
        } else {
            pstmt.setString(29, "1");
        }
        pstmt.setString(30, Integer.valueOf(k.getMaxordersu()).toString());
        pstmt.setString(31, Integer.valueOf(k.getMaxordersutotal()).toString());
        pstmt.setString(32, Integer.valueOf(k.getMaxordersj()).toString());
        pstmt.setString(33, Integer.valueOf(k.getOrderlimits()).toString());
        if (!k.isUserlogin()) {
            pstmt.setString(34, "0");
        } else {
            pstmt.setString(34, "1");
        }
        if (!k.isUserbestellung()) {
            pstmt.setString(35, "0");
        } else {
            pstmt.setString(35, "1");
        }
        if (!k.isGbvbestellung()) {
            pstmt.setString(36, "0");
        } else {
            pstmt.setString(36, "1");
        }
        if (!k.isKontostatus()) {
            pstmt.setString(37, "0");
        } else {
            pstmt.setString(37, "1");
        }
        pstmt.setString(38, Integer.valueOf(k.getKontotyp()).toString());
        pstmt.setString(39, k.getDefault_deloptions());
        if (k.getPaydate() != null) {
            pstmt.setDate(40, k.getPaydate());
        } else {
            pstmt.setDate(40, null);
        }
        if (k.getExpdate() != null) {
            pstmt.setDate(41, k.getExpdate());
        } else {
            pstmt.setDate(41, null);
        }
        return pstmt;
    }

    public java.sql.Date getExpdate() {
        return expdate;
    }

    public void setExpdate(java.sql.Date expdate) {
        this.expdate = expdate;
    }

    /**
     * Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll. (in Tagen)
     * @return Returns the accounting_rhythmday.
     */
    public int getAccounting_rhythmday() {
        return accounting_rhythmday;
    }


    /**
     * Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll. (in Tagen)
     * @param accounting_rhythmday The accounting_rhythmday to set.
     */
    public void setAccounting_rhythmday(int accounting_rhythmday) {
        this.accounting_rhythmday = accounting_rhythmday;
    }

    /**
     * Wird die Schwelle im Accounting_rhytm nicht erreicht, wird nach .... trotzdem
     * eine Rechnung gestellt. Feld leer oder 0 = kein Schwellwert
     * @return Returns the accounting_rhythmtimeout.
     */
    public int getAccounting_rhythmtimeout() {
        return accounting_rhythmtimeout;
    }


    /**
     * Wird die Schwelle im Accounting_rhytm nicht erreicht, wird nach ....
     * trotzdem eine Rechnung gestellt. Feld leer oder 0 = kein Schwellwert
     * @param accounting_rhythmtimeout The accounting_rhythmtimeout to set.
     */
    public void setAccounting_rhythmtimeout(int accounting_rhythmtimeout) {
        this.accounting_rhythmtimeout = accounting_rhythmtimeout;
    }


    public boolean isZdb() {
        return zdb;
    }

    public void setZdb(boolean zdb) {
        this.zdb = zdb;
    }

    /**
     * Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll in Fr.-
     * @return Returns the accounting_rhythmvalue.
     */
    public int getAccounting_rhythmvalue() {
        return accounting_rhythmvalue;
    }



    /**
     * Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll in Fr.-
     * @param accounting_rhythmvalue The accounting_rhythmvalue to set.
     */
    public void setAccounting_rhythmvalue(int accounting_rhythmvalue) {
        this.accounting_rhythmvalue = accounting_rhythmvalue;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getAdressenzusatz() {
        return Adressenzusatz;
    }

    public void setAdressenzusatz(String adressenzusatz) {
        Adressenzusatz = adressenzusatz;
    }

    public String getBibliotheksmail() {
        return Bibliotheksmail;
    }

    public void setBibliotheksmail(String bibliotheksmail) {
        Bibliotheksmail = bibliotheksmail;
    }

    public String getBibliotheksname() {
        return bibliotheksname;
    }

    public void setBibliotheksname(String bibname) {
        bibliotheksname = bibname;
    }

    /**
     * Globale Einstellung, von wem die Rechnungen beglichen werden soll.
     * Diese Einstellung kann durch den Wert welcher beim User hinterlegt ist berschrieben werden.
     * @return Returns the billing.
     */
    public Text getBilling() {
        return billing;
    }

    /**
     * Globale Einstellung wie die Rechnung an den Kunden geschickt werden soll.
     * Verweis auf die Tabelle Text mit dem Texttyp Billingtype
     * @param billing The billing to set.
     */
    public void setBilling(Text billing) {
        this.billing = billing;
    }

    /**
     * Globale Einstellung wie die Rechnung an den Kunden geschickt werden soll.
     * Verweis auf die Tabelle Text mit dem Texttyp Billingtype
     * @return Returns the billingtype.
     */
    public Text getBillingtype() {
        return billingtype;
    }

    public void setBillingtype(Text billingtype) {
        this.billingtype = billingtype;
    }

    /**
     * This is the email that receives ILL deliveries
     * @return Returns the dbsmail.
     */
    public String getDbsmail() {
        return dbsmail;
    }

    /**
     * This is the email that receives ILL deliveries
     * @param dbsmail The dbsmail to set.
     */
    public void setDbsmail(String dbsmail) {
        this.dbsmail = dbsmail;
    }

    public String getDbsmailpw() {
        return dbsmailpw;
    }

    public void setDbsmailpw(String dbsmailpw) {
        this.dbsmailpw = dbsmailpw;
    }

    public Date getEdatum() {
        return edatum;
    }

    public void setEdatum(Date edatum) {
        this.edatum = edatum;
    }

    public boolean isKontostatus() {
        return kontostatus;
    }


    public void setKontostatus(boolean kontostatus) {
        this.kontostatus = kontostatus;
    }

    public String getDefault_deloptions() {
        return default_deloptions;
    }

    public void setDefault_deloptions(String default_deloptions) {
        this.default_deloptions = default_deloptions;
    }

    public String getGtc() {
        return gtc;
    }

    public void setGtc(String gtc) {
        this.gtc = gtc;
    }

    public String getGtcdate() {
        return gtcdate;
    }

    public void setGtcdate(String gtcdate) {
        this.gtcdate = gtcdate;
    }

    public String getLand() {
        return Land;
    }

    public void setLand(String land) {
        Land = land;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Legt die maximale Artikelanzahl eines Kontos pro Jahr fest
     * @return Returns the maxordersj.
     */
    public int getMaxordersj() {
        return maxordersj;
    }

    /**
     * Legt die maximale Artikelanzahl eines Kontos pro Jahr fest
     * @param maxordersj The maxordersj to set.
     */
    public void setMaxordersj(int maxordersj) {
        this.maxordersj = maxordersj;
    }

    /**
     * Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
     * @return Returns the maxordersu.
     */
    public int getMaxordersu() {
        return maxordersu;
    }

    /**
     * Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
     * @param maxordersu The maxordersu to set.
     */
    public void setMaxordersu(int maxordersu) {
        this.maxordersu = maxordersu;
    }

    /**
     * Begrenzung mglicher Bestellungen durch einen Benutzer pro Jahr
     * @return Returns the maxordersutotal.
     */
    public int getMaxordersutotal() {
        return maxordersutotal;
    }

    /**
     * Begrenzung mglicher Bestellungen durch einen Benutzer pro Jahr
     * @param maxordersutotal The maxordersutotal to set.
     */
    public void setMaxordersutotal(int maxordersutotal) {
        this.maxordersutotal = maxordersutotal;
    }

    public int getOrderlimits() {
        return orderlimits;
    }

    public void setOrderlimits(int orderlimits) {
        this.orderlimits = orderlimits;
    }

    public String getOrt() {
        return Ort;
    }

    public void setOrt(String ort) {
        Ort = ort;
    }

    public String getPLZ() {
        return PLZ;
    }

    public void setPLZ(String plz) {
        PLZ = plz;
    }

    public String getGbvbenutzername() {
        return gbvbenutzername;
    }

    public void setGbvbenutzername(String gbvbenutzername) {
        this.gbvbenutzername = gbvbenutzername;
    }

    public String getGbvpasswort() {
        return gbvpasswort;
    }

    public void setGbvpasswort(String gbvpasswort) {
        this.gbvpasswort = gbvpasswort;
    }

    public String getGbvrequesterid() {
        return gbvrequesterid;
    }

    public void setGbvrequesterid(String gbvrequesterid) {
        this.gbvrequesterid = gbvrequesterid;
    }

    public String getEzbid() {
        return ezbid;
    }


    public void setEzbid(String ezbid) {
        this.ezbid = ezbid;
    }


    public String getInstlogolink() {
        return instlogolink;
    }

    public void setInstlogolink(String instlogolink) {
        this.instlogolink = instlogolink;
    }

    /**
     * Verrechnungsschwellwert Sammelrechnungen in Tagen
     * @return Returns the threshold_value.
     */
    public int getThreshold_value() {
        return threshold_value;
    }

    /**
     * Verrechnungsschwellwert Sammelrechnungen in Tagen
     * @param threshold_value The threshold_value to set.
     */
    public void setThreshold_value(int threshold_value) {
        this.threshold_value = threshold_value;
    }

    /**
     * @return Returns the userbestellung.
     */
    public boolean isUserbestellung() {
        return userbestellung;
    }

    /**
     * @param userbestellung The userbestellung to set.
     */
    public void setUserbestellung(boolean userbestellung) {
        this.userbestellung = userbestellung;
    }


    public boolean isGbvbestellung() {
        return gbvbestellung;
    }

    public void setGbvbestellung(boolean gbvbestellung) {
        this.gbvbestellung = gbvbestellung;
    }

    public boolean isUserlogin() {
        return userlogin;
    }

    public void setUserlogin(boolean userlogin) {
        this.userlogin = userlogin;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }


    public String getFaxno() {
        return faxno;
    }

    public void setFaxno(String faxno) {
        this.faxno = faxno;
    }

    public String getFaxpassword() {
        return faxpassword;
    }

    public void setFaxpassword(String faxpassword) {
        this.faxpassword = faxpassword;
    }

    public String getPopfaxend() {
        return popfaxend;
    }

    public void setPopfaxend(String popfaxend) {
        this.popfaxend = popfaxend;
    }

    public String getFaxusername() {
        return faxusername;
    }

    public void setFaxusername(String faxusername) {
        this.faxusername = faxusername;
    }

    public String getFax_extern() {
        return fax_extern;
    }

    public void setFax_extern(String fax_extern) {
        this.fax_extern = fax_extern;
    }

    public int getKontotyp() {
        return kontotyp;
    }

    public void setKontotyp(int kontotyp) {
        this.kontotyp = kontotyp;
    }

    /*
     * Zahlungseingang Betrag bei Doctor-Doc.com
     */
    public java.sql.Date getPaydate() {
        return paydate;
    }

    public void setPaydate(java.sql.Date paydate) {
        this.paydate = paydate;
    }

    public String getIsil() {
        return isil;
    }

    public void setIsil(String isil) {
        this.isil = isil;
    }


}
