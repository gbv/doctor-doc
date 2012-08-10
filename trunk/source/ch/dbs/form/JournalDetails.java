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

import org.apache.struts.action.ActionForm;

import util.CodeUrl;

public final class JournalDetails extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    
    final CodeUrl codeUrl = new CodeUrl();
    
    private String artikeltitel;
    private String zeitschriftentitel;
    private String author;
    private String jahr;
    private String jahrgang;
    private String heft;
    private String seiten;
    private String link;
    private String url_text;
    private String issn;
    private String submit = "";
    
    // these get set automatically. There are not public setter methods. Only getter methods are available.
    private String artikeltitel_encoded; // ISO-8859-1
    private String artikeltitel_encodedUTF8;
    private String zeitschriftentitel_encoded; // ISO-8859-1
    private String zeitschriftentitel_encodedUTF8;
    private String author_encoded; // ISO-8859-1
    private String author_encodedUTF8;
    
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
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
        this.author_encoded = codeUrl.encode(author, "ISO-8859-1");
        this.author_encodedUTF8 = codeUrl.encode(author, "UTF-8");
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
    
    public String getSeiten() {
        return seiten;
    }
    
    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }
    
    public String getIssn() {
        return issn;
    }
    
    public void setIssn(final String issn) {
        this.issn = issn;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(final String link) {
        this.link = link;
    }
    
    public String getUrl_text() {
        return url_text;
    }
    
    public void setUrl_text(final String url_text) {
        this.url_text = url_text;
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
    
    public String getSubmit() {
        return submit;
    }
    
    public void setSubmit(final String submit) {
        this.submit = submit;
    }
    
    public String getZeitschriftentitel_encodedUTF8() {
        return zeitschriftentitel_encodedUTF8;
    }
    
    public String getArtikeltitel_encodedUTF8() {
        return artikeltitel_encodedUTF8;
    }
    
    public String getAuthor_encodedUTF8() {
        return author_encodedUTF8;
    }
    
    public String getAuthor_encoded() {
        return author_encoded;
    }
    
}
