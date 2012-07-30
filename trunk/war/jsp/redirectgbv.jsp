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
  <title><bean:message key="redirectgbv.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  
<bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
  
<script type="text/javascript">
  function popup() { 
  <bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
  window.open('<%=url%>','GBV');
    }
</script>
   
 </head>
 <tiles:insert page="import/header.jsp" flush="true" />
 <body onload="window.setTimeout('popup()',700)">
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
 <table style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
      </logic:notEqual>
  </tr>
</table>
 
 <div class="content">

 <br> <br> 

<logic:present name="userinfo" property="konto">
  <h3><bean:message key="redirectgbv.header" /></h3>
 
 <bean:message key="redirect.open_window" />
 
 <bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
 
 <p><a href="<%=url%>" target="_blank"><bean:message key="redirect.gbv_order" /></a></p>
 
<div id="italic">
 	<p>
     <logic:notPresent name="orderform" property="bid">
        <bean:message key="save.manuell" />: <a href="prepareJournalSave.do?method=prepareJournalSave&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel" />&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" /></logic:equal>&author=<bean:write name="orderform" property="author" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&prio=<bean:write name="orderform" property="prio" />&interne_bestellnr=<bean:write name="orderform" property="interne_bestellnr" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" />
      </logic:notPresent>
      <logic:present name="orderform" property="bid">
        <bean:message key="save.manuell" />: <a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="orderform" property="bid" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" />
      </logic:present>
 </p>
</div>
 
</logic:present>

<p></p>

  <table>
    <logic:notEmpty name="orderform" property="author">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.author" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="author" /></div></td>
  </tr>
  </logic:notEmpty>
<logic:equal name="orderform" property="mediatype" value="Artikel">
  <logic:notEmpty name="orderform" property="artikeltitel">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.artikeltitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="artikeltitel" /></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="zeitschriftentitel">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.zeitschrift" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="zeitschriftentitel"/></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="issn">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.issn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="issn" /></div></td>
  </tr>
  </logic:notEmpty>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">
  <logic:notEmpty name="orderform" property="kapitel">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.kapitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="kapitel" /></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="buchtitel">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="isbn">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="verlag">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="jahr">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
  </logic:notEmpty>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Buch">
  <logic:notEmpty name="orderform" property="buchtitel">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="isbn">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="verlag">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="jahr">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
  </logic:notEmpty>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Artikel">
<logic:notEmpty name="orderform" property="jahr">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
</logic:notEmpty>
  <logic:notEmpty name="orderform" property="jahrgang">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahrgang" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahrgang" /></div></td>
  </tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="heft">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.heft" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="heft" /></div></td>
  </tr>
  </logic:notEmpty>
</logic:equal>
<logic:notEqual name="orderform" property="mediatype" value="Buch">
<logic:notEmpty name="orderform" property="seiten">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.seiten" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="seiten" /></div></td>
  </tr>
</logic:notEmpty>
</logic:notEqual>
  <logic:notEmpty name="orderform" property="anmerkungen">
    <tr>
        <td><div id="italic"><bean:message key="bestellform.bemerkungen" />:&nbsp;</div></td>
        <td><div id="italic"><bean:write name="orderform" property="anmerkungen" /></div></td>
    </tr>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="notizen">
    <tr>
      <td><div id="italic"><bean:message key="bestellform.interne_notizen" />:&nbsp;</div></td>
      <td><div id="italic"><bean:write name="orderform" property="notizen" /></div></td>
    </tr>
    </logic:notEmpty>

     <tr><td><br></td></tr>
     </table>
 </div>
 </body>
</html>

