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

package processes.discrete.filter;

import cells.Cell;
import control.arguments.Argument;
import control.identifiers.Coordinate;
import layers.cell.CellLayer;
import layers.cell.CellLayerViewer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dbborens on 5/5/14.
 */
public class CellStateFilter extends Filter {

    private CellLayer layer;

    private Argument<Integer> toChoose;

    /**
     *
     * @param toChoose The cell state to retain. If random, a value will be
     *               chosen each time the filter is applied.
     */
    public CellStateFilter(CellLayer layer, Argument<Integer> toChoose) {
        this.toChoose = toChoose;
        this.layer = layer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellStateFilter that = (CellStateFilter) o;

        if (toChoose != null ? !toChoose.equals(that.toChoose) : that.toChoose != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = layer != null ? layer.hashCode() : 0;
        result = 31 * result + (toChoose != null ? toChoose.hashCode() : 0);
        return result;
    }

    @Override
    public Collection<Coordinate> apply(Collection<Coordinate> toFilter) {
        ArrayList<Coordinate> toRetain = getRetained(toFilter);
        return toRetain;
    }

    private ArrayList<Coordinate> getRetained(Collection<Coordinate> toFilter) {
        int chosen = toChoose.next();
        ArrayList<Coordinate> toRetain = new ArrayList<>();
        CellLayerViewer viewer = layer.getViewer();
        for (Coordinate c : toFilter) {
            if (!viewer.isOccupied(c)) {
                continue;
            }

            Cell cell = layer.getViewer().getCell(c);

            if (cell.getState() == chosen) {
                toRetain.add(c);
            }
        }

        return toRetain;
    }
}
