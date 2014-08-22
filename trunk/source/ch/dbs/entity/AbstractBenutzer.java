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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ThreadSafeSimpleDateFormat;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserForm;
import ch.dbs.form.UserInfo;
import enums.TextType;

/**
 * Abstract base class for entities having a {@link Long} unique identifier,
 * this provides the base functionality for them. <p></p>
 * 
 * @author Pascal Steiner
 */

public class AbstractBenutzer extends AbstractIdEntity {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractBenutzer.class);

    private Long billing;
    private String institut;
    private String abteilung;
    private Text category;
    private String anrede = "";
    private String vorname;
    private String name;
    private String adresse;
    private String adresszusatz;
    private String telefonnrp;
    private String telefonnrg;
    private String plz;
    private String ort;
    private String land;
    private String email;
    private String password;
    private String librarycard = "";
    private boolean validation;
    private boolean loginopt; // Darf sich der Benutzer einloggen?
    private boolean userbestellung; // darf bei SUBITO bestellen
    private boolean gbvbestellung; // darf bei GBV bestellen
    private boolean kontostatus;
    private boolean kontovalidation;
    private String datum;
    private Date lastuse;
    private int rechte;
    private String gtc;
    private String gtcdate;

    public AbstractBenutzer() {
    }

    public AbstractBenutzer(final UserForm uf, final Connection cn) {

        if (uf.getInstitut() != null) {
            institut = uf.getInstitut().trim();
        } else {
            institut = uf.getInstitut();
        }
        if (uf.getAbteilung() != null) {
            abteilung = uf.getAbteilung().trim();
        } else {
            abteilung = uf.getAbteilung();
        }
        category = new Text(cn, Long.valueOf(uf.getCategory()), TextType.USER_CATEGORY);
        anrede = uf.getAnrede();
        if (uf.getVorname() != null) {
            vorname = uf.getVorname().trim();
        } else {
            vorname = uf.getVorname();
        }
        if (uf.getName() != null) {
            name = uf.getName().trim();
        } else {
            name = uf.getName();
        }
        if (uf.getAdresse() != null) {
            adresse = uf.getAdresse().trim();
        } else {
            adresse = uf.getAdresse();
        }
        if (uf.getAdresszusatz() != null) {
            adresszusatz = uf.getAdresszusatz().trim();
        } else {
            adresszusatz = uf.getAdresszusatz();
        }
        if (uf.getTelefonnrp() != null) {
            telefonnrp = uf.getTelefonnrp().trim();
        } else {
            telefonnrp = uf.getTelefonnrp();
        }
        if (uf.getTelefonnrg() != null) {
            telefonnrg = uf.getTelefonnrg().trim();
        } else {
            telefonnrg = uf.getTelefonnrg();
        }
        if (uf.getPlz() != null) {
            plz = uf.getPlz().trim();
        } else {
            plz = uf.getPlz();
        }
        if (uf.getOrt() != null) {
            ort = uf.getOrt().trim();
        } else {
            ort = uf.getOrt();
        }
        land = uf.getLand();
        if (uf.getEmail() != null) {
            email = uf.getEmail().trim();
        } else {
            email = uf.getEmail();
        }
        password = uf.getPassword();
        validation = uf.isValidation();
        kontostatus = uf.isKontostatus();
        loginopt = uf.isLoginopt(); // Darf sich der Benutzer einloggen?
        userbestellung = uf.isUserbestellung();
        gbvbestellung = uf.isGbvbestellung();
        kontovalidation = uf.isKontovalidation();
        gtc = uf.getGtc();
        gtcdate = uf.getGtcdate();

    }

    public AbstractBenutzer(final OrderForm of) {
        if (of.getKundenvorname() != null) {
            this.vorname = of.getKundenvorname().trim();
        } else {
            this.vorname = of.getKundenvorname();
        }
        if (of.getKundenname() != null) {
            this.name = of.getKundenname().trim();
        } else {
            this.name = of.getKundenname();
        }
        if (of.getKundenmail() != null) {
            this.email = of.getKundenmail().trim();
        } else {
            this.email = of.getKundenmail();
        }
        if (of.getKundeninstitution() != null) {
            this.institut = of.getKundeninstitution().trim();
        } else {
            this.institut = of.getKundeninstitution();
        }
        if (of.getKundenabteilung() != null) {
            this.abteilung = of.getKundenabteilung().trim();
        } else {
            this.abteilung = of.getKundenabteilung();
        }
        final Text t = new Text();
        t.setId(Long.valueOf(of.getKundenkategorieID()));
        this.category = t;
        if (of.getKundenadresse() != null) { // this may contain street, zip and place!
            this.adresszusatz = of.getKundenadresse().trim();
        } else {
            this.adresszusatz = of.getKundenadresse();
        }
        if (of.getKundenstrasse() != null) {
            this.adresse = of.getKundenstrasse().trim();
        } else {
            this.adresse = of.getKundenstrasse();
        }
        if (of.getKundenplz() != null) {
            this.plz = of.getKundenplz().trim();
        } else {
            this.plz = of.getKundenplz();
        }
        if (of.getKundenort() != null) {
            this.ort = of.getKundenort().trim();
        } else {
            this.ort = of.getKundenort();
        }
        if (of.getKundenland() != null) {
            this.land = of.getKundenland().trim();
        } else {
            this.land = of.getKundenland();
        }
        if (of.getKundentelefon() != null) {
            this.telefonnrg = of.getKundentelefon().trim();
        } else {
            this.telefonnrg = of.getKundentelefon();
        }
        if (of.getKundenbenutzernr() != null) {
            this.librarycard = of.getKundenbenutzernr().trim();
        } else {
            this.librarycard = of.getKundenbenutzernr();
        }
        this.setKontostatus(true);
    }

    /**
     * Sucht anhand einer Userid UID einen User heraus <p></p>
     * 
     * @param uid
     * @return User (Benutzer, Bibliothekar oder Administrator)
     */
    public AbstractBenutzer getUser(final Long uid, final Connection cn) {

        AbstractBenutzer u = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM benutzer WHERE uid=?");
            pstmt.setLong(1, uid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                u = getUser(rs, cn);
            }

        } catch (final Exception e) {
            LOG.error("getUser(Long uid, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getUser(Long uid, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getUser(Long uid, Connection cn): " + e.toString());
                }
            }
        }

        return u;
    }

    /**
     * Listet alle User eines Kontos auf
     * 
     * @param Konto k
     * @return List<AbstractBenutzer> ul
     */
    public List<AbstractBenutzer> getKontoUser(final Konto k, final Connection cn) {
        final ArrayList<AbstractBenutzer> ul = new ArrayList<AbstractBenutzer>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT b.* FROM `benutzer` AS b INNER JOIN (`v_konto_benutzer` AS vkb ) "
                    + "ON (b.UID=vkb.UID) WHERE vkb.KID = ? order by name, vorname");
            pstmt.setLong(1, k.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ul.add(getUser(rs, cn));
            }

        } catch (final Exception e) {
            LOG.error("getKontoUser(Konto k, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getKontoUser(Konto k, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getKontoUser(Konto k, Connection cn): " + e.toString());
                }
            }
        }

        return ul;
    }

    /**
     * Sucht anhand einer Email die betreffenden User eines Kontos heraus
     * <p></p>
     * 
     * @param Konto k
     * @param String email
     * @return List (Benutzer, Bibliothekar oder Administrator)
     */
    public List<AbstractBenutzer> getUserListFromEmailAndKonto(final Konto k, final String mail, final Connection cn) {

        final List<AbstractBenutzer> benutzerlist = new ArrayList<AbstractBenutzer>();
        AbstractBenutzer u = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT b.* FROM `benutzer` AS b INNER JOIN (`v_konto_benutzer` AS vkb ) "
                    + "ON (b.UID=vkb.UID) WHERE vkb.KID = ? AND b.mail=?");
            pstmt.setLong(1, k.getId());
            pstmt.setString(2, mail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                u = getUser(rs, cn);
                benutzerlist.add(u);
            }

        } catch (final Exception e) {
            LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
        }

        return benutzerlist;
    }

    /**
     * Sucht anhand einer Email alle User heraus und liefert einen davon zurück
     * <p></p>
     * 
     * @param mail
     * @return AbstractUser (Benutzer, Bibliothekar oder Administrator)
     */
    public AbstractBenutzer getUserFromEmail(final String mail, final Connection cn) {

        AbstractBenutzer u = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM benutzer WHERE mail=?");
            pstmt.setString(1, mail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                u = getUser(rs, cn); // gibt nur den letzten Treffer zurück
            }

        } catch (final Exception e) {
            LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
        }

        return u;
    }

    /**
     * Returns for an email a User who is allowed to login.
     * <p></p>
     * 
     * @param mail
     * @return AbstractUser (Benutzer, Bibliothekar oder Administrator)
     */
    public AbstractBenutzer getLoginUserFromEmail(final String mail, final Connection cn) {

        AbstractBenutzer u = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM benutzer WHERE mail=? AND loginopt = 1");
            pstmt.setString(1, mail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                u = getUser(rs, cn); // gibt nur den letzten Treffer zurück
            }

        } catch (final Exception e) {
            LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
        }

        return u;
    }

    /**
     * Sucht anhand einer Email alle User heraus <p></p>
     * 
     * @param mail
     * @return AbstractUser (Benutzer, Bibliothekar oder Administrator)
     */
    public List<AbstractBenutzer> getAllUserFromEmail(final String mail, final Connection cn) {

        final List<AbstractBenutzer> benutzerlist = new ArrayList<AbstractBenutzer>();
        AbstractBenutzer u = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM benutzer WHERE mail=?");
            pstmt.setString(1, mail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                u = getUser(rs, cn);
                benutzerlist.add(u);
            }

        } catch (final Exception e) {
            LOG.error("getAllUserFromEmail(String email, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getAllUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getAllUserFromEmail(String email, Connection cn): " + e.toString());
                }
            }
        }

        return benutzerlist;
    }

    /**
     * @return Alle Kontos bei welchen der {@link AbstractBenutzer} hinterlegt
     * ist in einer {@link ArrayList}
     */
    public List<Konto> getKontosDeposited(final AbstractBenutzer u, final Connection cn) {
        final List<Konto> kontos = new ArrayList<Konto>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `benutzer` AS b "
                    + "INNER JOIN (`v_konto_benutzer` AS vkb ) ON (b.UID=vkb.UID) "
                    + "INNER JOIN (`konto` AS k ) ON (k.KID=vkb.KID)WHERE vkb.UID = ?");
            pstmt.setLong(1, u.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kontos.add(new Konto(rs));
            }

        } catch (final Exception e) {
            LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
                }
            }
        }

        return kontos;
    }

    /**
     * @return Alle Kontos bei welchen der {@link AbstractBenutzer} hinterlegt
     * und bei welchen er sich auch einloggen darf in einer {@link ArrayList}
     */
    public List<Konto> getKontosAlowedLogin(final AbstractBenutzer u, final Connection cn) {
        final List<Konto> kontos = new ArrayList<Konto>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `benutzer` AS b "
                    + "INNER JOIN v_konto_benutzer AS vkb USING ( UID ) " + "INNER JOIN konto AS k USING ( KID ) "
                    + "WHERE b.UID = ? " + "AND b.kontostatus = 1 " + "AND k.kontostatus = 1 "
                    + "AND ((`loginopt` = 1 AND `userlogin` = 1 ) OR `rechte` > 1)");
            pstmt.setLong(1, u.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                kontos.add(new Konto(rs));
            }

        } catch (final Exception e) {
            LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("getKontosDeposited(AbstractBenutzer u, Connection cn): " + e.toString());
                }
            }
        }
        return kontos;
    }

    /**
     * Login
     */

    public List<UserInfo> login(final String mail, final String pw, final Connection cn) {

        UserInfo u;
        final List<UserInfo> userinfolist = new ArrayList<UserInfo>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `benutzer` AS b "
                    + "INNER JOIN v_konto_benutzer AS vkb USING(UID)" + "INNER JOIN konto AS k USING(KID) "
                    + "WHERE b.mail = ? AND b.pw = ? " + "AND k.kontostatus = 1 " + "AND b.kontostatus = 1 "
                    + "GROUP BY b.UID");

            pstmt.setString(1, mail);
            pstmt.setString(2, pw);
            rs = pstmt.executeQuery();
            List<Konto> kontolist;

            final Administrator admin = new Administrator();
            final Konto k = new Konto();
            while (rs.next()) {
                // Wenn Userlogin in Konto erlaubt ist, UserInfo erstellen
                if (rs.getBoolean("userlogin") && rs.getBoolean("loginopt") || rs.getInt("rechte") >= 2) {
                    u = new UserInfo();
                    final AbstractBenutzer benutzer = getUser(rs, cn);
                    if (benutzer.getClass().isInstance(admin)) {
                        kontolist = k.getAllKontos(cn);
                    } else {
                        kontolist = getKontosAlowedLogin(benutzer, cn);
                    }
                    u.setBenutzer(benutzer);
                    u.setKontos(kontolist);
                    //                   Bei nur einer Kontozugehrigkeit, diese gleich setzen
                    if (kontolist.size() == 1) {
                        u.setKonto(kontolist.get(0));
                    }
                    userinfolist.add(u);
                }

            }

        } catch (final Exception e) {
            LOG.error("login(String email, String pw, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("login(String email, String pw, Connection cn): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("login(String email, String pw, Connection cn): " + e.toString());
                }
            }
        }

        return userinfolist;
    }

    /**
     * Füllt ein Userobjekt mit einer Zeile aus der Datenbank
     */
    public AbstractBenutzer getUser(final ResultSet rs, final Connection cn) {

        AbstractBenutzer u = new AbstractBenutzer();

        try {
            if (rs.getString("rechte").equals("3")) {
                u = new Administrator();
            }
            if (rs.getString("rechte").equals("2")) {
                u = new Bibliothekar();
            }
            if (rs.getString("rechte").equals("1")) {
                u = new Benutzer();
            }

            u.setId(rs.getLong("UID"));
            u.setInstitut(rs.getString("institut"));
            u.setAbteilung(rs.getString("abteilung"));
            u.setCategory(new Text(cn, rs.getLong("category"), TextType.USER_CATEGORY));
            u.setAnrede(rs.getString("anrede"));
            u.setVorname(rs.getString("vorname"));
            u.setName(rs.getString("name"));
            u.setAdresse(rs.getString("adr"));
            u.setAdresszusatz(rs.getString("adrzus"));
            u.setValidation(rs.getBoolean("kontoval"));
            u.setKontostatus(rs.getBoolean("kontostatus"));
            u.setTelefonnrp(rs.getString("telp"));
            u.setTelefonnrg(rs.getString("telg"));
            u.setPlz(rs.getString("plz"));
            u.setOrt(rs.getString("ort"));
            u.setLand(rs.getString("land"));
            u.setEmail(rs.getString("mail"));
            u.setPassword(rs.getString("pw"));
            u.setLoginopt(rs.getBoolean("loginopt"));
            u.setUserbestellung(rs.getBoolean("userbestellung"));
            u.setGbvbestellung(rs.getBoolean("gbvbestellung"));
            u.setBilling(rs.getLong("billing"));
            u.setRechte(rs.getInt("rechte"));

            final Date d = rs.getTimestamp("datum");
            final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (d != null) {
                u.setDatum(fmt.format(d));
            } else {
                u.setDatum("0000-00-00 00:00:00");
            }

            u.setLastuse(rs.getTimestamp("lastuse"));
            u.setGtc(rs.getString("gtc"));
            u.setGtcdate(rs.getString("gtcdate"));

        } catch (final SQLException e) {
            LOG.error("getUser(ResultSet rs): " + e.toString());
        }

        return u;

    }

    /**
     * Speichert einen neuen Benutzer in der Datenbank
     * 
     * @param AbstractBenutzer u
     */
    public Long saveNewUser(final AbstractBenutzer u, final String timezone, final Connection cn) {

        Long uid = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setUserValues(cn.prepareStatement("INSERT INTO `benutzer` (`institut` , `abteilung` , "
                    + "`category` , `anrede` , `vorname` , `name` , `adr` , `adrzus` , `telp` , `telg` , `plz` , "
                    + "`ort` , `land` , `mail` , `pw` , `loginopt` , `userbestellung` , `gbvbestellung` , `billing` , "
                    + "`kontoval` , `kontostatus` , `rechte` , `gtc` , `gtcdate`, `lastuse` , `datum`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"), u,
                    timezone, cn);
            pstmt.setString(26, u.getDatum());
            pstmt.executeUpdate();

            //          ID des gerade gespeicherten Benutzers ermitteln und hinterlegen
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                uid = rs.getLong("LAST_INSERT_ID()");
            }

        } catch (final Exception e) {
            LOG.error("saveNewUser(): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error("saveNewUser(): " + e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("saveNewUser(): " + e.toString());
                }
            }
        }

        return uid;
    }

    /**
     * Verändert einen vorhandenen Benutzerin der DB
     * 
     * @param AbstractBenutzer u
     */
    public void updateUser(final AbstractBenutzer u, final String timezone, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = setUserValues(cn.prepareStatement("UPDATE `benutzer` SET `institut` = ?, "
                    + "`abteilung` = ?, `category` = ?, `anrede` = ?, `vorname` = ?, `name` = ?, `adr` = ?,"
                    + "`adrzus` = ?,`telp` = ?, `telg` = ?, `plz` = ?, `ort` = ?, `land` = ?, `mail` = ?, "
                    + "`pw` = ?,`loginopt` = ?, `userbestellung` = ?, `gbvbestellung` = ?, `billing` = ?, "
                    + "`kontoval` = ?, `kontostatus` = ?, `rechte` = ?, `gtc` = ?, `gtcdate` = ?, "
                    + "`lastuse` = ? WHERE `UID` =?"), u, timezone, cn);
            pstmt.setLong(26, u.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("updateUser(): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("updateUser(): " + e.toString());
                }
            }
        }
    }

    /**
     * Resets all categories to 0 for a given ID
     * 
     * @param Long id
     * @param Connection cn
     */
    public void resetCategories(final Long id, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `benutzer` SET `category` = '0' WHERE `category` =?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("resetCategories: " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("resetCategories: " + e.toString());
                }
            }
        }
    }

    /**
     * setzt das Lastuse-Datum bei einem AbstractBenutzer
     */
    public void updateLastuse(final AbstractBenutzer u, final String timezone, final Connection cn) {
        final Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(timezone));
        u.setLastuse(cal.getTime());
        u.updateUser(u, timezone, cn);
    }

    /**
     * Löscht einen vorhandenen Benutzerin aus der DB
     * 
     * @param AbstractBenutzer u
     */
    public boolean deleteUser(final AbstractBenutzer u, final Connection cn) {

        boolean success = false;

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `benutzer` WHERE `UID` =?");
            pstmt.setLong(1, u.getId());
            pstmt.executeUpdate();

            success = true;

        } catch (final Exception e) {
            LOG.error("deleteUser(): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("deleteUser(): " + e.toString());
                }
            }
        }

        return success;
    }

    /*
     * Setzt die Werte im Preparestatement der Methoden updateUser() sowie saveNewUser()
     * Funktionniert auch für Bibliothekare sowie Administratoren
     */
    public PreparedStatement setUserValues(final PreparedStatement pstmt, final AbstractBenutzer u,
            final String timezone, final Connection cn) throws Exception {
        String berechtigung = "";
        String userBestellung = "0";
        String gbvBestellung = "0";
        String loginOpt = "0";
        String kontoVal = "0";
        String kontoStatus = "0";

        if (u.isKontovalidation()) {
            kontoStatus = "1";
        }
        if (u.isLoginopt()) {
            loginOpt = "1";
        }
        if (u.isUserbestellung()) {
            userBestellung = "1";
        }
        if (u.isGbvbestellung()) {
            gbvBestellung = "1";
        }
        if (u.isKontostatus()) {
            kontoStatus = "1";
        }

        if (u.getClass().isInstance(new Benutzer())) {
            berechtigung = "1";
        }
        if (u.getClass().isInstance(new Bibliothekar())) {
            berechtigung = "2";
        }
        if (u.getClass().isInstance(new Administrator())) {
            berechtigung = "3";
        }
        if (u.getClass().isInstance(new AbstractBenutzer())) { // Als Sicherhheit, sollte aber vermieden weden
            berechtigung = "1";
        }

        if (u.isUserbestellung()) {
            userBestellung = "1";
        }
        if (u.isGbvbestellung()) {
            gbvBestellung = "1";
        }
        if (u.isLoginopt()) {
            loginOpt = "1";
        }
        if (u.isKontovalidation()) {
            kontoVal = "1";
        }

        if (u.getInstitut() != null) {
            pstmt.setString(1, u.getInstitut());
        } else {
            pstmt.setString(1, "");
        }
        if (u.getAbteilung() != null) {
            pstmt.setString(2, u.getAbteilung());
        } else {
            pstmt.setString(2, "");
        }
        if (u.getCategory() != null && u.getCategory().getId() != null) {
            pstmt.setLong(3, u.getCategory().getId());
        } else {
            pstmt.setLong(3, Long.valueOf("0"));
        }
        if (u.getAnrede() != null) {
            pstmt.setString(4, u.getAnrede());
        } else {
            pstmt.setString(4, "");
        }
        if (u.getVorname() != null) {
            pstmt.setString(5, u.getVorname());
        } else {
            pstmt.setString(5, "");
        }
        if (u.getName() != null) {
            pstmt.setString(6, u.getName());
        } else {
            pstmt.setString(6, "");
        }
        if (u.getAdresse() != null) {
            pstmt.setString(7, u.getAdresse());
        } else {
            pstmt.setString(7, "");
        }
        if (u.getAdresszusatz() != null) {
            pstmt.setString(8, u.getAdresszusatz());
        } else {
            pstmt.setString(8, "");
        }
        if (u.getTelefonnrp() != null) {
            pstmt.setString(9, u.getTelefonnrp());
        } else {
            pstmt.setString(9, "");
        }
        if (u.getTelefonnrg() != null) {
            pstmt.setString(10, u.getTelefonnrg());
        } else {
            pstmt.setString(10, "");
        }
        if (u.getPlz() != null) {
            pstmt.setString(11, u.getPlz());
        } else {
            pstmt.setString(11, "");
        }
        if (u.getOrt() != null) {
            pstmt.setString(12, u.getOrt());
        } else {
            pstmt.setString(12, "");
        }
        if (u.getLand() != null) {
            pstmt.setString(13, u.getLand());
        } else {
            pstmt.setString(13, "");
        }
        if (u.getEmail() != null) {
            pstmt.setString(14, u.getEmail());
        } else {
            pstmt.setString(14, "");
        }
        if (u.getPassword() != null) {
            // If the passsword is "": it shall not be changed / updated
            if (u.getPassword().equals("da39a3ee5e6b4bd3255bfef95601890afd879") && u.getId() != null) {
                AbstractBenutzer userpw = new AbstractBenutzer();
                userpw = userpw.getUser(u.getId(), cn);
                pstmt.setString(15, userpw.getPassword());
            } else {
                pstmt.setString(15, u.getPassword());
            }
        } else {
            pstmt.setString(15, "");
        }
        pstmt.setString(16, loginOpt);
        pstmt.setString(17, userBestellung);
        pstmt.setString(18, gbvBestellung);
        if (u.getBilling() != null) {
            pstmt.setLong(19, u.getBilling());
        } else {
            pstmt.setString(19, "0");
        }
        pstmt.setString(20, kontoVal);
        pstmt.setString(21, kontoStatus);
        pstmt.setString(22, berechtigung);
        if (u.getGtc() != null) {
            pstmt.setString(23, u.getGtc());
        } else {
            pstmt.setString(23, "");
        }
        if (u.getGtcdate() == null || u.getGtcdate().equals("")) {
            pstmt.setString(24, "0000-00-00 00:00:00");
        } else {
            pstmt.setString(24, u.getGtcdate());
        }
        final ThreadSafeSimpleDateFormat formater = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (u.getLastuse() == null) {
            pstmt.setString(25, "0000-00-00 00:00:00");
        } else {
            pstmt.setString(25, formater.format(u.getLastuse(), timezone));
        }

        return pstmt;
    }

    /**
     * @return Returns the abteilung.
     */
    public String getAbteilung() {
        return abteilung;
    }

    /**
     * @param abteilung The abteilung to set.
     */
    public void setAbteilung(final String abteilung) {
        this.abteilung = abteilung;
    }

    /**
     * @return Returns the adresse.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * @param adresse The adresse to set.
     */
    public void setAdresse(final String adresse) {
        this.adresse = adresse;
    }

    /**
     * @return Returns the adresszusatz.
     */
    public String getAdresszusatz() {
        return adresszusatz;
    }

    /**
     * @param adresszusatz The adresszusatz to set.
     */
    public void setAdresszusatz(final String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }

    /**
     * @return Returns the anrede.
     */
    public String getAnrede() {
        return anrede;
    }

    /**
     * @param anrede The anrede to set.
     */
    public void setAnrede(final String anrede) {
        this.anrede = anrede;
    }

    /**
     * @return Returns the billing.
     */
    public Long getBilling() {
        return billing;
    }

    /**
     * @param billing The billing to set.
     */
    public void setBilling(final Long billing) {
        this.billing = billing;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(final String datum) {
        this.datum = datum;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return Returns the institut.
     */
    public String getInstitut() {
        return institut;
    }

    /**
     * @param institut The institut to set.
     */
    public void setInstitut(final String institut) {
        this.institut = institut;
    }

    /**
     * @return Returns the kontovalidation.
     */
    public boolean isKontovalidation() {
        return kontovalidation;
    }

    /**
     * @param kontovalidation The kontovalidation to set.
     */
    public void setKontovalidation(final boolean kontovalidation) {
        this.kontovalidation = kontovalidation;
    }

    /**
     * @return Returns the land.
     */
    public String getLand() {
        return land;
    }

    /**
     * @param land The land to set.
     */
    public void setLand(final String land) {
        this.land = land;
    }

    /**
     * @return Returns the lastuse.
     */
    public Date getLastuse() {
        return lastuse;
    }

    /**
     * @param lastuse The lastuse to set.
     */
    public void setLastuse(final Date lastuse) {
        this.lastuse = lastuse;
    }

    /**
     * @return Returns the loginopt.
     */
    public boolean isLoginopt() {
        return loginopt;
    }

    /**
     * @param loginopt The loginopt to set.
     */
    public void setLoginopt(final boolean loginopt) {
        this.loginopt = loginopt;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the plz.
     */
    public String getPlz() {
        return plz;
    }

    /**
     * @param plz The plz to set.
     */
    public void setPlz(final String plz) {
        this.plz = plz;
    }

    /**
     * @return Returns the ort.
     */
    public String getOrt() {
        return ort;
    }

    /**
     * @param ort The ort to set.
     */
    public void setOrt(final String ort) {
        this.ort = ort;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Telefonnummer geschftlich
     * 
     * @return Returns the telefonnrg.
     */
    public String getTelefonnrg() {
        return telefonnrg;
    }

    /**
     * Telefonnummer geschftlich
     * 
     * @param telefonnrg The telefonnrg to set.
     */
    public void setTelefonnrg(final String telefonnrg) {
        this.telefonnrg = telefonnrg;
    }

    /**
     * Telefonnummer privat
     * 
     * @return Returns the telefonnrp.
     */
    public String getTelefonnrp() {
        return telefonnrp;
    }

    /**
     * Telefonnummer privat
     * 
     * @param telefonnrp The telefonnrp to set.
     */
    public void setTelefonnrp(final String telefonnrp) {
        this.telefonnrp = telefonnrp;
    }

    /**
     * Darf ein Benutzer bei SUBITO Bestellungen tätigen?
     * 
     * @return Returns the userbestellung.
     */
    public boolean isUserbestellung() {
        return userbestellung;
    }

    /**
     * Darf ein Benutzer bei SUBITO Bestellungen tätigen?
     * 
     * @param userbestellung The userbestellung to set.
     */
    public void setUserbestellung(final boolean userbestellung) {
        this.userbestellung = userbestellung;
    }

    /**
     * Darf ein Benutzer beim GBV Bestellungen tätigen?
     * 
     * @param gbvbestellung The gbvbestellung to set.
     */
    public boolean isGbvbestellung() {
        return gbvbestellung;
    }

    /**
     * Darf ein Benutzer beim GBV Bestellungen tätigen?
     * 
     * @param gbvbestellung The gbvbestellung to set.
     */
    public void setGbvbestellung(final boolean gbvbestellung) {
        this.gbvbestellung = gbvbestellung;
    }

    /**
     * Stimmt die Mailadresse?
     * 
     * @return Returns the validation.
     */
    public boolean isValidation() {
        return validation;
    }

    /**
     * Stimmt die Mailadresse?
     * 
     * @param validation The validation to set.
     */
    public void setValidation(final boolean validation) {
        this.validation = validation;
    }

    /**
     * @return Returns the vorname.
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * @param vorname The vorname to set.
     */
    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    public boolean isKontostatus() {
        return kontostatus;
    }

    public void setKontostatus(final boolean kontostatus) {
        this.kontostatus = kontostatus;
    }

    public int getRechte() {
        return rechte;
    }

    public void setRechte(final int rechte) {
        this.rechte = rechte;
    }

    public String getGtc() {
        return gtc;
    }

    public void setGtc(final String gtc) {
        this.gtc = gtc;
    }

    public String getGtcdate() {
        return gtcdate;
    }

    public void setGtcdate(final String gtcdate) {
        this.gtcdate = gtcdate;
    }

    public String getLibrarycard() {
        return librarycard;
    }

    public void setLibrarycard(String librarycard) {
        if (librarycard.length() > 50) {
            librarycard = librarycard.substring(0, 49);
        }
        this.librarycard = librarycard;
    }

    public Text getCategory() {
        return category;
    }

    public void setCategory(final Text category) {
        this.category = category;
    }

}
