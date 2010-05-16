<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<?xml version="1.0" standalone="yes"?>

<VFL_RETURN>
	<message_type><bean:write name="illform" property="message_type" /></message_type>
	<status><bean:write name="illform" property="status" /></status>
	<return_value></return_value>
	<return_comment><bean:write name="illform" property="comment" /></return_comment>
	<nr_of_trial>1</nr_of_trial>
<sender>
	<sender_id><bean:message bundle="systemConfig" key="myServer.gbvRequesterID"/></sender_id>
</sender>
<receiver>
	<receiver_url>http://www.doctor-doc.com/version1.0/illform.do</receiver_url>
	<repeat_data_to>http://www.doctor-doc.com/version1.0/illform.do</repeat_data_to>
<send_fatal_to>
	<url></url>
	<email><bean:message bundle="systemConfig" key="systemEmail.email"/></email>
</send_fatal_to>
</receiver>
<data>
message-type=<bean:write name="illform" property="message_type" />
&transaction-id=
&transaction-initial-req-id-symbol=<bean:write name="illform" property="transaction_initial_req_id_symbol" />
&transaction-group-qualifier=<bean:write name="illform" property="transaction_group_qualifier" />
&transaction-qualifier=<bean:write name="illform" property="transaction_qualifier" />
&transaction-sub-transaction-qualifier=<bean:write name="illform" property="transaction_sub_transaction_qualifier" />
&service-date-time=<bean:write name="illform" property="service_date_time" />
&requester-id=<bean:message bundle="systemConfig" key="myServer.gbvRequesterID"/>
&requester-group=<bean:write name="illform" property="requester_group" />
&client-id=
&client-identifier=<bean:write name="illform" property="client_identifier" />
&responder-id=<bean:write name="illform" property="responder_id" />
&client-name=<bean:write name="illform" property="client_name" />
&delivery-address=<bean:write name="illform" property="delivery_address" />
&del-postal-name-of-person-or-institution=<bean:write name="illform" property="del_postal_name_of_person_or_institution" />
&del-postal-extended-postal-delivery-address=<bean:write name="illform" property="del_postal_extended_postal_delivery_address" />
&del-postal-street-and-number=<bean:write name="illform" property="del_postal_street_and_number" />
&del-postal-post-office-box=<bean:write name="illform" property="del_postal_post_office_box" />
&del-postal-city=<bean:write name="illform" property="del_postal_city" />
&del-postal-country=<bean:write name="illform" property="del_postal_country" />
&del-postal-code=<bean:write name="illform" property="del_postal_code" />
&del-fax-address=<bean:write name="illform" property="del_fax_address" />
&del-status-level-requester=<bean:write name="illform" property="del_status_level_requester" />
&del-status-level-user=<bean:write name="illform" property="del_status_level_user" />
&del-customer-info-to-keep=<bean:write name="illform" property="del_customer_info_to_keep" />
&del-email-address=<bean:write name="illform" property="del_email_address" />
&delivery-service=<bean:write name="illform" property="delivery_service" />
&ill-service-type=<bean:write name="illform" property="ill_service_type" />
&item-id=
&del-notes=<bean:write name="illform" property="del_notes" />
&maximum-cost=<bean:write name="illform" property="maximum_cost" />
&search-type=<bean:write name="illform" property="search_type" />
&search-type-level-of-service=<bean:write name="illform" property="search_type_level_of_service" />
&search-type-expiry-date=<bean:write name="illform" property="search_type_expiry_date" />
&item-author=<bean:write name="illform" property="item_author" />
&item-title=<bean:write name="illform" property="item_title" />
&item-sub-title=<bean:write name="illform" property="item_sub_title" />
&item-place-of-publication=<bean:write name="illform" property="item_place_of_publication" />
&item-publisher=<bean:write name="illform" property="item_publisher" />
&item-publication-date-of-component=<bean:write name="illform" property="item_publication_date_of_component" />
&item-author-of-article=<bean:write name="illform" property="item_author_of_article" />
&item-title-of-article=<bean:write name="illform" property="item_title_of_article" />
&item-volume-issue=<bean:write name="illform" property="item_volume_issue" />
&item-pagination=<bean:write name="illform" property="item_pagination" />
&item-issn=<bean:write name="illform" property="item_iSSN" />
&item_isbn=<bean:write name="illform" property="item_iSBN" />
</VFL_RETURN>