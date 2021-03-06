<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="issnresult.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<table style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
      </logic:notEqual>
  </tr>
</table>

<div class="content">

<br><br>


<p><bean:message key="issnresult.text" /></p>

<logic:present name="regensburg" property="zeitschriften">
  <p><i>powered by EZB Regensburg</i></p>
  
  <table class="border">
  <logic:iterate id="rb" name="regensburg" property="zeitschriften">
    <logic:present name="rb" property="issn">
      <tr>
        <td id="border"><a href="issnsearch_.do?method=prepareIssnSearch&issn=<bean:write name="rb" property="issn" />&artikeltitel=<bean:write name="rb" property="artikeltitel_encodedUTF8" />&zeitschriftentitel=<bean:write name="rb" property="zeitschriftentitel_encodedUTF8" /><logic:notEmpty name="rb" property="author">&author=<bean:write name="rb" property="author_encodedUTF8" /></logic:notEmpty><logic:notEmpty name="rb" property="jahr">&jahr=<bean:write name="rb" property="jahr" /></logic:notEmpty><logic:notEmpty name="rb" property="jahrgang">&jahrgang=<bean:write name="rb" property="jahrgang" /></logic:notEmpty><logic:notEmpty name="rb" property="heft">&heft=<bean:write name="rb" property="heft" /></logic:notEmpty><logic:notEmpty name="rb" property="seiten">&seiten=<bean:write name="rb" property="seiten" /></logic:notEmpty>&autocomplete=<bean:write name="orderform" property="autocomplete" />&runs_autocomplete=<bean:write name="orderform" property="runs_autocomplete" />"><bean:write name="rb" property="zeitschriftentitel" /></a></td> <td id="border"> ISSN: <bean:write name="rb" property="issn" /></td> <td id="border">(<a href="<bean:write name="rb" property="link" />" target="_blank">Info</a>) </td>
         </tr>
          </logic:present>
     </logic:iterate>
          
     <logic:iterate id="rb" name="regensburg" property="zeitschriften">
    	<logic:empty name="rb" property="link">
      		<bean:message key="issnresult.nohits" />
         </logic:empty>
     </logic:iterate>
     
         </table>
</logic:present>

            <p>____________________________________________________________________________</p>

<logic:present name="journalseek" property="zeitschriften">
  <p><i>powered by Journalseek</i></p>
  
  <table class="border">  
  <logic:iterate id="js" name="journalseek" property="zeitschriften">
    <logic:present name="js" property="issn">
  <tr>
    <td id="border"><a href="issnsearch_.do?method=prepareIssnSearch&issn=<bean:write name="js" property="issn" />&artikeltitel=<bean:write name="js" property="artikeltitel_encodedUTF8" />&zeitschriftentitel=<bean:write name="js" property="zeitschriftentitel_encodedUTF8" /><logic:notEmpty name="js" property="author">&author=<bean:write name="js" property="author_encodedUTF8" /></logic:notEmpty><logic:notEmpty name="js" property="jahr">&jahr=<bean:write name="js" property="jahr" /></logic:notEmpty><logic:notEmpty name="js" property="jahrgang">&jahrgang=<bean:write name="js" property="jahrgang" /></logic:notEmpty><logic:notEmpty name="js" property="heft">&heft=<bean:write name="js" property="heft" /></logic:notEmpty><logic:notEmpty name="js" property="seiten">&seiten=<bean:write name="js" property="seiten" /></logic:notEmpty>&autocomplete=<bean:write name="orderform" property="autocomplete" />&runs_autocomplete=<bean:write name="orderform" property="runs_autocomplete" />"><bean:write name="js" property="zeitschriftentitel" /></a></td> <td id="border"> ISSN: <bean:write name="js" property="issn" /></td> <td id="border">(<a href="<bean:write name="js" property="link" />" target="_blank">Info</a>) </td>
  </tr>
          </logic:present>    
     </logic:iterate>
     
     <logic:iterate id="js" name="journalseek" property="zeitschriften">
    <logic:notPresent name="js" property="issn">
      <bean:message key="issnresult.nohits" />
          </logic:notPresent>
     </logic:iterate>     
     
       </table>         
</logic:present>

<p></p>

<logic:notPresent name="userinfo" property="konto">
  <p><bean:message key="error.timeout" /></p>
  <p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>

</div>

 </body>
</html>
