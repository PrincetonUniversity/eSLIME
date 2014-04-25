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

import agent.Behavior;
import agent.action.Action;
import agent.action.NullAction;
import agent.control.BehaviorDispatcher;
import cells.BehaviorCell;
import cells.Cell;
import cells.SimpleCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import structural.utilities.EpsilonUtil;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/18/14.
 */
public class CellFactoryTest extends EslimeTestCase {
    Element fixtureRoot;
    MockLayerManager layerManager = new MockLayerManager();
    MockGeneralParameters p = new MockGeneralParameters();

    @Override
    protected void setUp() throws Exception {
        fixtureRoot = readXmlFile("CellFactoryTest.xml");
        layerManager = new MockLayerManager();

        MockGeometry geom = buildMockGeometry();
        CellLayer layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        p.initializeRandom(RANDOM_SEED);
    }

    public void testSimpleCell() {
        Element fixture = fixtureRoot.element("cell-a");
        CellFactory query = new CellFactory(layerManager, fixture, p);
        Cell actual = query.instantiate();
        Cell expected = new SimpleCell(1);
        assertEquals(expected, actual);
    }

    public void testBehaviorCell() {
        Element fixture = fixtureRoot.element("cell-b");
        CellFactory query = new CellFactory(layerManager, fixture, p);
        Cell actual = query.instantiate();

        // Build the expected cell (no mock, as factory must call correct sub-component factories)
        BehaviorCell expected = new BehaviorCell(layerManager, 2, 0.5, 1.0);
        BehaviorDispatcher expectedDispatcher = new BehaviorDispatcher(expected, layerManager, p);

        Action[] actionList = new Action[]{new NullAction()};
        Behavior testBehavior = new Behavior(expected, layerManager, actionList);
        expectedDispatcher.map("test-behavior", testBehavior);
        expected.setDispatcher(expectedDispatcher);

        assertEquals(expected, actual);
    }

    /**
     * Integration test to verify that stochastic behavior works alright.
     */
    public void testStochastic() {
        Element fixture = fixtureRoot.element("cell-stochastic");
        CellFactory query = new CellFactory(layerManager, fixture, p);

        double[] results = new double[10];
        for (int i = 0; i < 10; i++) {
            Cell actual = query.instantiate();
            results[i] = actual.getFitness();
        }

        assertFalse(EpsilonUtil.epsilonEquals(0.0, var(results)));
    }

    // Statistics impl copied from http://introcs.cs.princeton.edu/java/stdlib/StdStats.java.html
    // I should really set up Apache commons math, but to do that I have to figure out Maven, which
    // I'm putting off.
    /**
     * Returns the average value in the array a[], NaN if no such value.
     */
    protected static double mean(double[] a) {
        if (a.length == 0) return Double.NaN;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum = sum + a[i];
        }
        return sum / a.length;
    }

    /**
     * Returns the sample variance in the array a[], NaN if no such value.
     */
    protected static double var(double[] a) {
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
}
