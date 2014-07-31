/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import cells.Cell;
import control.halt.BoundaryReachedEvent;
import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;

/**
 * A cell layer content object that throws a BoundaryReachedEvent
 * Created by David B Borenstein on 4/10/14.
 */
public class HaltCellLayerContent extends InfiniteCellLayerContent {

    public HaltCellLayerContent(Geometry geom, CellLayerIndices indices) {
        super(geom, indices);
    }

    @Override
    public void put(Coordinate coord, Cell current) throws BoundaryReachedEvent {
        if (coord.hasFlag(Flags.BEYOND_BOUNDS)) {
            throw new BoundaryReachedEvent();
        }
    }
}
