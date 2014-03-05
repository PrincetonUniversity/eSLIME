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

package layers;

import cells.Cell;
import cells.FissionCell;
import cells.SimpleCell;
import test.EslimeTestCase;
import layers.cell.MockCellLayerIndices;
import geometry.MockGeometry;
import structural.Flags;
import structural.identifiers.Coordinate;
import layers.cell.CellLayerContent;

public class CellLayerContentTest extends EslimeTestCase {

	public void testCheckExists() {
		// Initialize data structures
		Coordinate inside = new Coordinate(0, 0, 0);
		Coordinate outside = new Coordinate(-1, -1, Flags.END_OF_WORLD);
		
		MockGeometry geom = new MockGeometry();
		geom.setCanonicalSites(new Coordinate[] {inside});
		CellLayerContent content = new CellLayerContent(geom, null);

		// Set mock geometry to have "infinite" flag and test
		geom.setInfinite(true);
		assertEquals(inside, content.checkExists(inside));
		assertEquals(outside, content.checkExists(outside));
		
		// Now set infinite to false and check 
		geom.setInfinite(false);
		assertEquals(inside, content.checkExists(inside));
		
		boolean thrown = false;
		try {
			content.checkExists(outside);
		} catch (IllegalStateException ex) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	public void testPutGet() {
		// Initialize data structures
		Coordinate inside = new Coordinate(0, 0, 0);
		
		MockGeometry geom = new MockGeometry();
		geom.setCanonicalSites(new Coordinate[] {inside});
		
		MockCellLayerIndices indices = new MockCellLayerIndices();
		CellLayerContent content = new CellLayerContent(geom, indices);
		
		Cell cell = new SimpleCell(1);
		
		// "put" a cell in
		indices.setOccupied(inside, false);
		content.put(inside, cell);
		
		// set mock index to occupied
		indices.setOccupied(inside, true);

		// "get" cell
		assertEquals(cell, content.get(inside));
	}
	
	public void testGetCanonicalSites() {
		Coordinate[] c = new Coordinate[3];
		
		c[0] = new Coordinate(0, 0, 0);
		c[1] = new Coordinate(1, 0, 0);
		c[2] = new Coordinate(2, 0, 0);

		MockGeometry geom = new MockGeometry();
		geom.setCanonicalSites(c);
		
		CellLayerContent content = new CellLayerContent(geom, null);
		assertEquals(content.getCanonicalSites()[0], c[0]);
		assertEquals(content.getCanonicalSites()[1], c[1]);
		assertEquals(content.getCanonicalSites()[2], c[2]);
	}
	
	public void testGetStateVector() {
		// Mock canonical sites to 3 locations
		Coordinate[] c = new Coordinate[3];
		
		c[0] = new Coordinate(0, 0, 0);
		c[1] = new Coordinate(1, 0, 0);
		c[2] = new Coordinate(2, 0, 0);

		MockGeometry geom = new MockGeometry();
		geom.setCanonicalSites(c);
		
		// Mock canonical sites to 3 locations
		FissionCell f0 = new FissionCell(1, 0.5, 1.0);
		FissionCell f1 = new FissionCell(1, 0.5, 1.0);
		FissionCell f2 = new FissionCell(2, 0.7, 1.0);

		// Add a cell to each canonical site
		CellLayerContent content = new CellLayerContent(geom, null);
		content.put(c[0], f0);
		content.put(c[1], f1);
		content.put(c[2], f2);

		// Fitness vector goes in order of canonical sites array
		assertEquals(content.getStateVector()[0], 1);
		assertEquals(content.getStateVector()[1], 1);
		assertEquals(content.getStateVector()[2], 2);
	}
	
	public void testGetFitnessVector() {
		// Mock canonical sites to 3 locations
		Coordinate[] c = new Coordinate[3];
		
		c[0] = new Coordinate(0, 0, 0);
		c[1] = new Coordinate(1, 0, 0);
		c[2] = new Coordinate(2, 0, 0);

		MockGeometry geom = new MockGeometry();
		geom.setCanonicalSites(c);
		
		// Mock canonical sites to 3 locations
		FissionCell f0 = new FissionCell(1, 0.5, 1.0);
		FissionCell f1 = new FissionCell(1, 0.5, 1.0);
		FissionCell f2 = new FissionCell(2, 0.7, 1.0);

		// Add a cell to each canonical site
		CellLayerContent content = new CellLayerContent(geom, null);
		content.put(c[0], f0);
		content.put(c[1], f1);
		content.put(c[2], f2);

		// Fitness vector goes in order of canonical sites array
		assertEquals(content.getFitnessVector()[0], 0.5, epsilon);
		assertEquals(content.getFitnessVector()[1], 0.5, epsilon);
		assertEquals(content.getFitnessVector()[2], 0.7, epsilon);

	}
}
