//  Copyright (C) 2012  Markus Fischer
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

package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Check;

public class CheckTest {

    private final Check check = new Check();

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
    public void testIsEmail() throws Exception {
        assertTrue(check.isEmail("test@test.ch"));
        assertFalse(check.isEmail("test@testch"));
        assertFalse(check.isEmail("test@test.c"));
        assertFalse(check.isEmail("test@"));
        assertFalse(check.isEmail("test"));
        assertFalse(check.isEmail("@"));
        assertFalse(check.isEmail(""));
        assertFalse(check.isEmail(null));
    }

    @Test
    public void testIsMinLength() throws Exception {
        assertTrue(check.isMinLength("", 0));
        assertTrue(check.isMinLength("Z", 0));
        assertTrue(check.isMinLength("Z", 1));
        assertFalse(check.isMinLength("Z", 2));
        assertTrue(check.isMinLength("ABC", 3));
        assertFalse(check.isMinLength(null, 0));
    }

    @Test
    public void testIsExactLength() throws Exception {
        assertTrue(check.isExactLength("", 0));
        assertFalse(check.isExactLength("Z", 0));
        assertTrue(check.isExactLength("Z", 1));
        assertFalse(check.isExactLength("Z", 2));
        assertTrue(check.isExactLength("ABC", 3));
        assertFalse(check.isExactLength(null, 0));
    }

    @Test
    public void testIsLengthInMinMax() throws Exception {
        assertTrue(check.isLengthInMinMax("", 0, 1));
        assertFalse(check.isLengthInMinMax("", 1, 2));
        assertTrue(check.isLengthInMinMax("Z", 0, 1));
        assertTrue(check.isLengthInMinMax("Z", 1, 1));
        assertTrue(check.isLengthInMinMax("Z", 1, 2));
        assertFalse(check.isLengthInMinMax("Z", 2, 3));
        assertFalse(check.isLengthInMinMax("Z", 2, 0));
        assertFalse(check.isLengthInMinMax("Z", 2, 1));
        assertTrue(check.isLengthInMinMax("ABC", 0, 20));
        assertFalse(check.isLengthInMinMax(null, 30, 106));
    }

    @Test
    public void testIsUrl() throws Exception {
        assertTrue(check.isUrl("http://test.ch"));
        assertTrue(check.isUrl("https://test.ch"));
        assertTrue(check.isUrl("ftp://test.ch"));
        assertFalse(check.isUrl(""));
        assertFalse(check.isUrl("www.test.ch"));
        assertFalse(check.isUrl("fttp://test.ch"));
        assertFalse(check.isUrl(null));
    }

    @Test
    public void testIsFiletypeExtension() throws Exception {
        assertTrue(check.isFiletypeExtension("test.xls", ".xls"));
        assertTrue(check.isFiletypeExtension("test.XLs", ".xlS"));
        assertTrue(check.isFiletypeExtension("test.pdf.xls", ".xls"));
        assertFalse(check.isFiletypeExtension("testxls", ".xls"));
        assertFalse(check.isFiletypeExtension("", ".xls"));
        assertFalse(check.isFiletypeExtension("", ""));
        assertFalse(check.isFiletypeExtension(null, null));
        assertFalse(check.isFiletypeExtension(null, ".xls"));
    }

    @Test
    public void testGetAlphanumericWordCharacters() throws Exception {
        assertTrue(check.getAlphanumericWordCharacters("").size() == 0);
        assertTrue(check.getAlphanumericWordCharacters(" !?--_ * ").size() == 0);
        assertTrue(check.getAlphanumericWordCharacters("Aa9o= 2El ").size() == 2);
        assertTrue(check.getAlphanumericWordCharacters("Aa!9o= 2? El ").size() == 4);
        assertTrue(check.getAlphanumericWordCharacters(null).size() == 0);
    }

    @Test
    public void testCountCharacterInString() throws Exception {
        assertTrue(check.countCharacterInString("", "") == 0);
        assertTrue(check.countCharacterInString("", "test") == 0);
        assertTrue(check.countCharacterInString("test", "test") == 1);
        assertTrue(check.countCharacterInString("testAtest ", "test") == 2);
        assertTrue(check.countCharacterInString("testATest ", "test") == 1);
        assertTrue(check.countCharacterInString("test", null) == 0);
    }

    @Test
    public void testContainsOnlyNumbers() throws Exception {
        assertFalse(check.containsOnlyNumbers(""));
        assertFalse(check.containsOnlyNumbers("1A20"));
        assertFalse(check.containsOnlyNumbers("11 "));
        assertTrue(check.containsOnlyNumbers("11"));
        assertFalse(check.containsOnlyNumbers(null));

    }

    @Test
    public void testIsValidIssn() throws Exception {
        assertFalse(check.isValidIssn(""));
        assertFalse(check.isValidIssn("123-1111"));
        assertFalse(check.isValidIssn(" "));
        assertTrue(check.isValidIssn("1420-4398"));
        assertFalse(check.isValidIssn(null));
    }

    @Test
    public void testIsYear() throws Exception {
        assertFalse(check.isYear(""));
        assertFalse(check.isYear("AAA"));
        assertFalse(check.isYear("201"));
        assertTrue(check.isYear("1620"));
        assertTrue(check.isYear("2120"));
        assertFalse(check.isYear("2012 "));
        assertFalse(check.isYear(null));

    }
}
