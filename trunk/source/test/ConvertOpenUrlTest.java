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

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.dbs.actions.openurl.ConvertOpenUrl;

public class ConvertOpenUrlTest {
    
    private final ConvertOpenUrl check = new ConvertOpenUrl();
    
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
    
    // CHECKSTYLE:OFF
    
    @Test
    public void extractFirstNumber() throws Exception {
        assertTrue("60".equals(check.extractFirstNumber("60-4")));
        assertTrue("60".equals(check.extractFirstNumber("S60-4")));
        assertTrue("60".equals(check.extractFirstNumber(" 60 - 4")));
        assertTrue("60".equals(check.extractFirstNumber("saad s60dd-4")));
        assertTrue("".equals(check.extractFirstNumber("")));
    }
    
    @Test
    public void normalizeIssn() throws Exception {
        assertTrue("1234-1234".equals(check.normalizeIssn("12341234")));
        assertTrue("".equals(check.extractFirstNumber("")));
    }
    
    @Test
    public void extractFromSeveralIssns() throws Exception {
        // using reflection to test private method
        final Method method = ConvertOpenUrl.class.getDeclaredMethod("extractFromSeveralIssns", String.class);
        method.setAccessible(true);
        assertTrue("1234-1234".equals(method.invoke(check, "1234-1234; 2345-2345")));
        assertTrue("1234-12342345-2345".equals(method.invoke(check, "1234-12342345-2345"))); // returns input
        assertTrue("1234-1234".equals(method.invoke(check, "1234-1234; gtz66 2345-2345")));
        assertTrue("1234-1234".equals(method.invoke(check, "1234-1234; gtz662345-2345")));
        assertTrue("".equals(method.invoke(check, "")));
    }
    
    @Test
    public void checkLowercase() throws Exception {
        // using reflection to test private method
        final Method method = ConvertOpenUrl.class.getDeclaredMethod("checkLowercase", String.class);
        method.setAccessible(true);
        
        boolean test = (Boolean) method.invoke(check, "aHHH");
        assertTrue(test);
        test = (Boolean) method.invoke(check, "HHHa");
        assertTrue(test);
        test = (Boolean) method.invoke(check, "HHH");
        assertFalse(test);
        test = (Boolean) method.invoke(check, " ");
        assertFalse(test);
        test = (Boolean) method.invoke(check, "");
        assertFalse(test);
    }
    
    @Test
    public void toNormalLetters() throws Exception {
        // using reflection to test private method
        final Method method = ConvertOpenUrl.class.getDeclaredMethod("toNormalLetters", String.class);
        method.setAccessible(true);
        assertTrue("Methods in enzymology".equals(method.invoke(check, "METHODS IN ENZYMOLOGY")));
        assertTrue("Methods in enzymology ".equals(method.invoke(check, "METHODS IN ENZYMOLOGY ")));
        assertTrue("".equals(method.invoke(check, "")));
    }
    
    // CHECKSTYLE:ON
}
