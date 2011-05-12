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


/**
 * Liefert eine Teilmenge an Systemparameter aus SystemConfiguration.properties. Sämtliche
 * Systemparameter sind über AbstractReadSystemConfigurations zugänglich
 *
 * @author Markus Fischer
 */
public final class ReadSystemConfigurations {

    private ReadSystemConfigurations()  {

    }

    public static String getSystemTimezone() {
        return AbstractReadSystemConfigurations.SYSTEM_TIMEZONE;
    }

    public static String getLocale() {
        return AbstractReadSystemConfigurations.LOCALE;
    }

    public static String getSystemEmail() {
        return AbstractReadSystemConfigurations.SYSTEM_EMAIL;
    }

    public static String getErrorEmail() {
        return AbstractReadSystemConfigurations.ERROR_EMAIL;
    }

    public static String getApplicationName() {
        return AbstractReadSystemConfigurations.APPLICATION_NAME;
    }

    public static String getServerWelcomepage() {
        return AbstractReadSystemConfigurations.SERVER_WELCOMEPAGE;
    }

    public static String getServerInstallation() {
        return AbstractReadSystemConfigurations.SERVER_INSTALLATION;
    }

    public static boolean isAllowRegisterLibraryAccounts() {
        return AbstractReadSystemConfigurations.ALLOW_REGISTER_LIBRARY_ACCOUNTS;
    }

    public static boolean isAllowPatronAutomaticGoogleSearch() {
        return AbstractReadSystemConfigurations.ALLOW_PATRON_AUTOMATIC_GOOGLE_SEARCH;
    }

    public static boolean isActivatedGoogleSearch() {
        return AbstractReadSystemConfigurations.ACTIVATE_GOOGLE_SEARCH;
    }

    public static boolean isGTC() {
        return AbstractReadSystemConfigurations.ACTIVATE_GTC;
    }

    public static boolean isAnonymizationActivated() {
        return AbstractReadSystemConfigurations.ANONYMIZATION_ACTIVATED;
    }

    public static int getAnonymizationAfterMonths() {
        return AbstractReadSystemConfigurations.ANONYMIZATION_AFTER_MONTHS;
    }

    public static boolean isSearchCarelit() {
        return AbstractReadSystemConfigurations.SEARCH_CARELIT;
    }

    public static boolean isUseDaia() {
        return AbstractReadSystemConfigurations.USE_DAIA;
    }

    public static String[] getDaiaHosts() {
        return AbstractReadSystemConfigurations.DAIA_HOSTS;
    }

}
