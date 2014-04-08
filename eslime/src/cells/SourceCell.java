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

import control.identifiers.Coordinate;
import structural.utilities.EpsilonUtil;

import java.util.HashMap;

/**
 * Created by dbborens on 1/2/14.
 */
public class SourceCell extends Cell {
    private int considerCount;
    private HashMap<String, Double> production;

    private double epsilon;

    public SourceCell(int state, HashMap<String, Double> production) {
        super();
        considerCount = 0;
        setFitness(0);
        setState(state);
        this.production = production;
        this.epsilon = EpsilonUtil.epsilon();
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        throw new UnsupportedOperationException("Behaviors are not supported in the SourceCell class.");
    }

    @Override
    public int consider() {
        considerCount++;
        return considerCount;
    }

    @Override
    public void apply() {
        considerCount = 0;
    }

    @Override
    public Cell divide() {
        throw new UnsupportedOperationException("Source cells cannot divide");
    }

    @Override
    public Cell clone(int state) {
        HashMap<String, Double> prodCopy = (HashMap<String, Double>) production.clone();
        SourceCell cc = new SourceCell(getState(), prodCopy);
        return cc;
    }

    @Override
    public double getProduction(String solute) {
        return production.get(solute);
    }

    @Override
    public void adjustFitness(double delta) {
        throw new UnsupportedOperationException("Source cells cannot eat");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SourceCell)) {
            return false;
        }

        SourceCell other = (SourceCell) obj;

        if (other.getState() != this.getState()) {
            return false;
        }

        if (prodsUnequal(this, other)) {
            return false;
        }

        return true;
    }

    private boolean prodsUnequal(SourceCell p, SourceCell q) {
        if (p.production.size() != q.production.size()) {
            return true;
        }

        for (String s : p.production.keySet()) {
            if (!q.production.containsKey(s)) {
                return true;
            }

            double pProd = p.production.get(s);
            double qProd = q.production.get(s);

            double normDelta = Math.abs(pProd - qProd);

            if (normDelta > epsilon) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void die() {
        throw new UnsupportedOperationException();
    }
}
