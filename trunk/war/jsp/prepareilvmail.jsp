<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>
<html> 
 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="order.detailtitle" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  <style type="text/css">
		textarea { width: 100%; border-width: 0; }
  </style> 
 </head>
 <body>
  
 <tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

  <h3>Mail:</h3> <!-- Mailform noch internationalisieren -->
    
<logic:present name="IlvReportForm" property="labelto">   
  <form action="send-ilv-mailorder.do?method=sendIlvMail">
  <table border="1">
  <tr>
    <td valign="top">
    	to: 
    </td>

    <td>
    	<input name="to" type="text" size="100" value="Mailadresse@nochabfüllen.ch" />    		
    </td>
    
  </tr>
  <tr>
    <td>Betreff: 
    </td>    
    <td>
    <input name="betreff" type="text" size="100" value="Betreff noch abfüllen" autofocus />
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <textarea name="text" cols="25" rows="5">Text noch abfüllen</textarea>
    </td>
  </tr>
  
  </table>
  <p />

  <input type="hidden" name="method" value="sendIlvMail" />
  
  <input type="hidden" name="title" value="<bean:message key="ilv-report.title" />" />
  <input type="hidden" name="to" value="<bean:write name="IlvReportForm" property="lieferant" />" />  
  <input type="hidden" name="signatur" value="<bean:write name="IlvReportForm" property="signatur" />" />
  <input type="hidden" name="journaltitel" value="<bean:write name="IlvReportForm" property="journaltitel" />" />  
  <input type="hidden" name="customer" value="<bean:write name="IlvReportForm" property="name" />" />  
  <input type="hidden" name="librarycard" value="<bean:write name="IlvReportForm" property="librarycard" />" />  
  <input type="hidden" name="issn" value="<bean:write name="IlvReportForm" property="issn" />" />
  <input type="hidden" name="pmid" value="<bean:write name="IlvReportForm" property="pmid" />" />
  <input type="hidden" name="year" value="<bean:write name="IlvReportForm" property="year" />" />
  <input type="hidden" name="volumevintage" value="<bean:write name="IlvReportForm" property="volumevintage" />" />
  <input type="hidden" name="booklet" value="<bean:write name="IlvReportForm" property="booklet" />" />
  <input type="hidden" name="clinicinstitutedepartment" value="<bean:write name="IlvReportForm" property="clinicinstitutedepartment" />" />
  <input type="hidden" name="phone" value="<bean:write name="IlvReportForm" property="phone" />" />  
  <input type="hidden" name="pages" value="<bean:write name="IlvReportForm" property="pages" />" />
  <input type="hidden" name="authorofessay" value="<bean:write name="IlvReportForm" property="authorofessay" />" />
  <input type="hidden" name="titleofessay" value="<bean:write name="IlvReportForm" property="titleofessay" />" />
  <input type="hidden" name="notesfromrequestinglibrary" value="<bean:write name="IlvReportForm" property="notesfromrequestinglibrary" />" />
  <input type="hidden" name="post" value="<bean:write name="IlvReportForm" property="post" />" />
    
  <input type="hidden" name="reporttitle" value="<bean:message key="ilv-report.title" />" />
  <input type="hidden" name="labelfrom" value="<bean:message key="ilv-report.labelfrom" />" />
  <input type="hidden" name="labelto" value="<bean:message key="ilv-report.labelto" />" />
  <input type="hidden" name="labelsignatur" value="<bean:message key="stockimport.sig" />" />
  <input type="hidden" name="labeljournaltitel" value="<bean:message key="ilv-report.labeljournaltitel" />" />
  <input type="hidden" name="labelcustomer" value="<bean:message key="ilv-report.labelcustomer" />" />
  <input type="hidden" name="labelname" value="<bean:message key="ilv-report.labelname" />" />
  <input type="hidden" name="labellibrarycard" value="<bean:message key="ilv-report.labellibrarycard" />" />
  <input type="hidden" name="labelissn" value="<bean:message key="ilv-report.labelissn" />" />
  <input type="hidden" name="labelpmid" value="<bean:message key="ilv-report.labelpmid" />" />
  <input type="hidden" name="labelyear" value="<bean:message key="ilv-report.labelyear" />" />
  <input type="hidden" name="labelvolumevintage" value="<bean:message key="ilv-report.labelvolumevintage" />" />
  <input type="hidden" name="labelbooklet" value="<bean:message key="ilv-report.labelbooklet" />" />
  <input type="hidden" name="labelclinicinstitutedepartment" value="<bean:message key="ilv-report.labelclinicinstitutedepartment" />" />
  <input type="hidden" name="labelphone" value="<bean:message key="ilv-report.labelphone" />" />
  <input type="hidden" name="labelfax" value="<bean:message key="ilv-report.labelfax" />" />
  <input type="hidden" name="labelsendto" value="<bean:message key="ilv-report.labelsendto" />" />
  <input type="hidden" name="labelpages" value="<bean:message key="ilv-report.labelpages" />" />
  <input type="hidden" name="labelauthorofessay" value="<bean:message key="ilv-report.labelauthorofessay" />" />
  <input type="hidden" name="labeltitleofessay" value="<bean:message key="ilv-report.labeltitleofessay" />" />
  <input type="hidden" name="labelendorsementsofdeliveringlibrary" value="<bean:message key="ilv-report.labelendorsementsofdeliveringlibrary" />" />
  <input type="hidden" name="labelnotesfromrequestinglibrary" value="<bean:message key="ilv-report.labelnotesfromrequestinglibrary" />" />
  <input type="submit" value="<bean:message key="ilv-report.submit" />" />
  </form>
  
   </logic:present>
 </div>
 </body>
</html> 
