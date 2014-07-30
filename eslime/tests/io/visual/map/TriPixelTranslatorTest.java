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

package io.visual.map;

import control.identifiers.Coordinate;
import io.visual.VisualizationProperties;
import test.EslimeTestCase;

/**
 * Created by dbborens on 4/1/14.
 */
public class TriPixelTranslatorTest extends EslimeTestCase {

    private TriPixelTranslator query;
    private Coordinate c0, c1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c0 = new Coordinate(0, 0, 0);
        c1 = new Coordinate(1, 0, 0);

        Coordinate[] cc = new Coordinate[]{c0, c1};
        VisualizationProperties mapState = new VisualizationProperties(null, 10.0, 1);
        mapState.setCoordinates(cc);
        query = new TriPixelTranslator();
        query.init(mapState);
    }

    public void testOrigin() throws Exception {
        Coordinate expected = new Coordinate(10, 9, 0);
        Coordinate actual = query.indexToPixels(c0);
        assertEquals(expected, actual);
    }

    public void testIndexToPixels() throws Exception {
        Coordinate actual = query.indexToPixels(c1);
        Coordinate expected = new Coordinate(25, 18, 0);
        assertEquals(expected, actual);
    }


    public void testGetImageDims() throws Exception {
        Coordinate actual = query.getImageDims();
        Coordinate expected = new Coordinate(36, 27, 0);
        assertEquals(expected, actual);
    }

    public void testGetDiagonal() throws Exception {
        double expected = 20.0;
        double actual = query.getDiagonal();
        assertEquals(expected, actual, epsilon);
    }
}
