<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="welcome.heading" /> </title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" /> 
 </head>
 <body>
 
<table id="header_table">
  <tr style="background-image:url(img/verlauf.png); background-repeat:repeat-x; ">
    <td class="logo"><a href="<bean:message bundle="systemConfig" key="server.welcomepage"/>"><img class="logo" src='img/sp.gif' alt='<bean:message bundle="systemConfig" key="application.name"/>'  /></a></td>
    <td><h1><bean:message bundle="systemConfig" key="application.name"/></h1><p></p>
    </td>
    <td class="kontoinfos"><tiles:insert page="import/kontoinfo.jsp" flush="true" /></td>
      <td width="20%" style="text-align:right;">
    </td>
  </tr>
  <tr>
    <td></td>
    <td colspan="3">
    </td>
  </tr>
</table>

 
<div class="content">
<br> 
<br>
<h3><bean:message key="login.choose" />:</h3>
<br>
<br>
<br>
<br>
<p><tiles:insert page="import/kontochange_login.jsp" flush="true" /></p>
</div>
 </body>
</html>

