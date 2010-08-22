<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="searchgbv.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css">
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<table style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenu" nowrap title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV<logic:present name="orderform" property="mediatype">&mediatype=<bean:write name="orderform" property="mediatype" /></logic:present>"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV<logic:present name="orderform" property="mediatype">&mediatype=<bean:write name="orderform" property="mediatype" /></logic:present>"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
      </logic:notEqual>
  </tr>
</table>

<logic:present name="userinfo" property="konto">

<div class="content">

 <br /> <br />

  <h3><bean:message key="searchgbv.header" /></h3>
  <html:form action="orderGbv.do" method="post" focus="gbvsearch">
  
  <logic:present name="gbvmessage" property="message"><p class=miss><bean:write name="gbvmessage" property="message" /></p></logic:present>
   
  <table>
    <tr>
      <td><br /></td>
    </tr>
    <tr>
      <td>
        <select name="gbvfield" type="select">
          <option value="ALL" selected>Alle W&ouml;rter [ALL]</option>
        <option value="TIT">Titel (Stichwort) [TIT]</option>
        <option value="PER">Person, Autor [PER]</option>
        <option value="THM">Alle Themen [THM]</option>
        <option value="SLW">Schlagw&ouml;rter [SLW]</option>
        <option value="TXT">Inhaltsverzeichnisse [TXT]</option>
        <option value="GTI">Serie, Zeitschrift (Phrase) [GTI]</option>
        <option value="SER">Serie, Zeitschrift (Stichwort) [SER]</option>
        <option value="ZTW">Zeitschriftentitel im Aufsatz (Stichwort) [ZTW]</option>
        <option value="ZET">Zeitschriftentitel im Aufsatz (Phrase) [ZET]</option>
        <option value="HSS">Hochschulschriftenvermerk [HSS]</option>
        <option value="ISB">ISBN [ISB]</option>
        <option value="ISS">ISSN [ISS]</option>
        <option value="ZIS">ISSN im Aufsatz [ZIS]</option>
        <option value="NUM">Nummern (allgemein) [NUM]</option>
        <option value="KOR">K&ouml;rperschaft (Stichwort) [KOR]</option>
        <option value="KON">Kongress (Stichwort) [KON]</option>
        <option value="PUB">Ort,Verlag (Stichwort) [PUB]</option>
        <option value="URL">URL (Phrase) [URL]</option>
        <option value="URN">URN (Phrase) [URN]</option>
        <option value="URK">URL, URN (Stichwort) [URK]</option>
        <option value="BKL">Basisklassifikation [BKL]</option>
        <option value="PPN">PICA Prod.-Nr. [PPN]</option>
        <option value="PRS">Personennamen [PRS]</option>
        <option value="KOS">K&ouml;rperschaftsname (Phrase) [KOS]</option>
        <option value="KNS">Kongress (Phrase)[KNS]</option>
        <option value="LCC">LoC-Classification [LCC]</option>
        <option value="JAH">Erscheinungsjahr [JAH]</option>
        <option value="BIB">Bibliothek [BIB]</option>
        <option value="PLC">Erscheinungsort [PLC]</option>
        <option value="PUS">Ort,Verlag (Phrase) [PUS]</option>
        <option value="GAT">Gattungsbegriff (Alte Drucke) [GAT]</option>
        <option value="OLC">OLC Sortstring [OLC]</option>
        <option value="SGZ">Sachgruppen ZDB [SGZ]</option>
        <option value="PND">PND-Nummer [PND]</option>
        <option value="TPK">Titel (Phrase, kurz) [TPK]</option>
        <option value="TAF">Titel (Phrase, ab Anfang) [TAF]</option>
        <option value="ADO">Aenderungsdatum Openurl [ADO]</option>
        <option value="AED">&Auml;nderungsdatum [AED]</option>
        <option value="LSG">Lokale Notation [LSG]</option>
        <option value="ZZD">ZDB-Nr im Aufsatz [ZZD]</option>
        <option value="SBN">Stand. Bibl. Nachweis [SBN]</option>
        <option value="EKI">Verbund ID (Ph) [EKI]</option>
        <option value="DUH">DOI/URN/Handle (Ph) [DUH]</option>
        <option value="XPR">ZDB-Sigel (Ph) [XPR]</option>
        <option value="ZDB">ZDB-Nummer [ZDB]</option>
        <option value="SNR">Sonstige Nummern [SNR]</option>
        <option value="VLG">Verlag [VLG]</option>
        <option value="RTI">Serie (Phrase) [RTI]</option>
        <option value="VSE">Serie (Stichwort) [VSE]</option>
        <option value="SHA">Serie, Zeitschrift (Stichwort, HA) [SHA]</option>
        <option value="ZTI">Serie, Zeitschrift (Phrase) [ZTI]</option>
        <option value="MTI">Gesamttitel (Phrase) [MTI]</option>
        <option value="VDS">Verkn&uuml;pfte Datens&auml;tze [VDS]</option>
        <option value="SYS">Systematik aus Fremddaten [SYS]</option>
        <option value="DDC">DDC [DDC]</option>
        <option value="ASB">ASB [ASB]</option>
        <option value="SSD">SSD [SSD]</option>
        <option value="SFB">SfB [SFB]</option>
        <option value="KAB">KAB [KAB]</option>
        <option value="EKZ">ekz [EKZ]</option>
        <option value="SYO">Systematiken &Ouml;ffentl. Bibl. [SYO]</option>
        <option value="FNM">Fussnoten Musik [FNM]</option>
        <option value="MAK">Bibliogr. Gattung und Status [MAK]</option>
        <option value="UUU">URL fuer FT [UUU]</option>
        <option value="TMB">Gesamttitel (Stichwort) [TMB]</option>
        <option value="TIW">Titel (Stichwort) [TIW]</option>
        <option value="OSG">Online Contents SSG [OSG]</option>
        <option value="ODI">OSG-Adi [ODI]</option>
        <option value="CLT">Siehe auch [CLT]</option>
        <option value="ERJ">Erscheinungsjahr [ERJ]</option>
        <option value="MAT">Materialart [MAT]</option>
        <option value="REG">Region [REG]</option>
        <option value="TAA">Sprachcode [TAA]</option>
        <option value="LAN">L&auml;ndercode [LAN]</option>
        <option value="ZTR">Zeitraum [ZTR]</option>
        <option value="SUP">Sysflag [SUP]</option>
        </select>
      </td>
    </tr>
    <tr>
      <td><input name="gbvsearch" type="text" size="98" ></p></td>
    </tr>
    <tr>
      <td><br /></td>
    </tr>
    <tr>
      <td><input type="submit" value="suchen"></input></td>
    </tr>
  </table>
  
    <br />
    <br />
    <br />
    
  <table>
    <logic:notEqual name="orderform" property="author" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.author" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="author" /></div></td>
  </tr>
  </logic:notEqual>
<logic:equal name="orderform" property="mediatype" value="Artikel">
  <logic:notEqual name="orderform" property="artikeltitel" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.artikeltitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="artikeltitel" /></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="zeitschriftentitel" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.zeitschrift" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="zeitschriftentitel"/></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="issn" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.issn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="issn" /></div></td>
  </tr>
  </logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">
  <logic:notEqual name="orderform" property="kapitel" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.kapitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="kapitel" /></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="buchtitel" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="isbn" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="verlag" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="jahr" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
  </logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Buch">
  <logic:notEqual name="orderform" property="buchtitel" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.buchtitel" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="buchtitel"/></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="isbn" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.isbn" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="isbn" /></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="verlag" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.verlag" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="verlag"/></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="jahr" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
  </logic:notEqual>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Artikel">
<logic:notEqual name="orderform" property="jahr" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahr" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahr" /></div></td>
  </tr>
</logic:notEqual>
  <logic:notEqual name="orderform" property="jahrgang" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.jahrgang" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="jahrgang" /></div></td>
  </tr>
  </logic:notEqual>
  <logic:notEqual name="orderform" property="heft" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.heft" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="heft" /></div></td>
  </tr>
  </logic:notEqual>
</logic:equal>
<logic:notEqual name="orderform" property="mediatype" value="Buch">
<logic:notEqual name="orderform" property="seiten" value="">
  <tr>
    <td><div id="italic"><bean:message key="bestellform.seiten" />:&nbsp;</div></td>
    <td><div id="italic"><bean:write name="orderform" property="seiten" /></div></td>
  </tr>
</logic:notEqual>
</logic:notEqual>
  <logic:notEqual name="orderform" property="anmerkungen" value="">
    <tr>
        <td><div id="italic"><bean:message key="bestellform.bemerkungen" />:&nbsp;</div></td>
        <td><div id="italic"><bean:write name="orderform" property="anmerkungen" /></div></td>
    </tr>
    </logic:notEqual>
    <logic:notEqual name="orderform" property="notizen" value="">
    <tr>
      <td><div id="italic"><bean:message key="bestellform.interne_notizen" />:&nbsp;</div></td>
      <td><div id="italic"><bean:write name="orderform" property="notizen" /></div></td>
    </tr>
    </logic:notEqual>

     <tr><td><br></td></tr>
     </table>

  <logic:present name="orderform" property="bid">
   <input name="bid" type="hidden" value="<bean:write name="orderform" property="bid" />" />
  </logic:present>
  <input name="method" type="hidden" value="search" />
  <input name="mediatype" type="hidden" value="<bean:write name="orderform" property="mediatype" />" />
  <input name="author" type="hidden" value="<bean:write name="orderform" property="author" />" /> 
  <input name="artikeltitel" type="hidden" value="<bean:write name="orderform" property="artikeltitel" />" />
  <input name="zeitschriftentitel" type="hidden" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
  <input name="buchtitel" type="hidden" value="<bean:write name="orderform" property="buchtitel" />" />
  <input name="kapitel" type="hidden" value="<bean:write name="orderform" property="kapitel" />" />
  <input name="verlag" type="hidden" value="<bean:write name="orderform" property="verlag" />" />
  <input name="jahr" type="hidden" value="<bean:write name="orderform" property="jahr" />" /> 
  <input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" />
  <input name="heft" type="hidden" value="<bean:write name="orderform" property="heft" />" /> 
  <input name="seiten" type="hidden" value="<bean:write name="orderform" property="seiten" />" /> 
  <input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" />
  <input name="isbn" type="hidden" value="<bean:write name="orderform" property="isbn" />" />
  <input name="notizen" type="hidden" value="<bean:write name="orderform" property="notizen" />" />
  <input name="anmerkungen" type="hidden" value="<bean:write name="orderform" property="anmerkungen" />" />
  <input name="interne_bestellnr" type="hidden" value="<bean:write name="orderform" property="interne_bestellnr" />" />
  <input name="foruser" type="hidden" value="<bean:write name="orderform" property="foruser" />" />
  
  <input name="prio" type="hidden" value="<bean:write name="orderform" property="prio" />" />
  <input name="fileformat" type="hidden" value="<bean:write name="orderform" property="fileformat" />" />
  <input name="deloptions" type="hidden" value="<bean:write name="orderform" property="deloptions" />" />
  <input name="maximum_cost" type="hidden" value="<bean:write name="orderform" property="maximum_cost" />" />
  <input name="preisvorkomma" type="hidden" value="<bean:write name="orderform" property="preisvorkomma" />" />
  <input name="preisnachkomma" type="hidden" value="<bean:write name="orderform" property="preisvorkomma" />" />
  <input name="waehrung" type="hidden" value="<bean:write name="orderform" property="waehrung" />" />
  <input name="manuell" type="hidden" value="<bean:write name="orderform" property="manuell" />" />  
  <input name="pmid" type="hidden" value="<bean:write name="orderform" property="pmid" />" />
  <input name="doi" type="hidden" value="<bean:write name="orderform" property="doi" />" />
  <input name="zdbid" type="hidden" value="<bean:write name="orderform" property="zdbid" />" />
  
    </html:form>
  
</logic:present>
<p></p>

<logic:notPresent name="userinfo" property="konto">
  <p><bean:message key="error.timeout" /></p>
  <p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>

</div>

 </body>
</html>
