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
public class IndividualHaltWriter extends Serializer {

    private static final String FILENAME = "halt.txt";

    private BufferedWriter bw;

    public IndividualHaltWriter(GeneralParameters p) {
        super(p);
    }

    @Override
    public void init(LayerManager lm) {
        super.init(lm);
        String filename = p.getInstancePath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);
    }

    @Override
    public void flush(StepState stepState) {
    }

    public void dispatchHalt(HaltCondition ex) {
        StringBuilder line = new StringBuilder();
        line.append(ex.getGillespie());
        line.append("\t");
        line.append(ex.toString());
        line.append("\n");
        hAppend(bw, line);
        hClose(bw);
        closed = true;
    }
    public void close() {
        // Doesn't do anything.
    }
}
