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
import junit.framework.TestCase;
import layers.cell.CellLayerIndices;
import structural.identifiers.Coordinate;

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
