/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import cells.Cell;
import control.identifiers.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * @author David Bruce Borenstein
 * @tested CellLayerViewerTest
 */
public class CellLayerViewer {

    private CellLayerContent content;

    public CellLayerViewer(CellLayerContent content) {
        this.content = content;
    }

    /**
     * Returns a vector containing the canonical coordinate of each
     * active site on the lattice.
     *
     * @return
     */
    public HashSet<Coordinate> getOccupiedSites() {
        // Construct a copy of internal state
        HashSet<Coordinate> res = new HashSet<>(content.getOccupiedSites());

        // Return it
        return res;
    }

    /**
     * Returns a list of divisible sites on the lattice.
     *
     * @return
     */
    public HashSet<Coordinate> getDivisibleSites() {
        // Construct a copy of internal state
        HashSet<Coordinate> res = new HashSet<>(content.getDivisibleSites());

        // Return it
        return res;
    }

    public Cell getCell(Coordinate coord) {
        return content.get(coord);
    }

    public int[] getStateVector() {
        return content.getStateVector();
    }

    /**
     * Returns a vector of health values, in canonical site order.
     *
     * @return
     */
    public double[] getHealthVector() {
        return content.getHealthVector();
    }

    public StateMapViewer getStateMapViewer() {
        return new StateMapViewer(content.getStateMap());
    }

    public boolean isOccupied(Coordinate c) {
        return content.getOccupiedSites().contains(c);
    }

    public boolean isDivisible(Coordinate c) {
        return content.getDivisibleSites().contains(c);
    }

    public boolean exists(Cell cell) {
        return content.isIndexed(cell);
    }

    public Set<Coordinate> getImaginarySites() {
        return content.getImaginarySites();
    }

    /**
     * Returns 0 for vacant cells; otherwise, returns the cell's state.
     * @param coord
     * @return
     */
    public int getState(Coordinate coord) {
        if (!isOccupied(coord)) {
            return 0;
        }

        return getCell(coord).getState();
    }
}
