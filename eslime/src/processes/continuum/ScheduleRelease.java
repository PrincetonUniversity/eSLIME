/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.continuum;

import control.halt.HaltCondition;
import layers.continuum.ContinuumLayerScheduler;
import processes.BaseProcessArguments;
import processes.StepState;

/**
 * Created by dbborens on 1/22/15.
 */
public class ScheduleRelease extends ContinuumProcess {

    private ContinuumLayerScheduler scheduler;
    public ScheduleRelease(BaseProcessArguments arguments, ContinuumLayerScheduler scheduler) {
        super(arguments);
        this.scheduler = scheduler;
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        scheduler.release();
    }

    @Override
    public void init() {}
}
