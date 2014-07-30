/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import geometry.MockGeometry;
import io.visual.map.PixelTranslator;
import io.visual.map.PixelTranslatorFactory;
import io.visual.map.TriPixelTranslator;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/3/14.
 */
public class PixelTranslatorFactoryTest extends EslimeTestCase {
    private MockGeometry geom;

    @Override
    protected void setUp() throws Exception {
        geom = new MockGeometry();
    }

    public void testTriangularCase() {
        geom.setDimensionality(2);
        geom.setConnectivity(3);

        PixelTranslator actual = PixelTranslatorFactory.instantiate(geom);
        PixelTranslator expected = new TriPixelTranslator();

        assertEquals(expected, actual);
    }

}
