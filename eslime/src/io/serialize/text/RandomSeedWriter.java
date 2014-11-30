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
public class RandomSeedWriter extends Serializer {

    private static final String FILENAME = "random.txt";


    public RandomSeedWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);
    }

    @Override
    public void init() {
        super.init();
        String filename = p.getInstancePath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        BufferedWriter bw = makeBufferedWriter(filename);
        long seed = p.getRandomSeed();
        StringBuilder sb = new StringBuilder();
        sb.append(seed);
        hAppend(bw, sb);
        hClose(bw);
    }

    @Override
    public void flush(StepState stepState) {
    }

    public void dispatchHalt(HaltCondition ex) {
        closed = true;
    }
    public void close() {
        // Doesn't do anything.
    }
}
