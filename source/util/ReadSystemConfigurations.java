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


/**
 * Liefert eine Teilmenge an Systemparameter aus SystemConfiguration.properties. Sämtliche
 * Systemparameter sind über AbstractReadSystemConfigurations zugänglich
 * 
 * @author Markus Fischer
 */
public class ReadSystemConfigurations {
	
	public final static String getSystemTimezone() {
		return AbstractReadSystemConfigurations.SYSTEM_TIMEZONE;
	}
	
	public final static String getSystemEmail() {
		return AbstractReadSystemConfigurations.SYSTEM_EMAIL;
	}
	
	public final static String getErrorEmail() {
		return AbstractReadSystemConfigurations.ERROR_EMAIL;
	}
	
	public final static String getApplicationName() {
		return AbstractReadSystemConfigurations.APPLICATION_NAME;
	}
	
	public final static String getServerWelcomepage() {
		return AbstractReadSystemConfigurations.SERVER_WELCOMEPAGE;
	}
	
	public final static String getServerInstallation() {
		return AbstractReadSystemConfigurations.SERVER_INSTALLATION;
	}
	
	public final static boolean isAllowRegisterLibraryAccounts() {
		return AbstractReadSystemConfigurations.ALLOW_REGISTER_LIBRARY_ACCOUNTS;
	}
	
	public final static boolean isAllowPatronAutomaticGoogleSearch() {
		return AbstractReadSystemConfigurations.ALLOW_PATRON_AUTOMATIC_GOOGLE_SEARCH;
	}
	
	public final static boolean isGTC() {
		return AbstractReadSystemConfigurations.ACTIVATE_GTC;
	}
	
	public final static boolean isAnonymizationActivated() {
		return AbstractReadSystemConfigurations.ANONYMIZATION_ACTIVATED;
	}
	
	public final static int getAnonymizationAfterMonths() {
		return AbstractReadSystemConfigurations.ANONYMIZATION_AFTER_MONTHS;
	}
	
	public final static boolean isSearchCarelit() {
		return AbstractReadSystemConfigurations.SEARCH_CARELIT;
	}
	
	public final static boolean isUseDaia() {
		return AbstractReadSystemConfigurations.USE_DAIA;
	}
	
	public final static String getDaiaHost() {
		return AbstractReadSystemConfigurations.DAIA_HOST;
	}

	

}