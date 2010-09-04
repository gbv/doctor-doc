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

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Bestellungen;
import ch.dbs.entity.Countries;

public final class UserForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;
    private int kid;
    private List<Bestellungen> bestellungen;
    private List<AbstractBenutzer> users;
    private String[] kontos;
    private String anrede;
    private String vorname;
    private String name;
    private String email;
    private String telefonnrg;
    private String telefonnrp;
    private String institut;
    private String abteilung;
    private String adresse;
    private String adresszusatz;
    private String plz;
    private String ort;
    private String land;
    private List<Countries> countries;
    private String countryid;
    private String password;
    private String method;
    private String submit;
    private Long uid;
    private boolean loginopt;
    private boolean userbestellung; // darf bei SUBITO bestellen
    private boolean gbvbestellung; // darf bei GBV bestellen
    private boolean kontostatus;
    private boolean kontovalidation;
    private boolean validation;
    private Long billing;
    private String gtc;
    private String gtcdate;

    private transient AbstractBenutzer user;

    private Long bid;
    private boolean delete;
    // dient dazu die richtige Überschrift in modifykontousere.jsp anzuziehen...
    private boolean addFromBestellformEmail;
    // Kontrollvariable falls Orderangaben beim Benutzererstellen mitgeschickt werden sollen
    private boolean keepordervalues;
    // 2. Kontrollvariable falls Orderangaben beim Benutzererstellen mitgeschickt werden sollen
    private boolean keepordervalues2;

    public UserForm() {

    }

    public UserForm(final LoginForm lf) {

        if (lf.getKundeninstitution() != null) { institut = lf.getKundeninstitution().trim(); }
        if (lf.getKundenabteilung() != null) { abteilung = lf.getKundenabteilung().trim(); }
        if (lf.getKundenvorname() != null) { vorname = lf.getKundenvorname().trim(); }
        if (lf.getKundenname() != null) { name = lf.getKundenname().trim(); }
        if (lf.getKundenadresse() != null) { adresse = lf.getKundenadresse().trim(); }
        if (lf.getKundentelefon() != null) { telefonnrg = lf.getKundentelefon().trim(); }
        if (lf.getKundenplz() != null) { plz = lf.getKundenplz().trim(); }
        if (lf.getKundenort() != null) { ort = lf.getKundenort().trim(); }
        if (lf.getKundenland() != null) { land = lf.getKundenland().trim(); }
        if (lf.getKundenemail() != null) { email = lf.getKundenemail().trim(); }
    }

    public UserForm(final KontoForm kf) {

        if (kf.getKundeninstitution() != null) { institut = kf.getKundeninstitution().trim(); }
        if (kf.getKundenabteilung() != null) { abteilung = kf.getKundenabteilung().trim(); }
        if (kf.getKundenvorname() != null) { vorname = kf.getKundenvorname().trim(); }
        if (kf.getKundenname() != null) { name = kf.getKundenname().trim(); }
        if (kf.getKundenadresse() != null) { adresse = kf.getKundenadresse().trim(); }
        if (kf.getKundentelefon() != null) { telefonnrg = kf.getKundentelefon().trim(); }
        if (kf.getKundenplz() != null) { plz = kf.getKundenplz().trim(); }
        if (kf.getKundenort() != null) { ort = kf.getKundenort().trim(); }
        if (kf.getKundenland() != null) { land = kf.getKundenland().trim(); }
        if (kf.getKundenemail() != null) { email = kf.getKundenemail().trim(); }
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(final Long uid) {
        this.uid = uid;
    }

    public List<Bestellungen> getBestellungen() {
        return bestellungen;
    }


    public void setBestellungen(final List<Bestellungen> bestellungen) {
        this.bestellungen = bestellungen;
    }

    public String[] getKontos() {
        // defensive copying
        return kontos.clone();
    }


    public void setKontos(final String[] kontos) {
        // defensive copying
        this.kontos = kontos.clone();
    }


    public int getKid() {
        return kid;
    }


    public void setKid(final int kid) {
        this.kid = kid;
    }


    public List<AbstractBenutzer> getUsers() {
        return users;
    }


    public void setUsers(final List<AbstractBenutzer> users) {
        this.users = users;
    }


    public String getAbteilung() {
        return abteilung;
    }


    public void setAbteilung(final String abteilung) {
        this.abteilung = abteilung;
    }


    public String getAdresse() {
        return adresse;
    }


    public void setAdresse(final String adresse) {
        this.adresse = adresse;
    }


    public String getAdresszusatz() {
        return adresszusatz;
    }


    public void setAdresszusatz(final String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }


    public String getAnrede() {
        return anrede;
    }


    public void setAnrede(final String anrede) {
        this.anrede = anrede;
    }


    public Long getBilling() {
        return billing;
    }


    public void setBilling(final Long billing) {
        this.billing = billing;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


    public String getInstitut() {
        return institut;
    }


    public void setInstitut(final String institut) {
        this.institut = institut;
    }


    public boolean isKontovalidation() {
        return kontovalidation;
    }


    public void setKontovalidation(final boolean kontovalidation) {
        this.kontovalidation = kontovalidation;
    }


    public String getLand() {
        return land;
    }


    public void setLand(final String land) {
        this.land = land;
    }


    public boolean isLoginopt() {
        return loginopt;
    }


    public void setLoginopt(final boolean loginopt) {
        this.loginopt = loginopt;
    }


    public String getName() {
        return name;
    }


    public void setName(final String name) {
        this.name = name;
    }


    public String getOrt() {
        return ort;
    }


    public void setOrt(final String ort) {
        this.ort = ort;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(final String password) {
        this.password = password;
    }


    public String getPlz() {
        return plz;
    }


    public void setPlz(final String plz) {
        this.plz = plz;
    }


    public String getTelefonnrg() {
        return telefonnrg;
    }


    public void setTelefonnrg(final String telefonnrg) {
        this.telefonnrg = telefonnrg;
    }


    public String getTelefonnrp() {
        return telefonnrp;
    }


    public void setTelefonnrp(final String telefonnrp) {
        this.telefonnrp = telefonnrp;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(final String submit) {
        this.submit = submit;
    }

    public boolean isUserbestellung() {
        return userbestellung;
    }

    public void setUserbestellung(final boolean userbestellung) {
        this.userbestellung = userbestellung;
    }

    public boolean isGbvbestellung() {
        return gbvbestellung;
    }

    public void setGbvbestellung(final boolean gbvbestellung) {
        this.gbvbestellung = gbvbestellung;
    }

    public boolean isValidation() {
        return validation;
    }


    public void setValidation(final boolean validation) {
        this.validation = validation;
    }


    public String getVorname() {
        return vorname;
    }


    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }


    public boolean isKontostatus() {
        return kontostatus;
    }


    public void setKontostatus(final boolean kontostatus) {
        this.kontostatus = kontostatus;
    }

    public Long getBid() {
        return bid;
    }


    public void setBid(final Long bid) {
        this.bid = bid;
    }


    public String getMethod() {
        return method;
    }


    public void setMethod(final String method) {
        this.method = method;
    }


    public AbstractBenutzer getUser() {
        return user;
    }


    public void setUser(final AbstractBenutzer user) {
        this.user = user;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(final boolean delete) {
        this.delete = delete;
    }

    public List<Countries> getCountries() {
        return countries;
    }

    public void setCountries(final List<Countries> countries) {
        this.countries = countries;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(final String countryid) {
        this.countryid = countryid;
    }

    public boolean isKeepordervalues() {
        return keepordervalues;
    }

    public void setKeepordervalues(final boolean keepordervalues) {
        this.keepordervalues = keepordervalues;
    }

    public boolean isKeepordervalues2() {
        return keepordervalues2;
    }

    public void setKeepordervalues2(final boolean keepordervalues2) {
        this.keepordervalues2 = keepordervalues2;
    }

    public String getGtc() {
        return gtc;
    }

    public void setGtc(final String gtc) {
        this.gtc = gtc;
    }

    public String getGtcdate() {
        return gtcdate;
    }

    public void setGtcdate(final String gtcdate) {
        this.gtcdate = gtcdate;
    }

    public boolean isAddFromBestellformEmail() {
        return addFromBestellformEmail;
    }

    public void setAddFromBestellformEmail(final boolean addFromBestellformEmail) {
        this.addFromBestellformEmail = addFromBestellformEmail;
    }

}
