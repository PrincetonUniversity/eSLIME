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

package cells;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Tests the functionality of the SimpleCell class.
 */
public class SimpleCellTest extends EslimeTestCase {
	private int state = 1;
	
	public void testConstruct() {
		Cell query = new SimpleCell(state);
		assertNotNull(query);
		assertEquals(state, query.getState());
		
		// TODO This should become machine epsilon after adding parameters
		assertEquals(0D, query.getFitness(), epsilon);
	}
	
	public void testConsiderAndApply() {
		Cell query = new SimpleCell(state);
		
		int result = query.consider();
		assertEquals(1, result);
		
		result = query.consider();
		assertEquals(2, result);
		
		query.apply();
		
		result = query.consider();
		
		assertEquals(1, result);
	}
	
	public void testDivide() {
		Cell parent = new SimpleCell(state);
		Cell child  = parent.divide();
		
		assertEquals(state, child.getState());
	}
	
	public void testFeedThrowsException() {
		Cell c = new SimpleCell(state);
		try {
			c.feed(0.01D);
		} catch (UnsupportedOperationException e) {
			return;
		}
		
		fail();
	}

    public void testTriggerThrowsException() {
        boolean thrown = false;

        try {
            Cell query = new SimpleCell(state);
            query.trigger("a", new Coordinate(0, 0, 0));
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

}
