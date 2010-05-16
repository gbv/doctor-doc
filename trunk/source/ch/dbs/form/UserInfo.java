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

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Countries;
import ch.dbs.entity.Konto;

public final class UserInfo extends ValidatorForm{

	private static final long serialVersionUID = 1L;
	private AbstractBenutzer benutzer;
    private Konto konto;
    private List<Konto> kontos;
    private ArrayList<SearchesForm> searches; // wird für die Sortierung bei der Suche benötigt
    private List<Countries> countries;
    private int kontoanz; // Anzahl Kontos in welchen der Benutzer berechtigungen hat
    private boolean keepordervalues; // Kontrollvariable falls Orderangaben beim Benutzererstellen mitgeschickt werden sollen
    private boolean keepordervalues2; // 2. Kontrollvariable falls Orderangaben beim Benutzererstellen mitgeschickt werden sollen
    private Long defaultstandortid = Long.valueOf(0); // darf nicht null sein!    
    private String biblioname;
	
    
    public UserInfo(){
        
    }
    
    public int getKontoanz() {
		return kontoanz;
	}

	public void setKontoanz(int kontoanz) {
		this.kontoanz = kontoanz;
	}

	public UserInfo(AbstractBenutzer u, Konto k){
        benutzer = u;
        konto = k;
    }

    public AbstractBenutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(AbstractBenutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
        biblioname = konto.getBibliotheksname();
    }

    public List<Konto> getKontos() {
        return kontos;
    }

    public void setKontos(List<Konto> kontos) {
        this.kontos = kontos;
    }

	public ArrayList<SearchesForm> getSearches() {
		return searches;
	}

	public void setSearches(ArrayList<SearchesForm> searches) {
		this.searches = searches;
	}

	public String getBiblioname() {
        return biblioname;
    }

    public void setBiblioname(String biblioname) {
        this.biblioname = biblioname;
    }

	public List<Countries> getCountries() {
		return countries;
	}

	public void setCountries(List<Countries> countries) {
		this.countries = countries;
	}

	public boolean isKeepordervalues() {
		return keepordervalues;
	}

	public void setKeepordervalues(boolean keepordervalues) {
		this.keepordervalues = keepordervalues;
	}

	public boolean isKeepordervalues2() {
		return keepordervalues2;
	}

	public void setKeepordervalues2(boolean keepordervalues2) {
		this.keepordervalues2 = keepordervalues2;
	}

	public Long getDefaultstandortid() {
		return defaultstandortid;
	}

	public void setDefaultstandortid(Long defaultstandortid) {
		this.defaultstandortid = defaultstandortid;
	}

}
