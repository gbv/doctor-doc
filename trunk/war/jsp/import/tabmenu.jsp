<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<bean:define id="allowRegisterLibraryAccount" type="java.lang.String"><bean:message bundle="systemConfig" key="allow.registerLibraryAccounts"/></bean:define>
<bean:define id="activateGTC" type="java.lang.String"><bean:message bundle="systemConfig" key="activate.gtc"/></bean:define>
<bean:define id="wdth" type="java.lang.String">14.28%</bean:define>



<table width="100%">
	<tr>
		<td align="left">

<div id="menu">
<!--*** User not logged in - Menu outside ***-->
<logic:notPresent name="userinfo" property="benutzer">
<table border="0" style="width:980px;">
	<tr>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="login">id="menuActive"</logic:equal>>
			<logic:present name="orderform" property="resolver"><logic:equal name="orderform" property="resolver" value="true"><a href="pl.do?<logic:present name="orderform">mediatype=<bean:write name="orderform" property="mediatype" />&issn=<bean:write name="orderform" property="issn" />&jahr=<bean:write name="orderform" property="jahr" />&jahrgang=<bean:write name="orderform" property="jahrgang" />&heft=<bean:write name="orderform" property="heft" />&seiten=<bean:write name="orderform" property="seiten" />&isbn=<bean:write name="orderform" property="isbn" />&artikeltitel=<bean:write name="orderform" property="artikeltitel_encoded" />&zeitschriftentitel=<bean:write name="orderform" property="zeitschriftentitel_encoded" />&author=<bean:write name="orderform" property="author_encoded" />&kapitel=<bean:write name="orderform" property="kapitel_encoded" />&buchtitel=<bean:write name="orderform" property="buchtitel_encoded" />&verlag=<bean:write name="orderform" property="verlag_encoded" />&rfr_id=<bean:write name="orderform" property="rfr_id" />&genre=<bean:write name="orderform" property="genre" />&pmid=<bean:write name="orderform" property="pmid" />&doi=<bean:write name="orderform" property="doi" />&sici=<bean:write name="orderform" property="sici" />&zdbid=<bean:write name="orderform" property="zdbid" />&lccn=<bean:write name="orderform" property="lccn" />&artikeltitel_encoded=<bean:write name="orderform" property="artikeltitel_encoded" />&author_encoded=<bean:write name="orderform" property="author_encoded" /></logic:present>"><bean:message key="tabmenu.slide.login" /></a></logic:equal><logic:equal name="orderform" property="resolver" value="false"><a href="login.do?activemenu=login"><bean:message key="tabmenu.slide.login" /></a></logic:equal></logic:present><logic:notPresent name="orderform" property="resolver"><a href="login.do?activemenu=login"><bean:message key="tabmenu.slide.login" /></logic:notPresent></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="bestellform">id="menuActive"</logic:equal>>
			<a href="bestellform.do?method=validate&activemenu=bestellform<logic:present name="orderform" property="kkid">&kkid=<bean:write name="orderform" property="kkid" /></logic:present><logic:present name="orderform" property="bkid">&bkid=<bean:write name="orderform" property="bkid" /></logic:present>"><bean:message key="tabmenu.slide.order" /></a>
		</td>
		<logic:notEqual name="ActiveMenus" property="activemenu" value="bestellform">
		<logic:notEqual name="ActiveMenus" property="activemenu" value="suchenbestellen">
		<logic:equal name="allowRegisterLibraryAccount" value="true">
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="newkonto">id="menuActive"</logic:equal>>
			<nobr><a href="anmeldungkont.do?method=prepareNewKonto"><bean:message key="tabmenu.slide.register" /></a></nobr>
		</td>
		</logic:equal>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="kontotyp">id="menuActive"</logic:equal>>
			<a href="anmeldungtyp.do?activemenu=kontotyp"><bean:message key="tabmenu.slide.services" /></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="impressum">id="menuActive"</logic:equal>>
			<a href="impressum.do?activemenu=impressum"><bean:message key="tabmenu.slide.about" /></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="howto">id="menuActive"</logic:equal>>
			<a href="howto.do?activemenu=howto&activesubmenu=OpenURL"><bean:message key="tabmenu.slide.howto" /></a>
		</td>
		</logic:notEqual>
		</logic:notEqual>
		<td>
			&nbsp;
		</td>
	</tr>
</logic:notPresent>

<!--*** User logged in - Menu inside ***-->
<logic:present name="userinfo" property="benutzer">
<logic:notEqual name="userinfo" property="benutzer.rechte" value="3">
	<table border="0" style="width:980px;">
</logic:notEqual>
<logic:equal name="userinfo" property="benutzer.rechte" value="3">
	<table style="width:1260px;">
	<bean:define id="wdth" type="java.lang.String">11.11%</bean:define>
</logic:equal>

	<tr>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="suchenbestellen">id="menuActive"</logic:equal>>
			<nobr><a href="searchfree.do?activemenu=suchenbestellen" alt="<bean:message key="tabmenu.search" />" title="<bean:message key="tabmenu.search" />"><bean:message key="tabmenu.slide.search" /></a></nobr>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="uebersicht">id="menuActive"</logic:equal>>
			<a href="listkontobestellungen.do?method=overview&filter=offen&sort=statedate&sortorder=desc" alt="<bean:message key="tabmenu.tracking" />" title="<bean:message key="tabmenu.tracking" />"><bean:message key="tabmenu.slide.track" /></a>
		</td>
		<!-- Section visible only for librarian and admin  -->
		<logic:notEqual name="userinfo" property="benutzer.rechte" value="1">
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="bibliokunden">id="menuActive"</logic:equal>>			
			<a href="listkontousers.do?method=showkontousers&activemenu=bibliokunden" alt="<bean:message key="tabmenu.user" />" title="<bean:message key="tabmenu.user" />"><bean:message key="tabmenu.slide.patrons" /></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="stats">id="menuActive"</logic:equal>>
			<a href="statistics.do?method=kontoOrders&activemenu=stats" alt="<bean:message key="tabmenu.stats" />" title="<bean:message key="tabmenu.stats" />"><bean:message key="tabmenu.slide.statistics" /></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="konto">id="menuActive"</logic:equal>>
			<a href="modifykont.do?method=prepareModifyKonto&activemenu=konto" alt="<bean:message key="tabmenu.konto" />" title="<bean:message key="tabmenu.konto" />"><bean:message key="tabmenu.slide.account" /></a>
		</td>
<!--*** Section visible only for admin  ***-->
		<logic:equal name="userinfo" property="benutzer.rechte" value="3">
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="stock">id="menuActive"</logic:equal>>
			<a href="allstock.do?method=listBestand&activemenu=stock"><bean:message key="tabmenu.slide.holdings" /></a>
		</td>
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="admin">id="menuActive"</logic:equal>>
			<a href="kontoadmin.do?method=listKontos&activemenu=admin"><bean:message key="tabmenu.slide.admin" /></a>
		</td>
		</logic:equal>
		</logic:notEqual>
<!--*** Section visible to all users  ***-->
		<td width="<%=wdth%>"  <logic:equal name="ActiveMenus" property="activemenu" value="howto">id="menuActive"</logic:equal>>
			<a href="howto.do?activemenu=howto&activesubmenu=OpenURL"><bean:message key="tabmenu.slide.howto" /></a>
		</td>
		<logic:equal name="activateGTC" value="true">
		<td width="<%=wdth%>" <logic:equal name="ActiveMenus" property="activemenu" value="agb">id="menuActive"</logic:equal>>
			<a href="gtc.do?activemenu=agb" alt="<bean:message key="tabmenu.agb" />" title="<bean:message key="tabmenu.agb" />"><bean:message key="tabmenu.slide.gtc" /></a>
		</td>
		</logic:equal>
		<td>
			&nbsp;
		</td>
	</tr>
</logic:present>
</table>

</td>

</div>

<logic:notPresent name="orderform" property="resolver">
<!-- To minimize the risk of loosing parameters of an order during the change of locales -->
	<td align="right">
		<table border="0" align="right">
			<td align="right">
  	   			<a href="language.do?request_locale=fr"><img src="img/fr.png" class="whiteborder">
			</td>
			<td>&nbsp;</td>
			<td align="right">
				<a href="language.do?request_locale=en"><img src="img/en.png" class="whiteborder">
			</td>
			<td>&nbsp;</td>
			<td align="right">
  	   			<a href="language.do?request_locale=de"><img src="img/de.png" class="whiteborder">
			</td>
		</table>
	</td>
</logic:notPresent>
<logic:present name="orderform" property="resolver">
<logic:equal name="orderform" property="resolver" value="false">
<!-- To minimize the risk of loosing parameters of an order during the change of locales -->
	<td align="right">
		<table border="0" align="right">
			<td align="right">
  	   			<a href="language.do?request_locale=fr"><img src="img/fr.png" class="whiteborder">
			</td>
			<td align="right">
				<a href="language.do?request_locale=en"><img src="img/en.png" class="whiteborder">
			</td>
			<td>&nbsp;</td>
			<td align="right">
  	   			<a href="language.do?request_locale=de"><img src="img/de.png" class="whiteborder">
			</td>
		</table>
	</td>
</logic:equal>
</logic:present>

	</tr>
</table>


