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

import java.util.concurrent.Callable;

/**
 * Holt einen Webcontent in einem neuen Thread
 *
 * @author Markus Fischer
 */
public class ThreadedWebcontent implements Callable<String> {

    private static final int TIMEOUT = 2000;
    private static final int RETRYS = 3;

    private String link;

    public ThreadedWebcontent() {
    }

    public ThreadedWebcontent(final String threadLink) {
        this.link = threadLink;
    }
    public String call() {
        final Http http = new Http();
        return http.getWebcontent(link, TIMEOUT, RETRYS);
    }

    public String getLink() {
        return link;
    }
    public void setLink(final String link) {
        this.link = link;
    }


}
