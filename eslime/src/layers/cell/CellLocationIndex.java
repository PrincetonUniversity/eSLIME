/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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

    @Override
    public Coordinate remove(Object key) {
        return super.remove(key);
    }

    public boolean isIndexed(Cell cell) {
        return containsKey(cell);
    }

}
