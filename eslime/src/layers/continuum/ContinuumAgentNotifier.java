/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;

import java.util.function.Consumer;

/**
 * Notifies a ContinuumAgentIndex that a related cell has been added or
 * removed.
 *
 * Created by dbborens on 12/31/14.
 */
public class ContinuumAgentNotifier {

    private Consumer<BehaviorCell> adder;
    private Consumer<BehaviorCell> remover;

    public ContinuumAgentNotifier(Consumer<BehaviorCell> adder, Consumer<BehaviorCell> remover) {
        this.adder = adder;
        this.remover = remover;
    }

    public void add(BehaviorCell cell) {
        adder.accept(cell);
    }

    public void remove(BehaviorCell cell) {
        remover.accept(cell);
    }
}
