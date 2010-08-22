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

package ch.dbs.actions.illformat;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Text;
import ch.dbs.form.IllForm;

/**
 * Methode um Illformat-Requests zu empfangen
 *
 * @author Markus Fischer
 */
public final class GetIllFormatRequest extends Action {

  private static final SimpleLogger LOG = new SimpleLogger(GetIllFormatRequest.class);

  /**
   * empfängt Ill-Requests und stellt ein IllForm her
   */
  public ActionForward execute(ActionMapping mp, ActionForm form,
            HttpServletRequest rq, HttpServletResponse rp) {

    Text cn = new Text();
    String forward = "failure";
    IllHandler illHandlerInstance = new IllHandler();

    try {

      forward = "success";

      IllForm ill = illHandlerInstance.readIllRequest(rq);
          String returnvalue = illHandlerInstance.updateOrderState(ill, cn.getConnection());

          if (returnvalue.equals("OK")) {
            ill.setStatus("OK");
          } else {
            ill.setStatus("ERROR");
            ill.setComment(returnvalue);
          }

          rq.setAttribute("illform", ill);

    } catch (Exception e) {
      LOG.error("getIllFormatRequest - execute: " + e.toString());
      } finally {
        cn.close();
      }

        return mp.findForward(forward);
    }


}
