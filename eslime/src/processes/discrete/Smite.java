/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package processes.discrete;

import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Removes all cells in the active area.
 * <p/>
 * Created by David B Borenstein on 12/24/13.
 */
public class Smite extends CellProcess {

    private boolean skipDead;

    public Smite(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p) {
        super(loader, layerManager, id, p);

        skipDead = Boolean.valueOf(get("skip-dead-sites"));
    }

    // Minimal constructor for mock tests
    public Smite(LayerManager layerManager, boolean skipDead) {
        super(null, layerManager, 0, null);
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
