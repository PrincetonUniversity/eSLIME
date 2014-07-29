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

import cells.BehaviorCell;
import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.halt.ThresholdOccupancyReachedEvent;
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
import processes.discrete.check.CheckForThresholdOccupancy;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class CheckForThresholdOccupancyTest extends EslimeTestCase {

    private MockLayerManager layerManager;
    private CellLayer layer;
    private CheckForThresholdOccupancy query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Lattice lattice = new RectangularLattice();
        layerManager = new MockLayerManager();
        Shape shape = new Rectangle(lattice, 10, 1);
        Boundary boundary = new Periodic(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        MockGeneralParameters p = makeMockGeneralParameters();
        Argument<Double> thresholdArg = new ConstantDouble(0.2);

        // Create a 1D lattice of length 10.
        // Create an occupancy test that checks for 30% occupancy.
        query = new CheckForThresholdOccupancy(null, layerManager, new CompleteSet(geom), 0, p, thresholdArg);
    }

    public void testAboveThreshold() throws Exception {
        for (int i = 1; i < 4; i++) {
            placeNumberedCell(i);
        }

        doTest(true);
    }

    public void testAtThreshold() throws Exception {
        for (int i = 1; i < 3; i++) {
            placeNumberedCell(i);
        }

        doTest(true);
    }

    public void testBelowThreshold() throws Exception {
        for (int i = 1; i < 2; i++) {
            placeNumberedCell(i);
        }

        doTest(false);
    }

    private void doTest(boolean expectThrow) throws Exception {
        boolean thrown = false;

        try {
            query.fire(new StepState(0.0, 0));
        } catch (ThresholdOccupancyReachedEvent ex) {
            thrown = true;
        }

        assertEquals(expectThrow, thrown);
    }
    private void placeNumberedCell(int x) {
        BehaviorCell cell = new BehaviorCell(layerManager, x, x, x);
        Coordinate coord = new Coordinate(x, 0, 0);
        layer.getUpdateManager().place(cell, coord);
    }

}