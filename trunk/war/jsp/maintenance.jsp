<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="ch.dbs.form.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title> <bean:message key="suppliers.title" /></title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
 </head>
 <body>
 
<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<table style="position:absolute; text-align:left; left:<bean:message key="submenupos.konto" />px; z-index:2;">
  <tr>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.manage" />"><a href="modifykont.do?method=prepareModifyKonto&activemenu=konto"><bean:message key="modifykonto.header" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="modifykonto.managebf" />"><a href="bfconfigureselect.do?method=prepareConfigure"><bean:message key="bestellformconfigureselect.titel" /></a></td>
    <td id="submenu" nowrap="nowrap" title="<bean:message key="suppliers.title" />"><a href="listsuppliers.do"><bean:message key="suppliers.title" /></a></td>
    <td id="submenuactive" nowrap="nowrap" title="<bean:message key="maintenance.title" />"><a href="maintenance.do"><bean:message key="maintenance.title" /></a></td>
  </tr>
</table>

<br>

<h3><bean:message key="maintenance.title" /></h3>

<p><br></p>

<form action="bulkoperation.do?method=deleteorders" method="post">

<h4><bean:message key="maintenance.orders" /></h4>

<p>
	<select name="months">
       <option value="0" >0 [<bean:message key="maintenance.all" />]</option>
       <option value="1" >1 <bean:message key="maintenance.months" /></option>
       <option value="2" >2 <bean:message key="maintenance.months" /></option>
       <option value="3" >3 <bean:message key="maintenance.months" /></option>
       <option value="4" >4 <bean:message key="maintenance.months" /></option>
       <option value="5" >5 <bean:message key="maintenance.months" /></option>
       <option value="6" >6 <bean:message key="maintenance.months" /></option>
       <option value="7" >7 <bean:message key="maintenance.months" /></option>
       <option value="8" >8 <bean:message key="maintenance.months" /></option>
       <option value="9" >9 <bean:message key="maintenance.months" /></option>
       <option value="10" >10 <bean:message key="maintenance.months" /></option>
       <option value="11" >11 <bean:message key="maintenance.months" /></option>
       <option value="12" selected="selected">1 <bean:message key="bestellform.jahr" /></option>
       <option value="13" >13 <bean:message key="maintenance.months" /></option>
       <option value="14" >14 <bean:message key="maintenance.months" /></option>
       <option value="15" >15 <bean:message key="maintenance.months" /></option>
       <option value="16" >16 <bean:message key="maintenance.months" /></option>
       <option value="17" >17 <bean:message key="maintenance.months" /></option>
       <option value="18" >18 <bean:message key="maintenance.months" /></option>
       <option value="19" >19 <bean:message key="maintenance.months" /></option>
       <option value="20" >20 <bean:message key="maintenance.months" /></option>
       <option value="21" >21 <bean:message key="maintenance.months" /></option>
       <option value="22" >22 <bean:message key="maintenance.months" /></option>
       <option value="23" >23 <bean:message key="maintenance.months" /></option>
       <option value="24" >2 <bean:message key="maintenance.years" /></option>
       <option value="25" >25 <bean:message key="maintenance.months" /></option>
       <option value="26" >26 <bean:message key="maintenance.months" /></option>
       <option value="27" >27 <bean:message key="maintenance.months" /></option>
       <option value="28" >28 <bean:message key="maintenance.months" /></option>
       <option value="29" >29 <bean:message key="maintenance.months" /></option>
       <option value="30" >30 <bean:message key="maintenance.months" /></option>
       <option value="31" >31 <bean:message key="maintenance.months" /></option>
       <option value="32" >32 <bean:message key="maintenance.months" /></option>
       <option value="33" >33 <bean:message key="maintenance.months" /></option>
       <option value="34" >34 <bean:message key="maintenance.months" /></option>
       <option value="35" >35 <bean:message key="maintenance.months" /></option>
       <option value="36" >3 <bean:message key="maintenance.years" /></option>
       <option value="48" >4 <bean:message key="maintenance.years" /></option>
       <option value="60" >5 <bean:message key="maintenance.years" /></option>
       <option value="72" >6 <bean:message key="maintenance.years" /></option>
       <option value="84" >7 <bean:message key="maintenance.years" /></option>
       <option value="96" >8 <bean:message key="maintenance.years" /></option>
       <option value="108" >9 <bean:message key="maintenance.years" /></option>
       <option value="120" >10 <bean:message key="maintenance.years" /></option>
    </select>
</p>

<p><input type="submit" value="<bean:message key="stockplacesmodify.delete" />" /></p>

</form>

<p><br></p>

<form action="bulkoperation.do?method=deleteusernoroders" method="post">

<h4><bean:message key="maintenance.patronsNoOrders" /></h4>

<p>
	<select name="months">
       <option value="0" >0 [<bean:message key="maintenance.all" />]</option>
       <option value="1" >1 <bean:message key="maintenance.months" /></option>
       <option value="2" >2 <bean:message key="maintenance.months" /></option>
       <option value="3" >3 <bean:message key="maintenance.months" /></option>
       <option value="4" >4 <bean:message key="maintenance.months" /></option>
       <option value="5" >5 <bean:message key="maintenance.months" /></option>
       <option value="6" >6 <bean:message key="maintenance.months" /></option>
       <option value="7" >7 <bean:message key="maintenance.months" /></option>
       <option value="8" >8 <bean:message key="maintenance.months" /></option>
       <option value="9" >9 <bean:message key="maintenance.months" /></option>
       <option value="10" >10 <bean:message key="maintenance.months" /></option>
       <option value="11" >11 <bean:message key="maintenance.months" /></option>
       <option value="12" selected="selected">1 <bean:message key="bestellform.jahr" /></option>
       <option value="13" >13 <bean:message key="maintenance.months" /></option>
       <option value="14" >14 <bean:message key="maintenance.months" /></option>
       <option value="15" >15 <bean:message key="maintenance.months" /></option>
       <option value="16" >16 <bean:message key="maintenance.months" /></option>
       <option value="17" >17 <bean:message key="maintenance.months" /></option>
       <option value="18" >18 <bean:message key="maintenance.months" /></option>
       <option value="19" >19 <bean:message key="maintenance.months" /></option>
       <option value="20" >20 <bean:message key="maintenance.months" /></option>
       <option value="21" >21 <bean:message key="maintenance.months" /></option>
       <option value="22" >22 <bean:message key="maintenance.months" /></option>
       <option value="23" >23 <bean:message key="maintenance.months" /></option>
       <option value="24" >2 <bean:message key="maintenance.years" /></option>
       <option value="25" >25 <bean:message key="maintenance.months" /></option>
       <option value="26" >26 <bean:message key="maintenance.months" /></option>
       <option value="27" >27 <bean:message key="maintenance.months" /></option>
       <option value="28" >28 <bean:message key="maintenance.months" /></option>
       <option value="29" >29 <bean:message key="maintenance.months" /></option>
       <option value="30" >30 <bean:message key="maintenance.months" /></option>
       <option value="31" >31 <bean:message key="maintenance.months" /></option>
       <option value="32" >32 <bean:message key="maintenance.months" /></option>
       <option value="33" >33 <bean:message key="maintenance.months" /></option>
       <option value="34" >34 <bean:message key="maintenance.months" /></option>
       <option value="35" >35 <bean:message key="maintenance.months" /></option>
       <option value="36" >3 <bean:message key="maintenance.years" /></option>
       <option value="48" >4 <bean:message key="maintenance.years" /></option>
       <option value="60" >5 <bean:message key="maintenance.years" /></option>
       <option value="72" >6 <bean:message key="maintenance.years" /></option>
       <option value="84" >7 <bean:message key="maintenance.years" /></option>
       <option value="96" >8 <bean:message key="maintenance.years" /></option>
       <option value="108" >9 <bean:message key="maintenance.years" /></option>
       <option value="120" >10 <bean:message key="maintenance.years" /></option>
    </select>
</p>

<p><input type="submit" value="<bean:message key="stockplacesmodify.delete" />" /></p>

</form>

<p><br></p>

</div>
 </body>
</html>
