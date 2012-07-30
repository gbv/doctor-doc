<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="ch.dbs.entity.*"%>
<%@ page import="ch.dbs.form.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title><bean:message bundle="systemConfig" key="application.name"/> - <bean:message key="stats.titel" /></title>
<link rel="stylesheet" type="text/css" href="jsp/import/styles.css" />
</head>
<body>

<tiles:insert page="import/header.jsp" flush="true" />

<div class="content">

<logic:present name="userinfo" property="konto">

<br>

  <h3><bean:message key="stats.header" /></h3>
  
    <table>
    <html:form action="statistics" method="post" focus="from">
      <tr>
        <td align=center><select name="dfrom">
          <option value="1"
            <logic:equal name="overviewform" property="dfrom" value="1">selected</logic:equal>>1</option>
          <option value="2"
            <logic:equal name="overviewform" property="dfrom" value="2">selected</logic:equal>>2</option>
          <option value="3"
            <logic:equal name="overviewform" property="dfrom" value="3">selected</logic:equal>>3</option>
          <option value="4"
            <logic:equal name="overviewform" property="dfrom" value="4">selected</logic:equal>>4</option>
          <option value="5"
            <logic:equal name="overviewform" property="dfrom" value="5">selected</logic:equal>>5</option>
          <option value="6"
            <logic:equal name="overviewform" property="dfrom" value="6">selected</logic:equal>>6</option>
          <option value="7"
            <logic:equal name="overviewform" property="dfrom" value="7">selected</logic:equal>>7</option>
          <option value="8"
            <logic:equal name="overviewform" property="dfrom" value="8">selected</logic:equal>>8</option>
          <option value="9"
            <logic:equal name="overviewform" property="dfrom" value="9">selected</logic:equal>>9</option>
          <option value="10"
            <logic:equal name="overviewform" property="dfrom" value="10">selected</logic:equal>>10</option>
          <option value="11"
            <logic:equal name="overviewform" property="dfrom" value="11">selected</logic:equal>>11</option>
          <option value="12"
            <logic:equal name="overviewform" property="dfrom" value="12">selected</logic:equal>>12</option>
          <option value="13"
            <logic:equal name="overviewform" property="dfrom" value="13">selected</logic:equal>>13</option>
          <option value="14"
            <logic:equal name="overviewform" property="dfrom" value="14">selected</logic:equal>>14</option>
          <option value="15"
            <logic:equal name="overviewform" property="dfrom" value="15">selected</logic:equal>>15</option>
          <option value="16"
            <logic:equal name="overviewform" property="dfrom" value="16">selected</logic:equal>>16</option>
          <option value="17"
            <logic:equal name="overviewform" property="dfrom" value="17">selected</logic:equal>>17</option>
          <option value="18"
            <logic:equal name="overviewform" property="dfrom" value="18">selected</logic:equal>>18</option>
          <option value="19"
            <logic:equal name="overviewform" property="dfrom" value="19">selected</logic:equal>>19</option>
          <option value="20"
            <logic:equal name="overviewform" property="dfrom" value="20">selected</logic:equal>>20</option>
          <option value="21"
            <logic:equal name="overviewform" property="dfrom" value="21">selected</logic:equal>>21</option>
          <option value="22"
            <logic:equal name="overviewform" property="dfrom" value="22">selected</logic:equal>>22</option>
          <option value="23"
            <logic:equal name="overviewform" property="dfrom" value="23">selected</logic:equal>>23</option>
          <option value="24"
            <logic:equal name="overviewform" property="dfrom" value="24">selected</logic:equal>>24</option>
          <option value="25"
            <logic:equal name="overviewform" property="dfrom" value="25">selected</logic:equal>>25</option>
          <option value="26"
            <logic:equal name="overviewform" property="dfrom" value="26">selected</logic:equal>>26</option>
          <option value="27"
            <logic:equal name="overviewform" property="dfrom" value="27">selected</logic:equal>>27</option>
          <option value="28"
            <logic:equal name="overviewform" property="dfrom" value="28">selected</logic:equal>>28</option>
          <option value="29"
            <logic:equal name="overviewform" property="dfrom" value="29">selected</logic:equal>>29</option>
          <option value="30"
            <logic:equal name="overviewform" property="dfrom" value="30">selected</logic:equal>>30</option>
          <option value="31"
            <logic:equal name="overviewform" property="dfrom" value="31">selected</logic:equal>>31</option>
        </select> <select name="mfrom">
          <option value="1"
            <logic:equal name="overviewform" property="mfrom" value="1">selected</logic:equal>>1</option>
          <option value="2"
            <logic:equal name="overviewform" property="mfrom" value="2">selected</logic:equal>>2</option>
          <option value="3"
            <logic:equal name="overviewform" property="mfrom" value="3">selected</logic:equal>>3</option>
          <option value="4"
            <logic:equal name="overviewform" property="mfrom" value="4">selected</logic:equal>>4</option>
          <option value="5"
            <logic:equal name="overviewform" property="mfrom" value="5">selected</logic:equal>>5</option>
          <option value="6"
            <logic:equal name="overviewform" property="mfrom" value="6">selected</logic:equal>>6</option>
          <option value="7"
            <logic:equal name="overviewform" property="mfrom" value="7">selected</logic:equal>>7</option>
          <option value="8"
            <logic:equal name="overviewform" property="mfrom" value="8">selected</logic:equal>>8</option>
          <option value="9"
            <logic:equal name="overviewform" property="mfrom" value="9">selected</logic:equal>>9</option>
          <option value="10"
            <logic:equal name="overviewform" property="mfrom" value="10">selected</logic:equal>>10</option>
          <option value="11"
            <logic:equal name="overviewform" property="mfrom" value="11">selected</logic:equal>>11</option>
          <option value="12"
            <logic:equal name="overviewform" property="mfrom" value="12">selected</logic:equal>>12</option>
        </select> <select name="yfrom">
          <logic:iterate id="yf" name="overviewform" property="years">
            <bean:define id="tmp" name="overviewform" property="yfrom"
              type="java.lang.String" />
            <option value="<bean:write name="yf" />"
              <logic:equal name="yf" value="<%=tmp%>">selected</logic:equal>><bean:write
              name="yf" /></option>
          </logic:iterate>
        </select> - <select name="dto">
          <option value="1"
            <logic:equal name="overviewform" property="dto" value="1">selected</logic:equal>>1</option>
          <option value="2"
            <logic:equal name="overviewform" property="dto" value="2">selected</logic:equal>>2</option>
          <option value="3"
            <logic:equal name="overviewform" property="dto" value="3">selected</logic:equal>>3</option>
          <option value="4"
            <logic:equal name="overviewform" property="dto" value="4">selected</logic:equal>>4</option>
          <option value="5"
            <logic:equal name="overviewform" property="dto" value="5">selected</logic:equal>>5</option>
          <option value="6"
            <logic:equal name="overviewform" property="dto" value="6">selected</logic:equal>>6</option>
          <option value="7"
            <logic:equal name="overviewform" property="dto" value="7">selected</logic:equal>>7</option>
          <option value="8"
            <logic:equal name="overviewform" property="dto" value="8">selected</logic:equal>>8</option>
          <option value="9"
            <logic:equal name="overviewform" property="dto" value="9">selected</logic:equal>>9</option>
          <option value="10"
            <logic:equal name="overviewform" property="dto" value="10">selected</logic:equal>>10</option>
          <option value="11"
            <logic:equal name="overviewform" property="dto" value="11">selected</logic:equal>>11</option>
          <option value="12"
            <logic:equal name="overviewform" property="dto" value="12">selected</logic:equal>>12</option>
          <option value="13"
            <logic:equal name="overviewform" property="dto" value="13">selected</logic:equal>>13</option>
          <option value="14"
            <logic:equal name="overviewform" property="dto" value="14">selected</logic:equal>>14</option>
          <option value="15"
            <logic:equal name="overviewform" property="dto" value="15">selected</logic:equal>>15</option>
          <option value="16"
            <logic:equal name="overviewform" property="dto" value="16">selected</logic:equal>>16</option>
          <option value="17"
            <logic:equal name="overviewform" property="dto" value="17">selected</logic:equal>>17</option>
          <option value="18"
            <logic:equal name="overviewform" property="dto" value="18">selected</logic:equal>>18</option>
          <option value="19"
            <logic:equal name="overviewform" property="dto" value="19">selected</logic:equal>>19</option>
          <option value="20"
            <logic:equal name="overviewform" property="dto" value="20">selected</logic:equal>>20</option>
          <option value="21"
            <logic:equal name="overviewform" property="dto" value="21">selected</logic:equal>>21</option>
          <option value="22"
            <logic:equal name="overviewform" property="dto" value="22">selected</logic:equal>>22</option>
          <option value="23"
            <logic:equal name="overviewform" property="dto" value="23">selected</logic:equal>>23</option>
          <option value="24"
            <logic:equal name="overviewform" property="dto" value="24">selected</logic:equal>>24</option>
          <option value="25"
            <logic:equal name="overviewform" property="dto" value="25">selected</logic:equal>>25</option>
          <option value="26"
            <logic:equal name="overviewform" property="dto" value="26">selected</logic:equal>>26</option>
          <option value="27"
            <logic:equal name="overviewform" property="dto" value="27">selected</logic:equal>>27</option>
          <option value="28"
            <logic:equal name="overviewform" property="dto" value="28">selected</logic:equal>>28</option>
          <option value="29"
            <logic:equal name="overviewform" property="dto" value="29">selected</logic:equal>>29</option>
          <option value="30"
            <logic:equal name="overviewform" property="dto" value="30">selected</logic:equal>>30</option>
          <option value="31"
            <logic:equal name="overviewform" property="dto" value="31">selected</logic:equal>>31</option>
        </select> <select name="mto">
          <option value="1"
            <logic:equal name="overviewform" property="mto" value="1">selected</logic:equal>>1</option>
          <option value="2"
            <logic:equal name="overviewform" property="mto" value="2">selected</logic:equal>>2</option>
          <option value="3"
            <logic:equal name="overviewform" property="mto" value="3">selected</logic:equal>>3</option>
          <option value="4"
            <logic:equal name="overviewform" property="mto" value="4">selected</logic:equal>>4</option>
          <option value="5"
            <logic:equal name="overviewform" property="mto" value="5">selected</logic:equal>>5</option>
          <option value="6"
            <logic:equal name="overviewform" property="mto" value="6">selected</logic:equal>>6</option>
          <option value="7"
            <logic:equal name="overviewform" property="mto" value="7">selected</logic:equal>>7</option>
          <option value="8"
            <logic:equal name="overviewform" property="mto" value="8">selected</logic:equal>>8</option>
          <option value="9"
            <logic:equal name="overviewform" property="mto" value="9">selected</logic:equal>>9</option>
          <option value="10"
            <logic:equal name="overviewform" property="mto" value="10">selected</logic:equal>>10</option>
          <option value="11"
            <logic:equal name="overviewform" property="mto" value="11">selected</logic:equal>>11</option>
          <option value="12"
            <logic:equal name="overviewform" property="mto" value="12">selected</logic:equal>>12</option>
        </select> <select name="yto">
          <logic:iterate id="yt" name="overviewform" property="years">
            <bean:define id="tmp" name="overviewform" property="yto"
              type="java.lang.String" />
            <option value="<bean:write name="yt" />"
              <logic:equal name="yt" value="<%=tmp%>">selected</logic:equal>><bean:write
              name="yt" /></option>
          </logic:iterate>
        </select> <input type="hidden" name="method" value="kontoOrders" /> <input
          type="submit" value="<bean:message key="stats.submit" />"></td>
      </tr>
    </html:form>
  </table>

  <h4><bean:write name="userinfo" property="biblioname" /><br>
  <bean:write name="overviewform" property="dfrom" />-<bean:write
    name="overviewform" property="mfrom" />-<bean:write
    name="overviewform" property="yfrom" /> bis <bean:write
    name="overviewform" property="dto" />-<bean:write name="overviewform"
    property="mto" />-<bean:write name="overviewform" property="yto" /><br>
  </h4>
* <bean:message key="stats.comment1" /><br>
** <bean:message key="stats.comment2" />

<p></p>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.konto" />*</th>
      <th id="tablestats"><bean:message key="stats.total" /></th>
    </tr>
    <logic:present name="statistics" property="kontoordersstat">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
      <td id="tablestats">
        <bean:write name="statistics" property="kontoordersstat.total" />
      </td>
    </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.lieferant" />*</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="lieferantstat">
      <logic:iterate id="os" name="statistics"
        property="lieferantstat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="lieferantstat.total" /></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.kostenart" />*</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
    </tr>
    <logic:present name="statistics" property="gratis_kosten">
      <logic:iterate id="os" name="statistics"
        property="gratis_kosten.statistik">
        <tr>
          <bean:define id="tmp" name="os" property="label" type="java.lang.String" />
          <th id="tablestatsleft"><bean:message key="<%=tmp%>" /></th>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
        </tr>
      </logic:iterate>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.currency" />*</th>
      <th id="tablestats"><bean:message key="stats.kosten" /></th>
    </tr>
    <logic:present name="statistics" property="sum_gratis_kosten">
      <logic:iterate id="os" name="statistics"
        property="sum_gratis_kosten.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os" property="preis" /></td>
        </tr>
      </logic:iterate>
    </logic:present>
  </table>


  <br>


  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.medientyp" />*</th>
      <th id="tablestats"><bean:message key="stats.total" /></th>
      <logic:present name="statistics" property="mediatype">
        <logic:iterate id="os" name="statistics"
          property="mediatype.statistik">
          <th id="tablestats">
            <logic:equal name="os" property="label" value="Artikel">
              <bean:message key="save.artikel" />
            </logic:equal>
            <logic:equal name="os" property="label" value="Teilkopie Buch">
              <bean:message key="save.bookpart" />
            </logic:equal>
            <logic:equal name="os" property="label" value="Buch">
              <bean:message key="save.book" />
            </logic:equal>
          </th>
        </logic:iterate>
    </tr>
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="mediatype.total" /></td>
      <logic:iterate id="os" name="statistics"
        property="mediatype.statistik">
        <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
      </logic:iterate>
    </tr>
    </logic:present>
  </table>


  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.lieferart" />*</th>
      <th id="tablestats"><bean:message key="stats.total" /></th>
      <logic:present name="statistics" property="lieferartstat">
        <logic:iterate id="os" name="statistics"
          property="lieferartstat.statistik">
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
        </logic:iterate>
    </tr>
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="lieferartstat.total" /></td>
      <logic:iterate id="os" name="statistics"
        property="lieferartstat.statistik">
        <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
      </logic:iterate>
    </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.fileformat" />*</th>
      <th id="tablestats"><bean:message key="stats.total" /></th>
      <logic:present name="statistics" property="fileformatstat">
        <logic:iterate id="os" name="statistics"
          property="fileformatstat.statistik">
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
        </logic:iterate>
    </tr>
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="fileformatstat.total" /></td>
      <logic:iterate id="os" name="statistics"
        property="fileformatstat.statistik">
        <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
      </logic:iterate>
    </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <logic:present name="statistics" property="prioritystat">
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.prio" />*</th>
        <th id="tablestats"><bean:message key="stats.total" /></th>
        <logic:iterate id="os" name="statistics" property="prioritystat.statistik">
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
        </logic:iterate>
      </tr>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="prioritystat.total" /></td>
        <logic:iterate id="os" name="statistics" property="prioritystat.statistik">
          <td id="tablestats"><bean:write name="os" property="anzahl" />
          </td>
        </logic:iterate>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.user" />*</th>
      <th id="tablestats"><bean:message key="stats.total" /></th>
    </tr>
    <logic:present name="statistics" property="zeitschriftstat">
      <logic:iterate id="os" name="statistics"
        property="totuserwithordersstat.statistik">
        <tr>
          <th id="tablestatsleft"><bean:message key="stats.orders" /></th>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
        </tr>
      </logic:iterate>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.gender" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="genderstat">
      <logic:iterate id="os" name="statistics"
        property="genderstat.statistik">
      <bean:define id="tmp" name="os" property="label" type="java.lang.String" />
        <tr>
          <th id="tablestatsleft"><bean:message key="<%=tmp%>" /></th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="genderstat.total_two" /></td>
        <td id="tablestats"><bean:write name="statistics"
          property="genderstat.total" /></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.institution" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="institutionstat">
      <logic:iterate id="os" name="statistics"
        property="institutionstat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="institutionstat.total_two" /></td>
        <td id="tablestats"><bean:write name="statistics"
          property="institutionstat.total" /></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.abteilung" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="abteilungstat">
      <logic:iterate id="os" name="statistics"
        property="abteilungstat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="abteilungstat.total_two" /></td>
        <td id="tablestats"><bean:write name="statistics"
          property="abteilungstat.total" /></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
      </tr>
    </logic:present>
  </table>
  
    <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="modifykontousers.category" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="categorystat">
      <logic:iterate id="os" name="statistics"
        property="categorystat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="categorystat.total_two" /></td>
        <td id="tablestats"><bean:write name="statistics"
          property="categorystat.total" /></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
        <td id="tablestats"></td>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.plz" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="bestellform.ort" /></th>
    </tr>
    <logic:present name="statistics" property="ortstat">
      <logic:iterate id="os" name="statistics" property="ortstat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestatsleft"><bean:write name="os"
            property="label_two" />&nbsp;</td>
        </tr>
      </logic:iterate>
    </logic:present>
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.total" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="ortstat.total_two" /></td>
      <td id="tablestats"><bean:write name="statistics"
        property="ortstat.total" /></td>
      <td id="tablestats">&nbsp;</td>
    </tr>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestatsleft"><bean:message key="stats.land" />**</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
    </tr>
    <logic:present name="statistics" property="landstat">
      <logic:iterate id="os" name="statistics"
        property="landstat.statistik">
        <tr>
          <th id="tablestatsleft">
            <logic:notEqual name="os" property="label" value="k.A.">
              <bean:write name="os" property="label" />
            </logic:notEqual>
            <logic:equal name="os" property="label" value="k.A.">
              <bean:message key="stats.notSpecified" />
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os"
            property="anzahl_two" /></td>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
        </tr>
      </logic:iterate>
      <tr>
        <th id="tablestatsleft"><bean:message key="stats.total" /></th>
        <td id="tablestats"><bean:write name="statistics"
          property="landstat.total_two" /></td>
        <td id="tablestats"><bean:write name="statistics"
          property="landstat.total" /></td>
      </tr>
    </logic:present>
  </table>

  <br>

  <table class="border">
    <tr>
      <th id="tablestats"><bean:message key="stats.issn" /></th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
      <th id="tablestats"><bean:message key="stats.users" /></th>
      <th id="tablestatsleft"><bean:message key="stats.issn_comment" /></th>
      <th id="tablestatsleft"><bean:message key="stats.eur" /></th>
      <th id="tablestatsleft"><bean:message key="stats.chf" /></th>
      <th id="tablestatsleft"><bean:message key="stats.usd" /></th>
      <th id="tablestatsleft"><bean:message key="stats.gbp" /></th>
    </tr>
    <logic:present name="statistics" property="zeitschriftstat">
      <logic:iterate id="os" name="statistics"
        property="zeitschriftstat.statistik">
        <tr>
          <th id="tablestats">
            <logic:notEqual name="os" property="label" value="andere">
              <nobr><a href="http://ezb.uni-regensburg.de/ezeit/searchres.phtml?bibid=<bean:write name="userinfo" property="konto.ezbid" />&colors=7&lang=de&jq_type1=IS&jq_term1=<bean:write name="os" property="label" />&jq_bool2=AND&jq_not2=+&jq_type2=KS&jq_term2=&jq_bool3=AND&jq_not3=+&jq_type3=PU&jq_term3=&jq_bool4=AND&jq_not4=+&jq_type4=IS&jq_term4=&offset=-1&hits_per_page=50&search_journal=Suche+starten&Notations%5B%5D=all&selected_colors%5B%5D=1&selected_colors%5B%5D=2&selected_colors%5B%5D=4"
              target="_blank"><bean:write name="os" property="label" /></a></nobr>
            </logic:notEqual>
            <logic:equal name="os" property="label" value="andere">
              <bean:message key="save.andere" />*
            </logic:equal>
          </th>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
          <td id="tablestats"><logic:notEqual name="os"
            property="anzahl_two" value="0">
            <bean:write name="os" property="anzahl_two" />
          </logic:notEqual>&nbsp;</td>
          <td id="tablestatsleft"><bean:write name="os"
            property="label_two" /></td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.eur">
            <bean:write name="os" property="preiswaehrung.eur.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.chf">
            <bean:write name="os" property="preiswaehrung.chf.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.usd">
            <bean:write name="os" property="preiswaehrung.usd.preis" />
          </logic:present>&nbsp;</td>
          <td id="tablestatsleft"><logic:present name="os"
            property="preiswaehrung.gbp">
            <bean:write name="os" property="preiswaehrung.gbp.preis" />
          </logic:present>&nbsp;</td>
        </tr>
      </logic:iterate>
    </logic:present>
    <tr>
      <th id="tablestats"><bean:message key="stats.total" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="zeitschriftstat.total" /></td>
      <td id="tablestats">&nbsp;</td>
      <td id="tablestats">&nbsp;</td>
      <td id="tablestats">&nbsp;</td>
      <td id="tablestats">&nbsp;</td>
      <td id="tablestats">&nbsp;</td>
      <td id="tablestats">&nbsp;</td>
    </tr>
  </table>
  
  <bean:message key="stats.comment3" /><br>

  <br>

  <table class="border">
    <tr>
      <th id="tablestats"><bean:message key="stats.jahr" />*</th>
      <th id="tablestats"><bean:message key="stats.orders" /></th>
    </tr>
    <logic:present name="statistics" property="jahrstat">
      <logic:iterate id="os" name="statistics"
        property="jahrstat.statistik">
        <tr>
          <th id="tablestats"><bean:write name="os" property="label" /></th>
          <td id="tablestats"><bean:write name="os" property="anzahl" /></td>
        </tr>
      </logic:iterate>
    </logic:present>
    <tr>
      <th id="tablestats"><bean:message key="stats.total" /></th>
      <td id="tablestats"><bean:write name="statistics"
        property="jahrstat.total" /></td>
    </tr>
  </table>

  <br>






</logic:present>

<p></p>

<logic:notPresent name="userinfo" property="konto">
  <bean:message key="error.kontos" />
</logic:notPresent></div>
</body>
</html>
