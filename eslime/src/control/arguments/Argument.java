/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import control.halt.HaltCondition;

/**
 * Beginning in eSLIME 0.5.1, any object constructed by a factory can be
 * implemented to use flexible primitive arguments. These replace traditional
 * primitives and can be used to generate random numbers according to a
 * specified distribution.
 * Created by David B Borenstein on 4/7/14.
 */
public abstract class Argument<T> {

    @Override
    public abstract boolean equals(Object obj);

    public abstract T next() throws HaltCondition;
}
