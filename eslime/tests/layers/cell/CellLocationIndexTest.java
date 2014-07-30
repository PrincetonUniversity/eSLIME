/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import junit.framework.TestCase;

import java.util.IdentityHashMap;

/**
 * Created by David B Borenstein on 2/5/14.
 */
public class CellLocationIndexTest extends TestCase {
    private MockCell o1, o2;
    private Coordinate c1, c2;

    // The object to be tested
    private CellLocationIndex locationIndex;

    @Override
    protected void setUp() throws Exception {
        o1 = new MockCell();
        o2 = new MockCell();

        c1 = new Coordinate(0, 0, 0);
        c2 = new Coordinate(0, 1, 0);

        locationIndex = new CellLocationIndex();
    }

    public void testPlace() throws Exception {
        // Index should be empty
        assertEquals(0, locationIndex.keySet().size());

        // Place a cell
        locationIndex.add(o1, c1);

        // Cell should index to placed coordinate
        assertEquals(1, locationIndex.keySet().size());
        assertEquals(c1, locationIndex.get(o1));
    }

//    public void testMove() throws Exception {
//        locationIndex.add(o1, c1);
//        locationIndex.move(o1, c2);
//        assertEquals(c2, locationIndex.get(o1));
//    }

    public void testRemove() throws Exception {
        // Manually add a cell to the map
        locationIndex.put(o1, c1);
        assertEquals(1, locationIndex.keySet().size());

        // Remove the cell using the index object
        locationIndex.remove(o1);

        // Map should be empty
        assertEquals(0, locationIndex.keySet().size());
    }

    public void testLocate() throws Exception {
        locationIndex.add(o1, c1);
        locationIndex.add(o2, c2);
        assertEquals(c1, locationIndex.locate(o1));
        assertEquals(c2, locationIndex.locate(o2));
    }

    public void testIsIndexed() throws Exception {
        locationIndex.add(o1, c1);
        assertTrue(locationIndex.isIndexed(o1));
        assertFalse(locationIndex.isIndexed(o2));
    }

}
