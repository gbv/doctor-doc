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

    private static final String TIMEZONE_ID_PREFIXES =
        "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*"; // Filter

    private List<TimeZone> timeZones = null;
    private TreeSet<String> timeZonesAsString = null;

    public List<TimeZone> getTimeZones() {
        if (timeZones == null) {
            initTimeZones();
        }
        return timeZones;
    }

    public TreeSet<String> getTimeZonesAsString() {
        if (timeZonesAsString == null) {
            initTimeZonesAsStrings();
        }
        return timeZonesAsString;
    }

    private void initTimeZones() {
        timeZones = new ArrayList<TimeZone>();
        final String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (final String id : timeZoneIds) {
            if (id.matches(TIMEZONE_ID_PREFIXES)) { // Filter with Regex
                timeZones.add(TimeZone.getTimeZone(id));
            }
        }
        Collections.sort(timeZones, new Comparator<TimeZone>() { // sort
            public int compare(final TimeZone a, final TimeZone b) {
                return a.getID().compareTo(b.getID());
            }
        });
    }

    private void initTimeZonesAsStrings() {
        timeZonesAsString = new TreeSet<String>();
        final String[] timeZoneIds = TimeZone.getAvailableIDs();
        for (final String id : timeZoneIds) {
            if (id.matches(TIMEZONE_ID_PREFIXES)) { // Filter with Regex
                timeZonesAsString.add(id);
            }
        }
    }

}
