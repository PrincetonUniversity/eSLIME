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

import cells.Cell;
import layers.cell.CellIndex;
import layers.cell.CellLayerIndices;
import structural.identifiers.Coordinate;

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
			divisible.remove(k);	}
	
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
	
	public void decrStateCount(Cell cell) {
		if (!stateMap.containsKey(cell.getState())) {
			stateMap.put(cell.getState(), 0);
		}
		
		super.decrStateCount(cell);
	}
}
