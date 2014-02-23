package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.MockCell;
import geometry.Geometry;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.EpsilonUtil;
import test.EslimeLatticeTestCase;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 2/5/14.
 */
public class AdjustFitnessTest extends EslimeLatticeTestCase {
    private AdjustFitness query, identical, different;
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
        layer = new CellLayer(geom, 0);
        layerManager.setCellLayer(layer);
        cell = new BehaviorCell(layerManager, 1, 0.5, 1.0);
        layer.getUpdateManager().place(cell, origin);
        query = new AdjustFitness(cell, layerManager, 0.5);
        identical = new AdjustFitness(cell ,layerManager, 0.5);
        different = new AdjustFitness(cell, layerManager, 0.7);

        // Configure behavior dispatcher
        eventName = "TEST";
        Action[] actionSequence = new Action[] {query};
        behavior = new Behavior(cell, layerManager, actionSequence);
        dispatcher = new BehaviorDispatcher();
        cell.setDispatcher(dispatcher);
        dispatcher.map(eventName, behavior);
    }

    public void testRun() throws Exception {
        assertEquals(0.5, cell.getFitness(), epsilon);
        cell.trigger("TEST", null);
        assertEquals(1.0, cell.getFitness(), epsilon);
    }

    public void testEquals() throws Exception {
        // Create two equivalent AdjustFitness objects.
        // Should be equal.
        assertEquals(query, identical);

        // Create a third, different AdjustFitness object.
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
