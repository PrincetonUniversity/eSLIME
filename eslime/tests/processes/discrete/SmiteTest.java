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

package processes.discrete;

import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.gillespie.GillespieState;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 12/24/13.
 */
public class SmiteTest extends EslimeTestCase {

    private Smite smite;
    private Coordinate query;

    // We can use a real cell layer, because they manage
    // their own child objects (update manager, viewer, indices)
    // and therefore are compatible with mock infrastructure.
    private CellLayer layer;
    private MockLayerManager layerManager;

    @Override
    protected void setUp() throws Exception {
        // Construct coordinate to smite
        query = new Coordinate(0, 0, 0);
        Coordinate[] cc = new Coordinate[]{query};

        // Construct geometry
        MockGeometry geom = new MockGeometry();
        geom.setCanonicalSites(cc);

        layerManager = new MockLayerManager();

        // Construct layer
        layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);

        // Construct process
        smite = new Smite(layerManager, false);
        smite.setActiveSites(cc);
    }

    /**
     * Make sure that the "target" method works as expected without
     * a Gillespie parent.
     *
     * @throws Exception
     */
    public void testTargetSimple() throws Exception {
        // Target is a pass-through without Gillespie, but it
        // should still work (i.e., not throw an exception.).
        smite.target(null);
    }

    /**
     * Make sure that the "target" method works as expected when
     * using Gillespie mode.
     */
    public void testTargetGillespie() throws Exception {
        Integer[] keys = new Integer[]{0};
        GillespieState state = new GillespieState(keys);

        // Gillespie state should be updated by target
        smite.target(state);
        state.close();
        assertEquals(1, state.getTotalCount());
        assertEquals(1.0, state.getTotalWeight(), epsilon);
    }

    /**
     * Makes sure that the process behaves as expected.
     *
     * @throws Exception
     */
    public void testLifeCycle() throws Exception {
        // Create a cell on the lattice
        MockCell cell = new MockCell(1);
        layer.getUpdateManager().place(cell, query);

        // Verify that the cell is there
        assertTrue(layer.getViewer().isOccupied(query));

        // Smite it
        smite.fire(null);

        // Verify that the cell is not there
        assertFalse(layer.getViewer().isOccupied(query));
    }
}
