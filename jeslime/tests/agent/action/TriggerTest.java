package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import agent.targets.TargetAllNeighbors;
import agent.targets.TargetOccupiedNeighbors;
import agent.targets.TargetRule;
import cells.BehaviorCell;
import cells.MockCell;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/11/14.
 */
public class TriggerTest extends EslimeTestCase {

    private Action query;
    private BehaviorCell causeCell, effectCell;
    private BehaviorDispatcher dispatcher;
    private MockLayerManager layerManager;
    private Behavior cause, effect;
    private String causeName, effectName;
    private TargetRule targetRule;
    private Coordinate o;
    private MockGeometry geom;
    private CellLayer cellLayer;

    @Override
    protected void setUp() throws Exception {
        // Start initializing objects. Order is somewhat brittle.
        causeName = "triggers the effect";
        effectName = "gets triggered during test";

        layerManager = new MockLayerManager();

        causeCell = new BehaviorCell(1, 1.0, 1.0);
        effectCell = new BehaviorCell(1, 1.0, 1.0);

        targetRule = new TargetOccupiedNeighbors(causeCell, layerManager);
        query = new Trigger(causeCell, layerManager, causeName, targetRule);

        // Configure behavior dispatcher for CAUSE

        // Configure behavior dispatcher for EFFECT

        // Initialize geometry.

        // Place the cause and effect cells next to each other.


        // Old code -- use for guidance
//        Action[] actionSequence = new Action[] {query};
//        behavior = new Behavior(cell, layerManager, actionSequence);
//        dispatcher = new BehaviorDispatcher(cell, layerManager);
//        cell.setDispatcher(dispatcher);
//        dispatcher.map(eventName, behavior);
//
//        // Set up geometrical environment
//        geom = buildMockGeometry();
//        o = geom.getCanonicalSites()[0];
//        cellLayer = new CellLayer(geom, 0);
//        layerManager.setCellLayer(cellLayer);
//        cellLayer.getUpdateManager().place(cell, o);
    }

    public void testRun() throws Exception {
        // Create another behavior cell and give it a mock behavior.

        // Trigger that behavior via the trigger action on the test cell.

        // The mock behavior should have been triggered.
        fail();
    }

    public void testEquals() throws Exception {
        Action identical, differentBehavior, differentTargeter;
        makeCells();

        MockCell dummyCell1 = new MockCell();
        MockCell dummyCell2 = new MockCell();

        TargetRule sameTargetRule = new TargetOccupiedNeighbors(dummyCell1, layerManager);
        TargetRule differentTargetRule = new TargetOccupiedNeighbors(dummyCell2, layerManager);


        String differentBehaviorName = "not the same as causeName";

        identical = new Trigger(dummyCell1, layerManager, causeName, targetRule);
        differentBehavior = new Trigger(dummyCell1, layerManager, differentBehaviorName, sameTargetRule);
        differentTargeter = new Trigger(dummyCell2, layerManager, causeName, differentTargetRule);

        assertEquals(cause, identical);
        assertNotEquals(cause, differentBehavior);
        assertNotEquals(cause, differentTargeter);
    }

    public void testClone() throws Exception {
        // Clone a trigger.
        // Confirm that they are equal.
        fail();
    }

}
