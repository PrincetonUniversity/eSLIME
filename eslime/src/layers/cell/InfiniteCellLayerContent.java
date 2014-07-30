/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import control.identifiers.Coordinate;
import geometry.Geometry;
import structural.CanonicalCellMap;

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
    public InfiniteCellLayerContent clone() {
        CanonicalCellMap clonedMap = new CanonicalCellMap(map);
        HashSet<Coordinate> clonedSites = new HashSet<>(canonicalSites);
        CellLayerIndices clonedIndices = indices.clone(clonedMap);
        InfiniteCellLayerContent clone = new InfiniteCellLayerContent(geom, clonedIndices);
        clone.map = clonedMap;
        clone.canonicalSites = clonedSites;
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public void sanityCheck(Coordinate coord) {
    }

    @Override
    public Set<Coordinate> getImaginarySites() {
        HashSet<Coordinate> ret = new HashSet<>(getOccupiedSites().size());

        for (Coordinate c : getOccupiedSites()) {
            if (!hasCanonicalForm(c)) {
                ret.add(c);
            }
        }
        return ret;
    }
}
