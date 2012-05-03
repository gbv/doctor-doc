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

package ch.dbs.form;

import java.sql.Connection;

import org.apache.struts.action.ActionForm;

public final class MaintenanceForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private boolean confirmed;
    private int months;
    private int numerOfRecords;
    private String method;

    public MaintenanceForm() {

    }

    public int countDeleteOrders(final UserInfo ui, final Connection connection) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void deleteOrders(final UserInfo ui, final Connection connection) {
        // TODO Auto-generated method stub

    }

    public int countDeleteUserNoOrders(final UserInfo ui, final Connection connection) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void deleteuserNoOrders(final UserInfo ui, final Connection connection) {
        // TODO Auto-generated method stub

    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(final boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(final int months) {
        this.months = months;
    }

    public int getNumerOfRecords() {
        return numerOfRecords;
    }

    public void setNumerOfRecords(final int numerOfRecords) {
        this.numerOfRecords = numerOfRecords;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

}
