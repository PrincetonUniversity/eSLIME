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

package io.deserialize;

import geometry.MockGeometry;
import io.serialize.binary.ContinuumStateWriter;
import layers.ContinuumState;
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
        Map<String, ContinuumState> actual = query.next();
        assertEquals(2, actual.size());
        assertTrue(actual.containsKey("42"));

        // 43 is the reverse of 42
        double[] expectedValues = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};

        DenseVector actual42 = actual.get("42").getData();
        DenseVector actual43 = actual.get("43").getData();

        for (int i = 0; i < 5; i++) {
            double expected = expectedValues[i];
            assertEquals(expected, actual42.get(i));
            assertEquals(expected, actual43.get(4 - i));
        }
    }

    public void testPopulate() throws Exception {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(cc);
        LightweightSystemState state = new LightweightSystemState(deindexer);
        query.populate(state);

        // Origin corresponds to an index of 0.
        assertEquals(1.0, state.getValue("42", origin), epsilon);
        assertEquals(5.0, state.getValue("43", origin), epsilon);
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
        ContinuumStateWriter csw = new ContinuumStateWriter(p);
        csw.init(lm);
        double[] values = new double[]{5.0, 4.0, 3.0, 2.0, 1.0};
        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
            vec.set(i, values[i]);
        }

        SolutionViewer soln = new SolutionViewer(vec, geom);
        layer.push(soln);
        csw.record(new MockStepState(1.0, 1, null));
        csw.dispatchHalt(null);
    }
}
