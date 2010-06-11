<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

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
		<td id="submenu" nowrap title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
		<td id="submenuactive" nowrap title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
	<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">	
		<td id="submenu" nowrap title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		<logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
	</logic:notEqual>
	<logic:equal name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" nowrap title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
		<logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
				<td id="submenu" nowrap title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		</logic:equal>
		<logic:present name="userinfo" property="konto.gbvrequesterid">
		<logic:present name="userinfo" property="konto.isil">
		<logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
				<logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
		</logic:equal>
		</logic:present>
		</logic:present>
	</logic:equal>
			<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" nowrap title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
			</logic:notEqual>
	</tr>
</table>

<div class="content"><br />
<br />

<logic:present name="userinfo" property="konto">

	<h3><bean:message key="issnsearch.header1" /></h3>


	<table>
	
		<html:form action="findissn.do" method="post"
			focus="zeitschriftentitel">
			<logic:present name="orderform" property="mediatype">
				<logic:equal name="orderform" property="mediatype" value="Artikel">
					<logic:equal name="orderform" property="issn" value="">
						<tr>
							<td colspan="2">
							<div id="italic"><bean:message key="issnsearch.issn_comment" /></div>
							</td>
						</tr>
					</logic:equal>
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
						size="9" maxlength="15" /> oder</td>
				</tr>
					<tr>
						<td><bean:message key="bestellform.issn" /></td>
						<td><input name="issn" value="" type="text" size="9"
							maxlength="9" /> oder</td>
					</tr>
					<tr>
						<td><bean:message key="bestellform.zeitschrift" /></td>
						<td><input name="zeitschriftentitel" value="" type="text"
							size="60" maxlength="100" /> <bean:message key="issnsearch.zeitschrift_example" /></td>
					</tr>

					<logic:notEqual name="orderform" property="issn" value="">
						<tr>
							<td></td>
							<td><input type="submit" value="<bean:message key="issnsearch.submit_neu_suchen" />"></input></td>
						</tr>
					</logic:notEqual>
					<logic:equal name="orderform" property="issn" value="">
						<tr>
							<td></td>
							<td><input type="submit" value="<bean:message key="issnsearch.submit_fehlende_issn" />"></input></td>
						</tr>
					</logic:equal>
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
			<input name="didYouMean" type="hidden" value="<bean:write name="orderform" property="didYouMean" />" />
 			<input name="checkDidYouMean" type="hidden" value="<bean:write name="orderform" property="checkDidYouMean" />" />

		</html:form>

		<tr>
			<td><br />
			</td>
		</tr>

		<tr>
			<td colspan="2">
			<h3><bean:message key="issnsearch.header2" /></h3>
			</td>
		</tr>
		<logic:notEqual name="orderform" property="artikeltitel" value="">
			<tr>
				<td colspan="2">
				<div id="italic"><bean:message key="issnsearch.autocomplete_comment" /></div>
				</td>
			</tr>
		</logic:notEqual>

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
						size="9" maxlength="9" /> <logic:equal name="orderform"
						property="issn" value="">
						<font color="white"><i> <bean:message key="issnsearch.issn_comment2" /></i></font>
					</logic:equal></td>
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
						size="4" maxlength="4" /> <bean:message key="issnsearch.jahrgang_example" /></td>
				</tr>
				<tr>
					<td><bean:message key="bestellform.heft" />&nbsp;</td>
					<td><input name="heft" type="text"
						value="<bean:write name="orderform" property="heft" />" size="4"
						maxlength="4" /> <bean:message key="issnsearch.heft_example" /></td>
				</tr>
				<tr>
					<td><bean:message key="bestellform.seiten" />&nbsp;</td>
					<td><input name="seiten" type="text"
						value="<bean:write name="orderform" property="seiten" />"
						size="15" maxlength="15" /></td>
				</tr>
				<tr>
					<td>
					<td><input name="pmid" type="hidden"
						value="<bean:write name="orderform" property="pmid" />" size="60"
						maxlength="200" /></td>
				</tr>
				<tr>
					<td>
					<td><input name="doi" type="hidden"
						value="<bean:write name="orderform" property="doi" />" size="60"
						maxlength="200" /></td>
				</tr>
			</logic:equal>

			<tr>
				<td></td>
				<td><input type="submit" value="<bean:message key="issnsearch.submit_check_availability" />"></input></td>
			</tr>

			<tr>
				<td><br />
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
				<p><logic:present name="js"
					property="zeitschriftentitel_encoded">
					<i><b><a
						href="http://www.google.ch/search?hl=de&btnG=Google-Suche&meta&q=issn+%22<bean:write name="js" property="zeitschriftentitel_encoded" />%22"
						target="_blank"><bean:message key="issnsearch.google_zeitschrift" /></a></b></i>
					<br />
				</logic:present>
			</logic:iterate>

			<logic:iterate id="js" name="journalseek" property="zeitschriften">
				<logic:present name="js" property="artikeltitel_encoded">
					<i><b><a
						href="http://www.google.ch/search?hl=de&btnG=Google-Suche&meta&q=issn+%22<bean:write name="js" property="artikeltitel_encoded" />%22"
						target="_blank"><bean:message key="issnsearch.google_artikeltitel" /></a></b></i>
					<br />
					<p><bean:message key="bestellform.artikeltitel" />: <bean:write name="js" property="artikeltitel" />
					<input name="artikeltitel" type="hidden"
						value="<bean:write name="js" property="artikeltitel" />" /></p>
				</logic:present>
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
