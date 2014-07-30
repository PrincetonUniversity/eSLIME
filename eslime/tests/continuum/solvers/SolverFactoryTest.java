/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package continuum.solvers;

import geometry.MockGeometry;
import factory.continuum.solvers.SolverFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 1/6/14.
 */
public class SolverFactoryTest extends EslimeTestCase {
    private Element fixtureRoot;
    private MockGeometry geom;

    @Override
    protected void setUp() throws Exception {
        String fn = "SolverFactoryTest.xml";
        fixtureRoot = readXmlFile(fn);
        geom = new MockGeometry();
    }

    public void testGetSimpleCgsSolver() throws Exception {
        Element solverRoot = fixtureRoot.element("simple-cgs-test");
        Solver query = SolverFactory.instantiate(solverRoot, geom);
        assertTrue(query instanceof SimpleCgsSolver);
        System.err.println("WARNING! Test is not checking for range or coefficient scaling.");
    }

    /**
     * Test null case, which is included for testing other classes.
     *
     * @throws Exception
     */
    public void testNull() throws Exception {
        Element solverRoot = fixtureRoot.element("null-test");
        Solver query = SolverFactory.instantiate(solverRoot, geom);
        assertNull(query);
    }

    public void testScaling() {
        Element unscaledRoot = fixtureRoot.element("null-test");
        Element scaledRoot = fixtureRoot.element("scaled-test");

        SolverFactory.instantiate(unscaledRoot, geom);
        assertEquals(0, geom.getLastRequestedScale(), epsilon);

        SolverFactory.instantiate(scaledRoot, geom);
        assertEquals(2.0, geom.getLastRequestedScale(), epsilon);
    }
}
