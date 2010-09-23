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

package ch.dbs.actions.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.Encrypt;
import util.MHelper;
import util.PasswordGenerator;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Benutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.entity.VKontoBenutzer;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.KontoForm;
import ch.dbs.form.LoginForm;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.OrderStatistikForm;
import ch.dbs.form.OverviewForm;
import ch.dbs.form.SearchesForm;
import ch.dbs.form.UserForm;
import ch.dbs.form.UserInfo;
import ch.dbs.login.Gtc;

public final class UserAction extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(UserAction.class);
    private static final int FIRST_YEAR = 2007; // the first year relevant for statistic for this installation
    private static final String FAILURE = "failure";
    private static final String SUCCESS = "success";
    private static final String UEBERSICHT = "uebersicht";
    private static final String ACTIVEMENUS = "ActiveMenus";
    private static final String ERRORMESSAGE = "errormessage";


    public ActionForward stati(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = FAILURE;
        final Auth auth = new Auth();

        //    Make sure user is logged in
        if (auth.isLogin(rq)) {

            forward = SUCCESS;

            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu(UEBERSICHT);
            rq.setAttribute(ACTIVEMENUS, mf);

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        return mp.findForward(forward);
    }

    /**
     *  Übersicht Zeigt Bestellungen an. Kann durch Benutzer festgelegt werden, nach welchem
     *  Feld er ab- und aufsteigend sortieren will
     */
    public ActionForward overview(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;

        OverviewForm of = (OverviewForm) fm;
        final OrderStatistikForm osf = new OrderStatistikForm();
        final Bestellungen b = new Bestellungen();

        final Check check = new Check();
        final Auth auth = new Auth();
        final Text cn = new Text();

        if (auth.isLogin(rq)) {
            forward = SUCCESS;
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu(UEBERSICHT);
            rq.setAttribute(ACTIVEMENUS, mf);

            // hier wird geprüft, ob Suchkriterien eingegeben wurden, oder die Sortierung einer Suche vorliegt
            if (!checkIfInputIsNotEmpty(of) && !of.isS()) {

                // Auflistung möglicher Bestellstati um Statuswechsel dem User / Bibliothekar / Admin anzubieten
                // wird auch für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
                of.setStatitexts(cn.getAllTextPlusKontoTexts(
                        new Texttyp("Status", cn.getConnection()), ui.getKonto().getId(), cn.getConnection()));

                // angegebener Zeitraum prüfen, resp. Defaultbereich von 1 Monat zusammenstellen
                of = check.checkDateRegion(of, 1, ui.getKonto().getTimezone());
                // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
                of = check.checkSortOrderValues(of);
                //Check, damit nur gültige Sortierkriterien daherkommen
                of = check.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of);
                // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
                of = check.checkOrdersSortCriterias(of);

                // Benutzer dürfen nur ihre eigenen Bestellungen sehen
                if (!auth.isBibliothekar(rq) && !auth.isAdmin(rq)) {
                    if (of.getFilter() == null) {
                        of.setBestellungen(b.getAllUserOrders(ui.getBenutzer(), of.getSort(), of.getSortorder(),
                                of.getFromdate(), of.getTodate(), cn.getConnection()));
                    } else {
                        of.setBestellungen(b.getAllUserOrdersPerStatus(ui.getBenutzer(), of.getFilter(), of.getSort(),
                                of.getSortorder(), of.getFromdate(), of.getTodate(), false, cn.getConnection()));
                    }
                } else { // Bibliothekare dürfen nur Bestellungen ihrers Kontos sehen
                    if (of.getFilter() == null) {
                        of.setBestellungen(b.getOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(),
                                of.getFromdate(), of.getTodate(), cn.getConnection()));
                        osf.setAuflistung(b.countOrdersPerKonto(ui.getKonto(), of.getSort(), of.getSortorder(),
                                of.getFromdate(), of.getTodate(), cn.getConnection()));
                    } else {
                        of.setBestellungen(b.getOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(),
                                of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), false,
                                cn.getConnection()));
                        osf.setAuflistung(b.countOrdersPerKontoPerStatus(ui.getKonto().getId(), of.getFilter(),
                                of.getSort(), of.getSortorder(), of.getFromdate(), of.getTodate(), false,
                                cn.getConnection()));
                    }
                }

                // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
                final Date d = new Date(); // aktuelles Datum setzen
                final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
                final String datum = fmt.format(d, ui.getKonto().getTimezone());
                int yearNow = Integer.parseInt(datum);
                int yearStart = FIRST_YEAR;

                final ArrayList<Integer> years = new ArrayList<Integer>();
                yearNow++;
                for (int j = 0; yearStart < yearNow; j++) {
                    years.add(j, yearStart);
                    yearStart++;
                }
                of.setYears(years);

                // Suchfelder bestimmen
                final Map<String, String> result = composeSortedLocalisedOrderSearchFields(rq);
                rq.setAttribute("sortedSearchFields", result);

                rq.setAttribute("overviewform", of);
                rq.setAttribute("orderstatistikform", osf);

                cn.close();

            } else { // Umleitung auf Suche
                forward = "search";
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        return mp.findForward(forward);
    }

    /**
     * Wechselt den Status einer Bestellung, unter Berücksichtigung allfällig bestehender Sortierung und Filterung
     */
    public ActionForward changestat(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final OverviewForm of = (OverviewForm) form;
        final OrderState orderstate = new OrderState();
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            final Text t = new Text();

            try {
                final Text status = new Text(t.getConnection(), of.getTid());
                final Bestellungen b = new Bestellungen(t.getConnection(), of.getBid());
                if (b != null && status != null) {
                    orderstate.changeOrderState(b, ui.getKonto().getTimezone(), status, null,
                            ui.getBenutzer().getEmail(), t.getConnection());
                    forward = SUCCESS;
                    rq.setAttribute("overviewform", of);
                }

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("changestat: " + e.toString());

            } finally {
                t.close();
            }
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }
        return mp.findForward(forward);
    }

    /**
     * Wechselt den Status einer Bestellung, unter Berücksichtigung allfällig bestehender Sortierung und Filterung
     */
    public ActionForward changenotes(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final OverviewForm of = (OverviewForm) form;
        // Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {

            final Text cn = new Text();

            try {
                final Bestellungen b = new Bestellungen(cn.getConnection(), of.getBid());
                b.setNotizen(of.getNotizen());
                if (b != null) {
                    b.update(cn.getConnection());
                }
                forward = SUCCESS;
                rq.setAttribute("overviewform", of);

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("changenotes: " + e.toString());

            } finally {
                cn.close();
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }
        return mp.findForward(forward);
    }

    /**
     * Konto wechseln
     */
    public ActionForward changekonto(final ActionMapping mp,
            final ActionForm form,
            final HttpServletRequest rq,
            final HttpServletResponse rp) {

        //    Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        final KontoForm kf = (KontoForm) form;
        final OrderForm pageForm = new OrderForm(kf);
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            final Konto k = new Konto();
            ui.setKonto(new Konto(kf.getKid(), k.getConnection()));
            k.close();
            rq.getSession().setAttribute("userinfo", ui);

            if (auth.isUserlogin(rq) && auth.isUserAccount(rq)) {
                forward = SUCCESS;
            } else {
                rq.getSession().setAttribute("userinfo", null); // userinfo aus Session löschen
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("suchenbestellen");
            rq.setAttribute(ACTIVEMENUS, mf);

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        if (kf.isResolver()) {
            // hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
            rq.setAttribute("orderform", pageForm); // Übergabe in jedem Fall
            // Die Bestellberechtigung wird in der Methode prepare geprüft!
            if (forward.equals(SUCCESS) && auth.isBenutzer(rq)) { forward = "order"; }
            // Bibliothekar oder Admin auf Checkavailability
            if (forward.equals(SUCCESS) && !auth.isBenutzer(rq)) { forward = "checkavailability"; }
        }
        if (kf.getKundenemail() != null && !kf.getKundenemail().equals("")) {
            // Übergabe von Userangaben von Link aus Bestellform-Email
            if (forward.equals(SUCCESS) && !auth.isBenutzer(rq)) { forward = "adduser"; }
            final UserForm uf = new UserForm(kf);
            rq.setAttribute("userform", uf);
        }

        return mp.findForward(forward);
    }

    /**
     * Benutzer setzen wenn Logininfos Login bei verschiedenen konten zulassen
     */
    public ActionForward setuser(final ActionMapping mp,
            final ActionForm form,
            final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final LoginForm authuserlist = (LoginForm) rq.getSession().getAttribute("authuserlist");
        final UserInfo ui = new UserInfo();
        final LoginForm lf = (LoginForm) form; // Infos mit welchem Benutzer gearbeitet werden soll

        String forward = FAILURE;
        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("login");

        final Auth auth = new Auth();
        final Text cn = new Text();

        //  Ueberprüfung ob Auswahl aus LoginForm tatsächlich authorisierte Benutzer sind
        final Gtc gtc = new Gtc();
        for (final UserInfo authlist : authuserlist.getUserinfolist()) {
            if (lf.getUserid().equals(authlist.getBenutzer().getId())) {

                //Benutzer und Kontos in ui setzen
                ui.setBenutzer(authlist.getBenutzer());
                ui.setKontos(authlist.getKontos());
                rq.getSession().setAttribute("userinfo", ui);

                //Bei nur einem Konto dieses gleich setzen
                if (ui.getKontos().size() == 1) {
                    ui.setKonto(ui.getKontos().get(0));
                    // Last-Login Datum beim Benutzer hinterlegen
                    final AbstractBenutzer u = ui.getBenutzer();
                    u.updateLastuse(u, ui.getKonto(), cn.getConnection());


                    if (gtc.isAccepted(ui.getBenutzer(), cn.getConnection())) {
                        forward = SUCCESS;
                        mf.setActivemenu("suchenbestellen");
                    } else {
                        forward = "gtc";
                    }
                }
                //Falls Benutzer unter mehreren Kontos arbeiten darf weiterleitung zur Kontoauswahl
                if (ui.getKontos().size() > 1) {
                    // Last-Login Datum beim Benutzer hinterlegen
                    final AbstractBenutzer u = ui.getBenutzer();
                    u.updateLastuse(u, ui.getKontos().get(0), cn.getConnection());
                    forward = "kontochoose";
                }
            }
        }

        cn.close();

        // Fehlermeldung bereitstellen falls mittels URL-hacking versucht wurde zu manipulieren
        if (forward.equals(FAILURE)) {
            rq.getSession().setAttribute("userinfo", null);
            final ErrorMessage em = new ErrorMessage("error.hack", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
            LOG.info("setuser: prevented URL-hacking! " + ui.getBenutzer().getEmail());
        }

        // Angaben vom Linkresolver
        if (lf.isResolver()) {
            // hier kommen auch Artikelangaben aus der Übergabe des Linkresolvers mit...
            rq.setAttribute("orderform", lf); // Übergabe in jedem Fall
            // Die Bestellberechtigung wird in der Methode prepare geprüft!
            if (forward.equals(SUCCESS) && auth.isBenutzer(rq)) { forward = "order"; }
            // Bibliothekar oder Admin auf Checkavailability
            if (forward.equals(SUCCESS) && !auth.isBenutzer(rq)) { forward = "checkavailability"; }
        }

        rq.setAttribute(ACTIVEMENUS, mf);
        return mp.findForward(forward);
    }

    /**
     * Dient dazu beim Aufruf von prepareAddUser ab journalorder.jsp, alle Bestellangaben zu behalten...
     */
    public ActionForward keepOrderDetails(final ActionMapping mp,
            final ActionForm form,
            final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
            forward = SUCCESS;
            final OrderForm pageForm = (OrderForm) form;
            rq.getSession().setAttribute("ofjo", pageForm);

            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // damit kann in prepareAddUser kontrolliert werden, dass odervalues dabei sind!
            ui.setKeepordervalues(true);
            rq.getSession().setAttribute("userinfo", ui);

        } else {
            if (auth.isLogin(rq)) {
                final ErrorMessage em = new ErrorMessage("error.berechtigung");
                rq.setAttribute(ERRORMESSAGE, em);
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }
        }
        return mp.findForward(forward);
    }


    /**
     * Hinzufuegen eines neuen Benutzers vorbereiten
     */
    public ActionForward prepareAddUser(final ActionMapping mp,
            final ActionForm form,
            final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = FAILURE;
        ErrorMessage em = new ErrorMessage();
        final Text cn = new Text();
        final Countries countriesInstance = new Countries();
        final Konto kontoInstance = new Konto();
        final Auth auth = new Auth();

        if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

            if (ui.isKeepordervalues()) { // hier wird festegestellt, ob keepOrderDetails durchlaufen wurde
                ui.setKeepordervalues(false);
                ui.setKeepordervalues2(false);
                // keepordervalues aus Session löschen, damit nicht etwas hängen bleibt...
                rq.getSession().setAttribute("userinfo", ui);
                ui.setKeepordervalues2(true);
            } else {
                // ggf. orderform unterdrücken, welches noch in der Session hängt...
                rq.getSession().setAttribute("ofjo", null);
            }

            final List<Konto> allPossKontos = kontoInstance.getAllAllowedKontosAndSelectActive(ui, cn.getConnection());
            final ArrayList<KontoForm> lkf = new ArrayList<KontoForm>();

            for (final Konto k : allPossKontos) {
                final KontoForm kf = new KontoForm();
                kf.setKonto(k);
                lkf.add(kf);
            }

            final List<Countries> allPossCountries = countriesInstance.getAllCountries(cn.getConnection());

            ui.setKontos(allPossKontos);
            ui.setCountries(allPossCountries);
            forward = SUCCESS;
            rq.setAttribute("ui", ui);

        } else {
            if (auth.isLogin(rq)) {
                em = new ErrorMessage("error.berechtigung");
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                em = new ErrorMessage("error.timeout", "login.do");
            }
        }
        rq.setAttribute(ERRORMESSAGE, em); // unterdrückt falsche Fehlermeldungen aus Bestellform...
        cn.close();
        return mp.findForward(forward);
    }


    /**
     * Benutzereinstellungen ändern
     */
    public ActionForward changeuserdetails(final ActionMapping mp,
            final ActionForm form,
            final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = FAILURE;
        ErrorMessage em = new ErrorMessage();
        final Countries countriesInstance = new Countries();
        final Text cn = new Text();
        final Auth auth = new Auth();
        final VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();

        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                final UserForm uf = (UserForm) form;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                AbstractBenutzer u = new AbstractBenutzer();
                u = u.getUser(uf.getBid(), cn.getConnection());

                // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
                if (u != null && vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), u.getId(), cn.getConnection())) {

                    uf.setUser(u);

                    // selektiert alle Konten unter denen ein Kunde angehängt ist
                    final List<Konto> kontolist = ui.getKonto().getKontosForBenutzer(u, cn.getConnection());
                    final List<Konto> allPossKontos = ui.getKonto().getLoginKontos(ui.getBenutzer(), cn.getConnection());
                    for (final Konto k : kontolist) {
                        int y = 0;
                        for (final Konto uik : allPossKontos) {
                            if (uik.getId().longValue() == k.getId().longValue()) {
                                uik.setSelected(true);
                                allPossKontos.set(y, uik);
                            }
                            y++;
                        }
                    }
                    final ArrayList<KontoForm> lkf = new ArrayList<KontoForm>();
                    for (final Konto k : allPossKontos) {
                        final KontoForm kf = new KontoForm();
                        kf.setKonto(k);
                        lkf.add(kf);
                    }

                    final List<Countries> allPossCountries = countriesInstance.getAllCountries(cn.getConnection());
                    ui.setKontos(allPossKontos);
                    ui.setCountries(allPossCountries);
                    forward = SUCCESS;
                    rq.setAttribute("ui", ui);
                    rq.setAttribute("userform", uf);

                } else {
                    forward = FAILURE;
                    em = new ErrorMessage("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    LOG.info("changeuserdetails: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }

            } else {
                em = new ErrorMessage("error.berechtigung");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            em = new ErrorMessage("error.timeout", "login.do");
        }
        rq.setAttribute(ERRORMESSAGE, em); // unterdrückt falsche Fehlermeldungen aus Bestellform...
        cn.close();
        return mp.findForward(forward);
    }

    /**
     * Generiert ein neues Passwort und teilt dem Benutzer dieses Mail mit
     */
    public ActionForward pwreset(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final LoginForm lf = (LoginForm) form;
        String forward = FAILURE;
        final Text cn = new Text();
        final MessageResources messageResources = getResources(rq);
        final ErrorMessage em = new ErrorMessage();
        AbstractBenutzer u = new AbstractBenutzer();
        u = u.getUserFromEmail(lf.getEmail(), cn.getConnection());
        //Check ob der Benutzer berechtigt ist sein Passwort zurückzusetzen
        if (u != null) {
            if (u.isLoginopt() || u.getRechte() > 1) {
                // Passwort erzeugen
                final PasswordGenerator p = new PasswordGenerator(8);
                final String pw = p.getRandomString();
                // Passwort codieren und in DB speichern
                final Encrypt e = new Encrypt();
                u.setPassword(e.makeSHA(pw));
                final Konto tz = new Konto(); // we need this for setting a default timezone
                u.updateUser(u, tz, cn.getConnection());
                // Benutzer per Mail das neue Passwort mitteilen
                final String[] recipients = new String[1];
                recipients[0] = u.getEmail();
                final StringBuffer msg = new StringBuffer();
                msg.append(messageResources.getMessage("resend.email.intro"));
                msg.append("\n\n");
                msg.append(pw);
                msg.append("\n\n");
                msg.append(messageResources.getMessage("resend.email.greetings"));
                msg.append('\n');
                msg.append(messageResources.getMessage("resend.email.team"));
                msg.append('\040');
                msg.append(ReadSystemConfigurations.getApplicationName());
                final MHelper m = new MHelper();
                m.sendMail(recipients, messageResources.getMessage("resend.email.subject")
                        + "\040" + ReadSystemConfigurations.getApplicationName(),
                        msg.toString());
                forward = SUCCESS;
                rq.setAttribute("message", new Message("message.pwreset", "login.do"));

            } else {
                em.setError("error.pwreset");
                em.setLink("login.do");
                rq.setAttribute(ERRORMESSAGE, em);
                log.warn(lf.getEmail() + " ist registriert und hat versucht sich "
                        + "unberechtigterweise eine Loginberechtigung per Email zu schicken!");
            }
        } else {
            em.setError("error.unknown_email");
            em.setLink("login.do");
            rq.setAttribute(ERRORMESSAGE, em);
            log.warn(lf.getEmail() + " (unbekannt) hat versucht sich "
                    + "unberechtigterweise eine Loginberechtigung per Email zu schicken!");
        }
        cn.close();
        return mp.findForward(forward);
    }

    /**
     * Uebersicht Benutzer des Kontos
     */
    public ActionForward showkontousers(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;
        final UserForm uf = new UserForm();
        final Auth auth = new Auth();

        if (auth.isLogin(rq)) {
            forward = SUCCESS;
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            // Bei URL-hacking: User sehen nur sich selber
            if (auth.isBenutzer(rq)) {
                final ArrayList<AbstractBenutzer> ul = new ArrayList<AbstractBenutzer>();
                ul.add(ui.getBenutzer());
                uf.setUsers(ul);
            }
            // Bibliothekare sehen alle Benutzer eines Kontos
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                final Text t = new Text();
                uf.setUsers(ui.getBenutzer().getKontoUser(ui.getKonto(), t.getConnection()));
                t.close();
            }
            rq.setAttribute("userform", uf);
        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }
        return mp.findForward(forward);
    }

    /**
     * neuer User anlegen abschliessen, User bearbeiten
     */
    public ActionForward modifykontousers(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;
        final Text cn = new Text();
        final Check check = new Check();
        final Auth auth = new Auth();
        final VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();

        if (auth.isLogin(rq)) {
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                final UserForm uf = (UserForm) fm;

                // ggf. Leerschläge entfernen
                if (uf.getEmail() != null) { uf.setEmail(uf.getEmail().trim()); }

                final boolean name = check.isMinLength(uf.getName(), 2);
                final boolean vorname = check.isMinLength(uf.getVorname(), 2);
                final boolean email = check.isEmail(uf.getEmail());
                final boolean kont = uf.getKontos() != null;
                final boolean land = check.isMinLength(uf.getLand(), 2);

                //Test, ob alle Sollangaben gemacht wurden
                if (name && vorname && email && kont && land) {

                    final String[] kontos = (String[]) uf.getKontos();

                    forward = SUCCESS;
                    AbstractBenutzer u = new Benutzer();

                    if (uf.getBid() != null) {
                        u = u.getUser(uf.getBid(), cn.getConnection());
                    }

                    u.setAnrede(uf.getAnrede());
                    if (uf.getVorname() != null) {
                        u.setVorname(uf.getVorname().trim());
                    } else {
                        u.setVorname(uf.getVorname());
                    }
                    if (uf.getName() != null) {
                        u.setName(uf.getName().trim());
                    } else {
                        u.setName(uf.getName());
                    }
                    if (uf.getEmail() != null) {
                        u.setEmail(uf.getEmail().trim());
                    } else {
                        u.setEmail(uf.getEmail());
                    }
                    if (uf.getTelefonnrg() != null) {
                        u.setTelefonnrg(uf.getTelefonnrg().trim());
                    } else {
                        u.setTelefonnrg(uf.getTelefonnrg());
                    }
                    if (uf.getTelefonnrp() != null) {
                        u.setTelefonnrp(uf.getTelefonnrp().trim());
                    } else {
                        u.setTelefonnrp(uf.getTelefonnrp());
                    }
                    if (uf.getInstitut() != null) {
                        u.setInstitut(uf.getInstitut().trim());
                    } else {
                        u.setInstitut(uf.getInstitut());
                    }
                    if (uf.getAbteilung() != null) {
                        u.setAbteilung(uf.getAbteilung().trim());
                    } else {
                        u.setAbteilung(uf.getAbteilung());
                    }
                    if (uf.getAdresse() != null) {
                        u.setAdresse(uf.getAdresse().trim());
                    } else {
                        u.setAdresse(uf.getAdresse());
                    }
                    if (uf.getAdresszusatz() != null) {
                        u.setAdresszusatz(uf.getAdresszusatz().trim());
                    } else {
                        u.setAdresszusatz(uf.getAdresszusatz());
                    }
                    if (uf.getPlz() != null) {
                        u.setPlz(uf.getPlz().trim());
                    } else {
                        u.setPlz(uf.getPlz());
                    }
                    if (uf.getOrt() != null) {
                        u.setOrt(uf.getOrt().trim());
                    } else {
                        u.setOrt(uf.getOrt());
                    }
                    u.setLand(uf.getLand());
                    u.setLoginopt(uf.isLoginopt());
                    u.setKontostatus(uf.isKontostatus());
                    final Encrypt e = new Encrypt();
                    u.setPassword(e.makeSHA(uf.getPassword()));
                    u.setUserbestellung(uf.isUserbestellung()); // SUBITO
                    u.setGbvbestellung(uf.isGbvbestellung()); // GBV
                    u.setKontovalidation(uf.isKontovalidation());
                    u.setBilling(uf.getBilling());
                    u.setGtc(uf.getGtc());
                    u.setGtcdate(uf.getGtcdate());

                    if (uf.getBid() == null) {
                        final Date d = new Date();
                        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String datum = fmt.format(d, ui.getKonto().getTimezone());
                        u.setDatum(datum);
                        uf.setBid(u.saveNewUser(u, ui.getKonto(), cn.getConnection()));
                        // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
                    } else if (vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), uf.getBid(), cn.getConnection())) {
                        u.setId(uf.getBid());
                        u.updateUser(u, ui.getKonto(), cn.getConnection());
                    } else {
                        ErrorMessage em = new ErrorMessage();
                        forward = FAILURE;
                        em = new ErrorMessage("error.hack");
                        em.setLink("searchfree.do?activemenu=suchenbestellen");
                        rq.setAttribute(ERRORMESSAGE, em);
                        LOG.info("modifykontousers: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                    }

                    // Sicherstellen, dass nur Kunden vom eigenen Konto bearbeitet werden können...
                    if (u.getId() == null
                            || vKontoBenutzer.isUserFromKonto(ui.getKonto().getId(), u.getId(), cn.getConnection())) {

                        if (u.getId() != null) { vKontoBenutzer.deleteAllKontoEntries(u, cn.getConnection()); }
                        for (final String konto : kontos) {
                            final Konto k = new Konto(Long.parseLong(konto), cn.getConnection());
                            vKontoBenutzer.setKontoUser(u, k, cn.getConnection());
                        }

                        final AbstractBenutzer b = new AbstractBenutzer(uf);
                        uf.setUser(b);

                        // d.h. hier wurde ein User angelegt aus der journalorder.jsp
                        // => mit Bestellangaben wieder direkt zurück
                        if (uf.isKeepordervalues2() && (rq.getSession().getAttribute("ofjo") != null)) {
                            uf.setKeepordervalues2(false);
                            forward = "returntojournalorder";
                            OrderForm pageForm = new OrderForm();
                            pageForm = (OrderForm) rq.getSession().getAttribute("ofjo");
                            pageForm.setForuser(uf.getBid().toString()); // Vorselektion neu angelegter User
                            pageForm.setUid(uf.getBid().toString()); // Vorselektion neu angelegter User
                            if (pageForm.getUid() == null) { pageForm.setUid("0"); } // sonst kracht es auf jsp
                            rq.setAttribute("ofjo", pageForm);

                            if (pageForm.getOrigin() != null) {
                                forward = "returntojournalsave";
                            }
                            rq.getSession().setAttribute("ofjo", null);
                        }
                        rq.setAttribute("userform", uf);
                    }

                } else {
                    final ErrorMessage em = new ErrorMessage();
                    if (!vorname) { em.setError("error.vorname"); }
                    if (!name) { em.setError("error.name"); }
                    if (!email) { em.setError("error.mail"); }
                    if (!kont) { em.setError("error.kontos"); }
                    if (!land) { em.setError("error.land"); }
                    rq.setAttribute(ERRORMESSAGE, em);
                    final AbstractBenutzer b = new AbstractBenutzer(uf);
                    uf.setUser(b);
                    rq.setAttribute("userform", uf);
                    forward = "missing";
                }
            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }
        cn.close();
        final Message m = new Message("message.modifyuser");
        m.setLink("listkontousers.do?method=showkontousers&activemenu=bibliokunden");
        rq.setAttribute("message", m);
        return mp.findForward(forward);
    }
    /**
     * User aus Konto löschen,oder gänzlich wenn er keinem Konto mehr angehört
     *
     */
    public ActionForward deleteKontousers(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        //Sicherstellen, dass die Action nur von eingeloggten Benutzern aufgerufen wird
        String forward = FAILURE;
        final Text cn = new Text();
        final Auth auth = new Auth();
        final VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();

        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                final Benutzer u = new Benutzer();
                final UserForm uf = (UserForm) fm;
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final Konto k = ui.getKonto();
                if (vKontoBenutzer.isUserFromKonto(k.getId(), uf.getBid(), cn.getConnection())) {
                    forward = SUCCESS;

                    if (uf.getBid() != null) {
                        u.setId(uf.getBid());
                        final VKontoBenutzer vkb = new VKontoBenutzer();
                        vkb.deleteSingleKontoEntry(u, k, cn.getConnection());
                    }

                    final List<Konto> rkv = ui.getKonto().getLoginKontos(u, cn.getConnection());
                    final AbstractBenutzer b = new AbstractBenutzer();
                    if (rkv.isEmpty()) { b.deleteUser(u, cn.getConnection()); }

                    rq.setAttribute("userform", uf);
                } else {
                    ErrorMessage em = new ErrorMessage();
                    em = new ErrorMessage("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute(ERRORMESSAGE, em);
                    LOG.info("modifykontousers: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }
        cn.close();

        final Message m = new Message("message.deleteuser");
        m.setLink("listkontousers.do?method=showkontousers&activemenu=bibliokunden");
        rq.setAttribute("message", m);

        return mp.findForward(forward);
    }

    /**
     * Bereitet die Suche nach Bestellungen vor
     */
    public ActionForward prepareSearch(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = FAILURE;
        final Auth auth = new Auth();
        final Text cn = new Text();

        if (auth.isLogin(rq)) {

            OverviewForm of = (OverviewForm) fm;
            final OrderStatistikForm osf = new OrderStatistikForm();
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                forward = SUCCESS;

                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(UEBERSICHT);
                rq.setAttribute(ACTIVEMENUS, mf);

                final Check check = new Check();
                // angegebener Zeitraum prüfen, resp. Defaultbereich von 3 Monaten zusammenstellen
                of = check.checkDateRegion(of, 3, ui.getKonto().getTimezone());

                // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
                final Date d = new Date(); // aktuelles Datum setzen
                final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
                final String datum = fmt.format(d, ui.getKonto().getTimezone());
                int yearNow = Integer.parseInt(datum);
                int yearStart = FIRST_YEAR;

                final ArrayList<Integer> years = new ArrayList<Integer>();
                yearNow++;
                for (int j = 0; yearStart < yearNow; j++) {
                    years.add(j, yearStart);
                    yearStart++;
                }
                of.setYears(years);

                // Suchfelder bestimmen
                final Map<String, String> result = composeSortedLocalisedOrderSearchFields(rq);
                rq.setAttribute("sortedSearchFields", result);

                final Texttyp tty = new Texttyp();
                long id = 2; // Bestellstati
                tty.setId(id);
                of.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                id = 7; // Waehrungen
                tty.setId(id);
                of.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                cn.close();

                rq.setAttribute("overviewform", of);
                rq.setAttribute("orderstatistikform", osf);

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }


        return mp.findForward(forward);
    }

    /**
     * Suche nach Bestellungen
     */
    public ActionForward search(final ActionMapping mp, final ActionForm fm,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        String forward = FAILURE;
        final Auth auth = new Auth();
        final Bestellungen b = new Bestellungen();

        if (auth.isLogin(rq)) {

            OverviewForm of = (OverviewForm) fm;
            final OrderStatistikForm osf = new OrderStatistikForm();
            final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
            final Check check = new Check();
            final Text cn = new Text();

            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                forward = "result";

                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu(UEBERSICHT);
                rq.setAttribute(ACTIVEMENUS, mf);

                // wird für checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts benötigt
                of.setStatitexts(cn.getAllTextPlusKontoTexts(
                        new Texttyp("Status", cn.getConnection()), ui.getKonto().getId(), cn.getConnection()));

                // angegebener Zeitraum prüfen, resp. Defaultbereich von 3 Monaten zusammenstellen
                of = check.checkDateRegion(of, 3, ui.getKonto().getTimezone());
                // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
                of = check.checkSortOrderValues(of);
                //Check, damit nur gültige Sortierkriterien daherkommen
                of = check.checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(of);
                // Ueberprüfung der Sortierkriterien, ob diese gültig sind. Wenn ja, Sortierung anwenden
                of = check.checkOrdersSortCriterias(of);

                // angezeigter Jahresbereich im Select festlegen: 2007 bis aktuelles Jahr
                final Date d = new Date(); // aktuelles Datum setzen
                final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
                final String datum = fmt.format(d, ui.getKonto().getTimezone());
                int yearNow = Integer.parseInt(datum);
                int yearStart = FIRST_YEAR;

                final ArrayList<Integer> years = new ArrayList<Integer>();
                yearNow++;
                for (int j = 0; yearStart < yearNow; j++) {
                    years.add(j, yearStart);
                    yearStart++;
                }
                of.setYears(years);

                //          Suchfelder bestimmen
                final Map<String, String> result = composeSortedLocalisedOrderSearchFields(rq);
                rq.setAttribute("sortedSearchFields", result);

                final Texttyp t = new Texttyp();
                long id = 2; // Bestellstati
                t.setId(id);
                of.setStatitexts(cn.getAllTextPlusKontoTexts(t, ui.getKonto().getId(), cn.getConnection()));
                id = 7; // Waehrungen
                t.setId(id);
                of.setWaehrungen(cn.getAllTextPlusKontoTexts(t, ui.getKonto().getId(), cn.getConnection()));

                final String dateFrom = of.getYfrom() + "-" + of.getMfrom() + "-" + of.getDfrom() + " 00:00:00";
                final String dateTo = of.getYto() + "-" + of.getMto() + "-" + of.getDto() + " 24:00:00";

                List<SearchesForm> searches = new ArrayList<SearchesForm>();

                if (of.isS()) { // Suchkriterien aus Session holen
                    searches = ui.getSearches();
                }

                // hier wird ein Array aus den Suchbedingungen aus der JSP zusammengestellt,
                // falls etwas eingegeben wurde...
                // überschreibt Session, falls eine neue Suche eingegeben wurde
                if (checkIfInputIsNotEmpty(of)) {
                    if (of.getInput1() != null && !of.getInput1().equals("")) {
                        searches
                        .add(getSearchForm(of.getValue1(), of.getCondition1(), of.getInput1(), of.getBoolean1()));
                    }
                    if (of.getInput2() != null && !of.getInput2().equals("")) {
                        searches
                        .add(getSearchForm(of.getValue2(), of.getCondition2(), of.getInput2(), of.getBoolean2()));
                    }
                }

                if (!searches.isEmpty()) { // Suche, falls etwas im searchesForm liegt...

                    //               Suchkriterien in userinfo und Session legen, damit Sortierung funktioniert
                    ui.setSearches(searches);
                    of.setS(true); // Variable Suche auf true setzen
                    rq.getSession().setAttribute("userinfo", ui);

                    PreparedStatement pstmt = null;
                    try {
                        pstmt = composeSearchLogic(searches, ui.getKonto(), of.getSort(), of.getSortorder(),
                                dateFrom, dateTo, cn.getConnection());
                        of.setBestellungen(b.searchOrdersPerKonto(pstmt));
                    } catch (final Exception e) { // Fehler aus Methode abfangen
                        // zusätzliche Ausgabe von Fehlermeldung, falls versucht wurde
                        // Bestellungen nach Kunde > 3 Monate zu suchen (Datenschutz)
                        forward = FAILURE;
                        final ErrorMessage em = new ErrorMessage("error.system", "searchorder.do?method=prepareSearch");
                        rq.setAttribute(ERRORMESSAGE, em);
                        LOG.error("search: " + e.toString());
                    } finally {
                        if (pstmt != null) {
                            try {
                                pstmt.close();
                            } catch (final SQLException e) {
                                LOG.error("search: " + e.toString());
                            }
                        }
                    }
                    PreparedStatement pstmtb = null;
                    try {
                        pstmtb = composeCountSearchLogic(searches, ui.getKonto(),
                                of.getSort(), of.getSortorder(), dateFrom, dateTo, cn.getConnection());
                        osf.setAuflistung(b.countSearchOrdersPerKonto(pstmtb));
                    } catch (final Exception e) {
                        LOG.error("composeCountSearchLogic: " + e.toString());
                    } finally {
                        if (pstmtb != null) {
                            try {
                                pstmtb.close();
                            } catch (final SQLException e) {
                                LOG.error("composeCountSearchLogic: " + e.toString());
                            }
                        }
                    }
                } else {
                    of.setS(false); // Suchvariable auf false setzen
                }

                cn.close();

                rq.setAttribute("overviewform", of);
                rq.setAttribute("orderstatistikform", osf);

            } else {
                final ErrorMessage em = new ErrorMessage("error.berechtigung", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }


        return mp.findForward(forward);
    }


    /**
     * Stellt den MYSQL für die Suche zusammen / Suchlogik
     */
    public PreparedStatement composeSearchLogic(final List<SearchesForm> searches, final Konto k, final String sort,
            final String sortorder, final String date_from, final String date_to, final Connection cn) throws Exception {

        final Bestellungen b = new Bestellungen();

        final StringBuffer sql = new StringBuffer(500);

        sql.append("SELECT * FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) ON ( b.UID = u.UID ) "
                + "INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND (");
        // letzte Klammer wichtig: sonst kann mit OR Bestellungen anderer Kontos ausgelesen werden...!!!

        final int max = searches.size();
        for (int i = 0; i < max; i++) {

            SearchesForm sf = (SearchesForm) searches.get(i);

            sf = searchMapping(sf); // ggf. Suchwerte in Datenbankwerte übersetzen...

            // Suche zusammenstellen
            if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
                sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()));
                sql.append("AGAINST (? IN BOOLEAN MODE) ");
            } else {
                sql.append('`');
                sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()));
                sql.append("`\040");
                sql.append(composeSearchLogicCondition(sf.getCondition()));
                sql.append("\040?\040");
            }

            // Boolsche-Verknüpfung anhängen solange noch weiter Abfragen kommen...
            if (i + 1 < max) {
                sql.append(composeSearchLogicBoolean(sf.getBool()));
                sql.append('\040');
            }

        }

        // erste Klammer wichtig: sonst kann mit OR Bestellungen anderer Kontos ausgelesen werden...!!!
        sql.append(b.sortOrder(") AND orderdate >= '" + date_from + "' AND orderdate <= '"
                + date_to + "' ORDER BY ", sort, sortorder));

        PreparedStatement pstmt = cn.prepareStatement(sql.toString());
        pstmt.setLong(1, k.getId());

        // bricht die Suche ab, falls nach Name || Vorname ausserhalb
        // des erlaubten Datumbereiches (3 Monate) gesucht wird...
        boolean stop = false;
        for (int i = 0; i < max && !stop; i++) {

            final SearchesForm sf = (SearchesForm) searches.get(i);

            // Kontrolle, ob mit Name || Vorname || Email || Systembemerkungen
            // ausserhalb des erlaubten Datumbereiches (3 Monate) gesucht wird...
            if (composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("name")
                    || composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("vorname")
                    || composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("mail")
                    || composeSearchLogicTable(sf.getField(), sf.getCondition()).equals("systembemerkung")) {
                if (checkAnonymize(date_from, k.getTimezone())) {
                    stop = true;
                    pstmt = null;
                }
                if (pstmt == null) {
                    throw new Exception(
                    "Datenschutz: unerlaubte Suchperiode (max. 3 Monate) bei Name, Vorname, Email, Bemerkungen");
                }
            }

            String truncation = ""; // Normalerweise keine Trunkierung
            if (sf.getCondition().contains("contains")) { truncation = "%"; } // Trunkierung für LIKE

            if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
                // Suche für IN BOOLEAN MODE zusammenstellen
                pstmt.setString(2 + i, composeSearchInBooleanMode(sf.getInput()));
            } else {
                pstmt.setString(2 + i, truncation + sf.getInput() + truncation);
            }
        }

        return pstmt;

    }

    /**
     * Stellt den MYSQL für die Suche zusammen / Suchlogik
     */
    private PreparedStatement composeCountSearchLogic(final List<SearchesForm> searches, final Konto k, final String sort,
            final String sortorder, final String date_from, final String date_to, final Connection cn) throws Exception {
        final Bestellungen b = new Bestellungen();
        final StringBuffer sql = new StringBuffer(300);
        sql.append("SELECT count(bid) FROM `bestellungen` AS b INNER JOIN (`benutzer` AS u) "
                + "ON ( b.UID = u.UID ) INNER JOIN (`konto` AS k) ON ( b.KID = k.KID ) WHERE k.kid=? AND (");

        final int max = searches.size();
        for (int i = 0; i < max; i++) {

            SearchesForm sf = (SearchesForm) searches.get(i);

            sf = searchMapping(sf); // ggf. Suchwerte in Datenbankwerte übersetzen...

            // Suche zusammenstellen
            if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
                // Achtung ist immer als "contains" trunkiert...
                sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()));
                sql.append("AGAINST (? IN BOOLEAN MODE) ");
            } else {
                sql.append('`');
                sql.append(composeSearchLogicTable(sf.getField(), sf.getCondition()));
                sql.append("`\040");
                sql.append(composeSearchLogicCondition(sf.getCondition()));
                sql.append("\040?\040");
            }

            // Boolsche-Verknüpfung anhängen solange noch weiter Abfragen kommen...
            if (i + 1 < max) {
                sql.append(composeSearchLogicBoolean(sf.getBool()));
                sql.append('\040');
            }

        }

        sql.append(b.sortOrder(") AND orderdate >= '" + date_from + "' AND orderdate <= '"
                + date_to + "' ORDER BY ", sort, sortorder));

        final PreparedStatement pstmt = cn.prepareStatement(sql.toString());
        pstmt.setLong(1, k.getId());

        for (int i = 0; i < max; i++) {

            final SearchesForm sf = (SearchesForm) searches.get(i);

            String truncation = ""; // Normalerweise keine Trunkierung
            if (sf.getCondition().contains("contains")) { truncation = "%"; } // Trunkierung für LIKE

            if (composeSearchLogicTable(sf.getField(), sf.getCondition()).contains("MATCH (artikeltitel,autor,")) {
                // Suche für IN BOOLEAN MODE zusammenstellen
                pstmt.setString(2 + i, composeSearchInBooleanMode(sf.getInput()));
            } else {
                pstmt.setString(2 + i, truncation + sf.getInput() + truncation);
            }
        }

        return pstmt;

    }

    /**
     * Übersetzt die Suchfelder in die korrekten MYSQL Table-Felder / Suchlogik
     */
    private String composeSearchLogicTable(final String field, final String condition) {

        String table = "";

        if (checkIfSearchFieldIsValid(field)) {

            // Suche in einzelnen Feldern
            if ("searchorders.artikeltitel".equals(field)) { table = "artikeltitel"; }
            if ("searchorders.author".equals(field)) { table = "autor"; }
            if ("searchorders.bemerkungen".equals(field)) { table = "systembemerkung"; }
            if ("searchorders.delformat".equals(field)) { table = "fileformat"; }
            if ("searchorders.heft".equals(field)) { table = "heft"; }
            if ("searchorders.notizen".equals(field)) { table = "notizen"; }
            if ("searchorders.issn".equals(field)) { table = "issn"; }
            if ("searchorders.jahr".equals(field)) { table = "jahr"; }
            if ("searchorders.jahrgang".equals(field)) { table = "jahrgang"; }
            if ("searchorders.gender".equals(field)) { table = "anrede"; }
            if ("searchorders.email".equals(field)) { table = "mail"; }
            if ("searchorders.institut".equals(field)) { table = "institut"; }
            if ("searchorders.department".equals(field)) { table = "abteilung"; }
            if ("searchorders.name".equals(field)) { table = "name"; }
            if ("searchorders.vorname".equals(field)) { table = "vorname"; }
            if ("searchorders.supplier".equals(field)) { table = "bestellquelle"; }
            if ("searchorders.deliveryway".equals(field)) { table = "deloptions"; }
            if ("searchorders.prio".equals(field)) { table = "orderpriority"; }
            if ("searchorders.seiten".equals(field)) { table = "seiten"; }
            if ("searchorders.subitonr".equals(field)) { table = "subitonr"; }
            if ("searchorders.gbvnr".equals(field)) { table = "gbvnr"; }
            if ("searchorders.internenr".equals(field)) { table = "internenr"; }
            if ("searchorders.zeitschrift".equals(field)) { table = "zeitschrift"; }
            if ("searchorders.doi".equals(field)) { table = "doi"; }
            if ("searchorders.pmid".equals(field)) { table = "pmid"; }
            if ("searchorders.isbn".equals(field)) { table = "isbn"; }
            if ("searchorders.typ".equals(field)) { table = "mediatype"; }
            if ("searchorders.verlag".equals(field)) { table = "verlag"; }
            if ("searchorders.buchkapitel".equals(field)) { table = "buchkapitel"; }
            if ("searchorders.buchtitel".equals(field)) { table = "buchtitel"; }

        } else { // Suche nach allen Feldern
            table = "MATCH (artikeltitel,autor,fileformat,heft,notizen,issn,heft,jahr,bestellquelle,"
                + "jahrgang,bestellquelle,deloptions,seiten,subitonr,gbvnr,internenr,zeitschrift,doi,"
                + "pmid,isbn,mediatype,verlag,buchkapitel,buchtitel) ";
            // hier wird die Suche von AND zu AND NOT umgekehrt
            if ("contains not".equals(condition) || "is not".equals(condition)) {
                table = "NOT MATCH (artikeltitel,autor,fileformat,heft,notizen,issn,heft,jahr,bestellquelle,"
                    + "jahrgang,bestellquelle,deloptions,seiten,subitonr,gbvnr,internenr,zeitschrift,doi,pmid,"
                    + "isbn,mediatype,verlag,buchkapitel,buchtitel) ";
            }
        }

        return table;
    }

    /**
     * Übersetzt die Bedingungen in die korrekten MYSQL-Begriffe / Suchlogik
     */
    private String composeSearchLogicCondition(final String condition) {

        String expression = "";

        if ("contains".equals(condition) || "contains not".equals(condition) || "is".equals(condition)
                || "is not".equals(condition) || "higher".equals(condition) || "smaller".equals(condition)) {

            if ("contains".equals(condition)) { expression = "LIKE"; }
            if ("contains not".equals(condition)) { expression = "NOT LIKE"; }
            if ("is".equals(condition)) { expression = "="; }
            if ("is not".equals(condition)) { expression = "!="; }
            if ("higher".equals(condition)) { expression = ">"; }
            if ("smaller".equals(condition)) { expression = "<"; }

        } else {
            expression = "LIKE";
        }

        return expression;
    }

    /**
     * Übersetzt die Verknüpfung in die korrekten MYSQL-Begriffe / Suchlogik
     */
    private String composeSearchLogicBoolean(final String bool) {

        String expression = "";

        if ("and".equals(bool) || "and not".equals(bool) || "or".equals(bool)) {

            if ("and".equals(bool)) { expression = "AND"; }
            if ("and not".equals(bool)) { expression = "AND NOT"; }
            if ("or".equals(bool)) { expression = "OR"; }
        } else {
            expression = "AND";
        }

        return expression;
    }

    /**
     * Liefert eine sprachabhängig sortierte Liste an Suchfeldern für den Select
     * @param HttpServletRequest rq
     * @return Map<String, String>
     */
    private Map<String, String> composeSortedLocalisedOrderSearchFields(final HttpServletRequest rq) {

        final List<String> searchFields = prepareOrderSearchFields();
        final Map<String, String> result = new TreeMap<String, String>();
        final Locale locale = getLocale(rq);
        final MessageResources msgs = getResources(rq);
        String key = null;
        String value = null;
        for (final String searchField : searchFields) {
            value = "searchorders." + searchField;
            key = msgs.getMessage(locale, value);
            result.put(key, value);
        }

        return result;
    }

    /**
     * Ersetzt Anzeige Werte in die korrekten in der Datenbank gespeicherten Werte
     */
    private SearchesForm searchMapping(final SearchesForm sf) {

        //       Mapping bei Prio => express = urgent
        if (sf.getField().equals("Priorität") && sf.getInput().equalsIgnoreCase("express")) { sf.setInput("urgent"); }
        if (sf.getField().equals("Lieferant") && sf.getInput().equalsIgnoreCase("k.A.")) { sf.setInput("0"); }

        return sf;
    }

    /**
     * Legt die durchsuchbaren Felder im Select fest.
     * Enthält den hinteren Teil der Einträge im Properties-File
     * z.B. all => searchorders.all=...
     */
    private List<String> prepareOrderSearchFields() {

        final List<String> list = new ArrayList<String>();

        // the param "all" is hardcoded on the JSP, to ensure he is always on top, language independent
        //        list.add("all");
        list.add("department");
        list.add("gender");
        list.add("artikeltitel");
        list.add("author");
        list.add("bemerkungen");
        list.add("buchkapitel");
        list.add("buchtitel");
        list.add("delformat");
        list.add("doi");
        list.add("email");
        list.add("gbvnr");
        list.add("heft");
        list.add("institut");
        list.add("internenr");
        list.add("notizen");
        list.add("isbn");
        list.add("issn");
        list.add("jahr");
        list.add("jahrgang");
        list.add("supplier");
        list.add("deliveryway");
        list.add("name");
        list.add("pmid");
        list.add("prio");
        list.add("seiten");
        list.add("subitonr");
        list.add("typ");
        list.add("verlag");
        list.add("vorname");
        list.add("zeitschrift");

        return list;
    }

    /**
     * setzt die korrekten Plus- oder Minus-Zeichen vor die Begriffe für "IN BOOLEAN MODE"
     */
    private String composeSearchInBooleanMode(String input) {

        if (input != null) {

            final Check check = new Check();

            // nur Buchstaben inkl. Umlaute und Zahlen zugelassen. Keine Sonderzeichen wie ";.-?!" etc.
            final List<String> words = check.getAlphanumericWordCharacters(input);
            final StringBuffer buf = new StringBuffer();
            buf.append("");

            int i = 0;
            for (final String word : words) {
                i++;
                // Ausschluss von Leerzeichen und Wörten kürzer als drei Buchstaben (min. 4 Buchstaben)
                if (word.length() > 3) {
                    buf.append("+" + word + "*"); // Jedem Wort ein Plus-Zeichen voranstellen, und Trunkierung anhängen
                    if (i < words.size()) { buf.append('\040'); } // ggf. ein Leerschlag dazwischen setzen
                }
            }

            input = buf.toString().trim(); // ggf. überschüssigen Leerschlag entfernen...

        }

        return input;
    }

    /**
     * Prüft ob Suchwerte eingegeben wurden
     */
    private boolean checkIfInputIsNotEmpty(final OverviewForm of) {

        boolean check = false;

        if ((of.getInput1() != null && !of.getInput1().equals(""))
                || (of.getInput2() != null && !of.getInput2().equals(""))) {
            check = true;
        }

        return check;
    }

    /**
     * Prüft ob aus der Anzeige zulässige Suchfelder übermittelt werden
     * @param String input
     * @return boolean
     */
    private boolean checkIfSearchFieldIsValid(final String input) {

        boolean check = false;

        if (input != null && !input.equals("searchorders.all")) {

            final List<String> searchFields = prepareOrderSearchFields();

            for (final String searchField : searchFields) {
                final String compare = "searchorders." + searchField;
                if (input.equals(compare)) {
                    check = true;
                    break;
                }
            }
        }

        return check;
    }

    /**
     * Prüft, ob bei einer Suche der Datenschutz (3 Monate) verletzt wird...
     * <p></p>
     * @param Strinf date_from
     * @return true/false
     */
    public boolean checkAnonymize(final String date_from, final String timezone) {
        boolean check = false;

        if (date_from != null && ReadSystemConfigurations.isAnonymizationActivated()) {
            final Bestellungen b = new Bestellungen();
            final Calendar cal = b.stringFromMysqlToCal(date_from);
            final Calendar limit = Calendar.getInstance();
            limit.setTimeZone(TimeZone.getTimeZone(timezone));
            limit.add(Calendar.MONTH, -ReadSystemConfigurations.getAnonymizationAfterMonths());
            limit.add(Calendar.DAY_OF_MONTH, -1);
            if (cal.before(limit)) {
                check = true;
            }
        }

        return check;
    }

    /**
     * stellt ein SearchForm zusammen
     */
    public SearchesForm getSearchForm(final String field, final String condition, final String input, final String bool) {

        final SearchesForm sf = new SearchesForm();
        sf.setField(field);
        sf.setCondition(condition);
        sf.setInput(input);
        sf.setBool(bool);

        return sf;
    }


}
