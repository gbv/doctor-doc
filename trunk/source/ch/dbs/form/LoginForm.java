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

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.Konto;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the form data needed on the login page, which is all needed output data to display the
 * form and all input properties once the user entered its login data which will then be verified with
 * the backend.
 * <p/>
 * @author Pascal Steiner
 */
public class LoginForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private Long kontoid;
	private Long userid;
	private ArrayList<UserInfo> userinfolist;
  
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
  private String genre = ""; // dient dazu alle nicht abfangbaren Publikationsarten, wie Pamphlet, Proceeding etc. anzugeben
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
  private String kundentelefon;
  private String kundenadresse;
  private String kundenplz;
  private String kundenort;
  private String kundenland;


public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public ArrayList<UserInfo> getUserinfolist() {
	return userinfolist;
}

public void setUserinfolist(ArrayList<UserInfo> userinfolist) {
	this.userinfolist = userinfolist;
}


public Long getUserid() {
	return userid;
}

public void setUserid(Long userid) {
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
public void setKonto(List<Konto> konto) {
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
public void setKontoid(Long kontoid) {
    this.kontoid = kontoid;
}

public String getIssn() {
	return issn;
}

public void setIssn(String issn) {
	this.issn = issn;
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

public String getForuser() {
	return foruser;
}

public void setForuser(String foruser) {
	this.foruser = foruser;
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




  
}
