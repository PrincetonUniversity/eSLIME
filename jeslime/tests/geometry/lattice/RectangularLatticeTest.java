/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package geometry.lattice;

import test.EslimeTestCase;
import structural.Flags;
import structural.identifiers.Coordinate;

public class RectangularLatticeTest extends EslimeTestCase {

	private Lattice lattice;
	
	@Override
    public void setUp() throws Exception {
		super.setUp();
        lattice = new RectangularLattice();
    }

	public void testDimensionality() {
		assertEquals(2, lattice.getDimensionality());
	}
	
	public void testConnectivity() {
		assertEquals(2, lattice.getConnectivity());
	}
	
	public void testAdjust() {
		Coordinate initial, actual, expected;
		
		// The origin should be unaffected
		initial = new Coordinate(0, 0, 0);
		actual = lattice.adjust(initial);
		expected = new Coordinate(0, 0, 0);
		assertEquals(actual, expected);
		
		// Y offset should be unaffected
		initial = new Coordinate(0, 8, 0);
		actual = lattice.adjust(initial);
		expected = new Coordinate(0, 8, 0);
		assertEquals(actual, expected);
		
		// X offset should be unaffected
		initial = new Coordinate(8, 0, 0);
		actual = lattice.adjust(initial);
		expected = new Coordinate(8, 0, 0);
		assertEquals(actual, expected);
	}
	
	public void testBasis() {
		Coordinate[] basis = lattice.getBasis();
		
		assertEquals(basis.length, 2);
		
		Coordinate east = new Coordinate(1, 0, 0);
		Coordinate north = new Coordinate(0, 1, 0);
		
		assertEquals(basis[0], east);
		assertEquals(basis[1], north);
	}
	
	public void testGetAnnulus() {
		Coordinate[] actual, expected;
		Coordinate origin = new Coordinate(0, 0, 0);
		
		// r=0		
		actual = lattice.getAnnulus(origin, 0);
		expected = new Coordinate[] {origin};
		assertArraysEqual(actual, expected, true);

		// r=1
		actual = lattice.getAnnulus(origin, 1);
		expected = new Coordinate[] {new Coordinate( 1,  0,  0),
									 new Coordinate(-1,  0,  0),
									 new Coordinate( 0,  1,  0),
									 new Coordinate( 0, -1,  0)};
		
		assertArraysEqual(actual, expected, true);
		
		// r=2
		actual = lattice.getAnnulus(origin, 2);
		expected = new Coordinate[] {new Coordinate( 2,  0,  0),
									 new Coordinate( 1,  1,  0),
									 new Coordinate( 0,  2,  0),
									 new Coordinate(-1,  1,  0),
									 new Coordinate(-2,  0,  0),
									 new Coordinate(-1, -1,  0),
									 new Coordinate( 0, -2,  0),
									 new Coordinate( 1, -1,  0)};
		
		assertArraysEqual(actual, expected, true);
	}
	

	
	public void testGetNeighbors() {
		Coordinate[] actual, expected;
		Coordinate origin = new Coordinate(0, 0, 0);
		
		// Should be same as r=1 annulus
		actual = lattice.getNeighbors(origin);
		expected = new Coordinate[] {new Coordinate( 1,  0,  0),
									 new Coordinate(-1,  0,  0),
									 new Coordinate( 0,  1,  0),
									 new Coordinate( 0, -1,  0)};
		
		assertArraysEqual(expected, actual, true);
	}
	
	public void testGetDisplacement() {
		Coordinate o, p, q, r;
		Coordinate expected, actual;
		
		o = new Coordinate(0, 0, 0);
		p = new Coordinate(3, 0, 0);
		q = new Coordinate(0, 3, 0);
		r = new Coordinate(3, 3, 0);
		
		// Horizontal
		actual = lattice.getDisplacement(o, p);
		expected = new Coordinate(3, 0, Flags.VECTOR);
		assertEquals(expected, actual);
		
		// Vertical
		actual = lattice.getDisplacement(o, q);
		expected = new Coordinate(0, 3, Flags.VECTOR);
		assertEquals(expected, actual);
		
		// Both horizontal and vertical
		actual = lattice.getDisplacement(o, r);
		expected = new Coordinate(3, 3, Flags.VECTOR);
		assertEquals(expected, actual);
	}
	
	public void testRel2Abs() {
		Coordinate o, p, q;
		Coordinate actual, expected;
		
		o = new Coordinate(0, 0, 0);
		p = new Coordinate(3, 0, 0);
		q = new Coordinate(0, 3, 0);
		
		Coordinate op, oq;
		op = new Coordinate(3, 0, Flags.VECTOR);
		oq = new Coordinate(0, 3, Flags.VECTOR);
		
		// Horizontal
		actual = lattice.rel2abs(o, op);
		expected = p;
		assertEquals(expected, actual);
		
		// Vertical
		actual = lattice.rel2abs(o, oq);
		expected = q;
		assertEquals(expected, actual);
	}
	
	public void testGetL1Distance() {
		Coordinate o, p, q, r;
		int expected, actual;
		
		o = new Coordinate(0, 0, 0);
		p = new Coordinate(3, 0, 0);
		q = new Coordinate(0, -3, 0);
		r = new Coordinate(-3, 3, 0);
		
		// Horizontal
		actual = lattice.getL1Distance(o, p);
		expected = 3;
		assertEquals(expected, actual);
		
		// Vertical
		actual = lattice.getL1Distance(o, q);
		expected = 3;
		assertEquals(expected, actual);
		
		// Both horizontal and vertical
		actual = lattice.getL1Distance(o, r);
		expected = 6;
		assertEquals(expected, actual);
	}
	
	public void testOrthoDisplacement() {
		Coordinate o, p, q, r;
		Coordinate expected, actual;
		
		o = new Coordinate(0, 0, 0);
		p = new Coordinate(3, 0, 0);
		q = new Coordinate(0, 3, 0);
		r = new Coordinate(3, 3, 0);
		
		// Horizontal
		actual = lattice.getOrthoDisplacement(o, p);
		expected = new Coordinate(3, 0, Flags.VECTOR);
		assertEquals(expected, actual);
		
		// Vertical
		actual = lattice.getOrthoDisplacement(o, q);
		expected = new Coordinate(0, 3, Flags.VECTOR);
		assertEquals(expected, actual);
		
		// Both horizontal and vertical
		actual = lattice.getOrthoDisplacement(o, r);
		expected = new Coordinate(3, 3, Flags.VECTOR);
		assertEquals(expected, actual);	}
	
	public void testInvAdjust() {
		Coordinate initial, actual, expected;
		
		// The origin should be unaffected
		initial = new Coordinate(0, 0, 0);
		actual = lattice.invAdjust(initial);
		expected = new Coordinate(0, 0, 0);
		assertEquals(actual, expected);
		
		// Y offset should be unaffected
		initial = new Coordinate(0, 8, 0);
		actual = lattice.invAdjust(initial);
		expected = new Coordinate(0, 8, 0);
		assertEquals(actual, expected);
		
		// X offset should be unaffected
		initial = new Coordinate(8, 0, 0);
		actual = lattice.invAdjust(initial);
		expected = new Coordinate(8, 0, 0);
		assertEquals(actual, expected);	
	}

    public void testClone() {
        Object cloned = lattice.clone();
        assertEquals(lattice.getClass(), cloned.getClass());
        assertFalse(lattice == cloned);
    }
}
