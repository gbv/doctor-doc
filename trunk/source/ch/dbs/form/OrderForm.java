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

public final class OrderForm extends ValidatorForm implements OrderHandler {
    
    private static final SimpleLogger LOG = new SimpleLogger(OrderForm.class);
    
    final transient CodeUrl codeUrl = new CodeUrl();
    
    private static final long serialVersionUID = 1L;
    private transient List<Bestellungen> bestellungen;
    private transient List<OrderState> states;
    private transient List<Text> statitexts;
    private transient List<Lieferanten> quellen;
    private transient List<Text> waehrungen;
    private transient List<DefaultPreis> defaultpreise;
    private transient Bestellungen bestellung;
    private transient Lieferanten lieferant; // neue Verknüpfung zu Tabelle Lieferanten
    private String bestellquelle; // dient lediglich noch der Führung des doppelten Eintrages in der DB
    private String submit = ""; // dient zur Unterscheidung welcher Submit-Knopf in einem Formular gewählt wurde
    private Long bid; // Order-ID
    private String uid;
    private String lid;
    private String artikeltitel = "";
    private String heft = "";
    private String jahr = "";
    private String jahrgang = "";
    private String issn = "";
    private String zdbid; // may be null => important in view logic
    private String ppn; // Pica-Production-Number => unique media-ID in the GBV
    private String fileformat = "";
    private String deloptions = "";
    private String prio = "";
    private String author = "";
    private String seiten = "";
    private String foruser = "0"; // ID of patron/customer. 0 = "Please select" in the select menu
    private String sessionid;
    private String bibliothek = "";
    private String subitonr;
    private String gbvnr;
    // unique order-# for the communication between different orderingsystems (e.g. GBV-Order)
    private String trackingnr = "";
    private String interne_bestellnr = ""; // if a library uses an own internal numbering system
    private String sigel = "";
    private String faxno;
    private String zeitschriftentitel = "";
    private String contents;
    private String link;
    private String anmerkungen = "";
    private String notizen = "";
    private String status;
    private String preisvorkomma;
    private String preisnachkomma;
    private String waehrung;
    private String signatur = "";
    private boolean preisdefault;
    private BigDecimal kaufpreis;
    private String orderlink;
    private boolean autocomplete; // wird zur Kontrolle für die Funktion Autocomplete verwendet
    private boolean flag_noissn; // wird gesetzt, falls Autocomplete keine ISSN liefert
    private int runs_autocomplete;
    private boolean manuell;
    private boolean erledigt;
    private boolean delete;
    private String origin; // kann für forward benutzt werden, (nicht direkt, nur mit Werteprüfung!)
    private List<ErrorMessage> links;
    private transient List<AbstractBenutzer> kontouser;
    // u.a. ip-basiertes Kunden-Bestellform
    private String language; // Sprache des Dokumentes
    private String doi = ""; // Digital  Object Identifier
    private String pmid = ""; // Pubmed-ID
    private String sici = ""; // Serial Item and Contribution Identifier
    private String lccn = ""; // Library of Congress Number
    private String isbn = "";
    // dient dazu alle nicht abfangbaren Publikationsarten, wie Pamphlet, Proceeding etc. anzugeben
    private String genre = "";
    private String rfr_id = ""; // Referrent-ID (Angabe woher die OpenURL-Anfrage stammt)
    private String mediatype = "Artikel"; // Artikel, Buch oder Teilkopie Buch, Defaultwert Artikel
    private String verlag = ""; // Buchverlag
    private String kapitel = "";
    private String buchtitel = "";
    private String kundenvorname;
    private String kundenname;
    private String kundenmail;
    // hier folgt der parametrisierbare Teil...
    private String kundenlieferart1;
    private String kundenlieferart2;
    private String kundenlieferart3;
    private String kundeninstitution;
    private String kundenabteilung;
    private String kundenkategorieID = "0";
    private String freitxt1_label;
    private String freitxt1_inhalt;
    private String kundenadresse;
    private String kundenstrasse;
    private String freitxt2_label;
    private String freitxt2_inhalt;
    private String kundenplz;
    private String kundenort;
    private String kundenland;
    private transient List<Countries> countries;
    private String kundentelefon;
    private String kundenbenutzernr;
    private String freitxt3_label;
    private String freitxt3_inhalt;
    private String radiobutton_name = "";
    private String radiobutton = "";
    private String gebuehren;
    private String agb;
    // ...Ende parametrisierbarer Teil
    // stellt sicher, dass eine PMID nur auf manuellen Befehl aufgelöst wird (OpenURL und Bestellformular)
    private boolean resolve;
    private boolean resolver;
    private boolean carelit; // gibt es bei Carelit Volltexte?
    private String kkid; // Kontokennung (anstelle von IP-basiertem Zugriff)
    private String bkid; // Borkerkennung
    // OpenUrl
    private String url = "";
    // Captcha
    private String captcha_id;
    private String captcha_text;
    // GBV
    private String gbvsearch; // Suchbegriff
    private String gbvfield; // Suchfeld
    private int treffer_total;
    private int forwrd;
    private int back;
    private String maximum_cost = "";
    
    //DDL
    private String Preis;
    
    // these get set automatically. There are not public setter methods. Only getter methods are available.
    private String artikeltitel_encoded = ""; // ISO-8859-1
    private String artikeltitel_encodedUTF8 = "";
    private String author_encoded = ""; // ISO-8859-1
    private String author_encodedUTF8 = "";
    private String zeitschriftentitel_encoded = ""; // ISO-8859-1
    private String zeitschriftentitel_encodedUTF8 = "";
    private String verlag_encoded = ""; // ISO-8859-1
    private String verlag_encodedUTF8 = "";
    private String kapitel_encoded = ""; // ISO-8859-1
    private String kapitel_encodedUTF8 = "";
    private String buchtitel_encoded = ""; // ISO-8859-1
    private String buchtitel_encodedUTF8 = "";
    
    public OrderForm() {
        
    }
    
    public OrderForm(final Bestellungen b) {
        this.setBid(b.getId());
        
        this.setMediatype(b.getMediatype());
        this.setArtikeltitel(b.getArtikeltitel());
        this.setAuthor(b.getAutor());
        this.setBuchtitel(b.getBuchtitel());
        this.setDoi(b.getDoi());
        if (b.getBenutzer() != null && b.getBenutzer().getId() != null) {
            this.setForuser(Long.toString(b.getBenutzer().getId()));
            this.setUid(Long.toString(b.getBenutzer().getId()));
        } else {
            this.setForuser("0");
        }
        this.setHeft(b.getHeft());
        this.setIsbn(b.getIsbn());
        this.setIssn(b.getIssn());
        this.setJahr(b.getJahr());
        this.setJahrgang(b.getJahrgang());
        this.setKapitel(b.getKapitel());
        this.setPmid(b.getPmid());
        this.setSeiten(b.getSeiten());
        this.setVerlag(b.getVerlag());
        this.setZeitschriftentitel(b.getZeitschrift());
        
        this.setLieferant(b.getLieferant());
        this.setBestellquelle(b.getBestellquelle());
        this.setPrio(b.getPriority());
        this.setFileformat(b.getFileformat());
        this.setDeloptions(b.getDeloptions());
        this.setSubitonr(b.getSubitonr());
        this.setGbvnr(b.getGbvnr());
        this.setTrackingnr(b.getTrackingnr());
        this.setInterne_bestellnr(b.getInterne_bestellnr());
        this.setSigel(b.getSigel());
        this.setBibliothek(b.getBibliothek());
        this.setNotizen(b.getNotizen());
        this.setStatus(b.getStatustext());
        this.setWaehrung(b.getWaehrung());
        // nur Abfüllen wenn Preis und Währung vorhanden!
        if (b.getWaehrung() != null) {
            this.setKaufpreis(b.getKaufpreis());
        }
        this.setSignatur(b.getSignatur());
        this.setAnmerkungen(b.getSystembemerkung());
        
        final OrderForm of = bigDecimalToString(this);
        this.setPreisvorkomma(of.getPreisvorkomma());
        this.setPreisnachkomma(of.getPreisnachkomma());
        
    }
    
    public OrderForm(final LoginForm lf) {
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
        this.setForuser(lf.getForuser());
    }
    
    public OrderForm(final KontoForm kf) {
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
        this.setAuthor(kf.getAuthor());
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
    }
    
    /**
     * Ergänzt ein OrderForm bei den fehlenden Angaben mit den Angaben eines
     * zweiten OrderForms
     * 
     * @param OrderForm tocomplete
     * @param OrderForm tocompare
     * @return OrderForm tocomplete
     */
    public OrderForm completeOrderForm(final OrderForm tocomplete, final OrderForm tocompare) {
        
        // TODO: systematisch auscodieren...
        
        try {
            
            if (tocomplete.getArtikeltitel().equals("")) {
                tocomplete.setArtikeltitel(tocompare.getArtikeltitel());
            }
            if (tocomplete.getAuthor().equals("")) {
                tocomplete.setAuthor(tocompare.getAutor());
            }
            if (tocomplete.getDoi().equals("")) {
                tocomplete.setDoi(tocompare.getDoi());
            }
            if (tocomplete.getHeft().equals("")) {
                tocomplete.setHeft(tocompare.getHeft());
            }
            if (tocomplete.getIssn().equals("")) {
                tocomplete.setIssn(tocompare.getIssn());
            }
            if (tocomplete.getJahr().equals("")) {
                tocomplete.setJahr(tocompare.getJahr());
            }
            if (tocomplete.getJahrgang().equals("")) {
                tocomplete.setJahrgang(tocompare.getJahrgang());
            }
            if (tocomplete.getPmid().equals("")) {
                tocomplete.setPmid(tocompare.getPmid());
            }
            if (tocomplete.getSeiten().equals("")) {
                tocomplete.setSeiten(tocompare.getSeiten());
            }
            if (tocomplete.getZeitschriftentitel().equals("")) {
                tocomplete.setZeitschriftentitel(tocompare.getZeitschriftentitel());
            }
            
        } catch (final Exception e) {
            LOG.error("completeOrderForm: " + e.toString());
        }
        
        return tocomplete;
    }
    
    /**
     * Checks if there are missing relevant article details (ISSN, journal
     * title, author, year, volume, issue, pages).
     */
    public boolean areArticleValuesMissing() {
        
        boolean check = false;
        
        try {
            
            if (this.getMediatype().equals("Artikel") // um zu verhindern, dass bei eine Übergabe aus OpenURL auch Bücher
                    // über Pubmed etc. geprüft werden
                    && (this.getIssn().equals("") || this.getZeitschriftentitel().equals("")
                            || this.getAuthor().equals("") || this.getJahr().equals("")
                            || this.getArtikeltitel().equals("") || this.getJahrgang().equals("")
                            || this.getHeft().equals("") || this.getSeiten().equals(""))) {
                check = true;
            }
            
        } catch (final Exception e) {
            LOG.error("areArticleValuesMissing: " + e.toString());
            
        }
        
        return check;
    }
    
    public String getSubmit() {
        return submit;
    }
    
    public void setSubmit(final String submit) {
        this.submit = submit;
    }
    
    public List<OrderState> getStates() {
        return states;
    }
    
    public void setStates(final List<OrderState> states) {
        this.states = states;
    }
    
    public List<Text> getStatitexts() {
        return statitexts;
    }
    
    public void setStatitexts(final List<Text> statitexts) {
        this.statitexts = statitexts;
    }
    
    public List<DefaultPreis> getDefaultpreise() {
        return defaultpreise;
    }
    
    public void setDefaultpreise(final List<DefaultPreis> defaultpreise) {
        this.defaultpreise = defaultpreise;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getLid() {
        return lid;
    }
    
    public void setLid(final String lid) {
        this.lid = lid;
    }
    
    public List<Lieferanten> getQuellen() {
        return quellen;
    }
    
    public void setQuellen(final List<Lieferanten> quellen) {
        this.quellen = quellen;
    }
    
    public Lieferanten getLieferant() {
        return lieferant;
    }
    
    public void setLieferant(final Lieferanten lieferant) {
        this.lieferant = lieferant;
    }
    
    public String getBestellquelle() {
        return bestellquelle;
    }
    
    public void setBestellquelle(final String bestellquelle) {
        this.bestellquelle = bestellquelle;
    }
    
    public String getRfr_id() {
        return rfr_id;
    }
    
    public void setRfr_id(final String rfr_id) {
        this.rfr_id = rfr_id;
    }
    
    public String getOrderlink() {
        return orderlink;
    }
    
    public void setOrderlink(final String orderlink) {
        this.orderlink = orderlink;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(final String link) {
        this.link = link;
    }
    
    public String getContents() {
        return contents;
    }
    
    public void setContents(final String contents) {
        this.contents = contents;
    }
    
    public List<ErrorMessage> getLinks() {
        return links;
    }
    
    public void setLinks(final List<ErrorMessage> links) {
        this.links = links;
    }
    
    public String getZeitschriftentitel() {
        return zeitschriftentitel;
    }
    
    public void setZeitschriftentitel(final String zeitschriftentitel) {
        this.zeitschriftentitel = zeitschriftentitel;
        this.zeitschriftentitel_encoded = codeUrl.encode(zeitschriftentitel, "ISO-8859-1");
        this.zeitschriftentitel_encodedUTF8 = codeUrl.encode(zeitschriftentitel, "UTF-8");
    }
    
    public String getZeitschriftentitel_encoded() {
        return zeitschriftentitel_encoded;
    }
    
    public String getSigel() {
        return sigel;
    }
    
    public void setSigel(final String sigel) {
        this.sigel = sigel;
    }
    
    public String getFaxno() {
        return faxno;
    }
    
    public void setFaxno(final String faxno) {
        this.faxno = faxno;
    }
    
    public String getBibliothek() {
        return bibliothek;
    }
    
    public void setBibliothek(final String bibliothek) {
        this.bibliothek = bibliothek;
    }
    
    public String getSubitonr() {
        return subitonr;
    }
    
    public void setSubitonr(final String subitonr) {
        this.subitonr = subitonr;
    }
    
    public String getSessionid() {
        return sessionid;
    }
    
    public void setSessionid(final String sessionid) {
        this.sessionid = sessionid;
    }
    
    public String getForuser() {
        return foruser;
    }
    
    public void setForuser(final String foruser) {
        this.foruser = foruser;
    }
    
    public Bestellungen getBestellung() {
        return bestellung;
    }
    
    public void setBestellung(final Bestellungen bestellung) {
        this.bestellung = bestellung;
    }
    
    public List<Bestellungen> getBestellungen() {
        return bestellungen;
    }
    
    public void setBestellungen(final List<Bestellungen> bestellungen) {
        this.bestellungen = bestellungen;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(final String genre) {
        this.genre = genre;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public List<Text> getWaehrungen() {
        return waehrungen;
    }
    
    public void setWaehrungen(final List<Text> waehrungen) {
        this.waehrungen = waehrungen;
    }
    
    public String getSignatur() {
        return signatur;
    }
    
    public void setSignatur(final String signatur) {
        this.signatur = signatur;
    }
    
    public String getFileformat() {
        return fileformat;
    }
    
    public void setFileformat(final String fileformat) {
        this.fileformat = fileformat;
    }
    
    public String getDeloptions() {
        return deloptions;
    }
    
    public void setDeloptions(final String deloptions) {
        this.deloptions = deloptions;
    }
    
    public String getHeft() {
        return heft;
    }
    
    public void setHeft(final String heft) {
        this.heft = heft;
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
    
    public String getPpn() {
        return ppn;
    }
    
    public void setPpn(final String ppn) {
        this.ppn = ppn;
    }
    
    public String getJahr() {
        return jahr;
    }
    
    public String getCaptcha_id() {
        return captcha_id;
    }
    
    public void setCaptcha_id(final String captcha_id) {
        this.captcha_id = captcha_id;
    }
    
    public String getCaptcha_text() {
        return captcha_text;
    }
    
    public void setCaptcha_text(final String captcha_text) {
        this.captcha_text = captcha_text;
    }
    
    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }
    
    public String getPrio() {
        return prio;
    }
    
    public void setPrio(final String prio) {
        this.prio = prio;
    }
    
    public String getAutor() {
        return author;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
        this.author_encoded = codeUrl.encode(author, "ISO-8859-1");
        this.author_encodedUTF8 = codeUrl.encode(author, "UTF-8");
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getAuthor_encoded() {
        return author_encoded;
    }
    
    public String getSeiten() {
        return seiten;
    }
    
    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }
    
    public String getJahrgang() {
        return jahrgang;
    }
    
    public void setJahrgang(final String jahrgang) {
        this.jahrgang = jahrgang;
    }
    
    public Long getBid() {
        return bid;
    }
    
    public void setBid(final Long bid) {
        this.bid = bid;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(final String uid) {
        this.uid = uid;
    }
    
    public String getArtikeltitel() {
        return artikeltitel;
    }
    
    public void setArtikeltitel(final String artikeltitel) {
        this.artikeltitel = artikeltitel;
        this.artikeltitel_encoded = codeUrl.encode(artikeltitel, "ISO-8859-1");
        this.artikeltitel_encodedUTF8 = codeUrl.encode(artikeltitel, "UTF-8");
    }
    
    public String getArtikeltitel_encoded() {
        return artikeltitel_encoded;
    }
    
    public List<AbstractBenutzer> getKontouser() {
        return kontouser;
    }
    
    public void setKontouser(final List<AbstractBenutzer> kontouser) {
        this.kontouser = kontouser;
    }
    
    public String getAnmerkungen() {
        return anmerkungen;
    }
    
    public void setAnmerkungen(final String anmerkungen) {
        this.anmerkungen = anmerkungen;
    }
    
    public String getNotizen() {
        return notizen;
    }
    
    public void setNotizen(final String notizen) {
        this.notizen = notizen;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(final String origin) {
        this.origin = origin;
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
        this.verlag_encoded = codeUrl.encode(verlag, "ISO-8859-1");
        this.verlag_encodedUTF8 = codeUrl.encode(verlag, "UTF-8");
    }
    
    public String getKundenmail() {
        return kundenmail;
    }
    
    public void setKundenmail(final String kundenmail) {
        this.kundenmail = kundenmail;
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
    
    public String getPreisnachkomma() {
        return preisnachkomma;
    }
    
    public void setPreisnachkomma(final String preisnachkomma) {
        this.preisnachkomma = preisnachkomma;
    }
    
    public String getPreisvorkomma() {
        return preisvorkomma;
    }
    
    public void setPreisvorkomma(final String preisvorkomma) {
        this.preisvorkomma = preisvorkomma;
    }
    
    public String getWaehrung() {
        return waehrung;
    }
    
    public void setWaehrung(final String waehrung) {
        this.waehrung = waehrung;
    }
    
    public int getRuns_autocomplete() {
        return runs_autocomplete;
    }
    
    public void setRuns_autocomplete(final int runs_autocomplete) {
        this.runs_autocomplete = runs_autocomplete;
    }
    
    public BigDecimal getKaufpreis() {
        return kaufpreis;
    }
    
    public void setKaufpreis(final BigDecimal kaufpreis) {
        this.kaufpreis = kaufpreis;
    }
    
    public String getDoi() {
        return doi;
    }
    
    public void setDoi(final String doi) {
        this.doi = doi;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }
    
    public String getPmid() {
        return pmid;
    }
    
    public void setPmid(final String pmid) {
        this.pmid = pmid;
    }
    
    public String getLccn() {
        return lccn;
    }
    
    public void setLccn(final String lccn) {
        this.lccn = lccn;
    }
    
    public String getSici() {
        return sici;
    }
    
    public void setSici(final String sici) {
        this.sici = sici;
    }
    
    public String getKundenadresse() {
        return kundenadresse;
    }
    
    public void setKundenadresse(final String kundenadresse) {
        this.kundenadresse = kundenadresse;
    }
    
    public String getBuchtitel() {
        return buchtitel;
    }
    
    public void setBuchtitel(final String buchtitel) {
        this.buchtitel = buchtitel;
        this.buchtitel_encoded = codeUrl.encode(buchtitel, "ISO-8859-1");
        this.buchtitel_encodedUTF8 = codeUrl.encode(buchtitel, "UTF-8");
    }
    
    public String getKapitel() {
        return kapitel;
    }
    
    public void setKapitel(final String kapitel) {
        this.kapitel = kapitel;
        this.kapitel_encoded = codeUrl.encode(kapitel, "ISO-8859-1");
        this.kapitel_encodedUTF8 = codeUrl.encode(kapitel, "UTF-8");
    }
    
    public String getKkid() {
        return kkid;
    }
    
    public void setKkid(final String kkid) {
        this.kkid = kkid;
    }
    
    public String getBkid() {
        return bkid;
    }
    
    public void setBkid(final String bkid) {
        this.bkid = bkid;
    }
    
    public String getGbvsearch() {
        return gbvsearch;
    }
    
    public void setGbvsearch(final String gbvsearch) {
        this.gbvsearch = gbvsearch;
    }
    
    public String getGbvfield() {
        return gbvfield;
    }
    
    public void setGbvfield(final String gbvfield) {
        this.gbvfield = gbvfield;
    }
    
    public int getTreffer_total() {
        return treffer_total;
    }
    
    public void setTreffer_total(final int treffer_total) {
        this.treffer_total = treffer_total;
    }
    
    public int getForwrd() {
        return forwrd;
    }
    
    public void setForwrd(final int forwrd) {
        this.forwrd = forwrd;
    }
    
    public int getBack() {
        return back;
    }
    
    public void setBack(final int back) {
        this.back = back;
    }
    
    public String getMaximum_cost() {
        return maximum_cost;
    }
    
    public void setMaximum_cost(final String maximum_cost) {
        this.maximum_cost = maximum_cost;
    }
    
    public String getGbvnr() {
        return gbvnr;
    }
    
    public void setGbvnr(final String gbvnr) {
        this.gbvnr = gbvnr;
    }
    
    public String getInterne_bestellnr() {
        return interne_bestellnr;
    }
    
    public void setInterne_bestellnr(final String interne_bestellnr) {
        this.interne_bestellnr = interne_bestellnr;
    }
    
    public String getTrackingnr() {
        return trackingnr;
    }
    
    public void setTrackingnr(final String trackingnr) {
        this.trackingnr = trackingnr;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(final String language) {
        this.language = language;
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
    
    public String getKundenkategorieID() {
        return kundenkategorieID;
    }
    
    public void setKundenkategorieID(final String kundenkategorieID) {
        this.kundenkategorieID = kundenkategorieID;
    }
    
    public String getFreitxt1_label() {
        return freitxt1_label;
    }
    
    public void setFreitxt1_label(final String freitxt1_label) {
        this.freitxt1_label = freitxt1_label;
    }
    
    public String getFreitxt1_inhalt() {
        return freitxt1_inhalt;
    }
    
    public void setFreitxt1_inhalt(final String freitxt1_inhalt) {
        this.freitxt1_inhalt = freitxt1_inhalt;
    }
    
    public String getKundenstrasse() {
        return kundenstrasse;
    }
    
    public void setKundenstrasse(final String kundenstrasse) {
        this.kundenstrasse = kundenstrasse;
    }
    
    public String getFreitxt2_label() {
        return freitxt2_label;
    }
    
    public void setFreitxt2_label(final String freitxt2_label) {
        this.freitxt2_label = freitxt2_label;
    }
    
    public String getFreitxt2_inhalt() {
        return freitxt2_inhalt;
    }
    
    public void setFreitxt2_inhalt(final String freitxt2_inhalt) {
        this.freitxt2_inhalt = freitxt2_inhalt;
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
    
    public String getKundentelefon() {
        return kundentelefon;
    }
    
    public void setKundentelefon(final String kundentelefon) {
        this.kundentelefon = kundentelefon;
    }
    
    public String getKundenbenutzernr() {
        return kundenbenutzernr;
    }
    
    public void setKundenbenutzernr(final String kundenbenutzernr) {
        this.kundenbenutzernr = kundenbenutzernr;
    }
    
    public String getFreitxt3_label() {
        return freitxt3_label;
    }
    
    public void setFreitxt3_label(final String freitxt3_label) {
        this.freitxt3_label = freitxt3_label;
    }
    
    public String getFreitxt3_inhalt() {
        return freitxt3_inhalt;
    }
    
    public void setFreitxt3_inhalt(final String freitxt3_inhalt) {
        this.freitxt3_inhalt = freitxt3_inhalt;
    }
    
    public String getRadiobutton() {
        return radiobutton;
    }
    
    public void setRadiobutton(final String radiobutton) {
        this.radiobutton = radiobutton;
    }
    
    public String getRadiobutton_name() {
        return radiobutton_name;
    }
    
    public void setRadiobutton_name(final String radiobutton_name) {
        this.radiobutton_name = radiobutton_name;
    }
    
    public String getKundenlieferart1() {
        return kundenlieferart1;
    }
    
    public void setKundenlieferart1(final String kundenlieferart1) {
        this.kundenlieferart1 = kundenlieferart1;
    }
    
    public String getKundenlieferart2() {
        return kundenlieferart2;
    }
    
    public void setKundenlieferart2(final String kundenlieferart2) {
        this.kundenlieferart2 = kundenlieferart2;
    }
    
    public String getKundenlieferart3() {
        return kundenlieferart3;
    }
    
    public void setKundenlieferart3(final String kundenlieferart3) {
        this.kundenlieferart3 = kundenlieferart3;
    }
    
    public String getKundenland() {
        return kundenland;
    }
    
    public void setKundenland(final String kundenland) {
        this.kundenland = kundenland;
    }
    
    public List<Countries> getCountries() {
        return countries;
    }
    
    public void setCountries(final List<Countries> countries) {
        this.countries = countries;
    }
    
    public String getGebuehren() {
        return gebuehren;
    }
    
    public void setGebuehren(final String gebuehren) {
        this.gebuehren = gebuehren;
    }
    
    public String getAgb() {
        return agb;
    }
    
    public void setAgb(final String agb) {
        this.agb = agb;
    }
    
    public AbstractBenutzer getBenutzer() {
        return bestellung.getBenutzer();
    }
    
    public Konto getKonto() {
        return bestellung.getKonto();
    }
    
    public Date getOrderdate() {
        return Date.valueOf(bestellung.getOrderdate());
    }
    
    public String getPreis() {
        return Preis;
    }
    
    public void setPreis(final String preis) {
        Preis = preis;
    }
    
    public String getPriority() {
        return getPrio();
    }
    
    public String getVerlag_encoded() {
        return verlag_encoded;
    }
    
    public String getKapitel_encoded() {
        return kapitel_encoded;
    }
    
    public String getBuchtitel_encoded() {
        return buchtitel_encoded;
    }
    
    public boolean isPreisdefault() {
        return preisdefault;
    }
    
    public void setPreisdefault(final boolean preisdefault) {
        this.preisdefault = preisdefault;
    }
    
    public boolean isAutocomplete() {
        return autocomplete;
    }
    
    public void setAutocomplete(final boolean autocomplete) {
        this.autocomplete = autocomplete;
    }
    
    public boolean isFlag_noissn() {
        return flag_noissn;
    }
    
    public void setFlag_noissn(final boolean flag_noissn) {
        this.flag_noissn = flag_noissn;
    }
    
    public boolean isManuell() {
        return manuell;
    }
    
    public void setManuell(final boolean manuell) {
        this.manuell = manuell;
    }
    
    public boolean isErledigt() {
        return erledigt;
    }
    
    public void setErledigt(final boolean erledigt) {
        this.erledigt = erledigt;
    }
    
    public boolean isDelete() {
        return delete;
    }
    
    public void setDelete(final boolean delete) {
        this.delete = delete;
    }
    
    public boolean isResolve() {
        return resolve;
    }
    
    public void setResolve(final boolean resolve) {
        this.resolve = resolve;
    }
    
    public boolean isResolver() {
        return resolver;
    }
    
    public void setResolver(final boolean resolver) {
        this.resolver = resolver;
    }
    
    public boolean isCarelit() {
        return carelit;
    }
    
    public void setCarelit(final boolean carelit) {
        this.carelit = carelit;
    }
    
    public String getAuthor_encodedUTF8() {
        return author_encodedUTF8;
    }
    
    public String getArtikeltitel_encodedUTF8() {
        return artikeltitel_encodedUTF8;
    }
    
    public String getZeitschriftentitel_encodedUTF8() {
        return zeitschriftentitel_encodedUTF8;
    }
    
    public String getVerlag_encodedUTF8() {
        return verlag_encodedUTF8;
    }
    
    public String getKapitel_encodedUTF8() {
        return kapitel_encodedUTF8;
    }
    
    public String getBuchtitel_encodedUTF8() {
        return buchtitel_encodedUTF8;
    }
    
    /**
     * Liefert je nach <br> mediatype = book -> Buchtitel oder <br> mediatype =
     * journal -> Artikeltitel <br> als Titel
     * 
     * @return titel
     * @author Pascal Steiner
     */
    public String getTitel() {
        String titel = null;
        if ("book".equals(mediatype)) {
            titel = getBuchtitel();
        }
        if ("journal".equals(mediatype)) {
            titel = getArtikeltitel();
        }
        return titel;
    }
    
    /**
     * Liefert je nach <br> mediatype = book -> Verlag oder <br> mediatype =
     * journal -> Zeitschrift <br> als Titel
     * 
     * @return titel
     * @author Pascal Steiner
     */
    public String getZeitschrift_verlag() {
        String zeitschriftVerlag = null;
        if ("book".equals(mediatype)) {
            zeitschriftVerlag = getVerlag();
        }
        if ("journal".equals(mediatype)) {
            zeitschriftVerlag = getZeitschriftentitel();
        }
        return zeitschriftVerlag;
    }
    
    public OrderForm bigDecimalToString(final OrderForm pageForm) {
        
        if (pageForm.getKaufpreis() != null) {
            final BigDecimal bd = pageForm.getKaufpreis();
            
            final int vorkomma = bd.intValue();
            
            String nachkomma = bd.toString();
            nachkomma = nachkomma.substring(nachkomma.indexOf('.') + 1);
            if (nachkomma.length() == 1) {
                nachkomma = nachkomma + "0";
            }
            
            pageForm.setPreisvorkomma(Integer.toString(vorkomma));
            pageForm.setPreisnachkomma(nachkomma);
            
        }
        
        return pageForm;
    }
    
}
