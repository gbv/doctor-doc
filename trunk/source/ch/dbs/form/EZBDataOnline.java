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

import org.apache.struts.action.ActionForm;


public final class EZBDataOnline extends ActionForm {

    private static final long serialVersionUID = 1L;

    private int state;
    private String title;
    private String url;
    private String level;
    private String readme;
    private String additional;

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
    public String getUrl() {
        return url;
    }
    public void setUrl(final String url) {
        this.url = url;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(final String level) {
        this.level = level;
    }
    public String getReadme() {
        return readme;
    }
    public void setReadme(final String readme) {
        this.readme = readme;
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
    public String getAdditional() {
        return additional;
    }
    public void setAdditional(final String additional) {
        this.additional = additional;
    }

}
