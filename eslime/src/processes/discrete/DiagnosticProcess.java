/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import control.halt.HaltCondition;
import layers.cell.StateMapViewer;
import processes.BaseProcessArguments;
import processes.StepState;
import processes.gillespie.GillespieState;

/**
 * Reports information about the model state. Useful for debugging
 * broken eSLIME scripts.
 * Created by dbborens on 3/7/14.
 */
public class DiagnosticProcess extends CellProcess {
    public DiagnosticProcess(BaseProcessArguments arguments, CellProcessArguments cpArguments) {
        super(arguments, cpArguments);
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // Could add some debug information about the Gillespie state...
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        System.out.println("Occupied sites:" + layer.getViewer().getOccupiedSites().size());
        System.out.println("Divisible sites:" + layer.getViewer().getDivisibleSites().size());

        System.out.println("Cells by type:");
        StateMapViewer smv = layer.getViewer().getStateMapViewer();
        for (Integer s : smv.getStates()) {
            System.out.println("   type " + s + ": " + smv.getCount(s));
        }
    }
}
