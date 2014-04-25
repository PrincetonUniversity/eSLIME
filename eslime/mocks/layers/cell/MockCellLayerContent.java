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
import geometry.Geometry;

import java.util.HashSet;
import java.util.Set;

public class MockCellLayerContent extends CellLayerContent {

    private Set<Coordinate> imaginarySites;
    private int[] stateVector;
    private double[] fitnessVector;

    public MockCellLayerContent(Geometry geom, CellLayerIndices indices) {
        super(geom, indices);
    }

    @Override
    public void sanityCheck(Coordinate coord) {

    }

    @Override
    public Set<Coordinate> getImaginarySites() {
        return imaginarySites;
    }

	/* stateVector */

    private void setImaginarySites(Set<Coordinate> imaginarySites) {
        this.imaginarySites = imaginarySites;
    }

    public Cell get(Coordinate coord) {
        // Mock getter doesn't do any validation
        return map.get(coord);
    }

    @Override
    public int[] getStateVector() {
        return stateVector;
    }

	/* fitnessVector */

    public void setStateVector(int[] stateVector) {
        this.stateVector = stateVector;
    }

    @Override
    public double[] getFitnessVector() {
        return fitnessVector;
    }

    public void setFitnessVector(double[] fitnessVector) {
        this.fitnessVector = fitnessVector;
    }

    @Override
    public CellLayerContent clone() {
        MockCellLayerContent clone = new MockCellLayerContent(geom, indices);
        clone.imaginarySites = new HashSet<>(imaginarySites);
        clone.stateVector = stateVector.clone();
        clone.fitnessVector = fitnessVector.clone();
        return clone;
    }
}
