/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;//import junit.framework.TestCase;


import factory.cell.Reaction;
import org.junit.Before;
import org.junit.Test;
import test.TestBase;

import static org.junit.Assert.assertEquals;

public class ReactionTest extends TestBase {

    private Reaction query;

    @Before
    public void init() throws Exception {
        query = new Reaction(1.0, 2.0, "test");
    }

    @Test
    public void getInj() throws Exception {
        assertEquals(1.0, query.getInj(), epsilon);
    }

    @Test
    public void getExp() throws Exception {
        assertEquals(2.0, query.getExp(), epsilon);
    }

    @Test
    public void getId() throws Exception {
        assertEquals("test", query.getId());
    }
}