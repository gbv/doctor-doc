<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <meta name="robots" content="noindex" />
  <title> <bean:message key="server.memory" /> </title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br>
<h3>Memory usage:</h3>

<br>
<logic:present name="memoryform" property="freeMemory">
   Free memory (approximation to the total amount of memory currently available for future allocated objects): <bean:write name="memoryform" property="freeMemory" /> MB<br>
   Memory used (in JMV): <bean:write name="memoryform" property="memoryUsed" /> MB<br>
   Total memory (total amount of memory in the Java virtual machine. The value returned by this may vary over time, depending on the host environment): <bean:write name="memoryform" property="totalMemory" /> MB<br>
   Max memory (maximum amount of memory that the Java virtual machine will attempt to use): <bean:write name="memoryform" property="maxMemory" /> MB<br>
</logic:present>
  
        
</div>
 
 </body>
</html>
