package agent.behaviors;

import cells.Cell;
import cells.MockCell;
import layers.LayerManager;
import layers.MockLayerManager;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorTest extends EslimeTestCase {

    Behavior query;
    MockLayerManager layerManager;
    MockCell callBack;

    @Override
    protected void setUp() throws Exception {
        layerManager = new MockLayerManager();
        callBack = new MockCell();
        query = new ExposedBehavior(callBack, layerManager);
    }

    public void testGetLayerManager() throws Exception {
        LayerManager expected = layerManager;
        LayerManager actual = query.getLayerManager();
        assertEquals(expected, actual);
    }

    public void testGetCallback() throws Exception {
        Cell expected = callBack;
        Cell actual = query.getCallback();
        assertEquals(expected, actual);
    }

    private class ExposedBehavior extends Behavior {
        @Override
        public LayerManager getLayerManager() {
            return super.getLayerManager();
        }

        @Override
        public Cell getCallback() {
            return super.getCallback();
        }

        public ExposedBehavior(Cell callback, LayerManager layerManager) {
            super(callback, layerManager);
        }

        @Override
        public void run(Coordinate caller) {

        }

    }
}
