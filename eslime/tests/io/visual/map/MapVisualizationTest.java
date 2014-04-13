/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.visual.map;

import io.visual.glyph.Glyph;
import io.visual.glyph.GlyphTest;
import io.visual.glyph.MockGlyph;
import layers.LightweightSystemState;

/**
 * Created by dbborens on 4/1/14.
 */
public class MapVisualizationTest extends GlyphTest {
    @Override
    protected Glyph makeGlyph() {
        return new MockGlyph();
    }

    @Override
    protected String getFileName() {
        return "mapVisualizationTest.png";
    }

    @Override
    protected void populateStateAndFitness(LightweightSystemState systemState) {
        int n = makeGeometry().getCanonicalSites().length;
        double[] fitness = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            fitness[i] = (i % 2) + 1;
            state[i] = ((i + 1) % 2) + 1;
        }
        systemState.setFitnessVector(fitness);
        systemState.setStateVector(state);
    }
}
