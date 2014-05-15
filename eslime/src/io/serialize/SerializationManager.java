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

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.factory.SerializationFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.StepState;

import java.util.ArrayList;
import java.util.List;

/**
 * @untested
 */
public class SerializationManager extends Serializer {

    private List<Serializer> writers;

    public SerializationManager(GeneralParameters p, List<Serializer> writers) {
        super(p);
        this.writers = writers;
    }

    /**
     * Opens handles / initializes data structures for a new instance.
     * Blows up if these were left open from the previous instance.
     */
    public void init(LayerManager lm) {
        for (Serializer tw : writers) {
            tw.init(lm);
        }
    }

    public void flush(StepState stepState) {
        if (stepState.isRecorded()) {
            for (Serializer tw : writers) {
                tw.flush(stepState);
            }
        }
    }
    /**
     * Conclude the entire simulation project.
     */
    public void close() {
        for (Serializer tw : writers) {
            tw.close();
        }
    }

    public void dispatchHalt(HaltCondition ex) {
        for (Serializer tw : writers) {
            tw.dispatchHalt(ex);
        }
    }
}
