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

package io.serialize.text;

import control.GeneralParameters;
import control.halt.FixationEvent;
import control.halt.HaltCondition;
import control.halt.StepMaxReachedEvent;
import io.serialize.Serializer;
import processes.StepState;

import java.io.BufferedWriter;

public class HaltTimeWriter extends Serializer {

    private static final String FILENAME = "ttf.txt";
    private boolean closed = true;
    private BufferedWriter bw;

    public HaltTimeWriter(GeneralParameters p) {
        super(p);

        // We use the non-instance path because this metric aggregates over
        // all instances.
        String filename = p.getPath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);

        hAppend(bw, new StringBuilder("gillespie\thalt_info\n"));
    }

    @Override
    public void flush(StepState stepState) {

    }

    @Override
    public void close() {
        closed = true;
        hClose(bw);
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        StringBuilder line;
        if (ex.getClass().equals(FixationEvent.class)) {
            FixationEvent fix = (FixationEvent) ex;
            line = fixationLine(fix);
        } else if (ex.getClass().equals(StepMaxReachedEvent.class)) {
            line = maxStepLine(ex);
        } else {
            line = errorLine(ex);
        }

        hAppend(bw, line);

        closed = true;
    }

    private StringBuilder errorLine(HaltCondition ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getGillespie());
        sb.append("\tERR\n");
        return sb;
    }

    private StringBuilder maxStepLine(HaltCondition ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getGillespie());
        sb.append("\tMAX\n");
        return sb;
    }

    private StringBuilder fixationLine(FixationEvent fix) {
        StringBuilder sb = new StringBuilder();
        sb.append(fix.getGillespie());
        sb.append("\t");
        sb.append(fix.getFixationState());
        sb.append("\n");
        return sb;
    }

}
