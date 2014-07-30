/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
        int result = o + offset;
        return result;
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
