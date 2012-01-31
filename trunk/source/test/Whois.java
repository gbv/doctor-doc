package test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Whois {

    public final static int DEFAULT_PORT = 43;
    public final static String DEFAULT_HOST = "rpsl.ripe.net";

    private transient int port = DEFAULT_PORT;
    private final transient InetAddress host;

    public Whois(final InetAddress host, final int port) {
        this.host = host;
        this.port = port;
    }

    public Whois(final InetAddress host) {
        this(host, DEFAULT_PORT);
    }

    public Whois(final String hostname, final int port)
            throws UnknownHostException {
        this(InetAddress.getByName(hostname), port);
    }

    public Whois(final String hostname) throws UnknownHostException {
        this(InetAddress.getByName(hostname), DEFAULT_PORT);
    }

    public Whois() throws UnknownHostException {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    // Items to search for
    public static class SearchFor {

        public static final SearchFor ANY = new SearchFor();
        public static final SearchFor NETWORK = new SearchFor();
        public static final SearchFor PERSON = new SearchFor();
        public static final SearchFor HOST = new SearchFor();
        public static final SearchFor DOMAIN = new SearchFor();
        public static final SearchFor ORGANIZATION = new SearchFor();
        public static final SearchFor GROUP = new SearchFor();
        public static final SearchFor GATEWAY = new SearchFor();
        public static final SearchFor ASN = new SearchFor();

        private SearchFor() {};

    }

    // Categories to search in
    public static class SearchIn {

        public static final SearchIn ALL = new SearchIn();
        public static final SearchIn NAME = new SearchIn();
        public static final SearchIn MAILBOX = new SearchIn();
        public static final SearchIn HANDLE = new SearchIn();

        private SearchIn() {};

    }

    public String lookUpNames(final String target, final SearchFor category,
            final SearchIn group, final boolean exactMatch) throws IOException {

        String suffix = "";
        if (!exactMatch) suffix = ".";

        String searchInLabel  = "";
        String searchForLabel = "";

        if (group == SearchIn.ALL) searchInLabel = "";
        else if (group == SearchIn.NAME) searchInLabel = "Name ";
        else if (group == SearchIn.MAILBOX) searchInLabel = "Mailbox ";
        else if (group == SearchIn.HANDLE) searchInLabel = "!";

        if (category == SearchFor.NETWORK) searchForLabel = "Network ";
        else if (category == SearchFor.PERSON) searchForLabel = "Person ";
        else if (category == SearchFor.HOST) searchForLabel = "Host ";
        else if (category == SearchFor.DOMAIN) searchForLabel = "Domain ";
        else if (category == SearchFor.ORGANIZATION) {
            searchForLabel = "Organization ";
        }
        else if (category == SearchFor.GROUP) searchForLabel = "Group ";
        else if (category == SearchFor.GATEWAY) {
            searchForLabel = "Gateway ";
        }
        else if (category == SearchFor.ASN) searchForLabel = "ASN ";

        final String prefix = searchForLabel + searchInLabel;
        final String query = prefix + target + suffix;

        final Socket theSocket = new Socket(host, port);
        final Writer out
        = new OutputStreamWriter(theSocket.getOutputStream(), "ASCII");
        final SafeBufferedReader in = new SafeBufferedReader(new
                InputStreamReader(theSocket.getInputStream(), "ASCII"));
        out.write(query + "\r\n");
        out.flush();
        final StringBuffer response = new StringBuffer();
        String theLine = null;
        while ((theLine = in.readLine()) != null) {
            response.append(theLine);
            response.append("\r\n");
        }
        theSocket.close();

        return response.toString();

    }

    public InetAddress getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

}
