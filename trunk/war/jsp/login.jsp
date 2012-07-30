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
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="welcome.heading" /> </title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 

<tiles:insert page="import/header.jsp" flush="true" />

 
<div class="content">
 <br>
 <h3><bean:message key="login.message" />:</h3>
 
  
 
  <table>
  
  <html:form action="logincheck" focus="email">
  
    <tr>
      <td><p>&nbsp;</p></td><td></td>
    </tr>
    <tr>
      <td><label for="email_login"><bean:message key="login.email" /></label></td><td><input id="email_login" name="email" type="email" required size="50" maxlength="50" />
     <font color=red><html:errors property="email" /></font></td>
    </tr>
    <tr>
      <td><label for="password"><bean:message key="login.password" /></label></td><td><input id="password" name="password" type="password" required size="50" maxlength="50" />
     <font color=red><html:errors property="password" /></font><p></p> 
     <input type="hidden" name="method" value="overview" /></td>
    </tr>
  <tr>
  	<td>
     <logic:present name="loginform">
       <input type="hidden" name="resolver" value="true" />
       <input type="hidden" name="issn" value="<bean:write name="loginform" property="issn" />" />
       <input type="hidden" name="mediatype" value="<bean:write name="loginform" property="mediatype" />" />
       <input type="hidden" name="jahr" value="<bean:write name="loginform" property="jahr" />" />
       <input type="hidden" name="jahrgang" value="<bean:write name="loginform" property="jahrgang" />" />
       <input type="hidden" name="heft" value="<bean:write name="loginform" property="heft" />" />
       <input type="hidden" name="seiten" value="<bean:write name="loginform" property="seiten" />" />
       <input type="hidden" name="isbn" value="<bean:write name="loginform" property="isbn" />" />
       <input type="hidden" name="artikeltitel" value="<bean:write name="loginform" property="artikeltitel" />" />
       <input type="hidden" name="zeitschriftentitel" value="<bean:write name="loginform" property="zeitschriftentitel" />" />
       <input type="hidden" name="author" value="<bean:write name="loginform" property="author" />" />
       <input type="hidden" name="kapitel" value="<bean:write name="loginform" property="kapitel" />" />
       <input type="hidden" name="buchtitel" value="<bean:write name="loginform" property="buchtitel" />" />
       <input type="hidden" name="verlag" value="<bean:write name="loginform" property="verlag" />" />
       <input type="hidden" name="rfr_id" value="<bean:write name="loginform" property="rfr_id" />" />
       <input type="hidden" name="genre" value="<bean:write name="loginform" property="genre" />" />
       <input type="hidden" name="pmid" value="<bean:write name="loginform" property="pmid" />" />
       <input type="hidden" name="doi" value="<bean:write name="loginform" property="doi" />" />
       <input type="hidden" name="sici" value="<bean:write name="loginform" property="sici" />" />
       <input type="hidden" name="lccn" value="<bean:write name="loginform" property="lccn" />" />
       <input type="hidden" name="zdbid" value="<bean:write name="loginform" property="zdbid" />" />
       <input type="hidden" name="foruser" value="<bean:write name="loginform" property="foruser" />" />
     </logic:present>
     <logic:present name="userform">
       <logic:present name="userform" property="name">
         <input type="hidden" name="kundenname" value="<bean:write name="userform" property="name" />" />
       </logic:present>
       <logic:present name="userform" property="vorname">
         <input type="hidden" name="kundenvorname" value="<bean:write name="userform" property="vorname" />" />
       </logic:present>
       <logic:present name="userform" property="email">
         <input type="hidden" name="kundenemail" value="<bean:write name="userform" property="email" />" />
       </logic:present>
       <logic:present name="userform" property="institut">
         <input type="hidden" name="kundeninstitution" value="<bean:write name="userform" property="institut" />" />
       </logic:present>
       <logic:present name="userform" property="abteilung">
         <input type="hidden" name="kundenabteilung" value="<bean:write name="userform" property="abteilung" />" />
       </logic:present>
       <logic:present name="userform" property="category">
         <input type="hidden" name="category" value="<bean:write name="userform" property="category" />" />
       </logic:present>
       <logic:present name="userform" property="telefonnrg">
         <input type="hidden" name="kundentelefon" value="<bean:write name="userform" property="telefonnrg" />" />
       </logic:present>
       <logic:present name="userform" property="adresse">
         <input type="hidden" name="kundenadresse" value="<bean:write name="userform" property="adresse" />" />
       </logic:present>
       <logic:present name="userform" property="plz">
         <input type="hidden" name="kundenplz" value="<bean:write name="userform" property="plz" />" />
       </logic:present>
       <logic:present name="userform" property="ort">
         <input type="hidden" name="kundenort" value="<bean:write name="userform" property="ort" />" />
       </logic:present>
       <logic:present name="userform" property="land">
         <input type="hidden" name="kundenland" value="<bean:write name="userform" property="land" />" />
       </logic:present>
     </logic:present>
     </td>
     <td><html:submit property="action" value="login" /></td>
  </tr>
 
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
    <tr><td colspan="2"><br></td></tr>
  
 </html:form>

  <html:form action="pwreset">
  
    <tr>
      <td colspan="2">
      	<bean:message key="login.vergessen" /> <label for="email_resend"><bean:message key="login.email" /></label>&nbsp; <input type="email" id="email_resend" name="email" required size="39" maxlength="50" />
 		<font color=red><html:errors property="email" /></font>
		<input type="hidden" name="method" value="pwreset" />
  		<input type="submit" value="<bean:message key="login.submitpwrt" />" />
  		</td>
     </tr>
  </html:form>
  
  </table>
  
  
  <p><br></p>
  <p><br></p>
  <p><br></p>
  <p><br></p>
  <tiles:insert page="import/footer.jsp" flush="true" />
  
</div>

 
 </body>
</html>
