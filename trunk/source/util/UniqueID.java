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

public final class UniqueID {

    // private constructor so no one else can instantiate objects
    private UniqueID() {

    }

    // Since the granulaty of a PC can be as high as 55ms (down to 10ms), you can't use
    // the System time to generate a unique ID because of the risk of getting duplicated IDs.
    // This can be solved by using the following technique to make sure that the number
    // returned is unique (in a single JVM).

    // GBV nimmt nur max. 16 Stellen entgegen! Diese Methode liefert momentan 13 Stellen...

    private static long current = System.currentTimeMillis();
    public static synchronized long get() {

        if (String.valueOf(current).length() > 15) {
            final MHelper mh = new MHelper();
            mh.sendErrorMail("GBV-Trackingnummer hat maximale Anzahl Stellen erreicht oder überschritten!!!",
                    "Util - UniqueID: Es wurden 16 oder mehr Stellen erreicht!:\012"
                    + String.valueOf(current).length());
        }

        return current++;
    }

}
