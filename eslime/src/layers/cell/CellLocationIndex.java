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

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Class for associating cells to coordinates, with
 * cells identified by reference (==) rather than
 * object equality.
 * <p/>
 * Created by David B Borenstein on 2/5/14.
 */
public class CellLocationIndex extends IdentityHashMap<Cell, Coordinate> {


    public CellLocationIndex() {
        super();
    }

    public void add(Cell cell, Coordinate coordinate) {
        if (containsKey(cell)) {
            throw new IllegalStateException("Attempting to overwrite existing location key.");
        }

        put(cell, coordinate);
    }

    public Coordinate locate(Cell cell) {
        if (!containsKey(cell)) {
            throw new IllegalStateException("Attempting to locate a cell that does not have an indexed spatial location.");
        }

        return get(cell);

    }

    public boolean isIndexed(Cell cell) {
        return containsKey(cell);
    }

}
