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

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.grlea.log.SimpleLogger;



/**
 * Diese Klasse vereinfacht verschiedene Abläufe zwischen Programm und Mailserver.
 * </P></P>
 * @author Pascal Steiner
 */
public class MHelper extends AbstractReadSystemConfigurations {
	
	private static final SimpleLogger log = new SimpleLogger(MHelper.class);
	
	private static final String PRIORITY = "3"; // normal


    public MHelper(){
   
    }
    
    public void sendMail(String[] to, Message msg) {
    	sendMail(to, msg, PRIORITY);
    }
    
//    public void sendMailReplyTo(String[] to, Message msg, String replyto) {
//    	sendMailReplyTo(to, msg, replyto, PRIORITY);
//    }
    
    public void sendMail(String recipients[], String subject, String message) {
    	String from = SYSTEM_EMAIL;
    	sendMail(recipients, subject, message, from, PRIORITY);
    }
    
    public void sendErrorMail(String subject, String message) {
    	String[] recipients = new String[1];
    	recipients[0] = ERROR_EMAIL;
    	String from = SYSTEM_EMAIL;
    	sendMail(recipients, subject, message, from, PRIORITY);
    }
    
    public void sendMailReplyTo(String recipients[], String subject, 
    							String message, String replyto) {
    	sendMailReplyTo(recipients, subject, message, replyto, PRIORITY);
    }
    
    /**
     * Versendet eine {@link Message} an Empfänger</P></P>
     * 
     * @param to die Empfängerliste in einem {@link String[]}
     * @param msg das Mail welches versendet werden soll als {@link Message}
     * </P></P>
     * @author Pascal Steiner
     * */
    public void sendMail(String[] to, Message msg, String prio) {
        // set the from and to address
        try {
            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            InternetAddress[] addressFrom = null;
            addressFrom = InternetAddress.parse(SYSTEM_EMAIL);
            
            // SMPT Authentifizierung einschalten
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");

            // create some properties and get the default Session
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
//          create a message
            MimeMessage m = new MimeMessage(session);
            m.setFrom(addressFrom[0]); // From setzen
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */ 
            m.addHeader("charset", "UTF-8"); // Header setzen
//            m.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
            m.addHeader("X-Priority", prio);
            m.setSentDate(new Date()); // Sendedatum setzen
            String subject = "";
            if (msg.getSubject()!=null )subject = msg.getSubject();
            m.setSubject(subject); // Betreff setzen
            m.setText(msg.getContent().toString(), "UTF-8");
//            m.setContent(msg.getContent(), msg.getContentType()); // Inhalt/Attachements der Mail anhngen
            m.saveChanges();

            // Mail versenden
            Transport bus = session.getTransport("smtp");
            bus.connect(SYSTEM_EMAIL_HOST, SYSTEM_EMAIL_ACCOUNTNAME, SYSTEM_EMAIL_PASSWORD);
            bus.sendMessage(m, addressTo); // An diese Adressen senden
            bus.close();

        } catch (Exception e) {
        		log.error("sendMail(String[] to, Message msg, String prio): " + e.toString());
        	// Critical Error-Message
        	String msgText = null; // versucht zu übermitteln in welchen Situationen Versendfehler auftreten
        	try {msgText = "sendMail(String[] to, Message msg, String prio)" + "\n" + msg.getContent().toString();} catch (Exception ex) 
        		{log.error("coudn't send additional errormessage by email: " + ex.toString());};
            sendError(e + "\n" + msgText);
        }

    }
    
    /**
     * Versendet eine {@link Message} an Empfänger</P></P>
     * 
     * @param to die Empfängerliste in einem {@link String[]}
     * @param msg das Mail welches versendet werden soll als {@link Message}
     * </P></P>
     * @author Pascal Steiner
     * */
    public void sendMailReplyTo(String[] to, Message msg, String replyto, String prio) {
        // set the from and to address
        try {
            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            InternetAddress[] addressFrom = null;
            addressFrom = InternetAddress.parse(SYSTEM_EMAIL);
            
            // SMPT Authentifizierung einschalten
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");

            // create some properties and get the default Session
            Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
//          create a message
            MimeMessage m = new MimeMessage(session);
            m.setFrom(addressFrom[0]); // From setzen
            m.setReplyTo(new InternetAddress[] {new InternetAddress(replyto)}); // ReplyTo setzen
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */ 
            m.addHeader("charset", "UTF-8"); // Header setzen
//            m.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
            m.addHeader("X-Priority", prio);
            m.setSentDate(new Date()); // Sendedatum setzen
            String subject = "";
            if (msg.getSubject()!=null )subject = msg.getSubject();
            m.setSubject(subject); // Betreff setzen
            m.setText(msg.getContent().toString(), "UTF-8");
//            m.setContent(msg.getContent(), msg.getContentType()); // Inhalt/Attachements der Mail anhngen
            m.saveChanges();

            // Mail versenden
            Transport bus = session.getTransport("smtp");
            bus.connect(SYSTEM_EMAIL_HOST, SYSTEM_EMAIL_ACCOUNTNAME, SYSTEM_EMAIL_PASSWORD);
            bus.sendMessage(m, addressTo); // An diese Adressen senden
            bus.close();

        } catch (Exception e) {
        		log.error("sendMailReplyTo(String[] to, Message msg, String replyto, String prio): " + e.toString());
        	// Critical Error-Message
        	String msgText = null; // versucht zu übermitteln in welchen Situationen Versendfehler auftreten
        	try {msgText = "sendMailReplyTo(String[] to, Message msg, String replyto, String prio)" + "\n" + msg.getContent().toString();} catch (Exception ex) 
        		{log.error("coudn't send additional errormessage by email: " + ex.toString());};
            sendError(e + "\n" + msgText);
        }

    }

    /**
     * Verschickt ein Mail anhand der mitgegebenen Parameter
     * </P></P>
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * </P></P>
     * @author Pascal Steiner
     */
    public void sendMail(String recipients[], String subject, String message,
            String from, String prio) {

        try {
        // Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        
        // create some properties and get the default Session
        Session session = Session.getInstance(props, null);
        session.setDebug(false);

        // create a message
        Message msg = new MimeMessage(session);
        
        msg.addHeader("charset", "UTF-8");
        msg.addHeader("X-Priority", prio);
        
        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/plain");
        
        sendMail(recipients ,msg, prio);
        } catch (Exception e){
        	log.error("String recipients[], String subject, String message, String from, String prio: " + e.toString());
        	// Critical Error-Message
            sendError(e + "\n" + "sendMail(String recipients[], String subject, String message, String from, String prio)" + "\n" + message);
        }
    }
    
    /**
     * Verschickt ein Mail anhand der mitgegebenen Parameter
     * </P></P>
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * @param replyto
     * </P></P>
     * @author Pascal Steiner
     */
    public void sendMailReplyTo(String recipients[], String subject, String message,
            String replyto, String prio) {
    	
    	String from = SYSTEM_EMAIL;

        try {
        // Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        
        // create some properties and get the default Session
        Session session = Session.getInstance(props, null);
        session.setDebug(false);

        // create a message
        Message msg = new MimeMessage(session);
        
        msg.addHeader("charset", "UTF-8");
        msg.addHeader("X-Priority", prio);
        
        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        InternetAddress[] addressReplyTo = new InternetAddress[1];
        addressReplyTo[0] = new InternetAddress(replyto);
        msg.setFrom(addressFrom);
        msg.setReplyTo(addressReplyTo);

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/plain");
        
        sendMailReplyTo(recipients ,msg, replyto, prio);
        } catch (Exception e){
        	log.error("sendMailReplyTo(String recipients[], String subject, String message, String replyto, String prio: " + e.toString());
        	// Critical Error-Message
            sendError(e + "\n" + "sendMailReplyTo(String recipients[], String subject, String message, String from, String replyto, String prio)" + "\n" + message);
        }
    }
    
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse
     * <p></p>
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception}
     * <p></p>
     * @author Pascal Steiner
     */
    public void sendError(Exception e) {
        try {
            String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            String subject = "!!! In " + APPLICATION_NAME + " ist ein Fehler aufgetreten !!! ";
            sendMail(to, subject, e.toString());
        } catch (Exception ee) {
        	log.error("sendError(Exception e): " + ee.toString());
        } 
    }
    
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse
     * <p></p>
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception}
     * <p></p>
     * @author Pascal Steiner
     */
    public void sendError(String e) {
        try {
            String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            String subject = "!!! In " + APPLICATION_NAME + " ist ein Fehler aufgetreten !!! ";
            sendMail(to, subject, e);
        } catch (Exception ee) {
        	log.error("sendError(String e): " + ee.toString());
        } 
    }
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse
     * <p></p>
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception}
     * <p></p>
     * @author Pascal Steiner
     */
    public void sendError(String subject, Exception e) {
        try {
            String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            sendMail(to, subject, e.toString());
        } catch (Exception ee) {
        	log.error("sendError(String subject, Exception e): " + ee.toString());
        } 
    }
   
    /**
     * Gibt ein geöffnetes Verzeichnis eines pop3 Mailservers zurück <p></p>
     * </P></P>
     * @param host 
     * @param bn
     * @param pw
     * @param folder_ das zu ffnende Verzeichnis. Z. B. INBOX
     * @return folder gibt das geffnete Verzeichniss zurück
     * </P></P>
     * @author Pascal Steiner
     */
    public Folder getPop3Folder(String host, String bn, String pw, String folder_){
        Folder folder = null;
        try {
            
//          SMPT Authentifizierung einschalten
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props, null);

            Store store = session.getStore("pop3");
            store.connect(host, bn, pw);


            folder = store.getFolder(folder_);
            folder.open(Folder.READ_ONLY);
        } 
        catch (Exception e) {
        	log.error("getPop3Folder(String host, String bn, String pw, String folder_): " + e.toString());
            sendError(e);
        }
        return folder;
    }

    
    private void process(){
        Folder inbox = getPop3Folder(SYSTEM_EMAIL_HOST, SYSTEM_EMAIL_ACCOUNTNAME, SYSTEM_EMAIL_PASSWORD,"INBOX");
        
        try {
            Message[] message = inbox.getMessages();
            for (int i = 0; i < message.length; i++) { // was passiert wenn Inbox leer ist?
                Message m = message[i];
//                System.out.println("Nachricht: " + i);
//                System.out.println("From: " + m.getFrom()[0]);
//                System.out.println("Subject: " + m.getSubject());
//                System.out.println("Contenttype: " + m.getContentType());
                
                String[] to = new String[1];
                to[0] = SYSTEM_EMAIL;
                sendMail(to, m);
                System.out.println("\012Die Mail mit dem Betreff " + m.getSubject() + " wurde erfolgreich an " + to[0] + " versendet\012\012");
            }
            inbox.close(true);            
        } catch (Exception e){
        	log.error("process(): " + e.toString());
            sendError(e);
        }

    }
    
    
    /*
     * Mail Session, Debug eingeschalten, im Normalfall zum erstellen einer Nachricht Message
     */
    public Session getSession(){
//    	 SMPT Authentifizierung einschalten
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");

        // create some properties and get the default Session
        Session session = Session.getInstance(props, null);
        session.setDebug(false);
    	
    	return session;
    }

    public static void main(String args[]) throws Exception {
        MHelper mail = new MHelper();
        mail.process();
   }

}
