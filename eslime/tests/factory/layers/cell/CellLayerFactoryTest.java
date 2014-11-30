/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.cell;

import control.arguments.GeometryDescriptor;
import factory.control.arguments.GeometryDescriptorFactory;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import layers.cell.CellLayer;
import org.dom4j.Element;
import test.EslimeTestCase;

public class CellLayerFactoryTest extends EslimeTestCase {

    private Element fixtureRoot;
    private GeometryDescriptor descriptor;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("factories/layers/cell/CellLayerFactoryTest.xml");
        Element geometryRoot = fixtureRoot.element("geometry");
        descriptor = GeometryDescriptorFactory.instantiate(geometryRoot);
    }

    public void testInstantiate() throws Exception {
        Element e = fixtureRoot.element("general-case");
        CellLayer actual = CellLayerFactory.instantiate(e, descriptor);
        Geometry geom = makeGeometry();
        CellLayer expected = new CellLayer(geom);
        assertEquals(expected, actual);
    }

    private Geometry makeGeometry() {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geometry = new Geometry(lattice, shape, boundary);
        return geometry;
    }
}