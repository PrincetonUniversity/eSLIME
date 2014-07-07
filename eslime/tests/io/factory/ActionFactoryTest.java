/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.factory;

import agent.action.*;
import agent.targets.TargetRule;
import agent.targets.TargetSelf;
import agent.targets.TargetVacantNeighbors;
import cells.MockCell;
import layers.MockLayerManager;
import org.dom4j.Element;
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
        Action expected = new NullAction();
        assertEquals(expected, actual);
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

    public void testStochasticChoice() throws Exception {
        Element e = fixtureRoot.element("stochastic-choice");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        ActionRangeMap chooser = new ActionRangeMap(1);
        Action child = new NullAction();
        chooser.add(child, 1.0);
        Action expected = new StochasticChoice(callback, layerManager, chooser, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testTrigger() throws Exception {
        Element e = fixtureRoot.element("trigger");
        TargetRule rule = new TargetSelf(callback, layerManager, -1, null);
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new Trigger(callback, layerManager, "test", rule, null, null);
        assertEquals(expected, actual);
    }

    public void testClone() throws Exception {
        Element e = fixtureRoot.element("clone");
        TargetRule rule = new TargetVacantNeighbors(callback, layerManager, 1, null);
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new CloneTo(callback, layerManager, rule, false, null, null, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testAdjustHealth() throws Exception {
        Element e = fixtureRoot.element("adjust-health");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new AdjustHealth(callback, layerManager, 0.5);
        assertEquals(expected, actual);
    }

    public void testExpand() throws Exception {
        Element e = fixtureRoot.element("expand");
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new Expand(callback, layerManager, null, null, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testExpandTo() throws Exception {
        Element e = fixtureRoot.element("expand-to");
        TargetRule rule = new TargetVacantNeighbors(callback, layerManager, 1, null);
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new ExpandTo(callback, layerManager, rule, null, null, p.getRandom());
        assertEquals(expected, actual);
    }

    public void testSwap() throws Exception {
        Element e = fixtureRoot.element("swap");
        TargetRule rule = new TargetVacantNeighbors(callback, layerManager, 1, null);
        Action actual = ActionFactory.instantiate(e, callback, layerManager, p);
        Action expected = new Swap(callback, layerManager, rule, null, null);
        assertEquals(expected, actual);
    }
}
