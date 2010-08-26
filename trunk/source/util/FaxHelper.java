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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.grlea.log.SimpleLogger;

import ch.dbs.entity.Fax;
import ch.dbs.entity.Konto;

public class FaxHelper extends AbstractReadSystemConfigurations {

    private static final SimpleLogger LOG = new SimpleLogger(FaxHelper.class);
    private static final int WAIT = 6000;

    /**
     * Hilfsklasse um Faxe von popfax.com abzuholen und zu verwalten
     *
     * @author Markus Fischer
     */
    public FaxHelper() {


        // All requests to execute necessary actions are sent to the server as a HTTP POST
        // request over a secured link (https) with specific data. Communication is established over HTTPS protocol to:
        // https://api.popfax.com/api-server.php

        // Server will process the request and will send back a response as XML Document or, for
        // some specific actions (like RetrieveFaxImage), a document.

        // ********************************************
        // Dieser Teil vermutlich nur zum Senden benötigt!
        // The client must send a Content-Type HTTP header, with the content type set to: multipart/form-data
        // The client shall include the character encoding of the content being sent a
        // 8 in the charset attribute of the Content-Type header.
        // For example, a request might look like:
        // POST /api-server.php HTTP/1.1
        // Host: api.popfax.com
        // Content-Type: multipart/form-data; charset=utf-8
        // ********************************************

    }

    /**
     * Holt ein Fax ab und leitet diesen an die Mailadresse des Bibliothekskontos weiter
     *
     * @param k
     * @param faxId
     */
    public void forwardFaxToMail(final Konto k, final String faxId) throws FaxHelperException {
        //      //Post Methode vorbereiten um einen Fax abzuholen

        final String link = "https://api.popfax.com/api-server.php";
        String data = "";
        URLConnection conn = null;
        OutputStreamWriter wr = null;
        try {
            data = URLEncoder.encode("action", "UTF-8") + "="
            + URLEncoder.encode("get_fax_file", "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "="
            + URLEncoder.encode(k.getFaxusername(), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
            + URLEncoder.encode(k.getFaxpassword(), "UTF-8");
            data += "&" + URLEncoder.encode("faxId", "UTF-8") + "="
            + URLEncoder.encode(faxId, "UTF-8");
            data += "&" + URLEncoder.encode("fileType", "UTF-8") + "="
            + URLEncoder.encode("PDF", "UTF-8");

            DisableSSLCertificateCheckUtil.disableChecks();
            //       Send data
            final URL url = new URL(link);
            conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn
                    .getOutputStream());
            wr.write(data);
            wr.flush();

        } catch (final Exception e) {
            // Critical Error-Message
            final MHelper m = new MHelper();
            final String f = "Beim Fax weiterleiten ist ein Fehler während dem Kontaktieren von Popfax aufgetreten: \n"
                + e.toString();
            m.sendError(f);
        } finally {
            try { wr.close(); } catch (final IOException e) {
                LOG.error("forwardFaxToMail, while closing OutputStreamWriter: " + e.toString());
            }
        }

        //     hier kann es sein, dass statt ein Fax eine Fehlermeldung daher kommt... z.B.
        //    <?xml version="1.0" encoding="utf-8"?>
        //    <API_RESULT>
        //      <AUTHENTICATION>
        //        <RESULT>0</RESULT>
        //      </AUTHENTICATION>
        //      <ERROR>201</ERROR>
        //    </API_RESULT>

        //Mail vorbereiten
        final InternetAddress[] addressTo = new InternetAddress[1];
        try {
            addressTo[0] = new InternetAddress(k.getDbsmail());
            InternetAddress[] addressFrom = null;
            addressFrom = InternetAddress.parse(SYSTEM_EMAIL);

            //          Nachricht erstellen
            final MHelper mh = new MHelper();
            final Session session = mh.getSession();
            final Message m = new MimeMessage(session);
            m.setFrom(addressFrom[0]); // From setzen
            m.setRecipients(Message.RecipientType.TO, addressTo);

            final MimeMultipart mcontent = new MimeMultipart("multipart"); //Definition dass Mail Anhänge hat

            //Text der Nachricht
            final MimeBodyPart text = new MimeBodyPart();
            text.setText("Sehr geehrter Kunde\nSehr geehrte Kundin\n\nAnbei ein von Ihnen per Fax bestellter Artikel "
                    + "im PDF-Format." + "\n\nFuer Reklamationen wenden Sie sich bitte direkt an den Lieferanten "
                    + "(z.B. Subito) oder an Ihre zustaendige Bibliothek: " + k.getBibliotheksmail() + "\n\nFuer die "
                    + "Einhaltung der mit den zugestellten Materialien verbundenen Urheber-, Persoenlichkeits- und "
                    + "sonstigen Rechte sind Sie allein verantwortlich. Wir weisen insbesondere darauf hin, dass Sie "
                    + "die von uns zugestellten Dokumentkopien ausschliesslich zum privaten oder sonstigen eigenen "
                    + "Gebrauch verwenden duerfen. Die gelieferten Dokumente duerfen Sie weder entgeltlich noch "
                    + "unentgeltlich, weder in Papierform noch als elektronische Kopie, weiterverbreiten.\n\nMit "
                    + "freundlichen Gruessen\n\nTeam " + ReadSystemConfigurations.getApplicationName()
                    + "\nhttp://www.doctor-doc.com");
            text.setHeader("MIME-Version" , "1.0");
            text.setHeader("Content-Type" , text.getContentType());
            mcontent.addBodyPart(text);

            //Fax anhängen
            final InputStreamDataSource isds = new InputStreamDataSource(faxId + ".pdf", "application/pdf", conn
                    .getInputStream());
            final BodyPart faxattachement = new MimeBodyPart();
            faxattachement.setDataHandler(new DataHandler(isds));
            faxattachement.setHeader("MIME-Version" , "1.0");
            faxattachement.setHeader("Content-Type" , "application/pdf");
            faxattachement.setFileName(faxId + ".pdf");
            //            System.out.println(faxattachement.getContentType());
            mcontent.addBodyPart(faxattachement);

            //Nachricht fertigstellen
            m.setContent(mcontent); // Inhalt/Attachements der Mail anhngen
            m.setHeader("MIME-Version" , "1.0");
            m.setHeader("Content-Type" , mcontent.getContentType());
            m.setHeader("X-Mailer", "Java-Mailer V 1.60217733");
            m.setSentDate(new Date()); // Sendedatum setzen
            final String subject = "Artikellieferung Nr. " + faxId + " - Fax to Email";
            m.setSubject(subject); // Betreff setzen
            m.saveChanges();


            final SimpleLineReader slr = new SimpleLineReader(mcontent.getBodyPart(1).getDataHandler().getInputStream());

            if (!slr.readLine().contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
                // bei gültigem Fax ist erste Zeile: %PDF-1.2
                //              System.out.println("gültiger Fax");

                //          Mail versenden
                final Transport bus = session.getTransport("smtp");
                bus.connect(SYSTEM_EMAIL_HOST, SYSTEM_EMAIL_ACCOUNTNAME, SYSTEM_EMAIL_PASSWORD);
                bus.sendMessage(m, addressTo); // An diese Adressen senden
                bus.close();

            } else {
                // bei Fehler kommt ein XML zurück
                System.out.println("ungültiger Fax");
                final FaxHelperException ex = new FaxHelperException();
                throw ex;
            }

        } catch (final AddressException e) {
            // Critical Error-Message
            final MHelper mh = new MHelper();
            mh.sendError("Fehler in der Mailadresse des Kontos " + k.getBibliotheksname(), e);
        } catch (final MessagingException e) {
            // Critical Error-Message
            final MHelper mh = new MHelper();
            mh.sendError("Fehler beim im FaxHelper.forwardFaxToMail des Kontos " + k.getBibliotheksname(), e);
        } catch (final IOException e) {
            // Critical Error-Message
            final MHelper mh = new MHelper();
            mh.sendError("Fehler beim Fax als Attachement anhängen. Kunde: " + k.getBibliotheksname(), e);
        }
    }

    /**
     * Holt die Liste in XML aller Faxe eines Faxserver-Kontos ab
     * <p></p>
     * @param
     * @return
     */
    public void retrieveIncomingFaxList(final Konto k) {

        // action=retrieve_incoming_fax_list [int?]
        // username=email [popfax-email | String]
        // password=password [popfax-password | String]
        // dateFrom = [The date shall be specified in the format YYYY-MM-DD HH24:MI:SS | String]
        // dateTo = [The date shall be specified in the format YYYY-MM-DD HH24:MI:SS | String]

        // Return Values XML-File :
        // RETRIEVE_INCOMING_FAX_LIST [Successful retrieval, result=0; 'Error codes'=107, 406 | int]
        // FAX [Each fax of fax list is delimited by <FAX></FAX>. The included field are faxId,
        // sender, date and pages count | none]
        // FAX_ID [Fax identificator as set by Popfax.com system | int]
        // FROM [Sender fax number (if declared) | String]
        // DATE [Sent fax date in the format YYYY-MM-DD HH24:MI:SS | String]
        // PAGES [Pages count | int]

        // Note : Please note that for security reasons M2M will apply limit of no more than 1000
        // records per one request. In case of more than 1000 messages available for the
        // specified period, the system will return corresponding error. In such cases we
        // recommend to decrease the time interval and try again.

        final Fax f = new Fax();

        final String link = "https://api.popfax.com/api-server.php";
        String data = "";
        try {
            data = URLEncoder.encode("action", "UTF-8") + "="
            + URLEncoder.encode("retrieve_incoming_fax_list", "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "="
            + URLEncoder.encode(k.getFaxusername(), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
            + URLEncoder.encode(k.getFaxpassword(), "UTF-8");
            data += "&" + URLEncoder.encode("dateFrom", "UTF-8") + "="
            + URLEncoder.encode("2009-10-01 00:00:00", "UTF-8");
            data += "&" + URLEncoder.encode("dateTo", "UTF-8") + "="
            + URLEncoder.encode("2010-12-31 00:00:00", "UTF-8");
        } catch (final UnsupportedEncodingException e1) {
            // Critical Error-Message
            e1.printStackTrace();
            final MHelper mh = new MHelper();
            mh.sendErrorMail("Faxserver Abfrage-URL konnte nicht erstellt werden", "retrieve_incoming_fax_list:  - "
                    + e1.toString() + " KID: " + k.getId() + "Konto: " + k.getBibliotheksname());

        }


        final Http http = new Http();

        String contents = http.getWebcontent(link, data);

        f.setKid(String.valueOf(k.getId()));


        if (contents != null && contents.contains("<API_RESULT>") && !contents.contains("<ERROR>")) {
            //        System.out.println("Faxliste erhalten:\012");

            f.setState("23"); // Faxserver aktiv

            String contentCopy = contents;

            while (contents.contains("<FAX>")) {

                contentCopy = contentCopy.substring(contentCopy.indexOf("<FAX>"), contentCopy.indexOf("</FAX>") + 6);
                f.setPopfaxid(contentCopy.substring(contentCopy.indexOf("<FAX_ID>") + 8, contentCopy
                        .indexOf("</FAX_ID>")));
                if (contentCopy.contains("<FROM>")) {
                    f.setFrom(contentCopy.substring(contentCopy.indexOf("<FROM>") + 6, contentCopy.indexOf("</FROM>")));
                }
                if (contentCopy.contains("<DATE>")) {
                    f.setPopfaxdate(contentCopy.substring(contentCopy.indexOf("<DATE>") + 6, contentCopy
                            .indexOf("</DATE>")));
                }
                if (contentCopy.contains("<PAGES>")) {
                    f.setPages(contentCopy.substring(contentCopy.indexOf("<PAGES>") + 7, contentCopy
                            .indexOf("</PAGES>")));
                }
                f.setState("20"); // Status erhalten
                f.setStatedate("NOW()"); // wird eigentlich in Methode saveNewFax als Default geschrieben...

                if (f.getFax(f.getPopfaxid(), f.getKid()).getId() == null) { // Prüfen, ob der Eintrag bereits besteht

                    System.out.println("KID: " + f.getKid());
                    System.out.println("Status: " + f.getState());
                    System.out.println("fax_id: " + f.getPopfaxid());
                    System.out.println("from: " + f.getFrom());
                    System.out.println("date: " + f.getPopfaxdate());
                    System.out.println("pages: " + f.getPages() + "\012");


                    //Fax per Mail weiterleiten, wenn kein Spam von Popfax (+33170447011 und +4969975392378)
                    if (!f.getFrom().equalsIgnoreCase("Popfax.com") && !f.getFrom().contains("+331704")
                            && !f.getFrom().contains("+4969975392378")) {
                        final FaxHelper fh = new FaxHelper();
                        final Lock lock = new ReentrantLock();
                        lock.lock();
                        try {
                            synchronized (fh) {
                                System.out.println("Warten auf Popfaxunlock");
                                fh.wait(WAIT);
                                // 6 sekunden warten, da nur alle 5 s auf popfax zugegriffen werden darf

                                try {
                                    fh.forwardFaxToMail(k, f.getPopfaxid());
                                    f.saveNewFax();
                                } catch (final FaxHelperException e) {
                                    // Critical Error-Message
                                    LOG.error(e.toString());
                                    final MHelper mh = new MHelper();
                                    final StringBuffer bf = new StringBuffer();
                                    bf.append("Fehler Faxserver - ");
                                    bf.append(e.toString());
                                    bf.append(" KID: ");
                                    bf.append(k.getId());
                                    bf.append(" Fax-ID: ");
                                    bf.append(f.getPopfaxid());
                                    mh.sendErrorMail("!!!Faxholperer!!!", bf.toString());

                                }


                            }
                        } catch (final InterruptedException e) {
                            // Critical Error-Message
                            LOG.error(e.toString());
                            final MHelper mh = new MHelper();
                            final StringBuffer bf = new StringBuffer();
                            bf.append("Fehler Faxserver - ");
                            bf.append(e.toString());
                            bf.append(" KID: ");
                            bf.append(k.getId());
                            bf.append(" Fax-ID: ");
                            bf.append(f.getPopfaxid());
                            mh.sendErrorMail("!!!Faxserver: InterruptedException!!!", bf.toString());
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                        //              System.out.println("Und weiter...");
                    }
                }

                contents = contents.substring(contents.indexOf("</FAX>") + 6);
                contentCopy = contents;

            }

        } else {
            LOG.error("Faxserver-Error! retrieveIncomingFaxList! API-Anwort:\012" + contents);
            f.setState("24"); // Faxserver Verbindungsfehler!
            f.saveFaxRunStati(f);

            final MHelper mh = new MHelper();
            mh.sendErrorMail("!!!Fehler auf Faxserver!!! - Verbindung fehlgeschlagen", "Fehler Faxserver - "
                    + "retrieveIncomingFaxList. API-Antwort:\012" + contents);
        }

    }


    /**
     * Löscht Faxe nach ID aus einem Faxserver-Konto
     * <p></p>
     * @param ID
     * @return
     */
    public void deleteFax() {

        // action=delete_fax [int]
        // username=email [popfax-email | String]
        // password=password [popfax-password | String]
        // faxId= [Fax identificator of the fax to be deleted | int]

        // Return Values: Successful delete, Result=0; 'Error codes'=107

        // Note : once deleted, the fax image can not be retrieved anymore, it will not appear in
        // the Incoming or Outgoing lists and will disappear from customer Inbox and Outbox in
        // web interface.

    }

}


