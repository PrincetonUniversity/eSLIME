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
import geometry.Geometry;
import structural.identifiers.Coordinate;

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
