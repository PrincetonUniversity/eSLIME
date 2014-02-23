package agent.action;

import agent.Behavior;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.MockCell;
import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/10/14.
 */
public class DieTest extends EslimeTestCase {
    private Action query, identical, different;
    private BehaviorCell cell;
    private BehaviorDispatcher dispatcher;
    private MockLayerManager layerManager;
    private Behavior behavior;
    private String eventName;

    private Coordinate o;
    private MockGeometry geom;
    private CellLayer cellLayer;

    @Override
    protected void setUp() throws Exception {
        // Set up test objects
        layerManager = new MockLayerManager();
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

        // Set up geometrical environment
        geom = buildMockGeometry();
        o = geom.getCanonicalSites()[0];
        cellLayer = new CellLayer(geom, 0);
        layerManager.setCellLayer(cellLayer);
        cellLayer.getUpdateManager().place(cell, o);
    }

    public void testRun() throws Exception {
        assertTrue(cellLayer.getViewer().isOccupied(o));
        cell.trigger("TEST", null);
        assertFalse(cellLayer.getViewer().isOccupied(o));
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
