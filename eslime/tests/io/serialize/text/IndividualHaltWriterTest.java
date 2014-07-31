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
        ManualHaltEvent ret =  new ManualHaltEvent("TestSuccessful" + Math.round(time));
        ret.setGillespie(time);
        return ret;
    }
}