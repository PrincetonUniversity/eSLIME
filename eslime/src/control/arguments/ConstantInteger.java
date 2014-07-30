/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class ConstantInteger extends Argument<Integer> {

    private Integer value;

    public ConstantInteger(Integer value) {
        this.value = value;
    }

    @Override
    public Integer next() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConstantInteger)) {
            return false;
        }

        ConstantInteger other = (ConstantInteger) obj;

        if (other.value != value) {
            return false;
        }

        return true;
    }
}
