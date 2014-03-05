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

import agent.targets.MockTargetRule;
import agent.targets.TargetOccupiedNeighbors;
import agent.targets.TargetRule;
import cells.MockCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import layers.cell.CellUpdateManager;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by dbborens on 2/11/14.
 */
public class TriggerTest extends EslimeTestCase {

    private Action query;
    private MockCell causeCell, effectCell;
    private MockLayerManager layerManager;
    private String effectName;
    private MockTargetRule targetRule;
    private CellLayer cellLayer;
    private MockGeometry geom;
    private Coordinate o, p, q;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        // Restart "random" number generator with fixed seed
        random = new Random(RANDOM_SEED);

        // Construct base supporting objects.
        causeCell = new MockCell();
        effectCell = new MockCell();
        targetRule = new MockTargetRule();
        layerManager = new MockLayerManager();
        effectName = "effect";

        // Configure geometric state.
        geom = buildMockGeometry();
        o = geom.getCanonicalSites()[0];
        p = geom.getCanonicalSites()[1];
        q = geom.getCanonicalSites()[2];
        cellLayer = new CellLayer(geom, 0);
        cellLayer.getUpdateManager().place(effectCell, o);
        cellLayer.getUpdateManager().place(causeCell, q);
        layerManager.setCellLayer(cellLayer);
        Coordinate[] targets = new Coordinate[] {o};
        targetRule.setTargets(targets);

        // Create a trigger action.
        query = new Trigger(causeCell, layerManager, effectName, targetRule);
    }

    public void testRun() throws Exception {
        /*
          A note on "callers" in this test: the trigger action causes
          some named behavior to take place in the target cell(s). The
          cause of the trigger action is therefore distinct from the
          cause of the behavior that it triggers. Cells can of course
          trigger their own behaviors, depending on the specified target.
        */

        // Set up a calling cell at some site.
        MockCell dummy = new MockCell();

        CellUpdateManager updateManager = cellLayer.getUpdateManager();
        updateManager.place(dummy, p);

        // Run the proces originating at the dummy calling cell.
        query.run(p);

        // "dummy" should be the caller of the trigger() event.
        // (The caller of the targeter is the cause of the Trigger event.)
        assertEquals(dummy, targetRule.getLastCaller());

        // The target cell's "effect" behavior should have fired.
        assertEquals(effectName, effectCell.getLastTriggeredBehaviorName());

        // "causeCell", which causes the target cell to execute the effect
        // behavior, should be the caller of the effect behavior.
        assertEquals(q, effectCell.getLastTriggeredCaller());
    }

    public void testEquals() throws Exception {
        /*
         Trigger actions
         */
        Action identical, differentBehavior, differentTargeter;

        MockCell dummyCell1 = new MockCell();
        MockCell dummyCell2 = new MockCell();

        TargetRule sameTargetRule = new TargetOccupiedNeighbors(dummyCell1, layerManager, -1, random);
        TargetRule differentTargetRule = new TargetOccupiedNeighbors(dummyCell2, layerManager, -1, random);
        String differentEffectName = "not the same as effectName";

        identical = new Trigger(dummyCell1, layerManager, effectName, targetRule);
        differentBehavior = new Trigger(dummyCell1, layerManager, differentEffectName, sameTargetRule);
        differentTargeter = new Trigger(dummyCell2, layerManager, effectName, differentTargetRule);

        assertEquals(query, identical);
        assertNotEquals(query, differentBehavior);
        assertNotEquals(query, differentTargeter);
    }

    public void testClone() throws Exception {
        MockCell cloneCell = new MockCell();
        Action cloned = query.clone(cloneCell);
        assert (cloned != query);
        assertEquals(query, cloned);
        assertEquals(cloneCell, cloned.getCallback());
        assertEquals(causeCell, query.getCallback());
    }

}
