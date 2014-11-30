/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;

import java.io.BufferedWriter;

/**
 * Writes out the number of each "state" as a function of time.
 *
 * @author dbborens
 */
public class RunningTimeWriter extends Serializer {

    private static final String FILENAME = "runtime.txt";
    private double prevTime;

    private BufferedWriter bw;

    public RunningTimeWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
    }

    @Override
    public void init() {
        super.init();
        String filename = p.getInstancePath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);
        prevTime = System.currentTimeMillis();
    }

    @Override
    public void flush(StepState stepState) {
        double curTime = System.currentTimeMillis();

        double dt = curTime - prevTime;

        StringBuilder sb = new StringBuilder();
        sb.append(stepState.getFrame());
        sb.append("\t");
        sb.append(dt);
        sb.append("\n");
        hAppend(bw, sb);

        prevTime = curTime;
    }

    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

    private void conclude() {
        hClose(bw);
    }

    public void close() {
        // Doesn't do anything.
    }
}
