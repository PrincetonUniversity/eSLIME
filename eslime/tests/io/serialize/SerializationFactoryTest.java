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

import io.factory.SerializationFactory;
import io.serialize.binary.ContinuumStateWriter;
import io.serialize.binary.HighlightWriter;
import io.serialize.binary.TimeWriter;
import io.serialize.binary.VisualizationSerializer;
import io.serialize.interactive.ProgressReporter;
import io.serialize.text.*;
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

    public void testContinuumStateWriter() {
        doTest("continuum-state-writer", ContinuumStateWriter.class);
    }

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

        Serializer actual = SerializationFactory.instantiate(e, p);
        Serializer expected = new HighlightWriter(p, new int[] {3, 5});
        assertEquals(expected, actual);
    }

    public void testVisualizationSerializer() {
        Element e = new BaseElement("visualization-serializer");
        Element vp = new BaseElement("visualization");
        Element m = new BaseElement("class");
        m.setText("mock");
        vp.add(m);
        e.add(vp);
        Serializer result = SerializationFactory.instantiate(e, p);
        Class actual = result.getClass();
        assertEquals(VisualizationSerializer.class, actual);
    }

}
