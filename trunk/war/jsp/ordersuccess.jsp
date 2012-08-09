<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="ordersuccess.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">
 <br>

<logic:notPresent name="errormessage" property="error">
<logic:present name="orderform" property="manuell">
<logic:equal name="orderform" property="manuell" value="false">
   <h3><logic:present name="userinfo" property="konto"><bean:write name="userinfo" property="konto.bibliotheksname" /></logic:present></h3>
     <div id="italic"><bean:message key="ordersuccess.confirmation" /></div>
     <p></p>
</logic:equal>
</logic:present>
</logic:notPresent>

<logic:equal name="orderform" property="submit" value="GBV">
 <!-- GBV -->
 
<logic:notPresent name="errormessage" property="error">
 <table>
  
<tr>
  <td><bean:message key="ordersuccess.ordernumber" />:&nbsp;</td><td><bean:write name="orderform" property="gbvnr" /></td>
</tr>
<tr>
  <td><bean:message key="stats.medientyp" />:&nbsp;</td><td><bean:write name="orderform" property="mediatype" /></td>
</tr>
  <logic:notEmpty name="orderform" property="author">
<tr>
  <td><bean:message key="bestellform.author" />:&nbsp;</td><td><bean:write name="orderform" property="author" /></td>
</tr>
  </logic:notEmpty>
  <logic:notEmpty name="orderform" property="kapitel">
<tr>
  <td><bean:message key="bestellform.kapitel" />:&nbsp;</td><td><bean:write name="orderform" property="kapitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="buchtitel">
<tr>
  <td><bean:message key="bestellform.buchtitel" />:&nbsp;</td><td><bean:write name="orderform" property="buchtitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="artikeltitel">
<tr>
  <td> <bean:message key="bestellform.artikeltitel" />:&nbsp;</td><td><bean:write name="orderform" property="artikeltitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="zeitschriftentitel">
<tr>
  <td> <bean:message key="bestellform.zeitschrift" />:&nbsp;</td><td><bean:write name="orderform" property="zeitschriftentitel" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="issn">
<tr>
  <td><bean:message key="bestellform.issn" />:&nbsp;</td><td><bean:write name="orderform" property="issn" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="isbn">
<tr>
  <td><bean:message key="bestellform.isbn" />:&nbsp;</td><td><bean:write name="orderform" property="isbn" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="verlag">
<tr>
  <td><bean:message key="bestellform.verlag" />:&nbsp;</td><td><bean:write name="orderform" property="verlag" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahr">
<tr>
  <td><bean:message key="bestellform.jahr" />:&nbsp;</td><td><bean:write name="orderform" property="jahr" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="jahrgang">
<tr>
  <td><bean:message key="bestellform.jahrgang" />:&nbsp;</td><td><bean:write name="orderform" property="jahrgang" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="heft">
<tr>
  <td><bean:message key="bestellform.heft" />:&nbsp;</td><td><bean:write name="orderform" property="heft" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="seiten">
<tr>
  <td><bean:message key="bestellform.seiten" />:&nbsp;</td><td><bean:write name="orderform" property="seiten" /></td>
</tr>
  </logic:notEmpty>
<tr>
  <td><br></td>
</tr>
    <logic:notEmpty name="orderform" property="anmerkungen">
<tr>
  <td><bean:message key="bestellform.bemerkungen" />:&nbsp;</td><td><bean:write name="orderform" property="anmerkungen" /></td>
</tr>
  </logic:notEmpty>
    <logic:notEmpty name="orderform" property="notizen">
<tr>
  <td><bean:message key="bestellform.interne_notizen" />:&nbsp;</td><td><bean:write name="orderform" property="notizen" /></td>
</tr>
  </logic:notEmpty>
</table>
</logic:notPresent>

<logic:present name="errormessage" property="error">
  <h3><bean:message key="ordersuccess.error" /></h3>
  <bean:define id="em"><bean:write name="errormessage" property="error"/></bean:define>
   <div id="italic"><bean:message key="<%=em%>" /></div>
  
  <logic:present name="errormessage" property="error_specific">
    <p><bean:message key="ordersuccess.error_reason" />: <bean:write name="errormessage" property="error_specific" /></p>
  </logic:present>
   
   <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
    <bean:message key="ordersuccess.gbv_manuell" />: <a href="<bean:write name="orderform" property="link" />" target="_blank"><bean:message key="ordersuccess.click" /></a>
  <div id="italic">
    <p>
      <bean:message key="save.manuell" />: <a href="prepareJournalSave.do?method=prepareJournalSave&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel_encodedUTF8" />&buchtitel=<bean:write name="orderform" property="buchtitel_encodedUTF8" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag_encodedUTF8" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel_encodedUTF8" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag_encodedUTF8" /></logic:equal>&author=<bean:write name="orderform" property="author_encodedUTF8" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&prio=<bean:write name="orderform" property="prio" />&interne_bestellnr=<bean:write name="orderform" property="interne_bestellnr" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" />
    </p>
   </div>
  </logic:notEqual>
  
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
    <bean:message key="ordersuccess.contact" />
  </logic:equal>
  
</logic:present>

</logic:equal>

</div>

 </body>
</html>
