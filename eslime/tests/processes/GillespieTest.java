/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes;

import processes.gillespie.GillespieChooser;
import processes.gillespie.GillespieState;
import test.EslimeTestCase;

public class GillespieTest extends EslimeTestCase {

    /**
     * Test the GillespieState object.
     */
    public void testGillespieStateLifeCycle() {
        Integer[] arr = new Integer[]{100, 200};
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
        Integer[] arr = new Integer[]{100, 200, 300};
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
