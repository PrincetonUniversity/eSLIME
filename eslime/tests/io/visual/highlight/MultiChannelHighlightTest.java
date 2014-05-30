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

package io.visual.highlight;

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
import io.visual.glyph.BullseyeGlyph;
import io.visual.glyph.CrosshairsGlyph;
import io.visual.glyph.Glyph;
import io.visual.VisualizationProperties;
import io.visual.map.MapVisualization;
import layers.LightweightSystemState;
import layers.SystemState;
import test.EslimeTestCase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Test to make sure that multi-channel highlighting works as expected.
 * Created by dbborens on 4/3/14.
 */
public class MultiChannelHighlightTest extends EslimeTestCase {

    Geometry geometry;
    private HighlightManager highlightManager;
    private MapVisualization map;
    protected SystemState systemState;

    @Override
    protected void setUp() throws Exception {
        geometry = makeGeometry();

        // Create 10x10 triangular lattice.
        ColorManager colorManager = new DefaultColorManager();

        // Create a 10 x 10 hexagonal map.
        VisualizationProperties mapState = new VisualizationProperties(colorManager, 50.0, 1);

        // Create highlight manager.
        highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);

        // Channel 0 has a small glyph.
        Glyph bullseye = new BullseyeGlyph(Color.DARK_GRAY, Color.WHITE, 0.5);
        highlightManager.setGlyph(0, bullseye);

        Glyph crosshairs = new CrosshairsGlyph(Color.PINK, 0.3, 1.2);
        highlightManager.setGlyph(1, crosshairs);

        // Create map visualization.
        map = new MapVisualization(mapState);
        map.init(geometry, null, null);

        // Create system state
        systemState = makeSystemState(geometry);

    }

    protected void populateStateAndHealth(Geometry geom, LightweightSystemState systemState) {
        int n = geom.getCanonicalSites().length;
        double[] health = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            health[i] = 0;
            state[i] = 0;
        }
        systemState.setHealthVector(health);
        systemState.setStateVector(state);

    }

    protected LightweightSystemState makeSystemState(Geometry geom) {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(geom.getCanonicalSites());


        LightweightSystemState ret = new LightweightSystemState(deindexer);
        populateStateAndHealth(geom, ret);

        Set<Coordinate> highlights0 = new HashSet<>();
        Set<Coordinate> highlights1 = new HashSet<>();

        Coordinate[] cc = geom.getCanonicalSites();
        Coordinate a = cc[0];
        Coordinate b = cc[1];
        Coordinate c = cc[2];

        highlights0.add(a);
        highlights0.add(b);

        highlights1.add(b);
        highlights1.add(c);

        ret.setHighlights(0, highlights0);
        ret.setHighlights(1, highlights1);

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


    protected String getFileName() {
        return "multipleGlyphs.png";
    }
}

