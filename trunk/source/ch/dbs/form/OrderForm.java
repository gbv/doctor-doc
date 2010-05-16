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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import org.apache.struts.validator.ValidatorForm;
import org.grlea.log.SimpleLogger;

import util.CodeUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Countries;
import ch.dbs.entity.DefaultPreis;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.interf.OrderHandler;

public final class OrderForm extends ValidatorForm implements OrderHandler{
	
	private static final SimpleLogger log = new SimpleLogger(OrderForm.class);

	private static final long serialVersionUID = 1L;
	private List<Bestellungen> bestellungen;
    private List<OrderState> states;
    private List<Text> statitexts;
    private List<Lieferanten> quellen;
    private List<Text> waehrungen;
    private List<DefaultPreis> defaultpreise;
    private Bestellungen bestellung;
    private Lieferanten lieferant; // neue Verknüpfung zu Tabelle Lieferanten
    private String bestellquelle; // dient lediglich noch der Führung des doppelten Eintrages in der DB
    private String submit = ""; // dient zur Unterscheidung welcher Submit-Knopf in einem Formular gewählt wurde
    private Long bid;
    private String uid;
    private String lid;
    private String artikeltitel = "";
    private String artikeltitel_encoded = "";
    private String heft = "";
    private String jahr = "";
    private String jahrgang = "";
    private String issn = "";
    private String zdbid; // kann null sein, für Anzeige-Logik
    private String ppn; // Pica-Production-Number => eindeutige Datensatznummer beim GBV 
    private String fileformat = "";
    private String deloptions = "";
    private String prio = "";
    private String author = "";
    private String author_encoded = "";
    private String seiten = "";
    private String foruser = "0"; // enthält ID eines Benutzers. 0 = "Bitte auswählen" im Select
    private String sessionid;
    private String bibliothek = "";
    private String subitonr;
    private String gbvnr;
    private String trackingnr = ""; // eindeutige Nr. in der Kommunikation zwischen Bestellsystemen. Wird vor dem Abspeichern der Bestellung benötigt...
    private String interne_bestellnr = ""; // falls eine Bibliothek ein eigenes Nummersystem führt
    private String sigel = "";
    private String faxno;
    private String zeitschriftentitel = "";
    private String zeitschriftentitel_encoded = "";
    private String contents;
    private String link;
    private String anmerkungen = "";
    private String notizen = "";
    private String status;
    private String preisvorkomma;
    private String preisnachkomma;
    private String waehrung;
    private boolean preisdefault;
    private BigDecimal kaufpreis;
    private String orderlink;
    private String didYouMean = "";
    private boolean checkDidYouMean;
    private boolean autocomplete; // wird zur Kontrolle für die Funktion Autocomplete verwendet
    private boolean flag_noissn; // wird gesetzt, falls Autocomplete keine ISSN liefert 
    private int runs_autocomplete;
    private boolean manuell;
    private boolean erledigt;
    private boolean delete;
    private String origin; // kann für forward benutzt werden, (nicht direkt, nur mit Werteprüfung!) 
    private List<ErrorMessage> links;    
    private List<AbstractBenutzer> kontouser;    
    // u.a. ip-basiertes Kunden-Bestellform
    private String language; // Sprache des Dokumentes
    private String doi = ""; // Digital  Object Identifier
    private String pmid = ""; // Pubmed-ID
    private String sici = ""; // Serial Item and Contribution Identifier
    private String lccn = ""; // Library of Congress Number
    private String isbn = "";
    private String genre = ""; // dient dazu alle nicht abfangbaren Publikationsarten, wie Pamphlet, Proceeding etc. anzugeben
    private String rfr_id = ""; // Referrent-ID (Angabe woher die OpenURL-Anfrage stammt)
    private String mediatype = "Artikel"; // Artikel, Buch oder Teilkopie Buch, Defaultwert Artikel
    private String verlag = ""; // Buchverlag
    private String verlag_encoded = ""; // Buchverlag
    private String kapitel = "";
    private String kapitel_encoded = "";
    private String buchtitel = "";
    private String buchtitel_encoded = "";
    private String kundenvorname;
    private String kundenname;
    private String kundenmail;
    // hier folgt der parametrisierbare Teil...
    private String kundenlieferart1;
    private String kundenlieferart2;
    private String kundenlieferart3;
    private String kundeninstitution;
    private String kundenabteilung;
    private String freitxt1_label;
    private String freitxt1_inhalt;
    private String kundenadresse;
    private String kundenstrasse;
    private String freitxt2_label;
    private String freitxt2_inhalt;
    private String kundenplz;
    private String kundenort;
    private String kundenland;
    private List<Countries> countries;
    private String kundentelefon;
    private String kundenbenutzernr;
    private String freitxt3_label;
    private String freitxt3_inhalt;
    private String radiobutton_name = "";
    private String radiobutton = "";
    private String gebuehren;
    private String agb;
    // ...Ende parametrisierbarer Teil
    private boolean resolve; // stellt sicher, dass eine PMID nur auf manuellen Befehl aufgelöst wird (OpenURL und Bestellformular)
    private boolean resolver;
    private boolean carelit; // gibt es bei Carelit Volltexte?
    private String kkid; // Kontokennung (anstelle von IP-basiertem Zugriff)
    private String bkid; // Borkerkennung
    // OpenUrl
    private String url = "";
    // Captcha
    private String captcha_id;
    private String captcha_text;
    // Bestandesangaben
    private boolean fromstock; // um forward wieder auf stock zu setzen (Bestandesangaben)
    // GBV
    private String gbvsearch; // Suchbegriff
    private String gbvfield; // Suchfeld
    private int treffer_total;
    private int forwrd;
    private int back;
    private String maximum_cost = "";
    
    //DDL
    private String Preis;
    
    public OrderForm(){
        
    }
    
    public OrderForm(LoginForm lf) {
    	this.setResolver(lf.isResolver());
    	this.setIssn(lf.getIssn());
    	this.setMediatype(lf.getMediatype());
    	this.setJahr(lf.getJahr());
    	this.setJahrgang(lf.getJahrgang());
    	this.setHeft(lf.getHeft());
    	this.setSeiten(lf.getSeiten());
    	this.setIsbn(lf.getIsbn());
    	this.setArtikeltitel(lf.getArtikeltitel());
    	this.setZeitschriftentitel(lf.getZeitschriftentitel());
    	this.setAuthor(lf.getAuthor());
    	this.setKapitel(lf.getKapitel());
    	this.setBuchtitel(lf.getBuchtitel());
    	this.setVerlag(lf.getVerlag());
    	this.setRfr_id(lf.getRfr_id());
    	this.setGenre(lf.getGenre());
    	this.setPmid(lf.getPmid());
    	this.setDoi(lf.getDoi());
    	this.setSici(lf.getSici());
    	this.setLccn(lf.getLccn());
    	this.setZdbid(lf.getZdbid());
    	this.setArtikeltitel_encoded(lf.getArtikeltitel_encoded());
    	this.setAuthor_encoded(lf.getAuthor_encoded());
    	this.setForuser(lf.getForuser());
    }
    
    public OrderForm(KontoForm kf) {
    	this.setResolver(kf.isResolver());
    	this.setIssn(kf.getIssn());
    	this.setMediatype(kf.getMediatype());
    	this.setJahr(kf.getJahr());
    	this.setJahrgang(kf.getJahrgang());
    	this.setHeft(kf.getHeft());
    	this.setSeiten(kf.getSeiten());
    	this.setIsbn(kf.getIsbn());
    	this.setArtikeltitel(kf.getArtikeltitel());
    	this.setZeitschriftentitel(kf.getZeitschriftentitel());
    	this.setAuthor(this.getAuthor());
    	this.setKapitel(kf.getKapitel());
    	this.setBuchtitel(kf.getBuchtitel());
    	this.setVerlag(kf.getVerlag());
    	this.setRfr_id(kf.getRfr_id());
    	this.setGenre(kf.getGenre());
    	this.setPmid(kf.getPmid());
    	this.setDoi(kf.getDoi());
    	this.setSici(kf.getSici());
    	this.setLccn(kf.getLccn());
    	this.setZdbid(kf.getZdbid());
    	this.setArtikeltitel_encoded(kf.getArtikeltitel_encoded());
    	this.setAuthor_encoded(kf.getAuthor_encoded());
    }
    
    /**
     * Ergänzt ein OrderForm bei den fehlenden Angaben mit den Angaben eines zweiten OrderForms
     * 
     * @param OrderForm tocomplete
     * @param OrderForm tocompare
     * 
     * @return OrderForm tocomplete
     */
    public OrderForm completeOrderForm(OrderForm tocomplete, OrderForm tocompare) {
    	
    	// TODO: systematisch auscodieren...
    	
    	try {
    	
    	if (tocomplete.getArtikeltitel().equals("")) tocomplete.setArtikeltitel(tocompare.getArtikeltitel());
    	if (tocomplete.getAuthor().equals("")) tocomplete.setAuthor(tocompare.getAutor());
    	if (tocomplete.getDoi().equals("")) tocomplete.setDoi(tocompare.getDoi());
    	if (tocomplete.getHeft().equals("")) tocomplete.setHeft(tocompare.getHeft());
    	if (tocomplete.getIssn().equals("")) tocomplete.setIssn(tocompare.getIssn());
    	if (tocomplete.getJahr().equals("")) tocomplete.setJahr(tocompare.getJahr());
    	if (tocomplete.getJahrgang().equals("")) tocomplete.setJahrgang(tocompare.getJahrgang());
    	if (tocomplete.getPmid().equals("")) tocomplete.setPmid(tocompare.getPmid());
    	if (tocomplete.getSeiten().equals("")) tocomplete.setSeiten(tocompare.getSeiten());
    	if (tocomplete.getZeitschriftentitel().equals("")) tocomplete.setZeitschriftentitel(tocompare.getZeitschriftentitel());
    	
    	} catch (Exception e) {
    		log.error("completeOrderForm: " + e.toString());
    	}
    	
    	return tocomplete;
    }
    
    /**
     * Encodiert diejenigen Felder, die einen Textstring mit Sonderzeichen enthalten können.
     * Verhindert Fehler bei der Übergabe in Get-Methoden 
     * 
     * @param OrderForm of
     * 
     * @return OrderForm of
     */
    public OrderForm encodeOrderForm(OrderForm of) {
    	
    	CodeUrl codeUrl = new CodeUrl();
    	
    	try {    	
    		of.setArtikeltitel_encoded(codeUrl.encode(of.getArtikeltitel()));
    		of.setAuthor_encoded(codeUrl.encode(of.getAuthor()));
    		of.setZeitschriftentitel_encoded(codeUrl.encode(of.getZeitschriftentitel()));
    		of.setVerlag_encoded(codeUrl.encode(of.getVerlag()));
    		of.setKapitel_encoded(codeUrl.encode(of.getKapitel()));
    		of.setBuchtitel_encoded(codeUrl.encode(of.getBuchtitel()));
    		
    	} catch (Exception e) {
    		log.error("encodeOrderForm(OrderForm of): " + e.toString());
    	}
    	
    	return of;
    }
    
//    /**
//     * 
//     * 
//     * @param of
//     */
//    public OrderForm(OrderForm of){
//
//    }
        
    public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public List<OrderState> getStates() {
		return states;
	}

	public void setStates(List<OrderState> states) {
		this.states = states;
	}

	public List<Text> getStatitexts() {
		return statitexts;
	}

	public void setStatitexts(List<Text> statitexts) {
		this.statitexts = statitexts;
	}

	public List<DefaultPreis> getDefaultpreise() {
		return defaultpreise;
	}

	public void setDefaultpreise(List<DefaultPreis> defaultpreise) {
		this.defaultpreise = defaultpreise;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public List<Lieferanten> getQuellen() {
		return quellen;
	}

	public void setQuellen(List<Lieferanten> quellen) {
		this.quellen = quellen;
	}

	public Lieferanten getLieferant() {
		return lieferant;
	}

	public void setLieferant(Lieferanten lieferant) {
		this.lieferant = lieferant;
	}

	public String getBestellquelle() {
		return bestellquelle;
	}

	public void setBestellquelle(String bestellquelle) {
		this.bestellquelle = bestellquelle;
	}

	public String getRfr_id() {
		return rfr_id;
	}

	public void setRfr_id(String rfr_id) {
		this.rfr_id = rfr_id;
	}

	public String getOrderlink() {
		return orderlink;
	}

	public void setOrderlink(String orderlink) {
		this.orderlink = orderlink;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<ErrorMessage> getLinks() {
		return links;
	}

	public void setLinks(List<ErrorMessage> links) {
		this.links = links;
	}

	public String getZeitschriftentitel() {
		return zeitschriftentitel;
	}

	public void setZeitschriftentitel(String zeitschriftentitel) {
		this.zeitschriftentitel = zeitschriftentitel;
	}

	public String getZeitschriftentitel_encoded() {
		return zeitschriftentitel_encoded;
	}

	public void setZeitschriftentitel_encoded(String zeitschriftentitel_encoded) {
		this.zeitschriftentitel_encoded = zeitschriftentitel_encoded;
	}

	public String getSigel() {
		return sigel;
	}

	public void setSigel(String sigel) {
		this.sigel = sigel;
	}

	public String getFaxno() {
		return faxno;
	}

	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getBibliothek() {
		return bibliothek;
	}

	public void setBibliothek(String bibliothek) {
		this.bibliothek = bibliothek;
	}

	public String getSubitonr() {
		return subitonr;
	}

	public void setSubitonr(String subitonr) {
		this.subitonr = subitonr;
	}
    public String getSessionid() {
        return sessionid;
    }
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
    public String getForuser() {
		return foruser;
	}
    public void setForuser(String foruser) {
		this.foruser = foruser;
	}
    public Bestellungen getBestellung() {
        return bestellung;
    }
    public void setBestellung(Bestellungen bestellung) {
        this.bestellung = bestellung;
    }

    public List<Bestellungen> getBestellungen() {
        return bestellungen;
    }

    public void setBestellungen(List<Bestellungen> bestellungen) {
        this.bestellungen = bestellungen;
    }

    public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Text> getWaehrungen() {
		return waehrungen;
	}

	public void setWaehrungen(List<Text> waehrungen) {
		this.waehrungen = waehrungen;
	}

	public String getFileformat() {
        return fileformat;
    }

    public void setFileformat(String fileformat) {
        this.fileformat = fileformat;
    }

    public String getDeloptions() {
		return deloptions;
	}

	public void setDeloptions(String deloptions) {
		this.deloptions = deloptions;
	}

	public String getHeft() {
        return heft;
    }


    public void setHeft(String heft) {
        this.heft = heft;
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

	public String getPpn() {
		return ppn;
	}

	public void setPpn(String ppn) {
		this.ppn = ppn;
	}

	public String getJahr() {
        return jahr;
    }

    public String getCaptcha_id() {
		return captcha_id;
	}

	public void setCaptcha_id(String captcha_id) {
		this.captcha_id = captcha_id;
	}

	public String getCaptcha_text() {
		return captcha_text;
	}

	public void setCaptcha_text(String captcha_text) {
		this.captcha_text = captcha_text;
	}

	public void setJahr(String jahr) {
        this.jahr = jahr;
    }


    public String getPrio() {
        return prio;
    }


    public void setPrio(String prio) {
        this.prio = prio;
    }


    public String getAutor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }
       
    public String getAuthor() {
		return author;
	}

	public void setAuthor_encoded(String author_encoded) {
		this.author_encoded = author_encoded;
	}

	public String getAuthor_encoded() {
		return author_encoded;
	}

	public void setAutor_encoded(String author_encoded) {
		this.author_encoded = author_encoded;
	}

	public String getSeiten() {
        return seiten;
    }


    public void setSeiten(String seiten) {
        this.seiten = seiten;
    }


    public String getJahrgang() {
        return jahrgang;
    }


    public void setJahrgang(String jahrgang) {
        this.jahrgang = jahrgang;
    }


    public Long getBid() {
        return bid;
    }


    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public List<AbstractBenutzer> getKontouser() {
        return kontouser;
    }
    public void setKontouser(List<AbstractBenutzer> kontouser) {
        this.kontouser = kontouser;
    }

	public String getAnmerkungen() {
		return anmerkungen;
	}

	public void setAnmerkungen(String anmerkungen) {
		this.anmerkungen = anmerkungen;
	}

	public String getDidYouMean() {
		return didYouMean;
	}

	public void setDidYouMean(String didYouMean) {
		this.didYouMean = didYouMean;
	}

	public String getNotizen() {
		return notizen;
	}

	public void setNotizen(String notizen) {
		this.notizen = notizen;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public String getKundenmail() {
		return kundenmail;
	}

	public void setKundenmail(String kundenmail) {
		this.kundenmail = kundenmail;
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

	public String getPreisnachkomma() {
		return preisnachkomma;
	}

	public void setPreisnachkomma(String preisnachkomma) {
		this.preisnachkomma = preisnachkomma;
	}

	public String getPreisvorkomma() {
		return preisvorkomma;
	}

	public void setPreisvorkomma(String preisvorkomma) {
		this.preisvorkomma = preisvorkomma;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public int getRuns_autocomplete() {
		return runs_autocomplete;
	}

	public void setRuns_autocomplete(int runs_autocomplete) {
		this.runs_autocomplete = runs_autocomplete;
	}

	public BigDecimal getKaufpreis() {
		return kaufpreis;
	}

	public void setKaufpreis(BigDecimal kaufpreis) {
		this.kaufpreis = kaufpreis;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getLccn() {
		return lccn;
	}

	public void setLccn(String lccn) {
		this.lccn = lccn;
	}

	public String getSici() {
		return sici;
	}

	public void setSici(String sici) {
		this.sici = sici;
	}

	public String getKundenadresse() {
		return kundenadresse;
	}

	public void setKundenadresse(String kundenadresse) {
		this.kundenadresse = kundenadresse;
	}

	public String getBuchtitel() {
		return buchtitel;
	}

	public void setBuchtitel(String buchtitel) {
		this.buchtitel = buchtitel;
	}

	public String getKapitel() {
		return kapitel;
	}

	public void setKapitel(String kapitel) {
		this.kapitel = kapitel;
	}

	public String getKkid() {
		return kkid;
	}

	public void setKkid(String kkid) {
		this.kkid = kkid;
	}

	public String getBkid() {
		return bkid;
	}

	public void setBkid(String bkid) {
		this.bkid = bkid;
	}

	public String getGbvsearch() {
		return gbvsearch;
	}

	public void setGbvsearch(String gbvsearch) {
		this.gbvsearch = gbvsearch;
	}

	public String getGbvfield() {
		return gbvfield;
	}

	public void setGbvfield(String gbvfield) {
		this.gbvfield = gbvfield;
	}

	public int getTreffer_total() {
		return treffer_total;
	}

	public void setTreffer_total(int treffer_total) {
		this.treffer_total = treffer_total;
	}

	public int getForwrd() {
		return forwrd;
	}

	public void setForwrd(int forwrd) {
		this.forwrd = forwrd;
	}

	public int getBack() {
		return back;
	}

	public void setBack(int back) {
		this.back = back;
	}

	public String getMaximum_cost() {
		return maximum_cost;
	}

	public void setMaximum_cost(String maximum_cost) {
		this.maximum_cost = maximum_cost;
	}

	public String getGbvnr() {
		return gbvnr;
	}

	public void setGbvnr(String gbvnr) {
		this.gbvnr = gbvnr;
	}

	public String getInterne_bestellnr() {
		return interne_bestellnr;
	}

	public void setInterne_bestellnr(String interne_bestellnr) {
		this.interne_bestellnr = interne_bestellnr;
	}

	public String getTrackingnr() {
		return trackingnr;
	}

	public void setTrackingnr(String trackingnr) {
		this.trackingnr = trackingnr;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getFreitxt1_label() {
		return freitxt1_label;
	}

	public void setFreitxt1_label(String freitxt1_label) {
		this.freitxt1_label = freitxt1_label;
	}

	public String getFreitxt1_inhalt() {
		return freitxt1_inhalt;
	}

	public void setFreitxt1_inhalt(String freitxt1_inhalt) {
		this.freitxt1_inhalt = freitxt1_inhalt;
	}

	public String getKundenstrasse() {
		return kundenstrasse;
	}

	public void setKundenstrasse(String kundenstrasse) {
		this.kundenstrasse = kundenstrasse;
	}

	public String getFreitxt2_label() {
		return freitxt2_label;
	}

	public void setFreitxt2_label(String freitxt2_label) {
		this.freitxt2_label = freitxt2_label;
	}

	public String getFreitxt2_inhalt() {
		return freitxt2_inhalt;
	}

	public void setFreitxt2_inhalt(String freitxt2_inhalt) {
		this.freitxt2_inhalt = freitxt2_inhalt;
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

	public String getKundentelefon() {
		return kundentelefon;
	}

	public void setKundentelefon(String kundentelefon) {
		this.kundentelefon = kundentelefon;
	}

	public String getKundenbenutzernr() {
		return kundenbenutzernr;
	}

	public void setKundenbenutzernr(String kundenbenutzernr) {
		this.kundenbenutzernr = kundenbenutzernr;
	}

	public String getFreitxt3_label() {
		return freitxt3_label;
	}

	public void setFreitxt3_label(String freitxt3_label) {
		this.freitxt3_label = freitxt3_label;
	}

	public String getFreitxt3_inhalt() {
		return freitxt3_inhalt;
	}

	public void setFreitxt3_inhalt(String freitxt3_inhalt) {
		this.freitxt3_inhalt = freitxt3_inhalt;
	}

	public String getRadiobutton() {
		return radiobutton;
	}

	public void setRadiobutton(String radiobutton) {
		this.radiobutton = radiobutton;
	}

	public String getRadiobutton_name() {
		return radiobutton_name;
	}

	public void setRadiobutton_name(String radiobutton_name) {
		this.radiobutton_name = radiobutton_name;
	}

	public String getKundenlieferart1() {
		return kundenlieferart1;
	}

	public void setKundenlieferart1(String kundenlieferart1) {
		this.kundenlieferart1 = kundenlieferart1;
	}

	public String getKundenlieferart2() {
		return kundenlieferart2;
	}

	public void setKundenlieferart2(String kundenlieferart2) {
		this.kundenlieferart2 = kundenlieferart2;
	}

	public String getKundenlieferart3() {
		return kundenlieferart3;
	}

	public void setKundenlieferart3(String kundenlieferart3) {
		this.kundenlieferart3 = kundenlieferart3;
	}

	public String getKundenland() {
		return kundenland;
	}

	public void setKundenland(String kundenland) {
		this.kundenland = kundenland;
	}

	public List<Countries> getCountries() {
		return countries;
	}

	public void setCountries(List<Countries> countries) {
		this.countries = countries;
	}

	public String getGebuehren() {
		return gebuehren;
	}

	public void setGebuehren(String gebuehren) {
		this.gebuehren = gebuehren;
	}

	public String getAgb() {
		return agb;
	}

	public void setAgb(String agb) {
		this.agb = agb;
	}

	public AbstractBenutzer getBenutzer() {
		return bestellung.getBenutzer();
	}

	public Konto getKonto() {
		return bestellung.getKonto();
	}

	public Date getOrderdate() {
		Date d = Date.valueOf(bestellung.getOrderdate());
		return d;
	}

	public String getPreis() {
		return Preis;
	}

	public void setPreis(String preis) {
		Preis = preis;
	}

	public String getPriority() {		
		return getPrio();
	}

	public String getVerlag_encoded() {
		return verlag_encoded;
	}

	public void setVerlag_encoded(String verlag_encoded) {
		this.verlag_encoded = verlag_encoded;
	}

	public String getKapitel_encoded() {
		return kapitel_encoded;
	}

	public void setKapitel_encoded(String kapitel_encoded) {
		this.kapitel_encoded = kapitel_encoded;
	}

	public String getBuchtitel_encoded() {
		return buchtitel_encoded;
	}

	public void setBuchtitel_encoded(String buchtitel_encoded) {
		this.buchtitel_encoded = buchtitel_encoded;
	}

	public boolean isPreisdefault() {
		return preisdefault;
	}

	public void setPreisdefault(boolean preisdefault) {
		this.preisdefault = preisdefault;
	}

	public boolean isCheckDidYouMean() {
		return checkDidYouMean;
	}

	public void setCheckDidYouMean(boolean checkDidYouMean) {
		this.checkDidYouMean = checkDidYouMean;
	}

	public boolean isAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(boolean autocomplete) {
		this.autocomplete = autocomplete;
	}

	public boolean isFlag_noissn() {
		return flag_noissn;
	}

	public void setFlag_noissn(boolean flag_noissn) {
		this.flag_noissn = flag_noissn;
	}

	public boolean isManuell() {
		return manuell;
	}

	public void setManuell(boolean manuell) {
		this.manuell = manuell;
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isResolve() {
		return resolve;
	}

	public void setResolve(boolean resolve) {
		this.resolve = resolve;
	}

	public boolean isResolver() {
		return resolver;
	}

	public void setResolver(boolean resolver) {
		this.resolver = resolver;
	}

	public boolean isCarelit() {
		return carelit;
	}

	public void setCarelit(boolean carelit) {
		this.carelit = carelit;
	}

	public boolean isFromstock() {
		return fromstock;
	}

	public void setFromstock(boolean fromstock) {
		this.fromstock = fromstock;
	}

	/**
	 * Liefert je nach <br>
	 * mediatype = book -> Buchtitel oder <br>
	 * mediatype = journal -> Artikeltitel <br>
	 * als Titel
	 * @return titel
	 * @author Pascal Steiner
	 */
	public String getTitel() {
		String titel = null;
		if (mediatype.equals("book")){
			titel = getBuchtitel();
		}
		if (mediatype.equals("journal")){
			titel = getArtikeltitel();
		}
		return titel;
	}

	/**
	 * Liefert je nach <br>
	 * mediatype = book -> Verlag oder <br>
	 * mediatype = journal -> Zeitschrift <br>
	 * als Titel
	 * @return titel
	 * @author Pascal Steiner
	 */
	public String getZeitschrift_verlag() {
		String zeitschrift_verlag = null;
		if (mediatype.equals("book")){
			zeitschrift_verlag = getVerlag();
		}
		if (mediatype.equals("journal")){
			zeitschrift_verlag = getZeitschriftentitel();
		}		
		return zeitschrift_verlag;
	}
    
    

}
