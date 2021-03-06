/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.CellDescriptor;
import control.arguments.ConstantInteger;
import control.arguments.MockCellDescriptor;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CompleteSet;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.BaseProcessArguments;
import test.EslimeTestCase;

public class ScatterTest extends EslimeTestCase {
    private Geometry geom;
    private MockLayerManager lm;
    private GeneralParameters p;
    private BaseProcessArguments arguments;
    private CellDescriptor cd;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        p = makeMockGeneralParameters();
        lm = new MockLayerManager();

        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        geom = new Geometry(lattice, shape, boundary);

        CellLayer layer = new CellLayer(geom);
        lm.setCellLayer(layer);

        arguments = makeBaseProcessArguments(lm, p);

        cd = new MockCellDescriptor();
    }

    public void testBaseBehavior() throws Exception {
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);
        Scatter query = new Scatter(arguments, cpArguments, cd);
        query.init();
        query.iterate();

        assertEquals(10, lm.getCellLayer().getViewer().getOccupiedSites().size());
    }

    public void testRespectActiveSites() throws Exception {
        CoordinateSet activeSites = new CustomSet();
        for (int y = 2; y < 5; y++) {
            activeSites.add(new Coordinate(0, y, 0));
        }

        CellProcessArguments cpArguments = new CellProcessArguments(activeSites, new ConstantInteger(-1));

        Scatter query = new Scatter(arguments, cpArguments, cd);
        query.init();
        query.iterate();

        for (Coordinate c : geom.getCanonicalSites()) {
            boolean actual = lm.getCellLayer().getViewer().isOccupied(c);
            boolean expected = activeSites.contains(c);
            assertEquals(expected, actual);
        }
    }

    public void testRespectMaxTargets() throws Exception {
        CoordinateSet activeSites = new CompleteSet(geom);
        Argument<Integer> maxTargets = new ConstantInteger(3);
        CellProcessArguments cpArguments = new CellProcessArguments(activeSites, maxTargets);
        Scatter query = new Scatter(arguments, cpArguments, cd);
        query.init();
        query.iterate();

        assertEquals(3, lm.getCellLayer().getViewer().getOccupiedSites().size());
    }
}