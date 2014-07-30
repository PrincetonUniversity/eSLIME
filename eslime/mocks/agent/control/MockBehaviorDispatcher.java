/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.control;

import agent.Behavior;
import control.identifiers.Coordinate;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 1/23/14.
 */
public class MockBehaviorDispatcher extends BehaviorDispatcher {

    private String lastTriggeredName;
    private Coordinate lastTriggeredCaller;
    private String lastMappedName;
    private Behavior lastMappedBehavior;
    private ArrayList<String> mappedNames;
    private ArrayList<Behavior> mappedBehaviors;

    public MockBehaviorDispatcher() {
        mappedNames = new ArrayList<>();
        mappedBehaviors = new ArrayList<>();
    }

    public ArrayList<String> getMappedNames() {
        return mappedNames;
    }

    public ArrayList<Behavior> getMappedBehaviors() {
        return mappedBehaviors;
    }

    public Behavior getLastMappedBehavior() {
        return lastMappedBehavior;
    }

    public String getLastMappedName() {
        return lastMappedName;
    }

    public Coordinate getLastTriggeredCaller() {
        return lastTriggeredCaller;
    }

    public String getLastTriggeredName() {
        return lastTriggeredName;
    }

    @Override
    public void map(String name, Behavior behavior) {
        mappedNames.add(name);
        mappedBehaviors.add(behavior);
        lastMappedName = name;
        lastMappedBehavior = behavior;
    }

    @Override
    public void trigger(String behaviorName, Coordinate caller) {
        lastTriggeredName = behaviorName;
        lastTriggeredCaller = caller;
    }

    private boolean reportEquals;
    private boolean overrideEquals;

    public void setOverrideEquals(boolean overrideEquals) {
        this.overrideEquals = overrideEquals;

    }

    public void setReportEquals(boolean reportEquals) {
        this.reportEquals = reportEquals;
    }

    @Override
    public boolean equals(Object obj) {
        if (overrideEquals) {
            return reportEquals;
        } else {
            return super.equals(obj);
        }
    }
}
