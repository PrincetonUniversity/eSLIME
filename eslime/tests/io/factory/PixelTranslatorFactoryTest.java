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
