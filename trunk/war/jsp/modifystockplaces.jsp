<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - Standortverwaltung</title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  

  
 </head>
 
<body>
<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">
<logic:present name="userinfo" property="benutzer">
			<logic:equal name="userinfo" property="benutzer.rechte" value="3">
<table
	style="position:absolute; text-align:left; left:<bean:message key="submenupos.stock" />px; z-index:2;">
	<tr>
		<td id="submenu" nowrap title="Bestandes&uuml;bersicht"><a
			href="allstock.do?method=listBestand&activemenu=stock">&Uuml;bersicht</a></td>
		<td id="submenu" nowrap
			title="Neuen Bestand eingeben: Positivliste"><a
			href="stock.do?method=prepare&activemenu=stock">Bestand eingeben</a></td>
		<td id="submenu" nowrap
			title="Fehlendes vermerken: Negativliste"><a
			href="stock.do?method=prepare&submit=minus&activemenu=stock">L&uuml;cke eingeben</a></td>
		<td id="submenuactive" nowrap
			title="Standorte hinzuf&uuml;gen und verwalten"><a
			href="modplace.do?method=listStandorte&activemenu=stock">Standortverwaltung</a></td>
	</tr>
</table>
</logic:equal>
</logic:present>
 <br />
<h3>Standortverwaltung</h3>

<logic:equal name="holdingform" property="mod" value="false">
<p>neuen Standort eingeben:</p>
<html:form action="modplace.do" method="post" focus="standortid">
<table>
    <tr>
    	<td>Standort</td>
    	<td><input name="standortid" type="text" size="50" maxlength="100" value="" /></td>
    </tr>
	<tr>
    	<td><br /></td>
    </tr>
    <tr>
      <td><input type="submit" value="speichern"></input></td>
    </tr>
     <tr>
    	<td><br /></td>
    </tr>
    <tr>
    	<logic:present name="holdingform" property="message"><td colspan="2"><div id="italic"><bean:write name="holdingform" property="message" /></div></td></logic:present>
    	<logic:notPresent name="holdingform" property="message"><td colspan="2"><br /></td></logic:notPresent>
    </tr>
    </table>
  <input name="method" type="hidden" value="listStandorte" />
  </html:form>

<logic:present name="holdingform" property="standorte">  
  <p>bestehende Standorte &auml;ndern</p>  
  <table border="1">
  <logic:iterate id="st" name="holdingform" property="standorte">
    <tr>
    	<td><a href="modplace.do?method=changeStandort&stid=<bean:write name="st" property="id" />">&auml;ndern</a></td>
      	<td> 			
     		<bean:write name="st" property="inhalt" />
		</td>
		<td><a href="modplace.do?method=changeStandort&stid=<bean:write name="st" property="id" />&del=true">l&ouml;schen</a></td>
    </tr>
   </logic:iterate>   
  </table>
</logic:present>
</logic:equal>

<logic:equal name="holdingform" property="mod" value="true">
<html:form action="modplace.do" method="post" focus="standortid">
<p>Standort &auml;ndern:</p>
<table>
	<logic:iterate id="st" name="holdingform" property="standorte">
    <tr>
      	<td> 			
     		<input name="standortid" type="text" size="50" maxlength="100" value="<bean:write name="st" property="inhalt" />" />
		</td>
    </tr>
   <input name="stid" type="hidden" value="<bean:write name="st" property="id" />" /> 
   </logic:iterate>
   <tr>
    	<td><br></td>
    </tr>
    <tr>
      	<td><div id="italic">Die &Auml;nderung betrifft auch alle bereits erfassten Best&auml;nde mit diesem Standort!</div></td>
    </tr>
   <tr>
    	<td><br></td>
    </tr>
    <tr>
      	<td><input type="submit" value="&auml;ndern"></input></td>
    </tr>
</table>
<input name="mod" type="hidden" value="true" />
<input name="method" type="hidden" value="changeStandort" />
</html:form>			
</logic:equal>
      				
</div>
</body>
</html>
