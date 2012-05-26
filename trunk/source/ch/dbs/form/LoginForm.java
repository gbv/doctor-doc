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

import ch.dbs.entity.Konto;

/**
 * Encapsulates the form data needed on the login page, which is all needed output data to display the
 * form and all input properties once the user entered its login data which will then be verified with
 * the backend.
 * <p></p>
 * @author Pascal Steiner
 */
public class LoginForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
    private Long kontoid;
    private Long userid;
    private List<UserInfo> userinfolist;

    /**
     * Eine Liste der Institutionen, welche für das Login angeboten werden.
     */
    private List<Konto> konto;

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
    private String foruser = "0"; // enthält ID eines Benutzers. 0 = "Bitte auswählen" im Select

    //dient dazu die Userangaben zu übernehmen, ab Link aus Bestellform-Email

    private String kundenname;
    private String kundenvorname;
    private String kundenemail;
    private String kundeninstitution;
    private String kundenabteilung;
    private String category = "0";
    private String kundentelefon;
    private String kundenadresse;
    private String kundenplz;
    private String kundenort;
    private String kundenland;


    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<UserInfo> getUserinfolist() {
        return userinfolist;
    }

    public void setUserinfolist(final List<UserInfo> userinfolist) {
        this.userinfolist = userinfolist;
    }


    public Long getUserid() {
        return userid;
    }

    public void setUserid(final Long userid) {
        this.userid = userid;
    }

    /**
     * @return Returns the konto.
     */
    public List<Konto> getKonto() {
        return konto;
    }

    /**
     * @param konto The konto to set.
     */
    public void setKonto(final List<Konto> konto) {
        this.konto = konto;
    }

    /**
     * @return Returns the kontoid.
     */
    public Long getKontoid() {
        return kontoid;
    }

    /**
     * @param kontoid The kontoid to set.
     */
    public void setKontoid(final Long kontoid) {
        this.kontoid = kontoid;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(final String issn) {
        this.issn = issn;
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

    public String getForuser() {
        return foruser;
    }

    public void setForuser(final String foruser) {
        this.foruser = foruser;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
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

}
