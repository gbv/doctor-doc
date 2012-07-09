//  Copyright (C) 2005 - 2012  Markus Fischer, Pascal Steiner
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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.List;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Text;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;

import enums.TextType;

public class IPChecker {
    
    private static final SimpleLogger LOG = new SimpleLogger(IPChecker.class);
    
    /**
     * Checks an IPv4 or IP6 against entries in the database. 1. Priority:
     * distinct IPs 2. Priority: IP ranges. IP4: valid are ranges and wildcards
     * in the third and fourth octet IP6: valid are ranges from the third and
     * consequent octets
     */
    
    public Text contains(final String ip, final Connection cn) {
        Text t = new Text();
        
        try {
            
            // make sure we look up only valid IP addresses
            final InetAddress inetaddress = InetAddress.getByName(ip);
            
            if (inetaddress instanceof Inet4Address) {
                t = lookUpIP(ip, TextType.IP4, cn);
            } else if (inetaddress instanceof Inet6Address) {
                t = lookUpIP(ip, TextType.IP6, cn);
            }
            
        } catch (final UnknownHostException ex) {
            LOG.error("Text contains(String ip, Connection cn): " + ip + "\040" + ex.toString());
        }
        
        return t;
    }
    
    private Text lookUpIP(final String ip, final TextType type, final Connection cn) {
        
        Text t = new Text();
        
        // if we have a distinct IP in the database
        t = new Text(cn, type, ip); // Text with IP
        
        if (isTextNull(t)) {
            // checking for ranges
            final List<Text> list = t.possibleIPRanges(ip, type, cn);
            
            if (TextType.IP4.getValue() == type.getValue()) {
                for (final Text ipToCheck : list) {
                    if (compareIP4(ip, ipToCheck.getInhalt())) {
                        t = ipToCheck;
                        return t;
                    }
                }
            } else if (TextType.IP6.getValue() == type.getValue()) {
                for (final Text ipToCheck : list) {
                    if (compareIP6(ip, ipToCheck.getInhalt())) {
                        t = ipToCheck;
                        return t;
                    }
                }
            }
        }
        
        return t;
    }
    
    private boolean compareIP4(final String ip, final String ip_db) {
        boolean check = false;
        
        try {
            
            final String compare = ip_db.substring(ip.indexOf(".", ip.indexOf(".") + 1) + 1);
            final String oktett3 = ip.substring(ip.indexOf(".", ip.indexOf(".") + 1) + 1, ip.lastIndexOf("."));
            final String oktett4 = ip.substring(ip.lastIndexOf(".") + 1);
            
            if (!compare.contains(".")) { // Wildcard oder Bereich im dritten Oktett
            
                if ("*".equals(compare)) { // Wildcard
                    check = true;
                    return check;
                }
                if (compare.contains("-")) { // Bereich
                    final int compStart = Integer.valueOf(compare.substring(0, compare.indexOf('-')));
                    final int compEnd = Integer.valueOf(compare.substring(compare.indexOf('-') + 1));
                    final int okt3 = Integer.valueOf(oktett3);
                    if (compStart <= okt3 && okt3 <= compEnd) {
                        check = true;
                        return check;
                    }
                }
                
            } else { // Wildcard oder Bereich im vierten Oktett
                final String compare3 = compare.substring(0, compare.indexOf('.'));
                final String compare4 = compare.substring(compare.indexOf('.') + 1);
                if (compare3.equals(oktett3)) { // drittes Oktett muss übereinstimmen
                
                    if ("*".equals(compare4)) { // Wildcard
                        check = true;
                        return check;
                    }
                    if (compare4.contains("-")) { // Bereich
                        final int compStart = Integer.valueOf(compare4.substring(0, compare4.indexOf('-')));
                        final int compEnd = Integer.valueOf(compare4.substring(compare4.indexOf('-') + 1));
                        final int okt4 = Integer.valueOf(oktett4);
                        if (compStart <= okt4 && okt4 <= compEnd) {
                            check = true;
                            return check;
                        }
                    }
                    
                }
            }
            
        } catch (final Exception e) {
            LOG.error("boolean compareIP4(String ip, String ip_db): " + e.toString());
        }
        
        return check;
    }
    
    private boolean compareIP6(final String ip, final String ip_db) {
        boolean check = false;
        
        try {
            // valid are only ranges for IP6 and no wildcards.
            if (ip_db.contains("-")) {
                final String fromAddress = ip_db.substring(0, ip_db.indexOf("-"));
                final String toAddress = ip_db.substring(ip_db.indexOf("-") + 1);
                
                final IPv6AddressRange range = IPv6AddressRange.fromFirstAndLast(IPv6Address.fromString(fromAddress),
                        IPv6Address.fromString(toAddress));
                
                check = range.contains(IPv6Address.fromString(ip));
                
            }
            
        } catch (final Exception e) {
            LOG.error("boolean compareIP6(String ip, String ip_db): " + e.toString());
        }
        
        return check;
    }
    
    private boolean isTextNull(final Text t) {
        boolean check = true;
        
        if (t != null && t.getId() != null) {
            check = false;
        }
        
        return check;
        
    }
    
}
