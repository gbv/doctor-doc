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

import org.apache.struts.action.ActionForm;


public final class IlvReportForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    
    private int ilvformnr; // ILV form report number

    private String lieferant = "";
    private String signatur = ""; // <bean:write name="IlvReportForm" property="signatur" />
    private String titleofessay = ""; // <bean:write name="IlvReportForm" property="titleofessay" />
    private String name = ""; // <bean:write name="IlvReportForm" property="name" />
    private String librarycard = ""; // <bean:write name="IlvReportForm" property="librarycard" />
    private String issn = ""; // <bean:write name="IlvReportForm" property="issn" />
    private String pmid = ""; // Pubmed-ID <bean:write name="IlvReportForm" property="pmid" />
    private String year = ""; // <bean:write name="IlvReportForm" property="year" />
    private String volumevintage = ""; // <bean:write name="IlvReportForm" property="volumevintage" />
    private String booklet = ""; // <bean:write name="IlvReportForm" property="booklet" />
    private String pages = ""; // <bean:write name="IlvReportForm" property="pages" />
    private String phone = ""; // <bean:write name="IlvReportForm" property="phone" />
    private String clinicinstitutedepartment = ""; // <bean:write name="IlvReportForm" property="clinicinstitutedepartment" />
    private String authorofessay = ""; // <bean:write name="IlvReportForm" property="authorofessay" />
    private String journaltitel = ""; // <bean:write name="IlvReportForm" property="journaltitel" />
    private String notesfromrequestinglibrary = ""; // <bean:write name="IlvReportForm" property="notesfromrequestinglibrary" />
    private String post = ""; // <bean:write name="IlvReportForm" property="post" />
    private Long lid = new Long(0);
    
    // E-Mail fields
    private String to = ""; // <bean:write name="IlvReportForm" property="subject" /> 
    private String subject = ""; // <bean:write name="IlvReportForm" property="subject" />
    private String mailtext = ""; // <bean:write name="IlvReportForm" property="mailtext" />

    private String reporttitle = "";
    private String labelfrom = "";
    private String labelto = "";
    private String labelsignatur = "";
    private String labeljournaltitel = "";
    private String labelcustomer = "";
    private String labelname = "";
    private String labellibrarycard = "";
    private String labelissn = "";
    private String labelpmid = "";
    private String labelyear = "";
    private String labelvolumevintage = "";
    private String labelbooklet = "";
    private String labelclinicinstitutedepartment = "";
    private String labelphone = "";
    private String labelfax = "";
    private String labelsendto   = "";
    private String labelpages = "";
    private String labelauthorofessay = "";
    private String labeltitleofessay = "";
    private String labelendorsementsofdeliveringlibrary = "";
    private String labelnotesfromrequestinglibrary = "";



    public int getIlvformnr() {
        return ilvformnr;
    }
    public void setIlvformnr(int ilvformnr) {
        this.ilvformnr = ilvformnr;
    }
    public String getTitleofessay() {
        return titleofessay;
    }
    public void setTitleofessay(final String titleofessay) {
        this.titleofessay = titleofessay;
    }
    public String getLieferant() {
        return lieferant;
    }
    public void setLieferant(final String lieferant) {
        this.lieferant = lieferant;
    }
    public String getSignatur() {
        return signatur;
    }
    public void setSignatur(final String signatur) {
        this.signatur = signatur;
    }
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getLibrarycard() {
        return librarycard;
    }
    public void setLibrarycard(final String librarycard) {
        this.librarycard = librarycard;
    }
    public String getIssn() {
        return issn;
    }
    public void setIssn(final String issn) {
        this.issn = issn;
    }
    public String getPmid() {
        return pmid;
    }
    public void setPmid(final String pmid) {
        this.pmid = pmid;
    }
    public String getVolumevintage() {
        return volumevintage;
    }
    public void setVolumevintage(final String volumevintage) {
        this.volumevintage = volumevintage;
    }
    public String getBooklet() {
        return booklet;
    }
    public void setBooklet(final String booklet) {
        this.booklet = booklet;
    }
    public String getPages() {
        return pages;
    }
    public void setPages(final String pages) {
        this.pages = pages;
    }
    public String getAuthorofessay() {
        return authorofessay;
    }
    public void setAuthorofessay(final String authorofessay) {
        this.authorofessay = authorofessay;
    }
    public String getJournaltitel() {
        return journaltitel;
    }
    public void setJournaltitel(final String journaltitel) {
        this.journaltitel = journaltitel;
    }
    public String getNotesfromrequestinglibrary() {
        return notesfromrequestinglibrary;
    }
    public void setNotesfromrequestinglibrary(final String notesfromrequestinglibrary) {
        this.notesfromrequestinglibrary = notesfromrequestinglibrary;
    }
    public String getYear() {
        return year;
    }
    public void setYear(final String year) {
        this.year = year;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(final String phone) {
        this.phone = phone;
    }
    public String getClinicinstitutedepartment() {
        return clinicinstitutedepartment;
    }
    public void setClinicinstitutedepartment(final String clinicinstitutedepartment) {
        this.clinicinstitutedepartment = clinicinstitutedepartment;
    }
    public String getPost() {
        return post;
    }
    public void setPost(final String post) {
        this.post = post;        
    }
    public Long getLid() {
		return lid;
	}
	public void setLid(Long lid) {
		this.lid = lid;
	}
	public String getReporttitle() {
        return reporttitle;
    }
    public void setReporttitle(final String reporttitle) {
        this.reporttitle = reporttitle;
    }    
    public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailtext() {
		return mailtext;
	}
	public void setMailtext(String mailtext) {
		this.mailtext = mailtext;
	}
	public String getLabelfrom() {
        return labelfrom;
    }
    public void setLabelfrom(final String labelfrom) {
        this.labelfrom = labelfrom;
    }
    public String getLabelto() {
        return labelto;
    }
    public void setLabelto(final String labelto) {
        this.labelto = labelto;
    }
    public String getLabelsignatur() {
        return labelsignatur;
    }
    public void setLabelsignatur(final String labelsignatur) {
        this.labelsignatur = labelsignatur;
    }
    public String getLabeljournaltitel() {
        return labeljournaltitel;
    }
    public void setLabeljournaltitel(final String labeljournaltitel) {
        this.labeljournaltitel = labeljournaltitel;
    }
    public String getLabelcustomer() {
        return labelcustomer;
    }
    public void setLabelcustomer(final String labelcustomer) {
        this.labelcustomer = labelcustomer;
    }
    public String getLabelname() {
        return labelname;
    }
    public void setLabelname(final String labelname) {
        this.labelname = labelname;
    }
    public String getLabellibrarycard() {
        return labellibrarycard;
    }
    public void setLabellibrarycard(final String labellibrarycard) {
        this.labellibrarycard = labellibrarycard;
    }
    public String getLabelissn() {
        return labelissn;
    }
    public void setLabelissn(final String labelissn) {
        this.labelissn = labelissn;
    }
    public String getLabelpmid() {
        return labelpmid;
    }
    public void setLabelpmid(final String labelpmid) {
        this.labelpmid = labelpmid;
    }
    public String getLabelyear() {
        return labelyear;
    }
    public void setLabelyear(final String labelyear) {
        this.labelyear = labelyear;
    }
    public String getLabelvolumevintage() {
        return labelvolumevintage;
    }
    public void setLabelvolumevintage(final String labelvolumevintage) {
        this.labelvolumevintage = labelvolumevintage;
    }
    public String getLabelbooklet() {
        return labelbooklet;
    }
    public void setLabelbooklet(final String labelbooklet) {
        this.labelbooklet = labelbooklet;
    }
    public String getLabelclinicinstitutedepartment() {
        return labelclinicinstitutedepartment;
    }
    public void setLabelclinicinstitutedepartment(
            final String labelclinicinstitutedepartment) {
        this.labelclinicinstitutedepartment = labelclinicinstitutedepartment;
    }
    public String getLabelphone() {
        return labelphone;
    }
    public void setLabelphone(final String labelphone) {
        this.labelphone = labelphone;
    }
    public String getLabelfax() {
        return labelfax;
    }
    public void setLabelfax(final String labelfax) {
        this.labelfax = labelfax;
    }
    public String getLabelsendto() {
        return labelsendto;
    }
    public void setLabelsendto(final String labelsendto) {
        this.labelsendto = labelsendto;
    }
    public String getLabelpages() {
        return labelpages;
    }
    public void setLabelpages(final String labelpages) {
        this.labelpages = labelpages;
    }
    public String getLabelauthorofessay() {
        return labelauthorofessay;
    }
    public void setLabelauthorofessay(final String labelauthorofessay) {
        this.labelauthorofessay = labelauthorofessay;
    }
    public String getLabeltitleofessay() {
        return labeltitleofessay;
    }
    public void setLabeltitleofessay(final String labeltitleofessay) {
        this.labeltitleofessay = labeltitleofessay;
    }
    public String getLabelendorsementsofdeliveringlibrary() {
        return labelendorsementsofdeliveringlibrary;
    }
    public void setLabelendorsementsofdeliveringlibrary(
            final String labelendorsementsofdeliveringlibrary) {
        this.labelendorsementsofdeliveringlibrary = labelendorsementsofdeliveringlibrary;
    }
    public String getLabelnotesfromrequestinglibrary() {
        return labelnotesfromrequestinglibrary;
    }
    public void setLabelnotesfromrequestinglibrary(
            final String labelnotesfromrequestinglibrary) {
        this.labelnotesfromrequestinglibrary = labelnotesfromrequestinglibrary;
    }

}
