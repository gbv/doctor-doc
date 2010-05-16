<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="success.heading" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />

<h3><bean:message key="message.header" />:</h3>

<bean:define id="translationKey" name="message" property="message" type="java.lang.String"/>      				
<p><pre><bean:message key="<%=translationKey%>" /></pre></p>

<logic:present name="message" property="systemMessage">
	<p><pre><bean:write name="message" property="systemMessage" /></pre></p>
</logic:present>

<br></br>
<logic:present name="message" property="link">
	<a href="<bean:write name="message" property="link" />"><bean:message key="message.next" /></a>
</logic:present>	
 </body>
</html>
