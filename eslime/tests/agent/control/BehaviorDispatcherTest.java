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

package agent.control;

import agent.Behavior;
import agent.MockBehavior;
import cells.BehaviorCell;
import control.identifiers.Coordinate;
import junit.framework.TestCase;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorDispatcherTest extends TestCase {

    private BehaviorDispatcher query;
    private Coordinate caller1, caller2;
    private MockBehavior behavior1, behavior2;

    @Override
    protected void setUp() throws Exception {
        query = new BehaviorDispatcher();
        caller1 = new Coordinate(0, 0, 0);
        caller2 = new Coordinate(1, 0, 0);
        behavior1 = new MockBehavior();
        behavior2 = new MockBehavior();
    }

    public void testLifeCycle() throws Exception {
        String name = "testBehavior";

        // Neither behavior has occurred.
        assertEquals(0, behavior1.getTimesRun());
        assertEquals(0, behavior2.getTimesRun());

        // Map first behavior.
        query.map(name, behavior1);

        // Trigger behavior.
        query.trigger(name, null);

        // Only the first behavior should have occured.
        assertEquals(1, behavior1.getTimesRun());
        assertEquals(0, behavior2.getTimesRun());

        // Remap to second behavior.
        query.map(name, behavior2);

        // Trigger behavior.
        query.trigger(name, null);

        // Each behavior should have happened once.
        assertEquals(1, behavior1.getTimesRun());
        assertEquals(1, behavior2.getTimesRun());
    }

    public void testTriggerWithCallback() throws Exception {
        // Set up
        String name = "testBehavior";
        query.map(name, behavior1);

        // Nobody's been the caller yet
        assertEquals(0, behavior1.timesCaller(caller1));
        assertEquals(0, behavior1.timesCaller(caller2));

        // Trigger with first caller
        query.trigger(name, caller1);

        // First caller has been caller once
        assertEquals(1, behavior1.timesCaller(caller1));
        assertEquals(0, behavior1.timesCaller(caller2));

        // Trigger with second caller
        query.trigger(name, caller2);

        // Each caller has called once
        assertEquals(1, behavior1.timesCaller(caller1));
        assertEquals(1, behavior1.timesCaller(caller2));
    }

    public void testClone() throws Exception {
        String name = "testBehavior";
        query.map(name, behavior1);

        BehaviorCell alternate = new BehaviorCell();
        BehaviorDispatcher clone = query.clone(alternate);

        // The objects should be equal in that their behavior lists are equal.
        assertEquals(query, clone);

        // The objects should not be the same object.
        assertFalse(query == clone);

        // The new object should have the alternate as a callback.
        Behavior clonedBehavior = clone.getMappedBehavior(name);
        assertEquals(alternate, clonedBehavior.getCallback());
    }

}
