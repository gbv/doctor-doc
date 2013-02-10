//  Copyright (C) 2012  Markus Fischer, Pascal Steiner
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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.struts.validator.ValidatorForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.entity.Konto;
import ch.dbs.entity.Lieferanten;

public final class SupplierForm extends ValidatorForm {
    
    final Logger LOG = LoggerFactory.getLogger(SupplierForm.class);
    private static final long serialVersionUID = 1L;
    
    private Long lid;
    private Long kid;
    private String sigel;
    private String name;
    private String emailILL;
    private String countryCode;
    private boolean land_allgemein;
    
    // additional parameters
    private boolean individual; // used for checkbox in UI
    private boolean showprivsuppliers;
    private boolean showpubsuppliers;
    private boolean changedsettings; // hidden value in form to indicate we have changed settings
    
    public SupplierForm() {
        
    }
    
    public SupplierForm(final Lieferanten l) {
        this.setLid(l.getLid());
        this.setKid(l.getKid());
        this.setSigel(l.getSigel());
        this.setName(l.getName());
        this.setEmailILL(l.getEmailILL());
        this.setCountryCode(l.getCountryCode());
        this.setLand_allgemein(l.isLand_allgemein());
        if (l.getKid() != null) {
            this.setIndividual(true);
        }
    }
    
    public void updateAccount(final SupplierForm sf, final Konto konto, final Connection cn) {
        
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `konto` SET `showprivsuppliers` = ?, "
                    + "`showpubsuppliers` = ? WHERE `konto`.`KID` = ?");
            pstmt.setBoolean(1, sf.isShowprivsuppliers());
            pstmt.setBoolean(2, sf.isShowpubsuppliers());
            pstmt.setLong(3, konto.getId());
            pstmt.executeUpdate();
            
        } catch (final Exception e) {
            LOG.error("updateAccount(final SupplierForm sf, final Konto konto, final Connection cn): " + e.toString());
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
    
    public Long getLid() {
        return lid;
    }
    
    public void setLid(final Long lid) {
        this.lid = lid;
    }
    
    public Long getKid() {
        return kid;
    }
    
    public void setKid(final Long kid) {
        this.kid = kid;
    }
    
    public String getSigel() {
        return sigel;
    }
    
    public void setSigel(final String sigel) {
        this.sigel = sigel;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getEmailILL() {
        return emailILL;
    }
    
    public void setEmailILL(final String emailILL) {
        this.emailILL = emailILL;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }
    
    public boolean isLand_allgemein() {
        return land_allgemein;
    }
    
    public void setLand_allgemein(final boolean land_allgemein) {
        this.land_allgemein = land_allgemein;
    }
    
    public boolean isIndividual() {
        return individual;
    }
    
    public void setIndividual(final boolean individual) {
        this.individual = individual;
    }
    
    public boolean isShowprivsuppliers() {
        return showprivsuppliers;
    }
    
    public void setShowprivsuppliers(final boolean showprivsuppliers) {
        this.showprivsuppliers = showprivsuppliers;
    }
    
    public boolean isShowpubsuppliers() {
        return showpubsuppliers;
    }
    
    public void setShowpubsuppliers(final boolean showpubsuppliers) {
        this.showpubsuppliers = showpubsuppliers;
    }
    
    public boolean isChangedsettings() {
        return changedsettings;
    }
    
    public void setChangedsettings(final boolean changedsettings) {
        this.changedsettings = changedsettings;
    }
    
}
