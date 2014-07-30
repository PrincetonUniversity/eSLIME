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
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.cell.CellUpdateManager;
import layers.cell.MockCellLayerContent;
import layers.cell.MockCellLayerIndices;
import test.EslimeTestCase;

public class CellUpdateManagerTest extends EslimeTestCase {
    private MockGeometry geom;
    private MockCellLayerIndices indices;
    private MockCellLayerContent content;
    private Coordinate o, t;
    private CellUpdateManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geom = new MockGeometry();
        o = new Coordinate(0, 0, 0);
        t = new Coordinate(1, 0, 0);
        Coordinate[] cc = new Coordinate[]{o, t};
        geom.setCanonicalSites(cc);

        indices = new MockCellLayerIndices();
        content = new MockCellLayerContent(geom, indices);
        query = new CellUpdateManager(content);
    }

    public void testConsiderApply() {
        MockCell cell = new MockCell();
        query.place(cell, o);
        assertEquals(1, query.consider(o));
        assertEquals(2, query.consider(o));
        query.apply(o);
        assertEquals(1, query.consider(o));

    }

    public void testDivideTo() {
        MockCell cell = new MockCell(1);
        query.place(cell, o);
        MockCell child = new MockCell(2);
        cell.setChild(child);
        assertNull(indices.getLastPrevious());
        assertEquals(cell, indices.getLastCurrent());
        assertEquals(o, indices.getLastCoord());
        assertTrue(content.has(o));

        query.divideTo(o, t);
        assertNull(indices.getLastPrevious());
        assertEquals(t, indices.getLastCoord());
        assertTrue(content.has(t));

        assertEquals(t, content.locate(child));
    }

    public void testDivide() {
        MockCell cell = new MockCell(1);
        MockCell child = new MockCell(2);
        cell.setChild(child);
        query.place(cell, o);

        Cell actual = query.divide(o);
        Cell expected = child;
        assertEquals(expected, actual);
    }


    public void testBanish() {
        Cell cell = new MockCell();
        content.put(o, cell);

        assertTrue(content.has(o));
        query.banish(o);
        assertFalse(content.has(o));
    }

    public void testMove() {
        Cell cell = new MockCell(1);
        query.place(cell, o);
        assertNull(indices.getLastPrevious());
        assertEquals(cell, indices.getLastCurrent());
        assertEquals(o, indices.getLastCoord());
        assertTrue(content.has(o));

        query.move(o, t);

        assertFalse(content.has(o));
        assertEquals(cell, indices.getLastCurrent());
        assertEquals(t, indices.getLastCoord());
        assertTrue(content.has(t));
    }

    public void testSwap() {
        Cell cell1 = new MockCell(1);
        Cell cell2 = new MockCell(2);
        query.place(cell1, o);
        query.place(cell2, t);

        assertEquals(o, content.locate(cell1));
        assertEquals(t, content.locate(cell2));

        assertEquals(content.get(o).getState(), 1);
        assertEquals(content.get(t).getState(), 2);

        query.swap(o, t);

        assertEquals(o, content.locate(cell2));
        assertEquals(t, content.locate(cell1));

        assertEquals(content.get(o).getState(), 2);
        assertEquals(content.get(t).getState(), 1);

    }

    public void testPlace() {
        Cell cell = new MockCell(1);

        assertFalse(content.has(o));
        query.place(cell, o);
        assertTrue(content.has(o));
    }

}
