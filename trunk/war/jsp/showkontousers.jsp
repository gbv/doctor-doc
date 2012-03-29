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
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="showkontousers.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" /> 
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">

<logic:present name="userinfo" property="konto">
 <br /> 
 <h3><bean:message key="showkontousers.header" /></h3>

<a href="addkontousers.do?method=prepareAddUser"><img border="0" src="img/newuser.png" alt="<bean:message key="showkontousers.info_new" />" title="<bean:message key="showkontousers.info_new" />"/></a>
<table class="border">
  <tr>
    <th id="th-center" colspan="2" >&nbsp;</th><th id="th-center"><bean:message key="adressen.anrede" /></th><th id="th-center"><bean:message key="adressen.vorname" /></th><th id="th-center"><bean:message key="adressen.name" /></th><th id="th-center"><bean:message key="adressen.email" /></th><th id="th-center"><bean:message key="showkontousers.typ" /></th><logic:equal name="userinfo" property="konto.userlogin" value="true"><th id="th-center"><bean:message key="showkontousers.login" /></th></logic:equal><logic:equal name="userinfo" property="konto.userbestellung" value="true"><th id="th-center"><bean:message key="showkontousers.subito" /></th></logic:equal><logic:equal name="userinfo" property="konto.gbvbestellung" value="true"><th id="th-center"><bean:message key="showkontousers.gbv" /></th></logic:equal><th id="th-center"><bean:message key="showkontousers.aktiv" /></th><th id="th-center"><bean:message key="showkontousers.telefon_g" /></th><th id="th-center"><bean:message key="showkontousers.telefon_p" /></th><th id="th-center"><bean:message key="bestellform.institution" /></th><th id="th-center"><bean:message key="bestellform.abteilung" /></th><th id="th-center"><a href="usercategories.do?method=prepareCategories"><bean:message key="modifykontousers.category" /></a></th><th id="th-center"><bean:message key="modifykonto.adress" /></th><th id="th-center"><bean:message key="modifykonto.adress_second" /></th><th id="th-center"><bean:message key="modifykonto.plz" /></th><th id="th-center"><bean:message key="modifykonto.ort" /></th>
  </tr>
 <logic:present name="userform" property="users">
   <logic:iterate id="u" name="userform" property="users">
  <tr>
      <td id="border"><a href="edituserdetail.do?method=changeuserdetails&bid=<bean:write name="u" property="id" />"><img border="0" src="img/edit.png" alt="<bean:message key="showkontousers.info_edit" />" title="<bean:message key="showkontousers.info_edit" />"/></a></td>
      <td id="border"><a href="edituserdetail.do?method=changeuserdetails&bid=<bean:write name="u" property="id" />&delete=true"> <img border="0" src="img/drop.png" alt="<bean:message key="showkontousers.info_delete" />" title="<bean:message key="showkontousers.info_delete" />"/></a></td>
      <td id="border"><bean:write name="u" property="anrede" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="vorname" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="name" />&nbsp;</td>
      <td id="border"><a href=mailto:<bean:write name="u" property="email" />><bean:write name="u" property="email" /></a>&nbsp;</td>
      <td id="border" align="center"><i><font color="white"><logic:equal name="u" property="rechte" value="1">User</logic:equal><logic:equal name="u" property="rechte" value="2"><logic:equal name="u" property="anrede" value="Herr"><bean:message key="showkontousers.bibliothekar" /></logic:equal><logic:equal name="u" property="anrede" value="Frau"><bean:message key="showkontousers.bibliothekarin" /></logic:equal></logic:equal><logic:equal name="u" property="rechte" value="3">Admin</logic:equal>&nbsp;</font></i></td>
      <logic:equal name="userinfo" property="konto.userlogin" value="true">
      <td id="border" align="center"><logic:equal name="u" property="loginopt" value="true"><i><font color="white"><bean:message key="modifykonto.yes" /></font></i></logic:equal><logic:equal name="u" property="loginopt" value="false"><i><font color="red"><bean:message key="modifykonto.no" /></font></i></logic:equal>&nbsp;</td>
      </logic:equal>
      <logic:equal name="userinfo" property="konto.userbestellung" value="true">
      <td id="border" align="center"><logic:equal name="u" property="userbestellung" value="true"><i><font color="white"><bean:message key="modifykonto.yes" /></font></i></logic:equal><logic:equal name="u" property="userbestellung" value="false"><i><font color="red"><bean:message key="modifykonto.no" /></font></i></logic:equal>&nbsp;</td>
      </logic:equal>
      <logic:equal name="userinfo" property="konto.gbvbestellung" value="true">
      <td id="border" align="center"><logic:equal name="u" property="gbvbestellung" value="true"><i><font color="white"><bean:message key="modifykonto.yes" /></font></i></logic:equal><logic:equal name="u" property="gbvbestellung" value="false"><i><font color="red"><bean:message key="modifykonto.no" /></font></i></logic:equal>&nbsp;</td>
      </logic:equal>
      <td id="border" align="center"><logic:equal name="u" property="kontostatus" value="true"><i><font color="white"><bean:message key="modifykonto.yes" /></font></i></logic:equal><logic:equal name="u" property="kontostatus" value="false"><i><font color="red"><bean:message key="modifykonto.no" /></font></i></logic:equal>&nbsp;</td>
      <td id="border" nowrap="nowrap"><bean:write name="u" property="telefonnrg" />&nbsp;</td>
      <td id="border" nowrap="nowrap"><bean:write name="u" property="telefonnrp" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="institut" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="abteilung" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="category.inhalt" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="adresse" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="adresszusatz" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="plz" />&nbsp;</td>
      <td id="border"><bean:write name="u" property="ort" />&nbsp;</td>
  </tr>
   </logic:iterate>
</logic:present>

</table> 
</logic:present>

<p></p>

<logic:notPresent name="userinfo" property="konto">
  <bean:message key="error.kontos" />!
</logic:notPresent>

</div>
 </body>
</html>
