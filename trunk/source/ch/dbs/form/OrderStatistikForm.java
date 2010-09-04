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


public final class OrderStatistikForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private List<OrderStatistikForm> auflistung;
    private List<OrderStatistikForm> statistik;
    private PreisWaehrungForm preiswaehrung;
    private String label;
    private int anzahl;
    private String preis;
    private int total;
    private String label_two;
    private int anzahl_two;
    private String preis_two;
    private int total_two;
    private String konto;


    public List<OrderStatistikForm> getStatistik() {
        return statistik;
    }


    public void setStatistik(final List<OrderStatistikForm> statistik) {
        this.statistik = statistik;
    }

    public PreisWaehrungForm getPreiswaehrung() {
        return preiswaehrung;
    }


    public void setPreiswaehrung(final PreisWaehrungForm preiswaehrung) {
        this.preiswaehrung = preiswaehrung;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getLabel_two() {
        return label_two;
    }


    public void setLabel_two(final String label_two) {
        this.label_two = label_two;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(final int anzahl) {
        this.anzahl = anzahl;
    }

    public List<OrderStatistikForm> getAuflistung() {
        return auflistung;
    }

    public void setAuflistung(final List<OrderStatistikForm> auflistung) {
        this.auflistung = auflistung;
    }

    public String getKonto() {
        return konto;
    }

    public void setKonto(final String kono) {
        this.konto = kono;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public String getPreis() {
        return preis;
    }


    public void setPreis(final String preis) {
        this.preis = preis;
    }


    public String getPreis_two() {
        return preis_two;
    }


    public void setPreis_two(final String preis_two) {
        this.preis_two = preis_two;
    }


    public int getAnzahl_two() {
        return anzahl_two;
    }


    public void setAnzahl_two(final int anzahl_two) {
        this.anzahl_two = anzahl_two;
    }

    public int getTotal_two() {
        return total_two;
    }


    public void setTotal_two(final int total_two) {
        this.total_two = total_two;
    }

}
