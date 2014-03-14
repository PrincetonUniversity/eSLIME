/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package layers;

import cells.Cell;
import cells.SimpleCell;
import geometry.MockGeometry;
import layers.cell.CellUpdateManager;
import layers.cell.MockCellLayerContent;
import layers.cell.MockCellLayerIndices;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

public class CellUpdateManagerTest extends EslimeTestCase {
    public void testConsiderApply() {
        // Set up mocks
        MockGeometry geom = new MockGeometry();
        Coordinate o = new Coordinate(0, 0, 0);
        geom.setCanonicalSites(new Coordinate[]{o});
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

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();

        MockCellLayerContent content = new MockCellLayerContent(geom, indices);
        Cell cell = new SimpleCell(1);
        CellUpdateManager manager = new CellUpdateManager(content, indices);
        manager.place(cell, o);
        assertTrue(indices.isOccupied(o));
        assertTrue(indices.isDivisible(o));
        assertFalse(indices.isOccupied(t));
        assertFalse(indices.isDivisible(t));
        assertEquals(o, indices.getCellLocationIndex().locate(cell));
        manager.divideTo(o, t);

        assertTrue(indices.isOccupied(t));
        assertTrue(indices.isDivisible(t));
        assertEquals(content.get(t).getState(), 1);

        Cell child = content.get(t);
        assertEquals(t, indices.getCellLocationIndex().locate(child));
    }

    public void testDivide() {
        // Set up mocks
        MockGeometry geom = new MockGeometry();
        Coordinate o = new Coordinate(0, 0, 0);
        Coordinate t = new Coordinate(1, 0, 0);

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();
//		indices.setOccupied(o, true);
//		indices.setDivisible(o, true);

        MockCellLayerContent content = new MockCellLayerContent(geom, indices);
        Cell cell = new SimpleCell(1);
        CellUpdateManager manager = new CellUpdateManager(content, indices);
        manager.place(cell, o);

        Cell daughter = manager.divide(o);

        assertEquals(daughter.getState(), cell.getState());
        assertEquals(daughter.getFitness(), cell.getFitness(), epsilon);

        // Child is not yet placed and should therefore not be indexed
        assertFalse(indices.getCellLocationIndex().isIndexed(daughter));
    }


    public void testBanish() {
        // Set up mocks
        MockGeometry geom = new MockGeometry();
        Coordinate o = new Coordinate(0, 0, 0);
        Coordinate t = new Coordinate(1, 0, 0);

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();
//		indices.setOccupied(o, true);
//		indices.setDivisible(o, true);

        MockCellLayerContent content = new MockCellLayerContent(geom, indices);
        Cell cell = new SimpleCell(1);

        CellUpdateManager manager = new CellUpdateManager(content, indices);
        manager.place(cell, o);

        assertTrue(indices.isOccupied(o));
        assertTrue(indices.isDivisible(o));

        assertTrue(indices.getCellLocationIndex().isIndexed(cell));

        manager.banish(o);

        assertTrue(content.get(o) == null);
        assertFalse(indices.isOccupied(o));
        assertFalse(indices.isDivisible(o));

        assertFalse(indices.getCellLocationIndex().isIndexed(cell));
    }

    public void testMove() {
        // Set up mocks
        MockGeometry geom = new MockGeometry();
        Coordinate o = new Coordinate(0, 0, 0);
        Coordinate t = new Coordinate(1, 0, 0);

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();
//		indices.setOccupied(o, true);
//		indices.setDivisible(o, true);
        Cell cell = new SimpleCell(1);
        //indices.getCellLocationIndex().place(cell, o);
        MockCellLayerContent content = new MockCellLayerContent(geom, indices);

        CellUpdateManager manager = new CellUpdateManager(content, indices);
        manager.place(cell, o);

        assertTrue(indices.isOccupied(o));
        assertTrue(indices.isDivisible(o));
        assertFalse(indices.isOccupied(t));
        assertFalse(indices.isDivisible(t));
        assertEquals(o, indices.getCellLocationIndex().locate(cell));

        manager.move(o, t);

        assertEquals(t, indices.getCellLocationIndex().locate(cell));
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

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();
        Cell cell1 = new SimpleCell(1);
        Cell cell2 = new SimpleCell(2);
//	    indices.getCellLocationIndex().place(cell1, o);
//        indices.getCellLocationIndex().place(cell2, t);
        MockCellLayerContent content = new MockCellLayerContent(geom, indices);

        CellUpdateManager manager = new CellUpdateManager(content, indices);
        manager.place(cell1, o);
        manager.place(cell2, t);

        assertEquals(o, indices.getCellLocationIndex().locate(cell1));
        assertEquals(t, indices.getCellLocationIndex().locate(cell2));

        assertEquals(content.get(o).getState(), 1);
        assertEquals(content.get(t).getState(), 2);

        manager.swap(o, t);

        assertEquals(o, indices.getCellLocationIndex().locate(cell2));
        assertEquals(t, indices.getCellLocationIndex().locate(cell1));

        assertEquals(content.get(o).getState(), 2);
        assertEquals(content.get(t).getState(), 1);

    }

    public void testPlace() {
        // Set up mocks
        MockGeometry geom = new MockGeometry();
        Coordinate o = new Coordinate(0, 0, 0);
        Coordinate t = new Coordinate(1, 0, 0);

        geom.setCanonicalSites(new Coordinate[]{o, t});
        MockCellLayerIndices indices = new MockCellLayerIndices();

        MockCellLayerContent content = new MockCellLayerContent(geom, indices);
        Cell cell = new SimpleCell(1);

        CellUpdateManager manager = new CellUpdateManager(content, indices);

        assertFalse(indices.getCellLocationIndex().isIndexed(cell));
        assertTrue(content.get(o) == null);

        manager.place(cell, o);

        assertTrue(indices.getCellLocationIndex().isIndexed(cell));

        assertFalse(content.get(o) == null);
    }

}
