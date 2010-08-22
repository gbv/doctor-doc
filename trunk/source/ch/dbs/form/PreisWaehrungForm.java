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

import org.apache.struts.action.ActionForm;


public final class PreisWaehrungForm extends ActionForm {

  private static final long serialVersionUID = 1L;
  private PreisWaehrungForm eur;
  private PreisWaehrungForm chf;
  private PreisWaehrungForm gbp;
  private PreisWaehrungForm usd;

  private String preis;
    private String waehrung;


    public PreisWaehrungForm() {

    }



  public PreisWaehrungForm getEur() {
    return eur;
  }

  public void setEur(PreisWaehrungForm eur) {
    this.eur = eur;
  }

  public PreisWaehrungForm getChf() {
    return chf;
  }

  public void setChf(PreisWaehrungForm chf) {
    this.chf = chf;
  }

  public PreisWaehrungForm getGbp() {
    return gbp;
  }

  public void setGbp(PreisWaehrungForm gbp) {
    this.gbp = gbp;
  }

  public PreisWaehrungForm getUsd() {
    return usd;
  }

  public void setUsd(PreisWaehrungForm usd) {
    this.usd = usd;
  }

  public String getPreis() {
    return preis;
  }
  public void setPreis(String preis) {
    this.preis = preis;
  }
  public String getWaehrung() {
    return waehrung;
  }
  public void setWaehrung(String waehrung) {
    this.waehrung = waehrung;
  }

}
