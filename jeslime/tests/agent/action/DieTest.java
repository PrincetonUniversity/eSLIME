package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.MockCell;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 2/10/14.
 */
public class DieTest extends EslimeLatticeTestCase {
    private Action query, identical, different;
    private BehaviorCell cell;
    private BehaviorDispatcher dispatcher;
    private Behavior behavior;
    private String eventName;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Set up test objects
        cell = new BehaviorCell(layerManager, 1, 1.0, 1.0);
        query = new Die(cell, layerManager);
        identical = new Die(cell ,layerManager);
        different = new AdjustFitness(cell, layerManager, 0.7);

        // Configure behavior dispatcher
        eventName = "TEST";
        Action[] actionSequence = new Action[] {query};
        behavior = new Behavior(cell, layerManager, actionSequence);
        dispatcher = new BehaviorDispatcher(cell, layerManager, null);
        cell.setDispatcher(dispatcher);
        dispatcher.map(eventName, behavior);

        layer.getUpdateManager().place(cell, origin);
    }

    public void testRun() throws Exception {
        assertTrue(layer.getViewer().isOccupied(origin));
        cell.trigger("TEST", null);
        assertFalse(layer.getViewer().isOccupied(origin));
    }

    public void testEquals() throws Exception {
        // Create two equivalent Die objects.
        // Should be equal.
        assertEquals(query, identical);

        // Create a third, different Die object.
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
