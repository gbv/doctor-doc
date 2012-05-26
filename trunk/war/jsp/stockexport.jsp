<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="menu.export" /></title>
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
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.export_explain" />"><a
      href="allstock.do?method=prepareExport&activemenu=stock"><bean:message key="menu.export" /></a></td>
    <td id="submenu" nowrap="nowrap"
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
 

<h3><bean:message key="stockexport.heading" /></h3>

<p><b><bean:message key="stockexport.export" /></b></p>

<table>
  <tr>
    <td>MS-Excel:</td>
    <td><a href="export-holdings.do?filetype=xls" target="_blank">XLS</a></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>OpenOffice:</td>
    <td><a href="export-holdings.do?filetype=csv" target="_blank">CSV</a></td>
    <td>&nbsp;(<bean:message key="stockexport.no.excel" />)</td>
  </tr>
  <tr>
    <td>Text (UTF-8):</td>
    <td><a href="export-holdings.do?filetype=txt" target="_blank">TXT</a></td>
    <td>&nbsp;</td>
  </tr>
</table>
 
<h4><bean:message key="stockexport.subheader1" /></h4>

<ol>
  <li><bean:message key="stockexport.list1.one" /></li>
  <li><bean:message key="stockexport.list1.three" /></li>
  <li><bean:message key="stockexport.list1.four" /></li>
  <li><bean:message key="stockexport.list1.five" /></li>
</ol>

<p>
  <bean:message key="stockexport.tip1" />&nbsp;<bean:message key="stockexport.tip2" />
</p>

<h4><bean:message key="stockexport.subheader2" /></h4>

<ul>
<li><p><bean:message key="stockexport.list2.one" /></p></li>
<li><p><bean:message key="stockexport.list2.two" /></p></li>
<li><p><bean:message key="stockexport.list2.three" /></p></li>
<li><p><bean:message key="stockexport.list2.four" /></p></li>
<li><p><bean:message arg0="<%=appName%>" key="stockexport.list2.five" /></p></li>
<li><p><bean:message key="stockexport.list2.six" /></p></li>
<li><p><bean:message key="stockexport.list2.seven" /></p></li>
<li><p><bean:message key="stockexport.list2.eight" /></p></li>
<li><p><bean:message key="stockexport.list2.nine" /></p></li>
<li><p><bean:message key="stockexport.list2.ten" /></p></li>
<li><p><bean:message key="stockexport.list2.eleven" />
<br><bean:message key="stockexport.list2.twelve" />
<br><bean:message key="stockexport.list2.thirteen" />
<br><bean:message key="stockexport.list2.fourteen" />
</p></li>
</ul>
<p>
  <bean:message key="stockexport.list2.fifteen" />
</p>
<p><br></p>


</div>
</body>
</html>
