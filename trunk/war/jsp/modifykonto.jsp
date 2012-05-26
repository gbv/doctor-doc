<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="modifykonto.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
  <tr>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>
<h3><bean:message key="modifykonto.header" /></h3>

<html:form action="modifykonto_.do" method="post" focus="biblioname">
  
  <font color="red">
    <logic:present name="kontoform" property="message">
      <bean:define id="em"><bean:write name="kontoform" property="message"/></bean:define>
      <p><bean:message key="<%=em%>" /></p>
    </logic:present>
  </font>
   
<table>   
    <tr>
      <td><div id="italic"><bean:message key="modifykonto.accountdetails" /></div></td>
      <td></td>
    </tr>
     <tr>
       <td><bean:message key="modifykonto.biblioname" /></td><td><bean:write name="kontoform" property="biblioname" /></td>
     </tr>
        <tr>
           <td><bean:message key="modifykonto.status" /></td><td><logic:equal name="kontoform" property="kontostatus" value="false"><bean:message key="modifykonto.deaktiv" /></logic:equal><logic:equal name="kontoform" property="kontostatus" value="true"><bean:message key="modifykonto.aktiv" /></logic:equal>
           </td>
         </tr>
     
     <tr>
       <td><bean:message key="modifykonto.abonniert" /></td><td><bean:write name="kontoform" property="edatum" /></td>
     </tr>

   <tr>
      <td><bean:message key="modifykonto.kontotyp" /> (<a href="anmeldungtyp.do" target="_blank"><bean:message key="modifykonto.list" /></a>)</td>
      <td>
        <logic:equal name="kontoform" property="kontotyp" value="0"><bean:message arg0="<%=appName%>" key="modifykonto.free" /></logic:equal>
        <logic:equal name="kontoform" property="kontotyp" value="2"><bean:message arg0="<%=appName%>" key="modifykonto.faxyear" /></logic:equal>
        <logic:equal name="kontoform" property="kontotyp" value="3"><bean:message arg0="<%=appName%>" key="modifykonto.fax3" /></logic:equal>
      </td>
      </tr>
      
      <logic:present name="kontoform" property="faxno">
        <tr><td><bean:message key="modifykonto.faxnum" /></td><td><bean:write name="kontoform" property="faxno" /></td></tr>
      </logic:present>
   
      <logic:present name="kontoform" property="popfaxend">
        <tr><td><bean:message key="modifykonto.faxnumend" /></td><td><bean:write name="kontoform" property="popfaxend" /></td></tr>
       </logic:present>
    
    <tr><td><br></td></tr>
    
    <tr>
      <td><div id="italic"><bean:message key="modifykonto.adressdetails" /></div></td>
      <td></td>
    </tr>
    <tr>
      <td><b><bean:message key="modifykonto.biblioname" />*</b></td><td><input name="biblioname" type="text" required<logic:present name="kontoform" property="biblioname"> value="<bean:write name="kontoform" property="biblioname" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="biblioname" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="modifykonto.adress" />*</b></td><td><input name="adresse" type="text" required<logic:present name="kontoform" property="adresse"> value="<bean:write name="kontoform" property="adresse" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="adresse" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.adress_second" /></td><td><input name="adressenzusatz" type="text"<logic:present name="kontoform" property="adressenzusatz"> value="<bean:write name="kontoform" property="adressenzusatz" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>    
    <tr>
      <td><b><bean:message key="modifykonto.plz" />*</b></td><td><input name="PLZ" type="text" required<logic:present name="kontoform" property="PLZ"> value="<bean:write name="kontoform" property="PLZ" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="plz" /></font></td>
    </tr>    
    <tr>
      <td><b><bean:message key="modifykonto.ort" />*</b></td><td><input name="ort" type="text" required<logic:present name="kontoform" property="ort"> value="<bean:write name="kontoform" property="ort" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="ort" /></font></td>
    </tr>    
    <tr>
          <td><b><bean:message key="modifykonto.land" />*</b></td>
          <td>
            <select name="land">
            <option value="0" selected="selected"><bean:message key="modifykonto.countrychoose" /></option>
         <logic:iterate id="c" name="kontoform" property="countries">
        <bean:define id="tmp" name="c" property="countrycode" type="java.lang.String"/>
             <option value="<bean:write name="c" property="countrycode" />"<logic:present name="kontoform" property="land"><logic:equal name="kontoform" property="land" value="<%=tmp%>"> selected</logic:equal></logic:present>><bean:write name="c" property="countryname" /></option>
           </logic:iterate>
      </select>
      </td>  
      
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
      <td><b><bean:message key="modifykonto.telefon" />*</b></td><td><input name="telefon" type="tel" required<logic:present name="kontoform" property="telefon"> value="<bean:write name="kontoform" property="telefon" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="telefon" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.localfax" /></td><td><input name="fax_extern" type="tel"<logic:present name="kontoform" property="fax_extern"> value="<bean:write name="kontoform" property="fax_extern" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>
     <logic:present name="userinfo" property="benutzer">
   <logic:equal name="userinfo" property="benutzer.rechte" value="3">
    <tr>
      <td><bean:message key="modifykonto.ddfaxnum" /></td><td><input name="faxno" type="tel"<logic:present name="kontoform" property="faxno"> value="<bean:write name="kontoform" property="faxno" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="faxno" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.ddfaxid" /></td><td><input name="faxusername" type="text"<logic:present name="kontoform" property="faxusername"> value="<bean:write name="kontoform" property="faxusername" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="faxusername" /></font></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.ddfaxpw" /></td><td><input name="faxpassword" type="text"<logic:present name="kontoform" property="faxpassword"> value="<bean:write name="kontoform" property="faxpassword" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="faxpassword" /></font>
       <input name="kid" type="hidden" value="<bean:write name="kontoform" property="kid" />" /></td>
    </tr> 
    </logic:equal>
    </logic:present>
    <tr>
      <td><b><bean:message key="modifykonto.libraryemail" />*</b></td><td><input name="bibliotheksmail" type="email" required<logic:notPresent name="kontoform" property="bibliotheksmail"> value="<bean:message key="register.bibliomail_explain" />"</logic:notPresent><logic:present name="kontoform" property="bibliotheksmail"> value="<bean:write name="kontoform" property="bibliotheksmail" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="email" /></font></td>
    </tr>
    <tr>
      <td><b><bean:message key="modifykonto.email_delivery" />*</b></td><td><input name="dbsmail" type="email" required<logic:notPresent name="kontoform" property="dbsmail"> value="<bean:message key="register.dbsmail_explain" />"</logic:notPresent><logic:present name="kontoform" property="dbsmail"> value="<bean:write name="kontoform" property="dbsmail" />"</logic:present> size="50" maxlength="100" /> <font color="red"><html:errors property="email" /></font></td>
    </tr>
    <logic:notPresent name="userinfo" property="konto.faxno">
    <!-- Only relevant for customer without fax option -->
    <tr>
      <td><bean:message key="modifykonto.subito_standard" />&nbsp;</td><td><select name="default_deloptions">
          <option value="post"<logic:present name="kontoform" property="default_deloptions"><logic:equal name="kontoform" property="default_deloptions" value="post"> selected</logic:equal></logic:present>>Post</option>
          <option value="fax"<logic:present name="kontoform" property="default_deloptions"><logic:equal name="kontoform" property="default_deloptions" value="fax"> selected</logic:equal></logic:present>>Fax</option>
          </select>
      </td>
    </tr>
    </logic:notPresent>
    <tr title="<bean:message key="modifykonto.gbv_explain" />">
      <td><bean:message key="modifykonto.idgbv" /><img border="0" src="img/info.png" alt="<bean:message key="modifykonto.gbv_explain" />" /></td><td><input name="gbvbenutzername" type="text"<logic:present name="kontoform" property="gbvbenutzername"> value="<bean:write name="kontoform" property="gbvbenutzername" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>    
    <tr>
      <td><bean:message key="modifykonto.pwgbv" /></td><td><input name="gbvpasswort" type="password"<logic:present name="kontoform" property="gbvpasswort"> value="<bean:write name="kontoform" property="gbvpasswort" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.idids" /></td><td><input name="idsid" type="text"<logic:present name="kontoform" property="idsid"> value="<bean:write name="kontoform" property="idsid" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>    
    <tr>
      <td><bean:message key="modifykonto.pwids" /></td><td><input name="idspasswort" type="password"<logic:present name="kontoform" property="idspasswort"> value="<bean:write name="kontoform" property="idspasswort" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>
    <logic:present name="userinfo" property="benutzer">
   <logic:equal name="userinfo" property="benutzer.rechte" value="3">
   <tr>
      <td>GBV-Requester-ID</td><td><input name="gbvrequesterid" type="text"<logic:present name="kontoform" property="gbvrequesterid"> value="<bean:write name="kontoform" property="gbvrequesterid" />"</logic:present> size="50" maxlength="100" /></td>
    </tr>
   </logic:equal>
    </logic:present>
    <tr>
      <td><bean:message key="modifykonto.ezbid" /></td><td><input name="ezbid" type="text"<logic:notPresent name="kontoform" property="ezbid"> value="<bean:message key="register.ezb_url_comment" />"</logic:notPresent><logic:present name="kontoform" property="ezbid"> value="<bean:write name="kontoform" property="ezbid" />"</logic:present> size="50" maxlength="300" /></td>
    </tr>
    <tr title="<bean:message key="modifykonto.isil_explain" />">
      <td><bean:message key="modifykonto.isil" /><img src="img/info.png" alt="<bean:message key="modifykonto.isil_explain" />" /></td>
      <td><input name="isil" type="text"<logic:present name="kontoform" property="isil"> value="<bean:write name="kontoform" property="isil" />"</logic:present> size="50" maxlength="16" /> <bean:message key="modifykonto.isil_bp" /></td>
    </tr>
    <tr title="<bean:message key="modifykonto.instlogo_explain" />">
      <td><bean:message key="modifykonto.instlogo" /><img src="img/info.png" alt="<bean:message key="modifykonto.instlogo_explain" />" /></td>
      <td><input name="instlogolink" type="url"<logic:present name="kontoform" property="instlogolink"> value="<bean:write name="kontoform" property="instlogolink" />"</logic:present> size="50" maxlength="254" /> [jpg, jpeg, gif, png]</td>
    </tr>
    <tr>
      <td><bean:message key="modifykonto.checkzdb" />&nbsp;</td><td><select name="zdb">
          <option value="true"<logic:present name="kontoform" property="zdb"><logic:equal name="kontoform" property="zdb" value="true"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
          <option value="false"<logic:present name="kontoform" property="zdb"><logic:equal name="kontoform" property="zdb" value="false"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          </select>
      </td>
    </tr>
        <tr>
      <td></td>
    </tr>
    <tr>
      <td></td><td><b>*<bean:message key="modifykonto.required" /></b></td>
    </tr>
    
        <tr><td><br></td></tr>     
     
      <tr>
      <td><div id="italic"><bean:message key="modifykonto.authorization" /></div></td>
      <td></td>
    </tr>
     <tr>
       <td><bean:message key="modifykonto.login" /></td>
       <td><select name="userlogin">
            <option value="true"<logic:present name="kontoform" property="userlogin"><logic:equal name="kontoform" property="userlogin" value="true"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
            <option value="false"<logic:present name="kontoform" property="userlogin"><logic:equal name="kontoform" property="userlogin" value="false"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          </select> <i><font color="white"><bean:message key="modifykonto.note" /></font></i>
        </td>
     </tr>
     <tr>
       <td><bean:message key="modifykonto.subitoorder" /></td>
       <td><select name="userbestellung">
            <option value="true"<logic:present name="kontoform" property="userbestellung"><logic:equal name="kontoform" property="userbestellung" value="true"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
            <option value="false"<logic:present name="kontoform" property="userbestellung"><logic:equal name="kontoform" property="userbestellung" value="false"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          </select> <i><font color="white"><bean:message key="modifykonto.note" /></font></i>
        </td>
     </tr>
     <tr>
       <td><bean:message key="modifykonto.gbvorder" /></td>
       <td><select name="gbvbestellung">
            <option value="true"<logic:present name="kontoform" property="gbvbestellung"><logic:equal name="kontoform" property="gbvbestellung" value="true"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
            <option value="false"<logic:present name="kontoform" property="gbvbestellung"><logic:equal name="kontoform" property="gbvbestellung" value="false"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          </select> <i><font color="white"><bean:message key="modifykonto.note" /></font></i>
        </td>
     </tr>
     <tr>
       <td><bean:message key="modifykonto.limit" /></td>
       <td><select name="orderlimits">
            <option value="1"<logic:present name="kontoform" property="orderlimits"><logic:equal name="kontoform" property="orderlimits" value="1"> selected</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
            <option value="0"<logic:present name="kontoform" property="orderlimits"><logic:equal name="kontoform" property="orderlimits" value="0"> selected</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
          </select>
        </td>
     </tr>
     <!--    
     <tr>      
       <td>Max. / Kunde</td><td><input name="maxordersu" type="text"<logic:present name="kontoform" property="maxordersu"> value="<bean:write name="kontoform" property="maxordersu" />"</logic:present> size="5" maxlength="10"> <i><font color="white">0 = unbegrenzt </font></i><font color=red><html:errors property="maxordersu" /></font></td>
     </tr>
     -->
     <tr>
       <td><bean:message key="modifykonto.maxcustomyear" /></td><td><input name="maxordersutotal" type="number"<logic:present name="kontoform" property="maxordersutotal"> value="<bean:write name="kontoform" property="maxordersutotal" />"</logic:present> size="5" maxlength="10" /> <i><font color="white"><bean:message key="modifykonto.unlimited" /> </font></i> <font color="red"><html:errors property="maxordersutotal" /></font></td>
     </tr>
      <tr>
       <td><bean:message key="modifykonto.maxtotalyear" />&nbsp;</td><td><input name="maxordersj" type="number"<logic:present name="kontoform" property="maxordersj"> value="<bean:write name="kontoform" property="maxordersj" />"</logic:present> size="5" maxlength="10" /> <i><font color="white"><bean:message key="modifykonto.unlimited" /> </font></i> <font color="red"><html:errors property="maxordersj" /></font></td>
     </tr>

      
  </table>
  <logic:notEqual name="userinfo" property="konto.bibliotheksname" value="Demo-Bibliothek">    
      <br> <input type="submit" value="<bean:message key="modifykonto.save" />">
          <input name="method" type="hidden" value="modifyKonto" />
   </logic:notEqual>
   
   <p></p>
       
  </html:form>
</div>



 </body>
</html>
