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
import ch.dbs.entity.Bestand;
import ch.dbs.entity.Holding;
import ch.dbs.entity.Issn;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.FileForm;
import ch.dbs.form.HoldingForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;


/**
 * Class to handle holdings information
 *
 * @author Markus Fischer
 */
public class Stock extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(Stock.class);
    private static final int COLUMNS = 21; // Number of columns per import line
    private static final int FILESIZELIMIT = 6000000; // Limits the file size for upload to avoid OutOfMemory errors
    private static final char DELIMITER_CSV = ';'; // Delimiter for CSV-Export

    //Delimiter for Tab delimited txt-Export (Excel can't read UTF-8 in CSV...)
    private static final char DELIMITER_TXT = '\t';

    /**
     * Access control for the holdings export page
     */
    public ActionForward prepareExport(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        Auth auth = new Auth();

        // Access control
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                forward = "success";


            } else {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }


        return mp.findForward(forward);
    }

    /**
     * Access control for the holdings import page
     */
    public ActionForward prepareImport(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
        final long id = 10; // TextTyp ID for holding locations
        ty.setId(id);
        Auth auth = new Auth();

        // Access control
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                forward = "success";
                UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                HoldingForm hf = (HoldingForm) fm;

                hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));

                rq.setAttribute("holdingform", hf);


            } else {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        t.close();
        return mp.findForward(forward);
    }

    /**
     * Import a file with holdings information
     */
    public ActionForward importHoldings(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        // Prepare classes
        String forward = "failure";
        Auth auth = new Auth();
        Check ck = new Check();
        Text cn = new Text();
        FileForm fileForm = (FileForm) fm;
        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

        // get uploaded File
        FormFile upload = fileForm.getFile();
        String fileName = upload.getFileName();

        // Access control
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                if (fileForm.isCondition()) { // conditions for upload must be accepted
                    // must be tab delimited or csv file
                    if (ck.isFiletypeExtension(fileName, ".txt") || ck.isFiletypeExtension(fileName, ".csv")) {
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

                            // Get an ArrayList<List<String>> representation of the file
                            ArrayList<List<String>> stockList = readImport(upload, delimiter, encoding);

                            // Check if the file contains the correct number of columns
                            ArrayList<Message> messageList = checkColumns(stockList);
                            if (messageList.size() == 0) {

                                // Basic checks and make sure all entries in stockList are parsable
                                messageList = checkBasicParsability(stockList);
                                if (messageList.size() == 0) {

                                    // Convert to ArrayList<Bestand>
                                    ArrayList<Bestand> bestandList = convertToBestand(stockList, ui);

                                    // Check integrity of Bestand()
                                    messageList = checkBestandIntegrity(bestandList, ui, cn.getConnection());
                                    if (messageList.size() == 0) {

                                        // save or update holdings, delete all other holdings
                                        String successMessage = update(bestandList, ui, cn.getConnection());

                                        forward = "success";

                                        Message msg = new Message("import.success", successMessage,
                                        "allstock.do?method=prepareExport&activemenu=stock");

                                        rq.setAttribute("message", msg);


                                    } else { // detailed errors while checking integrity of Bestand() objects
                                        forward = "importError";
                                        ActiveMenusForm mf = new ActiveMenusForm();
                                        mf.setActivemenu("stock");
                                        rq.setAttribute("ActiveMenus", mf);
                                        rq.setAttribute("messageList", messageList);
                                        Message em = new Message("error.import.heading",
                                        "stock.do?method=prepareImport&activemenu=stock");
                                        rq.setAttribute("singleMessage", em);
                                    }
                                } else { // basic errors before parsing to Bestand() objects
                                    forward = "importError";
                                    ActiveMenusForm mf = new ActiveMenusForm();
                                    mf.setActivemenu("stock");
                                    rq.setAttribute("ActiveMenus", mf);
                                    rq.setAttribute("messageList", messageList);
                                    Message em = new Message("error.import.heading",
                                    "stock.do?method=prepareImport&activemenu=stock");
                                    rq.setAttribute("singleMessage", em);
                                }
                            } else { // Wrong number of columns
                                forward = "importError";
                                ActiveMenusForm mf = new ActiveMenusForm();
                                mf.setActivemenu("stock");
                                rq.setAttribute("ActiveMenus", mf);
                                rq.setAttribute("messageList", messageList);
                                Message em = new Message("error.import.heading",
                                "stock.do?method=prepareImport&activemenu=stock");
                                rq.setAttribute("singleMessage", em);
                            }

                        } else { // Filesize limit
                            ActiveMenusForm mf = new ActiveMenusForm();
                            mf.setActivemenu("stock");
                            rq.setAttribute("ActiveMenus", mf);
                            ErrorMessage em = new ErrorMessage("error.filesize",
                            "stock.do?method=prepareImport&activemenu=stock");
                            rq.setAttribute("errormessage", em);
                        }
                    } else { // Filetype error
                        ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("stock");
                        rq.setAttribute("ActiveMenus", mf);
                        ErrorMessage em = new ErrorMessage("error.filetype",
                        "stock.do?method=prepareImport&activemenu=stock");
                        rq.setAttribute("errormessage", em);
                    }
                } else { // Did not accept conditions
                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("stock");
                    rq.setAttribute("ActiveMenus", mf);
                    ErrorMessage em = new ErrorMessage("error.import.condition",
                    "stock.do?method=prepareImport&activemenu=stock");
                    rq.setAttribute("errormessage", em);
                }
            } else { // User is not allowed to access this function
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else { // Timeout
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        cn.close();

        return mp.findForward(forward);
    }

    /**
     * Gets all holding locations for a given library
     */
    public ActionForward listStockplaces(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
        final long id = 10; // TextTyp ID for holding locations
        ty.setId(id);
        Auth auth = new Auth();

        // Access control
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                forward = "success";
                UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                HoldingForm hf = (HoldingForm) fm;

                hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));

                if (hf.getStandorte().size() == 0) { //
                    hf.setMessage("error.stockplacesmodify.nolocations");
                }

                rq.setAttribute("holdingform", hf);

            } else {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        t.close();
        return mp.findForward(forward);
    }


    /**
     * Prepares and changes a given holding location
     */
    public ActionForward changeStockplace(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        Text t = new Text();
        Texttyp ty = new Texttyp();
        final long id = 10; // TextTyp ID for holding locations
        ty.setId(id);
        Auth auth = new Auth();

        // Access control
        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
                UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                HoldingForm hf = (HoldingForm) fm;

                // Make sure the Text() and the location belongs to the given account
                Text txt = new Text(t.getConnection(), hf.getStid(), ui.getKonto().getId(), ty.getId());
                if (txt.getId() != null) {
                    forward = "success";

                    if (!hf.isMod() && !hf.isDel()) { // Prepares for changing a location
                        hf.setMod(true); // flags a location to be changed for the JSP
                        ArrayList<Text> sl = new ArrayList<Text>();
                        sl.add(txt);
                        hf.setStandorte(sl);
                    } else if (hf.isMod()) { // update the given Text() / location
                        txt.setInhalt(hf.getStandortid());
                        txt.updateText(t.getConnection(), txt);
                        hf.setMod(false); // back to the location list
                        hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
                    } else if (hf.isDel()) { // delete the given Text() / location
                        // Check if there still exist holdings for this location
                        Bestand bestandInstance = new Bestand();
                        ArrayList<Bestand> sl = new ArrayList<Bestand>(bestandInstance.getAllBestandForStandortId(
                                txt.getId(), t.getConnection()));
                        if (sl == null || sl.size() == 0) {
                            txt.deleteText(t.getConnection(), txt);
                            hf.setMod(false);
                            hf.setDel(false);
                        } else {
                            // there are still holdings for this location!
                            hf.setMessage("error.stockplacesmodify.notdeletable");
                        }
                        hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
                    }

                    rq.setAttribute("holdingform", hf);

                } else { // URL-hacking
                    ErrorMessage em = new ErrorMessage("error.hack", "login.do");
                    rq.setAttribute("errormessage", em);
                    LOG.info("changeStandort: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }


            } else {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute("errormessage", em);
            }
        } else {
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        t.close();
        return mp.findForward(forward);
    }

    /**
     * Gets a list of all holdings from all libraries
     */
    public ArrayList<Bestand> checkGeneralStockAvailability(OrderForm pageForm, boolean internal) {

        ArrayList<Bestand> bestaende = new ArrayList<Bestand>();

        if (pageForm.getIssn() != null && !pageForm.getIssn().equals("")) {
            Text cn = new Text();

            ArrayList<String> setIssn = getRelatedIssn(pageForm.getIssn(), cn.getConnection());

            Holding ho = new Holding();
            ArrayList<String> hoids = ho.getAllHOIDs(setIssn, cn.getConnection());

            Bestand be = new Bestand();
            bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn.getConnection());

            cn.close();
        }

        return bestaende;

    }

    /**
     * Gets a list of all holdings for a given library from an IP
     */
    public ArrayList<Bestand> checkStockAvailabilityForIP(OrderForm pageForm, Text tip, boolean internal,
            Connection cn) {

        ArrayList<Bestand> bestaende = new ArrayList<Bestand>();

        Bestand be = new Bestand();
        Holding ho = new Holding();

        if (tip.getKonto() != null && tip.getKonto().getId() != null) { // Nur prüfen, falls Konto vorhanden

            ArrayList<String> setIssn = getRelatedIssn(pageForm.getIssn(), cn);

            ArrayList<String> hoids = ho.getAllHOIDsForKonto(setIssn, tip.getKonto().getId(), cn);

            bestaende = be.getAllBestandForHoldings(hoids, pageForm, internal, cn);
        }

        return bestaende;

    }

    /**
     * Runs basic checks and makes sure that the ArrayList<Bestand> is parsable to Bestand()
     *
     * @param ArrayList<List<String> stocklist
     * @return ArrayList<Message> messageList
     */
    private ArrayList<Message> checkBasicParsability(ArrayList<List<String>> stockList) {

        ArrayList<Message> messageList = new ArrayList<Message>();

        for (int i = 1; i < stockList.size(); i++) { // start at position 1, thus ignoring the header
            List<String> importLine = stockList.get(i);
            int column = 0; // column number of element to check
            for (String line : importLine) {
                Message msg;
                column++;
                if (line == null) { line = ""; }
                switch (column) {
                case 1: // Stock-ID
                    msg = checkStockID(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 2: // Holding-ID
                    msg = checkHoldingID(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 3: // Location-ID
                    msg = checkLocationID(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 4: // Location
                    break;
                case 5: // Shelfmark
                    break;
                case 6: // Title
                    msg = checkTitle(i, line);
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
                    msg = checkISSN(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 11: // ZDB-ID
                    break;
                case 12: // Startyear
                    msg = checkStartyear(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 13: // Startvolume
                    break;
                case 14: // Startissue
                    break;
                case 15: // Endyear
                    msg = checkEndyear(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 16: // Endvolume
                    break;
                case 17: // Endissue
                    break;
                case 18: // Supplement
                    msg = checkSuppl(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 19: // remarks
                    break;
                case 20: // eissue
                    msg = checkBoolean(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                case 21: // internal
                    msg = checkBoolean(i, line);
                    if (msg.getMessage() != null) {
                        messageList.add(msg);
                    }
                    break;
                default:
                    LOG.error("Stock ckeck BasicParsability - Unpredicted switch case in default: " + column);
                }
            }
            System.out.println();
        }

        return messageList;
    }

    /**
     * Checks if the import file has the correct format, by counting
     * the columns per line.
     *
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Message> messageList
     */
    private ArrayList<Message> checkColumns(ArrayList<List<String>> stockList) {

        ArrayList<Message> messageList = new ArrayList<Message>();
        int lineCount = 0;

        for (List<String> importLine : stockList) {
            lineCount++; // Lines start at position 1
            if (importLine.size() != COLUMNS) {
                Message msg = new Message("error.import.separators", composeSystemMessage(lineCount, ""), "");
                messageList.add(msg);
            }
        }

        return messageList;
    }

    /**
     * Checks that each Bestand() has all necessary entries and belongs to the
     * account uploading the file
     *
     * @param ArrayList<Bestand> bestandList
     * @return ArrayList<Message> messageList
     */
    private ArrayList<Message> checkBestandIntegrity(ArrayList<Bestand> bestandList, UserInfo ui, Connection cn) {

        ArrayList<Message> messageList = new ArrayList<Message>();
        HashSet<Long> uniqueSetStockID = new HashSet<Long>();
        int lineCount = 1; // the header of the ArrayList<Bestand> is already omitted

        for (Bestand b : bestandList) {
            lineCount++;

            if (b.getId() != null) {

                // check for unique Stock-ID
                if (uniqueSetStockID.contains(b.getId())) {
                    Message msg = new Message();
                    msg.setMessage("error.import.stid.notUnique");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getId().toString()));
                    messageList.add(msg);
                } else { // if not found add Stock-ID
                    uniqueSetStockID.add(b.getId());
                }

                // check if Bestand is from account
                Bestand control = new Bestand(b.getId(), cn); // get control Bestand from specified ID

                // check if KID from Bestand matches KID from ui
                if (control.getHolding() != null && control.getHolding().getKid().equals(ui.getKonto().getId())) {

                    // check if Holding-ID from control matches Holding-ID specified
                    if (!control.getHolding().getId().equals(b.getHolding().getId())) {
                        Message msg = new Message();
                        msg.setMessage("error.import.hoid.doesNotMatchStid");
                        msg.setSystemMessage(composeSystemMessage(lineCount, b.getHolding().getId().toString()));
                        messageList.add(msg);
                    }
                } else { // KID from Bestand does not match KID from ui!
                    Message msg = new Message();
                    msg.setMessage("error.import.stid.notFromAccount");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getId().toString()));
                    messageList.add(msg);
                }
            }

            // check location-ID and location
            if (b.getStandort().getId() != null) {
                // check if location-ID is from account
                // get control location from specified ID
                Text control = new Text(cn, b.getStandort().getId(), ui.getKonto().getId());

                if (control.getId() == null) { // Location-ID does not belong to account
                    Message msg = new Message();
                    msg.setMessage("error.import.locationid");
                    msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getId().toString()));
                    messageList.add(msg);

                    // Location-ID belongs to account, but do the locations match?
                } else if (!b.getStandort().getInhalt().equals("")) {
                    if (!b.getStandort().getInhalt().equals(control.getInhalt())) { // locations do not match...
                        Message msg = new Message();
                        msg.setMessage("error.import.locationsDoNotMatch");
                        msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getId().toString()
                                + "/" + b.getStandort().getInhalt()));
                        messageList.add(msg);
                    }
                }
            } else if (b.getStandort().getInhalt().equals("")) { // check if location is present
                Message msg = new Message();
                msg.setMessage("error.import.location");
                msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getInhalt()));
                messageList.add(msg);
            }

        }

        return messageList;
    }



    /**
     * Converts an import file into an ArrayList<List<String>> with all the text line elements
     *
     * @param FormFile upload
     * @param char delimiter
     * @return ArrayList<List<String>> list
     */
    private ArrayList<List<String>> readImport(FormFile upload, char delimiter, String encoding) {

        ArrayList<List<String>> list = new ArrayList<List<String>>();
        String line = "";
        BufferedInputStream fileStream = null;
        BufferedReader br = null;

        try {
            fileStream = new BufferedInputStream(upload.getInputStream());
            br = new BufferedReader(new InputStreamReader(fileStream, encoding));

            while ((line = br.readLine()) != null && !line.equals("")) {
                CSV importFile = new CSV(delimiter);
                list.add(importFile.parse(line));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                br.close();
                fileStream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return list;
    }

    /**
     * Converts an ArrayList<List<String>> with import elements into an ArrayList<Bestand>
     *
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Bestand> bestandList
     */
    private ArrayList<Bestand> convertToBestand(ArrayList<List<String>> stockList, UserInfo ui) {

        ArrayList<Bestand> bestandList = new ArrayList<Bestand>();

        for (int i = 1; i < stockList.size(); i++) { // start at position 1, thus ignoring the header
            bestandList.add(getBestand(stockList.get(i), ui.getKonto().getId()));
        }

        return bestandList;
    }

    /**
     * Converts a List<String> with elements of one import line into a Bestand().
     * It relies on the assumption, that all integrity checks for formatting
     * etc. have been run before!
     *
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Bestand> bestandList
     */
    private Bestand getBestand(List<String> list, long kid) {
        Bestand b = new Bestand();

        int column = 0; // column number of CSV entry to check
        for (String line : list) {
            column++;
            if (line == null) { line = ""; }

            switch (column) {
            case 1: // Stock-ID
                if (!line.equals("")) { // If ID unknown, it should be null
                    b.setId(Long.valueOf(line));
                }
                break;
            case 2: // Holding-ID
                b.getHolding().setKid(kid); // set the kid from UserInfo
                if (!line.equals("")) { // If ID unknown, it should be null
                    b.getHolding().setId(Long.valueOf(line));
                }
                break;
            case 3: // Location-ID
                if (!line.equals("")) { // If ID unknown, it should be null
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
                if (line.equals("")) {
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
                if (line.equals("")) {
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
                if (line.equals("")) {
                    b.setSuppl(1); // Defaultvalue
                } else {
                    b.setSuppl(Integer.valueOf(line));
                }
                break;
            case 19: // remarks
                b.setBemerkungen(line);
                break;
            case 20: // eissue
                if (!line.equals("")) { // Defaultvalue remains false
                    b.setEissue(Boolean.valueOf(line));
                }
                break;
            case 21: // internal
                if (!line.equals("")) { // Defaultvalue remains false
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
     * Gets from an ISSN a TreeSet<String> list of all 'related' ISSNs to map them
     */
    private ArrayList<String> getRelatedIssn(String issn, Connection cn) {

        Issn issnInstance = new Issn();

        ArrayList<String> issns = issnInstance.getAllIssnsFromOneIssn(issn, cn);

        if (issns.size() == 0) { issns.add(issn); } // if there has been no hit, return the ISSN from the input

        return issns;
    }

    /**
     * Checks if the string is a parsable Stock-ID
     */
    private Message checkStockID(int lineCount, String content) {

        Message msg = new Message();

        if (!content.equals("")) { // the ID may be empty
            lineCount++; // raise lineCount +1, because List starts at 0
            // if not empty it must be a valid number
            if (!org.apache.commons.lang.StringUtils.isNumeric(content)) {
                msg.setMessage("error.import.stid");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Checks if the string is a parsable Holding-ID
     */
    private Message checkHoldingID(int lineCount, String content) {

        Message msg = new Message();

        if (!content.equals("")) { // the ID may be empty
            lineCount++; // raise lineCount +1, because List starts at 0
            // if not empty it must be a valid number
            if (!org.apache.commons.lang.StringUtils.isNumeric(content)) {
                msg.setMessage("error.import.hoid");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Checks if the string is a parsable Location-ID
     */
    private Message checkLocationID(int lineCount, String content) {

        Message msg = new Message();

        if (!content.equals("")) { // the ID may be empty
            lineCount++; // raise lineCount +1, because List starts at 0
            // if not empty it must be a valid number
            if (!org.apache.commons.lang.StringUtils.isNumeric(content)) {
                msg.setMessage("error.import.locationid");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Checks if there has been specified a title
     */
    private Message checkTitle(int lineCount, String content) {

        Message msg = new Message();
        Check ck = new Check();

        lineCount++; // raise lineCount +1, because List starts at 0
        // if not empty it must be a valid number
        if (!ck.isMinLength(content, 1)) {
            msg.setMessage("error.import.title");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }

        return msg;
    }

    /**
     * If an ISSN has been specified, checks that the string specified is a valid ISSN
     */
    private Message checkISSN(int lineCount, String content) {

        Message msg = new Message();
        Check ck = new Check();
        final int issnLenght = 9;

        if (!content.equals("")) { // the ISSN may be empty
            lineCount++; // raise lineCount +1, because List starts at 0
            // if not empty it must be a valid ISSN
            if (!ck.isExactLength(content, issnLenght)) { // length must be 9 characters
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
     * Checks if there has been specified a startyear and
     *  the string is a valid year
     */
    private Message checkStartyear(int lineCount, String content) {

        Message msg = new Message();
        Check ck = new Check();

        if (!ck.isYear(content)) {
            lineCount++; // raise lineCount +1, because List starts at 0
            msg.setMessage("error.import.startyear");
            msg.setSystemMessage(composeSystemMessage(lineCount, content));
        }

        return msg;
    }

    /**
     * Checks if the string is a valid (end)year
     */
    private Message checkEndyear(int lineCount, String content) {

        Message msg = new Message();
        Check ck = new Check();

        if (!content.equals("")) { // the endyear may be empty
            lineCount++; // raise lineCount +1, because List starts at 0
            // if not empty it must be a valid year
            if (!ck.isYear(content)) {
                msg.setMessage("error.import.endyear");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Checks if the string is a valid Supplement (0 / 1 / 2)
     */
    private Message checkSuppl(int lineCount, String content) {

        Message msg = new Message();

        if (!content.equals("")) { // may be empty => we will use default value

            if (!content.equals("0")
                    && !content.equals("1")
                    && !content.equals("2")) {
                msg.setMessage("error.import.suppl");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Checks if the string is a valid boolean value
     */
    private Message checkBoolean(int lineCount, String content) {

        Message msg = new Message();

        if (!content.equals("")) { // may be empty => we will use default value

            if (!content.equals("true")
                    && !content.equals("false")) {
                msg.setMessage("error.import.boolean");
                msg.setSystemMessage(composeSystemMessage(lineCount, content));
            }
        }

        return msg;
    }

    /**
     * Uses a StringBuffer to compose a String in the form:
     * Row x: text...
     */
    private String composeSystemMessage(int lineCount, String element) {

        StringBuffer bf = new StringBuffer();

        bf.append("Row ");
        bf.append(lineCount);
        bf.append(": ");
        bf.append(element);

        return bf.toString();
    }

    /**
     * Handles the update, save and delete process for the import file into the DB.
     * It relies on the assumption, that all integrity checks for formatting
     * etc. have been run before!
     *
     */
    private String update(ArrayList<Bestand> bestandList, UserInfo ui, Connection cn) {

        StringBuffer bf = new StringBuffer();
        int countUpdate = 0;
        int countInsert = 0;

        Holding hold = new Holding();
        Bestand best = new Bestand();

        // delete all existing stock entries for this account
        best.deleteAllKontoBestand(ui.getKonto(), cn);

        for (Bestand b : bestandList) {

            // try to get an existing Holding for this account with
            // the values specified in the Bestand, but without an ev. ID
            Holding h = new Holding(b.getHolding(), cn);

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
                Text loc = new Text(cn, new Texttyp("Standorte", cn),
                        ui.getKonto().getId(), b.getStandort().getInhalt());
                if (loc.getId() == null) { // save a new location for this account
                    b.getStandort().setKonto(ui.getKonto());
                    b.getStandort().setTexttyp(new Texttyp("Standorte", cn));
                    loc.saveNewText(cn, b.getStandort());
                    // get back the saved location
                    loc = new Text(cn, new Texttyp("Standorte", cn),
                            ui.getKonto().getId(), b.getStandort().getInhalt());
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
        bf.append("\n");
        bf.append("Inserted: ");
        bf.append(countInsert);

        return bf.toString();
    }

    public static char getDelimiterCsv() {
        return DELIMITER_CSV;
    }

    public static char getDelimiterTxt() {
        return DELIMITER_TXT;
    }





}
