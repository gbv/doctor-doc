//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package ch.dbs.statistik;

import ch.dbs.form.OrderStatistikForm;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them. 
 * <p/>
 * @author Markus Fischer
 */
public class Statistik  {
	
	private OrderStatistikForm kontoordersstat; // Total aller Kontobestellungen
	private OrderStatistikForm lieferantstat; // Anzahl pro Lieferant
	private OrderStatistikForm gratis_kosten; // Anzahl pro Kostenart
	private OrderStatistikForm sum_gratis_kosten; // Kosten pro Kostenart
	private OrderStatistikForm lieferartstat; // Anzahl pro Lieferart
	private OrderStatistikForm mediatype; // Anzahl pro Medientyp
	private OrderStatistikForm fileformatstat; // Anzahl pro Fileformat
	private OrderStatistikForm prioritystat; // Anzahl nach Priorität
	private OrderStatistikForm statusstat; // Anzahl nach Status
	private OrderStatistikForm lieferzeitstat; // durchschnittliche Lieferzeit
	private OrderStatistikForm zeitschriftstat; // Anzahl Bestellungen pro Zeitschrift
	private OrderStatistikForm jahrstat; // Anzahl pro Erscheinungsjahr
	private OrderStatistikForm totuserwithordersstat; // Total Kunden mit Bestellungen
	private OrderStatistikForm ordersperuserstat; // durchschnittliche Anzahl pro Kunde
	private OrderStatistikForm genderstat; // Anzahl pro Gender
	private OrderStatistikForm institutionstat; // Anzahl pro Institution
	private OrderStatistikForm abteilungstat; // Anzahl pro Abteilung
	private OrderStatistikForm ortstat; // Anzahl pro Ort
	private OrderStatistikForm landstat; // Anzahl pro Land
	private OrderStatistikForm bibliothekarordersstat; // Anzahl Bestellungen durch Bibliothekare
	private OrderStatistikForm kundenordersstat; // Anzahl Bestellungen durch Kunden

  
  public Statistik() { }


public OrderStatistikForm getAbteilungstat() {
	return abteilungstat;
}


public void setAbteilungstat(OrderStatistikForm abteilungstat) {
	this.abteilungstat = abteilungstat;
}


public OrderStatistikForm getBibliothekarordersstat() {
	return bibliothekarordersstat;
}


public void setBibliothekarordersstat(OrderStatistikForm bibliothekarordersstat) {
	this.bibliothekarordersstat = bibliothekarordersstat;
}

public OrderStatistikForm getMediatype() {
	return mediatype;
}


public void setMediatype(OrderStatistikForm mediatype) {
	this.mediatype = mediatype;
}


public OrderStatistikForm getFileformatstat() {
	return fileformatstat;
}


public void setFileformatstat(OrderStatistikForm fileformatstat) {
	this.fileformatstat = fileformatstat;
}


public OrderStatistikForm getGenderstat() {
	return genderstat;
}


public void setGenderstat(OrderStatistikForm genderstat) {
	this.genderstat = genderstat;
}


public OrderStatistikForm getGratis_kosten() {
	return gratis_kosten;
}


public void setGratis_kosten(OrderStatistikForm gratis_kosten) {
	this.gratis_kosten = gratis_kosten;
}


public OrderStatistikForm getSum_gratis_kosten() {
	return sum_gratis_kosten;
}


public void setSum_gratis_kosten(OrderStatistikForm sum_gratis_kosten) {
	this.sum_gratis_kosten = sum_gratis_kosten;
}


public OrderStatistikForm getInstitutionstat() {
	return institutionstat;
}


public void setInstitutionstat(OrderStatistikForm institutionstat) {
	this.institutionstat = institutionstat;
}


public OrderStatistikForm getJahrstat() {
	return jahrstat;
}


public void setJahrstat(OrderStatistikForm jahrstat) {
	this.jahrstat = jahrstat;
}


public OrderStatistikForm getKontoordersstat() {
	return kontoordersstat;
}


public void setKontoordersstat(OrderStatistikForm kontoordersstat) {
	this.kontoordersstat = kontoordersstat;
}


public OrderStatistikForm getKundenordersstat() {
	return kundenordersstat;
}


public void setKundenordersstat(OrderStatistikForm kundenordersstat) {
	this.kundenordersstat = kundenordersstat;
}


public OrderStatistikForm getLandstat() {
	return landstat;
}


public void setLandstat(OrderStatistikForm landstat) {
	this.landstat = landstat;
}


public OrderStatistikForm getLieferantstat() {
	return lieferantstat;
}


public void setLieferantstat(OrderStatistikForm lieferantstat) {
	this.lieferantstat = lieferantstat;
}


public OrderStatistikForm getLieferartstat() {
	return lieferartstat;
}


public void setLieferartstat(OrderStatistikForm lieferartstat) {
	this.lieferartstat = lieferartstat;
}


public OrderStatistikForm getLieferzeitstat() {
	return lieferzeitstat;
}

public void setLieferzeitstat(OrderStatistikForm lieferzeitstat) {
	this.lieferzeitstat = lieferzeitstat;
}


public OrderStatistikForm getOrdersperuserstat() {
	return ordersperuserstat;
}


public void setOrdersperuserstat(OrderStatistikForm ordersperuserstat) {
	this.ordersperuserstat = ordersperuserstat;
}


public OrderStatistikForm getOrtstat() {
	return ortstat;
}


public void setOrtstat(OrderStatistikForm ortstat) {
	this.ortstat = ortstat;
}


public OrderStatistikForm getPrioritystat() {
	return prioritystat;
}


public void setPrioritystat(OrderStatistikForm prioritystat) {
	this.prioritystat = prioritystat;
}


public OrderStatistikForm getStatusstat() {
	return statusstat;
}


public void setStatusstat(OrderStatistikForm statusstat) {
	this.statusstat = statusstat;
}


public OrderStatistikForm getTotuserwithordersstat() {
	return totuserwithordersstat;
}


public void setTotuserwithordersstat(OrderStatistikForm totuserwithordersstat) {
	this.totuserwithordersstat = totuserwithordersstat;
}


public OrderStatistikForm getZeitschriftstat() {
	return zeitschriftstat;
}


public void setZeitschriftstat(OrderStatistikForm zeitschriftstat) {
	this.zeitschriftstat = zeitschriftstat;
}


  
}
