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

package layers.solute;

import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.MockLayerManager;
import test.EslimeTestCase;

public class LightweightSoluteLayerTest extends EslimeTestCase {

    private Coordinate c;
    private LightweightSoluteLayer query;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockGeometry geom = new MockGeometry();
        c = new Coordinate(0, 0, 0);
        Coordinate[] cc = new Coordinate[] {c};
        geom.setCanonicalSites(cc);
        MockLayerManager layerManager = new MockLayerManager();
        query = new LightweightSoluteLayer(geom, layerManager, "test");
    }

    public void testLifeCycle() {
        double actual = query.getState().getAbsolute(c);
        double expected = 0.0;

        assertEquals(expected, actual, epsilon);

        query.set(c, 1.0);
        actual = query.getState().getAbsolute(c);
        expected = 1.0;
        assertEquals(expected, actual, epsilon);
    }
}