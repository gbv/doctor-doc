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

package ch.dbs.actions.bestellung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.Http;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.SpecialCharacters;
import util.CodeUrl;
import util.ThreadSafeSimpleDateFormat;
import util.ThreadedJournalSeek;
import util.ThreadedWebcontent;
import ch.dbs.actions.bestand.Stock;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.DefaultPreis;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.FindFree;
import ch.dbs.form.JournalDetails;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import ch.ddl.daia.DaiaRequest;

public final class OrderAction extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(OrderAction.class);
    
    /**
     * Methode zum pruefen, ob das Journal frei verfuegbar ist
     */
    public ActionForward findForFree(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp){
    	ArrayList<JournalDetails> trefferGoogle = new ArrayList<JournalDetails>();
    	ArrayList<JournalDetails> trefferGoogleScholar = new ArrayList<JournalDetails>();
    	Check check = new Check();
    	BestellformAction bestellFormActioInstance = new BestellformAction();
    	String link = "";
    	String content = "";
    	String linkPdfGoogle = "";
    	String linkPdfGoogleScholar = "";
    	
    	String forward = "failure";
    	OrderForm pageForm = (OrderForm) form;
    	Auth auth = new Auth();
    	CodeUrl codeUrl = new CodeUrl();
    	SpecialCharacters specialCharacters = new SpecialCharacters();
        
     // hier wird eine allfällige PMID aufgelöst
        if (pageForm.getArtikeltitel().toLowerCase().contains("pmid:")) {
        	pageForm = bestellFormActioInstance.resolvePmid(bestellFormActioInstance.extractPmid(pageForm.getArtikeltitel()));
        	pageForm.setAutocomplete(true); // vermeiden, dass noch zusätzlich Autocomplete ausgeführt wird
        	if (pageForm.getArtikeltitel().equals("")) forward = "pmidfailure"; // hier hat die Auflösung nicht geklappt => zurück zum Eingabeformular
        }
        
    	// ISO-8859-1 encoding is important for automatic search!
    	pageForm.setArtikeltitel_encoded(codeUrl.encodeLatin1(shortenGoogleSearchPhrase(pageForm.getArtikeltitel())));
        
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        if (auth.isLogin(rq)) {
        
        if (!forward.equals("pmidfailure")) {
        
        // ***Funktion Autocomplete ein erstes Mal ausführen
        if (pageForm.isAutocomplete()==false) pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
        	
        if (!auth.isBenutzer(rq) || // Automatische Google-Suche für Bibliothekare und Admins...
        	ReadSystemConfigurations.isAllowPatronAutomaticGoogleSearch()) { // ...und für User, falls aufgrund der SystemConfigurations erlaubt.
        	
        if (pageForm.getCaptcha_id()==null && pageForm.getCaptcha_text()==null) { // ...nur Google-Suche durchführen, falls nicht captcha.jsp dazwischengeschaltet wurde!
        	
	// Methoden um Verfuegbarkeit zu pruefen
        	
    // Google
    
//  Suchstufen:
    // 1. phrase + allintitle: + filetype:pdf
    // 2. phrase + allintitle + pdf extrahieren 
    // 3. "meinten Sie" phrase => Pruefung 1 und 2
    // 4. Stufe: Titel als Phrase + [text] pdf OR "full-text" => Ergebnis ohne PDF-Pruefung
    // 5. "meinten Sie" => Pruefung 4    	        	
        	
    int searches = 0;
	boolean ergebnis = false;
	boolean did_you_mean = false;
	int start = 0;
    String compare = "";
        
    while ( (ergebnis == false) &&
    		(searches < 6) ){
    	
    	if ( (searches==0) ||
    		 (searches==2) ){ // phrase + allintitle + filetype:pdf
    		link = "http://www.google.ch/search?as_q=&hl=de&num=10&btnG=Google-Suche&as_oq=&as_eq=&lr=&as_ft=i&as_filetype=pdf&as_qdr=all&as_occt=title&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
    	}
    	if ( (searches==1) ||
    		 (searches==3) ){ // phrase + allintitle
    		link = "http://www.google.ch/search?as_q=&hl=de&num=10&btnG=Google-Suche&as_oq=&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=title&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
        }    	
    	if ( (searches > 3) &&
    		 (searches < 6) ){
//    	http://www.google.ch/search?hl=de&q=%22text+als+phrase%22+pdf+OR+full-text&btnG=Google-Suche&meta=
//		Nummerierung auf 5 gestellt. Ev. auf 3 anpassen...!!!  		
    	link = "http://www.google.ch/search?as_q=&hl=de&num=5&btnG=Google-Suche&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
    	}	
    	if (did_you_mean == false){
    		link = link + pageForm.getArtikeltitel_encoded();
    	}
    	if (did_you_mean == true){ // Verwendung von String aus Methode googleDidYouMean
    		link = link + pageForm.getDidYouMean();
    	}
    	
    	content = getWebcontent(link, 1000, 3);
    	
//    	content = "<form action=\"Captcha\" method=\"get\">" +
//    	"<input type=\"hidden\" name=\"id\" value=\"17179006839024668804\">" +
//    	"<input type=\"text\" name=\"captcha\" value=\"\" id=\"captcha\" size=\"12\">" +
//    	"<img src=\"/sorry/image?id=17179006839024668804&amp;hl=de\" border=\"1\" alt=\"Falls Sie dies lesen können, ist die Bilddarstellung bei Ihnen deaktiviert. Aktivieren Sie die Bilddarstellung, um fortzufahren.\"></div>";
    	
    	try {
    	
    	if (check.containsGoogleCaptcha(content)) {
    		Message m = new Message();
    		m = handleGoogleCaptcha(content);
    		forward = "captcha";
    		rq.setAttribute("message", m);
    	}
    	} catch(Exception e) {
    		log.error("Problem treating captcha: " + e.toString() + "\012" + content);
    	}
    	

    if (searches < 4){
    	compare = "[PDF]";
    }
    else {
    	compare = "class=g>"; // neu
    }
	
	//	 Sicherstellen, dass Google nicht mit Captcha reagiert
	if (!check.containsGoogleCaptcha(content)) {
	
    if (content.contains(compare)){
    	ergebnis = true;
    	linkPdfGoogle ="";
    	
	    String content2 = content;	    
	    
	    while (content2.contains(compare)){    		
    		   		
    		JournalDetails jdGoogle = new JournalDetails();
 
    		start = content2.indexOf("<a href=", content2.indexOf(compare))+9; // plus 9 = ab http:....
    		linkPdfGoogle = content2.substring(start, content2.indexOf("\"", start+9)); // erster Teil
    		linkPdfGoogle = correctGoogleURL(linkPdfGoogle);
    		String textLinkPdfGoogle = content2.substring(content2.indexOf(">", start)+1, content2.indexOf("</a>", start)); // zweiter Teil ohne Javascript etc.
    		textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("<b>", "");
    		textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("</b>", "");
    		textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("<em>", "");
    		textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("</em>", "");
    		textLinkPdfGoogle = specialCharacters.replace(textLinkPdfGoogle);

    		jdGoogle.setLink(linkPdfGoogle);
    		jdGoogle.setUrl_text(textLinkPdfGoogle);    		
    		trefferGoogle.add(jdGoogle);
    		
    		if (searches < 4) { // nur bei PDF-Suche Google-Cache benutzen
    		String cache = "";
    		int cache_position = 0;
    		int end = 0;
    		if (searches < 4){ // eigentlich: if ((searches < 5) || (searches == 7))
    			end = content2.indexOf("class=w", start);   // class=w => Anfang des naechsten Datensatzes
    		}
    		else {
    			end = content2.indexOf(compare, start);   // class=g => Anfang des naechsten Datensatzes
    		}
    		
    		if ( (end != -1) && (content2.substring(start, end).contains("=cache:")) ||
    			 (end == -1) && (content2.substring(start).contains("=cache:")) ){
    			cache_position = content2.indexOf("=cache:", start);
    			cache = content2.substring(content2.lastIndexOf("http:", cache_position), content2.indexOf("+", cache_position)); // bis + => ohne highlighten der Suchbegriffe im Cache
    			
    			jdGoogle = new JournalDetails();
    			jdGoogle.setLink(cache);
        		jdGoogle.setUrl_text("(Google-Cache): " + textLinkPdfGoogle);    		
        		trefferGoogle.add(jdGoogle);    			
    		}
    		}
	   
    		content2 = content2.substring(start);    		
    							}
	    searches = 6; // = Suche erfolgreich 

		FindFree ff = new FindFree();
		ff.setZeitschriften(trefferGoogle);
    	rq.setAttribute("treffer_gl", ff);
    	
    } else {
    	searches = searches + 1;
    	
    	if ( (searches == 2) &&
    		 (did_you_mean == false) ){ // zweite Kondition eigentlich unnoetig...
    		did_you_mean = true;
    		pageForm.setDidYouMean(googleDidYouMean(pageForm.getArtikeltitel_encoded()));   
    		if (pageForm.getDidYouMean().equals("")){
    			searches = searches + 2; // Zaehler vorwaertssetzen
    			did_you_mean = false;
    		}    		
    	}
    	if (searches == 4) {
    		did_you_mean = false;
    	}
    	if (searches == 5) {
    		did_you_mean = true;
    		if (pageForm.getDidYouMean().equals("")){
    			searches = searches + 1; // Zaehler vorwaertssetzen
    			did_you_mean = false;
    		} 
    	}
    }
    
	} else { // Google Captcha Schlaufe => forward um Captcha auzulösen
				
		searches = 6;
		
		Message m = new Message();
		m = handleGoogleCaptcha(content);
		forward = "captcha";
		rq.setAttribute("message", m);
			
		}
    
    
    } // Ende while-Schlaufe
        
    // Google Scholar
    
    searches = 0;
	ergebnis = false;
	did_you_mean = false;
	start = 0;
	pageForm.setDidYouMean("");    
	    
    while ((ergebnis == false) && (searches < 2)){
    
    	if (searches==0){ // phrase + allintitle + filetype:pdf
    		if (did_you_mean == false){
    		link = "http://scholar.google.com/scholar?q=allintitle%3A%22" + pageForm.getArtikeltitel_encoded() + "%22+filetype%3Apdf+OR+filetype%3Ahtm&hl=de&lr=&btnG=Suche&lr=";
    		}
    		if (did_you_mean == true) {
    		link = "http://scholar.google.com/scholar?q=allintitle%3A%22" + pageForm.getDidYouMean() + "%22+filetype%3Apdf+OR+filetype%3Ahtm&hl=de&lr=&btnG=Suche&lr=";	
    		}
    	}
    	if (searches==1){ // phrase + allintitle
    		if (did_you_mean == false){
    		link = "http://scholar.google.com/scholar?q=allintitle%3A%22" + pageForm.getArtikeltitel_encoded() + "%22&hl=de&lr=&btnG=Suche&lr=";
    		}
    		if (did_you_mean == true) {
    			link = "http://scholar.google.com/scholar?q=allintitle%3A%22" + pageForm.getDidYouMean() + "%22&hl=de&lr=&btnG=Suche&lr=";	
    		}
        }

    content = getWebcontent(link, 1000, 3);
    
    if (!check.containsGoogleCaptcha(content)) { // Sicherstellen, dass Google Scholar nicht mit Captcha reagiert
    	
    	String identifierHitsGoogleScholar = "<div class=gs_rt><h3><span class=gs_ctc>[PDF]</span> <a href=\""; // You may have to change this, to adapt to any major changes of GoogleScholars sourcecode.
    
     if (content.contains(identifierHitsGoogleScholar)){
    	ergebnis = true;
    	
    	linkPdfGoogleScholar ="";
    	
	    while (content.contains(identifierHitsGoogleScholar)){
	    String content2 = "";	
	    if (content.substring(content.indexOf(identifierHitsGoogleScholar)+9).contains(identifierHitsGoogleScholar)){
	    content2 = content.substring(content.indexOf(identifierHitsGoogleScholar), content.indexOf(identifierHitsGoogleScholar, content.indexOf(identifierHitsGoogleScholar)+9));
	    }
	    else {
	    content2 = content.substring(content.indexOf(identifierHitsGoogleScholar));	
	    }
    		
    		JournalDetails jdGoogleScholar = new JournalDetails();
	    	start = content2.indexOf("<a href=\"")+9;
    		linkPdfGoogleScholar = content2.substring(start, content2.indexOf("\"", start));
    		linkPdfGoogleScholar = correctGoogleURL(linkPdfGoogleScholar);
	    	
	    	// url-text extrahieren
	    	String textLinkPdfGoogleScholar = "";
	    	textLinkPdfGoogleScholar = content2.substring(content2.indexOf(">", start)+1, content2.indexOf("</a>", start)); // zweiter Teil ohne Javascript etc.
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("<b>", "");
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</b>", "");
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("<em>", "");
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</em>", "");
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("<font color=#CC0033>", "");
    		textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</font>", "");
    		textLinkPdfGoogleScholar = specialCharacters.replace(textLinkPdfGoogleScholar); 		
    		
        	jdGoogleScholar.setLink(linkPdfGoogleScholar);
        	jdGoogleScholar.setUrl_text(textLinkPdfGoogleScholar);
	    	trefferGoogleScholar.add(jdGoogleScholar);
    			    	
	   		if (content2.contains("=cache:")){
	   				    		
	    		int cache_position = content2.indexOf("=cache:");
	    		String cache = content2.substring(content2.lastIndexOf("http:", cache_position), content2.indexOf("+", cache_position)); // bis + => ohne highlighten der Suchbegriffe im Cache
	    			
	    		jdGoogleScholar = new JournalDetails();
	    		jdGoogleScholar.setLink(cache);
	        	jdGoogleScholar.setUrl_text("(Google-Cache): " + textLinkPdfGoogleScholar);    		
	        	trefferGoogleScholar.add(jdGoogleScholar); 
    		}
    	
		content = content.substring(content.indexOf(identifierHitsGoogleScholar)+9);
    	
		}
	    FindFree ff = new FindFree();
		ff.setZeitschriften(trefferGoogleScholar);
    	rq.setAttribute("treffer_gs", ff);
    	       
    } else {
    	searches = searches + 1;
    	
    	if ((searches == 2) && (did_you_mean == false)){
    		did_you_mean = true;
    		pageForm.setDidYouMean(googleDidYouMean(pageForm.getArtikeltitel_encoded()));   
    		if (!pageForm.getDidYouMean().equals("")){
    			searches = 0; // Zaehler zuruecksetzen
    		}
    		
    	}
    }
    
    } else { // Google-Scholar Captcha  => forward um Captcha auzulösen
		
		searches = 2;
		
		Message m = new Message();
		m = handleGoogleCaptcha(content);
		forward = "captcha";
		rq.setAttribute("message", m);
			
		}
    
    }
    
        } else { // Captcha auflösen...
        	
        	// Suche ausführen mit test, dann folgendes aufrufen:
        	// http://www.google.ch/sorry/?continue=http://www.google.ch/search?hl=de&q=test&btnG=Google-Suche&meta=
        	// Captcha erscheint, aufzulösen mit:
        	// http://www.google.ch/sorry/Captcha?continue=http%3A%2F%2Fwww.google.ch%2Fsearch%3Fhl%3Dde&id=7584471529417997108&captcha=nonaryl
        	// Bildquelle: http://www.google.ch/sorry/image?id=6926821699383349053
        	// wobei id aus Quelltext und Text aus captcha übereinstimmen müssen => man landet auf Google Grundseite...
        	
        	link = "http://www.google.ch/sorry/Captcha?continue=http://www.google.ch/search?hl=de&id=" + pageForm.getCaptcha_id() + "&captcha=" + pageForm.getCaptcha_text();
        	content = getWebcontent(link, 1000, 1);
        	
        	if (!check.containsGoogleCaptcha(content)) {        		
        		// Important message
        		MHelper mh = new MHelper();
    	        mh.sendErrorMail("Catchpa has been successfully resolved!", "Cheers...\012");        		
        	}
        	
        	// Captcha: manuelle Google-Suche vorbereiten
        	link = "http://www.google.ch/search?as_q=&hl=de&num=4&btnG=Google-Suche&as_epq=" + pageForm.getArtikeltitel_encoded() + "&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images";
    		
    		JournalDetails jdGoogleCaptcha = new JournalDetails();
    		jdGoogleCaptcha.setLink(link);
        	jdGoogleCaptcha.setUrl_text("Search Google!");		
        	trefferGoogle.add(jdGoogleCaptcha);
        	
        	FindFree ff = new FindFree();
    		ff.setZeitschriften(trefferGoogle);
        	rq.setAttribute("treffer_gl", ff);
        	
        	// needs to be UTF-8 encoded
        	link = "http://scholar.google.com/scholar?as_q=&num=4&btnG=Scholar-Suche&as_epq=" + codeUrl.encodeUTF8(pageForm.getArtikeltitel()) + "&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&hl=de&lr=";
    		
    		JournalDetails jdGoogleScholarCaptcha = new JournalDetails();
    		jdGoogleScholarCaptcha.setLink(link);
        	jdGoogleScholarCaptcha.setUrl_text("Search Google-Scholar!");    		
        	trefferGoogleScholar.add(jdGoogleScholarCaptcha);
        	
        	FindFree fs = new FindFree();
    		fs.setZeitschriften(trefferGoogleScholar);
        	rq.setAttribute("treffer_gs", fs);
        	
        }
        
        } else {
        	
        	// User: manuelle Google-Suche vorbereiten
        	link = "http://www.google.ch/search?as_q=&hl=de&num=4&btnG=Google-Suche&as_epq=" + pageForm.getArtikeltitel_encoded() + "&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images";
    		
    		JournalDetails jdGoogleManual = new JournalDetails();
    		jdGoogleManual.setLink(link);
        	jdGoogleManual.setUrl_text("Search Google!");  		
        	trefferGoogle.add(jdGoogleManual);
        	
        	FindFree ff = new FindFree();
    		ff.setZeitschriften(trefferGoogle);
        	rq.setAttribute("treffer_gl", ff);
        	
        	// needs to be UTF-8 encoded
        	link = "http://scholar.google.com/scholar?as_q=&num=4&btnG=Scholar-Suche&as_epq=" + codeUrl.encodeUTF8(pageForm.getArtikeltitel()) + "&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=";
    		
    		JournalDetails jdGoogleScholarManual = new JournalDetails();
    		jdGoogleScholarManual.setLink(link);
        	jdGoogleScholarManual.setUrl_text("Search Google-Scholar!");		
        	trefferGoogleScholar.add(jdGoogleScholarManual);
        	
        	FindFree fs = new FindFree();
    		fs.setZeitschriften(trefferGoogleScholar);
        	rq.setAttribute("treffer_gs", fs);
        	
        }
        
        } else {
           // Auflösen von PMID fehlgeschlagen	
        }
        	
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
        	forward = "error";
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        if (!forward.equals("error")) { // failure wird hier leider nicht als globale Fehlermedlung verwendet. Deshalb Ausklammerung, falls ein tatsächlicher Fehler vorliegt...
        
        if (!forward.equals("captcha") && !forward.equals("pmidfailure")) {
        if ( (trefferGoogle.size()==0) &&
        	 (trefferGoogleScholar.size()==0) ) {
        	forward="notfound";
        } else {
        	forward = "found";
        }
        }
        
        //*** ggf. nochmals Funktion AutoComplete ausführen
        if (!forward.equals("captcha") && pageForm.getCaptcha_id()==null && // weder bei vorliegendem Captcha noch nach dessen Auflösung...
        	 pageForm.isAutocomplete() == false){
        	
        	if (pageForm.getDidYouMean().length() == 0){ // falls bis jetzt keine googleDidYouMean Prüfung stattgefunden hat => ausführen      		
        		pageForm.setDidYouMean(googleDidYouMean(pageForm.getArtikeltitel_encoded()));
        	}
        	if (pageForm.getDidYouMean().length() != 0){ // autocomplete mit meinten_sie ausführen
        		pageForm.setCheckDidYouMean(true);
        		pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
        	}
        	
        }
//        System.out.println("Ergebnis autocomplete: " + pageForm.isAutocomplete());
//        System.out.println("Testausgabe ISSN: " + pageForm.getIssn());
         
        // fehlende pmid bestimmen und ggf. fehlende Artikelangaben über Pubmed ergänzen
        if (isPubmedSearchWithoutPmidPossible(pageForm) &&
        	 pageForm.isAutocomplete() ) { // Autocomplete musste erfolgreich sein
        	pageForm.setPmid(bestellFormActioInstance.getPmid(pageForm)); // falls nicht eindeutig => pmid = ""
        	// falls eine PMID vorhanden und wichtige Artikel-Angaben fehlen: 
        	if (!pageForm.getPmid().equals("") && bestellFormActioInstance.areArticleValuesMissing(pageForm)) {
        		OrderForm of = new OrderForm();
        		of = bestellFormActioInstance.resolvePmid(bestellFormActioInstance.extractPmid(pageForm.getPmid()));
        		pageForm.completeOrderForm(pageForm, of);
        	}
        }
        
        if (!forward.equals("captcha")) pageForm.setRuns_autocomplete(1); // um zu verhindern, dass vor dem ISSN-Assistent nochmals erfolglos versucht wir Autocomplete auszuführen...
        
        pageForm.setArtikeltitel(prepareWorldCat2(pageForm.getArtikeltitel())); // ersetzt eigentlich nur griechisches Alphabet zu alpha, beta etc.
        
        }
        
     // für Get-Methode in PrepareLogin of URL-codieren
        pageForm = pageForm.encodeOrderForm(pageForm);
        
        rq.setAttribute("orderform", pageForm);
        return mp.findForward(forward);        
    }
    
    public ActionForward issnAssistent(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo"); // für ezbid auslesen
    	String bibid = ui.getKonto().getEzbid();
    	if (bibid == null || bibid.equals("") ) bibid = "AAAAA"; // entspricht unbestimmter Bibliothek
        
    	OrderForm pageForm = (OrderForm) form;
    	Auth auth = new Auth();
    	BestellformAction bestellFormActionInstance = new BestellformAction();
    	CodeUrl codeUrl = new CodeUrl();

		String artikeltitel_encoded = codeUrl.encodeLatin1(pageForm.getArtikeltitel());
       
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        boolean treffer = false;
        if (auth.isLogin(rq)) {
        	
        	if (pageForm.getPmid()==null || pageForm.getPmid().equals("")) { // keine PMID eingegeben => normaler ISSN-Assistent-Ablauf
        	if (!(pageForm.getIssn().length()==0 && pageForm.getZeitschriftentitel().length()==0 && 
        		  pageForm.isAutocomplete()==false) )  { // keine Eingabe ohne Autocomplete ausschliessen...
            forward = "success";        
            try {
            	
            	if ( (pageForm.isAutocomplete() == true) && // Autocomplete wurde ausgeführt...
            		 ((pageForm.getIssn().length() != 0) || (pageForm.getZeitschriftentitel().length() != 0)) && //...es ist eine Eingabe erfolgt...
            		 pageForm.isFlag_noissn()==false) { // ...und es handelt sich nicht um den Speziafall Autocomplete ohne ISSN!
            		// Eingabe zur Korrektur von autocomplete erfolgt => Formulardaten leeren
            		pageForm.setAuthor("");
            		pageForm.setJahr("");
            		pageForm.setJahrgang("");
            		pageForm.setHeft("");
            		pageForm.setSeiten("");
            		pageForm.setPmid("");
            		pageForm.setDoi("");
            		pageForm.setAutocomplete(false);
            		pageForm.setRuns_autocomplete(0);
            	}
            	
            	if (pageForm.isAutocomplete() == true && pageForm.isFlag_noissn()==true) {
               		// Seltener Fall Angaben aus autocomplete korrekt aber keine ISSN vorhanden
            		// z.B. Establishing a national resource: a health informatics collection to maintain the legacy of health    		
               		
               		// alle anderen Angaben URL-codieren und vorbereiten für Übergabe an ff, da Weitergabe über Hyperlink auf jsp läuft
                		
        				pageForm.setJahr(codeUrl.encodeLatin1(pageForm.getJahr()));
        				pageForm.setSeiten(codeUrl.encodeLatin1(pageForm.getSeiten()));        				
        				pageForm.setHeft(codeUrl.encodeLatin1(pageForm.getHeft()));
        				pageForm.setJahrgang(codeUrl.encodeLatin1(pageForm.getJahrgang()));
        				pageForm.setAuthor(codeUrl.encodeLatin1(pageForm.getAuthor()));               		
               	}
            	
        	    String zeitschriftentitel_encoded = correctArtikeltitIssnAssist(pageForm.getZeitschriftentitel());
                zeitschriftentitel_encoded = codeUrl.encodeLatin1(zeitschriftentitel_encoded);
            	
//              Methode 1 ueber Journalseek
                FindFree ff = new FindFree();
                ArrayList<JournalDetails> issn_js = new ArrayList<JournalDetails>();
                
                // der Zeitschriftentitel im OrderForm kann sich im Thread von Regensburg ändern 
        		final String concurrentCopyZeitschriftentitel = pageForm.getZeitschriftentitel();
                
                ThreadedJournalSeek tjs = new ThreadedJournalSeek(zeitschriftentitel_encoded, artikeltitel_encoded, pageForm, concurrentCopyZeitschriftentitel);
                ExecutorService executor = Executors.newCachedThreadPool();
        		Future<ArrayList <JournalDetails>> journalseekResult = null;
        		boolean jsThread = false;

            if ( (pageForm.getIssn().length()==0) &&
             	 (pageForm.getZeitschriftentitel().length()!=0) ){ // Ausklammerung von Journalseek bei Eingabe einer ISSN, da Auswertung anders ist...
            	
            	jsThread = true;
            	journalseekResult = executor.submit(tjs);
        	    
            	} else {
            		forward = "issn_direkt"; // es wurde eine ISSN eingegeben. Mit Regensburg wird versucht den Zeitschriftentitel zu holen...
            	}        	         	    
        	    
//        	  Methode 2 ueber Regensburger Zeitschriftenkatalog
        	    // Anzeige auf 30 limitiert (hits_per_page):
            FindFree ff_rb = new FindFree();
            ArrayList<JournalDetails> issn_rb = new ArrayList<JournalDetails>(); // Print ISSN Regensburg
        	    
        	    issn_rb = searchEzbRegensburg(zeitschriftentitel_encoded, artikeltitel_encoded, pageForm, bibid);
        	    
        	    if (issn_rb.size()>0) {
        	    	treffer = true;
        	    	if (issn_rb.size()==1) pageForm.setZeitschriftentitel(issn_rb.get(0).getZeitschriftentitel()); // es wird versucht den Zeitschriftentitel zu bestimmen...
        	    	
        	    }  else {        	    	
            		JournalDetails jd_rb = new JournalDetails();
            		jd_rb.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
            		jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
            		jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
            		issn_rb.add(jd_rb);
            		}
        	    
        	    ff_rb.setZeitschriften(issn_rb);
        	    rq.setAttribute("regensburg", ff_rb);
        	    
        	    // Journalseek-Thread zurückholen
        	    try {
        	    	if (jsThread) issn_js = journalseekResult.get(12, TimeUnit.SECONDS);    				
    			} catch (TimeoutException e) {
    				log.warn("Journalseek-TimeoutException: " + e.toString());
    			} catch (Exception e) {
    				log.error("Journalseek-Thread failed in issnAssistent: " + e.toString());
	    		} finally {
	    			if (jsThread) {
	    			if (issn_js!=null && issn_js.size()>0) {
    					treffer = true;
    				} else {
    					JournalDetails jd = new JournalDetails();
            	    	jd.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
            	    	jd.setZeitschriftentitel_encoded(zeitschriftentitel_encoded);
            	    	jd.setArtikeltitel(pageForm.getArtikeltitel());
            	    	jd.setArtikeltitel_encoded(artikeltitel_encoded);	
            	    	issn_js.add(jd);
    				}
	    			
	    			ff.setZeitschriften(issn_js);
            	    rq.setAttribute("journalseek", ff);
            	    
            	 // ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
	    			journalseekResult.cancel(true);            	    
	    		}
	    		}
            	
            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("issnAssistent: " + e.toString());

            }
            
        } else {
        	 forward ="noresult"; // Keine Eingabe erfolgt...       	
        }
        	
       	} else { // PMID eingegeben => Auflösung
       		forward = "noresult"; // d.h. direkt zurück zum Engabeformular
       		pageForm = bestellFormActionInstance.resolvePmid(bestellFormActionInstance.extractPmid(pageForm.getPmid())); 
       		pageForm.setAutocomplete(true); // unterdrückt die Funktion Autocomplete
       		pageForm.setRuns_autocomplete(1);
       	}
            
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        if ((treffer == false) && (pageForm.getIssn().length()==0)){
        forward ="noresult";	
        }
 if (pageForm.getArtikeltitel().length()!=0 && (pageForm.getRuns_autocomplete()==0 && pageForm.getIssn().length()!=0)) {       
        //*** Funktion AutoComplete ausführen
        pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
        if (pageForm.isAutocomplete() == false){
        	
        	if (pageForm.getDidYouMean().length() == 0){ // falls bis jetzt keine googleDidYouMean Prüfung stattgefunden hat => ausführen      		
        		pageForm.setDidYouMean(googleDidYouMean(artikeltitel_encoded));
        	}
        	if (pageForm.getDidYouMean().length() != 0){ // autocomplete mit meinten_sie ausführen
        		pageForm.setCheckDidYouMean(true);
        		pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
        	}
        	
        }
//        System.out.println("Ergebnis autocomplete: " + pageForm.isAutocomplete());
//        System.out.println("Testausgabe ISSN: " + pageForm.getIssn());
        
        pageForm.setRuns_autocomplete(1); // um zu verhindern, dass vor dem ISSN-Assistent nochmals erfolglos versucht wir Autocomplete auszuführen...
 } else {
	 pageForm.setAutocomplete(false); // d.h. Autocomplete im nächsten Schritt...
	 pageForm.setRuns_autocomplete(0);
 }
 
 
 		if (pageForm.isFromstock()==true) { // der Assistent wurde aus den Bestandesangaben heraus aufgerufen
 			if (forward.equals("noresult") || forward.equals("issn_direkt")) forward = "stock";
 				}

        
        rq.setAttribute("orderform", pageForm);
        rq.setAttribute("form", pageForm);
        
        return mp.findForward(forward);
    }
    
    public ActionForward checkAvailabilityOpenUrl(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp){
    	
    	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo"); // will be needed for the ezbid
        OrderForm pageForm = (OrderForm) form; 
        BestellformAction bfInstance = new BestellformAction();
        CodeUrl codeUrl = new CodeUrl();
        Text t = new Text();
    	Auth auth = new Auth();
        FindFree ff = new FindFree();
    	
    	ExecutorService executor = Executors.newCachedThreadPool();
    	// GBV-Thread-Management
    	ThreadedWebcontent gbvthread = new ThreadedWebcontent();
		Future<String> gbvcontent = null;
		// Pubmed-Thread-Management
    	ThreadedWebcontent pubmedthread = new ThreadedWebcontent();
		Future<String> pubmedcontent = null;
		// Carelit-Thread-Management
    	ThreadedWebcontent carelitthread = new ThreadedWebcontent();
		Future<String> carelitcontent = null;
    	
		String forward = "failure";
		String bibid = "AAAAA"; // ID in the EZB for an 'undefined' library
    	String land = "";
    	String link = "";
        String content = "";
        long daiaId = 0;
    	long kid = 0;
    	boolean zdb = false;
        
    	// if coming from getOpenUrlRequest or prepareReorder
        if (rq.getAttribute("ofjo")!=null) {
        	pageForm = (OrderForm) rq.getAttribute("ofjo");
        	pageForm.setResolver(true);
        	rq.setAttribute("ofjo", pageForm);
        }
		
     // falls nicht eingeloggt, aus Request lesen (falls vorhanden)
		if (!auth.isLogin(rq)) {
			t = (Text) rq.getAttribute("ip");
			if (t.getTexttyp().getId()==11) pageForm.setBkid(t.getInhalt()); // Text mit Kontoangaben anhand Broker-Kennung holen
			if (t.getTexttyp().getId()==12) pageForm.setKkid(t.getInhalt()); // Text mit Kontoangaben anhand Konto-Kennung holen
		}
    	
		// get bibid and land (country) from ui
    	if (ui!=null) {
    		if (ui.getKonto().getEzbid()!=null && !ui.getKonto().getEzbid().equals("")) bibid = ui.getKonto().getEzbid();
    		land = ui.getKonto().getLand();
    		zdb = ui.getKonto().isZdb();
    		daiaId = getDaiaId(ui.getKonto().getId());
    		kid = ui.getKonto().getId();
    		} else {
    			if (t.getInhalt()!=null) { // ggf. bibid und land aus IP-basiertem Zugriff abfüllen
    				if (pageForm.getBkid()==null && t.getKonto().getEzbid()!=null && !t.getKonto().getEzbid().equals("")) bibid = t.getKonto().getEzbid();
    				if (t.getKonto().getLand()!=null && !t.getKonto().getLand().equals("")) land = t.getKonto().getLand();
    				zdb = t.getKonto().isZdb();
    				daiaId = t.getKonto().getId();
    				kid = t.getKonto().getId();
    			}
    		}
        
        // normalize PMID if available
        pageForm.setPmid(bfInstance.extractPmid(pageForm.getPmid()));
        
        // PMID available and there are article references missing
        if (pageForm.getPmid()!=null && !pageForm.getPmid().equals("") &&
        	bfInstance.areArticleValuesMissing(pageForm)) {
        	OrderForm of = new OrderForm();
        	of = bfInstance.resolvePmid(pageForm.getPmid());
        	pageForm.completeOrderForm(pageForm, of);
        } else {
        // try to get missing PMID and complete missing article references
        if (isPubmedSearchWithoutPmidPossible(pageForm)) {
        	pubmedthread.setLink(bfInstance.composePubmedlinkToPmid(pageForm));
        	pubmedcontent = executor.submit(pubmedthread);
        }
        }
        
        // get zdbid from ISSN. Only necessary if logged in...
        boolean gbvThread = false;
        if (auth.isLogin(rq) && pageForm.getIssn()!=null && !pageForm.getIssn().equals("")) {        	
        	pageForm.setZdbid(getZdbidFromIssn(pageForm.getIssn(), t.getConnection())); // gets zdbid from database (will be an e-journal)
        	t.close();
        	// System.out.println("ZDB-ID aus dbs: " + pageForm.getZdbid());
        	// Try to get from e-ZDB-ID a p-ZDB-ID from GBV using a seperate thread.
        	if (pageForm.getZdbid()!=null && !pageForm.getZdbid().equals("")) {
        	String gbvlink = "http://gso.gbv.de/sru/DB=2.1/?version=1.1&operation=searchRetrieve&query=pica.zdb%3D%22"
				+ pageForm.getZdbid()
				+ "%22&recordSchema=pica&sortKeys=YOP%2Cpica%2C0%2C%2C&maximumRecords=10&startRecord=1";
        	gbvthread.setLink(gbvlink);
        	gbvcontent = executor.submit(gbvthread);
        	gbvThread = true;
        	}
        }
        
    		// eingeloggt, oder Zugriff IP-basiert/kkid/bkid
        if (auth.isLogin(rq) || t.getInhalt() != null ) {
        	forward = "notfreeebz";
        	
        	// set link in request if there is institution logo for this account
            if (t.getInhalt()!=null && t.getKonto().getInstlogolink()!=null) rq.setAttribute("logolink", t.getKonto().getInstlogolink());
            
            ContextObject co = new ContextObject();
            ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
            co = convertOpenUrlInstance.makeContextObject(pageForm);
            
    		OpenUrl openUrlInstance = new OpenUrl();
    		String openurl = openUrlInstance.composeOpenUrl(co);
    		openurl = openurl.replaceAll("&rft.", "&"); // scheint rft. als Identifier nicht zu unterstützen...
            openurl = openurl.replaceAll("&rfr.", "&"); // scheint rfr. als Identifier nicht zu unterstützen...
            
            pageForm.setLink(openurl); // damit auf Checkavailability codierte OpenURL-Anfragen zusammengestellt werden können (z.B. Carelit)
    		
            // Link für Abfrage EZB/ZDB-Schnittstelle (nur D)
    		if (land.equals("Deutschland") && zdb == true && !bibid.equals("AAAAA")) {
               	// neue Methode: eigentlicher Linkresolver auf Artikel-Ebene unter Berücksichtigung der eigenen Bestände:
            	// http://services.d-nb.de/fize-service/gvr/html-service.htm?sid=admin:info&genre=journal&issn=0001-6446&eissn=1588-2667&pid=bibid=UBR
            	// ansprechbar bis allen Identifiern von oben! &genre=article&atitle=...&date=1994-10-01&volume=26&issue=10&issn=0022-2828&spage=1349&pid=bibid=UBR
            	// z.B. http://services.d-nb.de/fize-service/gvr/html-service.htm?sid=admin:info&genre=article&atitle=robotics-based&date=2005&volume=11&issue=1-2&issn=1022-0038&spage=189&pid=bibid=UBR
    		
            link = "http://services.d-nb.de/fize-service/gvr/html-service.htm?"; // baseurl
            link = link + codeUrl.encodeLatin1("sid=DRDOC:doctor-doc") + openurl + "&pid=bibid=" + bibid;
            
    		} else { // Link für Vascoda-Schnittstelle (alle anderen Länder und für Nicht-ZDB-Teilnehmer in D)            
        	
        	// BaseURL: http://ezb.uni-regensburg.de/ezeit/vascoda/openURL
        	// z.B. http://ezb.uni-regensburg.de/ezeit/vascoda/openURL?genre=article&sid=suniltest&atitle=Prevention%20of%20Extracellular%20K%20Inhomogeneity%20Across&date=1994-10-01&volume=26&issue=10&issn=0022-2828&spage=1349&pid=%3Csrv_id%3Eft%3C%2Fsrv_id%3E    	

    			link = "http://ezb.uni-regensburg.de/ezeit/vascoda/openURL?genre=article&sid=DRDOC:doctor-doc";
    			// Prevention%20of%20Extracellular%20K%20Inhomogeneity%20Across&date=1994-10-01&volume=26&issue=10&issn=0022-2828&spage=1349&pid=%3Csrv_id%3Eft%3C%2Fsrv_id%3E

    		link = link + openurl + "&bibid=" + bibid;    
    		}
    		

			if (ReadSystemConfigurations.isSearchCarelit()) {
				carelitthread.setLink("http://217.91.37.16/LISK_VOLLTEXT/resolver/drdoc.asp?sid=DRDOC:doctor-doc"
								+ openurl);
				carelitcontent = executor.submit(carelitthread);
			}
            
            content = getWebcontent(link, 3000, 2);
            content = content.replaceAll("\012", ""); // Layout-Umbrueche entfernen
        	
         // Hier folgt die Prüfung über die EZB
         // Schnittstelle EZB/ZDB für Deutschland, bei EZB-Teilnehmern, die auch in der ZDB dabei sind...
            if (land.equals("Deutschland") && zdb == true && !bibid.equals("AAAAA")) {
            	
         if (auth.isLogin(rq) || ((content.contains("../icons/e") && !content.contains("/e4_html.gif") ) || // Online vorhanden
        	 (content.contains("../icons/p") && !content.contains("/p4_html.gif"))) ) { // Print vorhanden
        	 	 // falls eingeloggt immer auf availabilityresult.jsp (wegen Wahl SUBITO / GBV), IP-basiert nur falls etwas zugänglich ist...
        		 forward = "freeezb";
        		 ff = getFindFreeFromEzbZdb(content, link);
        		 
        		 // Link nach ZDB aus Content extrahieren, da der Katalog sich unzuverlässig auf 
        		 // die eigentlich logische Suchsyntax verhält....
        		 SpecialCharacters specialCharacters = new SpecialCharacters();
        		 String zdb_link = specialCharacters.replace(getZdbLinkFromEzbZdb(content));
        		 if (!zdb_link.contains("&HOLDINGS_YEAR=") && !pageForm.getJahr().equals("")) zdb_link = zdb_link + "&HOLDINGS_YEAR=" + pageForm.getJahr();
        		 ff.setZdb_link(zdb_link);
        		 
        		 // bestehende Angaben werden korrekterweise überschrieben:
        		 pageForm.setLieferant(ff.getLieferant());  // Bestellquelle setzen (Internet / abonniert)...
        		 pageForm.setDeloptions(ff.getDeloptions()); // // Deloptions setzen (Online / Email)...
        	 
         }
         
            } else {
            	// Schnittstelle EZB/Vascoda, für alle anderen Länder und nicht ZDB-Teilnehmer in D
              if (auth.isLogin(rq) || ((content.contains("img/free.gif")) || 
//              	 (content.contains("img/rest.gif")) || // kostenpflichtig...
              	 (content.contains("img/subsread.gif")) ||
              	 (content.contains("img/subs.gif")) || // eigentlich ident. mit img/subsread.gif wird aber in der OpenUrl-Auflösung der EZB verwendet...
              	 (content.contains("img/light6.gif")))) {
            	// falls eingeloggt immer auf availabilityresult.jsp (wegen Wahl SUBITO / GBV), IP-basiert nur falls etwas zugänglich ist... 
            	 forward = "freeezb";
         		 ff = getFindFreeFromEzbVascoda(content, link);
         		 pageForm.setLieferant(ff.getLieferant()); // Bestellquelle setzen (Internet / abonniert)...
         		 pageForm.setDeloptions(ff.getDeloptions()); // // Deloptions setzen (Online / Email)...
         	    	
          	    }

        		// z.B. The American Naturalist
        		if ((pageForm.getZeitschriftentitel() == null || pageForm.getZeitschriftentitel().equals("")) && 
        			content.contains("warpto")) {
    				// Zeitschriftentitel extrahieren
    				int start = content.indexOf("warpto"); 
    				String zeitschriftentitel_rb = content.substring((content.indexOf(">", start)+1), (content.indexOf("<", start)));
    	        	pageForm.setZeitschriftentitel(zeitschriftentitel_rb);
        		}
            	
            }
            
            // Check for internal / external Holdings using DAIA Document Availability Information API
            ArrayList<Bestand> allHoldings = new ArrayList<Bestand>();
            ArrayList<Bestand> internalHoldings = new ArrayList<Bestand>();
            ArrayList<Bestand> externalHoldings = new ArrayList<Bestand>();
            
            if (ReadSystemConfigurations.isUseDaia()) { // Check an external register over DAIA
            	DaiaRequest daiaRequest = new DaiaRequest();
            	allHoldings = daiaRequest.get(openurl);
            	internalHoldings = extractInternalHoldings(allHoldings, daiaId);
        		externalHoldings = extractExternalHoldings(allHoldings, daiaId);
            } 
            // Check internal database
            Stock stock = new Stock();
            allHoldings = stock.checkGeneralStockAvailability(pageForm, true);
            internalHoldings.addAll(extractInternalHoldings(allHoldings, kid));
        	externalHoldings.addAll(extractExternalHoldings(allHoldings, kid));
        		
        	if (internalHoldings.size()>0) { // we have own holdings
        		forward = "freeezb";
	        	ff = getFindFreeForInternalHoldings(ff, internalHoldings, link);
	        	pageForm.setLieferant(ff.getLieferant()); // Bestellquelle setzen (Internet / abonniert)...
	        	pageForm.setDeloptions(ff.getDeloptions()); // // Deloptions setzen (Online / Email)...
        	}
        	if (externalHoldings.size()>0) { // there external holdings
        		rq.setAttribute("holdings", externalHoldings);		
        	}
            
         // hier wird der GBV-Thread mit einem zusätzlich Maximum Timeout von 2 Sekunden zurückgeholt
            if (gbvThread) {
    			try {
    				String gbvanswer = gbvcontent.get(2, TimeUnit.SECONDS);
    				// holt aus ggf. mehreren möglichen Umleitungen die letztmögliche 
    				if (gbvanswer!=null) {
    					String p_zdbid = OrderGbvAction.getPrintZdbidIgnoreMultipleHits(gbvanswer);
    				if (p_zdbid!=null) { 
    					pageForm.setZdbid(p_zdbid); // e-ZDB-ID wird nur überschrieben, falls p-ZDB-ID erhalten  				
//    					System.out.println("p-ZDB-ID aus GBV: " + pageForm.getZdbid());
    					}
    				}
    			} catch (TimeoutException e) {
    				log.warn("GBV-TimeoutException: " + e.toString());
    			} catch (Exception e) {
    				log.error("GBV-Thread failed in checkAvailability: " + e.toString());
	    		} finally {
	    			// ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
	    			gbvcontent.cancel(true);
	    		}
    		}
         // hier wird der Pubmed-Thread mit einem zusätzlich Maximum Timeout von 1 Sekunden zurückgeholt
            if (isPubmedSearchWithoutPmidPossible(pageForm)) {
    			try {
    				String pubmedanswer = pubmedcontent.get(1, TimeUnit.SECONDS);
    				if (pubmedanswer!=null) pageForm.setPmid(bfInstance.getPmid(pubmedanswer));
    				
    				if (pageForm.getPmid()!=null && !pageForm.getPmid().equals("") && // falls PMID gefunden wurde
    					bfInstance.areArticleValuesMissing(pageForm)) { // und Artikelangaben fehlen
    		        	OrderForm of = new OrderForm();
    		        	of = bfInstance.resolvePmid(pageForm.getPmid());
    		        	pageForm.completeOrderForm(pageForm, of);  // ergänzen
    				}
    				
    			} catch (TimeoutException e) {
    				log.warn("Pubmed-TimeoutException: " + e.toString());
    			} catch (Exception e) {
    				log.error("Pubmed-thread (Pos. 2) failed in checkavailability: " + e.toString());
    			} finally {
    				// ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
	    			pubmedcontent.cancel(true);
    			}
    		}
            
            if (ReadSystemConfigurations.isSearchCarelit()) {
         // hier wird der Carelit-Thread mit einem zusätzlich Maximum Timeout von 1 Sekunde zurückgeholt
    			try {
    				String carelitanswer = carelitcontent.get(1, TimeUnit.SECONDS);
    				if (carelitanswer!=null && 
    					carelitanswer.contains("<span id=\"drdoc\" style=\"display:block\">1</span>")) {
    					System.out.println("Es gibt Volltexte bei Carelit!");
    					pageForm.setCarelit(true); // Anzeige für den Moment unterdrückt...
    					forward = "freeezb";
    				}
    				
    			} catch (TimeoutException e) {
    				log.warn("Carelit-TimeoutException: " + e.toString());
    			} catch (Exception e) {
    				log.error("Carelitthread failed in checkAvailability: " + e.toString());
    			} finally {
    				// ungefährlich, falls der Task schon beendet ist. Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
	    			carelitcontent.cancel(true);
    			}
            }
            
        	
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        pageForm.setAutocomplete(false); // Variable zurückstellen
        rq.setAttribute("findfree", ff);
        
        // for get-method in PrepareLogin encode pageForm
        pageForm = pageForm.encodeOrderForm(pageForm);
        
        rq.setAttribute("orderform", pageForm);

     
        return mp.findForward(forward);        
    }
    
    
    public ArrayList<JournalDetails> searchJournalseek(String zeitschriftentitel_encoded, String artikeltitel_encoded, OrderForm pageForm, String concurrentCopyZeitschriftentitel) {
    	
    	ArrayList<JournalDetails> issn_js = new ArrayList<JournalDetails>();
    	CodeUrl codeUrl = new CodeUrl();
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	// erster Versuch ueber Journalseek

	    String link = "http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=title&editorID=&send=Go&query=";
	    link = link + zeitschriftentitel_encoded;
	    
//	    System.out.println("Suchstring ISSN Journalseek erster Versuch: " + link + "\012");        	    
	    String content = getWebcontent(link, 2000, 3); 
	    
//zweiter Versuch ueber Journalseek
	    String zeitschriftentitel_encoded_trunkiert = correctArtikeltitIssnAssist(concurrentCopyZeitschriftentitel);
	    
	    if (content.contains("no matches")){ // falls keine Treffer => Suchbegriffe trunkieren
	    	zeitschriftentitel_encoded_trunkiert = zeitschriftentitel_encoded_trunkiert.replaceAll("\040", "*\040") + "*";// Achtung Regexp hat * spezielle Bedeutung...
//    	    System.out.println("Sternchentitel; " + zeitschriftentitel_encoded_trunkiert);
    	    
    	    	zeitschriftentitel_encoded_trunkiert = codeUrl.encodeLatin1(zeitschriftentitel_encoded_trunkiert);
    	    	link = "http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=title&editorID=&send=Go&query=" + zeitschriftentitel_encoded_trunkiert;
    	    
//    	    System.out.println("Suchstring ISSN Journalseek zweiter Versuch: " + link + "\012");
    	    content = getWebcontent(link, 2000, 3);
	    	}
	    
//Trefferauswertung
	    
	    if ( (!content.contains("no matches")) &&
	    	 (!content.contains("Wildcards cannot be used on short searches")) ){

	    	while (content.contains("query=")){
	    		JournalDetails jd = new JournalDetails();
	    		jd.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
	    		int start = content.indexOf("query=");
	        	int start_issn = content.indexOf("-", start)-4;
	        	jd.setIssn(content.substring(start_issn, start_issn+9));
	        	// Zeitschriftentitel extrahieren
	        	start = content.indexOf(">", start_issn)+1;
	        	int end = content.indexOf("<", start_issn);
	        	jd.setArtikeltitel(pageForm.getArtikeltitel());
	        	jd.setArtikeltitel_encoded(artikeltitel_encoded);
	        	String zeitschriftentitel_js = specialCharacters.replace(content.substring(start, end));
	        	jd.setZeitschriftentitel(zeitschriftentitel_js); 
	        	String zeitschriftentitel_encoded_js = zeitschriftentitel_js;
        	    try {
        	    	zeitschriftentitel_encoded_js = java.net.URLEncoder.encode(zeitschriftentitel_encoded_js, "ISO-8859-1");
        	    } catch(Exception e){
        	    	log.error("ISSN-Assistent - Point F zeitschriftentitel_encoded_js: " + e.toString());
        	    		}
        	    jd.setZeitschriftentitel_encoded(zeitschriftentitel_encoded_js);
	        	
	        	jd.setLink("http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=issn&query=" + jd.getIssn());
	        	
	        	if (pageForm.isFlag_noissn() == true) {
	        		jd.setAuthor(pageForm.getAuthor());
	        		jd.setJahr(pageForm.getJahr());
	        		jd.setJahrgang(pageForm.getJahrgang());
	        		jd.setHeft(pageForm.getHeft());
	        		jd.setSeiten(pageForm.getSeiten());
	        	}
	        
	        	content = content.substring(end);
	        	issn_js.add(jd);
	    	}        	    	     	    	      	    		
	    }
    	
    	return issn_js;
    }
    
    private ArrayList<JournalDetails> searchEzbRegensburg(String zeitschriftentitel_encoded, String artikeltitel_encoded, OrderForm pageForm, String bibid) {
    	
    	ArrayList<JournalDetails> issn_rb = new ArrayList<JournalDetails>();
    	
    	String link = "";
    	String content = "";
    	CodeUrl codeUrl = new CodeUrl();
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	if (pageForm.getIssn().length()==0) { // Suche anhand des Zeitschriftentitels
    	    link = "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?colors=7&lang=de&jq_type1=KT&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&offset=-1&hits_per_page=30&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=";        	    
    	   	link = link +bibid + "&jq_term1=" + zeitschriftentitel_encoded;
    	   	
    	    } else { // Suche anhand ISSN
    	    link = "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?colors=5&lang=de&jq_type1=KT&jq_term1=&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&jq_bool4=AND&jq_not4=+&jq_type4=IS&offset=-1&hits_per_page=50&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=";
    	    link = link +bibid + "&jq_term4=" + pageForm.getIssn();
    	    }
    	    
    	    content = getWebcontent(link, 3000, 2);
    	    content = content.replaceAll("\012", ""); // Layout-Umbrueche entfernen
     	
    	    
    	    if (content.contains("Treffer")){ // Zusatzschlaufe um bei Treffer >30 eine andere Suche zu versuchen
      		
    			int start = content.indexOf("Treffer");
    			String subcontent = content.substring(start-7, start-1);
    			subcontent = subcontent.replaceAll("\040", "");
    			int x = subcontent.indexOf(">")+1;
    			subcontent = subcontent.substring(x);
    			x = Integer.parseInt(subcontent);
//    			System.out.println("Anzahl Treffer: " + x);
    			if (x>30){
//    			System.out.println("Ihre Suche hat mehr als 30 Treffer ergeben!");
        	    link = "http://ezb.uni-regensburg.de/ezeit/searchres.phtml?colors=7&lang=de&jq_type1=KS&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&offset=-1&hits_per_page=30&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4&bibid=";
        	    // Unterschied im Suchstring ist &jq_type1=KS statt &jq_type1=KT
        	    link = link +bibid + "&jq_term1=" + zeitschriftentitel_encoded;
        	    content = getWebcontent(link, 3000, 2);
        	    content = content.replaceAll("\012", ""); // Layout-Umbrueche entfernen            	   
    			}
    	    }
    	           	    
    	if (content.contains("Treffer")) {
    		
    			int start = content.indexOf("Treffer");
    			String subcontent = content.substring(start-7, start-1);
    			subcontent = subcontent.replaceAll("\040", "");
    			int x = subcontent.indexOf(">")+1;
    			subcontent = subcontent.substring(x);
    			x = Integer.parseInt(subcontent);
    			
    			if (x==1){

    				start = content.indexOf("warpto");
    				int end = content.indexOf("\">", start);
    				String link_rb = "http://ezb.uni-regensburg.de/ezeit/" + content.substring(start, end);
    				link_rb = link_rb.replaceAll("warpto", "detail");
    				
    				// Zeitschriftentitel extrahieren
    				String zeitschriftentitel_rb = getZeitschriftentitelFromEzb(content);
    				
    				// notwendig, da Titel per Get-Methode weitergeschickt wird, z.B. Pflege & Managament
    					String zeitschriftentitel_rb_encoded = codeUrl.encodeLatin1(zeitschriftentitel_rb);
    				
    	        					
    				if (content.contains("P-ISSN(s):")){		
    					// P-ISSN extrahieren
    		    		start = content.indexOf("P-ISSN(s):")+2; // Startpunkt auf nach P- stellen
    		        	int start_issn = content.indexOf("-", start)-4;
    		        	
    		        	JournalDetails jd_rb = new JournalDetails();
    		        	jd_rb.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
    		        	jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
    		        	jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
    		        	jd_rb.setZeitschriftentitel(zeitschriftentitel_rb);
    		        	jd_rb.setZeitschriftentitel_encoded(zeitschriftentitel_rb_encoded);
    		        	jd_rb.setIssn(content.substring(start_issn, start_issn+9));
    		        	jd_rb.setLink(link_rb);
    		        	
        	        	if (pageForm.isFlag_noissn() == true) {
        	        		jd_rb.setAuthor(pageForm.getAuthor());
        	        		jd_rb.setJahr(pageForm.getJahr());
        	        		jd_rb.setJahrgang(pageForm.getJahrgang());
        	        		jd_rb.setHeft(pageForm.getHeft());
        	        		jd_rb.setSeiten(pageForm.getSeiten());
        	        	}
    		        	
    		        	issn_rb.add(jd_rb);
    		        	while (content.startsWith(";", start_issn+9)==true){
    		        		// falls es weitere ISSNs gibt...
    		        		content = content.substring(start_issn+10);
    		        		start_issn = content.indexOf("-")-4;
    		        		jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
    		        		jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
    			        	jd_rb.setZeitschriftentitel(zeitschriftentitel_rb);
    			        	jd_rb.setZeitschriftentitel_encoded(zeitschriftentitel_rb_encoded);
    			        	jd_rb.setIssn(content.substring(start_issn, start_issn+9));
    			        	jd_rb.setLink(link_rb);
    			        	
            	        	if (pageForm.isFlag_noissn() == true) {
            	        		jd_rb.setAuthor(pageForm.getAuthor());
            	        		jd_rb.setJahr(pageForm.getJahr());
            	        		jd_rb.setJahrgang(pageForm.getJahrgang());
            	        		jd_rb.setHeft(pageForm.getHeft());
            	        		jd_rb.setSeiten(pageForm.getSeiten());
            	        	}        			        	
    			        	
    			        	issn_rb.add(jd_rb);
    		        	}
    		    	} else{
    					if (content.contains("E-ISSN(s):")){
    					// Grundsaetzlich Artikel nicht bestellbar => Pruefung Verfuegbarkeit
    						start = content.indexOf("E-ISSN(s):")+2; // Startpunkt auf nach P- stellen
    			        	int start_issn = content.indexOf("-", start)-4;
    			        	JournalDetails jd_rb = new JournalDetails();
    			        	jd_rb.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
    			        	jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
    			        	jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
    			        	jd_rb.setZeitschriftentitel(zeitschriftentitel_rb + "(E-ISSN, nicht bestellbar)");
    			        	jd_rb.setZeitschriftentitel_encoded(zeitschriftentitel_rb_encoded);
    			        	jd_rb.setIssn(content.substring(start_issn, start_issn+9));
    			        	jd_rb.setLink(link_rb);
    			        	
            	        	if (pageForm.isFlag_noissn() == true) {
            	        		jd_rb.setAuthor(pageForm.getAuthor());
            	        		jd_rb.setJahr(pageForm.getJahr());
            	        		jd_rb.setJahrgang(pageForm.getJahrgang());
            	        		jd_rb.setHeft(pageForm.getHeft());
            	        		jd_rb.setSeiten(pageForm.getSeiten());
            	        	}
    			        	
    			        	issn_rb.add(jd_rb);
    			        	
    			        while (content.startsWith(";", start_issn+9)==true){
    			        		// falls es weitere ISSNs gibt...
    			        		content = content.substring(start_issn+10);
    			        		start_issn = content.indexOf("-")-4;
    			        		jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
    			        		jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
    				        	jd_rb.setZeitschriftentitel(zeitschriftentitel_rb + "(E-ISSN, nicht bestellbar)");
    				        	jd_rb.setZeitschriftentitel_encoded(zeitschriftentitel_rb_encoded);
        			        	jd_rb.setIssn(content.substring(start_issn, start_issn+9));
        			        	jd_rb.setLink(link_rb);
        			        	
                	        	if (pageForm.isFlag_noissn() == true) {
                	        		jd_rb.setAuthor(pageForm.getAuthor());
                	        		jd_rb.setJahr(pageForm.getJahr());
                	        		jd_rb.setJahrgang(pageForm.getJahrgang());
                	        		jd_rb.setHeft(pageForm.getHeft());
                	        		jd_rb.setSeiten(pageForm.getSeiten());
                	        	}
        			        	
        			        	issn_rb.add(jd_rb);
    			        	}
    					}
    				}
    				    	
    		    }
    			String zeitschriftentitel_rb ="";
    			if (x > 1){
    				
    				// Titel und Link extrahieren
    				while (content.contains("warpto")){
    				start = content.indexOf("detail");
    				int end = content.indexOf("\">", start);
    				String link_rb = "http://ezb.uni-regensburg.de/ezeit/" + content.substring(start, end);
    				link_rb = link_rb.replaceAll("warpto", "detail");
    				start = content.indexOf("warpto");
    				start = content.indexOf(">", start)+1;
    				end = content.indexOf("<", start); 
    				zeitschriftentitel_rb = specialCharacters.replace(content.substring(start, end));
    				
    				zeitschriftentitel_rb = zeitschriftentitel_rb.replaceAll("\r", ""); // Korrektur Breaks...
    				zeitschriftentitel_rb = zeitschriftentitel_rb.replaceAll("\040\040", ""); // Korrektur übermässige Leerschläge...
//    				System.out.println("Zeitschriftentitel_rb: " + zeitschriftentitel_rb);
    				String zeitschriftentitel_rb_encoded = "";
    				
    				// notwendig, da Titel per Get-Methode weitergeschickt wird, z.B. Pflege & Managament
    				zeitschriftentitel_rb_encoded = codeUrl.encodeLatin1(zeitschriftentitel_rb);
    				
    	        	content = content.substring(start+1);
    	        	
    	        	// Methode um ISSN zu bestimmen
    	        	
    	        	String issn = getIssnRegensburg(link_rb);
    	        	
    	        	if (issn != ""){ // nur Treffer mit ISSN zulassen
    	        	
    	        	JournalDetails jd_rb = new JournalDetails();
    	        	jd_rb.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
            		jd_rb.setArtikeltitel(pageForm.getArtikeltitel());
            		jd_rb.setArtikeltitel_encoded(artikeltitel_encoded);
            		jd_rb.setZeitschriftentitel(zeitschriftentitel_rb);
            		jd_rb.setZeitschriftentitel_encoded(zeitschriftentitel_rb_encoded);
            		jd_rb.setIssn(issn);
            		jd_rb.setLink(link_rb);
            		
    	        	if (pageForm.isFlag_noissn() == true) {
    	        		jd_rb.setAuthor(pageForm.getAuthor());
    	        		jd_rb.setJahr(pageForm.getJahr());
    	        		jd_rb.setJahrgang(pageForm.getJahrgang());
    	        		jd_rb.setHeft(pageForm.getHeft());
    	        		jd_rb.setSeiten(pageForm.getSeiten());
    	        	}
            		
            		issn_rb.add(jd_rb);
    	        	}        	        	
    				}
    			}        	    	
    	    }    	
    	
    	
    	return issn_rb;    	
    }

    
	  private String getIssnRegensburg(String link){
			String issn = "";
			
			String content_get_issn = getWebcontent(link, 3000, 2);
			
			if (content_get_issn.contains("P-ISSN(s):")){			
				// P-ISSN extrahieren
	    		int start = content_get_issn.indexOf("P-ISSN(s):")+2; // Startpunkt auf nach P- stellen
	        	int start_issn = content_get_issn.indexOf("-", start)-4;
	        	        	
	        	issn = content_get_issn.substring(start_issn, start_issn+9);
	        	
	        	while (content_get_issn.startsWith(";", start_issn+9)==true){
	        		// falls es weitere ISSNs gibt...
	        		content_get_issn = content_get_issn.substring(start_issn+10);
	        		start_issn = content_get_issn.indexOf("-")-4;
	        		
		        	issn = content_get_issn.substring(start_issn, start_issn+9);
		        	
	        	}
	    	}
					
			else{
				if (content_get_issn.contains("E-ISSN(s):")){
					int start = content_get_issn.indexOf("E-ISSN(s):")+2; // Startpunkt auf nach P- stellen
		        	int start_issn = content_get_issn.indexOf("-", start)-4;
		        	issn = content_get_issn.substring(start_issn, start_issn+9);		        	
		        	
		        while (content_get_issn.startsWith(";", start_issn+9)==true){
		        		// falls es weitere ISSNs gibt...
		        		content_get_issn = content_get_issn.substring(start_issn+10);
		        		start_issn = content_get_issn.indexOf("-")-4;
			        	issn = content_get_issn.substring(start_issn, start_issn+9);
		        	}
				}
				
				else {
					System.out.println("kein Treffer bei Regensburg!");					
				}
			}			

	  	  return issn;
		  }
 
	    private boolean autoComplete(ActionMapping mp, ActionForm form,
	            HttpServletRequest rq, HttpServletResponse rp){
	    	
	    	boolean autocomplete = false;
	    	String link = "";
	    		    	
	  // Syntax WorldCat z.B. http://www.worldcat.org/search?q=ti%3AAntioxidant+supplementation+sepsis+and+systemic+inflammatory+response+syndrome+issn%3A0090-3493&qt=advanced
	  // &fq=+dt%3Aart+%3E&qt=advanced (Einschränkung auf nur Artikelsuche) 
	    	
	        
	    	OrderForm pageForm = (OrderForm) form;
	    	Auth auth = new Auth();
	        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wi
	        
	        if (auth.isLogin(rq)) {
	        	
	        	String artikeltitel_wc = prepareWorldCat2(pageForm.getArtikeltitel()); // first correction, e.g. for β => beta
	        	int run = 0;
	        	
	        	// *** bis zu 4 WorldCat Prüfungen
	        	// Verscheidene Varianten von Umlauten erstetzen und ggf. meinten_sie
	        	int x = 2;
	        	
	        	if (pageForm.isCheckDidYouMean() == true) { // sicherstellen, dass WorldCat meinten_sie nur bei vorhandenem meinten-sie ausgeführt wird.
	        		run = 2; // verhindert, dass bei einer meinten_sie Prüfung wieder von vorne begonnen wird...
	        		x = 4;
	        	}
	        	
	        	while ((run < x) && (autocomplete == false)) {
	        		link = composeLinkWorldCat(artikeltitel_wc, pageForm, run);
	        		autocomplete = searchWorldCat(link, mp, form, rq, rp);
	        		run = run + 1;
	        		if (run < x && !checkPrepareWorldCat1(artikeltitel_wc)) run = run + 1; // prüft, ob Umlaute vorhanden sind und verhindert ggf. eine unötige WorldCat-Abfrage
	        	}        	    
        	    
	        }

	        return autocomplete;
	    }
	
	    
		  private String composeLinkWorldCat(String artikeltitel_wc, OrderForm pageForm, int run) {
			  String link_wc = "";
			  String tmp_wc = "";
			  String link = "";
			  CodeUrl codeUrl = new CodeUrl();
			  
			  String artikeltitel_encoded = artikeltitel_wc;
			  
			  if ( (pageForm.isCheckDidYouMean() == true) && ((run == 2) || (run == 3)) ) {
				  artikeltitel_encoded = pageForm.getDidYouMean().replaceAll("%22", "");
			  }
			  
			  
			  if (artikeltitel_encoded.contains("--")) { // Untertitel nicht in Feld Titel suchbar
				  tmp_wc = artikeltitel_encoded.substring(artikeltitel_encoded.indexOf("--")+2); // Untertitel
				  artikeltitel_encoded = artikeltitel_encoded.substring(0, artikeltitel_encoded.indexOf("--"));	// Titel			 
			  }
	        	

	 	        	artikeltitel_encoded = codeUrl.encodeLatin1(artikeltitel_encoded);
//	 	        	System.out.println("artikeltitel_encoded_wc: " + artikeltitel_encoded);
	 	        	if (tmp_wc.length() != 0){
	 	        	tmp_wc = codeUrl.encodeLatin1(tmp_wc);
//	 	        	System.out.println("tmp_wc (Untertitel): " + tmp_wc);
	 	        	}

	        	
	        	if (run == 0) {
	        		artikeltitel_encoded = prepareWorldCat2(artikeltitel_encoded);
	 	        	if (tmp_wc.length() != 0){
		 	        	tmp_wc = prepareWorldCat2(tmp_wc);
		 	        	} 
	        	}
	        	if (run == 1) {
	        		artikeltitel_encoded = prepareWorldCat1(artikeltitel_encoded);
	 	        	if (tmp_wc.length() != 0){
		 	        	tmp_wc = prepareWorldCat1(tmp_wc);
		 	        	} 
	        	}
	        	if ((run == 2) && (pageForm.isCheckDidYouMean() == true)) {
	        		artikeltitel_encoded = prepareWorldCat2(artikeltitel_encoded);
	 	        	if (tmp_wc.length() != 0){
		 	        	tmp_wc = prepareWorldCat2(tmp_wc);
		 	        	} 
	        	}
	        	if ((run == 3) && (pageForm.isCheckDidYouMean() == true)) {
	        		artikeltitel_encoded = prepareWorldCat1(artikeltitel_encoded);
	 	        	if (tmp_wc.length() != 0){
		 	        	tmp_wc = prepareWorldCat1(tmp_wc);
		 	        	} 
	        	}	        	
	        	
	        	link = "http://www.worldcat.org/search?q=";
	        	if (tmp_wc.length() != 0) link = link + tmp_wc + "+";	        	
	        	link = link + "ti%3A" + artikeltitel_encoded;
	        	try {
	        	if (pageForm.getIssn().length() != 0){
	        		link = link  + "+issn%3A" + pageForm.getIssn();
	        	}
	        } catch(Exception e){
  	    	// keine ISSN vorhanden
	        log.info("composeLinkWorldCat: no issn available" + e.toString());
  	    } 
	        	
	      link_wc = link + "&fq=+dt%3Aart+%3E&qt=advanced";

	  	  return link_wc;
		  }
		  
	    
	    private boolean searchWorldCat(String link, ActionMapping mp, ActionForm form, HttpServletRequest rq, HttpServletResponse rp){
	    	
	    	boolean worldcat = false;
	    	String content = "";
	    	String OpenURL = "";
	        
	    	OrderForm pageForm = (OrderForm) form;
	    	Auth auth = new Auth();
	        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wi
	        
	        if (auth.isLogin(rq)) {
       	    
        	    content = getWebcontent(link, 2000, 3);
        	    
        	    // Hier folgt die Auswertung nach Z39.88
        	    if (content.contains("url_ver=Z39.88")) { // bei nicht eindeutigem Treffer wird einfach der erste genommen...
        	    	worldcat = true;
        	    	pageForm.setRuns_autocomplete(+1);
        	    	
        	    	OpenURL = content.substring(content.indexOf("url_ver=Z39.88"), content.indexOf(">", content.indexOf("url_ver=Z39.88")));
//        	    	System.out.println("String OpenURL: " + OpenURL);
        	    	OpenURL = correctWorldCat(OpenURL);
        	    
        	    // Hier folgt die OpenURL-Auswertung	
        	    	ContextObject co = new ContextObject();
        	    	ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        	    	OpenUrl openUrlInstance = new OpenUrl();
        	    	co = openUrlInstance.readOpenUrlFromString(OpenURL); // ContextObject mit Inhalten von content abfüllen
        	    	OrderForm of = new OrderForm();
        	    	of = convertOpenUrlInstance.makeOrderform(co); // in ein OrderForm übersetzen

        	    // Artikeltitel als User-Eingabe muss behalten werden
        	    pageForm.setZeitschriftentitel(prepareWorldCat2(of.getZeitschriftentitel()));
        	    pageForm.setIssn(of.getIssn());
        	    pageForm.setJahr(of.getJahr());
        	    pageForm.setJahrgang(of.getJahrgang());
        	    pageForm.setHeft(of.getHeft());
        	    pageForm.setSeiten(of.getSeiten());
        	    pageForm.setAuthor(of.getAuthor());
        	    pageForm.setFlag_noissn(of.isFlag_noissn());
        	    
        	    }
        	    
	        }

	        return worldcat;
	        
	    }

    
    private boolean isPubmedSearchWithoutPmidPossible(OrderForm pageForm) {
    	boolean check = false;
    	
    	try {
    	
        if (pageForm.getPmid().equals("") && // pmid nicht schon vorhanden
           !pageForm.getIssn().equals("") && // issn muss vorhanden sein, damit überhaupt eine gewisse Chance besteht
          (!pageForm.getJahrgang().equals("") || !pageForm.getHeft().equals("") ) ) { // Jahrgang oder Heft, da ansonsten sehr wahrscheinlich Epub mit grosser möglicher Fehlerquote
        	check = true;
        }
    	} catch (Exception e) {
    		log.error("isPumedSearchWithoutPmidPossible: " + e.toString());
		}
    	
    	return check;
    }
    
    /**
     * Holt den Link zum Artikel/Journal der Online Version Dienst EZB/ZDB
     * 
     */
    private FindFree getFindFreeFromEzbZdb(String content, String link) {
    	
    	FindFree ff = new FindFree();
    	Text t = new Text();
    	Lieferanten lieferantenInstance = new Lieferanten();
    	// Achtung: es erfolgt eine Priorisierung (abonniert, gratis etc.). U.U. kann ein Artikel sowohl gratis über
    	// einen anderen Kanal, als auch abonniert zur Verfügung stehen.
    	try {
    		
    		// Check auf Online-Bestand
    		if (content.contains("../icons/e") && content.contains("/e2_html.gif")) {
            	ff.setLink(getOnlineLinkFromEzbZdb(content));
            	ff.setLinktitle("availresult.link_title_online");
             	ff.setMessage("availresult.abonniert");
             	ff.setE_ampel("yellow");
             	ff.setLieferant(lieferantenInstance.getLieferantFromName("abonniert", t.getConnection()));
             	ff.setDeloptions("email");
             	ff.setLink_search(link); // Suchlink zu ZDB/EZB
    			
    		} else {
    			if (content.contains("../icons/e") && content.contains("/e0_html.gif")) {
        			ff.setLink(getOnlineLinkFromEzbZdb(content));
           		 	ff.setLinktitle("availresult.link_title_online");
           		 	ff.setMessage("availresult.free");
           		 	ff.setE_ampel("green");
           		 	ff.setLieferant(lieferantenInstance.getLieferantFromName("Internet", t.getConnection()));
           		 	ff.setDeloptions("online");
           		 	ff.setLink_search(link); // Suchlink zu ZDB/EZB
        			
        		} else {
        			if (content.contains("../icons/e") && content.contains("/e1_html.gif")) {
                		ff.setLink(getOnlineLinkFromEzbZdb(content));
                   	 	ff.setLinktitle("availresult.link_title_online");
                   	 	ff.setMessage("availresult.partially_free");
                   	 	ff.setE_ampel("green");
                   	 	ff.setLieferant(lieferantenInstance.getLieferantFromName("Internet", t.getConnection()));
                   	 	ff.setDeloptions("online");
                   	 	ff.setLink_search(link); // Suchlink zu ZDB/EZB
                			
        			} else {
        				if (content.contains("../icons/e") && content.contains("/e3_html.gif")) {
        					ff.setLink(link);
        					ff.setLinktitle("availresult.link_title_ezb_zdb");
        					ff.setMessage("availresult.timeperiode");
        					ff.setE_ampel("red");
        					
        				} else {
        					ff.setLink(link);
        					ff.setLinktitle("availresult.link_title_ezb_zdb");
        					ff.setMessage("availresult.not_licensed");
        					ff.setE_ampel("red");	 
                 }
        		}
        	  }
    		}
    		
    		// Check auf Print-Bestand
           	 if (content.contains("../icons/p") && !content.contains("/p4_html.gif")) { 
           	ff.setLink_print(link);
       		ff.setLinktitle_print("availresult.link_title_print");
       		ff.setMessage_print("availresult.print");
       		ff.setP_ampel("yellow");
       		ff.setLieferant(lieferantenInstance.getLieferantFromName("abonniert", t.getConnection()));
       		ff.setDeloptions("email");
           	 }
           	 
    		
    	} catch(Exception e){
    		log.error("getFindFreeFromEzbZdb in OrderAction: " + e.toString() + "\012" + content);
	        
		     ff.setLink(link);
	   		 ff.setLinktitle("availresult.manual");
	   		 ff.setMessage("availresult.failed");
    		
    	} finally {
    		t.close();
    	}
    	
    	return ff;    	
    }
    
    /**
     * Holt den Link zum Artikel/Journal der Online Version Dienst EZB/Vascoda
     * 
     */
    private FindFree getFindFreeFromEzbVascoda(String content, String link) {
    	
    	FindFree ff = new FindFree();
    	Text t = new Text();
    	Lieferanten lieferantenInstance = new Lieferanten();

    	try {

    		if (content.contains("img/subsread.gif") || content.contains("img/subs.gif")) {
            	ff.setLink(getOnlineLinkFromEzbVascoda(content));
            	ff.setLinktitle("availresult.link_title_online");
             	ff.setMessage("availresult.abonniert");
             	ff.setE_ampel("yellow");
             	ff.setLieferant(lieferantenInstance.getLieferantFromName("abonniert", t.getConnection()));
             	ff.setDeloptions("email");
    			
    		} else {
    			if (content.contains("img/free.gif")) {
        			ff.setLink(getOnlineLinkFromEzbVascoda(content));
           		 	ff.setLinktitle("availresult.link_title_online");
           		 	ff.setMessage("availresult.free");
           		 	ff.setE_ampel("green");
           		 	ff.setLieferant(lieferantenInstance.getLieferantFromName("Internet", t.getConnection()));
           		 	ff.setDeloptions("online");    			    		 
        			
        		} else {
        			if (content.contains("img/light6.gif")) {
                		ff.setLink(getOnlineLinkFromEzbVascoda(content));
                   	 	ff.setLinktitle("availresult.link_title_online");
                   	 	ff.setMessage("availresult.partially_free");
                   	 	ff.setE_ampel("green");
                   	 	ff.setLieferant(lieferantenInstance.getLieferantFromName("Internet", t.getConnection()));
                   	 	ff.setDeloptions("online");
                			
        			} else {
        					ff.setLink(link);
        					ff.setLinktitle("availresult.link_title_ezb");
        					ff.setMessage("availresult.not_licensed");
        					ff.setE_ampel("red");
        		}
        	  }
    		}
           	 
    		
    	} catch(Exception e){
    		log.error("getFindFreeFromEzbVascoda in OrderAction: " + e.toString() + "\012" + content);
	        
		     ff.setLink(link);
	   		 ff.setLinktitle("availresult.manual");
	   		 ff.setMessage("availresult.failed");
    		
    	} finally {
    		t.close();
    	}
    	
    	return ff;    	
    }
    
    /**
     * Erstellt ein FindFree für die internen Bestände unter Berücksichtigung
     * vorhandener Einträge
     * 
     */
    private FindFree getFindFreeForInternalHoldings(FindFree ff, ArrayList<Bestand> internalHoldings, String link) {
    	
    	Lieferanten lieferantenInstance = new Lieferanten();
    	Text t = new Text();

    	if (ff.getLink()==null) { // Online-Bestand auf Rot setzen, falls nicht schon abgefüllt
    		ff.setLink(link);
			ff.setLinktitle("availresult.link_title_ezb");
			ff.setMessage("availresult.not_licensed");
			ff.setE_ampel("red");
    	}
		ff.setLink_print(link);
   		ff.setLinktitle_print("availresult.link_title_print");
   		ff.setMessage_print("availresult.print");
   		ff.setP_ampel("yellow");
   		ff.setLieferant(lieferantenInstance.getLieferantFromName("abonniert", t.getConnection()));
   		ff.setDeloptions("email");
   		
   		ArrayList<String> location = new ArrayList<String>();
   		ArrayList<String> shelfmark = new ArrayList<String>();
   		
   		for (int i=0;internalHoldings.size()>i;i++) {
   			
   			if (internalHoldings.get(i).getStandort().getInhalt()!=null) {
   				location.add(internalHoldings.get(i).getStandort().getInhalt());
   			} else {
   				location.add("");
   			}
   			
   			if (internalHoldings.get(i).getShelfmark()!=null) {
   				shelfmark.add(internalHoldings.get(i).getShelfmark());
   			} else {
   				shelfmark.add("");
   			}
   			
   		}
   		
   		ff.setLocation_print(location);
   		ff.setShelfmark_print(shelfmark);
   		
   		t.close();
    	
    	return ff;
    }
    
    /**
     * Holt den Artikeltitel aus eine EZB-Seite
     * 
     */
    private String getZeitschriftentitelFromEzb(String content) {
    	
    	String artikeltitel = "";
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	try {
    	
    	if (content.contains("<h2><a href=\"warpto")) { // Normalfall ohne Zeitraum, Nationallizenz etc.
    		
    		int start = content.indexOf("warpto");
			
			// Zeitschriftentitel extrahieren
			start = content.indexOf(">", start)+1;
			int end = content.indexOf("<", start); 
			artikeltitel = specialCharacters.replace(content.substring(start, end));
			artikeltitel = artikeltitel.replaceAll("\r", ""); // Korrektur Breaks...
			artikeltitel = artikeltitel.replaceAll("\040\040", ""); // Korrektur übermässige Leerschläge...
    		
    	} else {
    	
    	if (content.contains("<h2>")) {  // Spezialfall Nationallizenz, Zeitraum etc.
    		
    		int start = content.indexOf("<h2>"); // etwas fehleranfaellig auf Veraenderungen...
			
			// Zeitschriftentitel extrahieren
			start = content.indexOf(">", start)+1;
			int end = content.indexOf("<", start); 
			artikeltitel = specialCharacters.replace(content.substring(start, end));
			artikeltitel = artikeltitel.replaceAll("\r", ""); // Korrektur Breaks...
			artikeltitel = artikeltitel.replaceAll("\040\040", ""); // Korrektur übermässige Leerschläge...
    		
    	}
    	}
    	} catch(Exception e){ 
    		log.error("getZeitschriftentitelFromEzb in OrderAction: " + e.toString() + "\012" + content);  		
    	}
    	
    	artikeltitel = specialCharacters.replace(artikeltitel);
    	
    	return artikeltitel;    	
    }
    
    /**
     * Holt den Link zum Artikel/Journal der Online Version aus Dienst EZB/ZDB
     * 
     */
    private String getOnlineLinkFromEzbZdb(String content) {
    	
    	String link = "";
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	// es wird nicht gewichtet nach den verschiedenen Zugangsmethoden (gratis, abonniert etc.).
    	// der direkteste Zugang wird ausgegeben...
    	
    	try {
    	
    	if (content.contains("Zum Artikel")) {
    		
    		link = content.substring(content.lastIndexOf("http://", content.indexOf("Zum Artikel")), content.lastIndexOf("\">", content.indexOf("Zum Artikel")));
    		
    	} else {
    	
    	if (content.contains("Zur Zeitschrift")) {
    		
    		link = content.substring(content.lastIndexOf("http://", content.indexOf("Zur Zeitschrift")), content.lastIndexOf("\">", content.indexOf("Zur Zeitschrift")));
    		
    	}
    	}
    	} catch(Exception e){
    		log.error("getOnlineLinkFromEzbZdb in OrderAction: " + e.toString() + "\012" + content);   		
    	}
    	
    	link = specialCharacters.replace(link);
    	
    	return link;   	
    }
    
    /**
     * Holt den Link zur ZDB aus der Verfügbarkeitsanzeige von EZB/ZDB
     * 
     */
    private String getZdbLinkFromEzbZdb(String content) {
    	
    	String link = "";
    	SpecialCharacters specialCharacters = new SpecialCharacters();

    	try {
    	
    	if (content.contains("\"http://dispatch.opac.d-nb.de")) {    		
    		link = content.substring(content.indexOf("\"http://dispatch.opac.d-nb.de")+1, content.indexOf("\"", content.indexOf("\"http://dispatch.opac.d-nb.de")+1));    		
    	} else {
    	if (content.contains("Recherche nach Best&auml;nden im ZDB-Katalog")) {
    		link = content.substring(content.lastIndexOf("http:", content.indexOf("Recherche nach Best&auml;nden im ZDB-Katalog")), content.lastIndexOf("\">", content.indexOf("Recherche nach Best&auml;nden im ZDB-Katalog")));
    		// Important message
    		MHelper mh = new MHelper();
	        mh.sendErrorMail("Adressänderung in ZDB! Vermutlich zu http://zdb-opac.de", "getZdbLinkFromEzbZdb in OrderAction:\012" + content);
    	}
    	}
    	
    	} catch(Exception e){
    		log.error("getZdbLinkFromEzbZdb in OrderAction: " + e.toString() + "\012" + content);  		
    	}
    	
    	link = specialCharacters.replace(link);
    	
    	return link;    	
    }
    
    /**
     * Trys to get zdbid from an ISSN out of the local DB
     * 
     */
    public String getZdbidFromIssn(String issn, Connection cn) {
    	
    	String zdbid = null;

    	PreparedStatement pstmt = null;
		ResultSet rs = null;
    	try {

    		pstmt = cn.prepareStatement("SELECT DISTINCT a.zdbid FROM `zdb_id` AS a JOIN issn AS b ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier WHERE b.issn = ?");
    		pstmt.setString(1, issn);
    		rs = pstmt.executeQuery();
    		           
    		if (rs.next()) { // only the first zdbid is used
    			zdbid = rs.getString("zdbid");
    		}
    		     
    		
    	} catch(Exception e){
    		log.error("getZdbidFromIssn in OrderAction: " + issn + "\040" + e.toString()); 		
    	} finally {
        	if (rs != null) {
        		try {
        			rs.close();
        		} catch (SQLException e) {
        			System.out.println(e);
        		}
        	}
        	if (pstmt != null) {
        		try {
        			pstmt.close();
        		} catch (SQLException e) {
        			System.out.println(e);
        		}
        	}
        }
    	
    	return zdbid;
    }
    
    /**
     * Holt den Link zum Artikel/Journal der Online Version aus Dienst EZB/Vascoda
     * 
     */
    private String getOnlineLinkFromEzbVascoda(String content) {
    	
    	String link = "";
    	SpecialCharacters specialCharacters = new SpecialCharacters();
    	
    	
	    	if (content.contains("class=\"linkingtext\">")) { // erster Versuch Link auf Artikelebene
	    		
	    		try {
	    		
	    		int start = content.indexOf("href=\"", content.indexOf("class=\"linkingtext\">"));
	    		link = "http://ezb.uni-regensburg.de" + content.substring(start+6, content.indexOf("\"", start+6));
	    		
	    		} catch(Exception e){
	    			log.error("getOnlineLinkFromEzbVascoda class=linking in OrderAction: " + e.toString() + "\012" + content);	        		
	        	}		

	    	}
    		
    	if (link.equals("") && content.contains("warpto")) { // zweiter Versuch Link auf Journalebene
    			
    			try {
    			
  				int start = content.indexOf("warpto"); 
  	        	link = "http://ezb.uni-regensburg.de/ezeit/" + content.substring(start, (content.indexOf("\"", start)));
    			
    			} catch(Exception e){
    				log.error("getOnlineLinkFromEzbVascoda warpto in OrderAction: " + e.toString() + "\012" + content);   	    		
    	    	}
      		}
    	
    	
    	
    	link = specialCharacters.replace(link);
    	
    	return link;    	
    }
    
    /**
     * Detailansicht einer einzelnen Bestellung vorbereiten
     */
    public ActionForward journalorderdetail(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        OrderState orderstate = new OrderState();
        Auth auth = new Auth();
        BestellformAction instance = new BestellformAction();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        if (auth.isLogin(rq)) {
            forward = "success";
            
            Text cn = new Text();
            
            try {
            	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            	Bestellungen order = new Bestellungen(cn.getConnection(), pageForm.getBid());
            	// URL-hacking unterdrücken!
            	if (auth.isLegitimateOrder(rq, order)) {
            	
            	order.setPmid(instance.extractPmid(order.getPmid()));
            	order.setDoi(instance.extractDoi(order.getDoi()));
                pageForm.setBestellung(order);
                pageForm.setStates(orderstate.getOrderState(order, cn.getConnection()));
                rq.setAttribute("orderform", pageForm);
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("uebersicht");
                rq.setAttribute("ActiveMenus", mf);
                
            	} else {
            		forward = "failure";
            		ErrorMessage em = new ErrorMessage();
                    em.setError("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute("errormessage", em);
                    log.info("journalorderdetail: prevented URL-hacking! " + ui.getBenutzer().getEmail());
            	}

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("journalorderdetail: " + e.toString());
                
            } finally{
            	cn.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }
    
    /**
     * Bereitet das erneute Bestellen einer bestehenden Bestellung vor
     */
    public ActionForward prepareReorder(ActionMapping mp,
                              ActionForm form,
                              HttpServletRequest rq,
                              HttpServletResponse rp) {

        String forward = "failure";
        OrderForm pageForm = (OrderForm) form;
        ErrorMessage em = new ErrorMessage();
        Text cn = new Text();
        Auth auth = new Auth();
        
        if (auth.isLogin(rq)) {
        	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        	Bestellungen order = new Bestellungen(cn.getConnection(), pageForm.getBid());
        	// URL-hacking unterdrücken!
        	if (auth.isLegitimateOrder(rq, order)) {
        	 	forward = "success";        	 	
            	
            	ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);
        	 	
        	 	OrderForm of = new OrderForm(order);
        	 	
        	 	rq.setAttribute("ofjo", of);
        	 	
        	 // mediatype != Artikel: go directly to the page for saving/modifying the order and not to checkavailability
        	 	if (!of.getMediatype().equals("Artikel")) forward = "save";
        	
        	} else {
        		forward = "failure";
                em.setError("error.hack");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.info("prepareReorder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
        	}
           
      } else {
    	  ActiveMenusForm mf = new ActiveMenusForm();
    	  mf.setActivemenu("login"); 
          rq.setAttribute("ActiveMenus", mf);
          em = new ErrorMessage("error.timeout", "login.do");
          rq.setAttribute("errormessage", em);
      }
      cn.close();
      return mp.findForward(forward);
    }
    
    public ActionForward prepareIssnSearch(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isLogin(rq)) {
        	
        	if (pageForm.isAutocomplete()==false && pageForm.getRuns_autocomplete()==0 && pageForm.getArtikeltitel().length()!=0) { // noch kein autocomplete ausgeführt...
//        		*** Funktion AutoComplete ausführen
                pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
                if (pageForm.isAutocomplete() == false){
                	
                	if (pageForm.getDidYouMean().length() == 0){ // falls bis jetzt keine googleDidYouMean Prüfung stattgefunden hat => ausführen
                		CodeUrl codeUrl = new CodeUrl();
                		pageForm.setDidYouMean(googleDidYouMean(codeUrl.encodeLatin1(pageForm.getArtikeltitel())));
                	}
                	if (pageForm.getDidYouMean().length() != 0){ // autocomplete mit meinten_sie ausführen
                		pageForm.setCheckDidYouMean(true);
                		pageForm.setAutocomplete(autoComplete(mp, form, rq, rp));
                	}
                	
                }
                // basically replaces greek alphabet to alpha, beta...
                pageForm.setArtikeltitel(prepareWorldCat2(pageForm.getArtikeltitel()));
        		
        	}
        	
            forward = "success";
            try {
                rq.setAttribute("orderform", pageForm);

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("prepareIssnSearch: " + e.toString());
            }
            
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute("ActiveMenus", mf);
            
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }

    /**
     * Sucht u.a. die Benutzer des aktiven Kontos heraus, um sie für die Bestellung zur Auswahl anzubieten
     */
    public ActionForward prepare(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
		Text cn = new Text();
		Text t = new Text();
		Auth auth = new Auth();
    	
    	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
        OrderForm pageForm = (OrderForm) form;

        if (rq.getAttribute("ofjo")!=null) {
        	pageForm = (OrderForm) rq.getAttribute("ofjo"); // Übergabe aus checkAvailability von getOpenUrlRequest und nach Kunde neu erstellen...
        	rq.setAttribute("ofjo", pageForm);
        }
        
        if (pageForm.getKkid()==null) t = auth.grantAccess(rq);
        
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        if ((t!=null && t.getInhalt()!=null) || (pageForm.getKkid()!=null || pageForm.getBkid()!=null) || auth.isLogin(rq)) {
        	
            forward = "success";
            
            try {
                
                if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                	List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                	AbstractBenutzer b = new AbstractBenutzer();
                	b = ui.getBenutzer();
                	kontouser.add(b);
                	pageForm.setKontouser(kontouser);
                } 
                if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                	pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                }
                
                if (ui != null) { // bei IP-basiertem Zugriff kein ui vorhanden
                // in pageForm Defaultpreis von Subito legen, damit Preisauswahl für manuelle Bestellung bei Subito vorhanden ist
                DefaultPreis dp = new DefaultPreis();
                dp = dp.getDefaultPreis("Subito", ui.getKonto().getId(), cn.getConnection());
                pageForm.setPreisvorkomma(dp.getVorkomma());
                pageForm.setPreisnachkomma(dp.getNachkomma());
                pageForm.setWaehrung(dp.getWaehrung());
                // Default Bestellart setzen, falls nicht schon eine deloption übers Formular angegeben wurde
                if (ui.getKonto().getFaxno()==null && (pageForm.getDeloptions()==null || 
                	pageForm.getDeloptions().equals("")) ) pageForm.setDeloptions(ui.getKonto().getDefault_deloptions());
                }
                
                if (pageForm.getSubmit().contains("GBV")) { // Bestellung über GBV
                	if (auth.isUserGBVBestellung(rq)) { // verhindert URL-hacking
                		pageForm.setSubmit("GBV"); // setzt z.B. "zur GBV-Bestellung" auf "GBV", um in journalorder.jsp zwischen Subito- und GBV-Bestellungen unterscheiden zu können.
                	pageForm.setMaximum_cost("8"); // Default bei GBV
                	if (ui.getKonto().getIsil()==null || ui.getKonto().getGbvrequesterid()==null) {pageForm.setManuell(true);} else {pageForm.setManuell(false);} // ISIL wird für autom. Bestellung benötigt
                	} else {
                		pageForm.setSubmit("bestellform");
                	}
                }
                
                // Benutzer ohne Bestellberechtigung werden auf Bestellformular (Mail an Bibliothek statt direkt bestellen) weitergeleitet
                // Bei Übergabe aus Linkresolver, Einloggen und mediatype != Artikel => auf Bestellformular, da keine Bestellung über Subito möglich...
                // ui == null => IP-basierter Zugriff
                
                if (ui == null ||  // IP-basierter Zugriff
                	// erste Kondition ist problematisch falls die Übergabe ab pl (prepareLogin) kommt und der Kunde Benutzer mit GBV-Bestellberechtigung ist.
                	// d.h. GBV-Submit ist nicht vorhanden und er hat keine Wahl den Artikel beim GBV zu bestellen....
                	(!auth.isUserSubitoBestellung(rq) && !(auth.isUserGBVBestellung(rq) && pageForm.getSubmit().equals("GBV")) ) || // keine Bestellberechtigung
                	(!pageForm.getSubmit().equals("GBV") && auth.isBenutzer(rq) && (pageForm.getMediatype()==null || !pageForm.getMediatype().equals("Artikel"))) || // Für Subito nur Artikel zugelassen...
                	  pageForm.getSubmit().contains("meine Bibliothek") || pageForm.getSubmit().contains("my library") || 
                	  pageForm.getSubmit().contains("ma bibliothèque") || pageForm.getSubmit().contains("bestellform")) { // der Kunde will das Doku bei seiner Bibliothek bestellen
                	
                	forward = "bestellform";
                    if (pageForm.getDeloptions() == null || // Defaultwert deloptions
                            (!pageForm.getDeloptions().equals("post") && !pageForm.getDeloptions().equals("fax to pdf") && !pageForm.getDeloptions().equals("urgent")) ) {
                    			pageForm.setDeloptions("fax to pdf");
                         	}                	
                }
                
                // Bei Bibliothekaren läuft eine Nicht-GBV-Bestellung mit mediatype!=Artikel auf das Formular zum manuellen Speichern einer Bestellung
                if (ui != null && !pageForm.getSubmit().equals("GBV") && !auth.isBenutzer(rq) &&
                	(pageForm.getMediatype()==null || !pageForm.getMediatype().equals("Artikel"))) {
                	forward = "save";
                }
                // Umleitung bei Subito-Bestellung auf redirectsubito, da autom. Bestellung nicht mehr machbar
                if (forward.equals("success") && !pageForm.getSubmit().equals("GBV")) {
                	forward = "redirectsubito";
                	pageForm.setLink("http://www.subito-doc.de/order/po.php?BI=CH_SO%2FDRDOC&VOL=" +
                    		pageForm.getJahrgang() + "/" + pageForm.getHeft() + "&APY=" + pageForm.getJahr() +
                    		"&PG=" + pageForm.getSeiten() + "&SS=" + pageForm.getIssn() + "&JT=" + pageForm.getZeitschriftentitel() +
                    		"&ATI=" + pageForm.getArtikeltitel() + "&AAU=" + pageForm.getAuthor());
                	if (pageForm.getDeloptions()!=null && pageForm.getDeloptions().equals("")) { // deloptions einstellen
                		if (ui.getKonto().getFaxno()!=null) {
                			pageForm.setDeloptions("fax to pdf");
                		} else {
                			pageForm.setDeloptions(ui.getKonto().getDefault_deloptions());
                		}
                	}
                }
                
             // für Get-Methode in PrepareLogin of URL-codieren
                pageForm = pageForm.encodeOrderForm(pageForm);
                
                rq.setAttribute("orderform", pageForm);

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("prepare: " + e.toString());

            } finally {
            	cn.close();
            }
            
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        return mp.findForward(forward);
    }
    
   
    /**
     * Bereitet das Abspeichern aller momentan vorhandenen Angaben vor
     */
    public ActionForward prepareJournalSave(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        Auth auth = new Auth();
        Text cn = new Text();
        Lieferanten lieferantenInstance = new Lieferanten();
        if (rq.getAttribute("ofjo")!=null) {
        	pageForm = (OrderForm) rq.getAttribute("ofjo");
        	rq.setAttribute("ofjo", null);
        }
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        if (auth.isLogin(rq)) {
            forward = "success";
            try {
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            	Texttyp tty = new Texttyp();
                
                if (auth.isBenutzer(rq)) { // user may only see his own address
                	List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                	AbstractBenutzer b = new AbstractBenutzer();
                	b = ui.getBenutzer();
                	kontouser.add(b);
                	pageForm.setKontouser(kontouser);
                } else {
                	pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                }
              
                if (pageForm.getDeloptions()==null || pageForm.getDeloptions().equals("")) pageForm.setDeloptions("email"); // default
                if (pageForm.getMediatype()==null) pageForm.setMediatype("Artikel"); // default value 'article'
                if (pageForm.getBid()==null && pageForm.getMediatype().equals("Buch")) { // if not coming from function reorder with an existing bid
                	pageForm.setDeloptions("post"); // logical consequence
                	pageForm.setFileformat("Papierkopie"); // logical consequence
                }
                
            	long id = 2; // Bestellstati
            	tty.setId(id);
                pageForm.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                pageForm.setQuellen(lieferantenInstance.getListForKontoAndCountry(ui.getKonto().getLand(), ui.getKonto().getId(), cn.getConnection()));
                id = 7; // Waehrungen
                tty.setId(id);
                pageForm.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                DefaultPreis dp = new DefaultPreis();
                pageForm.setDefaultpreise(dp.getAllKontoDefaultPreise(ui.getKonto().getId(), cn.getConnection()));                
                
                // benötigt damit auf journalsave.jsp lieferant.name nicht kracht...
                Lieferanten l = new Lieferanten();
                if (pageForm.getLid()!=null && !pageForm.getLid().equals("") && !pageForm.getLid().equals("0")) { // lid wurde übermittelt aus pageForm
                	l = lieferantenInstance.getLieferantFromLid(pageForm.getLid(), cn.getConnection());
                } else {
                	l.setName("k.A.");
                	l.setLid(Long.valueOf(0));
                }

                pageForm.setLieferant(l);
                pageForm.setBestellquelle(l.getName());
                
                if (pageForm.getStatus()==null) pageForm.setStatus("bestellt"); // Default

                rq.setAttribute("orderform", pageForm);

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("prepareJournalSave: " + e.toString());
            } finally {
            	cn.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        
        ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute("ActiveMenus", mf);
        
        return mp.findForward(forward);
    }
    
    
    
    /**
     * Speichert eine manuelle Bestellung ab
     */
    public ActionForward saveOrder(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        Lieferanten lieferantenInstance = new Lieferanten();
        OrderState orderstate = new OrderState();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isLogin(rq)) {
        	
           // aufgrund von IE Bug wird value bei einem eigenen Icon im submit nicht übermittelt:
        if (!pageForm.getSubmit().equals("neuen Kunden anlegen") && !pageForm.getSubmit().equals("add new patron") &&
        	!pageForm.getSubmit().equals("Ajouter un nouveau client")) { // Post-Methode um vor dem Abspeichern einer Bestellung einen neuen Kunden anzulegen
        	
            forward = "success";
            
            Text cn = new Text();
            
            try {
            	Bestellungen b = new Bestellungen();
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            	            	
            	if (pageForm.getStatus().equals("0")) pageForm.setStatus("bestellt"); // Defaultwert, falls keine Angaben (stellt sicher, dass History funktioniert)
            	
            	pageForm.setKaufpreis(b.stringToBigDecimal(pageForm.getPreisvorkomma(), pageForm.getPreisnachkomma()));
            	
            	try {
            	
            	if (pageForm.isPreisdefault() == true) {
            		DefaultPreis dp = new DefaultPreis(pageForm, ui);            		
            		dp.saveOrUpdate(cn.getConnection());
            	} else {
//            		System.out.println("kein Default-Preis...");
            	}
            	
            	} catch (Exception e) {
            		log.error("SaveOrder Default-Preis eintragen: " + e.toString());
            	}
            		
            	
        		Date d = new Date(); 
        		ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datum = fmt.format(d, ui.getKonto().getTimezone());
//                System.out.println("Bestelldatum: " + datum);
            	
//            	 Bestellung in DB speichern:
                AbstractBenutzer kunde = new AbstractBenutzer();
                kunde = kunde.getUser(new Long(pageForm.getForuser()), cn.getConnection());
                
                
                
                if (pageForm.getBid()!=null) {
                	b = new Bestellungen(cn.getConnection(), pageForm.getBid()); // zum Updaten vollständige Bestellung holen
                	if (!pageForm.getStatus().equals(b.getStatustext())) { // falls Status verändert wurde
                        
                        // um zu verhindern, dass eine Bestellung kein Statusdatum erhält, falls beim Statusschreiben etwas schief geht
                        b.setStatusdate(datum);
                        b.setStatustext(pageForm.getStatus());
              
                        Text t = new Text(cn.getConnection(), pageForm.getStatus());

                        orderstate.setNewOrderState(b, ui.getKonto(), t, null, ui.getBenutzer().getEmail(), cn.getConnection()); // Status setzen
                		
                	}
                }
                
                if (kunde == null) kunde = ui.getBenutzer(); // falls keine Kundenangaben => Besteller = eingeloggter User
                
                b.setKonto(ui.getKonto());
                b.setBenutzer(kunde);
                
                b.setLieferant(lieferantenInstance.getLieferantFromLid(pageForm.getLid(), cn.getConnection()));
                if (b.getLieferant().getSigel()==null || b.getLieferant().getSigel().equals("")) {
                	b.setBestellquelle(b.getLieferant().getName()); // doppelter Eintrag um Sortieren und Suche zu ermöglichen/vereinfachen
                } else { // Eintrag mit Sigel
                	b.setBestellquelle(b.getLieferant().getSigel() + "\040" + b.getLieferant().getName()); // doppelter Eintrag um Sortieren und Suche zu ermöglichen/vereinfachen
                }                	
                b.setPriority(pageForm.getPrio());
                b.setDeloptions(pageForm.getDeloptions());
                b.setFileformat(pageForm.getFileformat());
                if (pageForm.getDeloptions().equalsIgnoreCase("post")) b.setFileformat("Papierkopie"); // logische Konsequenz...
                b.setHeft(pageForm.getHeft());
                b.setSeiten(pageForm.getSeiten());
                b.setIssn(pageForm.getIssn());
                b.setAutor(pageForm.getAuthor());
                b.setZeitschrift(pageForm.getZeitschriftentitel());
                b.setJahr(pageForm.getJahr());
                b.setArtikeltitel(pageForm.getArtikeltitel());
                b.setJahrgang(pageForm.getJahrgang());
                b.setDoi(pageForm.getDoi());
                b.setPmid(pageForm.getPmid());
                b.setIsbn(pageForm.getIsbn());
                b.setMediatype(pageForm.getMediatype());
                b.setVerlag(pageForm.getVerlag());
                b.setKapitel(pageForm.getKapitel());
                b.setBuchtitel(pageForm.getBuchtitel());
                b.setInterne_bestellnr(pageForm.getInterne_bestellnr());
                b.setSubitonr(extractSubitonummer(pageForm.getSubitonr())); // Subitonr. normalisieren, da relativ komplex aufgebaut SUBITO:2009040801219
                b.setGbvnr(pageForm.getGbvnr()); // relativ einfach aufgebaut: A09327811X
                
                b.setSystembemerkung(pageForm.getAnmerkungen());
                b.setNotizen(pageForm.getNotizen());
                b.setKaufpreis(pageForm.getKaufpreis());
                if (pageForm.getKaufpreis() != null) {b.setWaehrung(pageForm.getWaehrung());} else {b.setWaehrung(null);}
                
                if (pageForm.getBid()==null) { // hier wird eine neue Bestellung abgespeichert
                
                // um zu verhindern, dass eine Bestellung kein Datum erhält, falls beim Statusschreiben etwas schief geht
                b.setOrderdate(datum);
                b.setStatusdate(datum);
                b.setStatustext(pageForm.getStatus());

                b.save(cn.getConnection());
                
                //Sicherheit, ob das so wirklich klappt mit Benachrichtigung
                if (b.getId()==null){
                	b = b.getOrderSimpleWay(b, cn.getConnection());
                	log.warn("b.getId() has been null! We had to use b.getOrderSimpleWay!");
                }
                
                // klappt leider nicht zuverlässig (s. Workaround oben)                
                Text t = new Text(cn.getConnection(), pageForm.getStatus());

                orderstate.setNewOrderState(b, ui.getKonto(), t, null, ui.getBenutzer().getEmail(), cn.getConnection()); // Status Bestellt setzen
                
                } else { // hier wird eine bestehende Bestellung geupdated
                	b.update(cn.getConnection());                	
                }
                
                rq.setAttribute("orderform", pageForm);
                          

            } catch (Exception e) {
                forward = "failure";

                ErrorMessage em = new ErrorMessage();
                em.setError("error.save");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("saveOrder: " + e.toString());

            } finally {
            	cn.close();
            }
            
        } else { // Umleitung zu Kundenanlegen        	
        	forward = "newcustomer";
        	pageForm.setOrigin("js");
        	rq.setAttribute("orderform", pageForm);        	
        }
            
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }
    
    /**
     * bereitet das manuelle Ändern einer Bestellung vor
     */
    public ActionForward prepareModifyOrder(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = "failure";
        Auth auth = new Auth();
        if (auth.isLogin(rq)) {
            forward = "success";
            
            Text cn = new Text();
            
            try {
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
            	
            	Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());
            	
            	if (b.getId()!=null) {
            	
            	pageForm = new OrderForm(b);
            	
            	if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                	List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                	AbstractBenutzer ab = ui.getBenutzer();
                	kontouser.add(ab);
                	pageForm.setKontouser(kontouser);
                } else {
                	pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                }
            	
                Texttyp tty = new Texttyp();
            	long id = 2; // Bestellstati
            	tty.setId(id);
                pageForm.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));

                Lieferanten lieferantenInstance = new Lieferanten();
                pageForm.setQuellen(lieferantenInstance.getListForKontoAndCountry(ui.getKonto().getLand(), ui.getKonto().getId(), cn.getConnection()));
                id = 7; // Waehrungen
                tty.setId(id);
                pageForm.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                
                DefaultPreis dp = new DefaultPreis();
                pageForm.setDefaultpreise(dp.getAllKontoDefaultPreise(ui.getKonto().getId(), cn.getConnection()));
                
                rq.setAttribute("orderform", pageForm);
                
                if (b.checkAnonymize(b)) {            	
            		
            			forward = "failure";
                        ErrorMessage em = new ErrorMessage("error.anonymised", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute("errormessage", em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
            	}
                
            	if (auth.isBibliothekar(rq)) {            	
            		if (!b.getKonto().getId().equals(ui.getKonto().getId())) { // Sicherstellen, dass der Bibliothekar nur Bestellungen vom eigenen Konto bearbeitet!
            			System.out.println("URL-hacking... ;-)");
            			forward = "failure";
                        ErrorMessage em = new ErrorMessage("error.hack", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute("errormessage", em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                        log.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
            	}
            	}
            	if (auth.isBenutzer(rq)) {         	
                	if (!b.getBenutzer().getId().equals(ui.getBenutzer().getId())) { // Sicherstellen, dass der User nur eigene Bestellungen bearbeitet!
                		System.out.println("URL-hacking... ;-)");
                		forward = "failure";
                        ErrorMessage em = new ErrorMessage("error.hack", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute("errormessage", em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                        log.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                	}
                	}
                          
            } else {
            	forward = "failure";
                ErrorMessage em = new ErrorMessage("error.hack", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                rq.setAttribute("errormessage", em);
                rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                log.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
            	
            }
            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
                log.error("prepareModifyOrder: " + e.toString());

            } finally {
            	cn.close();
            }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }
    
    /**
     * löscht eine Bestellung
     */
    public ActionForward prepareDeleteOrder(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        String forward = "failure";
        Text cn = new Text();
        
        Auth auth = new Auth();
        if (auth.isLogin(rq)) { // Test auf gültige Session
        	Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());
        	
        if (b.getId()!=null && // BID muss vorhanden sein
        	(auth.isBibliothekar(rq) || auth.isAdmin(rq)) && // nur Bibliothekare und Admins dürfen Bestellungen löschen
        	auth.isLegitimateOrder(rq, b)) { // nur kontoeigene Bestellungen dürfen gelöscht werden
            
        	forward = "promptDelete";
        	pageForm.setDelete(true);
        	pageForm.setBestellung(b);
        	rq.setAttribute("orderform", pageForm);
        	
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
        
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.hack", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
            rq.setAttribute("errormessage", em);
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            log.info("prepareDeleteOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
        }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        cn.close();
        return mp.findForward(forward);
    }
    
    
    /**
     * löscht eine Bestellung
     */
    public ActionForward deleteOrder(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        String forward = "failure";
        Text cn = new Text();
        
        Auth auth = new Auth();
        if (auth.isLogin(rq)) { // Test auf gültige Session
        	Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());
        	
        if (b.getId()!=null && // BID muss vorhanden sein
        	(auth.isBibliothekar(rq) || auth.isAdmin(rq)) && // nur Bibliothekare und Admins dürfen Bestellungen löschen
        	auth.isLegitimateOrder(rq, b)) { // nur kontoeigene Bestellungen dürfen gelöscht werden
            
        	if(b.deleteBestellung(b, cn.getConnection())) {
        		forward = "success";
        		ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("uebersicht");
                rq.setAttribute("ActiveMenus", mf);
        		Message m = new Message("message.deleteorder");
                m.setLink("listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                rq.setAttribute("message", m);
        	} else { // löschen fehlgeschlagen
        		ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);  
                log.error("deleteOrder: couldn't delete order");
        	}
        
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("uebersicht");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.hack", "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
            rq.setAttribute("errormessage", em);
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            log.info("deleteOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
        }
        } else {
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        cn.close();
        return mp.findForward(forward);
    }

	  public String extractJahreszahl(String datum) {
		  String jahr = "";
		  // Suchpattern funktioniert vom 14. Jahrhundert bis 22. Jahrhundert. Sollte vermutlich für die nächste Zeit reichen...
		  Pattern p = Pattern.compile("13[0-9]{2}|14[0-9]{2}|15[0-9]{2}|16[0-9]{2}|17[0-9]{2}|18[0-9]{2}|19[0-9]{2}|20[0-9]{2}|21[0-9]{2}");
	  	  Matcher m = p.matcher(datum);
	  	  try{
	  	  if (m.find()) { // Idee: nur erste Zahl abfüllen...
	  		 jahr = datum.substring(m.start(), m.end()); // hier wird nur der letzte Treffer abgefüllt...
	  	  	}
	  	  } catch (Exception e) {
	  		log.error("extractJahreszahl(String datum): " + datum + "\040" + e.toString());
	  	  }
		  
		return jahr;
      }
	  
	  private String extractSubitonummer(String subitonr) {
		  
	  Pattern z = Pattern.compile("[A-Z]{0,2}[0-9]+");
  	  Matcher w = z.matcher(subitonr);    	  
  	  try{
  	  if (w.find()) { // Idee: nur erste Zahl abfüllen...
  		 subitonr = "SUBITO:" + subitonr.substring(w.start(), w.end());
  	  	}
  	  }
  	  catch (Exception e) {
  		log.error("String extractSubitonummer(String subitonr): " + subitonr + "\040" + e.toString());  		  
  	  }

  	  return subitonr;
	  }
    
    private String getWebcontent(String link, int timeout_ms, int retrys){
    	Http http = new Http();

        return http.getWebcontent(link, timeout_ms, retrys);
    }
    
	  private String googleDidYouMean(String artikeltitel_encoded) {
		String meinten_sie = "";
		
		String google_link = "http://www.google.ch/search?as_q=&hl=de&num=10&btnG=Google-Suche&as_oq=&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=" + artikeltitel_encoded;
		String google_content = getWebcontent(google_link, 1000, 3);
		
		if (google_content.contains("Meinten Sie:")){
		int start = google_content.indexOf("Meinten Sie:");
		start = google_content.indexOf("q=", start)+2;
		meinten_sie = google_content.substring(start, google_content.indexOf("&", start+2));
		}

  	  return meinten_sie;
	  }
	  
	  private String shortenGoogleSearchPhrase(String artikeltitel) {
			
		   	try {		
	    		if (artikeltitel.length()>75){ // Google ist limitiert in der Laenge bei Phrasen-Suche im Titel
	    			if (artikeltitel.substring(0, 75).contains("\040")) {
	    				artikeltitel = artikeltitel.substring(0, artikeltitel.lastIndexOf("\040", 75));
	    			}
	    		}
	    		
	        } catch(Exception e){
	        	log.error("googlePreparePhraseSearch: " + artikeltitel + "\040" + e.toString());
	        }		
			

	  	  return artikeltitel;
		  }
	  
	  private Message handleGoogleCaptcha(String content) {
			
		  Message m = new Message();
		  SpecialCharacters specialCharacters = new SpecialCharacters();
		  content = specialCharacters.replace(content);
		  String link = "";
		  String id = "";
		  
		  if (content.contains("sorry/image?id=")) { // Versuch 1
			  
			 link = content.substring(content.indexOf("sorry/image?id="), content.indexOf("\"", content.indexOf("sorry/image?id=")));
			 if (link.contains("&")) link = link.substring(0, link.indexOf("&"));
			 link = "http://www.google.ch/" + link;
			 id = link.substring(link.indexOf("sorry/image?id=")+15);

			  
		  } else {
			  
			  if (content.contains("name=\"id\" value=\"")) { // Versuch 2
				  
				  id = content.substring(content.indexOf("name=\"id\" value=\"")+17, content.indexOf("\"", content.indexOf("name=\"id\" value=\"")+17));
				  link = "http://www.google.ch/sorry/image?id=" + id;
				  
			  }
			  
		  }  
			 
			 m.setLink(link);
			 m.setMessage(id);
			 
			 log.warn("Google-Captcha!");
			 // Important message
			 MHelper mh = new MHelper();
			 mh.sendErrorMail("Google-Captcha Alarm!!!", "handleGoogleCaptcha:\012" + content);

	  	  return m;
		  }
	  
	  private String correctWorldCat(String input) {
		  // Methode um abartige Umlaute aus Resultat von WorldCat zu entfernen
		  String output = input;
		  
		  output = output.replaceAll("%CC%90%C6%B0", "");
		  output = output.replaceAll(".\\+--&", "&"); // WorldCat setzt manchmal '. --' ans Ende eines Identifiers...
		  
		  return output;
	  }
	  
	  private String correctGoogleURL(String input) {
		  // Methode um gefundene Google-URL für die Ausgabe vorzubereiten
		  String output = input;
		  SpecialCharacters specialCharacters = new SpecialCharacters();
		  
		  if (input.startsWith("/url?q=")) { // hier ist eigentlich kein Treffer gefunden worde. Google schlägt ähnliche Treffer mit dieser URL-Syntax vor...			  
			  output = input.substring(7);			  
		  }
		  
		  output = specialCharacters.replace(output);
		  
		  return output;
	  }
	 
	  private String correctArtikeltitIssnAssist(String input) {
		  // Methode um die Trefferchancen beim ISSN-Assistenten zu erhöhen
		  String output = input;
		  SpecialCharacters specialCharacters = new SpecialCharacters();
		  	  
		  output = output.replaceAll("\040und\040", "\040"); // entfernt "und"	  
		  output = specialCharacters.replace(output); // übersetzt allfällige &amp; in &
		  output = output.replaceAll("\040&\040", "\040"); // entfernt &
		  output = output.replaceAll("\040+\040", "\040"); // entfernt +
		  
		  return output;
	  }
	  
	  /**
	   * Holt aus einer ArrayList<Bestand> die eigenen Bestände
	   */
	  private ArrayList<Bestand> extractInternalHoldings (ArrayList<Bestand> bestaende, long id) {
		  
		  ArrayList<Bestand> internalHoldings = new ArrayList<Bestand>();
		  
		  try {
			  
			  for (int i=0;i<bestaende.size();i++) {
				  if (bestaende.get(i).getHolding().getKid().equals(id)) {
					  internalHoldings.add(bestaende.get(i));
					  }
			  }
			  
		  } catch(Exception e) {
			  log.error("ArrayList<Bestand> extractInternalHoldings (ArrayList<Bestand> bestaende, long daiaId): " + e.toString());
		  }
		  
		  return internalHoldings;
	  }
	  
	  /**
	   * Holt aus einer ArrayList<Bestand> die Fremdbestände
	   */
	  private ArrayList<Bestand> extractExternalHoldings (ArrayList<Bestand> bestaende, long id) {
		  
		  ArrayList<Bestand> externalHoldings = new ArrayList<Bestand>();
		  
		  try {
			  
			  for (int i=0;i<bestaende.size();i++) {
				  if (!bestaende.get(i).getHolding().getKid().equals(id) && 
					  !bestaende.get(i).isInternal()) {
					  externalHoldings.add(bestaende.get(i));
					  }
			  }
			  
		  } catch(Exception e) {
			  log.error("ArrayList<Bestand> extractExternalHoldings (ArrayList<Bestand> bestaende, long daiaId): " + e.toString());
		  }
		  
		  return externalHoldings;
	  }
	  
	  /**
	   * Nimmt grundsätzlich ein Mapping der KIDs aus verschiedenen Doctor-Doc-Instanzen vor
	   * um die Bestände den entsprechenden Konti zuordenen zu können.
	   */
	  private long getDaiaId (long kid) {
		  long daiaId = 0;
		  
		  try {
		  Text cn = new Text();
		  Text t = new Text(cn.getConnection(), new Texttyp("DAIA-ID", cn.getConnection()), kid);
		  cn.close();
		  if (t!=null && t.getInhalt()!=null) daiaId = Long.valueOf(t.getInhalt());
		  } catch (Exception e) {
			  log.error("getDaiaId - kid: " + kid + "\040" + e.toString());
		  }
		  
		  return daiaId;
	  }
	  
	  private boolean checkPrepareWorldCat1(String input) {
		  
		  // Methode um zu prüfen, ob eine zusätzliche WorldCat-Abfrage gefahren werden muss...
		  boolean check = false;
		  CodeUrl codeUrl = new CodeUrl();
		  
		  input = codeUrl.encodeLatin1(input);
		  
		  if (input.contains("%E4") || input.contains("%F6") || input.contains("%FC") ||
			  input.contains("%C4") || input.contains("%D6") || input.contains("%DC")) {
			  check = true;
		  }

		  // nicht abschliessend...

  
		  return check;
	  }
	  
	  private String prepareWorldCat1(String input) {
		  
		  // Methode um Treffer aus WorldCat bei Suchstrings mit Umlauten zu erhalten
		  String output = input;
		  
		  output = output.replaceAll("\\+-\\+", "\\+"); // WorldCat akzeptiert keine " - "	
		  
		  output = output.replaceAll("%E4", "u%92a");
		  output = output.replaceAll("%F6", "u%92o");
		  output = output.replaceAll("%FC", "u%92u");
		  output = output.replaceAll("%C4", "u%92A");
		  output = output.replaceAll("%D6", "u%92O");
		  output = output.replaceAll("%DC", "u%92U");
		  // nicht abschliessend...

		  
//		  C0	192	À	ANSI 192	großes A Grave
//		  C1	193	Á	ANSI 193	großes A Acute
//		  C2	194	Â	ANSI 194	großes A Zirkumflex
//		  C3	195	Ã	ANSI 195	großes A Tilde
//		  C4	196	Ä	ANSI 196	großes A Umlaut
//		  C5	197	Å	ANSI 197	großes A mit Ring
//		  C6	198	Æ	ANSI 198	große AE-Ligatur
//		  C7	199	Ç	ANSI 199	großes C mit Cedille
//		  C8	200	È	ANSI 200	großes E Grave
//		  C9	201	É	ANSI 201	großes E Acute
//		  CA	202	Ê	ANSI 202	großes E Zirkumflex
//		  CB	203	Ë	ANSI 203	großes E Trema
//		  CC	204	Ì	ANSI 204	großes I Grave
//		  CD	205	Í	ANSI 205	großes I Acute
//		  CE	206	Î	ANSI 206	großes I Zirkumflex
//		  CF	207	Ï	ANSI 207	großes I Trema
//		  D0	208	Ð	ANSI 208	Isländisches großes Eth
//		  D1	209	Ñ	ANSI 209	großes N Tilde
//		  D2	210	Ò	ANSI 210	großes O Grave
//		  D3	211	Ó	ANSI 211	großes O Acute
//		  D4	212	Ô	ANSI 212	großes O Zirkumflex
//		  D5	213	Õ	ANSI 213	großes O Tilde
//		  D6	214	Ö	ANSI 214	großes O Umlaut
//		  D7	215	×	ANSI 215	Multiplikationszeichen
//		  D8	216	Ø	ANSI 216	großes O mit diagonalem Strich
//		  D9	217	Ù	ANSI 217	großes U Grave
//		  DA	218	Ú	ANSI 218	großes U Acute
//		  DB	219	Û	ANSI 219	großes U Zirkumflex
//		  DC	220	Ü	ANSI 220	großes U Umlaut
//		  DD	221	Ý	ANSI 221	großes Y Acute
//		  DE	222	Þ	ANSI 222	Isländisches großes Thorn
//		  DF	223	ß	ANSI 223	Deutsches scharfes S (sz-Ligatur)
//		  E0	224	à	ANSI 224	kleines a Grave
//		  E1	225	á	ANSI 225	kleines a Acute
//		  E2	226	â	ANSI 226	kleines a Zirkumflex
//		  E3	227	ã	ANSI 227	kleines a Tilde
//		  E4	228	ä	ANSI 228	kleines a Umlaut
//		  E5	229	å	ANSI 229	kleines a Ring
//		  E6	230	æ	ANSI 230	kleine ae-Ligatur
//		  E7	231	ç	ANSI 231	kleines c Cedille
//		  E8	232	è	ANSI 232	kleines e Grave
//		  E9	233	é	ANSI 233	kleines e Acute
//		  EA	234	ê	ANSI 234	kleines e Zirkumflex
//		  EB	235	ë	ANSI 235	kleines e Trema
//		  EC	236	ì	ANSI 236	kleines i Grave
//		  ED	237	í	ANSI 237	kleines i Acute
//		  EE	238	î	ANSI 238	kleines i Zirkumflex
//		  EF	239	ï	ANSI 239	kleines i Trema
//		  F0	240	ð	ANSI 240	Isländisches kleines eth
//		  F1	241	ñ	ANSI 241	kleines n Tilde
//		  F2	242	ò	ANSI 242	kleines o Grave
//		  F3	243	ó	ANSI 243	kleines o Acute
//		  F4	244	ô	ANSI 244	kleines o Zirkumflex
//		  F5	245	õ	ANSI 245	kleines o Tilde
//		  F6	246	ö	ANSI 246	kleines o Umlaut
//		  F7	247	÷	ANSI 247	Divisionszeichen
//		  F8	248	ø	ANSI 248	kleines o mit diagonalem Strich
//		  F9	249	ù	ANSI 249	kleines u Grave
//		  FA	250	ú	ANSI 250	kleines u Acute
//		  FB	251	û	ANSI 251	kleines u Zirkumflex
//		  FC	252	ü	ANSI 252	kleines u Umlaut
//		  FD	253	ý	ANSI 253	kleines y Acute
//		  FE	254	þ	ANSI 254	Isländisches kleines thorn
//		  FF	255	ÿ	ANSI 255	kleines y Umlaut
		  
		  return output;
	  }
	  
	  private String prepareWorldCat2(String input) {
		  // Methode um Treffer aus WorldCat bei Suchstrings mit Umlauten zu erhalten
		  String output = input;
		  
		  output = output.replaceAll("–", "-"); // WorldCat akzeptiert keine ndash 
		  output = output.replaceAll("", "-"); // WorldCat akzeptiert keine ndash 
		  output = output.replaceAll("\\+-\\+", "\\+"); // WorldCat akzeptiert keine " - "
		  output = output.replaceAll("", ""); // scheint für ¨ zu stehen...
		  
		  output = output.replaceAll("%C0", "A"); //		  C0	192	À	ANSI 192	großes A Grave
		  output = output.replaceAll("%C1", "A"); //		  C1	193	Á	ANSI 193	großes A Acute
		  output = output.replaceAll("%C2", "A"); //		  C2	194	Â	ANSI 194	großes A Zirkumflex
		  output = output.replaceAll("%C3", "A"); //		  C3	195	Ã	ANSI 195	großes A Tilde
		  output = output.replaceAll("%C4", "A"); //		  C4	196	Ä	ANSI 196	großes A Umlaut
		  output = output.replaceAll("%C5", "A"); //		  C5	197	Å	ANSI 197	großes A mit Ring
		  output = output.replaceAll("%C6", "AE"); // ?		  C6	198	Æ	ANSI 198	große AE-Ligatur
		  output = output.replaceAll("%C7", "C"); //		  C7	199	Ç	ANSI 199	großes C mit Cedille
		  output = output.replaceAll("%C8", "E"); //		  C8	200	È	ANSI 200	großes E Grave
		  output = output.replaceAll("%C9", "E"); //		  C9	201	É	ANSI 201	großes E Acute
		  output = output.replaceAll("%CA", "E"); //		  CA	202	Ê	ANSI 202	großes E Zirkumflex
		  output = output.replaceAll("%CB", "E"); //		  CB	203	Ë	ANSI 203	großes E Trema
		  output = output.replaceAll("%CC", "I"); //		  CC	204	Ì	ANSI 204	großes I Grave
		  output = output.replaceAll("%CD", "I"); //		  CD	205	Í	ANSI 205	großes I Acute
		  output = output.replaceAll("%CE", "I"); //		  CE	206	Î	ANSI 206	großes I Zirkumflex
		  output = output.replaceAll("%CF", "I"); // 		  CF	207	Ï	ANSI 207	großes I Trema	  
		  output = output.replaceAll("%D0", "D"); // ?		  D0	208	Ð	ANSI 208	Isländisches großes Eth
		  output = output.replaceAll("%D1", "N"); //		  D1	209	Ñ	ANSI 209	großes N Tilde
		  output = output.replaceAll("%D2", "O"); //		  D2	210	Ò	ANSI 210	großes O Grave
		  output = output.replaceAll("%D3", "O"); //		  D3	211	Ó	ANSI 211	großes O Acute
		  output = output.replaceAll("%D4", "O"); //		  D4	212	Ô	ANSI 212	großes O Zirkumflex
		  output = output.replaceAll("%D5", "O"); //		  D5	213	Õ	ANSI 213	großes O Tilde
		  output = output.replaceAll("%D6", "O"); //		  D6	214	Ö	ANSI 214	großes O Umlaut
		  output = output.replaceAll("%D7", "×"); // ?		  D7	215	×	ANSI 215	Multiplikationszeichen
		  output = output.replaceAll("%D8", "O"); //		  D8	216	Ø	ANSI 216	großes O mit diagonalem Strich
		  output = output.replaceAll("%D9", "U"); //		  D9	217	Ù	ANSI 217	großes U Grave
		  output = output.replaceAll("%DA", "U"); //		  DA	218	Ú	ANSI 218	großes U Acute
		  output = output.replaceAll("%DB", "U"); //		  DB	219	Û	ANSI 219	großes U Zirkumflex
		  output = output.replaceAll("%DC", "U"); //		  DC	220	Ü	ANSI 220	großes U Umlaut
		  output = output.replaceAll("%DD", "Y"); //		  DD	221	Ý	ANSI 221	großes Y Acute
		  output = output.replaceAll("%DE", "Þ"); // ?		  DE	222	Þ	ANSI 222	Isländisches großes Thorn
		  output = output.replaceAll("%DF", "ss"); //		  DF	223	ß	ANSI 223	Deutsches scharfes S (sz-Ligatur)
		  output = output.replaceAll("%E0", "a"); //		  E0	224	à	ANSI 224	kleines a Grave
		  output = output.replaceAll("%E1", "a"); //		  E1	225	á	ANSI 225	kleines a Acute
		  output = output.replaceAll("%E2", "a"); //		  E2	226	â	ANSI 226	kleines a Zirkumflex
		  output = output.replaceAll("%E3", "a"); // 		  E3	227	ã	ANSI 227	kleines a Tilde
		  output = output.replaceAll("%E4", "a"); //		  E4	228	ä	ANSI 228	kleines a Umlaut
		  output = output.replaceAll("%E5", "a"); //		  E5	229	å	ANSI 229	kleines a Ring
		  output = output.replaceAll("%E6", "ae"); // ?		  E6	230	æ	ANSI 230	kleine ae-Ligatur
		  output = output.replaceAll("%E7", "c"); //		  E7	231	ç	ANSI 231	kleines c Cedille
		  output = output.replaceAll("%E8", "e"); //		  E8	232	è	ANSI 232	kleines e Grave
		  output = output.replaceAll("%E9", "e"); //		  E9	233	é	ANSI 233	kleines e Acute
		  output = output.replaceAll("%EA", "e"); //		  EA	234	ê	ANSI 234	kleines e Zirkumflex
		  output = output.replaceAll("%EB", "e"); //		  EB	235	ë	ANSI 235	kleines e Trema
		  output = output.replaceAll("%EC", "i"); //		  EC	236	ì	ANSI 236	kleines i Grave
		  output = output.replaceAll("%ED", "i"); //		  ED	237	í	ANSI 237	kleines i Acute
		  output = output.replaceAll("%EE", "i"); //		  EE	238	î	ANSI 238	kleines i Zirkumflex
		  output = output.replaceAll("%EF", "i"); //		  EF	239	ï	ANSI 239	kleines i Trema
		  output = output.replaceAll("%F0", "ð"); // ?		  F0	240	ð	ANSI 240	Isländisches kleines eth
		  output = output.replaceAll("%F1", "n"); //		  F1	241	ñ	ANSI 241	kleines n Tilde
		  output = output.replaceAll("%F2", "o"); //		  F2	242	ò	ANSI 242	kleines o Grave
		  output = output.replaceAll("%F3", "o"); //		  F3	243	ó	ANSI 243	kleines o Acute
		  output = output.replaceAll("%F4", "o"); //		  F4	244	ô	ANSI 244	kleines o Zirkumflex
		  output = output.replaceAll("%F5", "o"); //		  F5	245	õ	ANSI 245	kleines o Tilde
		  output = output.replaceAll("%F6", "o"); //		  F6	246	ö	ANSI 246	kleines o Umlaut
		  output = output.replaceAll("%F7", "÷"); // ?		  F7	247	÷	ANSI 247	Divisionszeichen
		  output = output.replaceAll("%F8", "o"); //		  F8	248	ø	ANSI 248	kleines o mit diagonalem Strich
		  output = output.replaceAll("%F9", "u"); //		  F9	249	ù	ANSI 249	kleines u Grave
		  output = output.replaceAll("%FA", "u"); //		  FA	250	ú	ANSI 250	kleines u Acute
		  output = output.replaceAll("%FB", "u"); //		  FB	251	û	ANSI 251	kleines u Zirkumflex
		  output = output.replaceAll("%FC", "u"); //		  FC	252	ü	ANSI 252	kleines u Umlaut
		  output = output.replaceAll("%FD", "y"); //		  FD	253	ý	ANSI 253	kleines y Acute
		  output = output.replaceAll("%FE", "þ"); //		  FE	254	þ	ANSI 254	Isländisches kleines thorn
		  output = output.replaceAll("%FF", "y"); //		  FF	255	ÿ	ANSI 255	kleines y Umlaut
		  
			
			output = output.replaceAll("&#913;", "Alpha");
			output = output.replaceAll("&#914;", "Beta");
			output = output.replaceAll("&#915;", "Gamma");
			output = output.replaceAll("&#916;", "Delta");
			output = output.replaceAll("&#917;", "Epsilon");
			output = output.replaceAll("&#918;", "Zeta");
			output = output.replaceAll("&#919;", "Eta");
			output = output.replaceAll("&#920;", "Theta");
			output = output.replaceAll("&#921;", "Iota");
			output = output.replaceAll("&#922;", "Kappa");
			output = output.replaceAll("&#923;", "Lambda");
			output = output.replaceAll("&#924;", "Mu");
			output = output.replaceAll("&#925;", "Nu");
			output = output.replaceAll("&#926;", "Xi");
			output = output.replaceAll("&#927;", "Omicron");
			output = output.replaceAll("&#928;", "Pi");
			output = output.replaceAll("&#929;", "Rho");

			output = output.replaceAll("&#931;", "Sigma");
			output = output.replaceAll("&#932;", "Tau");
			output = output.replaceAll("&#933;", "Ypsilon");
			output = output.replaceAll("&#934;", "Phi");
			output = output.replaceAll("&#935;", "Chi");
			output = output.replaceAll("&#936;", "Psi");
			output = output.replaceAll("&#937;", "Omega");
			
			output = output.replaceAll("&#945;", "alpha");
			output = output.replaceAll("&#946;", "beta");
			output = output.replaceAll("&#947;", "gamma");
			output = output.replaceAll("&#948;", "delta");
			output = output.replaceAll("&#949;", "epsilon");
			output = output.replaceAll("&#950;", "zeta");
			output = output.replaceAll("&#951;", "eta");
			output = output.replaceAll("&#952;", "theta");
			output = output.replaceAll("&#953;", "iota");
			output = output.replaceAll("&#954;", "kappa");
			output = output.replaceAll("&#955;", "lambda");
			output = output.replaceAll("&#956;", "mu");
			output = output.replaceAll("&#957;", "nu");
			output = output.replaceAll("&#958;", "xi");			
			output = output.replaceAll("&#959;", "omicron");
			output = output.replaceAll("&#960;", "pi");
			output = output.replaceAll("&#961;", "rho");
			output = output.replaceAll("&#962;", "sigma");
			output = output.replaceAll("&#963;", "sigma");
			output = output.replaceAll("&#964;", "tau");
			output = output.replaceAll("&#965;", "ypsilon");
			output = output.replaceAll("&#966;", "phi");
			output = output.replaceAll("&#967;", "chi");
			output = output.replaceAll("&#968;", "psi");
			output = output.replaceAll("&#969;", "omega");
			
			output = output.replaceAll("&#977;", "theta");
			output = output.replaceAll("&#978;", "ypsilon");

		  
		  return output;
	  }
    
    
}
    
    
