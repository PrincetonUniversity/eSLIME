/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.List;

/**
 * Created by dbborens on 5/5/14.
 */
public abstract class Filter {
    private boolean called = false;

    /**
     * Applies filter to input collection. Original collection should not be
     * modified.
     */
    public abstract List<Coordinate> apply(List<Coordinate> toFilter);

    @Override
    public abstract boolean equals(Object o);

    public boolean isCalled() {
        return called;
    }
}
