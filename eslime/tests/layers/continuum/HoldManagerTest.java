/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HoldManagerTest {

    private ContinuumAgentManager manager;
    private ContinuumSolver solver;
    private HoldManager query;

    @Before
    public void init() {
        manager = mock(ContinuumAgentManager.class);
        solver = mock(ContinuumSolver.class);

        query = new HoldManager(manager, solver);
    }

    @Test(expected = IllegalStateException.class)
    public void releaseWhileNotHeldThrows() throws Exception {
        query.release();
    }

    @Test
    public void releaseFlipsHeldFlag() throws Exception {
        query.hold();
        query.release();
        assertFalse(query.isHeld());
    }

    @Test
    public void releaseCallsSolve() throws Exception {
        query.hold();
        query.release();
        verify(solver).solve();
    }

    @Test(expected = IllegalStateException.class)
    public void holdWhileHeldThrows() throws Exception {
        query.hold();
        query.hold();
    }

    @Test
    public void holdFlipsHeldFlag() throws Exception {
        query.hold();
        assertTrue(query.isHeld());
    }

    @Test(expected = IllegalStateException.class)
    public void solveWhileHeldThrows() throws Exception {
        query.hold();
        query.solve();
    }

    @Test
    public void solveCallsSolver() throws Exception {
        query.solve();
        verify(solver).solve();
    }

    @Test
    public void solveAppliesReactions() throws Exception {
        query.solve();
        verify(manager).apply();
    }

    @Test
    public void resetClearsHold() throws Exception {
        query.hold();
        query.reset();
        assertFalse(query.isHeld());
    }

    @Test
    public void resetCallsManager() throws Exception {
        query.reset();
        verify(manager).reset();
    }

    @Test
    public void idComesFromManager() throws Exception {
        when(manager.getId()).thenReturn("test");
        assertEquals("test", query.getId());
    }

    @Test
    public void linkerComesFromManager() throws Exception {
        ContinuumAgentLinker linker = mock(ContinuumAgentLinker.class);
        when(manager.getLinker(any())).thenReturn(linker);
    }
}