<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en_US" xml:lang="en_US">

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
		<td id="submenu" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
		<td id="submenu" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
	<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">	
		<td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive"</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu"</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		<logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive"</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu"</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
	</logic:notEqual>
	<logic:equal name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
		<logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
				<td <logic:notEqual name="orderform" property="submit" value="GBV">id="submenuactive"</logic:notEqual><logic:equal name="orderform" property="submit" value="GBV">id="submenu"</logic:equal>title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
		</logic:equal>
		<logic:present name="userinfo" property="konto.gbvrequesterid">
		<logic:present name="userinfo" property="konto.isil">
		<logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
				<logic:present name="userinfo" property="konto.gbvbenutzername"><td <logic:equal name="orderform" property="submit" value="GBV">id="submenuactive"</logic:equal><logic:notEqual name="orderform" property="submit" value="GBV">id="submenu"</logic:notEqual>title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
		</logic:equal>
		</logic:present>
		</logic:present>
	</logic:equal>
			<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
				<td id="submenu" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
			</logic:notEqual>
	</tr>
</table>
<div class="content">

<br /><br />

<logic:equal name="orderform" property="submit" value="GBV">
<!-- GBV Bestellformular -->

<logic:present name="userinfo" property="konto">
	<h3><bean:message key="order.header_gbv" /></h3>
	
  <logic:present name="gbvmessage" property="message">
	  <p class=miss><bean:write name="gbvmessage" property="message" /></p>
  </logic:present>
   
  <table>
  <logic:notPresent name="message" property="error">
      <tr><td><br></td></tr>
  </logic:notPresent>

 <!--
 Hier folgt die Auswahl des Medientyps / Bestellart. Separates Action-Form.
 -->
 <html:form action="changeMediatypeGbv.do" method="post">
    <tr>
		<td><bean:message key="order.copy" /></td>
		<td><input type="radio" name="mediatype" value="Artikel" <logic:equal name="orderform" property="mediatype" value="Artikel">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Artikel">onClick="submit()"</logic:notEqual> /><bean:message key="save.artikel" /></input>
		<input type="radio" name="mediatype" value="Teilkopie Buch" <logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Teilkopie Buch">onClick="submit()"</logic:notEqual> /><bean:message key="save.bookpart" /></input>
	</tr>
    <tr>   
      <td><bean:message key="order.loan" /></td><td>
      <input type="radio" name="mediatype" value="Buch" <logic:equal name="orderform" property="mediatype" value="Buch">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Buch">onClick="submit()"</logic:notEqual> /><bean:message key="order.book" /></input></td>
    </tr>
<input name="method" type="hidden" value="changeMediatypeGbv" />
</html:form>

<!--
 eigentliches Bestellform
 -->
<html:form action="orderGbv.do" method="post" focus="foruser">
<input name="mediatype" type="hidden" value="<bean:write name="orderform" property="mediatype" />" /> 
    <tr>   
      <td><bean:message key="bestellform.bestellart" /></td><td>
      <!-- GBV akzeptiert für den Moment nur Bestellungen per Post, obwohl technische möglich
      <logic:present name="userinfo" property="konto.faxno"><input type="radio" name="deloptions" value="post" <logic:equal name="orderform" property="deloptions" value="post"> checked="checked"</logic:equal>>Post&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</input><logic:notEqual name="orderform" property="mediatype" value="Buch"><input type="radio" name="deloptions" value="fax to pdf" <logic:notEqual name="orderform" property="deloptions" value="post"> checked="checked"</logic:notEqual>>Fax to Email [als PDF | Faxnummer: <bean:write name="userinfo" property="konto.faxno" /> ]</input></logic:notEqual></logic:present>
      <logic:notPresent name="userinfo" property="konto.faxno"><input type="radio" name="deloptions" value="post" checked="checked">Post&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</input><logic:notEqual name="orderform" property="mediatype" value="Buch"><logic:present name="userinfo" property="konto.fax_extern"><input type="radio" name="deloptions" value="fax">Fax &nbsp;[ <bean:write name="userinfo" property="konto.fax_extern" /> ]</input></logic:present><logic:notPresent name="userinfo" property="konto.fax_extern"><input type="radio" name="deloptions" disabled="true" value="fax">Fax</input> <font color="white"><i>(Hinterlegen Sie unter 'Konto' ihre lokale Faxnummer!) </i></font></logic:notPresent></logic:notEqual></logic:notPresent>
      -->
      <input type="radio" name="deloptions" value="post" checked="checked"><bean:message key="save.post" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</input>
      </td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.prio" /></td>
      <td><select name="prio">
    			<option value="normal" <logic:notEqual name="orderform" property="prio" value="urgent">selected</logic:notEqual>><bean:message key="bestellform.normal" /></option>
    			<option value="urgent" <logic:equal name="orderform" property="prio" value="urgent">selected</logic:equal>><bean:message key="bestellform.urgent" /></option>
  			</select>
  		</td>
    </tr>    
    <tr>
      <td><b><bean:message key="save.foruser" />*&nbsp;</b></td><td><select name="foruser">
      									<option value="0" selected><bean:message key="select.kunde" /></option>
 										<logic:iterate id="u" name="orderform" property="kontouser">
 										<bean:define id="tmp"><bean:write name="orderform" property="foruser"/></bean:define>
     										<option value="<bean:write name="u" property="id" />"<logic:equal name="u" property="id" value="<%=tmp%>"> selected</logic:equal>><bean:write name="u" property="name" />, <bean:write name="u" property="vorname" /> - [<bean:write name="u" property="email" />]</option>
   										</logic:iterate>
									</select> <logic:present name="userinfo" property="benutzer"><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><a href="addkontousersfrombestellung.do?method=keepOrderDetails&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&submit=GBV&artikeltitel=<bean:write name="orderform" property="artikeltitel" />&zeitschriftentitel=<logic:present name="orderform" property="zeitschriftentitel"><bean:write name="orderform" property="zeitschriftentitel"/></logic:present>&author=<bean:write name="orderform" property="author" />"><img border="0" width="30px" src="img/newuser.png" alt="<bean:message key="showkontousers.info_new" />" title="<bean:message key="showkontousers.info_new" />"/><bean:message key="order.new" /></a></logic:notEqual></logic:present></td>
    </tr>

		<tr>
			<td><bean:message key="bestellform.author" /></td>
			<td><input name="author"
				value="<bean:write name="orderform" property="author" />"
				type="text" size="60" maxlength="100" /></td>
		</tr>

		<logic:equal name="orderform" property="mediatype" value="Artikel">
			<tr>
				<td><bean:message key="bestellform.artikeltitel" /></td>
				<td><input name="artikeltitel"
					value="<bean:write name="orderform" property="artikeltitel" />"
					type="text" size="98" maxlength="200" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="bestellform.zeitschrift" /></b> <font color="white"><i><bean:message key="order.and_or" />&nbsp;</i></font></td>
				<td><input name="zeitschriftentitel"
					value="<logic:present name="orderform" property="zeitschriftentitel"><bean:write name="orderform" property="zeitschriftentitel"/></logic:present>"
					type="text" size="98" maxlength="200" /></td>
			</tr>
			<tr title="<bean:message key="info.issn" />">
				<td title="<bean:message key="info.issn" />"><b><bean:message key="bestellform.issn" /></b> <img border="0" src="img/info.png" alt="<bean:message key="info.issn" />" /></td>
				<td><input name="issn"
					value="<logic:present name="orderform" property="issn"><bean:write name="orderform" property="issn" /></logic:present>"
					type="text" size="9" maxlength="9"></input>
				</td>
			</tr>
		</logic:equal>

		<logic:equal name="orderform" property="mediatype"
			value="Teilkopie Buch">
			<tr>
				<td><bean:message key="bestellform.kapitel" /></td>
				<td><input name="kapitel"
					value="<bean:write name="orderform" property="kapitel" />"
					type="text" size="98" maxlength="200" /></td>
			</tr>
			<tr>
				<td><b><bean:message key="bestellform.buchtitel" /></b> <font color="white"><i><bean:message key="order.and_or" />&nbsp;</i></font></td>
				<td><input name="buchtitel"
					value="<logic:present name="orderform" property="buchtitel"><bean:write name="orderform" property="buchtitel"/></logic:present>"
					type="text" size="98" maxlength="200" /></td>
			</tr>
			<tr title="<bean:message key="info.isbn" />">
				<td><b><bean:message key="bestellform.isbn" /></b> <img border="0" src="img/info.png" alt="<bean:message key="info.isbn" />" /></td>
				<td><input name="isbn" type="text"
					value="<bean:write name="orderform" property="isbn" />" size="17"
					maxlength="23" /></td>
			</tr>
			<tr>
				<td><bean:message key="bestellform.verlag" /></td>
				<td><input name="verlag"
					value="<bean:write name="orderform" property="verlag"/>"
					type="text" size="60" maxlength="100" /></td>
			</tr>
			<tr>
				<td><bean:message key="bestellform.jahr" /></td>
				<td><input name="jahr" type="text" value="<bean:write name="orderform" property="jahr" />" size="4" maxlength="4" /><logic:equal name="orderform" property="mediatype" value="Artikel">  (<bean:message key="bestellform.jahrbsp" />)</logic:equal></td>
			</tr>
		</logic:equal>

		<logic:equal name="orderform" property="mediatype" value="Buch">
			<tr>
				<td><b><bean:message key="bestellform.buchtitel" /></b> <font color="white"><i><bean:message key="order.and_or" />&nbsp;</i></font></td>
				<td><input name="buchtitel"
					value="<logic:present name="orderform" property="buchtitel"><bean:write name="orderform" property="buchtitel"/></logic:present>"
					type="text" size="98" maxlength="200" /></td>
			</tr>
			<tr title="<bean:message key="info.isbn" />">
				<td><b><bean:message key="bestellform.isbn" /></b> <img border="0" src="img/info.png" alt="<bean:message key="info.isbn" />" /></td>
				<td><input name="isbn" type="text"
					value="<bean:write name="orderform" property="isbn" />" size="17"
					maxlength="23" /></td>
			</tr>
			<tr>
				<td><bean:message key="bestellform.verlag" /></td>
				<td><input name="verlag"
					value="<bean:write name="orderform" property="verlag"/>"
					type="text" size="60" maxlength="100" /></td>
			</tr>
			<tr>
				<td><bean:message key="bestellform.jahr" /></td>
				<td><input name="jahr" type="text" value="<bean:write name="orderform" property="jahr" />" size="4" maxlength="4" /><logic:equal name="orderform" property="mediatype" value="Artikel">  (<bean:message key="bestellform.jahrbsp" />)</logic:equal></td>
			</tr>
		</logic:equal>

		<logic:equal name="orderform" property="mediatype" value="Artikel">
		<tr>
			<td><b><bean:message key="bestellform.jahr" />*</b></td>
			<td><input name="jahr" type="text"
				value="<bean:write name="orderform" property="jahr" />" size="4"
				maxlength="4" /><logic:equal name="orderform"
				property="mediatype" value="Artikel">  (<bean:message key="bestellform.jahrbsp" />)</logic:equal></td>
		</tr>
			<tr>
				<td><b><bean:message key="bestellform.jahrgang" /></b> <font color="white"><i><bean:message key="order.and_or" />&nbsp;</i></font></td>
				<td><input name="jahrgang" type="text"
					value="<bean:write name="orderform" property="jahrgang" />"
					size="4" maxlength="20" /> (<bean:message key="bestellform.jahrgangbsp" />)</td>
			</tr>
			<tr>
				<td><b><bean:message key="bestellform.heft" /></b></td>
				<td><input name="heft" type="text"
					value="<bean:write name="orderform" property="heft" />" size="4"
					maxlength="20" /> (<bean:message key="bestellform.heftbsp" />)</td>
			</tr>
		</logic:equal>
		<logic:notEqual name="orderform" property="mediatype" value="Buch">
			<tr>
				<td><b><bean:message key="bestellform.seiten" />*</b></td>
				<td><input name="seiten" type="text"
					value="<bean:write name="orderform" property="seiten" />" size="25"
					maxlength="30" /></td>
			</tr>
		</logic:notEqual>
		<tr>
			<td><bean:message key="order.mehrkosten" />&nbsp;</td><td><input name="maximum_cost" type="text"
					value="<bean:write name="orderform" property="maximum_cost" />" size="2"
					maxlength="2" />&nbsp;<bean:message key="order.eur" /> <logic:notEqual name="orderform" property="mediatype" value="Buch">(<bean:message key="order.copy_plus" />)</logic:notEqual><td></td>
		</tr>
	<tr title="<bean:message key="info.internenr" />">
  		<td><bean:message key="save.internenr" /><img src="img/info.png" alt="<bean:message key="info.internenr" />" />&nbsp;</td>
  		<td><input name="interne_bestellnr" value="<bean:write name="orderform" property="interne_bestellnr" />" type="text" size="62" maxlength="100" /> (<bean:message key="order.intern" />)</td>
  	</tr>

    <tr title="<bean:message key="order.bemerkungen_explain" />">
      <td><bean:message key="bestellform.bemerkungen" /> <img src="img/info.png" alt="<bean:message key="order.bemerkungen_explain" />" /></td><td><input name="anmerkungen" type="text" value="<bean:write name="orderform" property="anmerkungen" />" size="62" maxlength="100"> (<bean:message key="order.send" />)</td>
    </tr>
    <tr title="<bean:message arg0="<%=appName%>" key="save.interne_notizen_comment" />">
      <td><bean:message key="bestellform.interne_notizen" /> <img src="img/info.png" alt="<bean:message arg0="<%=appName%>" key="save.interne_notizen_comment" />" />&nbsp;</td><td><input name="notizen" type="text" value="<bean:write name="orderform" property="notizen" />" size="62" maxlength="500"> (<bean:message key="order.intern" />)</td>
    </tr>

     <tr><td><br></td></tr>


      <tr>
      <td><bean:message key="order.method" />&nbsp;</td><td>
      <input type="radio" name="manuell" <logic:notPresent name="userinfo" property="konto.gbvrequesterid">disabled="true" </logic:notPresent><logic:present name="userinfo" property="konto.gbvrequesterid"><logic:notPresent name="userinfo" property="konto.isil">disabled="true" </logic:notPresent></logic:present>value="false" <logic:present name="orderform" property="manuell"><logic:equal name="orderform" property="manuell" value="false">checked="checked"</logic:equal></logic:present><logic:notPresent name="orderform" property="manuell">checked="checked"</logic:notPresent>><bean:message key="order.gbv_autom" /> [<b>* <bean:message key="bestellform.required" /></b>] <font color="white"><i><logic:notPresent name="userinfo" property="konto.gbvrequesterid"> <bean:message key="error.requesterid" /></logic:notPresent><logic:present name="userinfo" property="konto.gbvrequesterid"><logic:notPresent name="userinfo" property="konto.isil"> <bean:message key="error.isil" /></logic:notPresent></logic:present></i></font>
      <logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><br>
      <input type="radio" name="manuell" value="true" <logic:present name="orderform" property="manuell"><logic:equal name="orderform" property="manuell" value="true">checked="checked"</logic:equal></logic:present>><bean:message key="order.gbv_manuell" />
      </logic:notEqual>
      </td>
    </tr>
      
 
      <tr><td><br></td></tr>

    <logic:notEqual name="userinfo" property="konto.bibliotheksname" value="Demo-Bibliothek">
	
	<tr>
	<td colspan="2">
	<table>
<!--
	<tr>
	<td><input type="submit" name="submit" value="GBV-Suche"></input></td>
	</tr>
-->
	<tr>
	<td><input type="submit" name="submit" value="<bean:message key="order.order" />"></input></td>
	<td><br />&nbsp;&nbsp;</td>
	<td><div id="italic"><bean:message key="order.doppelklicks" /></td>
	</tr>
	</table>
	</td>
	</tr>
	
	</logic:notEqual>

	</table>
		<p></p>
		
  <logic:present name="orderform" property="bid">
 	<input name="bid" type="hidden" value="<bean:write name="orderform" property="bid" />" />
  </logic:present>
  <input name="pmid" type="hidden" value="<bean:write name="orderform" property="pmid" />" />
  <input name="doi" type="hidden" value="<bean:write name="orderform" property="doi" />" />
  <input name="sici" type="hidden" value="<bean:write name="orderform" property="sici" />" />
  <input name="method" type="hidden" value="validate" />
  </html:form>
  
</logic:present>

</logic:equal>



<logic:notPresent name="userinfo" property="konto">
	<p><bean:message key="error.timeout" /></p>
	<p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>


</div>


 </body>
</html>
