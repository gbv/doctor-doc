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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64String {
    
    final Logger LOG = LoggerFactory.getLogger(Base64String.class);
    
    /**
     * Encodes and decodes a string with Base64. Not meant to be an encryption
     * but rather a simple encoding! This method is used to hide the user
     * details in the cookie of the order form.
     */
    
    public String encodeString(final String psString) {
        if (psString == null) {
            return "";
        }
        
        try {
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(psString.getBytes("UTF-8")));
        } catch (final Exception e) {
            LOG.error("encodeString: " + psString + "\040" + e.toString());
            return null;
        }
    }
    
    public String decodeString(final String psString) {
        if (psString == null || psString.equals("")) {
            return null;
        }
        
        try {
            return new String(org.apache.commons.codec.binary.Base64.decodeBase64(psString.getBytes("UTF-8")));
        } catch (final Exception e) {
            LOG.error("decodeString: " + psString + "\040" + e.toString());
            return null;
        }
    }
    
}
