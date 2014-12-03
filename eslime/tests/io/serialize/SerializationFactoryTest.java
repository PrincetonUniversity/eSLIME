/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import factory.io.serialize.SerializationFactory;
import io.serialize.binary.HighlightWriter;
import io.serialize.binary.TimeWriter;
import io.serialize.binary.VisualizationSerializer;
import io.serialize.interactive.ProgressReporter;
import io.serialize.text.*;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import structural.MockGeneralParameters;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 1/17/14.
 */
public class SerializationFactoryTest extends EslimeLatticeTestCase {

    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p = new MockGeneralParameters();
        p.setInstancePath(outputPath);
        p.setPath(outputPath);
    }

    private void doTest(String elementName, Class expected) {
        Element e = new BaseElement(elementName);
        Serializer result = SerializationFactory.instantiate(e, p, layerManager);
        Class actual = result.getClass();
        assertEquals(expected, actual);
    }

    public void testCellStateWriter() {
        doTest("cell-state-writer", LegacyCellStateWriter.class);
    }

    public void testHaltTimeWriter() {
        doTest("halt-time-writer", HaltTimeWriter.class);
    }

    public void testParameterWriter() {
        doTest("parameter-writer", ParameterWriter.class);
    }

    public void testProgressReporter() {
        doTest("progress-reporter", ProgressReporter.class);
    }

    public void testCensusWriter() {
        doTest("census-writer", CensusWriter.class);

    }

    public void testIntervalWriter() {
        doTest("interval-writer", IntervalWriter.class);
    }

//    public void testContinuumStateWriter() {
//        doTest("continuum-state-writer", ContinuumStateWriter.class);
//    }

    public void testTimeWriter() {
        doTest("time-writer", TimeWriter.class);
    }

    public void testCoordinateIndexer() {
        doTest("coordinate-indexer", CoordinateIndexer.class);
    }

    public void testHighlightWriter() {
        Element e = new BaseElement("highlight-writer");
        // Create channels
        Element channels = new BaseElement("channels");
        Element c1 = new BaseElement("channel");
        c1.setText("3");
        Element c2 = new BaseElement("channel");
        c2.setText("5");
        channels.add(c1);
        channels.add(c2);
        e.add(channels);

        Serializer actual = SerializationFactory.instantiate(e, p, layerManager);
        Serializer expected = new HighlightWriter(p, new int[]{3, 5}, layerManager);
        assertEquals(expected, actual);
    }

    public void testVisualizationSerializer() {
        Element e = new BaseElement("visualization-serializer");
        Element vp = new BaseElement("visualization");
        Element m = new BaseElement("class");
        m.setText("mock");
        vp.add(m);
        e.add(vp);
        Serializer result = SerializationFactory.instantiate(e, p, layerManager);
        Class actual = result.getClass();
        assertEquals(VisualizationSerializer.class, actual);
    }

    public void testIndividualHaltWriter() {
        doTest("individual-halt-writer", IndividualHaltWriter.class);
    }

    public void testRunningTimeWriter() {
        doTest("running-time-writer", RunningTimeWriter.class);
    }

    public void testRandomSeedWriter() {
        doTest("random-seed-writer", RandomSeedWriter.class);
    }

    public void testSurfaceCensusWriter() {
        doTest("surface-census-writer", SurfaceCensusWriter.class);

    }

}
