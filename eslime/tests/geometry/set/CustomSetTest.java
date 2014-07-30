/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.set;//import junit.framework.TestCase;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import test.EslimeTestCase;

/**
 * Since the custom set is agnostic to geometry,
 * this test looks a little different from the other
 * coordinate set tests.
 *
 * @throws Exception
 */
public class CustomSetTest extends EslimeTestCase {

    public void testLifeCycle() throws Exception {
        Coordinate c = new Coordinate(0, 0, 0);
        CustomSet set = new CustomSet();

        assertEquals(0, set.size());
        assertFalse(set.contains(c));

        set.add(c);

        assertEquals(1, set.size());
        assertTrue(set.contains(c));
    }

    /**
     * Confirm that coordinate sets are equivalent as long
     * as they have the same contents
     *
     * @throws Exception
     */
    public void testEquivalency() throws Exception {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 3);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        Argument<Integer> radiusArg = new ConstantInteger(0);
        Coordinate offset = new Coordinate(0, -1, 0);
        DiscSet discSet = new DiscSet(geom, radiusArg, offset);

        Coordinate c = new Coordinate(0, 0, 0);
        CustomSet customSet = new CustomSet();
        customSet.add(c);

        assertEquals(discSet, customSet);

    }
}