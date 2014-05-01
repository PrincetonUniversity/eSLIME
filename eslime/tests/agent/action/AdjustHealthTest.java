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

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.MockCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import test.EslimeLatticeTestCase;

/**
 * Created by David B Borenstein on 2/5/14.
 */
public class AdjustHealthTest extends EslimeLatticeTestCase {
    private AdjustHealth query, identical, different;
    private BehaviorCell cell;
    private BehaviorDispatcher dispatcher;
    private Behavior behavior;
    private String eventName;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Set up test objects
        layerManager = new MockLayerManager();
        MockGeometry geom = buildMockGeometry();
        cellLayer = new CellLayer(geom);
        layerManager.setCellLayer(cellLayer);
        cell = new BehaviorCell(layerManager, 1, 0.5, 1.0);
        cellLayer.getUpdateManager().place(cell, origin);
        query = new AdjustHealth(cell, layerManager, 0.5);
        identical = new AdjustHealth(cell, layerManager, 0.5);
        different = new AdjustHealth(cell, layerManager, 0.7);

        // Configure behavior dispatcher
        eventName = "TEST";
        Action[] actionSequence = new Action[]{query};
        behavior = new Behavior(cell, layerManager, actionSequence);
        dispatcher = new BehaviorDispatcher();
        cell.setDispatcher(dispatcher);
        dispatcher.map(eventName, behavior);
    }

    public void testRun() throws Exception {
        assertEquals(0.5, cell.getHealth(), epsilon);
        cell.trigger("TEST", null);
        assertEquals(1.0, cell.getHealth(), epsilon);
    }

    public void testEquals() throws Exception {
        // Create two equivalent AdjustHealth objects.
        // Should be equal.
        assertEquals(query, identical);

        // Create a third, different AdjustHealth object.
        // Should not be equal.
        assertNotEquals(query, different);
    }


    public void testClone() throws Exception {
        MockCell cloneCell = new MockCell();

        // Clone it.
        Action clone = query.clone(cloneCell);

        // Clone should not be the same object.
        assertFalse(clone == query);

        // Clone should be equal.
        assertTrue(clone.equals(query));
    }
}
