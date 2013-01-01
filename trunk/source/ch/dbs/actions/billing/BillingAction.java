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

package ch.dbs.actions.billing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import util.Auth;
import ch.dbs.admin.KontoAdmin;
import ch.dbs.entity.Billing;
import ch.dbs.entity.Konto;
import ch.dbs.form.BillingForm;
import ch.dbs.form.KontoForm;
import ch.dbs.form.Message;
import enums.Result;

/**
 * Handelt Rechnungsinformationen für Konten sowie für Admins
 * 
 * @author Pascal Steiner
 */
public final class BillingAction extends DispatchAction {
    
    /**
     * Listet die Rechnungen zu einem Konto sortiert nach Offenen Rechnungen,
     * Rechnungsdatum auf
     */
    public ActionForward listBillings(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
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
        
        String forward = Result.FAILURE.getValue();
        final Billing b = new Billing();
        
        try {
            
            // Darf der Benutzer Rechnungen sehen? NUR SEINE EIGENEN... ;-)
            
            // Rechnungen zum Konto auflisten
            final KontoForm kf = (KontoForm) fm;
            
            final BillingForm bf = new BillingForm();
            final Konto k = new Konto(kf.getKid(), b.getConnection());
            bf.setKonto(k);
            bf.setBillings(b.getBillings(k, b.getConnection()));
            
            rq.setAttribute("billingform", bf);
            
            forward = Result.SUCCESS.getValue();
            
        } finally {
            b.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * Admins können Paydate setzen
     */
    public ActionForward setPayDate(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        
        String forward = Result.FAILURE.getValue();
        final Billing cn = new Billing();
        
        try {
            
            final BillingForm bf = (BillingForm) fm;
            final Billing b = new Billing(bf.getBillid(), cn.getConnection());
            b.setZahlungseingang(bf.getZahlungseingang());
            b.update(cn.getConnection());
            
            final Message em = new Message("message.setpaydate", b.getKonto().getBibliotheksname() + "\n\n"
                    + b.getRechnungsgrund().getInhalt(), "kontoadmin.do?method=listKontos");
            rq.setAttribute("message", em);
            forward = Result.SUCCESS.getValue();
            
        } finally {
            cn.close();
        }
        
        return mp.findForward(forward);
    }
    
    /**
     * PDF Vorschau für eine Rechnung
     */
    public ActionForward billingPreview(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        
        final Auth auth = new Auth();
        // make sure the user is logged in
        if (!auth.isLogin(rq)) {
            return mp.findForward(Result.ERROR_TIMEOUT.getValue());
        }
        // check access rights
        if (!auth.isAdmin(rq)) {
            return mp.findForward(Result.ERROR_MISSING_RIGHTS.getValue());
        }
        
        final Billing b = new Billing();
        
        try {
            
            BillingForm bf = (BillingForm) fm;
            final Konto k = new Konto(bf.getKontoid(), b.getConnection());
            
            // Rechnung speichern
            final KontoAdmin ka = new KontoAdmin();
            bf = ka.prepareBillingText(k, b.getConnection(), null, bf);
            //					bf.getBill().save(b.getConnection());
            
            // Parameter abfüllen
            final Map<String, Object> param = new HashMap<String, Object>();
            param.put("adress", k.getBibliotheksname());
            param.put("billingtext", bf.getBillingtext());
            
            final InputStream reportStream = new BufferedInputStream(this.getServlet().getServletContext()
                    .getResourceAsStream("/reports/rechnung.jasper"));
            
            // PDF erstellen
            OutputStream out = null;
            
            // prepare output stream
            rp.setContentType("application/pdf");
            
            final Collection<Map<String, ?>> al = new ArrayList<Map<String, ?>>();
            final HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("Fake", "Daten damit Report nicht leer wird..");
            al.add(hm);
            final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(al);
            
            try {
                out = rp.getOutputStream();
                JasperRunManager.runReportToPdfStream(reportStream, out, param, ds);
            } catch (final JRException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Report an den Browser senden
            try {
                out.flush();
                out.close();
            } catch (final IOException e) {
                // LOG.error("BillingAction.billingPreview: " +
                // e.toString());
            }
            
        } finally {
            b.close();
        }
        return mp.findForward(null);
    }
    
}
