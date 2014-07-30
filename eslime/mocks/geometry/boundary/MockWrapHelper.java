/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
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
