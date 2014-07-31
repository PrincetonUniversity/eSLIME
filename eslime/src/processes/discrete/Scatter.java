/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.halt.LatticeFullEvent;
import control.identifiers.Coordinate;
import factory.cell.CellFactory;
import factory.control.arguments.IntegerArgumentFactory;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.HashSet;
import java.util.Random;

public class Scatter extends CellProcess {

    private Argument<Integer> numGroups;
    private Argument<Integer> groupSize;

    private Random random;

    private HashSet<Coordinate> candidates = null;

    public Scatter(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                   GeneralParameters p) {

        super(loader, layerManager, activeSites, id, p);

        random = p.getRandom();

        numGroups = IntegerArgumentFactory.instantiate(e, "types", 1, p.getRandom());
        groupSize = IntegerArgumentFactory.instantiate(e, "tokens", 1, p.getRandom());
    }

    public void target(GillespieState gs) throws HaltCondition {
        // Construct initial set of candidates
        candidates = new HashSet<>();

        for (Coordinate c : activeSites) {
            if (!layer.getViewer().isOccupied(c)) {
                candidates.add(c);
            }
        }
        if (gs != null) {
            gs.add(this.getID(), candidates.size(), candidates.size() * 1.0D);
        }
    }

    public void fire(StepState state) throws HaltCondition {
        System.out.println("Executing Scatter.");
        if (candidates == null) {
            throw new IllegalStateException("fire() invoked on scatter before target().");
        }

        int n = numGroups.next();
        for (int i = 0; i < n; i++) {
            CellFactory factory = getCellFactory(layerManager);

            // Create a cell factory for this group
            int m = groupSize.next();
            System.out.println("Tokens: " + m);
            for (int j = 0; j < m; j++) {

                if (candidates.isEmpty()) {
                    throw new LatticeFullEvent();
                }

                // Choose target randomly
                Coordinate[] cVec = candidates.toArray(new Coordinate[0]);

                int o = random.nextInt(cVec.length);
                Coordinate target = cVec[o];

                Cell cell = factory.instantiate();

                //System.out.println("   Placing cell of type " + cell.getState() + " at location " + target);
                layer.getUpdateManager().place(cell, target);

                //state.highlight(target);
                candidates.remove(target);
            }
        }

        // Make sure that a new target must be chosen prior to next invocation.
        candidates = null;

    }
}
