/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.halt.HaltCondition;
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

    public SourceCell(int state, HashMap<String, Double> production) throws HaltCondition {
        super();
        considerCount = 0;
        setHealth(0);
        setState(state);
        this.production = production;
        this.epsilon = EpsilonUtil.epsilon();
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) throws HaltCondition {
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
    public Cell clone(int state) throws HaltCondition {
        HashMap<String, Double> prodCopy = (HashMap<String, Double>) production.clone();
        SourceCell cc = new SourceCell(getState(), prodCopy);
        return cc;
    }

    @Override
    public double getProduction(String solute) {
        return production.get(solute);
    }

    @Override
    public void adjustHealth(double delta) {
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
