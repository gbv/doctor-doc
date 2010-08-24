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


public final class ErrorMessage extends ActionForm {

  private static final long serialVersionUID = 1L;
  private String error; // steuert MessageRessources an
  private String error_specific; // dient für spezifische, genauere Fehlermeldung
    private String link;


    public ErrorMessage() {

    }
    public ErrorMessage(String err, String errLink) {
        this.error = err;
        this.link = errLink;
    }

    public ErrorMessage(String err, String error_spec, String errlink) {
        this.error = err;
        this.error_specific = error_spec;
        this.link = errlink;
    }

    public ErrorMessage(String err) {
        this.error = err;
    }


    public String getError() {
        return error;
    }


    public void setError(String error) {
        this.error = error;
    }


    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }

  public String getError_specific() {
    return error_specific;
  }

  public void setError_specific(String error_specific) {
    this.error_specific = error_specific;
  }






}
