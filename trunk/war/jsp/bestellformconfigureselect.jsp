<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>

<html>

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
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>
<h3><bean:message key="bestellformconfigureselect.titel" /> - <bean:message key="bestellformconfigureselect.possible" /></h3>


<p><bean:message key="bestellformconfigureselect.noconfig" /></p>
<p><bean:message key="bestellformconfigureselect.possibleconfig" /></p>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellformconfigureselect.zugriff" /></th>
    <th id="th-left"><bean:message key="bestellformconfigureselect.szenario" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellformconfigureselect.ip" />&nbsp;</td>
    <td id="border"><bean:message key="bestellformconfigureselect.ip_explain" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellformconfigureselect.eingeloggt" />&nbsp;</td>
    <td id="border"><bean:message key="bestellformconfigureselect.eingeloggt_explain" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellformconfigureselect.kkid" />&nbsp;</td>
    <td id="border"><bean:message key="bestellformconfigureselect.kkid_explain" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellformconfigureselect.bkid" />&nbsp;</td>
    <td id="border"><bean:message key="bestellformconfigureselect.bkid_explain" /></td>
  </tr>
</table>

<p><bean:message key="bestellformconfigureselect.kontakt" /></p>

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

<p><br></p>

<h3><bean:message key="bestellform.external_title" /></h3>
<p><a href="externalform.do?method=prepDaiaForm"><bean:message key="bestellform.external_create" /></a></p>
       
</div>



 </body>
</html>
