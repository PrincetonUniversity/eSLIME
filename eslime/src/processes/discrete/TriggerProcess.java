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

import cells.Cell;
import io.loader.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.MaxTargetHelper;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.utilities.XmlUtil;

import java.util.ArrayList;

/**
 * Causes cells within the active area to perform the specified behavior.
 * Created by David B Borenstein on 2/15/14.
 */
public class TriggerProcess extends CellProcess {
    private String behaviorName;
    private boolean skipVacant;
    private boolean requireNeighbors;
    private int maxTargets;

    // We use a cell array because triggering may also move cells
    private Cell[] targets;

    public TriggerProcess(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p) {
        super(loader, layerManager, id, p);
        behaviorName = get("behavior");
        Element e = loader.getProcess(id);
        skipVacant = XmlUtil.getBoolean(e, "skip-vacant-sites");
        maxTargets = XmlUtil.getInteger(e, "max-targets", -1);
        requireNeighbors = XmlUtil.getBoolean(e, "require-neighbors");
    }

    public TriggerProcess(LayerManager layerManager, String behaviorName, GeneralParameters p, boolean skipVacant, boolean requireNeighbors, int maxTargets) {
        super(null, layerManager, 0, p);
        this.behaviorName = behaviorName;
        this.skipVacant = skipVacant;
        this.maxTargets = maxTargets;
        this.requireNeighbors = requireNeighbors;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        targets = resolveTargets();
        // There may be a meaningful Gillespie implementation of this. If so,
        // we can add it when needed.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    private Cell[] resolveTargets() {
        ArrayList<Coordinate> vacancyFiltered = respectVacancyRequirements(activeSites);
        ArrayList<Coordinate> neighborFiltered = respectNeighborhoodRequirements(vacancyFiltered);
        Coordinate[] selectedCoords = MaxTargetHelper.respectMaxTargets(neighborFiltered, maxTargets, p.getRandom());

        Cell[] selectedCells = new Cell[selectedCoords.length];
        for (int i = 0; i < selectedCells.length; i++) {
            Coordinate coord = selectedCoords[i];
            selectedCells[i] = layer.getViewer().getCell(coord);
        }

        return selectedCells;
    }

    /**
     * If require-neighbors is set, removes any candidates that don't have any
     * occupied neighbors.
     */
    private ArrayList<Coordinate> respectNeighborhoodRequirements(ArrayList<Coordinate> unfiltered) {
        ArrayList<Coordinate> filtered = new ArrayList<>(unfiltered.size());
        if (!requireNeighbors) {
            return unfiltered;
        }

        for (Coordinate candidate : unfiltered) {
            int[] neighborStates = layer.getLookupManager().getNeighborStates(candidate);

            // Count up the number of vacant neighbors.
            int numVacantNeighbors = 0;
            for (int i = 0; i < numVacantNeighbors; i++) {
                if (neighborStates[i] == 0) {
                    numVacantNeighbors++;
                }
            }

            // This cell has occupied neighbors only if the number of neighbors
            // is greater than the number of vacant neighbors.
            if (numVacantNeighbors < neighborStates.length) {
                filtered.add(candidate);
            }
        }

        return filtered;
    }

    /**
     * If active sites are to be skipped, eliminates them from the candidate
     * sites. If they are not to be skipped, blows up if it finds one.
     *
     * @param unfiltered
     */
    private ArrayList<Coordinate> respectVacancyRequirements(Coordinate[] unfiltered) {
        ArrayList<Coordinate> candidates = new ArrayList<>(unfiltered.length);


        for (Coordinate c : unfiltered) {

            boolean vacant = !layer.getViewer().isOccupied(c);
            // If it's vacant and we don't expect already-vacant cells, throw error
            if (vacant && !skipVacant) {
                String msg = "Attempted to queue triggering of behavior " +
                        behaviorName + " in coordinate " + c.toString() +
                        " but the site was dead or vacant. This is illegal unless" +
                        " the <skip-vacant-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!vacant) {

                candidates.add(c);
            } else {
                // Do nothing if site is vacant and skipDead is true.
            }
        }

        return candidates;
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        System.out.println("Executing Trigger. " + targets.length);
        for (Cell target : targets) {
            System.out.println("   Triggering behavior '" + behaviorName + "' in cell of type" + target.getState());
            // A null caller on the trigger method means that the caller is
            // a process rather than a cell.
            target.trigger(behaviorName, null);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TriggerProcess)) {
            return false;
        }

        TriggerProcess other = (TriggerProcess) obj;

        if (!this.behaviorName.equals(other.behaviorName)) {
            return false;
        }

        if (skipVacant != other.skipVacant) {
            return false;
        }

        return true;
    }
}
