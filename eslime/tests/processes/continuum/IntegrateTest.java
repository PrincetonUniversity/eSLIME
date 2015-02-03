/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import layers.continuum.ContinuumLayerScheduler;
import org.junit.Before;
import org.junit.Test;
import processes.BaseProcessArguments;
import processes.StepState;

import static org.mockito.Mockito.*;

public class IntegrateTest {

    public Integrate query;
    public StepState state;
    public ContinuumLayerScheduler scheduler;
    public BaseProcessArguments arguments;

    @Before
    public void init() throws Exception {
        state = mock(StepState.class);
        scheduler = mock(ContinuumLayerScheduler.class);
        arguments = mock(BaseProcessArguments.class);
        query = new Integrate(arguments, scheduler);
    }

    @Test
    public void fireCallsSchedulerSolve() throws Exception {
        query.fire(state);
        verify(scheduler).solve();
    }

    @Test
    public void initDoesNothing() throws Exception {
        query.init();
        verifyNoMoreInteractions(state, scheduler, arguments);
    }
}