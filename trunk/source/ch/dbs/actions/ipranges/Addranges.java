//  Copyright (C) 2013  Markus Fischer
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

package ch.dbs.actions.ipranges;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Auth;
import util.IPChecker;
import ch.dbs.form.IPForm;
import ch.dbs.form.Message;
import enums.Result;

public class Addranges extends Action {

    /**
     * Manage IP ranges for an account.
     */
    public ActionForward execute(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
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

        // get IPForm containing all IPs in one String
        final IPForm ranges = (IPForm) form;
        // extract all trimmed IPs
        final List<String> ips = getExtractIPs(ranges);
        // validate and separate IPs into ip4 and ip6 addresses
        final IPChecker checker = new IPChecker();
        checker.validateIPs(ips, ranges);

        // all IPs are valid
        if (ranges.getInvalidIPs().isEmpty()) {
            // check for existing overlapping IPs

            // delete old IPs

            // save new IPs

        } else {
            // we got invalid IPs
            final Message msg = new Message("error.ip");
            msg.setSystemMessage(createErrorText(ranges.getInvalidIPs()));
            rq.setAttribute("message", msg);
        }

        return mp.findForward(Result.SUCCESS.getValue());
    }

    private String createErrorText(final List<String> invalidIPs) {

        final StringBuffer error = new StringBuffer();

        for (int i = 0; i < invalidIPs.size(); i++) {
            error.append(invalidIPs.get(i));
            if (i + 1 < invalidIPs.size()) {
                error.append(" ; ");
            }
        }

        return error.toString();
    }

    private List<String> getExtractIPs(final IPForm ranges) {

        final List<String> ips = new ArrayList<String>();
        Scanner lines = null;

        if (ranges != null && ranges.getIps() != null) {

            try {
                lines = new Scanner(ranges.getIps());
                while (lines.hasNextLine()) {
                    final String ip = lines.nextLine();
                    if (!isEmpty(ip)) {
                        ips.add(ip.trim());
                    }
                }

            } catch (final Exception e) {

            } finally {
                if (lines != null) {
                    lines.close();
                }
            }

        }

        return ips;
    }

    private boolean isEmpty(final String input) {

        if (input == null || "".equals(input.trim())) {
            return true;
        }
        return false;
    }

}