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
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.MockStepState;
import processes.gillespie.GillespieState;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/21/14.
 */
public class NeighborSwapTest extends EslimeTestCase {

    private NeighborSwap query;
    private MockLayerManager layerManager;
    private MockCell a, b, c;
    private Coordinate ac, bc, cc;

    @Override
    protected void setUp() throws Exception {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 2, 2);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        CellLayer cellLayer = new CellLayer(geom);

        layerManager = new MockLayerManager();
        layerManager.setCellLayer(cellLayer);

        MockGeneralParameters p = makeMockGeneralParameters();
        query = new NeighborSwap(null, layerManager, 0, p, new ConstantInteger(1));

        /*
         * Cell layout:
         *     0 1
         *  1  a .
         *  0  b c
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
        query.fire(state);

        CellLayer cl = layerManager.getCellLayer();

        // No matter what, B should no longer be at (0, 0)
        assertFalse(cl.getViewer().getCell(bc).equals(b));

        // It so happens that, with this random seed, b and a are swapped
        assertTrue(cl.getViewer().getCell(bc).equals(a));
        assertTrue(cl.getViewer().getCell(ac).equals(b));
        assertTrue(cl.getViewer().getCell(cc).equals(c));

    }

    public void testGillespie() throws Exception {
        GillespieState gs = new GillespieState(new Integer[] {0});
        query.target(gs);
        gs.close();
        // a, b, and c are all involved.
        // WARNING: This method will count cells that cannot participate in
        // a swap toward the weight of the Gillespie function as of 4/30/2014.
        assertEquals(3.0, gs.getWeight(0));
        assertEquals(2, gs.getEventCount(0));
    }
}
