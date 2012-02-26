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
  <script src="jsp/import/js/jquery.js" type="text/javascript"></script>
  <script src="jsp/import/js/jquery.hoverIntent.min.js" type="text/javascript"></script>
  <script src="jsp/import/js/jquery.cluetip.min.js" type="text/javascript"></script>
  
  		<script type="text/javascript">
			$(document).ready(function() {

				$('span[title]').css({borderBottom: '1px solid #900'}).cluetip({
					  splitTitle: '|',
					  arrows: true,
					  dropShadow: false,
					  hoverIntent: true,
					  sticky: true,
					  mouseOutClose: true,
					  closePosition: 'title',
					  closeText: '<img src="img/drop.png" title="<bean:message key="showkontousers.close" />" alt="<bean:message key="showkontousers.close" />" />',										  
					  cluetipClass: 'jtip'}
					);

  				$('a.title').cluetip({
  	  				  splitTitle: '|',
  	  				  arrows: true,
					  dropShadow: false,
					  hoverIntent: true,
					  sticky: true,
					  mouseOutClose: true,
					  clickThrough:     true,
					  closePosition: 'title',
					  closeText: '<a href="test"><img src="img/drop.png" title="<bean:message key="showkontousers.close" />" alt="<bean:message key="showkontousers.close" />" /></a>',										  
					  cluetipClass: 'jtip'}
	  				);

		});
		</script>
		
		<link rel="stylesheet" type="text/css" href="jsp/import/js/jquery.cluetip.css" />
		<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="uebersicht.titel" /></title>
 </head>
 <body>
  
<tiles:insert page="import/header.jsp" flush="true" />
 
 <table style="position:absolute; text-align:left; left:<bean:message key="submenupos.uebersicht" />px; z-index:2;">
  <tr>
    <td 
      <logic:equal name="overviewform" property="filter" value="offen">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="offen">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.open_explain" />"><a href="listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.open" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="erledigt">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="erledigt">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.closed_explain" />"><a href="listkontobestellungen.do?method=overview&filter=erledigt&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.closed" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="reklamiert">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="reklamiert">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.claimed_explain" />"><a href="listkontobestellungen.do?method=overview&filter=reklamiert&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.claimed" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="zu bestellen">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="zu bestellen">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.toOrder_explain" />"><a href="listkontobestellungen.do?method=overview&filter=zu%20bestellen&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.toOrder" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="bestellt">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="bestellt">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.ordered_explain" />"><a href="listkontobestellungen.do?method=overview&filter=bestellt&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.ordered" /></a></td>
      <td 
      <logic:equal name="overviewform" property="filter" value="geliefert">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="geliefert">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.shipped_explain" />"><a href="listkontobestellungen.do?method=overview&filter=geliefert&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.shipped" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="nicht lieferbar">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="nicht lieferbar">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.unfilled_explain" />"><a href="listkontobestellungen.do?method=overview&filter=nicht%20lieferbar&sort=statedate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.unfilled" /></a></td>
    <td 
      <logic:equal name="overviewform" property="filter" value="">id="submenuactive" nowrap="nowrap" </logic:equal>
      <logic:notEqual name="overviewform" property="filter" value="">id="submenu" nowrap="nowrap" </logic:notEqual>
      title="<bean:message key="menu.allorders_explain" />"><a href="listkontobestellungen.do?method=overview&sort=orderdate&sortorder=desc&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />"><bean:message key="menu.allorders" /></a></td>
    <logic:present name="userinfo" property="benutzer">
    <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
      <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.advancedsearch_explain" />"><a href="searchorder.do?method=prepareSearch"><bean:message key="menu.advancedsearch" /></a></td>
    </logic:notEqual>
    </logic:present>
  </tr>
</table>
 
<div class="content">

<br />
<br />

<html:form action="/searchorder">

<table width="100%">
	<tr>
		<td align="center">
<table align="center">
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
        <option value="contains" selected="selected"><bean:message key="uebersicht.contains" /></option>
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
		</td>
	</tr>
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

<table width="100%">
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

<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
	<a class="black" target="_blank" href="orderreport.do?method=orderspdf&sort=<bean:write name="overviewform" property="sort" />&sortorder=<bean:write name="overviewform" property="sortorder" />&yfrom=<bean:write name="overviewform" property="yfrom" />&mfrom=<bean:write name="overviewform" property="mfrom" />&dfrom=<bean:write name="overviewform" property="dfrom" />&yto=<bean:write name="overviewform" property="yto" />&mto=<bean:write name="overviewform" property="mto" />&dto=<bean:write name="overviewform" property="dto" />&filter=<bean:write name="overviewform" property="filter" /><logic:present name="overviewform" property="s">&s=<bean:write name="overviewform" property="s" /></logic:present>"><img src='img/pdf-druckvorschau.png' alt="<bean:message key="uebersicht.pdf" />" title="<bean:message key="uebersicht.pdf" />" height="30" width="26" border="0"></a>
</logic:notEqual>
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
        <logic:empty name="b" property="subitonr">
        <!-- non Subito orders -->
          <logic:present name="b" property="gbvnr">
          <!-- GBV orders -->
        <a href="https://www.gbv.de/cbs4/bestellverlauf.pl?BestellID=<bean:write name="b" property="gbvnr" />" target="_blank"><bean:write name="b" property="bestellquelle" /></a>&nbsp;
      </logic:present>
      <logic:notPresent name="b" property="gbvnr">
      <!-- all other orders -->
            <logic:notEqual name="b" property="bestellquelle" value="k.A.">
            <!-- normal orders -->
              <logic:empty name="b" property="lieferant.emailILL">
              <!-- no ILL email defined -->
              	<bean:write name="b" property="bestellquelle" />&nbsp;
              </logic:empty>
              <logic:notEmpty name="b" property="lieferant.emailILL">
              <!-- ILL email defined -->
              	<span class="title" title="<bean:write name="b" property="bestellquelle" />|<a href='mailto:<bean:write name="b" property="lieferant.emailILL" />'><bean:write name="b" property="lieferant.emailILL" /></a>">
              	<bean:write name="b" property="bestellquelle" />&nbsp;</span>              
              </logic:notEmpty>
            </logic:notEqual>
            <logic:equal name="b" property="bestellquelle" value="k.A.">
            <!-- no supplier defined -->
              <bean:message key="stats.notSpecified" />&nbsp;
            </logic:equal>
          </logic:notPresent>
        </logic:empty>
        <logic:notEmpty name="b" property="subitonr">
        <!-- Subito orders -->
          <a href="http://www.subito-doc.de/index.php?mod=subo&task=trackingdetail&tgq=<bean:write name="b" property="subitonr" />" target="_blank"><bean:write name="b" property="bestellquelle" /></a>&nbsp;
        </logic:notEmpty>
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
          <a class="title" title="<a title='<bean:message key="showkontousers.info_edit" />' href='edituserdetail.do?method=changeuserdetails&bid=<bean:write name="b" property="benutzer.id" />'> <img src='img/edit.png' alt='<bean:message key="showkontousers.info_edit" />' /></a>
          	&nbsp;<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.category.inhalt" />|
          	<bean:write name="b" property="benutzer.librarycard" />|
          	&nbsp;|
          	<bean:define id="tit" name="b" property="artikeltitel" type="java.lang.String"/>
          	<a href='mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.journalorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22'><bean:write name="b" property="benutzer.email" /></a>|
          	&nbsp;|
          	<logic:notEmpty name="b" property="benutzer.telefonnrp"><bean:message key="showkontousers.telefon_p" />: <bean:write name="b" property="benutzer.telefonnrp" />|<logic:empty name="b" property="benutzer.telefonnrg">&nbsp;|</logic:empty></logic:notEmpty>
          	<logic:notEmpty name="b" property="benutzer.telefonnrg"> <bean:message key="showkontousers.telefon_g" />: <bean:write name="b" property="benutzer.telefonnrg" />|&nbsp;|</logic:notEmpty>
          	<logic:equal name="b" property="benutzer.anrede" value="Frau"><bean:message key="adressen.anrede_frau" /></logic:equal><logic:equal name="b" property="benutzer.anrede" value="Herr"><bean:message key="adressen.anrede_herr" /></logic:equal>|
          	<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.institut" />|<bean:write name="b" property="benutzer.abteilung" />|
          	<bean:write name="b" property="benutzer.adresse" />|<bean:write name="b" property="benutzer.adresszusatz" />|
          	<bean:write name="b" property="benutzer.land" /><logic:notEmpty name="b" property="benutzer.plz">-<bean:write name="b" property="benutzer.plz" /></logic:notEmpty> <bean:write name="b" property="benutzer.ort" />" 
          	href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.journalorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22">
          	<bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
        </logic:equal>
        <logic:equal name="b" property="mediatype" value="Buch">
        	<a class="title" title="<a title='<bean:message key="showkontousers.info_edit" />' href='edituserdetail.do?method=changeuserdetails&bid=<bean:write name="b" property="benutzer.id" />'> <img src='img/edit.png' alt='<bean:message key="showkontousers.info_edit" />' /></a>
          	&nbsp;<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.category.inhalt" />|
          	<bean:write name="b" property="benutzer.librarycard" />|
          	&nbsp;|
          	<bean:define id="tit" name="b" property="buchtitel" type="java.lang.String"/>
          	<a href='mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22'><bean:write name="b" property="benutzer.email" /></a>|
          	&nbsp;|
          	<logic:notEmpty name="b" property="benutzer.telefonnrp"><bean:message key="showkontousers.telefon_p" />: <bean:write name="b" property="benutzer.telefonnrp" />|<logic:empty name="b" property="benutzer.telefonnrg">&nbsp;|</logic:empty></logic:notEmpty>
          	<logic:notEmpty name="b" property="benutzer.telefonnrg"><bean:message key="showkontousers.telefon_g" />: <bean:write name="b" property="benutzer.telefonnrg" />|&nbsp;|</logic:notEmpty>
          	<logic:equal name="b" property="benutzer.anrede" value="Frau"><bean:message key="adressen.anrede_frau" /></logic:equal><logic:equal name="b" property="benutzer.anrede" value="Herr"><bean:message key="adressen.anrede_herr" /></logic:equal>|
          	<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.institut" />|<bean:write name="b" property="benutzer.abteilung" />|
          	<bean:write name="b" property="benutzer.adresse" />|<bean:write name="b" property="benutzer.adresszusatz" />|
          	<bean:write name="b" property="benutzer.land" /><logic:notEmpty name="b" property="benutzer.plz">-<bean:write name="b" property="benutzer.plz" /></logic:notEmpty> <bean:write name="b" property="benutzer.ort" />" 
          	href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22">
          	<bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
        </logic:equal>
        <logic:equal name="b" property="mediatype" value="Teilkopie Buch">
        	<a class="title" title="<a title='<bean:message key="showkontousers.info_edit" />' href='edituserdetail.do?method=changeuserdetails&bid=<bean:write name="b" property="benutzer.id" />'> <img src='img/edit.png' alt='<bean:message key="showkontousers.info_edit" />' /></a>
          	&nbsp;<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.category.inhalt" />|
          	<bean:write name="b" property="benutzer.librarycard" />|
          	&nbsp;|
          	<bean:define id="tit" name="b" property="kapitel" type="java.lang.String"/>
          	<a href='mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookpartorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22'><bean:write name="b" property="benutzer.email" /></a>|
          	&nbsp;|
          	<logic:notEmpty name="b" property="benutzer.telefonnrp"><bean:message key="showkontousers.telefon_p" />: <bean:write name="b" property="benutzer.telefonnrp" />|<logic:empty name="b" property="benutzer.telefonnrg">&nbsp;|</logic:empty></logic:notEmpty>
          	<logic:notEmpty name="b" property="benutzer.telefonnrg"><bean:message key="showkontousers.telefon_g" />: <bean:write name="b" property="benutzer.telefonnrg" />|&nbsp;|</logic:notEmpty>
          	<logic:equal name="b" property="benutzer.anrede" value="Frau"><bean:message key="adressen.anrede_frau" /></logic:equal><logic:equal name="b" property="benutzer.anrede" value="Herr"><bean:message key="adressen.anrede_herr" /></logic:equal>|
          	<bean:write name="b" property="benutzer.vorname" /> <bean:write name="b" property="benutzer.name" />|
          	<bean:write name="b" property="benutzer.institut" />|<bean:write name="b" property="benutzer.abteilung" />|
          	<bean:write name="b" property="benutzer.adresse" />|<bean:write name="b" property="benutzer.adresszusatz" />|
          	<bean:write name="b" property="benutzer.land" /><logic:notEmpty name="b" property="benutzer.plz">-<bean:write name="b" property="benutzer.plz" /></logic:notEmpty> <bean:write name="b" property="benutzer.ort" />" 
          	href="mailto:<bean:write name="b" property="benutzer.email" />?subject=<bean:message key="uebersicht.bookpartorder" />:%20%22<% out.println(tit.replaceAll("\"", "").replaceAll("'", "")); %>%22">
          	<bean:write name="b" property="benutzer.name" /> <bean:write name="b" property="benutzer.vorname" /></a>
        </logic:equal>
        &nbsp;
      </td>
      <td><bean:write name="b" property="artikeltitel" /><bean:write name="b" property="kapitel" />&nbsp;</td>
      <td>
      	<span class="title" title="<bean:write name="b" property="jahr" /><logic:notEmpty name="b" property="jahrgang">;<bean:write name="b" property="jahrgang" /></logic:notEmpty><logic:notEmpty name="b" property="heft">(<bean:write name="b" property="heft" />)</logic:notEmpty><logic:notEmpty name="b" property="seiten">:<bean:write name="b" property="seiten" /></logic:notEmpty>|
      		<bean:write name="b" property="autor" />|
      		<bean:write name="b" property="verlag" />|  
      		<bean:write name="b" property="issn" /><bean:write name="b" property="isbn" />|    		
		    <logic:notEmpty name="b" property="pmid">PMID: <a href='http://www.ncbi.nlm.nih.gov/pubmed/<bean:write name="b" property="pmid" />' target='_blank'><bean:write name="b" property="pmid" /></a>|</logic:notEmpty>
		    <logic:notEmpty name="b" property="doi">DOI: <a href='http://dx.doi.org/<bean:write name="b" property="doi" />' target='_blank'><bean:write name="b" property="doi" /></a>|</logic:notEmpty>
      		">
      		<bean:write name="b" property="zeitschrift" /><bean:write name="b" property="buchtitel" />&nbsp;
      	</span>
      </td>
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
