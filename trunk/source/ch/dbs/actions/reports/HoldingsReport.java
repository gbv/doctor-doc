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
 * Creates a CSV-export of the holdings of a given library
 * 
 * @author Markus Fischer
 *
 */
public final class HoldingsReport extends DispatchAction {
	
	private static final SimpleLogger log = new SimpleLogger(HoldingsReport.class);
	private static final char DELIMITER = ch.dbs.actions.bestand.Stock.getDelimiter(); // use same delimiter as in Stock()
    
    /**
     * Gets all holdings of a given library and creates an CSV-File
     */
    public ActionForward execute(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
    	
    	String forward = "failure";
    	Auth auth = new Auth();
    	
    	// Check if the user is logged in and is librarian or admin
    	if (auth.isLogin(rq)) {
			if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) { // not accessible for users
				
				// Prepare classes 		
				UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");				
				ThreadSafeSimpleDateFormat tf = new ThreadSafeSimpleDateFormat("yyyy-MM-dd_HH-mm-ss");	        	
	        	Date date = new Date();
				
				try {
					// Compose filename with date and time
					String filename = "holdings-" + tf.format(date, ui.getKonto().getTimezone()) + ".csv";
					// Prepare Output	
					rp.setContentType("text/csv;charset=UTF-8"); // Set ContentType in the response for the Browser
					rp.addHeader("Content-Disposition", "attachment;filename="+filename); // Set filename

					rp.flushBuffer();
					
					// Use writer to render text
					PrintWriter pw = rp.getWriter();
				    pw.write(getCsvContent(ui.getKonto()));
				    pw.flush();
				    pw.close();
					
				} catch (IOException e) {
					// Output failed
					log.error("Failure in HoldingsReport.execute: " + e.toString());
				} finally {	        
						forward=null;
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
    
    private String getCsvContent(Konto k) {
    	Bestand b = new Bestand();
    	Text cn = new Text();
    	
    	// get a StringBuffer with a header describing the content of the fields
    	StringBuffer buf = initStringBuffer();

    	ArrayList<Bestand> stock = b.getAllKontoBestand(k.getId(), cn.getConnection());
    	
    	for (int i=0;i<stock.size();i++) {
    		buf.append(getCsvLine(stock.get(i)));
    	}
    	
    	cn.close();
    	
    	return buf.toString();
    }
    


	private String getCsvLine(Bestand b) {
		
		StringBuffer buf = new StringBuffer();
		RemoveNullValuesFromObject nullValues = new RemoveNullValuesFromObject();
		
		b = (Bestand) nullValues.remove(b);
		b.setHolding((Holding) nullValues.remove(b.getHolding()));
		
		buf.append("\""+b.getId()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.getHolding().getId()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.getStandort().getId()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.getStandort().getInhalt()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getShelfmark())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getTitel())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getCoden())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getVerlag())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getOrt())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getIssn())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getHolding().getZdbid())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getStartyear())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getStartvolume())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getStartissue())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getEndyear())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getEndvolume())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getEndissue())+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.getSuppl()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.isEissue()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+b.isInternal()+"\"");
		buf.append(DELIMITER);
		buf.append("\""+removeSpecialCharacters(b.getBemerkungen())+"\"");
		buf.append("\n");
		
		return buf.toString();
	}
	
	private StringBuffer initStringBuffer() {
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("\"Stock ID\"");
		buf.append(DELIMITER);
		buf.append("\"Holding ID\"");
		buf.append(DELIMITER);
		buf.append("\"Location ID\"");
		buf.append(DELIMITER);
		buf.append("\"Location Name\"");
		buf.append(DELIMITER);
		buf.append("\"Shelfmark\"");
		buf.append(DELIMITER);
		buf.append("\"Title\"");
		buf.append(DELIMITER);
		buf.append("\"Coden\"");
		buf.append(DELIMITER);
		buf.append("\"Publisher\"");
		buf.append(DELIMITER);
		buf.append("\"Place\"");
		buf.append(DELIMITER);
		buf.append("\"ISSN\"");
		buf.append(DELIMITER);
		buf.append("\"ZDB-ID\"");
		buf.append(DELIMITER);
		buf.append("\"Staryear\"");
		buf.append(DELIMITER);
		buf.append("\"Startvolume\"");
		buf.append(DELIMITER);
		buf.append("\"Startissue\"");
		buf.append(DELIMITER);
		buf.append("\"Endyear\"");
		buf.append(DELIMITER);
		buf.append("\"Endvolume\"");
		buf.append(DELIMITER);
		buf.append("\"Endissue\"");
		buf.append(DELIMITER);
		buf.append("\"Suppl\"");
		buf.append(DELIMITER);
		buf.append("\"eissue\"");
		buf.append(DELIMITER);
		buf.append("\"internal\"");
		buf.append(DELIMITER);
		buf.append("\"remarks\"");
		buf.append("\n");
		
		return buf;
	}
	
	private String removeSpecialCharacters(String str) {
		
		if (str!=null) {
//			str = str.replaceAll(";", "-");
			str = str.replaceAll("\012", "\040").trim();
		}
		
		return str;
	}


}