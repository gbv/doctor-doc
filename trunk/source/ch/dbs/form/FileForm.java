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
import org.apache.struts.upload.FormFile;


public final class FileForm extends ActionForm {

  /**
   * FileForm used to upload a file
   *
   */

  private static final long serialVersionUID = 1L;
  private FormFile file;
  private boolean condition;


  public FormFile getFile() {
    return file;
  }

  public void setFile(FormFile file) {
    this.file = file;
  }

  public boolean isCondition() {
    return condition;
  }

  public void setCondition(boolean condition) {
    this.condition = condition;
  }

}
