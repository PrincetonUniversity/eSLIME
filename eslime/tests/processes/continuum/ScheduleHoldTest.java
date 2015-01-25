/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import layers.continuum.ContinuumLayerScheduler;
import org.junit.Before;
import org.junit.Test;
import processes.BaseProcessArguments;
import processes.StepState;

public class ScheduleHoldTest {

    public ScheduleHold query;
    public StepState state;
    public ContinuumLayerScheduler scheduler;
    public BaseProcessArguments arguments;

    @Before
    public void init() throws Exception {
        state = mock(StepState.class);
        scheduler = mock(ContinuumLayerScheduler.class);
        arguments = mock(BaseProcessArguments.class);
        query = new ScheduleHold(arguments, scheduler);
    }

    @Test
    public void fireCallsSchedulerHold() throws Exception {
        query.fire(state);
        verify(scheduler).hold();
    }

    @Test
    public void initDoesNothing() throws Exception {
        query.init();
        verifyNoMoreInteractions(state, scheduler, arguments);
    }
}