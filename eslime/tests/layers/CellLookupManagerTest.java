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
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.cell.CellLookupManager;
import layers.cell.MockCellLayerContent;

public class CellLookupManagerTest extends TestCase {

    public void testGetNeighborStates() {
        MockGeometry geometry = new MockGeometry();

        Coordinate[] c = new Coordinate[3];
        c[0] = new Coordinate(0, 0, 0);
        c[1] = new Coordinate(1, 0, 0);
        c[2] = new Coordinate(2, 0, 0);
        geometry.setCanonicalSites(c);

        MockCellLayerContent content = new MockCellLayerContent(geometry, null);

        Coordinate[] neighborhood = new Coordinate[]{c[0], c[2]};
        geometry.setCellNeighbors(c[1], neighborhood);

        Cell f0 = new SimpleCell(4);
        Cell f2 = new SimpleCell(6);

        content.put(c[0], f0);
        content.put(c[2], f2);

        CellLookupManager lookup = new CellLookupManager(geometry, content, null);
        assertEquals(lookup.getNeighborStates(c[1]).length, 2);
        assertEquals(lookup.getNeighborStates(c[1])[0], 4);
        assertEquals(lookup.getNeighborStates(c[1])[1], 6);
    }
}
