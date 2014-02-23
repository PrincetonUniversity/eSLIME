package io.project;

import agent.action.*;
import agent.targets.TargetRule;
import agent.targets.TargetSelf;
import cells.MockCell;
import junit.framework.TestCase;
import layers.MockLayerManager;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 1/24/14.
 */
public class ActionFactoryTest extends EslimeTestCase {
    private MockCell callback;
    private MockLayerManager layerManager;
    private MockGeneralParameters p;
    private Element fixtureRoot;

    @Override
    protected void setUp() throws Exception {
        fixtureRoot = readXmlFile("ActionFactoryTest.xml");
        callback = new MockCell();
        layerManager = new MockLayerManager();
        p = new MockGeneralParameters();
        p.initializeRandom(RANDOM_SEED);
    }

    public void testNull() throws Exception {
        Element e = fixtureRoot.element("null");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        assertNull(actual);
    }

    public void testMock() throws Exception {
        Element e = fixtureRoot.element("mock");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new MockAction();

        assertEquals(expected, actual);
    }

    public void testDie() throws Exception {
        Element e = fixtureRoot.element("die");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new Die(callback, layerManager);
        assertEquals(expected, actual);
    }

    public void testTrigger() throws Exception {
        Element e = fixtureRoot.element("trigger");
        TargetRule rule = new TargetSelf(callback, layerManager, -1, null);
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new Trigger(callback, layerManager, "test", rule);
        assertEquals(expected, actual);
    }

    public void testAdjustFitness() throws Exception {
        Element e = fixtureRoot.element("adjust-fitness");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new AdjustFitness(callback, layerManager, 0.5);
        assertEquals(expected, actual);

    }
}
