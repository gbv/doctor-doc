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

import static org.junit.Assert.assertTrue;

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
    // CHECKSTYLE:ON
}
