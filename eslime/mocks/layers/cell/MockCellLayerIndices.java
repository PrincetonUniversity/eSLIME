/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import cells.Cell;
import control.identifiers.Coordinate;

public class MockCellLayerIndices extends CellLayerIndices {

    private CellIndex occupied = new CellIndex();
    private CellIndex divisible = new CellIndex();

    public void setOccupied(Coordinate k, Boolean v) {
        if (v)
            occupied.add(k);
        else
            occupied.remove(k);
    }

    public void setDivisible(Coordinate k, Boolean v) {
        if (v)
            divisible.add(k);
        else
            divisible.remove(k);
    }

    public void setOccupiedSites(CellIndex occupied) {
        this.occupied = occupied;
    }

    public void setDivisibleSites(CellIndex divisible) {
        this.divisible = divisible;
    }

    public boolean isOccupied(Coordinate k) {
        return occupied.contains(k);
    }

    public boolean isDivisible(Coordinate k) {
        return divisible.contains(k);
    }

    public CellIndex getOccupiedSites() {
        return occupied;
    }

    public CellIndex getDivisibleSites() {
        return divisible;
    }

    private Coordinate lastCoord;
    private Cell lastPrevious;

    public Coordinate getLastCoord() {
        return lastCoord;
    }

    public Cell getLastPrevious() {
        return lastPrevious;
    }

    public Cell getLastCurrent() {
        return lastCurrent;
    }

    private Cell lastCurrent;

    @Override
    public void refresh(Coordinate coord, Cell previous, Cell current) {
        super.refresh(coord, previous, current);
        lastPrevious = previous;
        lastCurrent = current;
        lastCoord = coord;
    }
}
