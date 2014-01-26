package agent.action;

import cells.Cell;
import cells.MockCell;
import junit.framework.TestCase;
import layers.LayerManager;
import layers.MockLayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public class ActionTest extends TestCase {

    MockLayerManager layerManager;
    Coordinate caller;
    Cell callback;
    ExposedAction query;

    @Override
    protected void setUp() throws Exception {
        layerManager = new MockLayerManager();
        callback = new MockCell();
        caller = new Coordinate(0, 0, 0);

        query = new ExposedAction(callback, layerManager);
    }

    public void testGetLayerManager() throws Exception {
        LayerManager expected = layerManager;
        LayerManager actual = query.getLayerManager();
        assertEquals(expected, actual);
    }

    public void testGetCallback() throws Exception {
        Cell expected = callback;
        Cell actual = query.getCallback();
        assertEquals(expected, actual);
    }

    public void testRunNullCaller() throws Exception {
        query.run(null);
        assertTrue(query.isRun());
    }

    public void testRunWithCaller() throws Exception {
        query.run(caller);
        assertTrue(query.isRun());
        assertEquals(caller, query.getLastCaller());
    }

    private class ExposedAction extends Action {

        private boolean isRun = false;
        private Coordinate lastCaller = null;

        public boolean isRun() {
            return isRun;
        }

        public Coordinate getLastCaller() {
            return lastCaller;
        }

        @Override
        public void run(Coordinate caller) {
            isRun = true;
            lastCaller = caller;
        }

        @Override
        public Cell getCallback() {
            return super.getCallback();
        }

        public ExposedAction(Cell callback, LayerManager layerManager) {
            super(callback, layerManager);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof ExposedAction);
        }

        @Override
        public Action clone(Cell child) {
            return new ExposedAction(child, layerManager);
        }
    }
}
