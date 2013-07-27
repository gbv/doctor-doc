<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
  <bean:message key="bestellform.external_title" />
</h3>

<logic:present name="message" property="message">
  <bean:define id="missingKey" name="message" property="message" type="java.lang.String"/>
  <p><font color="red"><bean:message key="<%=missingKey%>" /></font></p>
</logic:present>

<html:form action="externalformsave.do" method="post">

<h4><bean:message key="bestellform.external_create" /></h4>

<logic:present name="daiaparam" property="baseurl">
<p><a href="externalformdelete.do?method=deleteDaiaForm"><bean:message key="bestellform.external_delete" /></a></p>
</logic:present>

<table>
	<tr>
		<td><bean:message key="daia.baseurl" /></td>
		<td><input name="baseurl" type="text" size="80" maxlength="200" <logic:present name="daiaparam" property="baseurl">value="<bean:write name='daiaparam' property='baseurl'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="daia.firstparam" /></td>
		<td>
			<select name="firstParam">
				<option value="?" <logic:present name="daiaparam" property="firstParam"><logic:equal name="daiaparam" property="firstParam" value="?">selected="selected"</logic:equal></logic:present>>?</option>
				<option value="&" <logic:present name="daiaparam" property="firstParam"><logic:equal name="daiaparam" property="firstParam" value="&">selected="selected"</logic:equal></logic:present>>&amp;</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="daia.protocol" /></td>
		<td>
			<select name="protocol">
				<option value="custom" <logic:present name="daiaparam" property="protocol"><logic:equal name="daiaparam" property="protocol" value="custom">selected="selected"</logic:equal></logic:present>>custom</option>
				<option value="openurl" <logic:present name="daiaparam" property="protocol"><logic:equal name="daiaparam" property="protocol" value="openurl">selected="selected"</logic:equal></logic:present>>OpenURL</option>
				<option value="internal" <logic:present name="daiaparam" property="protocol"><logic:equal name="daiaparam" property="protocol" value="internal">selected="selected"</logic:equal></logic:present>>internal</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="daia.redirect" /></td>
		<td>
			<select name="redirect">
				<option value="true" <logic:present name="daiaparam" property="redirect"><logic:equal name="daiaparam" property="redirect" value="true">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
				<option value="false" <logic:present name="daiaparam" property="redirect"><logic:equal name="daiaparam" property="redirect" value="false">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="daia.method" /></td>
		<td>
			<select name="post">
				<option value="false" <logic:present name="daiaparam" property="post"><logic:equal name="daiaparam" property="post" value="false">selected="selected"</logic:equal></logic:present>>GET</option>
				<option value="true" <logic:present name="daiaparam" property="post"><logic:equal name="daiaparam" property="post" value="true">selected="selected"</logic:equal></logic:present>>POST</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="daia.ip_overrides" /></td>
		<td>
			<select name="ip_overrides">
				<option value="false" <logic:present name="daiaparam" property="ip_overrides"><logic:equal name="daiaparam" property="ip_overrides" value="false">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
				<option value="true" <logic:present name="daiaparam" property="ip_overrides"><logic:equal name="daiaparam" property="ip_overrides" value="true">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
			</select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="daia.worldWideAccess" /></td>
		<td>
			<select name="worldWideAccess">
				<option value="false" <logic:present name="daiaparam" property="worldWideAccess"><logic:equal name="daiaparam" property="worldWideAccess" value="false">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.no" /></option>
				<option value="true" <logic:present name="daiaparam" property="worldWideAccess"><logic:equal name="daiaparam" property="worldWideAccess" value="true">selected="selected"</logic:equal></logic:present>><bean:message key="modifykonto.yes" /></option>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="2"><br></td>
	</tr>
	<tr>
		<td colspan="2"><b><bean:message key="daia.mappings" /></b></td>
	</tr>
	<tr>
		<td colspan="2"><br></td>
	</tr>
	<tr>
		<td><bean:message key="stats.medientyp" /></td>
		<td><input name="mapMediatype" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapMediatype">value="<bean:write name='daiaparam' property='mapMediatype'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="daia.authors" /></td>
		<td><input name="mapAuthors" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapAuthors">value="<bean:write name='daiaparam' property='mapAuthors'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.artikeltitel" /></td>
		<td><input name="mapAtitle" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapAtitle">value="<bean:write name='daiaparam' property='mapAtitle'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.buchtitel" /></td>
		<td><input name="mapBtitle" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapBtitle">value="<bean:write name='daiaparam' property='mapBtitle'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.buchkapitel" /></td>
		<td><input name="mapChapter" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapChapter">value="<bean:write name='daiaparam' property='mapChapter'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.zeitschrift" /></td>
		<td><input name="mapJournal" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapJournal">value="<bean:write name='daiaparam' property='mapJournal'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.verlag" /></td>
		<td><input name="mapPublisher" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapPublisher">value="<bean:write name='daiaparam' property='mapPublisher'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.issn" /></td>
		<td><input name="mapIssn" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapIssn">value="<bean:write name='daiaparam' property='mapIssn'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.isbn" /></td>
		<td><input name="mapIsbn" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapIsbn">value="<bean:write name='daiaparam' property='mapIsbn'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.jahr" /></td>
		<td><input name="mapDate" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapDate">value="<bean:write name='daiaparam' property='mapDate'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.jahrgang" /></td>
		<td><input name="mapVolume" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapVolume">value="<bean:write name='daiaparam' property='mapVolume'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.heft" /></td>
		<td><input name="mapIssue" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapIssue">value="<bean:write name='daiaparam' property='mapIssue'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.seiten" /></td>
		<td><input name="mapPages" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapPages">value="<bean:write name='daiaparam' property='mapPages'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.pmid" /></td>
		<td><input name="mapPmid" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapPmid">value="<bean:write name='daiaparam' property='mapPmid'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="searchorders.doi" /></td>
		<td><input name="mapDoi" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapDoi">value="<bean:write name='daiaparam' property='mapDoi'/>"</logic:present> /></td>
	</tr>
	<tr>
		<td><bean:message key="daia.reference" /></td>
		<td><input name="mapReference" type="text" size="20" maxlength="20" <logic:present name="daiaparam" property="mapReference">value="<bean:write name='daiaparam' property='mapReference'/>"</logic:present> /></td>
	</tr>
<!-- 
	<tr>
		<td><bean:message key="daia.limitations" /></td>
		<td>
			<textarea cols="40" rows="3" name="limitations" >
				<logic:present name="daiaparam" property="limitations"><bean:write name='daiaparam' property='limitations'/></logic:present>
			</textarea>
		</td>
	</tr>
 -->
</table>

<input name="method" type="hidden" value="saveDaiaForm" />
<p>
	<input type="submit" value="<bean:message key="bestellform.save" />">
</p>
</html:form>

<p></p>
</div>



 </body>
</html>
