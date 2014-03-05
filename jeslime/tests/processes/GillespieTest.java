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

package processes;

import test.EslimeTestCase;
import processes.gillespie.GillespieChooser;
import processes.gillespie.GillespieState;

public class GillespieTest extends EslimeTestCase {

	/**
	 * Test the GillespieState object.
	 */
	public void testGillespieStateLifeCycle() {
		Integer[] arr = new Integer[] {100, 200};
		GillespieState gs = new GillespieState(arr);
		
		// Setters should work right now
		gs.add(100, 5, 0.7);
		gs.add(200, 3, 0.4);

		// Close it so we can use getters
		gs.close();

		assertEquals(gs.getTotalWeight(), 1.1, epsilon);
		assertEquals(2, gs.getKeys().length);
		assertEquals(5, gs.getEventCount(100));
		assertEquals(3, gs.getEventCount(200));
		assertEquals(0.7, gs.getWeight(100), epsilon);
		assertEquals(0.4, gs.getWeight(200), epsilon);
	}
	

	/**
	 * Test that the weighted target selector works.
	 */
	public void testSelectTarget() {
		Integer[] arr = new Integer[] {100, 200, 300};
		GillespieState gs = new GillespieState(arr);
		
		gs.add(100, 5, 2.0);
		gs.add(200, 3, 1.0);
		gs.add(300, 3, 7.0);
		
		gs.close();
		GillespieChooser gc = new GillespieChooser(gs);
		assertEquals(100, (int) gc.selectTarget(0.0));
		assertEquals(100, (int) gc.selectTarget(0.5));
		assertEquals(200, (int) gc.selectTarget(2.0));
		assertEquals(200, (int) gc.selectTarget(2.5));
		assertEquals(300, (int) gc.selectTarget(3.0));
		assertEquals(300, (int) gc.selectTarget(5.0));
		assertEquals(300, (int) gc.selectTarget(7.0));
		assertEquals(300, (int) gc.selectTarget(9.9));
	}
	
	
}
