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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.grlea.log.SimpleLogger;


/**
 * Benutzt die Klasse commons-configuration um Properties-Files auszulesen und
 * an andere Klassen zu vererben
 *
 * @author Markus Fischer
 */

abstract class AbstractReadSystemConfigurations {

    private static final SimpleLogger LOG = new SimpleLogger(AbstractReadSystemConfigurations.class);
    private static final String PATH = "resources/SystemConfiguration.properties";

    protected static final String SYSTEM_TIMEZONE = readSystemTimezone();
    protected static final String LOCALE = readLocale();

    protected static final String SYSTEM_EMAIL = readSystemEmail();
    protected static final String SYSTEM_EMAIL_HOST = readSystemEmailHost();
    protected static final String SYSTEM_EMAIL_ACCOUNTNAME = readSystemEmailAccountname();
    protected static final String SYSTEM_EMAIL_PASSWORD = readSystemEmailPassword();
    protected static final String ERROR_EMAIL = readErrorEmail();

    protected static final String DATABASE_SERVERADDRESS = readDatabaseServerAddress();
    protected static final String DATABASE_NAME = readDatabaseName();
    protected static final String DATABASE_USER = readDatabaseUser();
    protected static final String DATABASE_PASSWORD = readDatabasePassword();
    protected static final boolean DATABASE_POOLED_CONNECTIONS = readDatabasePooledConnections();

    protected static final String SERVER_WELCOMEPAGE = readServerWelcomepage();
    protected static final String SERVER_INSTALLATION = readServerInstallation();
    protected static final String APPLICATION_NAME = readApplicationName();

    protected static final boolean ALLOW_REGISTER_LIBRARY_ACCOUNTS = readAllowRegisterLibraryAccounts();
    protected static final boolean ALLOW_PATRON_AUTOMATIC_GOOGLE_SEARCH = readAllowPatronAutomaticGoogleSearch();
    protected static final boolean ACTIVATE_GTC = readActivateGTC();

    protected static final boolean ANONYMIZATION_ACTIVATED = readAnonymizationActivated();
    protected static final int ANONYMIZATION_AFTER_MONTHS = readAnonymizationAfterMonths();

    protected static final boolean SEARCH_CARELIT = searchCarelit();
    protected static final boolean USE_DAIA = readUseDaia();
    protected static final String DAIA_HOST = readDaiaHost();

    private static String readSystemTimezone() {

        String systemTimezone = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            systemTimezone = config.getString("system.timezone");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return systemTimezone;
    }

    private static String readLocale() {

        String locale = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            locale = config.getString("locale");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return locale;
    }

    private static String readSystemEmail() {

        String systemEmail = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            systemEmail = config.getString("systemEmail.email");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return systemEmail;
    }

    private static String readSystemEmailHost() {

        String host = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            host = config.getString("systemEmail.host");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return host;
    }

    private static String readSystemEmailAccountname() {

        String accountname = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            accountname = config.getString("systemEmail.accountname");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return accountname;
    }

    private static String readSystemEmailPassword() {

        String password = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            password = config.getString("systemEmail.password");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return password;
    }

    private static String readErrorEmail() {

        String errorEmail = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            errorEmail = config.getString("errorEmail.email");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return errorEmail;
    }

    private static String readDatabaseServerAddress() {

        String databaseServerAdress = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            databaseServerAdress = config.getString("mysql.serveraddress");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return databaseServerAdress;
    }

    private static String readDatabaseName() {

        String databaseName = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            databaseName = config.getString("mysql.databaseName");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return databaseName;
    }

    private static String readDatabaseUser() {

        String databaseUser = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            databaseUser = config.getString("mysql.user");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return databaseUser;
    }

    private static String readDatabasePassword() {

        String databasePassword = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            databasePassword = config.getString("mysql.password");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return databasePassword;
    }

    private static boolean readDatabasePooledConnections() {

        boolean activated = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            activated = config.getBoolean("mysql.pooledConnections");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return activated;
    }

    private static String readServerWelcomepage() {

        String serverAddress = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            serverAddress = config.getString("server.welcomepage");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return serverAddress;
    }

    private static String readServerInstallation() {

        String serverInstallation = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            serverInstallation = config.getString("server.installation");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return serverInstallation;
    }

    private static String readApplicationName() {

        String applicationName = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            applicationName = config.getString("application.name");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return applicationName;
    }

    private static boolean readAllowRegisterLibraryAccounts() {

        boolean allow = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            allow = config.getBoolean("allow.registerLibraryAccounts");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return allow;
    }

    private static boolean readAllowPatronAutomaticGoogleSearch() {

        boolean allow = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            allow = config.getBoolean("allow.patronAutomaticGoogleSearch");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return allow;
    }

    private static boolean readActivateGTC() {

        boolean activate = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            activate = config.getBoolean("activate.gtc");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return activate;
    }

    private static boolean readAnonymizationActivated() {

        boolean activated = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            activated = config.getBoolean("anonymization.activated");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return activated;
    }

    private static int readAnonymizationAfterMonths() {

        int months = 3; // Default, if in the configuration file a not valid value was specified

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            months = config.getInt("anonymization.after.months");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return months;
    }

    private static boolean searchCarelit() {

        boolean searchCarelit = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            searchCarelit = config.getBoolean("searchCarelit");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return searchCarelit;
    }

    private static boolean readUseDaia() {

        boolean useDaia = false;

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            useDaia = config.getBoolean("useDaia");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return useDaia;
    }

    private static String readDaiaHost() {

        String daiaHost = "";

        try {
            final Configuration config = new PropertiesConfiguration(PATH);
            daiaHost = config.getString("daiaHost");

        } catch (final ConfigurationException e) {
            LOG.error(e.toString());
        }

        return daiaHost;
    }


}
