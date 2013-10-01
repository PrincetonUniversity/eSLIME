package jeslime;
import java.util.HashSet;

import junit.framework.TestCase;
import structural.Flags;
import structural.identifiers.Coordinate;


public class CoordinateTest extends TestCase {
	
	public void testConstructors2D () {
		// Construct a 3D coordinate.
		Coordinate first = new Coordinate(2, 4, 6, 0);

		// Check values and dimensionality flag.
		assertEquals(first.x(), 2);
		assertEquals(first.y(), 4);
		assertEquals(first.z(), 6);
		assertEquals(first.flags(), 0);

		// Copy that coordinate into another coordinate.
		Coordinate second = first;

		// Check overloaded equality operator.
		assertEquals(first, second);

		// Replace one of the coordinates with something different.
		second = new Coordinate(3, 6, 9, 0);

		// Check values of new coordinate.
		assertEquals(second.x(), 3);
		assertEquals(second.y(), 6);
		assertEquals(second.z(), 9);

		// Verify non-equality.
		assertFalse(first.equals(second));
	}

	public void testConstructors3D() {
		// Construct a 2D coordinate.
		Coordinate first = new Coordinate(2, 4, 0);

		// Check values and dimensionality flag.
		assertEquals(first.x(), 2);
		assertEquals(first.y(), 4);
		assertEquals(Flags.PLANAR, first.flags());

		// Copy that coordinate into another coordinate.
		Coordinate second = first;

		// Check overloaded equality operator.
		assertEquals(first, second);

		// Replace one of the coordinates with something different.
		second = new Coordinate(3, 6, 0);

		// Check values of new coordinate.
		assertEquals(second.x(), 3);
		assertEquals(second.y(), 6);
		assertEquals(second.flags(), Flags.PLANAR);

		// Verify non-equality.
		assertFalse(first.equals(second));
	}

	public void testFlags() {
		// Create a coordinate with a couple of flags.
		Coordinate first = new Coordinate(2, 4, Flags.TRIANGULAR | Flags.BEYOND_BOUNDS);

		// Verify that hasFlag(...) works for each.
		assertEquals(first.flags(), Flags.TRIANGULAR | Flags.BEYOND_BOUNDS | Flags.PLANAR);
		assertTrue(first.hasFlag(Flags.TRIANGULAR));
		assertTrue(first.hasFlag(Flags.BEYOND_BOUNDS));
		assertTrue(first.hasFlag(Flags.PLANAR));

		// Create a second coordinate at same location but without flags.
		Coordinate second = new Coordinate(2, 4, 0);
		assertEquals(second.flags(), Flags.PLANAR);

		// Verify non-equality.
		assertFalse(first.equals(second));;
	}

	public void testStrings() {
		// Create a 2D coordinate.
		Coordinate first = new Coordinate(2, 4, 0);

		// EXPECT_STREQ, but it was acting weird -- done is better than perfect
		// Verify expected string form.

		assertEquals("(2, 4 | 1)", first.stringForm());

		// Verify expected vector form.
		assertEquals("<2, 4>", first.vectorForm());

		// Create a 3D coordinate.
		Coordinate second = new Coordinate(2, 4, 6, 0);

		// Verify expected string form.
		assertEquals("(2, 4, 6 | 0)", second.stringForm());

		// Verify expected vector form.
		assertEquals("<2, 4, 6>", second.vectorForm());

	}

	public void testHashing() {
		// Create two logically equivalent coordinates.
		Coordinate first = new Coordinate(2, 4, 0);
		Coordinate second = new Coordinate(2, 4, 0);

		// Note that GoogleTest's assertEquals does not use the equality operator,
		// which is weird.

		assertEquals(first, second);

		// Create a logically different coordinate.
		Coordinate third = new Coordinate(3, 6, 0);
		assertFalse(second.equals(third));

		// Adding two logically different coordinates to a set should be fine.
		HashSet<Coordinate> coords = new HashSet<Coordinate>();;
		assertEquals(coords.size(), 0);

		coords.add(first);
		assertEquals(coords.size(), 1);

		coords.add(third);
		assertEquals(coords.size(), 2);


		// Adding a coordinate that is logically identical to one already present
		// shouldn't add anything
		coords.add(second);
		assertEquals(coords.size(), 2);
	}

}
