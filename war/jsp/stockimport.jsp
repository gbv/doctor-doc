<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="ch.dbs.form.*"%>
<%@ page import="ch.dbs.entity.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Bestandesverwaltung</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css">
  
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">
<logic:present name="userinfo" property="benutzer">
			<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
<table
	style="position:absolute; text-align:left; left:<bean:message key="submenupos.stock" />px; z-index:2;">
	<tr>
		<td id="submenu" nowrap title="<bean:message key="menu.export_explain" />"><a
			href="allstock.do?method=prepareExport&activemenu=stock"><bean:message key="menu.export" /></a></td>
		<td <logic:notEqual name="holdingform" property="submit" value="minus">id="submenuactive" nowrap</logic:notEqual><logic:equal name="holdingform" property="submit" value="minus">id="submenu" nowrap</logic:equal>
			title="<bean:message key="menu.import_explain" />"><a
			href="stock.do?method=prepareImport&activemenu=stock"><bean:message key="menu.import" /></a></td>
		<td id="submenu" nowrap
			title="<bean:message key="menu.locations_explain" />"><a
			href="modplace.do?method=listStockplaces&activemenu=stock"><bean:message key="menu.locations" /></a></td>
	</tr>
</table>
</logic:notEqual>
</logic:present>

<br />

<h3><bean:message key="stockimport.header" /></h3>

<p><bean:message key="stockimport.intro" /></p>

<p><bean:message key="stockimport.delete" /></p>

<h4><bean:message key="stockimport.subheader1" /></h4>

<table border="1">
	<tr>
		<th id="th-left">Stock ID&nbsp;</th>
		<td><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" />&nbsp;<bean:message key="stockimport.unique" /></td>
	</tr>
	<tr>
		<th id="th-left">Holding ID&nbsp;</th>
		<td><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" /></td>
	</tr>
	<tr>
		<th id="th-left">Location ID&nbsp;</th>
		<td><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" />&nbsp;<bean:message key="stockimport.copy" /></td>
	</tr>
	<tr>
		<th id="th-left">Location Name&nbsp;</th>
		<td><bean:message key="stockimport.location" /></td>
	</tr>
	<tr>
		<th id="th-left">Shelfmark&nbsp;</th>
		<td><bean:message key="stockimport.shelfmark" /></td>
	</tr>
	<tr>
		<th id="th-left">Title&nbsp;</th>
		<td><bean:message key="stockimport.title" /></td>
	</tr>
	<tr>
		<th id="th-left">Coden&nbsp;</th>
		<td><bean:message key="stockimport.coden" /></td>
	</tr>
	<tr>
		<th id="th-left">Publisher&nbsp;</th>
		<td><bean:message key="stockimport.publisher" /></td>
	</tr>
	<tr>
		<th id="th-left">Place&nbsp;</th>
		<td><bean:message key="stockimport.place" /></td>
	</tr>
	<tr>
		<th id="th-left">ISSN&nbsp;</th>
		<td><bean:message key="stockimport.issn" />&nbsp;<bean:message key="stockimport.machinereadable" />&nbsp;<bean:message key="stockimport.issn_example" /></td>
	</tr>
	<tr>
		<th id="th-left">ZDB-ID&nbsp;</th>
		<td><bean:message key="stockimport.zdbid" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Staryear&nbsp;</th>
		<td><bean:message key="stockimport.year" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Starvolume&nbsp;</th>
		<td><bean:message key="stockimport.volume" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Startissue&nbsp;</th>
		<td><bean:message key="stockimport.issue" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Endyear&nbsp;</th>
		<td><bean:message key="stockimport.year" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Endvolume&nbsp;</th>
		<td><bean:message key="stockimport.volume" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Endissue&nbsp;</th>
		<td><bean:message key="stockimport.issue" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
	</tr>
	<tr>
		<th id="th-left">Suppl&nbsp;</th>
		<td><bean:message key="stockimport.supplements" /></td>
	</tr>
		<tr>
		<th id="th-left">eissue&nbsp;</th>
		<td><bean:message key="stockimport.eissue" /></td>
	</tr>
	<tr>
		<th id="th-left">internal&nbsp;</th>
		<td><bean:message key="stockimport.internal" /></td>
	</tr>
	
</table>

<p></p>

<bean:message key="stockimport.csvfile" />:

<html:form action="/importholdinglist" method="post" enctype="multipart/form-data">
	<html:file property="file"/>
	<input type="hidden" name="method" value="importHoldings"  />
	<html:submit><bean:message key="stockimport.import" /></html:submit>


<p><input type="checkbox" name="condition" value="true"></input>
	<bean:message arg0="<%=appName%>" key="stockimport.condition1" />&nbsp;
	<bean:message key="stockimport.condition2" />&nbsp; 
	<bean:message key="stockimport.condition3" />	
</p>

</html:form>

<p><br /></p>


</body>
</html>