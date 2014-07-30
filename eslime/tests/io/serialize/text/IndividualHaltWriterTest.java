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

package io.serialize.text;

import control.GeneralParameters;
import control.halt.ManualHaltEvent;
import io.serialize.Serializer;
import processes.StepState;
import test.EslimeLatticeTestCase;

public class IndividualHaltWriterTest extends EslimeLatticeTestCase {
    public void testLifeCycle() {
        GeneralParameters p = makeMockGeneralParameters();
        IndividualHaltWriter writer = new IndividualHaltWriter(p);
        runCycle(writer, 0.0);
        assertFilesEqual("serializations/halt.txt", "halt.txt");
    }

    private void runCycle(Serializer writer, double time) {
        writer.init(layerManager);
        StepState state = new StepState(time, (int) Math.round(time));
        state.record(cellLayer);
        writer.flush(state);

        ManualHaltEvent haltEvent = createHaltEvent(time);
        writer.dispatchHalt(haltEvent);
        writer.close();
    }

    private ManualHaltEvent createHaltEvent(double time) {
        return new ManualHaltEvent(time, "TestSuccessful" + Math.round(time));
    }
}