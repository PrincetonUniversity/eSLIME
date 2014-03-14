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
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.StepState;
import structural.halt.ExtinctionEvent;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 3/5/14.
 */
public class CheckForExtinctionTest extends TestCase {
    private MockGeometry geometry;
    private CellLayer layer;
    private MockLayerManager layerManager;
    private CheckForExtinction query;

    private void setup(Coordinate[] cc) {
        geometry = new MockGeometry();
        geometry.setCanonicalSites(cc);

        layer = new CellLayer(geometry, 0);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        query = new CheckForExtinction(null, layerManager, 0, null);
    }

    private void doTest(boolean expected) {
        boolean actual = false;

        try {
            StepState state = new StepState();
            query.fire(state);
        } catch (ExtinctionEvent event) {
            actual = true;

            // Nothing else is supposed to be thrown!
        } catch (HaltCondition condition) {
            fail("Unexpected halt condition " + condition.getClass().getSimpleName());
        }

        assertEquals(expected, actual);

    }

    public void testExtinctionCase() {
        makeOneCanonicalSite();
        doTest(true);
    }

    public void testNonExtinctionCase() {
        makeOneCanonicalSite();
        Coordinate coord = new Coordinate(0, 0, 1);
        MockCell cell = new MockCell();
        cell.setState(1);
        layer.getUpdateManager().place(cell, coord);
        doTest(false);
    }

    /**
     * Make sure that, if there are occupied sites and they subsequently all
     * become vacant, this is still recorded as an exitnction event.
     */
    public void testTransitionToExtinction() {
        fail("Not yet implemented.");
    }

    private void makeOneCanonicalSite() {
        Coordinate[] cc = new Coordinate[]{
                new Coordinate(0, 0, 1)
        };
        setup(cc);
    }
}
