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

package ch.dbs.actions.bestellung;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.CodeString;
import util.CodeUrl;
import util.Http;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.BestellParam;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.entity.VKontoBenutzer;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;

/**
 * BestellformAction prüft ip-basierte Zugriffe und erlaubt Kundenbestellungen innerhalb einer Institution z.Hd. der
 * betreffenden Bibliothek
 *
 * @author Markus Fischer
 */
public final class BestellformAction extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(BestellformAction.class);
    private static final long BKID = 11;
    private static final long KKID = 12;
    private static final long IP = 9;
    private static final long LOGGED_IN = 9;
    private static final int TIMEOUT = 2000;
    private static final int RETRYS = 2;

    /**
     * Prüft IP und ordnet den Request der betreffenden Bibliothek zu, ergänzt Angaben anhand PMID und DOI
     */
    public ActionForward validate(ActionMapping mp, ActionForm fm, HttpServletRequest rq, HttpServletResponse rp) {

        Text t = new Text();
        Text cn = new Text();
        Auth auth = new Auth();
        String forward = "failure";
        OrderForm of = (OrderForm) fm;
        BestellParam bp = new BestellParam();
        Countries countriesInstance = new Countries();

        if (rq.getAttribute("ofjo") != null) {
            of = (OrderForm) rq.getAttribute("ofjo"); // if coming from checkAvailability and getOpenUrlRequest
        }

        // There are three ways of taking access, without being logged in. Priority is as follows:
        // 1. Kontokennung (overwrites IP-based access)
        // 2. IP-based (overwrites Broker-Kennung)
        // 3. Broker-Kennung (e.g. Careum Explorer)

        if (of.getKkid() == null) {
            t = auth.grantAccess(rq);
        }

        // Not logged in: IP-based, Kontokennung or Brokerkennung
        if (((t != null && t.getInhalt() != null) || (of.getKkid() != null || of.getBkid() != null))
                && !auth.isLogin(rq)) {
            forward = "success";

            String kkid = of.getKkid(); // separate variables to avoid that kkid gets overwritten in resolvePmid
            String bkid = of.getBkid();

            if (of.getMediatype() == null || // default orderform 'Article'
                    (!of.getMediatype().equals("Artikel") && !of.getMediatype().equals("Teilkopie Buch") && !of
                            .getMediatype().equals("Buch"))) {
                of.setMediatype("Artikel");
            }

            // resolve PMID or DOI
            if (of.isResolve() && of.getPmid() != null && !of.getPmid().equals("") && areArticleValuesMissing(of)) {
                of = resolvePmid(extractPmid(of.getPmid()));
            } else {
                if (of.isResolve() && of.getDoi() != null && !of.getDoi().equals("") && areArticleValuesMissing(of)) {
                    of = resolveDoi(extractDoi(of.getDoi()));
                    if (of.getDoi() == null || of.getDoi().equals("")) {
                        of = (OrderForm) fm;
                    } // sometimes we can't resolve a DOI...
                }
            }

            // has to be placed after resolvePmid, to avoid overwriting of library name (Bibliotheksname)...
            if (t != null && t.getInhalt() != null) {
                rq.setAttribute("ip", t);
                of.setBibliothek(t.getKonto().getBibliotheksname());
                if (t.getTexttyp().getId() == BKID) {
                    of.setBkid(t.getInhalt());
                }
                if (t.getTexttyp().getId() == KKID) {
                    of.setKkid(t.getInhalt());
                }
            } else {
                if (kkid != null) { // Kontokennung
                    t = new Text(cn.getConnection(), KKID, kkid); // Text with Kontokennung
                    if (t != null && t.getInhalt() != null) { // makes sure the kkid entered is valid!
                        rq.setAttribute("ip", t);
                        of.setBibliothek(t.getKonto().getBibliotheksname());
                        of.setKkid(kkid);
                    } else { // invalid kkid
                        forward = "failure";
                        ErrorMessage em = new ErrorMessage("error.kkid", "login.do");
                        rq.setAttribute("errormessage", em);
                        ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("bestellform");
                        rq.setAttribute("ActiveMenus", mf);
                    }
                }
                if (bkid != null) { // Brokerkennung
                    t = new Text(cn.getConnection(), BKID, bkid); // Text with Brokerkennung
                    if (t != null && t.getInhalt() != null) { // makes sure the bkid entered is valid!
                        if (t.getKonto().getId() != null) { // Brokerkennung belongs to ONE account
                            rq.setAttribute("ip", t);
                            of.setBibliothek(t.getKonto().getBibliotheksname());
                            of.setBkid(bkid);
                        } else { // Brokerkennung remains unresolved
                            // TODO: Prüfungen wer was liefern kann und Anzeige
                            System.out.println("need to be resolved yet!");
                        }
                    } else { // invalid bkid
                        forward = "failure";
                        ErrorMessage em = new ErrorMessage("error.bkid", "login.do");
                        rq.setAttribute("errormessage", em);
                        ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("bestellform");
                        rq.setAttribute("ActiveMenus", mf);
                    }
                }
            }

            // get additional orderform parameters (BestellParam bp)
            // Änderungen in diesem Abschnitt müssen in save() wiederholt werden
            if (t != null && t.getInhalt() != null) {
                bp = new BestellParam(t, cn.getConnection());
                // Länderauswahl setzen
                List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
                of.setCountries(allPossCountries);
                if (of.getRadiobutton().equals("")) {
                    of.setRadiobutton(bp.getOption_value1());
                } // default Option1

                // set link in request if there is institution logo for this account
                if (t.getKonto().getInstlogolink() != null) {
                    rq.setAttribute("logolink", t.getKonto().getInstlogolink());
                }
            }

            if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // default value deloptions
                if (!bp.isLieferart()) {
                    of.setDeloptions("email");
                } else {
                    of.setDeloptions(bp.getLieferart_value1());
                }
            }

            // read Cookie
            Cookie[] cookies = rq.getCookies();

            if (cookies == null) {
                LOG.ludicrous("no Cookie set!");
            } else {
                CodeString codeString = new CodeString();

                for (int i = 0; i < cookies.length; i++) {

                    if (cookies[i].getName() != null && cookies[i].getName().equals("doctordoc-bestellform")) {
                        String cookietext = codeString.decodeString(cookies[i].getValue());
                        if (cookietext != null && cookietext.contains("---")) {
                            try {
                                of.setKundenvorname(cookietext.substring(0, cookietext.indexOf("---")));
                                cookietext = cookietext.substring(cookietext.indexOf("---") + 3);
                                of.setKundenname(cookietext.substring(0, cookietext.indexOf("---")));
                                cookietext = cookietext.substring(cookietext.indexOf("---") + 3);
                                of.setKundenmail(cookietext);
                            } catch (Exception e) { //
                                LOG.error("Error while reading cookie!: " + e.toString());
                                System.out.println("Error while reading cookie!: " + e.toString());
                            }
                        }

                    }
                }
            }

            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("bestellform");
            rq.setAttribute("ActiveMenus", mf);

            // URL-encode contents of OrderForm for get-methode in PrepareLogin
            of = of.encodeOrderForm(of);

            rq.setAttribute("bestellparam", bp);
            rq.setAttribute("orderform", of);
        } else {

            // Case User is logged in
            if (auth.isLogin(rq)) {

                forward = "success";
                UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                if (of.getMediatype() == null || // default orderform 'Artikel'
                        (!of.getMediatype().equals("Artikel") && !of.getMediatype().equals("Teilkopie Buch") && !of
                                .getMediatype().equals("Buch"))) {
                    of.setMediatype("Artikel");
                }

                // resolve PMID or DOI
                if (!of.isResolver() && of.getPmid() != null && !of.getPmid().equals("")
                        && areArticleValuesMissing(of)) {
                    of = resolvePmid(extractPmid(of.getPmid()));
                } else {
                    if (!of.isResolver() && of.getDoi() != null && !of.getDoi().equals("")
                            && areArticleValuesMissing(of)) {
                        of = resolveDoi(extractDoi(of.getDoi()));
                        if (of.getDoi() == null || of.getDoi().equals("")) {
                            of = (OrderForm) fm;
                        } // sometimes we can't resolve a DOI...
                    }
                }

                bp = new BestellParam(ui.getKonto(), cn.getConnection()); // special case BestellParam when logged in

                // set country select
                if (bp != null && bp.getId() != null) {
                    List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
                    of.setCountries(allPossCountries);
                    if (of.getRadiobutton().equals("")) {
                        of.setRadiobutton(bp.getOption_value1());
                    } // default Option1
                    // values for customizable orderform
                    of.setKundeninstitution(ui.getBenutzer().getInstitut());
                    of.setKundenabteilung(ui.getBenutzer().getAbteilung());
                    of.setKundenadresse(ui.getBenutzer().getAdresse() + "\012" + ui.getBenutzer().getAdresszusatz()
                            + "\012" + ui.getBenutzer().getPlz() + "\040" + ui.getBenutzer().getOrt());
                    of.setKundenstrasse(ui.getBenutzer().getAdresse() + "\040" + ui.getBenutzer().getAdresszusatz());
                    of.setKundenplz(ui.getBenutzer().getPlz());
                    of.setKundenort(ui.getBenutzer().getOrt());
                    of.setKundenland(ui.getBenutzer().getLand());
                    if (ui.getBenutzer().getTelefonnrg() != null && !ui.getBenutzer().getTelefonnrg().equals("")) {
                        of.setKundentelefon(ui.getBenutzer().getTelefonnrg());
                    } else {
                        if (ui.getBenutzer().getTelefonnrp() != null && !ui.getBenutzer().getTelefonnrp().equals("")) {
                            of.setKundentelefon(ui.getBenutzer().getTelefonnrp());
                        }
                    }
                }

                if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // default values all other
                    // situations of deloptions
                    if (!bp.isLieferart()) {
                        of.setDeloptions("email");
                    } else {
                        of.setDeloptions(bp.getLieferart_value1());
                    }
                }

                // of.setBibliothek(t.getKonto().getBibliotheksname());
                of.setKundenvorname(ui.getBenutzer().getVorname());
                of.setKundenname(ui.getBenutzer().getName());
                of.setKundenmail(ui.getBenutzer().getEmail());

                // URL-encode contents of OrderForm for get-methode in PrepareLogin
                of = of.encodeOrderForm(of);

                rq.setAttribute("bestellparam", bp);
                rq.setAttribute("orderform", of);

                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);

            } else {
                ErrorMessage em = new ErrorMessage("error.ip", "login.do");
                rq.setAttribute("errormessage", em);
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("bestellform");
                rq.setAttribute("ActiveMenus", mf);
            }

        }

        cn.close();

        return mp.findForward(forward);
    }

    /**
     * Prüft Angaben und schickt Email mit Bestellangaben an Bibliothek und an User
     */
    public ActionForward sendOrder(ActionMapping mp, ActionForm fm, HttpServletRequest rq, HttpServletResponse rp) {

        Text t = new Text();
        Text cn = new Text();
        Auth auth = new Auth();
        String forward = "failure";
        OrderForm of = (OrderForm) fm;
        BestellParam bp = new BestellParam();
        Countries countriesInstance = new Countries();
        ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();

        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        Konto k = new Konto();
        String library = "";
        boolean saveOrder = false;

        // There are three ways of taking access, without being logged in. Priority is as follows:
        // 1. Kontokennung (overwrites IP-based access)
        // 2. IP-based (overwrites Broker-Kennung)
        // 3. Broker-Kennung (e.g. Careum Explorer)

        if (of.getKkid() == null && !auth.isLogin(rq)) {
            t = auth.grantAccess(rq);
        }

        // Logged in. IP-based, Kontokennung or Brokerkennung
        if (((t != null && t.getInhalt() != null) || (of.getKkid() != null || of.getBkid() != null))
                || auth.isLogin(rq)) {

            if (t == null || t.getInhalt() == null) {
                if (of.getKkid() != null) { // Kontokennung
                    t = new Text(cn.getConnection(), KKID, of.getKkid()); // Text with Kontokennung
                }
                if (of.getBkid() != null) { // Brokerkennung
                    t = new Text(cn.getConnection(), BKID, of.getBkid()); // Text with Brokerkennung
                }
            }

            if (t != null && t.getInhalt() != null) {
                library = t.getKonto().getBibliotheksname();
                k = t.getKonto();

                // set link in request if there is institution logo for this account
                if (t.getKonto().getInstlogolink() != null) {
                    rq.setAttribute("logolink", t.getKonto().getInstlogolink());
                }
            }

            // zugehörige Bestellformular-Parameter holen
            if (!auth.isLogin(rq) && t != null && t.getInhalt() != null) {
                bp = new BestellParam(t, cn.getConnection());
                // Länderauswahl setzen
                List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
                of.setCountries(allPossCountries);
            } else {
                if (auth.isLogin(rq)) {
                    k = ui.getKonto();
                    bp = new BestellParam(k, cn.getConnection());
                    // Länderauswahl setzen
                    List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn.getConnection());
                    of.setCountries(allPossCountries);
                }
            }

            if (bp != null && bp.getId() != null) {
                saveOrder = bp.isSaveorder();
            } // additionally save order in the database?

            try {
                // remove empty spaces from email
                if (of.getKundenmail() != null) {
                    of.setKundenmail(of.getKundenmail().trim());
                }
                Message message = getMessageForMissingBestellParams(of, bp);
                if (message.getMessage() == null) {

                    of.setKundenmail(extractEmail(of.getKundenmail())); // remove invalid characters
                    CodeString codeString = new CodeString();
                    // Cookie Base64 coded for better privacy
                    Cookie cookie = new Cookie("doctordoc-bestellform", codeString.encodeString(of.getKundenvorname()
                            + "---" + of.getKundenname() + "---" + of.getKundenmail()));
                    cookie.setMaxAge(-1); // only valid for session
                    cookie.setVersion(1);
                    try { // if there were invalid not-ASCI-characters, the order will still be processed and no cookie
                        // set.
                        rp.addCookie(cookie);
                    } catch (Exception e) {
                        LOG.error("Setting Cookie: " + e.toString());
                    }

                    AbstractBenutzer u = new AbstractBenutzer();

                    if (auth.isLogin(rq)) { // User is already known
                        u = ui.getBenutzer();
                        // if registered email is not the same as specified in the orderform
                        // append to remarks
                        if (!u.getEmail().equals(of.getKundenmail())) {
                            of.setNotizen((of.getKundenmail() + "\012" + of.getNotizen()).trim());
                        }
                    } else { // try to look up user from given Emailaddress
                        library = k.getBibliotheksname();
                        u = getUserFromBestellformEmail(k, of.getKundenmail(), cn.getConnection());
                    }

                    if (u.getId() != null) { // we do have already a valid user
                        of.setForuser(u.getId().toString()); // Preselection of user for saving an order through the
                        // getMethod in the email (depreceated)
                    } else if (saveOrder) {
                        // save as new user
                        Date d = new Date();
                        ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String datum = fmt.format(d, k.getTimezone());
                        u = new AbstractBenutzer(of);
                        u.setDatum(datum);
                        if (u.getLand() == null || u.getLand().equals("0")) {
                            u.setLand(k.getLand());
                        } // use same value as library, if not specified
                        u.setId(u.saveNewUser(u, k, cn.getConnection()));
                        VKontoBenutzer vKontoBenutzer = new VKontoBenutzer();
                        vKontoBenutzer.setKontoUser(u, k, cn.getConnection());
                        of.setForuser(u.getId().toString());
                    }

                    if (saveOrder) {
                        // save oder
                        Bestellungen b = new Bestellungen(of, u, k);
                        // set standard values. Fileformat isn't implemented in any possible orderform
                        if (!b.getMediatype().equals("Buch")) {
                            b.setFileformat("PDF");
                        } else {
                            b.setFileformat("Papierkopie");
                        }
                        b.setStatustext("zu bestellen");
                        b.save(cn.getConnection());

                        Text state = new Text(cn.getConnection(), "zu bestellen");
                        OrderState orderstate = new OrderState();
                        orderstate.setNewOrderState(b, k, state, null, u.getEmail(), cn.getConnection());
                    }

                    forward = "success";

                    // set current date
                    Date d = new Date();
                    ThreadSafeSimpleDateFormat sdf = new ThreadSafeSimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    String date = sdf.format(d, k.getTimezone());

                    MHelper mh = new MHelper();
                    String[] to = new String[2];
                    to[0] = k.getDbsmail();

                    to[1] = of.getKundenmail();
                    StringBuffer m = new StringBuffer();
                    m.append("First name: " + of.getKundenvorname() + "\n");
                    m.append("Last name: " + of.getKundenname() + "\n");
                    m.append("Email: " + of.getKundenmail() + "\n");

                    // configurable part (orderforms)
                    if (of.getFreitxt1_inhalt() != null && !of.getFreitxt1_inhalt().equals("")) {
                        m.append(of.getFreitxt1_label() + ": " + of.getFreitxt1_inhalt() + "\n");
                    }

                    if (of.getKundeninstitution() != null && !of.getKundeninstitution().equals("")) {
                        m.append("Institution: " + of.getKundeninstitution() + "\n");
                    } else { // ggf. Angaben aus dbs
                        if (u.getInstitut() != null && !u.getInstitut().equals("")) {
                            m.append("Institution: " + u.getInstitut() + "\n");
                        }
                    }

                    if (of.getKundenabteilung() != null && !of.getKundenabteilung().equals("")) {
                        m.append("Department: " + of.getKundenabteilung() + "\n");
                    } else { // ggf. Angaben aus dbs
                        if (u.getAbteilung() != null && !u.getAbteilung().equals("")) {
                            m.append("Department: " + u.getAbteilung() + "\n");
                        }
                    }

                    if (of.getFreitxt2_inhalt() != null && !of.getFreitxt2_inhalt().equals("")) {
                        m.append(of.getFreitxt2_label() + ": " + of.getFreitxt2_inhalt() + "\n");
                    }
                    if (of.getKundenadresse() != null && !of.getKundenadresse().equals("")) {
                        m.append("Address: " + of.getKundenadresse() + "\n");
                    }
                    if (of.getKundenstrasse() != null && !of.getKundenstrasse().equals("")) {
                        m.append("Street: " + of.getKundenstrasse() + "\n");
                    }
                    if (of.getKundenplz() != null && !of.getKundenplz().equals("")) {
                        m.append("ZIP: " + of.getKundenplz() + "\n");
                    }
                    if (of.getKundenort() != null && !of.getKundenort().equals("")) {
                        m.append("Place: " + of.getKundenort() + "\n");
                    }
                    if (of.getKundenland() != null && !of.getKundenland().equals("0")) {
                        m.append("Country: " + of.getKundenland() + "\n");
                    }

                    if (of.getKundentelefon() != null && !of.getKundentelefon().equals("")) { // ggf. Angaben aus
                        // Formular
                        m.append("Phone: " + of.getKundentelefon() + "\n");
                    } else { // ggf. Angaben aus dbs
                        if (u.getTelefonnrg() != null && !u.getTelefonnrg().equals("")) {
                            m.append("Phone B: " + u.getTelefonnrg() + "\n");
                        }
                        if (u.getTelefonnrp() != null && !u.getTelefonnrp().equals("")) {
                            m.append("Phone P: " + u.getTelefonnrp() + "\n");
                        }
                    }

                    if (of.getKundenbenutzernr() != null && !of.getKundenbenutzernr().equals("")) {
                        m.append("Library card #: " + of.getKundenbenutzernr() + "\n");
                    }
                    if (of.getFreitxt3_inhalt() != null && !of.getFreitxt3_inhalt().equals("")) {
                        m.append(of.getFreitxt3_label() + ": " + of.getFreitxt3_inhalt() + "\n");
                    }
                    if (of.getRadiobutton() != null && !of.getRadiobutton().equals("")) {
                        m.append(of.getRadiobutton_name() + ": " + of.getRadiobutton() + "\n");
                    }

                    m.append("\n");

                    if (of.getDeloptions() != null && !of.getDeloptions().equals("")) {
                        m.append("Desired deliveryway: " + of.getDeloptions().toUpperCase() + "\n");
                    }
                    if (of.getPrio() != null && of.getPrio().equals("urgent")) {
                        m.append("Priority: URGENT" + "\n");
                    }

                    m.append("-----" + "\n");

                    if (of.getMediatype().equals("Artikel")) {
                        if (of.getRfr_id() != null && !of.getRfr_id().equals("")) {
                            m.append("DATABASE: " + of.getRfr_id() + "\n");
                        }
                        m.append("PUBLICATION: Journal Article " + of.getGenre() + "\n");
                        m.append("AUTHOR: " + of.getAuthor() + "\n");
                        m.append("TITLE OF ARTICLE: " + of.getArtikeltitel() + "\n");
                        m.append("JOURNAL: " + of.getZeitschriftentitel() + "\n");
                        m.append("ISSN: " + of.getIssn() + "\n");
                        m.append("YEAR: " + of.getJahr() + "\n");
                        m.append("VOLUME: " + of.getJahrgang() + "\n");
                        m.append("ISSUE: " + of.getHeft() + "\n");
                        m.append("PAGES: " + of.getSeiten() + "\n");

                        // If there is only an ISSN and no journaltitle present...
                        if (of.getIssn() != null && !of.getIssn().equals("")
                                && (of.getZeitschriftentitel() == null || of.getZeitschriftentitel().equals(""))) {

                            // Add a link to the EZB
                            String bibid = "AAAAA";
                            if (k.getEzbid() != null && !k.getEzbid().equals("")) {
                                bibid = k.getEzbid();
                            }
                            String link = "http://rzblx1.uni-regensburg.de/ezeit/searchres.phtml?bibid="
                                + bibid
                                + "&colors=7&lang=de&jq_type1=KT&jq_term1=&jq_bool2=AND&jq_not2=+&jq_type2="
                                + "KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&jq_bool4=AND&jq_not4=+"
                                + "&jq_type4=IS&offset=-1&hits_per_page=50&search_journal=Suche+starten&"
                                + "Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=4&jq_term4=";

                            m.append("EZB link: " + link + of.getIssn() + "\n");
                        }

                        if (of.getDoi() != null && !of.getDoi().equals("")) {
                            m.append("DOI: " + of.getDoi() + "\n");
                            if (!extractDoi(of.getDoi()).contains("http://")) {
                                m.append("DOI-URI: " + "http://dx.doi.org/" + extractDoi(of.getDoi()) + "\n");
                            } else {
                                m.append("DOI-URI: " + extractDoi(of.getDoi()) + "\n");
                            }
                        }
                        if (of.getPmid() != null && !of.getPmid().equals("")) {
                            m.append("PMID: " + of.getPmid() + "\n");
                            m.append("PMID-URI: " + "http://www.ncbi.nlm.nih.gov/pubmed/" + extractPmid(of.getPmid())
                                    + "\n");
                        }
                        m.append("\n");
                    }

                    if (of.getMediatype().equals("Teilkopie Buch") || of.getMediatype().equals("Buch")) {
                        if (of.getRfr_id() != null && !of.getRfr_id().equals("")) {
                            m.append("DATABASE: " + of.getRfr_id() + "\n");
                        }
                        if (of.getMediatype().equals("Teilkopie Buch")) {
                            m.append("PUBLICATION: Book Part " + of.getGenre() + "\n");
                        } else {
                            m.append("PUBLICATION: Book " + of.getGenre() + "\n");
                        }
                        m.append("AUTHOR: " + of.getAuthor() + "\n");
                        if (of.getMediatype().equals("Teilkopie Buch")) {
                            m.append("CHAPTER: " + of.getKapitel() + "\n");
                        }
                        m.append("TITLE OF BOOK: " + of.getBuchtitel() + "\n");
                        m.append("PUBLISHER: " + of.getVerlag() + "\n");
                        m.append("ISBN: " + of.getIsbn() + "\n");
                        if (!of.getIssn().equals("")) {
                            m.append("ISSN: " + of.getIssn() + "\n");
                        } // Buchserie mit ISSN
                        m.append("JAHR: " + of.getJahr() + "\n");
                        if (!of.getJahrgang().equals("")) {
                            m.append("VOLUME: " + of.getJahrgang() + "\n");
                        } // Buchserie mit Zählung
                        if (of.getMediatype().equals("Teilkopie Buch")) {
                            m.append("PAGES: " + of.getSeiten() + "\n");
                        }

                        if (of.getDoi() != null && !of.getDoi().equals("")) {
                            m.append("DOI: " + of.getDoi() + "\n");
                            if (!extractDoi(of.getDoi()).contains("http://")) {
                                m.append("DOI-URI: " + "http://dx.doi.org/" + extractDoi(of.getDoi()) + "\n");
                            } else {
                                m.append("DOI-URI: " + extractDoi(of.getDoi()) + "\n");
                            }
                        }
                        m.append("\n");

                    }

                    if (of.getNotizen() != null && !of.getNotizen().equals("")) {
                        m.append("Remarks of patron: " + of.getNotizen() + "\n");
                    }

                    m.append("-----" + "\n");
                    m.append("Order date: " + date + "\n");
                    m.append("Brought to you by ");
                    m.append(ReadSystemConfigurations.getApplicationName() + ": "
                            + ReadSystemConfigurations.getServerWelcomepage() + "\n");

                    // Prepare a direct login link for librarians, to save order details
                    String loginlink = "http://www.doctor-doc.com/version1.0/pl.do?"
                        + convertOpenUrlInstance.makeGetMethodString(of) + "&foruser=" + of.getForuser();

                    String adduserlink = "";
                    if (u.getId() == null) { // User unknown => Prepare a direct login link for librarians, to save new
                        // user
                        adduserlink = "http://www.doctor-doc.com/version1.0/add.do?" + createUrlParamsForAddUser(of);
                    }

                    String prio = "3"; // Email priority 3 = normal
                    if (of.getPrio() != null && of.getPrio().equals("urgent")) {
                        prio = "1";
                    } // high

                    if (of.getMediatype().equals("Artikel")) {
                        String[] toemail = new String[1];

                        // Mail to patron, ReplyTo = library
                        toemail[0] = to[1]; // email of patron
                        mh.sendMailReplyTo(toemail, "Article: " + of.getZeitschriftentitel() + "\040" + of.getJahr()
                                + ";" + of.getJahrgang() + "(" + of.getHeft() + "):" + of.getSeiten(), m.toString(),
                                to[0]);

                        // Mail to library, ReplyTo = patron
                        toemail[0] = to[0]; // email of library
                        if (u.getId() != null) { // User already exists
                            mh.sendMailReplyTo(toemail, "Article: " + of.getZeitschriftentitel() + "\040"
                                    + of.getJahr() + ";" + of.getJahrgang() + "(" + of.getHeft() + "):"
                                    + of.getSeiten(), m.toString() + "\012Save order details in "
                                    + ReadSystemConfigurations.getApplicationName() + ":\012" + loginlink, of
                                    .getKundenmail(), prio);
                        } else { // User unknown
                            mh.sendMailReplyTo(toemail, "Article: " + of.getZeitschriftentitel() + "\040"
                                    + of.getJahr() + ";" + of.getJahrgang() + "(" + of.getHeft() + "):"
                                    + of.getSeiten(), m.toString() + "\012Unknown Email! Save patron in "
                                    + ReadSystemConfigurations.getApplicationName() + ":\012" + adduserlink + "\012"
                                    + "\012Save order details in " + ReadSystemConfigurations.getApplicationName()
                                    + ":\012" + loginlink, of.getKundenmail(), prio);
                        }
                    }

                    if (of.getMediatype().equals("Teilkopie Buch")) {
                        String[] toemail = new String[1];

                        // Mail to patron, ReplyTo = library
                        toemail[0] = to[1]; // email of patron
                        mh.sendMailReplyTo(toemail, "Book part: " + of.getBuchtitel() + "\040" + of.getJahr() + ":"
                                + of.getSeiten(), m.toString(), to[0]);

                        // Mail to library, ReplyTo = patron
                        toemail[0] = to[0]; // email of library
                        if (u.getId() != null) { // User already exists
                            mh.sendMailReplyTo(toemail, "Book part: " + of.getBuchtitel() + "\040" + of.getJahr() + ":"
                                    + of.getSeiten(), m.toString(), of.getKundenmail(), prio);
                        } else { // User unknown
                            mh.sendMailReplyTo(toemail, "Book part: " + of.getBuchtitel() + "\040" + of.getJahr() + ":"
                                    + of.getSeiten(), m.toString() + "\012Unknown Email! Save patron in "
                                    + ReadSystemConfigurations.getApplicationName() + ":\012" + adduserlink, of
                                    .getKundenmail(), prio);
                        }
                    }

                    if (of.getMediatype().equals("Buch")) {
                        String[] toemail = new String[1];

                        // Mail to patron, ReplyTo = library
                        toemail[0] = to[1]; // email of patron
                        mh.sendMailReplyTo(toemail, "Book: " + of.getBuchtitel() + "\040" + of.getJahr(), m.toString(),
                                to[0]);

                        // Mail to library, ReplyTo = patron
                        toemail[0] = to[0]; // email of library
                        if (u.getId() != null) { // User already exists
                            mh.sendMailReplyTo(toemail, "Book: " + of.getBuchtitel() + "\040" + of.getJahr(), m
                                    .toString(), of.getKundenmail(), prio);
                        } else { // User unknown
                            mh.sendMailReplyTo(toemail, "Book: " + of.getBuchtitel() + "\040" + of.getJahr(), m
                                    .toString()
                                    + "\012Unknown Email! Save patron in "
                                    + ReadSystemConfigurations.getApplicationName() + ":\012" + adduserlink, of
                                    .getKundenmail(), prio);
                        }
                    }

                } else {
                    forward = "missingvalues";
                    rq.setAttribute("messagemissing", message);
                }

                // URL-encode for get-methode in PrepareLogin
                of = of.encodeOrderForm(of);

                rq.setAttribute("orderform", of);
                if (!library.equals("")) {
                    rq.setAttribute("library", library);
                }

            } catch (Exception e) {
                forward = "failure";
                ErrorMessage em = new ErrorMessage("error.send", "login.do");
                rq.setAttribute("errormessage", em);
                // Severe error
                MHelper mh = new MHelper();
                mh.sendErrorMail(e.toString(), "Order form - Error sending an order");
            }

            if (auth.isLogin(rq)) {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);
            } else {
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("bestellform");
                rq.setAttribute("ActiveMenus", mf);
            }

        } else {
            ErrorMessage em = new ErrorMessage("error.ip", "login.do");
            rq.setAttribute("errormessage", em);
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("bestellform");
            rq.setAttribute("ActiveMenus", mf);
        }

        cn.close();
        return mp.findForward(forward);
    }

    /**
     * Bereitet die Bestellformular-Konfiguration vor
     */
    public ActionForward prepareConfigure(ActionMapping mp, ActionForm fm,
            HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        Text cn = new Text();
        Text ip = new Text();
        Auth auth = new Auth();
        BestellParam ipbasiert = new BestellParam();

        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {

                try {
                    forward = "success";

                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("konto");
                    rq.setAttribute("ActiveMenus", mf);

                    boolean hasIP = cn.hasIP(cn.getConnection(), ui.getKonto());

                    if (hasIP) {
                        ip.setId(Long.valueOf(0));
                        ip.setTexttyp(new Texttyp(IP, cn.getConnection()));
                        ip.setKonto(ui.getKonto());
                        ipbasiert = new BestellParam(ip, cn.getConnection());
                    }

                    BestellParam eingeloggt = new BestellParam(ui.getKonto(), cn.getConnection());

                    List<Text> kkid = cn.getAllKontoText(new Texttyp(KKID, cn.getConnection()), ui.getKonto().getId(),
                            cn.getConnection());
                    List<Text> bkid = cn.getAllKontoText(new Texttyp(BKID, cn.getConnection()), ui.getKonto().getId(),
                            cn.getConnection());

                    if (eingeloggt != null && eingeloggt.getId() != null) {
                        rq.setAttribute("eingeloggt", eingeloggt.getId()); // allenfalls vorhandene BestellParam-ID in
                        // Request
                    } else {
                        rq.setAttribute("eingeloggt", "0"); // 0 als ID
                    }

                    if (hasIP) { // IP hinterlegt
                        if (ipbasiert != null && ipbasiert.getId() != null) {
                            rq.setAttribute("ipbasiert", ipbasiert.getId()); // allenfalls vorhandene BestellParam-ID in
                            // Request
                        } else {
                            rq.setAttribute("ipbasiert", "-1"); // -1 als ID
                        }
                    }

                    if (kkid.size() > 0) {
                        rq.setAttribute("kkid", kkid);
                    }
                    if (bkid.size() > 0) {
                        rq.setAttribute("bkid", bkid);
                    }

                } catch (Exception e) {
                    LOG.error("BestellformAction - prepareConfigure: " + e.toString());
                } finally {
                    cn.close();
                }

            } else { // keine Berechtigung
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
            }
        } else { // nicht eingeloggt
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        return mp.findForward(forward);
    }

    /**
     * ändert und erstellt angepasste Bestellformulare
     */
    public ActionForward modify(ActionMapping mp, ActionForm fm, HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        Text cn = new Text();
        Auth auth = new Auth();

        BestellParam bp = (BestellParam) fm;

        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                if (checkPermission(ui, bp, cn.getConnection())) { // Prüfung auf URL-hacking

                    try {
                        forward = "success";

                        ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("konto");
                        rq.setAttribute("ActiveMenus", mf);

                        BestellParam custom = new BestellParam();
                        custom.setKid(ui.getKonto().getId());

                        if (bp.getId() > 0) { // bestehendes BestellParam (eingeloggt oder IP-basiert)
                            custom = new BestellParam(bp.getId(), cn.getConnection());
                        }
                        if (bp.getId() == 0) {
                            custom.setTyid(LOGGED_IN);
                            custom.setKennung("Bestellformular eingeloggt");
                        }
                        if (bp.getId() == -1) {
                            custom.setTyid(IP); // IP
                        }
                        if (bp.getId() == -2) { // Konto-Kennung
                            custom = new BestellParam(bp.getKennung(), ui.getKonto().getId(), cn.getConnection());
                            if (custom.getId() == null) {
                                custom.setTyid(KKID);
                                custom.setKid(ui.getKonto().getId());
                                custom.setKennung(bp.getKennung());
                                custom.setId(bp.getId());
                            }
                        }
                        if (bp.getId() == -3) { // Borker-Kennung
                            custom = new BestellParam(bp.getKennung(), ui.getKonto().getId(), cn.getConnection());
                            if (custom.getId() == null) {
                                custom.setTyid(BKID);
                                custom.setKid(ui.getKonto().getId());
                                custom.setKennung(bp.getKennung());
                                custom.setId(bp.getId());
                            }
                        }

                        rq.setAttribute("bestellform", custom);

                    } catch (Exception e) {
                        LOG.error("modify: " + e.toString());
                    } finally {
                        cn.close();
                    }

                } else { // URL-hacking
                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("suchenbestellen");
                    rq.setAttribute("ActiveMenus", mf);
                    ErrorMessage em = new ErrorMessage("error.hack", "searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute("errormessage", em);
                    LOG.info("modify: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
            } else { // keine Berechtigung
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
            }
        } else { // nicht eingeloggt
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        cn.close(); // wird benötigt
        return mp.findForward(forward);
    }

    /**
     * speichert neue und bestehende Bestellformulare
     */
    public ActionForward save(ActionMapping mp, ActionForm fm, HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";
        UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        Text cn = new Text();
        Auth auth = new Auth();

        BestellParam bp = (BestellParam) fm;
        Countries countriesInstance = new Countries();

        if (auth.isLogin(rq)) {
            if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                if (checkPermission(ui, bp, cn.getConnection())) { // Prüfung auf URL-hacking

                    try {
                        forward = "success";

                        ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("konto");
                        rq.setAttribute("ActiveMenus", mf);

                        bp = checkBPLogic(bp); // logische Prüfungen und setzt abhängige Werte

                        if (bp.getMessage() == null) { // keine Fehlermedlungen

                            forward = "bestellform";
                            OrderForm of = new OrderForm();

                            if (bp.getId() <= 0) { // negative ID => save
                                bp.setId(bp.save(bp, cn.getConnection()));
                            } else { // positive ID => update
                                bp.update(bp, cn.getConnection());
                            }
                            bp.setBack(true); // Flag für "Back" auf Bestellform
                            bp.setLink_back("bfconfigure.do?method=modify&id=" + bp.getId());

                            // analog wie in validate()
                            // Länderauswahl setzen
                            List<Countries> allPossCountries = countriesInstance.getAllActivatedCountries(cn
                                    .getConnection());
                            of.setCountries(allPossCountries);
                            if (of.getRadiobutton().equals("")) {
                                of.setRadiobutton(bp.getOption_value1());
                            } // default Option1

                            if (of.getDeloptions() == null || of.getDeloptions().equals("")) { // Defaultwert deloptions
                                if (!bp.isLieferart()) {
                                    of.setDeloptions("email");
                                } else {
                                    of.setDeloptions(bp.getLieferart_value1());
                                }
                            }

                            rq.setAttribute("orderform", of);
                            rq.setAttribute("bestellparam", bp);

                        } else { // Fehlermeldung vorhanden
                            forward = "success"; // auf bestellformconfigure
                            rq.setAttribute("message", bp.getMessage());
                            rq.setAttribute("bestellform", bp);
                        }

                    } catch (Exception e) {
                        LOG.error("save: " + e.toString());
                    } finally {
                        cn.close();
                    }

                } else { // URL-hacking
                    ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("suchenbestellen");
                    rq.setAttribute("ActiveMenus", mf);
                    ErrorMessage em = new ErrorMessage("error.hack", "searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute("errormessage", em);
                    LOG.info("save: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
            } else { // keine Berechtigung
                ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("suchenbestellen");
                rq.setAttribute("ActiveMenus", mf);
                ErrorMessage em = new ErrorMessage("error.berechtigung", "searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute("errormessage", em);
            }
        } else { // nicht eingeloggt
            ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute("ActiveMenus", mf);
            ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute("errormessage", em);
        }

        cn.close(); // wird benötigt
        return mp.findForward(forward);
    }

    /**
     * Bereitet den Redirect mit SessionID für die British Library vor. Notwendig für Suche in Subsets (z.B. nur
     * Journals)
     */
    public ActionForward redirect(ActionMapping mp, ActionForm fm, HttpServletRequest rq, HttpServletResponse rp) {

        String forward = "failure";

        try {
            forward = "success";
            OrderForm pageForm = (OrderForm) fm;
            String link = "";

            String sessionid = getSessionIdBl();

            if (!sessionid.equals("")) { // erlaubt Suche in Subset Journals
                link = "http://catalogue.bl.uk/F/" + sessionid + "?func=find-b&request=" + pageForm.getIssn()
                + "&find_code=ISSN&adjacent=Y&image.x=41&image.y=13";
            } else { // Suche durch alles, auch einzelne Artikel etc.
                link = "http://catalogue.bl.uk/F/?func=find-b&request=" + pageForm.getIssn()
                + "&find_code=ISSN&adjacent=Y&image.x=37&image.y=9";
            }

            pageForm.setLink(link);

            rq.setAttribute("orderform", pageForm);

        } catch (Exception e) {
            LOG.error("redirect: " + e.toString());
        }

        return mp.findForward(forward);
    }

    /**
     * Extrahiert aus einem String die DOI
     */
    public String extractDoi(String doi) {

        final int offset = 4;

        if (doi != null && !doi.equals("")) {
            try {

                doi = doi.trim().toLowerCase();
                if (doi.contains("doi:")) {
                    doi = doi.substring(doi.indexOf("doi:") + offset);
                } // ggf. Text "DOI:" entfernen
                if (doi.contains("dx.doi.org/")) {
                    doi = doi.substring(doi.indexOf("dx.doi.org/") + 11);
                } // verschiedene Formen der Angaben entfernen ( dx.doi.org/... , http://dx.doi.org/...)
                if (doi.contains("doi/")) {
                    doi = doi.substring(doi.indexOf("doi/") + offset);
                } // ggf. Text "DOI/" entfernen

            } catch (Exception e) {
                LOG.error("extractDoi: " + doi + "\040" + e.toString());
            }
        }

        return doi;
    }

    /**
     * Extrahiert aus einem String die PMID (Pubmed-ID
     */
    public String extractPmid(String pmid) {

        if (pmid != null && !pmid.equals("")) {

            try {

                Matcher w = Pattern.compile("[0-9]+").matcher(pmid);
                if (w.find()) {
                    pmid = pmid.substring(w.start(), w.end());
                }

            } catch (Exception e) {
                LOG.error("extractPmid: " + pmid + "\040" + e.toString());
            }
        }

        return pmid;
    }

    /**
     * holt anhand einer Doi alle Artikelangaben
     */
    public OrderForm resolveDoi(String doi) {

        // http://generator.ocoins.info/ [Eingabe: 10.1002/hec.1381 ]

        OrderForm of = new OrderForm();
        ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        OpenUrl openUrlInstance = new OpenUrl();
        Http http = new Http();
        String link = "http://generator.ocoins.info/?doi=" + doi;
        // String link = "http://generator.ocoins.info/crossref?handle=" + doi;
        String content = "";

        try {

            content = http.getWebcontent(link, TIMEOUT, RETRYS);

            content = content.replaceAll("&amp;amp;", "&amp;"); // falsche Doppelkodierung korrigieren...

            // Sicherstellen, dass die Anfrage aufgelöst wurde und vom OCoinS-Generator selber stammt (Ausschluss von
            // direkter Weiterleitung)
            if (!content.contains("DOI Resolution Error")
                    && content.contains("rfr_id=info%3Asid%2Focoins.info%3Agenerator")) {

                ContextObject co = openUrlInstance.readOpenUrlFromString(content);
                of = convertOpenUrlInstance.makeOrderform(co);

            }

        } catch (Exception e) {
            LOG.error("resolveDoi: " + doi + "\040" + e.toString());
        }

        return of;
    }

    /**
     * holt anhand einer PMID alle Artikelangaben
     */
    public OrderForm resolvePmid(String pmid) {

        // automatisches Vervollständigen der Artikelangaben anhand der PMID (Pubmed-ID):
        // http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=3966282

        OrderForm of = new OrderForm();
        ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();
        OpenUrl openUrlInstance = new OpenUrl();
        Http http = new Http();
        String link = "http://www.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&retmode=xml&id=" + pmid;
        // empfohlener Link wäre:
        // String link = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmode=xml&id=" + pmid;
        // TODO: Pubmed XML-Auswertung umstellen
        String content = "";

        try {

            content = http.getWebcontent(link, TIMEOUT, RETRYS);
            ContextObject co = openUrlInstance.readXmlPubmed(content);
            of = convertOpenUrlInstance.makeOrderform(co);

        } catch (Exception e) {
            LOG.error("resolvePmid: " + pmid + "\040" + e.toString());
        }

        return of;
    }

    /**
     * holt die PMID anhand von Artikelangaben
     *
     * @param OrderForm
     *            og
     * @return String pmid
     */
    public String getPmid(OrderForm of) {

        String pmid = "";
        Http http = new Http();

        try {

            String content = http.getWebcontent(composePubmedlinkToPmid(of), TIMEOUT, RETRYS);

            if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
                pmid = content.substring(content.indexOf("<Id>") + 4, content.indexOf("</Id>"));
            }

        } catch (Exception e) {
            LOG.error("getPmid(of): " + e.toString());
        }

        return pmid;
    }

    /**
     * holt die PMID aus dem Webcontent
     *
     * @param String
     *            webcontent
     * @return String pmid
     */
    public String getPmid(String content) {

        String pmid = "";

        try {

            if (content.contains("<Count>1</Count>") && content.contains("<Id>")) {
                pmid = content.substring(content.indexOf("<Id>") + 4, content.indexOf("</Id>"));
            }

        } catch (Exception e) {
            LOG.error("getPmid(String): " + e.toString() + "\012" + content);
        }

        return pmid;
    }

    /**
     * stellt den Suchlink zusammen um die PMID anhand von Artikelangaben zu holen
     */
    public String composePubmedlinkToPmid(OrderForm pageForm) {

        ConvertOpenUrl convertOpenUrlInstance = new ConvertOpenUrl();

        StringBuffer link = new StringBuffer();
        link.append("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=");
        link.append(pageForm.getIssn() + "[TA]");
        if (pageForm.getJahrgang() != null && !pageForm.getJahrgang().equals("")) {
            link.append("+AND+" + pageForm.getJahrgang() + "[VI]");
        }
        if (pageForm.getHeft() != null && !pageForm.getHeft().equals("")) {
            link.append("+AND+" + pageForm.getHeft() + "[IP]");
        }
        if (pageForm.getSeiten() != null && !pageForm.getSeiten().equals("")) {
            link.append("+AND+" + convertOpenUrlInstance.extractSpage(pageForm.getSeiten()) + "[PG]");
        }
        if (pageForm.getJahr() != null && !pageForm.getJahr().equals("")) {
            link.append("+AND+" + pageForm.getJahr() + "[DP]");
        }

        return link.toString();
    }

    /**
     * prüft, ob wichtige of-Werte fehlen (ISSN, Zeitschriftentitel, Author, Jahr, Jahrgang, Heft, Seitenzahlen)
     */
    public boolean areArticleValuesMissing(OrderForm of) {

        boolean check = false;

        try {

            if (of.getMediatype().equals("Artikel") // um zu verhindern, dass bei eine Übergabe aus OpenURL auch Bücher
                    // über Pubmed etc. geprüft werden
                    && (of.getIssn().equals("") || of.getZeitschriftentitel().equals("") || of.getAuthor().equals("")
                            || of.getJahr().equals("") || of.getArtikeltitel().equals("")
                            || of.getJahrgang().equals("") || of.getHeft().equals("") || of.getSeiten().equals(""))) {
                check = true;
            }

        } catch (Exception e) {
            LOG.error("areArticleValuesMissing: " + e.toString());

        }

        return check;
    }

    /**
     * prüft, ob of-Werte bei Mussfeldern bei einem allfällig vorliegenden BestellParam fehlen, und gibt ggf. eine
     * Message mit der entsprechenden Fehlermeldung zurück
     */
    public Message getMessageForMissingBestellParams(OrderForm of, BestellParam bp) {

        Message m = new Message();
        Check ck = new Check();

        try {

            if (!ck.isMinLength(of.getKundenvorname(), 1)) {
                m.setMessage("error.vorname");
            } // auf jeden Fall Mussfeld
            if (!ck.isMinLength(of.getKundenname(), 1)) {
                m.setMessage("error.name");
            } // auf jeden Fall Mussfeld
            if (!ck.isEmail(of.getKundenmail())) {
                m.setMessage("error.mail");
            } // auf jeden Fall Mussfeld

            if (bp != null && bp.getId() != null) {
                if (bp.isInst_required() && !ck.isMinLength(of.getKundeninstitution(), 1)) {
                    m.setMessage("error.institution");
                }
                if (bp.isAbt_required() && !ck.isMinLength(of.getKundenabteilung(), 1)) {
                    m.setMessage("error.abteilung");
                }
                if (bp.isFreitxt1_required() && !ck.isMinLength(of.getFreitxt1_inhalt(), 1)) {
                    m.setMessage("error.values");
                }
                if (bp.isFreitxt2_required() && !ck.isMinLength(of.getFreitxt2_inhalt(), 1)) {
                    m.setMessage("error.values");
                }
                if (bp.isFreitxt3_required() && !ck.isMinLength(of.getFreitxt3_inhalt(), 1)) {
                    m.setMessage("error.values");
                }
                if (bp.isAdr_required() && !ck.isMinLength(of.getKundenadresse(), 1)) {
                    m.setMessage("error.adresse");
                }
                if (bp.isStr_required() && !ck.isMinLength(of.getKundenstrasse(), 1)) {
                    m.setMessage("error.strasse");
                }
                if (bp.isPlz_required() && !ck.isMinLength(of.getKundenplz(), 1)) {
                    m.setMessage("error.plz");
                }
                if (bp.isOrt_required() && !ck.isMinLength(of.getKundenort(), 1)) {
                    m.setMessage("error.ort");
                }
                if (bp.isLand_required() && !ck.isMinLength(of.getKundenland(), 2)) {
                    m.setMessage("error.land");
                }
                if (bp.isTelefon_required() && !ck.isMinLength(of.getKundentelefon(), 1)) {
                    m.setMessage("error.telefon");
                }
                if (bp.isBenutzernr_required() && !ck.isMinLength(of.getKundenbenutzernr(), 1)) {
                    m.setMessage("error.benutzernummer");
                }
                if (bp.isGebuehren() && of.getGebuehren() == null) {
                    m.setMessage("error.fees");
                } // muss "on" sein
                if (bp.isAgb() && of.getAgb() == null) {
                    m.setMessage("error.agb");
                } // muss "on" sein
            }

        } catch (Exception e) {
            LOG.error("areBestellParamMissing: " + e.toString());
        }

        return m;
    }

    /**
     * Sucht anhand der im Bestellformular eingegebenen Email den zugehörigen Benutzer des betreffenden Kontos zu holen
     */
    public AbstractBenutzer getUserFromBestellformEmail(Konto k, String email, Connection cn) {

        AbstractBenutzer u = new AbstractBenutzer();

        try {

            ArrayList<AbstractBenutzer> list = u.getUserListFromEmailAndKonto(k, email, cn);

            if (list.size() > 0) {
                u = list.get(0);
            } // es wird der erste Benutzer zurückgegeben

        } catch (Exception e) {
            LOG.error("getUserFromBestellformEmail: " + email + "\040" + e.toString());
        }

        return u;
    }

    private String createUrlParamsForAddUser(OrderForm of) {
        StringBuffer urlParam = new StringBuffer();
        CodeUrl urlCoder = new CodeUrl();

        if (of.getKundenmail() != null && !of.getKundenmail().equals("")) {
            urlParam.append("email=" + of.getKundenmail());
        }
        if (of.getKundenname() != null && !of.getKundenname().equals("")) {
            urlParam.append("&name=" + urlCoder.encodeLatin1(of.getKundenname()));
        }
        if (of.getKundenvorname() != null && !of.getKundenvorname().equals("")) {
            urlParam.append("&vorname=" + urlCoder.encodeLatin1(of.getKundenvorname()));
        }
        if (of.getKundeninstitution() != null && !of.getKundeninstitution().equals("")) {
            urlParam.append("&institut=" + urlCoder.encodeLatin1(of.getKundeninstitution()));
        }
        if (of.getKundenabteilung() != null && !of.getKundenabteilung().equals("")) {
            urlParam.append("&abteilung=" + urlCoder.encodeLatin1(of.getKundenabteilung()));
        }
        if (of.getKundenadresse() != null && !of.getKundenadresse().equals("")) {
            urlParam.append("&adresse=" + urlCoder.encodeLatin1(of.getKundenadresse()));
        }
        if (of.getKundenstrasse() != null && !of.getKundenstrasse().equals("")) {
            urlParam.append("&adresse=" + urlCoder.encodeLatin1(of.getKundenstrasse()));
        }
        if (of.getKundentelefon() != null && !of.getKundentelefon().equals("")) {
            urlParam.append("&telefonnrg=" + of.getKundentelefon());
        }
        if (of.getKundenplz() != null && !of.getKundenplz().equals("")) {
            urlParam.append("&plz=" + urlCoder.encodeLatin1(of.getKundenplz()));
        }
        if (of.getKundenort() != null && !of.getKundenort().equals("")) {
            urlParam.append("&ort=" + urlCoder.encodeLatin1(of.getKundenort()));
        }
        if (of.getKundenland() != null && !of.getKundenland().equals("")) {
            urlParam.append("&land=" + urlCoder.encodeLatin1(of.getKundenland()));
        }

        return urlParam.toString();
    }

    /**
     * extrahiert mit einem Regex die Email aus einem String
     */
    private String extractEmail(String email) {
        String extractedEmail = email;
        try {
            Pattern p = Pattern.compile("[A-Za-z0-9._-]+@[A-Za-z0-9][A-Za-z0-9.-]{0,61}[A-Za-z0-9]\\.[A-Za-z.]{2,6}");
            Matcher m = p.matcher(email);

            if (m.find()) {
                extractedEmail = email.substring(m.start(), m.end());
            }

        } catch (Exception e) {
            LOG.error("extractEmail(String email): " + email + "\040" + e.toString());
        }

        return extractedEmail;
    }

    /**
     * holt anhand eine SessionID der British Library
     */
    private String getSessionIdBl() {

        // reduziert die Treffer z.B. bei Long-term Dietary Cadmium Intake and Postmenopausal Endometrial Cancer
        // Incidence
        // von 47 bei Suche ohne Sessionid auf die relevanten 2

        String sessionid = "";
        Http http = new Http();
        String link = "http://catalogue.bl.uk/F/?func=file&file_name=find-b";

        try {

            String content = http.getWebcontent(link, TIMEOUT, RETRYS);

            if (content.contains("title=\"Catalogue subset search\"")) {

                link = content.substring(content.lastIndexOf("http://", content
                        .indexOf("title=\"Catalogue subset search\"")), content.lastIndexOf("\"", content
                                .indexOf("title=\"Catalogue subset search\"")));
                content = http.getWebcontent(link, TIMEOUT, RETRYS);

                if (content.contains("Serials and periodicals")) {

                    link = content.substring(
                            content.lastIndexOf("http://", content.indexOf("Serials and periodicals")), content
                            .lastIndexOf("\"", content.indexOf("Serials and periodicals")));
                    content = http.getWebcontent(link, TIMEOUT, RETRYS);

                    if (content.contains("action=\"http://catalogue.bl.uk:80/F/")) {

                        sessionid = content.substring(content.indexOf("action=\"http://catalogue.bl.uk:80/F/") + 36,
                                content.indexOf("\"", content.indexOf("action=\"") + 8));

                    }
                }

            }

        } catch (Exception e) {
            LOG.error("getSessionIdBl: " + e.toString());
        }

        return sessionid;
    }

    /**
     * Prüft auf URL-hacking bei modify (Bestellformular)
     */
    private boolean checkPermission(UserInfo ui, BestellParam bp, Connection cn) {

        boolean check = false;
        Text t = new Text();

        try {

            if (bp != null && bp.getId() != null) {

                if (bp.getId() == 0) {
                    check = true; // Bestellformular eingeloggt
                    return check;
                }
                if (bp.getId() == -1) { // neues Bestellformulat IP-basiert
                    if (t.hasIP(cn, ui.getKonto())) {
                        check = true;
                    }
                    return check;
                }
                if (bp.getId() == -2 && bp.getKennung() != null) { // Konto-Kennung
                    t = new Text(cn, bp.getKennung());
                    if (t.getKonto().getId() != null && t.getKonto().getId().equals(ui.getKonto().getId())) {
                        check = true;
                        return check;
                    }
                }
                if (bp.getId() == -3 && bp.getKennung() != null) { // Broker-Kennung
                    t = new Text(cn, bp.getKennung());
                    if (t.getKonto().getId() != null && t.getKonto().getId().equals(ui.getKonto().getId())) {
                        check = true;
                        return check;
                    }
                }
                BestellParam bpCompare = new BestellParam(bp.getId(), cn);
                // Prüfung, ob die ID zum Konto gehört! (URL-hacking)
                if (bpCompare.getKid() != null && bpCompare.getKid().equals(ui.getKonto().getId())) {
                    check = true;
                    return check;
                }
            }

        } catch (Exception e) {
            LOG.error("checkPermission(UserInfo ui, Long id): " + e.toString());
        }

        return check;
    }

    /**
     * Prüft Eingaben bei einem BestellParam auf die logischen Abhängigkeiten, setzt automatisch abhängige Werte und
     * gibt bei fehlenden Werten ggf. eine Fehlermeldung aus
     */
    private BestellParam checkBPLogic(BestellParam bp) {

        // serielle Ausgabe um ggf. auf der jsp mehrsprachige Fehlermeldungen zu triggern

        try {

            Message m = new Message();
            Check ck = new Check();

            if (ck.isMinLength(bp.getLieferart_value1(), 1) || ck.isMinLength(bp.getLieferart_value2(), 1)
                    || ck.isMinLength(bp.getLieferart_value3(), 1)) {

                if (ck.isMinLength(bp.getLieferart_value1(), 1)) {
                    bp.setLieferart(true); // gültige Eingaben erfolgt
                } else {
                    m.setMessage("bestellformconfigure.deliveryway");
                    bp.setMessage(m);
                }

            }

            if (bp.isFreitxt1() && !ck.isMinLength(bp.getFreitxt1_name(), 1)) {
                m.setMessage("bestellformconfigure.frei1");
                bp.setMessage(m);
            } else {
                if (!bp.isFreitxt1() && ck.isMinLength(bp.getFreitxt1_name(), 1)) {
                    bp.setFreitxt1_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben
                    // werden
                }
            }
            if (bp.isFreitxt2() && !ck.isMinLength(bp.getFreitxt2_name(), 1)) {
                m.setMessage("bestellformconfigure.frei2");
                bp.setMessage(m);
            } else {
                if (!bp.isFreitxt2() && ck.isMinLength(bp.getFreitxt2_name(), 1)) {
                    bp.setFreitxt2_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben
                    // werden
                }
            }
            if (bp.isFreitxt3() && !ck.isMinLength(bp.getFreitxt3_name(), 1)) {
                m.setMessage("bestellformconfigure.frei3");
                bp.setMessage(m);
            } else {
                if (!bp.isFreitxt3() && ck.isMinLength(bp.getFreitxt3_name(), 1)) {
                    bp.setFreitxt3_name(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben
                    // werden
                }
            }

            if (ck.isMinLength(bp.getOption_value1(), 1) || ck.isMinLength(bp.getOption_value2(), 1)
                    || ck.isMinLength(bp.getOption_value3(), 1)) {

                if (ck.isMinLength(bp.getOption_value1(), 1)) {
                    bp.setOption(true); // gültige Eingaben erfolgt
                } else {
                    m.setMessage("bestellformconfigure.option");
                    bp.setMessage(m);
                }

            } else {
                bp.setOption_name("");
                bp.setOption_comment("");
                bp.setOption_linkout("");
                bp.setOption_linkoutname("");
            }

            if (bp.isGebuehren()) {
                if (!ck.isMinLength(bp.getLink_gebuehren(), 1)) {
                    m.setMessage("bestellformconfigure.fee");
                    bp.setMessage(m);
                } else {
                    if (!ck.isUrl(bp.getLink_gebuehren())) {
                        m.setMessage("bestellformconfigure.fee_link");
                        bp.setMessage(m);
                    }
                }
            } else {
                if (!bp.isGebuehren() && ck.isMinLength(bp.getLink_gebuehren(), 1)) {
                    bp.setLink_gebuehren(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben
                    // werden
                }
            }
            if (bp.isAgb()) {
                if (!ck.isMinLength(bp.getLink_agb(), 1)) {
                    m.setMessage("bestellformconfigure.agb");
                    bp.setMessage(m);
                } else {
                    if (!ck.isUrl(bp.getLink_agb())) {
                        m.setMessage("bestellformconfigure.agb_link");
                        bp.setMessage(m);
                    }
                }
            } else {
                if (!bp.isAgb() && ck.isMinLength(bp.getLink_agb(), 1)) {
                    bp.setLink_agb(""); // verhindert ,dass Werte bei nicht aktivierter Option in DB geschrieben werden
                }
            }

        } catch (Exception e) {
            LOG.error("checkBPLogic(BestellParam bp): " + e.toString());
        }

        return bp;
    }

}
