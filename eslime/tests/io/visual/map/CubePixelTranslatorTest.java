/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.map;//import junit.framework.TestCase;

import control.identifiers.Coordinate;
import geometry.lattice.CubicLattice;
import geometry.lattice.Lattice;
import geometry.shape.Cuboid;
import geometry.shape.Shape;
import io.visual.VisualizationProperties;
import test.EslimeTestCase;

public class CubePixelTranslatorTest extends EslimeTestCase {
    private CubePixelTranslator query;
    private Coordinate c0, c1;

    @Override
    protected void setUp() throws Exception {
        c0 = new Coordinate(0, 0, 2, 0);
        c1 = new Coordinate(1, 4, 2, 0);
        super.setUp();
        Lattice lattice = new CubicLattice();
        Shape shape = new Cuboid(lattice, 5, 5, 5);
        Coordinate[] cc = shape.getCanonicalSites();
        VisualizationProperties mapState = new VisualizationProperties(null, 10, 1);
        mapState.setCoordinates(cc);
        query = new CubePixelTranslator();
        query.init(mapState);
    }

    public void testOrigin() throws Exception {
        Coordinate expected = new Coordinate(5, 45, 0);
        Coordinate actual = query.indexToPixels(c0);
        assertEquals(expected, actual);
    }

    public void testIndexToPixels() throws Exception {
        Coordinate actual = query.indexToPixels(c1);
        Coordinate expected = new Coordinate(15, 5, 0);
        assertEquals(expected, actual);
    }


    public void testGetImageDims() throws Exception {
        Coordinate actual = query.getImageDims();
        Coordinate expected = new Coordinate(50, 50, 0);
        assertEquals(expected, actual);
    }

    public void testGetDiagonal() throws Exception {
        double expected = Math.sqrt(2.0) * 10.0;
        double actual = query.getDiagonal();
        assertEquals(expected, actual, epsilon);
    }

    public void testNonMiddleThrows() throws Exception {
        Coordinate nonMiddle = new Coordinate(0, 0, 0, 0);

        boolean thrown = false;

        try {
            query.indexToPixels(nonMiddle);
        } catch (IllegalStateException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }
}