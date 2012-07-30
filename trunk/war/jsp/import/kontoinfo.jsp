<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


      <logic:present name="userinfo" property="konto">
        <p>
        <logic:notEqual name="userinfo" property="kontoanz" value="1">
          <tiles:insert page="kontochange.jsp" flush="true" />
        </logic:notEqual>        
        </p>
        <h4><bean:write name="userinfo" property="konto.bibliotheksname" /> | <bean:write name="userinfo" property="konto.telefon" /> | 
        <a href="mailto:<bean:write name="userinfo" property="konto.bibliotheksmail" />"><font color="white"><bean:write name="userinfo" property="konto.bibliotheksmail" /></font></a></h4>
      </logic:present>
      
      <logic:notPresent name="userinfo" property="konto">
        <logic:present name="logolink">
          <table align="left">
            <tr>
              <td>
                <img class="instlogo" src='<bean:write name="logolink" />' />
              </td>
            </tr>          
          </table>
        </logic:present>
      </logic:notPresent>  


      