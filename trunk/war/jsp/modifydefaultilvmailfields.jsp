<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="success.heading" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  <style type="text/css">
		textarea { width: 100%; border-width: 0; }
  </style> 
 </head>
<body> 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">
<br />
<h3><bean:message key="message.header" />:</h3>

<bean:define id="translationKey" name="message" property="message" type="java.lang.String"/>              
<p /><bean:message key="<%=translationKey%>" />
<logic:present name="message" property="systemMessage">
  <p /><pre><bean:write name="message" property="systemMessage" /></pre>
</logic:present>
<p />
<logic:present name="message" property="link">
  <a href="<bean:write name="message" property="link" />"><bean:message key="message.next" /></a>
</logic:present>
<p />  
<h4><bean:message key="ilvmail.changemailfields" /></h4> 
    
<logic:present name="IlvReportForm" property="labelto">   
  <form action="save-ilv-maildefaultfields.do">
  <input type="hidden" name="method" value="saveMailFields" />
  <input type="submit" value="<bean:message key="ilvmail.default" />" />
  <p />
  <table border="1">
  <tr>
    <td><bean:message key="ilvmail.subject" /> 
    </td>    
    <td>
    <input name="subject" type="text" size="100" value="<bean:write name="IlvReportForm" property="subject" />" autofocus />
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <textarea name="mailtext" cols="25" rows="15"><bean:write name="IlvReportForm" property="mailtext" /></textarea>
    </td>
  </tr>
</table>
</form>
</logic:present>

</div>
 </body>
</html>
