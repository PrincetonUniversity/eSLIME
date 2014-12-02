/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;//import junit.framework.TestCase;

import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import test.EslimeTestCase;

public class GeometryDescriptorTest extends EslimeTestCase {

    private Lattice lattice;
    private Shape shape;
    private GeometryDescriptor query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        lattice = new LinearLattice();
        shape = new Line(lattice, 10);
        query = new GeometryDescriptor(lattice, shape);
    }

    public void testEquals() throws Exception {
        Lattice otherLattice = new LinearLattice();
        Shape otherShape = new Line(otherLattice, 10);
        GeometryDescriptor otherDescriptor = new GeometryDescriptor(otherLattice, otherShape);
        assertEquals(query, otherDescriptor);
        assertFalse(query == otherDescriptor);
    }

    public void testMake() throws Exception {
        Boundary boundary = new Arena(shape, lattice);

        Geometry expected = new Geometry(lattice, shape, boundary);
        Geometry actual = query.make(boundary);

        assertEquals(expected, actual);
    }

    public void testGetLattice() throws Exception {
        assertTrue(lattice == query.getLattice());
    }

    public void testGetShape() throws Exception {
        assertTrue(shape == query.getShape());
    }
}