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

package util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Administrator;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.KontoForm;
import ch.dbs.form.UserInfo;

public class Auth {

    private static final SimpleLogger LOG = new SimpleLogger(Auth.class);

    /**
     * Authentifizierungsklasse
     * @param args
     */
    public Auth() {

    }
    /**
     * Überprüft, ob eine gültige Session vorhanden ist {@link ch.dbs.form.UserInfo}<p>
     *
     * @param rq HttpServletRequest
     * @return auth (true/false)
     */

    public boolean isLogin(HttpServletRequest rq) {
        boolean auth = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (ui.getBenutzer() != null) {
                auth = true;
            }
        }
        return auth;
    }

    /**
     * Überprüft, ob ein Zugriff berechtigt erfolgt, falls der Kunde nicht
     * eingeloggt ist.
     *
     *
     * @param rq HttpServletRequest
     * @return t Text
     */
    public Text grantAccess(HttpServletRequest rq) {

        // es gibt drei mögliche Zugriffsformen, ohne eingeloggt zu sein. Priorisiert wie folgt:
        // 1. Kontokennung (überschreibt IP-basiert)
        // 2. IP-basiert (überschreibt Broker-Kennung)
        // 3. Broker-Kennung (z.B. Careum Explorer)

        Text cn = new Text();
        Auth auth = new Auth();

        // IP-basiert
        String ip = rq.getRemoteAddr();

        if (ip != null && ip.contains(":")) { // Verdacht auf IPv6
            try {
                InetAddress a6 = InetAddress.getByName(ip);
                if (a6 instanceof Inet6Address) {
                    // in Linux kann intern schon mal IPv6 zum Zuge kommen...
                    if (!a6.isLoopbackAddress()) { // Nachricht schicken, falls IPv6 und nicht lokale Loopback
                        MHelper mh = new MHelper();
                        mh.sendErrorMail("IPv6 empfangen!", a6.getHostAddress());
                    }
                    if (((Inet6Address) a6).isIPv4CompatibleAddress()) {
                        Inet4Address a4 = (Inet4Address) Inet4Address.getByName(a6.getHostName());
                        System.out.println("umgewandelte IP6 to IP4: " + a4.getHostAddress());
                        ip = a4.getHostAddress(); // Umwandlung in IP4 // TODO: IPv6 grundsätzlich ermöglichen
                    }
                }
            } catch (UnknownHostException ex) {
                LOG.error("grantAccess ip: " + ip + "\040" + ex.toString());
            }
        }

        IPChecker ipck = new IPChecker();
        Text tip = ipck.contains(ip, cn.getConnection());

        // Broker-Kennung
        Texttyp bktyp = new Texttyp(Long.valueOf(11), cn.getConnection()); // Texttyp Brokerkennung
        Text tbk = new Text(cn.getConnection(), bktyp, rq.getParameter("bkid")); // Text mit Brokerkennung

        // Kontokennung
        Texttyp kktyp = new Texttyp(Long.valueOf(12), cn.getConnection()); // Texttyp Kontokennung
        Text tkk = new Text(cn.getConnection(), kktyp, rq.getParameter("kkid")); // Text mit Kontokennung

        cn.close();

        // erste Priorität: ist eine Kontokennung vorhanden?
        if ((tkk != null && tkk.getInhalt() != null) // anhand einer kkid im Request
                && !auth.isLogin(rq)) {
            cn = tkk;
        } else {
            // zweite Priorität: kann der Zugriff einer bekannten IP zugeordnet werden?
            if ((tip != null && tip.getInhalt() != null) // IP-basiert
                    && !auth.isLogin(rq)) {
                cn = tip;
            } else {
                // dritte Priorität: kommt der Zugriff von einem Broker
                if ((tbk != null && tbk.getInhalt() != null) // anhand einer Kontokennung
                        && !auth.isLogin(rq)) {
                    cn = tbk;
                }
            }
        }

        return cn;
    }

    /**
     * Überprüft, ob in der Session ein gültiges {@link ch.dbs.form.KontoForm}
     * vorhanden ist. Gleichzeitig darf kein userinfo vorhanden sein. Wird bei erster
     * Kontoregistration verwendet. Verhindert dass ein Konto Bibliothekar von einem
     * User angelegt werden kann.
     *
     * @param rq HttpServletRequest
     * @return auth (true/false)
     */

    public boolean isKontoform(HttpServletRequest rq) {
        boolean auth = false;

        if ((rq.getSession().getAttribute("kontoform") != null) && (rq.getSession().getAttribute("userinfo") == null)) {
            KontoForm kf = (KontoForm) rq.getSession().getAttribute("kontoform");
            if (kf.getBiblioname() != null) { // könnte ev. auch etwas anderes sein...
                auth = true;
            }
        }
        return auth;
    }

    /**
     * Kontrolliert, ob der angemeldete User ein Benutzer ist
     * @param rq
     * @return true / false
     */
    public boolean isBenutzer(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (ui.getBenutzer() != null) {
                Benutzer b = new Benutzer();
                if (ui.getBenutzer().getClass().isInstance(b)) { check = true; }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der angemeldete User ein Bibliothekar ist
     * @param rq
     * @return true / false
     */
    public boolean isBibliothekar(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (ui.getBenutzer() != null) {
                Bibliothekar b = new Bibliothekar();
                if (ui.getBenutzer().getClass().isInstance(b)) { check = true; }
            }
        }
        return check;
    }
    /**
     * Kontrolliert, ob der angemeldete User ein Administrator ist
     * @param rq
     * @return true / false
     */
    public boolean isAdmin(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (ui.getBenutzer() != null) {
                Administrator a = new Administrator();
                if (ui.getBenutzer().getClass().isInstance(a)) { check = true; }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der angemeldete User bestellen darf
     * @param rq
     * @return true / false
     */
    public boolean isBestellopt(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (ui.getBenutzer() != null) {

                if (ui.getBenutzer().isUserbestellung()) { check = true; }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User sich einloggen darf
     * @param rq
     * @return true / false
     */
    public boolean isUserlogin(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // TODO: ausser wenn Konto deaktiviert...)
            AbstractBenutzer b = ui.getBenutzer();
            Bibliothekar bib = new Bibliothekar();
            Administrator admin = new Administrator();
            if ((b.getClass().isInstance(bib)) || (b.getClass().isInstance(admin))) {
                check = true;
            }

            if (b != null) {
                // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
                if (ui.getKonto() != null) {
                    if ((b.isLoginopt()) && (ui.getKonto().isUserlogin())) {
                        check = true;
                    }
                } else {
                    // mehrere Kontos, Prüfung kommt nochmals...
                    check = true;
                }

            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User der betreffende Bestellungansehen darf
     * @param rq
     * @return true / false
     */
    public boolean isLegitimateOrder(HttpServletRequest rq, Bestellungen order) {
        boolean check = false;
        Auth auth = new Auth();

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            if ((!auth.isBenutzer(rq) && order.getId() != null
                    && ui.getKonto().getId().equals(order.getKonto().getId()))
                    || // Bibliothekare und Admins sehen Bestellungen des Kontos
                    (auth.isBenutzer(rq) && order.getId() != null && ui.getBenutzer().getId().equals(
                            order.getBenutzer().getId()))) { // User sehen nur die eigenen Bestellungen
                check = true;
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User bei SUBITO bestellen darf
     * @param rq
     * @return true / false
     */
    public boolean isUserSubitoBestellung(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // Check nach Bibliothekar / Admin => können grundsätzlich immer bestellen
            // TODO: ausser wenn Konto deaktiviert...
            AbstractBenutzer b = ui.getBenutzer();
            Bibliothekar bib = new Bibliothekar();
            Administrator a = new Administrator();
            if ((b.getClass().isInstance(bib)) || (b.getClass().isInstance(a))) {
                check = true;
            }

            if (b != null) {
                // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
                if (ui.getKonto() != null) {
                    if ((b.isUserbestellung()) && (ui.getKonto().isUserbestellung())) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User beim GBV bestellen darf
     * @param rq
     * @return true / false
     */
    public boolean isUserGBVBestellung(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // Check nach Bibliothekar / Admin => können grundsätzlich immer bestellen
            // TODO: ausser wenn Konto deaktiviert...
            AbstractBenutzer b = ui.getBenutzer();
            Bibliothekar bib = new Bibliothekar();
            Administrator a = new Administrator();
            if ((b.getClass().isInstance(bib)) || (b.getClass().isInstance(a))) {
                check = true;
            }

            if (b != null) {
                // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
                if (ui.getKonto() != null) {
                    // für User gibt es nur autom. GBV-Bestellung, deshalb muss requester-id und ISIL vorhanden sein
                    if (b.isGbvbestellung() && ui.getKonto().isGbvbestellung()
                            && ui.getKonto().getGbvrequesterid() != null && ui.getKonto().getIsil() != null) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der Useraccount aktiv ist
     * @param rq
     * @return true / false
     */
    public boolean isUserAccount(HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // Check nach Bibliothekar / Admin => sind grundsätzlich immer aktiv
            // TODO: ausser wenn Konto deaktiviert...)
            AbstractBenutzer b = ui.getBenutzer();
            Bibliothekar bib = new Bibliothekar();
            Administrator a = new Administrator();
            if ((b.getClass().isInstance(bib)) || (b.getClass().isInstance(a))) {
                check = true;
            }

            if (b != null) {
                if (ui.getKonto() != null) {
                    // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
                    if ((b.isKontostatus()) && (ui.getKonto().isKontostatus())) {
                        check = true;
                    }
                } else {
                    // mehrere Kontos, Prüfung kommt nochmals...
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der Useraccount aktiv ist
     * @param String uid
     * @return true / false
     */
    public boolean isUserAccount(String uid, Connection cn) {
        boolean check = false;

        if (uid != null) {
            AbstractBenutzer b = new AbstractBenutzer();

            if (uid.equals("0")) { // kein Kunde ausgewählt = manuelle Bestellung
                check = true;
            } else {
                b = b.getUser(Long.valueOf(uid), cn); // hier holen wir Bestellkunde
                if (!b.isKontostatus()) { check = false; } else { check = true; }
            }
        }

        return check;
    }

    /**
     * Kontrolliert die max. Anzahl Bestellungen eines Kontos pro Kalenderjahr
     * @param String rq
     * @return true / false
     */
    public boolean isMaxordersj(HttpServletRequest rq, Connection cn) {
        boolean check = false;
        int maxordersj = 0;
        Bestellungen b = new Bestellungen();

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            if (ui.getKonto().getOrderlimits() == 0) { check = true; } // falls keine Orderlimitis aktiviert sind...
            maxordersj = ui.getKonto().getMaxordersj();
            if (maxordersj == 0) { check = true; } // unlimitiert...

            // Orderlimits aktiv / maxordersj nicht unlimitiert & Limite noch nicht überschritten
            if (!check && b.allOrdersThisYearForKonto(ui.getKonto(), cn) < maxordersj) { check = true; }

        }

        return check;
    }

    /**
     * Kontrolliert die max. Anzahl Bestellungen eines Kunden pro Kalenderjahr
     * @param String uid, rq
     * @return true / false
     */
    public boolean isMaxordersutotal(String uid, HttpServletRequest rq, Connection cn) {
        boolean check = false;
        int maxordersutotal = 0;
        Bestellungen b = new Bestellungen();

        if (rq.getSession().getAttribute("userinfo") != null) {
            UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            if (ui.getKonto().getOrderlimits() == 0) { check = true; } // falls keine Orderlimitis aktiviert sind...
            maxordersutotal = ui.getKonto().getMaxordersutotal();
            if (maxordersutotal == 0) { check = true; } // unlimitiert...

            // Orderlimits aktiv / maxordersutotal nicht unbegrenzt & Limite noch nicht überschritten
            if (!check && b.countOrdersPerUser(uid, ui.getKonto(), cn) < maxordersutotal) { check = true; }

        }

        return check;
    }

}


