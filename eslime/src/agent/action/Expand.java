/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import agent.targets.TargetRule;
import cells.BehaviorCell;
import cells.Cell;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import layers.cell.CellUpdateManager;
import processes.discrete.ShoveHelper;

import java.util.Random;

/**
 * Places a copy or copies of the current cell toward any vacant location.
 *
 * This uses the "clone" method, rather than the "divide" method, meaning
 * that the state of the cell is exactly preserved.
 *
 * Created by dbborens on 5/2/14.
 */
public class Expand extends Action{

    // Highlight channels for the targeting and targeted cells
    private Argument<Integer> selfChannel;
    private Argument<Integer> targetChannel;

    // Displaces cells along a trajectory in the event that the cell is
    // divided into an occupied site and replace is disabled.
    private ShoveHelper shoveHelper;

    private Random random;

    public Expand(BehaviorCell callback, LayerManager layerManager,
                  Argument<Integer> selfChannel, Argument<Integer> targetChannel, Random random) {

        super(callback, layerManager);
        this.selfChannel = selfChannel;
        this.targetChannel = targetChannel;
        this.random = random;

        shoveHelper = new ShoveHelper(layerManager, random);
    }

    @Override
    public void run(Coordinate caller) throws HaltCondition {
        Coordinate parentLocation = getOwnLocation();

        CellUpdateManager u = getLayerManager().getCellLayer().getUpdateManager();

        // Step 1: identify nearest vacant site.
        Coordinate target = shoveHelper.chooseVacancy(parentLocation);

        // Step 2: shove parent toward nearest vacant site.
        shoveHelper.shove(parentLocation, target);

        // Step 3: Clone parent.
        Cell child = getCallback().clone();

        // Step 4: Place child in parent location.
        u.place(child, parentLocation);

        // Step 5: Clean up out-of-bounds cells.
        shoveHelper.removeImaginary();

        // Step 6: Highlight the parent and target locations.
        highlight(target, parentLocation);
    }

    private void highlight(Coordinate target, Coordinate ownLocation) {
        doHighlight(targetChannel, target);
        doHighlight(selfChannel, ownLocation);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        return new Expand(child, getLayerManager(), selfChannel, targetChannel,
                random);
    }
}
