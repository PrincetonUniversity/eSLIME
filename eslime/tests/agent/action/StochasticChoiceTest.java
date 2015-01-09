/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.MockCell;
import layers.MockLayerManager;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by dbborens on 3/6/14.
 */
public class StochasticChoiceTest extends EslimeTestCase {

    private StochasticChoice query;
    private MockCell callback;
    private MockLayerManager layerManager;
    private MockAction action;
    private MockActionRangeMap chooser;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        random = new Random(RANDOM_SEED);
        callback = new MockCell();
        action = new MockAction();
        chooser = new MockActionRangeMap();
        chooser.setNextTarget(action);
        layerManager = new MockLayerManager();
        //query = new StochasticChoice(callback, layerManager, chooser, random);
    }

    public void testRun() throws Exception {
        assertEquals(0, action.getTimesRun());
        query.run(null);
        assertEquals(1, action.getTimesRun());
    }

    public void testEquals() throws Exception {
        MockCell otherCell = new MockCell();
        //MockActionRangeMap otherChooser = new MockActionRangeMap();
        fail("Rewrite me");
//        StochasticChoice other = new StochasticChoice(otherCell, null, otherChooser, null);
//
//        /*
//         * The only conditions for equality are (1) that both objects are
//         * StochasticChoice objects and (2) that they have equal choice sets.
//         */
//
//        chooser.setReportEquality(true);
//        otherChooser.setReportEquality(true);
//
//        assertEquals(query, other);
//
//        chooser.setReportEquality(false);
//        assertNotEquals(query, other);
    }

    public void testClone() throws Exception {
        chooser.setReportEquality(true);
        MockCell child = new MockCell();
        Action clone = query.clone(child);
        assertEquals(query, clone);
        assertEquals(1, chooser.getTimesCloned());
    }
}
