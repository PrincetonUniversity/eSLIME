/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package processes.discrete;

import cells.MockCell;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.set.CompleteSet;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.MockStepState;
import structural.MockGeneralParameters;
import structural.MockRandom;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/21/14.
 */
public class GeneralNeighborSwapTest extends EslimeTestCase {

    private GeneralNeighborSwap query;
    private MockLayerManager layerManager;
    private MockCell a, b, c;
    private Coordinate ac, bc, cc;
    private MockRandom random;

    @Override
    protected void setUp() throws Exception {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 3, 3);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CellLayer cellLayer = new CellLayer(geom);

        layerManager = new MockLayerManager();
        layerManager.setCellLayer(cellLayer);

        MockGeneralParameters p = makeMockGeneralParameters();
        random = new MockRandom();
        p.setRandom(random);
        query = new GeneralNeighborSwap(null, layerManager, new CompleteSet(geom), 0, p, new ConstantInteger(1));

        /*
         * Cell layout:
         *     0 1 2
         *  0  b . .
         *  1  a c .
         *  2  . . .
         */
        a = new MockCell(1);
        b = new MockCell(2);
        c = new MockCell(3);

        ac = new Coordinate(0, 1, 0);
        bc = new Coordinate(0, 0, 0);
        cc = new Coordinate(1, 1, 0);
        cellLayer.getUpdateManager().place(a, ac);
        cellLayer.getUpdateManager().place(b, bc);
        cellLayer.getUpdateManager().place(c, cc);
    }

    public void testCellsReflectSwap() throws Exception {
        query.target(null);
        MockStepState state = new MockStepState();

        CellLayer cl = layerManager.getCellLayer();

        // Enforce that the desired random event happens
        random.setNextIntValue(1);
        // a should move
        assertTrue(cl.getViewer().getCell(ac).equals(a));
        query.fire(state);
        assertFalse(cl.getViewer().getCell(ac).equals(a));

        // specifically, a should be swapped with c
        assertTrue(cl.getViewer().getCell(bc).equals(b));
        assertTrue(cl.getViewer().getCell(ac).equals(c));
        assertTrue(cl.getViewer().getCell(cc).equals(a));

    }
}
