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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.grlea.log.SimpleLogger;

public class Http {

    private static final SimpleLogger LOG = new SimpleLogger(Http.class);

    private static final int TIMEOUT = 2000; //Timeout in ms
    private static final int RETRYS = 3; // Anzahl versuche zum Website auslesen
    private static final String LOC = "location";


    /**
     *
     * @param link
     * @return Website als String mit fixem nicht einstellbarem timeout sowie mit 3 versuchen bei Fehler
     */
    public String getWebcontent(final String link) {
        return getWebcontent(link, TIMEOUT, RETRYS);
    }

    /**
     * @param method
     * @return Website als String mit fixem nicht einstellbarem timeout sowie mit 3 versuchen bei Fehler
     */
    public String getWebcontent(final PostMethod method) {
        return getWebcontent(method, TIMEOUT, RETRYS);
    }

    /**
     *
     * @param String link
     * @param int timeout_ms
     * @param int retrys
     * @return Liefert den inhalt einer Website als String zurück
     */
    public String getWebcontent(final String link, final int timeoutMs, final int retrys) {
        final HttpClientParams params = new HttpClientParams();
        final HttpClient client = new HttpClient(params);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(timeoutMs); // funzt!
        String contents = "";
        String redirectLocation = "";
        int trys = 0;
        boolean connection = false;
        GetMethod gmethod = new GetMethod(link.replaceAll("&amp;", "&")); // doppelt gemoppelt mit Funktion encode!
        while (!connection && trys < retrys) {
            try {
                int statusCode = client.executeMethod(gmethod);
                if (statusCode != -1) {
                    Header locationHeader = gmethod.getResponseHeader(LOC);
                    while (locationHeader != null) {
                        redirectLocation = locationHeader.getValue();
                        gmethod = new GetMethod(redirectLocation);
                        statusCode = client.executeMethod(gmethod);
                        locationHeader = gmethod.getResponseHeader(LOC);
                    }
                    // Diese Methode bringt content ohne Umbrüche
                    // d.h. content.substring+fixeZahl funktioniert nicht mehr...
                    // ausserdem werden Umlaute anders behandelt
                    ////                  Get the response
                    //          BufferedReader rd = new BufferedReader(new InputStreamReader(gmethod.
                    //          getResponseBodyAsStream()));
                    //          StringBuffer sb = new StringBuffer();
                    //          String s = null;
                    //          while ((s = rd.readLine()) != null) {
                    //            sb.append(s + "\n");
                    //          }
                    //                    contents = sb.toString();

                    contents = gmethod.getResponseBodyAsString();
                    connection = true;

                    //                    DataInputStream dis = new DataInputStream( gmethod.getResponseBodyAsStream());
                    //                    byte[] b =new byte[1000000];
                    //                    dis.read(b);
                    //                    String s = new String(b);
                    //                    contents = s;
                    //                    // Funzt nur wenn die Info im Header steht, ansonsten -1
                    //                    Long l = gmethod.getResponseContentLength();
                    //                    System.out.println(gmethod.getResponseCharSet()); //
                }

            } catch (final IOException e) {
                LOG.ludicrous("getWebContent(String link, int timeout_ms, int retrys), failure attempt: "
                        + trys + "\040" + e.toString());
                if (trys + 1 == retrys) { // beim letzten Versuch ein Email schicken
                    LOG.error("getWebContent(String link, int timeout_ms, int retrys), last attempt failed: "
                            + link + "\040" + e.toString());
                }
                trys++;
            } finally {
                gmethod.releaseConnection();
            }
        }
        return contents;
    }
    /**
     *
     * @param PostMethod method
     * @return Liefert den inhalt einer Website als String zurück
     */
    public String getWebcontent(PostMethod method, final int timeoutMs, final int retrys) {

        String content = "";
        String f = "";
        try {
            f = method.getURI().toString();
        } catch (final URIException e1) {
            LOG.error("getWebcontent(PostMethod method, int timeout_ms, int retrys): " + e1.toString());
        }
        String redirectLocation = "";

        //        Protocol myhttps = new Protocol("https", new SSLSocketFactory(), 443);

        final HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(timeoutMs); // funzt!
        boolean connection = false;
        int trys = 0;
        while (!connection && trys < retrys) {
            try {
                int statusCode = client.executeMethod(method);
                if (statusCode != -1) {
                    Header locationHeader = method.getResponseHeader(LOC);
                    while (locationHeader != null) {
                        redirectLocation = locationHeader.getValue();
                        method = new PostMethod(redirectLocation);
                        statusCode = client.executeMethod(method);
                        locationHeader = method.getResponseHeader(LOC);
                        //                        System.out.println("Location: " + redirectLocation);
                    }
                    // Diese Methode bringt content ohne Umbrüche
                    // d.h. content.substring+fixeZahl funktioniert nicht mehr...
                    // ausserdem werden Umlaute anders behandelt
                    //                    // Get the response
                    //          BufferedReader rd = new BufferedReader(new InputStreamReader(method.
                    //          getResponseBodyAsStream()));
                    //          StringBuffer sb = new StringBuffer();
                    //          String s = null;
                    //          while ((s = rd.readLine()) != null) {
                    //            sb.append(s);
                    //          }
                    //                    content = sb.toString();

                    content = method.getResponseBodyAsString();
                    connection = true;
                    //                    System.out.println();
                }

            } catch (final IOException e) {
                LOG.ludicrous("getWebContent(PostMethod method, int timeout_ms, int retrys), failure attempt: "
                        + trys + "\040" + e.toString());
                if (trys + 1 == retrys) { // letzter Versuch
                    LOG.error("getWebContent(PostMethod method, int timeout_ms, int retrys), last attempt failed: "
                            + e.toString() + "\012" + f);
                }
                trys++;
            } finally {
                method.releaseConnection();
            }
        }
        return content;
    }

    /**
     *
     * @param link
     * @param postdata
     * @return
     */
    public String getWebcontent(final String link, final String postdata) {

        final StringBuffer response = new StringBuffer();
        try {
            DisableSSLCertificateCheckUtil.disableChecks();
            // Send data
            final URL url = new URL(link);
            final URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            final OutputStreamWriter wr = new OutputStreamWriter(conn
                    .getOutputStream());
            wr.write(postdata);
            wr.flush();

            // Get the response
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            String content = "";
            while ((content = rd.readLine()) != null) {
                response.append(content);
                response.append('\n');
            }
            wr.close();
            rd.close();
        } catch (final Exception e) {
            LOG.error("getWebcontent(String link, String postdata): "
                    + e.toString() + "\012" + link + "\012" + postdata);
        }

        return response.toString();
    }
}
