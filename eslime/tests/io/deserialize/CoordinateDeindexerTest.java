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

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.deserialize;

import control.identifiers.Coordinate;
import test.EslimeTestCase;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateDeindexerTest extends EslimeTestCase {

    private ExposedDeindexer query;

    @Override
    public void setUp() {
        query = new ExposedDeindexer(fixturePath);
    }

    public void testIndex() {
        // This coordinate has index 1 -- see test fixture
        Coordinate input = new Coordinate(0, 1, 1);
        assertEquals(1, (int) query.getIndex(input));
    }

    public void testDeindex() {
        Coordinate expected = new Coordinate(0, 1, 1);
        Coordinate actual = query.getCoordinate(1);
        assertEquals(expected, actual);
    }

    public void testParseCoordinate() {
        String input = "(3, 5, 9 | 0)";
        Coordinate expected = new Coordinate(3, 5, 9, 0);
        Coordinate actual = query.parseCoordinate(input);
        assertEquals(expected, actual);
    }

    public void testGetNumSites() {
        assertEquals(4, query.getNumSites());
    }

    private class ExposedDeindexer extends CoordinateDeindexer {

        public HashMap<Integer, Coordinate> indexToCoord;

        public ExposedDeindexer(String path) {
            super(path);
            indexToCoord = super.indexToCoord;
        }

        public void deindex() throws IOException {
            super.deindex();
        }

        public Coordinate parseCoordinate(String token) {
            return super.parseCoordinate(token);
        }
    }
}
