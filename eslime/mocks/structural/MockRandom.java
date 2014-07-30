/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import java.util.Random;

/**
 * Created by dbborens on 5/15/14.
 */
public class MockRandom extends Random {
    private boolean booleanValue;

    private int nextIntValue = -1;
    @Override
    /**
     * Returns either the highest possible value, or,
     * if overriden, the specified value.
     */
    public int nextInt(int n) {
        if (nextIntValue == -1) {
            return n - 1;
        } else {
            return nextIntValue;
        }
    }

    public void setNextIntValue(int nextIntValue) {
        this.nextIntValue = nextIntValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public boolean nextBoolean() {
        return booleanValue;
    }
}
