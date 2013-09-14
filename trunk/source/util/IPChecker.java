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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.entity.Text;
import ch.dbs.form.IPForm;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;

import enums.TextType;

public class IPChecker {

    private static final Logger LOG = LoggerFactory.getLogger(IPChecker.class);

    /**
     * Checks an IPv4 or IP6 against entries in the database. 1. Priority:
     * distinct IPs 2. Priority: IP ranges. IP4: valid are ranges and wildcards
     * in the third and fourth octet IP6: valid are ranges from the third and
     * consequent octets
     */
    public Text contains(final String ip, final Connection cn) {
        Text t = new Text();

        try {

            // ordinary lookup: no wildcard or range
            if (!ip.contains("*") && !ip.contains("-")) {
                // make sure we look up only valid IP addresses
                final InetAddress inetaddress = InetAddress.getByName(ip);

                if (inetaddress instanceof Inet4Address) {
                    t = lookUpIP(ip, TextType.IP4, cn);
                } else if (inetaddress instanceof Inet6Address) {
                    t = lookUpIP(ip, TextType.IP6, cn);
                }
            } else {
                // IP to lookup conatins wildcard or range
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

            final String compare = ip_db.substring(ip.indexOf('.', ip.indexOf('.') + 1) + 1);
            final String oktett3 = ip.substring(ip.indexOf('.', ip.indexOf('.') + 1) + 1, ip.lastIndexOf('.'));
            final String oktett4 = ip.substring(ip.lastIndexOf('.') + 1);

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
                final String fromAddress = ip_db.substring(0, ip_db.indexOf('-'));
                final String toAddress = ip_db.substring(ip_db.indexOf('-') + 1);

                final IPv6AddressRange range = IPv6AddressRange.fromFirstAndLast(IPv6Address.fromString(fromAddress),
                        IPv6Address.fromString(toAddress));

                check = range.contains(IPv6Address.fromString(ip));

            }

        } catch (final Exception e) {
            LOG.error("boolean compareIP6(String ip, String ip_db): " + e.toString());
        }

        return check;
    }

    /**
     * Validates a list of IPs and returns them separated into IP4, IP6 and
     * invalid IPs in the IPForm.
     */
    public void validateIPs(final List<String> ips, final IPForm ranges) {

        for (final String ip : ips) {

            if (ip.contains(".")) {
                // IP4
                validateIP4(ip, ranges);
            } else if (ip.contains(":")) {
                // IP6
                validateIP6(ip, ranges);
            } else {
                // invalid
                ranges.getInvalidIPs().add(ip);
            }

        }

    }

    private void validateIP4(final String ip, final IPForm ranges) {
        // get IP parts
        final List<String> parts = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(ip, ".");

        while (tokenizer.hasMoreElements()) {
            parts.add(tokenizer.nextElement().toString().trim());
        }

        // validate each part...
        if (isValidIP4Parts(parts)) {
            ranges.getIp4().add(ip);
        } else {
            ranges.getInvalidIPs().add(ip);
        }

    }

    private void validateIP6(final String ip, final IPForm ranges) {
        // TODO Auto-generated method stub

    }

    private boolean isValidIP4Parts(final List<String> parts) {
        // we need at least two sections, but not more than 4
        if (parts.size() < 2 || parts.size() > 4) {
            return false;
        }
        // check each part
        for (int i = 0; i < parts.size(); i++) {
            // 1. and 2. part
            if (i == 0 || i == 1) {
                if (isInvalidIP4Part1or2(parts.get(i))) {
                    return false;
                }
            } else {
                // make sure if we have in part 3 a wildcard or range, that there is no part 4
                if (i == 2 && (parts.get(i).contains("*") || parts.get(i).contains("-")) && parts.size() == 4) {
                    return false;
                }
                // 3. or 4. part
                if (isInvalidIP4Part3or4(parts.get(i))) {
                    return false;
                }
            }

        }

        return true;
    }

    private boolean isInvalidIP4Part1or2(final String part) {
        // 1. and 2. part must contain 1-3 characters... 
        if (part.length() < 1 || part.length() > 3) {
            return true;
        }
        // ...and only contain digits
        if (!org.apache.commons.lang.StringUtils.isNumeric(part)) {
            return true;
        }

        // validate range for 1-255
        if (isInvalidIP4PartRange(part)) {
            return true;
        }

        return false;
    }

    private boolean isInvalidIP4Part3or4(final String part) {
        // 1. and 2. part may contain 1-7 characters... 
        if (part.length() < 1 || part.length() > 7) {
            return true;
        }

        // wildcard is valid
        if ("*".equals(part)) {
            return false;
        }

        final String validChars = "0123456789-";
        // ...and only contain digits and dash
        if (!org.apache.commons.lang.StringUtils.containsOnly(part, validChars)) {
            return true;
        }

        // validate range
        if (isInvalidSubpartsIP4(part)) {
            return true;
        }

        return false;
    }

    private boolean isInvalidSubpartsIP4(final String part) {

        if (part.contains("-")) {

            final List<String> subparts = new ArrayList<String>();
            final StringTokenizer tokenizer = new StringTokenizer(part, "-");
            while (tokenizer.hasMoreElements()) {
                subparts.add(tokenizer.nextElement().toString().trim());
            }
            // we need exact two subparts
            if (subparts.size() != 2) {
                return true;
            }
            final String validChars = "0123456789";
            for (final String subpart : subparts) {
                // ...and only contain digits and dash
                if (!org.apache.commons.lang.StringUtils.containsOnly(subpart, validChars)
                        || isInvalidIP4PartRange(subpart)) {
                    return true;
                }
            }
            // make sure subpart 1 is smaller than subpart 2
            final int sp1 = Integer.valueOf(subparts.get(0));
            final int sp2 = Integer.valueOf(subparts.get(1));
            if (sp2 == sp1 || sp2 < sp1) {
                return true;
            }

        } else {
            // validate single part for 1-255
            if (isInvalidIP4PartRange(part)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidIP4PartRange(final String part) {
        final int value = Integer.valueOf(part);
        if (value < 0 || value > 255) {
            return true;
        }
        return false;
    }

    private boolean isTextNull(final Text t) {
        boolean check = true;

        if (t != null && t.getId() != null) {
            check = false;
        }

        return check;
    }

}
