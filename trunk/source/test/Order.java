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

package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Lieferanten;

/**
 * Testklasse zum handeln von Bestellungen
 * @author Pascal Steiner
 *
 */
public class Order {
	
	public Order(){
		
	}
	/**
	 * Testet das Erstellen eines Bestellungsobjektes und speichert dieses in der Datenbank
	 * @author Pascal Steiner
	 * @return Bestellungen b
	 */
	public Bestellungen newOrder(){
	    //Bestellung Schreiben und ID auslesen
	    Bestellungen b = new Bestellungen();
	    AbstractBenutzer ab = new AbstractBenutzer();
	    Lieferanten lieferantenInstance = new Lieferanten();
	    b.setKonto(new Konto(Long.valueOf(1),b.getSingleConnection()));
	    b.setBenutzer(ab.getUser(Long.valueOf(1), b.getSingleConnection()));
        b.setLieferant(lieferantenInstance.getLieferantFromName("Subito", b.getSingleConnection()));
        b.setPriority("normal");
        b.setDeloptions("email");// Lieferbedingungen. In Subito neu "deliveryway". Nur noch "post" und "fax" möglich
        b.setFileformat("Fax to PDF");
        b.setHeft("Heft");
        b.setSeiten("Seite");
        b.setIssn("ISSN");
        b.setSigel("Sigel");
        b.setBibliothek("Bestellbibliothek");
        b.setAutor("Author");
        b.setZeitschrift("Zeitschift");
        b.setJahr("Jahr");
        b.setArtikeltitel("Artikeltitel");
        b.setJahrgang("Jahrgang");
        b.setSubitonr("Subittonummer");
        b.setSystembemerkung("Systembemerkung");
        b.setNotizen("Notizen"); // interne Anmerkungen
        
        Date d = new Date(); 
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datum = fmt.format(d);
        
        // um zu verhindern, dass eine Bestellung kein Datum erhält, falls beim Statusschreiben etwas schief geht
        b.setOrderdate(datum);
        b.setStatusdate(datum);
        b.setStatustext("bestellt");        
        b.save(b.getSingleConnection());
        b.close();
        
        System.out.println("Die Testbestellung wurde erfolgreich gespeichert. ID der soeben gespeicherten Bestellung lautet: "+ b.getId());
	    
        return b;
		
	}
	
   

}
