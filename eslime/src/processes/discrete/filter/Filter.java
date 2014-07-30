/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by dbborens on 5/5/14.
 */
public abstract class Filter {
    private boolean called;

    /**
     * Applies filter to input collection. Original collection should not be
     * modified.
     */
    public abstract Collection<Coordinate> apply(Collection<Coordinate> toFilter);

    public Collection<Coordinate> apply(Coordinate[] toFilter) {
        Collection<Coordinate> asCollection = Arrays.asList(toFilter);
        return apply(asCollection);
    }

    @Override
    public abstract boolean equals(Object o);

    public boolean isCalled() {
        return called;
    }
}
