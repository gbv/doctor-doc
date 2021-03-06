<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - Internetsuche</title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />
<div class="content"><br>

<logic:present name="userinfo" property="konto">
  <h3>Bitte Captcha aufl&ouml;sen!</h3>
  
  <html:form action="journalorder1.do" method="post" focus="artikeltitel">
   
  <table>
    <tr>
      <td><p></p>Captcha-Bild: </td><td><img src='<bean:write name="message" property="link" />' alt='Captcha' border='0'/><p></p></td>
    </tr>
    <tr>
      <td><p></p>Text eingeben: </td><td><input name="captcha_text" type="text" size="98" /><p></p></td>
    </tr>
    <tr>
      <td><input type="submit" value="weiter"></td>
    </tr>
  </table>
  <input name="method" type="hidden" value="findForFree" />
  <input name="artikeltitel" type="hidden" value="<bean:write name="orderform" property="artikeltitel" />" />
  <input name="mediatype" type="hidden" value="<bean:write name="orderform" property="mediatype" />" />
  <input name="captcha_id" type="hidden" value="<bean:write name="message" property="message" />" />
  </html:form>
  <br>
  <br>
  <br>
  <div id="italic" align="center">Um Weiterzufahren geben Sie bitte den Text im Captcha-Bild ein.</div>
  
</logic:present>
<p></p>

<logic:notPresent name="userinfo" property="konto">
  <p>Session Timeout! Bitte erneut einloggen:</p>
  <p></p><a href="login.do">zur&uuml;ck</a>
</logic:notPresent>

</div>

 </body>
</html>
