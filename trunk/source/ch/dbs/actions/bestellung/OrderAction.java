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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.grlea.log.SimpleLogger;

import util.Auth;
import util.Check;
import util.CodeUrl;
import util.Http;
import util.MHelper;
import util.ReadSystemConfigurations;
import util.SpecialCharacters;
import util.ThreadSafeSimpleDateFormat;
import util.ThreadedJournalSeek;
import util.ThreadedWebcontent;
import ch.dbs.actions.bestand.Stock;
import ch.dbs.actions.openurl.ContextObject;
import ch.dbs.actions.openurl.ConvertOpenUrl;
import ch.dbs.actions.openurl.OpenUrl;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestand;
import ch.dbs.entity.BestellParam;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.DefaultPreis;
import ch.dbs.entity.Konto;
import ch.dbs.entity.Lieferanten;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.entity.Texttyp;
import ch.dbs.form.ActiveMenusForm;
import ch.dbs.form.EZBDataOnline;
import ch.dbs.form.EZBDataPrint;
import ch.dbs.form.EZBForm;
import ch.dbs.form.EZBReference;
import ch.dbs.form.ErrorMessage;
import ch.dbs.form.FindFree;
import ch.dbs.form.JournalDetails;
import ch.dbs.form.Message;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import ch.ddl.daia.DaiaRequest;

public final class OrderAction extends DispatchAction {

    private static final SimpleLogger LOG = new SimpleLogger(OrderAction.class);
    private static final int TIMEOUT_2 = 2000;
    private static final int TIMEOUT_1 = 1000;
    private static final int RETRYS_3 = 3;
    private static final int RETRYS_2 = 2;
    private static final int RETRYS_1 = 1;
    private static final String FAILURE = "failure";
    private static final String SUCCESS = "success";
    private static final String ACTIVEMENUS = "ActiveMenus";
    private static final String ERRORMESSAGE = "errormessage";

    /**
     * Check if an article is freely available in the Internet.
     */
    public ActionForward findForFree(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {
        final ArrayList<JournalDetails> hitsGoogle = new ArrayList<JournalDetails>();
        final ArrayList<JournalDetails> hitsGoogleScholar = new ArrayList<JournalDetails>();
        final Check check = new Check();
        final BestellformAction bfAction = new BestellformAction();
        String content;
        String linkGoogle = "";
        String linkGS = "";

        String forward = FAILURE;
        OrderForm pageForm = (OrderForm) form;
        final Auth auth = new Auth();
        final CodeUrl codeUrl = new CodeUrl();
        final SpecialCharacters specialCharacters = new SpecialCharacters();

        // resolve a PMID entered by an user
        if (pageForm.getArtikeltitel().toLowerCase().contains("pmid:")) {
            pageForm = bfAction.resolvePmid(bfAction.extractPmid(pageForm.getArtikeltitel()));
            pageForm.setAutocomplete(true); // avoid additional autocomplete runs
            // resolving pmid failed => back to search
            if (pageForm.getArtikeltitel().equals("")) {
                forward = "pmidfailure";
            }
        }

        // ISO-8859-1 encoding is important for automatic search!
        pageForm.setArtikeltitel_encoded(codeUrl.encodeLatin1(shortenGoogleSearchPhrase(pageForm.getArtikeltitel())));

        // Make sure method is only accessible when user is logged in
        if (auth.isLogin(rq)) {

            if (!"pmidfailure".equals(forward)) {

                // *** run autocomplete for the first time
                if (!pageForm.isAutocomplete()) {
                    pageForm.setAutocomplete(autoComplete(pageForm, rq));
                }

                // Automatic Google search
                if (google(rq, auth)) {

                    // ...only search Google, if there we don't come from captcha.jsp!
                    if (pageForm.getCaptcha_id() == null && pageForm.getCaptcha_text() == null) {

                        // Google

                        //  Search steps:
                        // 1. phrase + allintitle: + filetype:pdf
                        // 2. phrase + allintitle + extract pdf
                        // 3. title as phrase + [text] pdf OR "full-text" => result without check for PDFs 3

                        int searches = 0;
                        boolean ergebnis = false;
                        int start = 0;
                        String compare = "";

                        while ((!ergebnis) && (searches < 3)) {

                            if (searches == 0) { // phrase + allintitle + filetype:pdf
                                linkGoogle = "http://www.google.ch/search?as_q=&hl=de&num=10&btnG=Google-Suche&as_oq=&as_eq=&lr=&as_ft=i&as_filetype=pdf&as_qdr=all&as_occt=title&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
                            }
                            if (searches == 1) { // phrase + allintitle
                                linkGoogle = "http://www.google.ch/search?as_q=&hl=de&num=10&btnG=Google-Suche&as_oq=&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=title&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
                            }
                            if (searches == 2) {
                                // Number of result is set to 5.
                                linkGoogle = "http://www.google.ch/search?as_q=&hl=de&num=5&btnG=Google-Suche&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images&as_epq=";
                            }

                            linkGoogle = linkGoogle + pageForm.getArtikeltitel_encoded();

                            content = getWebcontent(linkGoogle, TIMEOUT_1, RETRYS_3);

                            //      content = "<form action=\"Captcha\" method=\"get\">" +
                            //      "<input type=\"hidden\" name=\"id\" value=\"17179006839024668804\">" +
                            //      "<input type=\"text\" name=\"captcha\" value=\"\" id=\"captcha\" size=\"12\">" +
                            //      "<img src=\"/sorry/image?id=17179006839024668804&amp;hl=de\" border=\"1\" alt=\"Falls Sie dies lesen können, ist die Bilddarstellung bei Ihnen deaktiviert. Aktivieren Sie die Bilddarstellung, um fortzufahren.\"></div>";

                            try {

                                if (check.containsGoogleCaptcha(content)) {
                                    Message m = new Message();
                                    m = handleGoogleCaptcha(content);
                                    forward = "captcha";
                                    rq.setAttribute("message", m);
                                }
                            } catch (final Exception e) {
                                LOG.error("Problem treating captcha: " + e.toString() + "\012" + content);
                            }

                            if (searches < 2) {
                                compare = "[PDF]";
                            } else {
                                compare = "class=\"r\">";
                            }

                            //   make sure Google did not respond with Captcha
                            if (!check.containsGoogleCaptcha(content)) {

                                if (content.contains(compare)) {
                                    ergebnis = true;
                                    String linkPdfGoogle = "";

                                    String content2 = content;

                                    while (content2.contains(compare)) {

                                        JournalDetails jdGoogle = new JournalDetails();

                                        start = content2.indexOf("<a href=", content2.indexOf(compare)) + 9;
                                        linkPdfGoogle = content2.substring(start, content2.indexOf('"', start + 9));
                                        linkPdfGoogle = correctGoogleURL(linkPdfGoogle);
                                        String textLinkPdfGoogle = content2.substring(content2.indexOf('>', start) + 1,
                                                content2.indexOf("</a>", start));
                                        textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("<b>", "");
                                        textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("</b>", "");
                                        textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("<em>", "");
                                        textLinkPdfGoogle = textLinkPdfGoogle.replaceAll("</em>", "");
                                        textLinkPdfGoogle = specialCharacters.replace(textLinkPdfGoogle);

                                        jdGoogle.setLink(linkPdfGoogle);
                                        jdGoogle.setUrl_text(textLinkPdfGoogle);
                                        hitsGoogle.add(jdGoogle);

                                        if (searches < 2) { // use Google-Cache only, if looking for PDFs
                                            String cache = "";
                                            int cachePosition = 0;
                                            int end = 0;
                                            if (searches < 2) {
                                                // class=w => start of next record
                                                end = content2.indexOf("class=w", start);
                                            } else {
                                                // class=g => start of next record
                                                end = content2.indexOf(compare, start);
                                            }

                                            if ((end != -1) && (content2.substring(start, end).contains("=cache:"))
                                                    || (end == -1) && (content2.substring(start).contains("=cache:"))) {
                                                cachePosition = content2.indexOf("=cache:", start);
                                                // bis + => ohne highlighten der Suchbegriffe im Cache
                                                cache = content2.substring(
                                                        content2.lastIndexOf("http:", cachePosition),
                                                        content2.indexOf('+', cachePosition));

                                                jdGoogle = new JournalDetails();
                                                jdGoogle.setLink(cache);
                                                jdGoogle.setUrl_text("(Google-Cache): " + textLinkPdfGoogle);
                                                hitsGoogle.add(jdGoogle);
                                            }
                                        }

                                        content2 = content2.substring(start);
                                    }
                                    searches = 3; // search successful

                                    final FindFree ff = new FindFree();
                                    ff.setZeitschriften(hitsGoogle);
                                    rq.setAttribute("treffer_gl", ff);

                                } else {
                                    searches = searches + 1;
                                }

                            } else { // Google captcha => forward to resolve Captcha

                                searches = 6;

                                Message m = new Message();
                                m = handleGoogleCaptcha(content);
                                forward = "captcha";
                                rq.setAttribute("message", m);
                            }
                        }

                        // Google Scholar

                        searches = 0;
                        ergebnis = false;
                        start = 0;

                        while ((!ergebnis) && (searches < 2)) {

                            if (searches == 0) { // phrase + allintitle + filetype:pdf
                                linkGS = "http://scholar.google.com/scholar?q=allintitle%3A%22"
                                        + pageForm.getArtikeltitel_encoded()
                                        + "%22+filetype%3Apdf+OR+filetype%3Ahtm&hl=de&lr=&btnG=Suche&lr=";
                            }
                            if (searches == 1) { // phrase + allintitle
                                linkGS = "http://scholar.google.com/scholar?q=allintitle%3A%22"
                                        + pageForm.getArtikeltitel_encoded() + "%22&hl=de&lr=&btnG=Suche&lr=";
                            }

                            content = getWebcontent(linkGS, TIMEOUT_1, RETRYS_3);

                            // make sure Google Scholar does not respond with Captcha
                            if (!check.containsGoogleCaptcha(content)) {

                                // Change this, to adapt to any major changes of GoogleScholars sourcecode.
                                final String identifierHitsGoogleScholar = "[PDF]</span> <a href=\"";
                                //                                        "<div class=gs_rt><h3><span class=gs_ctc>[PDF]</span> <a href=\"";

                                if (content.contains(identifierHitsGoogleScholar)) {
                                    ergebnis = true;

                                    String linkPdfGS = "";

                                    while (content.contains(identifierHitsGoogleScholar)) {
                                        String content2 = "";
                                        if (content.substring(content.indexOf(identifierHitsGoogleScholar) + 9)
                                                .contains(identifierHitsGoogleScholar)) {
                                            content2 = content.substring(
                                                    content.indexOf(identifierHitsGoogleScholar),
                                                    content.indexOf(identifierHitsGoogleScholar,
                                                            content.indexOf(identifierHitsGoogleScholar) + 9));
                                        } else {
                                            content2 = content.substring(content.indexOf(identifierHitsGoogleScholar));
                                        }

                                        JournalDetails jdGoogleScholar = new JournalDetails();
                                        start = content2.indexOf("<a href=\"") + 9;
                                        linkPdfGS = content2.substring(start, content2.indexOf('"', start));
                                        linkPdfGS = correctGoogleURL(linkPdfGS);

                                        // url-text extrahieren
                                        String textLinkPdfGoogleScholar = "";
                                        textLinkPdfGoogleScholar = content2.substring(content2.indexOf('>', start) + 1,
                                                content2.indexOf("</a>", start));
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("<b>", "");
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</b>", "");
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("<em>", "");
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</em>", "");
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll(
                                                "<font color=#CC0033>", "");
                                        textLinkPdfGoogleScholar = textLinkPdfGoogleScholar.replaceAll("</font>", "");
                                        textLinkPdfGoogleScholar = specialCharacters.replace(textLinkPdfGoogleScholar);

                                        jdGoogleScholar.setLink(linkPdfGS);
                                        jdGoogleScholar.setUrl_text(textLinkPdfGoogleScholar);
                                        hitsGoogleScholar.add(jdGoogleScholar);

                                        if (content2.contains("=cache:")) {

                                            final int cachePosition = content2.indexOf("=cache:");
                                            // bis + => ohne highlighten der Suchbegriffe im Cache
                                            final String cache = content2.substring(
                                                    content2.lastIndexOf("http:", cachePosition),
                                                    content2.indexOf('+', cachePosition));

                                            jdGoogleScholar = new JournalDetails();
                                            jdGoogleScholar.setLink(cache);
                                            jdGoogleScholar.setUrl_text("(Google-Cache): " + textLinkPdfGoogleScholar);
                                            hitsGoogleScholar.add(jdGoogleScholar);
                                        }

                                        content = content.substring(content.indexOf(identifierHitsGoogleScholar) + 9);

                                    }
                                    final FindFree ff = new FindFree();
                                    ff.setZeitschriften(hitsGoogleScholar);
                                    rq.setAttribute("treffer_gs", ff);

                                } else {
                                    searches = searches + 1;
                                }

                            } else { // Google-Scholar captcha  => forward to resolve Captcha

                                searches = 2;

                                Message m = new Message();
                                m = handleGoogleCaptcha(content);
                                forward = "captcha";
                                rq.setAttribute("message", m);

                            }

                        }

                    } else { // resolve captcha...

                        // Suche ausführen mit test, dann folgendes aufrufen:
                        // http://www.google.ch/sorry/?continue=http://www.google.ch/search?hl=de&q=test&btnG=Google-Suche&meta=
                        // Captcha erscheint, aufzulösen mit:
                        // http://www.google.ch/sorry/Captcha?continue=http%3A%2F%2Fwww.google.ch%2Fsearch%3Fhl%3Dde&id=7584471529417997108&captcha=nonaryl
                        // Bildquelle: http://www.google.ch/sorry/image?id=6926821699383349053
                        // wobei id aus Quelltext und Text aus captcha übereinstimmen
                        // müssen => man landet auf Google Grundseite...

                        linkGoogle = "http://www.google.ch/sorry/Captcha?continue=http://www.google.ch/search?hl=de&id="
                                + pageForm.getCaptcha_id() + "&captcha=" + pageForm.getCaptcha_text();
                        content = getWebcontent(linkGoogle, TIMEOUT_1, RETRYS_1);

                        if (!check.containsGoogleCaptcha(content)) {
                            // Important message
                            final MHelper mh = new MHelper();
                            mh.sendErrorMail("Catchpa has been successfully resolved!", "Cheers...\012");
                        }

                        // Captcha: prepare manual Google search
                        linkGoogle = "http://www.google.ch/search?as_q=&hl=de&num=4&btnG=Google-Suche&as_epq="
                                + pageForm.getArtikeltitel_encoded()
                                + "&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images";

                        final JournalDetails jdGoogleCaptcha = new JournalDetails();
                        jdGoogleCaptcha.setLink(linkGoogle);
                        jdGoogleCaptcha.setUrl_text("Search Google!");
                        hitsGoogle.add(jdGoogleCaptcha);

                        final FindFree ff = new FindFree();
                        ff.setZeitschriften(hitsGoogle);
                        rq.setAttribute("treffer_gl", ff);

                        // needs to be UTF-8 encoded
                        linkGS = "http://scholar.google.com/scholar?as_q=&num=4&btnG=Scholar-Suche&as_epq="
                                + codeUrl.encodeUTF8(pageForm.getArtikeltitel())
                                + "&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&hl=de&lr=";

                        final JournalDetails jdGoogleScholarCaptcha = new JournalDetails();
                        jdGoogleScholarCaptcha.setLink(linkGS);
                        jdGoogleScholarCaptcha.setUrl_text("Search Google-Scholar!");
                        hitsGoogleScholar.add(jdGoogleScholarCaptcha);

                        final FindFree fs = new FindFree();
                        fs.setZeitschriften(hitsGoogleScholar);
                        rq.setAttribute("treffer_gs", fs);

                    }

                } else {

                    // User: prepare manual Google search
                    linkGoogle = "http://www.google.ch/search?as_q=&hl=de&num=4&btnG=Google-Suche&as_epq="
                            + pageForm.getArtikeltitel_encoded()
                            + "&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images";

                    final JournalDetails jdGoogleManual = new JournalDetails();
                    jdGoogleManual.setLink(linkGoogle);
                    jdGoogleManual.setUrl_text("Search Google!");
                    hitsGoogle.add(jdGoogleManual);

                    final FindFree ff = new FindFree();
                    ff.setZeitschriften(hitsGoogle);
                    rq.setAttribute("treffer_gl", ff);

                    // needs to be UTF-8 encoded
                    linkGS = "http://scholar.google.com/scholar?as_q=&num=4&btnG=Scholar-Suche&as_epq="
                            + codeUrl.encodeUTF8(pageForm.getArtikeltitel())
                            + "&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=";

                    final JournalDetails jdGoogleScholarManual = new JournalDetails();
                    jdGoogleScholarManual.setLink(linkGS);
                    jdGoogleScholarManual.setUrl_text("Search Google-Scholar!");
                    hitsGoogleScholar.add(jdGoogleScholarManual);

                    final FindFree fs = new FindFree();
                    fs.setZeitschriften(hitsGoogleScholar);
                    rq.setAttribute("treffer_gs", fs);

                }

            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            forward = "error";
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        // failure wird leider nicht als globale Fehlermeldung verwendet.
        // Deshalb Ausklammerung, falls ein tatsächlicher Fehler vorliegt...
        if (!"error".equals(forward)) {

            if (!"captcha".equals(forward) && !"pmidfailure".equals(forward)) {
                if ((hitsGoogle.isEmpty()) && (hitsGoogleScholar.isEmpty())) {
                    forward = "notfound";
                } else {
                    forward = "found";
                }
            }

            // if we do not have a PMID, try to get it and complete any missing article details over Pubmed
            if (isPubmedSearchWithoutPmidPossible(pageForm) && pageForm.isAutocomplete()) { // autocomplete nust have been successful
                pageForm.setPmid(bfAction.getPmid(pageForm)); // if we have several hits => set pmid = ""
                // complete any missing article details:
                if (!pageForm.getPmid().equals("") && bfAction.areArticleValuesMissing(pageForm)) {
                    OrderForm of = new OrderForm();
                    of = bfAction.resolvePmid(bfAction.extractPmid(pageForm.getPmid()));
                    pageForm.completeOrderForm(pageForm, of);
                }
            }

            // avoid in issnAssistent that autocomplete will be run again...
            if (!"captcha".equals(forward)) {
                pageForm.setRuns_autocomplete(1);
            }

            // replace greek alphabet to alpha, beta etc.
            pageForm.setArtikeltitel(prepareWorldCat2(pageForm.getArtikeltitel()));
        }

        // URL encode for GET method in PrepareLogin
        pageForm = pageForm.encodeOrderForm(pageForm);

        rq.setAttribute("orderform", pageForm);
        return mp.findForward(forward);
    }

    public ActionForward issnAssistent(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo"); // to get ezbid
        String bibid = ui.getKonto().getEzbid();
        if (bibid == null || bibid.equals("")) {
            bibid = "AAAAA"; // library unknown
        }

        OrderForm pageForm = (OrderForm) form;
        final Auth auth = new Auth();
        final BestellformAction bfAction = new BestellformAction();
        final CodeUrl codeUrl = new CodeUrl();

        final String artikeltitelEnc = codeUrl.encodeLatin1(pageForm.getArtikeltitel());

        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        boolean treffer = false;
        if (auth.isLogin(rq)) {

            // no PMID => ordinary processing of ISSN assistent
            if (pageForm.getPmid() == null || pageForm.getPmid().equals("")) {
                if (!(pageForm.getIssn().length() == 0 && pageForm.getZeitschriftentitel().length() == 0 && !pageForm
                        .isAutocomplete())) { // exclude no input without autocomplete...
                    forward = SUCCESS;
                    try {

                        // autocomplete has been already done...
                        if ((pageForm.isAutocomplete())
                        //...we have input...
                                && ((pageForm.getIssn().length() != 0) || (pageForm.getZeitschriftentitel().length() != 0))
                                // ...and it is not the special case autocomplete without ISSN!
                                && !pageForm.isFlag_noissn()) {
                            // we have input to be corrected by autocomplete => empty data in form
                            pageForm.setAuthor("");
                            pageForm.setJahr("");
                            pageForm.setJahrgang("");
                            pageForm.setHeft("");
                            pageForm.setSeiten("");
                            pageForm.setPmid("");
                            pageForm.setDoi("");
                            pageForm.setAutocomplete(false);
                            pageForm.setRuns_autocomplete(0);
                        }

                        if (pageForm.isAutocomplete() && pageForm.isFlag_noissn()) {
                            // special case: data from autocomplete but no ISSN

                            // alle anderen Angaben URL-codieren und vorbereiten für
                            // Übergabe an ff, da Weitergabe über Hyperlink auf jsp läuft

                            pageForm.setJahr(codeUrl.encodeLatin1(pageForm.getJahr()));
                            pageForm.setSeiten(codeUrl.encodeLatin1(pageForm.getSeiten()));
                            pageForm.setHeft(codeUrl.encodeLatin1(pageForm.getHeft()));
                            pageForm.setJahrgang(codeUrl.encodeLatin1(pageForm.getJahrgang()));
                            pageForm.setAuthor(codeUrl.encodeLatin1(pageForm.getAuthor()));
                        }

                        String zeitschriftentitelEncoded = correctArtikeltitIssnAssist(pageForm.getZeitschriftentitel());
                        zeitschriftentitelEncoded = codeUrl.encodeLatin1(zeitschriftentitelEncoded);

                        //              Methode 1 ueber Journalseek
                        final FindFree ff = new FindFree();
                        List<JournalDetails> issnJS = new ArrayList<JournalDetails>();

                        // der Zeitschriftentitel im OrderForm kann sich im Thread von Regensburg ändern
                        final String concurrentCopyZeitschriftentitel = pageForm.getZeitschriftentitel();

                        final ThreadedJournalSeek tjs = new ThreadedJournalSeek(zeitschriftentitelEncoded,
                                artikeltitelEnc, pageForm, concurrentCopyZeitschriftentitel);
                        final ExecutorService executor = Executors.newCachedThreadPool();
                        Future<List<JournalDetails>> journalseekResult = null;
                        boolean jsThread = false;

                        if ((pageForm.getIssn().length() == 0)
                        // Ausklammerung von Journalseek bei Eingabe einer ISSN, da Auswertung anders ist...
                                && (pageForm.getZeitschriftentitel().length() != 0)) {

                            jsThread = true;
                            journalseekResult = executor.submit(tjs);

                        } else {
                            // es wurde eine ISSN eingegeben. Zeitschriftentitel aus Regensburg holen...
                            forward = "issn_direkt";
                        }

                        //            Methode 2 ueber Regensburger Zeitschriftenkatalog
                        // Anzeige auf 30 limitiert (hits_per_page):
                        final FindFree ffRB = new FindFree();

                        // get ISSN from EZB Regensburg
                        final List<JournalDetails> issnRB = searchEZBxml(pageForm, bibid);

                        if (!issnRB.isEmpty()) {
                            treffer = true;
                            // es wird versucht den Zeitschriftentitel zu bestimmen...
                            if (issnRB.size() == 1) {
                                pageForm.setZeitschriftentitel(issnRB.get(0).getZeitschriftentitel());
                            }

                        } else {
                            final JournalDetails jdRB = new JournalDetails();
                            jdRB.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
                            jdRB.setArtikeltitel(pageForm.getArtikeltitel());
                            jdRB.setArtikeltitel_encoded(artikeltitelEnc);
                            issnRB.add(jdRB);
                        }

                        ffRB.setZeitschriften(issnRB);
                        rq.setAttribute("regensburg", ffRB);

                        // Journalseek-Thread zurückholen
                        try {
                            if (jsThread) {
                                issnJS = journalseekResult.get(12, TimeUnit.SECONDS);
                            }
                        } catch (final TimeoutException e) {
                            log.warn("Journalseek-TimeoutException: " + e.toString());
                        } catch (final Exception e) {
                            LOG.error("Journalseek-Thread failed in issnAssistent: " + e.toString());
                        } finally {
                            if (jsThread) {
                                if (issnJS != null && !issnJS.isEmpty()) {
                                    treffer = true;
                                } else {
                                    // avoid possible nullpointer dereference, due to threaded request
                                    // which may, upon failing, set issnJS to null
                                    if (issnJS == null) {
                                        issnJS = new ArrayList<JournalDetails>();
                                    }
                                    final JournalDetails jd = new JournalDetails();
                                    jd.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
                                    jd.setZeitschriftentitel_encoded(zeitschriftentitelEncoded);
                                    jd.setArtikeltitel(pageForm.getArtikeltitel());
                                    jd.setArtikeltitel_encoded(artikeltitelEnc);
                                    issnJS.add(jd);
                                }

                                ff.setZeitschriften(issnJS);
                                rq.setAttribute("journalseek", ff);

                                // ungefährlich, falls der Task schon beendet ist.
                                // Stellt sicher, dass nicht noch unnötige Ressourcen belegt werden
                                journalseekResult.cancel(true);
                            }
                        }

                    } catch (final Exception e) {
                        forward = FAILURE;
                        final ErrorMessage em = new ErrorMessage();
                        em.setError("error.system");
                        em.setLink("searchfree.do?activemenu=suchenbestellen");
                        rq.setAttribute(ERRORMESSAGE, em);
                        LOG.error("issnAssistent: " + e.toString());

                    }

                } else {
                    forward = "noresult"; // No input...
                }

            } else { // PMID present => resolve
                forward = "noresult"; // back to input form
                pageForm = bfAction.resolvePmid(bfAction.extractPmid(pageForm.getPmid()));
                pageForm.setAutocomplete(true); // suppress autocomplete
                pageForm.setRuns_autocomplete(1);
            }

        } else {
            final ActiveMenusForm mf = new ActiveMenusForm();
            mf.setActivemenu("login");
            rq.setAttribute(ACTIVEMENUS, mf);
            final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
            rq.setAttribute(ERRORMESSAGE, em);
        }

        if ((!treffer) && (pageForm.getIssn().length() == 0)) {
            forward = "noresult";
        }
        if (pageForm.getArtikeltitel().length() != 0
                && (pageForm.getRuns_autocomplete() == 0 && pageForm.getIssn().length() != 0)) {
            // Funktion AutoComplete ausführen
            pageForm.setAutocomplete(autoComplete(pageForm, rq));

            //        System.out.println("Ergebnis autocomplete: " + pageForm.isAutocomplete());
            //        System.out.println("Testausgabe ISSN: " + pageForm.getIssn());

            // um zu verhindern, dass vor dem ISSN-Assistent nochmals erfolglos versucht wir Autocomplete auszuführen...
            pageForm.setRuns_autocomplete(1);
        } else {
            pageForm.setAutocomplete(false); // d.h. Autocomplete im nächsten Schritt...
            pageForm.setRuns_autocomplete(0);
        }

        rq.setAttribute("orderform", pageForm);
        rq.setAttribute("form", pageForm);

        return mp.findForward(forward);
    }

    public ActionForward checkAvailabilityOpenUrl(final ActionMapping mp, final ActionForm form,
            final HttpServletRequest rq, final HttpServletResponse rp) {

        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        OrderForm pageForm = (OrderForm) form;
        final BestellformAction bfAction = new BestellformAction();
        Text cn = new Text();
        final Auth auth = new Auth();
        EZBForm ezbform = new EZBForm();

        final ExecutorService executor = Executors.newCachedThreadPool();
        // EZB-Thread-Management
        final ThreadedWebcontent ezbthread = new ThreadedWebcontent();
        Future<String> ezbcontent = null;
        // GBV-Thread-Management
        final ThreadedWebcontent gbvthread = new ThreadedWebcontent();
        Future<String> gbvcontent = null;
        // Pubmed-Thread-Management
        final ThreadedWebcontent pubmedthread = new ThreadedWebcontent();
        Future<String> pubmedcontent = null;
        // Carelit-Thread-Management
        final ThreadedWebcontent carelitthread = new ThreadedWebcontent();
        Future<String> carelitcontent = null;

        String forward = FAILURE;
        String bibid = null;
        long daiaId = 0;
        long kid = 0;

        try {

            // if coming from getOpenUrlRequest or prepareReorder
            if (rq.getAttribute("ofjo") != null) {
                pageForm = (OrderForm) rq.getAttribute("ofjo");
                pageForm.setResolver(true);
                rq.setAttribute("ofjo", pageForm);
            }

            // if not logged in, try to get account from request
            if (!auth.isLogin(rq)) {
                cn = (Text) rq.getAttribute("ip");
                if (cn != null) {
                    // Text mit Kontoangaben anhand Broker-Kennung holen
                    if (cn.getTexttyp().getId() == 11) {
                        pageForm.setBkid(cn.getInhalt());
                    }
                    // Text mit Kontoangaben anhand Konto-Kennung holen
                    if (cn.getTexttyp().getId() == 12) {
                        pageForm.setKkid(cn.getInhalt());
                    }
                }
            }

            if (ui != null) {
                // get bibid from ui
                if (ui.getKonto().getEzbid() != null && !ui.getKonto().getEzbid().equals("")) {
                    bibid = ui.getKonto().getEzbid();
                }
                daiaId = getDaiaId(ui.getKonto().getId());
                kid = ui.getKonto().getId();
            } else {
                // get bibid from IP based access
                if (cn != null && cn.getInhalt() != null) {
                    if (pageForm.getBkid() == null && cn.getKonto().getEzbid() != null
                            && !cn.getKonto().getEzbid().equals("")) {
                        bibid = cn.getKonto().getEzbid();
                    }
                    daiaId = cn.getKonto().getId();
                    kid = cn.getKonto().getId();
                }
            }

            // normalize PMID if available
            pageForm.setPmid(bfAction.extractPmid(pageForm.getPmid()));

            // PMID available and there are article references missing
            if (pageForm.getPmid() != null && !pageForm.getPmid().equals("")
                    && bfAction.areArticleValuesMissing(pageForm)) {
                OrderForm of = new OrderForm();
                of = bfAction.resolvePmid(pageForm.getPmid());
                pageForm.completeOrderForm(pageForm, of);
            } else {
                // try to get missing PMID and complete missing article references
                if (isPubmedSearchWithoutPmidPossible(pageForm)) {
                    pubmedthread.setLink(bfAction.composePubmedlinkToPmid(pageForm));
                    pubmedcontent = executor.submit(pubmedthread);
                }
            }

            // get zdbid from ISSN. Only necessary if logged in...
            boolean gbvThread = false;
            if (auth.isLogin(rq) && pageForm.getIssn() != null && !pageForm.getIssn().equals("")) {
                // gets zdbid from database (will be an e-journal)
                pageForm.setZdbid(getZdbidFromIssn(pageForm.getIssn(), cn.getConnection()));
                // System.out.println("ZDB-ID aus dbs: " + pageForm.getZdbid());
                // Try to get from e-ZDB-ID a p-ZDB-ID from GBV using a seperate thread.
                if (pageForm.getZdbid() != null && !pageForm.getZdbid().equals("")) {
                    final String gbvlink = "http://gso.gbv.de/sru/DB=2.1/?version=1.1&operation=searchRetrieve&query=pica.zdb%3D%22"
                            + pageForm.getZdbid()
                            + "%22&recordSchema=pica&sortKeys=YOP%2Cpica%2C0%2C%2C&maximumRecords=10&startRecord=1";
                    gbvthread.setLink(gbvlink);
                    gbvcontent = executor.submit(gbvthread);
                    gbvThread = true;
                }
            }

            // logged in or access IP based/kkid/bkid
            if (auth.isLogin(rq) || (cn != null && cn.getInhalt() != null)) {
                forward = "notfreeebz";

                // set link in request if there is institution logo for this account
                if (cn.getInhalt() != null && cn.getKonto().getInstlogolink() != null) {
                    rq.setAttribute("logolink", cn.getKonto().getInstlogolink());
                }

                ContextObject co = new ContextObject();
                final ConvertOpenUrl openurlConv = new ConvertOpenUrl();
                co = openurlConv.makeContextObject(pageForm);

                final OpenUrl openU = new OpenUrl();
                final String openurl = openU.composeOpenUrl(co);

                // needed for  creating OpenURL links on checkavailability.jsp (e.g. Carelit)
                pageForm.setLink(openurl);

                if (ReadSystemConfigurations.isSearchCarelit()) {
                    carelitthread.setLink("http://217.91.37.16/LISK_VOLLTEXT/resolver/drdoc.asp?sid=DRDOC:doctor-doc&"
                            + openurl);
                    carelitcontent = executor.submit(carelitthread);
                }

                // use link to services from ZDB/EZB
                // http://services.d-nb.de/fize-service/gvr/html-service.htm?
                final StringBuffer linkEZB = new StringBuffer("http://services.d-nb.de/fize-service/gvr/full.xml?");
                linkEZB.append(openurl);
                if (bibid != null) {
                    // use bibid from account
                    linkEZB.append("&pid=bibid=");
                    linkEZB.append(bibid);
                } else {
                    // use IP for request
                    linkEZB.append("client_ip=");
                    linkEZB.append(rq.getRemoteAddr());
                }

                // set EZB request into thread, get back after timeout and if empty use alternate API over
                // http://rzblx1.uni-regensburg.de/ezeit/vascoda/info/dokuXML.html
                // http://ezb.uni-regensburg.de/ezeit/vascoda/openURL?pid=format%3Dxml&genre=article&issn=1538-3598&bibid=AAAAA
                ezbthread.setLink(linkEZB.toString());
                ezbcontent = executor.submit(ezbthread);

                // compose link to EZB for UI
                final StringBuffer linkUIezb = new StringBuffer("http://ezb.uni-regensburg.de/ezeit/vascoda/openURL?");
                linkUIezb.append(openurl);
                if (bibid != null) {
                    // use bibid from account
                    linkUIezb.append("&bibid=");
                    linkUIezb.append(bibid);
                } else {
                    // use IP for request
                    linkUIezb.append("client_ip=");
                    linkUIezb.append(rq.getRemoteAddr());
                }

                // Check for internal / external Holdings using DAIA Document Availability Information API
                List<Bestand> allHoldings = new ArrayList<Bestand>();
                List<Bestand> internalHoldings = new ArrayList<Bestand>();
                List<Bestand> externalHoldings = new ArrayList<Bestand>();

                if (ReadSystemConfigurations.isUseDaia()) { // Check an external register over DAIA
                    final DaiaRequest daiaRequest = new DaiaRequest();
                    allHoldings = daiaRequest.get(openurl);
                    internalHoldings = extractInternalHoldings(allHoldings, daiaId);
                    externalHoldings = extractExternalHoldings(allHoldings, daiaId, ui);
                }
                // Check internal database
                final Stock stock = new Stock();
                allHoldings = stock.checkGeneralStockAvailability(pageForm, true);
                internalHoldings.addAll(extractInternalHoldings(allHoldings, kid));
                externalHoldings.addAll(extractExternalHoldings(allHoldings, kid, ui));

                // get back EZB thread
                final String ezbanswer = getBackThreadedWebcontent(ezbcontent, 3, "EZB/ZDB");
                if (ezbanswer != null && !ezbanswer.contains("<Error code=")
                        && !ezbanswer.contains("503 Service Temporarily Unavailable")) {
                    // read EZB response as XML
                    final EZBJOP ezb = new EZBJOP();
                    ezbform = ezb.read(ezbanswer);
                } else {
                    // use alternate Vascoda API
                    // http://rzblx1.uni-regensburg.de/ezeit/vascoda/info/dokuXML.html

                    final EZBVascoda vascoda = new EZBVascoda();
                    // &pid=format%3Dxml => output as XML
                    final EZBForm efVascoda = vascoda.read(getWebcontent(linkUIezb.toString() + "&pid=format%3Dxml",
                            2000, 2));
                    // returns only online holdings. Keep local print holdings...
                    ezbform.setOnline(efVascoda.getOnline());

                    // only show error in UI if library has ZDB holdings
                    if (ui != null && ui.getKonto().isZdb() || cn != null && cn.getKonto() != null
                            && cn.getKonto().isZdb()) {
                        final EZBDataPrint timeout = new EZBDataPrint();
                        timeout.setAmpel("red");
                        timeout.setComment("error.zdb_timeout");
                        ezbform.getPrint().add(timeout);
                    }
                }

                // set Link for "Powered by EZB/ZDB" for manual checks by the user
                ezbform.setLinkezb(linkUIezb.toString());

                if (!internalHoldings.isEmpty()) { // we have own holdings
                    forward = "freeezb";
                    addInternalHoldings(ezbform, pageForm, internalHoldings, cn.getConnection());
                    rq.setAttribute("internalHoldings", internalHoldings);
                }
                if (!externalHoldings.isEmpty()) { // there external holdings
                    rq.setAttribute("holdings", externalHoldings);
                }

                // if logged in go to availabilityresult.jsp or if we have found some holdings
                if (auth.isLogin(rq) || analyzeEZBResult(ezbform, pageForm, cn.getConnection())) {
                    forward = "freeezb";
                }

                // ge back GBV thread
                if (gbvThread) {
                    final String gbvanswer = getBackThreadedWebcontent(gbvcontent, 2, "GBV");
                    // holt aus ggf. mehreren möglichen Umleitungen die letztmögliche
                    if (gbvanswer != null) {
                        final String pZdbid = OrderGbvAction.getPrintZdbidIgnoreMultipleHits(gbvanswer);
                        if (pZdbid != null) {
                            pageForm.setZdbid(pZdbid); // e-ZDB-ID wird nur überschrieben, falls p-ZDB-ID erhalten
                        }
                    }
                }

                // get back Pubmed thread
                if (isPubmedSearchWithoutPmidPossible(pageForm)) {
                    final String pubmedanswer = getBackThreadedWebcontent(pubmedcontent, 1, "Pubmed");
                    if (pubmedanswer != null) {
                        pageForm.setPmid(bfAction.getPmid(pubmedanswer));
                    }

                    if (pageForm.getPmid() != null && !pageForm.getPmid().equals("") && // falls PMID gefunden wurde
                            bfAction.areArticleValuesMissing(pageForm)) { // und Artikelangaben fehlen
                        final OrderForm of = bfAction.resolvePmid(pageForm.getPmid());
                        pageForm.completeOrderForm(pageForm, of); // ergänzen
                    }
                }

                // get back Carelit thread
                if (ReadSystemConfigurations.isSearchCarelit()) {
                    final String carelitanswer = getBackThreadedWebcontent(carelitcontent, 1, "Carelit");
                    if (carelitanswer != null
                            && carelitanswer.contains("<span id=\"drdoc\" style=\"display:block\">1</span>")) {
                        // there is a fulltext available in Carelit
                        pageForm.setCarelit(true);
                        forward = "freeezb";
                    }
                }

            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

            pageForm.setAutocomplete(false); // reset
            rq.setAttribute("ezb", ezbform);

            // for get-method in PrepareLogin encode pageForm
            pageForm = pageForm.encodeOrderForm(pageForm);

            rq.setAttribute("orderform", pageForm);

        } finally {
            if (cn != null) {
                cn.close();
            }
        }

        return mp.findForward(forward);
    }

    private void addInternalHoldings(final EZBForm ezbform, final OrderForm pageForm,
            final List<Bestand> internalHoldings, final Connection cn) {

        // set Supplier in pageForm
        final Lieferanten supplier = new Lieferanten();
        pageForm.setLieferant(supplier.getLieferantFromName("abonniert", cn));
        pageForm.setDeloptions("email");

        for (final Bestand hold : internalHoldings) {

            // set Print data
            final EZBDataPrint print = new EZBDataPrint();
            print.setLocation(hold.getStandort().getInhalt());
            print.setCallnr(hold.getShelfmark());
            print.setCoverage(hold.getCoverage(hold));
            print.setComment("availresult.print");
            print.setAmpel("yellow");

            // set new Reference for Print
            final EZBReference ref = new EZBReference();

            // set link for D-D holdings
            final StringBuffer buf = new StringBuffer();
            if (hold.getHolding().getBaseurl() != null) {
                // holding from remote register
                buf.append(hold.getHolding().getBaseurl());
                buf.append("/stockinfo.do?stock=");
                buf.append(hold.getId());
                buf.append('&');
                buf.append(pageForm.getLink());
                ref.setUrl(buf.toString());
            } else {
                // holding from local register
                buf.append("stockinfo.do?stock=");
                buf.append(hold.getId());
                buf.append('&');
                buf.append(pageForm.getLink());
                ref.setUrl(buf.toString());
                ref.setUrl(buf.toString());
            }

            ref.setLabel("availresult.link_title_print");
            print.setInfo(ref);

            ezbform.getPrint().add(print);

        }

    }

    private boolean analyzeEZBResult(final EZBForm ezbform, final OrderForm pageForm, final Connection cn) {

        boolean result = false;
        final Lieferanten supplier = new Lieferanten();

        for (final EZBDataOnline online : ezbform.getOnline()) {
            // 0 free accessible ; 1 partially free accesible
            if (online.getState() == 0 || online.getState() == 1) {
                result = true;
                pageForm.setLieferant(supplier.getLieferantFromName("Internet", cn));
                pageForm.setDeloptions("email");
                // 2 licensed ; 3 partially licensed
            } else if (online.getState() == 2 || online.getState() == 3) {
                result = true;
                pageForm.setLieferant(supplier.getLieferantFromName("abonniert", cn));
                pageForm.setDeloptions("email");
                //
            }
        }

        for (final EZBDataPrint print : ezbform.getPrint()) {
            // in stock ; partially in stock
            if (print.getState() == 2 || print.getState() == 3) {
                result = true;
                pageForm.setLieferant(supplier.getLieferantFromName("abonniert", cn));
                pageForm.setDeloptions("email");
            }
        }

        return result;
    }

    public List<JournalDetails> searchJournalseek(final String zeitschriftentitel_encoded,
            final String artikeltitel_encoded, final OrderForm pageForm, final String concurrentCopyZeitschriftentitel) {

        final List<JournalDetails> issnJS = new ArrayList<JournalDetails>();
        final CodeUrl codeUrl = new CodeUrl();
        final SpecialCharacters specialCharacters = new SpecialCharacters();

        // erster Versuch ueber Journalseek

        String link = "http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=title&editorID=&send=Go&query=";
        link = link + zeitschriftentitel_encoded;

        //      System.out.println("Suchstring ISSN Journalseek erster Versuch: " + link + "\012");
        String content = getWebcontent(link, TIMEOUT_2, RETRYS_3);

        //zweiter Versuch ueber Journalseek
        String zeitschriftentitelEncodedTrunkiert = correctArtikeltitIssnAssist(concurrentCopyZeitschriftentitel);

        if (content.contains("no matches")) { // falls keine Treffer => Suchbegriffe trunkieren
            // Achtung Regexp hat * spezielle Bedeutung...
            zeitschriftentitelEncodedTrunkiert = zeitschriftentitelEncodedTrunkiert.replaceAll("\040", "*\040") + "*";

            zeitschriftentitelEncodedTrunkiert = codeUrl.encodeLatin1(zeitschriftentitelEncodedTrunkiert);
            link = "http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=title&editorID=&send=Go&query="
                    + zeitschriftentitelEncodedTrunkiert;

            //          System.out.println("Suchstring ISSN Journalseek zweiter Versuch: " + link + "\012");
            content = getWebcontent(link, TIMEOUT_2, RETRYS_3);
        }

        //Trefferauswertung

        if ((!content.contains("no matches")) && (!content.contains("Wildcards cannot be used on short searches"))) {

            while (content.contains("query=")) {
                final JournalDetails jd = new JournalDetails();
                jd.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
                int start = content.indexOf("query=");
                final int startIssn = content.indexOf('-', start) - 4;
                jd.setIssn(content.substring(startIssn, startIssn + 9));
                // Zeitschriftentitel extrahieren
                start = content.indexOf('>', startIssn) + 1;
                final int end = content.indexOf('<', startIssn);
                jd.setArtikeltitel(pageForm.getArtikeltitel());
                jd.setArtikeltitel_encoded(artikeltitel_encoded);
                final String zeitschriftentitelJS = specialCharacters.replace(content.substring(start, end));
                jd.setZeitschriftentitel(zeitschriftentitelJS);
                String zeitschriftentitelEncodedJS = zeitschriftentitelJS;
                try {
                    zeitschriftentitelEncodedJS = java.net.URLEncoder.encode(zeitschriftentitelEncodedJS, "ISO-8859-1");
                } catch (final Exception e) {
                    LOG.error("ISSN-Assistent - Point F zeitschriftentitel_encoded_js: " + e.toString());
                }
                jd.setZeitschriftentitel_encoded(zeitschriftentitelEncodedJS);

                jd.setLink("http://journalseek.net/cgi-bin/journalseek/journalsearch.cgi?field=issn&query="
                        + jd.getIssn());

                if (pageForm.isFlag_noissn()) {
                    jd.setAuthor(pageForm.getAuthor());
                    jd.setJahr(pageForm.getJahr());
                    jd.setJahrgang(pageForm.getJahrgang());
                    jd.setHeft(pageForm.getHeft());
                    jd.setSeiten(pageForm.getSeiten());
                }

                content = content.substring(end);
                issnJS.add(jd);
            }
        }

        return issnJS;
    }

    private List<JournalDetails> searchEZBxml(final OrderForm pageForm, final String bibid) {

        List<JournalDetails> result = new ArrayList<JournalDetails>();
        final EZBXML ezb = new EZBXML();

        if (pageForm.getIssn().length() == 0) {
            result = ezb.searchByTitle(pageForm.getZeitschriftentitel(), bibid);
        } else {
            result = ezb.searchByIssn(pageForm.getIssn(), bibid);
        }

        // due to compatibility of legacy code
        final List<JournalDetails> issnRB = new ArrayList<JournalDetails>();
        final CodeUrl codeUrl = new CodeUrl();
        for (final JournalDetails jdRB : result) {

            jdRB.setSubmit(pageForm.getSubmit()); // für modifystock, kann 'minus' enthalten
            jdRB.setArtikeltitel(pageForm.getArtikeltitel());
            jdRB.setArtikeltitel_encoded(codeUrl.encodeLatin1(pageForm.getArtikeltitel()));
            jdRB.setZeitschriftentitel_encoded(codeUrl.encodeLatin1(jdRB.getZeitschriftentitel()));

            if (pageForm.isFlag_noissn()) {
                jdRB.setAuthor(pageForm.getAuthor());
                jdRB.setJahr(pageForm.getJahr());
                jdRB.setJahrgang(pageForm.getJahrgang());
                jdRB.setHeft(pageForm.getHeft());
                jdRB.setSeiten(pageForm.getSeiten());
            }
            issnRB.add(jdRB);
        }

        return issnRB;
    }

    private boolean autoComplete(final OrderForm pageForm, final HttpServletRequest rq) {

        boolean autocomplete = false;
        String link;

        // Syntax WorldCat z.B. http://www.worldcat.org/search?q=ti%3AAntioxidant+supplementation+sepsis+and+systemic+inflammatory+response+syndrome+issn%3A0090-3493&qt=advanced
        // &fq=+dt%3Aart+%3E&qt=advanced (search only for articles)

        final Auth auth = new Auth();

        // make sure that this method will only be run, if the user is logged in
        if (auth.isLogin(rq)) {

            // first correction, e.g. for β => beta
            final String artikeltitelWC = prepareWorldCat2(pageForm.getArtikeltitel());

            // *** up to 2 runs on WorldCat
            // replace different versions of umlauts and use of "did you mean"

            for (int i = 0; i < 2 && !autocomplete; i++) {
                link = getWorldCatLinkBaseSearch(artikeltitelWC, pageForm, i);
                autocomplete = searchWorldCat(link, pageForm);
                // checks for umlauts and avoids an additional run on WorldCat
                if (i < 1 && !checkPrepareWorldCat1(artikeltitelWC)) {
                    i++;
                }
            }

        }

        return autocomplete;
    }

    /**
     * Create the link to search World Cat. The search of this link will return
     * several records. It is necessary to redirect on the detail page
     * (getWorldCatLinkDetailPage) of a record to retrieve the metadata from
     * from Z39.88.
     */
    private String getWorldCatLinkBaseSearch(final String artikeltitelWC, final OrderForm pageForm, final int run) {
        String tmpWC = "";
        final CodeUrl codeUrl = new CodeUrl();

        String artikeltitelEncoded = artikeltitelWC;

        if (artikeltitelEncoded.contains("--")) { // The subtitle can not be search in the title field
            tmpWC = artikeltitelEncoded.substring(artikeltitelEncoded.indexOf("--") + 2); // Subtitle
            artikeltitelEncoded = artikeltitelEncoded.substring(0, artikeltitelEncoded.indexOf("--")); // Title
        }

        artikeltitelEncoded = codeUrl.encodeLatin1(artikeltitelEncoded);
        //               System.out.println("artikeltitel_encoded_wc: " + artikeltitel_encoded);
        if (tmpWC.length() != 0) {
            tmpWC = codeUrl.encodeLatin1(tmpWC);
            //               System.out.println("tmp_wc (Untertitel): " + tmp_wc);
        }

        if (run == 0) {
            artikeltitelEncoded = prepareWorldCat2(artikeltitelEncoded);
            if (tmpWC.length() != 0) {
                tmpWC = prepareWorldCat2(tmpWC);
            }
        }
        if (run == 1) {
            artikeltitelEncoded = prepareWorldCat1(artikeltitelEncoded);
            if (tmpWC.length() != 0) {
                tmpWC = prepareWorldCat1(tmpWC);
            }
        }

        String link = "http://www.worldcat.org/search?q=";
        if (tmpWC.length() != 0) {
            link = link + tmpWC + "+";
        }
        link = link + "ti%3A" + artikeltitelEncoded;
        try {
            if (pageForm.getIssn().length() != 0) {
                link = link + "+issn%3A" + pageForm.getIssn();
            }
        } catch (final Exception e) {
            // keine ISSN vorhanden
            LOG.info("composeLinkWorldCat: no issn available" + e.toString());
        }

        final String linkWC = link + "&fq=+dt%3Aart+%3E&qt=advanced";

        return linkWC;
    }

    /**
     * Get Link of Worldcat detail page.
     */
    private String getWorldCatLinkDetailPage(final String content) {

        final StringBuffer link = new StringBuffer("http://www.worldcat.org");
        link.append(content.substring(content.lastIndexOf("href=\"", content.indexOf("&referer=brief_results")) + 6,
                content.indexOf("&referer=brief_results") + 22));

        return link.toString();
    }

    private boolean searchWorldCat(String link, final OrderForm pageForm) {

        boolean worldcat = false;

        String content = getWebcontent(link, TIMEOUT_2, RETRYS_2);

        // reload to detail page of first record
        if (content.contains("&referer=brief_results")) {
            // get and create link to detail page
            link = getWorldCatLinkDetailPage(content);

            content = getWebcontent(link, TIMEOUT_2, RETRYS_2);

            // get article details from Z39.88
            if (content.contains("url_ver=Z39.88")) {
                worldcat = true;
                pageForm.setRuns_autocomplete(+1);

                String openURL = content.substring(content.indexOf("url_ver=Z39.88"),
                        content.indexOf('>', content.indexOf("url_ver=Z39.88")));
                //                System.out.println("String OpenURL: " + OpenURL);
                openURL = correctWorldCat(openURL);

                // Hier folgt die OpenURL-Auswertung
                final ConvertOpenUrl openurlConv = new ConvertOpenUrl();
                final OpenUrl openurl = new OpenUrl();
                // ContextObject mit Inhalten von content abfüllen
                final ContextObject co = openurl.readOpenUrlFromString(openURL);
                final OrderForm of = openurlConv.makeOrderform(co); // in ein OrderForm übersetzen

                // Artikeltitel als User-Eingabe muss behalten werden
                pageForm.setZeitschriftentitel(prepareWorldCat2(of.getZeitschriftentitel()));
                pageForm.setIssn(of.getIssn());
                pageForm.setJahr(of.getJahr());
                pageForm.setJahrgang(of.getJahrgang());
                pageForm.setHeft(of.getHeft());
                pageForm.setSeiten(of.getSeiten());
                pageForm.setAuthor(of.getAuthor());
                pageForm.setFlag_noissn(of.isFlag_noissn());

            }

        }

        return worldcat;

    }

    private boolean isPubmedSearchWithoutPmidPossible(final OrderForm pageForm) {
        boolean check = false;

        try {

            if (pageForm.getPmid().equals("") // pmid nicht schon vorhanden
                    // issn muss vorhanden sein, damit überhaupt eine gewisse Chance besteht
                    && !pageForm.getIssn().equals("")
                    // Jahrgang oder Heft, da ansonsten sehr wahrscheinlich Epub mit grosser möglicher Fehlerquote
                    && (!pageForm.getJahrgang().equals("") || !pageForm.getHeft().equals(""))) {
                check = true;
            }
        } catch (final Exception e) {
            LOG.error("isPumedSearchWithoutPmidPossible: " + e.toString());
        }

        return check;
    }

    /**
     * Trys to get zdbid from an ISSN out of the local DB
     */
    public String getZdbidFromIssn(final String issn, final Connection cn) {

        String zdbid = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = cn.prepareStatement("SELECT DISTINCT a.zdbid FROM `zdb_id` AS a JOIN issn AS b "
                    + "ON a.identifier_id = b.identifier_id AND a.identifier = b.identifier WHERE b.issn = ?");
            pstmt.setString(1, issn);
            rs = pstmt.executeQuery();

            if (rs.next()) { // only the first zdbid is used
                zdbid = rs.getString("zdbid");
            }

        } catch (final Exception e) {
            LOG.error("getZdbidFromIssn in OrderAction: " + issn + "\040" + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }

        return zdbid;
    }

    /**
     * Detailansicht einer einzelnen Bestellung vorbereiten
     */
    public ActionForward journalorderdetail(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form;
        final OrderState orderstate = new OrderState();
        final Auth auth = new Auth();
        final BestellformAction bfAction = new BestellformAction();
        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        if (auth.isLogin(rq)) {
            forward = SUCCESS;

            final Text cn = new Text();

            try {
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final Bestellungen order = new Bestellungen(cn.getConnection(), pageForm.getBid());
                // URL-hacking unterdrücken!
                if (auth.isLegitimateOrder(rq, order)) {

                    order.setPmid(bfAction.extractPmid(order.getPmid()));
                    order.setDoi(bfAction.extractDoi(order.getDoi()));
                    pageForm.setBestellung(order);
                    pageForm.setStates(orderstate.getOrderState(order, cn.getConnection()));
                    rq.setAttribute("orderform", pageForm);
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("uebersicht");
                    rq.setAttribute(ACTIVEMENUS, mf);

                } else {
                    forward = FAILURE;
                    final ErrorMessage em = new ErrorMessage();
                    em.setError("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute(ERRORMESSAGE, em);
                    LOG.info("journalorderdetail: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("journalorderdetail: " + e.toString());

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
     * Bereitet das erneute Bestellen einer bestehenden Bestellung vor
     */
    public ActionForward prepareReorder(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        String forward = FAILURE;
        final OrderForm pageForm = (OrderForm) form;
        ErrorMessage em = new ErrorMessage();
        final Text cn = new Text();
        final Auth auth = new Auth();

        try {

            if (auth.isLogin(rq)) {
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final Bestellungen order = new Bestellungen(cn.getConnection(), pageForm.getBid());
                // URL-hacking unterdrücken!
                if (auth.isLegitimateOrder(rq, order)) {
                    forward = SUCCESS;

                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("suchenbestellen");
                    rq.setAttribute(ACTIVEMENUS, mf);

                    final OrderForm of = new OrderForm(order);

                    rq.setAttribute("ofjo", of);

                    // mediatype != Artikel: go directly to the page for saving/modifying the order
                    // and not to checkavailability
                    if (!of.getMediatype().equals("Artikel")) {
                        forward = "save";
                    }

                } else {
                    forward = FAILURE;
                    em.setError("error.hack");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute(ERRORMESSAGE, em);
                    LOG.info("prepareReorder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }

            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } finally {
            cn.close();
        }

        return mp.findForward(forward);
    }

    public ActionForward prepareIssnSearch(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form;
        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {

            if (!pageForm.isAutocomplete() && pageForm.getRuns_autocomplete() == 0
                    && pageForm.getArtikeltitel().length() != 0) { // noch kein autocomplete ausgeführt...
                // ...Funktion AutoComplete ausführen
                pageForm.setAutocomplete(autoComplete(pageForm, rq));

                // basically replaces greek alphabet to alpha, beta...
                pageForm.setArtikeltitel(prepareWorldCat2(pageForm.getArtikeltitel()));

            }

            forward = SUCCESS;
            try {
                rq.setAttribute("orderform", pageForm);

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("prepareIssnSearch: " + e.toString());
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
        return mp.findForward(forward);
    }

    /**
     * Sucht u.a. die Benutzer des aktiven Kontos heraus, um sie für die
     * Bestellung zur Auswahl anzubieten
     */
    public ActionForward prepare(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final Text cn = new Text();
        Text t = new Text();
        final Auth auth = new Auth();

        final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
        OrderForm pageForm = (OrderForm) form;

        if (rq.getAttribute("ofjo") != null) {
            // Übergabe aus checkAvailability von getOpenUrlRequest und nach Kunde neu erstellen...
            pageForm = (OrderForm) rq.getAttribute("ofjo");
            rq.setAttribute("ofjo", pageForm);
        }

        if (pageForm.getKkid() == null) {
            t = auth.grantAccess(rq);
        }

        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        if ((t != null && t.getInhalt() != null) || (pageForm.getKkid() != null || pageForm.getBkid() != null)
                || auth.isLogin(rq)) {

            forward = SUCCESS;

            try {

                if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                    final List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                    final AbstractBenutzer b = ui.getBenutzer();
                    kontouser.add(b);
                    pageForm.setKontouser(kontouser);
                }
                if (auth.isBibliothekar(rq) || auth.isAdmin(rq)) {
                    pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                }

                if (ui != null) { // bei IP-basiertem Zugriff kein ui vorhanden
                    // in pageForm Defaultpreis von Subito legen, damit Preisauswahl
                    // für manuelle Bestellung bei Subito vorhanden ist
                    DefaultPreis dp = new DefaultPreis();
                    dp = dp.getDefaultPreis("Subito", ui.getKonto().getId(), cn.getConnection());
                    pageForm.setPreisvorkomma(dp.getVorkomma());
                    pageForm.setPreisnachkomma(dp.getNachkomma());
                    pageForm.setWaehrung(dp.getWaehrung());
                    // Default Bestellart setzen, falls nicht schon eine deloption übers Formular angegeben wurde
                    if (ui.getKonto().getFaxno() == null
                            && (pageForm.getDeloptions() == null || pageForm.getDeloptions().equals(""))) {
                        pageForm.setDeloptions(ui.getKonto().getDefault_deloptions());
                    }
                }

                if (pageForm.getSubmit().contains("GBV")) { // Bestellung über GBV
                    if (auth.isUserGBVBestellung(rq)) { // verhindert URL-hacking
                        // setzt z.B. "zur GBV-Bestellung" auf "GBV", um in journalorder.jsp
                        // zwischen Subito- und GBV-Bestellungen unterscheiden zu können.
                        pageForm.setSubmit("GBV");
                        pageForm.setMaximum_cost("8"); // Default bei GBV
                        // ISIL wird für autom. Bestellung benötigt
                        if (ui.getKonto().getIsil() == null || ui.getKonto().getGbvrequesterid() == null) {
                            pageForm.setManuell(true);
                        } else {
                            pageForm.setManuell(false);
                        }
                    } else {
                        pageForm.setSubmit("bestellform");
                    }
                }

                // Benutzer ohne Bestellberechtigung werden auf Bestellformular
                // (Mail an Bibliothek statt direkt bestellen) weitergeleitet
                // Bei Übergabe aus Linkresolver, Einloggen und mediatype != Artikel => auf
                // Bestellformular, da keine Bestellung über Subito möglich...
                // ui == null => IP-basierter Zugriff

                if (ui == null
                        || // IP-basierter Zugriff
                           // erste Kondition ist problematisch falls die Übergabe ab pl (prepareLogin)
                           // kommt und der Kunde Benutzer mit GBV-Bestellberechtigung ist.
                           // d.h. GBV-Submit ist nicht vorhanden und er hat keine Wahl
                           // den Artikel beim GBV zu bestellen....

                        (!auth.isUserSubitoBestellung(rq)
                        // keine Bestellberechtigung
                        && !(auth.isUserGBVBestellung(rq) && pageForm.getSubmit().equals("GBV")))
                        // Für Subito nur Artikel zugelassen...
                        || (!pageForm.getSubmit().equals("GBV") && auth.isBenutzer(rq) && (pageForm.getMediatype() == null || !pageForm
                                .getMediatype().equals("Artikel")))
                        || pageForm.getSubmit().contains("meine Bibliothek")
                        || pageForm.getSubmit().contains("my library")
                        || pageForm.getSubmit().contains("ma bibliothèque")
                        // der Kunde will das Doku bei seiner Bibliothek bestellen
                        || pageForm.getSubmit().contains("bestellform")) {

                    forward = "bestellform";
                    if (pageForm.getDeloptions() == null || // Defaultwert deloptions
                            (!pageForm.getDeloptions().equals("post") && !pageForm.getDeloptions().equals("fax to pdf") && !pageForm
                                    .getDeloptions().equals("urgent"))) {
                        pageForm.setDeloptions("fax to pdf");
                    }
                }

                // Bei Bibliothekaren läuft eine Nicht-GBV-Bestellung mit
                // mediatype!=Artikel auf das Formular zum manuellen Speichern einer Bestellung
                if (ui != null && !pageForm.getSubmit().equals("GBV") && !auth.isBenutzer(rq)
                        && (pageForm.getMediatype() == null || !pageForm.getMediatype().equals("Artikel"))) {
                    forward = "save";
                }
                // Umleitung bei Subito-Bestellung auf redirectsubito, da autom. Bestellung nicht mehr machbar
                if (forward.equals(SUCCESS) && !pageForm.getSubmit().equals("GBV")) {
                    forward = "redirectsubito";
                    pageForm.setLink("http://www.subito-doc.de/order/po.php?BI=CH_SO%2FDRDOC&VOL="
                            + pageForm.getJahrgang() + "/" + pageForm.getHeft() + "&APY=" + pageForm.getJahr() + "&PG="
                            + pageForm.getSeiten() + "&SS=" + pageForm.getIssn() + "&JT="
                            + pageForm.getZeitschriftentitel() + "&ATI=" + pageForm.getArtikeltitel() + "&AAU="
                            + pageForm.getAuthor());
                    // deloptions einstellen
                    if (pageForm.getDeloptions() != null && pageForm.getDeloptions().equals("")) {
                        if (ui.getKonto().getFaxno() != null) {
                            pageForm.setDeloptions("fax to pdf");
                        } else {
                            pageForm.setDeloptions(ui.getKonto().getDefault_deloptions());
                        }
                    }
                }

                // für Get-Methode in PrepareLogin of URL-codieren
                pageForm = pageForm.encodeOrderForm(pageForm);

                rq.setAttribute("orderform", pageForm);

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("prepare: " + e.toString());

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

        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute(ACTIVEMENUS, mf);

        return mp.findForward(forward);
    }

    /**
     * Bereitet das Abspeichern aller momentan vorhandenen Angaben vor
     */
    public ActionForward prepareJournalSave(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        final Auth auth = new Auth();
        final Text cn = new Text();
        final Lieferanten supplier = new Lieferanten();
        if (rq.getAttribute("ofjo") != null) {
            pageForm = (OrderForm) rq.getAttribute("ofjo");
            rq.setAttribute("ofjo", null);
        }
        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        if (auth.isLogin(rq)) {
            forward = SUCCESS;
            try {
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                final Texttyp tty = new Texttyp();

                if (auth.isBenutzer(rq)) { // user may only see his own address
                    final List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                    final AbstractBenutzer b = ui.getBenutzer();
                    kontouser.add(b);
                    pageForm.setKontouser(kontouser);
                } else {
                    pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                }

                if (pageForm.getDeloptions() == null || pageForm.getDeloptions().equals("")) {
                    pageForm.setDeloptions("email"); // default
                }
                if (pageForm.getMediatype() == null) {
                    pageForm.setMediatype("Artikel");
                } // default value 'article'
                  // if not coming from function reorder with an existing bid
                if (pageForm.getBid() == null && pageForm.getMediatype().equals("Buch")) {
                    pageForm.setDeloptions("post"); // logical consequence
                    pageForm.setFileformat("Papierkopie"); // logical consequence
                }

                long id = 2; // Bestellstati
                tty.setId(id);
                pageForm.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                pageForm.setQuellen(supplier.getLieferanten(ui, cn.getConnection()));
                id = 7; // Waehrungen
                tty.setId(id);
                pageForm.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));
                final DefaultPreis dp = new DefaultPreis();
                pageForm.setDefaultpreise(dp.getAllKontoDefaultPreise(ui.getKonto().getId(), cn.getConnection()));

                // benötigt damit auf journalsave.jsp lieferant.name nicht kracht...
                Lieferanten l = new Lieferanten();
                if (pageForm.getLid() != null && !pageForm.getLid().equals("") && !pageForm.getLid().equals("0")) { // lid wurde übermittelt aus pageForm
                    l = supplier.getLieferantFromLid(Long.valueOf(pageForm.getLid()), cn.getConnection());
                } else {
                    l.setName("k.A.");
                    l.setLid(Long.valueOf(0));
                }

                pageForm.setLieferant(l);
                pageForm.setBestellquelle(l.getName());

                if (pageForm.getStatus() == null) {
                    pageForm.setStatus("bestellt");
                } // Default

                // deloptions
                final Set<String> dynamicDeloptions = getDeloptions(ui.getKonto(), cn.getConnection());

                rq.setAttribute("delopts", dynamicDeloptions);

                rq.setAttribute("orderform", pageForm);

            } catch (final Exception e) {
                forward = FAILURE;

                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("prepareJournalSave: " + e.toString());
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

        final ActiveMenusForm mf = new ActiveMenusForm();
        mf.setActivemenu("suchenbestellen");
        rq.setAttribute(ACTIVEMENUS, mf);

        return mp.findForward(forward);
    }

    /**
     * Speichert eine manuelle Bestellung ab
     */
    public ActionForward saveOrder(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form;
        final Lieferanten supplier = new Lieferanten();
        final OrderState orderstate = new OrderState();
        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {

            // aufgrund von IE Bug wird value bei einem eigenen Icon im submit nicht übermittelt:
            if (!pageForm.getSubmit().equals("neuen Kunden anlegen") && !pageForm.getSubmit().equals("add new patron")
            // Post-Methode um vor dem Abspeichern einer Bestellung einen neuen Kunden anzulegen
                    && !pageForm.getSubmit().equals("Ajouter un nouveau client")) {

                forward = SUCCESS;

                final Text cn = new Text();

                try {
                    Bestellungen b = new Bestellungen();
                    final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                    // Defaultwert, falls keine Angaben (stellt sicher, dass History funktioniert)
                    if (pageForm.getStatus().equals("0")) {
                        pageForm.setStatus("bestellt");
                    }

                    pageForm.setKaufpreis(b.stringToBigDecimal(pageForm.getPreisvorkomma(),
                            pageForm.getPreisnachkomma()));

                    try {

                        if (pageForm.isPreisdefault()) {
                            final DefaultPreis dp = new DefaultPreis(pageForm, ui);
                            dp.saveOrUpdate(cn.getConnection());
                        }

                    } catch (final Exception e) {
                        LOG.error("SaveOrder Default-Preis eintragen: " + e.toString());
                    }

                    final Date d = new Date();
                    final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final String datum = fmt.format(d, ui.getKonto().getTimezone());
                    //                System.out.println("Bestelldatum: " + datum);

                    //               Bestellung in DB speichern:
                    AbstractBenutzer kunde = new AbstractBenutzer();
                    kunde = kunde.getUser(Long.valueOf(pageForm.getForuser()), cn.getConnection());

                    if (pageForm.getBid() != null) {
                        // zum Updaten vollständige Bestellung holen
                        b = new Bestellungen(cn.getConnection(), pageForm.getBid());
                        if (!pageForm.getStatus().equals(b.getStatustext())) { // falls Status verändert wurde

                            // um zu verhindern, dass eine Bestellung kein Statusdatum erhält,
                            // falls beim Statusschreiben etwas schief geht
                            b.setStatusdate(datum);
                            b.setStatustext(pageForm.getStatus());

                            final Text t = new Text(cn.getConnection(), pageForm.getStatus());

                            // Status setzen
                            orderstate.setNewOrderState(b, ui.getKonto(), t, null, ui.getBenutzer().getEmail(),
                                    cn.getConnection());

                        }
                    }

                    // falls keine Kundenangaben => Besteller = eingeloggter User
                    if (kunde == null) {
                        kunde = ui.getBenutzer();
                    }

                    b.setKonto(ui.getKonto());
                    b.setBenutzer(kunde);

                    b.setLieferant(supplier.getLieferantFromLid(Long.valueOf(pageForm.getLid()), cn.getConnection()));
                    if (b.getLieferant().getSigel() == null || b.getLieferant().getSigel().equals("")) {
                        // doppelter Eintrag um Sortieren und Suche zu ermöglichen/vereinfachen
                        b.setBestellquelle(b.getLieferant().getName());
                    } else { // Eintrag mit Sigel
                        // doppelter Eintrag um Sortieren und Suche zu ermöglichen/vereinfachen
                        b.setBestellquelle(b.getLieferant().getSigel() + "\040" + b.getLieferant().getName());
                    }
                    b.setPriority(pageForm.getPrio());
                    b.setDeloptions(pageForm.getDeloptions());
                    b.setFileformat(pageForm.getFileformat());
                    if (pageForm.getDeloptions().equalsIgnoreCase("post")) {
                        b.setFileformat("Papierkopie"); // logische Konsequenz...
                    }
                    b.setHeft(pageForm.getHeft());
                    b.setSeiten(pageForm.getSeiten());
                    b.setIssn(pageForm.getIssn());
                    b.setAutor(pageForm.getAuthor());
                    b.setZeitschrift(pageForm.getZeitschriftentitel());
                    b.setJahr(pageForm.getJahr());
                    b.setArtikeltitel(pageForm.getArtikeltitel());
                    b.setJahrgang(pageForm.getJahrgang());
                    b.setDoi(pageForm.getDoi());
                    b.setPmid(pageForm.getPmid());
                    b.setIsbn(pageForm.getIsbn());
                    b.setMediatype(pageForm.getMediatype());
                    b.setVerlag(pageForm.getVerlag());
                    b.setKapitel(pageForm.getKapitel());
                    b.setBuchtitel(pageForm.getBuchtitel());
                    b.setInterne_bestellnr(pageForm.getInterne_bestellnr());
                    // Subitonr. normalisieren, da relativ komplex aufgebaut SUBITO:2009040801219
                    b.setSubitonr(extractSubitonummer(pageForm.getSubitonr()));
                    b.setGbvnr(pageForm.getGbvnr()); // relativ einfach aufgebaut: A09327811X

                    b.setSystembemerkung(pageForm.getAnmerkungen());
                    b.setNotizen(pageForm.getNotizen());
                    b.setKaufpreis(pageForm.getKaufpreis());
                    if (pageForm.getKaufpreis() != null) {
                        b.setWaehrung(pageForm.getWaehrung());
                    } else {
                        b.setWaehrung(null);
                    }

                    if (pageForm.getBid() == null) { // hier wird eine neue Bestellung abgespeichert

                        // um zu verhindern, dass eine Bestellung kein Datum erhält,
                        // falls beim Statusschreiben etwas schief geht
                        b.setOrderdate(datum);
                        b.setStatusdate(datum);
                        b.setStatustext(pageForm.getStatus());

                        b.save(cn.getConnection());

                        //Sicherheit, ob das so wirklich klappt mit Benachrichtigung
                        if (b.getId() == null) {
                            b = b.getOrderSimpleWay(b, cn.getConnection());
                            log.warn("b.getId() has been null! We had to use b.getOrderSimpleWay!");
                        }

                        // klappt leider nicht zuverlässig (s. Workaround oben)
                        final Text t = new Text(cn.getConnection(), pageForm.getStatus());

                        // Status Bestellt setzen
                        orderstate.setNewOrderState(b, ui.getKonto(), t, null, ui.getBenutzer().getEmail(),
                                cn.getConnection());

                    } else { // hier wird eine bestehende Bestellung geupdated
                        b.update(cn.getConnection());
                    }

                    rq.setAttribute("orderform", pageForm);

                } catch (final Exception e) {
                    forward = FAILURE;

                    final ErrorMessage em = new ErrorMessage();
                    em.setError("error.save");
                    em.setLink("searchfree.do?activemenu=suchenbestellen");
                    rq.setAttribute(ERRORMESSAGE, em);
                    LOG.error("saveOrder: " + e.toString());

                } finally {
                    cn.close();
                }

            } else { // Umleitung zu Kundenanlegen
                forward = "newcustomer";
                pageForm.setOrigin("js");
                rq.setAttribute("orderform", pageForm);
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
     * bereitet das manuelle Ändern einer Bestellung vor
     */
    public ActionForward prepareModifyOrder(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        OrderForm pageForm = (OrderForm) form;
        // Make sure method is only accessible when user is logged in
        String forward = FAILURE;
        final Auth auth = new Auth();
        if (auth.isLogin(rq)) {
            forward = SUCCESS;

            final Text cn = new Text();

            try {
                final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");

                final Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());

                if (b.getId() != null) {

                    pageForm = new OrderForm(b);

                    if (auth.isBenutzer(rq)) { // Benutzer sehen nur die eigenen Adressen
                        final List<AbstractBenutzer> kontouser = new ArrayList<AbstractBenutzer>();
                        final AbstractBenutzer ab = ui.getBenutzer();
                        kontouser.add(ab);
                        pageForm.setKontouser(kontouser);
                    } else {
                        pageForm.setKontouser(ui.getBenutzer().getKontoUser(ui.getKonto(), cn.getConnection()));
                    }

                    final Texttyp tty = new Texttyp();
                    long id = 2; // Bestellstati
                    tty.setId(id);
                    pageForm.setStatitexts(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));

                    final Lieferanten supplier = new Lieferanten();
                    pageForm.setQuellen(supplier.getLieferanten(ui, cn.getConnection()));
                    id = 7; // Waehrungen
                    tty.setId(id);
                    pageForm.setWaehrungen(cn.getAllTextPlusKontoTexts(tty, ui.getKonto().getId(), cn.getConnection()));

                    final DefaultPreis dp = new DefaultPreis();
                    pageForm.setDefaultpreise(dp.getAllKontoDefaultPreise(ui.getKonto().getId(), cn.getConnection()));

                    // deloptions
                    final Set<String> dynamicDeloptions = getDeloptions(ui.getKonto(), cn.getConnection());

                    rq.setAttribute("delopts", dynamicDeloptions);
                    rq.setAttribute("orderform", pageForm);

                    if (b.checkAnonymize(b)) {

                        forward = FAILURE;
                        final ErrorMessage em = new ErrorMessage("error.anonymised",
                                "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute(ERRORMESSAGE, em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                    }

                    if (auth.isBibliothekar(rq)
                    // Sicherstellen, dass der Bibliothekar nur Bestellungen vom eigenen Konto bearbeitet!
                            && !b.getKonto().getId().equals(ui.getKonto().getId())) {
                        System.out.println("URL-hacking... ;-)");
                        forward = FAILURE;
                        final ErrorMessage em = new ErrorMessage("error.hack",
                                "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute(ERRORMESSAGE, em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                        LOG.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                    }
                    if (auth.isBenutzer(rq)
                    // Sicherstellen, dass der User nur eigene Bestellungen bearbeitet!
                            && !b.getBenutzer().getId().equals(ui.getBenutzer().getId())) {
                        // Sicherstellen, dass der User nur eigene Bestellungen bearbeitet!
                        System.out.println("URL-hacking... ;-)");
                        forward = FAILURE;
                        final ErrorMessage em = new ErrorMessage("error.hack",
                                "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute(ERRORMESSAGE, em);
                        rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                        LOG.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                    }

                } else {
                    forward = FAILURE;
                    final ErrorMessage em = new ErrorMessage("error.hack",
                            "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                    rq.setAttribute(ERRORMESSAGE, em);
                    rq.setAttribute("orderform", null); // unterdrücken von "manuell bestellen"
                    LOG.info("prepareModifyOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());

                }
            } catch (final Exception e) {
                forward = FAILURE;
                final ErrorMessage em = new ErrorMessage();
                em.setError("error.system");
                em.setLink("searchfree.do?activemenu=suchenbestellen");
                rq.setAttribute(ERRORMESSAGE, em);
                LOG.error("prepareModifyOrder: " + e.toString());

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
     * löscht eine Bestellung
     */
    public ActionForward prepareDeleteOrder(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form;
        String forward = FAILURE;
        final Text cn = new Text();

        try {

            final Auth auth = new Auth();
            if (auth.isLogin(rq)) { // Test auf gültige Session
                final Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());

                if (b.getId() != null && // BID muss vorhanden sein
                        // nur Bibliothekare und Admins dürfen Bestellungen löschen
                        (auth.isBibliothekar(rq) || auth.isAdmin(rq)) && auth.isLegitimateOrder(rq, b)) { // nur kontoeigene Bestellungen dürfen gelöscht werden

                    forward = "promptDelete";
                    pageForm.setDelete(true);
                    pageForm.setBestellung(b);
                    rq.setAttribute("orderform", pageForm);

                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("uebersicht");
                    rq.setAttribute(ACTIVEMENUS, mf);

                } else {
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("uebersicht");
                    rq.setAttribute(ACTIVEMENUS, mf);
                    final ErrorMessage em = new ErrorMessage("error.hack",
                            "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                    rq.setAttribute(ERRORMESSAGE, em);
                    final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                    LOG.info("prepareDeleteOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } finally {
            cn.close();
        }

        return mp.findForward(forward);
    }

    /**
     * löscht eine Bestellung
     */
    public ActionForward deleteOrder(final ActionMapping mp, final ActionForm form, final HttpServletRequest rq,
            final HttpServletResponse rp) {

        final OrderForm pageForm = (OrderForm) form;
        String forward = FAILURE;
        final Text cn = new Text();

        try {

            final Auth auth = new Auth();
            if (auth.isLogin(rq)) { // Test auf gültige Session
                final Bestellungen b = new Bestellungen(cn.getConnection(), pageForm.getBid());

                if (b.getId() != null && // BID muss vorhanden sein
                        // nur Bibliothekare und Admins dürfen Bestellungen löschen
                        (auth.isBibliothekar(rq) || auth.isAdmin(rq)) && auth.isLegitimateOrder(rq, b)) { // nur kontoeigene Bestellungen dürfen gelöscht werden

                    if (b.deleteOrder(b, cn.getConnection())) {
                        forward = SUCCESS;
                        final ActiveMenusForm mf = new ActiveMenusForm();
                        mf.setActivemenu("uebersicht");
                        rq.setAttribute(ACTIVEMENUS, mf);
                        final Message m = new Message("message.deleteorder");
                        m.setLink("listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                        rq.setAttribute("message", m);
                    } else { // löschen fehlgeschlagen
                        final ErrorMessage em = new ErrorMessage();
                        em.setError("error.system");
                        em.setLink("searchfree.do?activemenu=suchenbestellen");
                        rq.setAttribute(ERRORMESSAGE, em);
                        LOG.error("deleteOrder: couldn't delete order");
                    }

                } else {
                    final ActiveMenusForm mf = new ActiveMenusForm();
                    mf.setActivemenu("uebersicht");
                    rq.setAttribute(ACTIVEMENUS, mf);
                    final ErrorMessage em = new ErrorMessage("error.hack",
                            "listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc");
                    rq.setAttribute(ERRORMESSAGE, em);
                    final UserInfo ui = (UserInfo) rq.getSession().getAttribute("userinfo");
                    LOG.info("deleteOrder: prevented URL-hacking! " + ui.getBenutzer().getEmail());
                }
            } else {
                final ActiveMenusForm mf = new ActiveMenusForm();
                mf.setActivemenu("login");
                rq.setAttribute(ACTIVEMENUS, mf);
                final ErrorMessage em = new ErrorMessage("error.timeout", "login.do");
                rq.setAttribute(ERRORMESSAGE, em);
            }

        } finally {
            cn.close();
        }

        return mp.findForward(forward);
    }

    public String extractYear(final String date) {
        String year = "";
        // Search pattern works from 14th century till 22th century. This sould be usable for some time...
        final Pattern p = Pattern
                .compile("13[0-9]{2}|14[0-9]{2}|15[0-9]{2}|16[0-9]{2}|17[0-9]{2}|18[0-9]{2}|19[0-9]{2}|20[0-9]{2}|21[0-9]{2}");
        final Matcher m = p.matcher(date);
        try {
            if (m.find()) { // Only takes the first hit...
                year = date.substring(m.start(), m.end());
            }
        } catch (final Exception e) {
            LOG.error("extractYear(String date): " + date + "\040" + e.toString());
        }

        return year;
    }

    private Set<String> getDeloptions(final Konto konto, final Connection cn) {

        final Set<String> result = new TreeSet<String>();

        final BestellParam bp = new BestellParam();
        // get all Bestellparams for the account
        final List<BestellParam> bps = bp.getAllBestellParam(konto, cn);

        for (final BestellParam param : bps) {
            if (param.getLieferart_value1() != null) {
                result.add(param.getLieferart_value1());
            }
            if (param.getLieferart_value2() != null) {
                result.add(param.getLieferart_value2());
            }
            if (param.getLieferart_value3() != null) {
                result.add(param.getLieferart_value3());
            }
        }

        return result;
    }

    private String extractSubitonummer(String subitonr) {

        final Pattern z = Pattern.compile("[A-Z]{0,2}[0-9]+");
        final Matcher w = z.matcher(subitonr);
        try {
            if (w.find()) { // Idee: nur erste Zahl abfüllen...
                subitonr = "SUBITO:" + subitonr.substring(w.start(), w.end());
            }
        } catch (final Exception e) {
            LOG.error("String extractSubitonummer(String subitonr): " + subitonr + "\040" + e.toString());
        }

        return subitonr;
    }

    private String getWebcontent(final String link, final int timeoutMs, final int retrys) {
        final Http http = new Http();

        return http.getWebcontent(link, timeoutMs, retrys);
    }

    private String shortenGoogleSearchPhrase(String artikeltitel) {

        try {
            // Google is limited for the length of the search term while searching as a phrase
            if (artikeltitel.length() > 75 && artikeltitel.substring(0, 75).contains("\040")) {
                artikeltitel = artikeltitel.substring(0, artikeltitel.lastIndexOf('\040', 75));
            }

        } catch (final Exception e) {
            LOG.error("googlePreparePhraseSearch: " + artikeltitel + "\040" + e.toString());
        }

        return artikeltitel;
    }

    private Message handleGoogleCaptcha(String content) {

        final Message m = new Message();
        final SpecialCharacters specialCharacters = new SpecialCharacters();
        content = specialCharacters.replace(content);
        String link = "";
        String id = "";

        if (content.contains("sorry/image?id=")) { // Versuch 1

            link = content.substring(content.indexOf("sorry/image?id="),
                    content.indexOf('"', content.indexOf("sorry/image?id=")));
            if (link.contains("&")) {
                link = link.substring(0, link.indexOf('&'));
            }
            link = "http://www.google.ch/" + link;
            id = link.substring(link.indexOf("sorry/image?id=") + 15);

        } else {

            if (content.contains("name=\"id\" value=\"")) { // Versuch 2

                id = content.substring(content.indexOf("name=\"id\" value=\"") + 17,
                        content.indexOf("\"", content.indexOf("name=\"id\" value=\"") + 17));
                link = "http://www.google.ch/sorry/image?id=" + id;

            }

        }

        m.setLink(link);
        m.setMessage(id);

        log.warn("Google-Captcha!");
        // Important message
        final MHelper mh = new MHelper();
        mh.sendErrorMail("Google-Captcha Alarm!!!", "handleGoogleCaptcha:\012" + content);

        return m;
    }

    private String correctWorldCat(final String input) {
        // Methode um abartige Umlaute aus Resultat von WorldCat zu entfernen
        String output = input;

        output = output.replaceAll("%CC%90%C6%B0", "");
        output = output.replaceAll(".\\+--&", "&"); // WorldCat setzt manchmal '. --' ans Ende eines Identifiers...

        return output;
    }

    private String correctGoogleURL(final String input) {
        // Methode um gefundene Google-URL für die Ausgabe vorzubereiten
        String output = input;
        final SpecialCharacters specialCharacters = new SpecialCharacters();

        // hier ist eigentlich kein Treffer gefunden worde. Google schlägt ähnliche Treffer mit dieser URL-Syntax vor...
        if (input.startsWith("/url?q=")) {
            output = input.substring(7);
        }

        output = specialCharacters.replace(output);

        return output;
    }

    private String correctArtikeltitIssnAssist(final String input) {
        // Methode um die Trefferchancen beim ISSN-Assistenten zu erhöhen
        String output = input;
        final SpecialCharacters specialCharacters = new SpecialCharacters();

        output = output.replaceAll("\040und\040", "\040"); // entfernt "und"
        output = specialCharacters.replace(output); // übersetzt allfällige &amp; in &
        output = output.replaceAll("\040&\040", "\040"); // entfernt &
        output = output.replaceAll("\040+\040", "\040"); // entfernt +

        return output;
    }

    /**
     * Holt aus einer ArrayList<Bestand> die eigenen Bestände
     */
    private List<Bestand> extractInternalHoldings(final List<Bestand> bestaende, final long daiaId) {

        final ArrayList<Bestand> internalHoldings = new ArrayList<Bestand>();

        try {

            for (final Bestand b : bestaende) {
                if (b.getHolding().getKid().equals(daiaId)) {
                    internalHoldings.add(b);
                }
            }

        } catch (final Exception e) {
            LOG.error("ArrayList<Bestand> extractInternalHoldings (ArrayList<Bestand> bestaende, long daiaId): "
                    + e.toString());
        }

        return internalHoldings;
    }

    /**
     * Holt aus einer ArrayList<Bestand> die Fremdbestände
     */
    private List<Bestand> extractExternalHoldings(final List<Bestand> bestaende, final long daiaId, final UserInfo ui) {

        final List<Bestand> externalHoldings = new ArrayList<Bestand>();

        try {
            for (final Bestand b : bestaende) {

                // TODO: we need a better mechanism to manage the indication of external holdings
                // We make sure that the holdings are from the same country as the requester
                if (ui != null && b.getHolding().getKonto().getLand() != null
                        && b.getHolding().getKonto().getLand().equals(ui.getKonto().getLand())
                        // add to list if it is not a holding from the own account
                        && !b.getHolding().getKid().equals(daiaId) && !b.isInternal()) {
                    externalHoldings.add(b);
                }
            }

        } catch (final Exception e) {
            LOG.error("ArrayList<Bestand> extractExternalHoldings (ArrayList<Bestand> bestaende, long daiaId): "
                    + e.toString());
        }

        return externalHoldings;
    }

    /**
     * Nimmt grundsätzlich ein Mapping der KIDs aus verschiedenen
     * Doctor-Doc-Instanzen vor um die Bestände den entsprechenden Konti
     * zuordenen zu können.
     */
    private long getDaiaId(final long kid) {
        long daiaId = 0;
        final Text cn = new Text();

        try {
            final Text t = new Text(cn.getConnection(), new Texttyp("DAIA-ID", cn.getConnection()), kid);
            if (t != null && t.getInhalt() != null) {
                daiaId = Long.valueOf(t.getInhalt());
            }
        } catch (final Exception e) {
            LOG.error("getDaiaId - kid: " + kid + "\040" + e.toString());
        } finally {
            cn.close();
        }

        return daiaId;
    }

    private boolean checkPrepareWorldCat1(String input) {

        // Methode um zu prüfen, ob eine zusätzliche WorldCat-Abfrage gefahren werden muss...
        boolean check = false;
        final CodeUrl codeUrl = new CodeUrl();

        input = codeUrl.encodeLatin1(input);

        if (input.contains("%E4") || input.contains("%F6") || input.contains("%FC") || input.contains("%C4")
                || input.contains("%D6") || input.contains("%DC")) {
            check = true;
        }

        // nicht abschliessend...

        return check;
    }

    private String prepareWorldCat1(final String input) {

        // Methode um Treffer aus WorldCat bei Suchstrings mit Umlauten zu erhalten
        String output = input;

        output = output.replaceAll("\\+-\\+", "\\+"); // WorldCat akzeptiert keine " - "

        output = output.replaceAll("%E4", "u%92a");
        output = output.replaceAll("%F6", "u%92o");
        output = output.replaceAll("%FC", "u%92u");
        output = output.replaceAll("%C4", "u%92A");
        output = output.replaceAll("%D6", "u%92O");
        output = output.replaceAll("%DC", "u%92U");
        // nicht abschliessend...

        //      C0  192  À  ANSI 192  großes A Grave
        //      C1  193  Á  ANSI 193  großes A Acute
        //      C2  194  Â  ANSI 194  großes A Zirkumflex
        //      C3  195  Ã  ANSI 195  großes A Tilde
        //      C4  196  Ä  ANSI 196  großes A Umlaut
        //      C5  197  Å  ANSI 197  großes A mit Ring
        //      C6  198  Æ  ANSI 198  große AE-Ligatur
        //      C7  199  Ç  ANSI 199  großes C mit Cedille
        //      C8  200  È  ANSI 200  großes E Grave
        //      C9  201  É  ANSI 201  großes E Acute
        //      CA  202  Ê  ANSI 202  großes E Zirkumflex
        //      CB  203  Ë  ANSI 203  großes E Trema
        //      CC  204  Ì  ANSI 204  großes I Grave
        //      CD  205  Í  ANSI 205  großes I Acute
        //      CE  206  Î  ANSI 206  großes I Zirkumflex
        //      CF  207  Ï  ANSI 207  großes I Trema
        //      D0  208  Ð  ANSI 208  Isländisches großes Eth
        //      D1  209  Ñ  ANSI 209  großes N Tilde
        //      D2  210  Ò  ANSI 210  großes O Grave
        //      D3  211  Ó  ANSI 211  großes O Acute
        //      D4  212  Ô  ANSI 212  großes O Zirkumflex
        //      D5  213  Õ  ANSI 213  großes O Tilde
        //      D6  214  Ö  ANSI 214  großes O Umlaut
        //      D7  215  ×  ANSI 215  Multiplikationszeichen
        //      D8  216  Ø  ANSI 216  großes O mit diagonalem Strich
        //      D9  217  Ù  ANSI 217  großes U Grave
        //      DA  218  Ú  ANSI 218  großes U Acute
        //      DB  219  Û  ANSI 219  großes U Zirkumflex
        //      DC  220  Ü  ANSI 220  großes U Umlaut
        //      DD  221  Ý  ANSI 221  großes Y Acute
        //      DE  222  Þ  ANSI 222  Isländisches großes Thorn
        //      DF  223  ß  ANSI 223  Deutsches scharfes S (sz-Ligatur)
        //      E0  224  à  ANSI 224  kleines a Grave
        //      E1  225  á  ANSI 225  kleines a Acute
        //      E2  226  â  ANSI 226  kleines a Zirkumflex
        //      E3  227  ã  ANSI 227  kleines a Tilde
        //      E4  228  ä  ANSI 228  kleines a Umlaut
        //      E5  229  å  ANSI 229  kleines a Ring
        //      E6  230  æ  ANSI 230  kleine ae-Ligatur
        //      E7  231  ç  ANSI 231  kleines c Cedille
        //      E8  232  è  ANSI 232  kleines e Grave
        //      E9  233  é  ANSI 233  kleines e Acute
        //      EA  234  ê  ANSI 234  kleines e Zirkumflex
        //      EB  235  ë  ANSI 235  kleines e Trema
        //      EC  236  ì  ANSI 236  kleines i Grave
        //      ED  237  í  ANSI 237  kleines i Acute
        //      EE  238  î  ANSI 238  kleines i Zirkumflex
        //      EF  239  ï  ANSI 239  kleines i Trema
        //      F0  240  ð  ANSI 240  Isländisches kleines eth
        //      F1  241  ñ  ANSI 241  kleines n Tilde
        //      F2  242  ò  ANSI 242  kleines o Grave
        //      F3  243  ó  ANSI 243  kleines o Acute
        //      F4  244  ô  ANSI 244  kleines o Zirkumflex
        //      F5  245  õ  ANSI 245  kleines o Tilde
        //      F6  246  ö  ANSI 246  kleines o Umlaut
        //      F7  247  ÷  ANSI 247  Divisionszeichen
        //      F8  248  ø  ANSI 248  kleines o mit diagonalem Strich
        //      F9  249  ù  ANSI 249  kleines u Grave
        //      FA  250  ú  ANSI 250  kleines u Acute
        //      FB  251  û  ANSI 251  kleines u Zirkumflex
        //      FC  252  ü  ANSI 252  kleines u Umlaut
        //      FD  253  ý  ANSI 253  kleines y Acute
        //      FE  254  þ  ANSI 254  Isländisches kleines thorn
        //      FF  255  ÿ  ANSI 255  kleines y Umlaut

        return output;
    }

    private String prepareWorldCat2(final String input) {
        // Methode um Treffer aus WorldCat bei Suchstrings mit Umlauten zu erhalten
        String output = input;

        output = output.replaceAll("–", "-"); // WorldCat akzeptiert keine ndash 
        output = output.replaceAll("", "-"); // WorldCat akzeptiert keine ndash 
        output = output.replaceAll("\\+-\\+", "\\+"); // WorldCat akzeptiert keine " - "
        output = output.replaceAll("", ""); // scheint für ¨ zu stehen...

        output = output.replaceAll("%C0", "A"); //      C0  192  À  ANSI 192  großes A Grave
        output = output.replaceAll("%C1", "A"); //      C1  193  Á  ANSI 193  großes A Acute
        output = output.replaceAll("%C2", "A"); //      C2  194  Â  ANSI 194  großes A Zirkumflex
        output = output.replaceAll("%C3", "A"); //      C3  195  Ã  ANSI 195  großes A Tilde
        output = output.replaceAll("%C4", "A"); //      C4  196  Ä  ANSI 196  großes A Umlaut
        output = output.replaceAll("%C5", "A"); //      C5  197  Å  ANSI 197  großes A mit Ring
        output = output.replaceAll("%C6", "AE"); // ?      C6  198  Æ  ANSI 198  große AE-Ligatur
        output = output.replaceAll("%C7", "C"); //      C7  199  Ç  ANSI 199  großes C mit Cedille
        output = output.replaceAll("%C8", "E"); //      C8  200  È  ANSI 200  großes E Grave
        output = output.replaceAll("%C9", "E"); //      C9  201  É  ANSI 201  großes E Acute
        output = output.replaceAll("%CA", "E"); //      CA  202  Ê  ANSI 202  großes E Zirkumflex
        output = output.replaceAll("%CB", "E"); //      CB  203  Ë  ANSI 203  großes E Trema
        output = output.replaceAll("%CC", "I"); //      CC  204  Ì  ANSI 204  großes I Grave
        output = output.replaceAll("%CD", "I"); //      CD  205  Í  ANSI 205  großes I Acute
        output = output.replaceAll("%CE", "I"); //      CE  206  Î  ANSI 206  großes I Zirkumflex
        output = output.replaceAll("%CF", "I"); //       CF  207  Ï  ANSI 207  großes I Trema
        output = output.replaceAll("%D0", "D"); // ?      D0  208  Ð  ANSI 208  Isländisches großes Eth
        output = output.replaceAll("%D1", "N"); //      D1  209  Ñ  ANSI 209  großes N Tilde
        output = output.replaceAll("%D2", "O"); //      D2  210  Ò  ANSI 210  großes O Grave
        output = output.replaceAll("%D3", "O"); //      D3  211  Ó  ANSI 211  großes O Acute
        output = output.replaceAll("%D4", "O"); //      D4  212  Ô  ANSI 212  großes O Zirkumflex
        output = output.replaceAll("%D5", "O"); //      D5  213  Õ  ANSI 213  großes O Tilde
        output = output.replaceAll("%D6", "O"); //      D6  214  Ö  ANSI 214  großes O Umlaut
        output = output.replaceAll("%D7", "×"); // ?      D7  215  ×  ANSI 215  Multiplikationszeichen
        output = output.replaceAll("%D8", "O"); //      D8  216  Ø  ANSI 216  großes O mit diagonalem Strich
        output = output.replaceAll("%D9", "U"); //      D9  217  Ù  ANSI 217  großes U Grave
        output = output.replaceAll("%DA", "U"); //      DA  218  Ú  ANSI 218  großes U Acute
        output = output.replaceAll("%DB", "U"); //      DB  219  Û  ANSI 219  großes U Zirkumflex
        output = output.replaceAll("%DC", "U"); //      DC  220  Ü  ANSI 220  großes U Umlaut
        output = output.replaceAll("%DD", "Y"); //      DD  221  Ý  ANSI 221  großes Y Acute
        output = output.replaceAll("%DE", "Þ"); // ?      DE  222  Þ  ANSI 222  Isländisches großes Thorn
        output = output.replaceAll("%DF", "ss"); //      DF  223  ß  ANSI 223  Deutsches scharfes S (sz-Ligatur)
        output = output.replaceAll("%E0", "a"); //      E0  224  à  ANSI 224  kleines a Grave
        output = output.replaceAll("%E1", "a"); //      E1  225  á  ANSI 225  kleines a Acute
        output = output.replaceAll("%E2", "a"); //      E2  226  â  ANSI 226  kleines a Zirkumflex
        output = output.replaceAll("%E3", "a"); //       E3  227  ã  ANSI 227  kleines a Tilde
        output = output.replaceAll("%E4", "a"); //      E4  228  ä  ANSI 228  kleines a Umlaut
        output = output.replaceAll("%E5", "a"); //      E5  229  å  ANSI 229  kleines a Ring
        output = output.replaceAll("%E6", "ae"); // ?      E6  230  æ  ANSI 230  kleine ae-Ligatur
        output = output.replaceAll("%E7", "c"); //      E7  231  ç  ANSI 231  kleines c Cedille
        output = output.replaceAll("%E8", "e"); //      E8  232  è  ANSI 232  kleines e Grave
        output = output.replaceAll("%E9", "e"); //      E9  233  é  ANSI 233  kleines e Acute
        output = output.replaceAll("%EA", "e"); //      EA  234  ê  ANSI 234  kleines e Zirkumflex
        output = output.replaceAll("%EB", "e"); //      EB  235  ë  ANSI 235  kleines e Trema
        output = output.replaceAll("%EC", "i"); //      EC  236  ì  ANSI 236  kleines i Grave
        output = output.replaceAll("%ED", "i"); //      ED  237  í  ANSI 237  kleines i Acute
        output = output.replaceAll("%EE", "i"); //      EE  238  î  ANSI 238  kleines i Zirkumflex
        output = output.replaceAll("%EF", "i"); //      EF  239  ï  ANSI 239  kleines i Trema
        output = output.replaceAll("%F0", "ð"); // ?      F0  240  ð  ANSI 240  Isländisches kleines eth
        output = output.replaceAll("%F1", "n"); //      F1  241  ñ  ANSI 241  kleines n Tilde
        output = output.replaceAll("%F2", "o"); //      F2  242  ò  ANSI 242  kleines o Grave
        output = output.replaceAll("%F3", "o"); //      F3  243  ó  ANSI 243  kleines o Acute
        output = output.replaceAll("%F4", "o"); //      F4  244  ô  ANSI 244  kleines o Zirkumflex
        output = output.replaceAll("%F5", "o"); //      F5  245  õ  ANSI 245  kleines o Tilde
        output = output.replaceAll("%F6", "o"); //      F6  246  ö  ANSI 246  kleines o Umlaut
        output = output.replaceAll("%F7", "÷"); // ?      F7  247  ÷  ANSI 247  Divisionszeichen
        output = output.replaceAll("%F8", "o"); //      F8  248  ø  ANSI 248  kleines o mit diagonalem Strich
        output = output.replaceAll("%F9", "u"); //      F9  249  ù  ANSI 249  kleines u Grave
        output = output.replaceAll("%FA", "u"); //      FA  250  ú  ANSI 250  kleines u Acute
        output = output.replaceAll("%FB", "u"); //      FB  251  û  ANSI 251  kleines u Zirkumflex
        output = output.replaceAll("%FC", "u"); //      FC  252  ü  ANSI 252  kleines u Umlaut
        output = output.replaceAll("%FD", "y"); //      FD  253  ý  ANSI 253  kleines y Acute
        output = output.replaceAll("%FE", "þ"); //      FE  254  þ  ANSI 254  Isländisches kleines thorn
        output = output.replaceAll("%FF", "y"); //      FF  255  ÿ  ANSI 255  kleines y Umlaut

        output = output.replaceAll("&#913;", "Alpha");
        output = output.replaceAll("&#914;", "Beta");
        output = output.replaceAll("&#915;", "Gamma");
        output = output.replaceAll("&#916;", "Delta");
        output = output.replaceAll("&#917;", "Epsilon");
        output = output.replaceAll("&#918;", "Zeta");
        output = output.replaceAll("&#919;", "Eta");
        output = output.replaceAll("&#920;", "Theta");
        output = output.replaceAll("&#921;", "Iota");
        output = output.replaceAll("&#922;", "Kappa");
        output = output.replaceAll("&#923;", "Lambda");
        output = output.replaceAll("&#924;", "Mu");
        output = output.replaceAll("&#925;", "Nu");
        output = output.replaceAll("&#926;", "Xi");
        output = output.replaceAll("&#927;", "Omicron");
        output = output.replaceAll("&#928;", "Pi");
        output = output.replaceAll("&#929;", "Rho");

        output = output.replaceAll("&#931;", "Sigma");
        output = output.replaceAll("&#932;", "Tau");
        output = output.replaceAll("&#933;", "Ypsilon");
        output = output.replaceAll("&#934;", "Phi");
        output = output.replaceAll("&#935;", "Chi");
        output = output.replaceAll("&#936;", "Psi");
        output = output.replaceAll("&#937;", "Omega");

        output = output.replaceAll("&#945;", "alpha");
        output = output.replaceAll("&#946;", "beta");
        output = output.replaceAll("&#947;", "gamma");
        output = output.replaceAll("&#948;", "delta");
        output = output.replaceAll("&#949;", "epsilon");
        output = output.replaceAll("&#950;", "zeta");
        output = output.replaceAll("&#951;", "eta");
        output = output.replaceAll("&#952;", "theta");
        output = output.replaceAll("&#953;", "iota");
        output = output.replaceAll("&#954;", "kappa");
        output = output.replaceAll("&#955;", "lambda");
        output = output.replaceAll("&#956;", "mu");
        output = output.replaceAll("&#957;", "nu");
        output = output.replaceAll("&#958;", "xi");
        output = output.replaceAll("&#959;", "omicron");
        output = output.replaceAll("&#960;", "pi");
        output = output.replaceAll("&#961;", "rho");
        output = output.replaceAll("&#962;", "sigma");
        output = output.replaceAll("&#963;", "sigma");
        output = output.replaceAll("&#964;", "tau");
        output = output.replaceAll("&#965;", "ypsilon");
        output = output.replaceAll("&#966;", "phi");
        output = output.replaceAll("&#967;", "chi");
        output = output.replaceAll("&#968;", "psi");
        output = output.replaceAll("&#969;", "omega");

        output = output.replaceAll("&#977;", "theta");
        output = output.replaceAll("&#978;", "ypsilon");

        return output;
    }

    private boolean google(final HttpServletRequest rq, final Auth auth) {
        boolean result = false;

        if (ReadSystemConfigurations.isActivatedGoogleSearch() // Google search must be globally activated
                && (!auth.isBenutzer(rq) || // and the user must be a librarian or an admin...
                // ...or the configuration for users must be set to true.
                ReadSystemConfigurations.isAllowPatronAutomaticGoogleSearch())) {
            result = true;
        }

        return result;
    }

    private String getBackThreadedWebcontent(final Future<String> webcontent, final int i, final String serviceName) {
        String result = null;

        try {
            result = webcontent.get(i, TimeUnit.SECONDS);

        } catch (final TimeoutException e) {
            log.warn(serviceName + " TimeoutException: " + e.toString());
        } catch (final Exception e) {
            LOG.error(serviceName + " Thread failed in checkAvailability: " + e.toString());
        } finally {
            // secure if task is finished already.
            webcontent.cancel(true);
        }

        return result;
    }

}
