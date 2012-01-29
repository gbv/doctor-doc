<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

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
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
  </tr>
</table>

<br />

<h3><bean:message key="supplier.edit" /></h3>

<form action="supplier.do?method=save" method="post">

<table>
	<tr>
		<td><bean:message key="orderview.sigel" />:</td>
		<td><input type="text" name="sigel" value="<bean:write name="supplier" property="sigel" />" size="10" maxlength="15" /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.supplier" />:</td>
		<td><input type="text" name="name" value="<bean:write name="supplier" property="name" />" size="120" maxlength="200" /></td>
	</tr>
	<tr>
		<td><bean:message key="adressen.email" />:</td>
		<td><input type="text" name="emailILL" value="<bean:write name="supplier" property="emailILL" />" size="50" maxlength="100" /></td>
	</tr>
	<logic:notEqual name="supplier" property="kid" value="0">
		<tr>
			<td>
				<bean:message key="supplier.private" />:
			</td>
			<td>
				<input type="checkbox" name="individual" <logic:notEqual name="supplier" property="kid" value="0">checked="checked"</logic:notEqual> />
			</td>
		</tr>
	</logic:notEqual>
	<tr>
		<td colspan="2">
			<input type="hidden" name="lid" value="<bean:write name="supplier" property="lid" />" /><br />
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="<bean:message key="bestellform.save" />" /></td>
	</tr>
</table>

</form>

<p><a href="listsuppliers.do"><bean:message key="error.back" /></a></p>


<p><br /></p>

</div>
 </body>
</html>
