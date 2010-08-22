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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

import ch.dbs.entity.Lieferanten;


public final class FindFree extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String artikeltitel;
    private String zeitschriftentitel;
    private String author;
    private String jahr;
    private String jahrgang;
    private String heft;
    private String seiten;
    private Lieferanten lieferant;
    private String deloptions;

    // wird in Checkavailability gebraucht
    private String link_search; // Suchlink, wird immer ausgegeben (D)
    private String message;  // online
    private String link;  // online
    private String linktitle; // online
    private String e_ampel; // Steuerung Anzeige Ampel
    private String p_ampel; // // Steuerung Anzeige Ampel
    private String message_print;
    private String institution_print;
    private ArrayList<String> location_print;
    private ArrayList<String> shelfmark_print;
    private String link_print;
    private String linktitle_print;
    private String zdb_link; // enthält den Suchlink

    private List<JournalDetails> zeitschriften;


    public FindFree() {

    }


    public String getLink_search() {
        return link_search;
    }


    public void setLink_search(String link_search) {
        this.link_search = link_search;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getZdb_link() {
        return zdb_link;
    }


    public void setZdb_link(String zdb_link) {
        this.zdb_link = zdb_link;
    }


    public String getZeitschriftentitel() {
        return zeitschriftentitel;
    }


    public void setZeitschriftentitel(String zeitschriftentitel) {
        this.zeitschriftentitel = zeitschriftentitel;
    }


    public String getArtikeltitel() {
        return artikeltitel;
    }


    public void setArtikeltitel(String artikeltitel) {
        this.artikeltitel = artikeltitel;
    }

    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public String getHeft() {
        return heft;
    }


    public void setHeft(String heft) {
        this.heft = heft;
    }


    public String getJahr() {
        return jahr;
    }


    public void setJahr(String jahr) {
        this.jahr = jahr;
    }


    public String getJahrgang() {
        return jahrgang;
    }


    public void setJahrgang(String jahrgang) {
        this.jahrgang = jahrgang;
    }


    public String getSeiten() {
        return seiten;
    }


    public void setSeiten(String seiten) {
        this.seiten = seiten;
    }


    public List<JournalDetails> getZeitschriften() {
        return zeitschriften;
    }


    public void setZeitschriften(List<JournalDetails> zeitschriften) {
        this.zeitschriften = zeitschriften;
    }

    public String getLinktitle() {
        return linktitle;
    }


    public void setLinktitle(String linktitle) {
        this.linktitle = linktitle;
    }

    public String getLink_print() {
        return link_print;
    }


    public void setLink_print(String link_print) {
        this.link_print = link_print;
    }


    public String getLinktitle_print() {
        return linktitle_print;
    }


    public void setLinktitle_print(String linktitle_print) {
        this.linktitle_print = linktitle_print;
    }


    public String getMessage_print() {
        return message_print;
    }


    public void setMessage_print(String message_print) {
        this.message_print = message_print;
    }

    public Lieferanten getLieferant() {
        return lieferant;
    }


    public void setLieferant(Lieferanten lieferant) {
        this.lieferant = lieferant;
    }


    public String getDeloptions() {
        return deloptions;
    }


    public void setDeloptions(String deloptions) {
        this.deloptions = deloptions;
    }

    public String getE_ampel() {
        return e_ampel;
    }


    public void setE_ampel(String e_ampel) {
        this.e_ampel = e_ampel;
    }


    public String getP_ampel() {
        return p_ampel;
    }


    public void setP_ampel(String p_ampel) {
        this.p_ampel = p_ampel;
    }

    public String getInstitution_print() {
        return institution_print;
    }


    public void setInstitution_print(String institutionPrint) {
        institution_print = institutionPrint;
    }


    public ArrayList<String> getLocation_print() {
        return location_print;
    }


    public void setLocation_print(ArrayList<String> locationPrint) {
        location_print = locationPrint;
    }


    public ArrayList<String> getShelfmark_print() {
        return shelfmark_print;
    }


    public void setShelfmark_print(ArrayList<String> shelfmarkPrint) {
        shelfmark_print = shelfmarkPrint;
    }

}
