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
import structural.identifiers.Coordinate;

import java.util.IdentityHashMap;

/**
 * Class for associating cells to coordinates, with
 * cells identified by reference (==) rather than
 * object equality.
 * <p/>
 * Created by David B Borenstein on 2/5/14.
 */
public class CellLocationIndex {

    protected IdentityHashMap<Cell, Coordinate> locationMap;

    public CellLocationIndex() {
        locationMap = new IdentityHashMap<>();
    }

    public void place(Cell cell, Coordinate coordinate) {
        if (locationMap.containsKey(cell)) {
            throw new IllegalStateException("Attempting to overwrite existing location key.");
        }

        locationMap.put(cell, coordinate);
    }

    public void move(Cell cell, Coordinate coordinate) {
        if (!locationMap.containsKey(cell)) {
            throw new IllegalStateException("Attempting to move a cell that does not have an indexed spatial location.");
        }

        remove(cell);
        place(cell, coordinate);
    }

    public void remove(Cell cell) {
        if (!locationMap.containsKey(cell)) {
            throw new IllegalStateException("Attempting to remove a cell that does not have an indexed spatial location.");
        }

        locationMap.remove(cell);
    }

    public Coordinate locate(Cell cell) {
        if (!locationMap.containsKey(cell)) {
            throw new IllegalStateException("Attempting to locate a cell that does not have an indexed spatial location.");
        }

        return locationMap.get(cell);

    }

    public boolean isIndexed(Cell cell) {
        return locationMap.containsKey(cell);
    }
}
