<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message key="availresult.details" /></title>
 </head>
 <body>

<h3><bean:message key="availresult.library" /></h3>

<table border="0">
	<tr>
		<td>
			<bean:write name="konto" property="bibliotheksname" />
		</td>
	</tr>
	<tr>
		<td>
			<bean:write name="konto" property="adresse" />
		</td>
	</tr>
	<logic:present name="konto" property="adressenzusatz">
	<logic:notEqual name="konto" property="adressenzusatz" value="">
	<tr>
		<td>
			<bean:write name="konto" property="adressenzusatz" />
		</td>
	</tr>
	</logic:notEqual>
	</logic:present>
	<tr>
		<td>
			<bean:write name="konto" property="land" />-<bean:write name="konto" property="PLZ" />&nbsp;<bean:write name="konto" property="ort" />
		</td>
	</tr>
	<tr>
		<td>
			<a href="mailto:<bean:write name="konto" property="bibliotheksmail" />"><bean:write name="konto" property="dbsmail" /></a>
		</td>
	</tr>

</table>

<logic:notEqual name="orderform" property="issn" value="">
<!-- Show only when we have an ISSN -->
<h3><bean:message key="issnsearch.titel" /></h3>

  <logic:present name="orderform" property="author"><logic:notEqual name="orderform" property="author" value=""><bean:write name="orderform" property="author" /> : </logic:notEqual></logic:present>
  <logic:present name="orderform" property="artikeltitel"><logic:notEqual name="orderform" property="artikeltitel" value=""><bean:write name="orderform" property="artikeltitel" />. - </logic:notEqual></logic:present>
  <logic:present name="orderform" property="zeitschriftentitel"><logic:notEqual name="orderform" property="zeitschriftentitel" value=""><bean:write name="orderform" property="zeitschriftentitel" /></logic:notEqual></logic:present><br />
  <logic:present name="orderform" property="jahr"><logic:notEqual name="orderform" property="jahr" value=""><bean:write name="orderform" property="jahr" />;</logic:notEqual></logic:present><logic:present name="orderform" property="jahrgang"><logic:notEqual name="orderform" property="jahrgang" value=""><bean:write name="orderform" property="jahrgang" /></logic:notEqual></logic:present><logic:present name="orderform" property="heft"><logic:notEqual name="orderform" property="heft" value="">(<bean:write name="orderform" property="heft" />)</logic:notEqual></logic:present><logic:present name="orderform" property="seiten"><logic:notEqual name="orderform" property="seiten" value="">:<bean:write name="orderform" property="seiten" /></logic:notEqual></logic:present>
  <logic:present name="orderform" property="issn"><logic:notEqual name="orderform" property="issn" value="">. - ISSN: <bean:write name="orderform" property="issn" /></logic:notEqual></logic:present>
  <logic:present name="orderform" property="pmid"><logic:notEqual name="orderform" property="pmid" value="">. - PMID: <a href="http://www.ncbi.nlm.nih.gov/pubmed/<bean:write name="orderform" property="pmid" />" target="_blank"><bean:write name="orderform" property="pmid" /></a></logic:notEqual></logic:present>
</logic:notEqual>

<h3><bean:message key="availresult.link_title_print" /></h3>
<table>
	
	<logic:iterate id="stock" name="holdings">
	
	<tr>
		<td>
			<bean:message key="bestellform.zeitschrift" />:&nbsp;
		</td>
		<td>
    		<bean:write name="stock" property="holding.titel" />
    	</td>
    </tr>
    <tr>
		<td>
			ISSN:&nbsp;
		</td>
		<td>
    		<bean:write name="stock" property="holding.issn" />
    	</td>
    </tr>
    <tr>
		<td>
			<bean:message key="availresult.titel" />:&nbsp;
		</td>
		<td>
    		<logic:present name="stock" property="startyear"><logic:notEqual name="stock" property="startyear" value=""><bean:write name="stock" property="startyear" />;</logic:notEqual></logic:present><logic:present name="stock" property="startvolume"><logic:notEqual name="stock" property="startvolume" value=""><bean:write name="stock" property="startvolume" /></logic:notEqual></logic:present><logic:present name="stock" property="startissue"><logic:notEqual name="stock" property="startissue" value="">(<bean:write name="stock" property="startissue" />)</logic:notEqual></logic:present> -
    		<logic:present name="stock" property="endyear"><logic:notEqual name="stock" property="endyear" value=""><bean:write name="stock" property="endyear" />;</logic:notEqual></logic:present><logic:present name="stock" property="endvolume"><logic:notEqual name="stock" property="endvolume" value=""><bean:write name="stock" property="endvolume" /></logic:notEqual></logic:present><logic:present name="stock" property="endissue"><logic:notEqual name="stock" property="endissue" value="">(<bean:write name="stock" property="endissue" />)</logic:notEqual></logic:present> 
    	</td>
    </tr>
    <logic:present name="stock" property="bemerkungen">
    <logic:notEqual name="stock" property="bemerkungen" value="">
    <tr>
		<td>
			<bean:message key="bestellform.bemerkungen" />:&nbsp;
		</td>
		<td>
    		<bean:write name="stock" property="bemerkungen" />
    	</td>
    </tr>
    </logic:notEqual>
    </logic:present>
    <tr><td><br /></td></tr>
    
     </logic:iterate>
     	
</table>

<tiles:insert page="import/footer.jsp" flush="true" />
 
 </body>
</html>
