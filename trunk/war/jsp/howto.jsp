<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - HowTo OpenURL</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>
<bean:define id="openURL" type="java.lang.String"><bean:message bundle="systemConfig" key="server.installation"/></bean:define>
<bean:define id="allowRegisterLibraryAccount" type="java.lang.String"><bean:message bundle="systemConfig" key="allow.registerLibraryAccounts"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">

<table style="position:absolute; text-align:left; z-index:2;
  <logic:notPresent name="userinfo" property="benutzer">
    <logic:equal name="allowRegisterLibraryAccount" value="true">
      left:<bean:message key="submenupos.howto_logged_out" />px;
    </logic:equal>
    <logic:notEqual name="allowRegisterLibraryAccount" value="true">
      left:<bean:message key="submenupos.howto_logged_out_noRegister" />px;
    </logic:notEqual>
  </logic:notPresent>
  <logic:present name="userinfo" property="benutzer">
    <logic:equal name="userinfo" property="benutzer.rechte" value="1">
      left:<bean:message key="submenupos.howto_user" />px;
    </logic:equal>
    <logic:equal name="userinfo" property="benutzer.rechte" value="2">
      left:<bean:message key="submenupos.howto_librarian" />px;
    </logic:equal>
    <logic:equal name="userinfo" property="benutzer.rechte" value="3">
      left:<bean:message key="submenupos.howto_admin" />px;
    </logic:equal>  
  </logic:present>
  ">
  <tr>
    <td
      <logic:equal name="ActiveMenus" property="activesubmenu" value="OpenURL">id="submenuactive" nowrap</logic:equal>
      <logic:notEqual name="ActiveMenus" property="activesubmenu" value="OpenURL">id="submenu" nowrap</logic:notEqual>><a
      href="howto.do?activesubmenu=OpenURL&activemenu=howto">OpenURL</a></td>
    <td
      <logic:equal name="ActiveMenus" property="activesubmenu" value="bestellformular">id="submenuactive" nowrap</logic:equal>
      <logic:notEqual name="ActiveMenus" property="activesubmenu" value="bestellformular">id="submenu" nowrap</logic:notEqual>><a
      href="howto.do?activesubmenu=bestellformular&activemenu=howto"><bean:message key="bestellformconfigureselect.titel" /></a></td>
  </tr>
</table>

<br />
<h3><logic:equal name="ActiveMenus" property="activesubmenu"
  value="OpenURL">
OpenURL - Linkresolver
</logic:equal> <logic:equal name="ActiveMenus" property="activesubmenu"
  value="bestellformular">
    <bean:message key="bestellformconfigureselect.titel" /> - <bean:message key="bestellformconfigureselect.possible" />
</logic:equal>
</h3>


  <logic:equal name="ActiveMenus" property="activesubmenu"
    value="OpenURL">
      <p><b>Base-URL: <%=openURL%>/openurl.do</b></p>
      <p><bean:message arg0="<%=appName%>" key="openurl.intro" /></p>

      <p><bean:message arg0="<%=appName%>" key="openurl.anleitung" /></p>
      
      <ol>
        <li><bean:message arg0="<%=appName%>" key="openurl.newkonto" /></li>
        <li><a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/><bean:message key="openurl.ip.send.subject" />"><bean:message key="openurl.ip.send" /></a> <bean:message key="openurl.ip" /></li>
        <li><bean:message key="openurl.baseurl" /></li>
      </ol>

      <p><bean:message arg0="<%=appName%>" key="openurl.databases" /></p>
      
<table border="1" cellspacing="0px">
  <tr>
    <th id="th-left"><bean:message key="openurl.database" /></th>
    <th id="th-left"><bean:message key="openurl.kommentar" /></th>
  </tr>
  <tr>
    <td><a href="http://bibnet.org/vufind" target="_blank">bibnet.org</a>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Carelit&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>CINAHL / EBSCO HOST&nbsp;</td>
    <td><bean:message key="openurl.cinahl_query" /></td>
  </tr>
  <tr>
    <td>CSA&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Endnote&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>ISI Web of Science&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Ovid SP</td>
    <td><bean:message key="openurl.ovid_anleitung" /></td>
  </tr>
  <tr>
    <td>Pubmed</td>
    <td><bean:message key="openurl.pubmed_anleitung" /></td>
  </tr>
  <tr>
    <td>Refworks&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>ScienceDirect&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Scopus&nbsp;</td>
    <td><bean:message key="openurl.scopus_anleitung" /></td>
  </tr>
  <tr>
    <td>Springer&nbsp;</td>
    <td><bean:message key="openurl.springer_query" /></td>
  </tr>
  <tr>
    <td>UpToDate&nbsp;</td>
    <td><bean:message key="openurl.uptodate_anleitung" /></td>
  </tr>
</table>
      
      <p><bean:message key="openurl.gbv_anleitung" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/>"><bean:message key="openurl.kontakt" /></a> <bean:message key="openurl.kontakt.text" /></p>
      
   <h3>
     Demo
   </h3>
   <p><bean:message key="bestellformconfigureselect.test" /></p>
   
   <p><a href="http://www.doctor-doc.com/version1.0/openurl.do?kkid=medtest&sid=Entrez:PubMed&id=pmid:18221514">http://www.doctor-doc.com/version1.0/openurl.do?kkid=medtest&amp;sid=Entrez:PubMed&amp;id=pmid:18221514</a></p>
      

    
  </logic:equal>

  <logic:equal name="ActiveMenus" property="activesubmenu"
    value="bestellformular">
    
    <p><b>URL: <%=openURL%>/bestellform.do?method=validate</b></p>
    
    <p><bean:message arg0="<%=appName%>" key="bestellformconfigureselect.description" /> <bean:message arg0="<%=appName%>" key="bestellformconfigureselect.sprache" /></p>
    
    <p><bean:message key="bestellformconfigureselect.noconfig" /></p>
    <p><bean:message key="bestellformconfigureselect.possibleconfig" /></p>
<table border="1" cellspacing="0px">
  <tr>
    <th id="th-left"><bean:message key="bestellformconfigureselect.zugriff" /></th>
    <th id="th-left"><bean:message key="bestellformconfigureselect.szenario" /></th>
  </tr>
  <tr>
    <td><bean:message key="bestellformconfigureselect.ip" />&nbsp;</td>
    <td><bean:message key="bestellformconfigureselect.ip_explain" /></td>
  </tr>
  <tr>
    <td><bean:message key="bestellformconfigureselect.eingeloggt" />&nbsp;</td>
    <td><bean:message key="bestellformconfigureselect.eingeloggt_explain" /></td>
  </tr>
  <tr>
    <td><bean:message key="bestellformconfigureselect.kkid" />&nbsp;</td>
    <td><bean:message key="bestellformconfigureselect.kkid_explain" /></td>
  </tr>
  <tr>
    <td><bean:message key="bestellformconfigureselect.bkid" />&nbsp;</td>
    <td><bean:message key="bestellformconfigureselect.bkid_explain" /></td>
  </tr>
</table>

<p><bean:message key="bestellformconfigureselect.kontakt" /> <a href="mailto:<bean:message bundle="systemConfig" key="systemEmail.email"/>"><bean:message bundle="systemConfig" key="systemEmail.email"/></a></p>

	
   <h3>
     <bean:message key="bestellformconfigureselect.external.title" />
   </h3>
   
   <p><bean:message arg0="<%=appName%>" key="bestellformconfigureselect.external.text" />
   
   <a href="https://sourceforge.net/apps/mediawiki/doctor-doc/index.php?title=Help:Contents#Can_I_use_the_official_orderform_of_my_institution.3F" target="_blank"><bean:message key="bestellformconfigureselect.anleitung" /></a></p>
   
   <h3>
     Demo
   </h3>
   <p><bean:message key="bestellformconfigureselect.test" /></p>
   
   <p><a href="http://www.doctor-doc.com/version1.0/bestellform.do?method=validate&activemenu=bestellform&kkid=medtest">http://www.doctor-doc.com/version1.0/bestellform.do?method=validate&amp;activemenu=bestellform&amp;kkid=medtest</a></p>
    

  </logic:equal>
  
  <tiles:insert page="import/footer.jsp" flush="true" />
  
</div>
  
</body>
</html>

