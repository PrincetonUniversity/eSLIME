package agent.control;

import agent.Behavior;
import agent.MockBehavior;
import cells.BehaviorCell;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;

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

    public void testDie() {
        // Set up
        MockGeometry geom = new MockGeometry();
        Coordinate c = new Coordinate(0, 0, 0);
        Coordinate[] cc = new Coordinate[] {c};
        geom.setCanonicalSites(cc);
        CellLayer layer = new CellLayer(geom, 0);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);

        BehaviorCell cell = new BehaviorCell();
        layer.getUpdateManager().place(cell, c);
        query = new BehaviorDispatcher(cell, layerManager);

        // Perform the test
        assertTrue(layer.getViewer().isOccupied(c));
        query.die();
        assertFalse(layer.getViewer().isOccupied(c));
    }
}
