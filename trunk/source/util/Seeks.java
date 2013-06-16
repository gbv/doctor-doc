//  Copyright (C) 2013  Markus Fischer
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

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.form.SeeksForm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import enums.Connect;

public class Seeks {

    private static final Logger LOG = LoggerFactory.getLogger(Seeks.class);

    /**
     * Performs a query to a Seeks server. The query must be natural text. 
     * The query will get UTF-8 encoded an embedded in a search URL to a random
     * Seeks server. In case of an unresponsive Seeks server, it will retry to
     * query (other) Seeks server.
     **/
    public List<SeeksForm> search(final String query) {

        // http://seeks-project.info/wiki/index.php/API-0.4.0

        final List<SeeksForm> result = new ArrayList<SeeksForm>();

        final List<SeeksForm> firstPriority = new ArrayList<SeeksForm>();
        final List<SeeksForm> secondPriority = new ArrayList<SeeksForm>();
        final List<SeeksForm> thirdPriority = new ArrayList<SeeksForm>();

        try {

            final Http http = new Http();
            String json = http.getContent(composeSearch(query), Connect.TIMEOUT_3.getValue(),
                    Connect.TRIES_1.getValue(), "utf-8");

            // retry, possibly with other servers
            int retry = 0;
            while ((json == null || "".equals(json)) && retry < 2) {
                json = http.getContent(composeSearch(query), Connect.TIMEOUT_3.getValue(), Connect.TRIES_1.getValue(),
                        "utf-8");
                retry++;
            }

            // we may get back a null answer
            if (json != null && !"".equals(json)) {

                final JsonElement jsonRoot = new JsonParser().parse(json);

                final JsonArray jsonSnippets = jsonRoot.getAsJsonObject().getAsJsonArray("snippets");

                for (final JsonElement jsonElement : jsonSnippets) {
                    final SeeksForm record = new SeeksForm();
                    // we should always have an ID
                    record.setId(jsonElement.getAsJsonObject().get("id").getAsString());
                    // we should always have a title
                    record.setTitle(org.apache.commons.lang.StringEscapeUtils.unescapeHtml(Jsoup.clean(jsonElement
                            .getAsJsonObject().get("title").getAsString(), Whitelist.none()))); // clean possible HTML entities
                    // we should always have an URL
                    record.setUrl(jsonElement.getAsJsonObject().get("url").getAsString());
                    // type may be null
                    if (jsonElement.getAsJsonObject().get("type") != null) {
                        record.setType(jsonElement.getAsJsonObject().get("type").getAsString());
                    }
                    // summary may be null
                    if (jsonElement.getAsJsonObject().get("summary") != null) {
                        record.setSummary(jsonElement.getAsJsonObject().get("summary").getAsString());
                    }

                    // improving existing relevance sorting
                    if ("file".equals(record.getType()) && compareTitle(record, query)) {
                        firstPriority.add(record);
                    } else if (compareTitle(record, query)) {
                        secondPriority.add(record);
                    } else {
                        thirdPriority.add(record);
                    }
                }
            }

        } catch (final Exception e) {
            LOG.error(e.toString());
        } finally {
            // create order:
            // type "file" and matching title
            result.addAll(firstPriority);
            // matching title
            result.addAll(secondPriority);
            // the rest
            result.addAll(thirdPriority);
        }

        return result;
    }

    /**
     * Compare method, used to improve the relevance sorting of the results,
     * returned by the Seeks server.
     **/
    private boolean compareTitle(final SeeksForm record, final String query) {

        final String compare = prepareCompare(record.getTitle().substring(0, record.getTitle().length() / 2));

        if (prepareCompare(query).startsWith(compare)) {
            return true;
        }

        return false;
    }

    /**
     * Prepares a String for comparison: converts
     * string to lower case, resolves ß to ss and removes
     * all non letter and non digit characters.
     */
    private String prepareCompare(String input) {

        if (input == null) {
            return "";
        }

        input = input.toLowerCase();
        input = input.replaceAll("ß", "ss");

        // remove all non-letter characters (including spaces)
        final StringBuffer strBuff = new StringBuffer();
        char c;

        final int max = input.length();
        for (int i = 0; i < max; i++) {
            c = input.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                strBuff.append(c);
            }
        }

        return strBuff.toString();
    }

    /**
     * Creates a UTF-8 encoded search URL to a random Seeks server, specified in SystemConfiguration
     **/
    private String composeSearch(final String query) throws IllegalArgumentException {

        // final String search = "http://seeks.ru/search/txt/Refined+prediction+of+week+12+response+and+SVR+based+on+week+4+response+in+HCV+genotype+1+patients?output=json";

        if (query == null || "".equals(query)) {
            throw new IllegalArgumentException("query must not be empty!");
        }

        final CodeUrl coder = new CodeUrl();

        // get Seeks server to query
        final StringBuffer buf = new StringBuffer(128);
        buf.append(getDistributedSeekServer());
        if (buf.charAt(buf.length() - 1) != '/') {
            buf.append("/");
        }
        buf.append("search/txt/");
        buf.append(coder.encode(query, "utf-8"));
        buf.append("?output=json");
        buf.append("&rpp=10"); // limit number of records

        return buf.toString();
    }

    /**
     * Gets a random Seeks server from the array list of the servers specified.
     * It returns always the same server, if only one server is specified,
     **/
    private String getDistributedSeekServer() {
        final int randomNumber = getRandomNumber(1, ReadSystemConfigurations.getSeeksServer().length);
        return ReadSystemConfigurations.getSeeksServer()[randomNumber - 1];

    }

    /** Returns a random number between and including a minimum and maximum. */
    private int getRandomNumber(final int min, final int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

}
