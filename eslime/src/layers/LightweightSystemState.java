/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package layers;

import io.deserialize.CoordinateDeindexer;
import no.uib.cipr.matrix.DenseVector;
import structural.identifiers.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dbborens on 3/26/14.
 */
public class LightweightSystemState extends SystemState {

    private double[] fitnessVector;
    private Map<String, ContinuumState> continuumStates;
    private double time;
    private int frame;
    private Map<Integer, Set<Coordinate>> highlights;
    private CoordinateDeindexer deindexer;
    private int[] stateVector;

    public LightweightSystemState(CoordinateDeindexer deindexer) {
        continuumStates = new HashMap<>();
        highlights = new HashMap<>();
        this.deindexer = deindexer;
    }

    public void setContinuumStates(Map<String, ContinuumState> continuumStates) {
        this.continuumStates = continuumStates;
    }

    public void setHighlights(Integer channelId, Set<Coordinate> sites) {
        highlights.put(channelId, sites);
    }


    public void setFitnessVector(double[] fitnessVector) {
        this.fitnessVector = fitnessVector;
    }

    @Override
    public double getFitness(Coordinate coord) {
        int index = deindexer.getIndex(coord);
        return fitnessVector[index];
    }

    @Override
    public int getState(Coordinate coord) {
        int index = deindexer.getIndex(coord);
        return stateVector[index];
    }

    @Override
    public double getValue(String id, Coordinate coord) {
        int index = deindexer.getIndex(coord);
        ContinuumState state = continuumStates.get(id);
        DenseVector vector = state.getData();
        return vector.get(index);
    }

    @Override
    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    @Override
    public boolean isHighlighted(int channel, Coordinate coord) {
        Set<Coordinate> highlightedSites = highlights.get(channel);
        return highlightedSites.contains(coord);
    }

    public void setStateVector(int[] stateVector) {
        this.stateVector = stateVector;
    }
}
