/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public class NullFilterTest extends TestCase {
    private ArrayList<Coordinate> original, cloned;
    private NullFilter query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        original = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            original.add(new Coordinate(i, i, 0));
        }

        cloned = (ArrayList<Coordinate>) original.clone();

        query = new NullFilter();
    }

    public void testApply() throws Exception {
        Collection<Coordinate> actual = query.apply(original);

        // The result should be identical to the original.
        assertEquals(original, actual);

        // The original should be identical to its replicate (ie, unmodified).
        assertEquals(cloned, original);
    }
}