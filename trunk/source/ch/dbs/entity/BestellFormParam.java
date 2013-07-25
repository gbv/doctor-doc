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

import org.apache.struts.validator.ValidatorForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.form.Message;
import enums.TextType;

/**
 * Class to handle the parameters for the customizable internal order forms.<p></p>
 * 
 * @author Markus Fischer
 */

public class BestellFormParam extends ValidatorForm {

    private static final Logger LOG = LoggerFactory.getLogger(BestellFormParam.class);

    private static final long serialVersionUID = 1L;
    private Long id;
    // we'll have 1 orderform for several IPs within the same account
    private Long kid;
    private Long tyid;
    private Long use_did; // use external order form for this case
    private String kennung;
    // defines, if this order will be additionally saved in the database
    // an email will always be sent. This can be configured differently for each orderform.
    private boolean saveorder;

    // if deactivated, the patron can't order over this OrderForm
    private boolean deactivated;

    // Activation of optional fields true / false
    private boolean institution;
    private boolean abteilung;
    private boolean category;
    private boolean adresse; // Summary address. Contains street, zip, place (strasse, plz and ort)
    private boolean strasse;
    private boolean plz;
    private boolean ort;
    private boolean land;
    private boolean telefon;
    private boolean benutzernr;
    private boolean prio;
    private boolean lieferart;
    private String lieferart_value1;
    private String lieferart_value2;
    private String lieferart_value3;

    // fields that may be named freely
    private boolean freitxt1;
    private boolean freitxt2;
    private boolean freitxt3;
    private String freitxt1_name;
    private String freitxt2_name;
    private String freitxt3_name;
    private String comment1;
    private String comment2;

    // definition of required fields
    private boolean inst_required;
    private boolean abt_required;
    private boolean category_required;
    private boolean freitxt1_required;
    private boolean freitxt2_required;
    private boolean freitxt3_required;
    private boolean adr_required;
    private boolean str_required;
    private boolean plz_required;
    private boolean ort_required;
    private boolean land_required;
    private boolean telefon_required;
    private boolean benutzernr_required;

    // Option radio button (three buttons)
    private boolean option;
    private String option_name; // name of the field
    private String option_comment; // comment / description
    private String option_linkout; // additional external link
    private String option_linkoutname; // name of the link
    private String option_value1; // select values...
    private String option_value2;
    private String option_value3;

    // Fees and General Terms and Conditions
    private boolean gebuehren;
    private String link_gebuehren;
    private boolean agb;
    private String link_agb;

    // logical parameters without DB entry
    private Message message;
    private boolean back;
    private String link_back;

    public BestellFormParam() {

    }

    /**
     * Creates a BestellFormParam by ID and a Connection.
     * 
     * @param Long id
     * @param Connection cn
     * @return BestellFormParam bp
     */
    public BestellFormParam(final Long pbId, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellform_param WHERE BPID = ?");
            pstmt.setLong(1, pbId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(rs);
            }

        } catch (final Exception e) {
            LOG.error("BestellFormParam (Long id, Connection cn): " + e.toString());
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
     * Creates a BestellFormParam by its type, the KID and a Connection.
     * 
     * @param String kennung
     * @param Long kid
     * @param Connection cn
     * @return BestellFormParam bp
     */
    public BestellFormParam(final String kenn, final Long kid, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellform_param WHERE kennung = ? AND KID = ?");
            pstmt.setString(1, kenn);
            pstmt.setLong(2, kid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(rs);
            }

        } catch (final Exception e) {
            LOG.error("BestellFormParam (String kennung, Long kid, Connection cn): " + e.toString());
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
     * Creates a BestellFormParam by a Text a Connection.
     * 
     * @param Text t
     * @param Connection cn
     * @return BestellFormParam bp
     */
    public BestellFormParam(final Text t, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (t != null && t.getId() != null) {
                // nur 1 Formular für alle IP-Zugriffe
                if (t.getTexttype().getValue() == TextType.IP4.getValue()) {
                    t.setInhalt("");
                }

                pstmt = cn
                        .prepareStatement("SELECT * FROM bestellform_param WHERE KID = ? AND TYID = ? AND kennung = ?");
                pstmt.setLong(1, t.getKonto().getId());
                pstmt.setLong(2, t.getTexttype().getValue());
                pstmt.setString(3, t.getInhalt());
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    this.setRsValues(rs);
                }
            }

        } catch (final Exception e) {
            LOG.error("BestellFormParam (Text t, Connection cn): " + e.toString());
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
     * Creates a BestellFormParam of the type "oder form logged in" by a Konto and a Connection,
     * in the case, where a patron is logged in when ordering. This BestellFormParam is written
     * directly to the DB.
     * 
     * @param Konto k
     * @param Connection cn
     * @return BestellFormParam bp
     */
    public BestellFormParam(final Konto k, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellform_param WHERE KID = ? AND TYID = ? AND kennung = ?");
            pstmt.setLong(1, k.getId());
            pstmt.setLong(2, TextType.ORDERFORM_LOGGED_IN.getValue());
            pstmt.setString(3, "Bestellformular eingeloggt");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(rs);
            }

        } catch (final Exception e) {
            LOG.error("BestellFormParam (Text t, Connection cn): " + e.toString());
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
     * Gets all existing BestellFormParams for a given account.
     * 
     * @param Konto k
     * @param Connection cn
     * @return List<String> BestellFormParam result
     */
    public List<BestellFormParam> getAllBestellFormParam(final Konto k, final Connection cn) {

        final List<BestellFormParam> result = new ArrayList<BestellFormParam>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellform_param WHERE KID = ?");
            pstmt.setLong(1, k.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new BestellFormParam(rs));
            }

        } catch (final Exception e) {
            LOG.error("getAllBestellFormParam (Konto k, Connection cn): " + e.toString());
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
        return result;
    }

    /**
     * Saves a new BestellFormParam into the database.
     * 
     * @param BestellFormParam bp
     */
    public Long save(final BestellFormParam bp, final Connection cn) {

        Long pbId = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setBestellFormParamValues(
                    cn.prepareStatement("INSERT INTO `bestellform_param` (`KID` , `TYID` , `USE_DID` , "
                            + "`kennung` , `saveorder` , `deactivated` , `institution` , `abteilung` , `category` , `adresse` , `strasse` , "
                            + "`plz` , `ort` , `telefon` , `benutzernr` , `land` , `prio` , `lieferart` , `lieferart_value1` , "
                            + "`lieferart_value2` , `lieferart_value3` , `frei1` , `frei2` , `frei3` , `frei1_name` , "
                            + "`frei2_name` , `frei3_name` , `comment1` , `comment2` , `option` , `option_name` , "
                            + "`option_comment` , `option_linkout` , `option_linkoutname` , `option_value1` , "
                            + "`option_value2` , `option_value3` , `gebuehren` , `gebuehren_link` , `agb` , `agb_link` , "
                            + "`inst_required` , `abt_required` , `category_required` , `frei1_required` , `frei2_required` , `frei3_required` , "
                            + "`adr_required` , `str_required` , `plz_required` , `ort_required` , `land_required` , "
                            + "`tel_required` , `benutzernr_required`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "?, ?, ?, ?, ?, ?)"), bp);

            pstmt.executeUpdate();

            // get back last inserted ID
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                pbId = rs.getLong("LAST_INSERT_ID()");
            }

        } catch (final Exception e) {
            LOG.error("save(BestellFormParam bp, Connection cn): " + e.toString());
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

        return pbId;
    }

    /**
     * Updates an existing BestellFormParam.
     * 
     * @param BestellFormParam bp
     */
    public void update(final BestellFormParam bp, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = setBestellFormParamValues(
                    cn.prepareStatement("UPDATE `bestellform_param` SET "
                            + "`KID` = ?, `TYID` = ?, `USE_DID` = ?, `kennung` = ?, `saveorder` = ?, `deactivated` = ?, `institution` = ?, `abteilung` = ?, "
                            + "`category` = ?, `adresse` = ?,`strasse` = ?, `plz` = ?, `ort` = ?, `telefon` = ?, `benutzernr` = ?, `land` = ?, "
                            + "`prio` = ?, `lieferart` = ?,`lieferart_value1` = ?, `lieferart_value2` = ?, "
                            + "`lieferart_value3` = ?, `frei1` = ?, `frei2` = ?, `frei3` = ?, `frei1_name` = ?, "
                            + "`frei2_name` = ?, `frei3_name` = ?, `comment1` = ?, `comment2` = ?, `option` = ?, "
                            + "`option_name` = ?, `option_comment` = ?, `option_linkout` = ?, `option_linkoutname` = ?, "
                            + "`option_value1` = ?, `option_value2` = ?, `option_value3` = ?, `gebuehren` = ?, "
                            + "`gebuehren_link` = ?, `agb` = ?, `agb_link` = ?, `inst_required` = ?, `abt_required` = ?, "
                            + "`category_required` = ?, `frei1_required` = ?, `frei2_required` = ?, `frei3_required` = ?, `adr_required` = ?, "
                            + "`str_required` = ?, `plz_required` = ?, `ort_required` = ?, `land_required` = ?, "
                            + "`tel_required` = ?, `benutzernr_required` = ? WHERE `BPID` =?"), bp);
            pstmt.setLong(55, bp.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("bei update(BestellFormParam bp, Connection cn): " + e.toString());
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

    private BestellFormParam(final ResultSet rs) throws SQLException {

        this.setId(rs.getLong("BPID"));
        this.setKid(rs.getLong("KID"));
        this.setTyid(rs.getLong("TYID"));
        // only set if we have a USE_DID. If not, leave null!
        if (rs.getString("USE_DID") != null) {
            this.setUse_did(rs.getLong("USE_DID"));
        } else {
            this.setUse_did(Long.valueOf("0"));
        }
        this.setKennung(rs.getString("kennung"));
        this.setSaveorder(rs.getBoolean("saveorder"));
        this.setDeactivated(rs.getBoolean("deactivated"));
        this.setInstitution(rs.getBoolean("institution"));
        this.setAbteilung(rs.getBoolean("abteilung"));
        this.setCategory(rs.getBoolean("category"));
        this.setAdresse(rs.getBoolean("adresse"));
        this.setStrasse(rs.getBoolean("strasse"));
        this.setPlz(rs.getBoolean("plz"));
        this.setOrt(rs.getBoolean("ort"));
        this.setTelefon(rs.getBoolean("telefon"));
        this.setBenutzernr(rs.getBoolean("benutzernr"));
        this.setLand(rs.getBoolean("land"));
        this.setPrio(rs.getBoolean("prio"));
        this.setLieferart(rs.getBoolean("lieferart"));
        this.setLieferart_value1(rs.getString("lieferart_value1"));
        this.setLieferart_value2(rs.getString("lieferart_value2"));
        this.setLieferart_value3(rs.getString("lieferart_value3"));
        this.setFreitxt1(rs.getBoolean("frei1"));
        this.setFreitxt2(rs.getBoolean("frei2"));
        this.setFreitxt3(rs.getBoolean("frei3"));
        this.setFreitxt1_name(rs.getString("frei1_name"));
        this.setFreitxt2_name(rs.getString("frei2_name"));
        this.setFreitxt3_name(rs.getString("frei3_name"));
        this.setComment1(rs.getString("comment1"));
        this.setComment2(rs.getString("comment2"));
        this.setOption(rs.getBoolean("option"));
        this.setOption_name(rs.getString("option_name"));
        this.setOption_comment(rs.getString("option_comment"));
        this.setOption_linkout(rs.getString("option_linkout"));
        this.setOption_linkoutname(rs.getString("option_linkoutname"));
        this.setOption_value1(rs.getString("option_value1"));
        this.setOption_value2(rs.getString("option_value2"));
        this.setOption_value3(rs.getString("option_value3"));
        this.setGebuehren(rs.getBoolean("gebuehren"));
        this.setLink_gebuehren(rs.getString("gebuehren_link"));
        this.setAgb(rs.getBoolean("agb"));
        this.setLink_agb(rs.getString("agb_link"));
        this.setInst_required(rs.getBoolean("inst_required"));
        this.setAbt_required(rs.getBoolean("abt_required"));
        this.setCategory_required(rs.getBoolean("category_required"));
        this.setInst_required(rs.getBoolean("inst_required"));
        this.setFreitxt1_required(rs.getBoolean("frei1_required"));
        this.setFreitxt2_required(rs.getBoolean("frei2_required"));
        this.setFreitxt3_required(rs.getBoolean("frei3_required"));
        this.setAdr_required(rs.getBoolean("adr_required"));
        this.setStr_required(rs.getBoolean("str_required"));
        this.setPlz_required(rs.getBoolean("plz_required"));
        this.setOrt_required(rs.getBoolean("ort_required"));
        this.setLand_required(rs.getBoolean("land_required"));
        this.setTelefon_required(rs.getBoolean("tel_required"));
        this.setBenutzernr_required(rs.getBoolean("benutzernr_required"));

    }

    private void setRsValues(final ResultSet rs) throws Exception {
        this.setId(rs.getLong("BPID"));
        this.setKid(rs.getLong("KID"));
        this.setTyid(rs.getLong("TYID"));
        // only set if we have a USE_DID. If not, leave null!
        if (rs.getString("USE_DID") != null) {
            this.setUse_did(rs.getLong("USE_DID"));
        } else {
            this.setUse_did(Long.valueOf("0"));
        }
        this.setKennung(rs.getString("kennung"));
        this.setSaveorder(rs.getBoolean("saveorder"));
        this.setDeactivated(rs.getBoolean("deactivated"));
        this.setInstitution(rs.getBoolean("institution"));
        this.setAbteilung(rs.getBoolean("abteilung"));
        this.setCategory(rs.getBoolean("category"));
        this.setAdresse(rs.getBoolean("adresse"));
        this.setStrasse(rs.getBoolean("strasse"));
        this.setPlz(rs.getBoolean("plz"));
        this.setOrt(rs.getBoolean("ort"));
        this.setTelefon(rs.getBoolean("telefon"));
        this.setBenutzernr(rs.getBoolean("benutzernr"));
        this.setLand(rs.getBoolean("land"));
        this.setPrio(rs.getBoolean("prio"));
        this.setLieferart(rs.getBoolean("lieferart"));
        this.setLieferart_value1(rs.getString("lieferart_value1"));
        this.setLieferart_value2(rs.getString("lieferart_value2"));
        this.setLieferart_value3(rs.getString("lieferart_value3"));
        this.setFreitxt1(rs.getBoolean("frei1"));
        this.setFreitxt2(rs.getBoolean("frei2"));
        this.setFreitxt3(rs.getBoolean("frei3"));
        this.setFreitxt1_name(rs.getString("frei1_name"));
        this.setFreitxt2_name(rs.getString("frei2_name"));
        this.setFreitxt3_name(rs.getString("frei3_name"));
        this.setComment1(rs.getString("comment1"));
        this.setComment2(rs.getString("comment2"));
        this.setOption(rs.getBoolean("option"));
        this.setOption_name(rs.getString("option_name"));
        this.setOption_comment(rs.getString("option_comment"));
        this.setOption_linkout(rs.getString("option_linkout"));
        this.setOption_linkoutname(rs.getString("option_linkoutname"));
        this.setOption_value1(rs.getString("option_value1"));
        this.setOption_value2(rs.getString("option_value2"));
        this.setOption_value3(rs.getString("option_value3"));
        this.setGebuehren(rs.getBoolean("gebuehren"));
        this.setLink_gebuehren(rs.getString("gebuehren_link"));
        this.setAgb(rs.getBoolean("agb"));
        this.setLink_agb(rs.getString("agb_link"));
        this.setInst_required(rs.getBoolean("inst_required"));
        this.setAbt_required(rs.getBoolean("abt_required"));
        this.setCategory_required(rs.getBoolean("category_required"));
        this.setInst_required(rs.getBoolean("inst_required"));
        this.setFreitxt1_required(rs.getBoolean("frei1_required"));
        this.setFreitxt2_required(rs.getBoolean("frei2_required"));
        this.setFreitxt3_required(rs.getBoolean("frei3_required"));
        this.setAdr_required(rs.getBoolean("adr_required"));
        this.setStr_required(rs.getBoolean("str_required"));
        this.setPlz_required(rs.getBoolean("plz_required"));
        this.setOrt_required(rs.getBoolean("ort_required"));
        this.setLand_required(rs.getBoolean("land_required"));
        this.setTelefon_required(rs.getBoolean("tel_required"));
        this.setBenutzernr_required(rs.getBoolean("benutzernr_required"));
    }

    /*
     * Sets the values in the PreparedStatement of the methods update() and save().
     *
     */
    private PreparedStatement setBestellFormParamValues(final PreparedStatement pstmt, final BestellFormParam bp)
            throws Exception {

        pstmt.setLong(1, bp.getKid());
        pstmt.setLong(2, bp.getTyid());
        if (bp.getUse_did() == null || bp.getUse_did() == 0) {
            pstmt.setString(3, null);
        } else {
            pstmt.setLong(3, bp.getUse_did());
        }
        if (bp.getKennung() != null) {
            pstmt.setString(4, bp.getKennung());
        } else {
            pstmt.setString(4, "");
        }
        pstmt.setBoolean(5, bp.isSaveorder());
        pstmt.setBoolean(6, bp.isDeactivated());
        pstmt.setBoolean(7, bp.isInstitution());
        pstmt.setBoolean(8, bp.isAbteilung());
        pstmt.setBoolean(9, bp.isCategory());
        pstmt.setBoolean(10, bp.isAdresse());
        pstmt.setBoolean(11, bp.isStrasse());
        pstmt.setBoolean(12, bp.isPlz());
        pstmt.setBoolean(13, bp.isOrt());
        pstmt.setBoolean(14, bp.isTelefon());
        pstmt.setBoolean(15, bp.isBenutzernr());
        pstmt.setBoolean(16, bp.isLand());
        pstmt.setBoolean(17, bp.isPrio());
        pstmt.setBoolean(18, bp.isLieferart());
        if (bp.getLieferart_value1() == null || bp.getLieferart_value1().equals("")) {
            pstmt.setString(19, null);
            bp.setLieferart_value1(null);
        } else {
            pstmt.setString(19, bp.getLieferart_value1().trim());
        }
        if (bp.getLieferart_value2() == null || bp.getLieferart_value2().equals("")) {
            pstmt.setString(20, null);
            bp.setLieferart_value2(null);
        } else {
            pstmt.setString(20, bp.getLieferart_value2().trim());
        }
        if (bp.getLieferart_value3() == null || bp.getLieferart_value3().equals("")) {
            pstmt.setString(21, null);
            bp.setLieferart_value3(null);
        } else {
            pstmt.setString(21, bp.getLieferart_value3().trim());
        }
        pstmt.setBoolean(22, bp.isFreitxt1());
        pstmt.setBoolean(23, bp.isFreitxt2());
        pstmt.setBoolean(24, bp.isFreitxt3());
        if (bp.getFreitxt1_name() != null) {
            pstmt.setString(25, bp.getFreitxt1_name());
        } else {
            pstmt.setString(25, "");
        }
        if (bp.getFreitxt2_name() != null) {
            pstmt.setString(26, bp.getFreitxt2_name());
        } else {
            pstmt.setString(26, "");
        }
        if (bp.getFreitxt3_name() != null) {
            pstmt.setString(27, bp.getFreitxt3_name());
        } else {
            pstmt.setString(27, "");
        }
        if (bp.getComment1() == null || bp.getComment1().equals("")) {
            pstmt.setString(28, null);
        } else {
            pstmt.setString(28, bp.getComment1());
        }
        if (bp.getComment2() == null || bp.getComment2().equals("")) {
            pstmt.setString(29, null);
        } else {
            pstmt.setString(29, bp.getComment2());
        }
        pstmt.setBoolean(30, bp.isOption());
        if (bp.getOption_name() != null) {
            pstmt.setString(31, bp.getOption_name());
        } else {
            pstmt.setString(31, "");
        }
        if (bp.getOption_comment() != null) {
            pstmt.setString(32, bp.getOption_comment());
        } else {
            pstmt.setString(32, "");
        }
        if (bp.getOption_linkout() != null) {
            pstmt.setString(33, bp.getOption_linkout());
        } else {
            pstmt.setString(33, "");
        }
        if (bp.getOption_linkoutname() != null) {
            pstmt.setString(34, bp.getOption_linkoutname());
        } else {
            pstmt.setString(34, "");
        }
        if (bp.getOption_value1() == null || bp.getOption_value1().equals("")) {
            pstmt.setString(35, null);
        } else {
            pstmt.setString(35, bp.getOption_value1());
        }
        if (bp.getOption_value2() == null || bp.getOption_value2().equals("")) {
            pstmt.setString(36, null);
        } else {
            pstmt.setString(36, bp.getOption_value2());
        }
        if (bp.getOption_value3() == null || bp.getOption_value3().equals("")) {
            pstmt.setString(37, null);
        } else {
            pstmt.setString(37, bp.getOption_value3());
        }
        pstmt.setBoolean(38, bp.isGebuehren());
        if (bp.getLink_gebuehren() != null) {
            pstmt.setString(39, bp.getLink_gebuehren());
        } else {
            pstmt.setString(39, "");
        }
        pstmt.setBoolean(40, bp.isAgb());
        if (bp.getLink_agb() != null) {
            pstmt.setString(41, bp.getLink_agb());
        } else {
            pstmt.setString(41, "");
        }
        pstmt.setBoolean(42, bp.isInst_required());
        pstmt.setBoolean(43, bp.isAbt_required());
        pstmt.setBoolean(44, bp.isCategory_required());
        pstmt.setBoolean(45, bp.isFreitxt1_required());
        pstmt.setBoolean(46, bp.isFreitxt2_required());
        pstmt.setBoolean(47, bp.isFreitxt3_required());
        pstmt.setBoolean(48, bp.isAdr_required());
        pstmt.setBoolean(49, bp.isStr_required());
        pstmt.setBoolean(50, bp.isPlz_required());
        pstmt.setBoolean(51, bp.isOrt_required());
        pstmt.setBoolean(52, bp.isLand_required());
        pstmt.setBoolean(53, bp.isTelefon_required());
        pstmt.setBoolean(54, bp.isBenutzernr_required());

        return pstmt;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getKid() {
        return kid;
    }

    public void setKid(final Long kid) {
        this.kid = kid;
    }

    public Long getTyid() {
        return tyid;
    }

    public void setTyid(final Long tyid) {
        this.tyid = tyid;
    }

    public Long getUse_did() {
        return use_did;
    }

    public void setUse_did(final Long useDid) {
        use_did = useDid;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(final String kennung) {
        this.kennung = kennung;
    }

    public boolean isSaveorder() {
        return saveorder;
    }

    public void setSaveorder(final boolean saveorder) {
        this.saveorder = saveorder;
    }

    public boolean isInstitution() {
        return institution;
    }

    public void setInstitution(final boolean institution) {
        this.institution = institution;
    }

    public boolean isAbteilung() {
        return abteilung;
    }

    public void setAbteilung(final boolean abteilung) {
        this.abteilung = abteilung;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(final boolean category) {
        this.category = category;
    }

    public boolean isAdresse() {
        return adresse;
    }

    public void setAdresse(final boolean adresse) {
        this.adresse = adresse;
    }

    public boolean isStrasse() {
        return strasse;
    }

    public void setStrasse(final boolean strasse) {
        this.strasse = strasse;
    }

    public boolean isPlz() {
        return plz;
    }

    public void setPlz(final boolean plz) {
        this.plz = plz;
    }

    public boolean isOrt() {
        return ort;
    }

    public void setOrt(final boolean ort) {
        this.ort = ort;
    }

    public boolean isLand() {
        return land;
    }

    public void setLand(final boolean land) {
        this.land = land;
    }

    public boolean isTelefon() {
        return telefon;
    }

    public void setTelefon(final boolean telefon) {
        this.telefon = telefon;
    }

    public boolean isBenutzernr() {
        return benutzernr;
    }

    public void setBenutzernr(final boolean benutzernr) {
        this.benutzernr = benutzernr;
    }

    public boolean isPrio() {
        return prio;
    }

    public void setPrio(final boolean prio) {
        this.prio = prio;
    }

    public boolean isLieferart() {
        return lieferart;
    }

    public void setLieferart(final boolean lieferart) {
        this.lieferart = lieferart;
    }

    public String getLieferart_value1() {
        return lieferart_value1;
    }

    public void setLieferart_value1(final String lieferart_value1) {
        this.lieferart_value1 = lieferart_value1;
    }

    public String getLieferart_value2() {
        return lieferart_value2;
    }

    public void setLieferart_value2(final String lieferart_value2) {
        this.lieferart_value2 = lieferart_value2;
    }

    public String getLieferart_value3() {
        return lieferart_value3;
    }

    public void setLieferart_value3(final String lieferart_value3) {
        this.lieferart_value3 = lieferart_value3;
    }

    public boolean isFreitxt1() {
        return freitxt1;
    }

    public void setFreitxt1(final boolean freitxt1) {
        this.freitxt1 = freitxt1;
    }

    public boolean isFreitxt2() {
        return freitxt2;
    }

    public void setFreitxt2(final boolean freitxt2) {
        this.freitxt2 = freitxt2;
    }

    public boolean isFreitxt3() {
        return freitxt3;
    }

    public void setFreitxt3(final boolean freitxt3) {
        this.freitxt3 = freitxt3;
    }

    public String getFreitxt1_name() {
        return freitxt1_name;
    }

    public void setFreitxt1_name(final String freitxt1_name) {
        this.freitxt1_name = freitxt1_name;
    }

    public String getFreitxt2_name() {
        return freitxt2_name;
    }

    public void setFreitxt2_name(final String freitxt2_name) {
        this.freitxt2_name = freitxt2_name;
    }

    public String getFreitxt3_name() {
        return freitxt3_name;
    }

    public void setFreitxt3_name(final String freitxt3_name) {
        this.freitxt3_name = freitxt3_name;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(final String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(final String comment2) {
        this.comment2 = comment2;
    }

    public boolean isInst_required() {
        return inst_required;
    }

    public void setInst_required(final boolean inst_required) {
        this.inst_required = inst_required;
    }

    public boolean isAbt_required() {
        return abt_required;
    }

    public void setAbt_required(final boolean abt_required) {
        this.abt_required = abt_required;
    }

    public boolean isCategory_required() {
        return category_required;
    }

    public void setCategory_required(final boolean categoryRequired) {
        category_required = categoryRequired;
    }

    public boolean isFreitxt1_required() {
        return freitxt1_required;
    }

    public void setFreitxt1_required(final boolean freitxt1_required) {
        this.freitxt1_required = freitxt1_required;
    }

    public boolean isFreitxt2_required() {
        return freitxt2_required;
    }

    public void setFreitxt2_required(final boolean freitxt2_required) {
        this.freitxt2_required = freitxt2_required;
    }

    public boolean isFreitxt3_required() {
        return freitxt3_required;
    }

    public void setFreitxt3_required(final boolean freitxt3_required) {
        this.freitxt3_required = freitxt3_required;
    }

    public boolean isAdr_required() {
        return adr_required;
    }

    public void setAdr_required(final boolean adr_required) {
        this.adr_required = adr_required;
    }

    public boolean isStr_required() {
        return str_required;
    }

    public void setStr_required(final boolean str_required) {
        this.str_required = str_required;
    }

    public boolean isPlz_required() {
        return plz_required;
    }

    public void setPlz_required(final boolean plz_required) {
        this.plz_required = plz_required;
    }

    public boolean isOrt_required() {
        return ort_required;
    }

    public boolean isLand_required() {
        return land_required;
    }

    public void setLand_required(final boolean land_required) {
        this.land_required = land_required;
    }

    public void setOrt_required(final boolean ort_required) {
        this.ort_required = ort_required;
    }

    public boolean isTelefon_required() {
        return telefon_required;
    }

    public void setTelefon_required(final boolean telefon_required) {
        this.telefon_required = telefon_required;
    }

    public boolean isBenutzernr_required() {
        return benutzernr_required;
    }

    public void setBenutzernr_required(final boolean benutzernr_required) {
        this.benutzernr_required = benutzernr_required;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(final boolean option) {
        this.option = option;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(final String option_name) {
        this.option_name = option_name;
    }

    public String getOption_comment() {
        return option_comment;
    }

    public void setOption_comment(final String option_comment) {
        this.option_comment = option_comment;
    }

    public String getOption_linkout() {
        return option_linkout;
    }

    public void setOption_linkout(final String option_linkout) {
        this.option_linkout = option_linkout;
    }

    public String getOption_linkoutname() {
        return option_linkoutname;
    }

    public void setOption_linkoutname(final String option_linkoutname) {
        this.option_linkoutname = option_linkoutname;
    }

    public String getOption_value1() {
        return option_value1;
    }

    public void setOption_value1(final String option_value1) {
        this.option_value1 = option_value1;
    }

    public String getOption_value2() {
        return option_value2;
    }

    public void setOption_value2(final String option_value2) {
        this.option_value2 = option_value2;
    }

    public String getOption_value3() {
        return option_value3;
    }

    public void setOption_value3(final String option_value3) {
        this.option_value3 = option_value3;
    }

    public boolean isGebuehren() {
        return gebuehren;
    }

    public void setGebuehren(final boolean gebuehren) {
        this.gebuehren = gebuehren;
    }

    public String getLink_gebuehren() {
        return link_gebuehren;
    }

    public void setLink_gebuehren(final String link_gebuehren) {
        this.link_gebuehren = link_gebuehren;
    }

    public boolean isAgb() {
        return agb;
    }

    public void setAgb(final boolean agb) {
        this.agb = agb;
    }

    public String getLink_agb() {
        return link_agb;
    }

    public void setLink_agb(final String link_agb) {
        this.link_agb = link_agb;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(final boolean back) {
        this.back = back;
    }

    public String getLink_back() {
        return link_back;
    }

    public void setLink_back(final String link_back) {
        this.link_back = link_back;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(final boolean deactivated) {
        this.deactivated = deactivated;
    }

}
