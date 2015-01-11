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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ContinuumAgentLinkerTest extends LinearMocks {

    private ContinuumAgentNotifier injNotifier, expNotifier;
    private Function<Coordinate, Double> stateLookup;
    private ContinuumAgentLinker query;

    @Before
    public void init() throws Exception {
        injNotifier = mock(ContinuumAgentNotifier.class);
        expNotifier = mock(ContinuumAgentNotifier.class);
        stateLookup = (Function<Coordinate, Double>) mock(Function.class);

        // Why do I get a null pointer exception if I don't do this? Why can't
        // I just return null?
        when(stateLookup.apply(any())).thenReturn(-1.0);

        query = new ContinuumAgentLinker(injNotifier, expNotifier, stateLookup);
    }

    @Test
    public void getInjNotifier() throws Exception {
        assertTrue(injNotifier == query.getInjNotifier());
    }

    @Test
    public void getExpNotifier() throws Exception {
        assertTrue(expNotifier == query.getExpNotifier());
    }

    @Test
    public void getAsksStateLookup() throws Exception {
        query.get(a);
        verify(stateLookup).apply(a);
    }
}