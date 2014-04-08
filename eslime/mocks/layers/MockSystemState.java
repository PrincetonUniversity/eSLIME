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

import control.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by dbborens on 4/2/14.
 */
public class MockSystemState extends SystemState {

    private HashMap<Coordinate, Integer> stateMap;
    private boolean highlighted;

    public MockSystemState() {
        stateMap = new HashMap<>();
    }

    @Override
    public double getFitness(Coordinate coord) {
        return 0;
    }

    public void setState(Coordinate coord, int state) {
        stateMap.put(coord, state);
    }

    @Override
    public int getState(Coordinate coord) {
        return stateMap.get(coord);
    }

    @Override
    public double getValue(String id, Coordinate coord) {
        return 0;
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public int getFrame() {
        return 0;
    }

    public void setHighlighted(boolean value) {
        highlighted = value;
    }

    @Override
    public boolean isHighlighted(int channel, Coordinate coord) {
        return highlighted;
    }

}
