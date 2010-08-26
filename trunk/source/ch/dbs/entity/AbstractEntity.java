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

package ch.dbs.entity;

import java.sql.Timestamp;
import java.util.Date;

import util.DBConn;

/**
 * Basis abstract class für die meisten tabellen innerhalb dbs. Beinhaltet
 * <EM>lastModification</EM> eigenschaften
 * <p/>
 * Der defaultwert für <EM>lastModification</EM> ist die Erstellungszeit der Klasse
 * <p/>
 * @author Pascal Steiner
 * @version $Id$
 */
public abstract class AbstractEntity extends DBConn {

    private Timestamp lastModification = new Timestamp(new Date().getTime());

    public Timestamp getLastModification() {
        return lastModification;
    }

    public void setLastModification(final Timestamp lastModification) {
        this.lastModification = lastModification;
    }

}
