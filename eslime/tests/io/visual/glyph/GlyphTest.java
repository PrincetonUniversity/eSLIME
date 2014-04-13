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

package io.visual.glyph;

import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import io.deserialize.MockCoordinateDeindexer;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.highlight.HighlightManager;
import io.visual.map.MapState;
import io.visual.map.MapVisualization;
import layers.LightweightSystemState;
import layers.SystemState;
import test.EslimeTestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Integration test of the dot glyph (a filled dot at the center of the cell).
 * <p/>
 * Created by dbborens on 4/3/14.
 */
public abstract class GlyphTest extends EslimeTestCase {

    Geometry geometry;
    private HighlightManager highlightManager;
    private MapVisualization map;
    private SystemState systemState;

    @Override
    protected void setUp() throws Exception {
        geometry = makeGeometry();

        // Create 10x10 triangular lattice.
        ColorManager colorManager = new DefaultColorManager();

        // Create a 10 x 10 hexagonal map.
        MapState mapState = new MapState(colorManager, 50.0);

        // Create highlight manager.
        highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);

        // Channel 0 has a small glyph.
        Glyph small = makeGlyph();
        highlightManager.setGlyph(0, small);

        // Create map visualization.
        map = new MapVisualization(mapState);
        map.init(geometry);

        // Create system state
        systemState = makeSystemState();

    }

    protected abstract Glyph makeGlyph();

    protected void populateStateAndFitness(LightweightSystemState systemState) {
        int n = makeGeometry().getCanonicalSites().length;
        double[] fitness = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            fitness[i] = 0;
            state[i] = 0;
        }
        systemState.setFitnessVector(fitness);
        systemState.setStateVector(state);

    }

    private LightweightSystemState makeSystemState() {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(geometry.getCanonicalSites());


        LightweightSystemState ret = new LightweightSystemState(deindexer);
        populateStateAndFitness(ret);
        Set<Coordinate> highlights = new HashSet<>();
        for (Coordinate c : geometry.getCanonicalSites()) {
            highlights.add(c);
        }
        ret.setHighlights(0, highlights);
        ret.setTime(0.0);
        ret.setFrame(0);

        return ret;
    }

    protected Geometry makeGeometry() {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 10, 10);
        Boundary boundary = new Absorbing(shape, lattice);
        Geometry geometry = new Geometry(lattice, shape, boundary);
        return geometry;
    }

    public void testOverlay() throws Exception {
        // Render the frame.
        BufferedImage result = map.render(systemState);

        File file = new File(outputPath + getFileName());
        ImageIO.write(result, "png", file);

        assertBinaryFilesEqual("glyphs/" + getFileName(), getFileName());
    }

    protected abstract String getFileName();
}
