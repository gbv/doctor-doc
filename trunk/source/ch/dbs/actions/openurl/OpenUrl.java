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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.grlea.log.SimpleLogger;

import util.Decoder;
import util.SpecialCharacters;
import util.CodeUrl;

public class OpenUrl {
	
	private static final SimpleLogger log = new SimpleLogger(OpenUrl.class);

    /**
     * Klasse um OpenURL anzuwenden
     * @author Markus Fischer
     */
	
	/**
	 * identifiziert die OpenURL-Identifiers aus einem Request
	 * verwendet die Standardmethoden von Java
	 */
	public ContextObject readOpenUrlFromRequest(HttpServletRequest rq) {
		
		ContextObject co = new ContextObject();
		ConcurrentHashMap<String, String> params = new ConcurrentHashMap<String, String>();
		params = getOpenUrlParameters(rq);
		
		Iterator<Map.Entry<String, String>> i = params.entrySet().iterator();
		
		ArrayList<String> uri_schemas = new ArrayList<String>();
	    
		while (i.hasNext()) {
			
		Map.Entry<String, String> pairs = (Map.Entry<String, String>)i.next();
		
		String key = pairs.getKey();
		String value = pairs.getValue();
	    
	    try {
	    
	    // Parameter aus OpenURL Version 1.0    	
    	if (key.equals("rft_val_fmt")) { 
    		co.setRft_val_fmt(value);
    		continue;
    	}
    	if (key.equals("rfr_id")) {
    		co.setRfr_id(value);
    		continue;
    	}
    	if (key.equals("rft.genre")) {
    		co.setRft_genre(value);
    		continue;
    	}
		if (key.equals("rft.atitle")) {
			co.setRft_atitle(value);
			continue;
		}
		if (key.equals("rft.btitle")) {
			co.setRft_btitle(value);
			continue;
		}
		if (key.equals("rft.series")) {
			co.setRft_series(value);
			continue;
		}
		if (key.equals("rft.pub")) {
			co.setRft_pub(value);
			continue;
		}
		if (key.equals("rft.place")) {
			co.setRft_place(value);
			continue;
		}
		if (key.equals("rft.edition")) {
			co.setRft_edition(value);
			continue;
		}
    	if (key.equals("rft.title")) {
    		co.setRft_title(value);
    		continue;
    	}
    	if (key.equals("rft.jtitle")) {
    		co.setRft_jtitle(value);
    		continue;
    	}
    	if (key.equals("rft.stitle")) {
    		co.setRft_stitle(value);
    		continue;
    	}
    	if (key.equals("rft.issn")) {
    		co.setRft_issn(value);
    		continue;
    	}
    	if (key.equals("rft.issn1")) {
    		co.setRft_issn1(value);
    		continue;
    	}
    	if (key.equals("rft.eissn")) {
    		co.setRft_eissn(value);
    		continue;
    	}
    	if (key.equals("rft.eissn1")) {
    		co.setRft_eissn1(value);
    		continue;
    	}
    	if (key.equals("rft.isbn")) {
    		co.setRft_isbn(value);
    		continue;
    	}
    	if (key.equals("rft.date")) {
    		co.setRft_date(value);
    		continue;
    	}
    	if (key.equals("rft.volume")) {
    		co.setRft_volume(value);
    		continue;
    	}
    	if (key.equals("rft.part")) {
    		co.setRft_part(value);
    		continue;
    	}
    	if (key.equals("rft.issue")) {
    		co.setRft_issue(value);
    		continue;
    	}
    	if (key.equals("rft.spage")) {
    		co.setRft_spage(value);
    		continue;
    	}
    	if (key.equals("rft.epage")) {
    		co.setRft_epage(value);
    		continue;
    	}
    	if (key.equals("rft.pages")) {
    		co.setRft_pages(value);
    		continue;
    	}
    	if (key.equals("rft.tpages")) {
    		co.setRft_tpages(value);
    		continue;
    	}
    	if (key.equals("rft.author")) {
    		co.setRft_author(value);
    		continue;
    	}
    	if (key.equals("rft.au")) {
    		co.setRft_au(value);
    		continue;
    	}
    	if (key.equals("rft.aucorp")) {
    		co.setRft_aucorp(value);
    		continue;
    	}
    	if (key.equals("rft.auinit")) {
    		co.setRft_auinit(value);
    		continue;
    	}
    	if (key.equals("rft.auinit1")) {
    		co.setRft_auinit1(value);
    		continue;
    	}
    	if (key.equals("rft.auinitm")) {
    		co.setRft_auinitm(value);
    		continue;
    	}
    	if (key.equals("rft.ausuffix")) {
    		co.setRft_ausuffix(value);
    		continue;
    	}
    	if (key.equals("rft.aulast")) {
    		co.setRft_aulast(value);
    		continue;
    	}
    	if (key.equals("rft.aufirst")) {
    		co.setRft_aufirst(value);
    		continue;
    	}
    	if (key.equals("rft.sici")) {
    		co.setRft_sici(value);
    		continue;
    	}
    	if (key.equals("rft.bici")) {
    		co.setRft_bici(value);
    		continue;
    	}
    	if (key.equals("rft.coden")) {
    		co.setRft_coden(value);
    		continue;
    	}
    	if (key.equals("rft.artnum")) {
    		co.setRft_artnum(value);
    		continue;
    	}
    	// RFTs Dublin Core, z.B. in Blogs:
    	if (key.equals("rft.creator")) {
    		co.setRft_creator(value);
    		continue;
    	}
    	if (key.equals("rft.publisher")) {
    		co.setRft_publisher(value);
    		continue;
    	}
    	if (key.equals("rft.type")) {
    		co.setRft_type(value);
    		continue;
    	}
    	if (key.equals("rft.subject")) {
    		co.setRft_subject(value);
    		continue;
    	}
    	if (key.equals("rft.format")) {
    		co.setRft_format(value);
    		continue;
    	}
    	if (key.equals("rft.language")) {
    		co.setRft_language(value);
    		continue;
    	}
    	if (key.equals("rft.source")) {
    		co.setRft_source(value);
    		continue;
    	}
    	if (key.equals("rft.identifier")) {
    		co.setRft_identifier(value);
    		continue;
    	}
    	if (key.equals("rft.description")) {
    		co.setRft_description(value);
    		continue;
    	}
    	if (key.equals("rft.relation")) {
    		co.setRft_relation(value);
    		continue;
    	}
    	if (key.equals("rft.coverage")) {
    		co.setRft_coverage(value);
    		continue;
    	}
    	if (key.equals("rft.rights")) {
    		co.setRft_rights(value);
    		continue;
    	}
    		
//    		 Parameter aus OpenURL 0.1.
        if (key.equals("id") && !value.startsWith("doi:")) { // kommt bei BIOONE und Inforama vor: id=doi:
        	co.setRfr_id(value);
        	continue;
        }
        if (key.equals("sid")) {
        	co.setRfr_id(value); // überschreibt &id=
        	continue;
        }
        if (key.equals("genre")) {
        	co.setRft_genre(value);
        	continue;
        }
    	if (key.equals("atitle")) {
    		co.setRft_atitle(value);
    		continue;
    	}
    	if (key.equals("btitle")) {
    		co.setRft_btitle(value);
    		continue;
    	}
    	if (key.equals("series")) {
    		co.setRft_series(value);
    		continue;
    	}
    	if (key.equals("pub")) {
    		co.setRft_pub(value);
    		continue;
    	}
    	if (key.equals("place")) {
    		co.setRft_place(value);
    		continue;
    	}
    	if (key.equals("edition")) {
    		co.setRft_edition(value);
    		continue;
    	}
        if (key.equals("title")) {
        	co.setRft_title(value);
        	continue;
        }
        if (key.equals("jtitle")) {
        	co.setRft_jtitle(value);
        	continue;
        }
        if (key.equals("stitle")) {
        	co.setRft_stitle(value);
        	continue;
        }
        if (key.equals("issn")) {
        	co.setRft_issn(value);
        	continue;
        }
        if (key.equals("issn1")) {
        	co.setRft_issn1(value); // kann es manchmal geben, als ISSN mit Bindestrich
        	continue;
        }
        if (key.equals("eissn")) {
        	co.setRft_eissn(value);
        	continue;
        }
        if (key.equals("eissn1")) {
        	co.setRft_eissn1(value);
        	continue;
        }
        if (key.equals("isbn")) {
        	co.setRft_isbn(value);
        	continue;
        }
        if (key.equals("date")) {
        	co.setRft_date(value);
        	continue;
        }
        if (key.equals("volume")) {
        	co.setRft_volume(value);
        	continue;
        }
        if (key.equals("part")) {
        	co.setRft_part(value);
        	continue;
        }
        if (key.equals("issue")) {
        	co.setRft_issue(value);
        	continue;
        }
        if (key.equals("spage")) {
        	co.setRft_spage(value);
        	continue;
        }
        if (key.equals("epage")) {
        	co.setRft_epage(value);
        	continue;
        }
        if (key.equals("pages")) {
        	co.setRft_pages(value);
        	continue;
        }
        if (key.equals("tpages")) {
        	co.setRft_tpages(value);
        	continue;
        }
        if (key.equals("author")) {
        	co.setRft_author(value);
        	continue;
        }
        if (key.equals("au")) {
        	co.setRft_au(value);
        	continue;
        }
        if (key.equals("aucorp")) {
        	co.setRft_aucorp(value);
        	continue;
        }
        if (key.equals("auinit")) {
        	co.setRft_auinit(value);
        	continue;
        }
        if (key.equals("auinit1")) {
        	co.setRft_auinit1(value);
        	continue;
        }
        if (key.equals("auinitm")) {
        	co.setRft_auinitm(value);
        	continue;
        }
        if (key.equals("ausuffix")) {
        	co.setRft_ausuffix(value);
        	continue;
        }
        if (key.equals("aulast")) {
        	co.setRft_aulast(value);
        	continue;
        }
        if (key.equals("aufirst")) {
        	co.setRft_aufirst(value);
        	continue;
        }
        if (key.equals("sici")) {
        	co.setRft_sici(value); // doppelt?
        	continue;
        }
        if (key.equals("bici")) {
        	co.setRft_bici(value);
        	continue;
        }
        if (key.equals("coden")) {
        	co.setRft_coden(value);
        	continue;
        }
        if (key.equals("artnum")) {
        	co.setRft_artnum(value);
        	continue;
        }
        // RFTs Dublin Core, z.B. in Blogs:
        if (key.equals("creator")) {
        	co.setRft_creator(value);
        	continue;
        }
        if (key.equals("publisher")) {
        	co.setRft_publisher(value);
        	continue;
        }
        if (key.equals("type")) {
        	co.setRft_type(value);
        	continue;
        }
        if (key.equals("subject")) {
        	co.setRft_subject(value);
        	continue;
        }
        if (key.equals("format")) {
        	co.setRft_format(value);
        	continue;
        }
        if (key.equals("language")) {
        	co.setRft_language(value);
        	continue;
        }
        if (key.equals("source")) {
        	co.setRft_source(value);
        	continue;
        }
        if (key.equals("identifier")) {
        	co.setRft_identifier(value);
        	continue;
        }
        if (key.equals("description")) {
        	co.setRft_description(value);
        	continue;
        }
        if (key.equals("relation")) {
        	co.setRft_relation(value);
        	continue;
        }
        if (key.equals("coverage")) {
        	co.setRft_coverage(value);
        	continue;
        }
        if (key.equals("rights")) {
        	co.setRft_rights(value);
        	continue;
        }
    		
        	
        // hier muss aus Kompatibilitätsgründen das jeweilige URI-Schema hinzugefügt werden
        // http://www.info-uri.info/registry/
        if (key.equals("rft_id")) {
        	uri_schemas.add(value);
        	continue;
        }
        if (key.equals("pmid")) {
        	if (value.length()>0) uri_schemas.add("info:pmid/" + value);
        	continue;
        }
        if (key.equals("id") && value.startsWith("doi:")) { // kommt bei BIOONE und Inforama vor
        	if (value.length()>9) uri_schemas.add("info:doi/" + value.substring(value.indexOf("doi:")+4));
        	continue;
        }
        if (key.equals("doi")) {
        	if (value.length()>9) uri_schemas.add("info:doi/" + value);
        	continue;
        }
        if (key.equals("sici")) {
        	if (value.length()>10) uri_schemas.add("info:sici/" + value);
        	continue;
        }
        if (key.equals("lccn")) {
        	if (value.length()>0) uri_schemas.add("info:lccn/" + value);
        	continue;
        }
	    
	    } catch(Exception e){
	    	log.error("readOpenUrlFromRequest: " + key + "\040value:\040" + value + "\040" + e.toString());
    	}
	    
		}
		
		if (uri_schemas.size()>0) co.setRft_id(uri_schemas);
		
        return co;
    }

	

	/**
	 * identifiziert die OpenURL-Identifiers aus einer Webseite (z.B. OCoins / Worlcat etc.)
	 * verwendet selbstgefertigte Stringanalysen
	 * 
	 */
	public ContextObject readOpenUrlFromString(String content) {
		
		ContextObject co = new ContextObject();
	    	
	    try {
	    	
	    	String OpenURL = content;
	    	
	    	SpecialCharacters specialCharacters = new SpecialCharacters();
	    	OpenURL = specialCharacters.replace(OpenURL); // Entfernen von &amp; und co.

	    	//Achtung: OpenURL nicht vor if (OpenURL...contains(">") decodieren, sonst interferiert rtf.sici (enthält <...>)!
	    // Grosschreibung
    	if (OpenURL.contains("ver=Z39.88-2004") && OpenURL.substring(OpenURL.indexOf("ver=Z39.88-2004")).contains(">")) { // falls content von einer Webseite stammt...
    		OpenURL = OpenURL.substring(OpenURL.indexOf("ver=Z39.88-2004"), OpenURL.indexOf(">", OpenURL.indexOf("ver=Z39.88-2004"))); // ...String mit OpenUrl suchen und verkürzen
    	}
    	// Kleinschreibung
    	if (OpenURL.contains("ver=z39.88-2004") && OpenURL.substring(OpenURL.indexOf("ver=z39.88-2004")).contains(">")) { // falls content von einer Webseite stammt...
    		OpenURL = OpenURL.substring(OpenURL.indexOf("ver=z39.88-2004"), OpenURL.indexOf(">", OpenURL.indexOf("ver=z39.88-2004"))); // ...String mit OpenUrl suchen und verkürzen
    	}
    	
    	if (OpenURL.contains("&rft.")) { // OpenURL Version 1.0 (neben rft.genre etc. gibt es dasselbe nochmals mit rfr.genre)
    	
    	if (OpenURL.contains("rft_val_fmt=")) co.setRft_val_fmt(getOpenUrlIdentifiersVersion1_0("rft_val_fmt=", OpenURL));
    	if (OpenURL.contains("rfr_id=")) co.setRfr_id(getOpenUrlIdentifiersVersion1_0("rfr_id=", OpenURL));
    	if (OpenURL.contains("rft.genre=")) co.setRft_genre(getOpenUrlIdentifiersVersion1_0("rft.genre=", OpenURL));
		if (OpenURL.contains("rft.atitle=")) co.setRft_atitle(getOpenUrlIdentifiersVersion1_0("rft.atitle=", OpenURL));
		if (OpenURL.contains("rft.btitle=")) co.setRft_btitle(getOpenUrlIdentifiersVersion1_0("rft.btitle=", OpenURL));
		if (OpenURL.contains("rft.series=")) co.setRft_series(getOpenUrlIdentifiersVersion1_0("rft.series=", OpenURL));
		if (OpenURL.contains("rft.pub=")) co.setRft_pub(getOpenUrlIdentifiersVersion1_0("rft.pub=", OpenURL));
		if (OpenURL.contains("rft.place=")) co.setRft_place(getOpenUrlIdentifiersVersion1_0("rft.place=", OpenURL));
		if (OpenURL.contains("rft.edition=")) co.setRft_edition(getOpenUrlIdentifiersVersion1_0("rft.edition=", OpenURL));
    	if (OpenURL.contains("rft.title=")) co.setRft_title(getOpenUrlIdentifiersVersion1_0("rft.title=", OpenURL));
    	if (OpenURL.contains("rft.jtitle=")) co.setRft_jtitle(getOpenUrlIdentifiersVersion1_0("rft.jtitle=", OpenURL));
    	if (OpenURL.contains("rft.stitle=")) co.setRft_stitle(getOpenUrlIdentifiersVersion1_0("rft.stitle=", OpenURL));
    	if (OpenURL.contains("rft.issn=")) co.setRft_issn(getOpenUrlIdentifiersVersion1_0("rft.issn=", OpenURL));
    	if (OpenURL.contains("rft.issn1=")) co.setRft_issn1(getOpenUrlIdentifiersVersion1_0("rft.issn1=", OpenURL));
    	if (OpenURL.contains("rft.eissn=")) co.setRft_eissn(getOpenUrlIdentifiersVersion1_0("rft.eissn=", OpenURL));
    	if (OpenURL.contains("rft.eissn1=")) co.setRft_eissn1(getOpenUrlIdentifiersVersion1_0("rft.eissn1=", OpenURL));
    	if (OpenURL.contains("rft.isbn=")) co.setRft_isbn(getOpenUrlIdentifiersVersion1_0("rft.isbn=", OpenURL));
    	if (OpenURL.contains("rft.date=")) co.setRft_date(getOpenUrlIdentifiersVersion1_0("rft.date=", OpenURL));
    	if (OpenURL.contains("rft.volume=")) co.setRft_volume(getOpenUrlIdentifiersVersion1_0("rft.volume=", OpenURL));
    	if (OpenURL.contains("rft.part=")) co.setRft_part(getOpenUrlIdentifiersVersion1_0("rft.part=", OpenURL));
    	if (OpenURL.contains("rft.issue=")) co.setRft_issue(getOpenUrlIdentifiersVersion1_0("rft.issue=", OpenURL));
    	if (OpenURL.contains("rft.spage=")) co.setRft_spage(getOpenUrlIdentifiersVersion1_0("rft.spage=", OpenURL));
    	if (OpenURL.contains("rft.epage=")) co.setRft_epage(getOpenUrlIdentifiersVersion1_0("rft.epage=", OpenURL));
    	if (OpenURL.contains("rft.pages=")) co.setRft_pages(getOpenUrlIdentifiersVersion1_0("rft.pages=", OpenURL));
    	if (OpenURL.contains("rft.tpages=")) co.setRft_tpages(getOpenUrlIdentifiersVersion1_0("rft.tpages=", OpenURL));
    	if (OpenURL.contains("rft.author=")) co.setRft_author(getOpenUrlIdentifiersVersion1_0("rft.author=", OpenURL));
    	if (OpenURL.contains("rft.au=")) co.setRft_au(getOpenUrlIdentifiersVersion1_0("rft.au=", OpenURL));
    	if (OpenURL.contains("rft.aucorp=")) co.setRft_aucorp(getOpenUrlIdentifiersVersion1_0("rft.aucorp=", OpenURL));
    	if (OpenURL.contains("rft.auinit=")) co.setRft_auinit(getOpenUrlIdentifiersVersion1_0("rft.auinit=", OpenURL));
    	if (OpenURL.contains("rft.auinit1=")) co.setRft_auinit1(getOpenUrlIdentifiersVersion1_0("rft.auinit1=", OpenURL));
    	if (OpenURL.contains("rft.auinitm=")) co.setRft_auinitm(getOpenUrlIdentifiersVersion1_0("rft.auinitm=", OpenURL));
    	if (OpenURL.contains("rft.ausuffix=")) co.setRft_ausuffix(getOpenUrlIdentifiersVersion1_0("rft.ausuffix=", OpenURL));
    	if (OpenURL.contains("rft.aulast=")) co.setRft_aulast(getOpenUrlIdentifiersVersion1_0("rft.aulast=", OpenURL));
    	if (OpenURL.contains("rft.aufirst=")) co.setRft_aufirst(getOpenUrlIdentifiersVersion1_0("rft.aufirst=", OpenURL));
    	if (OpenURL.contains("rft.sici=")) co.setRft_sici(getOpenUrlIdentifiersVersion1_0("rft.sici=", OpenURL));
    	if (OpenURL.contains("rft.bici=")) co.setRft_bici(getOpenUrlIdentifiersVersion1_0("rft.bici=", OpenURL));
    	if (OpenURL.contains("rft.coden=")) co.setRft_coden(getOpenUrlIdentifiersVersion1_0("rft.coden=", OpenURL));
    	if (OpenURL.contains("rft.artnum=")) co.setRft_artnum(getOpenUrlIdentifiersVersion1_0("rft.artnum=", OpenURL));
    	// RFTs Dublin Core, z.B. in Blogs:
    	if (OpenURL.contains("rft.creator=")) co.setRft_creator(getOpenUrlIdentifiersVersion1_0("rft.creator=", OpenURL));
    	if (OpenURL.contains("rft.publisher=")) co.setRft_publisher(getOpenUrlIdentifiersVersion1_0("rft.publisher=", OpenURL));
    	if (OpenURL.contains("rft.type=")) co.setRft_type(getOpenUrlIdentifiersVersion1_0("rft.type=", OpenURL));
    	if (OpenURL.contains("rft.subject=")) co.setRft_subject(getOpenUrlIdentifiersVersion1_0("rft.subject=", OpenURL));
    	if (OpenURL.contains("rft.format=")) co.setRft_format(getOpenUrlIdentifiersVersion1_0("rft.format=", OpenURL));
    	if (OpenURL.contains("rft.language=")) co.setRft_language(getOpenUrlIdentifiersVersion1_0("rft.language=", OpenURL));
    	if (OpenURL.contains("rft.source=")) co.setRft_source(getOpenUrlIdentifiersVersion1_0("rft.source=", OpenURL));
    	if (OpenURL.contains("rft.identifier=")) co.setRft_identifier(getOpenUrlIdentifiersVersion1_0("rft.identifier=", OpenURL));
    	if (OpenURL.contains("rft.description=")) co.setRft_description(getOpenUrlIdentifiersVersion1_0("rft.description=", OpenURL));
    	if (OpenURL.contains("rft.relation=")) co.setRft_relation(getOpenUrlIdentifiersVersion1_0("rft.relation=", OpenURL));
    	if (OpenURL.contains("rft.coverage=")) co.setRft_coverage(getOpenUrlIdentifiersVersion1_0("rft.coverage=", OpenURL));
    	if (OpenURL.contains("rft.rights=")) co.setRft_rights(getOpenUrlIdentifiersVersion1_0("rft.rights=", OpenURL));
    	
    	ArrayList<String> id = new ArrayList<String>();
    	
    	while (OpenURL.contains("rft_id=")) { // kann mehrere rft_id enthalten
    		id.add(getOpenUrlIdentifiersVersion1_0("rft_id=", OpenURL));
    		OpenURL = OpenURL.substring(OpenURL.indexOf("rft_id=")+6);
    	}
    	
    	if (id.size()>0) co.setRft_id(id);
    	
    	} else {
    		
//    		 OpenURL Version 0.1
        	if (OpenURL.contains("rft_val_fmt=")) co.setRft_val_fmt(getOpenUrlIdentifiersVersion0_1("rft_val_fmt=", OpenURL)); // kommt nicht vor in OpenURL != Version 1.0
        	if (OpenURL.contains("&id=")) co.setRfr_id(getOpenUrlIdentifiersVersion0_1("&id=", OpenURL)); // mit &! sonst nicht eindeutig! etwas fragwürdig, ob das zuverlässig ist
        	if (OpenURL.contains("sid=")) co.setRfr_id(getOpenUrlIdentifiersVersion0_1("sid=", OpenURL)); // überschreibt &id=
        	if (OpenURL.contains("genre=")) co.setRft_genre(getOpenUrlIdentifiersVersion0_1("genre=", OpenURL));
    		if (OpenURL.contains("atitle=")) co.setRft_atitle(getOpenUrlIdentifiersVersion0_1("atitle=", OpenURL));
    		if (OpenURL.contains("btitle=")) co.setRft_btitle(getOpenUrlIdentifiersVersion0_1("btitle=", OpenURL));
    		if (OpenURL.contains("series=")) co.setRft_series(getOpenUrlIdentifiersVersion0_1("series=", OpenURL));
    		if (OpenURL.contains("pub=")) co.setRft_pub(getOpenUrlIdentifiersVersion0_1("pub=", OpenURL));
    		if (OpenURL.contains("place=")) co.setRft_place(getOpenUrlIdentifiersVersion0_1("place=", OpenURL));
    		if (OpenURL.contains("edition=")) co.setRft_edition(getOpenUrlIdentifiersVersion0_1("edition=", OpenURL));
        	if (OpenURL.contains("&title=")) co.setRft_title(getOpenUrlIdentifiersVersion0_1("&title=", OpenURL)); // mit &! sonst nicht eindeutig! etwas fragwürdig, ob das zuverlässig ist
        	if (OpenURL.contains("jtitle=")) co.setRft_jtitle(getOpenUrlIdentifiersVersion0_1("jtitle=", OpenURL));
        	if (OpenURL.contains("stitle=")) co.setRft_stitle(getOpenUrlIdentifiersVersion0_1("stitle=", OpenURL));
        	if (OpenURL.contains("issn=")) co.setRft_issn(getOpenUrlIdentifiersVersion0_1("issn=", OpenURL));
        	if (OpenURL.contains("issn1=")) co.setRft_issn1(getOpenUrlIdentifiersVersion0_1("issn1=", OpenURL)); // kann es manchmal geben, als ISSN mit Bindestrich
        	if (OpenURL.contains("eissn=")) co.setRft_eissn(getOpenUrlIdentifiersVersion0_1("eissn=", OpenURL));
        	if (OpenURL.contains("eissn1=")) co.setRft_eissn1(getOpenUrlIdentifiersVersion0_1("eissn1=", OpenURL));
        	if (OpenURL.contains("isbn=")) co.setRft_isbn(getOpenUrlIdentifiersVersion0_1("isbn=", OpenURL));
        	if (OpenURL.contains("date=")) co.setRft_date(getOpenUrlIdentifiersVersion0_1("date=", OpenURL));
        	if (OpenURL.contains("volume=")) co.setRft_volume(getOpenUrlIdentifiersVersion0_1("volume=", OpenURL));
        	if (OpenURL.contains("part=")) co.setRft_part(getOpenUrlIdentifiersVersion0_1("part=", OpenURL));
        	if (OpenURL.contains("issue=")) co.setRft_issue(getOpenUrlIdentifiersVersion0_1("issue=", OpenURL));
        	if (OpenURL.contains("spage=")) co.setRft_spage(getOpenUrlIdentifiersVersion0_1("spage=", OpenURL));
        	if (OpenURL.contains("epage=")) co.setRft_epage(getOpenUrlIdentifiersVersion0_1("epage=", OpenURL));
        	if (OpenURL.contains("pages=")) co.setRft_pages(getOpenUrlIdentifiersVersion0_1("pages=", OpenURL));
        	if (OpenURL.contains("tpages=")) co.setRft_tpages(getOpenUrlIdentifiersVersion0_1("tpages=", OpenURL));
        	if (OpenURL.contains("author=")) co.setRft_author(getOpenUrlIdentifiersVersion0_1("author=", OpenURL));
        	if (OpenURL.contains("&au=")) co.setRft_au(getOpenUrlIdentifiersVersion0_1("&au=", OpenURL)); // mit &, sonst nicht eindeutig!
        	if (OpenURL.contains("aucorp=")) co.setRft_aucorp(getOpenUrlIdentifiersVersion0_1("aucorp=", OpenURL));
        	if (OpenURL.contains("auinit=")) co.setRft_auinit(getOpenUrlIdentifiersVersion0_1("auinit=", OpenURL));
        	if (OpenURL.contains("auinit1=")) co.setRft_auinit1(getOpenUrlIdentifiersVersion0_1("auinit1=", OpenURL));
        	if (OpenURL.contains("auinitm=")) co.setRft_auinitm(getOpenUrlIdentifiersVersion0_1("auinitm=", OpenURL));
        	if (OpenURL.contains("ausuffix=")) co.setRft_ausuffix(getOpenUrlIdentifiersVersion0_1("ausuffix=", OpenURL));
        	if (OpenURL.contains("aulast=")) co.setRft_aulast(getOpenUrlIdentifiersVersion0_1("aulast=", OpenURL));
        	if (OpenURL.contains("aufirst=")) co.setRft_aufirst(getOpenUrlIdentifiersVersion0_1("aufirst=", OpenURL));
        	if (OpenURL.contains("sici=")) co.setRft_sici(getOpenUrlIdentifiersVersion0_1("sici=", OpenURL)); // doppelt?
        	if (OpenURL.contains("bici=")) co.setRft_bici(getOpenUrlIdentifiersVersion0_1("bici=", OpenURL));
        	if (OpenURL.contains("coden=")) co.setRft_coden(getOpenUrlIdentifiersVersion0_1("coden=", OpenURL));
        	if (OpenURL.contains("artnum=")) co.setRft_artnum(getOpenUrlIdentifiersVersion0_1("artnum=", OpenURL));
        	// RFTs Dublin Core, z.B. in Blogs:
        	if (OpenURL.contains("creator=")) co.setRft_creator(getOpenUrlIdentifiersVersion0_1("creator=", OpenURL));
        	if (OpenURL.contains("publisher=")) co.setRft_publisher(getOpenUrlIdentifiersVersion0_1("publisher=", OpenURL));
        	if (OpenURL.contains("type=")) co.setRft_type(getOpenUrlIdentifiersVersion0_1("type=", OpenURL));
        	if (OpenURL.contains("subject=")) co.setRft_subject(getOpenUrlIdentifiersVersion0_1("subject=", OpenURL));
        	if (OpenURL.contains("format=")) co.setRft_format(getOpenUrlIdentifiersVersion0_1("format=", OpenURL));
        	if (OpenURL.contains("language=")) co.setRft_language(getOpenUrlIdentifiersVersion0_1("language=", OpenURL));
        	if (OpenURL.contains("source=")) co.setRft_source(getOpenUrlIdentifiersVersion0_1("source=", OpenURL));
        	if (OpenURL.contains("identifier=")) co.setRft_identifier(getOpenUrlIdentifiersVersion0_1("identifier=", OpenURL));
        	if (OpenURL.contains("description=")) co.setRft_description(getOpenUrlIdentifiersVersion0_1("description=", OpenURL));
        	if (OpenURL.contains("relation=")) co.setRft_relation(getOpenUrlIdentifiersVersion0_1("relation=", OpenURL));
        	if (OpenURL.contains("coverage=")) co.setRft_coverage(getOpenUrlIdentifiersVersion0_1("coverage=", OpenURL));
        	if (OpenURL.contains("rights=")) co.setRft_rights(getOpenUrlIdentifiersVersion0_1("rights=", OpenURL));
    		
        	ArrayList<String> id = new ArrayList<String>();
        	// hier muss aus Kompatibilitätsgründen das jeweilige URI-Schema hinzugefügt werden
        	// http://www.info-uri.info/registry/
        	if (OpenURL.contains("pmid:")) {
        		String reg = "info:pmid/" + getOpenUrlIdentifiersVersion0_1("pmid:", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	if (OpenURL.contains("doi:")) {
        		String reg = "info:doi/" + getOpenUrlIdentifiersVersion0_1("doi:", OpenURL);
        		if (reg.length()>9) id.add(reg);
        	}
        	if (OpenURL.contains("sici:")) {
        		String reg = "info:sici/" + getOpenUrlIdentifiersVersion0_1("sici:", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	if (OpenURL.contains("lccn:")) {
        		String reg = "info:lccn/" + getOpenUrlIdentifiersVersion0_1("lccn:", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	if (OpenURL.contains("pmid=")) {
        		String reg = "info:pmid/" + getOpenUrlIdentifiersVersion0_1("pmid=", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	if (OpenURL.contains("doi=")) {
        		String reg = "info:doi/" + getOpenUrlIdentifiersVersion0_1("doi=", OpenURL);
        		if (reg.length()>9) id.add(reg);
        	}
        	if (OpenURL.contains("sici=")) {
        		String reg = "info:sici/" + getOpenUrlIdentifiersVersion0_1("sici=", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	if (OpenURL.contains("lccn=")) {
        		String reg = "info:lccn/" + getOpenUrlIdentifiersVersion0_1("lccn=", OpenURL);
        		if (reg.length()>10) id.add(reg);
        	}
        	
        	if (id.size()>0) co.setRft_id(id);
    	
    	
    	}
	    	
	    
	    } catch(Exception e){
	    	log.error("readOpenUrl: " + "\012" + content);    		
    	}
		
        return co;
    }
	
	/**
	 * Stellt aus einem ContextObject den entsprechenden OpenURL-String zusammen
	 */
	public String composeOpenUrl(ContextObject co) {
   	    
	    StringBuffer OpenURL = new StringBuffer();
    	
    	// hier werden die Identifiers gesetzt
	    if (co.getRft_val_fmt()!=null && !co.getRft_val_fmt().equals("")) OpenURL.append("&rft_val_fmt=" + co.getRft_val_fmt());
	    if (co.getRfr_id()!=null && !co.getRfr_id().equals("")) OpenURL.append("&rfr_id=" + co.getRfr_id());
	    if (co.getRft_genre()!=null && !co.getRft_genre().equals("")) OpenURL.append("&rft.genre=" + co.getRft_genre());
	    if (co.getRft_atitle()!=null && !co.getRft_atitle().equals("")) OpenURL.append("&rft.atitle=" + co.getRft_atitle());
	    if (co.getRft_btitle()!=null && !co.getRft_btitle().equals("")) OpenURL.append("&rft.btitle=" + co.getRft_btitle());
	    if (co.getRft_series()!=null && !co.getRft_series().equals("")) OpenURL.append("&rft.series=" + co.getRft_series());
	    if (co.getRft_pub()!=null && !co.getRft_pub().equals("")) OpenURL.append("&rft.pub=" + co.getRft_pub());
	    if (co.getRft_place()!=null && !co.getRft_place().equals("")) OpenURL.append("&rft.place=" + co.getRft_place());
	    if (co.getRft_edition()!=null && !co.getRft_edition().equals("")) OpenURL.append("&rft.edition=" + co.getRft_edition());
	    if (co.getRft_title()!=null && !co.getRft_title().equals("")) OpenURL.append("&rft.title=" + co.getRft_title());
	    if (co.getRft_jtitle()!=null && !co.getRft_jtitle().equals("")) OpenURL.append("&rft.jtitle=" + co.getRft_jtitle());
	    if (co.getRft_stitle()!=null && !co.getRft_stitle().equals("")) OpenURL.append("&rft.stitle=" + co.getRft_stitle());
	    if (co.getRft_issn()!=null && !co.getRft_issn().equals("")) OpenURL.append("&rft.issn=" + co.getRft_issn());
	    if (co.getRft_eissn()!=null && !co.getRft_eissn().equals("")) OpenURL.append("&rft.eissn=" + co.getRft_eissn());
	    if (co.getRft_isbn()!=null && !co.getRft_isbn().equals("")) OpenURL.append("&rft.isbn=" + co.getRft_isbn());
	    if (co.getRft_date()!=null && !co.getRft_date().equals("")) OpenURL.append("&rft.date=" + co.getRft_date());
	    if (co.getRft_volume()!=null && !co.getRft_volume().equals("")) OpenURL.append("&rft.volume=" + co.getRft_volume());
	    if (co.getRft_part()!=null && !co.getRft_part().equals("")) OpenURL.append("&rft.part=" + co.getRft_part());
	    if (co.getRft_issue()!=null && !co.getRft_issue().equals("")) OpenURL.append("&rft.issue=" + co.getRft_issue());
	    if (co.getRft_spage()!=null && !co.getRft_spage().equals("")) OpenURL.append("&rft.spage=" + co.getRft_spage());
	    if (co.getRft_epage()!=null && !co.getRft_epage().equals("")) OpenURL.append("&rft.epage=" + co.getRft_epage());
	    if (co.getRft_pages()!=null && !co.getRft_pages().equals("")) OpenURL.append("&rft.pages=" + co.getRft_pages());
	    if (co.getRft_tpages()!=null && !co.getRft_tpages().equals("")) OpenURL.append("&rft.tpages=" + co.getRft_tpages());
	    if (co.getRft_author()!=null && !co.getRft_author().equals("")) OpenURL.append("&rft.author=" + co.getRft_author());
	    if (co.getRft_au()!=null && !co.getRft_au().equals("")) OpenURL.append("&rft.au=" + co.getRft_au());
	    if (co.getRft_aucorp()!=null && !co.getRft_aucorp().equals("")) OpenURL.append("&rft.aucorp=" + co.getRft_aucorp());
	    if (co.getRft_auinit()!=null && !co.getRft_auinit().equals("")) OpenURL.append("&rft.auinit=" + co.getRft_auinit());
	    if (co.getRft_auinit1()!=null && !co.getRft_auinit1().equals("")) OpenURL.append("&rft.auinit1=" + co.getRft_auinit1());
	    if (co.getRft_auinitm()!=null && !co.getRft_auinitm().equals("")) OpenURL.append("&rft.auinitm=" + co.getRft_auinitm());
	    if (co.getRft_ausuffix()!=null && !co.getRft_ausuffix().equals("")) OpenURL.append("&rft.ausuffix=" + co.getRft_ausuffix());
	    if (co.getRft_aulast()!=null && !co.getRft_aulast().equals("")) OpenURL.append("&rft.aulast=" + co.getRft_aulast());
	    if (co.getRft_aufirst()!=null && !co.getRft_aufirst().equals("")) OpenURL.append("&rft.aufirst=" + co.getRft_aufirst());
	    if (co.getRft_sici()!=null && !co.getRft_sici().equals("")) OpenURL.append("&rft.sici=" + co.getRft_sici());
	    if (co.getRft_bici()!=null && !co.getRft_bici().equals("")) OpenURL.append("&rft.bici=" + co.getRft_bici());
	    if (co.getRft_coden()!=null && !co.getRft_coden().equals("")) OpenURL.append("&rft.coden=" + co.getRft_coden());
	    if (co.getRft_artnum()!=null && !co.getRft_artnum().equals("")) OpenURL.append("&rft.artnum=" + co.getRft_artnum());

    	// RFTs Dublin Core, z.B. in Blogs:
	    if (co.getRft_creator()!=null && !co.getRft_creator().equals("")) OpenURL.append("&rft.creator=" + co.getRft_creator());
	    if (co.getRft_publisher()!=null && !co.getRft_publisher().equals("")) OpenURL.append("&rft.publisher=" + co.getRft_publisher());
	    if (co.getRft_type()!=null && !co.getRft_type().equals("")) OpenURL.append("&rft.type=" + co.getRft_type());
	    if (co.getRft_subject()!=null && !co.getRft_subject().equals("")) OpenURL.append("&rft.subject=" + co.getRft_subject());
	    if (co.getRft_format()!=null && !co.getRft_format().equals("")) OpenURL.append("&rft.format=" + co.getRft_format());
	    if (co.getRft_language()!=null && !co.getRft_language().equals("")) OpenURL.append("&rft.language=" + co.getRft_language());
	    if (co.getRft_source()!=null && !co.getRft_source().equals("")) OpenURL.append("&rft.source=" + co.getRft_source());
	    if (co.getRft_identifier()!=null && !co.getRft_identifier().equals("")) OpenURL.append("&rft.identifier=" + co.getRft_identifier());
	    if (co.getRft_description()!=null && !co.getRft_description().equals("")) OpenURL.append("&rft.description=" + co.getRft_description());
	    if (co.getRft_relation()!=null && !co.getRft_relation().equals("")) OpenURL.append("&rft.relation=" + co.getRft_relation());
	    if (co.getRft_coverage()!=null && !co.getRft_coverage().equals("")) OpenURL.append("&rft.coverage=" + co.getRft_coverage());
	    if (co.getRft_rights()!=null && !co.getRft_rights().equals("")) OpenURL.append("&rft.rights=" + co.getRft_rights());

    	if (co.getRft_id()!=null) {
    	for (int i=0;i<co.getRft_id().size();i++) { // kann mehrere rft_id enthalten
    		OpenURL.append("&rft_id=" + co.getRft_id().get(i).toString());
    	}
    	}
    	
		String output = OpenURL.toString();
		
		if (output==null) output = "";
		
        return output;
    }

	
	/**
	 * liest den Inhalt einer Pubmed-Anzeige im XML-Format und liefert ein ContextObject zurück
	 */
	public ContextObject readXmlPubmed(String content) {

		// http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=3966282
		
		ContextObject co = new ContextObject();
		SpecialCharacters specialCharacters = new SpecialCharacters();
		content = specialCharacters.replace(content); // Entfernen von &amp; und co.
		
		try { // notwendig, da nicht kontrolliert wird, ob XML valide ist!
		
		String endtag = "</Item>";
		
		if (content.contains("<Item Name=\"PubType\"")) {co.setRft_genre(getXmlTag("<Item Name=\"PubType\"", endtag, content));} // wird bei Umwandlung in OrderForm zu "Artikel", da Angaben nicht OpenURL kompatibel... (Journal Article, Randomized Controlled Trial etc.)
		if (content.contains("<Item Name=\"PubDate\"")) {co.setRft_date(getXmlTag("<Item Name=\"PubDate\"", endtag, content));}
		if (content.contains("<Item Name=\"EPubDate\"") && (co.getRft_date()==null || co.getRft_date().equals("")) ) {co.setRft_date(getXmlTag("<Item Name=\"EPubDate\"", endtag, content));} // nur falls Datum nicht schon gesetzt
		if (content.contains("<Item Name=\"FullJournalName\"")) {co.setRft_jtitle(getXmlTag("<Item Name=\"FullJournalName\"", endtag, content));} // unter der Annahme, dass es sich um einen Artikel handelt
		if (content.contains("<Item Name=\"Source\"")) {co.setRft_stitle(getXmlTag("<Item Name=\"Source\"", endtag, content));} // unter der Annahme, dass es sich um einen Artikel handelt
		if (content.contains("<Item Name=\"Author\"")) {co.setRft_author(getXmlTag("<Item Name=\"Author\"", endtag, content));} // nur der erste Author wird abgefüllt
		if (content.contains("<Item Name=\"CollectiveName\"")) {co.setRft_aucorp(getXmlTag("<Item Name=\"CollectiveName\"", endtag, content));}
		if (content.contains("<Item Name=\"Title\"")) {co.setRft_atitle(getXmlTag("<Item Name=\"Title\"", endtag, content));} // unter der Annahme, dass es sich um einen Artikel handelt
		if (content.contains("<Item Name=\"Volume\"")) {co.setRft_volume(getXmlTag("<Item Name=\"Volume\"", endtag, content));}
		if (content.contains("<Item Name=\"Issue\"")) {co.setRft_issue(getXmlTag("<Item Name=\"Issue\"", endtag, content));}
		if (content.contains("<Item Name=\"Pages\"")) {co.setRft_pages(getXmlTag("<Item Name=\"Pages\"", endtag, content));}
		if (content.contains("<Item Name=\"PubStatus\" Type=\"String\">aheadofprint") && (co.getRft_pages()==null || co.getRft_pages().equals("")) ) {co.setRft_pages(getXmlTag("<Item Name=\"PubStatus\"", endtag, content));}
		if (content.contains("<Item Name=\"ISSN\"")) {co.setRft_issn(getXmlTag("<Item Name=\"ISSN\"", endtag, content));}
		if (content.contains("<Item Name=\"ESSN\"") && (co.getRft_issn()==null || co.getRft_issn().equals("")) ) {co.setRft_eissn(getXmlTag("<Item Name=\"ESSN\"", endtag, content));} // nur abfüllen, falls ISSN nicht schon gesetzt
		if (content.contains("<Item Name=\"Lang\"")) {co.setRft_language(getXmlTag("<Item Name=\"Lang\"", endtag, content));}
		
    	ArrayList<String> id = new ArrayList<String>();  	
		
		if (content.contains("<Item Name=\"doi\"")) {id.add("info:doi/" + getXmlTag("<Item Name=\"doi\"", endtag, content));}
		if (content.contains("<Item Name=\"pubmed\"")) {id.add("info:pmid/" + getXmlTag("<Item Name=\"pubmed\"", endtag, content));}
		
		co.setRft_id(id);
		
		} catch(Exception e){
			log.error("readXmlPubmed/getXmlTag:\012" + content);    		
    	}
		
		return co;
	}
	
	/**
	 * holt aus dem Request alle OpenURL-Parameter
	 */
	public ConcurrentHashMap<String, String> getOpenUrlParameters(HttpServletRequest rq) {
		
		ConcurrentHashMap<String, String> hm = new ConcurrentHashMap<String, String>();
		

		@SuppressWarnings("unchecked")
		Map<String, String[]> paramMap = rq.getParameterMap();
		
		Iterator<Map.Entry<String, String[]>> x = paramMap.entrySet().iterator();

		while (x.hasNext()) {
			
		Map.Entry<String, String[]> pairs = (Map.Entry<String, String[]>)x.next();
			
		String key = pairs.getKey();
		String[] values = pairs.getValue();
		
		if (key.equals("rft_id")) { // rft_id enthält nocheinmal unterschiedliche Identifier nach dem info: Scheme
			// rft_id wird jeweils separat abgelegt und nicht aneinander gehängt
			for (int z=0;z<values.length;z++) {
				hm.put(key, getOpenUrlValue(values[z]));
			}		
		} else { // hier werden Mehrfachparameter (z.B. mehrere Autoren) aneinander gehängt
			StringBuffer buf = new StringBuffer();
			for (int z=0;z<values.length;z++) {
				if (z==0) {buf.append(values[z]);} else {buf.append("\040;\040" + values[z]);}
			}
			hm.put(key, getOpenUrlValue(buf.toString()));			
		}
		
		}
		
		return hm;
	}
	
	/**
	 * extrahiert aus dem content den Inhalt eines RFTs, RFRs oder RFEs (Standard-Version)
	 */
	private String getOpenUrlIdentifiersVersion1_0(String rft, String content) {
		
		String output = "";
		CodeUrl codeUrl = new CodeUrl();
		
		if (content.substring(content.indexOf(rft)).contains("&rf")) { // Delimiter ist das nächste &rft (Referent), &rfe (Referring Entity), &rfr (Referrer)
		output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("&rf", content.indexOf(rft)));
		} else {
			if (content.substring(content.indexOf(rft)).contains("\"")) { // Delimiter ist das " am Ende des URLs in HTML
				output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\"", content.indexOf(rft)));
			} else {
				if (content.substring(content.indexOf(rft)).contains("\040") || content.substring(content.indexOf(rft)).contains("\012")) { // // Delimiter ist Space oder Umbruch
					if (content.substring(content.indexOf(rft)).contains("\040")) {output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\040", content.indexOf(rft)));} else {output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\012", content.indexOf(rft)));};
				} else {
					output = content.substring(content.indexOf(rft)+rft.length()); // kein Delimiter. String bis ans Ende
				}
			}
		}
		
		output = codeUrl.decode(output); // RFTs sind URL-codiert
		
		return output;
	}
	
	/**
	 * extrahiert aus dem content den Inhalt die nicht standardisierten RFTs (ohne RFT-Bezeichnung)
	 */
	private String getOpenUrlIdentifiersVersion0_1(String rft, String content) {
		
		String output = "";
		CodeUrl codeUrl = new CodeUrl();
		
		if (content.substring(content.indexOf(rft)+1).contains("&")) { // Delimiter ist das nächste &
		output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("&", content.indexOf(rft)+1));
		} else {
			if (content.substring(content.indexOf(rft)).contains("\"")) { // Delimiter ist das " am Ende des URLs in HTML
				output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\"", content.indexOf(rft)));
			} else {
				if (content.substring(content.indexOf(rft)).contains("\040") || content.substring(content.indexOf(rft)).contains("\012")) { // // Delimiter ist Space oder Umbruch
					if (content.substring(content.indexOf(rft)).contains("\040")) {output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\040", content.indexOf(rft)));} else {output = content.substring(content.indexOf(rft)+rft.length(), content.indexOf("\012", content.indexOf(rft)));};
				} else {
					output = content.substring(content.indexOf(rft)+rft.length()); // kein Delimiter. String bis ans Ende
				}
			}
		}
		
		output = codeUrl.decode(output); // RFTs sind URL-codiert
		
		return output;
	}
	
	/**
	 * extrahiert aus dem XML-Content den Inhalt eines bestimmten Tags
	 */
	private String getXmlTag(String starttag, String endtag, String content) {
		
		String output = "";
		
		output = content.substring(content.indexOf(">", content.indexOf(starttag))+1, content.indexOf(endtag, content.indexOf(starttag)));
		
		return output;
	}
	
	private String getOpenUrlValue (String input) {
		
		try {
		
		if (decode(input)) input = Decoder.utf8Convert(input);
		
		} catch (Exception e) {
			log.error("private boolean decode (String input): " + e.toString());
		}
		
		return input;
	}
	
	/**
	 * OpenURL-request sometimes are UTF-8 encoded, sometimes ISO-8895-1
	 * Here will, check if we need to decode a string or not
	 */
	private boolean decode (String input) {
		
		boolean check = false;
		
		try {
		
		if (input.length()>Decoder.utf8Convert(input).length()) check = true;
		
		} catch (Exception e) {
			log.error("private boolean decode (String input): " + e.toString());
		}
		
		return check;
	}


}
