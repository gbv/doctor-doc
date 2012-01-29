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

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.Lieferanten;


public final class SupplierForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    private Long lid;
    private Long kid;
    private String sigel;
    private String name;
    private String emailILL;
    private String countryCode;
    private boolean land_allgemein;



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

}
