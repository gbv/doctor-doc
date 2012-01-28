//  Copyright (C) 2012  Markus Fischer, Pascal Steiner
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

import org.apache.struts.action.ActionForm;

import ch.dbs.entity.Lieferanten;


public final class SupplierForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private transient Lieferanten supplier;



    public SupplierForm() {

    }


    public Lieferanten getSupplier() {
        return supplier;
    }
    public void setSupplier(final Lieferanten supplier) {
        this.supplier = supplier;
    }

}
