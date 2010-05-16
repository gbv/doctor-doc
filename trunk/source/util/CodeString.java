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

import java.io.IOException;

import org.grlea.log.SimpleLogger;

public class CodeString {
	
	private static final SimpleLogger log = new SimpleLogger(CodeString.class);
	
    /**
     * Codiert und DeCodiert einen String mit Base64. Keine Verschlüsselung sondern nur
     * Codierung!
     * @param args
     */

	// wird zum Maskieren der Benutzerangaben im Cookie des Bestellformulares benötigt.
	// Besserer Datenschutz!
	
	public String encodeString(String p_sString) {
		if (p_sString == null)
			return "";

		return (new sun.misc.BASE64Encoder()).encode(p_sString.getBytes());
	}

	public String decodeString(String p_sString) {
		if (p_sString == null || p_sString.equals(""))
			return null;

		try {
			return new String((new sun.misc.BASE64Decoder())
					.decodeBuffer(p_sString));
		} catch (IOException e) {
			log.error("decodeString: " + p_sString + "\040" + e.toString());
			return null;
		}
	}

}
