/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;
import junit.framework.TestCase;

public class CompositeFilterTest extends TestCase {

    public void testApply() throws Exception {
        Filter child1 = new MockFilter();
        Filter child2 = new MockFilter();
        Filter[] children = new Filter[] {child1, child2};
        CompositeFilter query = new CompositeFilter(children);
        query.apply(new Coordinate[0]);
        assertTrue(child1.isCalled());
        assertTrue(child2.isCalled());
    }
}