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

package ch.dbs.statistik;

import ch.dbs.form.OrderStatistikForm;

/**
 * Abstract base class for entities having a {@link Long} unique
 * identifier, this provides the base functionality for them.
 * <p/>
 * @author Markus Fischer
 */
public class Statistik  {

    private OrderStatistikForm kontoordersstat; // total of all orders from an account
    private OrderStatistikForm lieferantstat; // per supplier
    private OrderStatistikForm gratis_kosten; // free / payable
    private OrderStatistikForm sum_gratis_kosten; // cost of free / payable
    private OrderStatistikForm lieferartstat; // per deliveryway
    private OrderStatistikForm mediatype; // per mediatype
    private OrderStatistikForm fileformatstat; // per fileformat
    private OrderStatistikForm prioritystat; // per priority
    private OrderStatistikForm zeitschriftstat; // number of orders per journal
    private OrderStatistikForm jahrstat; // per publication year
    private OrderStatistikForm totuserwithordersstat; // total users with orders
    private OrderStatistikForm genderstat; // per gender
    private OrderStatistikForm institutionstat; // per institution
    private OrderStatistikForm abteilungstat; // per department
    private OrderStatistikForm categorystat; // per category
    private OrderStatistikForm ortstat; // per place
    private OrderStatistikForm landstat; // per country


    public OrderStatistikForm getAbteilungstat() {
        return abteilungstat;
    }


    public void setAbteilungstat(final OrderStatistikForm abteilungstat) {
        this.abteilungstat = abteilungstat;
    }

    public OrderStatistikForm getMediatype() {
        return mediatype;
    }


    public void setMediatype(final OrderStatistikForm mediatype) {
        this.mediatype = mediatype;
    }


    public OrderStatistikForm getFileformatstat() {
        return fileformatstat;
    }


    public void setFileformatstat(final OrderStatistikForm fileformatstat) {
        this.fileformatstat = fileformatstat;
    }


    public OrderStatistikForm getGenderstat() {
        return genderstat;
    }


    public void setGenderstat(final OrderStatistikForm genderstat) {
        this.genderstat = genderstat;
    }


    public OrderStatistikForm getGratis_kosten() {
        return gratis_kosten;
    }


    public void setGratis_kosten(final OrderStatistikForm gratis_kosten) {
        this.gratis_kosten = gratis_kosten;
    }


    public OrderStatistikForm getSum_gratis_kosten() {
        return sum_gratis_kosten;
    }


    public void setSum_gratis_kosten(final OrderStatistikForm sum_gratis_kosten) {
        this.sum_gratis_kosten = sum_gratis_kosten;
    }


    public OrderStatistikForm getInstitutionstat() {
        return institutionstat;
    }


    public void setInstitutionstat(final OrderStatistikForm institutionstat) {
        this.institutionstat = institutionstat;
    }


    public OrderStatistikForm getJahrstat() {
        return jahrstat;
    }


    public void setJahrstat(final OrderStatistikForm jahrstat) {
        this.jahrstat = jahrstat;
    }


    public OrderStatistikForm getKontoordersstat() {
        return kontoordersstat;
    }


    public void setKontoordersstat(final OrderStatistikForm kontoordersstat) {
        this.kontoordersstat = kontoordersstat;
    }


    public OrderStatistikForm getLandstat() {
        return landstat;
    }


    public void setLandstat(final OrderStatistikForm landstat) {
        this.landstat = landstat;
    }


    public OrderStatistikForm getLieferantstat() {
        return lieferantstat;
    }


    public void setLieferantstat(final OrderStatistikForm lieferantstat) {
        this.lieferantstat = lieferantstat;
    }


    public OrderStatistikForm getLieferartstat() {
        return lieferartstat;
    }


    public void setLieferartstat(final OrderStatistikForm lieferartstat) {
        this.lieferartstat = lieferartstat;
    }


    public OrderStatistikForm getOrtstat() {
        return ortstat;
    }


    public void setOrtstat(final OrderStatistikForm ortstat) {
        this.ortstat = ortstat;
    }


    public OrderStatistikForm getPrioritystat() {
        return prioritystat;
    }


    public void setPrioritystat(final OrderStatistikForm prioritystat) {
        this.prioritystat = prioritystat;
    }


    public OrderStatistikForm getTotuserwithordersstat() {
        return totuserwithordersstat;
    }


    public void setTotuserwithordersstat(final OrderStatistikForm totuserwithordersstat) {
        this.totuserwithordersstat = totuserwithordersstat;
    }


    public OrderStatistikForm getZeitschriftstat() {
        return zeitschriftstat;
    }


    public void setZeitschriftstat(final OrderStatistikForm zeitschriftstat) {
        this.zeitschriftstat = zeitschriftstat;
    }

    public OrderStatistikForm getCategorystat() {
        return categorystat;
    }


    public void setCategorystat(final OrderStatistikForm categorystat) {
        this.categorystat = categorystat;
    }



}
