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

package io.serialize.binary;

import geometry.MockGeometry;
import io.visual.MockVisualization;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

import java.io.File;

/**
 * As a graphics I/O class, this seemed better suited to an integration
 * test than to a system of unit tests. The fixtures are based on those
 * of the SystemStateReader class, and the output utilizes a base version
 * of the MapVisualization class.
 * <p/>
 * Created by dbborens on 4/2/14.
 */
public class VisualizationSerializerTest extends EslimeTestCase {
    private MockVisualization visualization;
    private VisualizationSerializer query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockLayerManager lm = new MockLayerManager();
        MockGeometry geom = buildMockGeometry();
        CellLayer layer = new CellLayer(geom);
        lm.setCellLayer(layer);
        visualization = new MockVisualization();
        MockGeneralParameters p = new MockGeneralParameters();

        // The class attempts to slurp in data from a simulation, and throws an
        // exception if it isn't there. We use the SystemStateReader data
        // because it exists.
        p.setInstancePath(fixturePath + "SystemStateReader/");
        String prefix = "../../output/test";
        query = new VisualizationSerializer(p, visualization, prefix);
        query.init(lm);
    }

    public void testLifeCycle() {
        query.dispatchHalt(null);
        assertTrue(visualization.isInit());
        assertTrue(visualization.isRender());
        assertTrue(visualization.isConclude());
        checkFileExists("test1.7.png");
        checkFileExists("test4.8.png");
    }

    private void checkFileExists(String fn) {
        File file = new File(outputPath + fn);
        assertTrue(file.exists());
    }
}
