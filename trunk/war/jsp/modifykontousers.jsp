<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="showkontousers.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">
 <br>
 
<logic:notPresent name="userform" property="delete"><h3><bean:message key="modifykontousers.add" /></h3></logic:notPresent>
  <logic:present name="userform" property="delete">
  <logic:equal name="userform" property="addFromBestellformEmail" value="false">
      <logic:equal name="userform" property="delete" value="true"><h3><bean:message key="modifykontousers.delete" /></h3></logic:equal>
      <logic:equal name="userform" property="delete" value="false"><h3><bean:message key="modifykontousers.modify" /></h3></logic:equal>
  </logic:equal>
  <logic:equal name="userform" property="addFromBestellformEmail" value="true">
      <h3><bean:message key="modifykontousers.add" /></h3>
  </logic:equal>
  </logic:present>
  
  <logic:present name="errormessage" property="error">
    <bean:define id="em"><bean:write name="errormessage" property="error"/></bean:define>
    <p><font color=red><bean:message key="<%=em%>" /></font></p>
  </logic:present>

  <html:form action="editkontousers1.do" method="post">
  <input name="gtc" type="hidden" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.gtc" />"</logic:present> />
  <input name="gtcdate" type="hidden" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.gtcdate" />"</logic:present> />
  <logic:present name="userform" property="user">
    <logic:notEmpty name="userform" property="bid">
      <input name="bid" type="hidden" value="<bean:write name="userform" property="bid" />" />
    </logic:notEmpty>
  </logic:present>
  <table>
    <tr>
     <td><b><bean:message key="modifykontousers.kontos" />*</b></td>
     <td><logic:iterate id="ui" name="userinfo" property="kontos">
           <input type="checkbox" name="kontos" value="<bean:write name="ui" property="id" />" 
           <logic:equal name="ui" property="selected" value="true"> checked="checked"</logic:equal> />
           <bean:write name="ui" property="bibliotheksname" />
         </logic:iterate>
         <font color=red><html:errors property="kontos" /></font>
       </td>
     </tr>
        
    <tr>
      <td><bean:message key="adressen.anrede" /></td>
      <td><select name="anrede" size="1">
          <option value="Frau" <logic:present name="userform" property="user.anrede"><logic:equal name="userform" property="user.anrede" value="Frau">selected</logic:equal></logic:present>><bean:message key="adressen.anrede_frau" /></option>
          <option value="Herr" <logic:present name="userform" property="user.anrede"><logic:equal name="userform" property="user.anrede" value="Herr">selected</logic:equal></logic:present>><bean:message key="adressen.anrede_herr" /></option>
        </select>
        <font color="red"><html:errors property="anrede" /></font>
      </td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.vorname" />*</b></td><td><input name="vorname" type="text" required autofocus size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.vorname" />"</logic:present> /><font color=red><html:errors property="vorname" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.name" />*</b></td><td><input name="name" type="text" required size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.name" />"</logic:present> /><font color=red><html:errors property="name" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="adressen.email" />*</b></td><td><input name="email" type="email" required size="50" maxlength="50" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.email" />"</logic:present> />
      <font color="red"><html:errors property="email" /></font></td>
    </tr>
        <tr>
      <td><bean:message key="modifykontousers.newpassword" /></td><td><input name="password" type="text" size="50" maxlength="50" /></td>
    </tr>   
    <tr>
      <td><bean:message key="showkontousers.telefon_g" /></td><td><input name="telefonnrg" type="tel" size="30" maxlength="30" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.telefonnrg" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="showkontousers.telefon_p" /></td><td><input name="telefonnrp" type="tel" size="30" maxlength="30" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.telefonnrp" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.institution" /></td><td><input name="institut" type="text" size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.institut" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.abteilung" /></td><td><input name="abteilung" type="text" size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.abteilung" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="modifykontousers.category" /></td>
      <td>
      	<select name="category">
      	<option value="0"><bean:message key="modifykontousers.choose" /></option>
      	<logic:present name="userform">
      		<bean:define id="tmp" name="userform" property="category" type="java.lang.String" />
      		<logic:iterate id="cat" name="categories">
         		<option value="<bean:write name="cat" property="id" />" <logic:equal name="cat" property="id" value="<%=tmp%>">selected</logic:equal>><bean:write name="cat" property="inhalt" /></option>
        	</logic:iterate>
        </logic:present>
        <logic:notPresent name="userform"> 	
      		<logic:iterate id="cat" name="categories">
         		<option value="<bean:write name="cat" property="id" />"><bean:write name="cat" property="inhalt" /></option>
        	</logic:iterate>
        </logic:notPresent>
        </select>
        <logic:present name="userinfo" property="benutzer">
     		<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        		<a href="usercategories.do?method=prepareCategories"><bean:message key="modifyusercategories.create" /></a>
        	</logic:notEqual>
        </logic:present>
      </td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.adress" /></td><td><input name="adresse" type="text" size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.adresse" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.adress_second" /></td><td><input name="adresszusatz" type="text" size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.adresszusatz" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.plz" /></td><td><input name="plz" type="text" size="4" maxlength="10" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.plz" />"</logic:present> /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.ort" /></td><td><input name="ort" type="text" size="50" maxlength="100" <logic:present name="userform" property="user">value="<bean:write name="userform" property="user.ort" />"</logic:present> /></td>
    </tr>
    <tr>    
          <td><b><bean:message key="bestellform.land" />*</b></td>
          <td>
            <select name="land">
            <option value="0"><bean:message key="select.countries" /></option>
         <logic:iterate id="c" name="userinfo" property="countries">
        <bean:define id="tmp" name="c" property="countrycode" type="java.lang.String"/>
             <option value="<bean:write name="c" property="countrycode" />"<logic:notPresent name="userform" property="user"><logic:equal name="userinfo" property="konto.land" value="<%=tmp%>"> selected</logic:equal></logic:notPresent><logic:present name="userform" property="user"><logic:equal name="userform" property="user.land" value="<%=tmp%>"> selected</logic:equal></logic:present>><bean:write name="c" property="countryname" /></option>
           </logic:iterate>
      </select>
      </td>      
    </tr>
     <logic:equal name="userinfo" property="keepordervalues2" value="true">
    <input name="keepordervalues2" type="hidden" value="true" />
    </logic:equal>
    
    <logic:present name="userinfo" property="benutzer">
     <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
    <logic:equal name="userinfo" property="konto.userlogin" value="true">
    <tr>
      <td><bean:message key="showkontousers.login" /></td>
      <td>   

      <select name="loginopt" size="1">
          <option value="0" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.loginopt" value="false">selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          <option value="1" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.loginopt" value="true">selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
        </select>
      </td>
    </tr>
    </logic:equal>
    <logic:equal name="userinfo" property="konto.userbestellung" value="true">
    <tr>
      <td><bean:message key="modifykontousers.subito" />&nbsp;</td>
      <td>
      <select name="userbestellung" size="1">
        <option value="false" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.userbestellung" value="false">selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          <option value="true" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.userbestellung" value="true">selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
        </select>
      </td>
    </tr>
    </logic:equal>
    <logic:equal name="userinfo" property="konto.gbvbestellung" value="true">
    <tr>
      <td><bean:message key="modifykontousers.gbv" />&nbsp;</td>
      <td>
      <select name="gbvbestellung" size="1">
        <option value="false" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.gbvbestellung" value="false">selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          <option value="true" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.gbvbestellung" value="true">selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
        </select>
      </td>
    </tr>
    </logic:equal>
    <tr>
      <td><bean:message key="modifykontousers.aktiv" /></td>
      <td>
      
    <select name="kontostatus" size="1">
          <option value="true" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.kontostatus" value="true">selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
          <option value="false" <logic:present name="userform" property="user"><logic:equal name="userform" property="user.kontostatus" value="false">selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
        </select> <font color="white"><bean:message key="modifykontousers.aktiv_comment" /></font>
        
      </td>
    </tr>
        </logic:notEqual>
        </logic:present>  
    <!-- <tr>
      <td>Rechnungsart</td>
      <td><select name="billing" size="1">Rechnung Funzt noch nicht!
          <option value="false" selected="selected">Keine Rechnungen???</option>
          <option value="true">Einzelrechnungen???</option>
        </select>
      </td>
    </tr> -->
    
    <logic:notEqual name="userinfo" property="konto.bibliotheksname" value="Demo-Bibliothek">
    
    <logic:present name="userform" property="delete">
    
      <tr>
      <logic:equal name="userform" property="delete" value="true">
        <td><br><bean:message key="modifykontousers.delete_confirm" /> <input type="submit" value="<bean:message key="modifykonto.yes" />" /></td>
          <input name="method" type="hidden" value="deleteKontousers" />
      </logic:equal>
      <logic:notEqual name="userform" property="delete" value="true">
        <td><br><input type="submit" value="sichern"></td>
        <td><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><br><bean:message key="modifykontousers.new_librarian" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/><bean:message key="modifykontousers.new_librarian.subject"/>"><bean:message key="modifykontousers.new_librarian.contact"/></a></logic:notEqual></td>
          <input name="method" type="hidden" value="modifykontousers" />
        </logic:notEqual>
        </tr>
        
    </logic:present>
    
    <logic:notPresent name="userform" property="delete">
    
      <tr>
        <logic:notEqual name="userinfo" property="keepordervalues2" value="true">
        	<td><br><input type="submit" value="<bean:message key="modifykontousers.save" />"/></td>
        	<td><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><br><bean:message key="modifykontousers.new_librarian" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/><bean:message key="modifykontousers.new_librarian.subject"/>"><bean:message key="modifykontousers.new_librarian.contact"/></a></logic:notEqual></td>
          	<input name="method" type="hidden" value="modifykontousers" />
        </logic:notEqual>
          
        <logic:equal name="userinfo" property="keepordervalues2" value="true">
        	<td><br><input type="submit" value="<bean:message key="modifykontousers.save_continue" />"></td>
        	<td><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><br><bean:message key="modifykontousers.new_librarian" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/><bean:message key="modifykontousers.new_librarian.subject"/>"><bean:message key="modifykontousers.new_librarian.contact"/></a></logic:notEqual></td>
          	<input name="method" type="hidden" value="modifykontousers" />
        </logic:equal>
          
        </tr>
        
    </logic:notPresent>
    
        </logic:notEqual>
    
  </table>
       
  </html:form>
  <p><b>*<bean:message key="modifykonto.required" /></b></p>
</div>
</body>
</html>
