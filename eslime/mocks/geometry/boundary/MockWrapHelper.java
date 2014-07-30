/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.boundary;

import control.identifiers.Coordinate;
import geometry.boundaries.helpers.WrapHelper;
import geometry.lattice.Lattice;
import geometry.shape.Shape;

/**
 * Created by dbborens on 5/10/14.
 */
public class MockWrapHelper extends WrapHelper {

    private boolean allWrapped, xWrapped, yWrapped, zWrapped;

    public MockWrapHelper() {
        super(null, null);

        allWrapped = false;
        xWrapped = false;
        yWrapped = false;
        zWrapped = false;
    }

    public boolean isAllWrapped() {
        return allWrapped;
    }

    public boolean isxWrapped() {
        return xWrapped;
    }

    public boolean isyWrapped() {
        return yWrapped;
    }

    public boolean iszWrapped() {
        return zWrapped;
    }

    @Override
    public Coordinate wrapAll(Coordinate toWrap) {
        allWrapped = true;
        return null;
    }

    @Override
    public Coordinate xWrap(Coordinate toWrap) {
        xWrapped = true;
        return null;
    }

    @Override
    public Coordinate yWrap(Coordinate toWrap) {
        yWrapped = true;
        return null;
    }

    @Override
    public Coordinate zWrap(Coordinate toWrap) {
        zWrapped = true;
        return null;
    }

    @Override
    protected void checkValid(Coordinate toWrap) {
    }
}
