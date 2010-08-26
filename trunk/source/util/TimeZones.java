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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

public class TimeZones {

    // Filter
    private static final String PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

    private static List<TimeZone> timeZs;
    private static TreeSet<String> timeZonesAsString;

    public List<TimeZone> getTimeZones() {
        if (timeZs == null) {
            initTimeZones();
        }
        return timeZs;
    }

    public TreeSet<String> getTimeZonesAsString() {
        if (timeZonesAsString == null) {
            initTimeZonesAsStrings();
        }
        return timeZonesAsString;
    }

    private static void initTimeZones() {
        timeZs = new ArrayList<TimeZone>();
        final String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (final String id : timeZoneIds) {
            if (id.matches(PREFIXES)) { // Filter with Regex
                timeZs.add(TimeZone.getTimeZone(id));
            }
        }
        Collections.sort(timeZs, new Comparator<TimeZone>() { // sort
            public int compare(final TimeZone a, final TimeZone b) {
                return a.getID().compareTo(b.getID());
            }
        });
    }

    private static void initTimeZonesAsStrings() {
        timeZonesAsString = new TreeSet<String>();
        final String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (final String id : timeZoneIds) {
            if (id.matches(PREFIXES)) { // Filter with Regex
                timeZonesAsString.add(id);
            }
        }
    }

}
