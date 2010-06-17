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
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="register.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">

<br />
<h3><bean:message key="register.header" /></h3>

  <html:form action="anmeldungkonto_.do" method="post" focus="biblioname">
  
  <font color=red>
  	<logic:present name="kontoform" property="message">
  		<bean:define id="em"><bean:write name="kontoform" property="message"/></bean:define>
  		<p><bean:message key="<%=em%>" /></p>
  	</logic:present>
  </font>
  
  <table>
    <tr>
    	<td><bean:message key="register.step1" /></td>
      	<td><bean:message key="register.library" />:</td>
    </tr>
    <tr>
      	<td><b><bean:message key="modifykonto.biblioname" />*</b></td>
      	<td><input name="biblioname" type="text"<logic:present name="kontoform" property="biblioname"> value="<bean:write name="kontoform" property="biblioname" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="biblioname" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="modifykonto.adress" />*</b></td>
      <td><input name="adresse" type="text"<logic:present name="kontoform" property="adresse"> value="<bean:write name="kontoform" property="adresse" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="adresse" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.adress_second" /></td>
      <td><input name="adressenzusatz" type="text"<logic:present name="kontoform" property="adressenzusatz"> value="<bean:write name="kontoform" property="adressenzusatz" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>    
    <tr>
      <td><b><bean:message key="modifykonto.plz" />*</b></td>
      <td><input name="PLZ" type="text"<logic:present name="kontoform" property="PLZ"> value="<bean:write name="kontoform" property="PLZ" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="plz" /></font></td>
    </tr>    
    <tr>
      <td><b><bean:message key="modifykonto.ort" />*</b></td>
      <td><input name="ort" type="text"<logic:present name="kontoform" property="ort"> value="<bean:write name="kontoform" property="ort" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="ort" /></font></td>
    </tr>    
    <tr>
      <td><b><bean:message key="modifykonto.land" />*</b></td>
      <td>
      	<select name="land">
      		<option value="0" selected><bean:message key="modifykonto.countrychoose" /></option>
 			<logic:iterate id="c" name="kontoform" property="countries">
				<bean:define id="tmp" name="c" property="countryname" type="java.lang.String"/>
     				<option value="<bean:write name="c" property="countryname" />"<logic:present name="kontoform" property="land"><logic:equal name="kontoform" property="land" value="<%=tmp%>"> selected</logic:equal></logic:present>><bean:write name="c" property="countryname" /></option>
   				</logic:iterate>
			</select>
      </td>
      <td><font color="red"><html:errors property="land" /></font></td>
    </tr>
        <tr>
          <td><b><bean:message key="modifykonto.timezone" /></b></td>
          <td>
          	<select name="timezone">
          		<bean:define id="tmp" name="kontoform" property="timezone" type="java.lang.String"/>
 				<logic:iterate id="tz" name="timezones">
     				<option value="<bean:write name="tz" />" <logic:equal name="tz" value="<%=tmp%>"> selected</logic:equal>><bean:write name="tz" /></option>
   				</logic:iterate>
			</select>
		  </td>      
    </tr>   
    <tr>
      <td><b><bean:message key="modifykonto.telefon" />*</b></td>
      <td><input name="telefon" type="text"<logic:present name="kontoform" property="telefon"> value="<bean:write name="kontoform" property="telefon" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="telefon" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.localfax" /></td>
      <td><input name="fax_extern" type="text"<logic:present name="kontoform" property="fax_extern"> value="<bean:write name="kontoform" property="fax_extern" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>   
    <tr>
      <td><b><bean:message key="modifykonto.libraryemail" />*</b></td>
      <td><input name="bibliotheksmail" type="text"<logic:notPresent name="kontoform" property="bibliotheksmail"> value="<bean:message key="register.bibliomail_explain" />"</logic:notPresent><logic:present name="kontoform" property="bibliotheksmail"> value="<bean:write name="kontoform" property="bibliotheksmail" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="email" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="modifykonto.email_delivery" />*</b></td>
      <td><input name="dbsmail" type="text"<logic:notPresent name="kontoform" property="dbsmail"> value="<bean:message key="register.dbsmail_explain" />"</logic:notPresent><logic:present name="kontoform" property="dbsmail"> value="<bean:write name="kontoform" property="dbsmail" />"</logic:present> size="50" maxlength="100" /> <font color=red><html:errors property="email" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.idgbv" /></td>
      <td><input name="gbvbenutzername" type="text"<logic:present name="kontoform" property="gbvbenutzername"> value="<bean:write name="kontoform" property="gbvbenutzername" />"</logic:present> size="50" maxlength="100" /> [<bean:message key="modifykonto.gbv_explain" />]</td>
    </tr>    
    <tr>
      <td><bean:message key="modifykonto.pwgbv" /></td>
      <td><input name="gbvpasswort" type="password"<logic:present name="kontoform" property="gbvpasswort"> value="<bean:write name="kontoform" property="gbvpasswort" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>
    <tr>
      <td><bean:message key="register.ezb_url" /></td>
      <td><input name="ezbid" type="text"<logic:notPresent name="kontoform" property="ezbid"> value="<bean:message key="register.ezb_url_comment" />"</logic:notPresent><logic:present name="kontoform" property="ezbid"> value="<bean:write name="kontoform" property="ezbid" />"</logic:present> size="50" maxlength="300" /> <bean:message key="register.ezb_url_explain" /></td>
    </tr>
    <tr title="<bean:message key="modifykonto.isil_explain" />">
      <td><bean:message key="modifykonto.isil" /><img src="img/info.png" alt="<bean:message key="modifykonto.isil_explain" />" /></td>
      <td><input name="isil" type="text"<logic:present name="kontoform" property="isil"> value="<bean:write name="kontoform" property="isil" />"</logic:present> size="50" maxlength="16" /> [z.B. DE-Bre14]</td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.checkzdb" />&nbsp;</td>
      <td>
      	<select name="zdb">
      		<option value="true"<logic:present name="kontoform" property="zdb"><logic:equal name="kontoform" property="zdb" value="true"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
      		<option value="false"<logic:present name="kontoform" property="zdb"><logic:equal name="kontoform" property="zdb" value="false"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
    	</select>
      </td>
    </tr>
    <tr>
    	<td>&nbsp;</td>
    	<td></td>
    </tr>
    <tr>
    	<td><b>*<bean:message key="modifykonto.required" /></b></td>
    </tr>
    <tr>
    	<td>&nbsp;</td>
    	<td></td>
    </tr>
    <tr>
      <td><bean:message key="register.account_type" /> (<a href="anmeldungtyp.do?activemenu=kontotyp" target="_blank"><bean:message key="modifykonto.list" /></a>)</td>
      <td><input type="radio" name="kontotyp" value="0" checked="checked" /><bean:message arg0="<%=appName%>" key="register.free" /></td>
    </tr>
    <tr>
      	<td></td>
      	<td><input type="radio" name="kontotyp" value="2" <logic:present name="kontoform" property="kontotyp"><logic:equal name="kontoform" property="kontotyp" value="2"> checked="checked"</logic:equal></logic:present> /><bean:message arg0="<%=appName%>" key="register.faxyear" /></td>
       </tr>
    <tr>
      	<td></td>
      	<td><input type="radio" name="kontotyp" value="3" <logic:present name="kontoform" property="kontotyp"><logic:equal name="kontoform" property="kontotyp" value="3"> checked="checked"</logic:equal></logic:present> /><bean:message arg0="<%=appName%>" key="register.fax3" /></td>
    </tr>
    <tr>
      	<td><br></td>
      	<td></td>
    </tr>
    <tr>
      	<td></td>
      	<td><bean:message key="register.fax_comment" /></td>
    </tr>
    <tr>        
    	<td><input type="submit" value="<bean:message key="register.submit_account" />" /></td>
    	<td></td>
    </tr>    
    <tr>        
    	<td colspan="2"><p /><bean:message key="register.fax_footnote" />
    	</td>
    </tr>

  </table>
 	 <input name="method" type="hidden" value="addNewKonto" />		
  </html:form>
  
  <tiles:insert page="import/footer.jsp" flush="true" />

</div>


 </body>
</html>
