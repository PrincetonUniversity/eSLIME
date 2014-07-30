/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.set.CoordinateSet;
import layers.LayerManager;
import processes.MaxTargetHelper;
import processes.StepState;
import processes.discrete.filter.Filter;
import processes.gillespie.GillespieState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Causes cells within the active area to perform the specified behavior.
 * Created by David B Borenstein on 2/15/14.
 */
public class TriggerProcess extends CellProcess {
    private String behaviorName;
    private boolean skipVacant;
    private boolean requireNeighbors;
    private int maxTargets;
    private Filter filter;

    // We use a cell array because triggering may also move cells
    private Cell[] targets;

    public TriggerProcess(LayerManager layerManager,
                          CoordinateSet activeSites,
                          int id,
                          String behaviorName,
                          GeneralParameters p,
                          Filter filter,
                          boolean skipVacant,
                          boolean requireNeighbors,
                          int maxTargets) {
        super(null, layerManager, activeSites, id, p);
        this.behaviorName = behaviorName;
        this.skipVacant = skipVacant;
        this.maxTargets = maxTargets;
        this.requireNeighbors = requireNeighbors;
        this.filter = filter;
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
        Collection<Coordinate> stateFiltered = filter.apply(vacancyFiltered);
        Collection<? extends Object> neighborFiltered = respectNeighborhoodRequirements(stateFiltered);
        Object[] selectedCoords = MaxTargetHelper.respectMaxTargets(neighborFiltered, maxTargets, p.getRandom());

        Cell[] selectedCells = new Cell[selectedCoords.length];
        for (int i = 0; i < selectedCells.length; i++) {
            Coordinate coord = (Coordinate) selectedCoords[i];
            selectedCells[i] = layer.getViewer().getCell(coord);
        }

        return selectedCells;
    }

    /**
     * If require-neighbors is set, removes any candidates that don't have any
     * occupied neighbors.
     */
    private Collection<? extends Object> respectNeighborhoodRequirements(Collection<? extends Object> unfiltered) {
        ArrayList<Object> filtered = new ArrayList<>(unfiltered.size());
        if (!requireNeighbors) {
            return unfiltered;
        }

        for (Object cObj : unfiltered) {
            Coordinate candidate = (Coordinate) cObj;
            int[] neighborStates = layer.getLookupManager().getNeighborStates(candidate, true);

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
    private ArrayList<Coordinate> respectVacancyRequirements(Set<Coordinate> unfiltered) {
        ArrayList<Coordinate> candidates = new ArrayList<>(unfiltered.size());

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
        for (Cell target : targets) {

            // If the cell has been removed as a result of firing the trigger
            // process in a previous target, skip it.
            if (!layer.getViewer().exists(target)) {
                continue;
            }

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
