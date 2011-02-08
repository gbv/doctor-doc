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

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Pascal Steiner
 */
public class VKontoBenutzer extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(VKontoBenutzer.class);

    private Long KID;
    private Long BID;
    private Konto konto;
    private AbstractBenutzer u;


    /**
     * Löscht die Kontoverknüpfung eines Benutzers zu einem bestimmten Konto
     * @param AbstractBenutzer u
     * @param Konto k
     * @return boolean success
     *
     */
    public boolean deleteSingleKontoEntry(final AbstractBenutzer user, final Konto k, final Connection cn) {

        boolean success = false;

        if (user != null && k != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = cn.prepareStatement("DELETE FROM `v_konto_benutzer` WHERE `UID` =? and `KID` =?");
                pstmt.setLong(1, user.getId());
                pstmt.setLong(2, k.getId());
                pstmt.executeUpdate();
                success = true;

            } catch (final Exception e) {
                LOG.error("deleteSingleKontoEntry(AbstractBenutzer u, Konto k, Connection cn): " + e.toString());
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

        return success;
    }

    /**
     * Löscht alle Kontoverknüpfungen eines Benutzers
     *
     * @param AbstractBenutzer
     * @param Connection cn
     */
    public void deleteAllKontoEntries(final AbstractBenutzer user, final Connection cn) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `v_konto_benutzer` WHERE `UID` =?");
            pstmt.setLong(1, user.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("deleteAllKontoEntries(AbstractBenutzer u, Connection cn): " + e.toString());
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
     * Erstell eine Konto-Benutzer Verknüpfung
     *
     */
    public boolean save(final AbstractBenutzer user, final Konto k, final Connection cn) {

        boolean success = false;

        if (user != null && k != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = cn.prepareStatement("INSERT INTO `v_konto_benutzer` (`UID` , "
                        + "`KID`) VALUES (?, ?)");
                pstmt.setLong(1, user.getId());
                pstmt.setLong(2, k.getId());
                pstmt.executeUpdate();
                success = true;

            } catch (final Exception e) {
                LOG.error("save(): " + e.toString());
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

        return success;
    }

    /**
     * Prüft anhand einer User-ID und einer Konto-ID, ob ein Eintrag in der Verknüpfungstabelle besteht
     * <p></p>
     * @param Long uid
     * @param Long kid
     * @return boolean check
     */
    public boolean isUserFromKonto(final Long kid, final Long uid, final Connection cn) {
        boolean check = false;
        int anzahl = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement(
            "SELECT count(vkbid) FROM `v_konto_benutzer` WHERE `KID` = ? AND `UID` = ?");
            pstmt.setString(1, kid.toString());
            pstmt.setString(2, uid.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                anzahl = rs.getInt("count(vkbid)");
            }

            if (anzahl > 0) { check = true; }

        } catch (final Exception e) {
            LOG.error("isUserFromKonto(): " + e.toString());
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

        return check;
    }

    /**
     * Verknüpft einen Benutzer mit einem Konto
     *
     * @param AbstractBenutzer b
     * @param Konto k
     */
    public void setKontoUser(AbstractBenutzer b, final Konto k, final Connection cn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = b.setUserValues(cn.prepareStatement("SELECT * FROM `benutzer` WHERE `institut` = ? AND "
                    + "`abteilung` = ? AND `category` = ? AND `anrede` = ? AND `vorname` = ? AND `name` = ? "
                    + "AND `adr` = ? AND `adrzus` = ? AND `telp` = ? AND `telg` = ? AND `plz` = ? AND `ort` = ? "
                    + "AND `land` = ? AND `mail` = ? AND `pw` = ? AND `loginopt` = ? AND `userbestellung` = ? "
                    + "AND `gbvbestellung` = ? AND `billing` = ? AND `kontoval` = ? AND `kontostatus` = ? "
                    + "AND `rechte` = ? AND `gtc` = ? AND `gtcdate` = ? AND `lastuse` = ?"), b, k, cn);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                b = b.getUser(rs, cn);
            }

            pstmt = cn.prepareStatement("INSERT INTO `v_konto_benutzer` (`UID` , "
                    + "`KID`) VALUES (?, ?)");
            pstmt.setLong(1, b.getId());
            pstmt.setLong(2, k.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("setKontoUser(): " + e.toString());
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
     * Verknüpft einen Bibliothekar mit einem Konto
     *
     * @param AbstractBenutzer b
     * @param Konto k
     */
    public void setKontoBibliothekar(AbstractBenutzer b, final Konto k, final Connection cn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setBibliothekarValues(cn.prepareStatement("SELECT * FROM `benutzer` WHERE `institut` = ? AND "
                    + "`abteilung` = ? AND `category` = ? AND `anrede` = ? AND `vorname` = ? AND `name` = ? "
                    + "AND `adr` = ? AND `adrzus` = ? AND `telp` = ? AND `telg` = ? AND `plz` = ? AND `ort` = ? "
                    + "AND `land` = ? AND `mail` = ? AND `pw` = ? AND `loginopt` = ? AND `userbestellung` = ? "
                    + "AND `gbvbestellung` = ? AND `billing` = ? AND `kontoval` = ? AND `kontostatus` = ? "
                    + "AND `rechte` = ? AND `gtc` = ? AND `gtcdate` = ?"), b, cn);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                b = b.getUser(rs, cn);
            }

            pstmt = cn.prepareStatement("INSERT INTO `v_konto_benutzer` (`UID` , "
                    + "`KID`) VALUES (?, ?)");
            pstmt.setLong(1, b.getId());
            pstmt.setLong(2, k.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("setKontoBibliothekar(): " + e.toString());
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

    // nötig beim Neuanlegen eines Bibliothekars aus der Registrierungsseite
    // (wird nicht von einem bestehenden User / Admin angelegt, deshalb nicht setUserValues)
    private PreparedStatement setBibliothekarValues(final PreparedStatement pstmt, final AbstractBenutzer user, final Connection cn)
    throws Exception {

        String userBestellung = "0";
        String gbvBestellung = "0";
        String loginOpt = "0";
        String kontoVal = "0";
        String kontoStatus = "0";

        if (user.isKontovalidation()) { kontoStatus = "1"; }
        if (user.isLoginopt()) { loginOpt = "1"; }
        if (user.isUserbestellung()) { userBestellung = "1"; }
        if (user.isGbvbestellung()) { gbvBestellung = "1"; }
        if (user.isKontostatus()) { kontoStatus = "1"; }
        if (user.isUserbestellung()) { userBestellung = "1"; }
        if (user.isGbvbestellung()) { gbvBestellung = "1"; }
        if (user.isLoginopt()) { loginOpt = "1"; }
        if (user.isKontovalidation()) { kontoVal = "1"; }

        if (user.getInstitut() != null) { pstmt.setString(1, user.getInstitut()); } else { pstmt.setString(1, ""); }
        if (user.getAbteilung() != null) { pstmt.setString(2, user.getAbteilung()); } else { pstmt.setString(2, ""); }
        pstmt.setLong(3, user.getCategory().getId());
        if (user.getAnrede() != null) { pstmt.setString(4, user.getAnrede()); } else { pstmt.setString(4, ""); }
        if (user.getVorname() != null) { pstmt.setString(5, user.getVorname()); } else { pstmt.setString(5, ""); }
        if (user.getName() != null) { pstmt.setString(6, user.getName()); } else { pstmt.setString(6, ""); }
        if (user.getAdresse() != null) { pstmt.setString(7, user.getAdresse()); } else { pstmt.setString(7, ""); }
        if (user.getAdresszusatz() != null) { pstmt.setString(8, user.getAdresszusatz()); } else { pstmt.setString(8, ""); }
        if (user.getTelefonnrp() != null) { pstmt.setString(9, user.getTelefonnrp()); } else { pstmt.setString(9, ""); }
        if (user.getTelefonnrg() != null) { pstmt.setString(10, user.getTelefonnrg()); } else { pstmt.setString(10, ""); }
        if (user.getPlz() != null) { pstmt.setString(11, user.getPlz()); } else { pstmt.setString(11, ""); }
        if (user.getOrt() != null) { pstmt.setString(12, user.getOrt()); } else { pstmt.setString(12, ""); }
        if (user.getLand() != null) { pstmt.setString(13, user.getLand()); } else { pstmt.setString(13, ""); }
        if (user.getEmail() != null) { pstmt.setString(14, user.getEmail()); } else { pstmt.setString(14, ""); }
        if (user.getPassword() != null) {
            // Falls das Passwort "" wäre, ist anzunehmen dass das PW bereits gesetzt ist und
            // beim Updaten nicht geändert werden soll
            if (user.getPassword().equals("da39a3ee5e6b4bd3255bfef95601890afd879") && user.getId() != null) {
                AbstractBenutzer userpw = new AbstractBenutzer();
                userpw = userpw.getUser(user.getId(), cn);
                pstmt.setString(15, userpw.getPassword());
            } else { pstmt.setString(15, user.getPassword()); }
        } else { pstmt.setString(15, ""); } // must not be null
        pstmt.setString(16, loginOpt);
        pstmt.setString(17, userBestellung);
        pstmt.setString(18, gbvBestellung);
        if (user.getBilling() != null) {
            pstmt.setString(19, user.getBilling().toString());
        } else {
            pstmt.setString(19, "");
        }
        pstmt.setString(20, kontoVal);
        pstmt.setString(21, kontoStatus);
        pstmt.setString(22, "2");
        if (user.getGtc() != null) { pstmt.setString(23, user.getGtc()); } else { pstmt.setString(23, ""); }
        if (user.getGtcdate() == null || user.getGtcdate().equals("")) {
            pstmt.setString(24, "0000:00:00 00:00:00");
        } else {
            pstmt.setString(24, user.getGtcdate());
        }

        return pstmt;
    }

    public Long getBID() {
        return BID;
    }

    public void setBID(final Long bid) {
        BID = bid;
    }

    public Long getKID() {
        return KID;
    }

    public void setKID(final Long kid) {
        KID = kid;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(final Konto konto) {
        this.konto = konto;
    }

    public AbstractBenutzer getU() {
        return u;
    }

    public void setU(final AbstractBenutzer u) {
        this.u = u;
    }

}
