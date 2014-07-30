/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.BehaviorCell;
import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.arguments.ConstantInteger;
import control.halt.DominationEvent;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.Periodic;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.set.CompleteSet;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.StepState;
import processes.discrete.check.CheckForDomination;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class CheckForDominationTest extends EslimeTestCase {
    private MockLayerManager layerManager;
    private CellLayer layer;
    private CheckForDomination query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new RectangularLattice();
        layerManager = new MockLayerManager();
        Shape shape = new Rectangle(lattice, 11, 1);
        Boundary boundary = new Periodic(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        MockGeneralParameters p = makeMockGeneralParameters();
        Argument<Double> thresholdArg = new ConstantDouble(0.2);
        Argument<Integer> stateArg = new ConstantInteger(1);

        // Create a 1D lattice of length 10.
        // Create an occupancy test that checks for 30% occupancy.
        query = new CheckForDomination(null, layerManager, new CompleteSet(geom), 0, p, stateArg, thresholdArg);
    }

    public void testAboveThreshold() throws Exception {
        for (int i = 1; i < 4; i++) {
            populate(i, 1);
        }

        for (int i = 4; i < 11; i++) {
            populate(i, i);
        }

        doTest(true);
    }

    public void testAtThreshold() throws Exception {
        for (int i = 1; i < 3; i++) {
            populate(i, 1);
        }

        for (int i = 3; i < 11; i++) {
            populate(i, i);
        }

        doTest(true);
    }

    public void testBelowThreshold() throws Exception {
        for (int i = 1; i < 2; i++) {
            populate(i, 1);
        }

        for (int i = 2; i < 11; i++) {
            populate(i, i);
        }

        doTest(false);
    }

    private void doTest(boolean expectThrow) throws Exception {
        boolean thrown = false;

        try {
            query.fire(new StepState(0.0, 0));
        } catch (DominationEvent ex) {
            thrown = true;
        }

        assertEquals(expectThrow, thrown);
    }
    private void populate(int x, int state) {
        BehaviorCell cell = new BehaviorCell(layerManager, state, state, state);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
    }
}