<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<%--********  Konto wechseln  ********--%>
<logic:present name="userinfo" property="kontos">
  <html:form action="/changekonto">
    <select name="kid" onchange="this.form.submit()">
       <logic:iterate id="ui" name="userinfo" property="kontos">
      <bean:define id="tmp" name="userinfo" property="konto.bibliotheksname" type="java.lang.String"/>
           <option value="<bean:write name="ui" property="id" />" <logic:notEqual name="ui" property="bibliotheksname" value="<%=tmp%>">selected</logic:notEqual> ><bean:write name="ui" property="bibliotheksname" /></option>
         </logic:iterate>
    </select>
    <input type="hidden" name="method" value="changekonto">
    <input type="submit" name="action "value="<bean:message key="kontochange.submit" />">
  </html:form>
</logic:present>

