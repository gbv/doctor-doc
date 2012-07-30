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

import org.grlea.log.SimpleLogger;

/**
 * Hilft URLs sicher zu entcodieren
 * 
 * @param args
 */
public class CodeUrl {
    
    private static final SimpleLogger LOG = new SimpleLogger(CodeUrl.class);
    
    /**
     * Decodier-Methode
     */
    public String decode(String input, final String encoding) {
        
        try {
            if (input != null) {
                input = java.net.URLDecoder.decode(input, encoding);
            }
            
        } catch (final Exception e) {
            LOG.error("decode in UrlCode: " + encoding + "\012" + input + "\040" + e.toString());
        }
        
        return input;
    }
    
    /**
     * Encode string with specified encoding.
     */
    public String encode(String input, final String encoding) {
        
        try {
            if (input != null) {
                input = java.net.URLEncoder.encode(input, encoding);
            }
            
        } catch (final Exception e) {
            LOG.error("encode in UrlCode: " + encoding + "\012" + input + "\040" + e.toString());
        }
        
        return input;
    }
    
}
