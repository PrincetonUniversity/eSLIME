/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dbborens on 5/5/14.
 */
public class CompositeFilter extends Filter {
    private Filter[] children;

    public CompositeFilter(Filter[] children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeFilter that = (CompositeFilter) o;

        if (!Arrays.equals(children, that.children)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return children != null ? Arrays.hashCode(children) : 0;
    }

    @Override
    public List<Coordinate> apply(List<Coordinate> toFilter) {
        for (Filter child : children) {
            toFilter = child.apply(toFilter);
        }

        return toFilter;
    }
}
