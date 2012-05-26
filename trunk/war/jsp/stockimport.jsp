<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="ch.dbs.form.*"%>
<%@ page import="ch.dbs.entity.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="menu.import" /></title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  
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
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.export_explain" />"><a
      href="allstock.do?method=prepareExport&activemenu=stock"><bean:message key="menu.export" /></a></td>
    <td <logic:notEqual name="holdingform" property="submit" value="minus">id="submenuactive" nowrap</logic:notEqual><logic:equal name="holdingform" property="submit" value="minus">id="submenu" nowrap</logic:equal>
      title="<bean:message key="menu.import_explain" />"><a
      href="stock.do?method=prepareImport&activemenu=stock"><bean:message key="menu.import" /></a></td>
    <td id="submenu" nowrap="nowrap"
      title="<bean:message key="menu.locations_explain" />"><a
      href="modplace.do?method=listStockplaces&activemenu=stock"><bean:message key="menu.locations" /></a></td>
  </tr>
</table>
</logic:notEqual>
</logic:present>

<br>

<h3><bean:message key="stockimport.header" /></h3>

<p><bean:message key="stockimport.intro" /></p>

<p><bean:message key="stockimport.delete" /></p>

<h4><bean:message key="stockimport.subheader1" /></h4>

<p><bean:message key="bestellform.required" /> (*)</p>
<table class="border">
  <tr>
    <th id="th-left">Stock ID&nbsp;</th>
    <td id="border"><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" />&nbsp;<bean:message key="stockimport.unique" /></td>
  </tr>
  <tr>
    <th id="th-left">Holding ID&nbsp;</th>
    <td id="border"><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" /></td>
  </tr>
  <tr>
    <th id="th-left">Location ID&nbsp;</th>
    <td id="border"><bean:message key="stockimport.machinegenerated" />&nbsp;<bean:message key="stockimport.dontchange" />&nbsp;<bean:message key="stockimport.copy" /></td>
  </tr>
  <tr>
    <th id="th-left">Location Name&nbsp;</th>
    <td id="border"><b><bean:message key="stockimport.location" />*</b></td>
  </tr>
  <tr>
    <th id="th-left">Shelfmark&nbsp;</th>
    <td id="border"><bean:message key="stockimport.shelfmark" /></td>
  </tr>
  <tr>
    <th id="th-left">Title&nbsp;</th>
    <td id="border"><b><bean:message key="stockimport.title" />*</b></td>
  </tr>
  <tr>
    <th id="th-left">Coden&nbsp;</th>
    <td id="border"><bean:message key="stockimport.coden" /></td>
  </tr>
  <tr>
    <th id="th-left">Publisher&nbsp;</th>
    <td id="border"><bean:message key="stockimport.publisher" /></td>
  </tr>
  <tr>
    <th id="th-left">Place&nbsp;</th>
    <td id="border"><bean:message key="stockimport.place" /></td>
  </tr>
  <tr>
    <th id="th-left">ISSN&nbsp;</th>
    <td id="border"><bean:message key="stockimport.issn" />&nbsp;<bean:message key="stockimport.machinereadable" />&nbsp;<bean:message key="stockimport.issn_example" /></td>
  </tr>
  <tr>
    <th id="th-left">ZDB-ID&nbsp;</th>
    <td id="border"><bean:message key="stockimport.zdbid" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Startyear&nbsp;</th>
    <td id="border"><b><bean:message key="stockimport.year" />&nbsp;<bean:message key="stockimport.machinereadable" />*</b></td>
  </tr>
  <tr>
    <th id="th-left">Starvolume&nbsp;</th>
    <td id="border"><bean:message key="stockimport.volume" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Startissue&nbsp;</th>
    <td id="border"><bean:message key="stockimport.issue" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Endyear&nbsp;</th>
    <td id="border"><bean:message key="stockimport.year" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Endvolume&nbsp;</th>
    <td id="border"><bean:message key="stockimport.volume" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Endissue&nbsp;</th>
    <td id="border"><bean:message key="stockimport.issue" />&nbsp;<bean:message key="stockimport.machinereadable" /></td>
  </tr>
  <tr>
    <th id="th-left">Suppl&nbsp;</th>
    <td id="border"><bean:message key="stockimport.supplements" /></td>
  </tr>
  <tr>
    <th id="th-left">remarks&nbsp;</th>
    <td id="border"><bean:message key="stockimport.remarks" /></td>
  </tr>
  <tr>
    <th id="th-left">eissue&nbsp;</th>
    <td id="border"><bean:message key="stockimport.eissue" /></td>
  </tr>
  <tr>
    <th id="th-left">internal&nbsp;</th>
    <td id="border"><b><bean:message key="stockimport.internal" />*</b></td>
  </tr>
  
</table>

<p></p>

<bean:message key="stockimport.csvfile" />:

<html:form action="/importholdinglist" method="post" enctype="multipart/form-data">
  <html:file property="file"/>
  <input type="hidden" name="method" value="importHoldings"  />
  <html:submit><bean:message key="stockimport.import" /></html:submit>


<p><input type="checkbox" name="condition" value="true">
  <bean:message arg0="<%=appName%>" key="stockimport.condition1" />&nbsp;
  <bean:message key="stockimport.condition2" />&nbsp; 
  <bean:message key="stockimport.condition3" />  
</p>

</html:form>

<p><br></p>

</div>
</body>
</html>
