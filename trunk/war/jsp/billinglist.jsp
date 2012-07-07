<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Einstellungen</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content"><br>

<h3>Rechnungen des Kontos <bean:write name="billingform"
  property="konto.bibliotheksname" /></h3>


<table class="border">
  <tr>
    <th id="th-left">Rechnungsgrund</th>
    <th id="th-left">Betrag</th>
    <th id="th-left">W&auml;hrung</th>
    <th id="th-left">Rechnungsnummer</th>
    <th id="th-left">Rechnungsdatum</th>
    <th id="th-left">Zahlungseingang</th>
  </tr>
  <logic:iterate id="b" name="billingform" property="billings">
    <tr>
      <td id="border"><bean:write name="b"
        property="rechnungsgrund.inhalt" /></td>
      <td id="border"><bean:write name="b" property="betrag" /></td>
      <td id="border"><bean:write name="b" property="waehrung" /></td>

      <td id="border"><bean:write name="b" property="rechnungsnummer" /></td>
      <td id="border"><bean:write name="b" property="rechnungsdatum" /></td>
      <td id="border">
      <form name="PayDateForm" method="post" action="setpaydate.do">
      <input type="text" name="zahlungseingang"
        value="<bean:write name="b" property="zahlungseingang" />" /> <input
        type="hidden" name="method" value="setPayDate" /> <input
        type="hidden" name="billid"
        value="<bean:write name="b" property="id" />" /> <input
        src="img/change.png" alt="pay date setzen" type="image" /></form>
      </td>
    </tr>
  </logic:iterate>
</table>

</div>
</body>
</html>
