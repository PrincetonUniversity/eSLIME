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

package processes;

import structural.identifiers.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * State object for a cycle in a simulation. Each
 * process can modify this state object as necessary.
 * <p/>
 * When the processes are done, information from this
 * state object is passed to appropriate downstream
 * elements.
 *
 * @author dbborens
 */
public class StepState {

    private Set<Coordinate> highlights = new HashSet<Coordinate>();
    private double dt = 0D;
    private double time;
    private boolean closed = false;


    public void stepState(double time) {
        this.time = time;
    }

    public void highlight(Coordinate c) {
        if (closed)
            throw new IllegalStateException("Consistency failure in StepState object");

        highlights.add(c);
    }

    public void advanceClock(double time) {
        if (closed)
            throw new IllegalStateException("Consistency failure in StepState object");

        dt += time;
    }

    public void close() {
        closed = true;
    }

    public double getDt() {
        if (!closed) {
            throw new IllegalStateException("Attempted to access state before it was finalized.");
        }

        return dt;
    }

    public Coordinate[] getHighlights() {
        if (!closed) {
            throw new IllegalStateException("Attempted to access state before it was finalized.");
        }

        return highlights.toArray(new Coordinate[0]);
    }

    /**
     * Gets the system time as of the start of the cycle. Does not factor
     * in the amount of time that has elapsed since the start of the
     * cycle (dt).
     *
     * @return
     */
    public double getTime() {
        return time;
    }
}
