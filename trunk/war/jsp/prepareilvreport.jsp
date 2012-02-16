<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html> 

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="order.detailtitle" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" /> 
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

  <h3>
  <bean:message key="ilv-report.titleprepare" />
  </h3>
    
  <logic:present name="orderform" property="bestellung">
  
  <!-- Definiert ggf. ein Trennzeichen zwischen Institution und Abteilung -->
  <bean:define id="separator" value="" type="java.lang.String"/>
  <logic:notEmpty name="orderform" property="benutzer.institut"><logic:notEmpty name="orderform" property="benutzer.abteilung">
    <bean:define id="separator" value=" / " type="java.lang.String"/>
  </logic:notEmpty></logic:notEmpty>
  
  <form action="ilv-order-pdf.do" target="_blank">
  <logic:present name="userinfo" property="konto">
  <table border="1">
  <tr>
    <td valign="top">
    	<bean:message key="ilv-report.labelfrom" /><bean:write name="userinfo" property="konto.isil" /><br />
      	<bean:write name="userinfo" property="konto.bibliotheksname" />
    </td>
<%     
  OrderForm pageForm = (OrderForm) request.getAttribute("orderform");  
  String lieferant = "";
  if (pageForm.getBestellung().getLieferant() != null ) {  
    lieferant = pageForm.getBestellung().getLieferant().getName();  
    if (pageForm.getBestellung().getLieferant().getSigel() != null) {
      if (!pageForm.getBestellung().getLieferant().getSigel().equals("")) {
        lieferant = lieferant + "\040/\040" + pageForm.getBestellung().getLieferant().getSigel();
      }    
    }   
  }
%>
    <td>
    	<table>
    		<tr>
    			<td valign="top"><bean:message key="ilv-report.labelto" /></td>
    			<td><textarea name="lieferant" cols="30" rows="2"><%=lieferant%></textarea></td>
    		</tr>
    	</table>
    </td>
    <td valign="top"><bean:message key="stockimport.sig" />: <br />
      <input name="signatur" type="text" size="30" value="" />
    </td>
  </tr>
  <tr>
    <td colspan="2"><bean:message key="ilv-report.labeljournaltitel" /><br />
      <textarea name="journaltitel" cols="57" rows="3"><bean:write name="orderform" property="bestellung.zeitschrift" /></textarea>      
    </td>    
    <td rowspan="2" valign="top"><bean:message key="ilv-report.labelcustomer" /> <br />
      <input name="name" type="text" size="30" value="<bean:write name="orderform" property="benutzer.vorname" /> <bean:write name="orderform" property="benutzer.name" />" />
      <p />
      <bean:message key="ilv-report.labellibrarycard" /> <br />
      <input name="librarycard" type="text" size="30" value="<bean:write name="orderform" property="benutzer.librarycard" />" />
    </td>
  </tr>
  <tr>
    <td><bean:message key="ilv-report.labelissn" /><br /> 
      <input name="issn" type="text" size="30" value="<bean:write name="orderform" property="bestellung.issn" />" />
    </td>
    <td><bean:message key="ilv-report.labelpmid" /><br />
      <input name="pmid" type="text" size="30" value="<bean:write name="orderform" property="bestellung.pmid" />" />
    </td>
  </tr>
  <tr>
    <td colspan="2">
    <table border="0" width="100%">
      <tr>
        <td>
          <bean:message key="ilv-report.labelyear" /> <br />
          <input name="year" type="text" size="15" value="<bean:write name="orderform" property="bestellung.jahr" />" />
        </td>
        <td>
          <bean:message key="ilv-report.labelvolumevintage" /><br />
          <input name="volumevintage" type="text" size="15" value="<bean:write name="orderform" property="bestellung.jahrgang" />" /> 
        </td>
        <td><bean:message key="ilv-report.labelbooklet" /><br /> 
        <input name="booklet" type="text" size="15" value="<bean:write name="orderform" property="bestellung.heft" />" />
        </td>
      </tr>
    </table>    
    </td>
    <td rowspan="2" valign="top"><bean:message key="ilv-report.labelclinicinstitutedepartment" /><br />
      <input name="clinicinstitutedepartment" type="text" size="30" value="<bean:write name="orderform" property="benutzer.institut" /><%=separator%><bean:write name="orderform" property="benutzer.abteilung" />" />
      <br />      
      <bean:message key="ilv-report.labelphone" /><br /> 
      <input name="phone" type="text" size="30" value="<bean:write name="orderform" property="benutzer.telefonnrg" />" />      
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><bean:message key="ilv-report.labelpages" /><br />
      <input name="pages" type="text" size="75" value="<bean:write name="orderform" property="bestellung.seiten" />" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><bean:message key="ilv-report.labelauthorofessay" /><br />
      <input name="authorofessay" type="text" size="75" value="<bean:write name="orderform" property="bestellung.autor" />" />
    </td>
    <td rowspan="2" valign="top">
      <bean:message key="bestellform.telefon" />:<br />
      <bean:write name="userinfo" property="konto.telefon" /><br />
      <bean:message key="save.fax" />:<br />
      <bean:write name="userinfo" property="konto.fax_extern" /><br />
      
      <br />
      <bean:message key="ilv-report.sendto" /><br />
      <textarea name="post" cols="25" rows="6"><bean:write name="userinfo" property="konto.bibliotheksname" />
<bean:write name="userinfo" property="konto.adresse" />
<logic:notEmpty name="userinfo" property="konto.adressenzusatz"><bean:write name="userinfo" property="konto.adressenzusatz" /></logic:notEmpty>
<bean:write name="userinfo" property="konto.PLZ" /> <bean:write name="userinfo" property="konto.ort" />
<bean:write name="orderform" property="konto.land" /></textarea>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><bean:message key="ilv-report.labeltitleofessay" /><br />
      <textarea name="titleofessay" cols="57" rows="4"><bean:write name="orderform" property="bestellung.artikeltitel" /></textarea>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><bean:message key="ilv-report.labelendorsementsofdeliveringlibrary" />
      
    </td><td><bean:message key="ilv-report.labelnotesfromrequestinglibrary" /><br />      
      <textarea name="notesfromrequestinglibrary" cols="25" rows="3"><bean:write name="orderform" property="bestellung.systembemerkung" /></textarea>
    </td>
  </tr>
  </table>
  <p />
  
  <input type="hidden" name="method" value="ilv" />
  <input type="hidden" name="reporttitle" value="<bean:message key="ilv-report.title" />" />
  <input type="hidden" name="labelfrom" value="<bean:message key="ilv-report.labelfrom" />" />
  <input type="hidden" name="labelto" value="<bean:message key="ilv-report.labelto" />" />
  <input type="hidden" name="labelsignatur" value="<bean:message key="stockimport.sig" />" />
  <input type="hidden" name="labeljournaltitel" value="<bean:message key="ilv-report.labeljournaltitel" />" />
  <input type="hidden" name="labelcustomer" value="<bean:message key="ilv-report.labelcustomer" />" />
  <input type="hidden" name="labelname" value="<bean:message key="ilv-report.labelname" />" />
  <input type="hidden" name="labellibrarycard" value="<bean:message key="ilv-report.labellibrarycard" />" />
  <input type="hidden" name="labelissn" value="<bean:message key="ilv-report.labelissn" />" />
  <input type="hidden" name="labelpmid" value="<bean:message key="ilv-report.labelpmid" />" />
  <input type="hidden" name="labelyear" value="<bean:message key="ilv-report.labelyear" />" />
  <input type="hidden" name="labelvolumevintage" value="<bean:message key="ilv-report.labelvolumevintage" />" />
  <input type="hidden" name="labelbooklet" value="<bean:message key="ilv-report.labelbooklet" />" />
  <input type="hidden" name="labelclinicinstitutedepartment" value="<bean:message key="ilv-report.labelclinicinstitutedepartment" />" />
  <input type="hidden" name="labelphone" value="<bean:message key="ilv-report.labelphone" />" />
  <input type="hidden" name="labelfax" value="<bean:message key="ilv-report.labelfax" />" />
  <input type="hidden" name="labelsendto" value="<bean:message key="ilv-report.labelsendto" />" />
  <input type="hidden" name="labelpages" value="<bean:message key="ilv-report.labelpages" />" />
  <input type="hidden" name="labelauthorofessay" value="<bean:message key="ilv-report.labelauthorofessay" />" />
  <input type="hidden" name="labeltitleofessay" value="<bean:message key="ilv-report.labeltitleofessay" />" />
  <input type="hidden" name="labelendorsementsofdeliveringlibrary" value="<bean:message key="ilv-report.labelendorsementsofdeliveringlibrary" />" />
  <input type="hidden" name="labelnotesfromrequestinglibrary" value="<bean:message key="ilv-report.labelnotesfromrequestinglibrary" />" />
  <input type="submit" value="<bean:message key="ilv-report.submit" />" />
  
  </logic:present>
  </form>
  
   </logic:present>
     
    
 </div>


 </body>
</html> 
