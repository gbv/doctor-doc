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

import java.util.List;

import ch.dbs.entity.Konto;

public class FaxserverCronjob {

    /**
     * Cronjob um das Abholen und Weiterleiten der Faxe von popfax durchzuführen
     * @param args
     */
    public FaxserverCronjob() {

        // Ausgangslage: xy-Faxserverkonten bei Popfax => schickt bei Faxempfang ein Email auf xy@doctor-doc.com
        // Szenario 1: regelmässiges (5-10 Min.) Abfragen aller Emailkonten auf doctor-doc => ggf. Aufruf von FaxHelper
        // Szenario 2: weiterleiten aller doctor-doc Emails auf ein zentrales Email => ggf.FaxHelper
        // Szenario 3: regelmässiges (10-15 Min.) direktes Abfragen aller Faxkonten bei popfax mittels FaxHelper.
        // Lokales Emailkonto nur als Buffer/Backup

    }

    /**
     * Kontrolliert für alle Kontos welche Popfax besitzen, ob ein neuer Fax eingetroffen ist und
     * liefert diesen gegebenenfalls an die im Konto konfigurierte Mailadresse aus
     */
    public void checkPopfaxEmail() {
        Konto kto = new Konto();
        List<Konto> kontos = kto.getFaxserverKontos();
        FaxHelper fh = new FaxHelper();
        for (Konto k : kontos) {
            //        System.out.println("Zur zeit wird dieses Konto bearbeitet: " + k.getBibliotheksname());
            fh.retrieveIncomingFaxList(k);
        }

    }



}


