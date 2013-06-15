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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.dbs.form.SeeksForm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import enums.Connect;

public class Seeks {

    private static final Logger LOG = LoggerFactory.getLogger(Seeks.class);

    public List<SeeksForm> search(final String query) {

        final List<SeeksForm> result = new ArrayList<SeeksForm>();

        final List<SeeksForm> firstPriority = new ArrayList<SeeksForm>();
        final List<SeeksForm> secondPriority = new ArrayList<SeeksForm>();

        try {

            final Http http = new Http();
            String json = http.getContent(composeSearch(query), Connect.TIMEOUT_3.getValue(),
                    Connect.TRIES_1.getValue(), "utf-8");

            // retry, possibly with other servers
            int retry = 0;
            while ((json == null || "".equals(json)) && retry < 3) {
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
                    record.setTitle(jsonElement.getAsJsonObject().get("title").getAsString());
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

                    // type may be null
                    if (compareTitle(record, query)) {
                        firstPriority.add(record);
                    } else {
                        secondPriority.add(record);
                    }
                }
            }

        } catch (final Exception e) {
            LOG.error(e.toString());
        } finally {
            // create order by file hits first
            result.addAll(firstPriority);
            result.addAll(secondPriority);
        }

        return result;
    }

    private boolean compareTitle(final SeeksForm record, final String query) {

        final String compare = record.getTitle().substring(0, record.getTitle().length() / 2).toLowerCase();

        if (query.toLowerCase().startsWith(compare)) {
            return true;
        }

        return false;
    }

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
     * Gets a random seeks server from the array list of the servers specified.
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
