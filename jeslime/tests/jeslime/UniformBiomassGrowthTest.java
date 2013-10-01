package jeslime;

import io.project.ProcessLoader;
import io.project.ProjectLoader;
import processes.cellular.CellProcess;
import processes.cellular.UniformBiomassGrowth;
import cells.Cell;
import cells.FissionCell;
import geometries.Geometry;
import geometries.HexRing;
import structural.Flags;
import structural.Lattice;
import structural.identifiers.Coordinate;
import junit.framework.TestCase;

public class UniformBiomassGrowthTest extends TestCase {
	
	private double epsilon = calcEpsilon();

	public void testUndeferred() {
		// Set up a dummy simulation
		Geometry geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);
		
		// Create two cells that can be fed
		Cell one = new FissionCell(1, 0.7, 2.0);
		Cell two = new FissionCell(2, 0.3, 2.0);
		
		Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
		Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);
		
		lattice.place(one, coord1);
		lattice.place(two, coord2);
		
		// Create a UniformBiomassGrowth
		CellProcess process = new UniformBiomassGrowth(lattice, geom, 1.5, false);

		// Verify that they get fed when the process is invoked
		assertEquals(lattice.getFitness(coord1), 0.7, epsilon);
		assertEquals(lattice.getFitness(coord2), 0.3, epsilon);

		assertTrue(!lattice.getDivisibleSites().contains(coord1));
		assertTrue(!lattice.getDivisibleSites().contains(coord2));
		
		try {
			process.iterate(null);
		} catch (Exception ex) {
			fail();
		}
		
		// Verify that they get fed when the process is invoked
		assertEquals(lattice.getFitness(coord1), 2.2, epsilon);
		assertEquals(lattice.getFitness(coord2), 1.8, epsilon);
		
		assertTrue(lattice.getDivisibleSites().contains(coord1));
		assertTrue(!lattice.getDivisibleSites().contains(coord2));
	}
	
	public void testDeferred() {
		// Set up a dummy simulation
		Geometry geom = new HexRing(6, 6);
		Lattice lattice = new Lattice(geom);
		
		// Create two cells that can be fed
		Cell one = new FissionCell(1, 0.7, 2.0);
		Cell two = new FissionCell(2, 0.3, 2.0);
		
		Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
		Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);
		
		lattice.place(one, coord1);
		lattice.place(two, coord2);
		
		// Create a UniformBiomassGrowth
		CellProcess process = new UniformBiomassGrowth(lattice, geom, 1.5, true);

		// Verify that they get fed when the process is invoked
		assertEquals(lattice.getFitness(coord1), 0.7, epsilon);
		assertEquals(lattice.getFitness(coord2), 0.3, epsilon);

		assertTrue(!lattice.getDivisibleSites().contains(coord1));
		assertTrue(!lattice.getDivisibleSites().contains(coord2));
		
		try {
			process.iterate(null);
		} catch (Exception ex) {
			fail();
		}
		
		assertEquals(lattice.getFitness(coord1), 0.7, epsilon);
		assertEquals(lattice.getFitness(coord2), 0.3, epsilon);

		assertTrue(!lattice.getDivisibleSites().contains(coord1));
		assertTrue(!lattice.getDivisibleSites().contains(coord2));
		
		lattice.apply(coord1);
		lattice.apply(coord2);
		
		// Verify that they get fed when the process is invoked
		assertEquals(lattice.getFitness(coord1), 2.2, epsilon);
		assertEquals(lattice.getFitness(coord2), 1.8, epsilon);
		
		assertTrue(lattice.getDivisibleSites().contains(coord1));
		assertTrue(!lattice.getDivisibleSites().contains(coord2));
	}
	
	private double calcEpsilon() {
        double machEps = 1.0d;
        
        do {
           machEps /= 2d;
        } while (1d + (machEps/2d) != 1d);
        
        return machEps;
	}
}
