<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="robots" content="noindex" />
<title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="bestellform.bestellformular" /></title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css">
</head>
<body>

<bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<logic:present name="userinfo" property="benutzer.rechte">  
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">          
    <table style="position:absolute; text-align:left; left:111px; z-index:2;">
      <tr>
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
        <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
<logic:present name="userinfo" property="benutzer.userbestellung"> 
  <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
  </logic:equal>
</logic:present>
<logic:present name="userinfo" property="konto.gbvrequesterid">
<logic:present name="userinfo" property="konto.isil">
<logic:present name="userinfo" property="benutzer.gbvbestellung">
  <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:equal>
</logic:present>
</logic:present>
</logic:present>
      </tr>
</table>
  </logic:equal>
</logic:present>

<div class="content">

<br>

<logic:equal name="bestellparam" property="back" value="true">
  <a href="<bean:write name="bestellparam" property="link_back" />"><bean:message key="bestellform.back" /></a>
</logic:equal>

<table>
  <logic:notPresent name="userinfo" property="benutzer">
    <logic:notEmpty name="orderform" property="artikeltitel">
        <tr>
          <td><a
            href="http://www.google.ch/search?as_q=&num=4&btnG=Google-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images"
            target="_blank"><i><bean:message key="bestellform.google" />&nbsp;</i></a></td>
          <td><a
            href="http://scholar.google.com/scholar?as_q=&num=10&btnG=Scholar-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr="
            target="_blank"><i><bean:message key="bestellform.googlescholar" /></i></a></td>
        </tr>
    </logic:notEmpty>
  </logic:notPresent>

  <tr>
    <td colspan="2">
    <h3><logic:notEmpty name="orderform" property="bibliothek">
        <bean:write name="orderform" property="bibliothek" /> - 
    </logic:notEmpty><bean:message key="bestellform.bestellformular" /></h3>
    </td>
  </tr>


  <html:form action="bestellform.do" method="post">
    <logic:equal name="orderform" property="mediatype" value="Artikel">
      <tr>
        <td title="<bean:message key="info.pubmed" />">PMID <img border="0" src="img/info.png" alt="<bean:message key="info.pubmed" />" /></td>
        <td><input name="pmid" type="text"
          value="<bean:write name="orderform" property="pmid" />" size="60"
          maxlength="100"><input type="submit" value="<bean:message key="bestellform.resolve" />">
        (<bean:message key="bestellform.optional" />)</td>
      </tr>
      <tr>
        <td title="<bean:message key="info.doi" />">DOI <img border="0" src="img/info.png" alt="<bean:message key="info.doi" />" /></td>
        <td><input name="doi" type="text"
          value="<bean:write name="orderform" property="doi" />" size="60"
          maxlength="200"><input type="submit" value="<bean:message key="bestellform.resolve" />">
        (<bean:message key="bestellform.optional" />)</td>
      </tr>
      <tr>
        <td><br>
        </td>
      </tr>
    </logic:equal>
    <tr>
      <td><bean:message key="bestellform.typ" />&nbsp;</td>
      <td><input type="radio" name="mediatype" value="Artikel"
        <logic:equal name="orderform" property="mediatype" value="Artikel">checked="checked"</logic:equal>
        <logic:notEqual name="orderform" property="mediatype" value="Artikel">onclick="submit()"</logic:notEqual> /><bean:message key="bestellform.artikelkopie" />
      <input type="radio" name="mediatype" value="Teilkopie Buch"
        <logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">checked="checked"</logic:equal>
        <logic:notEqual name="orderform" property="mediatype" value="Teilkopie Buch">onclick="submit()"</logic:notEqual> /><bean:message key="bestellform.buchausschnitt" />
      <input type="radio" name="mediatype" value="Buch"
        <logic:equal name="orderform" property="mediatype" value="Buch">checked="checked"</logic:equal>
        <logic:notEqual name="orderform" property="mediatype" value="Buch">onclick="submit()"</logic:notEqual> /><bean:message key="bestellform.buch" /></td>
    </tr>
    <input name="method" type="hidden" value="validate" />
    <input name="resolve" type="hidden" value="true" />
    
    <logic:present name="orderform" property="kkid">
       <input name="kkid" type="hidden" value="<bean:write name="orderform" property="kkid" />" />
    </logic:present>
    <logic:present name="orderform" property="bkid">
      <input name="bkid" type="hidden" value="<bean:write name="orderform" property="bkid" />" />
    </logic:present>

  </html:form>

  <html:form action="validateBestellform.do" method="post">
    
    <logic:equal name="bestellparam" property="lieferart" value="false">
    <logic:notEqual name="orderform" property="mediatype" value="Buch">
    <!-- Lieferart Standardparameter -->
      <tr>
        <td><bean:message key="bestellform.bestellart" />&nbsp;</td>
        <td><input type="radio" name="deloptions" value="post"
          <logic:equal name="orderform" property="deloptions" value="post"> checked="checked"</logic:equal> /><bean:message key="bestellform.post" />
          <input type="radio"  name="deloptions" value="email"
          <logic:equal name="orderform" property="deloptions" value="email"> checked="checked"</logic:equal> /><bean:message key="bestellform.pdf" />
          </td>
      </tr>
    </logic:notEqual>
    </logic:equal>
    
    <logic:notEqual name="orderform" property="mediatype" value="Buch">
    <logic:equal name="bestellparam" property="lieferart" value="true">
    <!-- Lieferart Customparameter -->
      <tr>
        <td><bean:message key="bestellform.bestellart" />&nbsp;</td>
        <td><bean:define id="tmp" name="orderform" property="deloptions" type="java.lang.String"/>
          <logic:present name="bestellparam" property="lieferart_value1">
            <input type="radio" name="deloptions" value="<bean:write name="bestellparam" property="lieferart_value1" />"
            <logic:equal name="bestellparam" property="lieferart_value1" value="<%=tmp%>"> checked="checked"
            </logic:equal> /><bean:write name="bestellparam" property="lieferart_value1" /> </logic:present><logic:present name="bestellparam" property="lieferart_value2"><input type="radio"
            name="deloptions" value="<bean:write name="bestellparam" property="lieferart_value2" />"
            <logic:equal name="bestellparam" property="lieferart_value2" value="<%=tmp%>"> checked="checked"
            </logic:equal> /><bean:write name="bestellparam" property="lieferart_value2" /> </logic:present><logic:present name="bestellparam" property="lieferart_value3"><input type="radio" 
            name="deloptions" value="<bean:write name="bestellparam" property="lieferart_value3" />" 
            <logic:equal name="bestellparam" property="lieferart_value3" value="<%=tmp%>"> checked="checked"
            </logic:equal> /><bean:write name="bestellparam" property="lieferart_value3" /></logic:present></td>
      </tr>
    </logic:equal>
    </logic:notEqual>
    
    <logic:equal name="bestellparam" property="prio" value="false">
    <tr>
      <td><br>
      </td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.prio" />&nbsp;</td>
      <td>
        <select name="prio">
          <option value="normal" <logic:equal name="orderform" property="prio" value="normal">selected</logic:equal>><bean:message key="bestellform.normal" /></option>
          <option value="urgent" <logic:equal name="orderform" property="prio" value="urgent">selected</logic:equal>><bean:message key="bestellform.urgent" /></option>
        </select>        
      </td>
    </tr>
    </logic:equal>

    <logic:present name="messagemissing" property= "message">
    <tr>
      <td><br>
      </td>
    </tr>
    <tr>
      <td></td>
      <td>
        <bean:define id="missingKey" name="messagemissing" property="message" type="java.lang.String"/>
        <font color="red"><bean:message key="<%=missingKey%>" /></font> 
      </td>
    </tr>
    </logic:present>
    
    <tr>
      <td><br>
      </td>
    </tr>
    
    <logic:present name="bestellparam" property="comment1">
    <tr>
      <td></td>
      <td><bean:write name="bestellparam" property="comment1" /></td>
    </tr>    
    <tr>
      <td><br>
      </td>
    </tr>
    </logic:present>

    <tr>
      <td><b><bean:message key="bestellform.vorname" />*&nbsp;</b></td>
      <td><input name="kundenvorname"
        value="<bean:write name="orderform" property="kundenvorname" />"
        type="text" size="60" maxlength="100" autofocus /></td>
    </tr>
    <tr>
      <td><b><bean:message key="bestellform.name" />*&nbsp;</b></td>
      <td><input name="kundenname"
        value="<bean:write name="orderform" property="kundenname" />"
        type="text" size="60" maxlength="100" /></td>
    </tr>
    <tr>
      <td><b><bean:message key="bestellform.email" />*&nbsp;</b></td>
      <td><input name="kundenmail"
        value="<bean:write name="orderform" property="kundenmail" />"
        type="email" size="60" maxlength="100" /></td>
    </tr>
    
<!-- Hier folgen allf&auml;llige zus&auml;tzliche Bestellformular-Parameter -->
    <logic:equal name="bestellparam" property="freitxt1" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="freitxt1_required" value="true"><b></logic:equal>
          <bean:write name="bestellparam" property="freitxt1_name" /><logic:equal name="bestellparam" property="freitxt1_required" value="true">*</b></logic:equal>&nbsp;
          <input name="freitxt1_label" type="hidden" value="<bean:write name="bestellparam" property="freitxt1_name" />" />
        </td>
        <td><input name="freitxt1_inhalt"
          value="<bean:write name="orderform" property="freitxt1_inhalt" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="institution" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="inst_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.institution" /><logic:equal name="bestellparam" property="inst_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundeninstitution"
          value="<bean:write name="orderform" property="kundeninstitution" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="abteilung" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="abt_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.abteilung" /><logic:equal name="bestellparam" property="abt_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundenabteilung"
          value="<bean:write name="orderform" property="kundenabteilung" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
   <logic:equal name="bestellparam" property="category" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="category_required" value="true"><b></logic:equal>
          <bean:message key="modifykontousers.category" /><logic:equal name="bestellparam" property="category_required" value="true">*</b></logic:equal>&nbsp;
        </td>
      <td>
      <select name="kundenkategorieID">
      	<option value="0"><bean:message key="modifykontousers.choose" /></option>
      		<bean:define id="tmp" name="orderform" property="kundenkategorieID" type="java.lang.String" />
      		<logic:iterate id="cat" name="categories">
         		<option value="<bean:write name="cat" property="id" />" <logic:equal name="cat" property="id" value="<%=tmp%>">selected</logic:equal>><bean:write name="cat" property="inhalt" /></option>
        	</logic:iterate>
       </select>
      </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="freitxt2" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="freitxt2_required" value="true"><b></logic:equal>
          <bean:write name="bestellparam" property="freitxt2_name" /><logic:equal name="bestellparam" property="freitxt2_required" value="true">*</b></logic:equal>&nbsp;
          <input name="freitxt2_label" type="hidden" value="<bean:write name="bestellparam" property="freitxt2_name" />" />
        </td>
        <td><input name="freitxt2_inhalt"
          value="<bean:write name="orderform" property="freitxt2_inhalt" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="orderform" property="mediatype" value="Buch">
    <logic:equal name="bestellparam" property="strasse" value="false">
    <logic:equal name="bestellparam" property="plz" value="false">
    <logic:equal name="bestellparam" property="ort" value="false">
    <logic:equal name="bestellparam" property="adresse" value="false">
    <!-- erscheint nur bei Buch und wenn keine BestellParam f&uuml;r Strasse, PLZ, Ort und ADRESSE angegeben wurden -->
      <tr>
        <td><bean:message key="bestellform.adress" />&nbsp;</td>
        <td><textarea cols="54" rows="2" name="kundenadresse"
          style="word-wrap:break-word;"><bean:write
          name="orderform" property="kundenadresse" /></textarea></td>
      </tr>
    </logic:equal>
    </logic:equal>
    </logic:equal>
    </logic:equal>
    </logic:equal>
    <logic:equal name="bestellparam" property="adresse" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="adr_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.adress" /><logic:equal name="bestellparam" property="adr_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><textarea cols="54" rows="2" name="kundenadresse"
          style="word-wrap:break-word;"><bean:write
          name="orderform" property="kundenadresse" /></textarea>
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="strasse" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="str_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.strasse" /><logic:equal name="bestellparam" property="str_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundenstrasse"
          value="<bean:write name="orderform" property="kundenstrasse" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>    
    <logic:equal name="bestellparam" property="plz" value="true">
    <!-- PLZ und ev. Ort -->
      <tr>
        <td>
          <logic:equal name="bestellparam" property="plz_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.plz" /><logic:equal name="bestellparam" property="plz_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td>
            <input name="kundenplz"
            value="<bean:write name="orderform" property="kundenplz" />"
            type="text" size="8" maxlength="8" />          
              <logic:equal name="bestellparam" property="ort" value="true">
                <logic:equal name="bestellparam" property="ort_required" value="true"><b></logic:equal>
                <bean:message key="bestellform.ort" /><logic:equal name="bestellparam" property="ort_required" value="true">*</b></logic:equal>&nbsp;
                <input name="kundenort"
                value="<bean:write name="orderform" property="kundenort" />"
                type="text" size="30" maxlength="100" />
          </logic:equal>
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="plz" value="false">
    <logic:equal name="bestellparam" property="ort" value="true">
    <!-- Keine PLZ aber Ort -->
      <tr>
        <td>
          <logic:equal name="bestellparam" property="ort_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.ort" /><logic:equal name="bestellparam" property="ort_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundenort"
          value="<bean:write name="orderform" property="kundenort" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    </logic:equal>
    <logic:equal name="bestellparam" property="land" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="land_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.land" /><logic:equal name="bestellparam" property="land_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td>
        <select name="kundenland">
          <option value="0" selected="selected"><bean:message key="select.countries" /></option>
       <logic:iterate id="c" name="orderform" property="countries">
        <bean:define id="tmp" name="c" property="countrycode" type="java.lang.String"/>
             <option value="<bean:write name="c" property="countrycode" />"<logic:present name="orderform" property="kundenland"><logic:equal name="orderform" property="kundenland" value="<%=tmp%>"> selected</logic:equal></logic:present>><bean:write name="c" property="countryname" /></option>
           </logic:iterate>
      </select>
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="telefon" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="telefon_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.telefon" /><logic:equal name="bestellparam" property="telefon_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundentelefon"
          value="<bean:write name="orderform" property="kundentelefon" />"
          type="tel" size="25" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="benutzernr" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="benutzernr_required" value="true"><b></logic:equal>
          <bean:message key="bestellform.benutzernr" /><logic:equal name="bestellparam" property="benutzernr_required" value="true">*</b></logic:equal>&nbsp;
        </td>
        <td><input name="kundenbenutzernr"
          value="<bean:write name="orderform" property="kundenbenutzernr" />"
          type="text" size="25" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="freitxt3" value="true">
      <tr>
        <td>
          <logic:equal name="bestellparam" property="freitxt3_required" value="true"><b></logic:equal>
          <bean:write name="bestellparam" property="freitxt3_name" /><logic:equal name="bestellparam" property="freitxt3_required" value="true">*</b></logic:equal>&nbsp;
          <input name="freitxt3_label" type="hidden" value="<bean:write name="bestellparam" property="freitxt3_name" />" />
        </td>
        <td><input name="freitxt3_inhalt"
          value="<bean:write name="orderform" property="freitxt3_inhalt" />"
          type="text" size="60" maxlength="100" />
        </td>
      </tr>      
    </logic:equal>
    <tr>
      <td></td>
      <td>[<b>*<bean:message key="bestellform.required" /></b>]</td>
    </tr>
    
    
    <logic:equal name="bestellparam" property="option" value="true">
    <!-- Allf&auml;llige Radiobuttons -->
    <tr>
      <td><br>
      </td>
    </tr>
      <tr>
        <td>
          <bean:write name="bestellparam" property="option_name" />&nbsp;
          <input name="radiobutton_name" type="hidden" value="<bean:write name="bestellparam" property="option_name" />" />
        </td>
        <td>
          <bean:write name="bestellparam" property="option_comment" />&nbsp; <a href="<bean:write name="bestellparam" property="option_linkout" />" target="_blank"><bean:write name="bestellparam" property="option_linkoutname" /></a>
        </td>
      </tr>
      <tr>
        <td></td>
        <td>
          <bean:define id="tmp" name="orderform" property="radiobutton" type="java.lang.String"/>
          <logic:notEmpty name="bestellparam" property="option_value1">
              <input type="radio" name="radiobutton" value="<bean:write name="bestellparam" property="option_value1" />" <logic:equal name="bestellparam" property="option_value1" value="<%=tmp%>">checked="checked"</logic:equal> /><bean:write name="bestellparam" property="option_value1" />
          </logic:notEmpty>
          <logic:notEmpty name="bestellparam" property="option_value2">
              <input type="radio" name="radiobutton" value="<bean:write name="bestellparam" property="option_value2" />" <logic:equal name="bestellparam" property="option_value2" value="<%=tmp%>">checked="checked"</logic:equal> /><bean:write name="bestellparam" property="option_value2" />
          </logic:notEmpty>
          <logic:notEmpty name="bestellparam" property="option_value3">
              <input type="radio" name="radiobutton" value="<bean:write name="bestellparam" property="option_value3" />" <logic:equal name="bestellparam" property="option_value3" value="<%=tmp%>">checked="checked"</logic:equal> /><bean:write name="bestellparam" property="option_value3" />
          </logic:notEmpty>
        </td>
      </tr>      
    </logic:equal>
     
    <tr>
      <td><br>
      </td>
    </tr>
    
    <logic:present name="orderform" property="language">
    <tr>
      <td><div id="italic"><bean:message key="bestellform.sprache" />&nbsp;</div></td>
      <td><div id="italic"><bean:write name="orderform" property="language" /></div></td>
    </tr>
    </logic:present>

    <tr>
      <td><bean:message key="bestellform.author" />&nbsp;</td>
      <td><input name="author"
        value="<bean:write name="orderform" property="author" />"
        type="text" size="60" maxlength="100" /></td>
    </tr>

    <logic:equal name="orderform" property="mediatype" value="Artikel">
      <tr>
        <td><bean:message key="bestellform.artikeltitel" />&nbsp;</td>
        <td><input name="artikeltitel"
          value="<bean:write name="orderform" property="artikeltitel" />"
          type="text" size="98" maxlength="200" /></td>
      </tr>
      <tr>
        <td><bean:message key="bestellform.zeitschrift" />&nbsp;</td>
        <td><input name="zeitschriftentitel"
          value="<logic:present name="orderform" property="zeitschriftentitel"><bean:write name="orderform" property="zeitschriftentitel"/></logic:present>"
          type="text" size="98" maxlength="200" /></td>
      </tr>
      <tr title="<bean:message key="info.issn" />">
        <td title="<bean:message key="info.issn" />"><bean:message key="bestellform.issn" /> <img border="0" src="img/info.png" alt="<bean:message key="info.issn" />" /></td>
        <td><input name="issn"
          value="<logic:present name="orderform" property="issn"><bean:write name="orderform" property="issn" /></logic:present>"
          type="text" size="9" maxlength="9">
        (<bean:message key="bestellform.optional" />)</td>
      </tr>
    </logic:equal>

    <logic:equal name="orderform" property="mediatype"
      value="Teilkopie Buch">
      <tr>
        <td><bean:message key="bestellform.kapitel" />&nbsp;</td>
        <td><input name="kapitel"
          value="<bean:write name="orderform" property="kapitel" />"
          type="text" size="98" maxlength="200" /></td>
      </tr>
      <tr>
        <td><bean:message key="bestellform.buchtitel" />&nbsp;</td>
        <td><input name="buchtitel"
          value="<logic:present name="orderform" property="buchtitel"><bean:write name="orderform" property="buchtitel"/></logic:present>"
          type="text" size="98" maxlength="200" /></td>
      </tr>
      <tr>
        <td><bean:message key="bestellform.verlag" />&nbsp;</td>
        <td><input name="verlag"
          value="<bean:write name="orderform" property="verlag"/>"
          type="text" size="60" maxlength="100" /></td>
      </tr>
      <tr>
        <td title="<bean:message key="info.isbn" />"><bean:message key="bestellform.isbn" /> <img border="0" src="img/info.png" alt="<bean:message key="info.isbn" />" /></td>
        <td><input name="isbn" type="text"
          value="<bean:write name="orderform" property="isbn" />" size="17"
          maxlength="23" /> (<bean:message key="bestellform.optional" />)</td>
      </tr>
      <logic:present name="orderform" property="issn">
        <input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" />
      </logic:present>
      <logic:present name="orderform" property="jahrgang">
        <input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" />
      </logic:present>
    </logic:equal>

    <logic:equal name="orderform" property="mediatype" value="Buch">
      <tr>
        <td><bean:message key="bestellform.buchtitel" />&nbsp;</td>
        <td><input name="buchtitel"
          value="<logic:present name="orderform" property="buchtitel"><bean:write name="orderform" property="buchtitel"/></logic:present>"
          type="text" size="98" maxlength="200" /></td>
      </tr>
      <tr>
        <td><bean:message key="bestellform.verlag" />&nbsp;</td>
        <td><input name="verlag"
          value="<bean:write name="orderform" property="verlag"/>"
          type="text" size="60" maxlength="100" /></td>
      </tr>
      <tr>
        <td title="<bean:message key="info.isbn" />"><bean:message key="bestellform.isbn" /> <img border="0" src="img/info.png" alt="<bean:message key="info.isbn" />" /></td>
        <td><input name="isbn" type="text"
          value="<bean:write name="orderform" property="isbn" />" size="17"
          maxlength="23" /> (<bean:message key="bestellform.optional" />)</td>
      </tr>
      <logic:present name="orderform" property="issn">
        <input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" />
      </logic:present>
      <logic:present name="orderform" property="jahrgang">
        <input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" />
      </logic:present>
    </logic:equal>

    <tr>
      <td><bean:message key="bestellform.jahr" />&nbsp;</td>
      <td><input name="jahr" type="text"
        value="<bean:write name="orderform" property="jahr" />" size="4"
        maxlength="4" /><logic:equal name="orderform"
        property="mediatype" value="Artikel">  (<bean:message key="bestellform.jahrbsp" />)</logic:equal></td>
    </tr>
    <logic:equal name="orderform" property="mediatype" value="Artikel">
      <tr>
        <td><bean:message key="bestellform.jahrgang" />&nbsp;</td>
        <td><input name="jahrgang" type="text"
          value="<bean:write name="orderform" property="jahrgang" />"
          size="4" maxlength="20" /> (<bean:message key="bestellform.jahrgangbsp" />)</td>
      </tr>
      <tr>
        <td><bean:message key="bestellform.heft" />&nbsp;</td>
        <td><input name="heft" type="text"
          value="<bean:write name="orderform" property="heft" />" size="4"
          maxlength="20" /> (<bean:message key="bestellform.heftbsp" />)</td>
      </tr>
    </logic:equal>
    <logic:notEqual name="orderform" property="mediatype" value="Buch">
      <tr>
        <td><bean:message key="bestellform.seiten" />&nbsp;</td>
        <td><input name="seiten" type="text"
          value="<bean:write name="orderform" property="seiten" />" size="25"
          maxlength="30" /></td>
      </tr>
    </logic:notEqual>
    <tr>
      <td><bean:message key="bestellform.anmerkungen" />&nbsp;</td>
      <td><textarea cols="54" rows="2" name="notizen"
        style="word-wrap:break-word;"><bean:write
        name="orderform" property="notizen" /></textarea></td>
    </tr>
    
    <logic:present name="bestellparam" property="comment2">
    <tr>
      <td><br>
      </td>
    </tr>
    <tr>
      <td></td>
      <td><bean:write name="bestellparam" property="comment2" /></td>
    </tr>    
    </logic:present>
    
    <logic:equal name="bestellparam" property="gebuehren" value="true">
    <tr>
      <td><br>
      </td>
    </tr>
      <tr>
        <td>
        </td>
        <td>
          <input type="checkbox" name="gebuehren" />&nbsp;<bean:message key="bestellform.gebuehrentxt" />&nbsp;<a href="<bean:write name="bestellparam" property="link_gebuehren" />" target="_blank"><bean:message key="bestellform.gebuehren" /></a>
        </td>
      </tr>      
    </logic:equal>
    <logic:equal name="bestellparam" property="agb" value="true">
    <logic:equal name="bestellparam" property="gebuehren" value="false">
    <!-- Abstand nur falls gebuhren nicht eingeblendet -->
      <tr>
        <td><br>
        </td>
      </tr>
    </logic:equal>
    
      <tr>
        <td>
        </td>
        <td>
          <input type="checkbox" name="agb" />&nbsp;<bean:message key="bestellform.agbtxt" />&nbsp;<a href="<bean:write name="bestellparam" property="link_agb" />" target="_blank"><bean:message key="bestellform.agb" /></a>
        </td>
      </tr>      
    </logic:equal>
    

    <tr>
      <td><br>
      </td>
    </tr>

    <tr>
      <td></td>
      <td><input type="submit" value="<bean:message key="bestellform.submit" />"></td>
    </tr>

<logic:present name="orderform" property="kkid">
   <input name="kkid" type="hidden" value="<bean:write name="orderform" property="kkid" />" />
</logic:present>
<logic:present name="orderform" property="bkid">
  <input name="bkid" type="hidden" value="<bean:write name="orderform" property="bkid" />" />
</logic:present>
<logic:present name="orderform" property="language">
  <input name="language" type="hidden" value="<bean:write name="orderform" property="language" />" />
</logic:present>

<input name="rfr_id" type="hidden"
  value="<bean:write name="orderform" property="rfr_id" />" />
<input name="genre" type="hidden"
  value="<bean:write name="orderform" property="genre" />" />
<input name="pmid" type="hidden"
  value="<bean:write name="orderform" property="pmid" />" />
<input name="doi" type="hidden"
  value="<bean:write name="orderform" property="doi" />" />
<input name="mediatype" type="hidden"
  value="<bean:write name="orderform" property="mediatype" />" />
<input name="method" type="hidden" value="sendOrder" />
</html:form>

</table>

<tiles:insert page="import/footer.jsp" flush="true" />

</div>

</body>
</html>
