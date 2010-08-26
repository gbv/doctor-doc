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

package ch.dbs.form;

import java.sql.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Billing;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;

/**
 * BillingForm
 *
 * @author Pascal Steiner
 *
 */
public final class BillingForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private Long billid;
    private Long userid;
    private AbstractBenutzer user;
    private Billing bill;
    private List<Billing> billings;
    private Konto konto;
    private Long kontoid;
    private Text rechnungsgrund;
    private List<Text> rechnungsgrundliste;
    private String rechnungsgrundstring = "";
    private Long rechnungsgrundid;
    private double betrag;
    private String waehrung;
    private String rechnungsnummer;
    private Date rechnungsdatum;
    private String rechnungsdatestring;
    private Date zahlungseingang;
    private String zahlungseingangstring;
    private boolean storniert;
    private String billingtext = "";
    private String manuelltext = "";


    public double getBetrag() {
        return betrag;
    }
    public void setBetrag(final double betrag) {
        this.betrag = betrag;
    }
    public Konto getKonto() {
        return konto;
    }
    public void setKonto(final Konto konto) {
        this.konto = konto;
    }
    public Long getKontoid() {
        return kontoid;
    }
    public void setKontoid(final Long kontoid) {
        this.kontoid = kontoid;
    }
    public String getRechnungsdatestring() {
        return rechnungsdatestring;
    }
    public void setRechnungsdatestring(final String rechnungsdatestring) {
        this.rechnungsdatestring = rechnungsdatestring;
    }
    public Date getRechnungsdatum() {
        return rechnungsdatum;
    }
    public void setRechnungsdatum(final Date rechnungsdatum) {
        this.rechnungsdatum = rechnungsdatum;
    }
    public Long getRechnungsgrundid() {
        return rechnungsgrundid;
    }
    public void setRechnungsgrundid(final Long rechnungsgrundid) {
        this.rechnungsgrundid = rechnungsgrundid;
    }
    public Text getRechnungsgrund() {
        return rechnungsgrund;
    }
    public void setRechnungsgrund(final Text rechnungsgrund) {
        this.rechnungsgrund = rechnungsgrund;
    }
    public String getRechnungsnummer() {
        return rechnungsnummer;
    }
    public void setRechnungsnummer(final String rechnungsnummer) {
        this.rechnungsnummer = rechnungsnummer;
    }
    public AbstractBenutzer getUser() {
        return user;
    }
    public void setUser(final AbstractBenutzer user) {
        this.user = user;
    }
    public Long getUserid() {
        return userid;
    }
    public void setUserid(final Long userid) {
        this.userid = userid;
    }
    public Date getZahlungseingang() {
        return zahlungseingang;
    }
    public void setZahlungseingang(final Date zahlungseingang) {
        this.zahlungseingang = zahlungseingang;
    }
    public String getZahlungseingangstring() {
        return zahlungseingangstring;
    }
    public void setZahlungseingangstring(final String zahlungseingangstring) {
        this.zahlungseingangstring = zahlungseingangstring;
    }
    public String getWaehrung() {
        return waehrung;
    }
    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }
    public boolean isStorniert() {
        return storniert;
    }
    public void setStorniert(final boolean storniert) {
        this.storniert = storniert;
    }
    public String getBillingtext() {
        return billingtext;
    }
    public void setBillingtext(final String billingtext) {
        this.billingtext = billingtext;
    }
    public Billing getBill() {
        return bill;
    }
    public void setBill(final Billing bill) {
        this.bill = bill;
    }
    public List<Billing> getBillings() {
        return billings;
    }
    public void setBillings(final List<Billing> billings) {
        this.billings = billings;
    }
    public Long getBillid() {
        return billid;
    }
    public void setBillid(final Long billid) {
        this.billid = billid;
    }
    public String getRechnungsgrundstring() {
        return rechnungsgrundstring;
    }
    public void setRechnungsgrundstring(final String rechnungsgrundstring) {
        this.rechnungsgrundstring = rechnungsgrundstring;
    }
    public String getManuelltext() {
        return manuelltext;
    }
    public void setManuelltext(final String manuelltext) {
        this.manuelltext = manuelltext;
    }
    public List<Text> getRechnungsgrundliste() {
        return rechnungsgrundliste;
    }
    public void setRechnungsgrundliste(final List<Text> rechnungsgrundliste) {
        this.rechnungsgrundliste = rechnungsgrundliste;
    }



}
