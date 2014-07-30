/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import structural.utilities.EpsilonUtil;

/**
 * This class is exactly equivalent to a Double object, except it extends
 * Argument, which is the base class for eSLIME factory arguments.
 * <p/>
 * Created by David B Borenstein on 4/7/14.
 */
public class ConstantDouble extends Argument<Double> {

    private Double value;

    public ConstantDouble(Double value) {
        this.value = value;
    }

    @Override
    public Double next() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConstantDouble)) {
            return false;
        }

        ConstantDouble other = (ConstantDouble) obj;

        if (!EpsilonUtil.epsilonEquals(value, other.value)) {
            return false;
        }

        return true;
    }
}
