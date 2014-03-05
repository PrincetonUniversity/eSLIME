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
import layers.cell.CellUpdateManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Kill all cells whose biomass is less than a defined
 * threshold.
 * <p/>
 * Created by dbborens on 3/5/14.
 */
public class Cull extends CellProcess {
    private Coordinate[] targetsArr = null;
    private double threshold;

    public Cull(ProcessLoader loader, LayerManager layerManager, int id,
                GeneralParameters p, double threshold) {
        super(loader, layerManager, id, p);
        this.threshold = threshold;
    }

    public void target(GillespieState gs) throws HaltCondition {
        HashSet<Coordinate> candidates = layer.getViewer().getOccupiedSites();

        ArrayList<Coordinate> targets = new ArrayList<Coordinate>();

        for (Coordinate candidate : candidates) {
            Cell cell = layer.getViewer().getCell(candidate);
            if (cell.getFitness() <= threshold) {
                targets.add(candidate);
            }
        }

        targetsArr = targets.toArray(new Coordinate[0]);

        if (gs != null) {
            gs.add(getID(), targets.size(), targets.size() * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        execute(state, targetsArr);
        targetsArr = null;
    }

    private void execute(StepState state, Coordinate[] targetsArr) {
        CellUpdateManager manager = layer.getUpdateManager();
        for (Coordinate target : targetsArr) {
            manager.banish(target);
        }
    }

}
