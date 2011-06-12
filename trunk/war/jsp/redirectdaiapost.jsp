<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>
<%@ page contentType="text/html; charset=utf-8" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

<head>
  <title>Test</title>
</head>

<body onload="submit_form();">

<bean:define id="url" name="daiaparam" property="linkout" type="java.lang.String"/>


<form name="myform" action="<bean:write name="daiaparam" property="linkout"/>" method="post">

<logic:notEmpty name="daiaparam" property="mapMediatype">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapMediatype"/>" value="<bean:write name="ofjo" property="mediatype"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapAuthors">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapAuthors"/>" value="<bean:write name="ofjo" property="author"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapAtitle">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapAtitle"/>" value="<bean:write name="ofjo" property="artikeltitel" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapBtitle">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapBtitle"/>" value="<bean:write name="ofjo" property="buchtitel"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapChapter">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapChapter"/>" value="<bean:write name="ofjo" property="kapitel"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapJournal">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapJournal"/>" value="<bean:write name="ofjo" property="zeitschriftentitel"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapPublisher">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapPublisher"/>" value="<bean:write name="ofjo" property="verlag"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapIssn">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapIssn"/>" value="<bean:write name="ofjo" property="issn"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapIsbn">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapIsbn"/>" value="<bean:write name="ofjo" property="isbn"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapDate">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapDate"/>" value="<bean:write name="ofjo" property="jahr"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapVolume">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapVolume"/>" value="<bean:write name="ofjo" property="jahrgang"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapIssue">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapIssue"/>" value="<bean:write name="ofjo" property="heft"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapPages">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapPages"/>" value="<bean:write name="ofjo" property="seiten"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapPmid">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapPmid"/>" value="<bean:write name="ofjo" property="pmid"/>" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="mapDoi">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapDoi"/>" value="<bean:write name="ofjo" property="doi"/>" />
</logic:notEmpty>
  
  <p>You are being redirected automatically.</p>
  
  <p>If not, then you do not have JavaScript enabled.  
  Please click here: <input type="submit" value="Continue..." /></p>
  
</form>

<script language="javascript">
  <!--
  function submit_form() 
  {
 	document.myform.submit()
  }
  -->
</script>

</body>
</html>

