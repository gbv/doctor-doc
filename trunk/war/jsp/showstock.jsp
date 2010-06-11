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
		<td id="submenuactive" nowrap title="Bestandes&uuml;bersicht"><a
			href="allstock.do?method=listBestand&activemenu=stock">&Uuml;bersicht</a></td>
		<td id="submenu" nowrap
			title="Neuen Bestand eingeben: Positivliste"><a
			href="stock.do?method=prepare&activemenu=stock">Bestand eingeben</a></td>
		<td id="submenu" nowrap
			title="Fehlendes vermerken: Negativliste"><a
			href="stock.do?method=prepare&submit=minus&activemenu=stock">L&uuml;cke eingeben</a></td>
		<td id="submenu" nowrap
			title="Standorte hinzuf&uuml;gen und verwalten"><a
			href="modplace.do?method=listStandorte&activemenu=stock">Standortverwaltung</a></td>
	</tr>
</table>
</logic:equal>
</logic:present>
 <br />
<h3>Bestandes&uuml;bersicht</h3>

<p>Under Construction!</p>
      				
</div>
</body>
</html>
