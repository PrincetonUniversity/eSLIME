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

package factory.layers;

import factory.geometry.GeometryFactory;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import layers.LayerManager;
import layers.MockSoluteLayer;
import layers.cell.CellLayer;
import org.dom4j.Element;
import test.EslimeTestCase;

public class LayerManagerFactoryTest extends EslimeTestCase {
    private Element root;
    private GeometryFactory factory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/layers/LayerManagerFactoryTest.xml");
        Element geometryRoot = root.element("geometry");
        factory = new GeometryFactory(geometryRoot);

    }

    public void testInstantiate() throws Exception {
        Element e = root.element("general-case");
        LayerManager actual = LayerManagerFactory.instantiate(e, factory);
        LayerManager expected = makeExpected();
        assertEquals(expected, actual);
    }

    private LayerManager makeExpected() {
        CellLayer cellLayer = new CellLayer(arenaGeom());
        MockSoluteLayer soluteLayer1 = new MockSoluteLayer();
        MockSoluteLayer soluteLayer2 = new MockSoluteLayer();

        LayerManager layerManager = new LayerManager();
        layerManager.addSoluteLayer("test1", soluteLayer1);
        layerManager.addSoluteLayer("test2", soluteLayer2);
        layerManager.setCellLayer(cellLayer);
        return layerManager;
    }

    private Geometry arenaGeom() {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geometry = new Geometry(lattice, shape, boundary);
        return geometry;
    }

}