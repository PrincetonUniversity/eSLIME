package io.project;

import agent.Behavior;
import agent.MockBehavior;
import agent.control.BehaviorDispatcher;
import agent.control.MockBehaviorDispatcher;
import cells.Cell;
import cells.MockCell;
import layers.LayerManager;
import layers.MockLayerManager;
import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorLoaderTest extends EslimeTestCase {

    MockCell callback;
    MockBehaviorDispatcher dispatcher;


    MockLayerManager manager;
    BehaviorLoader query;
    Element fixtures;

    @Override
    protected void setUp() throws Exception {
        callback = new MockCell();
        dispatcher = new MockBehaviorDispatcher();
        manager = new MockLayerManager();
        query = new BehaviorLoader(dispatcher, callback, manager);

        fixtures = readXmlFile("BehaviorLoaderTest.xml");
    }

    public void testLoadAllBehaviors() {
        query.loadAllBehaviors(fixtures);
        assertEquals(2, dispatcher.getMappedBehaviors().size());
        assertEquals("second-behavior", dispatcher.getLastMappedName());

        Behavior expected = new MockBehavior();
        Behavior actual = dispatcher.getLastMappedBehavior();
        assertEquals(expected, actual);
    }

    public void testLoadBehavior() {
        Element firstBehavior = fixtures.element("first-behavior");
        query.loadBehavior(firstBehavior);
        assertEquals(1, dispatcher.getMappedBehaviors().size());
        assertEquals("first-behavior", dispatcher.getLastMappedName());
    }

    private class ExposedBehaviorLoader extends BehaviorLoader {

        @Override
        public void loadBehavior(Object o) {
            super.loadBehavior(o);
        }

        public ExposedBehaviorLoader(BehaviorDispatcher behaviorDispatcher, Cell callback, LayerManager layerManager) {
            super(behaviorDispatcher, callback, layerManager);
        }
    }
}
