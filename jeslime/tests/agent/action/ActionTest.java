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

package agent.action;

import cells.BehaviorCell;
import cells.Cell;
import cells.MockCell;
import junit.framework.TestCase;
import layers.LayerManager;
import layers.MockLayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public class ActionTest extends TestCase {

    MockLayerManager layerManager;
    Coordinate caller;
    BehaviorCell callback;
    ExposedAction query;

    @Override
    protected void setUp() throws Exception {
        layerManager = new MockLayerManager();
        callback = new BehaviorCell();
        caller = new Coordinate(0, 0, 0);

        query = new ExposedAction(callback, layerManager);
    }

    public void testGetLayerManager() throws Exception {
        LayerManager expected = layerManager;
        LayerManager actual = query.getLayerManager();
        assertEquals(expected, actual);
    }

    public void testGetCallback() throws Exception {
        Cell expected = callback;
        Cell actual = query.getCallback();
        assertEquals(expected, actual);
        assertTrue(expected == actual);
    }

    public void testRunNullCaller() throws Exception {
        query.run(null);
        assertTrue(query.isRun());
    }

    public void testRunWithCaller() throws Exception {
        query.run(caller);
        assertTrue(query.isRun());
        assertEquals(caller, query.getLastCaller());
    }

    private class ExposedAction extends Action {

        private boolean isRun = false;
        private Coordinate lastCaller = null;

        public boolean isRun() {
            return isRun;
        }

        public Coordinate getLastCaller() {
            return lastCaller;
        }

        @Override
        public void run(Coordinate caller) {
            isRun = true;
            lastCaller = caller;
        }

        @Override
        public BehaviorCell getCallback() {
            return super.getCallback();
        }

        public ExposedAction(BehaviorCell callback, LayerManager layerManager) {
            super(callback, layerManager);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof ExposedAction);
        }

        @Override
        public Action clone(BehaviorCell child) {
            return new ExposedAction(child, layerManager);
        }
    }
}
