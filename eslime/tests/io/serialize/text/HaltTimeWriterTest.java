/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import control.halt.ManualHaltEvent;
import io.serialize.Serializer;
import processes.StepState;
import test.EslimeLatticeTestCase;

public class HaltTimeWriterTest extends EslimeLatticeTestCase {
    public void testLifeCycle() throws Exception {
        GeneralParameters p = makeMockGeneralParameters();
        HaltTimeWriter writer = new HaltTimeWriter(p, layerManager);
        for (double t = 0; t < 10.0; t += 1.0) {
            runCycle(writer, t);
        }
        writer.close();
        assertFilesEqual("serializations/tth.txt", "tth.txt");
    }

    private void runCycle(Serializer writer, double time) {
        writer.init();
        StepState state = new StepState(time, (int) Math.round(time));
        state.record(cellLayer);
        writer.flush(state);

        ManualHaltEvent haltEvent = createHaltEvent(time);
        writer.dispatchHalt(haltEvent);
    }

    private ManualHaltEvent createHaltEvent(double time) {
        ManualHaltEvent ret =  new ManualHaltEvent("TestSuccessful" + Math.round(time));
        ret.setGillespie(time);
        return ret;
    }

}