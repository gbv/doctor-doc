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
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestand;
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
    public ActionForward execute(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Get export filetype for export as either CSV with semicolon delimiter or TXT as tab delimited file
        String filetype = rq.getParameter("filetype");
        if (filetype == null || !filetype.equals("txt")) { filetype = "csv"; } // set default value csv

        // Check if the user is logged in and is librarian or admin
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users

                // Prepare classes
                final  UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                final Date date = new Date();

                try {
                    // Compose filename with date and time
                    final StringBuffer filename = new StringBuffer("holdings-");
                    filename.append(tf.format(date, ui.getKonto().getTimezone())); // append date and time
                    filename.append('.');
                    filename.append(filetype);

                    // define delimiter
                    char delimiter = ch.dbs.actions.bestand.Stock.getDelimiterCsv(); // default value
                    if ("txt".equals(filetype)) { delimiter = ch.dbs.actions.bestand.Stock.getDelimiterTxt(); }

                    // Prepare Output
                    rp.setContentType("text/txt;charset=UTF-8"); // Set ContentType in the response for the Browser
                    rp.addHeader("Content-Disposition", "attachment;filename=" + filename.toString()); // Set filename
                    rp.setCharacterEncoding("UTF-8");

                    rp.flushBuffer();

                    // Use writer to render text
                    final PrintWriter pw = rp.getWriter();
                    pw.write(getExportContent(ui.getKonto(), delimiter));
                    pw.flush();
                    pw.close();

                } catch (final IOException e) {
                    // Output failed
                    LOG.error("Failure in HoldingsReport.execute: " + e.toString());
                } finally {
                    forward = null;
                }

            } else {
                final ErrorMessage em = new ErrorMessage(
                        "error.berechtigung",
                        "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage(
                    "error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    private String getExportContent(final Konto k, final char delimiter) {
        final Text cn = new Text();

        // get a StringBuffer with a header describing the content of the fields
        final StringBuffer buf = initStringBuffer(delimiter);

        // internal holdings are visible
        final List<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), true, cn.getConnection());

        for (final Bestand b : stock) {
            buf.append(getExportLine(b, delimiter));
        }

        cn.close();

        return buf.toString();
    }



    private String getExportLine(final Bestand b, final char delimiter) {

        final StringBuffer buf = new StringBuffer(336);

        if (b.getId() != null) {
            buf.append("\"" + b.getId() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getId() != null) {
            buf.append("\"" + b.getHolding().getId() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getStandort().getId() != null) {
            buf.append("\"" + b.getStandort().getId() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getStandort().getInhalt() != null) {
            buf.append("\"" + b.getStandort().getInhalt() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getShelfmark() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getShelfmark()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getTitel() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getTitel()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getCoden() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getCoden()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getVerlag() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getVerlag()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getOrt() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getOrt()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getIssn() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getIssn()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getHolding().getZdbid() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getHolding().getZdbid()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getStartyear() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getStartyear()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getStartvolume() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getStartvolume()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getStartissue() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getStartissue()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getEndyear() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getEndyear()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getEndvolume() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getEndvolume()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (b.getEndissue() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getEndissue()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        buf.append("\"" + b.getSuppl() + "\"");
        buf.append(delimiter);
        if (b.getBemerkungen() != null) {
            buf.append("\"" + removeSpecialCharacters(b.getBemerkungen()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        buf.append("\"" + b.isEissue() + "\"");
        buf.append(delimiter);
        buf.append('"');
        buf.append(b.isInternal());
        buf.append("\"\n");

        return buf.toString();
    }

    private StringBuffer initStringBuffer(final char delimiter) {

        final StringBuffer buf = new StringBuffer(251);

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
        buf.append("\"internal\"\n");

        return buf;
    }

    private String removeSpecialCharacters(String str) {

        if (str != null) {
            str = str.replaceAll("\012", "\040").trim();
        }

        return str;
    }


}
