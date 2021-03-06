/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import control.GeneralParameters;
import control.halt.HaltCondition;
import layers.LayerManager;
import processes.StepState;

import java.util.List;

/**
 * @untested
 */
public class SerializationManager extends Serializer {

    private List<Serializer> writers;

    public SerializationManager(GeneralParameters p, LayerManager layerManager, List<Serializer> writers) {
        super(p, layerManager);
        this.writers = writers;
    }

    /**
     * Opens handles / initializes data structures for a new instance.
     * Blows up if these were left open from the previous instance.
     */
    public void init() {
        for (Serializer tw : writers) {
            tw.init();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializationManager that = (SerializationManager) o;

        if (that.writers.size() != this.writers.size()) {
            return false;
        }

        for (int i = 0; i < writers.size(); i++) {
            if (!writers.get(i).equals(that.writers.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return writers != null ? writers.hashCode() : 0;
    }
}
