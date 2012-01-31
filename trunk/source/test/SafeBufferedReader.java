package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SafeBufferedReader extends BufferedReader {

    public SafeBufferedReader(final Reader in) {
        this(in, 1024);
    }

    public SafeBufferedReader(final Reader in, final int bufferSize) {
        super(in, bufferSize);
    }

    private transient boolean lookingForLineFeed;

    public String readLine() throws IOException {
        final StringBuffer sb = new StringBuffer("");
        while (true) {
            final int c = this.read();
            if (c == -1) { // end of stream
                if (sb.length() == 0) return null;
                return sb.toString();
            }
            else if (c == '\n') {
                if (lookingForLineFeed) {
                    lookingForLineFeed = false;
                    continue;
                }
                else {
                    return sb.toString();
                }
            }
            else if (c == '\r') {
                lookingForLineFeed = true;
                return sb.toString();
            }
            else {
                lookingForLineFeed = false;
                sb.append((char) c);
            }
        }
    }

}
