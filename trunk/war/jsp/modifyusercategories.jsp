<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@ page import="ch.dbs.form.*" %>
<%@ page import="ch.dbs.entity.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>

<html>

 <head>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  <title><bean:message bundle="systemConfig" key="application.name"/> - Benutzerkategorien</title>
  <link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
  

  
 </head>
 
<body>
<tiles:insert page="import/header.jsp" flush="true" />
 
<div class="content">

 <br />
<h3><bean:message key="modifykontousers.categories" /></h3>

<html:form action="usercategories.do?method=changeCategory" method="post">

<table>
	<tr>
		<td>
			<bean:message key="modifyusercategories.create" />:
		</td>
	</tr>
	<tr>
		<td>
			<input name="category" <logic:present name="categoryText">value="<bean:write name="categoryText" property="inhalt" />"</logic:present> type="text" size="30" maxlength="50" autofocus />
		</td>
	</tr>
		<tr>
		<td>
			<logic:notPresent name="categoryText">
				<input type="submit" value="<bean:message key="modifyusercategories.submit" />" />
			</logic:notPresent>
			<logic:present name="categoryText">
				<input type="submit" value="<bean:message key="stockplacesmodify.change" />" />
			</logic:present>
		</td>
	</tr>
</table>

<logic:present name="categoryText">
	<input name="id" value="<bean:write name="categoryText" property="id" />" type="hidden" />
	<input name="mod" value="true" type="hidden" />
	<p><a href="usercategories.do?method=prepareCategories"><bean:message key="modifyusercategories.create" /></a></p>
</logic:present>
<logic:notPresent name="categoryText">
	<input name="sav" value="true" type="hidden" />
</logic:notPresent>

</html:form>

<p><br /></p>

<logic:present name="categories">

<p><bean:message key="modifyusercategories.intro" /><br /></p>

  <table border="1">
  <logic:iterate id="cat" name="categories">
    <tr>
      <td><a href="usercategories.do?method=changeCategory&id=<bean:write name="cat" property="id" />"><bean:message key="stockplacesmodify.change" /></a></td>
        <td>       
         <bean:write name="cat" property="inhalt" />
    </td>
    <td><a href="usercategories.do?method=changeCategory&id=<bean:write name="cat" property="id" />&del=true"><bean:message key="stockplacesmodify.delete" /></a></td>
    </tr>
   </logic:iterate>   
  </table>
</logic:present>
              
</div>
</body>
</html>
