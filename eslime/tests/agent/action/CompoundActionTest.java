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

package agent.action;

import cells.BehaviorCell;
import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import layers.LayerManager;
import layers.MockLayerManager;
import test.EslimeTestCase;

/**
 * Created by dbborens on 3/6/14.
 */
public class CompoundActionTest extends EslimeTestCase {

    ExposedCompoundAction query;
    MockLayerManager layerManager;
    MockCell callBack;
    MockAction a, b;
    Coordinate caller;

    Action[] actionSequence;

    @Override
    protected void setUp() throws Exception {
        layerManager = new MockLayerManager();
        callBack = new MockCell();
        caller = new Coordinate(0, 0, 0);
        initActionSequence();
        query = new ExposedCompoundAction(callBack, layerManager, actionSequence);
    }

    private void initActionSequence() {
        a = new MockAction();
        b = new MockAction();

        actionSequence = new Action[]{a, b};
    }

    public void testGetLayerManager() throws Exception {
        LayerManager expected = layerManager;
        LayerManager actual = query.getLayerManager();
        assertEquals(expected, actual);
    }

    public void testGetCallback() throws Exception {
        Cell expected = callBack;
        Cell actual = query.getCallback();
        assertEquals(expected, actual);
    }

    public void testGetActionSequence() throws Exception {
        Action[] expected = actionSequence;
        Action[] actual = query.getActionSequence();

        assertEquals(2, actual.length);
        for (int i = 0; i < 2; i++) {
            // These should just be assigned
            assertEquals(expected[i], actual[i]);
        }
    }

    public void testRunNullCaller() throws Exception {
        query.run(null);
        assertEquals(1, a.getTimesRun());
        assertEquals(1, b.getTimesRun());
        assertNull(a.getLastCaller());
        assertNull(b.getLastCaller());
    }

    public void testRunWithCaller() throws Exception {
        query.run(caller);
        assertEquals(1, a.getTimesRun());
        assertEquals(1, b.getTimesRun());
        assertEquals(caller, a.getLastCaller());
        assertEquals(caller, b.getLastCaller());
    }

    public void testClone() throws Exception {
        BehaviorCell cloneCell = new BehaviorCell();
        CompoundAction clone = query.clone(cloneCell);
        assertEquals(cloneCell, clone.getCallback());
        assertEquals(clone, query);
    }

    private class ExposedCompoundAction extends CompoundAction {
        @Override
        public LayerManager getLayerManager() {
            return super.getLayerManager();
        }

        @Override
        public void run(Coordinate caller) {
            super.run(caller);
        }

        public ExposedCompoundAction(BehaviorCell callback, LayerManager layerManager, Action[] actionSequence) {
            super(callback, layerManager, actionSequence);
        }

        @Override
        public Action[] getActionSequence() {
            return super.getActionSequence();
        }
    }
}
