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

<!-- This is the overall general, single message to be displayed as title of the page -->

<bean:define id="translationKey" name="singleMessage" property="message" type="java.lang.String"/>      				
<h3><bean:message key="<%=translationKey%>" /></h3>

<logic:present name="singleMessage" property="systemMessage">
	<p><pre><bean:write name="message" property="systemMessage" /></pre></p>
</logic:present>


<!-- This is the ArrayList <Message> to be iterated -->

<logic:iterate id="msgs" name="messageList">

	<bean:define id="translationKey" name="msgs" property="message" type="java.lang.String"/>      				
	<p><bean:message key="<%=translationKey%>" />:

	<logic:present name="msgs" property="systemMessage">
		<bean:write name="msgs" property="systemMessage" /></p>
	</logic:present>

</logic:iterate>

<!-- This is the the link of the single message to be displayed -->

<br></br>
<logic:present name="singleMessage" property="link">
	<a href="<bean:write name="singleMessage" property="link" />"><bean:message key="error.back" /></a>
</logic:present>
 </body>
</html>
