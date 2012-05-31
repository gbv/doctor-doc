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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.dbs.entity.TestBenutzer;
import test.dbs.entity.TestKonto;

//disabled, outdated and not working test classes: TestKontoAction.class, TestLoginAction.class, TestPosition.class

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestBenutzer.class, TestKonto.class, CheckTest.class })
public class AllTests {

}
