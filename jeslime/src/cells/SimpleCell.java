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

package cells;

import structural.identifiers.Coordinate;

/**
 * class SimpleCell
 * <p/>
 * As its name suggests, a simple cell is the minimal implementation of the
 * cell class. It does not interact with any solute field. The cell state field can be used
 * to differentiate between genotypes.
 *
 * @author David Bruce Borenstein
 * @test SimpleCellTest
 */
public class SimpleCell extends Cell {

    private int considerCount;

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        throw new UnsupportedOperationException("Behaviors are not supported in the SimpleCell class.");
    }

    @Override
    public void die() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public SimpleCell(int state) {

        setState(state);
        setFitness(0D);
        setDivisible(true);

        considerCount = 0;
    }

    @Override
    public int consider() {
        considerCount++;
        return considerCount;
    }

    public void apply() {
        considerCount = 0;
    }

    public SimpleCell divide() {
        // Make a copy of this SimpleCell
        SimpleCell child = new SimpleCell(getState());

        // Return it
        return child;
    }

    @Override
    public void adjustFitness(double delta) {
        throw new UnsupportedOperationException("SimpleCell does not use a nutrient metric.");
    }

    @Override
    public SimpleCell clone(int childState) {
        SimpleCell child = new SimpleCell(childState);
        child.considerCount = considerCount;
        return child;
    }

    @Override
    public double getProduction(String solute) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleCell)) {
            return false;
        }

        SimpleCell other = (SimpleCell) obj;

        if (other.getState() != getState()) {
            return false;
        }

        return true;
    }
}
