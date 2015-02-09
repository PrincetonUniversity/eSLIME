/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundaries;

import control.identifiers.Coordinate;
import geometry.boundaries.helpers.WrapHelper2D;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import test.EslimeTestCase;

/**
 * Integration test for the interaction between
 * the 2D wrap helper and a triangular lattice
 * with even width.
 */
public class WrapHelper2DTriTest extends EslimeTestCase {

    private WrapHelper2D query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 2, 2);
        query = new WrapHelper2D(shape, lattice);
    }

    public void testWrapUpperRight() throws Exception {
        Coordinate toWrap = new Coordinate(2, 2, 0);
        Coordinate expected = new Coordinate(0, 1, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }


    public void testWrapUpperLeft() throws Exception {
        Coordinate toWrap = new Coordinate(-1, 1, 0);
        Coordinate expected = new Coordinate(1, 0, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapLowerRight() throws Exception {
        Coordinate toWrap = new Coordinate(2, 0, 0);
        Coordinate expected = new Coordinate(0, 1, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapLowerLeft() throws Exception {
        Coordinate toWrap = new Coordinate(-1, 1, 0);
        Coordinate expected = new Coordinate(1, 0, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapRight() throws Exception {
        Coordinate toWrap = new Coordinate(2, 1, 0);
        Coordinate expected = new Coordinate(0, 0, 0);
        Coordinate actual = query.xWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapLeft() throws Exception {
        Coordinate toWrap = new Coordinate(-1, 0, 0);
        Coordinate expected = new Coordinate(1, 1, 0);
        Coordinate actual = query.xWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapTop() throws Exception {
        Coordinate toWrap = new Coordinate(1, 2, 0);
        Coordinate expected = new Coordinate(1, 0, 0);
        Coordinate actual = query.yWrap(toWrap);
        assertEquals(expected, actual);
    }

    public void testWrapBottom() throws Exception {
        Coordinate toWrap = new Coordinate(1, -1, 0);
        Coordinate expected = new Coordinate(1, 1, 0);
        Coordinate actual = query.yWrap(toWrap);
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
        Coordinate toWrap = new Coordinate(1, 1, 0);
        Coordinate expected = new Coordinate(1, 1, 0);
        Coordinate actual = query.wrapAll(toWrap);
        assertEquals(expected, actual);
    }

}