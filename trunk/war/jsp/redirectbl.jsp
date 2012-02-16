<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message key="redirectbl.titel" /></title>
   
 </head>
 <body>
 
 <bean:message key="redirectbl.header" />!
 
 <bean:define id="url" name="orderform" property="link" type="java.lang.String"/>
 
 <% response.sendRedirect(url); %>
 
 
 </body>
</html>

