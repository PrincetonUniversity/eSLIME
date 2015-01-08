/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.cell;

/**
 * Lightweight tuple or capturing a continuum reaction.
 * <p>
 * Created by dbborens on 1/8/15.
 */
public class Reaction {

    private double inj;
    private double exp;
    private String id;

    /**
     * @param inj The magnitude of injection (source vector)
     * @param exp The magnituude of exponentiation (matrix diagonal)
     * @param id  The ID of the layer upon which the reaction occurs
     */
    public Reaction(double inj, double exp, String id) {
        this.inj = inj;
        this.exp = exp;
        this.id = id;
    }

    public double getInj() {
        return inj;
    }

    public double getExp() {
        return exp;
    }

    public String getId() {
        return id;
    }
}
