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
  private Holding holding;
  private String zeitschrift = ""; // u.a. auch ISSN-Assistent
  private String startyear;
  private String startvolume;
  private String startissue;
  private String endyear;
  private String endvolume;
  private String endissue;
  private int suppl; // 0 = keine Supplemente / 1 = inkl. Supplemente / 2 = nur Supplemente
  private boolean eissue;
  private Text standort;
  private String standortid;
  private String shelfmark; // Notation, Büchergestellnummer etc.
  private String bemerkungen = "";
  private boolean internal;

  // Ablauftechn. Eigenschaften
  private String submit = "";
  private List<Text> standorte;
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

    public HoldingForm(Bestand be) {
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
    public HoldingForm(HoldingForm hf) {
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

  public void setHolding(Holding holding) {
    this.holding = holding;
  }

  public String getStartvolume() {
    return startvolume;
  }

  public void setStartvolume(String startvolume) {
    this.startvolume = startvolume;
  }

  public String getStartissue() {
    return startissue;
  }

  public void setStartissue(String startissue) {
    this.startissue = startissue;
  }

  public String getStartyear() {
    return startyear;
  }

  public void setStartyear(String startyear) {
    this.startyear = startyear;
  }

  public String getEndyear() {
    return endyear;
  }

  public void setEndyear(String endyear) {
    this.endyear = endyear;
  }

  public String getEndvolume() {
    return endvolume;
  }

  public void setEndvolume(String endvolume) {
    this.endvolume = endvolume;
  }

  public String getEndissue() {
    return endissue;
  }

  public void setEndissue(String endissue) {
    this.endissue = endissue;
  }

  public Text getStandort() {
    return standort;
  }

  public void setStandort(Text standort) {
    this.standort = standort;
  }

  public boolean isEissue() {
    return eissue;
  }

  public void setEissue(boolean eissue) {
    this.eissue = eissue;
  }

  public String getShelfmark() {
    return shelfmark;
  }

  public void setShelfmark(String shelfmark) {
    this.shelfmark = shelfmark;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getIdentifierdescription() {
    return identifierdescription;
  }

  public void setIdentifierdescription(String identifierdescription) {
    this.identifierdescription = identifierdescription;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public int getSuppl() {
    return suppl;
  }

  public void setSuppl(int suppl) {
    this.suppl = suppl;
  }

  public List<Text> getStandorte() {
    return standorte;
  }

  public void setStandorte(List<Text> standorte) {
    this.standorte = standorte;
  }

  public String getStandortid() {
    return standortid;
  }

  public void setStandortid(String standortid) {
    this.standortid = standortid;
  }

  public Long getStid() {
    return stid;
  }

  public void setStid(Long stid) {
    this.stid = stid;
  }

  public String getZeitschrift() {
    return zeitschrift;
  }

  public void setZeitschrift(String zeitschrift) {
    this.zeitschrift = zeitschrift;
  }

  public String getSubmit() {
    return submit;
  }

  public void setSubmit(String submit) {
    this.submit = submit;
  }

  public String getBemerkungen() {
    return bemerkungen;
  }

  public void setBemerkungen(String bemerkungen) {
    this.bemerkungen = bemerkungen;
  }

  public boolean isMorefields() {
    return morefields;
  }

  public void setMorefields(boolean morefields) {
    this.morefields = morefields;
  }

  public boolean isMod() {
    return mod;
  }

  public void setMod(boolean mod) {
    this.mod = mod;
  }

  public boolean isDel() {
    return del;
  }

  public void setDel(boolean del) {
    this.del = del;
  }

  public boolean isStandardplace() {
    return standardplace;
  }

  public void setStandardplace(boolean standardplace) {
    this.standardplace = standardplace;
  }

  public boolean isInternal() {
    return internal;
  }

  public void setInternal(boolean internal) {
    this.internal = internal;
  }

}
