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

import java.util.ArrayList;
import java.util.concurrent.Callable;

import ch.dbs.actions.bestellung.OrderAction;
import ch.dbs.form.JournalDetails;
import ch.dbs.form.OrderForm;

/**
 * Holt einen Webcontent in einem neuen Thread
 *
 * @author Markus Fischer
 */
public class ThreadedJournalSeek implements Callable<ArrayList<JournalDetails>> {

    private String zeitschriftentitel_encoded;
    private String artikeltitel_encoded;
    private OrderForm pageForm;
    private String concurrCopyTitle;

    public ThreadedJournalSeek() {
    }

    public ThreadedJournalSeek(final String zeitschriftentitel_enc, final String artikeltitel_enc, final OrderForm of,
            String concurrentCopyZeitschriftentit) {
        this.zeitschriftentitel_encoded = zeitschriftentitel_enc;
        this.artikeltitel_encoded = artikeltitel_enc;
        this.pageForm = of;
        this.concurrCopyTitle = concurrentCopyZeitschriftentit;
    }
    public ArrayList<JournalDetails> call() {
        final OrderAction oa = new OrderAction();
        final ArrayList<JournalDetails> jd = oa.searchJournalseek(zeitschriftentitel_encoded,
                artikeltitel_encoded, pageForm, concurrCopyTitle);
        return jd;
    }

    public String getZeitschriftentitel_encoded() {
        return zeitschriftentitel_encoded;
    }

    public void setZeitschriftentitel_encoded(String zeitschriftentitelEncoded) {
        zeitschriftentitel_encoded = zeitschriftentitelEncoded;
    }

    public String getArtikeltitel_encoded() {
        return artikeltitel_encoded;
    }

    public void setArtikeltitel_encoded(String artikeltitelEncoded) {
        artikeltitel_encoded = artikeltitelEncoded;
    }

    public OrderForm getPageForm() {
        return pageForm;
    }

    public void setPageForm(OrderForm pageForm) {
        this.pageForm = pageForm;
    }

    public String getConcurrentCopyZeitschriftentitel() {
        return concurrCopyTitle;
    }

    public void setConcurrentCopyZeitschriftentitel(
            String concurrentCopyZeitschriftentitel) {
        this.concurrCopyTitle = concurrentCopyZeitschriftentitel;
    }

}