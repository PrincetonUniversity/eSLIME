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

package cells;

import control.identifiers.Coordinate;
import test.EslimeTestCase;

public class FissionCellTest extends EslimeTestCase {

    public void testConstruct() {
        // Cell with biomass > threshold --> should be immediately divisible
        Cell query = new FissionCell(1, 1.0, 0.5);
        assertTrue(query.isDivisible());
        assertEquals(1, query.getState());

        // Cell with biomass < threshold --> not divisible
        query = new FissionCell(2, 0.5, 1.0);
        assertEquals(query.getFitness(), 0.5, epsilon);

        assertTrue(!query.isDivisible());
        assertEquals(2, query.getState());
    }

    public void testFeedConsiderApply() {

        Cell query = new FissionCell(1, 0.5, 1.0);
        assertEquals(0.5, query.getFitness(), epsilon);

        // Feed, but don't apply -- nothing should happen, except the
        // ConsiderCount goes up.
        query.adjustFitness(0.75);

        // consider() doesn't do anything for FissionCell
        int result = query.consider();

        assertEquals(1, result);
        assertEquals(0.5, query.getFitness(), epsilon);
        assertTrue(!query.isDivisible());

        result = query.consider();
        assertEquals(2, result);

        // After applying, changes should take effect.
        query.apply();
        assertEquals(1.25, query.getFitness(), epsilon);
        assertTrue(query.isDivisible());

        // Now adjustFitness a negative delta (i.e., a stressor) until it's too weak
        query.adjustFitness(-0.5);
        query.apply();
        assertEquals(0.75, query.getFitness(), epsilon);
        assertTrue(!query.isDivisible());

        result = query.consider();

        assertEquals(1, result);
    }

    public void testDivide() {
        // Start with a cell whose fitness is 3 times its division threshold
        Cell parent = new FissionCell(1, 3.0, 1.0);

        // Divide once -- each daughter should be divisible
        Cell daughter = parent.divide();

        assertEquals(parent.getState(), 1);
        assertEquals(daughter.getState(), 1);

        assertEquals(parent.getFitness(), 1.5, epsilon);
        assertEquals(daughter.getFitness(), 1.5, epsilon);

        assertTrue(parent.isDivisible());
        assertTrue(daughter.isDivisible());

        // Divide the daughter a second time -- neither cell involved should
        // be divisible
        Cell granddaughter = daughter.divide();

        assertEquals(parent.getState(), 1);
        assertEquals(daughter.getState(), 1);
        assertEquals(granddaughter.getState(), 1);

        assertEquals(daughter.getFitness(), 0.75, epsilon);
        assertEquals(granddaughter.getFitness(), 0.75, epsilon);
        assertEquals(parent.getFitness(), 1.5, epsilon);

        assertTrue(parent.isDivisible());
        assertTrue(!daughter.isDivisible());
        assertTrue(!granddaughter.isDivisible());
    }

    public void testTriggerThrowsException() {
        boolean thrown = false;

        try {
            Cell query = new FissionCell(1, 3.0, 1.0);
            query.trigger("a", new Coordinate(0, 0, 0));
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }
}
