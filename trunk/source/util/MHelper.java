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

import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Diese Klasse vereinfacht verschiedene Abläufe zwischen Programm und
 * Mailserver. </P></P>
 * 
 * @author Pascal Steiner, Markus Fischer
 */
public class MHelper extends AbstractReadSystemConfigurations {
    
    final Logger LOG = LoggerFactory.getLogger(MHelper.class);
    private static final String PRIORITY = "3"; // normal
    private static final String XPRIO = "X-Priority";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String TRUE = "true";
    private static final String CHARSET = "charset";
    private static final String UTF8 = "UTF-8";
    
    public void sendMail(final String[] to, final Message msg) {
        sendMail(to, msg, PRIORITY);
    }
    
    public void sendMail(final String[] recipients, final String subject, final String message) {
        final String from = SYSTEM_EMAIL;
        sendMail(recipients, subject, message, from, PRIORITY);
    }
    
    public void sendErrorMail(final String subject, final String message) {
        final String[] recipients = new String[1];
        recipients[0] = ERROR_EMAIL;
        final String from = SYSTEM_EMAIL;
        sendMail(recipients, subject, message, from, PRIORITY);
    }
    
    public void sendMailReplyTo(final String[] recipients, final String subject, final String message,
            final String replyto) {
        sendMailReplyTo(recipients, subject, message, replyto, PRIORITY);
    }
    
    /**
     * Versendet eine {@link Message} an Empfänger</P></P>
     * 
     * @param to die Empfängerliste in einem {@link String[]}
     * @param msg das Mail welches versendet werden soll als {@link Message}
     * </P></P>
     * @author Pascal Steiner
     */
    public void sendMail(final String[] to, final Message msg, final String prio) {
        // set the from and to address
        try {
            final InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            InternetAddress[] addressFrom = null;
            addressFrom = InternetAddress.parse(SYSTEM_EMAIL);
            
            // SMPT Authentifizierung einschalten
            final Properties props = new Properties();
            props.put(SMTP_AUTH, TRUE);
            
            // create some properties and get the default Session
            final Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
            //          create a message
            final MimeMessage m = new MimeMessage(session);
            m.setFrom(addressFrom[0]); // From setzen
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */
            m.addHeader(CHARSET, UTF8); // Header setzen
            //            m.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
            m.addHeader(XPRIO, prio);
            m.setSentDate(new Date()); // Sendedatum setzen
            String subject = "";
            if (msg.getSubject() != null) {
                subject = msg.getSubject();
            }
            m.setSubject(MimeUtility.encodeText(subject, UTF8, null)); // Betreff setzen
            m.setText(msg.getContent().toString(), UTF8);
            //            m.setContent(msg.getContent(), msg.getContentType()); // Inhalt/Attachements der Mail anhngen
            m.saveChanges();
            
            // Mail versenden
            final Transport bus = session.getTransport("smtp");
            try {
                bus.connect(SYSTEM_EMAIL_HOST, getLoadBalancedAccontname(), SYSTEM_EMAIL_PASSWORD);
                bus.sendMessage(m, addressTo); // An diese Adressen senden
            } finally {
                bus.close();
            }
            
        } catch (final Exception e) {
            LOG.error("sendMail(String[] to, Message msg, String prio): " + e.toString());
            // Critical Error-Message
            String msgText = null; // versucht zu übermitteln in welchen Situationen Versendfehler auftreten
            try {
                msgText = "sendMail(String[] to, Message msg, String prio)" + "\n" + msg.getContent().toString();
            } catch (final Exception ex) {
                LOG.error("coudn't send additional errormessage by email: " + ex.toString());
            }
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
     */
    public void sendMailReplyTo(final String[] to, final Message msg, final String replyto, final String prio) {
        // set the from and to address
        try {
            final InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            InternetAddress[] addressFrom = null;
            addressFrom = InternetAddress.parse(SYSTEM_EMAIL);
            
            // SMPT Authentifizierung einschalten
            final Properties props = new Properties();
            props.put(SMTP_AUTH, TRUE);
            
            // create some properties and get the default Session
            final Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
            //          create a message
            final MimeMessage m = new MimeMessage(session);
            m.setFrom(addressFrom[0]); // From setzen
            m.setReplyTo(new InternetAddress[] { new InternetAddress(replyto) }); // ReplyTo setzen
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */
            m.addHeader(CHARSET, UTF8); // Header setzen
            //            m.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
            m.addHeader(XPRIO, prio);
            m.setSentDate(new Date()); // Sendedatum setzen
            String subject = "";
            if (msg.getSubject() != null) {
                subject = msg.getSubject();
            }
            m.setSubject(MimeUtility.encodeText(subject, UTF8, null)); // Betreff setzen
            m.setText(msg.getContent().toString(), UTF8);
            //            m.setContent(msg.getContent(), msg.getContentType()); // Inhalt/Attachements der Mail anhngen
            m.saveChanges();
            
            // Mail versenden
            sendMessage(session, m, addressTo);
            
        } catch (final Exception e) {
            LOG.error("sendMailReplyTo(String[] to, Message msg, String replyto, String prio): " + e.toString());
            // Critical Error-Message
            String msgText = null; // versucht zu übermitteln in welchen Situationen Versendfehler auftreten
            try {
                msgText = "sendMailReplyTo(String[] to, Message msg, String replyto, String prio)" + "\n"
                        + msg.getContent().toString();
            } catch (final Exception ex) {
                LOG.error("coudn't send additional errormessage by email: " + ex.toString());
            }
            sendError(e + "\n" + msgText);
        }
        
    }
    
    /**
     * Verschickt ein Mail anhand der mitgegebenen Parameter </P></P>
     * 
     * @param recipients
     * @param subject
     * @param message
     * @param from </P></P>
     * @author Pascal Steiner
     */
    public void sendMail(final String[] recipients, final String subject, final String message, final String from,
            final String prio) {
        
        try {
            // Set the host smtp address
            final Properties props = new Properties();
            props.put(SMTP_AUTH, TRUE);
            
            // create some properties and get the default Session
            final Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
            // create a message
            final Message msg = new MimeMessage(session);
            
            msg.addHeader(CHARSET, UTF8);
            msg.addHeader(XPRIO, prio);
            
            // set the from and to address
            final InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            
            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            msg.setSentDate(new Date());
            msg.setContent(message, "text/plain; charset=UTF-8");
            
            sendMail(recipients, msg, prio);
        } catch (final Exception e) {
            LOG.error("String recipients[], String subject, String message, String from, String prio: " + e.toString());
            // Critical Error-Message
            sendError(e + "\n" + "sendMail(String recipients[], String subject, "
                    + "String message, String from, String prio)" + "\n" + message);
        }
    }
    
    /**
     * Verschickt ein Mail anhand der mitgegebenen Parameter </P></P>
     * 
     * @param recipients
     * @param subject
     * @param message
     * @param from
     * @param replyto </P></P>
     * @author Pascal Steiner
     */
    public void sendMailReplyTo(final String[] recipients, final String subject, final String message,
            final String replyto, final String prio) {
        
        try {
            // Set the host smtp address
            final Properties props = new Properties();
            props.put(SMTP_AUTH, TRUE);
            
            // create some properties and get the default Session
            final Session session = Session.getInstance(props, null);
            session.setDebug(false);
            
            // create a message
            final Message msg = new MimeMessage(session);
            
            msg.addHeader(CHARSET, UTF8);
            msg.addHeader(XPRIO, prio);
            
            // set the from and to address
            final InternetAddress addressFrom = new InternetAddress(SYSTEM_EMAIL);
            final InternetAddress[] addressReplyTo = new InternetAddress[1];
            addressReplyTo[0] = new InternetAddress(replyto);
            msg.setFrom(addressFrom);
            msg.setReplyTo(addressReplyTo);
            
            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            msg.setSentDate(new Date());
            msg.setContent(message, "text/plain; charset=UTF-8");
            
            sendMailReplyTo(recipients, msg, replyto, prio);
        } catch (final Exception e) {
            LOG.error("sendMailReplyTo(String recipients[], String subject, String message, "
                    + "String replyto, String prio: " + e.toString());
            // Critical Error-Message
            sendError(e + "\n" + "sendMailReplyTo(String recipients[], String subject, String message, "
                    + "String from, String replyto, String prio)" + "\n" + message);
        }
    }
    
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse <p></p>
     * 
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception} <p></p>
     * @author Pascal Steiner
     */
    public void sendError(final Exception e) {
        try {
            final String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            final String subject = "!!! In " + APPLICATION_NAME + " ist ein Fehler aufgetreten !!! ";
            sendMail(to, subject, e.toString());
        } catch (final Exception ee) {
            LOG.error("sendError(Exception e): " + ee.toString());
        }
    }
    
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse <p></p>
     * 
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception} <p></p>
     * @author Pascal Steiner
     */
    public void sendError(final String e) {
        try {
            final String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            final String subject = "!!! In " + APPLICATION_NAME + " ist ein Fehler aufgetreten !!! ";
            sendMail(to, subject, e);
        } catch (final Exception ee) {
            LOG.error("sendError(String e): " + ee.toString());
        }
    }
    
    /**
     * Verschickt eine Fehlermeldung an eine E-Mail-Adresse <p></p>
     * 
     * @param too Die E-Mail-Adresse
     * @param e Die {@link Exception} <p></p>
     * @author Pascal Steiner
     */
    public void sendError(final String subject, final Exception e) {
        try {
            final String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            sendMail(to, subject, e.toString());
        } catch (final Exception ee) {
            LOG.error("sendError(String subject, Exception e): " + ee.toString());
        }
    }
    
    /**
     * Gibt ein geöffnetes Verzeichnis eines pop3 Mailservers zurück <p></p>
     * </P></P>
     * 
     * @param host
     * @param bn
     * @param pw
     * @param folder_ das zu ffnende Verzeichnis. Z. B. INBOX
     * @return folder gibt das geffnete Verzeichniss zurück </P></P>
     * @author Pascal Steiner
     */
    public Folder getPop3Folder(final String host, final String bn, final String pw, final String folder_) {
        Folder folder = null;
        try {
            
            //          SMPT Authentifizierung einschalten
            final Properties props = new Properties();
            props.put(SMTP_AUTH, TRUE);
            
            final Session session = Session.getInstance(props, null);
            
            final Store store = session.getStore("pop3");
            store.connect(host, bn, pw);
            
            folder = store.getFolder(folder_);
            folder.open(Folder.READ_ONLY);
        } catch (final Exception e) {
            LOG.error("getPop3Folder(String host, String bn, String pw, String folder_): " + e.toString());
            sendError(e);
        }
        return folder;
    }
    
    private void process() {
        final Folder inbox = getPop3Folder(SYSTEM_EMAIL_HOST, getLoadBalancedAccontname(), SYSTEM_EMAIL_PASSWORD,
                "INBOX");
        
        try {
            final Message[] message = inbox.getMessages();
            for (int i = 0; i < message.length; i++) { // was passiert wenn Inbox leer ist?
                final Message m = message[i];
                //                System.out.println("Nachricht: " + i);
                //                System.out.println("From: " + m.getFrom()[0]);
                //                System.out.println("Subject: " + m.getSubject());
                //                System.out.println("Contenttype: " + m.getContentType());
                
                final String[] to = new String[1];
                to[0] = SYSTEM_EMAIL;
                sendMail(to, m);
            }
            inbox.close(true);
        } catch (final Exception e) {
            LOG.error("process(): " + e.toString());
            sendError(e);
        }
        
    }
    
    public static void main(final String[] args) {
        final MHelper mail = new MHelper();
        mail.process();
    }
    
    /** Gets the system properties */
    public Properties getProperties() {
        // Get system properties
        final Properties props = System.getProperties();
        // Setup mail server
        props.setProperty("mail.smtp.host", SYSTEM_EMAIL_HOST);
        props.setProperty("mail.user", getLoadBalancedAccontname());
        props.setProperty("mail.password", SYSTEM_EMAIL_PASSWORD);
        props.setProperty(SMTP_AUTH, TRUE);
        return props;
    }
    
    /** Gets the MimeMessage */
    public MimeMessage getMimeMessage(final Session session) throws MessagingException {
        // Define message
        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SYSTEM_EMAIL));
        message.addHeader(CHARSET, UTF8); // set Header
        message.addHeader(XPRIO, PRIORITY);
        message.setSentDate(new Date()); // set date
        return message;
    }
    
    /** send the message to @addressTo */
    public void sendMessage(final Session session, final Message m, final InternetAddress[] addressTo)
            throws MessagingException {
        // Mail versenden
        final Transport bus = session.getTransport("smtp");
        try {
            bus.connect(SYSTEM_EMAIL_HOST, getLoadBalancedAccontname(), SYSTEM_EMAIL_PASSWORD);
            bus.sendMessage(m, addressTo); // An diese Adressen senden
        } finally {
            bus.close();
        }
    }
    
    /**
     * Gets a random email from the array list of the system emails specified.
     * It returns always the same email, if only one email is specified,
     **/
    private String getLoadBalancedAccontname() {
        final int randomNumber = getRandomNumber(1, SYSTEM_EMAIL_ACCOUNTNAME.length);
        return SYSTEM_EMAIL_ACCOUNTNAME[randomNumber - 1];
        
    }
    
    /** Returns a random number between and including a minimum and maximum. */
    private int getRandomNumber(final int min, final int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
