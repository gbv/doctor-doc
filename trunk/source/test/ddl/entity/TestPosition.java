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

package test.ddl.entity;


import java.util.Date;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.PrepareTestObjects;
import util.ThreadSafeSimpleDateFormat;
import ch.dbs.entity.AbstractBenutzer;
import ch.dbs.entity.Konto;
import ch.ddl.entity.Position;

public class TestPosition extends TestCase{

    private static Konto k = new Konto();

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

    @Test //@BeforeClass funzt nicht
    public void testSetUp() {
        final Konto tz = new Konto(); // we need this for setting a default timezone
        final Date d = new Date();
        final ThreadSafeSimpleDateFormat fmt = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String datum = fmt.format(d, tz.getTimezone());
        final AbstractBenutzer b = PrepareTestObjects.getBibliothekar();
        b.setDatum(datum);
        b.saveNewUser(b, tz, k.getSingleConnection());
        k.close();
    }

    @Test
    public void testSaveLoadUpdateDeletePostionen() {

        // Test
        final Position p = PrepareTestObjects.getPosition();
        p.save(p.getSingleConnection());
        Position loadestpos = new Position(p.getId(), p.getSingleConnection());
        loadestpos.setAutor("modified");
        loadestpos.update(p.getSingleConnection());
        loadestpos = new Position(p.getId(), p.getSingleConnection());
        final Boolean del = p.deleteSelf(p.getSingleConnection());
        p.close();

        // Prüfung
        assertNotNull("Es konnte keine Verbindung zur Datenbank hergestellt werden", p.getSingleConnection());
        assertNotNull("Positionen wurde nicht gespeichert", p.getId());
        assertNotNull("Laden von Positionen hat nicht geklappt", loadestpos.getId());
        assertEquals("Update von Positionen hat nicht geklappt", "modified", loadestpos.getAutor());
        assertTrue("Löschen von Positionen hat nicht geklappt", del);
    }

    @Test
    public void testFillPositionenFromFormBean() {
        //    PositionForm pf  = PrepareTestObjects.getPositionForm();
        //    Position p = new Position(pf);
        //    assertNotNull(p.getId());
    }

    @Test //@AfterClass funzt nicht
    public void testTearDownUp() {
        final AbstractBenutzer b = new AbstractBenutzer();
        b.deleteUser(PrepareTestObjects.getTestBibliothekarFromDb(k.getSingleConnection()), k.getSingleConnection());
        k.close();
    }

}
