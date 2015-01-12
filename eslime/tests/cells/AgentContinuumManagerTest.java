/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package cells;

import control.identifiers.Coordinate;
import layers.continuum.ContinuumAgentLinker;
import layers.continuum.ContinuumAgentNotifier;
import org.junit.Before;
import test.LinearMocks;

import java.util.function.Supplier;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class AgentContinuumManagerTest extends LinearMocks {

    private ContinuumAgentLinker linker;
    private BehaviorCell cell;
    private Supplier<Coordinate> locate;
    private RemoverIndex index;
    private ContinuumAgentNotifier notifier;
    private String id;

    private AgentContinuumManager query;

    @Before
    public void init() throws Exception {
        id = "test";

        notifier = mock(ContinuumAgentNotifier.class);

        linker = mock(ContinuumAgentLinker.class);
        when(linker.get(any())).thenReturn(1.0);
        when(linker.getNotifier()).thenReturn(notifier);

        cell = mock(BehaviorCell.class);

        locate = (Supplier<Coordinate>) mock(Supplier.class);
        when(locate.get()).thenReturn(a);

        index = mock(RemoverIndex.class);

//        query = new AgentContinuumScheduler(cell, index, locate, );
    }

    public void nothing() throws Exception {
        fail("Implement me");
    }
}