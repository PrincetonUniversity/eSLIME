/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.control.arguments;//import junit.framework.TestCase;

import control.arguments.GeometryDescriptor;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import org.dom4j.Element;
import test.EslimeTestCase;

public class GeometryDescriptorFactoryTest extends EslimeTestCase {

    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/control/arguments/GeometryDescriptorFactoryTest.xml");
    }

    public void testExplicitCase() throws Exception {
        Element elem = root.element("explicit-case");
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        GeometryDescriptor expected = new GeometryDescriptor(lattice, shape);
        GeometryDescriptor actual = GeometryDescriptorFactory.instantiate(elem);
        assertEquals(expected, actual);
    }

    // No implicit case because that behavior has not yet been implemented.
    // This one will need to be slightly complicated, since lattice and shape
    // arguments should be able to narrow down the default behavior for the
    // other one. This is a good example of a smart default -- an important
    // concept for Eco 1.0.
}