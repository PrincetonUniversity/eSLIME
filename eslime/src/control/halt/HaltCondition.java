/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;

public class HaltCondition extends Exception {

    private double gillespie;

    public HaltCondition(double gillespie) {
        this.gillespie = gillespie;
    }

    public double getGillespie() {
        return gillespie;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
