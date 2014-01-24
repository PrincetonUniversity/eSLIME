package io.project;

import agent.action.Action;
import agent.action.MockAction;
import cells.MockCell;
import junit.framework.TestCase;
import layers.MockLayerManager;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/24/14.
 */
public class ActionFactoryTest extends EslimeTestCase {
    private MockCell callback;
    private MockLayerManager layerManager;

    @Override
    protected void setUp() throws Exception {
        callback = new MockCell();
        layerManager = new MockLayerManager();
    }

    public void testNull() throws Exception {
        Element e = new BaseElement("null");
        Action actual = ActionFactory.instantiate(e, callback, layerManager);
        assertNull(actual);
    }

    public void testMock() throws Exception {
        Element e = new BaseElement("mock");
        Action actual = ActionFactory.instantiate(e, callback, layerManager);
        Action expected = new MockAction();

        assertEquals(expected, actual);
    }
}
