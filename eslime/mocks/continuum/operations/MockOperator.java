/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package continuum.operations;

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
