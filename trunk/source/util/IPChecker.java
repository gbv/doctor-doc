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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;

public class IPChecker {
	
	private static final SimpleLogger log = new SimpleLogger(IPChecker.class);
	
	public IPChecker(){
        
    	}
	
	/**
     * prüft eine IPv4 gegen eine IP-Adressliste in einer Datenbank
     * 1. Priorität: ganze IPs
     * 2. Priorität: IP-Bereiche, wobei nur Bereiche und Wildcards im dritten und vierten Oktett berücksichtigt werden
     */
	
	public Text contains(String ip, Connection cn) {
		Text t = new Text();		
		Texttyp iptyp = new Texttyp(Long.valueOf(9), cn); // Texttyp IP
		
		try { // Zur Absicherung, damit nur richtige IPs geprüft werden
		InetAddress a4 = InetAddress.getByName(ip);
		 
		if (a4 instanceof Inet4Address) {
		
		// falls IP eindeutig hinterlegt
		t = new Text(cn, iptyp, ip); // Text mit IP
		
		if (isTextNull(t)) {
			// Prüfung auf IP-Bereiche
			ArrayList<Text> list = new ArrayList<Text>();
			list = t.possibleIPRanges(ip, cn);
			
			for (int i=0;i<list.size();i++) { // zu prüfende IP-Bereiche				
				if (compare(ip, list.get(i).getInhalt())) {
					t = list.get(i);
					return t;
				}
			}			
		}
		}
		
		} catch (UnknownHostException ex) {
	  		  log.error("Text contains(String ip, Connection cn): " + ip + "\040" + ex.toString());
		}
		
		return t;
	}
	  
	private boolean compare(String ip, String ip_db) {
		boolean check = false;		
		
		try {
			
			String compare = ip_db.substring(ip.indexOf(".", ip.indexOf(".")+1)+1);
			String oktett3 = ip.substring(ip.indexOf(".", ip.indexOf(".")+1)+1, ip.lastIndexOf("."));
			String oktett4 = ip.substring(ip.lastIndexOf(".")+1);
			
			if (!compare.contains(".")) { // Wildcard oder Bereich im dritten Oktett
				
				if (compare.equals("*")) { // Wildcard
					check = true;
					return check;
				}
				if (compare.contains("-")) { // Bereich
					int comp_start = Integer.valueOf(compare.substring(0, compare.indexOf("-")));
					int comp_end = Integer.valueOf(compare.substring(compare.indexOf("-")+1));
					int okt3 = Integer.valueOf(oktett3);
					if (comp_start <= okt3 && okt3 <= comp_end) {
						check = true;
						return check;
					}					
				}
				
			} else { // Wildcard oder Bereich im vierten Oktett
				String compare3 = compare.substring(0, compare.indexOf("."));
				String compare4 = compare.substring(compare.indexOf(".")+1);
				if (compare3.equals(oktett3)) { // drittes Oktett muss übereinstimmen
					
					if (compare4.equals("*")) { // Wildcard
						check = true;
						return check;
					}
					if (compare4.contains("-")) { // Bereich
						int comp_start = Integer.valueOf(compare4.substring(0, compare4.indexOf("-")));
						int comp_end = Integer.valueOf(compare4.substring(compare4.indexOf("-")+1));
						int okt4 = Integer.valueOf(oktett4);
						if (comp_start <= okt4 && okt4 <= comp_end) {
							check = true;
							return check;
						}					
					}
					
				}				
			}
			
		} catch (Exception e) {
			log.error("boolean compare(String ip, String ip_db): " + e.toString());		
		}
		
		return check;
	}
	
	private boolean isTextNull(Text t) {
		boolean check = true;
		
		if (t!=null && t.getId()!=null) check = false;
		
		return check;
		
	}
	

}
