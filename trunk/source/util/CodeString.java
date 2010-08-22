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

package util;

import java.io.IOException;

import org.grlea.log.SimpleLogger;

public class CodeString {

  private static final SimpleLogger LOG = new SimpleLogger(CodeString.class);

    /**
     * Codiert und DeCodiert einen String mit Base64. Keine Verschlüsselung sondern nur
     * Codierung!
     * @param args
     */

  // wird zum Maskieren der Benutzerangaben im Cookie des Bestellformulares benötigt.
  // Besserer Datenschutz!

  public String encodeString(String psString) {
    if (psString == null) { return ""; }

    return (new sun.misc.BASE64Encoder()).encode(psString.getBytes());
  }

  public String decodeString(String psString) {
    if (psString == null || psString.equals("")) { return null; }

    try {
      return new String((new sun.misc.BASE64Decoder())
          .decodeBuffer(psString));
    } catch (IOException e) {
      LOG.error("decodeString: " + psString + "\040" + e.toString());
      return null;
    }
  }

}
