/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import agent.action.Action;
import agent.action.CompoundAction;
import agent.action.MockAction;
import factory.agent.action.ActionRangeMapFactory;
import org.dom4j.Element;
import structural.RangeMap;
import test.EslimeTestCase;

/**
 * Created by dbborens on 3/6/14.
 */
public class ActionChooserFactoryTest extends EslimeTestCase {
    private Element root;
    private Action singleton;
    private Action multi;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("ActionChooserFactoryTest.xml");
        buildExpectationActions();
    }

    public void testInstantiateOneChild() throws Exception {
        RangeMap<Action> chooser = getChooser("singleton-case");

        Action actual = chooser.selectTarget(0.25);
        Action expected = singleton;

        assertEquals(expected, actual);
    }

    public void testInstantiateMultipleChildren() throws Exception {
        RangeMap<Action> chooser = getChooser("multi-case");

        Action actual = chooser.selectTarget(0.25);
        Action expected = multi;
        assertEquals(expected, actual);
    }

    private void buildExpectationActions() {
        singleton = new MockAction();


        Action[] sequence = new Action[]{
                new MockAction(),
                new MockAction()
        };

        multi = new CompoundAction(null, null, sequence);
    }

    /**
     * Test that the factory successfully builds a chooser
     * with more than one child.
     *
     * @throws Exception
     */
    public void testChooserIntegration() throws Exception {
        RangeMap<Action> expected = new RangeMap<>();
        expected.add(singleton, 0.5);
        expected.add(multi, 1.0);

        RangeMap<Action> actual = getChooser("integration-case");
        assertEquals(expected, actual);
    }

    private RangeMap<Action> getChooser(String rootName) {
        Element singletonCase = root.element(rootName);
        RangeMap<Action> chooser = ActionRangeMapFactory.instantiate(singletonCase, null, null, null);
        return chooser;
    }
}
