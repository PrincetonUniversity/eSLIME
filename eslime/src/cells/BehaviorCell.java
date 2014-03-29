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

import agent.control.BehaviorDispatcher;
import layers.LayerManager;
import structural.identifiers.Coordinate;
import structural.utilities.EpsilonUtil;

/**
 * Flexible cell class capable of performing arbitrary
 * bottom-up (agent-driven) behaviors for agent-based
 * modeling.
 * <p/>
 * Created by David B Borenstein on 1/25/14.
 */
public class BehaviorCell extends Cell {

    // State
    private int considerCount;
    private double nextFitness;
    private double threshold;

    // Helpers
    private BehaviorDispatcher dispatcher;
    private CallbackManager callbackManager;

    // Default constructor for testing
    public BehaviorCell() {
    }

    public BehaviorCell(LayerManager layerManager, int state, double initialFitness, double threshold) {
        this.threshold = threshold;
        callbackManager = new CallbackManager(this, layerManager);

        setState(state);

        // We use the superclass setFitness here so it doesn't try to update
        // the location, as the cell is usually created before being placed.
        setFitness(initialFitness);

        considerCount = 0;

    }

    @Override
    public int consider() {
        considerCount++;
        return considerCount;
    }

    @Override
    public void apply() {
//        setFitness(nextFitness);
//        checkDivisibility();
        considerCount = 0;
    }

    @Override
    public Cell divide() {
        if (!isDivisible()) {
            throw new IllegalStateException("Attempted to divide non-divisible cell.");
        }

        LayerManager layerManager = callbackManager.getLayerManager();
        BehaviorCell daughter = new BehaviorCell(layerManager, getState(), getFitness() / 2, threshold);
        double halfFitness = getFitness() / 2.0D;
        setFitness(halfFitness);
        daughter.setDispatcher(dispatcher.clone(daughter));
        checkDivisibility();
        return daughter;
    }

    @Override
    public Cell clone(int childState) {
        double fitness = getFitness();

        LayerManager layerManager = callbackManager.getLayerManager();
        BehaviorCell child = new BehaviorCell(layerManager, childState, fitness, threshold);
        child.considerCount = considerCount;
        child.nextFitness = nextFitness;
        child.setDispatcher(dispatcher.clone(child));
        child.setDivisible(isDivisible());

        return child;
    }

    @Override
    public double getProduction(String solute) {
        throw new UnsupportedOperationException("BehaviorCell does not yet support diffusible solutes.");
    }

    @Override
    public void adjustFitness(double delta) {
        double current = getFitness();
        double next = current + delta;
//        System.out.println("      Adjusting fitness of cell at " + callbackManager.getMyLocation() + " from " + current + " to " + next);
        setFitness(next);
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        dispatcher.trigger(behaviorName, caller);
    }

    @Override
    public void die() {
        callbackManager.die();
    }

    @Override
    protected void setDivisible(boolean divisible) {
        super.setDivisible(divisible);
        callbackManager.refreshDivisibility();
    }

    private void checkDivisibility() {
        //System.out.println("   " + getFitness() + " -- " + threshold);
        if (getFitness() > threshold) {
            setDivisible(true);
        } else {
            setDivisible(false);
        }
    }

    public void setDispatcher(BehaviorDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void setFitness(double fitness) {
        super.setFitness(fitness);
//        if (fitness <= 0.0) {
//            try {
//                throw new RuntimeException();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                System.exit(0);
//            }
//        }
        checkDivisibility();
    }

    /**
     * A BehaviorCell is equal to another Object if and only if:
     * - The other Object is a BehaviorCell.
     * - The other Object has a division threshold equal to this one.
     * - The other Object has a state ID equal to this one.
     * - The other Object has a dispatcher that reports equality
     * with this cell's dispatcher.
     *
     * @see agent.control.BehaviorDispatcher
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof BehaviorCell)) {
            return false;
        }

        BehaviorCell other = (BehaviorCell) obj;

        if (other.getState() != this.getState()) {
            return false;
        }

        if (!EpsilonUtil.epsilonEquals(this.threshold, other.threshold)) {
            return false;
        }

        return other.dispatcher.equals(this.dispatcher);
    }

    public double getThreshold() {
        return threshold;
    }
}
