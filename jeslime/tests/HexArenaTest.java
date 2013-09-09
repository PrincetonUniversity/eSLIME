import java.util.HashSet;

import structural.Coordinate;
import structural.Flags;
import geometries.HexArena;
import junit.framework.TestCase;


public class HexArenaTest extends TestCase {

	private void checkCtorException(int x, int y, boolean expected) {
		boolean actual = false;
		try {
			new HexArena(x, y);
		} catch (Exception e) {
			actual = true;
		}
		
		assertEquals(actual, expected);
	}
	
	public void testConstructor() {
		checkCtorException(3,  2,  true);
		checkCtorException(2,  3,  true);
		checkCtorException(0,  2,  true);
		checkCtorException(2,  0,  true);
		checkCtorException(-6, 2,  true);
		checkCtorException(4,  4,  false);
	}


	// Test indexing and de-indexing behavior in 2D and 3D.
	//
	// Note that indexing works whether or not the coordinates are
	// valid for the specified geometry. Indexing should ultimately
	// be offloaded to an indexer object.
	//
	// Also note that calling the z coordinate of a 2D point will,
	// by design, return 0.
	public void testIndex() {
		HexArena hr = new HexArena(4, 4);

		Coordinate o2 = new Coordinate(0, 0, 0);
		Coordinate o3 = new Coordinate(0, 0, 0, 0);

		// Origin
		assertEquals(0, o2.x());
		assertEquals(0, o2.y());
		assertEquals(0, o2.z());

		assertEquals(0, o3.x());
		assertEquals(0, o3.y());
		assertEquals(0, o3.z());


		// 3D point
		Coordinate p3 = new Coordinate(15, 37, 262, 0);
		assertEquals(15, p3.x());
		assertEquals(37, p3.y());
		assertEquals(262, p3.z());

		// 2D point
		Coordinate p2 = new Coordinate(24, 99, 0);
		assertEquals(24, p2.x());
		assertEquals(99, p2.y());
		assertEquals(0, p2.z());
	}

	public void testCanonicalSites() {
		// Produce 6x4 HexArena
		int height = 6;
		int width = 4;
		HexArena hr = new HexArena(height, width);

		// Create unordered set of all expected coordinates
		HashSet<Coordinate> s = new HashSet<Coordinate>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int yAdj = y + (x / 2);
				Coordinate c = new Coordinate(x, yAdj, 0);
				s.add(c);
			}
		}

		Coordinate[] sites = hr.getCanonicalSites();

		// Vector should contain all of the sites.
		assertEquals(s.size(), sites.length);
		for (int i = 0; i < sites.length; i++) {
			Coordinate coord = sites[i];
			assertTrue(s.contains(coord));
		}
	}

	// "DONE"
	// getL2Distance(...)
	public void testL2Distance() {
		HexArena hr = new HexArena(4, 4);

		Coordinate p = new Coordinate(3, 3, 0);
		Coordinate q = new Coordinate(4, 4, 0);

		// This functionality should throw an U.O.E.
		boolean actual = false;
		try {
			hr.getL2Distance(p, q);
		} catch (Exception ex) {
			actual = true;
		}
		
		assertTrue(actual);
	}


	// getL1Distance(...)
	// getDisplacement(...)
	public void testL1AndDisplacement() {
		HexArena hr = new HexArena(4, 6);

		Coordinate p = new Coordinate(1, 1, 0);
		Coordinate q = new Coordinate(2, 4, 0);

		int[] disp = hr.getDisplacement(p, q);

		assertEquals(1, disp[0]);
		assertEquals(2, disp[1]);
		assertEquals(0, disp[2]);
		assertEquals(3, hr.getL1Distance(p, q));

		p = new Coordinate(1, 2, 0);
		q = new Coordinate(0, 0, 0);

		disp = hr.getDisplacement(p, q);

		assertEquals(-1, disp[0]);
		assertEquals(-1, disp[1]);
		assertEquals(0, disp[2]);
		assertEquals(2, hr.getL1Distance(p, q));

		p = new Coordinate(0, 0, 0);
		q = new Coordinate(0, 0, 0);

		disp = hr.getDisplacement(p, q);

		assertEquals(0, disp[0]);
		assertEquals(0, disp[1]);
		assertEquals(0, disp[1]);
		assertEquals(0, hr.getL1Distance(p, q));
	}


	// Test (non-)wrapping
	public void testWrap() {

		HexArena hr = new HexArena(4, 4);

		// Over right edge
		Coordinate actual = hr.wrap(4, 2, 0);
		Coordinate expected = new Coordinate(4, 2, Flags.END_OF_WORLD);
		assertEquals(actual, expected);

		// Over left edge
		actual = hr.wrap(-1, 0, 0);
		expected = new Coordinate(-1, 0, Flags.END_OF_WORLD);
		assertEquals(actual, expected);

		// Above
		actual = hr.wrap(4, 0, 0);
		expected = new Coordinate(4, 0, Flags.END_OF_WORLD);

		// No wrap (internal coordinate)
		actual = hr.wrap(2, 3, 0);
		expected = actual;
		assertEquals(actual, expected);

		// More than twice the system width
		actual = hr.wrap(9, 6, 0);
		expected = new Coordinate(9, 6, Flags.END_OF_WORLD);
		assertEquals(actual, expected);

	}

	public void testCellNeighbors() {
		HexArena hr = new HexArena(6, 6);

		// Interior
		Coordinate coord = hr.wrap(3, 4, 0);

		HashSet<Coordinate> interior_exp = new HashSet<Coordinate>();
		interior_exp.add(new Coordinate(3, 5, 0));
		interior_exp.add(new Coordinate(4, 5, 0));
		interior_exp.add(new Coordinate(4, 4, 0));
		interior_exp.add(new Coordinate(3, 3, 0));
		interior_exp.add(new Coordinate(2, 3, 0));
		interior_exp.add(new Coordinate(2, 4, 0));

		Coordinate[] neighbors = hr.getCellNeighbors(coord);

		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(interior_exp.contains(neighbor));
		}

		// Side -- check wrapped
		coord = hr.wrap(5, 5, 0);

		HashSet<Coordinate> side_exp = new HashSet<Coordinate>();
		side_exp.add(new Coordinate(5, 6, 0));
		side_exp.add(new Coordinate(6, 6, Flags.END_OF_WORLD));
		side_exp.add(new Coordinate(6, 5, Flags.END_OF_WORLD));
		side_exp.add(new Coordinate(5, 4, 0));
		side_exp.add(new Coordinate(4, 4, 0));
		side_exp.add(new Coordinate(4, 5, 0));

		neighbors = hr.getCellNeighbors(coord);
		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(side_exp.contains(neighbor));
		}

		// Bottom
		coord = hr.wrap(2, 1, 0);

		HashSet<Coordinate> bottom_exp = new HashSet<Coordinate>();
		bottom_exp.add(new Coordinate(2, 0, Flags.END_OF_WORLD));
		bottom_exp.add(new Coordinate(1, 0, 0));
		bottom_exp.add(new Coordinate(1, 1, 0));
		bottom_exp.add(new Coordinate(2, 2, 0));
		bottom_exp.add(new Coordinate(3, 2, 0));
		bottom_exp.add(new Coordinate(3, 1, 0));

		neighbors = hr.getCellNeighbors(coord);

		assertEquals(6, neighbors.length);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(bottom_exp.contains(neighbor));
		}
	}

	// All cases but top/bottom should be same as getCellNeighbors(...)
	// for the HexTorus geometry.
	// getSoluteNeighbors(...)
	public void testSoluteNeighbors() {
		HexArena hr = new HexArena(6, 6);

		Coordinate[] neighbors;

		// Interior
		Coordinate coord = new Coordinate(3, 4, 0);

		HashSet<Coordinate> interior_exp = new HashSet<Coordinate>();
		interior_exp.add(new Coordinate(3, 5, 0));
		interior_exp.add(new Coordinate(4, 5, 0));
		interior_exp.add(new Coordinate(4, 4, 0));
		interior_exp.add(new Coordinate(3, 3, 0));
		interior_exp.add(new Coordinate(2, 3, 0));
		interior_exp.add(new Coordinate(2, 4, 0));

		neighbors = hr.getSoluteNeighbors(coord);

		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(interior_exp.contains(neighbor));
		}

		// Side
		coord = new Coordinate(5, 5, 0);

		HashSet<Coordinate> side_exp = new HashSet<Coordinate>();
		side_exp.add(new Coordinate(5, 6, 0));
		side_exp.add(new Coordinate(6, 5, Flags.END_OF_WORLD));
		side_exp.add(new Coordinate(6, 6, Flags.END_OF_WORLD));
		side_exp.add(new Coordinate(5, 4, 0));
		side_exp.add(new Coordinate(4, 4, 0));
		side_exp.add(new Coordinate(4, 5, 0));

		neighbors = hr.getSoluteNeighbors(coord);
		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(side_exp.contains(neighbor));
		}

		// Bottom
		coord = new Coordinate(2, 1, 0);

		HashSet<Coordinate> bottom_exp = new HashSet<Coordinate>();
		bottom_exp.add(new Coordinate(1, 0, 0));
		bottom_exp.add(new Coordinate(1, 1, 0));
		bottom_exp.add(new Coordinate(2, 2, 0));
		bottom_exp.add(new Coordinate(3, 2, 0));
		bottom_exp.add(new Coordinate(3, 1, 0));

		// The cell is counted as a "neighbor" because southerly-moving
		// solute is reflected back.
		bottom_exp.add(new Coordinate(2, 0, Flags.END_OF_WORLD));

		neighbors = hr.getSoluteNeighbors(coord);
		assertEquals(6, neighbors.length);

		for (int i = 0; i < neighbors.length; i++) {

			Coordinate neighbor = neighbors[i];
			assertTrue(bottom_exp.contains(neighbor));
		}

	}

	// getAnnulus(...)
	public void testAnnulus() {
		HexArena hr = new HexArena(4, 4);

		Coordinate coord = new Coordinate(0, 2, 0);

		Coordinate[] result;

		// Point
		result = hr.getAnnulus(coord, 0, false);
		assertEquals(1, result.length);
		assertEquals(coord, result[0]);

		result = hr.getAnnulus(coord, 0, true);
		assertEquals(1, result.length);
		assertEquals(coord, result[0]);

		// r=1
		result = hr.getAnnulus(coord, 1, true);
		assertEquals(6, result.length);

		// r=2 (big)--circumnavigation shouldn't matter.
		result = hr.getAnnulus(coord, 2, true);
		assertEquals(12, result.length);
		result = hr.getAnnulus(coord, 2, false);
		assertEquals(12, result.length);
	}

	// Tests for correct behavior in vicinity of origin when dimensions
	// are 6x6, as in the lattice tests.
	public void originWrap() {
		// Explicitly test wrapping behavior in vicinity of origin
		HexArena hr = new HexArena(6, 6);

		// (1, 0) stays (1, 0)
		assertTrue(hr.wrap(1, 0, 0) == new Coordinate(1, 0, 0));

		// (1, 1) stays (1, 1)
		assertTrue(hr.wrap(1, 1, 0) == new Coordinate(1, 1, 0));

		// (0, 1) stays (0, 1)
		assertTrue(hr.wrap(0, 1, 0) == new Coordinate(0, 1, 0));

		// (-1, -1) gets a flag
		assertTrue(hr.wrap(-1, -1, 0) == new Coordinate(-1, -1, Flags.END_OF_WORLD));

		// (-1, 0) gets a flag
		assertTrue(hr.wrap(-1, 0, 0) == new Coordinate(-1, 0, Flags.END_OF_WORLD));

		// (0, -1) gets a flag
		assertTrue(hr.wrap(0, -1, 0) == new Coordinate(0, -1, Flags.END_OF_WORLD));
	}

}
