/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import factory.cell.Reaction;

/**
 * Created by dbborens on 12/30/14.
 */
public class RelationshipTuple {

    private final Coordinate coordinate;
    private final Reaction reaction;

    public RelationshipTuple(Coordinate coordinate, Reaction reaction) {
        this.coordinate = coordinate;
        this.reaction = reaction;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getInj() {
        return reaction.getInj();
    }

    public double getExp() {
        return reaction.getExp();
    }

}
