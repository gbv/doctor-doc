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

package ch.dbs.form;

import org.apache.struts.action.ActionForm;


public final class EZBDataPrint extends ActionForm {

    private static final long serialVersionUID = 1L;

    private int state;
    private String title;
    private String location;
    private String callnr;
    private String coverage;

    private EZBReference info;

    // logical properties
    private String ampel;
    private String comment;


    public int getState() {
        return state;
    }
    public void setState(final int state) {
        this.state = state;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(final String location) {
        this.location = location;
    }
    public String getCallnr() {
        return callnr;
    }
    public void setCallnr(final String callnr) {
        this.callnr = callnr;
    }
    public String getCoverage() {
        return coverage;
    }
    public void setCoverage(final String coverage) {
        this.coverage = coverage;
    }
    public String getAmpel() {
        return ampel;
    }
    public void setAmpel(final String ampel) {
        this.ampel = ampel;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(final String comment) {
        this.comment = comment;
    }
    public EZBReference getInfo() {
        return info;
    }
    public void setInfo(final EZBReference info) {
        this.info = info;
    }

}
