/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.set;//import junit.framework.TestCase;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CoordinateSet;
import geometry.set.DiscSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class DiscSetFactoryTest extends EslimeTestCase {

    private Geometry geom;
    private Element root;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/set/DiscSetFactoryTest.xml");
        p = makeMockGeneralParameters();
        geom = makeGeometry();
    }

    public void testDefault() {
        Element e = root.element("default-case");

        Argument<Integer> radiusArg = new ConstantInteger(1);
        Coordinate offset = geom.getZeroVector();
        CoordinateSet expected = new DiscSet(geom, radiusArg, offset);

        CoordinateSet actual = DiscSetFactory.instantiate(e, geom, p);

        assertEquals(expected, actual);
    }

    public void testExplicit() {
        Element e = root.element("explicit-case");

        Argument<Integer> radiusArg = new ConstantInteger(2);
        Coordinate offset = new Coordinate(0, -1, 0) ;
        CoordinateSet expected = new DiscSet(geom, radiusArg, offset);
        CoordinateSet actual = DiscSetFactory.instantiate(e, geom, p);

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