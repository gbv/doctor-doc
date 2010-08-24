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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.grlea.log.SimpleLogger;

import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Pascal Steiner
 */
public class OrderState extends AbstractIdEntity {

    private static final SimpleLogger LOG = new SimpleLogger(OrderState.class);

    private String orderstate;
    private String date;
    private String bemerkungen;
    private String bearbeiter;


    public OrderState() { }

    public OrderState(AbstractBenutzer user, Konto k) {

    }

    // History Bestellungen eröffnen, Status, Statusdatum sowie Bestelldatum eintragen
    public void setNewOrderState(Bestellungen b, Konto k, Text t, String bemerk, String bearb,
            Connection cn) {

        PreparedStatement pstmt = null;
        try {

            pstmt = cn.prepareStatement("INSERT INTO `bestellstatus` (`BID` , "
                    + "`TID` , `bemerkungen` , `bearbeiter` , `date`) VALUES (?, ?, ?, ?, ?)");
            pstmt.setLong(1, b.getId());
            pstmt.setLong(2, t.getId());
            pstmt.setString(3, bemerk);
            pstmt.setString(4, bearb);

            Date dt = new Date();
            ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datum = sdf.format(dt, k.getTimezone());

            pstmt.setString(5, datum);

            pstmt.executeUpdate();

            //Statusänderung auch in Bestellung schreiben
            pstmt = cn.prepareStatement(
            "UPDATE `bestellungen` SET statedate = ?, state = ?, orderdate = ? where bid = ?");
            pstmt.setString(1, datum);
            pstmt.setString(2, t.getInhalt());
            pstmt.setString(3, datum);
            pstmt.setLong(4, b.getId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            LOG.error("setNewOrderState(): " + e.toString());
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

    // History Bestellungen eröffnen, Status, Statusdatum (now) sowie Bestelldatum eintragen
    public void changeOrderState(Bestellungen b, String timezone, Text t, String bemerk, String bearb,
            Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("INSERT INTO `bestellstatus` (`BID` , "
                    + "`TID` , `bemerkungen` , `bearbeiter` , `date`) VALUES (?, ?, ?, ?, ?)");
            pstmt.setLong(1, b.getId());
            pstmt.setLong(2, t.getId());
            pstmt.setString(3, bemerk);
            pstmt.setString(4, bearb);

            Date dt = new Date();
            ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datum = sdf.format(dt, timezone);

            pstmt.setString(5, datum);

            pstmt.executeUpdate();

            //Statusänderung auch in Bestellung schreiben
            pstmt = cn.prepareStatement("UPDATE `bestellungen` SET statedate = ?, state = ? where bid = ?");
            pstmt.setString(1, datum);
            pstmt.setString(2, t.getInhalt());
            pstmt.setLong(3, b.getId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            LOG.error("changeOrderState(): " + e.toString());
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

    public List<OrderState> getOrderState(Bestellungen b, Connection cn) {
        ArrayList<OrderState> sl = new ArrayList<OrderState>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM bestellstatus b inner join text t on(b.tid=t.tid) WHERE bid=? "
                    + "ORDER BY date desc");
            pstmt.setLong(1, b.getId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderState bs = new OrderState();
                bs.setDate(b.removeMilliseconds(rs.getString("date")));
                bs.setOrderstate(rs.getString("inhalt"));
                bs.setBemerkungen(rs.getString("bemerkungen"));
                bs.setBearbeiter(rs.getString("bearbeiter"));
                if (checkAnonymizeOrderState(bs)) { bs = anonymizeOrderState(bs); }
                sl.add(bs);
            }

        } catch (Exception e) {
            LOG.error("getOrderState(): " + e.toString());
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
        return sl;
    }

    // History der Bestellungen, Status, Statusdatum (aktuelles) sowie Bestelldatum eintragen
    public void updateOrderState(Bestellungen b, Text t, String bemerk, String bearb, String datum,
            Connection cn) {

        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("INSERT INTO `bestellstatus` (`BID` , "
                    + "`TID` , `bemerkungen` , `bearbeiter` , `date`) VALUES (?, ?, ?, ?, ?)");
            pstmt.setLong(1, b.getId());
            pstmt.setLong(2, t.getId());
            pstmt.setString(3, bemerk);
            pstmt.setString(4, bearb);

            pstmt.setString(5, datum); // muss im Format "yyyy-MM-dd HH:mm:ss" sein!

            pstmt.executeUpdate();

            //Statusänderung auch in Bestellung schreiben
            pstmt = cn.prepareStatement("UPDATE `bestellungen` SET statedate = ?, state = ? where bid = ?");
            pstmt.setString(1, datum);
            pstmt.setString(2, t.getInhalt());
            pstmt.setLong(3, b.getId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            LOG.error("updateOrderState(): " + e.toString());
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
     * Prüft, ob der Bearbeiter in einem Bestellstatus anonymisiert werden muss
     * <p></p>
     * @param OrderState bs
     * @return true/false
     */
    private boolean checkAnonymizeOrderState(OrderState bs) {
        boolean check = false;
        Bestellungen b = new Bestellungen();

        if (bs.getBearbeiter() != null && ReadSystemConfigurations.isAnonymizationActivated()) {
            Calendar cal = b.stringFromMysqlToCal(bs.getDate());
            Calendar limit = Calendar.getInstance();
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
     * Anonymisiert Bestellstati für die Ausgabe
     * <p></p>
     * @param OrderState bs
     * @return OrderState bs
     */
    private OrderState anonymizeOrderState(OrderState bs) {

        if (bs.getBearbeiter() != null && ReadSystemConfigurations.isAnonymizationActivated()) {
            bs.setBearbeiter("anonymized");
        }

        return bs;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderstate() {
        return orderstate;
    }

    public void setOrderstate(String orderstate) {
        this.orderstate = orderstate;
    }

    public String getBearbeiter() {
        return bearbeiter;
    }

    public void setBearbeiter(String bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }




}
