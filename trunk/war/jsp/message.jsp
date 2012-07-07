<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="success.heading" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br>

<h3><bean:message key="message.header" />:</h3>

<bean:define id="translationKey" name="message" property="message" type="java.lang.String"/>              
<p></p><bean:message key="<%=translationKey%>" />

<logic:present name="message" property="systemMessage">
  <p></p><pre><bean:write name="message" property="systemMessage" /></pre>
</logic:present>

<br>
<logic:present name="message" property="link">
  <a href="<bean:write name="message" property="link" />"><bean:message key="message.next" /></a>
</logic:present>
</div>
 </body>
</html>
