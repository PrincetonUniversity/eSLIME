/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.set.CompleteSet;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Removes all cells in the active area.
 * <p/>
 * Created by David B Borenstein on 12/24/13.
 */
public class Smite extends CellProcess {

    private boolean skipDead;

    public Smite(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id, GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);

        skipDead = Boolean.valueOf(get("skip-dead-sites"));
    }

    // Minimal constructor for mock tests
    public Smite(LayerManager layerManager, boolean skipDead) {
        super(null, layerManager, new CompleteSet(layerManager.getCellLayer().getGeometry()), 0, null);
        this.skipDead = skipDead;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There may be a meaningful Gillespie implementation of this. If so,
        // we can add it when needed.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        for (Coordinate c : activeSites) {
            boolean dead = !layer.getViewer().isOccupied(c);

            // If it's dead and we don't expect already-dead cells, throw error
            if (dead && !skipDead) {
                String msg = "Attempted to smite coordinate " + c.toString() +
                        " but it was already dead. This is illegal unless" +
                        " the <skip-dead-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!dead) {
                layer.getUpdateManager().banish(c);
            } else {
                // Do nothing if site is vacant and skipDead is true.
            }
        }
    }


}
