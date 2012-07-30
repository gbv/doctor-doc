<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message key="redirectbl.header" /></title>
   
 </head>
 <body>
 
 <bean:message key="redirectbl.header" />!
 
 <bean:define id="url" name="daiaparam" property="linkout" type="java.lang.String"/>
 
 <% response.sendRedirect(url); %>
 
 
 </body>
</html>

