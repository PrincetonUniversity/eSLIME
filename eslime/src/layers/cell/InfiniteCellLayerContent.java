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

package layers.cell;

import control.identifiers.Coordinate;
import geometry.Geometry;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by David B Borenstein on 4/10/14.
 */
public class InfiniteCellLayerContent extends CellLayerContent {

    public InfiniteCellLayerContent(Geometry geom, CellLayerIndices indices) {
        super(geom, indices);
    }

    @Override
    public void sanityCheck(Coordinate coord) {
    }

    @Override
    public Set<Coordinate> getImaginarySites() {
        HashSet<Coordinate> ret = new HashSet<>(getOccupiedSites());

        for (Coordinate c : ret) {
            if (hasCanonicalForm(c)) {
                ret.remove(c);
            }
        }
        return ret;
    }
}
