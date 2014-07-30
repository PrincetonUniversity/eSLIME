/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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