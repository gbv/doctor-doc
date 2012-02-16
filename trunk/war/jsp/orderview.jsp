<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="order.detailtitle" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" /> 
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

  <h3>
    <logic:equal name="orderform" property="delete" value="false">
      <bean:message key="orderview.header" />
    </logic:equal>
    <logic:equal name="orderform" property="delete" value="true">
      <bean:message key="orderview.header_delete" />
    </logic:equal>
  </h3>
  
  
  <table border="1">
  <tr>
    <logic:notEmpty name="orderform" property="bestellung.subitonr">
      <th align="left"><bean:message key="save.subitonr" /></th>
    </logic:notEmpty>
    <logic:present name="orderform" property="bestellung.gbvnr">
      <th align="left"><bean:message key="save.gbvnr" /></th>
    </logic:present>
    <logic:notEmpty name="orderform" property="bestellung.interne_bestellnr">
      <th align="left"><bean:message key="save.internenr" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.mediatype">
      <th align="left"><bean:message key="bestellform.typ" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.autor">
    <logic:notEqual name="orderform" property="bestellung.autor" value=" ">    
      <th align="left"><bean:message key="bestellform.author" /></th>
    </logic:notEqual>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.artikeltitel">
      <th align="left"><bean:message key="bestellform.artikeltitel" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.kapitel">
      <th align="left"><bean:message key="bestellform.kapitel" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.buchtitel">
      <th align="left"><bean:message key="bestellform.buchtitel" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.verlag">
      <th align="left"><bean:message key="bestellform.verlag" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.zeitschrift">
      <th align="left"><bean:message key="bestellform.zeitschrift" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.issn">
      <th align="left"><bean:message key="bestellform.issn" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.isbn">
      <th align="left"><bean:message key="bestellform.isbn" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.jahrgang">
      <th align="left"><bean:message key="bestellform.jahrgang" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.jahr">
      <th align="left"><bean:message key="bestellform.jahr" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.heft">
      <th align="left"><bean:message key="bestellform.heft" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.seiten">
      <th align="left"><bean:message key="bestellform.seiten" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.bibliothek">
      <th align="left"><bean:message key="orderview.lieferbibliothek" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.sigel">
      <th align="left"><bean:message key="orderview.sigel" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.fileformat">
      <th align="left"><bean:message key="save.format" /></th>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.priority">
      <th align="left"><bean:message key="bestellform.prio" /></th>
    </logic:notEmpty>
  </tr>
   <logic:present name="orderform" property="bestellung">
   
     <tr>     
    <logic:notEmpty name="orderform" property="bestellung.subitonr">
      <td><a href="http://www.subito-doc.de/index.php?mod=subo&task=trackingdetail&tgq=<bean:write name="orderform" property="bestellung.subitonr" />" target="_blank"><bean:write name="orderform" property="bestellung.subitonr" /></a>&nbsp;</td>
    </logic:notEmpty>
    <logic:present name="orderform" property="bestellung.gbvnr">
      <td><a href="https://www.gbv.de/cbs4/bestellverlauf.pl?BestellID=<bean:write name="orderform" property="bestellung.gbvnr" />" target="_blank"><bean:write name="orderform" property="bestellung.gbvnr" /></a>&nbsp;</td>
    </logic:present>
    <logic:notEmpty name="orderform" property="bestellung.interne_bestellnr">
      <td><bean:write name="orderform" property="bestellung.interne_bestellnr" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.mediatype">
      <td>
        <logic:equal name="orderform" property="bestellung.mediatype" value="Artikel">
          <bean:message key="save.artikel" />
        </logic:equal>
        <logic:equal name="orderform" property="bestellung.mediatype" value="Teilkopie Buch">
          <bean:message key="save.bookpart" />
        </logic:equal>
        <logic:equal name="orderform" property="bestellung.mediatype" value="Buch">
          <bean:message key="save.book" />
        </logic:equal>
        &nbsp;
      </td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.autor">
    <logic:notEqual name="orderform" property="bestellung.autor" value=" ">    
      <td><bean:write name="orderform" property="bestellung.autor" />&nbsp;</td>
    </logic:notEqual>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.artikeltitel">
      <td><bean:write name="orderform" property="bestellung.artikeltitel" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.kapitel">
      <td><bean:write name="orderform" property="bestellung.kapitel" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.buchtitel">
      <td><bean:write name="orderform" property="bestellung.buchtitel" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.verlag">
      <td><bean:write name="orderform" property="bestellung.verlag" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.zeitschrift">
           <td><bean:write name="orderform" property="bestellung.zeitschrift" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.issn">
      <td><bean:write name="orderform" property="bestellung.issn" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.isbn">
      <td><bean:write name="orderform" property="bestellung.isbn" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.jahrgang">
      <td><bean:write name="orderform" property="bestellung.jahrgang" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.jahr">
      <td><bean:write name="orderform" property="bestellung.jahr" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.heft">
      <td><bean:write name="orderform" property="bestellung.heft" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.seiten">
      <td><bean:write name="orderform" property="bestellung.seiten" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.bibliothek">
      <td><bean:write name="orderform" property="bestellung.bibliothek" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.sigel">
      <td><bean:write name="orderform" property="bestellung.sigel" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.fileformat">
      <td><bean:write name="orderform" property="bestellung.fileformat" />&nbsp;</td>
    </logic:notEmpty>
    <logic:notEmpty name="orderform" property="bestellung.priority">
      <td>
        <logic:equal name="orderform" property="bestellung.priority" value="normal">
          <bean:message key="bestellform.normal" />
        </logic:equal>
        <logic:equal name="orderform" property="bestellung.priority" value="urgent">
          <bean:message key="bestellform.urgent" />
        </logic:equal>
      &nbsp;
      </td>
    </logic:notEmpty>    
     </tr>
     </table>
     
     <table>
     <tr>
       <td>
     <logic:notEmpty name="orderform" property="bestellung.pmid">
       <table border="1">
         <tr>
        <th align="left">PMID</th>
      </tr>
      <tr><td><a href="http://www.ncbi.nlm.nih.gov/pubmed/<bean:write name="orderform" property="bestellung.pmid" />" target="_blank"><bean:write name="orderform" property="bestellung.pmid" /></a>&nbsp;</td></tr>
    </table>
  </logic:notEmpty>
  </td>
  <td>
  <logic:notEmpty name="orderform" property="bestellung.doi">
       <table border="1">
         <tr>
        <th align="left">DOI</th>
      </tr>
      <tr><td><a href="http://dx.doi.org/<bean:write name="orderform" property="bestellung.doi" />" target="_blank"><bean:write name="orderform" property="bestellung.doi" /></a>&nbsp;</td></tr>
    </table>
  </logic:notEmpty>
  </td>
  </tr>
  </table>
  
   
  <logic:notEmpty name="orderform" property="bestellung.systembemerkung">   
     <p></p>
     <table border="1">
       <tr>
        <th align="left"><bean:message key="bestellform.bemerkungen" /></th>
      </tr>
      <tr>
        <td>
          <bean:write name="orderform" property="bestellung.systembemerkung" />&nbsp;
        </td>
      </tr>
    </table>
  </logic:notEmpty>
    
    
 </logic:present>
 
 
 <logic:present name="orderform" property="states">

 <h3><bean:message key="orderview.history" />:</h3>
 <table border="1">
  <tr>
    <th align="left"><bean:message key="orderview.date" /></th><th align="left"><bean:message key="save.status" /></th><th align="left"><bean:message key="orderview.bearbeiter" /></th><th align="left"><bean:message key="orderview.systemnotes" /></th>
  </tr>
   <logic:iterate id="s" name="orderform" property="states">
     <tr>
      <td><bean:write name="s" property="date" />&nbsp;</td>
      <td><logic:equal name="s" property="orderstate" value="bestellt"><bean:message key="menu.ordered" /></logic:equal><logic:equal name="s" property="orderstate" value="erledigt"><bean:message key="menu.closed" /></logic:equal><logic:equal name="s" property="orderstate" value="geliefert"><bean:message key="menu.shipped" /></logic:equal><logic:equal name="s" property="orderstate" value="nicht lieferbar"><bean:message key="menu.unfilled" /></logic:equal><logic:equal name="s" property="orderstate" value="reklamiert"><bean:message key="menu.claimed" /></logic:equal><logic:equal name="s" property="orderstate" value="zu bestellen"><bean:message key="menu.toOrder" /></logic:equal>&nbsp;</td>
      <td><logic:present name="s" property="bearbeiter"><bean:write name="s" property="bearbeiter" /></logic:present>&nbsp;</td>
      <td><logic:present name="s" property="bemerkungen"><bean:write name="s" property="bemerkungen" /></logic:present>&nbsp;</td>
    </tr>
   </logic:iterate>

   </table>
     </logic:present>

<p>
    <logic:equal name="orderform" property="delete" value="true">
      <html:form action="deleteorder.do?method=deleteOrder" method="post">
        <bean:message key="orderview.confirm_delete" /> <input type="submit" value="<bean:message key="modifykonto.yes" />"></input>
        <input type="hidden" name="bid" value="<bean:write name="orderform" property="bestellung.id" />"></input>
      </html:form>
    </logic:equal>
</p>

</div>


 </body>
</html>
