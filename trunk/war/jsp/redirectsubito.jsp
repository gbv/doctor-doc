<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message key="redirect.subito_titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css">
  
<script language="javascript" type="text/javascript">
	function popup() { 
	<bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
	window.open('<%=url%>','Subito');
		}
</script>
   
 </head>
 <tiles:insert page="import/header.jsp" flush="true" />
 <body onload="window.setTimeout('popup()',1000)">
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
 <table style="position:absolute; text-align:left; left:111px; z-index:2;">
	<tr>
		<td id="submenu" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
		<td id="submenu" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
	<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">	
		<td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive"</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu"</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		<logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive"</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu"</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
	</logic:notEqual>
	<logic:equal name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
		<logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
				<td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive"</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu"</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		</logic:equal>
		<logic:present name="userinfo" property="konto.gbvrequesterid">
		<logic:present name="userinfo" property="konto.isil">
		<logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
				<logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive"</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu"</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
		</logic:equal>
		</logic:present>
		</logic:present>
	</logic:equal>
			<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
			</logic:notEqual>
	</tr>
</table>
 
 <div class="content">

 <br /> <br /> 

<logic:present name="userinfo" property="konto">
  <h3><bean:message key="redirect.subito_header" /></h3>
 
 <bean:message key="redirect.open_window" />
 
 <bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
 
 <p><a href="<%=url%>" target="_blank" onclick=FensterOeffnen('this.href')><bean:message key="redirect.subito_order" /></a></p>
 
 <p><bean:message key="redirect.subito_tip" /></p>
 
 <p>
 	<div id="italic">
    <bean:message key="save.manuell" />: <a href="prepareJournalSave.do?method=prepareJournalSave&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel_encoded" />&buchtitel=<bean:write name="orderform" property="buchtitel_encoded" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag_encoded" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel_encoded" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag_encoded" /></logic:equal>&author=<bean:write name="orderform" property="author_encoded" />&status=bestellt&lid=32&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&prio=<bean:write name="orderform" property="prio" />&interne_bestellnr=<bean:write name="orderform" property="interne_bestellnr" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" />
    </div>
	</p>
 
</logic:present>

<p></p>

  <table>
    <logic:notEqual name="orderform" property="author" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.author" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="author" /></div></td>
	</tr>
	</logic:notEqual>
<logic:equal name="orderform" property="mediatype" value="Artikel">
	<logic:notEqual name="orderform" property="artikeltitel" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.artikeltitel" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="artikeltitel" /></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="zeitschriftentitel" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.zeitschrift" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="zeitschriftentitel"/></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="issn" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.issn" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="issn" /></div></td>
	</tr>
	</logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">
	<logic:notEqual name="orderform" property="kapitel" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.kapitel" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="kapitel" /></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="buchtitel" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="isbn" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="verlag" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="jahr" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
	</tr>
	</logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Buch">
	<logic:notEqual name="orderform" property="buchtitel" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="isbn" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="verlag" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="jahr" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
	</tr>
	</logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Artikel">
<logic:notEqual name="orderform" property="jahr" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
	</tr>
</logic:notEqual>
	<logic:notEqual name="orderform" property="jahrgang" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.jahrgang" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="jahrgang" /></div></td>
	</tr>
	</logic:notEqual>
	<logic:notEqual name="orderform" property="heft" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.heft" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="heft" /></div></td>
	</tr>
	</logic:notEqual>
</logic:equal>
<logic:notEqual name="orderform" property="mediatype" value="Buch">
<logic:notEqual name="orderform" property="seiten" value="">
	<tr>
		<td><div id="italic"><bean:message key="bestellform.seiten" />:&nbsp;</div></td>
		<td><div id="italic"><bean:write name="orderform" property="seiten" /></div></td>
	</tr>
</logic:notEqual>
</logic:notEqual>
	<logic:notEqual name="orderform" property="anmerkungen" value="">
    <tr>
      	<td><div id="italic"><bean:message key="bestellform.bemerkungen" />:&nbsp;</div></td>
      	<td><div id="italic"><bean:write name="orderform" property="anmerkungen" /></div></td>
    </tr>
    </logic:notEqual>
    <logic:notEqual name="orderform" property="notizen" value="">
    <tr>
      <td><div id="italic"><bean:message key="bestellform.interne_notizen" />:&nbsp;</div></td>
      <td><div id="italic"><bean:write name="orderform" property="notizen" /></div></td>
    </tr>
    </logic:notEqual>

     <tr><td><br></td></tr>
     </table>
 
 </body>
</html>

