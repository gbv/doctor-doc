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
 </head>
 <body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<br />
      				
  	<div id="italic">
  	<logic:present name="orderform" property="author"><logic:notEqual name="orderform" property="author" value=""><bean:write name="orderform" property="author" /> : </logic:notEqual></logic:present>
  	<logic:present name="orderform" property="artikeltitel"><logic:notEqual name="orderform" property="artikeltitel" value=""><bean:write name="orderform" property="artikeltitel" />. - </logic:notEqual></logic:present>
  	<logic:present name="orderform" property="zeitschriftentitel"><logic:notEqual name="orderform" property="zeitschriftentitel" value=""><bean:write name="orderform" property="zeitschriftentitel" /></logic:notEqual></logic:present><br />
  	<logic:present name="orderform" property="jahr"><logic:notEqual name="orderform" property="jahr" value=""><bean:write name="orderform" property="jahr" />;</logic:notEqual></logic:present><logic:present name="orderform" property="jahrgang"><logic:notEqual name="orderform" property="jahrgang" value=""><bean:write name="orderform" property="jahrgang" /></logic:notEqual></logic:present><logic:present name="orderform" property="heft"><logic:notEqual name="orderform" property="heft" value="">(<bean:write name="orderform" property="heft" />)</logic:notEqual></logic:present><logic:present name="orderform" property="seiten"><logic:notEqual name="orderform" property="seiten" value="">:<bean:write name="orderform" property="seiten" /></logic:notEqual></logic:present>
  	<logic:present name="orderform" property="issn"><logic:notEqual name="orderform" property="issn" value="">. - ISSN: <bean:write name="orderform" property="issn" /></logic:notEqual></logic:present>
  	<logic:present name="orderform" property="pmid"><logic:notEqual name="orderform" property="pmid" value="">. - PMID: <a href="http://www.ncbi.nlm.nih.gov/pubmed/<bean:write name="orderform" property="pmid" />" target="_blank"><bean:write name="orderform" property="pmid" /></a></logic:notEqual></logic:present>
	</div>

<logic:present name="findfree" property="link">    				
<h3><bean:message key="availresult.intern" /></h3>

<table border="1" cellspacing="0" cellpadding="3">
<tr>
<th id="th-left">
<logic:present name="findfree" property="link_search">
	<a href="<bean:write name="findfree" property="link_search"/>" target="_blank">powered by EZB und ZDB</a>
</logic:present>
<logic:notPresent name="findfree" property="link_search">
	powered by EZB und ZDB
</logic:notPresent>
</th>
</tr>
<tr>
	<td>
<table>
<logic:present name="findfree" property="link">
	<tr>
		<td>
			<bean:define id="linkTit" name="findfree" property="linktitle" type="java.lang.String"/>
			<a href="<bean:write name="findfree" property="link"/>" target="_blank"><bean:message key="<%=linkTit%>" /></a>
		</td>
		<td><br>&nbsp;</br></td>
		<td>
			<logic:present name="findfree" property="message">
				<bean:define id="ffMessage" name="findfree" property="message" type="java.lang.String"/>
				<bean:message key="<%=ffMessage%>" />
			</logic:present>
		</td>
		<td><br>&nbsp;</br></td>
		<td>
			<logic:present name="findfree" property="e_ampel"><logic:equal name="findfree" property="e_ampel" value="green"><img src='img/green.gif' alt="<bean:message key="availresult.img_alt_green" />" title="<bean:message key="availresult.img_alt_green" />" height="20" width="42" border="0"></logic:equal><logic:equal name="findfree" property="e_ampel" value="yellow"><img src='img/yellow.gif' alt="<bean:message key="availresult.img_alt_yellow" />" title="<bean:message key="availresult.img_alt_yellow" />" height="20" width="42" border="0"></logic:equal><logic:equal name="findfree" property="e_ampel" value="red"><img src='img/red.gif' alt="<bean:message key="availresult.img_alt_red" />" title="<bean:message key="availresult.img_alt_red" />" height="20" width="42" border="0"></logic:equal></logic:present>
		</td>
	</tr>
</logic:present>

<logic:present name="findfree" property="link_print">
	<tr>
		<td>
			<bean:define id="linkTitPrint" name="findfree" property="linktitle_print" type="java.lang.String"/>
			<a href="<bean:write name="findfree" property="link_print"/>" target="_blank"><bean:message key="<%=linkTitPrint%>" /></a>
		</td>
		<td><br>&nbsp;</br></td>
		<td>
			<logic:present name="findfree" property="message_print">
				<bean:define id="ffMessagePrint" name="findfree" property="message_print" type="java.lang.String"/>
				<bean:message key="<%=ffMessagePrint%>" />
			</logic:present>
		</td>
		<td><br>&nbsp;</br></td>
		<td>
			<logic:present name="findfree" property="p_ampel"><logic:equal name="findfree" property="p_ampel" value="yellow"><img src='img/yellow.gif' alt="<bean:message key="availresult.img_alt_p_yellow" />" title="<bean:message key="availresult.img_alt_p_yellow" />" height="20" width="42" border="0"></logic:equal></logic:present>
		</td>
	</tr>
	<logic:present name="findfree" property="location_print">
	<tr>
		<td align="center" colspan="5">
			<table border="1">
				<tr>
					<logic:iterate id="location" name="findfree" property="location_print">
						<td><bean:write name="location" /></td>
					</logic:iterate>					
				</tr>
				<tr>
					<logic:iterate id="shelfmark" name="findfree" property="shelfmark_print">
						<td align="center"><bean:write name="shelfmark" /></td>
					</logic:iterate>					
				</tr>
			</table>	
		</td>
	</tr>
	</logic:present>
</logic:present>
</table>
	</td>
</tr>
</table>
<p></p>
</logic:present>

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

 
<h3><bean:message key="availresult.extern" /></h3>

 <logic:present name="orderform" property="issn">
 <logic:notEqual name="orderform" property="issn" value="">
 	<table border="1" cellspacing="0" cellpadding="3">
		<tr>
			<th id="th-left" width="20%"><bean:message key="availresult.deutschland" /></th>
			<th id="th-left" width="15%"><bean:message key="availresult.schweiz" /></th>
			<th id="th-left" width="20%"><bean:message key="availresult.osterreich" /></th>
			<th id="th-left" width="20%"><bean:message key="availresult.england" /></th>
			<th id="th-left" width="25%"><bean:message key="availresult.general" /></th>
		</tr>
		<tr>
			<logic:present name="findfree" property="zdb_link">
			<td><a href="<bean:write name="findfree" property="zdb_link" />" target="_blank"><bean:message key="availresult.zdb" /></a></td>
			</logic:present>
			<logic:notPresent name="findfree" property="zdb_link">
			<td width="20%"><a href="http://zdb-opac.de/CHARSET=ISO-8859-1/DB=1.1/PRS=HOL/CMD?ACT=SRCHA&IKT=8&SRT=YOP&TRM=<bean:write name="orderform" property="issn" />+mat-o&HOLDINGS_YEAR=<bean:write name="orderform" property="jahr" />" target="_blank"><bean:message key="availresult.zdb" /></a></td>
			</logic:notPresent>
			<td><a href="http://www.ubka.uni-karlsruhe.de/hylib-bin/kvk/nph-kvk2.cgi?maske=chzk&timeout=20&title=Schweizer+Zeitschriftenportal+SZP+%3A+Ergebnisanzeige&header=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeige_de.htm&spacer=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeigetop_de.htm&footer=http%3A%2F%2Fead.nb.admin.ch%2Fweb%2Fswiss-serials%2Fanzeigemail_de.htm&lang=de&zeiten=nein&kvk-session=P0ER4LN0&flexpositon_start=1&RERO=&DEUTSCHSCHWEIZ=&WEITERE=&kataloge=CHZK_FRIB&kataloge=CHZK_GENF&kataloge=CHZK_RCBN&kataloge=CHZK_VALAIS&kataloge=CHZK_VAUD&kataloge=CHZK_BASEL&kataloge=CHZK_LUZERN&kataloge=CHZK_STGALLEN&kataloge=ZUERICH&kataloge=CHZK_NEBIS&kataloge=ALEXANDRIA&kataloge=CHZK_BGR&kataloge=HELVETICAT&kataloge=CHZK_SBT&kataloge=CHZK_SGBN&kataloge=LIECHTENSTEIN&kataloge=CHZK_CERN&kataloge=VKCH_KUNSTHAUS&kataloge=CHZK_RPVZ&ALL=&SE=&VORT=&CI=&target=_blank&Timeout=20&SS=<bean:write name="orderform" property="issn" />&inhibit_redirect=1" target="_blank">SZP</a></td>
			<td><a href="http://aleph20-prod-acc.obvsg.at/F?func=find-b&find_code=ISN&request=<bean:write name="orderform" property="issn" />" target="_blank"><bean:message key="availresult.gesamtkatalog" /></a></td>
			<td><a href="sessionbritishlibrary.do?method=redirect&issn=<bean:write name="orderform" property="issn" />" target="_blank">British Library</a></td>
			<td><a href="http://www.google.ch/search?as_q=&num=4&btnG=Google-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encoded" />&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images" target="_blank">Google</a></td>
		</tr>
		<tr>
			<td><a href="http://gateway-bayern.bib-bvb.de/aleph-cgi/bvb_suche?sid=ZDB&find_code_1=ZDB&find_request_1=<bean:write name="orderform" property="zdbid" />" target="_blank">BVB</a></td>
			<td><a href="http://www.ubka.uni-karlsruhe.de/hylib-bin/kvk/nph-chvk.cgi?maske=vk_schweiz&header=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-header_de_10_06_01.html&footer=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-footer_de_29_11_05.html&spacer=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-spacer_de.html&css=http%3A%2F%2Fwww.ubka.uni-karlsruhe.de%2Fkvk%2Fchvk%2Fvk_schweiz-result_01_07.css&target=_blank&kataloge=VKCH_BASEL&kataloge=HELVETICAT&kataloge=VKCH_CONS_GENEVE&kataloge=VKCH_SGB&kataloge=VKCH_GLARUS&kataloge=VKCH_CONS_LAUSANNE&kataloge=VKCH_LAUSANNE&kataloge=VKCH_LUZERN&kataloge=VKCH_SOLOTHURN&kataloge=VKCH_STGALLEN&kataloge=LIECHTENSTEIN&kataloge=VKCH_ZUG&kataloge=VKCH_ZUERICH&kataloge=VKCH_NEBIS&kataloge=VKCH_KUNSTHAUS&kataloge=ALEXANDRIA&kataloge=CHZK_BGR&kataloge=VKCH_THURGAU&kataloge=VKCH_RERO&kataloge=SCHAFFHAUSEN&kataloge=CHZK_SBT&kataloge=STGALLENNETZ&TI=&PY=&AU=&SB=&CI=&SS=<bean:write name="orderform" property="issn" />&ST=&PU=&sortiert=nein" target="_blank">CHVK</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td width="25%"><a href="http://scholar.google.com/scholar?as_q=&num=10&btnG=Scholar-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=" target="_blank">Google Scholar</a></td>
		</tr>
		<tr>
			<td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><a href="http://worldcatlibraries.org/registry/gateway?genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&atitle=<bean:write name="orderform" property="artikeltitel_encoded" />&aulast=<bean:write name="orderform" property="author_encoded" />" target="_blank">WorldCat</a></td>
		</tr>
		<tr>
			<td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK+&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK+</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://www.portal.hebis.de/direktsuche/vonCBS?issn=<bean:write name="orderform" property="issn" />&db=2.1" target="_blank">HeBis</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://rhea.hbz-nrw.de/openurl?sid=DRDOC:doctor-doc&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">DigiBib</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://sfx.kobv.de/cgi-bin/fernleihe3/cgi/nachauth?zdb_id=<bean:write name="orderform" property="zdbid" />" target="_blank">KOBV</a></td>
			<td>&nbsp;</td>
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
  </logic:notEqual>
  </logic:present>
  
  <logic:present name="orderform" property="issn">
 <logic:equal name="orderform" property="issn" value="">
 <div id="italic"><bean:message key="availresult.manuell" /></div><br />
 	<table border="1" cellspacing="0" cellpadding="3">
		<tr>
			<th id="th-left" width="20%"><bean:message key="availresult.deutschland" /></td>
			<th id="th-left" width="15%"><bean:message key="availresult.schweiz" /></td>
			<th id="th-left" width="20%"><bean:message key="availresult.osterreich" /></td>
			<th id="th-left" width="20%"><bean:message key="availresult.england" /></td>
			<th id="th-left" width="25%"><bean:message key="availresult.general" /></th>
		</tr>
		<tr>
			<td width="20%"><a href="http://zdb-opac.de" target="_blank">ZDB</a></td>
			<td width="15%"><a href="http://ead.nb.admin.ch/web/swiss-serials/psp_de.html" target="_blank">SZP</a></td>
			<td width="20%"><a href="http://opac.obvsg.at/acc01" target="_blank"><bean:message key="availresult.gesamtkatalog" /></a></td>
			<td width="20%"><a href="sessionbritishlibrary.do?method=redirect&issn=<bean:write name="orderform" property="issn" />" target="_blank">British Library</a></td>
			<td width="25%"><a href="http://www.google.ch/search?as_q=&num=4&btnG=Google-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encoded" />&as_oq=pdf+full-text&as_eq=&lr=&as_ft=i&as_filetype=&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=images" target="_blank">Google</a></td>
		</tr>
				<tr>
			<td><a href="http://opac.bib-bvb.de:8080/InfoGuideClient.fasttestsis/start.do?" target="_blank">BVB</a></td>
			<td><a href="http://www.chvk.ch/" target="_blank">CHVK</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td width="25%"><a href="http://scholar.google.com/scholar?as_q=&num=10&btnG=Scholar-Suche&as_epq=<bean:write name="orderform" property="artikeltitel_encodedUTF8" />&as_oq=&as_eq=&as_occt=any&as_sauthors=&as_publication=&as_ylo=&as_yhi=&lr=" target="_blank">Google Scholar</a></td>
		</tr>
		<tr>
			<td><a href="http://gso.gbv.de/DB=2.1/" target="_blank">GBV/GVK</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://www.gbv.de/gso/opengso.php?sid=DRDOC:doctor-doc&db=GVK+&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">GBV/GVK+</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://www.portal.hebis.de" target="_blank">HeBis</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://rhea.hbz-nrw.de/openurl?sid=DRDOC:doctor-doc&genre=article&issn=<bean:write name="orderform" property="issn" />&date=<bean:write name="orderform" property="jahr" />&volume=<bean:write name="orderform" property="jahrgang" />&issue=<bean:write name="orderform" property="heft" />&pages=<bean:write name="orderform" property="seiten" />&title=<bean:write name="orderform" property="zeitschriftentitel" />&atitle=<bean:write name="orderform" property="artikeltitel" />&aulast=<bean:write name="orderform" property="author" />" target="_blank">DigiBib</a></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><a href="http://www.kobv.de" target="_blank">KOBV</a></td>
			<td>&nbsp;</td>
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
  </logic:equal>
  </logic:present>

	<logic:equal name="userinfo" property="konto.land" value="Schweiz">
	<!-- This is experimental, so we show this at the moment only in Switzerland.
		 You may remove the above condition for your local installation -->
	<logic:present name="holdings">
	<h3><bean:message key="availresult.singleHoldings" /></h3>
		<table border="1" cellspacing="0" cellpadding="3">
			<tr>
				<th id="th-left"><bean:message key="availresult.library" /></td>
				<th id="th-left"><bean:message key="impressum.contact" /></td>
			</tr>
			<logic:iterate id="hold" name="holdings">
			<tr>
				<td><bean:write name="hold" property="holding.konto.bibliotheksname" /></td>
				<td><a href="mailto:<bean:write name="hold" property="holding.konto.bibliotheksmail" />"><bean:write name="hold" property="holding.konto.bibliotheksmail" /></a></td>
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
	</logic:equal>

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
			<a href="http://217.91.37.16/LISK_VOLLTEXT/index.asp?sid=DRDOC:doctor-doc<bean:write name="orderform" property="link" />" target="_blank">Download</a>
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
