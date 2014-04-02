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

package io.serialize.binary;

import junit.framework.TestCase;

/**
 * As a graphics I/O class, this seemed better suited to an integration
 * test than to a system of unit tests. The fixtures are based on those
 * of the SystemStateReader class, and the output utilizes a base version
 * of the MapVisualization class.
 * <p/>
 * Created by dbborens on 4/2/14.
 */
public class VisualizationSerializerTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testLifeCycle() {
        fail("Don't bother implementing this test until all aspects of MapVisualization work as expected. You will want to generate a set of reference images and then assert that these are binary-equal to the output of the visualization.");
    }
}
