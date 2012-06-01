/** Parse comma-separated values (CSV), a common Windows file format.
 * Sample input: "LU",86.25,"11/4/1998","2:19PM",+4.0625
 * <p>
 * Inner logic adapted from a C++ original that was
 * Copyright (C) 1999 Lucent Technologies
 * Excerpted from 'The Practice of Programming'
 * by Brian W. Kernighan and Rob Pike.
 * <p>
 * Included by permission of the http://tpop.awl.com/ web site,
 * which says:
 * "You may use this code for any purpose, as long as you leave
 * the copyright notice and book citation attached." I have done so.
 * @author Brian W. Kernighan and Rob Pike (C++ original)
 * @author Ian F. Darwin (translation into Java and removal of I/O)
 * @author Ben Ballard (rewrote advQuoted to handle '""' and for readability)
 */

package util;

import java.util.ArrayList;
import java.util.List;


public class CSV {

    public static final char DEFAULT_SEP = ',';

    /** Construct a CSV parser, with the default separator (`,'). */
    public CSV() {
        this(DEFAULT_SEP);
    }

    /** Construct a CSV parser with a given separator.
     * @param sep The single char for the separator (not a list of
     * separator characters)
     */
    public CSV(final char sep) {
        fieldSep = sep;
    }

    /** The fields in the current String */
    private final transient List<String> list = new ArrayList<String>();

    /** the separator char for this parser */
    private final transient char fieldSep;

    /** parse: break the input String into fields
     * @return java.util.Iterator containing each field
     * from the original as a trimmed String, in order.
     */
    public List<String> parse(final String line) {
        final StringBuffer buffer = new StringBuffer();
        list.clear();      // recycle to initial state
        int i = 0;

        if (line.length() == 0) {
            list.add(line);
            return list;
        }

        do {
            buffer.setLength(0);
            if (i < line.length() && line.charAt(i) == '"') {
                i = advQuoted(line, buffer, ++i);  // skip quote
            } else {
                i = advPlain(line, buffer, i);
            }
            list.add(buffer.toString().trim());
            i++;
        } while (i < line.length());

        return list;
    }

    /** advQuoted: quoted field; return index of next separator */
    private int advQuoted(final String input, final StringBuffer buffer, final int i) {
        int j;
        final int len = input.length();
        for (j = i; j < len; j++) {
            if (input.charAt(j) == '"' && j + 1 < len) {
                if (input.charAt(j + 1) == '"') {
                    j++; // skip escape char
                } else if (input.charAt(j + 1) == fieldSep) { //next delimeter
                    j++; // skip end quotes
                    break;
                }
            } else if (input.charAt(j) == '"' && j + 1 == len) { // end quotes at end of line
                break; //done
            }
            buffer.append(input.charAt(j));  // regular character.
        }
        return j;
    }

    /** advPlain: unquoted field; return index of next separator */
    private int advPlain(final String input, final StringBuffer buffer, final int i) {
        int j;

        j = input.indexOf(fieldSep, i); // look for separator
        if (j == -1) {                 // none found
            buffer.append(input.substring(i));
            return input.length();
        } else {
            buffer.append(input.substring(i, j));
            return j;
        }
    }
}
