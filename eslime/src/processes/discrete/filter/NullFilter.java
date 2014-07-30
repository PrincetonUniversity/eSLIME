/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.Collection;

/**
 * NullFilter passes all entries through unaffected.
 * Created by dbborens on 5/5/14.
 */
public class NullFilter extends Filter {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public Collection<Coordinate> apply(Collection<Coordinate> toFilter) {
        return toFilter;
    }
}
