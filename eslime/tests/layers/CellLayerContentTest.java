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

import cells.MockCell;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.MockGeometry;
import layers.cell.CellLayerContent;
import layers.cell.MockCellLayerIndices;
import test.EslimeTestCase;

public abstract class CellLayerContentTest extends EslimeTestCase {
    protected Coordinate[] c;
    protected MockGeometry geom;
    protected CellLayerContent query;
    protected MockCell f0, f1, f2;
    protected MockCellLayerIndices indices;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        c = new Coordinate[3];

        c[0] = new Coordinate(0, 0, 0);
        c[1] = new Coordinate(1, 0, 0);
        c[2] = new Coordinate(2, 0, 0);

        geom = new MockGeometry();
        geom.setCanonicalSites(c);

        indices = new MockCellLayerIndices();

        query = makeQuery();

        f0 = makeMockCell(1, 0.5);
        f1 = makeMockCell(1, 0.5);
        f2 = makeMockCell(2, 0.7);
    }

    private MockCell makeMockCell(int state, double health) {
        MockCell cell = new MockCell();
        cell.setState(state);
        cell.setHealth(health);

        return cell;
    }


    // Actually, create a full mock for dependencies and test out
    // that all appropriate requests are fired--nothing more
    public void testPutHasGetRemove() {

        // Test before and after state
        assertFalse(query.has(c[0]));
        query.put(c[0], f0);
        assertTrue(query.has(c[0]));
        assertEquals(f0, query.get(c[0]));

        // Check that indices were triggered correctly.
        assertEquals(c[0], indices.getLastCoord());
        assertNull(indices.getLastPrevious());
        assertEquals(f0, indices.getLastCurrent());

        query.remove(c[0]);

        assertFalse(query.has(c[0]));
        assertEquals(c[0], indices.getLastCoord());
        assertEquals(f0, indices.getLastPrevious());
        assertNull(indices.getLastCurrent());
    }

    public void testGetCanonicalSites() {
        assertEquals(query.getCanonicalSites()[0], c[0]);
        assertEquals(query.getCanonicalSites()[1], c[1]);
        assertEquals(query.getCanonicalSites()[2], c[2]);
    }

    public void testGetStateVector() {
        query.put(c[0], f0);
        query.put(c[1], f1);
        query.put(c[2], f2);

        // Health vector goes in order of canonical sites array
        assertEquals(query.getStateVector()[0], 1);
        assertEquals(query.getStateVector()[1], 1);
        assertEquals(query.getStateVector()[2], 2);
    }

    public void testGetHealthVector() {
        query.put(c[0], f0);
        query.put(c[1], f1);
        query.put(c[2], f2);

        // Health vector goes in order of canonical sites array
        assertEquals(query.getHealthVector()[0], 0.5, epsilon);
        assertEquals(query.getHealthVector()[1], 0.5, epsilon);
        assertEquals(query.getHealthVector()[2], 0.7, epsilon);
    }

    public void testHasCanonicalForm() {
        // Try something that does...
        Coordinate yes = new Coordinate(0, 0, Flags.BEYOND_BOUNDS);
        assertTrue(query.hasCanonicalForm(yes));

        // Try something that doesn't
        Coordinate no = new Coordinate(-1, 0, Flags.BEYOND_BOUNDS);
        assertFalse(query.hasCanonicalForm(no));
    }

    public abstract CellLayerContent makeQuery();

    public void testClone() {
        Object clone = query.clone();
        assertEquals(query, clone);
        assertFalse(query == clone);
    }
}
