//	Copyright (C) 2005 - 2010  Markus Fischer, Pascal Steiner
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	as published by the Free Software Foundation; version 2 of the License.
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//
//	Contact: info@doctor-doc.com

package ch.dbs.form;

import org.apache.struts.action.ActionForm;


public final class FaxForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	private String popfaxid;
	private String from;
	private String popfaxdate;
	private String pages;
	private String state;
	private String statedate;
	
    
    public FaxForm(){
        
    }

	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getPages() {
		return pages;
	}


	public void setPages(String pages) {
		this.pages = pages;
	}


	public String getPopfaxdate() {
		return popfaxdate;
	}


	public void setPopfaxdate(String popfaxdate) {
		this.popfaxdate = popfaxdate;
	}


	public String getPopfaxid() {
		return popfaxid;
	}


	public void setPopfaxid(String popfaxid) {
		this.popfaxid = popfaxid;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getStatedate() {
		return statedate;
	}


	public void setStatedate(String statedate) {
		this.statedate = statedate;
	}

    

	
	
    
}
