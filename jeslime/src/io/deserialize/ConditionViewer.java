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

package io.deserialize;

import structural.VectorViewer;
import structural.identifiers.Coordinate;

import java.util.HashSet;

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

    public double getFitness(Coordinate c) {
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

}
