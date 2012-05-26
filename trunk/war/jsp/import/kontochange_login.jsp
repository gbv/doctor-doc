<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<%--********  Konto wechseln  ********--%>
<logic:present name="userinfo" property="kontos">
  <html:form action="/changekonto">
    <select name="kid" onchange="this.form.submit()">
       <logic:iterate id="ui" name="userinfo" property="kontos">
           <option value="<bean:write name="ui" property="id" />"><bean:write name="ui" property="bibliotheksname" /></option>
         </logic:iterate>
    </select>
    <p>
    <input type="hidden" name="method" value="changekonto" />
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
       <input type="hidden" name="artikeltitel_encoded" value="<bean:write name="orderform" property="artikeltitel_encoded" />" />
       <input type="hidden" name="author_encoded" value="<bean:write name="orderform" property="author_encoded" />" />
       <input type="hidden" name="foruser" value="<bean:write name="orderform" property="foruser" />" />
    </logic:present>
    <logic:present name="userform">
       <logic:present name="userform" property="name">
         <input type="hidden" name="kundenname" value="<bean:write name="userform" property="name" />" />
       </logic:present>
       <logic:present name="userform" property="vorname">
         <input type="hidden" name="kundenvorname" value="<bean:write name="userform" property="vorname" />" />
       </logic:present>
       <logic:present name="userform" property="email">
         <input type="hidden" name="kundenemail" value="<bean:write name="userform" property="email" />" />
       </logic:present>
       <logic:present name="userform" property="institut">
         <input type="hidden" name="kundeninstitution" value="<bean:write name="userform" property="institut" />" />
       </logic:present>
       <logic:present name="userform" property="abteilung">
         <input type="hidden" name="kundenabteilung" value="<bean:write name="userform" property="abteilung" />" />
       </logic:present>
       <logic:present name="userform" property="category">
         <input type="hidden" name="category" value="<bean:write name="userform" property="category" />" />
       </logic:present>
       <logic:present name="userform" property="telefonnrg">
         <input type="hidden" name="kundentelefon" value="<bean:write name="userform" property="telefonnrg" />" />
       </logic:present>
       <logic:present name="userform" property="adresse">
         <input type="hidden" name="kundenadresse" value="<bean:write name="userform" property="adresse" />" />
       </logic:present>
       <logic:present name="userform" property="plz">
         <input type="hidden" name="kundenplz" value="<bean:write name="userform" property="plz" />" />
       </logic:present>
       <logic:present name="userform" property="ort">
         <input type="hidden" name="kundenort" value="<bean:write name="userform" property="ort" />" />
       </logic:present>
       <logic:present name="userform" property="land">
         <input type="hidden" name="kundenland" value="<bean:write name="userform" property="land" />" />
       </logic:present>
     </logic:present>
    <input type="submit" name="action "value="<bean:message key="kontochange.submit_login" />"></p>
  </html:form>
</logic:present>
