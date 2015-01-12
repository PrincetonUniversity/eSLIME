/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import cells.BehaviorCell;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContinuumAgentNotifierTest {

    private BiConsumer<BehaviorCell, Supplier<RelationshipTuple>> adder;
    private Consumer<BehaviorCell> remover;
    private Supplier<RelationshipTuple> supplier;
    private ContinuumAgentNotifier query;
    private BehaviorCell cell;

    @Before
    public void init() throws Exception {
        adder = (BiConsumer<BehaviorCell, Supplier<RelationshipTuple>>)
                mock(BiConsumer.class);
        remover = (Consumer<BehaviorCell>) mock(Consumer.class);
        cell = mock(BehaviorCell.class);
        supplier = (Supplier<RelationshipTuple>) mock(Supplier.class);

        query = new ContinuumAgentNotifier(adder, remover);
    }

    @Test
    public void addPassesCellAndSupplier() {
        query.add(cell, supplier);
        verify(adder).accept(cell, supplier);
    }

    @Test
    public void removePassesCell() {
        query.remove(cell);
        verify(remover).accept(cell);

    }
}