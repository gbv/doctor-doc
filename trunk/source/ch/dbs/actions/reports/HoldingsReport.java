//  Copyright (C) 2005 - 2012  Markus Fischer, Pascal Steiner
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
 */
public final class HoldingsReport extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(HoldingsReport.class);

    /**
     * Gets all holdings of a given library and creates an Export-File
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = "failure";
        final Auth auth = new Auth();

        // Get export filetype for export as either CSV with semicolon delimiter, TXT as tab delimited file or as XLS
        final String filetype = rq.getParameter("filetype");
        String contenttype = "text/txt;charset=UTF-8"; // used for CSV and TXT

        // Check if the user is logged in and is librarian or admin
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users

                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                final Date date = new Date();

                try {
                    // Compose filename with date and time
                    final StringBuffer filename = new StringBuffer("holdings-");
                    filename.append(tf.format(date, ui.getKonto().getTimezone())); // append date and time
                    filename.append('.');

                    // CSV- or TXT-Export
                    if (filetype != null && (filetype.equals("csv") || filetype.equals("txt"))) {
                        char delimiter = ch.dbs.actions.bestand.Stock.getDelimiterCsv(); // ';' as default delimiter
                        if (filetype.equals("csv")) {
                            filename.append("csv");
                        } else {
                            filename.append("txt");
                            delimiter = ch.dbs.actions.bestand.Stock.getDelimiterTxt(); // tab as delimiter
                        }
                        // Prepare Output
                        rp.setContentType(contenttype); // Set ContentType in the response for the Browser
                        rp.addHeader("Content-Disposition", "attachment;filename=" + filename.toString()); // Set filename
                        rp.setCharacterEncoding("UTF-8");

                        rp.flushBuffer();

                        // Use writer to render text
                        PrintWriter pw = null;
                        try {
                            pw = rp.getWriter();
                            pw.write(getCSVContent(ui.getKonto(), delimiter));
                            pw.flush();
                        } finally {
                            pw.close();
                        }
                    } else { // Excel-Export
                        filename.append("xls");
                        contenttype = "application/vnd.ms-excel";
                        // Prepare Output
                        rp.setContentType(contenttype); // Set ContentType in the response for the Browser
                        rp.addHeader("Content-Disposition", "attachment;filename=" + filename.toString()); // Set filename

                        ServletOutputStream outputStream = null;
                        try {
                            outputStream = rp.getOutputStream();
                            // get WorkBook and write to output stream
                            getXLSContent(ui.getKonto()).write(outputStream);
                            // Flush the stream
                            outputStream.flush();
                        } finally {
                            outputStream.close();
                        }

                    }

                } catch (final IOException e) {
                    // Output failed
                    LOG.error("Failure in HoldingsReport.execute: " + e.toString());
                } catch (final Exception e) {
                    LOG.error("Failure in HoldingsReport.execute: " + e.toString());
                } finally {
                    forward = null;
                }

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    private String getCSVContent(final Konto k, final char delimiter) {
        final Text cn = new Text();
        // get a StringBuffer with a header describing the content of the fields
        final StringBuffer buf = initCSV(delimiter);

        try {
            // internal holdings are visible
            final List<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), true, cn.getConnection());

            for (final Bestand b : stock) {
                buf.append(getCSVLine(b, delimiter));
            }

        } finally {
            cn.close();
        }

        return buf.toString();
    }

    private HSSFWorkbook getXLSContent(final Konto k) {
        final Text cn = new Text();
        final Workbook wb = new HSSFWorkbook();
        final Sheet s = wb.createSheet();

        try {
            // add header for XLS
            initXLS(wb, s);

            // internal holdings are visible
            final List<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), true, cn.getConnection());

            short rownumber = 0;

            for (final Bestand b : stock) {
                rownumber++;
                // add holdings
                getXLSLine(wb, s, b, rownumber);
            }

            // adjust all columns in size 
            s.autoSizeColumn((short) 0);
            s.autoSizeColumn((short) 1);
            s.autoSizeColumn((short) 2);
            s.autoSizeColumn((short) 3);
            s.autoSizeColumn((short) 4);
            s.autoSizeColumn((short) 5);
            s.autoSizeColumn((short) 6);
            s.autoSizeColumn((short) 7);
            s.autoSizeColumn((short) 8);
            s.autoSizeColumn((short) 9);
            s.autoSizeColumn((short) 10);
            s.autoSizeColumn((short) 11);
            s.autoSizeColumn((short) 12);
            s.autoSizeColumn((short) 13);
            s.autoSizeColumn((short) 14);
            s.autoSizeColumn((short) 15);
            s.autoSizeColumn((short) 16);
            s.autoSizeColumn((short) 17);
            s.autoSizeColumn((short) 18);
            s.autoSizeColumn((short) 19);
            s.autoSizeColumn((short) 19);

        } finally {
            cn.close();
        }

        return (HSSFWorkbook) wb;
    }

    private String getCSVLine(final Bestand b, final char delimiter) {

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

    private void getXLSLine(final Workbook wb, final Sheet s, final Bestand b, final short rownumber) {

        final Row row = s.createRow(rownumber);

        if (b.getId() != null) {
            row.createCell((short) 0).setCellValue(b.getId().toString());
        } else {
            row.createCell((short) 0).setCellValue("");
        }
        if (b.getHolding().getId() != null) {
            row.createCell((short) 1).setCellValue(b.getHolding().getId().toString());
        } else {
            row.createCell((short) 1).setCellValue("");
        }
        if (b.getStandort().getId() != null) {
            row.createCell((short) 2).setCellValue(b.getStandort().getId().toString());
        } else {
            row.createCell((short) 2).setCellValue("");
        }
        if (b.getStandort().getInhalt() != null) {
            row.createCell((short) 3).setCellValue(b.getStandort().getInhalt());
        } else {
            row.createCell((short) 3).setCellValue("");
        }
        if (b.getShelfmark() != null) {
            row.createCell((short) 4).setCellValue(removeSpecialCharacters(b.getShelfmark()));
        } else {
            row.createCell((short) 4).setCellValue("");
        }
        if (b.getHolding().getTitel() != null) {
            row.createCell((short) 5).setCellValue(removeSpecialCharacters(b.getHolding().getTitel()));
        } else {
            row.createCell((short) 5).setCellValue("");
        }
        if (b.getHolding().getCoden() != null) {
            row.createCell((short) 6).setCellValue(removeSpecialCharacters(b.getHolding().getCoden()));
        } else {
            row.createCell((short) 6).setCellValue("");
        }
        if (b.getHolding().getVerlag() != null) {
            row.createCell((short) 7).setCellValue(removeSpecialCharacters(b.getHolding().getVerlag()));
        } else {
            row.createCell((short) 7).setCellValue("");
        }
        if (b.getHolding().getOrt() != null) {
            row.createCell((short) 8).setCellValue(removeSpecialCharacters(b.getHolding().getOrt()));
        } else {
            row.createCell((short) 8).setCellValue("");
        }
        if (b.getHolding().getIssn() != null) {
            row.createCell((short) 9).setCellValue(removeSpecialCharacters(b.getHolding().getIssn()));
        } else {
            row.createCell((short) 9).setCellValue("");
        }
        if (b.getHolding().getZdbid() != null) {
            row.createCell((short) 10).setCellValue(removeSpecialCharacters(b.getHolding().getZdbid()));
        } else {
            row.createCell((short) 10).setCellValue("");
        }
        if (b.getStartyear() != null) {
            row.createCell((short) 11).setCellValue(removeSpecialCharacters(b.getStartyear()));
        } else {
            row.createCell((short) 11).setCellValue("");
        }
        if (b.getStartvolume() != null) {
            row.createCell((short) 12).setCellValue(removeSpecialCharacters(b.getStartvolume()));
        } else {
            row.createCell((short) 12).setCellValue("");
        }
        if (b.getStartissue() != null) {
            row.createCell((short) 13).setCellValue(removeSpecialCharacters(b.getStartissue()));
        } else {
            row.createCell((short) 13).setCellValue("");
        }
        if (b.getEndyear() != null) {
            row.createCell((short) 14).setCellValue(removeSpecialCharacters(b.getEndyear()));
        } else {
            row.createCell((short) 14).setCellValue("");
        }
        if (b.getEndvolume() != null) {
            row.createCell((short) 15).setCellValue(removeSpecialCharacters(b.getEndvolume()));
        } else {
            row.createCell((short) 15).setCellValue("");
        }
        if (b.getEndissue() != null) {
            row.createCell((short) 16).setCellValue(removeSpecialCharacters(b.getEndissue()));
        } else {
            row.createCell((short) 16).setCellValue("");
        }
        row.createCell((short) 17).setCellValue(String.valueOf(b.getSuppl()));
        if (b.getBemerkungen() != null) {
            row.createCell((short) 18).setCellValue(removeSpecialCharacters(b.getBemerkungen()));
        } else {
            row.createCell((short) 18).setCellValue("");
        }
        row.createCell((short) 19).setCellValue(String.valueOf(b.isEissue()));
        row.createCell((short) 20).setCellValue(String.valueOf(b.isInternal()));

    }

    private StringBuffer initCSV(final char delimiter) {

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

    private void initXLS(final Workbook wb, final Sheet s) {

        final Row rowhead = s.createRow((short) 0);
        rowhead.createCell((short) 0).setCellValue("Stock ID");
        rowhead.createCell((short) 1).setCellValue("Holding ID");
        rowhead.createCell((short) 2).setCellValue("Location ID");
        rowhead.createCell((short) 3).setCellValue("Location Name");
        rowhead.createCell((short) 4).setCellValue("Shelfmark");
        rowhead.createCell((short) 5).setCellValue("Title");
        rowhead.createCell((short) 6).setCellValue("Coden");
        rowhead.createCell((short) 7).setCellValue("Publisher");
        rowhead.createCell((short) 8).setCellValue("Place");
        rowhead.createCell((short) 9).setCellValue("ISSN");
        rowhead.createCell((short) 10).setCellValue("ZDB-ID");
        rowhead.createCell((short) 11).setCellValue("Startyear");
        rowhead.createCell((short) 12).setCellValue("Startvolume");
        rowhead.createCell((short) 13).setCellValue("Startissue");
        rowhead.createCell((short) 14).setCellValue("Endyear");
        rowhead.createCell((short) 15).setCellValue("Endvolume");
        rowhead.createCell((short) 16).setCellValue("Endissue");
        rowhead.createCell((short) 17).setCellValue("Suppl");
        rowhead.createCell((short) 18).setCellValue("remarks");
        rowhead.createCell((short) 19).setCellValue("eissue");
        rowhead.createCell((short) 20).setCellValue("internal");

    }

    private String removeSpecialCharacters(String str) {

        if (str != null) {
            str = str.replaceAll("\012", "\040").trim();
        }

        return str;
    }

}
