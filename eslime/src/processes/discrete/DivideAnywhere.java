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

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;

public class DivideAnywhere extends BulkDivisionProcess {

    private Coordinate[] candidates = null;

    public DivideAnywhere(ProcessLoader loader, LayerManager layerManager, int id,
                          GeneralParameters p, Argument<Integer> maxTargets) {
        super(loader, layerManager, id, p, maxTargets);
    }


    public void target(GillespieState gs) throws HaltCondition {
        HashSet<Coordinate> candSet = layer.getViewer().getDivisibleSites();
        candidates = candSet.toArray(new Coordinate[0]);

        if (gs != null) {
            gs.add(getID(), candidates.length, candidates.length * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        execute(candidates);
        candidates = null;
    }

}
