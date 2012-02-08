<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="searchorders.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">
 <br />

<h3><bean:message key="searchorders.header" /></h3>
  
  <bean:message key="searchorders.comment" />

<html:form action="searchorder.do?method=search" method="post">
  <table>
  
  <tr><td><br /></td></tr>
  
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
  
  <tr><td><br /></td></tr>
  
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
  
  <tr><td><br /></td></tr>
 <!-- 
    <tr>
    <td>
      <select name="value5" size="1">
        <option value="state" selected="selected">Status aktuell</option>
      </select>
    </td>
    <td>
      <select name="condition5" size="1">
        <option value="is" selected="selected">ist gleich</option>
        <option value="is not">ist nicht</option>
      </select>
    </td>
    <td>
      <select name="input5">
          <option value="0" selected="selected">Status ausw&auml;hlen</option>
         <logic:iterate id="s" name="overviewform" property="statitexts">
             <option value="<bean:write name="s" property="inhalt" />"><bean:write name="s" property="inhalt" /></option>
           </logic:iterate>
    </select>
    </td>
    <td>
      <select name="boolean5" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select></td>
  </tr>
    <tr>
    <td>
      <select name="value6" size="1">
        <option value="statedate">Bestelldatum</option>
        <option value="orderdate" selected="selected">Statusdatum aktuell</option>
      </select>
    </td>
    <td>
      <select name="condition6" size="1">
        <option value="is" selected="selected">ist gleich</option>
        <option value="higher">ist gr&ouml;sser</option>
        <option value="smaller">ist kleiner</option>
      </select>
    </td>
    <td>
      <input name="input6" value="" type="text" size="50" maxlength="10">
    </td>
    <td>
      <select name="boolean6" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select> tt.mm.yyyy</td>
  </tr>
      <tr>
    <td>
      <select name="value7" size="1">
        <option value="statedate" selected="selected">Bestelldatum</option>
        <option value="orderdate">Statusdatum aktuell</option>
      </select>
    </td>
    <td>
      <select name="condition7" size="1">
        <option value="is" selected="selected">ist gleich</option>
        <option value="higher">ist gr&ouml;sser</option>
        <option value="smaller">ist kleiner</option>
      </select>
    </td>
    <td>
      <input name="input7" value="" type="text" size="50" maxlength="10">
    </td>
    <td>
      <select name="boolean7" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select> tt.mm.yyyy</td>
  </tr>
  
  
  <tr><td><br></td></tr>
  
    <tr>
        <td>
      <select name="value8" size="1">
        <option value="state" selected="selected">Status History</option>
      </select>
    </td>
    <td>
      <select name="condition8" size="1">
        <option value="contains" selected="selected">enth&auml;lt</option>
        <option value="contains not">enth&auml;lt nicht</option>
      </select>
    </td>
    <td>
      <select name="input8">
          <option value="0" selected="selected">Status ausw&auml;hlen</option>
         <logic:iterate id="s" name="overviewform" property="statitexts">
             <option value="<bean:write name="s" property="inhalt" />"><bean:write name="s" property="inhalt" /></option>
           </logic:iterate>
    </select>
    </td>
    <td>
      <select name="boolean8" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select></td>
  </tr>
      <tr>
    <td>
      <select name="value9" size="1">
        <option value="orderdate">Statusdatum History</option>
      </select>
    </td>
    <td>
      <select name="condition9" size="1">
        <option value="is" selected="selected">ist gleich</option>
        <option value="higher">ist gr&ouml;sser</option>
        <option value="smaller">ist kleiner</option>
      </select>
    </td>
    <td>
      <input name="input9" value="" type="text" size="50" maxlength="10">
    </td>
    <td>
      <select name="boolean9" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select> tt.mm.yyyy</td>
  </tr>

  <tr>
    <td>
      <select name="value10" size="1">
        <option value="user" selected="selected">bearb. durch</option>
        <option value="systembemerkungen">Systembemerk.</option>
      </select>
    </td>
    <td>
      <select name="condition10" size="1">
        <option value="contains" selected="selected">enth&auml;lt</option>
        <option value="is">ist gleich</option>
        <option value="is not">ist nicht gleich</option>
      </select>
    </td>
    <td>
      <input name="input10" value="" type="text" size="50" maxlength="100">
    </td>
    <td>
      <select name="boolean10" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select></td>
  </tr>
  <tr>
    <td>
      <select name="value11" size="1">
        <option value="user">bearb. durch</option>
        <option value="systembemerkungen" selected="selected">Systembemerk.</option>
      </select>
    </td>
    <td>
      <select name="condition11" size="1">
        <option value="contains" selected="selected">enth&auml;lt</option>
        <option value="is">ist gleich</option>
        <option value="is not">ist nicht gleich</option>
      </select>
    </td>
    <td>
      <input name="input11" value="" type="text" size="50" maxlength="100">
    </td>
    <td>
      &nbsp;und
    </td>
  </tr>
  
  <tr><td><br></td></tr>

    <tr>
    <td>
      <select name="value12" size="1">
        <option value="price" selected="selected">Preis</option>
      </select>
    </td>
    <td>
      <select name="condition12" size="1">
        <option value="is" selected="selected">ist gleich</option>
        <option value="higher">ist gr&ouml;sser</option>
        <option value="smaller">ist kleiner</option>
      </select>
    </td>
    <td>
      <input name="input12" value="" type="text" size="50" maxlength="9">
    </td>
    <td>
      <select name="boolean12" size="1">
        <option value="and" selected="selected">und</option>
        <option value="or">oder</option>
      </select>
    </td>
  </tr>
    <tr>
    <td>
      <select name="value13" size="1">
        <option value="currency" selected="selected">W&auml;hrung</option>
      </select>
    </td>
    <td>
      <select name="condition13" size="1">
        <option value="is" selected="selected">ist gleich</option>
      </select>
    </td>
    <td>
      <select name="input13" size="1">
        <option value="0" selected="selected">W&auml;hrung ausw&auml;hlen</option>
        <logic:iterate id="w" name="overviewform" property="waehrungen">
        <bean:define id="tmp" name="w" property="inhalt" type="java.lang.String"/>
          <option value="<bean:write name="w" property="inhalt" />"<logic:equal name="overviewform" property="waehrungen" value="<%=tmp%>"> selected</logic:equal>><bean:write name="w" property="inhalt" /></option>
         </logic:iterate>
        </select>
    </td>
  </tr>
  
    <tr><td><br></td></tr>
 -->     
  <tr>
  <td><input type="submit" value="<bean:message key="searchorders.submit" />"></input></td>
  </tr>
  
  </table>
  
</html:form>

</div>


 </body>
</html>
