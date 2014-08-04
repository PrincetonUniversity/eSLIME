/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.List;

/**
 * Created by dbborens on 5/15/14.
 */
public class MockFilter extends Filter{
    private boolean called;

    public MockFilter() {
        called = false;
    }

    public boolean isCalled() {
        return called;
    }

    @Override
    public List<Coordinate> apply(List<Coordinate> toFilter) {
        called = true;
        return toFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
