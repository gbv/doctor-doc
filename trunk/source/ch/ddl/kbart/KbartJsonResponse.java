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

package ch.ddl.kbart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.entity.Bestand;
import ch.dbs.entity.Text;
import ch.dbs.form.KbartForm;

import com.google.gson.Gson;

import enums.Result;

/**
 * Exports KBART holding informations and additional information in JSON for a given library.<p></p>
 * 
 * @author Markus Fischer
 */
public class KbartJsonResponse extends Action {

    private static final Logger LOG = LoggerFactory.getLogger(KbartJsonResponse.class);

    public ActionForward execute(final ActionMapping mp, final ActionForm fm, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final Text cn = new Text();

        try {

            final Long kid = Long.valueOf(rq.getParameter("kid"));

            // internal holdings are visible
            final List<Bestand> stock = new Bestand().getAllKontoBestand(kid, true, cn.getConnection());

            // transform to KbartForm
            final KbartForm kbf = new KbartForm();
            final List<KbartForm> kbarts = kbf.createKbartForms(stock, cn.getConnection());

            rp.setContentType("application/json; charset=utf-8");
            rp.setCharacterEncoding("UTF-8");
            // set a max-age header (in seconds) to make it possible to cache the content
            rp.setHeader("Cache-Control", "max-age=86400");
            rp.setHeader("Content-Disposition", "inline");
            rp.flushBuffer();
            PrintWriter pw = null;
            try {
                pw = rp.getWriter();
                // use writer to Object as JSON
                final Gson gson = new Gson();
                pw.write(gson.toJson(kbarts));
                pw.flush();
            } finally {
                pw.close();
            }

            return null;

        } catch (final IOException e) {
            LOG.error(e.toString());
        } catch (final Exception e) {
            LOG.error(e.toString());
        } finally {
            cn.close();
        }

        return mp.findForward(Result.FAILURE.getValue()); // when we get here, there is an error
    }

}
