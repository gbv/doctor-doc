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



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;
import util.Auth;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.IlvReportForm;
import ch.dbs.form.Message;
import ch.dbs.form.UserInfo;

/**
 * Default ILV Mailfields operation
 *
 * @author Pascal Steiner
 *
 */
public final class ILVMailfields extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(ILVMailfields.class);

    /**
     * Save default Mailfields subject and mailtext for ILV ordermail with attached PDF
     */
    public ActionForward saveMailFields(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final Auth auth = new Auth();
        // Make sure method is only accessible when user is logged in
        String forward = "failure";
        if (auth.isLogin(rq)) {
        	
        	final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        	final IlvReportForm ilvf = (IlvReportForm) fm;
        	final Text cn = new Text();
        	Texttyp mailtexttyp = new Texttyp("ILV Mailtext", cn.getConnection());
        	Texttyp subjecttyp = new Texttyp("ILV Mailsubject", cn.getConnection());
        	
        	// Exists already a Mailtext or a subject?
        	Text subject = new Text(cn.getConnection(), subjecttyp, ui.getKonto().getId());
        	Text text = new Text(cn.getConnection(), mailtexttyp, ui.getKonto().getId());
        	
        	subject.setKonto(ui.getKonto());
        	if (subject.getInhalt() == null){ // save a new subject for the konto
        		subject.setInhalt(ilvf.getSubject());
        		subject.setTexttyp(subjecttyp);
        		subject.saveNewText(cn.getConnection(), subject);
        	} else { // update the existing subject for the konto
        		subject.setInhalt(ilvf.getSubject());
        		subject.updateText(cn.getConnection(), subject);
        	}
        	
        	text.setKonto(ui.getKonto());
        	if (text.getInhalt() == null){				 // save a new subject for the konto
        		text.setInhalt(ilvf.getMailtext());
        		text.setTexttyp(mailtexttyp);
        		text.saveNewText(cn.getConnection(), text);
        	} else { 									 // update the existing subject for the konto
        		text.setInhalt(ilvf.getMailtext());
        		text.updateText(cn.getConnection(), text);
        	}

        	cn.close();        	
        	forward = "success";
        	Message mes = new Message("ilvmail.defaultmailfiedsset", "listkontobestellungen.do?method=overview");
        	rq.setAttribute("message", mes);
            
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }
        return mp.findForward(forward);
    }
    
}
