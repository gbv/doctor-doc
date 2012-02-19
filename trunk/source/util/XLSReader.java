//  Copyright (C) 2012  Markus Fischer
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

package util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.grlea.log.SimpleLogger;

/**
 * Class using the event API for reading a XLS-file.
 */
public class XLSReader implements HSSFListener {
    private SSTRecord sstrec;
    private List<List<String>> result = new ArrayList<List<String>>();
    private ArrayList<String> rowcontent = new ArrayList<String>();
    private int lastrow = -1; // rows start by 0
    private int lastcol = -1; // columns start by 0

    private static final SimpleLogger LOG = new SimpleLogger(XLSReader.class);

    /**
     * This method listens for incoming records and handles them as required.
     * 
     * @param record
     */
    public void processRecord(final Record record) {
        switch (record.getSid()) {

        // the BOFRecord can represent either the beginning of a sheet or the workbook
        case BOFRecord.sid:
            final BOFRecord bof = (BOFRecord) record;
            if (bof.getType() == BOFRecord.TYPE_WORKBOOK) {
                LOG.ludicrous("Encountered workbook");
                // assigned to the class level member
            } else if (bof.getType() == BOFRecord.TYPE_WORKSHEET) {
                LOG.ludicrous("Encountered sheet reference");
            }
            break;
        case BoundSheetRecord.sid:
            final BoundSheetRecord bsr = (BoundSheetRecord) record;
            LOG.ludicrous("New sheet named: " + bsr.getSheetname());
            break;
        case RowRecord.sid:
            final RowRecord rowrec = (RowRecord) record;
            // counts for all rows
            LOG.ludicrous("Row at " + rowrec.getRowNumber() + " found, first column at " + rowrec.getFirstCol()
                    + " last column at " + rowrec.getLastCol() + ". Total number of columns: " + rowrec.getRecordSize());
            break;
        case NumberRecord.sid:
            // cells with numbers
            final NumberRecord numrec = (NumberRecord) record;
            if (getLastrow() < numrec.getRow()) { // start of a new record / row
                if (numrec.getRow() > 0) { // put the old record / row into result
                    // make a copy, because the reference will be reused
                    final ArrayList<String> copy = new ArrayList<String>();
                    setLastcol(-1); // reset column count
                    copy.addAll(rowcontent);
                    result.add(copy);
                }
                rowcontent = new ArrayList<String>();
            }

            // set current row number
            setLastrow(numrec.getRow());

            // add empty cell contents for the missing cells
            addEmptyCells(numrec.getColumn() - getLastcol());

            // set current column number
            setLastcol(numrec.getColumn());

            // add cell content into rowcontent...
            if (numrec.getColumn() == 3 || numrec.getColumn() == 4) {
                //... with possible float point numbers for Location Name (3) and Shelfmark (4)...
                rowcontent.add(String.valueOf(numrec.getValue()));
            } else {
                //.. and without float point numbers for all other cells...
                rowcontent.add(String.valueOf(new Double(numrec.getValue()).longValue()));
            }

            break;
        // SSTRecords store a array of unique strings used in Excel.
        case SSTRecord.sid:
            sstrec = (SSTRecord) record;
            for (int k = 0; k < sstrec.getNumUniqueStrings(); k++) {
                LOG.ludicrous("String table value " + k + " = " + sstrec.getString(k));
            }
            break;
        case LabelSSTRecord.sid:
            // cells with strings
            final LabelSSTRecord lrec = (LabelSSTRecord) record;
            if (getLastrow() < lrec.getRow()) { // start of a new record / row
                if (lrec.getRow() > 0) { // put the old record / row into result
                    // make a copy, because the reference will be reused
                    final ArrayList<String> copy = new ArrayList<String>();
                    setLastcol(-1); // reset column count
                    copy.addAll(rowcontent);
                    result.add(copy);
                }
                rowcontent = new ArrayList<String>();
            }

            // set current row number
            setLastrow(lrec.getRow());

            // add empty cell contents for the missing cells
            addEmptyCells(lrec.getColumn() - getLastcol());

            // set current column number
            setLastcol(lrec.getColumn());

            // add cell content into rowcontent
            rowcontent.add(sstrec.getString(lrec.getSSTIndex()).toString());

            break;
        }
    }

    /**
     * Read an excel file and return found cell elements.
     * 
     * @param aBufferedInputStream
     * @throws IOException
     * @return List<List<String>>
     */
    public List<List<String>> read(final BufferedInputStream ins) throws IOException {
        // create a new org.apache.poi.poifs.filesystem.Filesystem
        final POIFSFileSystem poifs = new POIFSFileSystem(ins);
        // get the Workbook (excel part) stream in a InputStream
        final InputStream din = poifs.createDocumentInputStream("Workbook");
        // construct out HSSFRequest object
        final HSSFRequest req = new HSSFRequest();
        // lazy listen for ALL records with the listener shown above
        req.addListenerForAllRecords(this);
        // create our event factory
        final HSSFEventFactory factory = new HSSFEventFactory();
        // process our events based on the document input stream
        factory.processEvents(req, din);

        // add last row into result
        if (!rowcontent.isEmpty()) {
            result.add(rowcontent);
        }

        // and our document input stream (don't want to leak these!)
        try {
            din.close();
        } catch (final Exception e) {
            LOG.error(e.toString());
        }

        return getResult();

    }

    private void addEmptyCells(final int celldiff) {

        // only add if celldiff < 1
        for (int i = 1; i < celldiff; i++) {
            rowcontent.add("");
        }
    }

    public List<List<String>> getResult() {
        return result;
    }

    public void setResult(final List<List<String>> result) {
        this.result = result;
    }

    public int getLastrow() {
        return lastrow;
    }

    public void setLastrow(final int lastrow) {
        this.lastrow = lastrow;
    }

    public int getLastcol() {
        return lastcol;
    }

    public void setLastcol(final int lastcol) {
        this.lastcol = lastcol;
    }

}
