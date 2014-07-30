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
import control.identifiers.Extrema;
import test.EslimeTestCase;

import java.io.File;

/**
 * Created by dbborens on 12/11/13.
 */
public class ExtremaReaderTest extends EslimeTestCase {
    /**
     * Read from a fixture file containing a single extreme value pair.
     *
     * @throws Exception
     */
    public void testReadSingleton() throws Exception {
        String filename = fixturePath + "solute42.metadata.txt";

        File singletonFixture = new File(filename);
        ExtremaReader reader = new ExtremaReader(singletonFixture);

        Extrema actual = reader.get("extrema");

        Extrema expected = new Extrema();
        expected.consider(0, new Coordinate(0, 0, 0, 0), 2.0);
        expected.consider(5.0, new Coordinate(1, 0, 0, 0), 1.0);
        assertEquals(expected, actual);
    }

    /**
     * Read from a fixture file containing multiple extreme value pairs.
     *
     * @throws Exception
     */
    public void testReadMulti() throws Exception {
        Extrema a, c, actual;

        // Set up expected values
        a = new Extrema();
        a.consider(0.0, new Coordinate(0, 0, 0, 0), 2.0);
        a.consider(7.0, new Coordinate(1, 0, 0, 0), 1.0);

        c = new Extrema();
        c.consider(2.0, new Coordinate(0, 2, 0, 0), 2.0);
        c.consider(9.0, new Coordinate(1, 0, 0, 0), 1.0);

        // Read file
        String filename = fixturePath + "multi.metadata.txt";

        File singletonFixture = new File(filename);
        ExtremaReader reader = new ExtremaReader(singletonFixture);

        // Check "a"
        actual = reader.get("a");
        assertEquals(a, actual);

        // Check "c"
        actual = reader.get("c");
        assertEquals(c, actual);
    }
}
