<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
          "http://www.w3.org/TR/html4/loose.dtd">

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="uebersicht.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css"> 
 </head>
 <body>
  
<tiles:insert page="import/header.jsp" flush="true" />
 
 <table style="position:absolute; text-align:left; left:<bean:message key="submenupos.uebersicht" />px; z-index:2;">
	<tr>
		<td 
			<logic:equal name="overviewform" property="filter" value="offen">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="offen">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.open_explain" />"><a href="listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.open" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="erledigt">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="erledigt">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.closed_explain" />"><a href="listkontobestellungen.do?method=overview&filter=erledigt&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.closed" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="reklamiert">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="reklamiert">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.claimed_explain" />"><a href="listkontobestellungen.do?method=overview&filter=reklamiert&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.claimed" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="zu bestellen">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="zu bestellen">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.toOrder_explain" />"><a href="listkontobestellungen.do?method=overview&filter=zu%20bestellen&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.toOrder" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="bestellt">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="bestellt">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.ordered_explain" />"><a href="listkontobestellungen.do?method=overview&filter=bestellt&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.ordered" /></a></td>
	    <td 
			<logic:equal name="overviewform" property="filter" value="geliefert">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="geliefert">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.shipped_explain" />"><a href="listkontobestellungen.do?method=overview&filter=geliefert&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.shipped" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="nicht lieferbar">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="nicht lieferbar">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.unfilled_explain" />"><a href="listkontobestellungen.do?method=overview&filter=nicht%20lieferbar&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.unfilled" /></a></td>
		<td 
			<logic:equal name="overviewform" property="filter" value="">id="submenuactive" nowrap </logic:equal>
			<logic:notEqual name="overviewform" property="filter" value="">id="submenu" nowrap </logic:notEqual>
			title="<bean:message key="menu.allorders_explain" />"><a href="listkontobestellungen.do?method=overview&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.allorders" /></a></td>
		<logic:present name="userinfo" property="benutzer">
		<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
			<td id="submenu" nowrap title="<bean:message key="menu.advancedsearch_explain" />"><a href="searchorder.do?method=prepareSearch"><bean:message key="menu.advancedsearch" /></a></td>
		</logic:notEqual>
		</logic:present>
	</tr>
</table>
 
<div class="content">

<br />
<br />

<html:form action="/searchorder">

<table border="0" align="center">
<logic:present name="userinfo" property="benutzer">
<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">

  <tr>
  	<td>
  		<select name="value1" >
			<option value="searchorders.all"><bean:message key="searchorders.all" /></option>
			<logic:iterate id="currentItem" name="sortedSearchFields">
				<bean:define id="itemValue" name="currentItem" property="value" type="java.lang.String"/>
			<option value="<%= itemValue %>"><bean:write name="currentItem" property="key"/></option>
			</logic:iterate>
		</select>
  	</td>
  	<td>
  		<select name="condition1" size="1">
    		<option value="contains" selected><bean:message key="uebersicht.contains" /></option>
    		<option value="contains not"><bean:message key="uebersicht.contains_not" /></option>
    		<option value="is"><bean:message key="uebersicht.is" /></option>
    		<option value="is not"><bean:message key="uebersicht.is_not" /></option>
  		</select>
  	</td>
  	<td>
  		<input name="input1" value="" type="text" size="35" maxlength="100">
  	</td>
  	<td>
  		<input name="boolean1" value="and" type="hidden">
	</td>
	<td>
		<input type="hidden" name="method" value="search" />
		<input type="submit" value="<bean:message key="searchorders.submit" />" />
	</td>
  </tr>  
</logic:notEqual>
</logic:present>
</table>

<input type="hidden" name="yfrom" value="<bean:write name="overviewform" property="yfrom" />">
<input type="hidden" name="mfrom" value="<bean:write name="overviewform" property="mfrom" />">
<input type="hidden" name="dfrom" value="<bean:write name="overviewform" property="dfrom" />">
<input type="hidden" name="yto" value="<bean:write name="overviewform" property="yto" />">
<input type="hidden" name="mto" value="<bean:write name="overviewform" property="mto" />">
<input type="hidden" name="dto" value="<bean:write name="overviewform" property="dto" />">

</html:form>

<p></p>

<html:form action="/listkontobestellungen">

<table border="0" width="100%">
<tr>
<td align="center">
	<select name="dfrom">
		<option value="1" <logic:equal name="overviewform" property="dfrom" value="1">selected</logic:equal>>1</option>
		<option value="2" <logic:equal name="overviewform" property="dfrom" value="2">selected</logic:equal>>2</option>
		<option value="3" <logic:equal name="overviewform" property="dfrom" value="3">selected</logic:equal>>3</option>
		<option value="4" <logic:equal name="overviewform" property="dfrom" value="4">selected</logic:equal>>4</option>
		<option value="5" <logic:equal name="overviewform" property="dfrom" value="5">selected</logic:equal>>5</option>
		<option value="6" <logic:equal name="overviewform" property="dfrom" value="6">selected</logic:equal>>6</option>
		<option value="7" <logic:equal name="overviewform" property="dfrom" value="7">selected</logic:equal>>7</option>
		<option value="8" <logic:equal name="overviewform" property="dfrom" value="8">selected</logic:equal>>8</option>
		<option value="9" <logic:equal name="overviewform" property="dfrom" value="9">selected</logic:equal>>9</option>
		<option value="10" <logic:equal name="overviewform" property="dfrom" value="10">selected</logic:equal>>10</option>
		<option value="11" <logic:equal name="overviewform" property="dfrom" value="11">selected</logic:equal>>11</option>
		<option value="12" <logic:equal name="overviewform" property="dfrom" value="12">selected</logic:equal>>12</option>
		<option value="13" <logic:equal name="overviewform" property="dfrom" value="13">selected</logic:equal>>13</option>
		<option value="14" <logic:equal name="overviewform" property="dfrom" value="14">selected</logic:equal>>14</option>
		<option value="15" <logic:equal name="overviewform" property="dfrom" value="15">selected</logic:equal>>15</option>
		<option value="16" <logic:equal name="overviewform" property="dfrom" value="16">selected</logic:equal>>16</option>
		<option value="17" <logic:equal name="overviewform" property="dfrom" value="17">selected</logic:equal>>17</option>
		<option value="18" <logic:equal name="overviewform" property="dfrom" value="18">selected</logic:equal>>18</option>
		<option value="19" <logic:equal name="overviewform" property="dfrom" value="19">selected</logic:equal>>19</option>
		<option value="20" <logic:equal name="overviewform" property="dfrom" value="20">selected</logic:equal>>20</option>
		<option value="21" <logic:equal name="overviewform" property="dfrom" value="21">selected</logic:equal>>21</option>
		<option value="22" <logic:equal name="overviewform" property="dfrom" value="22">selected</logic:equal>>22</option>
		<option value="23" <logic:equal name="overviewform" property="dfrom" value="23">selected</logic:equal>>23</option>
		<option value="24" <logic:equal name="overviewform" property="dfrom" value="24">selected</logic:equal>>24</option>
		<option value="25" <logic:equal name="overviewform" property="dfrom" value="25">selected</logic:equal>>25</option>
		<option value="26" <logic:equal name="overviewform" property="dfrom" value="26">selected</logic:equal>>26</option>
		<option value="27" <logic:equal name="overviewform" property="dfrom" value="27">selected</logic:equal>>27</option>
		<option value="28" <logic:equal name="overviewform" property="dfrom" value="28">selected</logic:equal>>28</option>
		<option value="29" <logic:equal name="overviewform" property="dfrom" value="29">selected</logic:equal>>29</option>
		<option value="30" <logic:equal name="overviewform" property="dfrom" value="30">selected</logic:equal>>30</option>
		<option value="31" <logic:equal name="overviewform" property="dfrom" value="31">selected</logic:equal>>31</option>
	</select>
	<select name="mfrom">
		<option value="1" <logic:equal name="overviewform" property="mfrom" value="1">selected</logic:equal>>1</option>
		<option value="2" <logic:equal name="overviewform" property="mfrom" value="2">selected</logic:equal>>2</option>
		<option value="3" <logic:equal name="overviewform" property="mfrom" value="3">selected</logic:equal>>3</option>
		<option value="4" <logic:equal name="overviewform" property="mfrom" value="4">selected</logic:equal>>4</option>
		<option value="5" <logic:equal name="overviewform" property="mfrom" value="5">selected</logic:equal>>5</option>
		<option value="6" <logic:equal name="overviewform" property="mfrom" value="6">selected</logic:equal>>6</option>
		<option value="7" <logic:equal name="overviewform" property="mfrom" value="7">selected</logic:equal>>7</option>
		<option value="8" <logic:equal name="overviewform" property="mfrom" value="8">selected</logic:equal>>8</option>
		<option value="9" <logic:equal name="overviewform" property="mfrom" value="9">selected</logic:equal>>9</option>
		<option value="10" <logic:equal name="overviewform" property="mfrom" value="10">selected</logic:equal>>10</option>
		<option value="11" <logic:equal name="overviewform" property="mfrom" value="11">selected</logic:equal>>11</option>
		<option value="12" <logic:equal name="overviewform" property="mfrom" value="12">selected</logic:equal>>12</option>
	</select>
	<select name="yfrom">
		<logic:iterate id="yf" name="overviewform" property="years">
			<bean:define id="tmp" name="overviewform" property="yfrom" type="java.lang.String"/>
			<option value="<bean:write name="yf" />" <logic:equal name="yf" value="<%=tmp%>">selected</logic:equal>><bean:write name="yf" /></option>
		</logic:iterate>
	</select> - 
	<select name="dto">
		<option value="1" <logic:equal name="overviewform" property="dto" value="1">selected</logic:equal>>1</option>
		<option value="2" <logic:equal name="overviewform" property="dto" value="2">selected</logic:equal>>2</option>
		<option value="3" <logic:equal name="overviewform" property="dto" value="3">selected</logic:equal>>3</option>
		<option value="4" <logic:equal name="overviewform" property="dto" value="4">selected</logic:equal>>4</option>
		<option value="5" <logic:equal name="overviewform" property="dto" value="5">selected</logic:equal>>5</option>
		<option value="6" <logic:equal name="overviewform" property="dto" value="6">selected</logic:equal>>6</option>
		<option value="7" <logic:equal name="overviewform" property="dto" value="7">selected</logic:equal>>7</option>
		<option value="8" <logic:equal name="overviewform" property="dto" value="8">selected</logic:equal>>8</option>
		<option value="9" <logic:equal name="overviewform" property="dto" value="9">selected</logic:equal>>9</option>
		<option value="10" <logic:equal name="overviewform" property="dto" value="10">selected</logic:equal>>10</option>
		<option value="11" <logic:equal name="overviewform" property="dto" value="11">selected</logic:equal>>11</option>
		<option value="12" <logic:equal name="overviewform" property="dto" value="12">selected</logic:equal>>12</option>
		<option value="13" <logic:equal name="overviewform" property="dto" value="13">selected</logic:equal>>13</option>
		<option value="14" <logic:equal name="overviewform" property="dto" value="14">selected</logic:equal>>14</option>
		<option value="15" <logic:equal name="overviewform" property="dto" value="15">selected</logic:equal>>15</option>
		<option value="16" <logic:equal name="overviewform" property="dto" value="16">selected</logic:equal>>16</option>
		<option value="17" <logic:equal name="overviewform" property="dto" value="17">selected</logic:equal>>17</option>
		<option value="18" <logic:equal name="overviewform" property="dto" value="18">selected</logic:equal>>18</option>
		<option value="19" <logic:equal name="overviewform" property="dto" value="19">selected</logic:equal>>19</option>
		<option value="20" <logic:equal name="overviewform" property="dto" value="20">selected</logic:equal>>20</option>
		<option value="21" <logic:equal name="overviewform" property="dto" value="21">selected</logic:equal>>21</option>
		<option value="22" <logic:equal name="overviewform" property="dto" value="22">selected</logic:equal>>22</option>
		<option value="23" <logic:equal name="overviewform" property="dto" value="23">selected</logic:equal>>23</option>
		<option value="24" <logic:equal name="overviewform" property="dto" value="24">selected</logic:equal>>24</option>
		<option value="25" <logic:equal name="overviewform" property="dto" value="25">selected</logic:equal>>25</option>
		<option value="26" <logic:equal name="overviewform" property="dto" value="26">selected</logic:equal>>26</option>
		<option value="27" <logic:equal name="overviewform" property="dto" value="27">selected</logic:equal>>27</option>
		<option value="28" <logic:equal name="overviewform" property="dto" value="28">selected</logic:equal>>28</option>
		<option value="29" <logic:equal name="overviewform" property="dto" value="29">selected</logic:equal>>29</option>
		<option value="30" <logic:equal name="overviewform" property="dto" value="30">selected</logic:equal>>30</option>
		<option value="31" <logic:equal name="overviewform" property="dto" value="31">selected</logic:equal>>31</option>	
	</select>
	<select name="mto">
		<option value="1" <logic:equal name="overviewform" property="mto" value="1">selected</logic:equal>>1</option>
		<option value="2" <logic:equal name="overviewform" property="mto" value="2">selected</logic:equal>>2</option>
		<option value="3" <logic:equal name="overviewform" property="mto" value="3">selected</logic:equal>>3</option>
		<option value="4" <logic:equal name="overviewform" property="mto" value="4">selected</logic:equal>>4</option>
		<option value="5" <logic:equal name="overviewform" property="mto" value="5">selected</logic:equal>>5</option>
		<option value="6" <logic:equal name="overviewform" property="mto" value="6">selected</logic:equal>>6</option>
		<option value="7" <logic:equal name="overviewform" property="mto" value="7">selected</logic:equal>>7</option>
		<option value="8" <logic:equal name="overviewform" property="mto" value="8">selected</logic:equal>>8</option>
		<option value="9" <logic:equal name="overviewform" property="mto" value="9">selected</logic:equal>>9</option>
		<option value="10" <logic:equal name="overviewform" property="mto" value="10">selected</logic:equal>>10</option>
		<option value="11" <logic:equal name="overviewform" property="mto" value="11">selected</logic:equal>>11</option>
		<option value="12" <logic:equal name="overviewform" property="mto" value="12">selected</logic:equal>>12</option>
	</select>
	<select name="yto">
		<logic:iterate id="yt" name="overviewform" property="years">
			<bean:define id="tmp" name="overviewform" property="yto" type="java.lang.String"/>
			<option value="<bean:write name="yt" />" <logic:equal name="yt" value="<%=tmp%>">selected</logic:equal>><bean:write name="yt" /></option>
		</logic:iterate>
	</select>
	
	<input type="hidden" name="method" value="overview" />
	<html:submit property="action" value="Go" />	
</td>
</tr>
</table>
<p></p>

</html:form>

<a class="black" target="_blank" href="orderreport.do?method=orderspdf&sort=<bean:write name="overviewform" property="sort" />&sortorder=<bean:write name="overviewform" property="sortorder" />&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />&filter=<bean:write name="overviewform" property="filter" /><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src='img/pdf-druckvorschau.png' alt="<bean:message key="uebersicht.pdf" />" title="<bean:message key="uebersicht.pdf" />" height="30" width="26" border="0"></a>
<bean:message key="uebersicht.titel" /> <logic:present name="orderstatistikform" property="auflistung">- <bean:message key="uebersicht.total" />: <logic:iterate id="o" name="orderstatistikform" property="auflistung"><bean:write name="o" property="total" /></logic:iterate></logic:present>

<table border="1">
	<tr>
		
		<th></th>
		<th><a class=sort href="listkontobestellungen.do?method=overview&sort=mediatype&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="mediatype"><i></logic:equal></logic:present><br><bean:message key="uebersicht.typ" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="mediatype"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=mediatype&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=black href="listkontobestellungen.do?method=overview&sort=orderdate&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="orderdate"><i></logic:equal></logic:present><br><bean:message key="uebersicht.orderdate" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="date"></i></logic:equal></logic:present><a class=black href="listkontobestellungen.do?method=overview&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=sort href="listkontobestellungen.do?method=overview&sort=bestellquelle&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="bestellquelle"><i></logic:equal></logic:present><br><bean:message key="uebersicht.supplier" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="bestellquelle"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=bestellquelle&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=sort href="listkontobestellungen.do?method=overview&sort=state&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="state"><i></logic:equal></logic:present><br><bean:message key="uebersicht.state" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="state"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=state&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=black href="listkontobestellungen.do?method=overview&sort=statedate&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="statedate"><i></logic:equal></logic:present><br><bean:message key="uebersicht.statedate" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="statedate"></i></logic:equal></logic:present><a class=black href="listkontobestellungen.do?method=overview&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=sort href="listkontobestellungen.do?method=overview&sort=deloptions&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="deloptions"><i></logic:equal></logic:present><br><bean:message key="uebersicht.deliveryway" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="deloptions"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=deloptions&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><a class=sort href="listkontobestellungen.do?method=overview&sort=name&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="name"><i></logic:equal></logic:present><br><bean:message key="uebersicht.patron" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="name"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=name&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th width="23%"><a class=sort href="listkontobestellungen.do?method=overview&sort=artikeltitel&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="artikeltitel"><i></logic:equal></logic:present><br><bean:message key="uebersicht.article" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="artikeltitel"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=artikeltitel&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th width="12%"><a class=sort href="listkontobestellungen.do?method=overview&sort=zeitschrift&sortorder=asc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/up.png" alt="<bean:message key="uebersicht.sort_asc" />" title="<bean:message key="uebersicht.sort_asc" />" border="0"></a><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="zeitschrift"><i></logic:equal></logic:present><br><bean:message key="uebersicht.journal" /><br><logic:present name="overviewform" property="sort"><logic:equal name="overviewform" property="sort" value="zeitschrift"></i></logic:equal></logic:present><a class=sort href="listkontobestellungen.do?method=overview&sort=zeitschrift&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" /><logic:present name="overviewform" property="filter">&filter=<bean:write name="overviewform" property="filter" /></logic:present><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src="img/down.png" alt="<bean:message key="uebersicht.sort_desc" />" title="<bean:message key="uebersicht.sort_desc" />" border="0"></a></th>
		<th><bean:message key="uebersicht.notes" /></th>
		<th>&nbsp;</th>
	</tr>
 <logic:present name="overviewform" property="bestellungen">
   <logic:iterate id="b" name="overviewform" property="bestellungen">
     <tr>  
      
      <td valign="middle">
      	<nobr>
      	&nbsp;
      	<logic:notEqual name="b" property="benutzer.name" value="anonymized"><a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="b" property="id" />">
      		<img border="0" src="img/edit.png" alt="<bean:message key="uebersicht.modify" />" title="<bean:message key="uebersicht.modify" />"/></a>
      	</logic:notEqual>
      	<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
 			&nbsp;&nbsp;<a href="prepare-ilv-order-pdf.do?method=journalorderdetail&bid=<bean:write name="b" property="id" />"><img border="0" src="img/faxpiktogramm.png" title="<bean:message key="uebersicht.ilv-order" />" /></a>
 		</logic:notEqual>
 		</nobr>    	
      	<br />
      	<nobr>
      	&nbsp;
      	<a href="reorder.do?method=prepareReorder&bid=<bean:write name="b" property="id" />"><img border="0" src="img/reorder.png" alt="<bean:message key="uebersicht.reorder" />" title="<bean:message key="uebersicht.reorder" />"/></a>
      	&nbsp;
      	<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
      		<a href="deleteorder.do?method=prepareDeleteOrder&bid=<bean:write name="b" property="id" />"><img border="0" src="img/drop.png" alt="<bean:message key="uebersicht.drop" />" title="<bean:message key="uebersicht.drop" />"/></a>
      	</logic:notEqual>
      	&nbsp;
      	</nobr>
      	
      </td>
      <!-- <td align="center"><a href="reorder.do?method=prepareReorder&bid=<bean:write name="b" property="id" />"><img border="0" src="img/reorder.png" alt="<bean:message key="uebersicht.reorder" />" title="<bean:message key="uebersicht.reorder" />"/></a></td> -->
      <td align="center"><logic:equal name="b" property="mediatype" value="Artikel"><img border="0" src="img/article.gif" alt="<bean:message key="bestellform.artikelkopie" />" title="<bean:message key="bestellform.artikelkopie" />"/></logic:equal><logic:equal name="b" property="mediatype" value="Buch"><img border="0" src="img/book.gif" alt="<bean:message key="bestellform.buch" />" title="<bean:message key="bestellform.buch" />"/></logic:equal><logic:equal name="b" property="mediatype" value="Teilkopie Buch"><img border="0" src="img/Teilkopie_B.png" alt="<bean:message key="bestellform.buchausschnitt" />" title="<bean:message key="bestellform.buchausschnitt" />"/></logic:equal></td>
      <td align="center"><bean:write name="b" property="orderdate" />&nbsp;</td>
      <td>
      	<logic:equal name="b" property="subitonr" value="">
      		<logic:present name="b" property="gbvnr">
				<a href="https://www.gbv.de/cbs4/bestellverlauf.pl?BestellID=<bean:write name="b" property="gbvnr" />" target="_blank"><bean:write name="b" property="bestellquelle" /></a>&nbsp;
			</logic:present>
			<logic:notPresent name="b" property="gbvnr">
      			<logic:notEqual name="b" property="bestellquelle" value="k.A.">
      				<bean:write name="b" property="bestellquelle" />&nbsp;
      			</logic:notEqual>
      			<logic:equal name="b" property="bestellquelle" value="k.A.">
      				<bean:message key="stats.notSpecified" />&nbsp;
      			</logic:equal>
      		</logic:notPresent>
      	</logic:equal>
      	<logic:notEqual name="b" property="subitonr" value="">
      		<a href="http://www.subito-doc.de/index.php?mod=subo&task=trackingdetail&tgq=<bean:write name="b" property="subitonr" />" target="_blank"><bean:write name="b" property="bestellquelle" /></a>&nbsp;
      	</logic:notEqual>
      </td>
      <td valign="middle">
      <html:form action="changestat">
      <nobr>
      	<select name="tid">
	 		<bean:define id="var" name="b" property="statustext" type="java.lang.String"/>
      			<logic:present name="overviewform" property="statitexts">
      				<logic:iterate id="s" name="overviewform" property="statitexts">
			 			<option value="<bean:write name="s" property="id" />" <logic:equal name="s" property="inhalt" value="<%=var%>">selected</logic:equal> ><logic:equal name="s" property="inhalt" value="bestellt"><bean:message key="menu.ordered" /></logic:equal><logic:equal name="s" property="inhalt" value="erledigt"><bean:message key="menu.closed" /></logic:equal><logic:equal name="s" property="inhalt" value="geliefert"><bean:message key="menu.shipped" /></logic:equal><logic:equal name="s" property="inhalt" value="nicht lieferbar"><bean:message key="menu.unfilled" /></logic:equal><logic:equal name="s" property="inhalt" value="reklamiert"><bean:message key="menu.claimed" /></logic:equal><logic:equal name="s" property="inhalt" value="zu bestellen"><bean:message key="menu.toOrder" /></logic:equal></option>		
   					</logic:iterate>
	  	</logic:present>
	  </select>	
	  <input type="image" src="img/change.png" alt="<bean:message key="uebersicht.change_state" />" title="<bean:message key="uebersicht.change_state" />"></nobr>
	  <input type="hidden" name="bid" value="<bean:write name="b" property="id" />" />
	  <input type="hidden" name="method" value="changestat" />
	  <input type="hidden" name="yfrom" value="<bean:write name="overviewform" property="yfrom" />" />
	  <input type="hidden" name="mfrom" value="<bean:write name="overviewform" property="mfrom" />" />
	  <input type="hidden" name="dfrom" value="<bean:write name="overviewform" property="dfrom" />" />
	  <input type="hidden" name="yto" value="<bean:write name="overviewform" property="yto" />" />
	  <input type="hidden" name="mto" value="<bean:write name="overviewform" property="mto" />" />
	  <input type="hidden" name="dto" value="<bean:write name="overviewform" property="dto" />" />
	  <logic:present name="overviewform" property="sort"><input type="hidden" name="sort" value="<bean:write name="overviewform" property="sort" />" /></logic:present>
	  <logic:present name="overviewform" property="sortorder"><input type="hidden" name="sortorder" value="<bean:write name="overviewform" property="sortorder" />" /></logic:present>
	  <logic:present name="overviewform" property="filter"><input type="hidden" name="filter" value="<bean:write name="overviewform" property="filter" />" /></logic:present>
	  </html:form>
	  </td>
	  <td align="center"><bean:write name="b" property="statusdate" />&nbsp;</td>
      <td align="center"><bean:write name="b" property="deloptions" />&nbsp;</td>
      <td align="center">
      	<logic:equal name="b" property="mediatype" value="Artikel">
      		<a href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.journalorder" />:%20%22<bean:write name="b" property="artikeltitel" />%22"><bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
      	</logic:equal>
      	<logic:equal name="b" property="mediatype" value="Buch">
      		<a href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookorder" />:%20%22<bean:write name="b" property="buchtitel" />%22"><bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
      	</logic:equal>
      	<logic:equal name="b" property="mediatype" value="Teilkopie Buch">
      		<a href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookpartorder" />:%20%22<bean:write name="b" property="artikeltitel" />%22"><bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
      	</logic:equal>
      	&nbsp;
      </td>
      <td><bean:write name="b" property="artikeltitel" /><bean:write name="b" property="kapitel" />&nbsp;</td>
      <td><bean:write name="b" property="zeitschrift" /><bean:write name="b" property="buchtitel" />&nbsp;</td>
      <td>      
      
       <html:form method="post" action="changenotes">
      	<nobr>
      		<input type="image" src="img/change.png" alt="<bean:message key="uebersicht.change_notes" />" title="<bean:message key="uebersicht.change_notes" />">
      		<textarea cols="15" rows="4" name="notizen" style="word-wrap:soft;"><bean:write name="b" property="notizen" /></textarea>
      	</nobr>
	  <input type="hidden" name="bid" value="<bean:write name="b" property="id" />" /> 
	  <input type="hidden" name="method" value="changenotes" />
	  <input type="hidden" name="yfrom" value="<bean:write name="overviewform" property="yfrom" />" />
	  <input type="hidden" name="mfrom" value="<bean:write name="overviewform" property="mfrom" />" />
	  <input type="hidden" name="dfrom" value="<bean:write name="overviewform" property="dfrom" />" />
	  <input type="hidden" name="yto" value="<bean:write name="overviewform" property="yto" />" />
	  <input type="hidden" name="mto" value="<bean:write name="overviewform" property="mto" />" />
	  <input type="hidden" name="dto" value="<bean:write name="overviewform" property="dto" />" />
	  <logic:present name="overviewform" property="sort"><input type="hidden" name="sort" value="<bean:write name="overviewform" property="sort" />" /></logic:present>
	  <logic:present name="overviewform" property="sortorder"><input type="hidden" name="sortorder" value="<bean:write name="overviewform" property="sortorder" />" /></logic:present>
	  <logic:present name="overviewform" property="filter"><input type="hidden" name="filter" value="<bean:write name="overviewform" property="filter" />" /></logic:present>
      	</html:form>
      </td>
      
	  <td><a href="journalorderdetail.do?method=journalorderdetail&bid=<bean:write name="b" property="id" />"><img src="img/details.png" alt="<bean:message key="uebersicht.details" />" title="<bean:message key="uebersicht.details" />" border="0"></a></td>
    </tr>
   </logic:iterate>
</logic:present>

</table> 
</div>
 </body>
</html>