/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import org.junit.Before;
import org.junit.Test;
import test.LinearMocks;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ContinuumAgentLinkerTest extends LinearMocks {

    private ContinuumAgentNotifier notifier;
    private Function<Coordinate, Double> stateLookup;
    private ContinuumAgentLinker query;

    @Before
    public void init() throws Exception {
        notifier = mock(ContinuumAgentNotifier.class);
        stateLookup = (Function<Coordinate, Double>) mock(Function.class);
        when(stateLookup.apply(any())).thenReturn(1.0);

        query = new ContinuumAgentLinker(notifier, stateLookup);
    }

    @Test
    public void getNotifier() throws Exception {
        assertEquals(notifier, query.getNotifier());
    }

    @Test
    public void getAsksStateLookup() throws Exception {
        Supplier<Coordinate> supplier = () -> a;
        query.get(supplier);
        verify(stateLookup).apply(a);
    }
}