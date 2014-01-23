package agent.behaviors;

import agent.Behavior;
import agent.action.Action;
import agent.action.MockAction;
import cells.Cell;
import cells.MockCell;
import layers.LayerManager;
import layers.MockLayerManager;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorTest extends EslimeTestCase {

    ExposedBehavior query;
    MockLayerManager layerManager;
    MockCell callBack;
    MockAction a, b;
    Action[] actionSequence;

    @Override
    protected void setUp() throws Exception {
        layerManager = new MockLayerManager();
        callBack = new MockCell();

        initActionSequence();
        query = new ExposedBehavior(callBack, layerManager, actionSequence);
    }

    private void initActionSequence() {
        a = new MockAction();
        b = new MockAction();
        actionSequence = new Action[] {a, b};
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

    public void testGetActionSequence() throws Exception {
        fail("Implement me");
    }
    public void testRunNullCaller() throws Exception {
        fail("Implement me");
    }

    public void testRunWithCaller() throws Exception {
        fail("Implement me");
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

        public ExposedBehavior(Cell callback, LayerManager layerManager, Action[] actionSequence) {
            super(callback, layerManager, actionSequence);
        }

        @Override
        public Action[] getActionSequence() {
            return super.getActionSequence();
        }
    }
}
