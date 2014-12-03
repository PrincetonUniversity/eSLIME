/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import geometry.MockGeometry;
import io.serialize.binary.ContinuumStateWriter;
import layers.LightweightSystemState;
import layers.MockLayerManager;
import layers.MockSoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import processes.MockStepState;
import structural.MockGeneralParameters;
import structural.postprocess.SolutionViewer;
import test.EslimeLatticeTestCase;

import java.util.Map;

/**
 * Test for the ContinuumStateReaderManager class.
 *
 * @see io.deserialize.ContinuumStateReaderTest
 * @see io.serialize.ContinuumStateWriterTest
 * <p/>
 * Created by dbborens on 3/28/14.
 */
public class ContinuumStateReaderManagerTest extends EslimeLatticeTestCase {
    private ContinuumStateReaderManager query;
    private String[] ids;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ids = new String[]{"42", "43"};
        query = new ContinuumStateReaderManager(fixturePath, ids);
    }

    public void testNext() throws Exception {
        Map<String, double[]> actual = query.next();
        assertEquals(2, actual.size());
        assertTrue(actual.containsKey("42"));

        // 43 is the reverse of 42
        double[] expected42 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] expected43 = new double[]{5.0, 4.0, 3.0, 2.0, 1.0};
        double[] actual42 = actual.get("42");
        double[] actual43 = actual.get("43");
        assertArraysEqual(expected42, actual42, false);
        assertArraysEqual(expected43, actual43, false);
    }

    public void testPopulate() throws Exception {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(cc);
        LightweightSystemState state = new LightweightSystemState(geom);
        query.populate(state);

        // Origin corresponds to an index of 0.
        double actual = state.getLayerManager().getSoluteLayer("42").getState().getAbsolute(origin);
        assertEquals(1.0, actual, epsilon);

        actual = state.getLayerManager().getSoluteLayer("43").getState().getAbsolute(origin);
        assertEquals(5.0, actual, epsilon);
    }

    /**
     * This code was used once to generate the fixture.
     * Solute42 is generated in ContinuumStateWriterTest.
     */
    @SuppressWarnings("unused")
    private void generateSolute43() {

        MockGeometry geom = buildMockGeometry();

        MockGeneralParameters p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);

        MockSoluteLayer layer = new MockSoluteLayer();

        // ID assignment is handled by the layer manager in the non-mock solute layer
        layer.setId("43");
        layer.setGeometry(geom);
        MockLayerManager lm = new MockLayerManager();
        lm.addSoluteLayer("43", layer);
        ContinuumStateWriter csw = new ContinuumStateWriter(p, lm);
        csw.init();
        double[] values = new double[]{5.0, 4.0, 3.0, 2.0, 1.0};
        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
            vec.set(i, values[i]);
        }

        SolutionViewer soln = new SolutionViewer(vec, geom);
        layer.push(soln);
        csw.flush(new MockStepState(1.0, 1));
        csw.dispatchHalt(null);
    }
}
