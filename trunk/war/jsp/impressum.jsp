<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="impressum.header" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
 
<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">
 <br>
 <h3><bean:message key="impressum.titel" /></h3>
 
<table width="800">
  <tr>
    <td><b>Open Source</b></td>
  </tr>
  <tr>
    <td><bean:message arg0="<%=appName%>" key="anmeldungtyp.kosten" /></td>  
  </tr>
  <tr><td><br></td></tr>
  <tr>
    <td><b>Hosting</b></td>
  </tr>
  <tr>
    <td><bean:message arg0="<%=appName%>" key="impressum.service" /> <bean:message key="impressum.programmierung" /></td>  
  </tr>
  <tr><td><br></td></tr>
  <tr>
  <td><b><bean:message key="impressum.contact" /></b></td>
  </tr>
  <tr>
    <td><bean:message arg0="<%=appName%>" key="impressum.entwicklung" /></td>  
  </tr>
  <tr><td><br></td></tr>
  <tr>
    <td>
       <a href="https://lists.sourceforge.net/lists/listinfo/doctor-doc-general" target="_blank">https://lists.sourceforge.net/lists/listinfo/doctor-doc-general</a>
    </td>
  </tr>
  <tr>
    <td>
       <a href="https://lists.sourceforge.net/lists/listinfo/doctor-doc-tech" target="_blank">https://lists.sourceforge.net/lists/listinfo/doctor-doc-tech</a>
    </td>
  </tr>
  <tr><td><br></td></tr>
  <tr>
    <td><b><bean:message key="impressum.presse" /></b></td>
  </tr>
    <tr>
      <td>
        <ul>
          <li><a href="http://prezi.com/naimoi1tak3j/doctor-doc-amb/" target="_blank">Prezi</a></li>
          <li><a href="http://www.gbv.de/wikis/cls/Doctor-Doc-GBV-Schnittstelle" target="_blank">GBV-Wiki</a></li>
          <li><a href="http://www.egms.de/static/de/journals/mbi/2010-10/mbi000189.shtml" target="_blank">GMS - German Medical Science: AGMB Meeting 2009</a></li>
          <li><a href="https://sourceforge.net/apps/mediawiki/doctor-doc/index.php?title=Main_Page" target="_blank">Wiki Doctor-Doc</a></li>
          <li><a href="https://sourceforge.net/apps/mediawiki/doctor-doc/index.php?title=Help:Contents" target="_blank">Help Pages</a></li>
        </ul>
      </td>
    </tr>
  <tr>
    <td><b><bean:message key="impressum.newsletter" /></b></td>
  </tr>
    <tr>
      <td>
        <ul>
          <li><a href="http://sourceforge.net/mailarchive/forum.php?forum_name=doctor-doc-general" target="_blank">News</a></li>
          <li><a href="http://www.doctor-doc.com/Newsletter6.pdf">Newsletter 6</a> (26-Mai-2010)</li>
          <li><a href="http://www.doctor-doc.com/Newsletter5.pdf">Newsletter 5</a> (04-Jan-2010)</li>
          <li><a href="http://www.doctor-doc.com/Newsletter4.pdf">Newsletter 4</a> (24-Aug-2009)</li>
          <li><a href="http://www.doctor-doc.com/Newsletter3.pdf">Newsletter 3</a> (11-Jun-2009)</li>
          <li><a href="http://www.doctor-doc.com/Newsletter2.pdf">Newsletter 2</a> (13-Apr-2009)</li>
          <li><a href="http://www.doctor-doc.com/Newsletter.pdf">Newsletter 1</a> (08-<bean:message key="impressum.march" />-2009)</li>
        </ul>
      </td>
    </tr>
    <tr><td><br></td></tr>
    <tr>
    	<td>
    		<script type="text/javascript" src="http://www.ohloh.net/p/483914/widgets/project_thin_badge.js"></script>
    	</td>    
    </tr>

</table>

<tiles:insert page="import/footer.jsp" flush="true" />

</div>

 
 </body>
</html>
