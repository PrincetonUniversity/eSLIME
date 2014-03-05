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
import structural.identifiers.Coordinate;
import geometry.Geometry;
import layers.cell.CellLayerContent;
import layers.cell.CellLayerIndices;

public class MockCellLayerContent extends CellLayerContent {

	public MockCellLayerContent(Geometry geom, CellLayerIndices indices) {
		super(geom, indices);
	}

	public Cell get(Coordinate coord) {
		// Mock getter doesn't do any validation
		return map.get(coord);
	}
	
	/* stateVector */
	
	private int[] stateVector;
	
	public void setStateVector(int[] stateVector) {
		this.stateVector = stateVector; 
	}
	
	@Override
	public int[] getStateVector() {
		return stateVector;
	}
	
	/* fitnessVector */
	
	private double[] fitnessVector;
	
	public void setFitnessVector(double[] fitnessVector) {
		this.fitnessVector = fitnessVector; 
	}
	
	@Override
	public double[] getFitnessVector() {
		return fitnessVector;
	}
}
