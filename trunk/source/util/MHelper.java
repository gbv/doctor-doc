//  Copyright (C) 2005 - 2013  Markus Fischer, Pascal Steiner
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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to send emails. </P></P>
 * 
 * @author Pascal Steiner, Markus Fischer
 */
public class MHelper extends AbstractReadSystemConfigurations {
    
    final Logger LOG = LoggerFactory.getLogger(MHelper.class);
    private static final String PRIORITY = "3"; // 3 = normal
    private static final String XPRIO = "X-Priority";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String TRUE = "true";
    private static final String UTF8 = "UTF-8";
    
    public void sendMail(final String[] to, final String subject, final String mailText) {
        sendMail(to, subject, mailText, PRIORITY);
    }
    
    public void sendMailReplyTo(final String[] to, final String subject, final String mailText, final String replyto) {
        sendMailReplyTo(to, subject, mailText, replyto, PRIORITY);
    }
    
    public void sendMailWithAttachement(final InternetAddress[] to, final String subject, final String mailText,
            final String replyto, final DataSource attachment, final String filename) {
        sendMailReplyTo(to, subject, mailText, replyto, PRIORITY, attachment, filename);
    }
    
    public void sendErrorMail(final String subject, final String mailText) {
        sendError(subject, mailText);
    }
    
    public void sendMailReplyTo(final String[] to, final String subject, final String mailText, final String replyto,
            final String prio) {
        
        try {
            
            // create properties and get the default Session
            final Session session = Session.getInstance(getProperties());
            
            // create a message
            final MimeMessage msg = getMimeMessage(session, prio);
            
            // set the replyTo address
            final InternetAddress[] addressReplyTo = new InternetAddress[1];
            addressReplyTo[0] = new InternetAddress(replyto);
            msg.setReplyTo(addressReplyTo);
            
            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            msg.setContent(mailText, "text/plain; charset=UTF-8");
            
            sendMailReplyTo(to, msg, replyto, prio);
        } catch (final Exception e) {
            LOG.error("sendMailReplyTo(String to[], String subject, String mailText, "
                    + "String replyto, String prio: " + e.toString());
            // Critical Error-Message
            sendError(e + "\n" + "sendMailReplyTo(String to[], String subject, String mailText, "
                    + "String from, String replyto, String prio)" + "\n" + mailText);
        }
    }
    
    private void sendMail(final String[] to, final Message msg, final String prio) {
        // set the from and to address
        try {
            final InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            
            // create properties and get the default Session
            final Session session = Session.getInstance(getProperties());
            
            //          create a message
            final MimeMessage m = getMimeMessage(session, prio);
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */
            String subject = "";
            if (msg.getSubject() != null) {
                subject = msg.getSubject();
            }
            m.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            m.setText(msg.getContent().toString(), UTF8);
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
    
    private void sendMailReplyTo(final String[] to, final Message msg, final String replyto, final String prio) {
        // set the from and to address
        try {
            final InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            
            // create properties and get the default Session
            final Session session = Session.getInstance(getProperties());
            
            //          create a message
            final MimeMessage m = getMimeMessage(session, prio);
            m.setReplyTo(new InternetAddress[] { new InternetAddress(replyto) }); // ReplyTo setzen
            m.setRecipients(Message.RecipientType.TO, addressTo); /* Damit Adressen TO sichtbar sind */
            String subject = "";
            if (msg.getSubject() != null) {
                subject = msg.getSubject();
            }
            m.setSubject(MimeUtility.encodeText(subject, UTF8, null)); // Betreff setzen
            m.setText(msg.getContent().toString(), UTF8);
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
    
    private void sendMail(final String[] to, final String subject, final String mailText, final String prio) {
        
        try {
            
            // create properties and get the default Session
            final Session session = Session.getInstance(getProperties());
            
            // create a message
            final MimeMessage msg = getMimeMessage(session, prio);
            
            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            msg.setContent(mailText, "text/plain; charset=UTF-8");
            
            sendMail(to, msg, prio);
        } catch (final Exception e) {
            LOG.error("String to[], String subject, String mailText, String from, String prio: " + e.toString());
            // Critical Error-Message
            sendError(e + "\n" + "sendMail(String to[], String subject, "
                    + "String mailText, String from, String prio)" + "\n" + mailText);
        }
    }
    
    private void sendMailReplyTo(final InternetAddress[] to, final String subject, final String mailText,
            final String replyto, final String prio, final DataSource attachment, final String filename) {
        
        try {
            
            // create properties and get the default Session
            final Session session = Session.getInstance(getProperties());
            
            // create a message
            final MimeMessage msg = getMimeMessage(session, prio);
            
            // set the replyTo address
            final InternetAddress[] addressReplyTo = new InternetAddress[1];
            addressReplyTo[0] = new InternetAddress(replyto);
            msg.setReplyTo(addressReplyTo);
            
            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(subject, UTF8, null));
            
            // create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            
            // set text message
            messageBodyPart.setText(mailText);
            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            // part two is the attachment
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(attachment));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            
            // put parts in message
            msg.setContent(multipart);
            msg.saveChanges();
            
            sendMessage(session, msg, to);
            
        } catch (final Exception e) {
            LOG.error("sendMailReplyTo(String to[], String subject, String mailText, "
                    + "String replyto, String prio: " + e.toString());
            // Critical Error-Message
            sendError(e + "\n" + "sendMailReplyTo(String to[], String subject, String mailText, "
                    + "String from, String replyto, String prio)" + "\n" + mailText);
        }
    }
    
    private void sendError(final String e) {
        try {
            final String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            final String subject = "!!! In " + APPLICATION_NAME + " ist ein Fehler aufgetreten !!! ";
            sendMail(to, subject, e);
        } catch (final Exception ee) {
            LOG.error("sendError(String e): " + ee.toString());
        }
    }
    
    private void sendError(final String subject, final String mailText) {
        try {
            final String[] to = new String[1];
            to[0] = ERROR_EMAIL;
            sendMail(to, subject, mailText);
        } catch (final Exception ee) {
            LOG.error("sendError(String subject, Exception e): " + ee.toString());
        }
    }
    
    public static void main(final String[] args) {
        
    }
    
    /** Gets the system properties */
    private Properties getProperties() {
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
    private MimeMessage getMimeMessage(final Session session, final String prio) throws MessagingException {
        // Define message
        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SYSTEM_EMAIL));
        message.setSentDate(new Date()); // set date
        //        message.addHeader("charset", UTF8);
        message.setHeader("Content-Type", "text/plain; charset=utf-8");
        message.addHeader(XPRIO, prio);
        
        return message;
    }
    
    /** send the message to @addressTo */
    private void sendMessage(final Session session, final Message m, final InternetAddress[] to)
            throws MessagingException {
        // Mail versenden
        final Transport bus = session.getTransport("smtp");
        try {
            bus.connect(SYSTEM_EMAIL_HOST, getLoadBalancedAccontname(), SYSTEM_EMAIL_PASSWORD);
            bus.sendMessage(m, to); // An diese Adressen senden
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
