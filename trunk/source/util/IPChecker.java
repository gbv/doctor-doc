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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.List;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Text;
import enums.TextType;

public class IPChecker {

    private static final SimpleLogger LOG = new SimpleLogger(IPChecker.class);

    /**
     * prüft eine IPv4 gegen eine IP-Adressliste in einer Datenbank
     * 1. Priorität: ganze IPs
     * 2. Priorität: IP-Bereiche, wobei nur Bereiche und Wildcards im dritten und vierten Oktett berücksichtigt werden
     */

    public Text contains(final String ip, final Connection cn) {
        Text t = new Text();

        try { // Zur Absicherung, damit nur richtige IPs geprüft werden
            final InetAddress a4 = InetAddress.getByName(ip);

            if (a4 instanceof Inet4Address) {

                // falls IP eindeutig hinterlegt
                t = new Text(cn, TextType.IP, ip); // Text mit IP

                if (isTextNull(t)) {
                    // Prüfung auf IP-Bereiche
                    final List<Text> list = t.possibleIPRanges(ip, cn);

                    for (final Text ipToCheck : list) {
                        if (compare(ip, ipToCheck.getInhalt())) {
                            t = ipToCheck;
                            return t;
                        }
                    }
                }
            }

        } catch (final UnknownHostException ex) {
            LOG.error("Text contains(String ip, Connection cn): " + ip + "\040" + ex.toString());
        }

        return t;
    }

    private boolean compare(final String ip, final String ip_db) {
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
            LOG.error("boolean compare(String ip, String ip_db): " + e.toString());
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
