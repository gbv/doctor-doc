<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>
<%@ page contentType="text/html; charset=ISO-8859-1" %>

<!DOCTYPE html>

<html>

<head>
  <title>Redirect</title>
</head>

<body onload="submit_form();">

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
<logic:notEmpty name="daiaparam" property="mapReference">
  <input type="hidden" name="<bean:write name="daiaparam" property="mapReference"/>" value="<bean:write name="daiaparam" property="referenceValue"/>" />
</logic:notEmpty>

<logic:notEmpty name="daiaparam" property="free1">
  <input type="hidden" name="<bean:write name="daiaparam" property="free1"/>" value="<bean:write name="daiaparam" property="free1Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free2">
  <input type="hidden" name="<bean:write name="daiaparam" property="free2"/>" value="<bean:write name="daiaparam" property="free2Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free3">
  <input type="hidden" name="<bean:write name="daiaparam" property="free3"/>" value="<bean:write name="daiaparam" property="free3Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free4">
  <input type="hidden" name="<bean:write name="daiaparam" property="free4"/>" value="<bean:write name="daiaparam" property="free4Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free5">
  <input type="hidden" name="<bean:write name="daiaparam" property="free5"/>" value="<bean:write name="daiaparam" property="free5Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free6">
  <input type="hidden" name="<bean:write name="daiaparam" property="free6"/>" value="<bean:write name="daiaparam" property="free6Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free7">
  <input type="hidden" name="<bean:write name="daiaparam" property="free7"/>" value="<bean:write name="daiaparam" property="free7Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free8">
  <input type="hidden" name="<bean:write name="daiaparam" property="free8"/>" value="<bean:write name="daiaparam" property="free8Value" />" />
</logic:notEmpty>
<logic:notEmpty name="daiaparam" property="free9">
  <input type="hidden" name="<bean:write name="daiaparam" property="free9"/>" value="<bean:write name="daiaparam" property="free9Value" />" />
</logic:notEmpty>

  <p><bean:message key="redirect.auto"/>.</p>
  
  <p><bean:message key="redirect.auto.alt"/>: <input type="submit" value="<bean:message key="findfree.submit"/>" /></p>
  
</form>

<script type="text/javascript">
  function submit_form()
  {
 	document.myform.submit();
  }
</script>

</body>
</html>

