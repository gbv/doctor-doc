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
 * <p/>
 * @author Markus Fischer
 */
public class Holding extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(Holding.class);

    private Konto konto;
    private Long kid;
    private String titel = "";
    private String coden;
    private String verlag = "";
    private String ort = "";
    private String issn;
    private String zdbid;

    // contains the baseUrl of a holding to be ordered
    private String baseurl;


    public Holding() {
        this.setKonto(new Konto());
    }

    public Holding(final Konto k) {
        this.setKonto(k);
    }

    /**
     * Erstellt ein Holding aus einem ResultSet
     *
     * @param cn Connection
     * @param rs ResultSet
     */
    public Holding(final Connection cn, final ResultSet rs) {

        try {
            this.setId(rs.getLong("HOID"));
            this.setKid(rs.getLong("KID"));
            this.setKonto(new Konto(this.getKid(), cn));
            this.setTitel(rs.getString("titel"));
            this.setCoden(rs.getString("coden"));
            this.setVerlag(rs.getString("verlag"));
            this.setOrt(rs.getString("ort"));
            this.setIssn(rs.getString("issn"));
            this.setZdbid(rs.getString("zdbid"));
        } catch (final SQLException e) {
            LOG.error("Holding(Connection cn, ResultSet rs: " + e.toString());
        }
    }

    private void setRsValues(final Connection cn, final ResultSet rs) throws Exception {
        this.setId(rs.getLong("HOID"));
        this.setKid(rs.getLong("KID"));
        this.setKonto(new Konto(this.getKid(), cn));
        this.setTitel(rs.getString("titel"));
        this.setCoden(rs.getString("coden"));
        this.setVerlag(rs.getString("verlag"));
        this.setOrt(rs.getString("ort"));
        this.setIssn(rs.getString("issn"));
        this.setZdbid(rs.getString("zdbid"));
    }

    private Holding setRsstValues(final Connection cn, final ResultSet rs) throws Exception {
        final Holding ho = new Holding();
        ho.setId(rs.getLong("HOID"));
        ho.setKid(rs.getLong("KID"));
        ho.setKonto(new Konto(ho.getKid(), cn));
        ho.setTitel(rs.getString("titel"));
        ho.setCoden(rs.getString("coden"));
        ho.setVerlag(rs.getString("verlag"));
        ho.setOrt(rs.getString("ort"));
        ho.setIssn(rs.getString("issn"));
        ho.setZdbid(rs.getString("zdbid"));
        return ho;
    }

    /**
     * Erstellt ein Holding anhand einer Verbindung und der ID
     *
     * @param Long hoid
     * @param Connection cn
     * @return Holding holding
     */
    public Holding(final Long hoid, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE HOID = ?");
            pstmt.setLong(1, hoid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                this.setRsValues(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("Holding (Long hoid, Connection cn): " + e.toString());
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
     * Holt ein Holding eines Kontos anhand einer Verbindung und dem Identifier (ISSN oder ZDB-ID)
     *
     * @param Long kid
     * @param String identwert
     * @param Connection cn
     * @return Holding holding
     */
    public Holding getHolding(final Long kId, final String identifier, final Connection cn) {

        Holding ho = new Holding();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE KID = ? "
                    + "AND (issn = ? OR coden = ? OR zdbid = ?)");
            pstmt.setLong(1, kId);
            pstmt.setString(2, identifier);
            pstmt.setString(3, identifier);
            pstmt.setString(4, identifier);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ho = setRsstValues(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("getHolding (Long kid, String identifier, Connection cn): " + e.toString());
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

        return ho;
    }

    /**
     * Gets a Holding from an existing Holding, ignoring the ID. This is useful to reduce the number of
     * identical Holdings for an account.
     *
     * @param Holding h
     * @param Connection cn
     * @return Holding holding
     */
    public Holding(final Holding h, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final StringBuffer sql = new StringBuffer(344);
        sql.append("SELECT * FROM holdings WHERE KID = ? AND titel = ? AND coden ");
        if (h.getCoden() == null || h.getCoden().equals("")) { sql.append("IS ? "); } else { sql.append("= ? "); }
        sql.append("AND verlag = ? AND ort = ? AND issn = ? AND zdbid ");
        if (h.getZdbid() == null || h.getZdbid().equals("")) { sql.append("IS ?"); } else { sql.append("= ?"); }

        try {
            pstmt = cn.prepareStatement(sql.toString());
            pstmt.setLong(1, h.getKid());
            pstmt.setString(2, h.getTitel());
            if (h.getCoden() == null || h.getCoden().equals("")) {
                pstmt.setString(3, null);
            } else {
                pstmt.setString(3, h.getCoden());
            }
            pstmt.setString(4, h.getVerlag());
            pstmt.setString(5, h.getOrt());
            pstmt.setString(6, h.getIssn());
            if (h.getZdbid() == null || h.getZdbid().equals("")) {
                pstmt.setString(7, null);
            } else {
                pstmt.setString(7, h.getZdbid());
            }
            rs = pstmt.executeQuery();

            if (rs.next()) { // just take the first one
                setRsValues(cn, rs);
            }

        } catch (final Exception e) {
            LOG.error("Holding (Holding h, Connection cn): " + e.toString());
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
     * Holt alle Holdings anhand einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID) und einer Verbindung
     *
     * @param ArrayList<String> identifiers
     * @param Connection cn
     * @return List<Holding> holdings
     */
    public List<Holding> getAllHoldings(final List<String> identifiers, final Connection cn) {

        final List<Holding> list = new ArrayList<Holding>();

        if (!identifiers.isEmpty()) {

            final StringBuffer sqlQuery = new StringBuffer(226);

            sqlQuery.append("SELECT * FROM holdings WHERE issn = ? OR coden = ? OR zdbid = ?");

            final int max = identifiers.size();
            for (int i = 1; i < max; i++) { // nur ausführen falls length > 1
                sqlQuery.append(" OR issn = ? OR coden = ? OR zdbid = ?");
            }

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = cn.prepareStatement(sqlQuery.toString());
                int pos = 1;
                for (final String identifier : identifiers) {
                    pstmt.setString(pos, identifier);
                    pstmt.setString(pos + 1, identifier);
                    pstmt.setString(pos + 2, identifier);
                    pos = pos + 3;
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    final Holding ho = setRsstValues(cn, rs);
                    list.add(ho);
                }

            } catch (final Exception e) {
                LOG.error("getAllHoldings (ArrayList<String> identifier, Connection cn): " + e.toString());
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

        return list;
    }

    /**
     * Gets all holdings for a given library from its KID and a connection
     *
     * @param Long kid
     * @param Connection cn
     * @return List<Holding> holdings
     */
    public List<Holding> getAllHoldingsForKonto(final Long kId, final Connection cn) {

        final List<Holding> list = new ArrayList<Holding>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM holdings WHERE KID = ?");
            pstmt.setLong(1, kId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                final Holding ho = setRsstValues(cn, rs);
                list.add(ho);
            }

        } catch (final Exception e) {
            LOG.error("getAllHoldingsForKonto (Long kid, Connection cn): " + e.toString());
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
     * Gets all holdings for a given library from its KID, a list of all related
     * identifieres (ISSN, Coden or ZDB-ID) and a connection
     *
     * @param ArrayList<String> identifiers
     * @param Long kid
     * @param Connection cn
     * @return List<Holding> holdings
     */
    public List<Holding> getAllHoldingsForKonto(final List<String> identifiers, final Long kId, final Connection cn) {

        final List<Holding> list = new ArrayList<Holding>();

        if (!identifiers.isEmpty()) {

            final StringBuffer sqlQuery = new StringBuffer(242);
            sqlQuery.append("SELECT * FROM holdings WHERE KID = ? AND (issn = ? OR coden = ? OR zdbid = ?");

            final int max = identifiers.size();
            for (int i = 1; i < max; i++) { // nur ausführen falls length > 1
                sqlQuery.append(" OR issn = ? OR coden = ? OR zdbid = ?");
            }

            sqlQuery.append(')');

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = cn.prepareStatement(sqlQuery.toString());
                pstmt.setLong(1, kId);
                int pos = 2;
                for (final String identifier : identifiers) {
                    pstmt.setString(pos, identifier);
                    pstmt.setString(pos + 1, identifier);
                    pstmt.setString(pos + 2, identifier);
                    pos = pos + 3;
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    final Holding ho = setRsstValues(cn, rs);
                    list.add(ho);
                }

            } catch (final Exception e) {
                LOG.error("getAllHoldingsForKonto (ArrayList<String> identifier, Long kid, "
                        + "Connection cn): " + e.toString());
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

        return list;
    }

    /**
     * Holt alle HOIDs anhand einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID) und einer Verbindung
     *
     * @param ArrayList<String> identifiers
     * @param Connection cn
     * @return List<String> HOIDs
     */
    public List<String> getAllHOIDs(final List<String> identifiers, final Connection cn) {

        final List<String> list = new ArrayList<String>();

        if (!identifiers.isEmpty()) {

            final StringBuffer sqlQuery = new StringBuffer(264);
            sqlQuery.append("SELECT HOID FROM holdings WHERE issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");

            final int max = identifiers.size();
            for (int i = 1; i < max; i++) { // nur ausführen falls length > 1
                sqlQuery.append(" OR issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
            }

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = cn.prepareStatement(sqlQuery.toString());
                int pos = 1;
                for (final String identifier : identifiers) {
                    pstmt.setString(pos, identifier);
                    pstmt.setString(pos + 1, identifier);
                    pstmt.setString(pos + 2, identifier);
                    pos = pos + 3;
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    list.add(rs.getString("HOID"));
                }

            } catch (final Exception e) {
                LOG.error("ArrayList<String> getAllHOIDs(ArrayList<String> identifier, "
                        + "Connection cn): " + e.toString());
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

        return list;
    }

    /**
     * Holt alle HOIDs eines spezifischen Kontos anhand der KID,
     * einer Liste aller verwandten Identifier (ISSN, Coden oder ZDB-ID)
     * und einer Verbindung
     *
     * @param ArrayList<String> identifiers
     * @param Long kid
     * @param Connection cn
     * @return List<String> HOIDs
     */
    public List<String> getAllHOIDsForKonto(final List<String> identifiers, final Long kId, final Connection cn) {

        final List<String> list = new ArrayList<String>();

        if (!identifiers.isEmpty()) {

            final StringBuffer sqlQuery = new StringBuffer(281);
            sqlQuery.append("SELECT HOID FROM holdings WHERE KID LIKE ? AND (issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");

            final int max = identifiers.size();
            for (int i = 1; i < max; i++) { // nur ausführen falls length > 1
                sqlQuery.append(" OR issn LIKE ? OR coden LIKE ? OR zdbid LIKE ?");
            }

            sqlQuery.append(')');

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = cn.prepareStatement(sqlQuery.toString());
                pstmt.setLong(1, kId);
                int pos = 2;
                for (final String identifier : identifiers) {
                    pstmt.setString(pos, identifier);
                    pstmt.setString(pos + 1, identifier);
                    pstmt.setString(pos + 2, identifier);
                    pos = pos + 3;
                }

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    list.add(rs.getString("HOID"));
                }

            } catch (final Exception e) {
                LOG.error("ArrayList<String> getAllHOIDsForKonto(ArrayList<String> identifier, Long kid, "
                        + "Connection cn): " + e.toString());
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

        return list;
    }

    /**
     * Prüft, ob bei einem Konto bereits ein betreffendes Holding besteht und
     * gibt es zurück. Falls noch kein Holding vorhanden ist wird ein neues erstellt.
     *
     * @param Long kid
     * @param String ident
     * @param String identwert
     * @param Connection cn
     * @return Holding holding
     */
    public Holding createHolding(final Konto k, final String identdescrip, final String ident, final Connection cn) {

        Holding ho = new Holding();

        try {
            ho = getHolding(k.getId(), ident, cn);

            if (ho.getId() == null) {
                ho.setKid(k.getId());
                ho.setKonto(k);
                if ("issn".equals(identdescrip)) { ho.setIssn(ident); }
                if ("zdbid".equals(identdescrip)) { ho.setZdbid(ident); }
                if ("coden".equals(identdescrip)) { ho.setCoden(ident); }
            }

        } catch (final Exception e) {
            LOG.error("createHolding (Konto k, String identdescrip, String ident, Connection cn): " + e.toString());
        }

        return ho;
    }

    /**
     * Speichert ein neues Holding in der Datenbank und gibt es mit der ID zurück
     *
     * @param Holding h
     * @param Connection cn
     * @return Holding h
     */
    public Holding save(final Holding h, final Connection cn) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("INSERT INTO `holdings` (`KID` , "
                    + "`titel` , `coden` , `verlag` , `ort` , `issn` , `zdbid`) VALUES (?, ?, ?, ?, ?, ?, ?)");

            pstmt.setLong(1, h.getKid());
            pstmt.setString(2, h.getTitel());
            pstmt.setString(3, h.getCoden());
            pstmt.setString(4, h.getVerlag());
            pstmt.setString(5, h.getOrt());
            pstmt.setString(6, h.getIssn());
            pstmt.setString(7, h.getZdbid());

            pstmt.executeUpdate();

            // ID des gerade gespeicherten Holdings ermitteln und im Holding hinterlegen
            rs = pstmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                h.setId(rs.getLong("LAST_INSERT_ID()"));
            } else {
                LOG.error("Didn't get an ID back at: save(Holding h, Connection cn)!");
            }

        } catch (final Exception e) {
            LOG.error("Holding save(Holding h, Connection cn)" + e.toString());
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
        return h;
    }

    /**
     * Deletes holdings from a given account that do not have any referenced stock.
     *
     * @param Konto k
     * @param Connection cn
     */
    public void purgeNotUsedKontoHoldings(final Konto k, final Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE a FROM holdings AS a LEFT OUTER JOIN stock AS b ON a.HOID = b.HOID "
                    + "WHERE a.KID=? AND b.STID IS null");
            pstmt.setLong(1, k.getId());
            pstmt.executeUpdate();

        } catch (final Exception e) {
            LOG.error("purgeNotUsedKontoHoldings(Konto k, Connection cn)" + e.toString());
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


    public Konto getKonto() {
        return konto;
    }


    public void setKonto(final Konto konto) {
        this.konto = konto;
    }


    public Long getKid() {
        return kid;
    }

    public void setKid(final Long kid) {
        this.kid = kid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(final String titel) {
        this.titel = titel;
    }

    public String getCoden() {
        return coden;
    }

    public void setCoden(final String coden) {
        this.coden = coden;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(final String verlag) {
        this.verlag = verlag;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(final String ort) {
        this.ort = ort;
    }

    public String getIssn() {
        return issn;
    }


    public void setIssn(final String issn) {
        this.issn = issn;
    }


    public String getZdbid() {
        return zdbid;
    }


    public void setZdbid(final String zdbid) {
        this.zdbid = zdbid;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(final String baseurl) {
        this.baseurl = baseurl;
    }


}
