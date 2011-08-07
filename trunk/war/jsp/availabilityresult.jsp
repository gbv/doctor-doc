<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="availresult.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  
  <!-- In the head section of the page -->
<script>
<!--
function wopen(url, name, w, h)
{
// Fudge factors for window decoration space.
// Works well on different platforms & browsers.
w += 32;
h += 96;
 var win = window.open(url,
  name,
  'width=' + w + ', height=' + h + ', ' +
  'location=no, menubar=no, ' +
  'status=no, toolbar=no, scrollbars=yes, resizable=yes');
 win.resizeTo(w, h);
 win.focus();
}
// -->
</script> 
  
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />
              
  <div id="italic">
    <logic:notEmpty name="orderform" property="author"><bean:write name="orderform" property="author" /> : </logic:notEmpty>
    <logic:notEmpty name="orderform" property="artikeltitel"><bean:write name="orderform" property="artikeltitel" />. - </logic:notEmpty>
    <logic:notEmpty name="orderform" property="zeitschriftentitel"><bean:write name="orderform" property="zeitschriftentitel" /></logic:notEmpty><br />
    <logic:notEmpty name="orderform" property="jahr"><bean:write name="orderform" property="jahr" />;</logic:notEmpty><logic:notEmpty name="orderform" property="jahrgang"><bean:write name="orderform" property="jahrgang" /></logic:notEmpty><logic:notEmpty name="orderform" property="heft">(<bean:write name="orderform" property="heft" />)</logic:notEmpty><logic:notEmpty name="orderform" property="seiten">:<bean:write name="orderform" property="seiten" /></logic:notEmpty>
    <logic:notEmpty name="orderform" property="issn">. - ISSN: <bean:write name="orderform" property="issn" /></logic:notEmpty>
    <logic:notEmpty name="orderform" property="pmid">. - PMID: <a href="http://www.ncbi.nlm.nih.gov/pubmed/<bean:write name="orderform" property="pmid" />" target="_blank"><bean:write name="orderform" property="pmid" /></a></logic:notEmpty>
  </div>



<h3><bean:message key="availresult.intern" /></h3>

<table style="border-style: solid;" cellspacing="1" cellpadding="4">
	<tr>
		<th id="th-left" colspan="7">
  			<a href="<bean:write name="ezb" property="linkezb"/>" target="_blank">powered by EZB and ZDB</a>
		</th>
	</tr>
<logic:empty name="ezb" property="online">
	<logic:empty name="ezb" property="print">
		<tr>
			<td colspan="7">
				<bean:message key="error.zdb_timeout"/>
			</td>
		</tr>
	</logic:empty>
</logic:empty>
<logic:notEmpty name="ezb" property="online">
	<logic:iterate id="ref" name="ezb" property="online">
		<tr>
			<td style="width:10px;" nowrap="nowrap">
				<logic:equal name="ref" property="ampel" value="green"><img src='img/green.gif' alt="<bean:message key="availresult.img_alt_green" />" title="<bean:message key="availresult.img_alt_green" />" height="20" width="42" border="0" /></logic:equal><logic:equal name="ref" property="ampel" value="yellow"><img src='img/yellow.gif' alt="<bean:message key="availresult.img_alt_yellow" />" title="<bean:message key="availresult.img_alt_yellow" />" height="20" width="42" border="0" /></logic:equal><logic:equal name="ref" property="ampel" value="red"><img src='img/red.gif' alt="<bean:message key="availresult.img_alt_red" />" title="<bean:message key="availresult.img_alt_red" />" height="20" width="42" border="0" /></logic:equal>
				<logic:notEmpty name="ref" property="readme"><a href="<bean:write name="ref" property="readme"/>" alt="Readme" title="Readme" target="_blank"><img border="0" src="img/info.gif" alt="<bean:message key="info.issn" />" /></a></logic:notEmpty>
			</td>
			<td style="width:10px;" nowrap="nowrap">
				<bean:define id="result" name="ref" property="comment" type="java.lang.String"/>
				<bean:message key="<%=result%>" />
			</td>
			<td colspan="5">
				<a href="<bean:write name="ref" property="url"/>" alt="<bean:write name="ref" property="level"/>" title="<bean:write name="ref" property="level"/>" target="_blank"><bean:write name="ref" property="title"/></a>
				<logic:notEmpty name="ref" property="additional">(<bean:write name="ref" property="additional"/>)</logic:notEmpty>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:notEmpty name="ezb" property="print">
	<logic:iterate id="ref" name="ezb" property="print">
		<tr>
			<td style="width:10px;" nowrap="nowrap">
				<logic:equal name="ref" property="ampel" value="yellow"><img src='img/yellow.gif' alt="<bean:message key="availresult.img_alt_p_yellow" />" title="<bean:message key="availresult.img_alt_p_yellow" />" height="20" width="42" border="0" /></logic:equal><logic:equal name="ref" property="ampel" value="red"><img src='img/red.gif' alt="<bean:message key="availresult.img_alt_p_yellow" />" title="<bean:message key="availresult.img_alt_p_yellow" />" height="20" width="42" border="0" /></logic:equal>
			</td>
			<td style="width:10px;" nowrap="nowrap">
				<bean:define id="result" name="ref" property="comment" type="java.lang.String"/>
				<bean:message key="<%=result%>" />
			</td>
			<td style="width:10px;" nowrap="nowrap">
			<logic:notEmpty name="ref" property="info">
				<bean:define id="label" name="ref" property="info.label" type="java.lang.String"/>
				<a href="<bean:write name="ref" property="info.url"/>" target="popup" onclick="wopen('<bean:write name="ref" property="info.url"/>', 'popup', 1040, 880); return false;"><bean:message key="<%=label%>" /></a>
			</logic:notEmpty>
			</td>
			<td style="width:10px;" nowrap="nowrap">
				<logic:notEmpty name="ref" property="coverage">
					(<bean:write name="ref" property="coverage"/>)
				</logic:notEmpty>
			</td>
			<td style="width:10px;" nowrap="nowrap">
				<bean:write name="ref" property="location"/>
			</td>
			<td style="width:10px;" nowrap="nowrap">
				<bean:write name="ref" property="callnr"/>
			</td>
			<td>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
</table>

<logic:present name="userinfo" property="benutzer">
<p />
  <logic:notPresent name="orderform" property="bid">
    <div id="italic"><bean:message key="save.artikel" /> <bean:message key="save.gefunden" /> <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /><logic:present name="orderform" property="foruser">&foruser=<bean:write name="orderform" property="foruser" /></logic:present>&status=erledigt<logic:present name="orderform" property="lieferant.lid">&lid=<bean:write name="orderform" property="lieferant.lid" /></logic:present><logic:present name="orderform" property="deloptions">&deloptions=<bean:write name="orderform" property="deloptions" /></logic:present><logic:notPresent name="orderform" property="deloptions">&deloptions=email</logic:notPresent>&preisvorkomma=0&preisnachkomma=00&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></div>
  </logic:notPresent>
  <logic:present name="orderform" property="bid">
    <div id="italic"><bean:message key="save.artikel" /> <bean:message key="save.gefunden" /> <a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="orderform" property="bid" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></div>
  </logic:present>
<p />
</logic:present>
 

<logic:present name="userinfo" property="benutzer">
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">

 
<h3><bean:message key="tabmenu.slide.search" /></h3>

 <logic:notEmpty name="orderform" property="issn">
   <table border="1" cellspacing="0" cellpadding="3">
    <tr>
      <th id="th-left"><bean:message key="availresult.deutschland" /></th>
      <th id="th-left" colspan="2"><bean:message key="availresult.schweiz" /></th>
      <th id="th-left"><bean:message key="availresult.osterreich" /></th>
      <th id="th-left"><bean:message key="availresult.england" />/<bean:message key="availresult.usa" /></th>
      <th id="th-left"><bean:message key="availresult.general" /></th>
    </tr>
    <tr>
      <td width="20%"><a href="http://zdb-opac.de/CHARSET=ISO-8859-1/DB=1.1/PRS=HOL/CMD?ACT=SRCHA&IKT=8&SRT=YOP&TRM=<bean:write name="orderform" property="issn" />+mat-o&HOLDINGS_YEAR=<bean:write name="orderform" property="jahr" />" target="_blank"><bean:message key="availresult.zdb" /></a></td>
      <td colspan="2"><a href="http://www.ubka.uni-karlsruhe.de/hylib-bin/kvk/nph-kvk2.cgi?maske=chzk&timeout=20&title=Schweizer+Zeitschriftenportal+SZP+%3A+Ergebnisanzeige&header=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeige_de.htm&spacer=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeigetop_de.htm&footer=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeigemail_de.htm&lang=de&zeiten=nein&kvk-session=P0ER4LN0&flexpositon_start=1&RERO=&DEUTSCHSCHWEIZ=&WEITERE=&kataloge=CHZK_FRIB&kataloge=CHZK_GENF&kataloge=CHZK_RCBN&kataloge=CHZK_VALAIS&kataloge=CHZK_VAUD&kataloge=CHZK_BASEL&kataloge=CHZK_LUZERN&kataloge=CHZK_STGALLEN&kataloge=ZUERICH&kataloge=CHZK_NEBIS&kataloge=ALEXANDRIA&kataloge=CHZK_BGR&kataloge=HELVETICAT&kataloge=CHZK_SBT&kataloge=CHZK_SGBN&kataloge=LIECHTENSTEIN&kataloge=CHZK_CERN&kataloge=VKCH_KUNSTHAUS&kataloge=CHZK_RPVZ&ALL=&SE=&VORT=&CI=&target=_blank&Timeout=20&SS=<bean:write name="orderform" property="issn" />&inhibit_redirect=1" target="_blank">SZP</a></td>
      <td><a href="http://aleph20-prod-acc.obvsg.at/F?func=find-b&find_code=ISN&request=<bean:write name="orderform" property="issn" />" target="_blank"><bean:message key="availresult.gesamtkatalog" /></a></td>
      <td><a href="sessionbritishlibrary.do?method=redirect&issn=<bean:write name="orderform" property="issn" />" target="_blank">British Library</a></td>
      <td><a href="http://www.google.ch/search?as_q=&num=10&btnG=Google-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encoded" />&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images" target="_blank">Google</a></td>
    </tr>
    <tr>
      <td><a href="http://sfx.bib-bvb.de/sfx_bvb?sid=nodata&__char_set=utf8&sid=DRDOC:doctor-doc&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">BVB</a></td>
      <td colspan="2"><a href="http://www.ubka.uni-karlsruhe.de/hylib-bin/kvk/nph-chvk.cgi?maske=vk_schweiz&header=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-header_de_10_06_01.html&footer=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-footer_de_29_11_05.html&spacer=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-spacer_de.html&css=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-result_01_07.css&target=_blank&kataloge=VKCH_BASEL&kataloge=HELVETICAT&kataloge=VKCH_CONS_GENEVE&kataloge=VKCH_SGB&kataloge=VKCH_GLARUS&kataloge=VKCH_CONS_LAUSANNE&kataloge=VKCH_LAUSANNE&kataloge=VKCH_LUZERN&kataloge=VKCH_SOLOTHURN&kataloge=VKCH_STGALLEN&kataloge=LIECHTENSTEIN&kataloge=VKCH_ZUG&kataloge=VKCH_ZUERICH&kataloge=VKCH_NEBIS&kataloge=VKCH_KUNSTHAUS&kataloge=ALEXANDRIA&kataloge=CHZK_BGR&kataloge=VKCH_THURGAU&kataloge=VKCH_RERO&kataloge=SCHAFFHAUSEN&kataloge=CHZK_SBT&kataloge=STGALLENNETZ&TI=&PY=&AU=&SB=&CI=&SS=<bean:write name="orderform" property="issn" />&ST=&PU=&sortiert=nein" target="_blank">CHVK</a></td>
      <td>&nbsp;</td>
      <td><a href="http://locatorplus.gov/cgi-bin/Pwebrecon.cgi?SAB1=<bean:write name="orderform" property="issn" />&BOOL1=as+a+phrase&FLD1=ISSN+[xxxx-xxxx]+%28ISSN%29&GRP1=AND+with+next+set&SAB2=&BOOL2=any+of+these&FLD2=Title+%28TKEY%29&GRP2=AND+with+next+set&SAB3=&BOOL3=any+of+these&FLD3=Title+%28TKEY%29&CNT=25&HIST=1" target="_blank">NLM</a></td>
      <td width="25%"><a href="http://scholar.google.com/scholar?as_q=&num=10&btnG=Scholar-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=" target="_blank">Google Scholar</a></td>
    </tr>
    <tr>
      <td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK</a></td>
      <td colspan="2"><a href="http://www.swissbib.ch/TouchPoint/start.do?Language=de&View=nose&Query=540=%22<bean:write name="orderform" property="issn" />%22" target="_blank">Swissbib</a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td><a href="http://worldcatlibraries.org/registry/gateway?genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&atitle=<bean:write name="orderform" property="artikeltitel_encoded" />&aulast=<bean:write name="orderform" property="author_encoded" />" target="_blank">WorldCat</a></td>
    </tr>
    <tr>
      <td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK+&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK+</a></td>
	  <td><a href="http://perunil.unil.ch/perunil/periodiques/FMPro?-db=per-bichi.fp5&-lay=results&-format=results.html&-error=errors.html&Cat=per&-max=25&-Script.PreSort=Recherche&Max=20&Debut=1&-find=chercher&TypeRech=on&TitreRech=<bean:write name="orderform" property="zeitschriftentitel" />" target="_blank">CHUV</a></td>
      <td><a href="http://www2.unil.ch/openillink/?issn=<bean:write name="orderform" property="issn" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&date=<bean:write name="orderform" property="jahr" />&pages=<bean:write name="orderform" property="seiten" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />&id=<logic:notEmpty name="orderform" property="pmid">pmid%3A<bean:write name="orderform" property="pmid" /></logic:notEmpty>&sid=doctor-doc&pid=&remarques=" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://www.portal.hebis.de/direktsuche/vonCBS?issn=<bean:write name="orderform" property="issn" />&db=2.1" target="_blank">HeBis</a></td>
      <td><a href="http://library.epfl.ch/periodicals/?stype=&ti=&pu=&me=&status=&lib=&shelfmark=&-find=CHERCHER&issn=<bean:write name="orderform" property="issn" />" target="_blank">EPFL</a></td>
      <td><a href="http://library.epfl.ch/pret-inter/?pg=article&pSerial=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />&pYear=<bean:write name="orderform" property="jahr" />&pVolume=<bean:write name="orderform" property="jahrgang" />&pIssue=<bean:write name="orderform" property="heft" />&pPage=<bean:write name="orderform" property="seiten" />&pAuthor=<bean:write name="orderform" property="author_encodedUTF8" />&pTitle=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&pIsbn=<bean:write name="orderform" property="issn" />&uComment=&uName=<bean:write name="userinfo" property="konto.bibliotheksname" />%20%2F%20<bean:write name="userinfo" property="benutzer.vorname" />%20<bean:write name="userinfo" property="benutzer.name" />&uStatus=other&uNebis=<logic:present name="userinfo" property="konto.idsid"><bean:write name="userinfo" property="konto.idsid" /></logic:present>&uEmail=<bean:write name="userinfo" property="konto.dbsmail" />&uAddress=<bean:write name="userinfo" property="konto.adresse" />%2C%20<bean:write name="userinfo" property="konto.adressenzusatz" />%2C%20<bean:write name="userinfo" property="konto.PLZ" />%20<bean:write name="userinfo" property="konto.ort" />&uPhone=<bean:write name="userinfo" property="konto.telefon" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://www.digibib.net/openurl?sid=DRDOC:doctor-doc&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">DigiBib</a></td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=ISSN&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="issn" />" target="_blank">UB Basel</a></td>
      <td><a href="redirectunibasel.do?issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel"/>&author=<bean:write name="orderform" property="author" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://sfx.kobv.de/cgi-bin/fernleihe3/cgi/nachauth?zdb_id=<bean:write name="orderform" property="zdbid" />" target="_blank">KOBV</a></td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=ISSN&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="issn" />" target="_blank">UB Bern</a></td>
      <td><a href="http://www.zb.unibe.ch/unicd/docdel.php?Journal=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />&Author=<bean:write name="orderform" property="author_encodedUTF8" />&Article=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&Volume=<bean:write name="orderform" property="jahrgang" />&Issue=<bean:write name="orderform" property="heft" />&Year=<bean:write name="orderform" property="jahr" />&Pages=<bean:write name="orderform" property="seiten" />&ISSN=<bean:write name="orderform" property="issn" />&meduid=<bean:write name="orderform" property="pmid" />&sid=doctor-doc&Publisher=<bean:write name="orderform" property="verlag_encodedUTF8" />&PubliPlace=&ou=&bennr=<logic:present name="userinfo" property="konto.idsid"><bean:write name="userinfo" property="konto.idsid" /></logic:present>&passwort=<logic:present name="userinfo" property="konto.idspasswort"><bean:write name="userinfo" property="konto.idspasswort" /></logic:present>&wo_bestellen=schweiz&mitteilung=" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=ISSN&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="issn" />" target="_blank">MBC Z&uuml;rich</a></td>
      <td><a href="redirectmbczurich.do?issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded"/>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    
  </table>
  
  <p><a href="http://www.ubka.uni-karlsruhe.de/kvk.html?SS=<bean:write name="orderform" property="issn" />" target="_blank"><bean:message key="availresult.kvk" /></a></p>
  
  <p>
    <logic:notPresent name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /><logic:present name="orderform" property="foruser">&foruser=<bean:write name="orderform" property="foruser" /></logic:present>&status=bestellt&deloptions=email&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:notPresent>
    <logic:present name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="orderform" property="bid" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:present>
  </p>
  </logic:notEmpty>
  
  <logic:empty name="orderform" property="issn">
 <div id="italic"><bean:message key="availresult.manuell" /></div><br />
   <table border="1" cellspacing="0" cellpadding="3">
    <tr>
      <th id="th-left"><bean:message key="availresult.deutschland" /></th>
      <th id="th-left" colspan="2"><bean:message key="availresult.schweiz" /></th>
      <th id="th-left"><bean:message key="availresult.osterreich" /></th>
      <th id="th-left"><bean:message key="availresult.england" />/<bean:message key="availresult.usa" /></th>
      <th id="th-left"><bean:message key="availresult.general" /></th>
    </tr>
    <tr>
      <td><a href="http://zdb-opac.de" target="_blank">ZDB</a></td>
      <td  colspan="2"><a href="http://ead.nb.admin.ch/web/swiss-serials/psp_de.html" target="_blank">SZP</a></td>
      <td><a href="http://aleph20-prod-acc.obvsg.at/F?CON_LNG=ger&func=file&file_name=start&local_base=acczs" target="_blank"><bean:message key="availresult.gesamtkatalog" /></a></td>
      <td><a href="sessionbritishlibrary.do?method=redirect&issn=<bean:write name="orderform" property="issn" />" target="_blank">British Library</a></td>
      <td><a href="http://www.google.ch/search?as_q=&num=4&btnG=Google-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encoded" />&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images" target="_blank">Google</a></td>
    </tr>
        <tr>
      <td><a href="https://opac.bib-bvb.de/InfoGuideClient.fasttestsis/start.do" target="_blank">BVB</a></td>
      <td colspan="2"><a href="http://www.chvk.ch/" target="_blank">CHVK</a></td>
      <td>&nbsp;</td>
      <td><a href="http://locatorplus.gov/cgi-bin/Pwebrecon.cgi?Search_Arg=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&Search_Code=JALL&CNT=25&HIST=1" target="_blank">NLM</a></td>
      <td width="25%"><a href="http://scholar.google.com/scholar?as_q=&num=10&btnG=Scholar-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=" target="_blank">Google Scholar</a></td>
    </tr>
    <tr>
      <td><a href="http://gso.gbv.de/DB=2.1/" target="_blank">GBV/GVK</a></td>
      <td colspan="2"><a href="http://www.swissbib.ch/TouchPoint/start.do?Language=de&View=nose&Query=331=%22<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />%22" target="_blank">Swissbib</a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK+&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK+</a></td>
	  <td><a href="http://perunil.unil.ch/perunil/periodiques/FMPro?-db=per-bichi.fp5&-lay=results&-format=results.html&-error=errors.html&Cat=per&-max=25&-Script.PreSort=Recherche&Max=20&Debut=1&-find=chercher&TypeRech=on&TitreRech=<bean:write name="orderform" property="zeitschriftentitel" />" target="_blank">CHUV</a></td>
      <td><a href="http://www2.unil.ch/openillink/?issn=<bean:write name="orderform" property="issn" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&date=<bean:write name="orderform" property="jahr" />&pages=<bean:write name="orderform" property="seiten" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />&id=<logic:notEmpty name="orderform" property="pmid">pmid%3A<bean:write name="orderform" property="pmid" /></logic:notEmpty>&sid=doctor-doc&pid=&remarques=" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://www.portal.hebis.de" target="_blank">HeBis</a></td>
      <td><a href="http://library.epfl.ch/periodicals/?stype=&ti=<bean:write name="orderform" property="zeitschriftentitel" />&pu=&issn=&me=&status=&lib=&shelfmark=&-find=CHERCHER" target="_blank">EPFL</a></td>
      <td><a href="http://library.epfl.ch/pret-inter/?pg=article&pSerial=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />&pYear=<bean:write name="orderform" property="jahr" />&pVolume=<bean:write name="orderform" property="jahrgang" />&pIssue=<bean:write name="orderform" property="heft" />&pPage=<bean:write name="orderform" property="seiten" />&pAuthor=<bean:write name="orderform" property="author_encodedUTF8" />&pTitle=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&pIsbn=<bean:write name="orderform" property="issn" />&uComment=&uName=<bean:write name="userinfo" property="konto.bibliotheksname" />%20%2F%20<bean:write name="userinfo" property="benutzer.vorname" />%20<bean:write name="userinfo" property="benutzer.name" />&uStatus=other&uNebis=<logic:present name="userinfo" property="konto.idsid"><bean:write name="userinfo" property="konto.idsid" /></logic:present>&uEmail=<bean:write name="userinfo" property="konto.dbsmail" />&uAddress=<bean:write name="userinfo" property="konto.adresse" />%2C%20<bean:write name="userinfo" property="konto.adressenzusatz" />%2C%20<bean:write name="userinfo" property="konto.PLZ" />%20<bean:write name="userinfo" property="konto.ort" />&uPhone=<bean:write name="userinfo" property="konto.telefon" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://www.digibib.net/openurl?sid=DRDOC:doctor-doc&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">DigiBib</a></td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=WRD&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />" target="_blank">UB Basel</a></td>
      <td><a href="redirectunibasel.do?issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel"/>&author=<bean:write name="orderform" property="author" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><a href="http://sfx.kobv.de/cgi-bin/fernleihe3/cgi/nachauth" target="_blank">KOBV</a></td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=WRD&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />" target="_blank">UB Bern</a></td>
      <td><a href="http://www.zb.unibe.ch/unicd/docdel.php?Journal=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />&Author=<bean:write name="orderform" property="author_encodedUTF8" />&Article=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&Volume=<bean:write name="orderform" property="jahrgang" />&Issue=<bean:write name="orderform" property="heft" />&Year=<bean:write name="orderform" property="jahr" />&Pages=<bean:write name="orderform" property="seiten" />&ISSN=<bean:write name="orderform" property="issn" />&meduid=<bean:write name="orderform" property="pmid" />&sid=doctor-doc&Publisher=<bean:write name="orderform" property="verlag_encodedUTF8" />&PubliPlace=&ou=&bennr=<logic:present name="userinfo" property="konto.idsid"><bean:write name="userinfo" property="konto.idsid" /></logic:present>&passwort=<logic:present name="userinfo" property="konto.idspasswort"><bean:write name="userinfo" property="konto.idspasswort" /></logic:present>&wo_bestellen=schweiz&mitteilung=" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><a href="http://ml.metabib.ch/V/?func=quick-1-check1&mode=advanced&find_request_1=&find_code_2=WRD&find_op_1=AND&find_code_3=WRD&find_request_3=&group_number=000002060&find_request_2=<bean:write name="orderform" property="zeitschriftentitel_encodedUTF8" />" target="_blank">MBC Z&uuml;rich</a></td>
      <td><a href="redirectmbczurich.do?issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded"/>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" target="_blank"><bean:message key="tabmenu.slide.order"/></a></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
  
  <p><a href="http://www.ubka.uni-karlsruhe.de/kvk.html" target="_blank"><bean:message key="availresult.kvk" /></a></p>
  
  <p>
    <logic:notPresent name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /><logic:present name="orderform" property="foruser">&foruser=<bean:write name="orderform" property="foruser" /></logic:present>&status=bestellt&deloptions=email&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:notPresent>
    <logic:present name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="orderform" property="bid" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:present>
  </p>
  </logic:empty>

  <logic:present name="holdings">
  
  <h3><bean:message key="availresult.singleHoldings" /></h3>
    <table border="1" cellspacing="0" cellpadding="3">
      <tr>
        <th id="th-left"><bean:message key="availresult.library" /></th>
        <th><bean:message key="bestellform.zeitschrift" /></th>
        <th><bean:message key="availresult.titel" /></th>
        <th><bean:message key="stockimport.loc" /></th>
        <th><bean:message key="bestellform.bemerkungen" /></th>
        <th>&nbsp;</th>
      </tr>
      <logic:iterate id="hold" name="holdings">
          <tr>
            <td><bean:write name="hold" property="holding.konto.bibliotheksname" /></td>
            <td><bean:write name="hold" property="holding.titel" /></td>
            <td>
              <logic:present name="hold" property="holding.baseurl">
              	<a href="<bean:write name="hold" property="holding.baseurl" />/stockinfo.do?holding=<bean:write name="hold" property="holding.id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" 
            		target="popup" onclick="wopen('<bean:write name="hold" property="holding.baseurl" />/stockinfo.do?holding=<bean:write name="hold" property="holding.id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />', 'popup', 1040, 880); return false;"><bean:message key="tabmenu.slide.holdings" /></a>
              </logic:present>
              <logic:notPresent name="hold" property="holding.baseurl">
            	<logic:notEmpty name="hold" property="startyear"><bean:write name="hold" property="startyear" /></logic:notEmpty><logic:notEmpty name="hold" property="startvolume">, <bean:message key="availresult.volume" /> <bean:write name="hold" property="startvolume" /></logic:notEmpty><logic:notEmpty name="hold" property="startissue">, <bean:message key="availresult.issue" /> <bean:write name="hold" property="startissue" /></logic:notEmpty> -
    			<logic:notEmpty name="hold" property="endyear"><bean:write name="hold" property="endyear" /></logic:notEmpty><logic:notEmpty name="hold" property="endvolume">, <bean:message key="availresult.volume" /> <bean:write name="hold" property="endvolume" /></logic:notEmpty><logic:notEmpty name="hold" property="endissue">, <bean:message key="availresult.issue" /> <bean:write name="hold" property="endissue" /></logic:notEmpty>
    		  </logic:notPresent>
            </td>
            <td><bean:write name="hold" property="standort.inhalt" />&nbsp;</td>
            <td><bean:write name="hold" property="bemerkungen" />&nbsp;</td>
            <td>
            	<logic:present name="hold" property="holding.baseurl">
            		<a href="<bean:write name="hold" property="holding.baseurl" />/stockinfo.do?stock=<bean:write name="hold" property="id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />" 
            		target="popup" onclick="wopen('<bean:write name="hold" property="holding.baseurl" />/stockinfo.do?stock=<bean:write name="hold" property="id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />', 'popup', 1040, 880); return false;"><bean:message key="availresult.details" /></a>
            	</logic:present>
            	<logic:notPresent name="hold" property="holding.baseurl">
            		<a href="stockinfo.do?stock=<bean:write name="hold" property="id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"
            		target="popup" onclick="wopen('stockinfo.do?stock=<bean:write name="hold" property="id" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />', 'popup', 1040, 880); return false;"><bean:message key="availresult.details" /></a>
            	</logic:notPresent>
            </td>
          </tr>
      </logic:iterate>
    </table>    
  <p>
    <logic:notPresent name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="prepareJournalSave.do?method=prepareJournalSave&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" /><logic:present name="orderform" property="foruser">&foruser=<bean:write name="orderform" property="foruser" /></logic:present>&status=bestellt&deloptions=email&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel_encoded"><bean:write name="orderform" property="zeitschriftentitel_encoded"/></logic:present>&author=<bean:write name="orderform" property="author_encoded" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:notPresent>
    <logic:present name="orderform" property="bid">
      <font color="white"><i><bean:message key="save.extern" /> <a href="preparemodifyorder.do?method=prepareModifyOrder&bid=<bean:write name="orderform" property="bid" />"><font color="white"><bean:message key="save.speichern" /></font></a> <bean:message key="save.statistik" /></i></font>
    </logic:present>
  </p>    
  </logic:present>

</logic:notEqual>
</logic:present>

<logic:present name="orderform" property="carelit">
<logic:equal name="orderform" property="carelit" value="true">

<logic:notPresent name="userinfo" property="benutzer"><h3><bean:message key="availresult.carelit" /></h3></logic:notPresent>

<table border="1" cellspacing="0" cellpadding="3">
<tr>
<th id="th-left">
  <bean:message key="availresult.carelit" />
</th>
</tr>
<tr>
  <td>
<table>
  <tr>
    <td>
      <a href="http://217.91.37.16/LISK_VOLLTEXT/index.asp?sid=DRDOC:doctor-doc&<bean:write name="orderform" property="link" />" target="_blank">Download</a>
    </td>
  </tr>
</table>
  </td>
</tr>
</table>
</logic:equal>
</logic:present>
<p></p>
<br></br>
 

  <form action="journalorder2.do" method="post">
  <input name="method" type="hidden" value="prepare" /> 
    
    <logic:present name="orderform" property="bid">
     <input name="bid" type="hidden" value="<bean:write name="orderform" property="bid" />" />
    </logic:present>
    
    <logic:present name="orderform" property="artikeltitel">
     <input name="artikeltitel" type="hidden" value="<bean:write name="orderform" property="artikeltitel" />" />
    </logic:present>
    
    <logic:present name="orderform" property="artikeltitel_encoded">
     <input name="artikeltitel_encoded" type="hidden" value="<bean:write name="orderform" property="artikeltitel_encoded" />" />
    </logic:present>
  
  <logic:present name="orderform" property="issn">
     <input name="issn" type="hidden" value="<bean:write name="orderform" property="issn" />" />
    </logic:present>
  
  <logic:present name="orderform" property="zeitschriftentitel">
     <input name="zeitschriftentitel" type="hidden" value="<bean:write name="orderform" property="zeitschriftentitel" />" />
    </logic:present>
   
      <logic:present name="orderform" property="jahr">
     <input name="jahr" type="hidden" value="<bean:write name="orderform" property="jahr" />" />
  </logic:present> 
  
  <logic:present name="orderform" property="jahrgang">
     <input name="jahrgang" type="hidden" value="<bean:write name="orderform" property="jahrgang" />" />
  </logic:present>
  
  <logic:present name="orderform" property="heft">
     <input name="heft" type="hidden" value="<bean:write name="orderform" property="heft" />" />
  </logic:present>
  
  <logic:present name="orderform" property="seiten">
     <input name="seiten" type="hidden" value="<bean:write name="orderform" property="seiten" />" />
  </logic:present>
  
    <logic:present name="orderform" property="author">
     <input name="author" type="hidden" value="<bean:write name="orderform" property="author" />" />
  </logic:present>
  <logic:present name="orderform" property="language">
     <input name="language" type="hidden" value="<bean:write name="orderform" property="language" />" />
  </logic:present>
  
  <logic:present name="orderform" property="author_encoded">
     <input name="author_encoded" type="hidden" value="<bean:write name="orderform" property="author_encoded" />" />
  </logic:present>
  
  <logic:present name="orderform" property="resolver">
     <input name="resolver" type="hidden" value="<bean:write name="orderform" property="resolver" />" />
  </logic:present>
  <logic:present name="orderform" property="kkid">
     <input name="kkid" type="hidden" value="<bean:write name="orderform" property="kkid" />" />
  </logic:present>
  <logic:present name="orderform" property="bkid">
     <input name="bkid" type="hidden" value="<bean:write name="orderform" property="bkid" />" />
  </logic:present>
  <logic:present name="orderform" property="doi">
     <input name="doi" type="hidden" value="<bean:write name="orderform" property="doi" />" />
  </logic:present>
  <logic:present name="orderform" property="pmid">
     <input name="pmid" type="hidden" value="<bean:write name="orderform" property="pmid" />" />
  </logic:present>
  <logic:present name="orderform" property="sici">
     <input name="sici" type="hidden" value="<bean:write name="orderform" property="sici" />" />
  </logic:present>
  <logic:present name="orderform" property="lccn">
     <input name="lccn" type="hidden" value="<bean:write name="orderform" property="lccn" />" />
  </logic:present>
  <logic:present name="orderform" property="author">
     <input name="author" type="hidden" value="<bean:write name="orderform" property="author" />" />
  </logic:present>
  <logic:present name="orderform" property="isbn">
     <input name="isbn" type="hidden" value="<bean:write name="orderform" property="isbn" />" />
  </logic:present>
  <logic:present name="orderform" property="mediatype">
     <input name="mediatype" type="hidden" value="<bean:write name="orderform" property="mediatype" />" />
  </logic:present>
  <logic:present name="orderform" property="genre">
     <input name="genre" type="hidden" value="<bean:write name="orderform" property="genre" />" />
  </logic:present>
  <logic:present name="orderform" property="rfr_id">
     <input name="rfr_id" type="hidden" value="<bean:write name="orderform" property="rfr_id" />" />
  </logic:present>
  <logic:present name="orderform" property="verlag">
     <input name="verlag" type="hidden" value="<bean:write name="orderform" property="verlag" />" />
  </logic:present>
  <logic:present name="orderform" property="kapitel">
     <input name="kapitel" type="hidden" value="<bean:write name="orderform" property="kapitel" />" />
  </logic:present>
  <logic:present name="orderform" property="buchtitel">
     <input name="buchtitel" type="hidden" value="<bean:write name="orderform" property="buchtitel" />" />
  </logic:present>
  <logic:present name="orderform" property="foruser">
      <input name="foruser" type="hidden" value="<bean:write name="orderform" property="foruser" />" />
  </logic:present>
    
  <table>
    <tr>
    <logic:notPresent name="userinfo" property="benutzer">
      <td><input type="submit" name="submit" value="<bean:message key="availresult.submitweiter" />"></input></td>
    </logic:notPresent>
    <logic:present name="userinfo" property="benutzer">
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
          <td><logic:equal name="orderform" property="mediatype" value="Artikel"><input type="submit" name="submit" value="<bean:message key="availresult.submitsubito" />"></input></logic:equal><logic:notEqual name="orderform" property="mediatype" value="Artikel"><input type="submit" name="submit" value="Bestellangaben speichern"></input></logic:notEqual></td>
          <td>&nbsp;<br />&nbsp;</td>
          <td><logic:present name="userinfo" property="konto.gbvbenutzername"><input type="submit" name="submit" value="<bean:message key="availresult.submitgbv" />"></input></logic:present></td>
        </logic:notEqual>
        <logic:equal name="userinfo" property="benutzer.rechte" value="1">
          <td><input type="submit" name="submit" value="<bean:message key="availresult.submitbibliothek" />"></input></td>
          <td>&nbsp;<br />&nbsp;</td>
          <td><logic:equal name="userinfo" property="benutzer.userbestellung" value="true"><input type="submit" name="submit" value="<bean:message key="availresult.submitsubito" />"></input></logic:equal></td>
          <td>&nbsp;<br />&nbsp;</td>
          <logic:present name="userinfo" property="konto.gbvrequesterid">
      <logic:present name="userinfo" property="konto.isil">
          <td><logic:present name="userinfo" property="konto.gbvbenutzername"><logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true"><input type="submit" name="submit" value="<bean:message key="availresult.submitgbv" />"></input></logic:equal></logic:present></td>
          </logic:present>
      </logic:present>
        </logic:equal>
     </logic:present>
    </tr>
  </table>

  </form>
  
</div>

 </body>
</html>
