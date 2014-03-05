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

package layers.cell;

import cells.Cell;
import cells.MockCell;
import com.sun.javafx.geom.transform.Identity;
import junit.framework.TestCase;
import structural.identifiers.Coordinate;

import java.util.IdentityHashMap;

/**
 * Created by David B Borenstein on 2/5/14.
 */
public class CellLocationIndexTest extends TestCase {
    private MockCell o1, o2;
    private Coordinate c1, c2;

    // The object to be tested
    private ExposedCellLocationIndex locationIndex;

    // Handle to the internal state of the tested object
    private IdentityHashMap<Cell, Coordinate> map;

    @Override
    protected void setUp() throws Exception {
        o1 = new MockCell();
        o2 = new MockCell();

        c1 = new Coordinate(0, 0, 0);
        c2 = new Coordinate(0, 1, 0);

        locationIndex = new ExposedCellLocationIndex();
        map = locationIndex.getLocationMap();
    }

    public void testPlace() throws Exception {
        // Index should be empty
        assertEquals(0, map.keySet().size());

        // Place a cell
        locationIndex.place(o1, c1);

        // Cell should index to placed coordinate
        assertEquals(1, map.keySet().size());
        assertEquals(c1, map.get(o1));
    }

    public void testMove() throws Exception {
        locationIndex.place(o1, c1);
        locationIndex.move(o1, c2);
        assertEquals(c2, map.get(o1));
    }

    public void testRemove() throws Exception {
        // Manually add a cell to the map
        map.put(o1, c1);
        assertEquals(1, map.keySet().size());

        // Remove the cell using the index object
        locationIndex.remove(o1);

        // Map should be empty
        assertEquals(0, map.keySet().size());
    }

    public void testLocate() throws Exception {
        locationIndex.place(o1, c1);
        locationIndex.place(o2, c2);
        assertEquals(c1, locationIndex.locate(o1));
        assertEquals(c2, locationIndex.locate(o2));
    }

    public void testIsIndexed() throws Exception {
        locationIndex.place(o1, c1);
        assertTrue(locationIndex.isIndexed(o1));
        assertFalse(locationIndex.isIndexed(o2));
    }

    private class ExposedCellLocationIndex extends CellLocationIndex {
        public IdentityHashMap<Cell, Coordinate> getLocationMap() {
            return locationMap;
        }
    }
}
