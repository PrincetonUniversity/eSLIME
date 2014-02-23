package cells;

import geometry.MockGeometry;
import junit.framework.TestCase;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import layers.cell.CellLayerViewer;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 2/21/14.
 */
public class CallbackManagerTest extends TestCase {
    private CallbackManager query;

    public void testRefreshDivisibility() throws Exception {
        fail("Not yet implemented.");
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
        query = new CallbackManager(cell, layerManager);

        // Perform the test
        CellLayerViewer viewer = layer.getViewer();
        boolean isOccupied = viewer.isOccupied(c);
        assertTrue(isOccupied);
        query.die();
        assertFalse(layer.getViewer().isOccupied(c));
    }
}
