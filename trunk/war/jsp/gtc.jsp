<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="gtc.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<logic:present name="userinfo">
	<logic:present name="userinfo" property="benutzer">
		<logic:notEmpty name="userinfo" property="benutzer.gtc"> 
			<tiles:insert page="import/header.jsp" flush="true" />
		</logic:notEmpty>
	</logic:present>
</logic:present>

<div class="content">

<br>
<h3>General Terms and Conditions (Version 1):</h3>
<logic:present name="userinfo">
	<logic:empty name="userinfo" property="benutzer.gtc">
	    <div id="italic"><bean:message key="gtc.comment" /></div>
	</logic:empty>
</logic:present>

<p></p>
 

<p>

[Place your GTC here]


</p>

</p>

<logic:present name="userinfo">
<logic:present name="userinfo" property="benutzer.gtc">
<logic:equal name="userinfo" property="benutzer.gtc" value=""> 
 <html:form action="gtc_" method="post">

<table>

 <logic:present name="orderform">
 <input type="hidden" name="resolver" value="true" />
 <input type="hidden" name="issn" value="<bean:write name="orderform" property="issn" />" />
 <input type="hidden" name="mediatype" value="<bean:write name="orderform" property="mediatype" />" />
 <input type="hidden" name="jahr" value="<bean:write name="orderform" property="jahr" />" />
 <input type="hidden" name="jahrgang" value="<bean:write name="orderform" property="jahrgang" />" />
 <input type="hidden" name="heft" value="<bean:write name="orderform" property="heft" />" />
 <input type="hidden" name="seiten" value="<bean:write name="orderform" property="seiten" />" />
 <input type="hidden" name="isbn" value="<bean:write name="orderform" property="isbn" />" />
 <input type="hidden" name="artikeltitel" value="<bean:write name="orderform" property="artikeltitel" />" />
 <input type="hidden" name="zeitschriftentitel" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
 <input type="hidden" name="author" value="<bean:write name="orderform" property="author" />" />
 <input type="hidden" name="kapitel" value="<bean:write name="orderform" property="kapitel" />" />
 <input type="hidden" name="buchtitel" value="<bean:write name="orderform" property="buchtitel" />" />
 <input type="hidden" name="verlag" value="<bean:write name="orderform" property="verlag" />" />
 <input type="hidden" name="rfr_id" value="<bean:write name="orderform" property="rfr_id" />" />
 <input type="hidden" name="genre" value="<bean:write name="orderform" property="genre" />" />
 <input type="hidden" name="pmid" value="<bean:write name="orderform" property="pmid" />" />
 <input type="hidden" name="doi" value="<bean:write name="orderform" property="doi" />" />
 <input type="hidden" name="sici" value="<bean:write name="orderform" property="sici" />" />
 <input type="hidden" name="lccn" value="<bean:write name="orderform" property="lccn" />" />
 <input type="hidden" name="zdbid" value="<bean:write name="orderform" property="zdbid" />" />
 <input type="hidden" name="foruser" value="<bean:write name="orderform" property="foruser" />" />
 <input type="hidden" name="category" value="<bean:write name="orderform" property="kundenkategorieID" />" />
 </logic:present>


  <tr>
    <td><html:submit property="method" value="accept" /></td>
    <td><br></td>
    <td><html:submit property="method" value="decline" /></td>
  </tr>
</table>

	 </html:form>
	</logic:equal>
</logic:present>
</logic:present> 

</div>

 
 </body>
</html>
