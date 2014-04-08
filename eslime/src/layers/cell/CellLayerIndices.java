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

package layers.cell;

import cells.Cell;
import control.identifiers.Coordinate;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Direct representation of cell layer indices. Only the cell layer and
 * its content object should have direct access to this object. Processes
 * should use a CellLayerViewer.
 *
 * @author David Bruce Borenstein
 * @test CellLayerIndicesTest
 */
public class CellLayerIndices {

    // Coordinate sets. We often need to know all the sites that
    // have a particular property so we can iterate over them,
    // so we track them as sets.

    // Occupied sites (non-vacant sites)
    protected CellIndex occupiedSites;

    // Divisible sites
    protected CellIndex divisibleSites;

    // Map that tracks count of cells with each state
    protected HashMap<Integer, Integer> stateMap;

    public CellLocationIndex getCellLocationIndex() {
        return cellLocationIndex;
    }

    protected CellLocationIndex cellLocationIndex;

    // IdentityHashMap resolves the actual memory address of the
    // key, ie, using == instead of equals(...). This way, no matter
    // how equality is defined for cells, the cell-->coordinate map
    // will uniquely map cells to a location.
    protected IdentityHashMap<Cell, Coordinate> cellToCoord;

    public CellLayerIndices() {
        occupiedSites = new CellIndex();
        divisibleSites = new CellIndex();
        stateMap = new HashMap<>();
        cellLocationIndex = new CellLocationIndex();
    }

    public boolean isOccupied(Coordinate cell) {
        Coordinate c = cell.canonicalize();
        if (occupiedSites.contains(c)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDivisible(Coordinate cell) {
        Coordinate c = cell.canonicalize();
        if (divisibleSites.contains(c)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a vector containing the canonical coordinate of each
     * active site on the lattice.
     *
     * @return
     */
    public CellIndex getOccupiedSites() {
        return occupiedSites;
    }

    /**
     * Returns a list of divisible sites on the lattice.
     *
     * @return
     */
    public CellIndex getDivisibleSites() {
        return divisibleSites;
    }

    public void incrStateCount(Cell cell) {
        Integer currentState = cell.getState();

        if (!stateMap.containsKey(currentState)) {
            stateMap.put(currentState, 0);
        }

        Integer currentCount = stateMap.get(currentState);
        stateMap.put(currentState, currentCount + 1);
    }

    public void decrStateCount(Cell cell) {
        Integer currentState = cell.getState();
        Integer currentCount = stateMap.get(currentState);
        stateMap.put(currentState, currentCount - 1);
    }

    public Map<Integer, Integer> getStateMap() {
        return stateMap;
    }
}
