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

package io.serialize;

import geometry.MockGeometry;
import io.serialize.binary.ContinuumStateWriter;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import processes.StepState;
import structural.MockGeneralParameters;
import structural.postprocess.SolutionViewer;
import test.EslimeTestCase;

/**
 * Created by dbborens on 12/11/13.
 */
public class ContinuumStateWriterTest extends EslimeTestCase {
    private ContinuumStateWriter csw;

    private MockGeometry geom;
    private MockGeneralParameters p;
    private MockSoluteLayer layer;
    private MockLayerManager lm;

    public void setUp() throws Exception {
        // Construct mock objects
        geom = buildMockGeometry();

        p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);

        layer = new MockSoluteLayer();

        // ID assignment is handled by the layer manager in the non-mock solute layer
        layer.setId("42");
        layer.setGeometry(geom);
        lm = new MockLayerManager();
        lm.addSoluteLayer("42", layer);
        csw = new ContinuumStateWriter(p);
    }

    /**
     * Functional test -- create a ContinuumStateWriter and
     * compare its output to a fixture.
     */
    public void testLifeCycle() {
        // Invoke the continuum state writer
        generateOutput();

        // Compare data file
        String dataFilename = "solute" + layer.getId() + ".state.bin";
        assertBinaryFilesEqual(dataFilename);

        // Compare metadata file
        String mdFilename = "solute" + layer.getId() + ".metadata.txt";
        assertFilesEqual(mdFilename);
    }

    private void generateOutput() {
        // Initialize csw files
        csw.init(lm);

        // Fabricate states
        SolutionViewer first = makeFirstVector();
        SolutionViewer second = makeSecondVector();

        // Push first state
        layer.push(first);
        csw.cycleStart(new StepState(1.0), 1);
        csw.cycleEnd(new StepState(1.0), 1);

        // Push second state
        layer.push(second);
        csw.cycleStart(new StepState(2.0), 2);
        csw.cycleEnd(new StepState(2.0), 2);

        // Close file handles
        csw.dispatchHalt(null);
    }

    private SolutionViewer makeFirstVector() {
        double[] values = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};

        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
            vec.set(i, values[i]);
        }

        SolutionViewer ret = new SolutionViewer(vec, geom);
        return ret;
    }

    private SolutionViewer makeSecondVector() {
        double[] values = new double[]{0.0, 0.1, 0.2, 0.3, 0.4};

        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
            vec.set(i, values[i]);
        }

        SolutionViewer ret = new SolutionViewer(vec, geom);
        return ret;
    }
}
