//  Copyright (C) 2014  Markus Fischer
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.Auth;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Text;
import ch.dbs.form.KbartForm;
import ch.dbs.form.UserInfo;
import enums.Result;

/**
 * Creates a KBART export of the holdings of a given library.
 * 
 * @author Markus Fischer
 */
public final class KbartReport extends DispatchAction {

    private static final Logger LOG = LoggerFactory.getLogger(KbartReport.class);

    /**
     * Gets all holdings of a given library and creates an Export-File
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }

        // Get export filetype for export as either CSV with semicolon delimiter, TXT as tab delimited file or as XLS
        final String filetype = rq.getParameter("filetype");
        String contenttype = "text/txt;charset=UTF-8"; // used for CSV and TXT

        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        final ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();

        try {
            // Compose filename with date and time

            final StringBuffer filename = new StringBuffer(ReadSystemConfigurations.getApplicationName().toUpperCase());
            filename.append("_AllTitles_");
            filename.append(tf.format(date, ui.getKonto().getTimezone())); // append date and time
            filename.append('.');

            // CSV- or TXT-Export
            if (filetype != null && (filetype.equals("csv") || filetype.equals("txt"))) {
                char delimiter = ch.dbs.actions.bestand.Stock.getDelimiterCsv(); // ';' as default delimiter
                if ("csv".equals(filetype)) {
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
                    // csv uses " to enclose values
                    if (filetype.equals("csv")) {
                        pw.write(getCSVContent(ui.getKonto(), delimiter));
                    } else {
                        // KBART in txt does not use " to enclose values
                        pw.write(getTXTContent(ui.getKonto(), delimiter));
                    }
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
            LOG.error("Failure in KbartReport.execute: " + e.toString());
        } catch (final Exception e) {
            LOG.error("Failure in KbartReport.execute: " + e.toString());
        }

        return mp.findForward(null);
    }

    private String getCSVContent(final Konto k, final char delimiter) {
        final Text cn = new Text();
        // get a StringBuffer with a header describing the content of the fields
        final StringBuffer buf = initCSV(delimiter);

        try {
            // internal holdings are visible
            final List<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), true, cn.getConnection());

            // transform to KbartForm
            final KbartForm kbf = new KbartForm();
            final List<KbartForm> kbarts = kbf.createKbartForms(stock, cn.getConnection());

            for (final KbartForm kbart : kbarts) {
                buf.append(getCSVLine(kbart, delimiter));
            }

        } finally {
            cn.close();
        }

        return buf.toString();
    }

    private String getTXTContent(final Konto k, final char delimiter) {
        final Text cn = new Text();
        // get a StringBuffer with a header describing the content of the fields
        final StringBuffer buf = initTXT(delimiter);

        try {
            // internal holdings are visible
            final List<Bestand> stock = new Bestand().getAllKontoBestand(k.getId(), true, cn.getConnection());

            // transform to KbartForm
            final KbartForm kbf = new KbartForm();
            final List<KbartForm> kbarts = kbf.createKbartForms(stock, cn.getConnection());

            for (final KbartForm kbart : kbarts) {
                buf.append(getTXTLine(kbart, delimiter));
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

            // transform to KbartForm
            final KbartForm kbf = new KbartForm();
            final List<KbartForm> kbarts = kbf.createKbartForms(stock, cn.getConnection());

            short rowNr = 0;

            for (final KbartForm kbart : kbarts) {
                rowNr++;
                // add holdings
                getXLSLine(wb, s, kbart, rowNr);
            }

            // adjust all columns in size
            short columnNr = 0;
            while (columnNr < 16) {
                s.autoSizeColumn(columnNr);
                columnNr++;
            }

        } finally {
            cn.close();
        }

        return (HSSFWorkbook) wb;
    }

    private String getCSVLine(final KbartForm kbart, final char delimiter) {

        final StringBuffer buf = new StringBuffer(336);

        if (kbart.getPublication_title() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getPublication_title()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getPrint_identifier() != null) {
            buf.append("\"" + kbart.getPrint_identifier() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getOnline_identifier() != null) {
            buf.append("\"" + kbart.getOnline_identifier() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getDate_first_issue_online() != null) {
            buf.append("\"" + kbart.getDate_first_issue_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getNum_first_vol_online() != null) {
            buf.append("\"" + kbart.getNum_first_vol_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getNum_first_issue_online() != null) {
            buf.append("\"" + kbart.getNum_first_issue_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getDate_last_issue_online() != null) {
            buf.append("\"" + kbart.getDate_last_issue_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getNum_last_vol_online() != null) {
            buf.append("\"" + kbart.getNum_last_vol_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getNum_last_issue_online() != null) {
            buf.append("\"" + kbart.getNum_last_issue_online() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getTitle_url() != null) {
            buf.append("\"" + kbart.getTitle_url() + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getFirst_author() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getFirst_author()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getTitle_id() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getTitle_id()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getEmbargo_info() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getEmbargo_info()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getCoverage_depth() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getCoverage_depth()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getCoverage_notes() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getCoverage_notes()) + "\"");
        } else {
            buf.append("\"\"");
        }
        buf.append(delimiter);
        if (kbart.getPublisher_name() != null) {
            buf.append("\"" + removeSpecialCharacters(kbart.getPublisher_name()) + "\"\n");
        } else {
            buf.append("\"\"\n");
        }

        return buf.toString();
    }

    private String getTXTLine(final KbartForm kbart, final char delimiter) {

        final StringBuffer buf = new StringBuffer(336);

        if (kbart.getPublication_title() != null) {
            buf.append(removeSpecialCharacters(kbart.getPublication_title()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getPrint_identifier() != null) {
            buf.append(kbart.getPrint_identifier());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getOnline_identifier() != null) {
            buf.append(kbart.getOnline_identifier());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getDate_first_issue_online() != null) {
            buf.append(kbart.getDate_first_issue_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getNum_first_vol_online() != null) {
            buf.append(kbart.getNum_first_vol_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getNum_first_issue_online() != null) {
            buf.append(kbart.getNum_first_issue_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getDate_last_issue_online() != null) {
            buf.append(kbart.getDate_last_issue_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getNum_last_vol_online() != null) {
            buf.append(kbart.getNum_last_vol_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getNum_last_issue_online() != null) {
            buf.append(kbart.getNum_last_issue_online());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getTitle_url() != null) {
            buf.append(kbart.getTitle_url());
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getFirst_author() != null) {
            buf.append(removeSpecialCharacters(kbart.getFirst_author()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getTitle_id() != null) {
            buf.append(removeSpecialCharacters(kbart.getTitle_id()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getEmbargo_info() != null) {
            buf.append(removeSpecialCharacters(kbart.getEmbargo_info()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getCoverage_depth() != null) {
            buf.append(removeSpecialCharacters(kbart.getCoverage_depth()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getCoverage_notes() != null) {
            buf.append(removeSpecialCharacters(kbart.getCoverage_notes()));
        } else {
            buf.append("");
        }
        buf.append(delimiter);
        if (kbart.getPublisher_name() != null) {
            buf.append(removeSpecialCharacters(kbart.getPublisher_name()) + "\n");
        } else {
            buf.append('\n');
        }

        return buf.toString();
    }

    private void getXLSLine(final Workbook wb, final Sheet s, final KbartForm kbart, final short rownumber) {

        final Row row = s.createRow(rownumber);

        if (kbart.getPublication_title() != null) {
            row.createCell((short) 0).setCellValue(removeSpecialCharacters(kbart.getPublication_title()));
        } else {
            row.createCell((short) 0).setCellValue("");
        }
        if (kbart.getPrint_identifier() != null) {
            row.createCell((short) 1).setCellValue(kbart.getPrint_identifier());
        } else {
            row.createCell((short) 1).setCellValue("");
        }
        if (kbart.getOnline_identifier() != null) {
            row.createCell((short) 2).setCellValue(kbart.getOnline_identifier());
        } else {
            row.createCell((short) 2).setCellValue("");
        }
        if (kbart.getDate_first_issue_online() != null) {
            row.createCell((short) 3).setCellValue(kbart.getDate_first_issue_online());
        } else {
            row.createCell((short) 3).setCellValue("");
        }
        if (kbart.getNum_first_vol_online() != null) {
            row.createCell((short) 4).setCellValue(kbart.getNum_first_vol_online());
        } else {
            row.createCell((short) 4).setCellValue("");
        }
        if (kbart.getNum_first_issue_online() != null) {
            row.createCell((short) 5).setCellValue(kbart.getNum_first_issue_online());
        } else {
            row.createCell((short) 5).setCellValue("");
        }
        if (kbart.getDate_last_issue_online() != null) {
            row.createCell((short) 6).setCellValue(kbart.getDate_last_issue_online());
        } else {
            row.createCell((short) 6).setCellValue("");
        }
        if (kbart.getNum_last_vol_online() != null) {
            row.createCell((short) 7).setCellValue(kbart.getNum_last_vol_online());
        } else {
            row.createCell((short) 7).setCellValue("");
        }
        if (kbart.getNum_last_issue_online() != null) {
            row.createCell((short) 8).setCellValue(kbart.getNum_last_issue_online());
        } else {
            row.createCell((short) 8).setCellValue("");
        }
        if (kbart.getTitle_url() != null) {
            row.createCell((short) 9).setCellValue(kbart.getTitle_url());
        } else {
            row.createCell((short) 9).setCellValue("");
        }
        if (kbart.getFirst_author() != null) {
            row.createCell((short) 10).setCellValue(removeSpecialCharacters(kbart.getFirst_author()));
        } else {
            row.createCell((short) 10).setCellValue("");
        }
        if (kbart.getTitle_id() != null) {
            row.createCell((short) 11).setCellValue(removeSpecialCharacters(kbart.getTitle_id()));
        } else {
            row.createCell((short) 11).setCellValue("");
        }
        if (kbart.getEmbargo_info() != null) {
            row.createCell((short) 12).setCellValue(removeSpecialCharacters(kbart.getEmbargo_info()));
        } else {
            row.createCell((short) 12).setCellValue("");
        }
        if (kbart.getCoverage_depth() != null) {
            row.createCell((short) 13).setCellValue(removeSpecialCharacters(kbart.getCoverage_depth()));
        } else {
            row.createCell((short) 13).setCellValue("");
        }
        if (kbart.getCoverage_notes() != null) {
            row.createCell((short) 14).setCellValue(removeSpecialCharacters(kbart.getCoverage_notes()));
        } else {
            row.createCell((short) 14).setCellValue("");
        }
        if (kbart.getPublisher_name() != null) {
            row.createCell((short) 15).setCellValue(removeSpecialCharacters(kbart.getPublisher_name()));
        } else {
            row.createCell((short) 15).setCellValue("");
        }

    }

    private StringBuffer initCSV(final char delimiter) {

        final StringBuffer buf = new StringBuffer(251);

        buf.append("\"publication_title\"");
        buf.append(delimiter);
        buf.append("\"print _identifier\"");
        buf.append(delimiter);
        buf.append("\"online_identifier\"");
        buf.append(delimiter);
        buf.append("\"date_first_issue_online\"");
        buf.append(delimiter);
        buf.append("\"num_first_vol_online\"");
        buf.append(delimiter);
        buf.append("\"num_first_issue_online\"");
        buf.append(delimiter);
        buf.append("\"date_last_issue_online\"");
        buf.append(delimiter);
        buf.append("\"num_last_vol_online\"");
        buf.append(delimiter);
        buf.append("\"num_last_issue_online\"");
        buf.append(delimiter);
        buf.append("\"title_url\"");
        buf.append(delimiter);
        buf.append("\"first_author\"");
        buf.append(delimiter);
        buf.append("\"title_id\"");
        buf.append(delimiter);
        buf.append("\"embargo_info\"");
        buf.append(delimiter);
        buf.append("\"coverage_depth\"");
        buf.append(delimiter);
        buf.append("\"coverage_notes\"");
        buf.append(delimiter);
        buf.append("\"publisher_name\"\n");

        return buf;
    }

    private StringBuffer initTXT(final char delimiter) {

        final StringBuffer buf = new StringBuffer(251);

        buf.append("publication_title");
        buf.append(delimiter);
        buf.append("print _identifier");
        buf.append(delimiter);
        buf.append("online_identifier");
        buf.append(delimiter);
        buf.append("date_first_issue_online");
        buf.append(delimiter);
        buf.append("num_first_vol_online");
        buf.append(delimiter);
        buf.append("num_first_issue_online");
        buf.append(delimiter);
        buf.append("date_last_issue_online");
        buf.append(delimiter);
        buf.append("num_last_vol_online");
        buf.append(delimiter);
        buf.append("num_last_issue_online");
        buf.append(delimiter);
        buf.append("title_url");
        buf.append(delimiter);
        buf.append("first_author");
        buf.append(delimiter);
        buf.append("title_id");
        buf.append(delimiter);
        buf.append("embargo_info");
        buf.append(delimiter);
        buf.append("coverage_depth");
        buf.append(delimiter);
        buf.append("coverage_notes");
        buf.append(delimiter);
        buf.append("publisher_name\n");

        return buf;
    }

    private void initXLS(final Workbook wb, final Sheet s) {

        final Row rowhead = s.createRow((short) 0);
        rowhead.createCell((short) 0).setCellValue("publication_title");
        rowhead.createCell((short) 1).setCellValue("print _identifier");
        rowhead.createCell((short) 2).setCellValue("online_identifier");
        rowhead.createCell((short) 3).setCellValue("date_first_issue_online");
        rowhead.createCell((short) 4).setCellValue("num_first_vol_online");
        rowhead.createCell((short) 5).setCellValue("num_first_issue_online");
        rowhead.createCell((short) 6).setCellValue("date_last_issue_online");
        rowhead.createCell((short) 7).setCellValue("num_last_vol_online");
        rowhead.createCell((short) 8).setCellValue("num_last_issue_online");
        rowhead.createCell((short) 9).setCellValue("title_url");
        rowhead.createCell((short) 10).setCellValue("first_author");
        rowhead.createCell((short) 11).setCellValue("title_id");
        rowhead.createCell((short) 12).setCellValue("embargo_info");
        rowhead.createCell((short) 13).setCellValue("coverage_depth");
        rowhead.createCell((short) 14).setCellValue("coverage_notes");
        rowhead.createCell((short) 15).setCellValue("publisher_name");

    }

    private String removeSpecialCharacters(String str) {

        if (str != null) {
            str = str.replaceAll("\012", "\040").trim();
        }

        return str;
    }

}
