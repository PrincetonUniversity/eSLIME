/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes.discrete;

import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CoordinateSet;
import geometry.set.DiscSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import layers.LayerManager;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.discrete.CellProcessArguments;
import processes.discrete.TriggerProcess;
import processes.discrete.filter.DepthFilter;
import processes.discrete.filter.Filter;
import processes.discrete.filter.NullFilter;
import test.EslimeTestCase;

public class TriggerProcessFactoryTest extends EslimeTestCase {
    private GeneralParameters p;
    private Element root;
    private LayerManager layerManager;
    private Geometry geom;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = makeMockGeneralParameters();
        root = readXmlFile("factories/processes/discrete/TriggerProcessFactoryTest.xml");

        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Absorbing(shape, lattice);
        geom = new Geometry(lattice, shape, boundary);

        CellLayer layer = new CellLayer(geom);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
    }

    public void testImplicit() throws Exception {
        Element testElem = root.element("implicit-case");

        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);
        CellProcessArguments cpArguments = makeCellProcessArguments(geom);

        TriggerProcess expected = new TriggerProcess(arguments, cpArguments, "default", new NullFilter(), false, false);
        TriggerProcess actual = TriggerProcessFactory.instantiate(testElem, layerManager, p, 0);

        assertEquals(expected, actual);
    }

    public void testExplicit() throws Exception {
        Element testElem = root.element("explicit-case");

        Argument<Integer> maxTargets = new ConstantInteger(2);
        CoordinateSet activeSites = new DiscSet(geom, new ConstantInteger(2), new Coordinate(0, 0, 0));
        CellProcessArguments cpArguments = new CellProcessArguments(activeSites, maxTargets);

        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, p);

        Filter filter = new DepthFilter(layerManager.getCellLayer(), new ConstantInteger(1));

        TriggerProcess expected = new TriggerProcess(arguments, cpArguments, "test", filter, true, true);
        TriggerProcess actual = TriggerProcessFactory.instantiate(testElem, layerManager, p, 0);

        assertEquals(expected, actual);
    }
}