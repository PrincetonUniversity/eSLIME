/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries.helpers;

import control.identifiers.Coordinate;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import junit.framework.TestCase;
import test.EslimeTestCase;

public class WrapHelper2DTest extends EslimeTestCase {

    private WrapHelper2D query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 5, 5);
        query = new WrapHelper2D(shape, lattice);
    }

    public void testWrapAll() throws Exception {
        Coordinate toWrap = new Coordinate(5, 5, 0);
        Coordinate expected = new Coordinate(0, 0, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

    public void testXWrap() throws Exception {
        // Positive out of bounds X coordinate
        Coordinate toWrap = new Coordinate(6, 0, 0);
        Coordinate expected = new Coordinate(1, 0, 0);
        Coordinate actual = query.xWrap(toWrap);
        assertEquals(expected, actual);

        // Negative out of bounds X coordinate
        toWrap = new Coordinate(-1, 0, 0);
        expected = new Coordinate(4, 0, 0);
        actual = query.xWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testYWrap() throws Exception {
        // Positive out of bounds Y coordinate
        Coordinate toWrap = new Coordinate(0, 6, 0);
        Coordinate expected = new Coordinate(0, 1, 0);
        Coordinate actual = query.yWrap(toWrap);
        assertEquals(expected, actual);

        // Negative out of bounds Y coordinate
        toWrap = new Coordinate(0, -1, 0);
        expected = new Coordinate(0, 4, 0);
        actual = query.yWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testZWrap() throws Exception {
        boolean thrown = false;

        try {
            query.zWrap(null);
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    public void testAllInBounds() throws Exception {
        Coordinate toWrap = new Coordinate(3, 3, 0);
        Coordinate expected = new Coordinate(3, 3, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }
}