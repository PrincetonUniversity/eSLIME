/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.boundaries;//import junit.framework.TestCase;

import geometry.boundaries.*;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import org.dom4j.Element;
import test.EslimeTestCase;

public class BoundaryFactoryTest extends EslimeTestCase {

    private Element root;
    private Shape shape;
    private Lattice lattice;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/boundary/BoundaryFactoryTest.xml");
        lattice = new RectangularLattice();
        shape = new Rectangle(lattice, 1, 1);
    }

    public void testArenaCase() {
        doTest("arena-case", Arena.class);
    }

    public void testPlaneRingHardCase() {
        doTest("plane-ring-hard-case", PlaneRingHard.class);
    }

    public void testPlaneRingReflectingCase() {
        doTest("plane-ring-reflecting-case", PlaneRingReflecting.class);
    }

    public void testAbsorbingCase() {
        doTest("absorbing-case", Absorbing.class);
    }

    public void testPeriodicCase() {
        doTest("periodic-case", Periodic.class);
    }

    public void testHaltCase() {
        doTest("halt-case", HaltBoundary.class);
    }

    private void doTest(String eName, Class expected) {
        Element e = root.element(eName);
        Class actual = BoundaryFactory.instantiate(e, lattice, shape).getClass();
        assertEquals(expected, actual);
    }
}