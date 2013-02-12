<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Einstellungen</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<tiles:insert page="import/header.jsp" flush="true" />

 <table style="position:absolute; text-align:left; left:<bean:message key="submenupos.admin" />px; z-index:2;">
  <tr>
    <td 
      <logic:equal name="kontoform" property="submenu" value="all">id="submenuactive"</logic:equal>
      <logic:notEqual name="kontoform" property="submenu" value="all">id="submenu"</logic:notEqual>       
      title="<bean:message key="submenu.listkontos.all" />"><a href="kontoadmin.do?method=listKontos&activemenu=admin&submenu=all"><bean:message key="submenu.listkontos.all" /></a></td>
    <td 
	  <logic:equal name="kontoform" property="submenu" value="premium">id="submenuactive"</logic:equal>
      <logic:notEqual name="kontoform" property="submenu" value="premium">id="submenu"</logic:notEqual>       
      title="<bean:message key="submenu.listkontos.premium" />"><a href="kontoadmin.do?method=listKontos&activemenu=admin&submenu=premium"><bean:message key="submenu.listkontos.premium" /></a></td>   
  	<td 
	  <logic:equal name="kontoform" property="submenu" value="openbill">id="submenuactive"</logic:equal>
      <logic:notEqual name="kontoform" property="submenu" value="openbill">id="submenu"</logic:notEqual>       
      title="<bean:message key="submenu.listkontos.premium" />"><a href="kontoadmin.do?method=listKontos&activemenu=admin&submenu=openbill"><bean:message key="submenu.listkontos.openbill" /></a></td>   
  </tr>
</table>

<div class="content">

<br>
<h3>Administration</h3>

<table class="border">
  <tr>
    <th id="th-left">Bibliotheksname</th>
    <th id="th-left">Kontotyp</th>
    <th id="th-left">Kontostatus</th>
    <th id="th-left">Faxoption bezahlt bis</th>
    <th id="th-left">Faxserver bezahlt bis</th>
    <th id="th-left">Bibliothekskontakt</th>
    <th id="th-left">Rechnung<br>
    versenden</th>
    <th id="th-left">Rechnungen</th>
  </tr>
  <logic:iterate id="k" name="kontoform" property="kontos">
    <tr>
      <td id="border"><bean:write name="k" property="bibliotheksname" /></td>
      <td id="border">
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
        <option value="4"
          <logic:equal name="k" property="kontotyp" value="4"> selected</logic:equal>>1
        Jahr <bean:message bundle="systemConfig" key="application.name"/> Premium</option>
      </select> <input type="hidden" name="method" value="changeKontoTyp" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="Typ &auml;ndern" type="image" /></form>
      </td>
      <td id="border">
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
      <td id="border">
      <form name="StatusForm" method="post" action="setexpdate.do">
      <input type="text" name="expdate"
        value="<bean:write name="k" property="expdate" />" /> <input
        type="hidden" name="method" value="setExpDate" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="expire date setzen" type="image" /></form>
      </td>
      <td id="border">
      <form name="StatusForm" method="post" action="setexpdateserver.do">
      <input type="text" name="popfaxend"
        value="<bean:write name="k" property="popfaxend" />" /> <input
        type="hidden" name="method" value="setExpDateServer" /> <input
        type="hidden" name="kid"
        value="<bean:write name="k" property="id" />" /> <input
        src="img/change.png" alt="expire date Server setzen" type="image" /></form>
      </td>
      <td id="border"><bean:write name="k" property="bibliotheksmail" /></td>
      <td id="border"><logic:notEqual name="k" property="kontotyp"
        value="0"></logic:notEqual>
        <form name="BillingForm" method="post"
          action="preparebillingtext.do"><input type="hidden"
          name="method" value="prepareBillingText" /> <input type="hidden"
          name="kontoid" value="<bean:write name="k" property="id" />" /> <input
          src="img/change.png" alt="Rechnung versenden"
          title="Rechnung versenden" type="image" /></form>
      
      </td>
      <td id="border">
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
