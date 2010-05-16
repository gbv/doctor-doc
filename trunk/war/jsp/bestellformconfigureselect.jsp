<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="bestellformconfigureselect.header" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
	<tr>
		<td id="submenu" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
		<td id="submenuactive" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
	</tr>
</table>

<br />
<h3><bean:message key="bestellformconfigureselect.titel" /> - <bean:message key="bestellformconfigureselect.possible" /></h3>


<p><bean:message key="bestellformconfigureselect.noconfig" /></p>
<p><bean:message key="bestellformconfigureselect.possibleconfig" /></p>
<table border="1" cellspacing="0px">
	<tr>
		<th id="th-left"><bean:message key="bestellformconfigureselect.zugriff" /></th>
		<th id="th-left"><bean:message key="bestellformconfigureselect.szenario" /></th>
	</tr>
	<tr>
		<td><bean:message key="bestellformconfigureselect.ip" />&nbsp;</td>
		<td><bean:message key="bestellformconfigureselect.ip_explain" /></td>
	</tr>
	<tr>
		<td><bean:message key="bestellformconfigureselect.eingeloggt" />&nbsp;</td>
		<td><bean:message key="bestellformconfigureselect.eingeloggt_explain" /></td>
	</tr>
	<tr>
		<td><bean:message key="bestellformconfigureselect.kkid" />&nbsp;</td>
		<td><bean:message key="bestellformconfigureselect.kkid_explain" /></td>
	</tr>
	<tr>
		<td><bean:message key="bestellformconfigureselect.bkid" />&nbsp;</td>
		<td><bean:message key="bestellformconfigureselect.bkid_explain" /></td>
	</tr>
<table>

<p><bean:message key="bestellformconfigureselect.kontakt" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/>"><bean:message bundle="systemConfig" key="systemEmail.email"/></a></p>

<h3><bean:message key="bestellformconfigureselect.config" /></h3>

<p><bean:message key="bestellformconfigureselect.selection" /></p>

<logic:present name="ipbasiert">
<p><a href="bfconfigure.do?method=modify&id=<bean:write name="ipbasiert" />"><bean:message key="bestellformconfigureselect.cf_ipbased" /></a></p>
</logic:present>

<logic:present name="eingeloggt">
<a href="bfconfigure.do?method=modify&id=<bean:write name="eingeloggt" />"><bean:message key="bestellformconfigureselect.cf_eingeloggt" /></a><br>
</logic:present>

<logic:present name="kkid">
<p>
	<logic:iterate id="k" name="kkid">
		<a href="bfconfigure.do?method=modify&id=-2&kennung=<bean:write name="k" property="inhalt" />"><bean:message key="bestellformconfigureselect.cf_kkid" />: <bean:write name="k" property="inhalt" /></a><br>
	</logic:iterate>
</p>
</logic:present>

<logic:present name="bkid">
<p>
	<logic:iterate id="b" name="bkid">
		<a href="bfconfigure.do?method=modify&id=-3&kennung=<bean:write name="b" property="inhalt" />"><bean:message key="bestellformconfigureselect.cf_bkid" />: <bean:write name="b" property="inhalt" /></a><br>
	</logic:iterate>
</p>
</logic:present>

<p><bean:message arg0="<%=appName%>" key="bestellformconfigureselect.sprache" /></p>
 			
</div>



 </body>
</html>
