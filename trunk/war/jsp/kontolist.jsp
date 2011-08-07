<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Einstellungen</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />
<h3>Administration</h3>

<table border="1">
  <tr>
    <th>Bibliotheksname</th>
    <th>Kontotyp</th>
    <th>Kontostatus</th>
    <th>Faxoption bezahlt bis</th>
    <th>Faxserver bezahlt bis</th>
    <th>Bibliothekskontakt</th>
    <th>Rechnung<br />
    versenden</th>
    <th>Rechnungen</th>
  </tr>
  <logic:iterate id="k" name="kontoform" property="kontos">
    <tr>
      <td><bean:write name="k" property="bibliotheksname" /></td>
      <td>
      <form name="TypForm" method="post" action="changekontotyp.do">
      <select name="kontotyp">
        <option value="0"
          <logic:equal name="k" property="kontotyp" value="0"> selected</logic:equal>><bean:message bundle="systemConfig" key="application.name"/>:
        gratis</option>
        <option value="1"
          <logic:equal name="k" property="kontotyp" value="1"> selected</logic:equal>>1
        Jahr <bean:message bundle="systemConfig" key="application.name"/> Enhanced</option>
        <option value="2"
          <logic:equal name="k" property="kontotyp" value="2"> selected</logic:equal>>1
        Jahr <bean:message bundle="systemConfig" key="application.name"/> Enhanced plus Fax to Mail</option>
        <option value="3"
          <logic:equal name="k" property="kontotyp" value="3"> selected</logic:equal>>3
        Monate <bean:message bundle="systemConfig" key="application.name"/> Enhanced plus Fax to Mail</option>
      </select> <input type="hidden" name="method" value="changeKontoTyp" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="Typ &auml;ndern" type="image" /></form>
      </td>
      <td>
      <form name="StatusForm" method="post" action="changekontostate.do"><select
        name="kontostatus">
        <option value="true"
          <logic:equal name="k" property="kontostatus" value="true"> selected</logic:equal>>Aktiv</option>
        <option value="false"
          <logic:equal name="k" property="kontostatus" value="false"> selected</logic:equal>>Deaktiviert</option>
      </select> <input type="hidden" name="method" value="changeKontoState" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="Status &auml;ndern" type="image" /></form>
      </td>
      <td>
      <form name="StatusForm" method="post" action="setexpdate.do">
      <input type="text" name="expdate"
        value="<bean:write name="k" property="expdate" />" /> <input
        type="hidden" name="method" value="setExpDate" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="expire date setzen" type="image" /></form>
      </td>
      <td>
      <form name="StatusForm" method="post" action="setexpdateserver.do">
      <input type="text" name="popfaxend"
        value="<bean:write name="k" property="popfaxend" />" /> <input
        type="hidden" name="method" value="setExpDateServer" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="expire date Server setzen" type="image" /></form>
      </td>
      <td><bean:write name="k" property="bibliotheksmail" /></td>
      <td id="center"><logic:notEqual name="k" property="kontotyp"
        value="0"></logic:notEqual>
        <form name="BillingForm" method="post"
          action="preparebillingtext.do"><input type="hidden"
          name="method" value="prepareBillingText" /> <input type="hidden"
          name="kontoid" value="<bean:write name="k" property="id" />" /> <input
          src="img/change.png" alt="Rechnung versenden"
          title="Rechnung versenden" type="image" /></form>
      
      </td>
      <td id="center">
      <form name="BillingForm" method="post" action="listbillings.do">
      <input type="hidden" name="method" value="listBillings" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="Rechnungen dieses Kontos anzeigen"
        title="Rechnungen dieses Kontos anzeigen" type="image" /></form>
      </td>
    </tr>
  </logic:iterate>
</table>

</div>

</body>
</html>
