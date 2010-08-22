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

package ch.dbs.actions.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.RemoveNullValuesFromObject;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Holding;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.UserInfo;

/**
 * Creates an export of the holdings of a given library
 *
 * @author Markus Fischer
 *
 */
public final class HoldingsReport extends DispatchAction {

  private static final SimpleLogger LOG = new SimpleLogger(HoldingsReport.class);

    /**
     * Gets all holdings of a given library and creates an Export-File
     */
    public ActionForward execute(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

      String forward = "failure";
      Auth auth = new Auth();

      // Get export filetype for export as either CSV with semicolon delimiter or TXT as tab delimited file
      String filetype = rq.getParameter("filetype");
      if (filetype == null || !filetype.equals("txt")) { filetype = "csv"; } // set default value csv

      // Check if the user is logged in and is librarian or admin
      if (auth.isLogin(rq)) {
      if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users

        // Prepare classes
        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();

        try {
          // Compose filename with date and time
          StringBuffer filename = new StringBuffer("holdings-");
          filename.append(tf.format(date, ui.getKonto().getTimezone())); // append date and time
          filename.append(".");
          filename.append(filetype);

          // define delimiter
          char delimiter = ch.dbs.actions.bestand.Stock.getDelimiterCsv(); // default value
          if (filetype.equals("txt")) { delimiter = ch.dbs.actions.bestand.Stock.getDelimiterTxt(); }

          // Prepare Output
          rp.setContentType("text/txt;charset=UTF-8"); // Set ContentType in the response for the Browser
          rp.addHeader("Content-Disposition", "attachment;filename=" + filename.toString()); // Set filename
          rp.setCharacterEncoding("UTF-8");

          rp.flushBuffer();

          // Use writer to render text
          PrintWriter pw = rp.getWriter();
            pw.write(getExportContent(ui.getKonto(), delimiter));
            pw.flush();
            pw.close();

        } catch (IOException e) {
          // Output failed
          LOG.error("Failure in HoldingsReport.execute: " + e.toString());
        } finally {
            forward = null;
          }

      } else {
        ErrorMessage em = new ErrorMessage(
            "error.berechtigung",
            "login.do");
        rq.setAttribute("errormessage", em);
      }
    } else {
      ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
      ErrorMessage em = new ErrorMessage(
          "error.timeout", "login.do");
      rq.setAttribute("errormessage", em);
    }

    return mp.findForward(forward);
  }

    private String getExportContent(Konto k, char delimiter) {
      Text cn = new Text();

      // get a StringBuffer with a header describing the content of the fields
      StringBuffer buf = initStringBuffer(delimiter);

      ArrayList<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), cn.getConnection());

      for (Bestand b : stock) {
        buf.append(getExportLine(b, delimiter));
      }

      cn.close();

      return buf.toString();
    }



  private String getExportLine(Bestand b, char delimiter) {

    StringBuffer buf = new StringBuffer();
    RemoveNullValuesFromObject nullValues = new RemoveNullValuesFromObject();

    b = (Bestand) nullValues.remove(b);
    b.setHolding((Holding) nullValues.remove(b.getHolding()));

    buf.append("\"" + b.getId() + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.getHolding().getId() + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.getStandort().getId() + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.getStandort().getInhalt() + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getShelfmark()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getTitel()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getCoden()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getVerlag()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getOrt()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getIssn()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getHolding().getZdbid()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getStartyear()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getStartvolume()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getStartissue()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getEndyear()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getEndvolume()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getEndissue()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.getSuppl() + "\"");
    buf.append(delimiter);
    buf.append("\"" + removeSpecialCharacters(b.getBemerkungen()) + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.isEissue() + "\"");
    buf.append(delimiter);
    buf.append("\"" + b.isInternal() + "\"");
    buf.append("\n");

    return buf.toString();
  }

  private StringBuffer initStringBuffer(char delimiter) {

    StringBuffer buf = new StringBuffer();

    buf.append("\"Stock ID\"");
    buf.append(delimiter);
    buf.append("\"Holding ID\"");
    buf.append(delimiter);
    buf.append("\"Location ID\"");
    buf.append(delimiter);
    buf.append("\"Location Name\"");
    buf.append(delimiter);
    buf.append("\"Shelfmark\"");
    buf.append(delimiter);
    buf.append("\"Title\"");
    buf.append(delimiter);
    buf.append("\"Coden\"");
    buf.append(delimiter);
    buf.append("\"Publisher\"");
    buf.append(delimiter);
    buf.append("\"Place\"");
    buf.append(delimiter);
    buf.append("\"ISSN\"");
    buf.append(delimiter);
    buf.append("\"ZDB-ID\"");
    buf.append(delimiter);
    buf.append("\"Startyear\"");
    buf.append(delimiter);
    buf.append("\"Startvolume\"");
    buf.append(delimiter);
    buf.append("\"Startissue\"");
    buf.append(delimiter);
    buf.append("\"Endyear\"");
    buf.append(delimiter);
    buf.append("\"Endvolume\"");
    buf.append(delimiter);
    buf.append("\"Endissue\"");
    buf.append(delimiter);
    buf.append("\"Suppl\"");
    buf.append(delimiter);
    buf.append("\"remarks\"");
    buf.append(delimiter);
    buf.append("\"eissue\"");
    buf.append(delimiter);
    buf.append("\"internal\"");
    buf.append("\n");

    return buf;
  }

  private String removeSpecialCharacters(String str) {

    if (str != null) {
      str = str.replaceAll("\012", "\040").trim();
    }

    return str;
  }


}
