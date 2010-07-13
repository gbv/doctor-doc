<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - Rechnungen</title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content"><br />

<h3>Rechnung versenden:</h3>
Folgende Rechnung f&uuml;r das Konto <bean:write name="billingform"
	property="konto.bibliotheksname" /> an die E-Mail Adresse <bean:write
	name="billingform" property="konto.bibliotheksmail" /> versenden:
<p></p>
<pre><bean:write name="billingform" property="billingtext" /></pre>
<p></p>
<form name="BillingSend" method="post" action="sendbill.do"><input
	type="hidden" name="kontoid"
	value="<bean:write name="billingform" property="konto.id" />" /> <input
	type="hidden" name="betrag"
	value="<bean:write name="billingform" property="bill.betrag" />" /> <input
	type="hidden" name="waehrung"
	value="<bean:write name="billingform" property="bill.waehrung" />" />
<input type="hidden" name="rechnungsdatum"
	value="<bean:write name="billingform" property="bill.rechnungsdatum" />" />
<input type="hidden" name="rechnungsgrundid"
	value="<bean:write name="billingform" property="bill.rechnungsgrund.id" />" />
<input type="hidden" name="manuelltext"
	value="<bean:write name="billingform" property="manuelltext" />" /> <input
	type="hidden" name="method" value="sendBill" /> <input name="action"
	value="Rechnung so versenden" type="submit" /></form>

<p />
<hr />

<h3>Rechnung anpassen:</h3>
<form name="BillingChange" method="post" action="preparebillingtext.do" />
<input type="hidden" name="method" value="prepareBillingText" />
<table border="0">
	<tr>
		<td>Rechnungstext<br></br>
		erg&auml;nzen:</td>
		<td><textarea name="manuelltext" cols="30" rows="5"><bean:write
			name="billingform" property="manuelltext" /></textarea></td>
	</tr>
	<tr>
		<td>Rechnungsbetrag:</td>
		<td><input type="text" name="betrag"
			value="<bean:write name="billingform" property="bill.betrag" />" /></td>
	</tr>
	<tr>
		<td>W&auml;hrung:</td>
		<td><input type="text" name="waehrung"
			value="<bean:write name="billingform" property="bill.waehrung" />" /></td>
	</tr>
	<tr>
		<td>Rechnungsdatum:</td>
		<td><input type="text" name="rechnungsdatum"
			value="<bean:write name="billingform" property="bill.rechnungsdatum" />" />(JJJJ-MM-DD)</td>
	</tr>
	<tr>
		<td>Rechnungsgrund:</td>
		<td><select name="rechnungsgrundid">
			<option
				value="<bean:write name="billingform" property="bill.rechnungsgrund.id" />"><bean:write
				name="billingform" property="bill.rechnungsgrund.inhalt" /></option>
			<logic:iterate id="rgl" name="billingform"
				property="rechnungsgrundliste">
				<option value="<bean:write name="rgl" property="id" />"><bean:write
					name="rgl" property="inhalt" /></option>
			</logic:iterate>
		</select></td>
	</tr>
</table>
<input type="hidden" name="kontoid"
	value="<bean:write name="billingform" property="konto.id" />" /> <input
	name="action" value="Rechnung anpassen" type="submit" />
</form>
</div>

</body>
</html>