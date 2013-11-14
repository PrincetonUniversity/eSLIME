package jeslime.layers;

import java.util.HashMap;
import java.util.HashSet;

import cells.Cell;
import cells.SimpleCell;
import jeslime.mock.MockCellLayerContent;
import jeslime.mock.MockCellLayerIndices;
import jeslime.mock.MockGeometry;
import junit.framework.TestCase;
import layers.cell.CellIndex;
import layers.cell.CellLayerViewer;
import structural.identifiers.Coordinate;

public class CellLayerViewerTest extends TestCase {

	public void testGetOccupiedSites() {
		MockCellLayerIndices indices = new MockCellLayerIndices();
		CellIndex expected = new CellIndex();
		
		Coordinate c1 = new Coordinate(1, 0, 0);
		Coordinate c2 = new Coordinate(5, 0, 0);

		expected.add(c1);
		expected.add(c2);
		
		indices.setOccupiedSites(expected);
		
		CellLayerViewer viewer = new CellLayerViewer(null, null, indices);
		assertEquals(viewer.getOccupiedSites().size(), expected.size());
		assertTrue(viewer.getOccupiedSites().contains(c1));
		assertTrue(viewer.getOccupiedSites().contains(c2));
	}
	
	public void testGetDivisibleSites() {
		MockCellLayerIndices indices = new MockCellLayerIndices();
		CellIndex expected = new CellIndex();
		
		Coordinate c1 = new Coordinate(1, 0, 0);
		Coordinate c2 = new Coordinate(5, 0, 0);

		expected.add(c1);
		expected.add(c2);
		
		indices.setDivisibleSites(expected);
		
		CellLayerViewer viewer = new CellLayerViewer(null, null, indices);
		assertEquals(viewer.getDivisibleSites().size(), expected.size());
		assertTrue(viewer.getDivisibleSites().contains(c1));
		assertTrue(viewer.getDivisibleSites().contains(c2));
	}
	
	public void testGetCell() {
		MockGeometry geom = new MockGeometry();
		Coordinate c = new Coordinate(1, 0, 0);
		geom.setCanonicalSites(new Coordinate[] {c});
		MockCellLayerContent content = new MockCellLayerContent(geom, null);
		
		Cell cell = new SimpleCell(1);
		content.put(c, cell);
		CellLayerViewer viewer = new CellLayerViewer(null, content, null);
		assertEquals(cell, viewer.getCell(c));
	}
	
	public void testGetFitnessVector() {
		MockGeometry geom = new MockGeometry();
		Coordinate c = new Coordinate(1, 0, 0);
		geom.setCanonicalSites(new Coordinate[] {c});
		MockCellLayerContent content = new MockCellLayerContent(geom, null);
		
		content.setFitnessVector(new double[] {0.1, 0.2, 0.3});
		CellLayerViewer viewer = new CellLayerViewer(null, content, null);
		assertEquals(viewer.getFitnessVector().length, 3);
		assertEquals(viewer.getFitnessVector()[0], 0.1, 1e-10);
	}
	
	public void testGetStateVector() {
		MockGeometry geom = new MockGeometry();
		Coordinate c = new Coordinate(1, 0, 0);
		geom.setCanonicalSites(new Coordinate[] {c});
		MockCellLayerContent content = new MockCellLayerContent(geom, null);
		
		content.setStateVector(new int[] {1, 2, 3});
		CellLayerViewer viewer = new CellLayerViewer(null, content, null);
		assertEquals(viewer.getStateVector().length, 3);
		assertEquals(viewer.getStateVector()[0], 1);
	}
	
	// StateMapViewer gets its own tests, so omit here
	//public void testGetStateMapViewer() {
	//	fail();
	//}
	
	public void testIsOccupied() {
		MockCellLayerIndices indices = new MockCellLayerIndices();
		CellIndex expected = new CellIndex();
		Coordinate c1 = new Coordinate(1, 0, 0);
		Coordinate c2 = new Coordinate(5, 0, 0);
		
		expected.add(c1);
		
		indices.setOccupiedSites(expected);
		CellLayerViewer viewer = new CellLayerViewer(null, null, indices);
		assertTrue(viewer.isOccupied(c1));
		assertFalse(viewer.isOccupied(c2));
	}
	
	public void testIsDivisible() {
		MockCellLayerIndices indices = new MockCellLayerIndices();
		CellIndex expected = new CellIndex();
		Coordinate c1 = new Coordinate(1, 0, 0);
		Coordinate c2 = new Coordinate(5, 0, 0);
		
		expected.add(c1);
		
		indices.setDivisibleSites(expected);
		CellLayerViewer viewer = new CellLayerViewer(null, null, indices);
		assertTrue(viewer.isDivisible(c1));
		assertFalse(viewer.isDivisible(c2));
	}
}
