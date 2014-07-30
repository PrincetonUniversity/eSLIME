/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import geometry.MockGeometry;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/25/14.
 */
public class CellLayerTest extends EslimeTestCase {

    public void testClone() {
        MockGeometry geom = buildMockGeometry();
        CellLayer query = new CellLayer(geom);
        CellLayer clone = query.clone();
        assertEquals(query, clone);
        assertFalse(query == clone);
    }
}
