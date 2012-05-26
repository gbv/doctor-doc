<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="save.titel" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  
<script type="text/javascript">
function vorkomma(a)
{
        <logic:present name="orderform" property="defaultpreise">
       <logic:iterate id="d" name="orderform" property="defaultpreise">
           s = "<bean:write name="d" property="lid" />" ;
             if (s == a) { x = "<bean:write name="d" property="vorkomma" />"; return(x); } else { x= ""; }
         </logic:iterate>
       </logic:present>
 return(x);
}
function nachkomma(a)
{
        <logic:present name="orderform" property="defaultpreise">
       <logic:iterate id="d" name="orderform" property="defaultpreise">
           s = "<bean:write name="d" property="lid" />" ;
             if (s == a) { x = "<bean:write name="d" property="nachkomma" />"; return(x); } else { x= ""; }
         </logic:iterate>
       </logic:present>
 return(x);
}
function wahrung(a)
{
        <logic:present name="orderform" property="defaultpreise">
       <logic:iterate id="w" name="orderform" property="defaultpreise">
           s = "<bean:write name="w" property="lid" />" ;
             if (s == a) { x = "<bean:write name="w" property="waehrung" />"; return(x); } else { x= ""; }
         </logic:iterate>
       </logic:present>
 return(x);
}
</script>
  
 </head>
 <body>
 
 <bean:define id="appName" type="java.lang.String"><bean:message bundle="systemConfig" key="application.name"/></bean:define>

<table id="header_table">
  <tr style="background-image:url(img/verlauf.png); background-repeat:repeat-x; ">
    <td class="logo"><a href="<bean:message bundle="systemConfig" key="server.welcomepage"/>"><img class="logo" src='img/sp.gif' alt='<bean:message bundle="systemConfig" key="application.name"/>'  /></a></td>
    <td><h1><bean:message bundle="systemConfig" key="application.name"/></h1><p></p>
    </td>
    <td class="kontoinfos"><tiles:insert page="import/kontoinfo.jsp" flush="true" /></td>
      <td width="20%" style="text-align:right;">
      <logic:present name="userinfo" property="benutzer">
      <a href="logout.do?activemenu=suchenbestellen"><font color="white"> [Log out]</font></a>
        </logic:present>
    </td>
  </tr>
  <tr>
    <td></td>
    <td colspan="3">
      <tiles:insert page="import/tabmenu.jsp" flush="true" />
    </td>
  </tr>
</table>

<table style="position:absolute; text-align:left; left:111px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.search_explain" />"><a href="searchfree.do?activemenu=suchenbestellen"><bean:message key="menu.search" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.issn_explain" />"><a href="issnsearch_.do?method=prepareIssnSearch"><bean:message key="menu.issn" /></a></td>
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">  
    <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
  </logic:notEqual>
  <logic:equal name="userinfo" property="benutzer.rechte" value="1">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.user_order_explain" />"><a href="journalorder.do?method=prepare&submit=bestellform"><bean:message key="menu.user_order" /></a></td>
    <logic:equal name="userinfo" property="benutzer.userbestellung" value="true">
        <td id="submenu" nowrap="nowrap" title="<bean:message key="menu.subito_explain" />"><a href="journalorder.do?method=prepare"><bean:message key="menu.subito" /></a></td>
    </logic:equal>
    <logic:present name="userinfo" property="konto.gbvrequesterid">
    <logic:present name="userinfo" property="konto.isil">
    <logic:equal name="userinfo" property="benutzer.gbvbestellung" value="true">
        <logic:present name="userinfo" property="konto.gbvbenutzername"><td id="submenu" nowrap="nowrap" title="<bean:message arg0="<%=appName%>" key="menu.gbv_explain" />"><a href="journalorder.do?method=prepare&submit=GBV"><bean:message key="menu.gbv" /></a></td></logic:present>
    </logic:equal>
    </logic:present>
    </logic:present>
  </logic:equal>
        <td id="submenuactive" nowrap="nowrap" title="<bean:message key="menu.save_explain" />"><a href="prepareJournalSave.do?method=prepareJournalSave"><bean:message key="menu.save" /></a></td>
  </tr>
</table>

<div class="content">

<br><br>

<logic:present name="userinfo" property="konto">

<logic:notPresent name="orderform" property="bid">
  <h3><bean:message key="save.headersave" /></h3>
</logic:notPresent>

<logic:present name="orderform" property="bid">
  <h3><bean:message key="save.headerchange" /></h3>
</logic:present>


<a href="listsuppliers.do"><bean:message key="suppliers.admin" /></a>
  

  <table>
      <tr><td><br></td></tr>

 <!--
 Hier folgt die Auswahl des Medientyps. Separates Action-Form. Eingegebene Daten werden nicht Übernommen.
 Wird nur angezeigt, wenn keine BID vorhanden, d.h. beim Ändern einer bereits erfolgten Bestellung, kann der Medientyp nicht mehr geändert werden.
 -->      
<logic:notPresent name="orderform" property="bid">
 <html:form action="changeMediatype.do" method="post">
   <tr>
    <td><bean:message key="bestellform.typ" /></td>
    <td><input type="radio" name="mediatype" value="Artikel" <logic:equal name="orderform" property="mediatype" value="Artikel">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Artikel">onclick="submit()"</logic:notEqual> /><bean:message key="save.artikel" />
    <input type="radio" name="mediatype" value="Teilkopie Buch" <logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Teilkopie Buch">onclick="submit()"</logic:notEqual> /><bean:message key="save.bookpart" />
    <input type="radio" name="mediatype" value="Buch" <logic:equal name="orderform" property="mediatype" value="Buch">checked="checked"</logic:equal><logic:notEqual name="orderform" property="mediatype" value="Buch">onclick="submit()"</logic:notEqual> /><bean:message key="save.book" /></td>
  </tr>

<input name="method" type="hidden" value="prepareJournalSave" />
</html:form>
</logic:notPresent>
  
 <!--
 Hier folgt das Form eine Bestellung zu speichern.
 -->  
  <html:form action="saveOrder.do" method="post" focus="foruser">
  <input name="mediatype" type="hidden" value="<bean:write name="orderform" property="mediatype" />" /> 
  
   <logic:present name="orderform" property="bid">
     <input name="bid" type="hidden" value="<bean:write name="orderform" property="bid" />" />
      <tr>
        <td><bean:message key="bestellform.typ" /></td>
        <td><bean:write name="orderform" property="mediatype" /></td>
      </tr>
  </logic:present>
    
    <tr>
    	<td><bean:message key="bestellform.bestellart" /></td>
    	<td>
    		<input type="radio" name="deloptions" value="online"<logic:equal name="orderform" property="deloptions" value="online"> checked="checked"</logic:equal> /><bean:message key="save.online" />
    		<input type="radio" name="deloptions" value="email"<logic:equal name="orderform" property="deloptions" value="email"> checked="checked"</logic:equal> /><bean:message key="save.email" />
    		<input type="radio" name="deloptions" value="post"<logic:equal name="orderform" property="deloptions" value="post"> checked="checked"</logic:equal> /><bean:message key="save.post" />
    		<input type="radio" name="deloptions" value="fax"<logic:equal name="orderform" property="deloptions" value="fax"> checked="checked"</logic:equal> /><bean:message key="save.fax" />
    		<input type="radio" name="deloptions" value="fax to pdf"<logic:equal name="orderform" property="deloptions" value="fax to pdf"> checked="checked"</logic:equal> /><bean:message key="save.faxtopdf" />
    		<!-- custom deloptions -->
    		<logic:iterate id="del" name="delopts">
    			<bean:define id="val"><bean:write name="del" /></bean:define>
    				<input type="radio" name="deloptions" value="<bean:write name="del" />"<logic:equal name="orderform" property="deloptions" value="<%=val%>"> checked="checked"</logic:equal> /><bean:write name="del" />
    		</logic:iterate>
    	</td>
    </tr>
    
   <tr><td><br></td></tr>
    <tr title="<bean:message key="save.subitonr_info" />">
      <td><bean:message key="save.subitonr" /><img border="0" src="img/info.png" alt="<bean:message key="save.subitonr_info" />" /></td><td><input name="subitonr" value="<bean:write name="orderform" property="subitonr" />" type="text" size="25" maxlength="25" /></td></tr>
    <logic:present name="userinfo" property="konto.gbvbenutzername">
    <tr title="<bean:message key="save.gbvnr_info" />">
      <td><bean:message key="save.gbvnr" /><img border="0" src="img/info.png" alt="<bean:message key="save.gbvnr_info" />" /></td><td><input name="gbvnr" value="<bean:write name="orderform" property="gbvnr" />" type="text" size="25" maxlength="25" /></td></tr>
    </logic:present>
  <tr><td><br></td></tr>
 
   <!--
Hier folgt die Auswahl der quelle mit JavaScript Preisselect

 --> 
 
 
     <tr><td><bean:message key="save.lieferquelle" /></td>
          <td>
              <select name="lid" onchange="preisvorkomma.value=(vorkomma(this.value));preisnachkomma.value=(nachkomma(this.value));waehrung.value=(wahrung(this.value))">
              <option value="0" selected="selected"><bean:message key="select.library" /></option>
           <logic:iterate id="l" name="orderform" property="quellen">
           <bean:define id="tmp"><bean:write name="orderform" property="lieferant.lid"/></bean:define>
             <option value="<bean:write name="l" property="lid" />"<logic:equal name="l" property="lid" value="<%=tmp%>"> selected</logic:equal>><bean:write name="l" property="sigel" /> <bean:write name="l" property="name" /></option>
             </logic:iterate>
        </select>
      </td>
      </tr>   
    <tr>
      <td><bean:message key="save.status" /></td><td>
                <select name="status">
                  <option value="0" selected="selected"><bean:message key="select.status" /></option>
               <logic:iterate id="s" name="orderform" property="statitexts">
               <bean:define id="tmp" name="s" property="inhalt" type="java.lang.String"/>
                   <option value="<bean:write name="s" property="inhalt" />"<logic:equal name="orderform" property="status" value="<%=tmp%>"> selected</logic:equal>>
                     <logic:equal name="s" property="inhalt" value="erledigt"><bean:message key="menu.closed" /></logic:equal>
                     <logic:equal name="s" property="inhalt" value="reklamiert"><bean:message key="menu.claimed" /></logic:equal>
                     <logic:equal name="s" property="inhalt" value="bestellt"><bean:message key="menu.ordered" /></logic:equal>
                     <logic:equal name="s" property="inhalt" value="zu bestellen"><bean:message key="menu.toOrder" /></logic:equal>
                     <logic:equal name="s" property="inhalt" value="geliefert"><bean:message key="menu.shipped" /></logic:equal>
                     <logic:equal name="s" property="inhalt" value="nicht lieferbar"><bean:message key="menu.unfilled" /></logic:equal>
                   </option>
                 </logic:iterate>
            </select>
            </td>
    </tr>
    
        <tr>
      <td><bean:message key="bestellform.prio" /></td>
      <td>
          <select name="prio" size="1">
          <option value="normal" <logic:equal name="orderform" property="prio" value="normal">selected</logic:equal>><bean:message key="bestellform.normal" /></option>
          <option value="urgent"<logic:equal name="orderform" property="prio" value="urgent">selected</logic:equal>><bean:message key="bestellform.urgent" /></option>
        </select>
      </td>
    </tr>
    <tr>
      <td><bean:message key="save.format" /></td>
      <td><select name="fileformat" size="1">
          <option value="PDF"<logic:equal name="orderform" property="fileformat" value="PDF"> selected</logic:equal>><bean:message key="save.pdf" /></option>
          <option value="PDF mit DRM"<logic:equal name="orderform" property="fileformat" value="PDF mit DRM"> selected</logic:equal>><bean:message key="save.drm" /></option>
          <option value="Papierkopie"<logic:equal name="orderform" property="fileformat" value="Papierkopie"> selected</logic:equal>><bean:message key="save.papierkopie" /></option>
          <option value="HTML"<logic:equal name="orderform" property="fileformat" value="HTML"> selected</logic:equal>><bean:message key="save.html" /></option>
          <option value="DOC"<logic:equal name="orderform" property="fileformat" value="DOC"> selected</logic:equal>><bean:message key="save.doc" /></option>
          <option value="TIFF"<logic:equal name="orderform" property="fileformat" value="TIFF"> selected</logic:equal>><bean:message key="save.tiff" /></option>
          <option value="andere"<logic:equal name="orderform" property="fileformat" value="andere"> selected</logic:equal>><bean:message key="save.andere" /></option>
        </select>
      </td>
    </tr>
  <logic:notPresent name="orderform" property="bid">
    <tr>
      <td><bean:message key="save.foruser" />&nbsp;</td><td><select name="foruser">
                        <option value="0" selected="selected"><bean:message key="select.kunde" /></option>
                     <logic:iterate id="u" name="orderform" property="kontouser">
                     <bean:define id="tmp" name="orderform" property="foruser" type="java.lang.String"/>
                         <option value="<bean:write name="u" property="id" />" <logic:equal name="u" property="id" value="<%=tmp%>"> selected</logic:equal>><bean:write name="u" property="name" />, <bean:write name="u" property="vorname" /> - [<bean:write name="u" property="email" />]</option>
                       </logic:iterate>
                  </select> <logic:present name="userinfo" property="benutzer"><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><input type="submit" name="submit" value="<bean:message key="save.newuser" />" /></logic:notEqual></logic:present></td>
    </tr>
  </logic:notPresent>
  <logic:present name="orderform" property="bid">
    <tr>
      <td><bean:message key="save.foruser" />&nbsp;</td><td><select name="foruser">
                        <option value="0" selected="selected"><bean:message key="select.kunde" /></option>
                     <logic:iterate id="u" name="orderform" property="kontouser">
                     <bean:define id="tmp" name="orderform" property="uid" type="java.lang.String"/>
                         <option value="<bean:write name="u" property="id" />"<logic:equal name="u" property="id" value="<%=tmp%>"> selected</logic:equal>><bean:write name="u" property="name" />, <bean:write name="u" property="vorname" /> - [<bean:write name="u" property="email" />]</option>
                       </logic:iterate>
                  </select> <logic:present name="userinfo" property="benutzer"><logic:notEqual name="userinfo" property="benutzer.rechte" value="1"><input type="submit" name="submit" value="<bean:message key="save.newuser" />" /></logic:notEqual></logic:present></td>
    </tr>
  </logic:present>
 
<logic:present name="userinfo" property="benutzer">
  <logic:notEqual name="userinfo" property="benutzer.rechte" value="1">    
    <tr>
      <td><bean:message key="save.preis" /></td><td><input name="preisvorkomma" value="<bean:write name="orderform" property="preisvorkomma" />" type="number" style="text-align:right;" size="3" maxlength="3" /> , <input name="preisnachkomma" value="<bean:write name="orderform" property="preisnachkomma" />" type="number" style="text-align:right;" size="2" maxlength="2" /> <font color="white"><i><bean:message key="save.preis_comment" /></i></font> <bean:message key="save.waehrung" />: 
      <select name="waehrung">
      <logic:iterate id="w" name="orderform" property="waehrungen">
      <bean:define id="tmp" name="w" property="inhalt" type="java.lang.String"/>
      <option value="<bean:write name="w" property="inhalt" />"<logic:equal name="orderform" property="waehrung" value="<%=tmp%>"> selected</logic:equal>><bean:write name="w" property="inhalt" /></option>
      </logic:iterate>
      </select>
      <input type="checkbox" name="preisdefault" value="true" /><bean:message key="save.preis_default" /></td>
    </tr>
  </logic:notEqual>
</logic:present>
 
    
    <tr>
      <td><bean:message key="bestellform.author" /></td><td><input name="author" value="<bean:write name="orderform" property="author" />" type="text" size="62" maxlength="100" /></td>
    </tr>
<logic:equal name="orderform" property="mediatype" value="Artikel">
    <tr>
      <td><bean:message key="bestellform.artikeltitel" /></td><td><input name="artikeltitel" value="<bean:write name="orderform" property="artikeltitel" />" type="text" size="98" maxlength="100" /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.zeitschrift" /></td><td><input name="zeitschriftentitel" value="<logic:present name="orderform" property="zeitschriftentitel"><bean:write name="orderform" property="zeitschriftentitel"/></logic:present>" type="text" size="62" maxlength="100" /></td>
    </tr>
    <tr>
      <td><bean:message key="order.issn" /></td><td><input name="issn" type="text" value="<bean:write name="orderform" property="issn" />" size="9" maxlength="9" />
      <font color=red><html:errors property="issn" /></font></td>
    </tr>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Teilkopie Buch">
    <tr>
      <td><bean:message key="bestellform.kapitel" /></td><td><input name="kapitel" value="<bean:write name="orderform" property="kapitel" />" type="text" size="98" maxlength="100" /></td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.buchtitel" /></td><td><input name="buchtitel" value="<bean:write name="orderform" property="buchtitel" />" type="text" size="98" maxlength="100" /></td>
    </tr>
     <tr>
      <td><bean:message key="bestellform.verlag" /></td><td><input name="verlag" value="<bean:write name="orderform" property="verlag"/>" type="text" size="62" maxlength="100" /></td>
    </tr>
    <tr>    
       <td><bean:message key="bestellform.isbn" /></td><td><input name="isbn" type="text" value="<bean:write name="orderform" property="isbn" />" size="17" maxlength="23" /></td>
    </tr>
</logic:equal>

<logic:equal name="orderform" property="mediatype" value="Buch">
    <tr>
      <td><bean:message key="bestellform.buchtitel" /></td><td><input name="buchtitel" value="<bean:write name="orderform" property="buchtitel" />" type="text" size="98" maxlength="100" /></td>
    </tr>
     <tr>
      <td><bean:message key="bestellform.verlag" /></td><td><input name="verlag" value="<bean:write name="orderform" property="verlag"/>" type="text" size="62" maxlength="100" /></td>
    </tr>
    <tr>    
       <td><bean:message key="bestellform.isbn" /></td><td><input name="isbn" type="text" value="<bean:write name="orderform" property="isbn" />" size="17" maxlength="23" /></td>
    </tr>
</logic:equal>
     
    <tr>
      <td><bean:message key="bestellform.jahr" /></td><td><input name="jahr" type="text" value="<bean:write name="orderform" property="jahr" />" size="4" maxlength="4" /> (<bean:message key="bestellform.jahrbsp" />)</td>
    </tr>
    
<logic:equal name="orderform" property="mediatype" value="Artikel">
    <tr>
      <td><bean:message key="bestellform.jahrgang" /></td><td><input name="jahrgang" type="text" value="<bean:write name="orderform" property="jahrgang" />" size="4" maxlength="4" /> (<bean:message key="bestellform.jahrgangbsp" />)</td>
    </tr>
    <tr>
      <td><bean:message key="bestellform.heft" /></td><td><input name="heft" type="text" value="<bean:write name="orderform" property="heft" />" size="4" maxlength="4" /> (<bean:message key="bestellform.heftbsp" />)</td>
    </tr>
</logic:equal>
<logic:notEqual name="orderform" property="mediatype" value="Buch">
    <tr>
      <td><bean:message key="bestellform.seiten" /></td><td><input name="seiten" type="text" value="<bean:write name="orderform" property="seiten" />" size="15" maxlength="15" /></td>
    </tr>
</logic:notEqual>
<tr>
      <td><bean:message key="stockimport.sig" /></td>
      <td><input name="signatur" value="<bean:write name="orderform" property="signatur" />" type="text" size="62" maxlength="250" /></td>
  </tr>
<logic:equal name="orderform" property="mediatype" value="Artikel">
    <tr>    
       <td title="<bean:message key="info.pubmed" />">PMID<img border="0" src="img/info.png" alt="<bean:message key="info.pubmed" />" /></td><td><input name="pmid" type="text" value="<bean:write name="orderform" property="pmid" />" size="62" maxlength="100" /> </td>
    </tr>
    <tr>    
       <td title="<bean:message key="info.doi" />">DOI<img border="0" src="img/info.png" alt="<bean:message key="info.doi" />" /></td><td><input name="doi" type="text" value="<bean:write name="orderform" property="doi" />" size="62" maxlength="200" /> </td>
    </tr>
</logic:equal>
  
  <tr>
      <td title="<bean:message key="info.internenr" />"><bean:message key="save.internenr" /><img border="0" src="img/info.png" alt="<bean:message key="info.internenr" />" />&nbsp;</td>
      <td><input name="interne_bestellnr" value="<bean:write name="orderform" property="interne_bestellnr" />" type="text" size="62" maxlength="100" /></td>
    </tr>
    <tr>
      <td title="<bean:message key="save.bemerkungen_comment" />"><bean:message key="bestellform.bemerkungen" /><img border="0" src="img/info.png" alt="<bean:message key="save.bemerkungen_comment" />" />&nbsp;</td>
      <td><input name="anmerkungen" type="text" value="<bean:write name="orderform" property="anmerkungen" />" size="62" maxlength="100" /></td>
    </tr>
    <tr>
      <td title="<bean:message arg0="<%=appName%>" key="save.interne_notizen_comment" />"><bean:message key="bestellform.interne_notizen" /><img border="0" src="img/info.png" alt="<bean:message arg0="<%=appName%>" key="save.interne_notizen_comment" />" />&nbsp;</td>
      <td><input name="notizen" type="text" value="<bean:write name="orderform" property="notizen" />" size="62" maxlength="500" /></td>
    </tr>     

     <tr><td><br></td></tr>
     
     <tr>
  		<td><input type="submit" name="submit" value="<bean:message key="save.speichern" />"></td>
  	</tr>
  	
  	  <input name="method" type="hidden" value="saveOrder" />
  	</html:form>
      
     </table>
  
  <p></p>
  <p></p>
  
  <logic:present name="message" property="error">
    <p class=miss><bean:write name="message" property="error" /></p>
  </logic:present>
  
  
</logic:present>

<logic:notPresent name="userinfo" property="konto">
  <p><bean:message key="error.timeout" /></p>
  <p><a href="login.do"><bean:message key="error.back" /></a></p>
</logic:notPresent>

</div>


 </body>
</html>
