package io.serialize;

import test.EslimeTestCase;
import structural.MockGeneralParameters;
import geometry.MockGeometry;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;

/**
 * Created by dbborens on 12/11/13.
 */
public class ContinuumStateWriterTest extends EslimeTestCase {
    private ContinuumStateWriter csw;

    private MockGeometry geom;
    private MockGeneralParameters p;
    private SoluteLayer layer;

    public void setUp() throws Exception {
        // Construct mock objects
        geom = buildMockGeometry();

        p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);

        layer = new SoluteLayer(geom, "42");
        csw = new ContinuumStateWriter(geom ,p);
    }

    /**
     * Functional test -- create a ContinuumStateWriter and
     * compare its output to a fixture.
     *
     */
    public void testLifeCycle() {
        // Invoke the continuum state writer
        generateOutput();

        // Compare data file
        String dataFilename = "solute" + layer.getId() + ".state.txt";
        assertBinaryFilesEqual(dataFilename);

        // Compare metadata file
        String mdFilename = "solute" + layer.getId() + ".metadata.txt";
        assertFilesEqual(mdFilename);
    }

    private void generateOutput() {
        // Initialize csw files
        csw.init(layer);

        // Fabricate states
        DenseVector first = makeFirstVector();
        DenseVector second = makeSecondVector();

        // Push first state
        layer.push(first, 1.0);
        csw.step(1.0, 1);

        // Push second state
        layer.push(second, 2.0);
        csw.step(2, 2);

        // Close file handles
        csw.dispatchHalt(null);
    }

    private DenseVector makeFirstVector() {
        double[] values = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};

        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
            vec.set(i, values[i]);
        }

        return vec;
    }

    private DenseVector makeSecondVector() {
        double[] values = new double[] {0.0, 0.1, 0.2, 0.3, 0.4};

        DenseVector vec = new DenseVector(5);
        for (int i = 0; i < 5; i++) {
               vec.set(i, values[i]);
        }

        return vec;
    }
}
