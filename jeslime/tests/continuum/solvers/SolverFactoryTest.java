/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package continuum.solvers;

import geometry.MockGeometry;
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
