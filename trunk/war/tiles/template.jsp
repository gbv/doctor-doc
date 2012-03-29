<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<html>

<head>
    <title><tiles:getAsString name="title" ignore="true"/></title>
</head>

<body>

<table class="border">
<tr>
<td id="border" width="100%" colspan="2" valign="top"><tiles:insert attribute="header"/></td>
</tr>
<tr>
<td id="border" width="23%"><tiles:insert attribute="menu"/></td>
<td id="border" width="77%" valign="top" valign="top"><tiles:insert attribute="body"/></td>
</tr>
<tr>
<td id="border" width="100%" colspan="2" valign="top"><tiles:insert attribute="bottom"/></td>
</tr>
</table>

</body>

</html>