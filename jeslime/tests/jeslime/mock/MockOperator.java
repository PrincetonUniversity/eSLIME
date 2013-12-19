package jeslime.mock;

import continuum.operations.Operator;
import geometry.Geometry;

/**
 * Created by dbborens on 12/19/13.
 */
public class MockOperator extends Operator {
    @Override
    public void init() {

    }

    public MockOperator(Geometry geometry, boolean useBoundaries) {
        super(geometry, useBoundaries);
    }

}
