//  Copyright (C) 2013  Markus Fischer
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

package ch.dbs.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

public final class IPForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private String ips;

    private final List<String> ip4 = new ArrayList<String>();
    private final List<String> ip6 = new ArrayList<String>();
    private final List<String> invalidIPs = new ArrayList<String>();

    public String getIps() {
        return ips;
    }

    public void setIps(final String ips) {
        this.ips = ips;
    }

    public List<String> getIp4() {
        return ip4;
    }

    public List<String> getIp6() {
        return ip6;
    }

    public List<String> getInvalidIPs() {
        return invalidIPs;
    }

}
