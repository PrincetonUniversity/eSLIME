/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */
package processes;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import layers.LayerManager;
import processes.gillespie.GillespieState;

public abstract class EcoProcess {

    protected LayerManager layerManager;
    protected GeneralParameters p;
    private int id;
    private Argument<Integer> period;
    private Argument<Integer> start;


    public EcoProcess(BaseProcessArguments arguments) {
        p = arguments.getGeneralParameters();
        id = arguments.getId();
        start = arguments.getStart();
        period = arguments.getPeriod();
        this.layerManager = arguments.getLayerManager();
    }

    public int getID() {
        return id;
    }


    /**
     * Identifies possible update targets in the event of an iteration. Should
     * accept a null GillespieState for non-Gillespie events.
     *
     * @throws HaltCondition
     */
    public abstract void target(GillespieState gs) throws HaltCondition;

    /**
     * Convenience interface for target(...) -- calls it with a null GillespieState
     * argument.
     *
     * @throws HaltCondition
     */
    public void target() throws HaltCondition {
        target(null);
    }

    /**
     * Chooses one of its available targets and executes the update.
     *
     * @param state
     * @throws HaltCondition
     */
    public abstract void fire(StepState state) throws HaltCondition;

    public void iterate() throws HaltCondition {
        target();
        StepState stepState = layerManager.getStepState();
        fire(stepState);
    }

    public Argument<Integer> getPeriod() {
        return period;
    }

    public Argument<Integer> getStart() {
        return start;
    }

    public abstract void init();
}
