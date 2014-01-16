package processes;

import layers.MockLayerManager;
import processes.discrete.CellProcess;
import processes.discrete.UniformBiomassGrowth;
import cells.Cell;
import cells.FissionCell;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingHard;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import structural.Flags;
import layers.cell.CellLayer; import structural.identifiers.Coordinate;
import junit.framework.TestCase;

public class UniformBiomassGrowthTest extends TestCase {
	
	private double epsilon = calcEpsilon();

	public void testUndeferred() {
		// Set up a dummy simulation
		//Geometry geom = new HexRing(6, 6);
		Lattice lattice = new TriangularLattice();
		Shape shape = new Rectangle(lattice, 6, 6);
		Boundary boundary = new PlaneRingHard(shape, lattice);
		Geometry geom = new Geometry(lattice, shape, boundary);
		CellLayer layer = new CellLayer(geom, 0);
	    MockLayerManager layerManager = new MockLayerManager();

		// Create two cells that can be fed
		Cell one = new FissionCell(1, 0.7, 2.0);
		Cell two = new FissionCell(2, 0.3, 2.0);
		
		Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
		Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);
		
		layer.getUpdateManager().place(one, coord1);
		layer.getUpdateManager().place(two, coord2);

        layerManager.setCellLayer(layer);

		// Create a UniformBiomassGrowth
		CellProcess process = new UniformBiomassGrowth(layerManager, 1.5, false);

		// Verify that they get fed when the process is invoked
		assertEquals(layer.getViewer().getCell(coord1).getFitness(), 0.7, epsilon);
		assertEquals(layer.getViewer().getCell(coord2).getFitness(), 0.3, epsilon);

		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
		
		try {
			process.iterate(null);
		} catch (Exception ex) {
			fail();
		}
		
		// Verify that they get fed when the process is invoked
		assertEquals(layer.getViewer().getCell(coord1).getFitness(), 2.2, epsilon);
		assertEquals(layer.getViewer().getCell(coord2).getFitness(), 1.8, epsilon);
		
		assertTrue(layer.getViewer().getDivisibleSites().contains(coord1));
		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
	}
	
	public void testDeferred() {
		// Set up a dummy simulation
		//Geometry geom = new HexRing(6, 6);
		Lattice lattice = new TriangularLattice();
		Shape shape = new Rectangle(lattice, 6, 6);
		Boundary boundary = new PlaneRingHard(shape, lattice);
		Geometry geom = new Geometry(lattice, shape, boundary);
		CellLayer layer = new CellLayer(geom, 0);
	    MockLayerManager layerManager = new MockLayerManager();

		// Create two cells that can be fed
		Cell one = new FissionCell(1, 0.7, 2.0);
		Cell two = new FissionCell(2, 0.3, 2.0);
		
		Coordinate coord1 = new Coordinate(0, 0, Flags.PLANAR);
		Coordinate coord2 = new Coordinate(1, 1, Flags.PLANAR);
		
		layer.getUpdateManager().place(one, coord1);
		layer.getUpdateManager().place(two, coord2);

        layerManager.setCellLayer(layer);

		// Create a UniformBiomassGrowth
		CellProcess process = new UniformBiomassGrowth(layerManager, 1.5, true);

		// Verify that they get fed when the process is invoked
		assertEquals(layer.getViewer().getCell(coord1).getFitness(), 0.7, epsilon);
		assertEquals(layer.getViewer().getCell(coord2).getFitness(), 0.3, epsilon);

		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
		
		try {
			process.iterate(null);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		assertEquals(layer.getViewer().getCell(coord1).getFitness(), 0.7, epsilon);
		assertEquals(layer.getViewer().getCell(coord2).getFitness(), 0.3, epsilon);

		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord1));
		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
		
		layer.getUpdateManager().apply(coord1);
		layer.getUpdateManager().apply(coord2);
		
		// Verify that they get fed when the process is invoked
		assertEquals(layer.getViewer().getCell(coord1).getFitness(), 2.2, epsilon);
		assertEquals(layer.getViewer().getCell(coord2).getFitness(), 1.8, epsilon);
		
		assertTrue(layer.getViewer().getDivisibleSites().contains(coord1));
		assertTrue(!layer.getViewer().getDivisibleSites().contains(coord2));
	}
	
	private double calcEpsilon() {
        double machEps = 1.0d;
        
        do {
           machEps /= 2d;
        } while (1d + (machEps/2d) != 1d);
        
        return machEps;
	}
}
