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

package test;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.dbs.actions.user.TestKontoAction;
import test.dbs.actions.user.TestLoginAction;
import test.dbs.entity.TestBenutzer;
import test.dbs.entity.TestKonto;

public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for test.dbs.entity");
        //$JUnit-BEGIN$
        suite.addTestSuite(TestBenutzer.class);
        suite.addTestSuite(TestKonto.class);
        suite.addTestSuite(TestKontoAction.class);
        suite.addTestSuite(TestLoginAction.class);
        //    suite.addTestSuite(TestPosition.class);
        //$JUnit-END$
        return suite;
    }

}
