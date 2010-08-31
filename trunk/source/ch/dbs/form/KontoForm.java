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

import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;
import org.grlea.log.SimpleLogger;

import util.ReadSystemConfigurations;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;

public final class KontoForm extends ValidatorForm {

    private static final SimpleLogger LOG = new SimpleLogger(KontoForm.class);

    private static final long serialVersionUID = 1L;
    private Long kid;
    private transient Konto konto;
    private List<Konto> kontos; // wird für Auswahl Admin - Kontoverwaltung gebraucht
    private String kontoselect; // Kontoauswahl für Admin prepareModifyKonto
    private String biblioname;
    private String isil; // International Standard Identifier for Libraries and Related Organizations
    private String Adresse;
    private String Adressenzusatz;
    private String PLZ;
    private String Ort;
    private String Land;
    private String timezone = ReadSystemConfigurations.getSystemTimezone();
    private List<Countries> countries;
    private String countryid;
    private String faxno; // DD-Faxservernummer, nur durch Admin editierbar!
    private String faxusername;
    private String faxpassword;
    private String popfaxend;
    private String fax_extern; // externe Faxnummer, editierbar durch Kunde
    private String Telefon;
    private String Bibliotheksmail; // Bibliothekskontakt
    private String dbsmail; // This is the email that receives ILL deliveries
    private String dbsmailpw;
    private String gbvbenutzername;
    private String gbvpasswort;
    private String gbvrequesterid; // ID jeder einzelnen Bibliothek beim GBV
    private String ezbid;
    private String instlogolink; // Link to a logo/image on a remote server
    private boolean zdb;
    private transient Text billing;
    private transient Text billingtype;
    private int accounting_rhythmvalue;
    private int accounting_rhythmday;
    private int accounting_rhythmtimeout;
    private int threshold_value; /* Verrechnungsschwellwert Sammelrechnungen in Tagen */
    private int maxordersu; // Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
    private int maxordersutotal; // Begrenzung mglicher Bestellungen durch einen Benutzer pro Kalenderjahr
    private int maxordersj; // Legt die maximale Artikelanzahl eines Kontos pro Kalenderjahr fest
    private int orderlimits;
    private boolean userlogin; // Dürfen sich "Nichtbibliothekare" einloggen
    private boolean userbestellung; // Dürfen "Nichtbibliothekare" bei SUBITO Bestellungen tätigen?
    private boolean gbvbestellung; // Dürfen "Nichtbibliothekare" beim GBV Bestellungen tätigen?
    private boolean selected; // Benutzer hat rechte bei diesem Konto angemeldet (Useraction.changeuserdetails)
    private boolean kontostatus = true; // Aktiv / Inaktiv
    private int kontotyp;
    private String default_deloptions = "post"; // Konto-Default Einstellung für deloptions
    private java.sql.Date paydate; // Zahlungseingangsdatum. Basis Berechnung wann das Konto abläuft
    private java.sql.Date expdate;
    private Date edatum; // Erstellungsdatum des kontos
    private String gtc; // enthält GTC-Version (General Terms and Conditions)
    private String gtcdate; // Datum der Annahme durch User
    private String message;

    // dient dazu die Bestellangaben nicht zu verlieren bei Übergabe aus Linkresolver

    private boolean resolver;
    private String artikeltitel = "";
    private String artikeltitel_encoded = "";
    private String heft = "";
    private String jahr = "";
    private String jahrgang = "";
    private String issn = "";
    private String zdbid; // kann null sein, für Anzeige-Logik
    private String author = "";
    private String author_encoded = "";
    private String seiten = "";
    private String zeitschriftentitel = "";
    private String doi = ""; // Digital  Object Identifier
    private String pmid = ""; // Pubmed-ID
    private String sici = ""; // Serial Item and Contribution Identifier
    private String lccn = ""; // Library of Congress Number
    private String isbn = "";
    private String genre = ""; // dient dazu alle Publikationsarten, wie Pamphlet, Proceeding etc. anzugeben
    private String rfr_id = ""; // Referrent-ID (Angabe woher die OpenURL-Anfrage stammt)
    private String mediatype = "Artikel"; // Artikel, Buch oder Teilkopie Buch, Defaultwert Artikel
    private String verlag = ""; // Buchverlag
    private String kapitel = "";
    private String buchtitel = "";

    //dient dazu die Userangaben zu übernehmen, ab Link aus Bestellform-Email

    private String kundenname;
    private String kundenvorname;
    private String kundenemail;
    private String kundeninstitution;
    private String kundenabteilung;
    private String kundentelefon;
    private String kundenadresse;
    private String kundenplz;
    private String kundenort;
    private String kundenland;


    public KontoForm() {

    }


    public KontoForm(final Konto k) {
        konto = k;
    }

    public String getKontoselect() {
        return kontoselect;
    }


    public void setKontoselect(final String kontoselect) {
        this.kontoselect = kontoselect;
    }


    public Long getKid() {
        return kid;
    }


    public void setKid(final Long kid) {
        this.kid = kid;
    }


    public Konto getKonto() {
        return konto;
    }


    public void setKonto(final Konto konto) {
        this.konto = konto;
    }

    public List<Konto> getKontos() {
        return kontos;
    }

    public void setKontos(final List<Konto> kontos) {
        this.kontos = kontos;
    }

    public boolean isZdb() {
        return zdb;
    }


    public void setZdb(final boolean zdb) {
        this.zdb = zdb;
    }


    public int getAccounting_rhythmday() {
        return accounting_rhythmday;
    }


    public void setAccounting_rhythmday(final int accounting_rhythmday) {
        this.accounting_rhythmday = accounting_rhythmday;
    }


    public int getAccounting_rhythmtimeout() {
        return accounting_rhythmtimeout;
    }


    public void setAccounting_rhythmtimeout(final int accounting_rhythmtimeout) {
        this.accounting_rhythmtimeout = accounting_rhythmtimeout;
    }


    public int getAccounting_rhythmvalue() {
        return accounting_rhythmvalue;
    }


    public void setAccounting_rhythmvalue(final int accounting_rhythmvalue) {
        this.accounting_rhythmvalue = accounting_rhythmvalue;
    }


    public String getAdresse() {
        return Adresse;
    }


    public void setAdresse(final String adresse) {
        Adresse = adresse;
    }


    public String getAdressenzusatz() {
        return Adressenzusatz;
    }


    public void setAdressenzusatz(final String adressenzusatz) {
        Adressenzusatz = adressenzusatz;
    }


    public String getBiblioname() {
        return biblioname;
    }


    public void setBiblioname(final String biblioname) {
        this.biblioname = biblioname;
    }


    public String getBibliotheksmail() {
        return Bibliotheksmail;
    }


    public void setBibliotheksmail(final String bibliotheksmail) {
        Bibliotheksmail = bibliotheksmail;
    }


    public Text getBilling() {
        return billing;
    }


    public void setBilling(final Text billing) {
        this.billing = billing;
    }


    public Text getBillingtype() {
        return billingtype;
    }


    public void setBillingtype(final Text billingtype) {
        this.billingtype = billingtype;
    }


    public String getDbsmail() {
        return dbsmail;
    }


    public void setDbsmail(final String dbsmail) {
        this.dbsmail = dbsmail;
    }


    public String getDbsmailpw() {
        return dbsmailpw;
    }


    public void setDbsmailpw(final String dbsmailpw) {
        this.dbsmailpw = dbsmailpw;
    }


    public Date getEdatum() {
        return edatum;
    }


    public void setEdatum(final Date edatum) {
        this.edatum = edatum;
    }

    public String getGtc() {
        return gtc;
    }


    public void setGtc(final String gtc) {
        this.gtc = gtc;
    }


    public String getGtcdate() {
        return gtcdate;
    }


    public void setGtcdate(final String gtcdate) {
        this.gtcdate = gtcdate;
    }


    public String getEzbid() {
        return ezbid;
    }


    public void setEzbid(final String ezbid) {
        this.ezbid = ezbid;
    }


    public String getInstlogolink() {
        return instlogolink;
    }


    public void setInstlogolink(final String instlogolink) {
        this.instlogolink = instlogolink;
    }


    public String getFaxno() {
        return faxno;
    }


    public void setFaxno(final String faxno) {
        this.faxno = faxno;
    }

    public String getFaxpassword() {
        return faxpassword;
    }


    public void setFaxpassword(final String faxpassword) {
        this.faxpassword = faxpassword;
    }

    public String getFaxusername() {
        return faxusername;
    }


    public void setFaxusername(final String faxusername) {
        this.faxusername = faxusername;
    }

    public String getPopfaxend() {
        return popfaxend;
    }


    public String getFax_extern() {
        return fax_extern;
    }


    public void setFax_extern(final String fax_extern) {
        this.fax_extern = fax_extern;
    }


    public void setPopfaxend(final String popfaxend) {
        this.popfaxend = popfaxend;
    }


    public boolean isKontostatus() {
        return kontostatus;
    }


    public void setKontostatus(final boolean kontostatus) {
        this.kontostatus = kontostatus;
    }


    public String getLand() {
        return Land;
    }


    public void setLand(final String land) {
        Land = land;
    }


    public String getTimezone() {
        return timezone;
    }


    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }


    public int getMaxordersj() {
        return maxordersj;
    }


    public void setMaxordersj(final int maxordersj) {
        this.maxordersj = maxordersj;
    }


    public int getMaxordersu() {
        return maxordersu;
    }


    public void setMaxordersu(final int maxordersu) {
        this.maxordersu = maxordersu;
    }


    public int getMaxordersutotal() {
        return maxordersutotal;
    }


    public void setMaxordersutotal(final int maxordersutotal) {
        this.maxordersutotal = maxordersutotal;
    }


    public int getOrderlimits() {
        return orderlimits;
    }


    public void setOrderlimits(final int orderlimits) {
        this.orderlimits = orderlimits;
    }


    public String getOrt() {
        return Ort;
    }


    public void setOrt(final String ort) {
        Ort = ort;
    }


    public String getPLZ() {
        return PLZ;
    }


    public void setPLZ(final String plz) {
        PLZ = plz;
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(final boolean selected) {
        this.selected = selected;
    }


    public String getGbvbenutzername() {
        return gbvbenutzername;
    }


    public void setGbvbenutzername(final String gbvbenutzername) {
        this.gbvbenutzername = gbvbenutzername;
    }


    public String getGbvpasswort() {
        return gbvpasswort;
    }


    public void setGbvpasswort(final String gbvpasswort) {
        this.gbvpasswort = gbvpasswort;
    }


    public String getGbvrequesterid() {
        return gbvrequesterid;
    }


    public void setGbvrequesterid(final String gbvrequesterid) {
        this.gbvrequesterid = gbvrequesterid;
    }


    public String getTelefon() {
        return Telefon;
    }


    public void setTelefon(final String telefon) {
        Telefon = telefon;
    }


    public int getThreshold_value() {
        return threshold_value;
    }


    public void setThreshold_value(final int threshold_value) {
        this.threshold_value = threshold_value;
    }

    public boolean isUserbestellung() {
        return userbestellung;
    }


    public void setUserbestellung(final boolean userbestellung) {
        this.userbestellung = userbestellung;
    }


    public boolean isGbvbestellung() {
        return gbvbestellung;
    }


    public void setGbvbestellung(final boolean gbvbestellung) {
        this.gbvbestellung = gbvbestellung;
    }


    public String getDefault_deloptions() {
        return default_deloptions;
    }


    public void setDefault_deloptions(final String default_deloptions) {
        this.default_deloptions = default_deloptions;
    }


    public boolean isUserlogin() {
        return userlogin;
    }


    public void setUserlogin(final boolean userlogin) {
        this.userlogin = userlogin;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(final String message) {
        this.message = message;
    }


    public int getKontotyp() {
        return kontotyp;
    }


    public void setKontotyp(final int kontotyp) {
        this.kontotyp = kontotyp;
    }


    public List<Countries> getCountries() {
        return countries;
    }


    public void setCountries(final List<Countries> countries) {
        this.countries = countries;
    }


    public String getCountryid() {
        return countryid;
    }


    public void setCountryid(final String countryid) {
        this.countryid = countryid;
    }


    public java.sql.Date getPaydate() {
        return paydate;
    }


    public void setPaydate(final java.sql.Date paydate) {
        this.paydate = paydate;
    }


    public java.sql.Date getExpdate() {
        return expdate;
    }


    public void setExpdate(final java.sql.Date expdate) {
        this.expdate = expdate;
    }


    public String getArtikeltitel() {
        return artikeltitel;
    }


    public void setArtikeltitel(final String artikeltitel) {
        this.artikeltitel = artikeltitel;
    }


    public String getArtikeltitel_encoded() {
        return artikeltitel_encoded;
    }


    public void setArtikeltitel_encoded(final String artikeltitel_encoded) {
        this.artikeltitel_encoded = artikeltitel_encoded;
    }


    public String getHeft() {
        return heft;
    }


    public void setHeft(final String heft) {
        this.heft = heft;
    }


    public String getJahr() {
        return jahr;
    }


    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }


    public String getJahrgang() {
        return jahrgang;
    }


    public void setJahrgang(final String jahrgang) {
        this.jahrgang = jahrgang;
    }


    public String getIssn() {
        return issn;
    }


    public void setIssn(final String issn) {
        this.issn = issn;
    }


    public String getZdbid() {
        return zdbid;
    }


    public void setZdbid(final String zdbid) {
        this.zdbid = zdbid;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(final String author) {
        this.author = author;
    }


    public String getAuthor_encoded() {
        return author_encoded;
    }


    public void setAuthor_encoded(final String author_encoded) {
        this.author_encoded = author_encoded;
    }


    public String getSeiten() {
        return seiten;
    }


    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }


    public String getZeitschriftentitel() {
        return zeitschriftentitel;
    }


    public void setZeitschriftentitel(final String zeitschriftentitel) {
        this.zeitschriftentitel = zeitschriftentitel;
    }


    public String getDoi() {
        return doi;
    }


    public void setDoi(final String doi) {
        this.doi = doi;
    }


    public String getPmid() {
        return pmid;
    }


    public void setPmid(final String pmid) {
        this.pmid = pmid;
    }


    public String getSici() {
        return sici;
    }


    public void setSici(final String sici) {
        this.sici = sici;
    }


    public String getLccn() {
        return lccn;
    }


    public void setLccn(final String lccn) {
        this.lccn = lccn;
    }


    public String getIsbn() {
        return isbn;
    }


    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }


    public String getGenre() {
        return genre;
    }


    public void setGenre(final String genre) {
        this.genre = genre;
    }


    public String getRfr_id() {
        return rfr_id;
    }


    public void setRfr_id(final String rfr_id) {
        this.rfr_id = rfr_id;
    }


    public String getMediatype() {
        return mediatype;
    }


    public void setMediatype(final String mediatype) {
        this.mediatype = mediatype;
    }


    public String getVerlag() {
        return verlag;
    }


    public void setVerlag(final String verlag) {
        this.verlag = verlag;
    }


    public String getKapitel() {
        return kapitel;
    }


    public void setKapitel(final String kapitel) {
        this.kapitel = kapitel;
    }


    public String getBuchtitel() {
        return buchtitel;
    }


    public void setBuchtitel(final String buchtitel) {
        this.buchtitel = buchtitel;
    }

    public String getIsil() {
        return isil;
    }

    public String getKundenname() {
        return kundenname;
    }


    public void setKundenname(final String kundenname) {
        this.kundenname = kundenname;
    }


    public String getKundenvorname() {
        return kundenvorname;
    }


    public void setKundenvorname(final String kundenvorname) {
        this.kundenvorname = kundenvorname;
    }


    public String getKundenemail() {
        return kundenemail;
    }


    public void setKundenemail(final String kundenemail) {
        this.kundenemail = kundenemail;
    }


    public String getKundeninstitution() {
        return kundeninstitution;
    }


    public void setKundeninstitution(final String kundeninstitution) {
        this.kundeninstitution = kundeninstitution;
    }


    public String getKundenabteilung() {
        return kundenabteilung;
    }


    public void setKundenabteilung(final String kundenabteilung) {
        this.kundenabteilung = kundenabteilung;
    }


    public String getKundentelefon() {
        return kundentelefon;
    }


    public void setKundentelefon(final String kundentelefon) {
        this.kundentelefon = kundentelefon;
    }


    public String getKundenadresse() {
        return kundenadresse;
    }


    public void setKundenadresse(final String kundenadresse) {
        this.kundenadresse = kundenadresse;
    }


    public String getKundenplz() {
        return kundenplz;
    }


    public void setKundenplz(final String kundenplz) {
        this.kundenplz = kundenplz;
    }


    public String getKundenort() {
        return kundenort;
    }


    public void setKundenort(final String kundenort) {
        this.kundenort = kundenort;
    }


    public String getKundenland() {
        return kundenland;
    }


    public void setKundenland(final String kundenland) {
        this.kundenland = kundenland;
    }

    public boolean isResolver() {
        return resolver;
    }


    public void setResolver(final boolean resolver) {
        this.resolver = resolver;
    }


    public void setIsil(String isil) {
        try {

            if (isil != null) {
                isil = isil.replaceAll("Ä", "Ae");
                isil = isil.replaceAll("Ö", "Oe");
                isil = isil.replaceAll("Ü", "Ue");
                isil = isil.replaceAll("ä", "ae");
                isil = isil.replaceAll("ö", "oe");
                isil = isil.replaceAll("ü", "ue");
                isil = isil.replaceAll("ß", "ss");
                isil = isil.replaceAll("\040", "");
                isil = isil.trim();
                if ("".equals(isil)) { isil = null; } // falls Leerstring, ISIL als null speichern
            }

        } catch (final Exception e) {
            LOG.error("setIsil: " + e.toString());
        }
        this.isil = isil;
    }

    /**
     * Setzt den Inhalt des Kontos aus konto in die Felder des Forms
     */
    public void setValuesFromKonto() {

        if (konto.getBibliotheksname() != null) { this.biblioname = konto.getBibliotheksname().trim(); }
        if (konto.getIsil() != null && !konto.getIsil().equals("")) {
            this.isil = konto.getIsil().trim();
        } else {
            this.isil = null; // kann null sein
        }
        if (konto.getAdresse() != null) { this.Adresse = konto.getAdresse().trim(); }
        if (konto.getAdressenzusatz() != null) { this.Adressenzusatz = konto.getAdressenzusatz().trim(); }
        if (konto.getPLZ() != null) { this.PLZ = konto.getPLZ().trim(); }
        if (konto.getOrt() != null) { this.Ort = konto.getOrt().trim(); }
        if (konto.getLand() != null) { this.Land = konto.getLand(); }
        if (konto.getTimezone() != null) { this.timezone = konto.getTimezone(); }
        if (konto.getFaxno() != null) { this.faxno = konto.getFaxno(); }
        if (konto.getFaxusername() != null) { this.faxusername = konto.getFaxusername(); }
        if (konto.getFaxpassword() != null) { this.faxpassword = konto.getFaxpassword(); }
        if (konto.getPopfaxend() != null) { this.popfaxend = konto.getPopfaxend(); }
        if (konto.getFax_extern() != null) { this.fax_extern = konto.getFax_extern().trim(); }
        if (konto.getTelefon() != null) { this.Telefon = konto.getTelefon().trim(); }
        if (konto.getBibliotheksmail() != null) { this.Bibliotheksmail = konto.getBibliotheksmail().trim(); }
        if (konto.getDbsmail() != null) { this.dbsmail = konto.getDbsmail().trim(); }
        if (konto.getDbsmailpw() != null) { this.dbsmailpw = konto.getDbsmailpw(); }
        this.gbvbenutzername = konto.getGbvbenutzername();
        this.gbvpasswort = konto.getGbvpasswort();
        this.gbvrequesterid = konto.getGbvrequesterid();
        if (konto.getEzbid() != null) { this.ezbid = konto.getEzbid(); }
        this.instlogolink = konto.getInstlogolink();
        this.zdb = konto.isZdb(); // ZDB-Teilnehmer
        if (konto.getBilling() != null) { this.billing = konto.getBilling(); }
        if (konto.getBillingtype() != null) { this.billingtype = konto.getBillingtype(); }
        this.accounting_rhythmvalue = konto.getAccounting_rhythmvalue();
        this.accounting_rhythmday = konto.getAccounting_rhythmday();
        this.accounting_rhythmtimeout = konto.getAccounting_rhythmtimeout();
        this.threshold_value = konto.getThreshold_value();
        this.maxordersu = konto.getMaxordersu();
        this.maxordersutotal = konto.getMaxordersutotal();
        this.maxordersj = konto.getMaxordersj();
        this.orderlimits = konto.getOrderlimits();
        this.userlogin = konto.isUserlogin();
        this.userbestellung = konto.isUserbestellung();
        this.gbvbestellung = konto.isGbvbestellung();
        this.selected = konto.isSelected();
        this.kontostatus = konto.isKontostatus();
        this.kontotyp = konto.getKontotyp();
        this.default_deloptions = konto.getDefault_deloptions();
    }


}
