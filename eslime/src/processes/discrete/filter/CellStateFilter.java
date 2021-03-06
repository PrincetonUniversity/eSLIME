/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import cells.Cell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.cell.CellLayer;
import layers.cell.CellLayerViewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public List<Coordinate> apply(List<Coordinate> toFilter) {
        ArrayList<Coordinate> toRetain = getRetained(toFilter);
        return toRetain;
    }

    private ArrayList<Coordinate> getRetained(Collection<Coordinate> toFilter) {
        int chosen;
        try {
            chosen = toChoose.next();
        } catch (HaltCondition ex) {
            throw new IllegalStateException(ex);
        }

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
