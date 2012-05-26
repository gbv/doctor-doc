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
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="bestellformconfigureselect.header" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>
<h3>
  <logic:equal name="bestellform" property="tyid" value="9"><bean:message key="bestellformconfigureselect.cf_ipbased" /></logic:equal>
  <logic:equal name="bestellform" property="tyid" value="11"><bean:message key="bestellformconfigureselect.cf_bkid" /></logic:equal>
  <logic:equal name="bestellform" property="tyid" value="12"><bean:message key="bestellformconfigureselect.cf_kkid" /></logic:equal>
  <logic:equal name="bestellform" property="tyid" value="13"><bean:message key="bestellformconfigureselect.cf_eingeloggt" /></logic:equal>
</h3>

<logic:present name="message" property="message">
  <bean:define id="missingKey" name="message" property="message" type="java.lang.String"/>
  <p><font color="red"><bean:message key="<%=missingKey%>" /></font></p>
</logic:present>

<html:form action="bfconfigure.do" method="post">

<h4><bean:message key="bestellform.deactivate" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.bestellformular" /></th>
    <th id="th-left"><bean:message key="bestellform.deactivate" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.bestellformular" />&nbsp;</td>
    <td id="border" align="center"><input type="checkbox" name="deactivated" <logic:equal name="bestellform" property="deactivated" value="true">checked="checked"</logic:equal> /></td>
  </tr>
</table>
<p><bean:message key="bestellform.deactivate_explain" /></p>
<p><br></p><hr>


<h4><bean:message key="bestellform.saveorder" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="order.title" /></th>
    <th id="th-left"><bean:message key="bestellform.save" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="order.title" />&nbsp;</td>
    <td id="border" align="center"><input type="checkbox" name="saveorder" <logic:equal name="bestellform" property="saveorder" value="true">checked="checked"</logic:equal> /></td>
  </tr>
</table>
<p><bean:message key="bestellform.saveorder_explain" /></p>
<p><br></p><hr>

<h4><bean:message key="bestellform.overwritedelivery" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.bestellart" /></th>
    <th id="th-left"><bean:message key="bestellform.content" /></th>
   </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value1" />&nbsp;</td>
    <td id="border"><input name="lieferart_value1"
        value="<bean:write name="bestellform" property="lieferart_value1" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value2" />&nbsp;</td>
    <td id="border"><input name="lieferart_value2"
        value="<bean:write name="bestellform" property="lieferart_value2" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value3" />&nbsp;</td>
    <td id="border"><input name="lieferart_value3"
        value="<bean:write name="bestellform" property="lieferart_value3" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
</table>
<p><bean:message key="bestellform.overwritedelivery_explain" /> "<bean:message key="bestellform.post" /> / <bean:message key="bestellform.pdf" />"</p>
<p><br></p><hr>

<h4><bean:message key="bestellform.prioselect" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.prio" /></th>
    <th id="th-left"><bean:message key="bestellform.deactivate" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.prio" />&nbsp;</td>
    <td id="border" align="center"><input type="checkbox" name="prio" <logic:equal name="bestellform" property="prio" value="true">checked="checked"</logic:equal> /></td>
  </tr>
</table>
<p><bean:message key="bestellform.prioselect_explain" /> "<bean:message key="bestellform.normal" /> / <bean:message key="bestellform.urgent" />"</p>
<p><br></p><hr>

<h4><bean:message key="bestellform.addcomment" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.comment" /></th>
    <th id="th-left"><bean:message key="bestellform.content" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.comment" />&nbsp;</td>
    <td id="border"><input name="comment1"
        value="<bean:write name="bestellform" property="comment1" />"
        type="text" size="100" maxlength="200" /></td>
  </tr>
</table>
<p><br></p><hr>

<h4><bean:message key="bestellform.addfields" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.field" /></th>
    <th id="th-left"><bean:message key="bestellform.addfield" /></th>
     <th id="th-left"><bean:message key="bestellform.req_singular" /></th>
     <th id="th-left"><bean:message key="bestellform.fieldname" /></th>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.freefield1" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="freitxt1" <logic:equal name="bestellform" property="freitxt1" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="freitxt1_required" <logic:equal name="bestellform" property="freitxt1_required" value="true">checked="checked"</logic:equal> /> </td>
     <td id="border"><input name="freitxt1_name"
        value="<bean:write name="bestellform" property="freitxt1_name" />"
        type="text" size="50" maxlength="50" /></td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.institution" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="institution" <logic:equal name="bestellform" property="institution" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="inst_required" <logic:equal name="bestellform" property="inst_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.abteilung" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="abteilung" <logic:equal name="bestellform" property="abteilung" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="abt_required" <logic:equal name="bestellform" property="abt_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><a href="usercategories.do?method=prepareCategories" target="_blank"><bean:message key="modifykontousers.category" /></a>&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="category" <logic:equal name="bestellform" property="category" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="category_required" <logic:equal name="bestellform" property="category_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.freefield2" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="freitxt2" <logic:equal name="bestellform" property="freitxt2" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="freitxt2_required" <logic:equal name="bestellform" property="freitxt2_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border"><input name="freitxt2_name"
        value="<bean:write name="bestellform" property="freitxt2_name" />"
        type="text" size="50" maxlength="50" /></td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.adress" /> <bean:message key="bestellform.adress_explain" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="adresse" <logic:equal name="bestellform" property="adresse" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="adr_required" <logic:equal name="bestellform" property="adr_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.strasse" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="strasse" <logic:equal name="bestellform" property="strasse" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="str_required" <logic:equal name="bestellform" property="str_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.plz" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="plz" <logic:equal name="bestellform" property="plz" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="plz_required" <logic:equal name="bestellform" property="plz_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.ort" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="ort" <logic:equal name="bestellform" property="ort" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="ort_required" <logic:equal name="bestellform" property="ort_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.land" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="land" <logic:equal name="bestellform" property="land" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="land_required" <logic:equal name="bestellform" property="land_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.telefon" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="telefon" <logic:equal name="bestellform" property="telefon" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="telefon_required" <logic:equal name="bestellform" property="telefon_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.benutzernr" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="benutzernr" <logic:equal name="bestellform" property="benutzernr" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="benutzernr_required" <logic:equal name="bestellform" property="benutzernr_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border">&nbsp;</td>
   </tr>
   <tr>
     <td id="border"><bean:message key="bestellform.freefield3" />&nbsp;</td>
     <td id="border" align="center"><input type="checkbox" name="freitxt3" <logic:equal name="bestellform" property="freitxt3" value="true">checked="checked"</logic:equal> /></td>
     <td id="border" align="center"><input type="checkbox" name="freitxt3_required" <logic:equal name="bestellform" property="freitxt3_required" value="true">checked="checked"</logic:equal> /></td>
     <td id="border"><input name="freitxt3_name"
        value="<bean:write name="bestellform" property="freitxt3_name" />"
        type="text" size="50" maxlength="50" /></td>
   </tr>
   
</table>
<p><br></p><hr>

<h4><bean:message key="bestellform.addoptions" /></h4>
<p><img src='img/option.gif' border='1' alt='<bean:message key="bestellform.addoptions" />'  /></p>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.options" /></th>
    <th id="th-left"><bean:message key="bestellform.content" /> / <bean:message key="bestellform.fieldname" /></th>
   </tr>
   <tr>
    <td id="border"><bean:message key="bestellform.nameoption" />&nbsp;</td>
    <td id="border"><input name="option_name"
        value="<bean:write name="bestellform" property="option_name" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value1" />&nbsp;</td>
    <td id="border"><input name="option_value1"
        value="<bean:write name="bestellform" property="option_value1" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value2" />&nbsp;</td>
    <td id="border"><input name="option_value2"
        value="<bean:write name="bestellform" property="option_value2" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.value3" />&nbsp;</td>
    <td id="border"><input name="option_value3"
        value="<bean:write name="bestellform" property="option_value3" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.comment" />&nbsp;</td>
    <td id="border"><input name="option_comment"
        value="<bean:write name="bestellform" property="option_comment" />"
        type="text" size="100" maxlength="200" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.link" />&nbsp;</td>
    <td id="border"><input name="option_linkout"
        value="<bean:write name="bestellform" property="option_linkout" />"
        type="url" size="100" maxlength="200" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.linkname" />&nbsp;</td>
    <td id="border"><input name="option_linkoutname"
        value="<bean:write name="bestellform" property="option_linkoutname" />"
        type="text" size="50" maxlength="50" /></td>
  </tr>
</table>
<p><bean:message key="bestellform.options_explain" /></p>
<p><br></p><hr>

<h4><bean:message key="bestellform.addcomment" /></h4>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.comment" /></th>
    <th id="th-left"><bean:message key="bestellform.content" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.comment" />&nbsp;</td>
    <td id="border"><input name="comment2"
        value="<bean:write name="bestellform" property="comment2" />"
        type="text" size="100" maxlength="200" /></td>
  </tr>
</table>
<p><br></p><hr>

<h4><bean:message key="bestellform.conditions" /></h4>
<p><img src='img/comment_conditions.gif' border='1' alt='<bean:message key="bestellform.conditions" />'  /></p>
<table class="border">
  <tr>
    <th id="th-left"><bean:message key="bestellform.condition" /></th>
    <th id="th-left"><bean:message key="bestellform.addfield" /></th>
    <th id="th-left"><bean:message key="bestellform.link" /></th>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.gebuehren" />&nbsp;</td>
    <td id="border" align="center"><input type="checkbox" name="gebuehren" <logic:equal name="bestellform" property="gebuehren" value="true">checked="checked"</logic:equal> /></td>
    <td id="border"><input name="link_gebuehren"
        value="<bean:write name="bestellform" property="link_gebuehren" />"
        type="url" size="100" maxlength="200" /></td>
  </tr>
  <tr>
    <td id="border"><bean:message key="bestellform.agb" />&nbsp;</td>
    <td id="border" align="center"><input type="checkbox" name="agb" <logic:equal name="bestellform" property="agb" value="true">checked="checked"</logic:equal> /></td>
    <td id="border"><input name="link_agb"
        value="<bean:write name="bestellform" property="link_agb" />"
        type="url" size="100" maxlength="200" /></td>
  </tr>
</table>

<p><br></p>

<input name="id" type="hidden" value="<bean:write name="bestellform" property="id" />" />
<input name="kid" type="hidden" value="<bean:write name="bestellform" property="kid" />" />
<input name="tyid" type="hidden" value="<bean:write name="bestellform" property="tyid" />" />
<input name="kennung" type="hidden" value="<bean:write name="bestellform" property="kennung" />" />
<input name="method" type="hidden" value="save" />
<input type="submit" value="<bean:message key="bestellform.save" />">
</html:form>

<p></p>
</div>



 </body>
</html>
