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

import enums.TextType;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p></p>
 * @author Pascal Steiner
 */
public class Text extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Text.class);

    private Konto konto;
    private TextType texttype;
    private String inhalt;

    public Text() {

    }

    /**
     * Gets a Text from a TextType and the content.
     *
     * @param cn Connection
     * @param type TextType
     * @param tInhalt String
     */
    public Text(final Connection cn, final TextType type, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `inhalt`=?");
            pstmt.setLong(1, type.getValue());
            pstmt.setString(2, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttype(type);
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(final Connection cn, final TextType type, final String tInhalt): " + e.toString());
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
     * Gets a Text from a TextType, a KID and the content.
     *
     * @param cn Connection
     * @param TextType type
     * @param Long KID
     * @param tInhalt String
     */
    public Text(final Connection cn, final TextType type, final Long kid, final String tInhalt) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `KID`=? AND `inhalt`=?");
            pstmt.setLong(1, type.getValue());
            pstmt.setLong(2, kid);
            pstmt.setString(3, tInhalt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttype(type);
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(final Connection cn, final TextType type, final Long kid, final String tInhalt): "
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
    }

    /**
     * Gets a Text from a TextType and a KID.
     *
     * @param cn Connection
     * @param type TextType
     * @param kid long
     */
    public Text(final Connection cn, final TextType type, final long kid) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TYID`=? AND `KID`=?");
            pstmt.setLong(1, type.getValue());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttype(type);
                this.setInhalt(rs.getString("inhalt"));
            }

        } catch (final Exception e) {
            LOG.error("Text(final Connection cn, final TextType type, final long kid): " + e.toString());
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
     * Gets a Lis of all Texts from a TextType. <p></p>
     *
     * @param type TextType
     * @param cn
     * @return List<Text>
     */
    public List<Text> getText(final TextType type, final Connection cn) {
        final ArrayList<Text> tl = new ArrayList<Text>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? ORDER BY inhalt");
            pstmt.setLong(1, type.getValue());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs, type);
                tl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("List<Text> getText(final TextType type, final Connection cn): " + e.toString());
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
    public Text(final Connection cn, final ResultSet rs, final TextType type) {

        try {
            this.setId(rs.getLong("TID"));
            this.setInhalt(rs.getString("inhalt"));
            this.setKonto(new Konto(rs.getLong("KID"), cn));
            this.setTexttype(type);
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
    public Text(final Connection cn, final Long id, final TextType type) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TID`=?");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttype(type);
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
     * Gets a Text from it's ID, a KID and a TextType
     *
     * @param cn Connection
     * @param id Long
     * @param kid Long
     * @param type TextType
     */
    public Text(final Connection cn, final Long id, final Long kid, final TextType type) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `text` WHERE `TID`=? AND `KID`=? AND `TYID`=?");
            pstmt.setLong(1, id);
            pstmt.setLong(2, kid);
            pstmt.setLong(3, type.getValue());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                this.setId(rs.getLong("TID"));
                this.setKonto(new Konto(rs.getLong("KID"), cn));
                this.setTexttype(type);
                this.setInhalt(rs.getString("inhalt"));
            }
        } catch (final Exception e) {
            LOG.error("Text(final Connection cn, final Long id, final Long kid, final TextType type): " + e.toString());
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
            pstmt.setLong(2, t.getTexttype().getValue());
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
            pstmt.setLong(2, TextType.IP.getValue());

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
            pstmt.setLong(2, TextType.IP.getValue());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text txt = new Text();
                txt.setId(rs.getLong("TID"));
                txt.setKonto(new Konto(rs.getLong("KID"), cn));
                txt.setTexttype(TextType.IP);
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
            pstmt.setLong(2, t.getTexttype().getValue());
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
     * Gets all Texts of an account from a given TextType.
     *
     * @return List<Text>
     */
    public List<Text> getAllKontoText(final TextType type, final Long kid, final Connection cn) {
        final ArrayList<Text> sl = new ArrayList<Text>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND KID=? ORDER BY inhalt");
            pstmt.setLong(1, type.getValue());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs, type);
                sl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("List<Text> getAllKontoText(final TextType type, final Long kid, final Connection cn): "
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

        return sl;
    }

    /**
     * Gets all general Texts from a TextType plus all account Texts.
     * @return List<Text>
     */
    public List<Text> getAllTextPlusKontoTexts(final TextType type, final Long kid, final Connection cn) {
        final ArrayList<Text> sl = new ArrayList<Text>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM text WHERE TYID=? AND (KID IS null OR KID=?) ORDER BY inhalt");
            pstmt.setLong(1, type.getValue());
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Text text = new Text(cn, rs, type);
                sl.add(text);
            }

        } catch (final Exception e) {
            LOG.error("List<Text> getAllTextPlusKontoTexts(final TextType type, final Long kid, final Connection cn): "
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

        return sl;
    }

    public TextType getTexttype() {
        return texttype;
    }

    public void setTexttype(final TextType texttype) {
        this.texttype = texttype;
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
