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

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Administrator;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Bibliothekar;
import ch.dbs.entity.Text;
import ch.dbs.form.KontoForm;
import ch.dbs.form.UserInfo;
import enums.TextType;

/**
 * Authentifizierungsklasse
 * @param args
 */
public class Auth {

    private static final String USERINFO = "userinfo";

    /**
     * Überprüft, ob eine gültige Session vorhanden ist {@link ch.dbs.form.UserInfo}<p>
     *
     * @param rq HttpServletRequest
     * @return auth (true/false)
     */

    public boolean isLogin(final HttpServletRequest rq) {
        boolean auth = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
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
    public Text grantAccess(final HttpServletRequest rq) {

        // es gibt drei mögliche Zugriffsformen, ohne eingeloggt zu sein. Priorisiert wie folgt:
        // 1. Kontokennung (überschreibt IP-basiert)
        // 2. IP-basiert (überschreibt Broker-Kennung)
        // 3. Broker-Kennung (z.B. Careum Explorer)

        Text result = new Text();
        final Text cn = new Text();
        final Auth auth = new Auth();

        // IP-basiert
        final String ip = rq.getRemoteAddr();

        try {

            final IPChecker ipck = new IPChecker();
            final Text tip = ipck.contains(ip, cn.getConnection());

            // Broker-Kennung
            final Text tbk = new Text(cn.getConnection(), TextType.ACCOUNT_ID_OVERRIDDEN_BY_IP, rq.getParameter("bkid")); // Text mit Brokerkennung

            // Kontokennung
            final Text tkk = new Text(cn.getConnection(), TextType.ACCOUNT_ID_OVERRIDES_IP, rq.getParameter("kkid")); // Text mit Kontokennung

            // erste Priorität: ist eine Kontokennung vorhanden?
            if ((tkk != null && tkk.getInhalt() != null) // anhand einer kkid im Request
                    && !auth.isLogin(rq)) {
                result = tkk;
            } else {
                // zweite Priorität: kann der Zugriff einer bekannten IP zugeordnet werden?
                if ((tip != null && tip.getInhalt() != null) // IP-basiert
                        && !auth.isLogin(rq)) {
                    result = tip;
                } else {
                    // dritte Priorität: kommt der Zugriff von einem Broker
                    if ((tbk != null && tbk.getInhalt() != null) // anhand einer Kontokennung
                            && !auth.isLogin(rq)) {
                        result = tbk;
                    }
                }
            }

        } finally {
            cn.close();
        }

        return result;
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

    public boolean isKontoform(final HttpServletRequest rq) {
        boolean auth = false;

        if ((rq.getSession().getAttribute("kontoform") != null) && (rq.getSession().getAttribute(USERINFO) == null)) {
            final KontoForm kf = (KontoForm) rq.getSession().getAttribute("kontoform");
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
    public boolean isBenutzer(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            if (ui.getBenutzer() != null) {
                final Benutzer b = new Benutzer();
                if (ui.getBenutzer().getClass().isInstance(b)) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der angemeldete User ein Bibliothekar ist
     * @param rq
     * @return true / false
     */
    public boolean isBibliothekar(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            if (ui.getBenutzer() != null) {
                final Bibliothekar b = new Bibliothekar();
                if (ui.getBenutzer().getClass().isInstance(b)) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der angemeldete User ein Administrator ist
     * @param rq
     * @return true / false
     */
    public boolean isAdmin(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            if (ui.getBenutzer() != null) {
                final Administrator a = new Administrator();
                if (ui.getBenutzer().getClass().isInstance(a)) {
                    check = true;
                }
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der angemeldete User bestellen darf
     * @param rq
     * @return true / false
     */
    public boolean isBestellopt(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            if (ui.getBenutzer() != null && ui.getBenutzer().isUserbestellung()) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User sich einloggen darf
     * @param rq
     * @return true / false
     */
    public boolean isUserlogin(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            // TODO: ausser wenn Konto deaktiviert...)
            final AbstractBenutzer b = ui.getBenutzer();
            final Bibliothekar bib = new Bibliothekar();
            final Administrator admin = new Administrator();
            if (b.getClass().isInstance(bib) || b.getClass().isInstance(admin)) {
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
    public boolean isLegitimateOrder(final HttpServletRequest rq, final Bestellungen order) {
        boolean check = false;
        final Auth auth = new Auth();

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);

            if ((!auth.isBenutzer(rq) && order.getId() != null && ui.getKonto().getId()
                    .equals(order.getKonto().getId()))
                    || // Bibliothekare und Admins sehen Bestellungen des Kontos
                    (auth.isBenutzer(rq) && order.getId() != null && ui.getBenutzer().getId()
                            .equals(order.getBenutzer().getId()))) { // User sehen nur die eigenen Bestellungen
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
    public boolean isUserSubitoBestellung(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            // Check nach Bibliothekar / Admin => können grundsätzlich immer bestellen
            // TODO: ausser wenn Konto deaktiviert...
            final AbstractBenutzer b = ui.getBenutzer();
            final Bibliothekar bib = new Bibliothekar();
            final Administrator a = new Administrator();
            if (b.getClass().isInstance(bib) || b.getClass().isInstance(a)) {
                check = true;
            }

            // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
            if (b != null && ui.getKonto() != null && b.isUserbestellung() && ui.getKonto().isUserbestellung()) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der User beim GBV bestellen darf
     * @param rq
     * @return true / false
     */
    public boolean isUserGBVBestellung(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            // Check nach Bibliothekar / Admin => können grundsätzlich immer bestellen
            // TODO: ausser wenn Konto deaktiviert...
            final AbstractBenutzer b = ui.getBenutzer();
            final Bibliothekar bib = new Bibliothekar();
            final Administrator a = new Administrator();
            if (b.getClass().isInstance(bib) || b.getClass().isInstance(a)) {
                check = true;
            }
            // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
            if (b != null && ui.getKonto() != null && b.isGbvbestellung() && ui.getKonto().isGbvbestellung()
                    && ui.getKonto().getGbvrequesterid() != null && ui.getKonto().getIsil() != null) {
                // für User gibt es nur autom. GBV-Bestellung, deshalb muss requester-id und ISIL vorhanden sein
                check = true;
            }
        }
        return check;
    }

    /**
     * Kontrolliert, ob der Useraccount aktiv ist
     * @param rq
     * @return true / false
     */
    public boolean isUserAccount(final HttpServletRequest rq) {
        boolean check = false;

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);
            // Check nach Bibliothekar / Admin => sind grundsätzlich immer aktiv
            // TODO: ausser wenn Konto deaktiviert...)
            final AbstractBenutzer b = ui.getBenutzer();
            final Bibliothekar bib = new Bibliothekar();
            final Administrator a = new Administrator();
            if (b.getClass().isInstance(bib) || b.getClass().isInstance(a)) {
                check = true;
            }

            if (b != null) {
                if (ui.getKonto() != null) {
                    // Sowohl generelle Kontoeinstellungen als auch Berechtigung beim User müssen vorhanden sein...
                    if (b.isKontostatus() && ui.getKonto().isKontostatus()) {
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
    public boolean isUserAccount(final String uid, final Connection cn) {
        boolean check = false;

        if (uid != null) {
            AbstractBenutzer b = new AbstractBenutzer();

            if ("0".equals(uid)) { // kein Kunde ausgewählt = manuelle Bestellung
                check = true;
            } else {
                b = b.getUser(Long.valueOf(uid), cn); // hier holen wir Bestellkunde
                if (b.isKontostatus()) {
                    check = true;
                }
            }
        }

        return check;
    }

    /**
     * Kontrolliert die max. Anzahl Bestellungen eines Kontos pro Kalenderjahr
     * @param String rq
     * @return true / false
     */
    public boolean isMaxordersj(final HttpServletRequest rq, final Connection cn) {
        boolean check = false;
        int maxordersj = 0;
        final Bestellungen b = new Bestellungen();

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);

            if (ui.getKonto().getOrderlimits() == 0) {
                check = true;
            } // falls keine Orderlimitis aktiviert sind...
            maxordersj = ui.getKonto().getMaxordersj();
            if (maxordersj == 0) {
                check = true;
            } // unlimitiert...

            // Orderlimits aktiv / maxordersj nicht unlimitiert & Limite noch nicht überschritten
            if (!check && b.allOrdersThisYearForKonto(ui.getKonto(), cn) < maxordersj) {
                check = true;
            }

        }

        return check;
    }

    /**
     * Kontrolliert die max. Anzahl Bestellungen eines Kunden pro Kalenderjahr
     * @param String uid, rq
     * @return true / false
     */
    public boolean isMaxordersutotal(final String uid, final HttpServletRequest rq, final Connection cn) {
        boolean check = false;
        int maxordersutotal = 0;
        final Bestellungen b = new Bestellungen();

        if (rq.getSession().getAttribute(USERINFO) != null) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute(USERINFO);

            if (ui.getKonto().getOrderlimits() == 0) {
                check = true;
            } // falls keine Orderlimitis aktiviert sind...
            maxordersutotal = ui.getKonto().getMaxordersutotal();
            if (maxordersutotal == 0) {
                check = true;
            } // unlimitiert...

            // Orderlimits aktiv / maxordersutotal nicht unbegrenzt & Limite noch nicht überschritten
            if (!check && b.countOrdersPerUser(uid, ui.getKonto(), cn) < maxordersutotal) {
                check = true;
            }

        }

        return check;
    }

}
