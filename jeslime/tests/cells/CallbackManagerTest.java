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
    private CellLayer layer;
    private Coordinate c;
    private BehaviorCell cell;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockGeometry geom = new MockGeometry();
        c = new Coordinate(0, 0, 0);
        Coordinate[] cc = new Coordinate[] {c};
        geom.setCanonicalSites(cc);
        layer = new CellLayer(geom, 0);
        MockLayerManager layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);

        cell = new MockCell();
        layer.getUpdateManager().place(cell, c);
        query = new CallbackManager(cell, layerManager);
    }


    public void testRefreshDivisibility() throws Exception {
        /*
          On the face of it, this looks nearly identical to a test
          in BehaviorCellTest. However, since MockCell does automatically
          update the layer indices as BehaviorCell does, this actually
          verifies the functionality of refreshDivisibility directly.
          It would be better to have a true mock cell layer that could
          confirm the appropriate calls were made and nothing more.
         */

        cell.setDivisible(false);
        query.refreshDivisibility();
        assertDivisibilityStatus(false);

        // Adjust above threshold.
        cell.setDivisible(true);
        query.refreshDivisibility();
        assertDivisibilityStatus(true);

        // Adjust below threshold again.
        cell.setDivisible(false);
        query.refreshDivisibility();
        assertDivisibilityStatus(false);
    }

    public void testDie() {
        // Perform the test
        CellLayerViewer viewer = layer.getViewer();
        boolean isOccupied = viewer.isOccupied(c);
        assertTrue(isOccupied);
        query.die();
        assertFalse(layer.getViewer().isOccupied(c));
    }

    private void assertDivisibilityStatus(boolean expected) {
        boolean actual = layer.getViewer().isDivisible(c);
        assertEquals(expected, actual);
    }
}
