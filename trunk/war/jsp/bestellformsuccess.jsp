<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="bestellformsuccess.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
<tiles:insert page="import/header.jsp" flush="true" />

<logic:present name="userinfo" property="benutzer.rechte">  
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">          
    <table style="position:absolute; text-align:left; left:111px; z-index:2;">
      <tr>
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
<logic:present name="userinfo" property="benutzer.userbestellung">  
  <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
  </logic:equal>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
</logic:present>
<logic:present name="userinfo" property="konto.gbvrequesterid">
<logic:present name="userinfo" property="konto.isil">
<logic:present name="userinfo" property="benutzer.gbvbestellung">
  <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:equal>
</logic:present>
</logic:present>
</logic:present>

      </tr>
</table>
  </logic:equal>
</logic:present>

<div class="content">

<br />
              
<h3><logic:present name="library"><bean:write name="library" /> - </logic:present><bean:message key="bestellformsuccess.header" />:</h3>

<p><bean:message key="bestellformsuccess.danke" /></p>
<p><bean:message key="bestellformsuccess.emailstart" /> <b><bean:write name="orderform" property="kundenmail" /></b> <bean:message key="bestellformsuccess.emailend" /></p>
<br />
<p><bean:message key="bestellformsuccess.neubestellung" /> <a href="bestellform.do?method=validate&amp;activemenu=bestellform<logic:present name="orderform" property="kkid">&kkid=<bean:write name="orderform" property="kkid" /></logic:present><logic:present name="orderform" property="bkid">&bkid=<bean:write name="orderform" property="bkid" /></logic:present>"><bean:message key="bestellformsuccess.back" /></a></p>

<table>

<logic:equal name="orderform" property="mediatype" value="journal">

<tr>
  <td><b><bean:message key="bestellform.artikelkopie" /></b></td>
</tr>
  <logic:present name="orderform" property="prio">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:message key="bestellform.urgent" /></td>
</tr>
  </logic:present>
  <logic:present name="orderform" property="deloptions">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:write name="orderform" property="deloptions" /></td>
</tr>
  </logic:present>
  <logic:notEmpty name="orderform" property="author">
<tr>
  <td><bean:message key="bestellform.author" />: </td><td><bean:write name="orderform" property="author" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="artikeltitel">
<tr>
  <td><bean:message key="bestellform.artikeltitel" />: </td><td><bean:write name="orderform" property="artikeltitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="zeitschriftentitel">
<tr>
  <td><bean:message key="bestellform.zeitschrift" />: </td><td><bean:write name="orderform" property="zeitschriftentitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="issn">
<tr>
  <td><bean:message key="bestellform.issn" />: </td><td><bean:write name="orderform" property="issn" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahr">
<tr>
  <td><bean:message key="bestellform.jahr" />: </td><td><bean:write name="orderform" property="jahr" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahrgang">
<tr>
  <td><bean:message key="bestellform.jahrgang" />: </td><td><bean:write name="orderform" property="jahrgang" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="heft">
<tr>
  <td><bean:message key="bestellform.heft" />: </td><td><bean:write name="orderform" property="heft" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="seiten">
<tr>
  <td><bean:message key="bestellform.seiten" />: </td><td><bean:write name="orderform" property="seiten" /></td>
</tr>
  </logic:notEmpty>
<logic:notEmpty name="orderform" property="doi">
<tr>
  <td>DOI: </td><td><bean:write name="orderform" property="doi" /></td>
</tr>
  </logic:notEmpty>
<logic:notEmpty name="orderform" property="pmid">
<tr>
  <td>PMID: </td><td><bean:write name="orderform" property="pmid" /></td>
</tr>
  </logic:notEmpty>
<tr>
  <td><br /></td>
</tr>
    <logic:notEmpty name="orderform" property="notizen">
<tr>
  <td><bean:message key="bestellform.anmerkungen" />: </td><td><bean:write name="orderform" property="notizen" /></td>
</tr>
  </logic:notEmpty>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="bookpart">
<tr>
  <td><b><bean:message key="bestellform.buchausschnitt" /></b></td>
</tr>
  <logic:present name="orderform" property="prio">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:message key="bestellform.urgent" /></td>
</tr>
  </logic:present>
  <logic:present name="orderform" property="deloptions">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:write name="orderform" property="deloptions" /></td>
</tr>
  </logic:present>
  <logic:notEmpty name="orderform" property="author">
<tr>
  <td><bean:message key="bestellform.author" />: </td><td><bean:write name="orderform" property="author" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="artikeltitel">
<tr>
  <td><bean:message key="bestellform.kapitel" />: </td><td><bean:write name="orderform" property="artikeltitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="zeitschriftentitel">
<tr>
  <td><bean:message key="bestellform.buchtitel" />: </td><td><bean:write name="orderform" property="zeitschriftentitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="verlag">
<tr>
  <td><bean:message key="bestellform.verlag" />: </td><td><bean:write name="orderform" property="verlag" /></td>
</tr>
  </logic:notEmpty>
<logic:notEmpty name="orderform" property="isbn">
<tr>
  <td><bean:message key="bestellform.isbn" />: </td><td><bean:write name="orderform" property="isbn" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahr">
<tr>
  <td><bean:message key="bestellform.jahr" />: </td><td><bean:write name="orderform" property="jahr" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="seiten">
<tr>
  <td><bean:message key="bestellform.seiten" />: </td><td><bean:write name="orderform" property="seiten" /></td>
</tr>
  </logic:notEmpty>
<tr>
  <td><br /></td>
</tr>
    <logic:notEmpty name="orderform" property="notizen">
<tr>
  <td><bean:message key="bestellform.anmerkungen" />: </td><td><bean:write name="orderform" property="notizen" /></td>
</tr>
  </logic:notEmpty>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="book">
<tr>
  <td><b><bean:message key="bestellform.buch" /></b></td>
</tr>
  <logic:present name="orderform" property="prio">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:message key="bestellform.urgent" /></td>
</tr>
  </logic:present>
  <logic:present name="orderform" property="deloptions">
<tr>
  <td><bean:message key="bestellform.bestellart" />: </td><td><bean:write name="orderform" property="deloptions" /></td>
</tr>
  </logic:present>
  <logic:notEmpty name="orderform" property="author">
<tr>
  <td><bean:message key="bestellform.author" />: </td><td><bean:write name="orderform" property="author" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="zeitschriftentitel">
<tr>
  <td><bean:message key="bestellform.buchtitel" />: </td><td><bean:write name="orderform" property="zeitschriftentitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="verlag">
<tr>
  <td><bean:message key="bestellform.verlag" />: </td><td><bean:write name="orderform" property="verlag" /></td>
</tr>
  </logic:notEmpty>
<logic:notEmpty name="orderform" property="isbn">
<tr>
  <td><bean:message key="bestellform.isbn" />: </td><td><bean:write name="orderform" property="isbn" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahr">
<tr>
  <td><bean:message key="bestellform.jahr" />: </td><td><bean:write name="orderform" property="jahr" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="seiten">
<tr>
  <td><bean:message key="bestellform.seiten" />: </td><td><bean:write name="orderform" property="seiten" /></td>
</tr>
  </logic:notEmpty>
<tr>
  <td><br /></td>
</tr>
    <logic:notEmpty name="orderform" property="notizen">
<tr>
  <td><bean:message key="bestellform.anmerkungen" />: </td><td><bean:write name="orderform" property="notizen" /></td>
</tr>
  </logic:notEmpty>
</logic:equal>

</table>

<tiles:insert page="import/footer.jsp" flush="true" />

</div>
 
 



 </body>
</html>
