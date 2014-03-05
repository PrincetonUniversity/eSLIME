/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package processes.discrete;

import cells.Cell;
import io.project.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.XmlUtil;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Causes cells within the active area to perform the specified behavior.
 * Created by David B Borenstein on 2/15/14.
 */
public class Trigger extends CellProcess {
    private String behaviorName;
    private boolean skipVacant;

    public Trigger(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p) {
        super(loader, layerManager, id, p);
        behaviorName = get("behavior");
        Element e = loader.getProcess(id);
        skipVacant = XmlUtil.getBoolean(e, "skip-vacant-sites");

    }

    public Trigger(LayerManager layerManager, String behaviorName, boolean skipVacant) {
        super(null, layerManager, 0, null);
        this.behaviorName = behaviorName;
        this.skipVacant = skipVacant;
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
            boolean vacant = !layer.getViewer().isOccupied(c);
            // If it's vacant and we don't expect already-vacant cells, throw error
            if (vacant && !skipVacant) {
                String msg = "Attempted to trigger behavior " + behaviorName + " in coordinate " + c.toString() +
                        " but the site was dead or vacant. This is illegal unless" +
                        " the <skip-vacant-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!vacant) {
                Cell toTrigger = layer.getViewer().getCell(c);

                // A null caller on the trigger method means that the caller is
                // a process rather than a cell.
                toTrigger.trigger(behaviorName, null);
            } else {
                // Do nothing if site is vacant and skipDead is true.
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Trigger)) {
            return false;
        }

        Trigger other = (Trigger) obj;

        if (!this.behaviorName.equals(other.behaviorName)) {
           return false;
        }

        if (skipVacant != other.skipVacant) {
            return false;
        }

        return true;
    }
}
