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

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.Bestand;
import ch.dbs.entity.Holding;
import ch.dbs.entity.Text;


/**
 * @author Markus Fischer
 */
public final class HoldingForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private transient Holding holding;
    private String zeitschrift = ""; // u.a. auch ISSN-Assistent
    private String startyear;
    private String startvolume;
    private String startissue;
    private String endyear;
    private String endvolume;
    private String endissue;
    private int suppl; // 0 = keine Supplemente / 1 = inkl. Supplemente / 2 = nur Supplemente
    private boolean eissue;
    private transient Text standort;
    private String standortid;
    private String shelfmark; // Notation, Büchergestellnummer etc.
    private String bemerkungen = "";
    private boolean internal;

    // Ablauftechn. Eigenschaften
    private String submit = "";
    private transient List<Text> standorte;
    private Long stid; // Standort-ID
    private String identifierdescription; // ISSN, ZDB-ID etc.
    private String identifier; // value
    private String message; // dient z.B. der Bestätigung eines erfolgreich abgespeicherten Bestandes
    private boolean morefields; // dient als Kontrollfeld um weitere Felder anzeigen zu lassen, falls Jahr nicht reicht.
    private boolean mod; // Indikator, um einen bestehenden Standort zu ändern
    private boolean del; // Indikator, um einen bestehenden Standort zu löschen
    private boolean standardplace; // um einen Standort für eine Session als default zu setzen


    public HoldingForm() {

    }

    public HoldingForm(final Bestand be) {
        this.setHolding(be.getHolding());
        this.setStartyear(be.getStartyear());
        this.setStartvolume(be.getStartvolume());
        this.setStartissue(be.getStartissue());
        this.setEndyear(be.getEndyear());
        this.setEndvolume(be.getEndvolume());
        this.setEndissue(be.getEndissue());
        this.setSuppl(be.getSuppl());
        this.setEissue(be.isEissue());
        this.setStandort(be.getStandort());
        this.setShelfmark(be.getShelfmark());
        this.setBemerkungen(be.getBemerkungen());
        this.setInternal(be.isInternal());
    }

    /**
     *
     * @param hf
     */
    public HoldingForm(final HoldingForm hf) {
        this.setHolding(hf.getHolding());
        this.setZeitschrift(hf.getZeitschrift());
        this.setStartyear(hf.getStartyear());
        this.setStartvolume(hf.getStartvolume());
        this.setStartissue(hf.getStartissue());
        this.setEndyear(hf.getEndyear());
        this.setEndvolume(hf.getEndvolume());
        this.setEndissue(hf.getEndissue());
        this.setSuppl(hf.getSuppl());
        this.setEissue(hf.isEissue());
        this.setStandort(hf.getStandort());
        this.setShelfmark(hf.getShelfmark());
        this.setBemerkungen(hf.getBemerkungen());
        this.setInternal(hf.isInternal());
        this.setMessage(hf.getMessage());
        this.setMorefields(hf.isMorefields());
        this.setStandorte(hf.getStandorte());
        this.setStandortid(hf.getStandortid());
        this.setMod(hf.isMod());
        this.setDel(hf.isDel());
        this.setStid(hf.getStid());
        this.setStandardplace(hf.isStandardplace());
        this.setSubmit(hf.getSubmit());
    }



    public Holding getHolding() {
        return holding;
    }

    public void setHolding(final Holding holding) {
        this.holding = holding;
    }

    public String getStartvolume() {
        return startvolume;
    }

    public void setStartvolume(final String startvolume) {
        this.startvolume = startvolume;
    }

    public String getStartissue() {
        return startissue;
    }

    public void setStartissue(final String startissue) {
        this.startissue = startissue;
    }

    public String getStartyear() {
        return startyear;
    }

    public void setStartyear(final String startyear) {
        this.startyear = startyear;
    }

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(final String endyear) {
        this.endyear = endyear;
    }

    public String getEndvolume() {
        return endvolume;
    }

    public void setEndvolume(final String endvolume) {
        this.endvolume = endvolume;
    }

    public String getEndissue() {
        return endissue;
    }

    public void setEndissue(final String endissue) {
        this.endissue = endissue;
    }

    public Text getStandort() {
        return standort;
    }

    public void setStandort(final Text standort) {
        this.standort = standort;
    }

    public boolean isEissue() {
        return eissue;
    }

    public void setEissue(final boolean eissue) {
        this.eissue = eissue;
    }

    public String getShelfmark() {
        return shelfmark;
    }

    public void setShelfmark(final String shelfmark) {
        this.shelfmark = shelfmark;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getIdentifierdescription() {
        return identifierdescription;
    }

    public void setIdentifierdescription(final String identifierdescription) {
        this.identifierdescription = identifierdescription;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public int getSuppl() {
        return suppl;
    }

    public void setSuppl(final int suppl) {
        this.suppl = suppl;
    }

    public List<Text> getStandorte() {
        return standorte;
    }

    public void setStandorte(final List<Text> standorte) {
        this.standorte = standorte;
    }

    public String getStandortid() {
        return standortid;
    }

    public void setStandortid(final String standortid) {
        this.standortid = standortid;
    }

    public Long getStid() {
        return stid;
    }

    public void setStid(final Long stid) {
        this.stid = stid;
    }

    public String getZeitschrift() {
        return zeitschrift;
    }

    public void setZeitschrift(final String zeitschrift) {
        this.zeitschrift = zeitschrift;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(final String submit) {
        this.submit = submit;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(final String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public boolean isMorefields() {
        return morefields;
    }

    public void setMorefields(final boolean morefields) {
        this.morefields = morefields;
    }

    public boolean isMod() {
        return mod;
    }

    public void setMod(final boolean mod) {
        this.mod = mod;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(final boolean del) {
        this.del = del;
    }

    public boolean isStandardplace() {
        return standardplace;
    }

    public void setStandardplace(final boolean standardplace) {
        this.standardplace = standardplace;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(final boolean internal) {
        this.internal = internal;
    }

}
