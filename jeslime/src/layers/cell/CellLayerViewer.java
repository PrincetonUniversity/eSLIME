/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package layers.cell;

import java.util.HashSet;

import cells.Cell;
import structural.identifiers.Coordinate;

/**
 * 
 * @tested CellLayerViewerTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayerViewer {
	
	private CellLayer layer;
	private CellLayerContent content;
	private CellLayerIndices indices;

	public CellLayerViewer (CellLayer layer, CellLayerContent content, CellLayerIndices indices) {
		this.layer = layer;
		this.content = content;
		this.indices = indices;
	}
	
	/**
	 * Returns a vector containing the canonical coordinate of each
	 * active site on the lattice.
	 * @return
	 */
	public HashSet<Coordinate> getOccupiedSites() {
		// Construct a copy of internal state
		HashSet<Coordinate> res = new HashSet<Coordinate>(indices.getOccupiedSites());

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
		HashSet<Coordinate> res = new HashSet<Coordinate>(indices.getDivisibleSites());

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
		return new StateMapViewer(indices.getStateMap());
	}

	public boolean isOccupied(Coordinate c) {
		return indices.isOccupied(c);
	}
	
	public boolean isDivisible(Coordinate c) {
		return indices.isDivisible(c);
	}

    public boolean exists(Cell cell) {
        return indices.getCellLocationIndex().isIndexed(cell);
    }
}
