/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import no.uib.cipr.matrix.Vector;

import java.util.HashMap;

/**
 * Created by dbborens on 12/26/14.
 */
public class ContinuumLayerContent {
    protected Vector state;

    protected ContinuumAgentIndex injIndex;    // Injection (source)
    protected ContinuumAgentIndex expIndex;    // Exponentiation (feedback, decay)

    public ContinuumLayerContent(String id) {
        injIndex = new ContinuumAgentIndex(cell -> cell.getRelationshipManager().getInj(id));
        expIndex = new ContinuumAgentIndex(cell -> cell.getRelationshipManager().getExp(id));
    }

    public Vector getState() {
        return state;
    }

    public void setState(Vector state) {
        this.state = state;
    }

    public void reset() {
        this.state = null;
    }

    public void addInj(BehaviorCell cell) {
        injIndex.add(cell);
    }

    public void addExp(BehaviorCell cell) {
        expIndex.add(cell);
    }

    public void removeInj(BehaviorCell cell) {
        injIndex.remove(cell);
    }

    public void removeExp(BehaviorCell cell) {
        expIndex.remove(cell);
    }

    public HashMap<Coordinate, Double> getInjRelationships() {
        return injIndex.getRelationShips();
    }

    public HashMap<Coordinate, Double> getExpRelationships() {
        return expIndex.getRelationShips();
    }
}
