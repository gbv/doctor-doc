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
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.ip" />"><a href="listipranges.do"><bean:message key="modifykonto.ip" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>

<form action="suppliersettings.do" method="post">

<h3><bean:message key="suppliers.new" /></h3>

<a href="supplier.do?method=create"><bean:message key="suppliers.new" /></a>

<logic:present name="message">
<bean:define id="translationKey" name="message" property="message" type="java.lang.String"/>
<div id="italic"><p><bean:message key="<%=translationKey%>" /></p></div>
</logic:present>

<h3><bean:message key="suppliers.privates" /></h3>

<p><input type="checkbox" name="showprivsuppliers" <logic:equal name="conf" property="showprivsuppliers" value="true">checked="checked"</logic:equal> /> <bean:message key="suppliers.showprivates" /></p>
<input type="hidden" name="changedsettings" value="true" />
<p><input type="submit" value="<bean:message key="bestellform.save" />" /></p>

<logic:notEmpty name="privsuppliers">
<table class="border">
	<tr>
		<th id="th-left">
			&nbsp;
		</th>
		<th id="th-left">
			<bean:message key="orderview.sigel" />
		</th>
		<th id="th-left">
			<bean:message key="searchorders.supplier" />
		</th>
		<th id="th-left">
			<bean:message key="adressen.email" />
		</th>
		<th id="th-left">
			&nbsp;
		</th>
	</tr>
	<logic:iterate id="priv" name="privsuppliers">
		<tr>
			<td id="border">
				<a href="supplier.do?method=edit&sid=<bean:write name="priv" property="lid" />">
          		<img border="0" src="img/edit.png" alt="<bean:message key="suppliers.edit" />" title="<bean:message key="suppliers.edit" />"/></a>
			</td>
			<td id="border">
				<bean:write name="priv" property="sigel" />
			</td>
			<td id="border">
				<bean:write name="priv" property="name" />
			</td>
			<td id="border">
				<bean:write name="priv" property="emailILL" />
			</td>
			<td id="border">
				<a href="supplier.do?method=delete&sid=<bean:write name="priv" property="lid" />"><img border="0" src="img/drop.png" alt="<bean:message key="suppliers.delete" />" title="<bean:message key="suppliers.delete" />"/></a>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="privsuppliers">
	<bean:message key="suppliers.none" />
</logic:empty>

<h3><bean:message key="suppliers.publics" /></h3>

<p><input type="checkbox" name="showpubsuppliers" <logic:equal name="conf" property="showpubsuppliers" value="true">checked="checked"</logic:equal> /> <bean:message key="suppliers.showpublics" /></p>
<p><input type="submit" value="<bean:message key="bestellform.save" />" /></p>

<logic:notEmpty name="pubsuppliers">
<table class="border">
	<tr>
		<th id="th-left">
			&nbsp;
		</th>
		<th id="th-left">
			<bean:message key="orderview.sigel" />
		</th>
		<th id="th-left">
			<bean:message key="searchorders.supplier" />
		</th>
		<th id="th-left">
			<bean:message key="adressen.email" />
		</th>
		<th id="th-left">
			<bean:message key="suppliers.show4country" />
		</th>
	</tr>
	<logic:iterate id="pub" name="pubsuppliers">
		<tr>
			<td id="border">
				<logic:notEqual name="pub" property="land_allgemein" value="true">
					<a href="supplier.do?method=edit&sid=<bean:write name="pub" property="lid" />">
	          		<img border="0" src="img/edit.png" alt="<bean:message key="suppliers.edit" />" title="<bean:message key="suppliers.edit" />"/></a>
          		</logic:notEqual>
			</td>
			<td id="border">
				<bean:write name="pub" property="sigel" />
			</td>
			<td id="border">
				<bean:write name="pub" property="name" />
			</td>
			<td id="border">
				<bean:write name="pub" property="emailILL" />
			</td>
			<td id="border">
				<logic:notEmpty name="pub" property="countryCode">
					<bean:write name="pub" property="countryCode" />
				</logic:notEmpty>
				<logic:empty name="pub" property="countryCode">
					<bean:message key="suppliers.all" />
				</logic:empty>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>
<logic:empty name="pubsuppliers">
	<bean:message key="suppliers.none" />
</logic:empty>

</form>

<p><br></p>

</div>
 </body>
</html>
