package io.serialize;

import junit.framework.TestCase;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 1/17/14.
 */
public class SerializationFactoryTest extends EslimeTestCase {

    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);
    }

    private void doTest(String elementName, Class expected) {
        Element e = new BaseElement(elementName);
        Serializer result = SerializationFactory.instantiate(e, p);
        Class actual = result.getClass();
        assertEquals(expected, actual);
    }

    public void testCellStateWriter() {
        doTest("cell-state-writer", CellStateWriter.class);
    }

    public void testFixationTimeWriter() {
        doTest("fixation-time", FixationTimeWriter.class);
    }

    public void testParameterWriter() {
        doTest("parameter-writer", ParameterWriter.class);
    }

    public void testFrequencyWriter() {
        doTest("progress-reporter", ProgressReporter.class);
    }

    public void testIntervalWriter() {
        doTest("frequency-writer", FrequencyWriter.class);

    }

    public void testCoordinateIndexer() {
        doTest("interval-writer", IntervalWriter.class);
    }

    public void testContinuumStateWriter() {
        doTest("continuum-state-writer", ContinuumStateWriter.class);
    }
}
