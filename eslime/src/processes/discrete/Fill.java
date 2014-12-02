/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.arguments.CellDescriptor;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Fills in all sites in the active site set
 * with cells of the type specified by the
 * process' cell descriptor. Does not throw
 * LatticeFullExceptions.
 *
 * @author dbborens
 */
public class Fill extends CellProcess {

    private CellDescriptor cellDescriptor;

    // If true, the process will skip over any already-filled sites. If
    // false, it will blow up if it encounters an already-filled site
    // that it expected to fill.
    private boolean skipFilled;

    public Fill(BaseProcessArguments arguments, CellProcessArguments cpArguments, boolean skipFilled, CellDescriptor cellDescriptor) {
        super(arguments, cpArguments);
        this.skipFilled = skipFilled;
        this.cellDescriptor = cellDescriptor;

        try {
            if (cpArguments.getMaxTargets().next() >= 0) {
                throw new IllegalArgumentException("Cannot specify maximum targets on fill operation. (Did you mean to limit active sites?)");
            }
        } catch (HaltCondition ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void init() {
    }

    public void target(GillespieState gs) throws HaltCondition {
        // This process only has one event: it affects all relevant cells.
        if (gs != null) {
            gs.add(getID(), 1, 1D);
        }
    }

    public void fire(StepState state) throws HaltCondition {

        for (Coordinate c : activeSites) {
            boolean filled = layer.getViewer().isOccupied(c);

            if (filled && !skipFilled) {
                String msg = "Attempted to fill coordinate " + c.toString() +
                        " but it was already filled. This is illegal unless" +
                        " the <skip-filled-sites> flag is set to true. Did you" +
                        " mean to set it? (id=" + getID() + ")";

                throw new IllegalStateException(msg);
            } else if (!filled) {
                Cell cell = cellDescriptor.next();
                layer.getUpdateManager().place(cell, c);
            } else {
                // Do nothing if site is filled and skipFilled is true.
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fill fill = (Fill) o;

        if (skipFilled != fill.skipFilled) return false;
        if (cellDescriptor != null ? !cellDescriptor.equals(fill.cellDescriptor) : fill.cellDescriptor != null)
            return false;

        if (activeSites != null ? !activeSites.equals(fill.activeSites) : fill.activeSites != null)
            return false;

        if (maxTargets != null ? !maxTargets.equals(fill.maxTargets) : fill.maxTargets != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cellDescriptor != null ? cellDescriptor.hashCode() : 0;
        result = 31 * result + (skipFilled ? 1 : 0);
        return result;
    }
}
