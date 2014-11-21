/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;//import junit.framework.TestCase;

import cells.Cell;
import cells.MockCell;
import control.halt.BoundaryReachedEvent;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import test.EslimeTestCase;

public class HaltCellLayerContentTest extends EslimeTestCase {

    private HaltCellLayerContent query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 5);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geometry = new Geometry(lattice, shape, boundary);
        CellLayerIndices indices = new CellLayerIndices();
        query = new HaltCellLayerContent(geometry, indices);
    }

    public void testPutInBounds() throws Exception {
        MockCell cell = new MockCell(1);
        Coordinate c = new Coordinate(0, 0, 0);
        query.put(c, cell);
        Cell actual = query.get(c);
        assertTrue(actual == cell);
    }

    public void testPutOutOfBounds() throws Exception {
        MockCell cell = new MockCell(1);
        Coordinate c = new Coordinate(-1, 0, Flags.END_OF_WORLD);

        boolean thrown = false;
        try {
            query.put(c, cell);
        } catch (BoundaryReachedEvent ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }
}