/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package io.project;

import agent.Behavior;
import agent.MockBehavior;
import agent.control.BehaviorDispatcher;
import agent.control.MockBehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import cells.MockCell;
import layers.LayerManager;
import layers.MockLayerManager;
import org.dom4j.Element;
import structural.MockGeneralParameters;
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
    MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        callback = new MockCell();
        dispatcher = new MockBehaviorDispatcher();
        manager = new MockLayerManager();
        p = new MockGeneralParameters();
        query = new BehaviorLoader(dispatcher, callback, manager, p);

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

        public ExposedBehaviorLoader(BehaviorDispatcher behaviorDispatcher, BehaviorCell callback, LayerManager layerManager) {
            super(behaviorDispatcher, callback, layerManager, null);
        }
    }
}
