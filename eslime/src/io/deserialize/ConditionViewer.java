/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import control.identifiers.Coordinate;
import structural.utilities.VectorViewer;

import java.util.HashSet;

@Deprecated
public class ConditionViewer {

    private VectorViewer f;
    private int[] states;
    private double gCurrent;
    private int frame;
    private HashSet<Coordinate> highlights;
    private CoordinateDeindexer deindexer;

    public ConditionViewer(VectorViewer f, int[] states, HashSet<Coordinate> highlights,
                           int frame, double gCurrent, CoordinateDeindexer deindexer) {

        this.f = f;
        this.states = states;
        this.gCurrent = gCurrent;
        this.deindexer = deindexer;
        this.frame = frame;
        this.highlights = highlights;
    }

    public int getState(Coordinate c) {
        int i = deindexer.getIndex(c);

        return states[i];
    }

    public double getHealth(Coordinate c) {
        int i = deindexer.getIndex(c);

        return f.get(i);
    }

    public HashSet<Coordinate> getHighlights() {
        return highlights;
    }

    public boolean isVacant(Coordinate c) {
        int i = deindexer.getIndex(c);

        return (states[i] == 0);
    }

    public double getGillespie() {
        return gCurrent;
    }

    public int getFrame() {
        return frame;
    }

    public int[] getStateVector() {
        return states;
    }

    public double[] getHealthVector() {
        return f.getData();
    }
}
