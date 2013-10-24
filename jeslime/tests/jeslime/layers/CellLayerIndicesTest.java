package jeslime.layers;

import cells.Cell;
import cells.SimpleCell;
import structural.identifiers.Coordinate;
import junit.framework.TestCase;
import layers.cell.CellLayerIndices;

public class CellLayerIndicesTest extends TestCase {

	public void testIsOccupied() {
		CellLayerIndices indices = new CellLayerIndices();
		
		Coordinate coord = new Coordinate(0, 0, 0);
		assertFalse(indices.isOccupied(coord));
		
		indices.getOccupiedSites().add(coord);
		assertTrue(indices.isOccupied(coord));
		
		indices.getOccupiedSites().remove(coord);
		assertFalse(indices.isOccupied(coord));
	}
	
	public void testIsDivisible() {
		CellLayerIndices indices = new CellLayerIndices();
		
		Coordinate coord = new Coordinate(0, 0, 0);
		assertFalse(indices.isDivisible(coord));
		
		indices.getDivisibleSites().add(coord);
		assertTrue(indices.isDivisible(coord));	
		
		indices.getDivisibleSites().remove(coord);
		assertFalse(indices.isDivisible(coord));	
	}
	
	public void testGetOccupiedSites() {
		CellLayerIndices indices = new CellLayerIndices();
		assertEquals(indices.getOccupiedSites().size(), 0);
		
		Coordinate coord = new Coordinate(0, 0, 0);
		indices.getOccupiedSites().add(coord);
		
		assertEquals(indices.getOccupiedSites().size(), 1);
	}
	
	public void testGetDivisibleSites() {
		CellLayerIndices indices = new CellLayerIndices();
		assertEquals(indices.getDivisibleSites().size(), 0);
		
		Coordinate coord = new Coordinate(0, 0, 0);
		indices.getDivisibleSites().add(coord);
		
		assertEquals(indices.getDivisibleSites().size(), 1);
	}
	
	public void testStateCount() {
		CellLayerIndices indices = new CellLayerIndices();

		indices.getStateMap().put(1, 5);
		assertEquals((int) indices.getStateMap().get(1), 5);
		
		Cell c = new SimpleCell(1);
		
		indices.incrStateCount(c);
		assertEquals((int) indices.getStateMap().get(1), 6);

		indices.decrStateCount(c);
		assertEquals((int) indices.getStateMap().get(1), 5);
	}
	
	public void testStateMap() {
		CellLayerIndices indices = new CellLayerIndices();
		
		assertEquals(indices.getStateMap().size(), 0);
		
		indices.getStateMap().put(0, 1);
		indices.getStateMap().put(1, 3);
		assertEquals(indices.getStateMap().size(), 2);
	}
}
