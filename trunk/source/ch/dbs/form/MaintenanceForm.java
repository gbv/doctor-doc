//  Copyright (C) 2012  Markus Fischer
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

package ch.dbs.form;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.struts.action.ActionForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MaintenanceForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    final Logger LOG = LoggerFactory.getLogger(MaintenanceForm.class);
    
    private boolean confirmed;
    private int months;
    private int numerOfRecords;
    private String method;
    
    public MaintenanceForm() {
        
    }
    
    public int countDeleteOrders(final UserInfo ui, final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT COUNT(*) FROM bestellungen WHERE KID=? AND `orderdate` < ?");
            
            pstmt.setLong(1, ui.getKonto().getId());
            pstmt.setDate(2, calcDate());
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                result = rs.getInt("COUNT(*)");
            }
            
        } catch (final Exception e) {
            LOG.error("countDeleteOrders(final UserInfo ui, final Connection cn): " + e.toString());
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
    
    public int deleteOrders(final UserInfo ui, final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("DELETE FROM bestellungen WHERE KID=? AND `orderdate` < ?");
            
            pstmt.setLong(1, ui.getKonto().getId());
            pstmt.setDate(2, calcDate());
            
            result = pstmt.executeUpdate();
            
            // delete all stati without orders
            deleteStatiNoOrders(cn);
            
        } catch (final Exception e) {
            LOG.error("deleteOrders(final UserInfo ui, final Connection cn): " + e.toString());
        } finally {
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
    
    public int countDeleteUserNoOrders(final UserInfo ui, final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn
                    .prepareStatement("SELECT COUNT(*) FROM benutzer c JOIN v_konto_benutzer v ON c.UID=v.UID WHERE v.KID=? AND c.rechte=1 "
                            + "AND NOT EXISTS (SELECT 1 FROM bestellungen o WHERE o.UID = c.UID AND o.`orderdate` > ?)");
            
            pstmt.setLong(1, ui.getKonto().getId());
            pstmt.setDate(2, calcDate());
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                result = rs.getInt("COUNT(*)");
            }
            
        } catch (final Exception e) {
            LOG.error("countDeleteUserNoOrders(final UserInfo ui, final Connection cn): " + e.toString());
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
    
    public int deleteUserNoOrders(final UserInfo ui, final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn
                    .prepareStatement("DELETE c.* FROM benutzer c JOIN v_konto_benutzer v ON c.UID=v.UID WHERE v.KID=? AND c.rechte=1 "
                            + "AND NOT EXISTS (SELECT 1 FROM bestellungen o WHERE o.UID = c.UID AND o.`orderdate` > ?)");
            
            pstmt.setLong(1, ui.getKonto().getId());
            pstmt.setDate(2, calcDate());
            
            result = pstmt.executeUpdate();
            
            // delete all orders without user
            deleteOrdersNouser(ui, cn);
            
            // delete all v_konto_benutzer without user
            deleteVKontoBenutzerWhereNoUser(cn);
            
        } catch (final Exception e) {
            LOG.error("deleteUserNoOrders(final UserInfo ui, final Connection cn): " + e.toString());
        } finally {
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
    
    private int deleteOrdersNouser(final UserInfo ui, final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn
                    .prepareStatement("DELETE b.* FROM `bestellungen` AS b LEFT JOIN `v_konto_benutzer` AS v ON b.UID=v.UID WHERE b.KID=? AND v.UID IS NULL");
            
            pstmt.setLong(1, ui.getKonto().getId());
            
            result = pstmt.executeUpdate();
            
            // delete all stati without orders
            deleteStatiNoOrders(cn);
            
        } catch (final Exception e) {
            LOG.error("deleteOrdersNouser(final UserInfo ui, final Connection cn): " + e.toString());
        } finally {
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
     * Deletes all stati with no orders from ALL accounts!
     */
    private int deleteStatiNoOrders(final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn
                    .prepareStatement("DELETE s.* FROM `bestellstatus` AS s  LEFT JOIN `bestellungen` AS b ON s.BID=b.BID WHERE b.BID IS NULL");
            
            result = pstmt.executeUpdate();
            
        } catch (final Exception e) {
            LOG.error("deleteStatiNoOrders(final Connection cn): " + e.toString());
        } finally {
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
     * Deletes all v_konto_benutzer with no users from ALL accounts!
     */
    private int deleteVKontoBenutzerWhereNoUser(final Connection cn) {
        
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = cn
                    .prepareStatement("DELETE v.* FROM `v_konto_benutzer` AS v LEFT JOIN `benutzer` AS b ON v.UID=b.UID WHERE b.UID IS NULL");
            
            result = pstmt.executeUpdate();
            
        } catch (final Exception e) {
            LOG.error("deleteVKontoBenutzerWhereNoUser(final Connection cn): " + e.toString());
        } finally {
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
    
    private Date calcDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -this.getMonths());
        
        final java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
        
        return sqlDate;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setConfirmed(final boolean confirmed) {
        this.confirmed = confirmed;
    }
    
    public int getMonths() {
        return months;
    }
    
    public void setMonths(final int months) {
        this.months = months;
    }
    
    public int getNumerOfRecords() {
        return numerOfRecords;
    }
    
    public void setNumerOfRecords(final int numerOfRecords) {
        this.numerOfRecords = numerOfRecords;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(final String method) {
        this.method = method;
    }
    
}
