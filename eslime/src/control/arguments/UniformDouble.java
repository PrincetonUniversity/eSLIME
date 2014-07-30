/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
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
