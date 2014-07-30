/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;//import junit.framework.TestCase;

import test.EslimeTestCase;

public class ManualHaltEventTest extends EslimeTestCase {

    public void testLifeCycle() throws Exception {
        ManualHaltEvent query = new ManualHaltEvent(1.0, "test123");
        assertEquals("test123", query.toString());
        assertEquals(1.0, query.getGillespie());
    }
}