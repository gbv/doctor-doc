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

package ch.dbs.form;

import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;
import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;

public final class KontoForm extends ValidatorForm{
	
	private static final SimpleLogger log = new SimpleLogger(KontoForm.class);

	private static final long serialVersionUID = 1L;
	private Long kid;
    private Konto konto;
    private List<Konto> kontos; // wird für Auswahl Admin - Kontoverwaltung gebraucht
    private String kontoselect; // Kontoauswahl für Admin prepareModifyKonto
    private String biblioname;
    private String isil; // International Standard Identifier for Libraries and Related Organizations
    private String Adresse;
    private String Adressenzusatz;
    private String PLZ;
    private String Ort;
    private String Land;
    private List<Countries> countries;
    private String countryid;
    private String faxno; // DD-Faxservernummer, nur durch Admin editierbar!
    private String faxusername;
    private String faxpassword;
    private String popfaxend;
    private String fax_extern; // externe Faxnummer, editierbar durch Kunde
    private String Telefon;
    private String Bibliotheksmail; // Bibliothekskontakt
    private String dbsmail; // Hier landen die bestellten Artikel
    private String dbsmailpw;
    private String gbvbenutzername;
    private String gbvpasswort;
    private String gbvrequesterid; // ID jeder einzelnen Bibliothek beim GBV
    private String ezbid;
    private boolean zdb = false;
    private Text billing; // Globale Einstellung, von wem die Rechnungen beglichen werden soll. Diese Einstellung kann durch den Wert welcher beim User hinterlegt ist berschrieben werden.
    private Text billingtype; // Globale Einstellung wie die Rechnung an den Kunden geschickt werden soll. Verweis auf die Tabelle Text mit dem Texttyp Billingtype
    private int accounting_rhythmvalue=0; // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll in Fr.-
    private int accounting_rhythmday=0; // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll. (in Tagen)
    private int accounting_rhythmtimeout=0; // Wird die Schwelle im Accounting_rhytm nicht erreicht, wird nach .... trotzdem eine Rechnung gestellt. Feld leer oder 0 = kein Schwellwert
    private int threshold_value=0; /* Verrechnungsschwellwert Sammelrechnungen in Tagen */
    private int maxordersu=0; // Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
    private int maxordersutotal=0; // Begrenzung mglicher Bestellungen durch einen Benutzer pro Kalenderjahr
    private int maxordersj=0; // Legt die maximale Artikelanzahl eines Kontos pro Kalenderjahr fest
    private int orderlimits=0; // Boolean. Bei True gelten die Beschrnkungen in maxordersj, maxordersutotal und maxordersu
    private boolean userlogin = false; // Dürfen sich "Nichtbibliothekare" einloggen
    private boolean userbestellung = false; // Dürfen "Nichtbibliothekare" bei SUBITO Bestellungen tätigen?
    private boolean gbvbestellung = false; // Dürfen "Nichtbibliothekare" beim GBV Bestellungen tätigen?
    private boolean selected = false; // Benutzer hat rechte bei diesem Konto angemeldet (Useraction.changeuserdetails)
    private boolean kontostatus = true; // Aktiv / Inaktiv
    private int kontotyp = 0; // Konto Basic = 0 / 1 Jahr Konto Enhanced = 1 / 1 Jahr Konto Enhanced plus Fax = 2 / 3 Monate Konto Enhanced plus Fax = 3
    private String default_deloptions = "post"; // Konto-Default Einstellung für deloptions
    private java.sql.Date paydate; // Zahlungseingangsdatum. Basis Berechnung wann das Konto abläuft
    private java.sql.Date expdate; // Konto Ablaufdatum. Automatische zurückstufung Kontotyp = 0 falls date>expdate Logfile schreiben welcher Ursprüngliche Kontotyp
    private Date edatum; // Erstellungsdatum des kontos
    private String gtc; // enthält GTC-Version (General Terms and Conditions)
    private String gtcdate; // Datum der Annahme durch User
    private String message;
    
 // dient dazu die Bestellangaben nicht zu verlieren bei Übergabe aus Linkresolver
    
    private boolean resolver = false;
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
    private String genre = ""; // dient dazu alle nicht abfangbaren Publikationsarten, wie Pamphlet, Proceeding etc. anzugeben
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
    
    
    public KontoForm(){
        
    }
    
    
    public KontoForm(Konto k){
    	konto = k;
    }

	public String getKontoselect() {
		return kontoselect;
	}


	public void setKontoselect(String kontoselect) {
		this.kontoselect = kontoselect;
	}


	public Long getKid() {
        return kid;
    }


    public void setKid(Long kid) {
        this.kid = kid;
    }


    public Konto getKonto() {
        return konto;
    }


    public void setKonto(Konto konto) {
        this.konto = konto;
    }

	public List<Konto> getKontos() {
		return kontos;
	}

	public void setKontos(List<Konto> kontos) {
		this.kontos = kontos;
	}

	public boolean isZdb() {
		return zdb;
	}


	public void setZdb(boolean zdb) {
		this.zdb = zdb;
	}


	public int getAccounting_rhythmday() {
		return accounting_rhythmday;
	}


	public void setAccounting_rhythmday(int accounting_rhythmday) {
		this.accounting_rhythmday = accounting_rhythmday;
	}


	public int getAccounting_rhythmtimeout() {
		return accounting_rhythmtimeout;
	}


	public void setAccounting_rhythmtimeout(int accounting_rhythmtimeout) {
		this.accounting_rhythmtimeout = accounting_rhythmtimeout;
	}


	public int getAccounting_rhythmvalue() {
		return accounting_rhythmvalue;
	}


	public void setAccounting_rhythmvalue(int accounting_rhythmvalue) {
		this.accounting_rhythmvalue = accounting_rhythmvalue;
	}


	public String getAdresse() {
		return Adresse;
	}


	public void setAdresse(String adresse) {
		Adresse = adresse;
	}


	public String getAdressenzusatz() {
		return Adressenzusatz;
	}


	public void setAdressenzusatz(String adressenzusatz) {
		Adressenzusatz = adressenzusatz;
	}


	public String getBiblioname() {
		return biblioname;
	}


	public void setBiblioname(String biblioname) {
		this.biblioname = biblioname;
	}


	public String getBibliotheksmail() {
		return Bibliotheksmail;
	}


	public void setBibliotheksmail(String bibliotheksmail) {
		Bibliotheksmail = bibliotheksmail;
	}


	public Text getBilling() {
		return billing;
	}


	public void setBilling(Text billing) {
		this.billing = billing;
	}


	public Text getBillingtype() {
		return billingtype;
	}


	public void setBillingtype(Text billingtype) {
		this.billingtype = billingtype;
	}


	public String getDbsmail() {
		return dbsmail;
	}


	public void setDbsmail(String dbsmail) {
		this.dbsmail = dbsmail;
	}


	public String getDbsmailpw() {
		return dbsmailpw;
	}


	public void setDbsmailpw(String dbsmailpw) {
		this.dbsmailpw = dbsmailpw;
	}


	public Date getEdatum() {
		return edatum;
	}


	public void setEdatum(Date edatum) {
		this.edatum = edatum;
	}

	public String getGtc() {
		return gtc;
	}


	public void setGtc(String gtc) {
		this.gtc = gtc;
	}


	public String getGtcdate() {
		return gtcdate;
	}


	public void setGtcdate(String gtcdate) {
		this.gtcdate = gtcdate;
	}


	public String getEzbid() {
		return ezbid;
	}


	public void setEzbid(String ezbid) {
		this.ezbid = ezbid;
	}


	public String getFaxno() {
		return faxno;
	}


	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getFaxpassword() {
		return faxpassword;
	}


	public void setFaxpassword(String faxpassword) {
		this.faxpassword = faxpassword;
	}

	public String getFaxusername() {
		return faxusername;
	}


	public void setFaxusername(String faxusername) {
		this.faxusername = faxusername;
	}

	public String getPopfaxend() {
		return popfaxend;
	}


	public String getFax_extern() {
		return fax_extern;
	}


	public void setFax_extern(String fax_extern) {
		this.fax_extern = fax_extern;
	}


	public void setPopfaxend(String popfaxend) {
		this.popfaxend = popfaxend;
	}


	public boolean isKontostatus() {
		return kontostatus;
	}


	public void setKontostatus(boolean kontostatus) {
		this.kontostatus = kontostatus;
	}


	public String getLand() {
		return Land;
	}


	public void setLand(String land) {
		Land = land;
	}


	public int getMaxordersj() {
		return maxordersj;
	}


	public void setMaxordersj(int maxordersj) {
		this.maxordersj = maxordersj;
	}


	public int getMaxordersu() {
		return maxordersu;
	}


	public void setMaxordersu(int maxordersu) {
		this.maxordersu = maxordersu;
	}


	public int getMaxordersutotal() {
		return maxordersutotal;
	}


	public void setMaxordersutotal(int maxordersutotal) {
		this.maxordersutotal = maxordersutotal;
	}


	public int getOrderlimits() {
		return orderlimits;
	}


	public void setOrderlimits(int orderlimits) {
		this.orderlimits = orderlimits;
	}


	public String getOrt() {
		return Ort;
	}


	public void setOrt(String ort) {
		Ort = ort;
	}


	public String getPLZ() {
		return PLZ;
	}


	public void setPLZ(String plz) {
		PLZ = plz;
	}


	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public String getGbvbenutzername() {
		return gbvbenutzername;
	}


	public void setGbvbenutzername(String gbvbenutzername) {
		this.gbvbenutzername = gbvbenutzername;
	}


	public String getGbvpasswort() {
		return gbvpasswort;
	}


	public void setGbvpasswort(String gbvpasswort) {
		this.gbvpasswort = gbvpasswort;
	}


	public String getGbvrequesterid() {
		return gbvrequesterid;
	}


	public void setGbvrequesterid(String gbvrequesterid) {
		this.gbvrequesterid = gbvrequesterid;
	}


	public String getTelefon() {
		return Telefon;
	}


	public void setTelefon(String telefon) {
		Telefon = telefon;
	}


	public int getThreshold_value() {
		return threshold_value;
	}


	public void setThreshold_value(int threshold_value) {
		this.threshold_value = threshold_value;
	}

	public boolean isUserbestellung() {
		return userbestellung;
	}


	public void setUserbestellung(boolean userbestellung) {
		this.userbestellung = userbestellung;
	}


	public boolean isGbvbestellung() {
		return gbvbestellung;
	}


	public void setGbvbestellung(boolean gbvbestellung) {
		this.gbvbestellung = gbvbestellung;
	}


	public String getDefault_deloptions() {
		return default_deloptions;
	}


	public void setDefault_deloptions(String default_deloptions) {
		this.default_deloptions = default_deloptions;
	}


	public boolean isUserlogin() {
		return userlogin;
	}


	public void setUserlogin(boolean userlogin) {
		this.userlogin = userlogin;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getKontotyp() {
		return kontotyp;
	}


	public void setKontotyp(int kontotyp) {
		this.kontotyp = kontotyp;
	}


	public List<Countries> getCountries() {
		return countries;
	}


	public void setCountries(List<Countries> countries) {
		this.countries = countries;
	}


	public String getCountryid() {
		return countryid;
	}


	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}


	public java.sql.Date getPaydate() {
		return paydate;
	}


	public void setPaydate(java.sql.Date paydate) {
		this.paydate = paydate;
	}


	public java.sql.Date getExpdate() {
		return expdate;
	}


	public void setExpdate(java.sql.Date expdate) {
		this.expdate = expdate;
	}


	public String getArtikeltitel() {
		return artikeltitel;
	}


	public void setArtikeltitel(String artikeltitel) {
		this.artikeltitel = artikeltitel;
	}


	public String getArtikeltitel_encoded() {
		return artikeltitel_encoded;
	}


	public void setArtikeltitel_encoded(String artikeltitel_encoded) {
		this.artikeltitel_encoded = artikeltitel_encoded;
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


	public String getIssn() {
		return issn;
	}


	public void setIssn(String issn) {
		this.issn = issn;
	}


	public String getZdbid() {
		return zdbid;
	}


	public void setZdbid(String zdbid) {
		this.zdbid = zdbid;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getAuthor_encoded() {
		return author_encoded;
	}


	public void setAuthor_encoded(String author_encoded) {
		this.author_encoded = author_encoded;
	}


	public String getSeiten() {
		return seiten;
	}


	public void setSeiten(String seiten) {
		this.seiten = seiten;
	}


	public String getZeitschriftentitel() {
		return zeitschriftentitel;
	}


	public void setZeitschriftentitel(String zeitschriftentitel) {
		this.zeitschriftentitel = zeitschriftentitel;
	}


	public String getDoi() {
		return doi;
	}


	public void setDoi(String doi) {
		this.doi = doi;
	}


	public String getPmid() {
		return pmid;
	}


	public void setPmid(String pmid) {
		this.pmid = pmid;
	}


	public String getSici() {
		return sici;
	}


	public void setSici(String sici) {
		this.sici = sici;
	}


	public String getLccn() {
		return lccn;
	}


	public void setLccn(String lccn) {
		this.lccn = lccn;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public String getRfr_id() {
		return rfr_id;
	}


	public void setRfr_id(String rfr_id) {
		this.rfr_id = rfr_id;
	}


	public String getMediatype() {
		return mediatype;
	}


	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}


	public String getVerlag() {
		return verlag;
	}


	public void setVerlag(String verlag) {
		this.verlag = verlag;
	}


	public String getKapitel() {
		return kapitel;
	}


	public void setKapitel(String kapitel) {
		this.kapitel = kapitel;
	}


	public String getBuchtitel() {
		return buchtitel;
	}


	public void setBuchtitel(String buchtitel) {
		this.buchtitel = buchtitel;
	}

	public String getIsil() {
		return isil;
	}

	public String getKundenname() {
		return kundenname;
	}


	public void setKundenname(String kundenname) {
		this.kundenname = kundenname;
	}


	public String getKundenvorname() {
		return kundenvorname;
	}


	public void setKundenvorname(String kundenvorname) {
		this.kundenvorname = kundenvorname;
	}


	public String getKundenemail() {
		return kundenemail;
	}


	public void setKundenemail(String kundenemail) {
		this.kundenemail = kundenemail;
	}


	public String getKundeninstitution() {
		return kundeninstitution;
	}


	public void setKundeninstitution(String kundeninstitution) {
		this.kundeninstitution = kundeninstitution;
	}


	public String getKundenabteilung() {
		return kundenabteilung;
	}


	public void setKundenabteilung(String kundenabteilung) {
		this.kundenabteilung = kundenabteilung;
	}


	public String getKundentelefon() {
		return kundentelefon;
	}


	public void setKundentelefon(String kundentelefon) {
		this.kundentelefon = kundentelefon;
	}


	public String getKundenadresse() {
		return kundenadresse;
	}


	public void setKundenadresse(String kundenadresse) {
		this.kundenadresse = kundenadresse;
	}


	public String getKundenplz() {
		return kundenplz;
	}


	public void setKundenplz(String kundenplz) {
		this.kundenplz = kundenplz;
	}


	public String getKundenort() {
		return kundenort;
	}


	public void setKundenort(String kundenort) {
		this.kundenort = kundenort;
	}


	public String getKundenland() {
		return kundenland;
	}


	public void setKundenland(String kundenland) {
		this.kundenland = kundenland;
	}

	public boolean isResolver() {
		return resolver;
	}


	public void setResolver(boolean resolver) {
		this.resolver = resolver;
	}


	public void setIsil(String isil) {
		try {
        	
        	if (isil!=null) {
        		isil = isil.replaceAll("Ä", "Ae");
        		isil = isil.replaceAll("Ö", "Oe");
        		isil = isil.replaceAll("Ü", "Ue");
        		isil = isil.replaceAll("ä", "ae");
        		isil = isil.replaceAll("ö", "oe");
        		isil = isil.replaceAll("ü", "ue");
        		isil = isil.replaceAll("ß", "ss");
        		isil = isil.replaceAll("\040", "");
        		isil = isil.trim();
        		if (isil.equals("")) isil = null; // falls Leerstring, ISIL als null speichern
        	}        	
        	
        } catch (Exception e) {    
        	log.error("setIsil: " + e.toString());
        }
		this.isil = isil;
	}

	/**
	 * Setzt den Inhalt des Kontos aus konto in die Felder des Forms
	 */
	public void setValuesFromKonto(){
		
	    if (konto.getBibliotheksname()!=null) this.biblioname = konto.getBibliotheksname().trim();
	    if (konto.getIsil()!=null && !konto.getIsil().equals("")) {this.isil = konto.getIsil().trim();} else {this.isil = null;} // kann null sein
	    if (konto.getAdresse()!=null) this.Adresse = konto.getAdresse().trim();
	    if (konto.getAdressenzusatz()!=null) this.Adressenzusatz = konto.getAdressenzusatz().trim();
	    if (konto.getPLZ()!=null) this.PLZ = konto.getPLZ().trim();
	    if (konto.getOrt()!=null) this.Ort = konto.getOrt().trim();
	    if (konto.getLand()!=null) this.Land = konto.getLand();
	    if (konto.getFaxno()!=null) this.faxno = konto.getFaxno(); // DD-Faxservernummer, nur durch Admin editierbar!
	    if (konto.getFaxusername()!=null) this.faxusername = konto.getFaxusername();
	    if (konto.getFaxpassword()!=null) this.faxpassword = konto.getFaxpassword();
	    if (konto.getPopfaxend()!=null) this.popfaxend = konto.getPopfaxend();
	    if (konto.getFax_extern()!=null) this.fax_extern = konto.getFax_extern().trim(); // externe Faxnummer, editierbar durch Kunde
	    if (konto.getTelefon()!=null) this.Telefon = konto.getTelefon().trim();
	    if (konto.getBibliotheksmail()!=null) this.Bibliotheksmail = konto.getBibliotheksmail().trim(); // Bibliothekskontakt
	    if (konto.getDbsmail()!=null) this.dbsmail = konto.getDbsmail().trim(); // Hier landen die bestellten Artikel
	    if (konto.getDbsmailpw()!=null) this.dbsmailpw = konto.getDbsmailpw();
	    this.gbvbenutzername = konto.getGbvbenutzername();
	    this.gbvpasswort = konto.getGbvpasswort();
	    this.gbvrequesterid = konto.getGbvrequesterid();
	    if (konto.getEzbid()!=null) this.ezbid = konto.getEzbid();
	    this.zdb = konto.isZdb(); // ZDB-Teilnehmer
	    if (konto.getBilling()!=null) this.billing = konto.getBilling(); // Globale Einstellung, von wem die Rechnungen beglichen werden soll. Diese Einstellung kann durch den Wert welcher beim User hinterlegt ist berschrieben werden.
	    if (konto.getBillingtype()!=null) this.billingtype = konto.getBillingtype(); // Globale Einstellung wie die Rechnung an den Kunden geschickt werden soll. Verweis auf die Tabelle Text mit dem Texttyp Billingtype
	    this.accounting_rhythmvalue = konto.getAccounting_rhythmvalue(); // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll in Fr.-
	    this.accounting_rhythmday = konto.getAccounting_rhythmday(); // Globale Einstellung ab wann die Rechnung an den Kunden geschickt werden soll. (in Tagen)
	    this.accounting_rhythmtimeout = konto.getAccounting_rhythmtimeout(); // Wird die Schwelle im Accounting_rhytm nicht erreicht, wird nach .... trotzdem eine Rechnung gestellt. Feld leer oder 0 = kein Schwellwert
	    this.threshold_value = konto.getThreshold_value(); /* Verrechnungsschwellwert Sammelrechnungen in Tagen */
	    this.maxordersu = konto.getMaxordersu(); // Begrenzung mglicher unbezahlter Bestellungen durch einen Benutzer
	    this.maxordersutotal = konto.getMaxordersutotal(); // Begrenzung mglicher Bestellungen durch einen Benutzer pro Jahr
	    this.maxordersj = konto.getMaxordersj(); // Legt die maximale Artikelanzahl eines Kontos pro Jahr fest
	    this.orderlimits = konto.getOrderlimits(); // Boolean. Bei True gelten die Beschrnkungen in maxordersj, maxordersutotal und maxordersu
	    this.userlogin = konto.isUserlogin(); // Drfen sich "Nichtbibliothekare" einloggen
	    this.userbestellung = konto.isUserbestellung(); // Dürfen "Nichtbibliothekare" bei SUBITO Bestellungen tätigen?
	    this.gbvbestellung = konto.isGbvbestellung(); // Dürfen "Nichtbibliothekare" beim GBV Bestellungen tätigen?
	    this.selected = konto.isSelected(); // Benutzer hat rechte bei diesem Konto angemeldet (Useraction.changeuserdetails)
	    this.kontostatus = konto.isKontostatus(); // Aktiv / Inaktiv
	    this.kontotyp = konto.getKontotyp(); // Konto Basic = 0 / 1 Jahr Konto Enhanced = 1 / 1 Jahr Konto Enhanced plus Fax = 2 / 3 Monate Konto Enhanced plus Fax = 3
	    this.default_deloptions = konto.getDefault_deloptions();
	}
    
    
}
