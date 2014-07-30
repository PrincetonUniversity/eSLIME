/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.kymograph;

import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.shape.Line;
import geometry.shape.Shape;
import io.deserialize.MockCoordinateDeindexer;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.highlight.HighlightManager;
import layers.LightweightSystemState;
import test.EslimeTestCase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class KymographTest extends EslimeTestCase {

    public void testWithOutline() throws Exception {
        doTest(1, "KymographOutline.png");
    }

    public void testNoOutline() throws Exception {
        doTest(0, "KymographNoOutline.png");
    }

    private void doTest(int outline, String filename) throws Exception {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 5);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        ColorManager colorManager = new DefaultColorManager();
        VisualizationProperties mapState = new VisualizationProperties(colorManager, 25.0, outline);
        HighlightManager highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);
        Kymograph map = new Kymograph(mapState);

        double[] times = new double[] {0.0};
        int[] frames = new int[] {0};
        map.init(geom, times, frames);
        LightweightSystemState systemState = makeSystemState(geom);

        BufferedImage result = map.render(systemState);
        File file = new File(outputPath + filename);
        ImageIO.write(result, "png", file);

        assertBinaryFilesEqual("glyphs/" + filename, filename);

    }

    protected LightweightSystemState makeSystemState(Geometry geom) {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(geom.getCanonicalSites());


        LightweightSystemState ret = new LightweightSystemState(geom);
        populateStateAndHealth(geom, ret);
        Set<Coordinate> highlights = new HashSet<>();
        for (Coordinate c : geom.getCanonicalSites()) {
            highlights.add(c);
        }
        ret.setHighlights(0, highlights);
        ret.setTime(0.0);
        ret.setFrame(0);

        return ret;
    }

    protected void populateStateAndHealth(Geometry geom, LightweightSystemState systemState) {
        int n = geom.getCanonicalSites().length;
        double[] health = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            health[i] = 0.0;
            state[i] = i % 3;
        }
        systemState.initCellLayer(state, health);

    }
}