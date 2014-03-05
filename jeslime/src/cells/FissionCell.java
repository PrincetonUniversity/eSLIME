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

package cells;

import structural.identifiers.Coordinate;

/**
 * FissionCell changes its fitness by delta whenever feed is
 * called. It reports that it is divisible if and only if its
 * fitness exceeds a defined threshold. When it divides, both
 * the parent and the child cell have half the original biomass.
 * Note that they may both still be divisible at this point.
 * 
 * Biomass accumulation (and the resulting changes to divisibility)
 * take place only on calls to "apply."
 * 
 * @author dbborens
 * @test FissionCellTest
 */
public class FissionCell extends Cell {

	private int considerCount;

	private double nextFitness;
	
	private double threshold;

    @Deprecated
	public FissionCell(int state, double initialFitness, double threshold) {
		this.threshold = threshold;
		
		setState(state);
		setFitness(initialFitness);
		checkDivisibility();
		considerCount = 0;
	}

    @Override
    public void die() {
        throw new UnsupportedOperationException();
    }

    public FissionCell clone(int childState) {
		double fitness = getFitness();
		
		FissionCell child = new FissionCell(childState, fitness, threshold);
    	child.considerCount = considerCount;
		child.nextFitness = nextFitness;
		child.setDivisible(isDivisible());
		
		return child;
	}

    @Override
    public double getProduction(String solute) {
        return 0;
    }

    private void checkDivisibility() {
		//System.out.println("   " + getFitness() + " -- " + threshold);
		if (getFitness() > threshold) {
			setDivisible(true);
		} else {
			setDivisible(false);
		}
	}
	
	@Override
	public int consider() {
		considerCount++;
		return considerCount;
	}

	@Override
	public void apply() {
		setFitness(nextFitness);
		checkDivisibility();
		considerCount = 0;
	}

	@Override
	public Cell divide() {
		if (!isDivisible()) {
			throw new IllegalStateException("Attempted to divide non-divisible cell.");
		}
		
		Cell daughter = new FissionCell(getState(), getFitness() / 2, threshold);
		double halfFitness = getFitness() / 2.0D;
		setFitness(halfFitness);
		checkDivisibility();
		return daughter;
		
	}

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        throw new UnsupportedOperationException("Behaviors are not supported in the FissionCell class.");
    }

    @Override
	public void feed(double delta) {
		nextFitness += delta;
	}

	@Override
	protected void setFitness(double fitness) {
		super.setFitness(fitness);
		nextFitness = fitness;
	}
}
