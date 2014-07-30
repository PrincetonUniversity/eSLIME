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

import control.identifiers.Coordinate;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public class NullFilterTest extends TestCase {
    private ArrayList<Coordinate> original, cloned;
    private NullFilter query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        original = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            original.add(new Coordinate(i, i, 0));
        }

        cloned = (ArrayList<Coordinate>) original.clone();

        query = new NullFilter();
    }

    public void testApply() throws Exception {
        Collection<Coordinate> actual = query.apply(original);

        // The result should be identical to the original.
        assertEquals(original, actual);

        // The original should be identical to its clone (ie, unmodified).
        assertEquals(cloned, original);
    }
}