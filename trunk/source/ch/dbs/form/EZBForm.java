//  Copyright (C) 2011  Markus Fischer
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

import org.apache.struts.action.ActionForm;


public final class EZBForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private String linkezb;

    private List<EZBReference> referencesonline = new ArrayList<EZBReference>();
    private List<EZBReference> referencesprint = new ArrayList<EZBReference>();
    private List<EZBDataOnline> online = new ArrayList<EZBDataOnline>();
    private List<EZBDataPrint> print = new ArrayList<EZBDataPrint>();


    public String getLinkezb() {
        return linkezb;
    }
    public void setLinkezb(final String linkezb) {
        this.linkezb = linkezb;
    }
    public List<EZBReference> getReferencesonline() {
        return referencesonline;
    }
    public void setReferencesonline(final List<EZBReference> referencesonline) {
        this.referencesonline = referencesonline;
    }
    public List<EZBReference> getReferencesprint() {
        return referencesprint;
    }
    public void setReferencesprint(final List<EZBReference> referencesprint) {
        this.referencesprint = referencesprint;
    }
    public List<EZBDataOnline> getOnline() {
        return online;
    }
    public void setOnline(final List<EZBDataOnline> online) {
        this.online = online;
    }
    public List<EZBDataPrint> getPrint() {
        return print;
    }
    public void setPrint(final List<EZBDataPrint> print) {
        this.print = print;
    }

}
