<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="findfree.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />
<div class="content"><br />
					
<form action="issnsearch_.do" method="post">
   
  <table>
    <tr>
      <td><input type="submit" value="<bean:message key="foundfree.submit" />"></input></td>
    </tr>
  </table>
  <input name="method" type="hidden" value="prepareIssnSearch" />
  <logic:present name="orderform" property="artikeltitel">
 		<input name="artikeltitel" type="hidden" value="<bean:write name="orderform" property="artikeltitel" />" />
 		 		<p><bean:message key="bestellform.artikeltitel" />: <bean:write name="orderform" property="artikeltitel" /></p>
<p>	<div id="italic"><bean:message key="save.artikel" /> <bean:message key="save.gefunden" /> <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&deloptions=email&status=erledigt&lid=30&preisvorkomma=0&preisnachkomma=00&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></div></p>
  </logic:present> 
  
  <logic:present name="orderform" property="issn">
 		<input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" />
  </logic:present>
  <logic:present name="orderform" property="zeitschriftentitel">
 		<input name="zeitschriftentitel" type="hidden" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
  </logic:present>
  <logic:present name="orderform" property="jahr">
 		<input name="jahr" type="hidden" value="<bean:write name="orderform" property="jahr" />" />
  </logic:present> 
  <logic:present name="orderform" property="jahrgang">
 		<input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" />
  </logic:present>
  <logic:present name="orderform" property="heft">
 		<input name="heft" type="hidden" value="<bean:write name="orderform" property="heft" />" />
  </logic:present>
  <logic:present name="orderform" property="seiten">
 		<input name="seiten" type="hidden" value="<bean:write name="orderform" property="seiten" />" />
  </logic:present>
    <logic:present name="orderform" property="author">
 		<input name="author" type="hidden" value="<bean:write name="orderform" property="author" />" />
  </logic:present>
  <logic:present name="orderform" property="autocomplete">
 		<input name="autocomplete" type="hidden" value="<bean:write name="orderform" property="autocomplete" />" />
  </logic:present>
  <logic:present name="orderform" property="flag_noissn">
 		<input name="flag_noissn" type="hidden" value="<bean:write name="orderform" property="flag_noissn" />" />
  </logic:present>
  <logic:present name="orderform" property="runs_autocomplete">
 		<input name="runs_autocomplete" type="hidden" value="<bean:write name="orderform" property="runs_autocomplete" />" />
  </logic:present>
  <logic:present name="orderform" property="pmid">
 		<input name="pmid" type="hidden" value="<bean:write name="orderform" property="pmid" />" />
  </logic:present>
  <logic:present name="orderform" property="doi">
 		<input name="doi" type="hidden" value="<bean:write name="orderform" property="doi" />" />
  </logic:present>
 		<input name="didYouMean" type="hidden" value="<bean:write name="orderform" property="didYouMean" />" />
 		<input name="checkDidYouMean" type="hidden" value="<bean:write name="orderform" property="checkDidYouMean" />" />
  
  </form>
  
<logic:present name="treffer_gs" property="zeitschriften">
	<p><i>powered by Google Scholar</i></p>
	<logic:iterate id="t2" name="treffer_gs" property="zeitschriften">
		<p><a href="<bean:write name="t2" property="link"/>" target="_blank"><bean:write name="t2" property="url_text"/></a>
		<logic:present name="userinfo" property="benutzer">
		<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
			<br><bean:write name="t2" property="link"/></br>
		</logic:notEqual>
		</logic:present>
		</p>
	</logic:iterate>
		<p>______________________________________________________________</p>
</logic:present>

<logic:present name="treffer_gl" property="zeitschriften">
	<p><i>powered by Google</i></p>
	<logic:iterate id="t1" name="treffer_gl" property="zeitschriften">
		<p><a href="<bean:write name="t1" property="link"/>" target="_blank"><bean:write name="t1" property="url_text"/></a>
		<logic:present name="userinfo" property="benutzer">
		<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
			<br><bean:write name="t1" property="link"/></br>
		</logic:notEqual>
		</logic:present>
		</p>
   	</logic:iterate>
</logic:present>

<p></p>

<logic:notPresent name="userinfo" property="konto">
	<p><bean:message key="error.timeout" /></p>
	<p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>

</div>

 </body>
</html>
