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

package ch.ddl.form;

import java.sql.Date;

import org.apache.struts.action.ActionForm;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Konto;
import ch.dbs.form.OrderForm;
import ch.dbs.interf.OrderHandler;
import ch.ddl.entity.Position;

/**
 * PositionForm
 *
 * @author Pascal Steiner
 *
 */
public final class PositionForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private AbstractBenutzer benutzer; // Endkunde / Bibliothekskunde
    private Konto konto; // Bibliothekskonto

    // Lieferinfos
    private String priority = ""; // Normal, Express
    private String deloptions = ""; // Online, Email, Postweg, Fax, Fax to PDF
    private String fileformat = ""; // HTML, PDF, Papierkopie,...
    private Date orderdate; // Datum der Bestellung

    // Produktinfos
    private String mediatype = ""; // Artikel, Teilkopie Buch oder Buch
    private String autor = "";
    private String zeitschrift_verlag = ""; // Zeitschrift / Verlag
    private String heft = ""; //
    private String jahrgang = "";
    private String jahr = "";
    private String titel = ""; // Artikeltiel / Buchtitel
    private String kapitel = "";
    private String seiten = "";

    // Preis
    private String waehrung = "";
    private String preis = "";

    /**
     * New PositionForm
     * @author Pascal Steiner
     */
    public PositionForm() {
        super();
    }

    /**
     * New PositionForm from a Position     *
     * @param p Position
     * @author Pascal Steiner
     */
    public PositionForm(final Position p) {
        super();
        fill(p);
    }

    /**
     * New PositionForm from a OrderForm     *
     * @param p Position
     * @author Pascal Steiner
     */
    public PositionForm(final OrderForm of) {
        super();
        fill(of);

    }

    /**
     * kopiert die Werte eins OrderHandler (Konkret: Position oder Bestellung) in das Form
     * @param p
     * @author Pascal Steiner
     */
    private void fill(final OrderHandler oh) {
        this.setBenutzer(oh.getBenutzer());
        this.setKonto(oh.getKonto());
        this.setPriority(oh.getPriority());
        this.setDeloptions(oh.getDeloptions());
        this.setFileformat(oh.getFileformat());
        this.setOrderdate(oh.getOrderdate());
        this.setMediatype(oh.getMediatype());
        this.setAutor(oh.getAutor());
        this.setZeitschrift_verlag(oh.getZeitschrift_verlag());
        this.setHeft(oh.getHeft());
        this.setJahrgang(oh.getJahrgang());
        this.setJahr(oh.getJahr());
        this.setTitel(oh.getTitel());
        this.setKapitel(oh.getKapitel());
        this.setSeiten(oh.getSeiten());
        this.setWaehrung(oh.getWaehrung());
        this.setPreis(oh.getPreis());
    }

    public AbstractBenutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(final AbstractBenutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(final Konto konto) {
        this.konto = konto;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getDeloptions() {
        return deloptions;
    }

    public void setDeloptions(final String deloptions) {
        this.deloptions = deloptions;
    }

    public String getFileformat() {
        return fileformat;
    }

    public void setFileformat(final String fileformat) {
        this.fileformat = fileformat;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(final Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(final String mediatype) {
        this.mediatype = mediatype;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(final String autor) {
        this.autor = autor;
    }

    public String getZeitschrift_verlag() {
        return zeitschrift_verlag;
    }

    public void setZeitschrift_verlag(final String zeitschrift_verlag) {
        this.zeitschrift_verlag = zeitschrift_verlag;
    }

    public String getHeft() {
        return heft;
    }

    public void setHeft(final String heft) {
        this.heft = heft;
    }

    public String getJahrgang() {
        return jahrgang;
    }

    public void setJahrgang(final String jahrgang) {
        this.jahrgang = jahrgang;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(final String titel) {
        this.titel = titel;
    }

    public String getKapitel() {
        return kapitel;
    }

    public void setKapitel(final String kapitel) {
        this.kapitel = kapitel;
    }

    public String getSeiten() {
        return seiten;
    }

    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }

    public String getPreis() {
        return preis;
    }

    public void setPreis(final String preis) {
        this.preis = preis;
    }

}
