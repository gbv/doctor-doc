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

package ch.dbs.form;

import org.apache.struts.action.ActionForm;


/**
 * @author Markus Fischer
 */
public class IllForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    // Lieferantenspezifische ILL-Parameter
    private String requester_id = ""; // ID beim GBV mit der sich diese Instanz authentifiziert
    private String responder_id = ""; // Das Feld wird im GBV (8101) nicht ausgewertet, in ihm kann auch GBV stehen.
    private String transaction_initial_req_id_symbol = ""; // Beim GBV Sigel der nehmenden Bibliothek
    private String requester_group = ""; // Kundengruppe, beim GBV USER-GROUP-0
    private String client_identifier = ""; // vermutlich Gbvbenutzername()...
    private String item_system_no = ""; // beim GBV PPN

    // allgemeine ILL-Parameter
    private String message_type = ""; // REQUEST, SHIPPED, ANSWER, BILL
    private String transaction_id = ""; // Das Feld transaction-id ist immer leer.
    // Bestellnummer des auslösenden Systems. Eindeutige Identifikation einer Bestellung.
    private String transaction_group_qualifier = "";
    private String transaction_qualifier = ""; // zweite Bestellnummer des auslösenden Systems. Kann leer sein.
    private String transaction_sub_transaction_qualifier = ""; // in der Antwort die Bestellnummer des gebenden Systems
    private String service_date_time = "";
    private String client_id = ""; // Das Feld ist immer leer.
    private String client_name = "";
    private String delivery_address = ""; // Das Feld delivery-address ist immer leer.
    private String del_postal_name_of_person_or_institution = ""; // Lieferadresse
    private String del_postal_extended_postal_delivery_address = ""; // Ergänzung zur Lieferadresse
    private String del_postal_street_and_number = "";
    private String del_postal_post_office_box = "";
    private String del_postal_city = "";
    private String del_postal_country = "";
    private String del_postal_code = "";
    private String del_fax_address = "";
    private String del_status_level_requester = "";
    private String del_status_level_user = "";
    // Überschreiben der gespeicherten Angaben mit den vorliegenden Kundenangaben
    private String del_customer_info_to_keep = "";
    private String del_email_address = "";
    private String delivery_service = "";
    private String ill_service_type = ""; // COPY oder Loan
    private String item_id = ""; // Feld ist immer leer
    private String del_notes = "";
    private String maximum_cost = ""; // ev. auf jsp wählbar machen? Enthält eine Zahl
    private String search_type = ""; // Das Feld ist immer leer.
    private String search_type_level_of_service = ""; // NORMAL, URGENT
    private String search_type_expiry_date = "";
    private String item_author = "";
    private String item_title = "";
    private String item_sub_title = "";
    private String item_place_of_publication = "";
    private String item_publisher = "";
    private String item_publication_date_of_component = "";
    private String item_author_of_article = "";
    private String item_title_of_article = "";
    private String item_volume_issue = "";
    private String item_pagination = "";
    private String item_iSSN = "";
    private String item_iSBN = "";
    private String responder_note = "";

    // zusätzliche Eigenschaften um mit XML im Zusammenhang mit ILL umzugehen
    private String return_value;
    private String comment;
    private String status;


    public String getRequester_id() {
        return requester_id;
    }
    public void setRequester_id(final String requester_id) {
        this.requester_id = requester_id;
    }
    public String getResponder_id() {
        return responder_id;
    }
    public void setResponder_id(final String responder_id) {
        this.responder_id = responder_id;
    }
    public String getTransaction_initial_req_id_symbol() {
        return transaction_initial_req_id_symbol;
    }
    public void setTransaction_initial_req_id_symbol(
            final String transaction_initial_req_id_symbol) {
        this.transaction_initial_req_id_symbol = transaction_initial_req_id_symbol;
    }
    public String getRequester_group() {
        return requester_group;
    }
    public void setRequester_group(final String requester_group) {
        this.requester_group = requester_group;
    }
    public String getClient_identifier() {
        return client_identifier;
    }
    public void setClient_identifier(final String client_identifier) {
        this.client_identifier = client_identifier;
    }
    public String getItem_system_no() {
        return item_system_no;
    }
    public void setItem_system_no(final String item_system_no) {
        this.item_system_no = item_system_no;
    }
    public String getMessage_type() {
        return message_type;
    }
    public void setMessage_type(final String message_type) {
        this.message_type = message_type;
    }
    public String getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(final String transaction_id) {
        this.transaction_id = transaction_id;
    }
    public String getTransaction_group_qualifier() {
        return transaction_group_qualifier;
    }
    public void setTransaction_group_qualifier(final String transaction_group_qualifier) {
        this.transaction_group_qualifier = transaction_group_qualifier;
    }
    public String getTransaction_qualifier() {
        return transaction_qualifier;
    }
    public void setTransaction_qualifier(final String transaction_qualifier) {
        this.transaction_qualifier = transaction_qualifier;
    }
    public String getTransaction_sub_transaction_qualifier() {
        return transaction_sub_transaction_qualifier;
    }
    public void setTransaction_sub_transaction_qualifier(
            final String transaction_sub_transaction_qualifier) {
        this.transaction_sub_transaction_qualifier = transaction_sub_transaction_qualifier;
    }
    public String getService_date_time() {
        return service_date_time;
    }
    public void setService_date_time(final String service_date_time) {
        this.service_date_time = service_date_time;
    }
    public String getClient_id() {
        return client_id;
    }
    public void setClient_id(final String client_id) {
        this.client_id = client_id;
    }
    public String getClient_name() {
        return client_name;
    }
    public void setClient_name(final String client_name) {
        this.client_name = client_name;
    }
    public String getDelivery_address() {
        return delivery_address;
    }
    public void setDelivery_address(final String delivery_address) {
        this.delivery_address = delivery_address;
    }
    public String getDel_postal_name_of_person_or_institution() {
        return del_postal_name_of_person_or_institution;
    }
    public void setDel_postal_name_of_person_or_institution(
            final String del_postal_name_of_person_or_institution) {
        this.del_postal_name_of_person_or_institution = del_postal_name_of_person_or_institution;
    }
    public String getDel_postal_extended_postal_delivery_address() {
        return del_postal_extended_postal_delivery_address;
    }
    public void setDel_postal_extended_postal_delivery_address(
            final String del_postal_extended_postal_delivery_address) {
        this.del_postal_extended_postal_delivery_address = del_postal_extended_postal_delivery_address;
    }
    public String getDel_postal_street_and_number() {
        return del_postal_street_and_number;
    }
    public void setDel_postal_street_and_number(final String del_postal_street_and_number) {
        this.del_postal_street_and_number = del_postal_street_and_number;
    }
    public String getDel_postal_post_office_box() {
        return del_postal_post_office_box;
    }
    public void setDel_postal_post_office_box(final String del_postal_post_office_box) {
        this.del_postal_post_office_box = del_postal_post_office_box;
    }
    public String getDel_postal_city() {
        return del_postal_city;
    }
    public void setDel_postal_city(final String del_postal_city) {
        this.del_postal_city = del_postal_city;
    }
    public String getDel_postal_country() {
        return del_postal_country;
    }
    public void setDel_postal_country(final String del_postal_country) {
        this.del_postal_country = del_postal_country;
    }
    public String getDel_postal_code() {
        return del_postal_code;
    }
    public void setDel_postal_code(final String del_postal_code) {
        this.del_postal_code = del_postal_code;
    }
    public String getDel_fax_address() {
        return del_fax_address;
    }
    public void setDel_fax_address(final String del_fax_address) {
        this.del_fax_address = del_fax_address;
    }
    public String getDel_status_level_requester() {
        return del_status_level_requester;
    }
    public void setDel_status_level_requester(final String del_status_level_requester) {
        this.del_status_level_requester = del_status_level_requester;
    }
    public String getDel_status_level_user() {
        return del_status_level_user;
    }
    public void setDel_status_level_user(final String del_status_level_user) {
        this.del_status_level_user = del_status_level_user;
    }
    public String getDel_customer_info_to_keep() {
        return del_customer_info_to_keep;
    }
    public void setDel_customer_info_to_keep(final String del_customer_info_to_keep) {
        this.del_customer_info_to_keep = del_customer_info_to_keep;
    }
    public String getDel_email_address() {
        return del_email_address;
    }
    public void setDel_email_address(final String del_email_address) {
        this.del_email_address = del_email_address;
    }
    public String getDelivery_service() {
        return delivery_service;
    }
    public void setDelivery_service(final String delivery_service) {
        this.delivery_service = delivery_service;
    }
    public String getIll_service_type() {
        return ill_service_type;
    }
    public void setIll_service_type(final String ill_service_type) {
        this.ill_service_type = ill_service_type;
    }
    public String getItem_id() {
        return item_id;
    }
    public void setItem_id(final String item_id) {
        this.item_id = item_id;
    }
    public String getDel_notes() {
        return del_notes;
    }
    public void setDel_notes(final String del_notes) {
        this.del_notes = del_notes;
    }
    public String getMaximum_cost() {
        return maximum_cost;
    }
    public void setMaximum_cost(final String maximum_cost) {
        this.maximum_cost = maximum_cost;
    }
    public String getSearch_type() {
        return search_type;
    }
    public void setSearch_type(final String search_type) {
        this.search_type = search_type;
    }
    public String getSearch_type_level_of_service() {
        return search_type_level_of_service;
    }
    public void setSearch_type_level_of_service(final String search_type_level_of_service) {
        this.search_type_level_of_service = search_type_level_of_service;
    }
    public String getSearch_type_expiry_date() {
        return search_type_expiry_date;
    }
    public void setSearch_type_expiry_date(final String search_type_expiry_date) {
        this.search_type_expiry_date = search_type_expiry_date;
    }
    public String getItem_author() {
        return item_author;
    }
    public void setItem_author(final String item_author) {
        this.item_author = item_author;
    }
    public String getItem_title() {
        return item_title;
    }
    public void setItem_title(final String item_title) {
        this.item_title = item_title;
    }
    public String getItem_sub_title() {
        return item_sub_title;
    }
    public void setItem_sub_title(final String item_sub_title) {
        this.item_sub_title = item_sub_title;
    }
    public String getItem_place_of_publication() {
        return item_place_of_publication;
    }
    public void setItem_place_of_publication(final String item_place_of_publication) {
        this.item_place_of_publication = item_place_of_publication;
    }
    public String getItem_publisher() {
        return item_publisher;
    }
    public void setItem_publisher(final String item_publisher) {
        this.item_publisher = item_publisher;
    }
    public String getItem_publication_date_of_component() {
        return item_publication_date_of_component;
    }
    public void setItem_publication_date_of_component(
            final String item_publication_date_of_component) {
        this.item_publication_date_of_component = item_publication_date_of_component;
    }
    public String getItem_author_of_article() {
        return item_author_of_article;
    }
    public void setItem_author_of_article(final String item_author_of_article) {
        this.item_author_of_article = item_author_of_article;
    }
    public String getItem_title_of_article() {
        return item_title_of_article;
    }
    public void setItem_title_of_article(final String item_title_of_article) {
        this.item_title_of_article = item_title_of_article;
    }
    public String getItem_volume_issue() {
        return item_volume_issue;
    }
    public void setItem_volume_issue(final String item_volume_issue) {
        this.item_volume_issue = item_volume_issue;
    }
    public String getItem_pagination() {
        return item_pagination;
    }
    public void setItem_pagination(final String item_pagination) {
        this.item_pagination = item_pagination;
    }
    public String getItem_iSSN() {
        return item_iSSN;
    }
    public void setItem_iSSN(final String item_iSSN) {
        this.item_iSSN = item_iSSN;
    }
    public String getItem_iSBN() {
        return item_iSBN;
    }
    public void setItem_iSBN(final String item_iSBN) {
        this.item_iSBN = item_iSBN;
    }
    public String getReturn_value() {
        return return_value;
    }
    public void setReturn_value(final String return_value) {
        this.return_value = return_value;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(final String comment) {
        this.comment = comment;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(final String status) {
        this.status = status;
    }
    public String getResponder_note() {
        return responder_note;
    }
    public void setResponder_note(final String responder_note) {
        this.responder_note = responder_note;
    }

}
