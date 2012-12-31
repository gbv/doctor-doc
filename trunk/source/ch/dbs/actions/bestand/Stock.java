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

package ch.dbs.actions.bestand;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.CSV;
import util.Check;
import util.XLSReader;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Holding;
import ch.dbs.entity.Issn;
import ch.dbs.entity.Text;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.FileForm;
import ch.dbs.form.HoldingForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import enums.Result;
import enums.TextType;

/**
 * Class to manage holdings information. Import / Export functions.
 * 
 * @author Markus Fischer
 */
public class Stock extends DispatchAction {
    
    private static final SimpleLogger LOG = new SimpleLogger(Stock.class);
    private static final int COLUMNS = 21; // Number of columns per import line
    private static final int FILESIZELIMIT = 500000; // ca. 500 KB. Limits the file size for upload to avoid OutOfMemory errors
    private static final char DELIMITER_CSV = ';'; // Delimiter for CSV-Export
    
    //Delimiter for Tab delimited txt-Export (Excel can't read UTF-8 in CSV...)
    private static final char DELIMITER_TXT = '\t';
    
    /**
     * Access control for the holdings export page.
     */
    public ActionForward prepareExport(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
            forward = Result.SUCCESS.getValue();
            
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu(Result.LOGIN.getValue());
            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
            final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
            rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Access control for the holdings import page.
     */
    public ActionForward prepareImport(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final Text t = new Text();
        
        try {
            
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                forward = Result.SUCCESS.getValue();
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final HoldingForm hf = (HoldingForm) fm;
                
                hf.setStandorte(t.getAllKontoText(TextType.LOCATION, ui.getKonto().getId(), t.getConnection()));
                
                rq.setAttribute("holdingform", hf);
                
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(Result.LOGIN.getValue());
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }
            
        } finally {
            t.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Import a file with holdings information.
     */
    public ActionForward importHoldings(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        
        // Prepare classes
        String forward = Result.FAILURE.getValue();
        final Check ck = new Check();
        final Text cn = new Text();
        final FileForm fileForm = (FileForm) fm;
        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        
        try {
            // get uploaded File
            final FormFile upload = fileForm.getFile();
            final String fileName = upload.getFileName();
            
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                if (fileForm.isCondition()) { // conditions for upload must be accepted
                    // must be tab delimited or csv file
                    if (ck.isFiletypeExtension(fileName, ".txt") || ck.isFiletypeExtension(fileName, ".csv")
                            || ck.isFiletypeExtension(fileName, ".xls") || ck.isFiletypeExtension(fileName, ".xlsx")) {
                        if (upload.getFileSize() < FILESIZELIMIT) { // limit file size to avoid OutOfMemory errors
                        
                            // Excel is able to read UTF-8 encoded tab delimited files. But after editing the file,
                            // it will save the file as ANSI CP1250. There is no option to save the file as UTF-8.
                            // So we have to switch encoding if there has been uploaded a TXT file.
                            String encoding = "UTF-8";
                            char delimiter = DELIMITER_CSV; // default value
                            if (ck.isFiletypeExtension(fileName, ".txt")) {
                                delimiter = DELIMITER_TXT; // tab delimited file
                                encoding = "CP1250"; // Windows ANSI encoding...
                            }
                            
                            List<List<String>> stockList = new ArrayList<List<String>>();
                            List<Message> messageList = new ArrayList<Message>();
                            
                            try {
                                // Get an List<List<String>> representation of the file
                                if (ck.isFiletypeExtension(fileName, ".txt")
                                        || ck.isFiletypeExtension(fileName, ".csv")) {
                                    stockList = readCSVImport(upload, delimiter, encoding);
                                } else if (ck.isFiletypeExtension(fileName, ".xls")) {
                                    stockList = readXLSImport(upload);
                                } else {
                                    // TODO: add support for XLSX
                                    final Message msg = new Message("error.import.failed",
                                            "Filetype XLSX not supported! Use XLS...", "");
                                    messageList.add(msg);
                                }
                            } catch (final Exception e) {
                                final Message msg = new Message("error.import.failed", e.toString(), "");
                                messageList.add(msg);
                            }
                            
                            // Check for errors reading file
                            if (messageList.isEmpty()) {
                                
                                // Check if the file contains the correct number of columns
                                messageList = checkColumns(stockList);
                                if (messageList.isEmpty()) {
                                    
                                    // Basic checks and make sure all entries in stockList are parsable
                                    messageList = checkBasicParsability(stockList);
                                    if (messageList.isEmpty()) {
                                        
                                        // Convert to ArrayList<Bestand>
                                        final List<Bestand> bestandList = convertToBestand(stockList, ui);
                                        
                                        // Check integrity of Bestand()
                                        messageList = checkBestandIntegrity(bestandList, ui, cn.getConnection());
                                        if (messageList.isEmpty()) {
                                            
                                            // save or update holdings, delete all other holdings
                                            final String successMessage = update(bestandList, ui, cn.getConnection());
                                            
                                            // TODO: check DAIA-ID
                                            
                                            forward = Result.SUCCESS.getValue();
                                            
                                            final Message msg = new Message("import.success", successMessage,
                                                    "allstock.do?method=prepareExport&activemenu=stock");
                                            
                                            rq.setAttribute("message", msg);
                                            
                                        } else { // detailed errors while checking integrity of Bestand() objects
                                            forward = "importError";
                                            final ActiveMenusForm mf = new ActiveMenusForm();
                                            mf.setActivemenu("stock");
                                            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                                            rq.setAttribute("messageList", messageList);
                                            final Message em = new Message("error.import.heading",
                                                    "stock.do?method=prepareImport&activemenu=stock");
                                            rq.setAttribute("singleMessage", em);
                                        }
                                    } else { // basic errors before parsing to Bestand() objects
                                        forward = "importError";
                                        final ActiveMenusForm mf = new ActiveMenusForm();
                                        mf.setActivemenu("stock");
                                        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                                        rq.setAttribute("messageList", messageList);
                                        final Message em = new Message("error.import.heading",
                                                "stock.do?method=prepareImport&activemenu=stock");
                                        rq.setAttribute("singleMessage", em);
                                    }
                                } else { // Wrong number of columns
                                    forward = "importError";
                                    final ActiveMenusForm mf = new ActiveMenusForm();
                                    mf.setActivemenu("stock");
                                    rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                                    rq.setAttribute("messageList", messageList);
                                    final Message em = new Message("error.import.heading",
                                            "stock.do?method=prepareImport&activemenu=stock");
                                    rq.setAttribute("singleMessage", em);
                                }
                            } else { // Error reading file
                                forward = "importError";
                                final ActiveMenusForm mf = new ActiveMenusForm();
                                mf.setActivemenu("stock");
                                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                                rq.setAttribute("messageList", messageList);
                                final Message em = new Message("error.import.heading",
                                        "stock.do?method=prepareImport&activemenu=stock");
                                rq.setAttribute("singleMessage", em);
                            }
                        } else { // Filesize limit
                            final ActiveMenusForm mf = new ActiveMenusForm();
                            mf.setActivemenu("stock");
                            rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                            final ErrorMessage em = new ErrorMessage("error.filesize",
                                    "stock.do?method=prepareImport&activemenu=stock");
                            rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
                        }
                    } else { // Filetype error
                        final ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("stock");
                        rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                        final ErrorMessage em = new ErrorMessage("error.filetype",
                                "stock.do?method=prepareImport&activemenu=stock");
                        rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
                    }
                } else { // Did not accept conditions
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("stock");
                    rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                    final ErrorMessage em = new ErrorMessage("error.import.condition",
                            "stock.do?method=prepareImport&activemenu=stock");
                    rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
                }
            } else { // User is not allowed to access this function
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(Result.LOGIN.getValue());
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }
            
        } finally {
            cn.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Gets all holding locations for a given library.
     */
    public ActionForward listStockplaces(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final Text t = new Text();
        
        try {
            
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                forward = Result.SUCCESS.getValue();
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final HoldingForm hf = (HoldingForm) fm;
                
                hf.setStandorte(t.getAllKontoText(TextType.LOCATION, ui.getKonto().getId(), t.getConnection()));
                
                if (hf.getStandorte().size() == 0) { //
                    hf.setMessage("error.stockplacesmodify.nolocations");
                }
                
                rq.setAttribute("holdingform", hf);
                
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(Result.LOGIN.getValue());
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }
            
        } finally {
            t.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Prepares and changes a given holding location.
     */
    public ActionForward changeStockplace(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final Text t = new Text();
        
        try {
            
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final HoldingForm hf = (HoldingForm) fm;
                
                // Make sure the Text() and the location belongs to the given account
                final Text txt = new Text(t.getConnection(), hf.getStid(), ui.getKonto().getId(), TextType.LOCATION);
                if (txt.getId() != null) {
                    forward = Result.SUCCESS.getValue();
                    
                    if (!hf.isMod() && !hf.isDel()) { // Prepares for changing a location
                        hf.setMod(true); // flags a location to be changed for the JSP
                        final ArrayList<Text> sl = new ArrayList<Text>();
                        sl.add(txt);
                        hf.setStandorte(sl);
                    } else if (hf.isMod()) { // update the given Text() / location
                        txt.setInhalt(hf.getStandortid());
                        txt.updateText(t.getConnection(), txt);
                        hf.setMod(false); // back to the location list
                        hf.setStandorte(t.getAllKontoText(TextType.LOCATION, ui.getKonto().getId(), t.getConnection()));
                    } else if (hf.isDel()) { // delete the given Text() / location
                        // Check if there still exist holdings for this location
                        final Bestand bestand = new Bestand();
                        final ArrayList<Bestand> sl = new ArrayList<Bestand>(bestand.getAllBestandForStandortId(
                                txt.getId(), t.getConnection()));
                        if (sl == null || sl.isEmpty()) {
                            txt.deleteText(t.getConnection(), txt);
                            hf.setMod(false);
                            hf.setDel(false);
                        } else {
                            // there are still holdings for this location!
                            hf.setMessage("error.stockplacesmodify.notdeletable");
                        }
                        hf.setStandorte(t.getAllKontoText(TextType.LOCATION, ui.getKonto().getId(), t.getConnection()));
                    }
                    
                    rq.setAttribute("holdingform", hf);
                    
                } else { // URL-hacking
                    final ErrorMessage em = new ErrorMessage("error.hack", "login.do");
                    rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
                    LOG.info("changeStockplace: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
                
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(Result.LOGIN.getValue());
                rq.setAttribute(Result.ACTIVEMENUS.getValue(), mf);
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(Result.ERRORMESSAGE.getValue(), em);
            }
            
        } finally {
            t.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Gets a list of all holdings from all libraries.
     * 
     * @param OrderForm
     * @param boolean
     * @return list<Bestand>
     */
    public List<Bestand> checkGeneralStockAvailability(final OrderForm pageForm, final boolean internal) {
        
        List<Bestand> bestaende = new ArrayList<Bestand>();
        final Text cn = new Text();
        final Holding ho = new Holding();
        final Bestand be = new Bestand();
        
        try {
            // get Holdings from ISSN
            if (pageForm.getIssn() != null && !"".equals(pageForm.getIssn())) {
                
                final List<String> setIssn = getRelatedIssn(pageForm.getIssn(), cn.getConnection());
                final List<String> hoids = ho.getAllHOIDs(setIssn, cn.getConnection());
                bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn.getConnection());
                
                // get Holdings from title
            } else if (pageForm.getZeitschriftentitel() != null && !"".equals(pageForm.getZeitschriftentitel())) {
                
                final List<String> hoids = ho.getAllHOIDs(pageForm.getZeitschriftentitel(), cn.getConnection());
                bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn.getConnection());
            }
            
        } finally {
            cn.close();
        }
        
        return bestaende;
        
    }
    
    /**
     * Gets a list of all holdings for a given library from an IP.
     * 
     * @param OrderForm
     * @param Text
     * @param boolean
     * @param Connection
     * @return list<Bestand>
     */
    public List<Bestand> checkStockAvailabilityForIP(final OrderForm pageForm, final Text tip, final boolean internal,
            final Connection cn) {
        
        List<Bestand> bestaende = new ArrayList<Bestand>();
        
        final Bestand be = new Bestand();
        final Holding ho = new Holding();
        
        // Do only check if we have an account (Konto)
        if (tip.getKonto() != null && tip.getKonto().getId() != null) {
            
            // get Holdings from ISSN
            if (pageForm.getIssn() != null && !"".equals(pageForm.getIssn())) {
                
                final List<String> setIssn = getRelatedIssn(pageForm.getIssn(), cn);
                final List<String> hoids = ho.getAllHOIDsForKonto(setIssn, tip.getKonto().getId(), cn);
                bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn);
                
                // get Holdings from title
            } else if (pageForm.getZeitschriftentitel() != null && !"".equals(pageForm.getZeitschriftentitel())) {
                
                final List<String> hoids = ho.getAllHOIDsForKonto(pageForm.getZeitschriftentitel(), tip.getKonto()
                        .getId(), cn);
                bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn);
                
            }
        }
        
        return bestaende;
        
    }
    
    /**
     * Runs basic checks and makes sure that the ArrayList<Bestand> is parsable
     * to Bestand().
     * 
     * @param List <List<String>>
     * @return List<Message>
     */
    private List<Message> checkBasicParsability(final List<List<String>> stockList) {
        
        final List<Message> messageList = new ArrayList<Message>();
        
        final int max = stockList.size();
        for (int i = 1; i < max; i++) { // start at position 1, thus ignoring the header
            final List<String> importLines = stockList.get(i);
            int column = 0; // column number of element to check
            for (String line : importLines) {
                Message msg;
                column++;
                if (line == null) {
                    line = "";
                }
                switch (column) {
                    case 1: // Stock-ID
                        msg = checkStockID(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 2: // Holding-ID
                        msg = checkHoldingID(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 3: // Location-ID
                        msg = checkLocationID(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 4: // Location
                        break;
                    case 5: // Shelfmark
                        break;
                    case 6: // Title
                        msg = checkTitle(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 7: // Coden
                        break;
                    case 8: // Publisher
                        break;
                    case 9: // Place
                        break;
                    case 10: // ISSN
                        msg = checkISSN(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 11: // ZDB-ID
                        break;
                    case 12: // Startyear
                        msg = checkStartyear(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 13: // Startvolume
                        break;
                    case 14: // Startissue
                        break;
                    case 15: // Endyear
                        msg = checkEndyear(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 16: // Endvolume
                        break;
                    case 17: // Endissue
                        break;
                    case 18: // Supplement
                        msg = checkSuppl(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 19: // remarks
                        break;
                    case 20: // eissue
                        msg = checkBoolean(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    case 21: // internal
                        msg = checkBoolean(i + 1, line);
                        if (msg.getMessage() != null) {
                            messageList.add(msg);
                        }
                        break;
                    default:
                        LOG.error("Stock ckeck BasicParsability - Unpredicted switch case in default: " + column);
                }
            }
        }
        
        return messageList;
    }
    
    /**
     * Checks if the import file has the correct format, by counting the columns
     * per line.
     * 
     * @param List <List<String>>
     * @return List<Message>
     */
    private List<Message> checkColumns(final List<List<String>> stockList) {
        
        final List<Message> messageList = new ArrayList<Message>();
        int lineCount = 0;
        
        for (final List<String> importLine : stockList) {
            lineCount++; // Lines start at position 1
            if (importLine.size() != COLUMNS) {
                final Message msg = new Message("error.import.separators", composeSystemMessage(lineCount, ""), "");
                messageList.add(msg);
            }
        }
        
        return messageList;
    }
    
    /**
     * Checks that each Bestand() has all necessary entries and belongs to the
     * account uploading the file.
     * 
     * @param List <Bestand>
     * @param UserInfo
     * @param Connection
     * @return List<Message>
     */
    private List<Message> checkBestandIntegrity(final List<Bestand> bestandList, final UserInfo ui, final Connection cn) {
        
        final List<Message> messageList = new ArrayList<Message>();
        final HashSet<Long> uniqueSetStockID = new HashSet<Long>();
        int lineCount = 1; // the header of the ArrayList<Bestand> is already omitted
        
        for (final Bestand b : bestandList) {
            lineCount++;
            
            if (b.getId() != null) {
                
                // check for unique Stock-ID
                if (uniqueSetStockID.contains(b.getId())) {
                    final Message msg = new Message();
                    msg.setMessage("error.import.stid.notUnique");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getId().toString()));
                    messageList.add(msg);
                } else { // if not found add Stock-ID
                    uniqueSetStockID.add(b.getId());
                }
                
                // check if Bestand is from account
                // internal holdings are visible
                final Bestand control = new Bestand(b.getId(), true, cn); // get control Bestand from specified ID
                
                // check if KID from Bestand matches KID from ui
                if (control.getHolding() != null && control.getHolding().getKid().equals(ui.getKonto().getId())) {
                    
                    // check if Holding-ID from control matches Holding-ID specified
                    if (!control.getHolding().getId().equals(b.getHolding().getId())) {
                        final Message msg = new Message();
                        msg.setMessage("error.import.hoid.doesNotMatchStid");
                        msg.setSystemMessage(composeSystemMessage(lineCount, b.getHolding().getId().toString()));
                        messageList.add(msg);
                    }
                } else { // KID from Bestand does not match KID from ui!
                    final Message msg = new Message();
                    msg.setMessage("error.import.stid.notFromAccount");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getId().toString()));
                    messageList.add(msg);
                }
            }
            
            // check location-ID and location
            if (b.getStandort().getId() != null) {
                // check if location-ID is from account
                // get control location from specified ID
                final Text control = new Text(cn, b.getStandort().getId(), ui.getKonto().getId(), TextType.LOCATION);
                
                if (control.getId() == null) { // Location-ID does not belong to account
                    final Message msg = new Message();
                    msg.setMessage("error.import.locationid");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getId().toString()));
                    messageList.add(msg);
                    
                    // Location-ID belongs to account, but do the locations match?
                } else if (!b.getStandort().getInhalt().equals("")
                        && !b.getStandort().getInhalt().equals(control.getInhalt())) { // locations do not match...
                    final Message msg = new Message();
                    msg.setMessage("error.import.locationsDoNotMatch");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getId().toString() + "/"
                            + b.getStandort().getInhalt()));
                    messageList.add(msg);
                }
            } else if (b.getStandort().getInhalt().equals("")) { // check if location is present
                final Message msg = new Message();
                msg.setMessage("error.import.location");
                msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getInhalt()));
                messageList.add(msg);
            }
            
            // if there is an endvolume or and endissue => there must be an endyear
            if ((!"".equals(b.getEndvolume()) || !"".equals(b.getEndissue())) && "".equals(b.getEndyear())) {
                final Message msg = new Message();
                msg.setMessage("error.import.endyear");
                msg.setSystemMessage(composeSystemMessage(lineCount, ""));
                messageList.add(msg);
            }
            
        }
        
        return messageList;
    }
    
    /**
     * Converts an CSV/TXT import file into an ArrayList<List<String>> with all
     * the text line elements.
     * 
     * @param FormFile
     * @param char
     * @param String
     * @return List<List<String>>
     */
    private List<List<String>> readCSVImport(final FormFile upload, final char delimiter, final String encoding)
            throws Exception {
        
        final List<List<String>> result = new ArrayList<List<String>>();
        String line = "";
        BufferedInputStream fileStream = null;
        BufferedReader br = null;
        
        try {
            
            fileStream = new BufferedInputStream(upload.getInputStream());
            br = new BufferedReader(new InputStreamReader(fileStream, encoding));
            
            while ((line = br.readLine()) != null && !line.equals("")) {
                final CSV importFile = new CSV(delimiter);
                result.add(importFile.parse(line));
            }
            
        } finally {
            try {
                br.close();
                fileStream.close();
            } catch (final IOException e) {
                LOG.error(e.toString());
            }
        }
        
        return result;
    }
    
    /**
     * Converts an XLS/XLSX import file into an ArrayList<List<String>> with all
     * the text line elements.
     * 
     * @param FormFile
     * @return List<List<String>>
     */
    private List<List<String>> readXLSImport(final FormFile upload) throws Exception {
        
        List<List<String>> result = new ArrayList<List<String>>();
        BufferedInputStream fileStream = null;
        
        try {
            
            final XLSReader xlsReader = new XLSReader();
            fileStream = new BufferedInputStream(upload.getInputStream());
            result = xlsReader.read(fileStream);
            
        } finally {
            try {
                fileStream.close();
            } catch (final IOException e) {
                LOG.error(e.toString());
            }
        }
        
        return result;
    }
    
    /**
     * Converts an ArrayList<List<String>> with import elements into an
     * ArrayList<Bestand>.
     * 
     * @param List<List<String>>
     * @param UserInfo
     * @return List<Bestand>
     */
    private List<Bestand> convertToBestand(final List<List<String>> stockList, final UserInfo ui) {
        
        final List<Bestand> bestandList = new ArrayList<Bestand>();
        
        final int max = stockList.size();
        for (int i = 1; i < max; i++) { // start at position 1, thus ignoring the header
            bestandList.add(getBestand(stockList.get(i), ui.getKonto().getId()));
        }
        
        return bestandList;
    }
    
    /**
     * Converts a List<String> with elements of one import line into a
     * Bestand(). It relies on the assumption, that all integrity checks for
     * formatting etc. have been run before!
     * 
     * @param List<String>
     * @param long
     * @return Bestand
     */
    private Bestand getBestand(final List<String> list, final long kid) {
        final Bestand b = new Bestand();
        
        int column = 0; // column number of CSV entry to check
        for (String line : list) {
            column++;
            if (line == null) {
                line = "";
            }
            
            switch (column) {
                case 1: // Stock-ID
                    if (!"".equals(line)) { // If ID unknown, it should be null
                        b.setId(Long.valueOf(line));
                    }
                    break;
                case 2: // Holding-ID
                    b.getHolding().setKid(kid); // set the kid from UserInfo
                    if (!"".equals(line)) { // If ID unknown, it should be null
                        b.getHolding().setId(Long.valueOf(line));
                    }
                    break;
                case 3: // Location-ID
                    if (!"".equals(line)) { // If ID unknown, it should be null
                        b.getStandort().setId(Long.valueOf(line));
                    }
                    break;
                case 4: // Location
                    b.getStandort().setInhalt(line);
                    break;
                case 5: // Shelfmark
                    b.setShelfmark(line);
                    break;
                case 6: // Title
                    b.getHolding().setTitel(line);
                    break;
                case 7: // Coden
                    if ("".equals(line)) {
                        b.getHolding().setCoden(null);
                    } else {
                        b.getHolding().setCoden(line);
                    }
                    break;
                case 8: // Publisher
                    b.getHolding().setVerlag(line);
                    break;
                case 9: // Place
                    b.getHolding().setOrt(line);
                    break;
                case 10: // ISSN
                    b.getHolding().setIssn(line);
                    break;
                case 11: // ZDB-ID
                    if ("".equals(line)) {
                        b.getHolding().setZdbid(null);
                    } else {
                        b.getHolding().setZdbid(line);
                    }
                    break;
                case 12: // Startyear
                    b.setStartyear(line);
                    break;
                case 13: // Startvolume
                    b.setStartvolume(line);
                    break;
                case 14: // Startissue
                    b.setStartissue(line);
                    break;
                case 15: // Endyear
                    b.setEndyear(line);
                    break;
                case 16: // Endvolume
                    b.setEndvolume(line);
                    break;
                case 17: // Endissue
                    b.setEndissue(line);
                    break;
                case 18: // Supplement
                    if ("".equals(line)) {
                        b.setSuppl(1); // Defaultvalue
                    } else {
                        b.setSuppl(Integer.valueOf(line));
                    }
                    break;
                case 19: // remarks
                    b.setBemerkungen(line);
                    break;
                case 20: // eissue
                    if (!"".equals(line)) { // Defaultvalue remains false
                        b.setEissue(Boolean.valueOf(line));
                    }
                    break;
                case 21: // internal
                    if (!"".equals(line)) { // Defaultvalue remains false
                        b.setInternal(Boolean.valueOf(line));
                    }
                    break;
                default:
                    LOG.error("Stock getBestand - Unpredicted switch case in default: " + column);
            }
        }
        
        return b;
    }
    
    /**
     * Gets from an ISSN a List<String> of all 'related' ISSNs to map them.
     * 
     * @param String
     * @param Connection
     * @return List<String>
     */
    private List<String> getRelatedIssn(final String issn, final Connection cn) {
        
        final Issn is = new Issn();
        
        final List<String> issns = is.getAllIssnsFromOneIssn(issn, cn);
        
        if (issns.isEmpty()) {
            issns.add(issn);
        } // if there has been no hit, return the ISSN from the input
        
        return issns;
    }
    
    /**
     * Checks if the string is a parsable Stock-ID.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkStockID(final int lineCount, final String content) {
        
        final Message msg = new Message();
        
        // the ID may be empty && if not empty it must be a valid number
        if (!"".equals(content) && !org.apache.commons.lang.StringUtils.isNumeric(content)) {
            msg.setMessage("error.import.stid");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if the string is a parsable Holding-ID.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkHoldingID(final int lineCount, final String content) {
        
        final Message msg = new Message();
        
        // the ID may be empty && if not empty it must be a valid number
        if (!"".equals(content) && !org.apache.commons.lang.StringUtils.isNumeric(content)) {
            msg.setMessage("error.import.hoid");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if the string is a parsable Location-ID.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkLocationID(final int lineCount, final String content) {
        
        final Message msg = new Message();
        
        // the ID may be empty && if not empty it must be a valid number
        if (!"".equals(content) && !org.apache.commons.lang.StringUtils.isNumeric(content)) {
            msg.setMessage("error.import.locationid");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if there has been specified a title.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkTitle(final int lineCount, final String content) {
        
        final Message msg = new Message();
        final Check ck = new Check();
        
        // if not empty it must be a valid number
        if (!ck.isMinLength(content, 1)) {
            msg.setMessage("error.import.title");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * If an ISSN has been specified, checks that the string specified is a
     * valid ISSN.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkISSN(final int lineCount, final String content) {
        
        final Message msg = new Message();
        final Check ck = new Check();
        
        if (!"".equals(content)) { // the ISSN may be empty
            // if not empty it must be a valid ISSN
            if (!ck.isExactLength(content, 9)) { // length must be 9 characters
                msg.setMessage("error.import.issn.length");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            } else if (!ck.isValidIssn(content)) { // check for typos...
                msg.setMessage("error.import.issn.notvalid");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }
        
        return msg;
    }
    
    /**
     * Checks if there has been specified a startyear and the string is a valid
     * year.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkStartyear(final int lineCount, final String content) {
        
        final Message msg = new Message();
        final Check ck = new Check();
        
        if (!ck.isYear(content)) {
            msg.setMessage("error.import.startyear");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if the string is a valid (end)year.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkEndyear(final int lineCount, final String content) {
        
        final Message msg = new Message();
        final Check ck = new Check();
        
        // the endyear may be empty && if not empty it must be a valid year
        if (!"".equals(content) && !ck.isYear(content)) {
            msg.setMessage("error.import.endyear");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if the string is a valid Supplement (0 / 1 / 2).
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkSuppl(final int lineCount, final String content) {
        
        final Message msg = new Message();
        
        if (!"".equals(content) // may be empty => we will use default value
                && !"0".equals(content) && !"1".equals(content) && !"2".equals(content)) {
            msg.setMessage("error.import.suppl");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Checks if the string is a valid boolean value.
     * 
     * @param int
     * @param String
     * @return Message
     */
    private Message checkBoolean(final int lineCount, final String content) {
        
        final Message msg = new Message();
        
        if (!"".equals(content) // may be empty => we will use default value
                && !"true".equals(content) && !"false".equals(content)) {
            msg.setMessage("error.import.boolean");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }
        
        return msg;
    }
    
    /**
     * Uses a StringBuffer to compose a String in the form: Row x: text...
     * 
     * @param int
     * @param String
     * @return String
     */
    private String composeSystemMessage(final int lineCount, final String element) {
        
        final StringBuffer bf = new StringBuffer();
        
        bf.append("Row ");
        bf.append(lineCount);
        bf.append(": ");
        bf.append(element);
        
        return bf.toString();
    }
    
    /**
     * Handles the update, save and delete process for the import file into the
     * DB. It relies on the assumption, that all integrity checks for formatting
     * etc. have been run before!
     * 
     * @param List<Bestand>
     * @param UserInfo
     * @param Connection
     * @return String
     */
    private String update(final List<Bestand> bestandList, final UserInfo ui, final Connection cn) {
        
        final StringBuffer bf = new StringBuffer(32);
        int countUpdate = 0;
        int countInsert = 0;
        
        final Holding hold = new Holding();
        final Bestand best = new Bestand();
        
        // delete all existing stock entries for this account
        best.deleteAllKontoBestand(ui.getKonto(), cn);
        
        for (final Bestand b : bestandList) {
            
            // try to get an existing Holding for this account with
            // the values specified in the Bestand, but without an ev. ID
            final Holding h = new Holding(b.getHolding(), cn);
            
            // here we replace the holding from Bestand with the holding found in the DB
            // this will deduplicate and centralize the holdings for each account
            if (h.getId() != null) {
                b.setHolding(h);
            } else { // the holdings needs to be updated! Make sure it will...
                b.getHolding().setId(null);
            }
            
            // create a location if we do not have a location ID
            if (b.getStandort().getId() == null) {
                // try to get an existing entry for this account
                Text loc = new Text(cn, TextType.LOCATION, ui.getKonto().getId(), b.getStandort().getInhalt());
                if (loc.getId() == null) { // save a new location for this account
                    b.getStandort().setKonto(ui.getKonto());
                    b.getStandort().setTexttype(TextType.LOCATION);
                    loc.saveNewText(cn, b.getStandort());
                    // get back the saved location
                    loc = new Text(cn, TextType.LOCATION, ui.getKonto().getId(), b.getStandort().getInhalt());
                }
                // set the location with ID
                b.setStandort(loc);
            }
            
            if (b.getId() == null) { // save as new Bestand
                b.saveWithoutID(b, cn);
                countInsert++;
            } else { // update Bestand
                b.saveWithID(b, cn);
                countUpdate++;
            }
        }
        
        // delete all not used Holdings for this account
        hold.purgeNotUsedKontoHoldings(ui.getKonto(), cn);
        
        bf.append("Updated: ");
        bf.append(countUpdate);
        bf.append("\nInserted: ");
        bf.append(countInsert);
        
        return bf.toString();
    }
    
    /**
     * Gets the delimiter csv.
     * 
     * @return char
     */
    public static char getDelimiterCsv() {
        return DELIMITER_CSV;
    }
    
    /**
     * Gets the delimiter txt.
     * 
     * @return char
     */
    public static char getDelimiterTxt() {
        return DELIMITER_TXT;
    }
    
}
