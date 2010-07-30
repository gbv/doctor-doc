//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package ch.dbs.actions.bestand;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

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
	
	private static final SimpleLogger log = new SimpleLogger(Stock.class);
	private static final int CSV_COLUMNS = 21; // Number of columns per CSV line
	private static final int FILESIZELIMIT = 6000000; // Limits the file size for upload to avoid OutOfMemory errors
	private static final char DELIMITER = ';';
	
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
    	long id = 10; // TextTyp ID for holding locations
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
     * Import a CSV-file with holdings information
     */
    public ActionForward importHoldings(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

    	// Prepare classes
        String forward = "failure";
    	Auth auth = new Auth();
    	Check ck = new Check();
    	Text cn = new Text();
    	FileForm fileForm = (FileForm)fm;
    	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");    	
    	
    	// get uploaded File
        FormFile upload = fileForm.getFile();
    	String fileName = upload.getFileName();
        
    	// Access control
    	if (auth.isLogin(rq)) {
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
        if (fileForm.isCondition()) { // conditions for upload must be accepted
        if (ck.isFiletypeExtension(fileName, ".csv")) {
        if (upload.getFileSize()<FILESIZELIMIT) { // limit file size to avoid OutOfMemory errors
            
        	// Get an ArrayList<List<String>> representation of the CSV file
            ArrayList<List<String>> stockList = readCSV(upload);
        	
        	// Check if the CSV file contains the correct number of columns
            ArrayList<Message> messageList = checkCSVFormat(stockList);
            if (messageList.size()==0) {
        	
        	// Basic checks and make sure all entries in stockList are parsable
        	messageList = checkBasicParsability(stockList);
        	if (messageList.size()==0) {

        	// Convert to ArrayList<Bestand>
        	ArrayList<Bestand> bestandList = convertToBestand(stockList, ui);
        		
       		// Check integrity of Bestand()
           	messageList = checkBestandIntegrity(bestandList, ui, cn.getConnection());
           	if (messageList.size()==0) {
        		
        	// save or update holdings, delete all other holdings
            String successMessage = update(bestandList, ui, cn.getConnection());
            
        	forward = "success";
            
            Message msg = new Message("import.success", successMessage, "allstock.do?method=prepareExport&activemenu=stock");
            
            rq.setAttribute("message", msg);
            
        	
        	} else { // detailed errors while checking integrity of Bestand() objects
        		forward = "importError";
            	ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("stock");
                rq.setAttribute("ActiveMenus", mf);
                rq.setAttribute("messageList", messageList);
                Message em = new Message("error.import.heading", "stock.do?method=prepareImport&activemenu=stock");
                rq.setAttribute("singleMessage", em);
            }
        	} else { // basic errors before parsing to Bestand() objects
        		forward = "importError";
            	ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("stock");
                rq.setAttribute("ActiveMenus", mf);
                rq.setAttribute("messageList", messageList);
                Message em = new Message("error.import.heading", "stock.do?method=prepareImport&activemenu=stock");
                rq.setAttribute("singleMessage", em);
            }
        	} else { // Wrong number of columns
        		forward = "importError";
            	ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("stock");
                rq.setAttribute("ActiveMenus", mf);
                rq.setAttribute("messageList", messageList);
                Message em = new Message("error.import.heading", "stock.do?method=prepareImport&activemenu=stock");
                rq.setAttribute("singleMessage", em);
            }
        	
        } else { // Filesize limit
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("stock");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.filesize", "stock.do?method=prepareImport&activemenu=stock");
            rq.setAttribute("errormessage", em);        	
        }        
        } else { // Filetype error
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("stock");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.filetype", "stock.do?method=prepareImport&activemenu=stock");
            rq.setAttribute("errormessage", em);
        }
        } else { // Did not accept conditions
        	ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("stock");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.import.condition", "stock.do?method=prepareImport&activemenu=stock");
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
    	long id = 10; // TextTyp ID for holding locations
    	ty.setId(id);
    	Auth auth = new Auth();
        
    	// Access control
        if (auth.isLogin(rq)) {
    	if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
            forward = "success";
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hf = (HoldingForm) fm;
            
            hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
            
            if (hf.getStandorte().size()==0) { // 
            	hf.setMessage("Sie habe noch keine Bestände und Standorte importiert!");
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
    	long id = 10; // TextTyp ID for holding locations
    	ty.setId(id);
    	Auth auth = new Auth();
        
    	// Access control
        if (auth.isLogin(rq)) {
    	if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
        	UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            HoldingForm hf = (HoldingForm) fm;
            
            // Make sure the Text() and the location belongs to the given account
            Text txt = new Text(t.getConnection(), hf.getStid(), ui.getKonto().getId(), ty.getId());
        if (txt.getId() !=null) {
        	forward = "success";
        	
        if (hf.isMod()==false && hf.isDel()==false) { // Prepares for changing a location
            hf.setMod(true); // flags a location to be changed for the JSP
            ArrayList<Text> sl = new ArrayList<Text>();
            sl.add(txt);
            hf.setStandorte(sl);
        } else if (hf.isMod()==true) { // update the given Text() / location
        	txt.setInhalt(hf.getStandortid());
    		txt.updateText(t.getConnection(), txt);
    		hf.setMod(false); // back to the location list
    		hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));        	
        } else if (hf.isDel()==true) { // delete the given Text() / location
        	// Check if there still exist holdings for this location
			Bestand bestandInstance = new Bestand();
			ArrayList<Bestand> sl = new ArrayList<Bestand>(bestandInstance.getAllBestandForStandortId(txt.getId(), t.getConnection()));
			if (sl==null || sl.size()==0) {
			txt.deleteText(t.getConnection(), txt);
			hf.setMod(false);
			hf.setDel(false);
			} else {
				// there are still holdings for this location!
				hf.setMessage("Standort nicht löschbar! Es existieren noch verknüpfte Bestände!");
			}
			hf.setStandorte(t.getAllKontoText(ty, ui.getKonto().getId(), t.getConnection()));
        }
        
        rq.setAttribute("holdingform", hf);
        
        } else { // URL-hacking
        	ErrorMessage em = new ErrorMessage("error.hack", "login.do");
            rq.setAttribute("errormessage", em);
            log.info("changeStandort: prevented URL-hacking! " + ui.getBenutzer().getEmail());
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
    public ArrayList<Bestand> checkGeneralStockAvailability (OrderForm pageForm, boolean internal) {
    	
    	ArrayList<Bestand> bestaende = new ArrayList<Bestand>();
    	
    	if (pageForm.getIssn()!=null && !pageForm.getIssn().equals("")) {
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
    public ArrayList<Bestand> checkStockAvailabilityForIP (OrderForm pageForm, Text tip, boolean internal, Connection cn) {
    	
    	ArrayList<Bestand> bestaende = new ArrayList<Bestand>();
    		
    		Bestand be = new Bestand();
    		Holding ho = new Holding();
    		
    		if (tip.getKonto()!=null && tip.getKonto().getId()!=null) { // Nur prüfen, falls Konto vorhanden

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
    private ArrayList<Message> checkBasicParsability (ArrayList<List<String>> stockList) {
    	
    	ArrayList<Message> messageList = new ArrayList<Message>();
    	
    	for (int i=1;i<stockList.size();i++) { // start at position 1, thus ignoring the header
    		List<String> csvLine = stockList.get(i);
    	    ListIterator<String> csv = csvLine.listIterator();
    	    int column = 0; // column number of CSV entry to check
    	    String content = ""; // content of column
    	    while (csv.hasNext()) {
    	    	Message msg;
    	    	column++;
    	    	content = (String) csv.next();
    	    	switch (column)
    	    	{
    	    	case 1: // Stock-ID
    	    		msg = checkStockID(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 2: // Holding-ID
    	    		msg = checkHoldingID(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 3: // Location-ID
    	    		msg = checkLocationID(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 4: // Location
    	    		break;
    	    	case 5: // Shelfmark
    	    		break;
    	    	case 6: // Title
    	    		msg = checkTitle(i, content);
    	    		if (msg.getMessage()!=null) {
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
    	    		msg = checkISSN(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 11: // ZDB-ID
    	    		break;
    	    	case 12: // Startyear
    	    		msg = checkStartyear(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 13: // Startvolume
    	    		break;
    	    	case 14: // Startissue
    	    		break;
    	    	case 15: // Endyear
    	    		msg = checkEndyear(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 16: // Endvolume
    	    		break;
    	    	case 17: // Endissue
    	    		break;
    	    	case 18: // Supplement
    	    		msg = checkSuppl(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 19: // eissue
    	    		msg = checkBoolean(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 20: // internal
    	    		msg = checkBoolean(i, content);
    	    		if (msg.getMessage()!=null) {
    	    			messageList.add(msg);
    	    		}
    	    		break;
    	    	case 21: // remarks
    	    		break;
    	    	}
    	    }
    	    System.out.println();
    	}

    	return messageList;    	
    }
    
    /**
     * Checks if the CSV-file has the correct format, by counting
     * the columns per line.
     * 
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Message> messageList
     */
    private ArrayList<Message> checkCSVFormat (ArrayList<List<String>> stockList) {

    	ArrayList<Message> messageList = new ArrayList<Message>();
        	
        	for (int i=0;i<stockList.size();i++) {
        		List<String> csvLine = stockList.get(i);
        		if (csvLine.size()!=CSV_COLUMNS) {
        			int lineCount = i+1;
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
    private ArrayList<Message> checkBestandIntegrity (ArrayList<Bestand> bestandList, UserInfo ui, Connection cn) {

    	ArrayList<Message> messageList = new ArrayList<Message>();
    	HashSet<Long> uniqueSetStockID = new HashSet<Long>();
    	int lineCount = 0;
        	
        	for (int i=0;i<bestandList.size();i++) {
        		
        		Bestand b = bestandList.get(i);
        		lineCount++;
        		
        		if (b.getId()!=null) {
        			
        			// check for unique Stock-ID
        			if( uniqueSetStockID.contains(b.getId()) ) {
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
        			if (control.getHolding()!=null && control.getHolding().getKid().equals(ui.getKonto().getId())) {
        				
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
        		if (b.getStandort().getId()!=null) {
        			// check if location-ID is from account
        			Text control = new Text(cn, b.getStandort().getId(), ui.getKonto().getId()); // // get control location from specified ID
        			
        			if (control.getId()==null) { // Location-ID does not belong to account
        				Message msg = new Message();
        				msg.setMessage("error.import.locationid");
        	    		msg.setSystemMessage(composeSystemMessage(lineCount, b.getStandort().getId().toString()));
        	    		messageList.add(msg);
        			} else if (!b.getStandort().getInhalt().equals("")) { // Location-ID belongs to account, but do the locations match?
        				if (!b.getStandort().getInhalt().equals(control.getInhalt())) { // locations do not match...
        					Message msg = new Message();
            				msg.setMessage("error.import.locationsDoNotMatch");
            	    		msg.setSystemMessage(composeSystemMessage(lineCount, b.getId().toString()));
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
     * Converts a CSV file into an ArrayList<List<String>> with all the CSV elements 
     * 
     * @param FormFile upload
     * @return ArrayList<List<String>> list
     */
    private ArrayList<List<String>> readCSV (FormFile upload) {
    	
    	ArrayList<List<String>> list = new ArrayList<List<String>>();    	
    	String line = "";
    	BufferedInputStream fileStream = null;
    	BufferedReader br = null;
    	
    	try {    		
    		fileStream = new BufferedInputStream(upload.getInputStream());
        	br = new BufferedReader(new InputStreamReader(fileStream));
    	    
    	    while ((line = br.readLine())!=null && !line.equals("")) {
    	    	CSV csv = new CSV(DELIMITER);
	    	    list.add(csv.parse(line));
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
     * Converts an ArrayList<List<String>> with CSV elements into an ArrayList<Bestand> 
     * 
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Bestand> bestandList
     */
    private ArrayList<Bestand> convertToBestand (ArrayList<List<String>> stockList, UserInfo ui) {
    	
    	ArrayList<Bestand> bestandList = new ArrayList<Bestand>();
    	
    	for (int i=1;i<stockList.size();i++) { // start at position 1, thus ignoring the header
    		bestandList.add(getBestand(stockList.get(i), ui.getKonto().getId()));
    	}
    	
    	return bestandList;
    }
    
    /**
     * Converts a List<String> with elements of one CSV line into a Bestand().
     * It relies on the assumption, that all integrity checks for formatting 
     * etc. have been run before!
     * 
     * @param ArrayList<List<String>> stockList
     * @return ArrayList<Bestand> bestandList
     */
    private Bestand getBestand(List<String> list, long kid) {
    	Bestand b = new Bestand();

	    ListIterator<String> csv = list.listIterator();
	    int column = 0; // column number of CSV entry to check
	    String content = ""; // content of column
	    while (csv.hasNext()) {
	    	column++;
	    	content = (String) csv.next();

	    	switch (column)
	    	{
	    	case 1: // Stock-ID
	    		if (!content.equals("")) { // If ID unknown, it should be null
	    			b.setId(Long.valueOf(content));
	    		}
	    		break;
	    	case 2: // Holding-ID
	    		b.getHolding().setKid(kid); // set the kid from UserInfo
	    		if (!content.equals("")) { // If ID unknown, it should be null
	    			b.getHolding().setId(Long.valueOf(content));
	    		}
	    		break;
	    	case 3: // Location-ID
	    		if (!content.equals("")) { // If ID unknown, it should be null
	    			b.getStandort().setId(Long.valueOf(content));
	    		}
	    		break;
	    	case 4: // Location
	    		b.getStandort().setInhalt(content);
	    		break;
	    	case 5: // Shelfmark
	    		b.setShelfmark(content);
	    		break;
	    	case 6: // Title
	    		b.getHolding().setTitel(content);
	    		break;
	    	case 7: // Coden
	    		if (content.equals("")) {
	    			b.getHolding().setCoden(null);
	    		} else {
	    			b.getHolding().setCoden(content);
	    		}
	    		break;
	    	case 8: // Publisher
	    		b.getHolding().setVerlag(content);
	    		break;
	    	case 9: // Place
	    		b.getHolding().setOrt(content);
	    		break;
	    	case 10: // ISSN
	    		b.getHolding().setIssn(content);
	    		break;
	    	case 11: // ZDB-ID
	    		if (content.equals("")) {
	    			b.getHolding().setZdbid(null);
	    		} else {
	    			b.getHolding().setZdbid(content);
	    		}
	    		break;
	    	case 12: // Startyear
	    		b.setStartyear(content);
	    		break;
	    	case 13: // Startvolume
	    		b.setStartvolume(content);
	    		break;
	    	case 14: // Startissue
	    		b.setStartissue(content);
	    		break;
	    	case 15: // Endyear
	    		b.setEndyear(content);
	    		break;
	    	case 16: // Endvolume
	    		b.setEndvolume(content);
	    		break;
	    	case 17: // Endissue
	    		b.setEndissue(content);
	    		break;
	    	case 18: // Supplement
	    		if (content.equals("")) {
	    			b.setSuppl(1); // Defaultvalue
	    		} else {
	    			b.setSuppl(Integer.valueOf(content));
	    		}
	    		break;
	    	case 19: // eissue
	    		if (!content.equals("")) { // Defaultvalue remains false
	    			b.setEissue(Boolean.valueOf(content));
	    		}
	    		break;
	    	case 20: // internal
	    		if (!content.equals("")) { // Defaultvalue remains false
	    			b.setInternal(Boolean.valueOf(content));
	    		}
	    		break;
	    	case 21: // remarks
	    		b.setBemerkungen(content);
	    		break;
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
    	
    	if (issns.size()==0) issns.add(issn); // if there has been no hit, return the ISSN from the input

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
    	
    	if (!content.equals("")) { // the ISSN may be empty
    		lineCount++; // raise lineCount +1, because List starts at 0
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
    	
	    	if (!content.equals("0") &&
	    		!content.equals("1") &&
	    		!content.equals("2")) {
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
        	
	    	if (!content.equals("true") &&
	    		!content.equals("false")) {
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
     * Handles the update, save and delete process for the CSV import into the DB.
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
    	
    	for (int i=0;i<bestandList.size();i++) {
    		Bestand b = bestandList.get(i);

    		// try to get an existing Holding for this account with
    		// the values specified in the Bestand, but without an ev. ID
    		Holding h = new Holding(b.getHolding(), cn);
    		
    		// here we replace the holding from Bestand with the holding found in the DB
    		// this will deduplicate and centralize the holdings for each account
    		if (h.getId()!=null) {
    			b.setHolding(h);
    		}

    		// create a location if we do not have a location ID
    		if (b.getStandort().getId()==null) {
    			// try to get an existing entry for this account
    			Text loc = new Text(cn, new Texttyp("Standorte", cn), ui.getKonto().getId(), b.getStandort().getInhalt());
    			if (loc.getId()==null) { // save a new location for this account
    				loc.saveNewText(cn, b.getStandort());
    				// get back the saved location
    				loc = new Text(cn, new Texttyp("Standorte", cn), ui.getKonto().getId(), b.getStandort().getInhalt());
    			}
    			// set the location with ID
    			b.setStandort(loc);
    		}
    		
    		if (b.getId()==null) { // save as new Bestand
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

	public static char getDelimiter() {
		return DELIMITER;
	}
	
	

}
