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
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

public class HikariPool extends AbstractReadSystemConfigurations {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String SERVER = "jdbc:mysql://127.0.0.1/" + DATABASE_NAME
            + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false";

    private static final long serialVersionUID = 1L;
    private final Logger LOG = LoggerFactory.getLogger(HikariPool.class);

    private static HikariPool instance = null;
    private HikariDataSource ds = null;
    private Connection cn;


    static {
        try {
            instance = new HikariPool();
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private HikariPool() {
        ds = new HikariDataSource();

        ds.setMaximumPoolSize(10);
        ds.setDriverClassName(DRIVER);
        ds.setJdbcUrl(SERVER);
        ds.setUsername(DATABASE_USER);
        ds.setPassword(DATABASE_PASSWORD);
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts", "true");
    }

    public static HikariPool getInstance() {
        return instance;
    }

    public Connection getConnection() {

        try {
            cn = ds.getConnection();
        } catch (final SQLException e) {
            LOG.error("getConnection: " + e.toString());
            // Critical error-message
            final MHelper mh = new MHelper(e, "HikariPool: error in getConnection!!!");
            mh.sendError();
        }
        return cn;
    }

}
