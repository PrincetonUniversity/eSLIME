import java.util.HashSet;

import geometries.HexArena;
import geometries.HexRing;
import structural.Coordinate;
import structural.Flags;
import junit.framework.TestCase;


public class HexRingTest extends TestCase {

	private void checkCtorException(int x, int y, boolean expected) {
		boolean actual = false;
		try {
			new HexArena(x, y);
		} catch (Exception e) {
			actual = true;
		}
		
		assertEquals(actual, expected);
	}
	
	// DONE
	public void testConstructor() {
		// These should blow up (for now)
		checkCtorException(3,  2,  true);
		checkCtorException(2,  3,  true);
		checkCtorException(0,  2,  true);
		checkCtorException(2,  0,  true);
		checkCtorException(-6, 2,  true);
		checkCtorException(4,  4,  false);
	}

	// DONE
	// Test indexing and de-indexing behavior in 2D and 3D.
	//
	// Note that indexing works whether or not the coordinates are
	// valid for the specified geometry. Indexing should ultimately
	// be offloaded to an indexer object.
	//
	// Also note that calling the z coordinate of a 2D point will,
	// by design, return 0.
	public void testIndex() {
		HexRing hr = new HexRing(4, 4);

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

	// DONE
	public void testCanonicalSites() {
		// Produce 6x4 HexRing
		int height = 6;
		int width = 4;
		HexRing hr = new HexRing(height, width);

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
		HexRing hr = new HexRing(4, 4);

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

	// DONE
	// getL1Distance(...)
	// getDisplacement(...)
	public void testL1AndDisplacement() {
		HexRing hr = new HexRing(4, 6);

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

	// DONE
	// Test wrapping
	public void testWrap() {

		HexRing hr = new HexRing(4, 4);

		// Over right edge
		Coordinate actual = hr.wrap(4, 2, 0);
		Coordinate expected = new Coordinate(0, 0, 0);
		assertEquals(actual, expected);

		// Over left edge
		actual = hr.wrap(-1, 0, 0);
		expected = new Coordinate(3, 2, 0);
		assertEquals(actual, expected);

		// Out of bounds
		actual = hr.wrap(4, 0, 0);
		assertTrue(actual.hasFlag(Flags.BEYOND_BOUNDS));

		// No wrap (internal coordinate)
		actual = hr.wrap(2, 3, 0);
		expected = actual;
		assertEquals(actual, expected);

		// Around twice
		actual = hr.wrap(9, 6, 0);
		expected = new Coordinate(1, 2, 0);
		assertEquals(actual, expected);

		
	}

	public void testCellNeighbors() {
		HexRing hr = new HexRing(6, 6);

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
		side_exp.add(new Coordinate(0, 3, 0));
		side_exp.add(new Coordinate(0, 2, 0));
		side_exp.add(new Coordinate(5, 4, 0));
		side_exp.add(new Coordinate(4, 4, 0));
		side_exp.add(new Coordinate(4, 5, 0));

		neighbors = hr.getCellNeighbors(coord);
		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(side_exp.contains(neighbor));
		}

		// Bottom -- check south is missing
		coord = hr.wrap(2, 1, 0);

		HashSet<Coordinate> bottom_exp = new HashSet<Coordinate>();
		bottom_exp.add(new Coordinate(1, 0, 0));
		bottom_exp.add(new Coordinate(1, 1, 0));
		bottom_exp.add(new Coordinate(2, 2, 0));
		bottom_exp.add(new Coordinate(3, 2, 0));
		bottom_exp.add(new Coordinate(3, 1, 0));

		neighbors = hr.getCellNeighbors(coord);

		assertEquals(5, neighbors.length);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(bottom_exp.contains(neighbor));
		}

		
	}

	// All cases but top/bottom should be same as getCellNeighbors(...)
	// for the HexTorus geometry.
	// getSoluteNeighbors(...)
	public void testSoluteNeighbors() {
		HexRing hr = new HexRing(6, 6);
		
		// Interior
		Coordinate coord = new Coordinate(3, 4, 0);

		HashSet<Coordinate> interior_exp = new HashSet<Coordinate>();
		interior_exp.add(new Coordinate(3, 5, 0));
		interior_exp.add(new Coordinate(4, 5, 0));
		interior_exp.add(new Coordinate(4, 4, 0));
		interior_exp.add(new Coordinate(3, 3, 0));
		interior_exp.add(new Coordinate(2, 3, 0));
		interior_exp.add(new Coordinate(2, 4, 0));

		Coordinate[] neighbors = hr.getSoluteNeighbors(coord);

		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(interior_exp.contains(neighbor));
		}

		// Side -- check wrapped
		coord = new Coordinate(5, 5, 0);

		HashSet<Coordinate> side_exp = new HashSet<Coordinate>();
		side_exp.add(new Coordinate(5, 6, 0));
		side_exp.add(new Coordinate(0, 3, 0));
		side_exp.add(new Coordinate(0, 2, 0));
		side_exp.add(new Coordinate(5, 4, 0));
		side_exp.add(new Coordinate(4, 4, 0));
		side_exp.add(new Coordinate(4, 5, 0));

		neighbors = hr.getSoluteNeighbors(coord);
		assertEquals(neighbors.length, 6);
		for (int i = 0; i < neighbors.length; i++) {
			Coordinate neighbor = neighbors[i];
			assertTrue(side_exp.contains(neighbor));
		}

		// Bottom -- check south is REFLECTED
		coord = new Coordinate(2, 1, 0);

		HashSet<Coordinate> bottom_exp = new HashSet<Coordinate>();
		bottom_exp.add(new Coordinate(1, 0, 0));
		bottom_exp.add(new Coordinate(1, 1, 0));
		bottom_exp.add(new Coordinate(2, 2, 0));
		bottom_exp.add(new Coordinate(3, 2, 0));
		bottom_exp.add(new Coordinate(3, 1, 0));

		// The cell is counted as a "neighbor" because southerly-moving
		// solute is reflected back.
		bottom_exp.add(new Coordinate(2, 1, 0));

		neighbors = hr.getSoluteNeighbors(coord);
		assertEquals(6, neighbors.length);

		for (int i = 0; i < neighbors.length; i++) {

			Coordinate neighbor = neighbors[i];

			assertTrue(bottom_exp.contains(neighbor));
		}

		
	}

	// getAnnulus(...)
	public void testAnnulus() {
		HexRing hr = new HexRing(4, 4);


		Coordinate coord = new Coordinate(0, 2, 0);

		// Point
		Coordinate[] result = hr.getAnnulus(coord, 0, false);
		assertEquals(1, result.length);
		assertEquals(coord, result[0]);

		result = hr.getAnnulus(coord, 0, true);
		assertEquals(1, result.length);
		assertEquals(coord, result[0]);

		// r=1
		result = hr.getAnnulus(coord, 1, true);
		assertEquals(6, result.length);

		// r=2 (big)--restrict circumnavigation. Note that I have some trouble
		// visualizing the smaller circles, so check this functionally as well.
		result = hr.getAnnulus(coord, 2, true);

		// Top and bottom are truncated, and wrapped cells that circumnavigate
		// are rejected, so 6:
		// (2, 4); (2, 3); (2, 2); (1, 1); (0, 0); (3, 2)
		//
		// Rejected circumanvigators are all double counts of existing cells.

		assertEquals(6, result.length);

		// r=2--don't restrict circumnavigation
		result = hr.getAnnulus(coord, 2, false);

		// We count some of the positions twice, but top/bottom
		// are truncated, so 9:
		//
		// (0, 0); (1, 1); (3, 2) <-- each counted once
		// (2, 2); (2, 3); (2, 4) <-- each counted twice

		assertEquals(9, result.length);



	}

	// Tests for correct behavior in vicinity of origin when dimensions
	// are 6x6, as in the lattice tests.
	public void testOriginWrap() {
		// Explicitly test wrapping behavior in vicinity of origin
		HexRing hr = new HexRing(6, 6);

		// (1, 0) stays (1, 0)
		assertEquals(hr.wrap(1, 0, 0), new Coordinate(1, 0, 0));

		// (1, 1) stays (1, 1)
		assertEquals(hr.wrap(1, 1, 0), new Coordinate(1, 1, 0));

		// (0, 1) stays (0, 1)
		assertEquals(hr.wrap(0, 1, 0), new Coordinate(0, 1, 0));

		// (-1, -1) becomes (5, 2)
		assertEquals(hr.wrap(-1, -1, 0), new Coordinate(5, 2, 0));

		// (-1, 0) becomes (5, 3)
		assertEquals(hr.wrap(-1, 0, 0), new Coordinate(5, 3, 0));

		// (0, -1) is out of bounds
		assertTrue(hr.wrap(0, -1, 0).hasFlag(Flags.BEYOND_BOUNDS));
	}

}
