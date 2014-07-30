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
public class FiniteCellLayerContent extends CellLayerContent {
    public FiniteCellLayerContent(Geometry geom, CellLayerIndices indices) {
        super(geom, indices);
    }

    @Override
    public Set<Coordinate> getImaginarySites() {
        return new HashSet<>(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public FiniteCellLayerContent clone() {
        CanonicalCellMap clonedMap = new CanonicalCellMap(map);
        HashSet<Coordinate> clonedSites = new HashSet<>(canonicalSites);
        CellLayerIndices clonedIndices = indices.clone(clonedMap);
        FiniteCellLayerContent clone = new FiniteCellLayerContent(geom, clonedIndices);
        clone.map = clonedMap;
        clone.canonicalSites = clonedSites;
        return clone;
    }

    @Override
    public void sanityCheck(Coordinate coord) {

        // Otherwise, it had better be in the coordinate system.
        if (!hasCanonicalForm(coord)) {
            StringBuilder ss = new StringBuilder();
            ss.append("Consistency failure: coordinate ");
            ss.append(coord.stringForm());
            ss.append(" does not exist in this geometry.\n");
            String str = ss.toString();
            throw new IllegalStateException(str);
        }
    }
}
