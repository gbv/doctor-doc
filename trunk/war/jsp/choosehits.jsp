<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="order.title" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<tiles:insert page="import/header.jsp" flush="true" />

<table style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive" nowrap</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu" nowrap</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
      </logic:notEqual>
  </tr>
</table>

<div class="content">
<br><br>
              
<h3><bean:message key="choosehits.header" /></h3>

<logic:notEqual name="orderform" property="submit" value="GBV">
<logic:present name="orderform" property="links">
  <bean:message key="choosehits.treffer_comment" />:<p></p>  
<form action="journalorder1.do" method="post">   

      <select name="link">
          <logic:iterate id="of" name="orderform" property="links">
        <option value="<bean:write name="of" property="link" />&PHPSESSID=<bean:write name="orderform" property="sessionid" />"> <bean:write name="of" property="error" /></option>
           </logic:iterate>
       </select>

  <table>
    <tr>
      <td><p><input type="submit" value="bestellen" /></p></td>
    </tr>
  </table>
  
 <p>
    <logic:present name="orderform" property="issn">
    <div id="italic">
    <bean:write name="orderform" property="zeitschriftentitel" /><br>
    <bean:write name="orderform" property="jahrgang" />. Jg., <bean:write name="orderform" property="jahr" />
    </div>
    </logic:present>
</p>  
  
  <input name="method" type="hidden" value="journalorder" />
  <input name="author" type="hidden" value="<bean:write name="orderform" property="author" />" /> 
  <input name="artikeltitel" type="hidden" value="<bean:write name="orderform" property="artikeltitel" />" /> 
  <input name="heft" type="hidden" value="<bean:write name="orderform" property="heft" />" /> 
  <input name="jahr" type="hidden" value="<bean:write name="orderform" property="jahr" />" /> 
  <input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" /> 
  <input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" /> 
  <input name="fileformat" type="hidden" value="<bean:write name="orderform" property="fileformat" />" /> 
  <input name="prio" type="hidden" value="<bean:write name="orderform" property="prio" />" /> 
  <input name="seiten" type="hidden" value="<bean:write name="orderform" property="seiten" />" />  
  <input name="foruser" type="hidden" value="<bean:write name="orderform" property="foruser" />" />  
  <input name="sessionid" type="hidden" value="<bean:write name="orderform" property="sessionid" />" />
  <input name="deloptions" type="hidden" value="<bean:write name="orderform" property="deloptions" />" />  
  <input name="faxno" type="hidden" value="<bean:write name="orderform" property="faxno" />" /> 
  <input name="manuell" type="hidden" value="<bean:write name="orderform" property="manuell" />" /> 
  <input name="notizen" type="hidden" value="<bean:write name="orderform" property="notizen" />" />
  <input name="anmerkungen" type="hidden" value="<bean:write name="orderform" property="anmerkungen" />" />
  <input name="pmid" type="hidden" value="<bean:write name="orderform" property="pmid" />" />
  <input name="doi" type="hidden" value="<bean:write name="orderform" property="doi" />" />
  <input name="hits" type="hidden" value="1" />  
  </form>  
</logic:present>
</logic:notEqual>

<logic:equal name="orderform" property="submit" value="GBV">

  
  
  <html:form action="orderGbv.do" method="post" focus="foruser">
  
  <logic:notEqual name="userinfo" property="konto.bibliotheksname" value="Demo-Bibliothek">
  <table>
  <tr>
  <td><input type="submit" name="submit" value="weiter im Bestellablauf"></td>
  <td><br></td>
  <td><input type="submit" name="submit" value="GBV-Suche"></td>
  </tr>    
  </table>
  
<div id="italic">
  <p>
  	<bean:message key="order.doppelklicks" />
  </p><br>
</div>
  
  <b><bean:write name="orderform" property="treffer_total" /> <bean:message key="choosehits.treffer" /></b><p></p>
  
  </logic:notEqual>
  
  <table>
  <logic:present name="matches">
        <tr>
          <td><logic:notEqual name="orderform" property="back" value="0"><a href="orderGbv.do?method=search&back=<bean:write name="orderform" property="back" />&gbvsearch=<bean:write name="orderform" property="gbvsearch" />&gbvfield=<bean:write name="orderform" property="gbvfield" />&manuell=<bean:write name="orderform" property="manuell" />&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel" />&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" /></logic:equal>&author=<bean:write name="orderform" property="author" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><img src="img/left.png" alt="<bean:message key="choosehits.back" />" title="<bean:message key="choosehits.back" />" border="0" /></a></logic:notEqual></td>
          
          <td><logic:notEqual name="orderform" property="forwrd" value="0"><a href="orderGbv.do?method=search&forwrd=<bean:write name="orderform" property="forwrd" />&gbvsearch=<bean:write name="orderform" property="gbvsearch" />&gbvfield=<bean:write name="orderform" property="gbvfield" />&manuell=<bean:write name="orderform" property="manuell" />&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel" />&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" /></logic:equal>&author=<bean:write name="orderform" property="author" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><img src="img/right.png" alt="<bean:message key="choosehits.forward" />" title="<bean:message key="choosehits.forward" />" border="0" /></a></logic:notEqual></td>
          <td></td>
        </tr>
        <tr>
          <td><br></td>
        </tr>
       <logic:iterate id="m" name="matches">
         <tr>
           <td><input type="radio" name="ppn" value="<bean:write name="m" property="ppn_003AT" />"></td>
           <td><bean:write name="m" property="record_number" />.&nbsp;</td>
           <td><logic:present name="m" property="materialbenennung_allg_016H"><bean:write name="m" property="materialbenennung_allg_016H" /> <bean:write name="m" property="materialbenennung_spezifisch_034D" /></logic:present> <bean:write name="m" property="typ_002AT" /> | <a href="http://gso.gbv.de/DB=2.1/SET=1/TTL=1/MAT=/NORMAT=T/CLK?IKT=12&TRM=<bean:write name="m" property="ppn_003AT" />" target="_blank">GBV</a></td>
         </tr>
         <logic:present name="m" property="link_009P_multiple">
         <tr>
           <td></td>
           <td></td>
           <td><a href="<bean:write name="m" property="link_009P_multiple" />" target="_blank"><bean:write name="m" property="link_009P_multiple" /></a></td>
         </tr>
         </logic:present>
         <tr>
           <td></td>
           <td></td>
           <td><b><bean:write name="m" property="bandzaehlung" /><bean:write name="m" property="hauptsachtitel_021A" /><bean:write name="m" property="rezensierteswerk_021G_multipel" /> <bean:write name="m" property="unterreihe_021C_multipel" /><logic:present name="m" property="gesamtheit_abteilungen_vorlage_036C"><logic:present name="m" property="hauptsachtitel_021A"> |</logic:present></logic:present> <bean:write name="m" property="gesamtheit_abteilungen_vorlage_036C" /></b> <bean:write name="m" property="hauptsachtitel_alternativ_021B" /> <bean:write name="m" property="band_021V_multipel" /> <logic:present name="m" property="verfasser_erster_028A"><b>/</b> <bean:write name="m" property="verfasser_erster_028A" /></logic:present> <logic:present name="m" property="koerperschaft_erste_029A"><logic:present name="m" property="verfasser_erster_028A">; </logic:present><logic:notPresent name="m" property="verfasser_erster_028A"><b>/</b> </logic:notPresent><bean:write name="m" property="koerperschaft_erste_029A" /></logic:present> <logic:present name="m" property="kongresse_030F">; <bean:write name="m" property="kongresse_030F" /></logic:present></td>
         </tr>
         <logic:notPresent name="m" property="umfang_031A">
         <tr>
           <td></td>
           <td></td>
           <td><logic:present name="m" property="ausgabe_032AT"><bean:write name="m" property="ausgabe_032AT" />. - </logic:present><logic:present name="m" property="ort_verlag_033A_multipel"><bean:write name="m" property="ort_verlag_033A_multipel" />, </logic:present><bean:write name="m" property="erscheinungsverlauf_031AT" /><logic:notPresent name="m" property="erscheinungsverlauf_031AT"><bean:write name="m" property="erscheinungsjahr_011AT" /></logic:notPresent><logic:present name="m" property="issn_005A_multipel"> ISSN: <bean:write name="m" property="issn_005A_multipel" /></logic:present><logic:present name="m" property="isbn_004A_multipel"> ISBN: <bean:write name="m" property="isbn_004A_multipel" /></logic:present></td>
         </tr>
         </logic:notPresent>
         <logic:present name="m" property="hochschulschriftenvermerk_037C_multipel">
         <tr>
           <td></td>
           <td></td>
           <td><bean:write name="m" property="hochschulschriftenvermerk_037C_multipel" /></td>
         </tr>
         </logic:present>
         <logic:present name="m" property="umfang_031A">
         <tr>
           <td></td>
           <td></td>
           <td><bean:write name="m" property="umfang_031A" /></td>
         </tr>
         </logic:present>
         <logic:present name="m" property="verknuepfung_groessere_einheit_039B_multipel">
         <tr>
           <td></td>
           <td></td>
           <td><bean:write name="m" property="verknuepfung_groessere_einheit_039B_multipel" /></td>
         </tr>
         </logic:present>
         <tr>
           <td><br></td>
         </tr>
         </logic:iterate>
     </logic:present>
         <tr>
        <td><logic:notEqual name="orderform" property="back" value="0"><a href="orderGbv.do?method=search&back=<bean:write name="orderform" property="back" />&gbvsearch=<bean:write name="orderform" property="gbvsearch" />&gbvfield=<bean:write name="orderform" property="gbvfield" />&manuell=<bean:write name="orderform" property="manuell" />&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel" />&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" /></logic:equal>&author=<bean:write name="orderform" property="author" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><img src="img/left.png" alt="<bean:message key="choosehits.back" />" title="<bean:message key="choosehits.back" />" border="0" /></a></logic:notEqual></td>
        <td><logic:notEqual name="orderform" property="forwrd" value="0"><a href="orderGbv.do?method=search&forwrd=<bean:write name="orderform" property="forwrd" />&gbvsearch=<bean:write name="orderform" property="gbvsearch" />&gbvfield=<bean:write name="orderform" property="gbvfield" />&manuell=<bean:write name="orderform" property="manuell" />&submit=<bean:write name="orderform" property="submit" />&mediatype=<bean:write name="orderform" property="mediatype" /><logic:equal name="orderform" property="mediatype" value="Artikel">&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">&kapitel=<bean:write name="orderform" property="kapitel" />&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" />&seiten=<bean:write name="orderform" property="seiten" /></logic:equal><logic:equal name="orderform" property="mediatype" value="Buch">&buchtitel=<bean:write name="orderform" property="buchtitel" />&isbn=<bean:write name="orderform" property="isbn" />&jahr=<bean:write name="orderform" property="jahr" />&verlag=<bean:write name="orderform" property="verlag" /></logic:equal>&author=<bean:write name="orderform" property="author" />&status=bestellt&lid=56&foruser=<bean:write name="orderform" property="foruser" />&deloptions=<bean:write name="orderform" property="deloptions" />&preisvorkomma=<bean:write name="orderform" property="preisvorkomma" />&preisnachkomma=<bean:write name="orderform" property="preisnachkomma" />&waehrung=<bean:write name="orderform" property="waehrung" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&anmerkungen=<bean:write name="orderform" property="anmerkungen" />&notizen=<bean:write name="orderform" property="notizen" />"><img src="img/right.png" alt="<bean:message key="choosehits.forward" />" title="<bean:message key="choosehits.forward" />" border="0" /></a></logic:notEqual></td>
        <td></td>
      </tr>
      <tr>
         <td><br></td>
       </tr>
     </table>
     
     <logic:notEqual name="userinfo" property="konto.bibliotheksname" value="Demo-Bibliothek">
  <table>
  <tr>
  <td><input type="submit" name="submit" value="weiter im Bestellablauf"></td>
  <td><br></td>
  <td><input type="submit" name="submit" value="GBV-Suche"></td>
  </tr>
  </table>
  
  </logic:notEqual>

  <logic:present name="orderform" property="bid">
   <input name="bid" type="hidden" value="<bean:write name="orderform" property="bid" />" />
  </logic:present>
  <input name="method" type="hidden" value="order" />
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

</logic:equal>


<p></p>

<logic:notPresent name="userinfo" property="konto">
  <p><bean:message key="error.timeout" /></p>
  <p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>
              
</div>


 </body>
</html>
