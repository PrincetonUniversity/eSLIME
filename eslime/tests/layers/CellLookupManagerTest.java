/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.cell.CellLookupManager;
import layers.cell.MockCellLayerContent;
import layers.cell.MockCellLayerIndices;

public class CellLookupManagerTest extends TestCase {

    public void testGetNeighborStates() {
        MockGeometry geometry = new MockGeometry();
        MockCellLayerIndices indices = new MockCellLayerIndices();
        Coordinate[] c = new Coordinate[3];
        c[0] = new Coordinate(0, 0, 0);
        c[1] = new Coordinate(1, 0, 0);
        c[2] = new Coordinate(2, 0, 0);
        geometry.setCanonicalSites(c);

        MockCellLayerContent content = new MockCellLayerContent(geometry, indices);

        Coordinate[] neighborhood = new Coordinate[]{c[0], c[2]};
        geometry.setCellNeighbors(c[1], neighborhood);

        Cell f0 = new MockCell(4);
        Cell f2 = new MockCell(6);

        content.put(c[0], f0);
        content.put(c[2], f2);

        CellLookupManager lookup = new CellLookupManager(geometry, content);
        assertEquals(lookup.getNeighborStates(c[1], true).length, 2);
        assertEquals(lookup.getNeighborStates(c[1], true)[0], 4);
        assertEquals(lookup.getNeighborStates(c[1], true)[1], 6);
    }
}
