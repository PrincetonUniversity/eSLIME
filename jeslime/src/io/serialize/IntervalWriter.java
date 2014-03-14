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

import layers.LayerManager;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IntervalWriter extends Serializer {

    // I/O handle for the interval file (What changed at each time step, and how long it took)
    private BufferedWriter intervalWriter;
    private final String INTERVAL_FILENAME = "interval.txt";

    private long prevTime;

    public IntervalWriter(GeneralParameters p) {
        super(p);


    }

    public void init(LayerManager lm) {
        String intervalFileStr = p.getInstancePath() + '/' + INTERVAL_FILENAME;

        try {
            File intervalFile = new File(intervalFileStr);
            FileWriter ifw = new FileWriter(intervalFile);
            intervalWriter = new BufferedWriter(ifw, 1048576);
            intervalWriter.append("Step,Gillespie,Running time\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        prevTime = System.currentTimeMillis();
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        Long interval = System.currentTimeMillis() - prevTime;
        if (p.isFrame(frame)) {
            interval(frame, gillespie, interval);
        }

        prevTime = System.currentTimeMillis();
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        try {
            intervalWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wall clock time and simulation time for last time step.
     */
    private void interval(int n, double gillespie, long interval) {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(',');
        sb.append(gillespie);
        sb.append(',');
        sb.append(interval);
        sb.append('\n');
        try {
            intervalWriter.append(sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }
}
