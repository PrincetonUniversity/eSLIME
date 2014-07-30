/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
