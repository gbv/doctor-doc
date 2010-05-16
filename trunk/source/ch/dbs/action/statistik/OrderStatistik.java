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

package ch.dbs.action.statistik;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import util.Auth;
import util.Check;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestellungen;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.OverviewForm;
import ch.dbs.form.UserInfo;
import ch.dbs.statistik.Statistik;


/**
 * LoginAction checkt die eingegebenen Logininformationen auf ihre Gltigkeit
 * und erstellt bei Erfolg (User existiert in der DB) das Bean userinfo 
 * {@link ch.dbs.form.UserInfo}
 *
 */
public final class OrderStatistik extends DispatchAction {
	 
	/**
	 * Gibt alle Statistiken des aktuellen Kontos aus
	 */
	public ActionForward kontoOrders(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {
		
		OverviewForm of = (OverviewForm) fm; // enthält Datumsbereiche
		String forward = "failure";
		Auth auth = new Auth();
        if (auth.isLogin(rq)) {// Benutzer eingeloggt?
        	//Benutzer ein Bibliothekar?
        	if (auth.isBibliothekar(rq) || auth.isAdmin(rq)){
            	forward = "success";
            	Statistik st = new Statistik();
            	UserInfo ui = (UserInfo)rq.getSession().getAttribute("userinfo");
           	
            	Check check = new Check();
            	
            	//Statistiken default ab anfang Jahr setzen
        		if (of.getYfrom() == null || of.getYto() == null || 
        				of.getMfrom() == null || of.getMto() == null || 
        				of.getDfrom() == null || of.getDto() == null) {

        			Calendar cal_to = Calendar.getInstance();
        			cal_to.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        			
        			of.setYfrom(new SimpleDateFormat("yyyy").format(cal_to.getTime()));
        			of.setMfrom("01");
        			of.setDfrom("01");
        			of.setFromdate(of.getYfrom()+"-"+of.getMfrom()+"-"+of.getDfrom() + "00:00:00");

        			of.setYto(new SimpleDateFormat("yyyy").format(cal_to.getTime()));
        			of.setMto(new SimpleDateFormat("MM").format(cal_to.getTime()));
        			of.setDto(new SimpleDateFormat("dd").format(cal_to.getTime()));
        			of.setTodate(of.getYto()+"-"+of.getMto()+"-"+of.getDto() + "23:59:59");
        		}
            	
            	of = check.checkDateRegion(of, 4);            	 
           	            
// 				angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
                            Date d = new Date(); // aktuelles Datum setzen
                            ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
                            String datum = fmt.format(d);
                            int year_now = Integer.parseInt(datum);
                            int year_start = 2007;
                            
                            ArrayList<Integer> years = new ArrayList<Integer>();
                            year_now++;
                            for (int j=0;year_start<year_now;j++){
                            	years.add(j,year_start);
                            	year_start++;
                            }
                            of.setYears(years);                           
                            
                            rq.setAttribute("overviewform", of);
            	
               // ***************** Statistiken ******************
                            
                Long kid = ui.getKonto().getId();
                
                Bestellungen b = new Bestellungen();
                
                st.setKontoordersstat(b.countOrdersPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));            	
            	st.setLieferantstat(b.countLieferantPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setGratis_kosten(b.countGratisKostenpflichtigPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setSum_gratis_kosten(b.sumGratisKostenpflichtigPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setLieferartstat(b.countLieferartPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setMediatype(b.countMediatypePerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setFileformatstat(b.countFileformatPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setPrioritystat(b.countPriorityPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setZeitschriftstat(b.countISSNPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setTotuserwithordersstat(b.countRowsUID(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setJahrstat(b.countOrderYears(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setGenderstat(b.countGenderPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setInstitutionstat(b.countInstPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setAbteilungstat(b.countAbteilungPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setOrtstat(b.countPLZPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	st.setLandstat(b.countLandPerKonto(kid, of.getFromdate(), of.getTodate(), b.getConnection()));
            	
            	rq.setAttribute("statistics", st);
            	
            	b.close();
            	
//            	Navigation: Tab Neues Konto aktiv schalten
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("stats");
                rq.setAttribute("ActiveMenus", mf);
            	
            	
        	} else {
        		ErrorMessage m = new ErrorMessage("error.berechtigung");
        		m.setLink("searchfree.do");
                rq.setAttribute("errormessage", m);        		
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

}
