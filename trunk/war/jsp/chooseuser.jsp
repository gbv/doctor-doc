<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>


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

<logic:present name="loginform">
     <input type="hidden" name="resolver" value="<bean:write name="loginform" property="resolver" />" />
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
     <input type="hidden" name="artikeltitel_encoded" value="<bean:write name="loginform" property="artikeltitel_encoded" />" />
     <input type="hidden" name="author_encoded" value="<bean:write name="loginform" property="author_encoded" />" />
     <input type="hidden" name="foruser" value="<bean:write name="loginform" property="foruser" />" />
     <input type="hidden" name="category" value="<bean:write name="loginform" property="category" />" />
</logic:present>

 
<div class="content">
<br> 
<br>
<h3><bean:message key="login.chooseuser" /></h3>
<br>
<p><tiles:insert page="import/userchange_login.jsp" flush="true" /></p>
</div>
 </body>
</html>

