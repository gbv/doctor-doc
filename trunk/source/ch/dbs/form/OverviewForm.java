//  Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//  This program is free software; you can redistribute it and/or
//  modify it under the terms of the GNU General Public License
//  as published by the Free Software Foundation; version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program; if not, write to the Free Software
//  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//  Contact: info@doctor-doc.com

package ch.dbs.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import util.ReadSystemConfigurations;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Text;

public class OverviewForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private static final int FIRST_YEAR = 2007; // the first year relevant for statistic for this installation

    private int kid;
    private List<Bestellungen> bestellungen;
    private List<SearchesForm> searches;
    private List<Text> statitexts;
    private List<Text> waehrungen;
    private List<Integer> years;
    private String sort;
    private String sortorder;
    private String filter;
    private String dfrom;
    private String mfrom;
    private String yfrom;
    private String dto;
    private String mto;
    private String yto;
    private String notizen;
    private String fromdate = "";
    private String todate = "";
    private Long tid;
    private Long bid;
    private String report;

    // wird für die Suche benötigt
    private boolean s; // search Variable für Session-Kontrolle bei Sortierung false = keine Suche, true = Suche
    // Suchfelder
    private String value1; // Suchfeld 1
    private String value2; // Suchfeld 2
    private String value3; // reserviert für Suchfeld 3
    private String value4; // reserviert für Suchfeld 4
    private String value5;
    private String value6;
    private String value7;
    private String value8;
    private String value9;
    private String value10;
    private String value11;
    private String value12;
    private String value13;

    // ist gleich, grösser als etc.
    private String condition1;
    private String condition2;
    private String condition3;
    private String condition4;
    private String condition5;
    private String condition6;
    private String condition7;
    private String condition8;
    private String condition9;
    private String condition10;
    private String condition11;
    private String condition12;
    private String condition13;

    // Werte - Suchtext
    private String input1;
    private String input2;
    private String input3;
    private String input4;
    private String input5;
    private String input6;
    private String input7;
    private String input8;
    private String input9;
    private String input10;
    private String input11;
    private String input12;
    private String input13;

    // Boolsche Verknüpfung UND / OR
    private String boolean1;
    private String boolean2;
    private String boolean3;
    private String boolean4;
    private String boolean5;
    private String boolean6;
    private String boolean7;
    private String boolean8;
    private String boolean9;
    private String boolean10;
    private String boolean11;
    private String boolean12;
    private String boolean13;


    public OverviewForm() {
        // set years for select in GUI
        this.setYears(getYearsInSelect());
    }

    /**
     * Gets the years offered in the select on the GUI
     */
    private static List<Integer> getYearsInSelect() {

        final ArrayList<Integer> years = new ArrayList<Integer>();

        // set years for select in GUI: 2007 to now
        final Date d = new Date(); // aktuelles Datum setzen
        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy");
        final String datum = fmt.format(d, ReadSystemConfigurations.getSystemTimezone());
        int yearNow = Integer.parseInt(datum);
        int yearStart = FIRST_YEAR;

        yearNow++;
        for (int j = 0; yearStart < yearNow; j++) {
            years.add(j, yearStart);
            yearStart++;
        }

        return years;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(final Long bid) {
        this.bid = bid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(final Long tid) {
        this.tid = tid;
    }

    public List<Text> getStatitexts() {
        return statitexts;
    }

    public void setStatitexts(final List<Text> statitexts) {
        this.statitexts = statitexts;
    }

    public List<Bestellungen> getBestellungen() {
        return bestellungen;
    }

    public void setBestellungen(final List<Bestellungen> bestellungen) {
        this.bestellungen = bestellungen;
    }

    public List<SearchesForm> getSearches() {
        return searches;
    }

    public void setSearches(final List<SearchesForm> searches) {
        this.searches = searches;
    }

    public boolean isS() {
        return s;
    }

    public void setS(final boolean s) {
        this.s = s;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(final int kid) {
        this.kid = kid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(final String sort) {
        this.sort = sort;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(final String sortorder) {
        this.sortorder = sortorder;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(final String notizen) {
        this.notizen = notizen;
    }

    public String getDfrom() {
        return dfrom;
    }

    public void setDfrom(final String dfrom) {
        this.dfrom = dfrom;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(final String dto) {
        this.dto = dto;
    }

    public String getMfrom() {
        return mfrom;
    }

    public void setMfrom(final String mfrom) {
        this.mfrom = mfrom;
    }

    public String getMto() {
        return mto;
    }

    public void setMto(final String mto) {
        this.mto = mto;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(final List<Integer> years) {
        this.years = years;
    }

    public List<Text> getWaehrungen() {
        return waehrungen;
    }

    public void setWaehrungen(final List<Text> waehrungen) {
        this.waehrungen = waehrungen;
    }

    public String getYfrom() {
        return yfrom;
    }

    public void setYfrom(final String yfrom) {
        this.yfrom = yfrom;
    }

    public String getYto() {
        return yto;
    }

    public void setYto(final String yto) {
        this.yto = yto;
    }

    public String getBoolean1() {
        return boolean1;
    }

    public void setBoolean1(final String boolean1) {
        this.boolean1 = boolean1;
    }

    public String getBoolean10() {
        return boolean10;
    }

    public void setBoolean10(final String boolean10) {
        this.boolean10 = boolean10;
    }

    public String getBoolean11() {
        return boolean11;
    }

    public void setBoolean11(final String boolean11) {
        this.boolean11 = boolean11;
    }

    public String getBoolean12() {
        return boolean12;
    }

    public void setBoolean12(final String boolean12) {
        this.boolean12 = boolean12;
    }

    public String getBoolean13() {
        return boolean13;
    }

    public void setBoolean13(final String boolean13) {
        this.boolean13 = boolean13;
    }

    public String getBoolean2() {
        return boolean2;
    }

    public void setBoolean2(final String boolean2) {
        this.boolean2 = boolean2;
    }

    public String getBoolean3() {
        return boolean3;
    }

    public void setBoolean3(final String boolean3) {
        this.boolean3 = boolean3;
    }

    public String getBoolean4() {
        return boolean4;
    }

    public void setBoolean4(final String boolean4) {
        this.boolean4 = boolean4;
    }

    public String getBoolean5() {
        return boolean5;
    }

    public void setBoolean5(final String boolean5) {
        this.boolean5 = boolean5;
    }

    public String getBoolean6() {
        return boolean6;
    }

    public void setBoolean6(final String boolean6) {
        this.boolean6 = boolean6;
    }

    public String getBoolean7() {
        return boolean7;
    }

    public void setBoolean7(final String boolean7) {
        this.boolean7 = boolean7;
    }

    public String getBoolean8() {
        return boolean8;
    }

    public void setBoolean8(final String boolean8) {
        this.boolean8 = boolean8;
    }

    public String getBoolean9() {
        return boolean9;
    }

    public void setBoolean9(final String boolean9) {
        this.boolean9 = boolean9;
    }

    public String getCondition1() {
        return condition1;
    }

    public void setCondition1(final String condition1) {
        this.condition1 = condition1;
    }

    public String getCondition10() {
        return condition10;
    }

    public void setCondition10(final String condition10) {
        this.condition10 = condition10;
    }

    public String getCondition11() {
        return condition11;
    }

    public void setCondition11(final String condition11) {
        this.condition11 = condition11;
    }

    public String getCondition12() {
        return condition12;
    }

    public void setCondition12(final String condition12) {
        this.condition12 = condition12;
    }

    public String getCondition13() {
        return condition13;
    }

    public void setCondition13(final String condition13) {
        this.condition13 = condition13;
    }

    public String getCondition2() {
        return condition2;
    }

    public void setCondition2(final String condition2) {
        this.condition2 = condition2;
    }

    public String getCondition3() {
        return condition3;
    }

    public void setCondition3(final String condition3) {
        this.condition3 = condition3;
    }

    public String getCondition4() {
        return condition4;
    }

    public void setCondition4(final String condition4) {
        this.condition4 = condition4;
    }

    public String getCondition5() {
        return condition5;
    }

    public void setCondition5(final String condition5) {
        this.condition5 = condition5;
    }

    public String getCondition6() {
        return condition6;
    }

    public void setCondition6(final String condition6) {
        this.condition6 = condition6;
    }

    public String getCondition7() {
        return condition7;
    }

    public void setCondition7(final String condition7) {
        this.condition7 = condition7;
    }

    public String getCondition8() {
        return condition8;
    }

    public void setCondition8(final String condition8) {
        this.condition8 = condition8;
    }

    public String getCondition9() {
        return condition9;
    }

    public void setCondition9(final String condition9) {
        this.condition9 = condition9;
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(final String input1) {
        this.input1 = input1;
    }

    public String getInput10() {
        return input10;
    }

    public void setInput10(final String input10) {
        this.input10 = input10;
    }

    public String getInput11() {
        return input11;
    }

    public void setInput11(final String input11) {
        this.input11 = input11;
    }

    public String getInput12() {
        return input12;
    }

    public void setInput12(final String input12) {
        this.input12 = input12;
    }

    public String getInput13() {
        return input13;
    }

    public void setInput13(final String input13) {
        this.input13 = input13;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(final String input2) {
        this.input2 = input2;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(final String input3) {
        this.input3 = input3;
    }

    public String getInput4() {
        return input4;
    }

    public void setInput4(final String input4) {
        this.input4 = input4;
    }

    public String getInput5() {
        return input5;
    }

    public void setInput5(final String input5) {
        this.input5 = input5;
    }

    public String getInput6() {
        return input6;
    }

    public void setInput6(final String input6) {
        this.input6 = input6;
    }

    public String getInput7() {
        return input7;
    }

    public void setInput7(final String input7) {
        this.input7 = input7;
    }

    public String getInput8() {
        return input8;
    }

    public void setInput8(final String input8) {
        this.input8 = input8;
    }

    public String getInput9() {
        return input9;
    }

    public void setInput9(final String input9) {
        this.input9 = input9;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(final String value1) {
        this.value1 = value1;
    }

    public String getValue10() {
        return value10;
    }

    public void setValue10(final String value10) {
        this.value10 = value10;
    }

    public String getValue11() {
        return value11;
    }

    public void setValue11(final String value11) {
        this.value11 = value11;
    }

    public String getValue12() {
        return value12;
    }

    public void setValue12(final String value12) {
        this.value12 = value12;
    }

    public String getValue13() {
        return value13;
    }

    public void setValue13(final String value13) {
        this.value13 = value13;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(final String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(final String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(final String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(final String value5) {
        this.value5 = value5;
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(final String value6) {
        this.value6 = value6;
    }

    public String getValue7() {
        return value7;
    }

    public void setValue7(final String value7) {
        this.value7 = value7;
    }

    public String getValue8() {
        return value8;
    }

    public void setValue8(final String value8) {
        this.value8 = value8;
    }

    public String getValue9() {
        return value9;
    }

    public void setValue9(final String value9) {
        this.value9 = value9;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(final String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(final String todate) {
        this.todate = todate;
    }

    public String getReport() {
        return report;
    }

    public void setReport(final String report) {
        this.report = report;
    }

}
