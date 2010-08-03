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

package ch.dbs.actions.openurl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.grlea.log.SimpleLogger;
import util.CodeUrl;
import ch.dbs.actions.bestellung.OrderAction;
import ch.dbs.form.OrderForm;

public class ConvertOpenUrl {
	
	private static final SimpleLogger log = new SimpleLogger(ConvertOpenUrl.class);

    /**
     * Klasse um OpenURL in "Doctor-Doc" zu übersetzen
     * @author Markus Fischer
     */


	
	/**
	 * Interpretiert aus einem ContextObject ein OrderForm für Doctor-Doc.
	 * Relativ komplex und hier mit Vereinfachungen und Anpassungen umgesetzt...
	 */
	public OrderForm makeOrderform(ContextObject co) {
		
		OrderForm of = new OrderForm();
		boolean val_fmt = false; // bei OpenURL Version 1.0 gibt es relativ klare Medientyp-Zuweisungen, d.h. Kontrollmechansismen sind für Medientyp "Artikel" nicht notwendig
		
		if (!co.getRft_sici().equals("")) of = resolveSiciNumber(co.getRft_sici()); // falls SICI vorhanden, of soweit wie möglich vorabfüllen
		
		// zuerst allfällige Seitenbereiche eruieren => Unterscheidung von Buch / Teilkopie Buch
		
		if (co.getRft_spage()!=null && !co.getRft_spage().equals("")) {
			of.setSeiten(co.getRft_spage());
		}
		
		if (co.getRft_epage()!=null && !co.getRft_epage().equals("")) {
			of.setSeiten(of.getSeiten() + "-" + co.getRft_epage());
		}
		
		if (co.getRft_pages()!=null && !co.getRft_pages().equals("")) { // überschreibt spage und epage
			of.setSeiten(co.getRft_pages());
		}
		
		if (co.getRft_tpages()!=null && !co.getRft_tpages().equals("") && (of.getSeiten()==null || of.getSeiten().equals(""))) { // ...nur ausführen, falls Seiten nicht schon gesetzt
			of.setSeiten(co.getRft_tpages());
		}
		
		of.setMediatype("Artikel"); // Default-Wert, falls rft_val_fmt, resp. rft.genre nicht analysierbar..
		
		if (co.getRft_val_fmt()!=null && !co.getRft_val_fmt().equals("")) {
			if (co.getRft_val_fmt().contains("journal")) {
				of.setMediatype("Artikel");
				val_fmt = true;
			}
			if (co.getRft_val_fmt().contains("book")) {
				if ((co.getRft_genre()!=null && !co.getRft_genre().equals("") && co.getRft_genre().equalsIgnoreCase("bookitem")) ||
					!of.getSeiten().equals("")) { // enthält einen Seitenbereich
					of.setMediatype("Teilkopie Buch");
				} else {
					of.setMediatype("Buch");
				}
			}
		}
		
		if (co.getRft_genre()!=null && !co.getRft_genre().equals("")) { // falls rft_val_fmt fehlt...
			if (co.getRft_genre().toLowerCase().contains("journal") || co.getRft_genre().toLowerCase().contains("article")) of.setMediatype("Artikel");			
			if (co.getRft_genre().toLowerCase().contains("book") || co.getRft_genre().toLowerCase().contains("dissertation")) of.setMediatype("Buch"); // wird ggf. von bookitem / chapter überschrieben
			if (co.getRft_genre().toLowerCase().contains("bookitem") || co.getRft_genre().toLowerCase().contains("chapter")) of.setMediatype("Teilkopie Buch");  // hier inoffizieller Identifier
			if (co.getRft_genre().length()<30) of.setGenre(co.getRft_genre()); // zusätzlich über of Genre mitschicken (es sei denn, es sei überlang...)
		}
		
		if (co.getRft_atitle()!=null && !co.getRft_atitle().equals("")) {
			if (of.getMediatype().equals("Artikel")) {
				of.setArtikeltitel(removeEndDot(co.getRft_atitle())); // Artikeltitel
			} else {
				if (of.getMediatype().equals("Teilkopie Buch")) of.setKapitel(removeEndDot(co.getRft_atitle()));
				if (of.getMediatype().equals("Buch")) of.setBuchtitel(removeEndDot(co.getRft_atitle()));
			}
		}
		
		if (co.getRft_title()!=null && !co.getRft_title().equals("")) { // nur OpenURL 0.1
			if (of.getMediatype().equals("Artikel")) {
				of.setZeitschriftentitel(toNormalLetters(removeEndDot(co.getRft_title()))); // Zeitschrift
			} else {
				of.setBuchtitel(toNormalLetters(removeEndDot(co.getRft_title()))); // Buch
			}
		}
		
		if (co.getRft_jtitle()!=null && !co.getRft_jtitle().equals("")) { // OpenURL 1.0, überschreibt Rft_title
			if (of.getMediatype().equals("Artikel")) {
				of.setZeitschriftentitel(toNormalLetters(removeEndDot(co.getRft_jtitle()))); // Zeitschrift
			} else {
				of.setBuchtitel(toNormalLetters(removeEndDot(co.getRft_jtitle()))); // Buch [unwahrscheinlich]
			}
		}
		
		if (co.getRft_btitle()!=null && !co.getRft_btitle().equals("")) {
				of.setBuchtitel(toNormalLetters(removeEndDot(co.getRft_btitle())));
		}
		
		if (co.getRft_pub()!=null && !co.getRft_pub().equals("")) {
			of.setVerlag(co.getRft_pub());
	}
		
		if (co.getRft_publisher()!=null && !co.getRft_publisher().equals("") && 
			(of.getVerlag()==null || of.getVerlag().equals(""))) { // ...nur ausführen, falls Verlag noch nicht gesetzt
			of.setVerlag(co.getRft_publisher());
	}
		
		if (co.getRft_stitle()!=null && !co.getRft_stitle().equals("") && // stitle = Titelabkürzung...
			(of.getZeitschriftentitel()==null || of.getZeitschriftentitel().equals("")) && // ...nur ausführen, falls Titel noch nicht gesetzt
			(of.getBuchtitel()==null || of.getBuchtitel().equals("")) ) {
			if (of.getMediatype().equals("Artikel")) {
				of.setZeitschriftentitel(removeEndDot(co.getRft_stitle())); // Zeitschrift
			} else {
				of.setBuchtitel(removeEndDot(co.getRft_stitle())); // Buch [unwahrscheinlich]
			}
		}
		
		if (co.getRft_series()!=null && !co.getRft_series().equals("")) { // Serientitel
			if (of.getMediatype().equals("Artikel")) { // unwahrscheinlich
				of.setZeitschriftentitel(of.getZeitschriftentitel() + "\040-\040" + co.getRft_series());
			} else {
				of.setBuchtitel(of.getBuchtitel() + "\040-\040" + co.getRft_series());
			}
			
		}
		
		// if issn or eissn are empty and issn1 or eissn1 are available => set them
		if ((co.getRft_issn()==null || co.getRft_issn().equals("")) && (co.getRft_issn1()!=null && !co.getRft_issn1().equals("")) ) co.setRft_issn(co.getRft_issn1());
		if ((co.getRft_eissn()==null || co.getRft_eissn().equals("")) && (co.getRft_eissn1()!=null && !co.getRft_eissn1().equals("")) ) co.setRft_eissn(co.getRft_eissn1());
		
		if (co.getRft_issn()!=null && !co.getRft_issn().equals("")) {
				of.setIssn(normalizeIssn(extractFromSeveralIssns(co.getRft_issn())));
		}
		
		if (co.getRft_eissn()!=null && !co.getRft_eissn().equals("") && (of.getIssn()==null || of.getIssn().equals("")) ) { // ...nur ausführen, falls P-ISSN nicht schon gesetzt
			of.setIssn(normalizeIssn(extractFromSeveralIssns(co.getRft_eissn())));
		}
		
		if (co.getRft_isbn()!=null && !co.getRft_isbn().equals("")) {
			of.setIsbn(co.getRft_isbn());
		}
		
		if (co.getRft_date()!=null && !co.getRft_date().equals("")) {
			OrderAction orderActionInstance = new OrderAction();
			of.setJahr(orderActionInstance.extractYear(co.getRft_date()));
		}
		
		if (co.getRft_volume()!=null && !co.getRft_volume().equals("")) {
			of.setJahrgang(co.getRft_volume());
		}
		
		if (co.getRft_part()!=null && !co.getRft_part().equals("")) {
			if (of.getJahrgang()!=null && !of.getJahrgang().equals("")) {
				of.setJahrgang(of.getJahrgang() + "\040" + co.getRft_part()); // zum Jahrgang addieren
			} else {
				of.setJahrgang(co.getRft_part());
			}
		}
		
		if (co.getRft_issue()!=null && !co.getRft_issue().equals("")) {
			of.setHeft(co.getRft_issue());
		}
		
		if (co.getRft_author()!=null && !co.getRft_author().equals("")) {
			of.setAuthor(co.getRft_author());
		}
		
		if (co.getRft_au()!=null && !co.getRft_au().equals("") && (of.getAuthor()==null || of.getAuthor().equals(""))) { // nur ausführen, falls Author nicht schon gesetzt
			of.setAuthor(co.getRft_au());
		}
		
		if (co.getRft_creator()!=null && !co.getRft_creator().equals("") && (of.getAuthor()==null || of.getAuthor().equals(""))) { // inoffizieller Identif. ...nur ausführen, falls Author nicht schon gesetzt
			of.setAuthor(co.getRft_creator());
		}
		
		if (co.getRft_aulast()!=null && !co.getRft_aulast().equals("")) {
			String authorCopy = of.getAuthor(); // bis jetzt bereits vorhandene Einträge kopieren			
			// Normalfall (auinit enthält sämtliche Initialen der Vornamen)
			if (co.getRft_auinit()!=null && !co.getRft_auinit().equals("")) {
				// falls Vorname vorhanden, hintansetzen...
				of.setAuthor(co.getRft_aulast() + ",\040" + co.getRft_auinit());				
			} else { // Spezialfall (Initialen sind aufgeteilt)
				if (co.getRft_aufirst()!=null && !co.getRft_aufirst().equals("")) {
					if (co.getRft_auinitm()!=null && !co.getRft_auinitm().equals("")) {
						 // falls Vorname und Mittelinitialen vorhanden, hintansetzen...
						of.setAuthor(co.getRft_aulast() + ",\040" + co.getRft_aufirst() + "\040" + co.getRft_auinitm());	
					} else {
					of.setAuthor(co.getRft_aulast() + ",\040" + co.getRft_aufirst()); // falls Vorname vorhanden, hintansetzen...
					}
				} else {
					of.setAuthor(co.getRft_aulast());
				}				
			}
			// Einträge ggf. aneinanderhängen: Vorhandenes und neues Element dürfen nicht identisch sein.
			if (authorCopy!=null && !authorCopy.equals("") && !authorCopy.equals(of.getAuthor())) {
				of.setAuthor(of.getAuthor() + "\040;\040" + authorCopy) ;
			}
		}
		
		if (co.getRft_aucorp()!=null && !co.getRft_aucorp().equals("")) {
			if (of.getAuthor()!=null && !of.getAuthor().equals("")) {
				of.setAuthor(of.getAuthor() + " ; " + co.getRft_aucorp()); // zum Author addieren
			} else {
				of.setAuthor(co.getRft_aucorp());
			}
		}
		
		if (co.getRft_id()!=null) {
		for (int i=0;co.getRft_id().size()>i;i++) { // hier werden URIs ausgelsen und ins co geschrieben			
			of = resolveUriScheme(co.getRft_id().get(i).toString(), of);			
			}
		}
		
		// Kontrollmechanismen
		if (!val_fmt && of.getMediatype().equals("Buch") && isBookItem(of)) of.setMediatype("Teilkopie Buch");
		if (!val_fmt && of.getMediatype().equals("Artikel") && isBook(of)) of.setMediatype("Buch");
		
		if (!val_fmt && of.getMediatype().equals("Artikel") && isBookItem(of)) {
			of.setMediatype("Teilkopie Buch");
			if (of.getKapitel()==null || of.getKapitel().equals("")) of.setKapitel(co.getRft_atitle()); // Nach Umstellen auf Teilkopie sicherstellen dass Kapitel abgefüllt wird
			if (of.getArtikeltitel()!=null || !of.getArtikeltitel().equals("")) of.setArtikeltitel(""); // ggf. Artikeltitel leeren
		}
		// Spezialfall E-Books von Springer
		if (of.getMediatype().equals("Artikel") && isSpringerBook(of) && !of.getDoi().contains("_")) {
			of.setMediatype("Buch");
			if (of.getBuchtitel()==null || of.getBuchtitel().equals("")) of.setBuchtitel(co.getRft_atitle()); // Nach Umstellen auf Buch sicherstellen dass Buchtitel abgefüllt wird
			if (of.getArtikeltitel()!=null || !of.getArtikeltitel().equals("")) of.setArtikeltitel(""); // ggf. Artikeltitel leeren
			if (of.getIsbn()==null || of.getIsbn().equals("")) of.setIsbn(getIsbnSpringerBook(of.getDoi()));
		}
		if (of.getMediatype().equals("Artikel") && isSpringerBook(of) && of.getDoi().contains("_")) {
			of.setMediatype("Teilkopie Buch");
			if (of.getKapitel()==null || of.getKapitel().equals("")) of.setKapitel(co.getRft_atitle()); // Nach Umstellen auf Teilkopie sicherstellen dass Kapitel abgefüllt wird
			if (of.getBuchtitel()==null || of.getBuchtitel().equals("")) of.setBuchtitel(co.getRft_title()); // Nach Umstellen auf Teilkopie sicherstellen dass Buchtitel abgefüllt wird
			if (of.getArtikeltitel()!=null || !of.getArtikeltitel().equals("")) of.setArtikeltitel(""); // ggf. Artikeltitel leeren
			if (of.getIsbn()==null || of.getIsbn().equals("")) of.setIsbn(getIsbnSpringerBook(of.getDoi()));
		}
		
		if (of.getMediatype().equals("Artikel") && of.getIssn().length()==0) of.setFlag_noissn(true);
		
		// ggf. Sprache des Artikels setzen
		if (co.getRft_language()!=null && !co.getRft_language().equals("") &&
		   !co.getRft_language().equalsIgnoreCase("English")) of.setLanguage(co.getRft_language()); // nur Sprachangabe wenn NICHT englisch
		
		return of;
	}
	
	/**
	 * Stellt aus einem Ordeform ein ContextObject her.
	 * 
	 */
	public ContextObject makeContextObject(OrderForm of) {
		
		ContextObject co = new ContextObject();
		CodeUrl codeUrl = new CodeUrl();
		
		if (of.getMediatype().equals("Artikel")) co.setRft_val_fmt(codeUrl.encodeLatin1("info:ofi/fmt:kev:mtx:journal"));
		if (of.getMediatype().contains("Buch")) co.setRft_val_fmt(codeUrl.encodeLatin1("info:ofi/fmt:kev:mtx:book"));		
//		co.setRfr_id(UrlCode.encode("DRDOC:doctor-doc")); // Aus Kompatibilität mit OpenURl 01.1 momentan ausserhalb dieser Methode zu implementieren
		
		if (of.getMediatype().equals("Artikel")) co.setRft_genre(codeUrl.encodeLatin1("article"));
		if (of.getMediatype().equals("Buch")) co.setRft_genre(codeUrl.encodeLatin1("book"));
		if (of.getMediatype().equals("Teilkopie Buch")) co.setRft_genre(codeUrl.encodeLatin1("bookitem"));
		
		co.setRft_atitle(codeUrl.encodeLatin1(of.getArtikeltitel()));
		co.setRft_btitle(codeUrl.encodeLatin1(of.getBuchtitel()));
		co.setRft_jtitle(codeUrl.encodeLatin1(of.getZeitschriftentitel()));
		co.setRft_pub(codeUrl.encodeLatin1(of.getVerlag())); // fragwürdig, da vermutlich nicht immer im korrekten Format...
		co.setRft_issn(codeUrl.encodeLatin1(of.getIssn()));
		co.setRft_isbn(codeUrl.encodeLatin1(of.getIsbn()));
		co.setRft_date(codeUrl.encodeLatin1(of.getJahr()));
		co.setRft_volume(codeUrl.encodeLatin1(of.getJahrgang()));
		co.setRft_issue(codeUrl.encodeLatin1(of.getHeft()));
		co.setRft_pages(codeUrl.encodeLatin1(of.getSeiten()));
		co.setRft_spage(codeUrl.encodeLatin1(extractSpage(of.getSeiten())));
		co.setRft_author(codeUrl.encodeLatin1(of.getAuthor()));
		
		// TODO: private List rft_id;	// enthält uri-infos wie pmid, doi, lccn etc.: // http://www.info-uri.info/registry/
		
		return co;
	}
	
	/**
	 * Stellt aus einem Orderform einen "Doctor-Doc-String" für interne Get-Methoden her.
	 * 
	 */
	public String makeGetMethodString(OrderForm of) {
		
		StringBuffer sb = new StringBuffer();
		CodeUrl codeUrl = new CodeUrl();
		sb.append("mediatype=" + codeUrl.encodeLatin1(of.getMediatype())); // Default "Artikel" (s.a. OrderForm)
		if (of.getIssn()!=null && !of.getIssn().equals("")) sb.append("&issn=" + codeUrl.encodeLatin1(of.getIssn()));
		if (of.getJahr()!=null && !of.getJahr().equals("")) sb.append("&jahr=" + codeUrl.encodeLatin1(of.getJahr()));
		if (of.getJahrgang()!=null && !of.getJahrgang().equals("")) sb.append("&jahrgang=" + codeUrl.encodeLatin1(of.getJahrgang())); // könnte Leerschläge und Interpunktion enthalten
		if (of.getHeft()!=null && !of.getHeft().equals("")) sb.append("&heft=" + codeUrl.encodeLatin1(of.getHeft())); // Leerschläge etc.
		if (of.getSeiten()!=null && !of.getSeiten().equals("")) sb.append("&seiten=" + codeUrl.encodeLatin1(of.getSeiten())); // Leerschläge etc.
		if (of.getIsbn()!=null && !of.getIsbn().equals("")) sb.append("&isbn=" + of.getIsbn());
		if (of.getArtikeltitel()!=null && !of.getArtikeltitel().equals("")) sb.append("&artikeltitel=" + codeUrl.encodeLatin1(of.getArtikeltitel()));
		if (of.getZeitschriftentitel()!=null && !of.getZeitschriftentitel().equals("")) sb.append("&zeitschriftentitel=" + codeUrl.encodeLatin1(of.getZeitschriftentitel()));
		if (of.getAuthor()!=null && !of.getAuthor().equals("")) sb.append("&author=" + codeUrl.encodeLatin1(of.getAuthor()));
		if (of.getKapitel()!=null && !of.getKapitel().equals("")) sb.append("&kapitel=" + codeUrl.encodeLatin1(of.getKapitel()));
		if (of.getBuchtitel()!=null && !of.getBuchtitel().equals("")) sb.append("&buchtitel=" + codeUrl.encodeLatin1(of.getBuchtitel()));
		if (of.getVerlag()!=null && !of.getVerlag().equals("")) sb.append("&verlag=" + codeUrl.encodeLatin1(of.getVerlag()));
		if (of.getRfr_id()!=null && !of.getRfr_id().equals("")) sb.append("&rfr_id=" + codeUrl.encodeLatin1(of.getRfr_id()));
		if (of.getForuser()!=null && !of.getForuser().equals("0")) sb.append("&foruser=" + codeUrl.encodeLatin1(of.getForuser())); // falls in bestellform.sendOrder bereits ein User anhand der Email gefunden wurde
		if (of.getGenre()!=null && !of.getGenre().equals("")) sb.append("&genre=" + codeUrl.encodeLatin1(of.getGenre()));
		if (of.getPmid()!=null && !of.getPmid().equals("")) sb.append("&pmid=" + codeUrl.encodeLatin1(of.getPmid()));
		if (of.getDoi()!=null && !of.getDoi().equals("")) sb.append("&doi=" + codeUrl.encodeLatin1(of.getDoi()));
		if (of.getSici()!=null && !of.getSici().equals("")) sb.append("&sici=" + codeUrl.encodeLatin1(of.getSici())); // kann Sonderzeichen enthalten
		if (of.getZdbid()!=null && !of.getZdbid().equals("")) sb.append("&zdbid=" + codeUrl.encodeLatin1(of.getZdbid()));
		if (of.getLccn()!=null && !of.getLccn().equals("")) sb.append("&lccn=" + codeUrl.encodeLatin1(of.getLccn())); // http://www.info-uri.info/registry/
		
		return sb.toString();
	}
	
	/**
	 * Holt den Inhalt aus einem URI-Schema und legt ihn ins OrderForm
	 */
	private OrderForm resolveUriScheme(String input, OrderForm of) {
		
		// http://www.info-uri.info/registry/
		
		try {
			
			if (input.contains("info:doi/")) of.setDoi(input.substring(input.indexOf("info:doi/")));
			if (input.contains("info:pmid/")) of.setPmid(input.substring(input.indexOf("info:pmid/")));
			if (input.contains("info:sici/")) of.setSici(input.substring(input.indexOf("info:sici/")));
			if (input.contains("info:lccn/")) of.setLccn(input.substring(input.indexOf("info:lccn/")));
			
			
		} catch(Exception e){
    		log.error("resolveUriScheme: " + e.toString());    		
    	}
		
		return of;
	}
	
	/**
	 * Löst eine SICI-Number auf und legt die Angaben ins OrderForm
	 */
	private OrderForm resolveSiciNumber(String sici) {
		
		OrderForm of = new OrderForm();
		
		// http://en.wikipedia.org/wiki/SICI
		// ANSI/NISO Z39.56
		// 0002-8231(199412)45:10<737:TIODIM>2.3.TX;2-M => Abstract from Lynch, Clifford A. "The Integrity of Digital Information; Mechanics and Definitional Issues." JASIS 45:10 (Dec. 1994) p. 737-44
		
		try {
			
		if (sici.contains("(")) {
			int start = sici.indexOf("(");
			String tmp = "";
			if (start > 0) of.setIssn(normalizeIssn(sici.substring(0, start))); // ISSN-Nummer steht am Anfang
			if (sici.substring(start).contains(")")) tmp = sici.substring(start, sici.indexOf(")"));
			if (tmp.length()>4) of.setJahr(tmp.substring(1, 5)); // Jahreszahl sind die ersten vier Stellen in der Klammer
		}
		if (sici.contains(")") && sici.contains("<") && (sici.indexOf(")") < sici.indexOf("<")) ) {
			String segment = sici.substring(sici.indexOf(")")+1, sici.indexOf("<")); // enthält Jg:Heft
			if (segment.contains(":")) {
				of.setJahrgang(segment.substring(0, segment.indexOf(":")));
				of.setHeft(segment.substring(segment.indexOf(":")+1));				
			} else {
//				of.setJahrgang(segment); // Annahme, dass es sich um Jahrgang handelt...
			}
		}
		if (sici.contains("<") && sici.substring(sici.indexOf("<")).contains(":")) {
			of.setSeiten(sici.substring(sici.indexOf("<")+1, sici.indexOf(":", sici.indexOf("<")))); // Seiten nach < bis :
		}	
			
		} catch(Exception e){
			log.error("resolveSiciNumber: " + sici + "\040" + e.toString());    		
    	}
		
		return of;
	}
	
	/**
	 * Prüft, ob Bestellobjekt logischerweise ein Buch sein muss
	 */
	private boolean isBook(OrderForm of) {
		
		boolean check = false;		
		
		try {
			
			if (of.getIsbn()!=null && !of.getIsbn().equals("") &&
			   (of.getArtikeltitel()==null || of.getArtikeltitel().equals("")) &&
			   (of.getSeiten()==null || of.getSeiten().equals("")) ) { // ISBN vorhanden, aber kein Kapitel (hier noch Artikeltitel) oder spezifische Seitenangaben
				check = true;				
			}
			
			if (of.getBuchtitel()!=null && !of.getBuchtitel().equals("") &&
			   (of.getArtikeltitel()==null || of.getArtikeltitel().equals("")) &&
			   (of.getSeiten()==null || of.getSeiten().equals("")) ) { // Buchtitel vorhanden, aber kein Kapitel (hier noch Artikeltitel) oder spezifische Seitenangaben
				check = true;				
			}
				
			} catch(Exception e){
    		log.error("isBook: " + e.toString());    		
    	}
		
		return check;
	}
	
	/**
	 * Prüft, ob das Bestellobjekt logischerweise eine Teilkopie Buch sein muss
	 */
	private boolean isBookItem(OrderForm of) {
		
		boolean check = false;		
		
		try {
			
			if (of.getIsbn()!=null && !of.getIsbn().equals("") &&
					   ((of.getKapitel()!=null && !of.getKapitel().equals("")) ||
					   (of.getSeiten()!=null && !of.getSeiten().equals(""))) ) { // ISBN vorhanden und Kapitel oder Artikeltitel oder spezifische Angaben vorhanden
						check = true;				
					}
			
			if (of.getBuchtitel()!=null && !of.getBuchtitel().equals("") &&
					   ((of.getKapitel()!=null && !of.getKapitel().equals("")) ||
						(of.getArtikeltitel()!=null && !of.getArtikeltitel().equals("")) ||
					   (of.getSeiten()!=null && !of.getSeiten().equals(""))) ) { // Buchtitel vorhanden und Kapitel oder Artikeltitel oder spezifische Angaben vorhanden
						check = true;				
					}
			

				
			} catch(Exception e){
				log.error("isBookItem: " + e.toString());    		
			}
		
		return check;
	}
	
	/**
	 * Prüft, ob das Bestellobjekt ein Springer-Buch ist
	 */
	private boolean isSpringerBook(OrderForm of) {
		
		boolean check = false;		
		
		try {
			
			if (of.getDoi().contains("10.1007/3-") || of.getDoi().contains("10.1007/978-3-") || // deutsch / spanisch
				of.getDoi().contains("10.1007/1-") || of.getDoi().contains("10.1007/978-1-") || // englisch
				of.getDoi().contains("10.1007/0-") || of.getDoi().contains("10.1007/978-0-") || // englisch
				of.getDoi().contains("10.1007/2-") || of.getDoi().contains("10.1007/978-2-") || // französisch
				of.getDoi().contains("10.1007/90-") || of.getDoi().contains("10.1007/978-90-") || // niederländisch
				of.getDoi().contains("10.1007/88-") || of.getDoi().contains("10.1007/978-88-") ) { // italienisch / portugisisch
				check = true;
			}
				
			} catch(Exception e){
				log.error("isSpringerBook:" + e.toString());    		
			}
		
		return check;
	}
	
	/**
	 * Holt aus einer doi von Springer die ISBN
	 */
	private String getIsbnSpringerBook(String doi) {
		
		String isbn = "";		
		
		try {
			
			if (doi.contains("_")) {
				isbn = doi.substring(doi.indexOf("10.1007/")+8, doi.indexOf("_"));
			} else {
				isbn = doi.substring(doi.indexOf("10.1007/")+8);
			}
			

				
			} catch(Exception e){
				log.error("getIsbnSpringerBook: " + doi + "\040" + e.toString());    		
			}
		
		return isbn;
	}
	
	/**
	 * gets an ISSN out of a String with several ISSNs
	 * e.g. Refworks is sending multiple ISSN in the param:
	 * issn=1234-1234; 2345-2345
	 */
	public String extractFromSeveralIssns(String input) {
		
		
		try {			
			if (input!=null && input.length()>19 && // must have at least the lenght of two ISSNs plus a separation character
			   (input.contains(";") || input.contains(",")) ) { // Separation character is comma or semicolon
				if (input.contains(";")) {
					if (input.indexOf(";")>8) { // must have a ISSN number before the separtion character
						input = input.substring(0, input.indexOf(";")).trim(); // take the first ISSN
						}
				} else if (input.contains(",")) {
					if (input.indexOf(",")>8) { // must have a ISSN number before the separtion character
						input = input.substring(0, input.indexOf(",")).trim(); // take the first ISSN
						}					
				}		
			}			
			
		} catch(Exception e){
			log.error("extractFromSeveralIssns:\040" + input + "\040" + e.toString());    		
    	}
		
		return input;
	}
	
	/**
	 * Bringt eine ISSN ohne Bindestrich in die "normale Form"
	 */
	public String normalizeIssn(String input) {
		
		
		try {			
			if (input!=null && input.length()==8 && !input.contains("-")) {				
				input = input.substring(0, 4) + "-" + input.substring(4, 8);				
			}			
			
		} catch(Exception e){
			log.error("normalizeIssn:\040" + input + "\040" + e.toString());    		
    	}
		
		return input;
	}
	
	/**
	 * Entfernt Punkt am Ende eines Strings
	 */
	private String removeEndDot(String input) {
		
		try {
			
			if (input != null && !input.equals("") && input.endsWith(".")) {
				
				int l = input.length();
				if (input.lastIndexOf(".") == l-1) input = input.substring(0, input.lastIndexOf(".")); // falls letztes Zeichen "." => eliminieren
				
			}
			
			
		} catch(Exception e){
			log.error("removeEndDot: " + input + "\040" + e.toString());    		
    	}
		
		return input;
	}
	
	  public String extractSpage(String seiten) {
		  String spage = "";
		  if (seiten!=null && seiten.length()!=0) {
	  	  try{	  		  
	  	  Pattern z = Pattern.compile("[0-9]*");
		  Matcher w = z.matcher(seiten);
		  	  
	  	  if (w.find()) { // Idee: nur erste Zahl abfüllen...
	  		 spage = seiten.substring(w.start(), w.end()); // hier wird nur der erste Treffer abgefüllt...
	  	  	}
	  	  // if (spage.equals("")) spage = seiten;
	  	  }
	  	  catch (Exception e) {
	  		  log.error("extractSpage: " + seiten + "\040" + e.toString());		  
	  	  }
		  }
		
		  
		return spage;
      }	 
	  
	/**
	 * Setzt einen String aus lauter GROSSBUCHSTABEN in die englische Schreibeweise:
	 * 1. Buchstabe gross, alles andere klein. Z.B. aus METHODS IN ENZYMOLOGY => wird Methods in enzymology
	 */
	  private String toNormalLetters(String input) {
		        	  
	  	  try{
	  		  
	  		  if (input!=null && input.length()!=0 && !testLowercase(input)) {
	  			  
	  			  input = input.toLowerCase();
	  			  input= input.substring(0, 1).toUpperCase() + input.substring(1); 
	  			  
	  		  }
	  	  
	  	  }
	  	  catch (Exception e) {
	  		  log.error("toNormalLetters: " + input + "\040" + e.toString());		  
	  	  }		  
		
		  
		return input;
      }
	  
		/**
		 * Testet ob ein String Kleinbuchstaben enthält
		 * 
		 */
		  private boolean testLowercase(String input) {
			  
			  boolean check = false;
			  
			  if (input!=null && input.length()!=0) {
			  
		  	  try{
		  		  
		  		 Pattern z = Pattern.compile("\\p{Lower}");
				 Matcher w = z.matcher(input);
				  	  
			  	  if (w.find()) { // Testen, ob String Kleinbuchstaben enthält...
			  		 check = true;
			  	  	}
		  	  
		  	  }
		  	  catch (Exception e) {
		  		  log.error("testLowercase: " + input + "\040" + e.toString()); 		  
		  	  }	
			  }
			  
			return check;
	      }
	  
	  
	  

}