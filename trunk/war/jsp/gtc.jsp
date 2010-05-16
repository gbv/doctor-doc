<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="gtc.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />
<h3>General Terms and Conditions (Version 1):</h3>
<logic:present name="userinfo" property="benutzer.gtc">
	<logic:equal name="userinfo" property="benutzer.gtc" value="">
		<div id="italic"><bean:message key="gtc.comment" /></div> 
	</logic:equal>
</logic:present>

<p></p>
 
 
<p>

[Place your GTC here]

</p>


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
 <input type="hidden" name="artikeltitel_encoded" value="<bean:write name="orderform" property="artikeltitel_encoded" />" />
 <input type="hidden" name="author_encoded" value="<bean:write name="orderform" property="author_encoded" />" />
 <input type="hidden" name="foruser" value="<bean:write name="orderform" property="foruser" />" />
 </logic:present>


	<tr>
		<td><html:submit property="method" value="accept" /></td>
		<td><br /></td>
		<td><html:submit property="method" value="decline" /></td>
	</tr>
</table>

 </html:form>
 </logic:equal>
 </logic:present>

</div>

 
 </body>
</html>
