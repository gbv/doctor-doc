<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta name="robots" content="noindex" />
  <title> <bean:message key="error.heading" /> </title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br>
<h3><bean:message key="error.titel" /></h3>
<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<logic:notPresent name="errormessage" property="error">
<!-- general error without bean 'errormessage'. Occurs with invalid paths and methods. -->
	<bean:message key="ordersuccess.error" />	
	<p><html:errors/></p>
</logic:notPresent>

<logic:present name="errormessage" property="error">
	<bean:define id="em"><bean:write name="errormessage" property="error"/></bean:define>
	<p><bean:message arg0="<%=appName%>" key="<%=em%>" /></p>
</logic:present>

<logic:present name="errormessage" property="error_specific">
  <p><bean:write name="errormessage" property="error_specific" /></p>
</logic:present>

<br>
<logic:present name="errormessage" property="error">
  <a href="<bean:write name="errormessage" property="link" />"><bean:message key="error.back" /></a>
</logic:present>
  
  <logic:present name="userinfo" property="benutzer">
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
  <logic:present name="orderform" property="issn">
  <logic:notEqual name="orderform" property="submit" value="GBV">
   <p><bean:message key="save.manuell_bestellen" />:<br>
   <a href="http://www.subito-doc.de/order/po.php?BI=CH_SO%2FDRDOC&VOL=<bean:write name="orderform" property="jahrgang" />/<bean:write name="orderform" property="heft" />&APY=<bean:write name="orderform" property="jahr" />&PG=<bean:write name="orderform" property="seiten" />&SS=<bean:write name="orderform" property="issn" />&JT=&ATI=<bean:write name="orderform" property="artikeltitel" />&AAU=<bean:write name="orderform" property="author" /><logic:present name="orderform" property="sessionid">&PHPSESSID=<bean:write name="orderform" property="sessionid" /></logic:present>" target="_blank"><bean:message key="order.subito" /></a><br>&nbsp;<bean:message key="order.keepvalues" />
   </p>
   <p>
    <bean:message key="save.manuell" />: <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&status=bestellt&lid=32&deloptions=<logic:present name="userinfo" property="konto.faxno">fax%20to%20pdf</logic:present><logic:notPresent name="userinfo" property="konto.faxno">fax</logic:notPresent>&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&author=<bean:write name="orderform" property="author" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" />
  </p>
  </logic:notEqual>
  </logic:present>
  </logic:notEqual>
  </logic:present>
        
</div>
 
 </body>
</html>
