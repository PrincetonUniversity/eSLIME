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
import cells.MockCell;
import cells.SimpleCell;
import geometry.MockGeometry;
import layers.cell.*;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

public class CellLayerViewerTest extends EslimeTestCase {

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
        geom.setCanonicalSites(new Coordinate[]{c});
        MockCellLayerContent content = new MockCellLayerContent(geom, null);

        Cell cell = new SimpleCell(1);
        content.put(c, cell);
        CellLayerViewer viewer = new CellLayerViewer(null, content, null);
        assertEquals(cell, viewer.getCell(c));
    }

    public void testGetFitnessVector() {
        MockGeometry geom = new MockGeometry();
        Coordinate c = new Coordinate(1, 0, 0);
        geom.setCanonicalSites(new Coordinate[]{c});
        MockCellLayerContent content = new MockCellLayerContent(geom, null);

        content.setFitnessVector(new double[]{0.1, 0.2, 0.3});
        CellLayerViewer viewer = new CellLayerViewer(null, content, null);
        assertEquals(viewer.getFitnessVector().length, 3);
        assertEquals(viewer.getFitnessVector()[0], 0.1, epsilon);
    }

    public void testGetStateVector() {
        MockGeometry geom = new MockGeometry();
        Coordinate c = new Coordinate(1, 0, 0);
        geom.setCanonicalSites(new Coordinate[]{c});
        MockCellLayerContent content = new MockCellLayerContent(geom, null);

        content.setStateVector(new int[]{1, 2, 3});
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

    public void testExists() {
        MockCellLayerIndices indices = new MockCellLayerIndices();
        CellLocationIndex locationIndex = indices.getCellLocationIndex();
        Cell cell = new MockCell();
        assertFalse(locationIndex.isIndexed(cell));
        Coordinate c = new Coordinate(0, 0, 0);
        locationIndex.place(cell, c);
        assertTrue(locationIndex.isIndexed(cell));
    }
}
