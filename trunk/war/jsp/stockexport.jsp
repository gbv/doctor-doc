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
    <td id="submenuactive" nowrap title="<bean:message key="menu.export_explain" />"><a
      href="allstock.do?method=prepareExport&activemenu=stock"><bean:message key="menu.export" /></a></td>
    <td id="submenu" nowrap
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
 

<h3><bean:message key="stockexport.heading" /></h3>

<p><b><bean:message key="stockexport.export" /></b></p>

<table>
  <tr>
    <td>OpenOffice:</td>
    <td><a href="export-holdings.do" target="_blank">CSV</a></td>
    <td>&nbsp;(<bean:message key="stockexport.no.excel" />)</td>
  </tr>
  <tr>
    <td>MS-Excel:</td>
    <td><a href="export-holdings.do?filetype=txt" target="_blank">TXT</a></td>
    <td>&nbsp;(<a href="http://www.doctor-doc.com/howto_export.pdf" target="_blank"><bean:message key="stockexport.excel" /></a>)</td>
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
<p><li><bean:message key="stockexport.list2.one" /></li></p>
<p><li><bean:message key="stockexport.list2.two" /></li></p>
<p><li><bean:message key="stockexport.list2.three" /></li></p>
<p><li><bean:message key="stockexport.list2.four" /></li></p>
<p><li><bean:message arg0="<%=appName%>" key="stockexport.list2.five" /></li></p>
<p><li><bean:message key="stockexport.list2.six" /></li></p>
<p><li><bean:message key="stockexport.list2.seven" /></li></p>
<p><li><bean:message key="stockexport.list2.eight" /></li></p>
<p><li><bean:message key="stockexport.list2.nine" /></li></p>
<p><li><bean:message key="stockexport.list2.ten" /></li></p>
<p><li><bean:message key="stockexport.list2.eleven" />
<br /><bean:message key="stockexport.list2.twelve" />
<br /><bean:message key="stockexport.list2.thirteen" />
<br /><bean:message key="stockexport.list2.fourteen" />
</li></p>
</ul>
<p>
  <bean:message key="stockexport.list2.fifteen" />
</p>
<p><br /></p>


</div>
</body>
</html>
