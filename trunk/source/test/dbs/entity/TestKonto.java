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

package test.dbs.entity;


import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.dbs.entity.Konto;

public class TestKonto extends TestCase{

    private transient Konto k = new Konto();
    private final transient Long id = Long.valueOf(1);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testLoadKonto() throws SQLException {

        // initialvariabeln
        boolean cn = false;

        if (k.getSingleConnection() != null) {
            cn = true;
        }

        //    Konto laden
        k = new Konto(id, k.getSingleConnection()); //Lädt das Konto
        k.close();

        assertTrue("Beim laden des Kontos konnte keine Verbindung zur Datenbank hergestellt werden", cn);
        assertEquals("Es konnte kein Konto mit der ID " + id.toString() + " geladen werden", k.getId(), id);
    }


    //  @Test
    //  public void testNewKonto() throws SQLException {
    //
    ////    Konto laden
    //    k = new Konto(id, k.getConnection()); //Lädt das Konto
    //
    ////    k.setSubitobenutzername("blaaa");
    //    k.setBibliotheksname("BiblioTest");
    //    k.update(k.getConnection());
    //    Konto changedkonto = new Konto(id, k.getConnection());
    //    k.update(k.getConnection());
    //    k.close();
    //
    ////    assertEquals("Leider konnte der Subito-Benutzername nicht auf blaaa geändert werden", "blaaa", changedkonto.getSubitobenutzername());
    //    assertEquals("Leider konnte der Subito-Benutzername nicht auf blaaa geändert werden", "blaaa", changedkonto.getBibliotheksname());
    //  }
}