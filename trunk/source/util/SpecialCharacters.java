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

package util;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.grlea.log.SimpleLogger;

/**
 * Umgang Zeichencodierung
 * @author Markus Fischer
 *
 */
public class SpecialCharacters {
	
	private static final SimpleLogger log = new SimpleLogger(SpecialCharacters.class);
	
		/**
		 * Ersetzt Spezialzeichen, damit im Web unabsichtlich codierte Zeichen wieder "übersetzt" werden.
		 * Falls null kommt null zurück
		 * @param input
		 * @return input mit ersetzten Zeichen
		 */
	  public String replace(String input) {
			String output = input;
			
			if (input!=null) {
			
//			http://www.web-toolbox.net/webtoolbox/html/ascii-zeichen.htm
//			http://www.html-seminar.de/referenz-sonderzeichen.htm
//			http://www.htmlgoodies.com/beyond/reference/article.php/3472611
//			http://www.trans4mind.com/personal_development/HTMLGuide/specialCharacters2.htm
//			http://www.trans4mind.com/personal_development/HTMLGuide/specialcharacters3.htm
//			http://www.trans4mind.com/personal_development/HTMLGuide/specialcharacters8000.htm
				
			if (output.contains("&#x")){ // Hexadezimal-encodiert
				output = htmlHexToString(output);				
			}				
			if (output.contains("&#")){ // Dezimal-encodiert
				output = htmlDezToString(output);		
			}
			
			output = output.replaceAll("a%CC%88", "ä");
			output = output.replaceAll("o%CC%88", "ö");
			output = output.replaceAll("u%CC%88", "ü");
			output = output.replaceAll("A%CC%88", "Ä");
			output = output.replaceAll("O%CC%88", "Ö");
			output = output.replaceAll("U%CC%88", "Ü");			
			
			if (output.contains("&")){
			output = output.replaceAll("&scaron;", "š");
			output = output.replaceAll("&Scaron;", "Š");
			output = output.replaceAll("&oelig;", "œ");
			output = output.replaceAll("&Oelig;", "Œ");
			output = output.replaceAll("&lsquo;", "‘");
			output = output.replaceAll("&rsquo;", "’");
			output = output.replaceAll("&sbquo;", "‚");
			output = output.replaceAll("&ldquo;", "“");
			output = output.replaceAll("&rdquo;", "”");
			output = output.replaceAll("&bdquo;", "„");
			output = output.replaceAll("&circ;", "ˆ");
			output = output.replaceAll("&tilde;", "˜");
			output = output.replaceAll("&bull;", "•");
			output = output.replaceAll("&prime;", "´");
			output = output.replaceAll("&Prime;", "\"");
			output = output.replaceAll("&sim;", "~");
			output = output.replaceAll("&minus;", "–");
			output = output.replaceAll("&zwnj;", "|");
			output = output.replaceAll("&dagger;", "†");
			output = output.replaceAll("&Dagger;", "‡");
			output = output.replaceAll("&permil;", "‰");
			output = output.replaceAll("&lsaquo;", "‹");
			output = output.replaceAll("&rsaquo;", "›");
			output = output.replaceAll("&spades;", "♠");
			output = output.replaceAll("&clubs;", "♣");
			output = output.replaceAll("&hearts;", "♥");
			output = output.replaceAll("&diams;", "♦");
			output = output.replaceAll("&oline;", "‾");
			output = output.replaceAll("&larr;", "←");
			output = output.replaceAll("&rarr;", "→");
			output = output.replaceAll("&uarr;", "↑");
			output = output.replaceAll("&darr;", "↓");
			output = output.replaceAll("&harr;", "↔");
			output = output.replaceAll("&rArr;", "⇒");
			output = output.replaceAll("&hArr;", "⇔");
			output = output.replaceAll("&ang;", "∠");
			output = output.replaceAll("&perp;", "⊥");
			output = output.replaceAll("&part;", "∂");
			output = output.replaceAll("&prod;", "∏");
			output = output.replaceAll("&sum;", "∑");
			output = output.replaceAll("&int;", "∫");
			output = output.replaceAll("&prop;", "∝");
			output = output.replaceAll("&there4;", "∴");
			output = output.replaceAll("&loz;", "◊");
			output = output.replaceAll("&nabla;", "∇");
			output = output.replaceAll("&trade;", "™");
			output = output.replaceAll("&quot;", "\"");
			output = output.replaceAll("&amp;", "&");
			output = output.replaceAll("&apos;", "'");
			output = output.replaceAll("&frasl;", "⁄");
			output = output.replaceAll("&lt;", "<");
			output = output.replaceAll("&gt;", ">");
			output = output.replaceAll("&ndash;", "–");
			output = output.replaceAll("&mdash;", "—");
			output = output.replaceAll("&iexcl;", "¡");
			output = output.replaceAll("&cent;", "¢");
			output = output.replaceAll("&pound;", "£");
			output = output.replaceAll("&curren;", "¤");
			output = output.replaceAll("&yen;", "¥");
			output = output.replaceAll("&brvbar;", "¦");
			output = output.replaceAll("&brkbar;", "¦");
			output = output.replaceAll("&sect;", "§");
			output = output.replaceAll("&uml;", "¨");
			output = output.replaceAll("&auml;", "ä");
			output = output.replaceAll("&ouml;", "ö");
			output = output.replaceAll("&uuml;", "ü");
			output = output.replaceAll("&Auml;", "Ä");
			output = output.replaceAll("&Ouml;", "Ö");
			output = output.replaceAll("&Uuml;", "Ü");
			output = output.replaceAll("&die;", "¨");
			output = output.replaceAll("&copy;", "©");
			output = output.replaceAll("&ordf;", "ª");
			output = output.replaceAll("&laquo;", "«");
			output = output.replaceAll("&raquo;", "»");
			output = output.replaceAll("&not;", "¬");
			output = output.replaceAll("&shy;", ""); // weiches Trennzeichen
			output = output.replaceAll("&reg;", "®");
			output = output.replaceAll("&macr;", "¯");
			output = output.replaceAll("&hibar;", "¯");
			output = output.replaceAll("&deg;", "°");
			output = output.replaceAll("&plusmn;", "±");
			output = output.replaceAll("&sup1;", "¹");
			output = output.replaceAll("&sup2;", "²");
			output = output.replaceAll("&sup3;", "³");
			output = output.replaceAll("&acute;", "´");
			output = output.replaceAll("&micro;", "µ");
			output = output.replaceAll("&para;", "¶");
			output = output.replaceAll("&middot;", "·");
			output = output.replaceAll("&cedil;", "¸");
			output = output.replaceAll("&Ccedil;", "Ç");
			output = output.replaceAll("&ccedil;", "ç");
			output = output.replaceAll("&sup1;", "¹");
			output = output.replaceAll("&ordm;", "º");
			output = output.replaceAll("&frac14;", "¼");
			output = output.replaceAll("&frac12;", "½");
			output = output.replaceAll("&frac34;", "¾");
			output = output.replaceAll("&iquest;", "¿");
			output = output.replaceAll("&times;", "×");
			output = output.replaceAll("&Oslash;", "Ø");
			output = output.replaceAll("&szlig;", "&szlig;");
			output = output.replaceAll("&divide;", "÷");
			output = output.replaceAll("&oslash;", "ø");
			output = output.replaceAll("&nbsp;", " "); // Space
			output = output.replaceAll("&Ntilde;", "Ñ");
			output = output.replaceAll("&ntilde;", "ñ");
			output = output.replaceAll("&THORN;", "Þ");
			output = output.replaceAll("&thorn;", "þ");
			output = output.replaceAll("&Yacute;", "Ý");
			output = output.replaceAll("&yacute;", "ý");
			output = output.replaceAll("&yuml;", "ÿ");
			output = output.replaceAll("&Yuml;", "Ÿ");
			output = output.replaceAll("&AElig;", "Æ");
			output = output.replaceAll("&Aacute;", "Á");
			output = output.replaceAll("&Acirc;", "Â");
			output = output.replaceAll("&Agrave;", "À");
			output = output.replaceAll("&Aring;", "Å");
			output = output.replaceAll("&Atilde;", "Ã");
			output = output.replaceAll("&aelig;", "æ");
			output = output.replaceAll("&aacute;", "á");
			output = output.replaceAll("&acirc;", "â");
			output = output.replaceAll("&agrave;", "à");
			output = output.replaceAll("&aring;", "å");
			output = output.replaceAll("&atilde;", "ã");
			output = output.replaceAll("&ETH;", "Ð");
			output = output.replaceAll("&Eacute;", "É");
			output = output.replaceAll("&Ecirc;", "Ê");
			output = output.replaceAll("&Egrave;", "È");
			output = output.replaceAll("&Euml;", "Ë");
			output = output.replaceAll("&eth;", "ð");
			output = output.replaceAll("&eacute;", "é");
			output = output.replaceAll("&ecirc;", "ê");
			output = output.replaceAll("&egrave;", "è");
			output = output.replaceAll("&euml;", "ë");
			output = output.replaceAll("&Iacute;", "Í");
			output = output.replaceAll("&Icirc;", "Î");
			output = output.replaceAll("&Igrave;", "Ì");
			output = output.replaceAll("&Iuml;", "Ï");
			output = output.replaceAll("&iacute;", "í");
			output = output.replaceAll("&icirc;", "î");
			output = output.replaceAll("&igrave;", "ì");
			output = output.replaceAll("&iuml;", "ï");
			output = output.replaceAll("&Oacute;", "Ó");
			output = output.replaceAll("&Ocirc;", "Ô");
			output = output.replaceAll("&Ograve;", "Ò");
			output = output.replaceAll("&Otilde;", "Õ");
			output = output.replaceAll("&oacute;", "ó");
			output = output.replaceAll("&ocirc;", "ô");
			output = output.replaceAll("&ograve;", "ò");
			output = output.replaceAll("&otilde;", "õ");
			output = output.replaceAll("&Uacute;", "Ú");
			output = output.replaceAll("&Ucirc;", "Û");
			output = output.replaceAll("&Ugrave;", "Ù");
			output = output.replaceAll("&uacute;", "ú");
			output = output.replaceAll("&ucirc;", "û");
			output = output.replaceAll("&ugrave;", "ù");
//			output = output.replaceAll("&hellip;", "…");
			output = output.replaceAll("&hellip;", "...");
			output = output.replaceAll("&pi;", "π");
			output = output.replaceAll("&radic;", "√");
			output = output.replaceAll("&ge;", "≥");
			output = output.replaceAll("&le;", "≤");
			output = output.replaceAll("&ne;", "≠");
			output = output.replaceAll("&equiv;", "≡");
			output = output.replaceAll("&asymp;", "≈");
			output = output.replaceAll("&and;", "∧");
			output = output.replaceAll("&or;", "∨");
			output = output.replaceAll("&cap;", "∩");
			output = output.replaceAll("&cup;", "∪");
			output = output.replaceAll("&sub;", "⊂");
			output = output.replaceAll("&sube;", "⊆");
			output = output.replaceAll("&sup;", "⊃");
			output = output.replaceAll("&supe;", "⊇");
			output = output.replaceAll("&isin;", "∈");
			output = output.replaceAll("&ni;", "∋");
			output = output.replaceAll("&exist;", "∃");
			output = output.replaceAll("&forall;", "∀");
			output = output.replaceAll("&oplus;", "⊕");
			output = output.replaceAll("&infin;", "∞");
			output = output.replaceAll("&divide;", "—");
			output = output.replaceAll("&euro;", "€");
			
			output = output.replaceAll("&fnof;", "ƒ");
			
			output = output.replaceAll("&Alpha;", "Α");
			output = output.replaceAll("&Beta;", "Β");
			output = output.replaceAll("&Gamma;", "Γ");
			output = output.replaceAll("&Delta;", "Δ");
			output = output.replaceAll("&Epsilon;", "Ε");
			output = output.replaceAll("&Zeta;", "Ζ");
			output = output.replaceAll("&Eta;", "Η");
			output = output.replaceAll("&Theta;", "Θ");
			output = output.replaceAll("&Iota;", "Ι");
			output = output.replaceAll("&Kappa;", "Κ");
			output = output.replaceAll("&Lambda;", "Λ");
			output = output.replaceAll("&Mu;", "Μ");
			output = output.replaceAll("&Nu;", "Ν");
			output = output.replaceAll("&Xi;", "Ξ");
			output = output.replaceAll("&Omicron;", "Ο");
			output = output.replaceAll("&Pi;", "Π");
			output = output.replaceAll("&Rho;", "Ρ");

			output = output.replaceAll("&Sigma;", "Σ");
			output = output.replaceAll("&Tau;", "Τ");
			output = output.replaceAll("&Upsilon;", "Υ");
			output = output.replaceAll("&Phi;", "Φ");
			output = output.replaceAll("&Chi;", "Χ");
			output = output.replaceAll("&Psi;", "Ψ");
			output = output.replaceAll("&Omega;", "Ω");
			
			output = output.replaceAll("&alpha;", "α");
			output = output.replaceAll("&beta;", "β");
			output = output.replaceAll("&gamma;", "γ");
			output = output.replaceAll("&delta;", "δ");
			output = output.replaceAll("&epsilon;", "ε");
			output = output.replaceAll("&zeta;", "ζ");
			output = output.replaceAll("&eta;", "η");
			output = output.replaceAll("&theta;", "θ");
			output = output.replaceAll("&iota;", "ι");
			output = output.replaceAll("&kappa;", "κ");
			output = output.replaceAll("&lambda;", "λ");
			output = output.replaceAll("&mu;", "μ");
			output = output.replaceAll("&nu;", "ν");
			output = output.replaceAll("&xi;", "ξ");			
			output = output.replaceAll("&omicron;", "ο");
			output = output.replaceAll("&pi;", "π");
			output = output.replaceAll("&rho;", "ρ");
			output = output.replaceAll("&sigmaf;", "ς");
			output = output.replaceAll("&sigma;", "σ");
			output = output.replaceAll("&tau;", "τ");
			output = output.replaceAll("&upsilon;", "υ");
			output = output.replaceAll("&phi;", "φ");
			output = output.replaceAll("&chi;", "χ");
			output = output.replaceAll("&psi;", "ψ");
			output = output.replaceAll("&omega;", "ω");
			
			output = output.replaceAll("&thetasym;", "ϑ");
			output = output.replaceAll("&upsih;", "ϒ");
			output = output.replaceAll("&piv;", "ϖ");
			
//			output = output.replaceAll("&[a-zA-Z]{1,6};", "..."); // alle anderen Sonderzeichen...
			
			}
			}

	  	  return output;
	  	  
		  }
	  
	  private String htmlHexToString (String output) {
		  
	  	  try{
	  		Pattern p = Pattern.compile("&#x[a-zA-Z0-9]{2,4};");
		  	Matcher m = p.matcher(output);
	  		TreeSet<String> set = new TreeSet<String>();
	  	  while (m.find()) {
	  		 String htmlHex = output.substring(m.start(), m.end());
	  		 set.add(htmlHex);
//	  		 System.out.println(htmlHex);
	  	  	}
	  	  
	  	  for (String htmlHex : set) {
	  		  String hex = htmlHex.substring(3, htmlHex.indexOf(";"));
	  		  int c = Integer.parseInt(hex,16);
			  char chr = (char)c;
			  String replace = Character.toString(chr);
			  output = output.replaceAll(htmlHex, replace);
	  	  }
	  	  
	  	  } catch (Exception e) {
	  		  log.error("htmlHexToString (String output): " + e.toString() + "\012" + output);
	  	  }
		  
		  return output;
	  }
	  
	  private String htmlDezToString (String output) {
		  
	  	  try{
	  		Pattern p = Pattern.compile("&#[0-9]{2,6};");
		  	Matcher m = p.matcher(output);
	  		TreeSet<String> set = new TreeSet<String>();
	  	  while (m.find()) {
	  		 String htmlDez = output.substring(m.start(), m.end());
	  		 set.add(htmlDez);
//	  		 System.out.println(htmlDez);
	  	  	}

	  	  for (String htmlDez : set) {
	  		  String dez = htmlDez.substring(2, htmlDez.indexOf(";"));
	  		  int c = Integer.parseInt(dez,10);
			  char chr = (char)c;
			  String replace = Character.toString(chr);
			  output = output.replaceAll(htmlDez, replace);
	  	  }
	  	  
	  	  } catch (Exception e) {
	  		  log.error("htmlDezToString (String output): " + e.toString() + "\012" + output);
	  	  }
		  
		  return output;
	  }
	  

}
