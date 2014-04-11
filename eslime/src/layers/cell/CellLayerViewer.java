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
     * Returns a vector of fitness values, in canonical site order.
     *
     * @return
     */
    public double[] getFitnessVector() {
        return content.getFitnessVector();
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
}
