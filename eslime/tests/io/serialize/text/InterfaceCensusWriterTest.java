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

package io.serialize.text;

import cells.MockCell;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import layers.cell.CellUpdateManager;
import processes.StepState;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/28/14.
 */
public class InterfaceCensusWriterTest extends EslimeTestCase {
    private CellLayer cellLayer;
    private MockLayerManager layerManager;

    @Override
    public void setUp() {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 3, 3);
        Boundary boundary = new Absorbing(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        cellLayer = new CellLayer(geom);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(cellLayer);

        buildInitialCondition();

    }

    /**
     * Initial condition (1 is focal state)
     *      0 1 2
     *      _____
     *
     * 2 |  1 2 3
     * 1 |  2 1 0
     * 0 |  0 1 2
     *
     *
     *  Upper left "1": 2 of 2 neighbors in state 2
     *
     *  Center "1": 1 of 4 neighbors vacant (state 0)
     *              1 of 4 neighbors in state 1
     *              2 of 4 neighbors in state 2
     *
     *  Lower "1": 1 of 3 neighbors vacant (state 0)
     *           : 1 of 3 neighbors in state 1
     *           : 1 of 3 neighbors in state 2
     *
     *  Therefore, expected totals:
     *      2 of 9 total neighbors vacant (state 0)
     *      2 of 9 total neighbors in state 1
     *      5 of 9 total neighbors in state 2
     *
     *  Note that "3" never appears as an interface state,
     *  despite existing in the system, because it does not
     *  border a 1.
     *
     */
    private void buildInitialCondition() {
        put(new Coordinate(0, 0, 0), 0);
        put(new Coordinate(1, 0, 0), 1);
        put(new Coordinate(2, 0, 0), 2);
        put(new Coordinate(0, 1, 0), 2);
        put(new Coordinate(1, 1, 0), 1);
        put(new Coordinate(2, 1, 0), 0);
        put(new Coordinate(0, 2, 0), 1);
        put(new Coordinate(1, 2, 0), 2);
        put(new Coordinate(2, 2, 0), 3);
    }

    public void testLifeCycle() throws Exception {
        MockGeneralParameters p = makeMockGeneralParameters();
        p.setInstancePath(outputPath);
        InterfaceCensusWriter writer = new InterfaceCensusWriter(p, new ConstantInteger(1));
        writer.init(layerManager);

        // Flush original configuration
        StepState state = new StepState(0.0, 0);
        state.record(cellLayer);
        writer.flush(state);

        /*
         * Replace "2" at (1, 2) with "3"
         *
         *      0 1 2
         *      _____
         *
         * 2 |  1 3 3
         * 1 |  2 1 0
         * 0 |  0 1 2
         *
         * Now expected values are:
         *   State 0 --> 2 of 9
         *   State 1 --> 2 of 9
         *   State 2 --> 3 of 9
         *   State 3 --> 2 of 9
         */
        replace(new Coordinate(1, 2, 0), 3);
        state = new StepState(1.0, 1);
        state.record(cellLayer);
        writer.flush(state);

        writer.dispatchHalt(null);
        writer.close();
        assertFilesEqual("interface_1.txt");
    }

    private void replace(Coordinate c, int state) {
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.banish(c);
        put(c, state);
    }

    private void put(Coordinate c, int state) {
        MockCell cell = new MockCell(state);
        CellUpdateManager u = cellLayer.getUpdateManager();
        u.place(cell, c);
    }
}
