//  Copyright (C) 2015  Markus Fischer
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

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class uses HikariCP to provide pooled connection.
 * </P></P>
 * @author Markus Fischer
 */
public class PooledConnections {

    private final Logger LOG = LoggerFactory.getLogger(PooledConnections.class);

    private transient Connection cn;

    public Connection getConnection() {

        if (cn == null) {
            cn = HikariPool.getInstance().getConnection();
        }

        return cn;
    }

    public void close() {
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (final Exception e) {
            LOG.error("close(): " + e.toString());
        }
    }

}
