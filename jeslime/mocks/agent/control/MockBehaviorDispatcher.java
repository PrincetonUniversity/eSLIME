/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package agent.control;

import agent.Behavior;
import structural.identifiers.Coordinate;

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
