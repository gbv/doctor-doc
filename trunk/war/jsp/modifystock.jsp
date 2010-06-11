<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="ch.dbs.form.*"%>
<%@ page import="ch.dbs.entity.*"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Bestandesverwaltung</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css">

<script language="javascript">

<!--
function more (a) {
if (a == 'true') {
document.getElementById('3').style.visibility = 'hidden';
document.getElementById('4').style.visibility = 'hidden';
}
else{
document.getElementById('3').style.visibility = 'visible';
document.getElementById('4').style.visibility = 'visible';
}

}

function hide()
{
document.getElementById('3').style. visibility = 'hidden';
document.getElementById('4').style. visibility = 'hidden';
}

//-->
</script>
  
 </head>
 
<body onload="hide()">
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">
<logic:present name="userinfo" property="benutzer">
			<logic:equal name="userinfo" property="benutzer.rechte" value="3">
<table
	style="position:absolute; text-align:left; left:<bean:message key="submenupos.stock" />px; z-index:2;">
	<tr>
		<td id="submenu" nowrap title="Bestandes&uuml;bersicht"><a
			href="allstock.do?method=listBestand&activemenu=stock">&Uuml;bersicht</a></td>
		<td <logic:notEqual name="holdingform" property="submit" value="minus">id="submenuactive" nowrap</logic:notEqual><logic:equal name="holdingform" property="submit" value="minus">id="submenu" nowrap</logic:equal>
			title="Neuen Bestand eingeben: Positivliste"><a
			href="stock.do?method=prepare&activemenu=stock">Bestand eingeben</a></td>
		<td <logic:notEqual name="holdingform" property="submit" value="minus">id="submenu" nowrap</logic:notEqual><logic:equal name="holdingform" property="submit" value="minus">id="submenuactive" nowrap</logic:equal>
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
<logic:notEqual name="holdingform" property="submit" value="minus">
	<h3>Bestand eingeben - Positivliste</h3>
	Vorhandener lokaler Bestand
</logic:notEqual>
<logic:equal name="holdingform" property="submit" value="minus">
	<h3>Bestand eingeben - Negativliste</h3>
	Bestandesl&uuml;cke
</logic:equal>

<table>
	<tr>
    	<td><br></td>
    </tr>
    
    <table>
    <html:form action="findissn.do" method="post" focus="issn">
    <tr>
    	<td>Titel</td>
    	<td colspan="3"><input name="zeitschriftentitel" type="text" size="33" maxlength="100"></td>
    </tr>
    <tr>
      <td colspan="3"><input type="submit" value="ISSN suchen"></input></td>
    </tr>
    <input name="fromstock" type="hidden" value="true">
    <input name="autocomplete" type="hidden" value="true">
    <input name="method" type="hidden" value="issnAssistent">
    <logic:equal name="holdingform" property="submit" value="minus"><input name="submit" type="hidden" value="minus"></logic:equal>
  	</html:form>
    
    <tr>
    	<td><br></td>
    </tr>
    	<logic:present name="holdingform" property="message">
	<tr>
    	<td colspan="2"><div id="italic"><bean:write name="holdingform" property="message" /></div></td>
    </tr>
    </logic:present>
    
    <html:form action="savestock.do" method="post" focus="issn"> 
    <tr>
    	<td colspan="2">Identifikation</td>
    </tr>
	<tr>
		<td><select name="identifierdescription" ><option value="issn" selected>ISSN</option><option value="zdbid">ZDB-ID</option></select></td>
		<td><input name="identifier" type="text" size="9" maxlength="10" value="<bean:write name="holdingform" property="identifier" />"></td>
	</tr>
	<tr>
	<td>Zeitschrift</td>
    	<td colspan="3"><input name="zeitschrift" type="text" size="100" maxlength="200" value="<bean:write name="holdingform" property="zeitschrift" />"></td>
    </tr>
	<tr>
    	<td><br></td>
    </tr>
    <tr>
    <table>
    	<tr>
    	<td>Anzeige</td>
    	<td><input type="radio" name="morefields" value="true" checked="checked" OnClick="more(this.value)">einfach</td>
    	<td><br></td>
    	<td><input type="radio" name="morefields" value="false" OnClick="more(this.value)">erweitert</td>
    </tr>
	<tr>
		<td>Startjahr</td>
		<td><input name="startyear" type="text" size="9" maxlength="4" value="<bean:write name="holdingform" property="startyear" />"></td>
		<td>Endjahr</td>
		<td><input name="endyear" type="text" size="9" maxlength="4" value="<bean:write name="holdingform" property="endyear" />"></td>
	</tr>
	<tr id="3">
		<td>Startvolume</td>
		<td><input name="startvolume" type="text" size="9" maxlength="20" value="<bean:write name="holdingform" property="startvolume" />"></td>
		<td>Endvolume</td>
		<td><input name="endvolume" type="text" size="9" maxlength="20" value="<bean:write name="holdingform" property="endvolume" />"></td>
	</tr>
	<tr id="4">
		<td>Startheft</td>
		<td><input name="startissue" type="text" size="9" maxlength="20" value="<bean:write name="holdingform" property="startissue" />"></td>
		<td>Endheft</td>
		<td><input name="endissue" type="text" size="9" maxlength="20" value="<bean:write name="holdingform" property="endissue" />"></td>
	</tr>
	</table>
	</tr>
	
	</table>
		<tr>
    	<td><br></td>
    </tr>
	<tr>
    	<td colspan="2">
    		<table>
    			<tr>
    				<td><input type="radio" name="eissue" value="false" checked="checked">Print</td>
    				<td><input type="radio" name="eissue" value="true">elektronisch</td>
    			</tr>
    			<tr>
    				<td><input type="radio" name="suppl" value="0">lokal ohne Suppl.</td>
    				<td><input type="radio" name="suppl" value="1" checked="checked">ggf. mit Suppl.</td>
    				<td><input type="radio" name="suppl" value="2">nur Suppl.</td>
    			</tr>
    		</table>
    	</td>
    </tr>
    <tr>
    	<td><br></td>
    </tr>
    <table>
    <tr>
    	<td>Standort</td>
      	<td><select name="standortid">
      		<logic:present name="holdingform" property="standorte">
      		<bean:define id="tmp"><bean:write name="userinfo" property="defaultstandortid"/></bean:define>
 				<logic:iterate id="st" name="holdingform" property="standorte">
     				<option value="<bean:write name="st" property="id" />"<logic:equal name="st" property="id" value="<%=tmp%>"> selected</logic:equal>><bean:write name="st" property="inhalt" /></option>
   				</logic:iterate>
   			</logic:present>
			</select>
		 </td>
		 <td>&nbsp;<input type="checkbox" name="standardplace" value="true"> diesen Standort als Standard w&auml;hlen</td>
    </tr>
	<tr>
		<td>Gestell / Notation&nbsp;</td>
		<td><input name="shelfmark" type="text" size="20" maxlength="100" value="<bean:write name="holdingform" property="shelfmark" />"></td>
	</tr>
	<tr>
    	<td><br></td>
    </tr> 
    <tr>
      <td><input type="submit" value="speichern"></input></td>
    </tr>
    <tr>
    	<td><br></td>
    </tr>
    </table>
  <logic:equal name="holdingform" property="submit" value="minus"><input name="submit" type="hidden" value="minus"></logic:equal> 
  <input name="method" type="hidden" value="save"> 
  </html:form>
	
	
</table>

</body>
</html>
