//  Copyright (C) 2012  Markus Fischer
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

package enums;

public enum Connect {

    TIMEOUT_1("TIMEOUT_1", 1000), TIMEOUT_2("TIMEOUT_2", 2000), TIMEOUT_3("TIMEOUT_3", 3000), TIMEOUT_4("TIMEOUT_4",
            4000), TIMEOUT_5("TIMEOUT_5", 5000), TIMEOUT_6("TIMEOUT_6", 6000), TIMEOUT_7("TIMEOUT_7", 7000), TIMEOUT_8(
            "TIMEOUT_8", 8000), TIMEOUT_9("TIMEOUT_9", 9000), RETRYS_1("RETRYS_1", 1), RETRYS_2("RETRYS_2", 2), RETRYS_3(
            "RETRYS_3", 3);

    private final String name;
    private final int value;

    private Connect(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
