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

package ch.dbs.actions.illformat;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.methods.PostMethod;
import org.grlea.log.SimpleLogger;

import util.Http;
import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import util.UniqueID;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Konto;
import ch.dbs.entity.OrderState;
import ch.dbs.entity.Text;
import ch.dbs.form.IllForm;
import ch.dbs.form.OrderForm;
import ch.dbs.form.UserInfo;
import enums.Connect;

public class IllHandler {

    private static final SimpleLogger LOG = new SimpleLogger(IllHandler.class);

    /**
     * Stellt einen ILL-Request zusammen
     * @param OrderForm of
     * @param Konto k
     * @param String baseurl
     *
     * @return String content
     *
     * @author Markus Fischer
     */
    public String sendIllRequest(final IllForm ill, final String baseurl) {
        String content = "";

        final Http http = new Http();

        // falls ein Wert unvermittelt null ist wird ein Leerstring verwendet, um in jedem Fall sicherzustellen,
        // dass die Methode nicht kracht

        final PostMethod method = new PostMethod(baseurl);
        if (ill.getMessage_type() != null) {
            method.addParameter("message-type", ill.getMessage_type());
        } else {
            method.addParameter("message-type", "");
        }
        if (ill.getTransaction_id() != null) {
            method.addParameter("transaction-id", ill.getTransaction_id());
        } else {
            method.addParameter("transaction-id", "");
        }
        if (ill.getTransaction_initial_req_id_symbol() != null) {
            method.addParameter("transaction-initial-req-id-symbol", ill.getTransaction_initial_req_id_symbol());
        } else {
            method.addParameter("transaction-initial-req-id-symbol", "");
        }
        if (ill.getTransaction_group_qualifier() != null) {
            method.addParameter("transaction-group-qualifier", ill.getTransaction_group_qualifier());
        } else {
            method.addParameter("transaction-group-qualifier", "");
        }
        if (ill.getTransaction_qualifier() != null) {
            method.addParameter("transaction-qualifier", ill.getTransaction_qualifier());
        } else {
            method.addParameter("transaction-qualifier", "");
        }
        if (ill.getTransaction_sub_transaction_qualifier() != null) {
            method.addParameter("transaction-sub-transaction-qualifier", ill.getTransaction_sub_transaction_qualifier());
        } else {
            method.addParameter("transaction-sub-transaction-qualifier", "");
        }
        if (ill.getService_date_time() != null) {
            method.addParameter("service-date-time", ill.getService_date_time());
        } else {
            method.addParameter("service-date-time", "");
        }
        if (ill.getRequester_id() != null) {
            method.addParameter("requester-id", ill.getRequester_id());
        } else {
            method.addParameter("requester-id", "");
        }
        if (ill.getRequester_group() != null) {
            method.addParameter("requester-group", ill.getRequester_group());
        } else {
            method.addParameter("requester-group", "");
        }
        if (ill.getResponder_id() != null) {
            method.addParameter("responder-id", ill.getResponder_id());
        } else {
            method.addParameter("responder-id", "");
        }
        if (ill.getClient_id() != null) {
            method.addParameter("client-id", ill.getClient_id());
        } else {
            method.addParameter("client-id", "");
        }
        if (ill.getClient_name() != null) {
            method.addParameter("client-name", ill.getClient_name());
        } else {
            method.addParameter("client-name", "");
        }
        if (ill.getClient_identifier() != null) {
            method.addParameter("client-identifier", ill.getClient_identifier());
        } else {
            method.addParameter("client-identifier", "");
        }
        if (ill.getDelivery_address() != null) {
            method.addParameter("delivery-address", ill.getDelivery_address());
        } else {
            method.addParameter("delivery-address", "");
        }
        if (ill.getDel_postal_name_of_person_or_institution() != null) {
            method.addParameter("del-postal-name-of-person-or-institution",
                    ill.getDel_postal_name_of_person_or_institution());
        } else {
            method.addParameter("del-postal-name-of-person-or-institution", "");
        }
        if (ill.getDel_postal_extended_postal_delivery_address() != null) {
            method.addParameter("del-postal-extended-postal-delivery-address",
                    ill.getDel_postal_extended_postal_delivery_address());
        } else {
            method.addParameter("del-postal-extended-postal-delivery-address", "");
        }
        if (ill.getDel_postal_street_and_number() != null) {
            method.addParameter("del-postal-street-and-number", ill.getDel_postal_street_and_number());
        } else {
            method.addParameter("del-postal-street-and-number", "");
        }
        if (ill.getDel_postal_post_office_box() != null) {
            method.addParameter("del-postal-post-office-box", ill.getDel_postal_post_office_box());
        } else {
            method.addParameter("del-postal-post-office-box", "");
        }
        if (ill.getDel_postal_city() != null) {
            method.addParameter("del-postal-city", ill.getDel_postal_city());
        } else {
            method.addParameter("del-postal-city", "");
        }
        if (ill.getDel_postal_country() != null) {
            method.addParameter("del-postal-country", ill.getDel_postal_country());
        } else {
            method.addParameter("del-postal-country", "");
        }
        if (ill.getDel_postal_code() != null) {
            method.addParameter("del-postal-code", ill.getDel_postal_code());
        } else {
            method.addParameter("del-postal-code", "");
        }
        if (ill.getDel_fax_address() != null) {
            method.addParameter("del-fax-address", ill.getDel_fax_address());
        } else {
            method.addParameter("del-fax-address", "");
        }
        if (ill.getDel_status_level_requester() != null) {
            method.addParameter("del-status-level-requester", ill.getDel_status_level_requester());
        } else {
            method.addParameter("del-status-level-requester", "");
        }
        if (ill.getDel_status_level_user() != null) {
            method.addParameter("del-status-level-user", ill.getDel_status_level_user());
        } else {
            method.addParameter("del-status-level-user", "");
        }
        if (ill.getDel_customer_info_to_keep() != null) {
            method.addParameter("del-customer-info-to-keep", ill.getDel_customer_info_to_keep());
        } else {
            method.addParameter("del-customer-info-to-keep", "");
        }
        if (ill.getDel_email_address() != null) {
            method.addParameter("del-email-address", ill.getDel_email_address());
        } else {
            method.addParameter("del-email-address", "");
        }
        if (ill.getDelivery_service() != null) {
            method.addParameter("delivery-service", ill.getDelivery_service());
        } else {
            method.addParameter("delivery-service", "");
        }
        if (ill.getIll_service_type() != null) {
            method.addParameter("ill-service-type", ill.getIll_service_type());
        } else {
            method.addParameter("ill-service-type", "");
        }
        if (ill.getItem_id() != null) {
            method.addParameter("item-id", ill.getItem_id());
        } else {
            method.addParameter("item-id", "");
        }
        if (ill.getItem_system_no() != null) {
            method.addParameter("item-system-no", ill.getItem_system_no());
        } else {
            method.addParameter("item-system-no", "");
        }
        if (ill.getDel_notes() != null) {
            method.addParameter("del-notes", ill.getDel_notes());
        } else {
            method.addParameter("del-notes", "");
        }
        if (ill.getMaximum_cost() != null) {
            method.addParameter("maximum-cost", ill.getMaximum_cost());
        } else {
            method.addParameter("maximum-cost", "");
        }
        if (ill.getSearch_type() != null) {
            method.addParameter("search-type", ill.getSearch_type());
        } else {
            method.addParameter("search-type", "");
        }
        if (ill.getSearch_type_level_of_service() != null) {
            method.addParameter("search-type-level-of-service", ill.getSearch_type_level_of_service());
        } else {
            method.addParameter("search-type-level-of-service", "");
        }
        if (ill.getSearch_type_expiry_date() != null) {
            method.addParameter("search-type-expiry-date", ill.getSearch_type_expiry_date());
        } else {
            method.addParameter("search-type-expiry-date", "");
        }
        if (ill.getItem_author() != null) {
            method.addParameter("item-author", ill.getItem_author());
        } else {
            method.addParameter("item-author", "");
        }
        if (ill.getItem_title() != null) {
            method.addParameter("item-title", ill.getItem_title());
        } else {
            method.addParameter("item-title", "");
        }
        if (ill.getItem_sub_title() != null) {
            method.addParameter("item-sub-title", ill.getItem_sub_title());
        } else {
            method.addParameter("item-sub-title", "");
        }
        if (ill.getItem_place_of_publication() != null) {
            method.addParameter("item-place-of-publication", ill.getItem_place_of_publication());
        } else {
            method.addParameter("item-place-of-publication", "");
        }
        if (ill.getItem_publisher() != null) {
            method.addParameter("item-publisher", ill.getItem_publisher());
        } else {
            method.addParameter("item-publisher", "");
        }
        if (ill.getItem_publication_date_of_component() != null) {
            method.addParameter("item-publication-date-of-component", ill.getItem_publication_date_of_component());
        } else {
            method.addParameter("item-publication-date-of-component", "");
        }
        if (ill.getItem_author_of_article() != null) {
            method.addParameter("item-author-of-article", ill.getItem_author_of_article());
        } else {
            method.addParameter("item-author-of-article", "");
        }
        if (ill.getItem_title_of_article() != null) {
            method.addParameter("item-title-of-article", ill.getItem_title_of_article());
        } else {
            method.addParameter("item-title-of-article", "");
        }
        if (ill.getItem_volume_issue() != null) {
            method.addParameter("item-volume-issue", ill.getItem_volume_issue());
        } else {
            method.addParameter("item-volume-issue", "");
        }
        if (ill.getItem_pagination() != null) {
            method.addParameter("item-pagination", ill.getItem_pagination());
        } else {
            method.addParameter("item-pagination", "");
        }
        if (ill.getItem_iSSN() != null) {
            method.addParameter("item-iSSN", ill.getItem_iSSN());
        } else {
            method.addParameter("item-iSSN", "");
        }
        if (ill.getItem_iSBN() != null) {
            method.addParameter("item-iSBN", ill.getItem_iSBN());
        } else {
            method.addParameter("item-iSBN", "");
        }

        //... bei Subito gibt es noch tonnenweise zusätzlicher Parameter
        // sollte aber kombinierbar sein. GBV wertet einfach nicht alle Parameter aus

        content = http.getWebcontent(method, Connect.TIMEOUT_8.getValue(), Connect.RETRYS_1.getValue()); // nur einmal abschicken!!!

        return content;

    }

    /**
     * Stellt die GBV-spezifischen ILL-Parameter zusammen
     * @param OrderForm of
     * @param Konto k
     *
     * @return IllForm gbv
     *
     */
    public IllForm prepareGbvIllRequest(final OrderForm of, final Konto k, final UserInfo ui) {

        final IllForm gbv = new IllForm();

        try {

            final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyyMMddHHmmss");
            final Calendar calendar = new GregorianCalendar();
            Date d = calendar.getTime();
            final String datum = fmt.format(d, k.getTimezone());
            //        System.out.println("Bestelldatum: " + datum);

            final ThreadSafeSimpleDateFormat ft = new ThreadSafeSimpleDateFormat("dd.MM.yyyy");
            calendar.add(Calendar.MONTH, +1);
            d = calendar.getTime();
            final String dateTo = ft.format(d, k.getTimezone());
            //        System.out.println("Expiry: " + date_to);

            String deloptions = "POST"; // default
            if (of.getDeloptions().equals("fax to pdf") && ui.getKonto().getFaxno() != null) {
                deloptions = "FAX";
            }
            if (of.getDeloptions().equals("fax") && ui.getKonto().getFax_extern() != null) {
                deloptions = "FAX";
            }

            String fax = ""; // default
            if (ui.getKonto().getFaxno() != null) {
                fax = correctFaxnumber(ui.getKonto().getFaxno());
            } else {
                if (ui.getKonto().getFax_extern() != null) {
                    fax = correctFaxnumber(ui.getKonto().getFax_extern());
                }
            }

            String serviceType = "COPY";
            if (of.getMediatype().equals("Buch")) {
                serviceType = "LOAN";
            }

            String isiltext = "";
            if (k.getIsil() != null) {
                isiltext = " | Sigel: " + k.getIsil();
            } // kann null sein

            // GBV-spezifische Angaben
            gbv.setClient_identifier(k.getGbvbenutzername()); // kann auch leer sein. momentan deaktiviert
            gbv.setItem_system_no(of.getPpn()); // beim GBV PPN
            gbv.setRequester_group("USER-GROUP-0"); //  Kundengruppe
            gbv.setRequester_id(k.getGbvrequesterid()); // ID für die jeweilige Bibliothek
            gbv.setResponder_id("8101"); // Das Feld wird im GBV (8101) nicht ausgewertet, in ihm kann auch GBV stehen.
            gbv.setTransaction_initial_req_id_symbol(isilToSigel(k.getIsil())); // beim GBV Sigel

            // allgemeine Parameter
            gbv.setMessage_type("REQUEST");
            gbv.setTransaction_id(""); // Das Feld transaction-id ist immer leer.
            // Bestellnummer des auslösenden Systems. Eindeutige Identifikation einer Bestellung.
            gbv.setTransaction_group_qualifier(String.valueOf(UniqueID.get()));
            // zweite Bestellnummer des auslösenden Systems. Kann leer sein.
            gbv.setTransaction_qualifier(of.getInterne_bestellnr());
            gbv.setTransaction_sub_transaction_qualifier("");
            gbv.setService_date_time(datum);
            gbv.setClient_id(""); // Das Feld ist immer leer.
            gbv.setClient_name(ReadSystemConfigurations.getServerWelcomepage()); // Adresse der Installation / Instanz
            gbv.setDelivery_address(""); // Das Feld delivery-address ist immer leer.
            // Lieferadresse und vorläufig Sigelangabe
            gbv.setDel_postal_name_of_person_or_institution(k.getBibliotheksname() + isiltext);
            gbv.setDel_postal_extended_postal_delivery_address(k.getAdressenzusatz()); // Ergänzung zur Lieferadresse
            gbv.setDel_postal_street_and_number(k.getAdresse());
            gbv.setDel_postal_post_office_box("");
            gbv.setDel_postal_city(k.getOrt());
            gbv.setDel_postal_country("DE");
            gbv.setDel_postal_code(k.getPLZ());
            gbv.setDel_fax_address(fax);
            gbv.setDel_status_level_requester("NONE");
            gbv.setDel_status_level_user("NEGATIVE");
            // Überschreiben der gespeicherten Angaben mit den vorliegenden Kundenangaben
            gbv.setDel_customer_info_to_keep("N");
            gbv.setDel_email_address(k.getDbsmail());
            gbv.setDelivery_service(deloptions);
            gbv.setIll_service_type(serviceType); // COPY oder Loan
            gbv.setItem_id(""); // Feld ist immer leer
            gbv.setDel_notes(of.getAnmerkungen() + " | Mehrkosten bis max.: " + of.getMaximum_cost() + " EUR"
                    + " | Priority: " + of.getPrio());
            // noch nicht aktiv im GBV. Momentan wird del-notes dazu verwendet
            gbv.setMaximum_cost(of.getMaximum_cost());
            gbv.setSearch_type(""); // Das Feld ist immer leer.
            gbv.setSearch_type_level_of_service(of.getPrio().toUpperCase()); // NORMAL, URGENT
            gbv.setSearch_type_expiry_date(dateTo);
            gbv.setItem_author(""); // in DD nie erfasst
            gbv.setItem_title(of.getBuchtitel() + of.getZeitschriftentitel());
            gbv.setItem_sub_title("");
            gbv.setItem_place_of_publication("");
            gbv.setItem_publisher(of.getVerlag());
            gbv.setItem_publication_date_of_component(of.getJahr());
            gbv.setItem_author_of_article(of.getAuthor());
            gbv.setItem_title_of_article(of.getKapitel() + of.getArtikeltitel());
            gbv.setItem_volume_issue(of.getJahrgang() + "[" + of.getHeft() + "]");
            gbv.setItem_pagination(of.getSeiten());
            gbv.setItem_iSSN(of.getIssn());
            gbv.setItem_iSBN(of.getIsbn());

        } catch (final Exception e) {
            LOG.error("IllHandler - prepareGbvIllRequest: " + e.toString());
        }

        return gbv;

    }

    /**
     * Liest die GBV-Bestellnummer aus der Antwort auf einen sendIllRequest aus, oder gibt ggf. die Fehlermeldung zurück
     * @param String content
     *
     * @return String gbvanswer
     *
     */
    public String readGbvIllAnswer(final String content) {
        String gbvanswer = "Fehlende GBV-Antwort!"; // darf nicht null sein, da sonst jsp Anzeige nicht funzt!

        if (content == null) {
            return gbvanswer;
        }

        try {

            if (content.contains("<status>OK</status>") && content.contains("<return_value>")) {
                // Bestellung hat geklappt
                gbvanswer = content.substring(content.indexOf("<return_value>") + 14,
                        content.indexOf("</return_value>", content.indexOf("<return_value>")));
            } else {
                if (content.contains("<status>ERROR</status>") && content.contains("<return_comment>")) {
                    // // Bestellung ist fehlgeschlagen
                    gbvanswer = content.substring(content.indexOf("<return_comment>") + 16,
                            content.indexOf("</return_comment>", content.indexOf("<return_comment>")));
                }
            }

        } catch (final Exception e) {
            LOG.error("IllHandler - readGbvIllAnswer: " + "\012" + content);
            // darf, nicht null sein! Hier ist grundsätzlich etwas fehlgeschlagen
            gbvanswer = "Fehler im Bestellablauf!";
        }

        return gbvanswer;
    }

    /**
     * Liest einen IllRequest. (Status-Antwort eines gebenden Systems auf eine Bestellung. z.B. shipped...)
     * @param String content
     *
     * @return String gbvanswer
     *
     */
    protected IllForm readIllRequest(final HttpServletRequest rq) {
        final IllForm ill = new IllForm();

        try {

            ill.setTransaction_initial_req_id_symbol(rq.getParameter("transaction-initial-req-id-symbol"));

            ill.setClient_identifier(rq.getParameter("client-identifier")); // kann auch leer sein
            ill.setItem_system_no(rq.getParameter("item-system-no")); // beim GBV PPN
            ill.setRequester_group(rq.getParameter("requester-group")); //  Kundengruppe
            ill.setRequester_id(rq.getParameter("requester-id")); // ID der nehmenden Bibliothek
            // Das Feld wird im GBV (8101) nicht ausgewertet, in ihm kann auch GBV stehen.
            ill.setResponder_id(rq.getParameter("responder-id"));

            // allgemeine Parameter
            ill.setMessage_type(rq.getParameter("message-type"));
            ill.setTransaction_id(rq.getParameter("transaction-id")); // Das Feld transaction-id ist immer leer.
            // Bestellnummer des auslösenden Systems. Eindeutige Identifikation einer Bestellung.
            ill.setTransaction_group_qualifier(rq.getParameter("transaction-group-qualifier"));
            // zweite Bestellnummer des auslösenden Systems. Kann leer sein.
            ill.setTransaction_qualifier(rq.getParameter("transaction-qualifier"));
            ill.setTransaction_sub_transaction_qualifier(rq.getParameter("transaction-sub-transaction-qualifier"));
            ill.setService_date_time(rq.getParameter("service-date-time"));
            ill.setClient_id(rq.getParameter("client-id")); // Das Feld ist immer leer.
            ill.setClient_name(rq.getParameter("client-name"));
            ill.setDelivery_address(rq.getParameter("delivery-address")); // Das Feld delivery-address ist immer leer.
            // Lieferadresse und vorläufig Sigelangabe
            ill.setDel_postal_name_of_person_or_institution(rq.getParameter("del-postal-name-of-person-or-institution"));
            // Ergänzung zur Lieferadresse
            ill.setDel_postal_extended_postal_delivery_address(rq
                    .getParameter("del-postal-extended-postal-delivery-address"));
            ill.setDel_postal_street_and_number(rq.getParameter("del-postal-street-and-number"));
            ill.setDel_postal_post_office_box(rq.getParameter("del-postal-post-office-box"));
            ill.setDel_postal_city(rq.getParameter("del-postal-city"));
            ill.setDel_postal_country(rq.getParameter("del-postal-country"));
            ill.setDel_postal_code(rq.getParameter("del-postal-code"));
            ill.setDel_fax_address(rq.getParameter("del-fax-address"));
            ill.setDel_status_level_requester(rq.getParameter("del-status-level-requester"));
            ill.setDel_status_level_user(rq.getParameter("del-status-level-user"));
            // Überschreiben der gespeicherten Angaben mit den vorliegenden Kundenangaben
            ill.setDel_customer_info_to_keep(rq.getParameter("del-customer-info-to-keep"));
            ill.setDel_email_address(rq.getParameter("del-email-address"));
            ill.setDelivery_service(rq.getParameter("delivery-service"));
            ill.setIll_service_type(rq.getParameter("ill-service-type")); // COPY oder Loan
            ill.setItem_id(rq.getParameter("item-id")); // Feld ist immer leer
            ill.setDel_notes(rq.getParameter("del-notes"));
            // noch nicht aktiv im GBV. Momentan wird del-notes dazu verwendet
            ill.setMaximum_cost(rq.getParameter("maximum-cost"));
            ill.setSearch_type(rq.getParameter("search-type")); // Das Feld ist immer leer.
            ill.setSearch_type_level_of_service(rq.getParameter("search-type-level-of-service")); // NORMAL, URGENT
            ill.setSearch_type_expiry_date(rq.getParameter("search-type-expiry-date"));
            ill.setItem_author(rq.getParameter("item-author")); // in DD nie erfasst
            ill.setItem_title(rq.getParameter("item-title"));
            ill.setItem_sub_title(rq.getParameter("item-sub-title"));
            ill.setItem_place_of_publication(rq.getParameter("item-place-of-publication"));
            ill.setItem_publisher(rq.getParameter("item-publisher"));
            ill.setItem_publication_date_of_component(rq.getParameter("item-publication-date-of-component"));
            ill.setItem_author_of_article(rq.getParameter("item-author-of-article"));
            ill.setItem_title_of_article(rq.getParameter("item-title-of-article"));
            ill.setItem_volume_issue(rq.getParameter("item-volume-issue"));
            ill.setItem_pagination(rq.getParameter("item-pagination"));
            ill.setItem_iSSN(rq.getParameter("item-issn"));
            ill.setItem_iSBN(rq.getParameter("item-isbn"));
            ill.setResponder_note(rq.getParameter("responder-note"));

        } catch (final Exception e) {
            LOG.error("IllHandler - readIllRequest(rq): " + e.toString());
        }

        return ill;
    }

    /**
     * Fährt den Status eine offenen Bestellung nach
     * @param IllForm ill
     *
     * @return String returnvalue
     *
     */
    protected String updateOrderState(final IllForm ill, final Connection cn) {

        String returnvalue = "ERROR";
        final OrderState orderstate = new OrderState();

        try {

            if (ill.getMessage_type().equals("REQUEST")) {
                returnvalue = ReadSystemConfigurations.getApplicationName() + " nimmt keine Bestellungen entgegen!";
            }
            if (ill.getMessage_type().equals("SHIPPED")) {
                returnvalue = "OK"; // wird immer mit OK quittiert, unabhängig, ob unten eine Bestellung gefunden wird
                // hier wird versucht die Bestellung anhand der Trackinnr und der Gbvnr zu holens
                final Bestellungen b = new Bestellungen(cn, ill.getTransaction_group_qualifier(),
                        ill.getTransaction_sub_transaction_qualifier());
                if (b.getId() != null) {

                    String date = ill.getService_date_time();
                    // ill enthält gültiges Datum (yyyyMMddHHmmss)
                    if (date.length() == 14 && date.substring(0, 2).equals("20")) {
                        // 20090120074238
                        date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + "\040"
                                + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
                        //               System.out.println("Status-Datum: " + date);
                    } else { // ill enthält kein gültiges Datum
                        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final Calendar calendar = new GregorianCalendar();
                        final Date d = calendar.getTime();
                        date = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());
                        //                System.out.println("Status-Datum: " + date);
                    }

                    b.setStatustext("geliefert");
                    b.setStatusdate(date);
                    String statustext = null; // darf tatsächlich null sein
                    if (ill.getResponder_note() != null && !ill.getResponder_note().equals("")) {
                        statustext = ill.getResponder_note();
                    }
                    orderstate.changeOrderState(b, ReadSystemConfigurations.getSystemTimezone(), new Text(cn,
                            "geliefert"), statustext, "automatisch", cn);
                }
            }
            if (ill.getMessage_type().equals("ANSWER")) {
                returnvalue = "OK";
                // hier wird versucht die Bestellung anhand der Trackinnr und der Gbvnr zu holens
                final Bestellungen b = new Bestellungen(cn, ill.getTransaction_group_qualifier(),
                        ill.getTransaction_sub_transaction_qualifier());
                if (b.getId() != null) {

                    String date = ill.getService_date_time();
                    // ill enthält gültiges Datum (yyyyMMddHHmmss)
                    if (date.length() == 14 && date.substring(0, 2).equals("20")) {
                        // 20090120074238
                        date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + "\040"
                                + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
                        //               System.out.println("Status-Datum: " + date);
                    } else { // ill enthält kein gültiges Datum
                        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final Calendar calendar = new GregorianCalendar();
                        final Date d = calendar.getTime();
                        date = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());
                        //                System.out.println("Status-Datum: " + date);
                    }

                    b.setStatustext("nicht lieferbar");
                    b.setStatusdate(date);
                    String statustext = null; // darf tatsächlich null sein
                    if (ill.getResponder_note() != null && !ill.getResponder_note().equals("")) {
                        statustext = ill.getResponder_note();
                    }
                    orderstate.changeOrderState(b, ReadSystemConfigurations.getSystemTimezone(), new Text(cn,
                            "nicht lieferbar"), statustext, "automatisch", cn);
                }

            }
            if (ill.getMessage_type().equals("BILL")) {
                returnvalue = "OK";

            }

        } catch (final Exception e) {
            LOG.error("IllHandler - updateOrderState:" + e.toString());
        }

        return returnvalue;
    }

    /**
     * Konvertiert ein deutsches ISIL in ein Sigel. Momentan beim GBV so benötigt
     * @param String isil
     *
     * @return String sigel
     *
     */
    private String isilToSigel(final String isil) {
        String sigel = isil;

        try {

            if (sigel != null && sigel.startsWith("DE-")) {
                sigel = sigel.substring(3); // entfernt deutschen Länderpräfix für GBV
            }

        } catch (final Exception e) {
            LOG.error("IllHandler - isilToSigel: " + isil + "\040" + e.toString());
        }

        return sigel;
    }

    /**
     * bringt eine Faxnummer beliebigen Formats in das korrekte ILL-Format
     * @param String faxnumber
     *
     * @return String fax
     *
     */
    private String correctFaxnumber(final String faxnumber) {
        String fax = faxnumber;

        try {

            if (fax != null && fax.charAt(0) == '+') {
                fax = "00" + fax.substring(1);
            }

        } catch (final Exception e) {
            LOG.error("correctFaxnumber: " + faxnumber + "\040" + e.toString());
        }

        return fax;
    }

}
