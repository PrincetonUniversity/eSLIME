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
import control.halt.FixationEvent;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.StepState;
import processes.gillespie.GillespieState;
import test.EslimeTestCase;

/**
 * Fixation is defined as only one cell type existing in the system. The
 * CheckForFixation process should throw a HaltCondition if and only if this
 * definition is met.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForFixationTest extends EslimeTestCase {
    private MockGeometry geometry;
    private CellLayer layer;
    private MockLayerManager layerManager;
    private CheckForFixation query;

    public void testTargetSimple() throws Exception {
        makeTwoCanonicalSites();
        query.target(null);
    }

    public void testTargetGillespie() throws Exception {
        makeTwoCanonicalSites();
        Integer id = query.getID();
        Integer[] ids = new Integer[]{id};
        GillespieState gs = new GillespieState(ids);
        query.target(gs);
        gs.close();
        assertEquals(0.0, gs.getTotalWeight(), epsilon);
        assertEquals(1, gs.getTotalCount());
        assertEquals(1, gs.getEventCount(id));
    }

    public void doTest(boolean expectFixation) {
        boolean fixed = false;
        try {
            StepState state = new StepState();
            query.fire(state);

            // Fixation events halt flow in the simulation.
        } catch (FixationEvent event) {
            fixed = true;

            // Nothing else is supposed to be thrown!
        } catch (HaltCondition condition) {
            fail("Unexpected halt condition " + condition.getClass().getSimpleName());
        }

        assertEquals(expectFixation, fixed);
    }

//    public void testExtinctionCase() {
//        makeTwoCanonicalSites();
//        boolean extinct = false;
//
//        try {
//            StepState state = new StepState();
//            query.fire(state);
//        } catch (ExtinctionEvent event) {
//            extinct = true;
//
//            // Nothing else is supposed to be thrown!
//        } catch (HaltCondition condition) {
//            fail("Unexpected halt condition " + condition.getClass().getSimpleName());
//        }
//
//        assertTrue(extinct);
//    }

    // There's only one site -- automatically fixed once filled
    public void testFixationCaseSingle() {
        makeTwoCanonicalSites();
        Coordinate coord = new Coordinate(0, 0, 1);
        MockCell cell = new MockCell();
        cell.setState(1);
        layer.getUpdateManager().place(cell, coord);
        doTest(true);
    }

    // There are two sites, but they're both the same type
    public void testFixationCaseMulti() {
        makeTwoCanonicalSites();
        for (int i = 0; i < 2; i++) {
            Coordinate coord = new Coordinate(i, 0, 0);
            MockCell cell = new MockCell();
            cell.setState(1);
            layer.getUpdateManager().place(cell, coord);
        }
        doTest(true);
    }

    // The lattice is full, but there are at least two
    // kinds of cells -- should not result in a thrown HaltCondition
    public void testFullNonFixationCase() {
        setUpMixedCase();
        doTest(false);
    }

    private void setUpMixedCase() {
        makeTwoCanonicalSites();
        for (int i = 0; i < 2; i++) {
            Coordinate coord = new Coordinate(i, 0, 0);
            MockCell cell = new MockCell();
            // state 0 is reserved for death / nullity
            cell.setState(i + 1);
            layer.getUpdateManager().place(cell, coord);
        }
    }

    // There's only one species, but there's still room
    // to grow -- should still be considered "fixation"
    public void testOpenSpaceCase() {
        makeTwoCanonicalSites();
        Coordinate coord = new Coordinate(0, 0, 0);
        MockCell cell = new MockCell();
        cell.setState(1);
        layer.getUpdateManager().place(cell, coord);
        doTest(true);
    }

    /**
     * Make sure that, after the extinction of all but one type of site, a
     * fixation event is triggered.
     */
    public void testTwoToOneStateRegression() {
        // This test should start with two cells, each of a different type.
        setUpMixedCase();

        // We don't expect a fixation state exception.
        doTest(false);

        // Remove one of the cells. Now there's only one cell type in the system.
        layer.getUpdateManager().banish(new Coordinate(0, 0, 0));

        // The state should now reflect fixation.
        doTest(true);
    }

    private void makeTwoCanonicalSites() {
        Coordinate[] cc = new Coordinate[]{
                new Coordinate(0, 0, 1),
                new Coordinate(1, 0, 1)
        };
        setup(cc);
    }

    private void setup(Coordinate[] cc) {
        geometry = new MockGeometry();
        geometry.setCanonicalSites(cc);

        layer = new CellLayer(geometry, 0);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        query = new CheckForFixation(null, layerManager, 0, null);
    }

}
