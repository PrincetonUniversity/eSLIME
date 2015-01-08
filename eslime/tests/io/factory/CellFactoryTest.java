/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import cells.Cell;
import factory.cell.CellFactory;
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

    // Statistics impl copied from http://introcs.cs.princeton.edu/java/stdlib/StdStats.java.html
    // I should really set up Apache commons math, but to do that I have to figure out Maven, which
    // I'm putting off.

    @Override
    protected void setUp() throws Exception {
        fixtureRoot = readXmlFile("CellFactoryTest.xml");
        layerManager = new MockLayerManager();

        MockGeometry geom = buildMockGeometry();
        CellLayer layer = new CellLayer(geom);
        layerManager.setCellLayer(layer);
        p.initializeRandom(RANDOM_SEED);
    }

    /**
     * Integration test to verify that stochastic behavior works alright.
     */
    public void testStochastic() throws Exception {
        Element fixture = fixtureRoot.element("cell-stochastic");
        CellFactory query = new CellFactory(layerManager, fixture, p);

        double[] results = new double[10];
        for (int i = 0; i < 10; i++) {
            Cell actual = query.instantiate();
            results[i] = actual.getHealth();
        }

        assertFalse(EpsilonUtil.epsilonEquals(0.0, var(results)));
    }

    public void testReactions() {
        fail("Not yet implemented");
    }
}
