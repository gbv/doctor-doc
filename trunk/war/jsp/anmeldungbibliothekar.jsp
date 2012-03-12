<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html>

<html>

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

  <html:form action="anmeldungbibliothekar_.do" method="post">
  <table>
    <tr><td><bean:message key="register.step2" /></td><td><bean:message key="register.step2_text" /></td></tr>
    
  <logic:present name="errormessage" property="error">
    <bean:define id="em"><bean:write name="errormessage" property="error"/></bean:define>
    <tr><td colspan="2"><font color=red><bean:message key="<%=em%>" /></font></td></tr>
  </logic:present>    
        
    <tr>
      <td><bean:message key="adressen.anrede" /></td>
      <td><select name="anrede" size="1">
          <option value="Frau" ><bean:message key="adressen.anrede_frau" /></option>
          <option value="Herr" ><bean:message key="adressen.anrede_herr" /></option>
        </select>
        <font color="red"><html:errors property="anrede" /></font>
      </td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.vorname" />*</b></td><td><input name="vorname" type="text" required autofocus<logic:present name="userform" property="vorname"> value="<bean:write name="userform" property="vorname" />"</logic:present> size="50" maxlength="100" /><font color=red><html:errors property="vorname" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.name" />*</b></td><td><input name="name" type="text" required<logic:present name="userform" property="name"> value="<bean:write name="userform" property="name" />"</logic:present> size="50" maxlength="100" /><font color=red><html:errors property="name" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.email" />*</b> ( = Login-ID)&nbsp;</td><td><input name="email" type="email" required<logic:present name="userform" property="email"> value="<bean:write name="userform" property="email" />"</logic:present> size="50" maxlength="50" /><font color=red><html:errors property="email" /></font> <bean:message key="register.email_comment" /></td>
    </tr>   
    <tr>
      <td><bean:message key="adressen.telefon_g" /></td><td><input name="telefonnrg" type="tel"<logic:present name="userform" property="telefonnrg"> value="<bean:write name="userform" property="telefonnrg" />"</logic:present> size="30" maxlength="30" /></td>
    </tr>
    <tr>
      <td><b><bean:message arg0="<%=appName%>" key="register.dd_pw" />*</b>&nbsp;</td><td><html:password property="password" size="50" maxlength="50" /> <bean:message key="register.dd_pw_min" /></td>
    </tr>
    <tr>
      <td><br /></td>
    </tr>
    <tr>
      <td><b>*<bean:message key="modifykonto.required" /></b></td><td></td>
    </tr>    
    <tr>
      <td><br /><input type="submit" value="<bean:message key="register.submit" />" /></td>
    </tr>
  </table>
  
  	 <input name="institut" type="hidden" value="<logic:present name="kontoform" property="biblioname"><bean:write name="kontoform" property="biblioname" /></logic:present>" />
     <input name="adresse" type="hidden" value="<logic:present name="kontoform" property="adresse"><bean:write name="kontoform" property="adresse" /></logic:present>" />
     <input name="adresszusatz" type="hidden" value="<logic:present name="kontoform" property="adressenzusatz"><bean:write name="kontoform" property="adressenzusatz" /></logic:present>" />
     <input name="plz" type="hidden" value="<logic:present name="kontoform" property="PLZ"><bean:write name="kontoform" property="PLZ" /></logic:present>" />
     <input name="ort" type="hidden" value="<logic:present name="kontoform" property="ort"><bean:write name="kontoform" property="ort" /></logic:present>" />
     <input name="land" type="hidden" value="<logic:present name="kontoform" property="land"><bean:write name="kontoform" property="land" /></logic:present>" />
   	 <input name="method" type="hidden" value="addNewBibliothekar" />
    
  </html:form>
  
  <tiles:insert page="import/footer.jsp" flush="true" />
  
</div>


 </body>
</html>
