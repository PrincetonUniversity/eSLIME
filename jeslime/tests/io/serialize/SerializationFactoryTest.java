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
