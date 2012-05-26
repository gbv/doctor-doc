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
import java.util.List;

import org.grlea.log.SimpleLogger;


/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p></p>
 * @author Pascal Steiner
 */
public class Text extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Text.class);

    private Konto konto;
    private Texttyp texttyp;
    private String inhalt;


    public Text() {

    }

    /**
     * Erstellt einen Text anhand seines Inhaltes
     * @param cn
     * @param tInhalt
     */
    public Text(final Connection cn, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `inhalt`=?");
            pstmt.setString(1, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(Connection cn, String inhalt): " + e.toString());
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
     * Erstellt einen Text anhand eines Texttyps und dem Inhalt
     *
     * @param cn Connection
     * @param typ Texttyp
     * @param tInhalt String
     */
    public Text(final Connection cn, final Texttyp typ, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `inhalt`=?");
            pstmt.setLong(1, typ.getId());
            pstmt.setString(2, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Texttyp typ, String inhalt): " + e.toString());
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
     * Gets a Text from a Texttype, a KID and the content
     *
     * @param cn Connection
     * @param Texttyp typ
     * @param Long KID
     * @param tInhalt String
     */
    public Text(final Connection cn, final Texttyp typ, final Long kid, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `KID`=? AND `inhalt`=?");
            pstmt.setLong(1, typ.getId());
            pstmt.setLong(2, kid);
            pstmt.setString(3, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Texttyp typ, Long kid, String inhalt): " + e.toString());
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
     * Erstellt einen Text anhand eines Texttyps und einer KID
     *
     * @param cn Connection
     * @param typ Texttyp
     * @param kid long
     */
    public Text(final Connection cn, final Texttyp typ, final long kid) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `KID`=?");
            pstmt.setLong(1, typ.getId());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Texttyp typ, long kid): " + e.toString());
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
     * Erstellt einen Text anhand eines Texttyp-ID und dem Inhalt
     *
     * @param cn Connection
     * @param tyid Long
     * @param tInhalt String
     */
    public Text(final Connection cn, final Long tyid, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `inhalt`=?");
            pstmt.setLong(1, tyid);
            pstmt.setString(2, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }
        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Long tyid, String inhalt): " + e.toString());
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
     * Liefert eine Liste aller Texte anhand eines Texttypes <p></p>
     *
     * @param typ Texttyp
     * @param cn
     * @return List mit Textobjekten
     */
    public List<Text> getText(final Texttyp typ, final Connection cn) {
        final ArrayList<Text> tl = new ArrayList<Text>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? ORDER BY inhalt");
            pstmt.setLong(1, typ.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs);
                tl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("getText(Texttyp typ, Connection cn): " + e.toString());
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
        return tl;
    }

    /**
     * Erstellt ein Text aus einem ResultSet
     *
     * @param cn Connection
     * @param rs ResultSet
     */
    public Text(final Connection cn, final ResultSet rs) {

        try {
            this.setId(rs.getLong("TID"));
            this.setInhalt(rs.getString("inhalt"));
            this.setKonto(new Konto(rs.getLong("KID"), cn));
            this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
        } catch (final SQLException e) {
            LOG.error("Text(Connection cn, ResultSet rs): " + e.toString());
        }
    }

    /**
     * Erstellt einen Text anhand seiner ID
     *
     * @param cn Connection
     * @param id Long
     */
    public Text(final Connection cn, final Long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TID`=?");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }
        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Long id): " + e.toString());
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
     * Erstellt einen Text anhand seiner ID und einer KID
     *
     * @param cn Connection
     * @param id Long
     * @param kid Long
     */
    public Text(final Connection cn, final Long id, final Long kid) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TID`=? AND `KID`=?");
            pstmt.setLong(1, id);
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }
        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Long id, Long kid): " + e.toString());
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
     * Erstellt einen Text anhand seiner ID, einer KID und einer TYID
     *
     * @param cn Connection
     * @param id Long
     * @param kid Long
     * @param tyid Long
     */
    public Text(final Connection cn, final Long id, final Long kid, final Long tyid) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TID`=? AND `KID`=? AND `TYID`=?");
            pstmt.setLong(1, id);
            pstmt.setLong(2, kid);
            pstmt.setLong(3, tyid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                this.setInhalt(rs.getString("inhalt"));
            }
        } catch (final Exception e) {
            LOG.error("Text(Connection cn, Long id, Long kid, Long tyid): " + e.toString());
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
     * Speichert einen neuen Text
     *
     * @param cn Connection
     * @param t Text
     */
    public void saveNewText(final Connection cn, final Text t) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("INSERT INTO `text` (`KID` , `TYID`, `inhalt`) VALUES (?, ?, ?)");
            pstmt.setLong(1, t.getKonto().getId());
            pstmt.setLong(2, t.getTexttyp().getId());
            pstmt.setString(3, t.getInhalt());

            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("saveNewText(Connection cn, Text t): " + e.toString());
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
     * prüft, ob es IP-Einträge unter einem Konto gibt
     *
     * @param cn Connection
     * @param k Konto
     */
    public boolean hasIP(final Connection cn, final Konto k) {
        boolean check = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `KID`=? AND `TYID`=?");
            pstmt.setLong(1, k.getId());
            pstmt.setString(2, "9");

            rs = pstmt.executeQuery();

            if (rs.next()) {
                check = true;
            }

        } catch (final Exception e) {
            LOG.error("hasIP(Connection cn, Konto k): " + e.toString());
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
     * holt alle Texte mit Bereichen oder Wildcards anhand der ersten beiden Oktetten einer IP
     *
     * @param ip String
     * @param cn Connection
     * @return list ArrayList<Text>
     */
    public List<Text> possibleIPRanges(final String ip, final Connection cn) {
        final List<Text> list = new ArrayList<Text>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            final String ipPart = ip.substring(0, ip.indexOf(".", ip.indexOf(".") + 1) + 1);
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE  `inhalt` LIKE ? AND `TYID`=? "
                    + "AND (`inhalt` LIKE '%.*%' OR `inhalt` LIKE '%-%')");
            pstmt.setString(1, ipPart + "%");
            pstmt.setString(2, "9");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text txt = new Text();
                txt.setId(rs.getLong("TID"));
                txt.setKonto(new Konto(rs.getLong("KID"), cn));
                txt.setTexttyp(new Texttyp(rs.getLong("TYID"), cn));
                txt.setInhalt(rs.getString("inhalt"));
                list.add(txt);
            }

        } catch (final Exception e) {
            LOG.error("possibleIPRanges(Connection cn, String ip): " + e.toString());
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
        return list;
    }

    /**
     * Verändert einen Text
     *
     * @param cn Connection
     * @param t Text
     */
    public void updateText(final Connection cn, final Text t) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `text` SET `KID` = ?, `TYID` = ?, `inhalt` = ? WHERE `TID` = ?");
            pstmt.setLong(1, t.getKonto().getId());
            pstmt.setLong(2, t.getTexttyp().getId());
            pstmt.setString(3, t.getInhalt());
            pstmt.setLong(4, t.getId());

            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("updateText(Connection cn, Text t): " + e.toString());
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
     * Löscht einen Text
     *
     * @param cn Connection
     * @param t Text
     */
    public void deleteText(final Connection cn, final Text t) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM `text` WHERE `TID` = ?");
            pstmt.setLong(1, t.getId());

            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("deleteText(Connection cn, Text t)" + e.toString());
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
     * Liefert alle Texte eines Kontos eines bestimmten Texttypes
     *
     * @return List mit Text
     */
    public List<Text> getAllKontoText(final Texttyp t, final Long kid, final Connection cn) {
        final ArrayList<Text> sl = new ArrayList<Text>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND KID=? ORDER BY inhalt");
            pstmt.setLong(1, t.getId());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs);
                sl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("etAllKontoText(Texttyp t, Long kid, Connection cn): " + e.toString());
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

        return sl;
    }

    /**
     * Liefert generell alle Texte anhand eines Texttypes plus alle kontospezifischen Texte dieses Texttypes
     * @return List<Text> mit Stati-Texten
     */
    public List<Text> getAllTextPlusKontoTexts(final Texttyp t, final Long kid, final Connection cn) {
        final ArrayList<Text> sl = new ArrayList<Text>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND (KID IS null OR KID=?) ORDER BY inhalt");
            pstmt.setLong(1, t.getId());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs);
                sl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("getAllTextPlusKontoTexts(Texttyp t, Long kid, Connection cn): " + e.toString());
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

        return sl;
    }

    public Texttyp getTexttyp() {
        return texttyp;
    }

    public void setTexttyp(final Texttyp texttyp) {
        this.texttyp = texttyp;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(final String inhalt) {
        this.inhalt = inhalt;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(final Konto konto) {
        this.konto = konto;
    }


}
