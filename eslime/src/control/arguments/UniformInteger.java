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

import java.util.Random;

/**
 * An argument for eSLIME objects that returns an integer with
 * uniform probability within a specified range.
 * <p/>
 * Created by David B Borenstein on 4/7/14.
 */
public class UniformInteger extends Argument<Integer> {
    private Random random;
    private int range;
    private int offset;

    public UniformInteger(int min, int max, Random random) {
        range = max - min;
        offset = min;
        this.random = random;
    }

    @Override
    public Integer next() {
        int o = random.nextInt(range);
        return o + offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UniformInteger)) {
            return false;
        }

        UniformInteger other = (UniformInteger) obj;

        if (range != other.range) {
            return false;
        }

        if (offset != other.offset) {
            return false;
        }

        return true;
    }
}
