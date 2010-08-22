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


/**
 * @author Markus Fischer
 */
public class GbvSruForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    //AT == @
    //* == wiederholbar/multipel

    private int treffer_total; // gibt bei jedem SRU-Objekt das Total aller Treffer an
    private int start_record; // Anzeige ab Treffer x
    private int maximum_record; // Anzeige maximale Anzahl Treffer (z.B. 10 aufs Mal)
    private int record_number; // Nummer des aktuellen Treffers

    // codierte Angaben
    private String ppn_003AT;
    private String typ_002AT; // Bibliographische Gattung und Status
    private String erscheinungsjahr_011AT;
    private String materialbenennung_allg_016H;
    private String sprache_010AT;
    private String erscheinungsland_019AT;
    // Standardnummern
    private String isbn_004A_multipel; // *
    private String issn_005A_multipel; // *
    private String ismn_004F;
    //  private String bnb_num_006B_multipel; // * British National Bibliography (BNB)
    private String loc_num_006A;
    private String doi_004V_multipel; // *
    private String dnb_num_006G;
    //  private String wv_num_db_006U_multipel; // * Wöchentliche Verzeichnisnummer der Deutschen Nationalbibliothek
    private String cip_num_db_006T_multipel; // * CIP-Nummer der Deutschen Nationalbibliothek
    private String zdbid_006Z_multipel; // *
    private String ean_004L_multipel; // *
    private String hochschulschriften_num_007E;
    // Personennamen
    private String verfasser_erster_028A;
    private String verfasser_zweiter_und_weitere_028B;
    private String interpreten_028E_multipel; // *
    private String gefeierte_personen_028F;
    // Körperschaften / Kongresse
    private String koerperschaft_erste_029A;
    private String koerperschaft_zweite_029F;
    private String koerperschaft_als_interpret_029E_multipel; // *
    private String koerperschaft_sonstige_029K_multipel; // *
    private String kongresse_030F;
    // Hauptsachtitel
    private String hauptsachtitel_021A; // Hauptsachtitel, Zusätze, Parallelsachtitel, Verfasserangabe
    private String hauptsachtitel_alternativ_021B; // Hauptsachtitel, Zusätze, Parallelsachtitel, Verfasserangabe
    private String unterreihe_021C_multipel; // *
    private String rezensierteswerk_021G_multipel;
    private String zusatzwerk_021M_multipel; // * Titel des ersten auf der Haupttitelseite genannten beigefügten Werkes
    private String zusaetze_und_verfasser_021N; // Zusätze und Verfasserangabe zur gesamten Vorlage
    private String band_021V_multipel; // * Titel eines unselbständigen Teils / Bandes
    // Veröffentlichungsvermerk, Umfang, Beigaben
    private String ausgabe_032AT;
    private String reprintvermerk_032B;
    private String erscheinungsverlauf_normiert_031N;
    private String erscheinungsverlauf_031AT;
    private String umfang_031A;
    private String ort_verlag_033A_multipel; // *
    private String materialbenennung_spezifisch_034D; // Umfangsangabe, spezifische Materialbenennung, techn. System
    private String illustration_034M; // Illustrationsangabe, sonstige physische und technische Angaben
    private String format_034I; // Format, Maßangaben
    private String begleitmaterial_034K;
    // übergeordnete Gesamtheiten
    private String gesamtheit_abteilungen_vorlage_036C; // Gesamtheit und Abteilungen in Vorlageform
    private String gesamtheit_ansetzungsform_036D; // Gesamtheit in Ansetzungsform
    private String gesamtheit_vorlage_036E_multipel; // * Gesamtheit in Vorlageform (Serie)
    private String gesamtheit_ansetzungsform_036F_multipel; // * Gesamtheit in Ansetzungsform (Serie)
    private String bandzaehlung;
    // Fussnoten
    private String fussnoten_unaufgegliedert_037A_multipel; // *
    private String hochschulschriftenvermerk_037C_multipel; // *
    private String enthaltene_werke_046Q; // Hinweise auf unselbstständig enthaltene Werke
    // Verknüpfungen
    private String link_009P_multiple;
    private String verknuepfung_groessere_einheit_039B_multipel; // *
    private String verknuepfung_kleinere_einheit_039C_multipel; // *
    private String verknuepfung_horizontal_039D_multipel; // *
    private int anzahl_039D = 0; // Kontroller für die Anzahl Verknüpfungen
    private String verknuepfung_zdbid_horizontal;
    private String verknuepfung_zdbid_groesser;
    private String verknuepfung_ppn_horizontal;
    private String verknuepfung_ppn_groesser;



    public GbvSruForm() {

    }


    public int getTreffer_total() {
        return treffer_total;
    }



    public void setTreffer_total(int treffer_total) {
        this.treffer_total = treffer_total;
    }



    public String getPpn_003AT() {
        return ppn_003AT;
    }



    public void setPpn_003AT(String ppn_003AT) {
        this.ppn_003AT = ppn_003AT;
    }


    public String getTyp_002AT() {
        return typ_002AT;
    }



    public void setTyp_002AT(String typ_002AT) {
        this.typ_002AT = typ_002AT;
    }



    public String getErscheinungsjahr_011AT() {
        return erscheinungsjahr_011AT;
    }



    public void setErscheinungsjahr_011AT(String erscheinungsjahr_011AT) {
        this.erscheinungsjahr_011AT = erscheinungsjahr_011AT;
    }



    public String getMaterialbenennung_allg_016H() {
        return materialbenennung_allg_016H;
    }



    public void setMaterialbenennung_allg_016H(String materialbenennung_allg_016H) {
        this.materialbenennung_allg_016H = materialbenennung_allg_016H;
    }



    public String getSprache_010AT() {
        return sprache_010AT;
    }



    public void setSprache_010AT(String sprache_010AT) {
        this.sprache_010AT = sprache_010AT;
    }



    public String getErscheinungsland_019AT() {
        return erscheinungsland_019AT;
    }



    public void setErscheinungsland_019AT(String erscheinungsland_019AT) {
        this.erscheinungsland_019AT = erscheinungsland_019AT;
    }



    public String getIsbn_004A_multipel() {
        return isbn_004A_multipel;
    }



    public void setIsbn_004A_multipel(String isbn_004A_multipel) {
        this.isbn_004A_multipel = isbn_004A_multipel;
    }



    public String getIssn_005A_multipel() {
        return issn_005A_multipel;
    }



    public void setIssn_005A_multipel(String issn_005A_multipel) {
        this.issn_005A_multipel = issn_005A_multipel;
    }



    public String getIsmn_004F() {
        return ismn_004F;
    }



    public void setIsmn_004F(String ismn_004F) {
        this.ismn_004F = ismn_004F;
    }



    public String getLoc_num_006A() {
        return loc_num_006A;
    }



    public void setLoc_num_006A(String loc_num_006A) {
        this.loc_num_006A = loc_num_006A;
    }



    public String getDoi_004V_multipel() {
        return doi_004V_multipel;
    }



    public void setDoi_004V_multipel(String doi_004V_multipel) {
        this.doi_004V_multipel = doi_004V_multipel;
    }



    public String getDnb_num_006G() {
        return dnb_num_006G;
    }



    public void setDnb_num_006G(String dnb_num_006G) {
        this.dnb_num_006G = dnb_num_006G;
    }



    public String getCip_num_db_006T_multipel() {
        return cip_num_db_006T_multipel;
    }



    public void setCip_num_db_006T_multipel(String cip_num_db_006T_multipel) {
        this.cip_num_db_006T_multipel = cip_num_db_006T_multipel;
    }



    public String getZdbid_006Z_multipel() {
        return zdbid_006Z_multipel;
    }



    public void setZdbid_006Z_multipel(String zdbid_006Z_multipel) {
        this.zdbid_006Z_multipel = zdbid_006Z_multipel;
    }



    public String getEan_004L_multipel() {
        return ean_004L_multipel;
    }



    public void setEan_004L_multipel(String ean_004L_multipel) {
        this.ean_004L_multipel = ean_004L_multipel;
    }



    public String getHochschulschriften_num_007E() {
        return hochschulschriften_num_007E;
    }



    public void setHochschulschriften_num_007E(String hochschulschriften_num_007E) {
        this.hochschulschriften_num_007E = hochschulschriften_num_007E;
    }



    public String getVerfasser_erster_028A() {
        return verfasser_erster_028A;
    }



    public void setVerfasser_erster_028A(String verfasser_erster_028A) {
        this.verfasser_erster_028A = verfasser_erster_028A;
    }



    public String getVerfasser_zweiter_und_weitere_028B() {
        return verfasser_zweiter_und_weitere_028B;
    }



    public void setVerfasser_zweiter_und_weitere_028B(
            String verfasser_zweiter_und_weitere_028B) {
        this.verfasser_zweiter_und_weitere_028B = verfasser_zweiter_und_weitere_028B;
    }



    public String getInterpreten_028E_multipel() {
        return interpreten_028E_multipel;
    }



    public void setInterpreten_028E_multipel(String interpreten_028E_multipel) {
        this.interpreten_028E_multipel = interpreten_028E_multipel;
    }



    public String getGefeierte_personen_028F() {
        return gefeierte_personen_028F;
    }



    public void setGefeierte_personen_028F(String gefeierte_personen_028F) {
        this.gefeierte_personen_028F = gefeierte_personen_028F;
    }



    public String getKoerperschaft_erste_029A() {
        return koerperschaft_erste_029A;
    }



    public void setKoerperschaft_erste_029A(String koerperschaft_erste_029A) {
        this.koerperschaft_erste_029A = koerperschaft_erste_029A;
    }



    public String getKoerperschaft_zweite_029F() {
        return koerperschaft_zweite_029F;
    }



    public void setKoerperschaft_zweite_029F(String koerperschaft_zweite_029F) {
        this.koerperschaft_zweite_029F = koerperschaft_zweite_029F;
    }



    public String getKoerperschaft_als_interpret_029E_multipel() {
        return koerperschaft_als_interpret_029E_multipel;
    }



    public void setKoerperschaft_als_interpret_029E_multipel(
            String koerperschaft_als_interpret_029E_multipel) {
        this.koerperschaft_als_interpret_029E_multipel = koerperschaft_als_interpret_029E_multipel;
    }



    public String getKoerperschaft_sonstige_029K_multipel() {
        return koerperschaft_sonstige_029K_multipel;
    }



    public void setKoerperschaft_sonstige_029K_multipel(
            String koerperschaft_sonstige_029K_multipel) {
        this.koerperschaft_sonstige_029K_multipel = koerperschaft_sonstige_029K_multipel;
    }



    public String getKongresse_030F() {
        return kongresse_030F;
    }



    public void setKongresse_030F(String kongresse_030F) {
        this.kongresse_030F = kongresse_030F;
    }



    public String getHauptsachtitel_021A() {
        return hauptsachtitel_021A;
    }



    public void setHauptsachtitel_021A(String hauptsachtitel_021A) {
        this.hauptsachtitel_021A = hauptsachtitel_021A;
    }



    public String getHauptsachtitel_alternativ_021B() {
        return hauptsachtitel_alternativ_021B;
    }



    public void setHauptsachtitel_alternativ_021B(
            String hauptsachtitel_alternativ_021B) {
        this.hauptsachtitel_alternativ_021B = hauptsachtitel_alternativ_021B;
    }



    public String getUnterreihe_021C_multipel() {
        return unterreihe_021C_multipel;
    }



    public void setUnterreihe_021C_multipel(String unterreihe_021C_multipel) {
        this.unterreihe_021C_multipel = unterreihe_021C_multipel;
    }



    public String getRezensierteswerk_021G_multipel() {
        return rezensierteswerk_021G_multipel;
    }


    public void setRezensierteswerk_021G_multipel(
            String rezensierteswerk_021G_multipel) {
        this.rezensierteswerk_021G_multipel = rezensierteswerk_021G_multipel;
    }


    public String getZusatzwerk_021M_multipel() {
        return zusatzwerk_021M_multipel;
    }



    public void setZusatzwerk_021M_multipel(String zusatzwerk_021M_multipel) {
        this.zusatzwerk_021M_multipel = zusatzwerk_021M_multipel;
    }



    public String getZusaetze_und_verfasser_021N() {
        return zusaetze_und_verfasser_021N;
    }



    public void setZusaetze_und_verfasser_021N(String zusaetze_und_verfasser_021N) {
        this.zusaetze_und_verfasser_021N = zusaetze_und_verfasser_021N;
    }



    public String getBand_021V_multipel() {
        return band_021V_multipel;
    }



    public void setBand_021V_multipel(String band_021V_multipel) {
        this.band_021V_multipel = band_021V_multipel;
    }



    public String getAusgabe_032AT() {
        return ausgabe_032AT;
    }



    public void setAusgabe_032AT(String ausgabe_032AT) {
        this.ausgabe_032AT = ausgabe_032AT;
    }



    public String getReprintvermerk_032B() {
        return reprintvermerk_032B;
    }



    public void setReprintvermerk_032B(String reprintvermerk_032B) {
        this.reprintvermerk_032B = reprintvermerk_032B;
    }



    public String getErscheinungsverlauf_normiert_031N() {
        return erscheinungsverlauf_normiert_031N;
    }



    public void setErscheinungsverlauf_normiert_031N(
            String erscheinungsverlauf_normiert_031N) {
        this.erscheinungsverlauf_normiert_031N = erscheinungsverlauf_normiert_031N;
    }


    public String getUmfang_031A() {
        return umfang_031A;
    }


    public void setUmfang_031A(String umfang_031A) {
        this.umfang_031A = umfang_031A;
    }


    public String getErscheinungsverlauf_031AT() {
        return erscheinungsverlauf_031AT;
    }



    public void setErscheinungsverlauf_031AT(String erscheinungsverlauf_031AT) {
        this.erscheinungsverlauf_031AT = erscheinungsverlauf_031AT;
    }



    public String getOrt_verlag_033A_multipel() {
        return ort_verlag_033A_multipel;
    }



    public void setOrt_verlag_033A_multipel(String ort_verlag_033A_multipel) {
        this.ort_verlag_033A_multipel = ort_verlag_033A_multipel;
    }



    public String getMaterialbenennung_spezifisch_034D() {
        return materialbenennung_spezifisch_034D;
    }



    public void setMaterialbenennung_spezifisch_034D(
            String materialbenennung_spezifisch_034D) {
        this.materialbenennung_spezifisch_034D = materialbenennung_spezifisch_034D;
    }



    public String getIllustration_034M() {
        return illustration_034M;
    }



    public void setIllustration_034M(String illustration_034M) {
        this.illustration_034M = illustration_034M;
    }



    public String getFormat_034I() {
        return format_034I;
    }



    public void setFormat_034I(String format_034I) {
        this.format_034I = format_034I;
    }



    public String getBegleitmaterial_034K() {
        return begleitmaterial_034K;
    }



    public void setBegleitmaterial_034K(String begleitmaterial_034K) {
        this.begleitmaterial_034K = begleitmaterial_034K;
    }



    public String getGesamtheit_abteilungen_vorlage_036C() {
        return gesamtheit_abteilungen_vorlage_036C;
    }



    public void setGesamtheit_abteilungen_vorlage_036C(
            String gesamtheit_abteilungen_vorlage_036C) {
        this.gesamtheit_abteilungen_vorlage_036C = gesamtheit_abteilungen_vorlage_036C;
    }



    public String getGesamtheit_ansetzungsform_036D() {
        return gesamtheit_ansetzungsform_036D;
    }



    public void setGesamtheit_ansetzungsform_036D(
            String gesamtheit_ansetzungsform_036D) {
        this.gesamtheit_ansetzungsform_036D = gesamtheit_ansetzungsform_036D;
    }



    public String getGesamtheit_vorlage_036E_multipel() {
        return gesamtheit_vorlage_036E_multipel;
    }



    public void setGesamtheit_vorlage_036E_multipel(
            String gesamtheit_vorlage_036E_multipel) {
        this.gesamtheit_vorlage_036E_multipel = gesamtheit_vorlage_036E_multipel;
    }



    public String getGesamtheit_ansetzungsform_036F_multipel() {
        return gesamtheit_ansetzungsform_036F_multipel;
    }



    public void setGesamtheit_ansetzungsform_036F_multipel(
            String gesamtheit_ansetzungsform_036F_multipel) {
        this.gesamtheit_ansetzungsform_036F_multipel = gesamtheit_ansetzungsform_036F_multipel;
    }



    public String getFussnoten_unaufgegliedert_037A_multipel() {
        return fussnoten_unaufgegliedert_037A_multipel;
    }



    public void setFussnoten_unaufgegliedert_037A_multipel(
            String fussnoten_unaufgegliedert_037A_multipel) {
        this.fussnoten_unaufgegliedert_037A_multipel = fussnoten_unaufgegliedert_037A_multipel;
    }



    public String getHochschulschriftenvermerk_037C_multipel() {
        return hochschulschriftenvermerk_037C_multipel;
    }



    public void setHochschulschriftenvermerk_037C_multipel(
            String hochschulschriftenvermerk_037C_multipel) {
        this.hochschulschriftenvermerk_037C_multipel = hochschulschriftenvermerk_037C_multipel;
    }



    public String getEnthaltene_werke_046Q() {
        return enthaltene_werke_046Q;
    }



    public void setEnthaltene_werke_046Q(String enthaltene_werke_046Q) {
        this.enthaltene_werke_046Q = enthaltene_werke_046Q;
    }



    public String getVerknuepfung_groessere_einheit_039B_multipel() {
        return verknuepfung_groessere_einheit_039B_multipel;
    }



    public void setVerknuepfung_groessere_einheit_039B_multipel(
            String verknuepfung_groessere_einheit_039B_multipel) {
        this.verknuepfung_groessere_einheit_039B_multipel = verknuepfung_groessere_einheit_039B_multipel;
    }



    public String getVerknuepfung_kleinere_einheit_039C_multipel() {
        return verknuepfung_kleinere_einheit_039C_multipel;
    }



    public void setVerknuepfung_kleinere_einheit_039C_multipel(
            String verknuepfung_kleinere_einheit_039C_multipel) {
        this.verknuepfung_kleinere_einheit_039C_multipel = verknuepfung_kleinere_einheit_039C_multipel;
    }



    public String getVerknuepfung_horizontal_039D_multipel() {
        return verknuepfung_horizontal_039D_multipel;
    }



    public void setVerknuepfung_horizontal_039D_multipel(
            String verknuepfung_horizontal_039D_multipel) {
        this.verknuepfung_horizontal_039D_multipel = verknuepfung_horizontal_039D_multipel;
    }

    public String getVerknuepfung_zdbid_horizontal() {
        return verknuepfung_zdbid_horizontal;
    }


    public void setVerknuepfung_zdbid_horizontal(
            String verknuepfung_zdbid_horizontal) {
        this.verknuepfung_zdbid_horizontal = verknuepfung_zdbid_horizontal;
    }


    public String getVerknuepfung_ppn_groesser() {
        return verknuepfung_ppn_groesser;
    }


    public void setVerknuepfung_ppn_groesser(String verknuepfung_ppn_groesser) {
        this.verknuepfung_ppn_groesser = verknuepfung_ppn_groesser;
    }

    public String getVerknuepfung_zdbid_groesser() {
        return verknuepfung_zdbid_groesser;
    }


    public void setVerknuepfung_zdbid_groesser(String verknuepfung_zdbid_groesser) {
        this.verknuepfung_zdbid_groesser = verknuepfung_zdbid_groesser;
    }

    public String getVerknuepfung_ppn_horizontal() {
        return verknuepfung_ppn_horizontal;
    }


    public void setVerknuepfung_ppn_horizontal(String verknuepfung_ppn_horizontal) {
        this.verknuepfung_ppn_horizontal = verknuepfung_ppn_horizontal;
    }

    public String getLink_009P_multiple() {
        return link_009P_multiple;
    }


    public void setLink_009P_multiple(String link_009P_multiple) {
        this.link_009P_multiple = link_009P_multiple;
    }

    public String getBandzaehlung() {
        return bandzaehlung;
    }


    public void setBandzaehlung(String bandzaehlung) {
        this.bandzaehlung = bandzaehlung;
    }

    public int getStart_record() {
        return start_record;
    }

    public void setStart_record(int start_record) {
        this.start_record = start_record;
    }


    public int getMaximum_record() {
        return maximum_record;
    }


    public void setMaximum_record(int maximum_record) {
        this.maximum_record = maximum_record;
    }

    public int getRecord_number() {
        return record_number;
    }


    public void setRecord_number(int record_number) {
        this.record_number = record_number;
    }


    public int getAnzahl_039D() {
        return anzahl_039D;
    }


    public void setAnzahl_039D(int anzahl_039D) {
        this.anzahl_039D = anzahl_039D;
    }





}
