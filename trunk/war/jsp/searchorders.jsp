<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="searchorders.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">
 <br>

<h3><bean:message key="searchorders.header" /></h3>
  
  <bean:message key="searchorders.comment" />

<html:form action="searchorder.do?method=search" method="post">
  <table>
  
  <tr><td><br></td></tr>
  
  <tr>
    <td align=center>
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
</td>    
  </tr>
</table>
<table>
  
  <tr><td><br></td></tr>
  
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
      <input name="input1" value="" type="search" size="50" maxlength="100" />
    </td>
    <td>
      <select name="boolean1" size="1">
        <option value="and" selected="selected"><bean:message key="searchorders.and" /></option>
        <option value="or"><bean:message key="searchorders.or" /></option>
      </select></td>
  </tr>
  <tr>
    <td>
      <select name="value2" >
      <option value="searchorders.all"><bean:message key="searchorders.all" /></option>
      <logic:iterate id="currentItem" name="sortedSearchFields">
        <bean:define id="itemValue" name="currentItem" property="value" type="java.lang.String"/>
      <option value="<%= itemValue %>"><bean:write name="currentItem" property="key"/></option>
      </logic:iterate>
    </select>
    </td>
    <td>
      <select name="condition2" size="1">
        <option value="contains" selected="selected"><bean:message key="uebersicht.contains" /></option>
        <option value="contains not"><bean:message key="uebersicht.contains_not" /></option>
        <option value="is"><bean:message key="uebersicht.is" /></option>
        <option value="is not"><bean:message key="uebersicht.is_not" /></option>
      </select>
    </td>
    <td>
      <input name="input2" value="" type="search" size="50" maxlength="100" />
    </td>
    <td>
      &nbsp;
    </td>
  </tr> 
  
  <tr><td><br></td></tr>
    
  <tr>
  <td><input type="submit" value="<bean:message key="searchorders.submit" />"></td>
  </tr>
  
  </table>
  
</html:form>

</div>


 </body>
</html>
