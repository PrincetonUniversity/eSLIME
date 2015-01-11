/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ContinuumAgentNotifierTest {

    private CellCaptor captor;
    private BehaviorCell cell;

    @Before
    public void init() {
        cell = mock(BehaviorCell.class);
        captor = new CellCaptor();
    }

    @Test
    public void add() {
        ContinuumAgentNotifier query = new ContinuumAgentNotifier(captor, null);
        query.add(cell);
        assertTrue(cell == captor.getCaptured());
    }

    @Test
    public void remove() {
        ContinuumAgentNotifier query = new ContinuumAgentNotifier(null, captor);
        query.remove(cell);
        assertTrue(cell == captor.getCaptured());
    }

    private class CellCaptor implements Consumer<BehaviorCell> {

        private BehaviorCell captured;

        @Override
        public void accept(BehaviorCell behaviorCell) {
            captured = behaviorCell;
        }

        public BehaviorCell getCaptured() {
            return captured;
        }
    }
}