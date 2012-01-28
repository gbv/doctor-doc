<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="suppliers.title" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
  </tr>
</table>

<br />

<h3><bean:message key="suppliers.privates" /></h3>
<table border="1">
	<tr>
		<th id="th-left">
			&nbsp;
		</th>
		<th id="th-left">
			<bean:message key="orderview.sigel" />
		</th>
		<th id="th-left">
			<bean:message key="searchorders.supplier" />
		</th>
		<th id="th-left">
			<bean:message key="adressen.email" />
		</th>
		<th id="th-left">
			&nbsp;
		</th>
	</tr>
	<logic:iterate id="priv" name="privsuppliers">
		<tr>
			<td>
				<a href="supplier.do?method=edit&sid=<bean:write name="priv" property="supplier.lid" />">
          		<img border="0" src="img/edit.png" alt="<bean:message key="suppliers.edit" />" title="<bean:message key="suppliers.edit" />"/></a>
			</td>
			<td>
				<bean:write name="priv" property="supplier.sigel" />
			</td>
			<td>
				<bean:write name="priv" property="supplier.name" />
			</td>
			<td>
				<bean:write name="priv" property="supplier.emailILL" />
			</td>
			<td>
				<a href="supplier.do?method=delete&sid=<bean:write name="priv" property="supplier.lid" />"><img border="0" src="img/drop.png" alt="<bean:message key="suppliers.delete" />" title="<bean:message key="suppliers.delete" />"/></a>
			</td>
		</tr>
	</logic:iterate>
</table>

<h3><bean:message key="suppliers.publics" /></h3>

<table border="1">
	<tr>
		<th id="th-left">
			&nbsp;
		</th>
		<th id="th-left">
			<bean:message key="orderview.sigel" />
		</th>
		<th id="th-left">
			<bean:message key="searchorders.supplier" />
		</th>
		<th id="th-left">
			<bean:message key="adressen.email" />
		</th>
		<th id="th-left">
			<bean:message key="modifykonto.land" />
		</th>
	</tr>
	<logic:iterate id="pub" name="pubsuppliers">
		<tr>
			<td>
				<a href="supplier.do?method=edit&sid=<bean:write name="pub" property="supplier.lid" />">
          		<img border="0" src="img/edit.png" alt="<bean:message key="suppliers.edit" />" title="<bean:message key="suppliers.edit" />"/></a>
			</td>
			<td>
				<bean:write name="pub" property="supplier.sigel" />
			</td>
			<td>
				<bean:write name="pub" property="supplier.name" />
			</td>
			<td>
				<bean:write name="pub" property="supplier.emailILL" />
			</td>
			<td>
				<logic:notEmpty name="pub" property="supplier.countryCode">
					<bean:write name="pub" property="supplier.countryCode" />
				</logic:notEmpty>
				<logic:empty name="pub" property="supplier.countryCode">
					<bean:message key="suppliers.all" />
				</logic:empty>
			</td>
		</tr>
	</logic:iterate>
</table>

<!-- 

				<select name="land">
      				<option value="0" selected="selected"><bean:message key="modifykonto.countrychoose" /></option>
						<logic:iterate id="c" name="countries">
						<bean:define id="tmp" name="pub" property="supplier.countryCode" type="java.lang.String"/>
		     				<option value="<bean:write name="c" property="countrycode" />"<logic:equal name="c" property="countrycode" value="<%=tmp%>"> selected</logic:equal>><bean:write name="c" property="countryname" /></option>
		   				</logic:iterate>
		   		</select>
 -->

<p><br /></p>

</div>
 </body>
</html>
