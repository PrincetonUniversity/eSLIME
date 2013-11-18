package jeslime.layers;

import cells.Cell;
import cells.SimpleCell;
import structural.identifiers.Coordinate;
import jeslime.EslimeTestCase;
import jeslime.mock.MockCellLayerContent;
import jeslime.mock.MockCellLayerIndices;
import jeslime.mock.MockGeometry;
import junit.framework.TestCase;
import layers.cell.CellUpdateManager;

public class CellUpdateManagerTest extends EslimeTestCase {
	public void testConsiderApply() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		geom.setCanonicalSites(new Coordinate[] {o});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);
		content.put(o, cell);
		CellUpdateManager manager = new CellUpdateManager(content, indices);
		
		assertEquals(manager.consider(o), 1);
		assertEquals(manager.consider(o), 2);
		manager.apply(o);
		assertEquals(manager.consider(o), 1);

	}
	
	public void testDivideTo() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		indices.setOccupied(o, true);
		indices.setDivisible(o, true);
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);
		content.put(o, cell);
		CellUpdateManager manager = new CellUpdateManager(content, indices);

		assertTrue(indices.isOccupied(o));
		assertTrue(indices.isDivisible(o));
		assertFalse(indices.isOccupied(t));
		assertFalse(indices.isDivisible(t));
		
		manager.divideTo(o, t);
		
		assertTrue(indices.isOccupied(t));
		assertTrue(indices.isDivisible(t));
		assertEquals(content.get(t).getState(), 1);
	}
	
	public void testDivide() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		indices.setOccupied(o, true);
		indices.setDivisible(o, true);
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);
		content.put(o, cell);
		CellUpdateManager manager = new CellUpdateManager(content, indices);

		Cell daughter = manager.divide(o);
		
		assertEquals(daughter.getState(), cell.getState());
		assertEquals(daughter.getFitness(), cell.getFitness(), epsilon);

	}

	
	public void testBanish() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		indices.setOccupied(o, true);
		indices.setDivisible(o, true);
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);

		content.put(o, cell);

		CellUpdateManager manager = new CellUpdateManager(content, indices);
		
		assertTrue(indices.isOccupied(o));
		assertTrue(indices.isDivisible(o));
		
		manager.banish(o);
		
		assertTrue(content.get(o) == null);
		assertFalse(indices.isOccupied(o));
		assertFalse(indices.isDivisible(o));
	}
	
	public void testMove() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		indices.setOccupied(o, true);
		indices.setDivisible(o, true);
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);

		content.put(o, cell);

		CellUpdateManager manager = new CellUpdateManager(content, indices);
		
		assertTrue(indices.isOccupied(o));
		assertTrue(indices.isDivisible(o));
		assertFalse(indices.isOccupied(t));
		assertFalse(indices.isDivisible(t));
		
		manager.move(o, t);
		
		assertFalse(indices.isOccupied(o));
		assertFalse(indices.isDivisible(o));		
		assertTrue(indices.isOccupied(t));
		assertTrue(indices.isDivisible(t));
	}
	
	public void testSwap() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		indices.setOccupied(o, true);
		indices.setDivisible(o, true);
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell1 = new SimpleCell(1);
		Cell cell2 = new SimpleCell(2);

		content.put(o, cell1);
		content.put(t, cell2);
		
		CellUpdateManager manager = new CellUpdateManager(content, indices);
		
		indices.setOccupied(o, true);
		indices.setOccupied(t, true);
		
		assertEquals(content.get(o).getState(), 1);
		assertEquals(content.get(t).getState(), 2);

		manager.swap(o, t);

		assertEquals(content.get(o).getState(), 2);
		assertEquals(content.get(t).getState(), 1);
		
	}
	
	public void testPlace() {
		// Set up mocks
		MockGeometry geom = new MockGeometry();
		Coordinate o = new Coordinate(0, 0, 0);
		Coordinate t = new Coordinate(1, 0, 0);
		
		geom.setCanonicalSites(new Coordinate[] {o, t});
		MockCellLayerIndices indices = new MockCellLayerIndices();
		
		MockCellLayerContent content = new MockCellLayerContent(geom, indices);
		Cell cell = new SimpleCell(1);
		
		CellUpdateManager manager = new CellUpdateManager(content, indices);

		assertTrue(content.get(o) == null);
		
		manager.place(cell, o);
		
		assertFalse(content.get(o) == null);
	}

	// We'll trust the f_swap test to the integration tests
	//public void testFSwap() {
	//	fail();
	//}

	
}
