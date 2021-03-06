<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="issnsearch.titel" /></title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<table
  style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
      </logic:notEqual>
  </tr>
</table>

<div class="content"><br>
<br>

<logic:present name="userinfo" property="konto">

  <h3><bean:message key="issnsearch.header1" /></h3>


  <table>
  
    <html:form action="findissn.do" method="post"
      focus="zeitschriftentitel">
      <logic:present name="orderform" property="mediatype">
        <logic:equal name="orderform" property="mediatype" value="Artikel">
          <logic:empty name="orderform" property="issn">
            <tr>
              <td colspan="2">
              <div id="italic"><bean:message key="issnsearch.issn_comment" /></div>
              </td>
            </tr>
          </logic:empty>
          <logic:present name="orderform" property="artikeltitel">
            <tr>
              <td><bean:message key="bestellform.artikeltitel" />:</td>
              <td><bean:write name="orderform" property="artikeltitel" /></td>
            </tr>
          </logic:present>
          <tr>
          <td title="<bean:message key="info.pubmed" />">PMID<img border="0" src="img/info.png" alt="<bean:message key="info.pubmed" />" /></td>
          <td><input name="pmid" type="text"
            value="" 
            size="9" maxlength="15" /> <bean:message key="searchorders.or" /></td>
        </tr>
          <tr>
            <td><bean:message key="bestellform.issn" /></td>
            <td><input name="issn" value="" type="text" size="9"
              maxlength="9" /> <bean:message key="searchorders.or" /></td>
          </tr>
          <tr>
            <td><bean:message key="bestellform.zeitschrift" /></td>
            <td><input name="zeitschriftentitel" value="" type="text"
              size="60" maxlength="100" /> <bean:message key="issnsearch.zeitschrift_example" /></td>
          </tr>

          <logic:notEmpty name="orderform" property="issn">
            <tr>
              <td></td>
              <td><input type="submit" value="<bean:message key="issnsearch.submit_neu_suchen" />"></td>
            </tr>
          </logic:notEmpty>
          <logic:empty name="orderform" property="issn">
            <tr>
              <td></td>
              <td><input type="submit" value="<bean:message key="issnsearch.submit_fehlende_issn" />"></td>
            </tr>
          </logic:empty>
        </logic:equal>
      </logic:present>

      <input name="method" type="hidden" value="issnAssistent" />
      <logic:present name="orderform" property="autocomplete">
        <input name="autocomplete" type="hidden"
          value="<bean:write name="orderform" property="autocomplete" />" />
      </logic:present>
      <logic:present name="orderform" property="flag_noissn">
        <input name="flag_noissn" type="hidden"
          value="<bean:write name="orderform" property="flag_noissn" />" />
      </logic:present>
      <logic:present name="orderform" property="runs_autocomplete">
        <input name="runs_autocomplete" type="hidden"
          value="<bean:write name="orderform" property="runs_autocomplete" />" />
      </logic:present>
      <logic:present name="orderform" property="artikeltitel">
        <input name="artikeltitel" type="hidden"
          value="<bean:write name="orderform" property="artikeltitel" />" />
      </logic:present>
      <logic:present name="orderform" property="jahr">
        <input name="jahr" type="hidden"
          value="<bean:write name="orderform" property="jahr" />" />
      </logic:present>
      <logic:present name="orderform" property="jahrgang">
        <input name="jahrgang" type="hidden"
          value="<bean:write name="orderform" property="jahrgang" />" />
      </logic:present>
      <logic:present name="orderform" property="heft">
        <input name="heft" type="hidden"
          value="<bean:write name="orderform" property="heft" />" />
      </logic:present>
      <logic:present name="orderform" property="seiten">
        <input name="seiten" type="hidden"
          value="<bean:write name="orderform" property="seiten" />" />
      </logic:present>
      <logic:present name="orderform" property="author">
        <input name="author" type="hidden"
          value="<bean:write name="orderform" property="author" />" />
      </logic:present>

</html:form>

    <tr>
      <td><br>
      </td>
    </tr>

    <tr>
      <td colspan="2">
      <h3><bean:message key="issnsearch.header2" /></h3>
      </td>
    </tr>
    <logic:notEmpty name="orderform" property="artikeltitel">
      <tr>
        <td colspan="2">
        <div id="italic"><bean:message key="issnsearch.autocomplete_comment" /></div>
        </td>
      </tr>
    </logic:notEmpty>

    <html:form action="findissn.do" method="post">

      <logic:equal name="orderform" property="mediatype" value="Artikel">
        <tr>
          <td><bean:message key="bestellform.author" />&nbsp;</td>
          <td><input name="author"
            value="<bean:write name="orderform" property="author" />"
            type="text" size="60" maxlength="100" /></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.artikeltitel" />&nbsp;</td>
          <td><input name="artikeltitel"
            value="<bean:write name="orderform" property="artikeltitel" />"
            type="text" size="98" maxlength="100" /></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.zeitschrift" />&nbsp;</td>
          <td><input name="zeitschriftentitel"
            value="<bean:write name="orderform" property="zeitschriftentitel"/>"
            type="text" size="98" maxlength="100" /></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.issn" />&nbsp;</td>
          <td><input name="issn"
            value="<bean:write name="orderform" property="issn"/>" type="text"
            size="9" maxlength="9" /> <logic:empty name="orderform"
            property="issn">
            <font color="white"><i> <bean:message key="issnsearch.issn_comment2" /></i></font>
          </logic:empty></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.jahr" />&nbsp;</td>
          <td><input name="jahr" type="text"
            value="<bean:write name="orderform" property="jahr" />" size="4"
            maxlength="4" /><logic:equal name="orderform"
            property="mediatype" value="Artikel">  <bean:message key="issnsearch.jahr_example" /></logic:equal></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.jahrgang" />&nbsp;</td>
          <td><input name="jahrgang" type="text"
            value="<bean:write name="orderform" property="jahrgang" />"
            size="4" maxlength="10" /> <bean:message key="issnsearch.jahrgang_example" /></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.heft" />&nbsp;</td>
          <td><input name="heft" type="text"
            value="<bean:write name="orderform" property="heft" />" size="4"
            maxlength="10" /> <bean:message key="issnsearch.heft_example" /></td>
        </tr>
        <tr>
          <td><bean:message key="bestellform.seiten" />&nbsp;</td>
          <td><input name="seiten" type="text"
            value="<bean:write name="orderform" property="seiten" />"
            size="15" maxlength="15" /></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td><input name="pmid" type="hidden"
            value="<bean:write name="orderform" property="pmid" />" size="60"
            maxlength="200" /></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td><input name="doi" type="hidden"
            value="<bean:write name="orderform" property="doi" />" size="60"
            maxlength="200" /></td>
        </tr>
      </logic:equal>

      <tr>
        <td></td>
        <td><input type="submit" value="<bean:message key="issnsearch.submit_check_availability" />"></td>
      </tr>

      <tr>
        <td><br>
        </td>
      </tr>

      <logic:present name="orderform" property="mediatype">
        <input name="mediatype" type="hidden"
          value="<bean:write name="orderform" property="mediatype" />" />
      </logic:present>

      <input name="method" type="hidden" value="checkAvailabilityOpenUrl" />
      <logic:present name="orderform" property="autocomplete">
        <input name="autocomplete" type="hidden"
          value="<bean:write name="orderform" property="autocomplete" />" />
      </logic:present>
      <logic:present name="orderform" property="runs_autocomplete">
        <input name="runs_autocomplete" type="hidden"
          value="<bean:write name="orderform" property="runs_autocomplete" />" />
      </logic:present>
    </html:form>



    <logic:present name="journalseek" property="zeitschriften">
      <p><i><bean:message key="issnsearch.no_hits" /></i></p>
      <logic:iterate id="js" name="journalseek" property="zeitschriften">
        <p></p><logic:notEmpty name="js" property="zeitschriftentitel_encodedUTF8">
          <i><b><a
            href="http://www.google.ch/search?hl=de&btnG=Google-Suche&meta&q=issn+%22<bean:write name="js" property="zeitschriftentitel_encodedUTF8" />%22"
            target="_blank"><bean:message key="issnsearch.google_zeitschrift" /></a></b></i>
          <br>
        </logic:notEmpty>
      </logic:iterate>

      <logic:iterate id="js" name="journalseek" property="zeitschriften">
        <logic:notEmpty name="js" property="artikeltitel_encodedUTF8">
          <i><b><a
            href="http://www.google.ch/search?hl=de&btnG=Google-Suche&meta&q=issn+%22<bean:write name="js" property="artikeltitel_encodedUTF8" />%22"
            target="_blank"><bean:message key="issnsearch.google_artikeltitel" /></a></b></i>
          <br>
          <p><bean:message key="bestellform.artikeltitel" />: <bean:write name="js" property="artikeltitel" />
          <input name="artikeltitel" type="hidden"
            value="<bean:write name="js" property="artikeltitel" />" /></p>
        </logic:notEmpty>
      </logic:iterate>
      <p></p>
    </logic:present>



  </table>

</logic:present> <logic:notPresent name="userinfo" property="konto">
  <p><bean:message key="error.timeout" /></p>
  <p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent></div>

</body>
</html>
