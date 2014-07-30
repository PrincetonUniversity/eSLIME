/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package processes.discrete.filter;

import cells.Cell;
import cells.MockCell;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import layers.cell.CellUpdateManager;
import test.EslimeLatticeTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CellStateFilterTest extends EslimeLatticeTestCase {
    private Cell yes, no;
    private CellStateFilter query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        yes = new MockCell(1);
        no = new MockCell(2);

        CellUpdateManager u = cellLayer.getUpdateManager();

        u.place(yes, x);
        u.place(no, y);

        query = new CellStateFilter(cellLayer, new ConstantInteger(1));
    }

    public void testLifeCycle() throws Exception {
        Collection<Coordinate> cc = Arrays.asList(geom.getCanonicalSites());
        Collection<Coordinate> ccCopy = new ArrayList<>(cc);

        // Apply filter.
        Collection<Coordinate> actual = query.apply(cc);

        // Only "x" should be retained.
        Collection<Coordinate> expected = Arrays.asList(new Coordinate[] {x});
        assertTrue(collectionsEqual(expected, actual));

        // Original list should be unmodified
        assertTrue(collectionsEqual(cc, ccCopy));
    }

    private boolean collectionsEqual(Collection<Coordinate> p, Collection<Coordinate> q) {
        if (p.size() != q.size()) {
            return false;
        }

        Iterator<Coordinate> qIter = q.iterator();

        for (Coordinate pCoord : p) {
            Coordinate qCoord = qIter.next();

            if (pCoord != qCoord) {
                return false;
            }
        }

        return true;
    }
}