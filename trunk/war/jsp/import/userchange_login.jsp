<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--********  Konto wechseln  ********--%>
<logic:present name="authuserlist" property="userinfolist">
  <html:form action="/setuser">
    <logic:iterate id="ui" name="authuserlist" property="userinfolist">           
         <input type="radio" name="userid" value="<bean:write name="ui" property="benutzer.id" />"> 
            <logic:iterate id="kl" name="ui" property="kontos">
              <bean:write name="kl" property="bibliotheksname" /> 
            </logic:iterate>
         <br>
       </logic:iterate>
    <p>
    <input type="hidden" name="method" value="setuser" />
    <logic:present name="orderform">
    <input type="hidden" name="issn" value="<bean:write name="orderform" property="issn" />" />
    <input type="hidden" name="resolver" value="true" />
     <input type="hidden" name="mediatype" value="<bean:write name="orderform" property="mediatype" />" />
     <input type="hidden" name="jahr" value="<bean:write name="orderform" property="jahr" />" />
     <input type="hidden" name="jahrgang" value="<bean:write name="orderform" property="jahrgang" />" />
     <input type="hidden" name="heft" value="<bean:write name="orderform" property="heft" />" />
     <input type="hidden" name="seiten" value="<bean:write name="orderform" property="seiten" />" />
     <input type="hidden" name="isbn" value="<bean:write name="orderform" property="isbn" />" />
     <input type="hidden" name="artikeltitel" value="<bean:write name="orderform" property="artikeltitel" />" />
     <input type="hidden" name="zeitschriftentitel" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
     <input type="hidden" name="author" value="<bean:write name="orderform" property="author" />" />
     <input type="hidden" name="kapitel" value="<bean:write name="orderform" property="kapitel" />" />
     <input type="hidden" name="buchtitel" value="<bean:write name="orderform" property="buchtitel" />" />
     <input type="hidden" name="verlag" value="<bean:write name="orderform" property="verlag" />" />
     <input type="hidden" name="rfr_id" value="<bean:write name="orderform" property="rfr_id" />" />
     <input type="hidden" name="genre" value="<bean:write name="orderform" property="genre" />" />
     <input type="hidden" name="pmid" value="<bean:write name="orderform" property="pmid" />" />
     <input type="hidden" name="doi" value="<bean:write name="orderform" property="doi" />" />
     <input type="hidden" name="sici" value="<bean:write name="orderform" property="sici" />" />
     <input type="hidden" name="lccn" value="<bean:write name="orderform" property="lccn" />" />
     <input type="hidden" name="zbid" value="<bean:write name="orderform" property="zdbid" />" />
     <input type="hidden" name="foruser" value="<bean:write name="orderform" property="foruser" />" />
    </logic:present>
    <html:submit property="action" value="go" /></p>
  </html:form>
</logic:present>
