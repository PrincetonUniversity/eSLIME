/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.set;//import junit.framework.TestCase;

import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class CustomSetFactoryTest extends EslimeTestCase {
    private Geometry geom;
    private Element root;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/set/CustomSetFactoryTest.xml");
        p = makeMockGeneralParameters();
        geom = makeGeometry();
    }

    public void testDefault() {
        Element e = root.element("default-case");
        CoordinateSet actual = CustomSetFactory.instantiate(e, geom);
        CoordinateSet expected = new CustomSet();
        assertEquals(expected, actual);
    }

    public void testMixedCase() {
        Element e = root.element("mixed-case");
        CoordinateSet actual = CustomSetFactory.instantiate(e, geom);

        CoordinateSet expected = new CustomSet();
        expected.add(new Coordinate(0, 0, 0));
        expected.add(new Coordinate(0, 1, 0));
        expected.add(new Coordinate(0, 2, 0));
        assertEquals(expected, actual);
    }

    private Geometry makeGeometry() {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        Geometry ret = new Geometry(lattice, shape, boundary);
        return ret;
    }
}