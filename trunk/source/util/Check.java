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

package util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Text;
import ch.dbs.form.OverviewForm;

public class Check {

    private static final SimpleLogger LOG = new SimpleLogger(Check.class);

    /**
     * Prüft ob der String eine E-Mail Adresse ist
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        boolean check = false;
        try {
            InternetAddress  ia = new InternetAddress();
            ia.setAddress(email);
            try {
                ia.validate(); // wirft bei grundsätzlich ungültigen Adressen eine AdressException
                // info@doctor-doc ohne .com wird noch als gültig angesehen...
                Pattern p = Pattern
                .compile("[A-Za-z0-9._-]+@[A-Za-z0-9][A-Za-z0-9.-]{0,61}[A-Za-z0-9]\\.[A-Za-z.]{2,6}");
                Matcher m = p.matcher(email);
                if (m.find()) {
                    check = true;
                }
            } catch (AddressException e1) {
                LOG.info("isEmail: " + email + "" + e1.toString());
            }
        } catch (Exception e) {
            LOG.error("isEmail: "  + email + "" + e.toString());
        }

        return check;
    }

    /**
     * Stellt sicher, dass der String nicht null ist und einen Wert der Mindestlänge l hat
     * @param s
     * @param l
     * @return
     */
    public boolean isMinLength(String s, int l) {
        boolean check = false;
        if (s != null) {
            if (s.length() >= l) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Stellt sicher, dass der String nicht null ist und einen Wert der genauen Länge l hat
     * @param s
     * @param l
     * @return
     */
    public boolean isExactLength(String s, int l) {
        boolean check = false;
        if (s != null) {
            if (s.length() == l) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Ueeberprueft, ob die Länge des Strings sich zwischen einem Minimum und einem Maximum befindet<br>
     * min <= s <= max<p>
     *
     * @param s
     * @param min
     * @param max
     * @return
     */
    public boolean isLengthInMinMax(String s, int min, int max) {
        boolean check = false;
        if (s != null) {
            if ((min <= s.length()) && (s.length() <= max)) {
                check = true;
            }
        }
        return check;
    }

    /**
     * Prüft Datumseingaben und stellt ggf. einen gültigen Datumsbereich von x Wochen zusammen
     *
     * @param of
     * @return
     */
    public OverviewForm checkDateRegion(OverviewForm of, int defaultPeriod, String defaultTimezone) {

        //Längen Sicherstellen
        if (this.isExactLength(of.getYfrom(), 4) && this.isExactLength(of.getYto(), 4)
                && this.isLengthInMinMax(of.getMfrom(), 1, 2) && this.isLengthInMinMax(of.getMto(), 1, 2)
                && this.isLengthInMinMax(of.getDfrom(), 1, 2) && this.isLengthInMinMax(of.getDto(), 1, 2)) {

            try {
                Calendar calTo = Calendar.getInstance();
                Calendar calFrom = Calendar.getInstance();
                calTo.set(Integer.parseInt(of.getYto()), Integer.parseInt(of.getMto()) - 1, Integer.parseInt(of
                        .getDto()), 0, 0, 0);
                calFrom.set(Integer.parseInt(of.getYfrom()), Integer.parseInt(of.getMfrom()) - 1, Integer.parseInt(of
                        .getDfrom()), 0, 0, 0);
                long time = calTo.getTime().getTime() - calFrom.getTime().getTime();

                if (time < 0) { // Überprüfung ob eine negativer Zeitbereicg vorliegt...
                    of.setYfrom(null); // zurückstellen, damit im nächsten Schritt die Default-Werte zum Zug kommen...
                } else {
                    //          System.out.println("Kalender wieder in of setzen, wegen Überläufen (31.2.2008)");
                    of.setYfrom(new SimpleDateFormat("yyyy").format(calFrom.getTime()));
                    of.setMfrom(new SimpleDateFormat("MM").format(calFrom.getTime()));
                    of.setDfrom(new SimpleDateFormat("dd").format(calFrom.getTime()));
                    of.setFromdate(of.getYfrom() + "-" + of.getMfrom() + "-" + of.getDfrom() + " 00:00:00");

                    of.setYto(new SimpleDateFormat("yyyy").format(calTo.getTime()));
                    of.setMto(new SimpleDateFormat("MM").format(calTo.getTime()));
                    of.setDto(new SimpleDateFormat("dd").format(calTo.getTime()));
                    of.setTodate(of.getYto() + "-" + of.getMto() + "-" + of.getDto() + " 24:00:00");
                }

            } catch (NumberFormatException e) {
                LOG.error("checkDateRegion: " + e.toString());
                of.setYfrom(null);
            }

        } else { //Benutzerangaben falsche anzahl Zeichen
            // zurückstellen, damit im nächsten Schritt die Default-Werte zum Zug kommen
            of.setYfrom(null);
        }

        // Defaultwerte bei bedarf Setzen
        if (of.getYfrom() == null || of.getYto() == null
                || of.getMfrom() == null || of.getMto() == null
                || of.getDfrom() == null || of.getDto() == null) {

            Calendar calTo = Calendar.getInstance();
            calTo.setTimeZone(TimeZone.getTimeZone(defaultTimezone));
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTimeZone(TimeZone.getTimeZone(defaultTimezone));
            calFrom.add(Calendar.MONTH, -defaultPeriod); // bessere Verständlichkeit wenn Monatsbereiche vorliegen...

            of.setYfrom(new SimpleDateFormat("yyyy").format(calFrom.getTime()));
            of.setMfrom(new SimpleDateFormat("MM").format(calFrom.getTime()));
            of.setDfrom(new SimpleDateFormat("dd").format(calFrom.getTime()));
            of.setFromdate(of.getYfrom() + "-" + of.getMfrom() + "-" + of.getDfrom() + " 00:00:00");

            of.setYto(new SimpleDateFormat("yyyy").format(calTo.getTime()));
            of.setMto(new SimpleDateFormat("MM").format(calTo.getTime()));
            of.setDto(new SimpleDateFormat("dd").format(calTo.getTime()));
            of.setTodate(of.getYto() + "-" + of.getMto() + "-" + of.getDto() + " 24:00:00");
        }

        return of;
    }

    /**
     * Ueberprueft Sortierkriterium (asc/desc), sowie ob nach gültigem Feld Sortiert wird<br>
     * Sollte dies nicht der Fall sein weird automatisch Sortierung auf asc ausser bei Sortierung nach<br>
     * orderdate nach desc
     * <p>
     * @param of OverviewForm
     * @return OverviewForm
     */
    public OverviewForm checkSortOrderValues(OverviewForm of) {

        //Check, damit nur gültige Sortierkriterien daherkommen (asc/desc)
        if (of.getSort() == null) {
            of.setSort("orderdate");
        }
        // nach dem Login Default-Sortierung nach asc ausser bei date nach desc
        if ((of.getSortorder() == null) ||  ((!of.getSortorder().equals("asc"))
                && (!of.getSortorder().equals("desc")))) {
            if (of.getSort().equals("orderdate")) {
                of.setSortorder("desc");
            } else {
                of.setSortorder("asc");
            }
        }
        return of;
    }

    /**
     * Validierung der Filterkriterien anhand erlaubter Bestellstati.<br>
     * Wenn ungueltig auf null setzen.
     * Benötigt, dass die kontoabhängigen Stati vorgängig mittels of.setStati abgelegt wurden
     * <p>
     *
     * @param of OverviewForm
     * @return of OverviewForm
     */
    public OverviewForm checkFilterCriteriasAgainstAllTextsFromTexttypPlusKontoTexts(OverviewForm of) {
        if (of.getFilter() != null) { // grundsätzlich muss ein zu prüfendes Filterkriterium vorhanden sein
            boolean validFilterCriteria = false;
            // Die kontoabhängigen Stati müssen vorgängig mittels of.setStati abgelegt werden
            if (of.getStatitexts().size() > 0) {
                for (Text status : of.getStatitexts()) {
                    if (of.getFilter().equals(status.getInhalt()) || of.getFilter().equals("offen")) {
                        validFilterCriteria = true;
                    }
                }
            }
            if (!validFilterCriteria) { of.setFilter(null); } // ungültige Filterkriterien löschen
        }

        return of;
    }

    /**
     * Sortierkriterium Bestellungen ueberpruefen auf Gueltigkeit, sonst ersetzen mit orderdate<p>
     * @param of OverviewForm
     * @return of OverviewForm
     */
    public OverviewForm checkOrdersSortCriterias(OverviewForm of) {
        if (of.getSort().equals("mail") || of.getSort().equals("artikeltitel") || of.getSort().equals("zeitschrift")
                || of.getSort().equals("inhalt") || of.getSort().equals("orderdate") || of.getSort().equals("statedate")
                || of.getSort().equals("state") || of.getSort().equals("deloptions") || of.getSort().equals("name")
                || of.getSort().equals("konto") || of.getSort().equals("bestellquelle")
                || of.getSort().equals("mediatype")) {

            if (of.getSort().equals("konto")) { of.setSort("b.KID"); }

        } else {
            of.setSort("orderdate");
        }

        return of;
    }

    /**
     * This method checks if a String is a valid URL
     */
    public boolean isUrl(String link) {

        boolean check = true;

        try {
            URL url = new URL(link);
            System.out.println("Gültige URL: " + url); // unterdrückt Warnung, dass url nie gebraucht wird
        } catch (MalformedURLException e) {
            LOG.info("isUrl: " + link + "\040" + e.toString());
            check = false;
        }

        return check;
    }

    /**
     * This method checks if a String is a valid URL and from a specific filetype
     */
    public boolean isUrlAndFiletype(String link, String[] filetypes) {

        boolean check = false;

        try {
            String extension = link.substring(link.lastIndexOf(".") + 1); // may throw exception

            // check for specified filetypes
            for (int i = 0; i < filetypes.length; i++) {
                if (extension.equalsIgnoreCase(filetypes[i])) { check = true; }
            }

            // link mustn't be longer than 254 charactres
            if (link.length() > 254) { check = false; }

            // needs to be an URL after all!
            if (check) { check = isUrl(link); }

        } catch (Exception e) {
            check = false; // if we got here, the check failed
        }

        return check;
    }

    /**
     * This method checks if a String ends with a valid filetype extension
     *
     * @param String path
     * @return boolean check
     */
    public boolean isFiletypeExtension(String fileName, String extension) {

        boolean check = false;

        if (fileName != null && extension != null
                && fileName.length() > extension.length()) { // filename must be longer than the filetype extension
            // make case insensitive
            fileName = fileName.toLowerCase();
            extension = extension.toLowerCase();
            try {
                if (fileName.contains(extension) // must contain extension
                        // extension must be placed at the end
                        && fileName.lastIndexOf(extension) == fileName.length() - extension.length()) {
                    check = true;
                }

            } catch (Exception e) {
                check = false; // if we got here, the check failed
            }

        }

        return check;
    }


    /**
     * Extrahiert aus einem String alle Wörter und Zahlen (Regexp: \w   A word character: [a-zA-Z_0-9])
     * @param s
     * @return ArrayList words
     */
    public ArrayList<String> getAlphanumericWordCharacters(String s) {

        ArrayList<String> words = new ArrayList<String>();

        if (s != null) {

            // Regexp: \w   A word character: [a-zA-Z_0-9]
            Pattern p = Pattern.compile("([\\p{L}||\\P{Alpha}&&[^\\p{Punct}]&&[^\\p{Space}]])*");
            Matcher m = p.matcher(s);

            while (m.find()) {
                words.add(s.substring(m.start(), m.end()));
            }
        }

        return words;
    }

    /**
     * Counts the occurence of a character in a string
     */
    public int countCharacterInString(String input, String countString) {
        return input.split("\\Q" + countString + "\\E", -1).length - 1;
    }

    /**
     * This method checks if a String contains only numbers
     */
    public boolean containsOnlyNumbers(String str) {

        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0) { return false; }

        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i))) { return false; }
        }

        return true;
    }

    /**
     * Prüft, ob ein Google Captcha vorliegt
     * @param content
     * @return boolean check
     */
    public boolean containsGoogleCaptcha(String content) {

        boolean check = false;

        if (content != null && (content.contains("=\"captcha\"") || content.contains("=\"Captcha\""))) {
            check = true;
        }

        return check;
    }

    /**
     * Checks if an ISSN is valid
     * @param String issn
     * @return boolean check
     */
    public boolean isValidIssn(String issn) {

        boolean check = false;
        String kontrollziffer = "";

        try {

            if (issn.length() == 9 && issn.substring(4, 5).equals("-")) {
                int pos8 = Integer.valueOf(issn.substring(0, 1));
                int pos7 = Integer.valueOf(issn.substring(1, 2));
                int pos6 = Integer.valueOf(issn.substring(2, 3));
                int pos5 = Integer.valueOf(issn.substring(3, 4));
                int pos4 = Integer.valueOf(issn.substring(5, 6));
                int pos3 = Integer.valueOf(issn.substring(6, 7));
                int pos2 = Integer.valueOf(issn.substring(7, 8));
                String pos1 = issn.substring(8);

                int sum = pos8 * 8 + pos7 * 7 + pos6 * 6 + pos5 * 5 + pos4 * 4 + pos3 * 3 + pos2 * 2;
                int checksum = 11 - (sum - (sum / 11) * 11);
                if (checksum == 10 || checksum == 11) {
                    if (checksum == 10) { kontrollziffer = "X"; }
                    if (checksum == 11) { kontrollziffer = "0"; }
                } else {
                    kontrollziffer = String.valueOf(checksum);
                }
                if (pos1.equalsIgnoreCase(kontrollziffer)) {
                    check = true;
                } else {
                    System.out.println("ungültige Prüfziffer: " + issn);
                }
            }
        } catch (Exception e) {
            LOG.error("isValidIssn: " + issn + "\040" + e.toString());
        }


        return check;
    }


    /**
     * Checks if a String is a valid year.
     * Works from 14th century till 22th century. This sould be usable for some time...
     * @param String year
     * @return boolean check
     */
    public boolean isYear(String year) {

        boolean check = false;

        // check if length = 4 and string contains only digits
        if (isExactLength(year, 4) && org.apache.commons.lang.StringUtils.isNumeric(year)) {
            // Search pattern works from 14th century till 22th century. This sould be usable for some time...
            Pattern p = Pattern.compile(
            "13[0-9]{2}|14[0-9]{2}|15[0-9]{2}|16[0-9]{2}|17[0-9]{2}|18[0-9]{2}|19[0-9]{2}|20[0-9]{2}|21[0-9]{2}");
            Matcher m = p.matcher(year);
            try {
                if (m.find()) {
                    check = true;
                }
            } catch (Exception e) {
                LOG.error("isYear(String year): " + year + "\040" + e.toString());
            }
        }

        return check;
    }


}
