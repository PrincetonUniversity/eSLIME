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

package structural;

import java.util.ArrayList;

/**
 * A weighted collection of items of class T. Each T has some weight associated
 * with it. The total weight of the system is the sum of these weights. The
 * order of the items is identical to that in which they were loaded.
 *
 * For example, I might load Foo with weight 0.5, and Bar with weight 1.0. That
 * would mean that weight range 0 <= weight < 0.5 corresponds to foo, and
 * 0.5 <= weight < 1.5 corresponds to bar.
 *
 * After the collection is declared final (using the close() method), the user
 * may sample the collection by invoking the selectTarget(...) method, which
 * will retrieve items by range in the manner described above.
 *
 * One would like
 */
public class RangeMap<T> {


    //private HashMap<T, Double> weights;

    // We track the floor of each element. It might seem easier to use, eg,
    // a HashMap, but we want order to be preserved and for items to be
    // loaded sequentially.

    // The first element is pre-loaded as 0.0, but each ceiling is then
    // appended. This means that floors has one too many elements, which is
    // handled accordingly.
    private ArrayList<Double> floors;

    // We track the value associated with each floor in the same order as
    // the floors. Note that keys should always be one element shorter than
    // floors.

    private ArrayList<T> keys;

    public RangeMap() {
        //weights = new HashMap<>();
        floors = new ArrayList<>();
        keys = new ArrayList<>();
        floors.add(0.0);
    }

    public void add(T token, double weight) {
        keys.add(token);
        appendBin(weight);
//        weights.put(token, weight);

    }

    private void appendBin(double weight) {
        int n = floors.size();
        double lastBin = floors.get(n - 1);
        floors.add(lastBin + weight);
    }

    /**
     * Returns a particular process based on an input between 0
     * and the total weight, with processes sorted numerically
     * by ID number.
     * <p/>
     * So if w_i is the weight of process i, then you'll get back
     * <p/>
     * 0 <= x < w_1         --> return process 1's id
     * w_1 <= x < w_1 + w_2 --> return process 2's id
     * ...
     * w_n-1 <= x < sum(w)  --> return process n's id
     *
     * @return
     */
    public T selectTarget(double x) {

        RangeSearchHelper helper = new RangeSearchHelper(floors);

        Integer target = helper.findKey(x);

        T ret = keys.get(target);

        return ret;
    }

    public double getTotalWeight() {

        if (floors.size() == 1) {
            return 0.0;
        }

        double totalWeight = 0;
        for (int i = 0; i < floors.size() - 1; i++) {
            double weight = floors.get(i + 1) - floors.get(i);
            totalWeight += weight;
        }

        return totalWeight;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RangeMap)) {
            return false;
        }

        RangeMap other = (RangeMap) obj;

        if (!EpsilonUtil.epsilonEquals(getTotalWeight(), other.getTotalWeight())) {
            return false;
        }


        if (!contentsEqual(this, other)) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if each weight range in each map returns equal objects for
     * the same value of x.
     *
     * One would like to use a simpler method of comparing maps, say by asking
     * whether each key is contained in both maps. However, as
     * this class is generic, one is instead forced to compare element by
     * element.
     *
     * @return
     */
    private boolean contentsEqual(RangeMap p, RangeMap q) {
        // If they have a different number of elements, we already know that 
        // they are unequal.
        if (p.floors.size() != q.floors.size()) {
            return false;
        }

        // Test each subsequent bin by its midpoint value.
        // The -1 is because the variable floors ends with the last ceiling added,
        // but it is supposed to be a list of floors.
        for (int i = 0; i < floors.size() - 1; i++) {
            double range = floors.get(i + 1) - floors.get(i);
            double floor = floors.get(i);
            double midpoint = floor + (range / 2.0);

            if (!binsEqual(p, q, midpoint)) {
                return false;
            }
        }

        return true;
    }

    private boolean binsEqual(RangeMap p, RangeMap q, double midpoint) {
        Object pResult = p.selectTarget(midpoint);
        Object qResult = q.selectTarget(midpoint);

        if (!pResult.equals(qResult)) {
            return false;
        }
       
        return true;
    }

    @Override
    public RangeMap<T> clone() {

        RangeMap<T> cloned = new RangeMap<T>();

        for (int i = 1; i < floors.size(); i++) {
            T key = keys.get(i - 1);
            Double weight = floors.get(i);

            cloned.add(key, weight);
        }

        return cloned;
    }

    /**
     * Return the total number of bins (ranges) in the range map.
     *
     * @return
     */
    public int getNumBins() {
        // The first bin is a dummy, so we want one fewer
        return floors.size() - 1;
    }
}
