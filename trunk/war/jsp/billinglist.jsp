<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
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

<div class="content"><br />

<h3>Rechnungen des Kontos <bean:write name="billingform"
  property="konto.bibliotheksname" /></h3>


<table border="1">
  <tr>
    <th>Rechnungsgrund</th>
    <th>Betrag</th>
    <th>W&auml;hrung</th>
    <th>Rechnungsnummer</th>
    <th>Rechnungsdatum</th>
    <th>Zahlungseingang</th>
  </tr>
  <logic:iterate id="b" name="billingform" property="billings">
    <tr>
      <td id="center"><bean:write name="b"
        property="rechnungsgrund.inhalt" /></td>
      <td id="center"><bean:write name="b" property="betrag" /></td>
      <td id="center"><bean:write name="b" property="waehrung" /></td>

      <td id="center"><bean:write name="b" property="rechnungsnummer" /></td>
      <td id="center"><bean:write name="b" property="rechnungsdatum" /></td>
      <td id="center">
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
