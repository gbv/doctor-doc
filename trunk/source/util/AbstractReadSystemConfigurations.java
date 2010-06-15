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

public abstract class AbstractReadSystemConfigurations {
	
	private static final SimpleLogger log = new SimpleLogger(AbstractReadSystemConfigurations.class);
	
	static final String SYSTEM_TIMEZONE = readSystemTimezone();
	
	static final String SYSTEM_EMAIL = readSystemEmail();
	static final String SYSTEM_EMAIL_HOST = readSystemEmailHost();
	static final String SYSTEM_EMAIL_ACCOUNTNAME = readSystemEmailAccountname();
	static final String SYSTEM_EMAIL_PASSWORD = readSystemEmailPassword();
	static final String ERROR_EMAIL = readErrorEmail();
	
	static final String DATABASE_SERVERADDRESS = readDatabaseServerAddress();
	static final String DATABASE_NAME = readDatabaseName();
	static final String DATABASE_USER = readDatabaseUser();
	static final String DATABASE_PASSWORD = readDatabasePassword();
	static final boolean DATABASE_POOLED_CONNECTIONS = readDatabasePooledConnections();
	
	static final String SERVER_WELCOMEPAGE = readServerWelcomepage();
	static final String SERVER_INSTALLATION = readServerInstallation();
	static final String APPLICATION_NAME = readApplicationName();
	
	static final boolean ALLOW_REGISTER_LIBRARY_ACCOUNTS = readAllowRegisterLibraryAccounts();
	static final boolean ALLOW_PATRON_AUTOMATIC_GOOGLE_SEARCH = readAllowPatronAutomaticGoogleSearch();
	static final boolean ACTIVATE_GTC = readActivateGTC();
	
	static final boolean ANONYMIZATION_ACTIVATED = readAnonymizationActivated();
	static final int ANONYMIZATION_AFTER_MONTHS = readAnonymizationAfterMonths();
	
	static final boolean USE_DAIA = readUseDaia();
	static final String DAIA_HOST = readDaiaHost();
	
	private static final String readSystemTimezone() {
		
		String systemTimezone = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			systemTimezone = config.getString("system.timezone");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return systemTimezone;
	}
	
	private static final String readSystemEmail() {
		
		String systemEmail = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			systemEmail = config.getString("systemEmail.email");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return systemEmail;
	}
	
	private static final String readSystemEmailHost() {
		
		String host = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			host = config.getString("systemEmail.host");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());		
		}
		
		return host;
	}
	
	private static final String readSystemEmailAccountname() {
		
		String accountname = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			accountname = config.getString("systemEmail.accountname");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());	
		}
		
		return accountname;
	}
	
	private static final String readSystemEmailPassword() {
		
		String password = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			password = config.getString("systemEmail.password");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return password;
	}
	
	private static final String readErrorEmail() {
		
		String errorEmail = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			errorEmail = config.getString("errorEmail.email");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return errorEmail;
	}
	
	private static final String readDatabaseServerAddress() {
		
		String databaseServerAdress = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			databaseServerAdress = config.getString("mysql.serveraddress");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return databaseServerAdress;
	}
	
	private static final String readDatabaseName() {
		
		String databaseName = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			databaseName = config.getString("mysql.databaseName");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return databaseName;
	}
	
	private static final String readDatabaseUser() {
		
		String databaseUser = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			databaseUser = config.getString("mysql.user");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return databaseUser;
	}
	
	private static final String readDatabasePassword() {
		
		String databasePassword = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			databasePassword = config.getString("mysql.password");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return databasePassword;
	}
	
	private static final boolean readDatabasePooledConnections() {
		
		boolean activated = false;
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			activated = config.getBoolean("mysql.pooledConnections");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return activated;
	}
	
	private static final String readServerWelcomepage() {
		
		String serverAddress = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			serverAddress = config.getString("server.welcomepage");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return serverAddress;
	}
	
	private static final String readServerInstallation() {
		
		String serverInstallation = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			serverInstallation = config.getString("server.installation");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return serverInstallation;
	}
	
	private static final String readApplicationName() {
		
		String applicationName = "";
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			applicationName = config.getString("application.name");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return applicationName;
	}

private static final boolean readAllowRegisterLibraryAccounts() {
		
		boolean allow = false;
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			allow = config.getBoolean("allow.registerLibraryAccounts");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return allow;
	}	

private static final boolean readAllowPatronAutomaticGoogleSearch() {
	
	boolean allow = false;
	
	try {			
		Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
		allow = config.getBoolean("allow.patronAutomaticGoogleSearch");
		
	} catch (ConfigurationException e) {
		log.error(e.toString());
	}
	
	return allow;
}

private static final boolean readActivateGTC() {
	
	boolean activate = false;
	
	try {			
		Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
		activate = config.getBoolean("activate.gtc");
		
	} catch (ConfigurationException e) {
		log.error(e.toString());
	}
	
	return activate;
}

private static final boolean readAnonymizationActivated() {
		
		boolean activated = false;
		
		try {			
			Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
			activated = config.getBoolean("anonymization.activated");
			
		} catch (ConfigurationException e) {
			log.error(e.toString());
		}
		
		return activated;
	}

private static final int readAnonymizationAfterMonths() {
	
	int months = 3; // Default, if in the configuration file a not valid value was specified
	
	try {			
		Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
		months = config.getInt("anonymization.after.months");
		
	} catch (ConfigurationException e) {
		log.error(e.toString());
	}
	
	return months;
}

private static final boolean readUseDaia() {
	
	boolean useDaia = false;
	
	try {			
		Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
		useDaia = config.getBoolean("useDaia");
		
	} catch (ConfigurationException e) {
		log.error(e.toString());
	}
	
	return useDaia;
}

private static final String readDaiaHost() {
	
	String daiaHost = "";
	
	try {			
		Configuration config = new PropertiesConfiguration("resources/SystemConfiguration.properties");
		daiaHost = config.getString("daiaHost");
		
	} catch (ConfigurationException e) {
		log.error(e.toString());
	}
	
	return daiaHost;
}
	

}
