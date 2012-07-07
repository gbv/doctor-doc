<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

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
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>

<h3><bean:message key="maintenance.confirm" /></h3>

<div id="italic"><p><bean:message key="maintenance.warning" /><br></p></div>

<form action="bulkoperation.do?method=<bean:write name="operation" property="method" />" method="post">

<bean:write name="operation" property="numerOfRecords" /> <bean:message key="maintenance.numberOfRecordsConfirm" />
<input type="hidden" name="confirmed" value="true">
<input type="hidden" name="months" value="<bean:write name="operation" property="months" />">

<p><input type="submit" value="<bean:message key="stockplacesmodify.delete" />" /></p>

</form>

<p><br></p>

<a href="maintenance.do"><bean:message key="error.back" /></a>

<p><br></p>

</div>
 </body>
</html>
