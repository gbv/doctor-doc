<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="suppliers.title" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="modifykonto.ip" />"><a href="listipranges.do"><bean:message key="modifykonto.ip" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>

<h3><bean:message key="modifykonto.ip" /></h3>

<logic:present name="message">
<bean:define id="translationKey" name="message" property="message" type="java.lang.String"/>              
	<div id="italic">
		<p><bean:message key="<%=translationKey%>" /> <bean:write name="message" property="systemMessage"/></p>
	</div>
</logic:present>

<h4><bean:write name="userinfo" property="konto.bibliotheksname" /></h4>

<form action="addipranges.do" method="post">

<textarea name="ips" rows="10" cols="40"><logic:iterate name="ranges" id="ip">
<bean:write name="ip" property="inhalt" /></logic:iterate></textarea>

<p><input type="submit" value="<bean:message key="modifykonto.save" />" /></p>

</form>

<p><br></p>

<bean:message key="modifykonto.ip4Example" />

</div>
 </body>
</html>
