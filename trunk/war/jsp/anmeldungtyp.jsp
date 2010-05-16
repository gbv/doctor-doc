<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="anmeldungtyp.header" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />					
<h3><bean:message key="anmeldungtyp.titel" /></h3>

<p><b><bean:message arg0="<%=appName%>" key="anmeldungtyp.intro" /></b></p>

<table border="1" cellspacing="0px">
<tr><th id="th-left"><bean:message bundle="systemConfig" key="application.name"/></th><th style="text-align:center;"><bean:message key="anmeldungtyp.free" /></th><th style="text-align:center;"><bean:message key="anmeldungtyp.faxserver" /></th></tr>
<tr><td><bean:message key="anmeldungtyp.web" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.issn" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.autocomplete" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.availability" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.subito" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.gbv" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.books" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.preise" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.pdf" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.ilv" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.suche" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.kunden" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.berechtigungen" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.statistik" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.dropdown" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.ip" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.kennung" />&nbsp;</td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.openurl" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.linkresolver" /></td><td align="center"><img src="img/checkboxchecked.png" /></td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td><bean:message key="anmeldungtyp.faxtopdf" /></td><td align="center">&nbsp;</td><td align="center"><img src="img/checkboxchecked.png" /></td></tr>
<tr><td colspan="3"><p><bean:message arg0="<%=appName%>" key="anmeldungtyp.nocosts" /><p /></td></tr>
</table>

<p><b><bean:message key="anmeldungtyp.register" /></b></p>

<tiles:insert page="import/footer.jsp" flush="true" />

</div>

 </body>
</html>
