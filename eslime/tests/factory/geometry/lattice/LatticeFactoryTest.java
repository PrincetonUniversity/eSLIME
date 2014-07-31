/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.lattice;//import junit.framework.TestCase;

import geometry.lattice.CubicLattice;
import geometry.lattice.LinearLattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import org.dom4j.Element;
import test.EslimeTestCase;

public class LatticeFactoryTest extends EslimeTestCase {

    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/lattice/LatticeFactoryTest.xml");
    }

    public void testLinearCase() {
        doTest("linear-case", LinearLattice.class);
    }

    public void testRectangularCase() {
        doTest("rectangular-case", RectangularLattice.class);
    }

    public void testTriangularCase() {
        doTest("triangular-case", TriangularLattice.class);
    }

    public void testCubicCase() {
        doTest("cubic-case", CubicLattice.class);
    }

    private void doTest(String eName, Class expected) {
        Element e = root.element(eName);
        Class actual = LatticeFactory.instantiate(e).getClass();
        assertEquals(expected, actual);
    }
}