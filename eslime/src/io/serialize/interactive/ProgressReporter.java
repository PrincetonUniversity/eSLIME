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

package io.serialize.interactive;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;

/**
 * Outputs some basic information about simulation progress.
 * Without this (or similar), eSLIME does not write to stdout.
 * Note that flush(...) reports time since last flush(...), not
 * the time since the last cycle!
 *
 * @author David Bruce Borenstein
 * @untested
 */
public class ProgressReporter extends Serializer {

    private long projectStart;
    private long instanceStart;
    private long cycleStart;

    public ProgressReporter(GeneralParameters p) {
        super(p);
        projectStart = System.currentTimeMillis();
    }

    public void init(LayerManager lm) {
        instanceStart = System.currentTimeMillis();
        cycleStart = System.currentTimeMillis();
        System.out.println("Instance " + p.getInstance() + ": " + p.getInstancePath());
        System.out.println("Random key: " + p.getRandomSeed());
    }

    @Override
    public void flush(StepState stepState) {
            long cycleTime = System.currentTimeMillis() - cycleStart;
            System.out.println("   Frame " + stepState.getFrame() + " (" + cycleTime + "ms).");
            cycleStart = System.currentTimeMillis();
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        System.out.println("Simulation ended. Cause: " + ex.getClass().getSimpleName());
        long instanceTime = (System.currentTimeMillis() - instanceStart) / 1000;
        System.out.print("Instance running time: " + instanceTime + " seconds.");
    }

    @Override
    public void close() {
        System.out.println(" " + p.getBasePath());
        System.out.println("Project concluded.");

        long totalTime = (System.currentTimeMillis() - projectStart) / 1000;

        System.out.println("=================================================");
        System.out.println("Total running time: " + totalTime + " seconds.");
        System.out.println("=================================================");
    }

}
