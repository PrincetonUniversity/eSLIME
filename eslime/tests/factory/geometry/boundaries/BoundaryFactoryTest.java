/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.geometry.boundaries;//import junit.framework.TestCase;

import control.arguments.GeometryDescriptor;
import geometry.boundaries.*;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import org.dom4j.Element;
import test.EslimeTestCase;

public class BoundaryFactoryTest extends EslimeTestCase {

    private Element root;
    private GeometryDescriptor geometryDescriptor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/boundary/BoundaryFactoryTest.xml");
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 1, 1);
        geometryDescriptor = new GeometryDescriptor(lattice, shape);
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
        doTest("halt-case", HaltArena.class);
    }

    public void testTetrisCase() {
        doTest("tetris-case", TetrisBoundary.class);
    }

    private void doTest(String eName, Class expected) {
        Element e = root.element(eName);
        Class actual = BoundaryFactory.instantiate(e, geometryDescriptor).getClass();
        assertEquals(expected, actual);
    }
}