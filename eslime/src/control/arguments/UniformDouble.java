/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import structural.utilities.EpsilonUtil;

import java.util.Random;

/**
 * An argument for eSLIME objects that returns a double-precision
 * floating point within a certain range between minimum (inclusive)
 * and maximum (exclusive), with uniform PDF.
 * <p/>
 * Created by David B Borenstein on 4/7/14.
 */
public class UniformDouble extends Argument<Double> {

    private Random random;
    private double range;
    private double offset;

    public UniformDouble(double min, double max, Random random) {
        range = max - min;
        offset = min;
        this.random = random;
    }

    @Override
    public Double next() {
        double o = random.nextDouble();
        return (o * range) + offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UniformDouble)) {
            return false;
        }

        UniformDouble other = (UniformDouble) obj;

        if (!EpsilonUtil.epsilonEquals(range, other.range)) {
            return false;
        }

        if (!EpsilonUtil.epsilonEquals(offset, other.offset)) {
            return false;
        }

        return true;
    }
}
