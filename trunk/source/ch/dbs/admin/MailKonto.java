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

package ch.dbs.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.grlea.log.SimpleLogger;

import util.Http;
import util.PasswordGenerator;
import ch.dbs.entity.Konto;


public final class MailKonto  {
	
	private static final SimpleLogger log = new SimpleLogger(MailKonto.class);
	
    /**
     * Hinzufuegen eines neuen Mailkontos
     * 
     * @author Pascal Steiner
     */
    public void createNewMailKonto(Konto k) {
    	
    	
//    	Login bei Hoststar
    	String link = "https://server22.hostfactory.ch:8443/login_up.php3";
    	String postdata = "";
   	
    	try {
    		postdata = URLEncoder.encode("login_name", "UTF-8") + "="
			+ URLEncoder.encode("CL22125", "UTF-8");
    		postdata += "&" + URLEncoder.encode("passwd", "UTF-8") + "="
			+ URLEncoder.encode("wFhLPk", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("createNewMailKonto: " + e.toString());
		}
		
		Http http = new Http();
		System.out.println(http.getWebcontent(link, postdata));
//		System.out.println(http.getWebcontent("https://server22.hostfactory.ch:8443/login_up.php3?login_name=CL22125&passwd=wFhLPk"));
		
		//Account anlegen:
		PasswordGenerator p = new PasswordGenerator(8);
		String pw = p.getRandomString();
		link = "https://server22.hostfactory.ch:8443/plesk/client@27/domain@87/mail/mailname@new/properties/";
		try {
			postdata = URLEncoder.encode("mail_name", "UTF-8") + "="
			+ URLEncoder.encode(k.getId().toString(), "UTF-8");
    		postdata += "&" + URLEncoder.encode("password", "UTF-8") + "="
			+ URLEncoder.encode(pw, "UTF-8");
    		postdata += "&" + URLEncoder.encode("password1", "UTF-8") + "="
			+ URLEncoder.encode(pw, "UTF-8");
    		postdata += "&" + URLEncoder.encode("max_button_length", "UTF-8") + "="
			+ URLEncoder.encode("", "UTF-8");
    		postdata += "&" + URLEncoder.encode("locale", "UTF-8") + "="
			+ URLEncoder.encode("de-DE", "UTF-8");
    		postdata += "&" + URLEncoder.encode("skin_id", "UTF-8") + "="
			+ URLEncoder.encode("1", "UTF-8");
    		postdata += "&" + URLEncoder.encode("multiply_login", "UTF-8") + "="
			+ URLEncoder.encode("true", "UTF-8");
    		postdata += "&" + URLEncoder.encode("disable_lock_screen", "UTF-8") + "="
			+ URLEncoder.encode("false", "UTF-8");
    		postdata += "&" + URLEncoder.encode("postbox", "UTF-8") + "="
			+ URLEncoder.encode("true", "UTF-8");
    		postdata += "&" + URLEncoder.encode("quota_on", "UTF-8") + "="
			+ URLEncoder.encode("limited", "UTF-8");
    		postdata += "&" + URLEncoder.encode("quota", "UTF-8") + "="
			+ URLEncoder.encode("2000", "UTF-8");
    		    		
    		postdata += "&" + URLEncoder.encode("cmd", "UTF-8") + "="
			+ URLEncoder.encode("update", "UTF-8");
    		postdata += "&" + URLEncoder.encode("lock", "UTF-8") + "="
			+ URLEncoder.encode("false", "UTF-8");
    		postdata += "&" + URLEncoder.encode("previous_page", "UTF-8") + "="
			+ URLEncoder.encode("mail_name_preferences", "UTF-8");
    		postdata += "&" + URLEncoder.encode("wizstep", "UTF-8") + "="
			+ URLEncoder.encode("2", "UTF-8");
    		postdata += "&" + URLEncoder.encode("wizard", "UTF-8") + "="
			+ URLEncoder.encode("/plesk/client@27/domain@/mail/mailname@new/properties/", "UTF-8");
    		
		} catch (UnsupportedEncodingException e) {
			log.error("createNewMailKonto: " + e.toString());			
		}
		
		System.out.println(http.getWebcontent(link, postdata));
		
		
		//hoststar:
//		link = "http://login-3.hoststar.ch/user/index.php";
//   	
//    	try {
//    		postdata = URLEncoder.encode("username", "UTF-8") + "="
//			+ URLEncoder.encode("web3", "UTF-8");
//    		postdata += "&" + URLEncoder.encode("password", "UTF-8") + "="
//			+ URLEncoder.encode("20662256", "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println(http.getWebcontent(link, postdata));
//		
//		link = "http://login-3.hoststar.ch/user/email_pop3_anlegen.php";
//		
//		PasswordGenerator p = new PasswordGenerator(8);
//		String pw = p.getRandomString();
//    	try {
//    		postdata = URLEncoder.encode("specifyPassword", "UTF-8") + "="
//			+ URLEncoder.encode("1", "UTF-8");
//    		postdata += "&" + URLEncoder.encode("newpw1", "UTF-8") + "="
//			+ URLEncoder.encode(pw, "UTF-8");
//    		postdata += "&" + URLEncoder.encode("newpw2", "UTF-8") + "="
//			+ URLEncoder.encode(pw, "UTF-8");
//    		postdata += "&" + URLEncoder.encode("comment", "UTF-8") + "="
//			+ URLEncoder.encode(ReadSystemConfigurations.getApplicationName() + ": " + k.getBibliotheksname(), "UTF-8");
//
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println(http.getWebcontent(link, postdata));
//		System.out.println(pw);
//		System.out.println(http.getWebcontent("https://server22.hostfactory.ch:8443/plesk/client@27/domain@87/mail/mailname@new/properties/?cmd=update&lock=false&previous_page=mail_name_preferences&wizstep=2&wizard=%2Fplesk%2Fclient%4027%2Fdomain%40%2Fmail%2Fmailname%40new%2Fproperties%2F&wizaction=finish&mail_name=" + k.getId().toString()+"&password=&password1=&max_button_length=&locale=de-DE&skin_id=8&multiply_login=true&disable_lock_screen=false&postbox=true&quota_on=limited&quota=2000&bname_finish="));


	}
 

    
}
