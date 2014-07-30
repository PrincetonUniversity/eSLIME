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

public class HaltTimeWriterTest extends EslimeLatticeTestCase {
    public void testLifeCycle() {
        GeneralParameters p = makeMockGeneralParameters();
        HaltTimeWriter writer = new HaltTimeWriter(p);
        for (double t = 0; t < 10.0; t += 1.0) {
            runCycle(writer, t);
        }
        writer.close();
        assertFilesEqual("serializations/tth.txt", "tth.txt");
    }

    private void runCycle(Serializer writer, double time) {
        writer.init(layerManager);
        StepState state = new StepState(time, (int) Math.round(time));
        state.record(cellLayer);
        writer.flush(state);

        ManualHaltEvent haltEvent = createHaltEvent(time);
        writer.dispatchHalt(haltEvent);
    }

    private ManualHaltEvent createHaltEvent(double time) {
        return new ManualHaltEvent(time, "TestSuccessful" + Math.round(time));
    }

}