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

public enum TextType {

    RIGHTS("RIGHTS", 1), STATE_ORDER("STATE_ORDER", 2), STATE_FAX("STATE_FAX", 3), STATE_ACCOUNT("STATE_ACCOUNT", 4), SUPPLIER(
            "SUPPLIER", 5), GTC("GTC", 6), CURRENCY("CURRENCY", 7), BILLING_REASON("BILLING_REASON", 8), IP("IP", 9), LOCATION(
            "LOCATION", 10), ACCOUNT_ID_OVERRIDDEN_BY_IP("ACCOUNT_ID_OVERRIDDEN_BY_IP", 11), ACCOUNT_ID_OVERRIDES_IP(
            "ACCOUNT_ID_OVERRIDES_IP", 12), ORDERFORM_LOGGED_IN("ORDERFORM_LOGGED_IN", 13), DAIA_ID("DAIA_ID", 14), USER_CATEGORY(
            "USER_CATEGORY", 15), MAIL_SUBJECT("MAIL_SUBJECT", 16), MAIL_BODY("MAIL_BODY", 17), ;

    private final String name;
    private final long value;

    private TextType(final String name, final long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

}
