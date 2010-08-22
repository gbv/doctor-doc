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

import java.security.MessageDigest;

import org.grlea.log.SimpleLogger;

public class Encrypt {

  private static final SimpleLogger LOG = new SimpleLogger(Encrypt.class);

  /**
     * Erstellt aus einem String den SHA-1 Hash
     * @param s
     * @return
     */
    public String makeSHA(String s) {

      StringBuffer encoded = new StringBuffer();

      try {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] digest = md.digest(s.getBytes());

      for (byte d : digest) { encoded.append(Integer.toHexString(d & 0xFF)); }

      } catch (Exception e) {
        LOG.error("makeSHA: " + e.toString());
    }

      return encoded.toString();

    }

}
