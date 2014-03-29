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

package geometry.lattice;

import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.identifiers.Coordinate;
import structural.identifiers.Flags;
import test.EslimeTestCase;

/**
 * Tests that the rectangle geometry shape works as expected
 * with the triangular lattice.
 *
 * @author dbborens
 */
public class TriLatticeRectangleTest extends EslimeTestCase {

    private Shape odd;
    private Shape even;

    @Override
    public void setUp() {
        Lattice oddLattice = new TriangularLattice();
        Lattice evenLattice = new TriangularLattice();

        even = new Rectangle(evenLattice, 4, 2);
        odd = new Rectangle(oddLattice, 5, 3);
    }

    public void testGetCenter() {
        Coordinate actual, expected;

        // Even -- we round down
        expected = new Coordinate(1, 0, 0);
        actual = even.getCenter();
        assertEquals(expected, actual);

        // Odd
        expected = new Coordinate(2, 2, 0);
        actual = odd.getCenter();
        assertEquals(expected, actual);
    }

    public void testGetBoundaries() {
        Coordinate[] actual, expected;

        // Even
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(3, 1, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(2, 2, 0),
                new Coordinate(3, 2, 0),
        };
        actual = even.getBoundaries();
        assertArraysEqual(actual, expected, true);

        // Odd
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(3, 1, 0),
                new Coordinate(4, 2, 0),
                new Coordinate(4, 3, 0),
                new Coordinate(4, 4, 0),
                new Coordinate(3, 3, 0),
                new Coordinate(2, 3, 0),
                new Coordinate(1, 2, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(0, 1, 0)
        };

        actual = odd.getBoundaries();
        assertArraysEqual(expected, actual, true);
    }

    public void testCanonicalSites() {
        Coordinate[] actual, expected;

        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(1, 0, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(3, 1, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(2, 2, 0),
                new Coordinate(3, 2, 0),
        };
        actual = even.getCanonicalSites();
        assertArraysEqual(actual, expected, true);
    }

    public void testOverbounds() {

        // In bounds coordinates
        Coordinate a, b, c;
        a = new Coordinate(0, 0, 0);
        b = new Coordinate(1, 0, 0);
        c = new Coordinate(1, 1, 0);

        // Out of bounds coordinates
        Coordinate p, q, r, s;
        p = new Coordinate(0, 3, 0);
        q = new Coordinate(5, 5, 0);
        r = new Coordinate(-1, 0, 0);
        s = new Coordinate(-1, -1, 0);

        Coordinate expected, actual;

        // Even
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(a);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(b);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(c);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 2, Flags.VECTOR);
        actual = even.getOverbounds(p);
        assertEquals(expected, actual);

        expected = new Coordinate(2, 2, Flags.VECTOR);
        actual = even.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, Flags.VECTOR);
        actual = even.getOverbounds(r);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, Flags.VECTOR);
        actual = even.getOverbounds(s);
        assertEquals(expected, actual);

        // Odd
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(a);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(b);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(c);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 1, Flags.VECTOR);
        actual = odd.getOverbounds(p);
        assertEquals(expected, actual);

        expected = new Coordinate(1, 1, Flags.VECTOR);
        actual = odd.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, Flags.VECTOR);
        actual = odd.getOverbounds(r);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, Flags.VECTOR);
        actual = odd.getOverbounds(s);
        assertEquals(expected, actual);
    }

    public void testDimensions() {
        int[] actual, expected;

        // Odd
        expected = new int[]{5, 3};
        actual = odd.getDimensions();
        assertArraysEqual(actual, expected, false);

        // Even
        expected = new int[]{4, 2};
        actual = even.getDimensions();
        assertArraysEqual(actual, expected, false);
    }

	/*public void testGetLimits() {
        Coordinate[] actual, expected;

		// Even
		expected = new Coordinate[] {
			new Coordinate(-1, 0, 0, Flags.VECTOR),
			new Coordinate(0, 2, 0, Flags.VECTOR)
		};
		
		actual = even.getLimits();
		assertArraysEqual(actual, expected, false);
		
		// Odd
		expected = new Coordinate[] {
				new Coordinate(-1, -2, Flags.VECTOR),
				new Coordinate(1, 2, Flags.VECTOR)
			};
		
		actual = odd.getLimits();
		assertArraysEqual(actual, expected, false);
	}*/
}
