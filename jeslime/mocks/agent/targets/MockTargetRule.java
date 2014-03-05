/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package agent.targets;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 2/14/14.
 */
public class MockTargetRule extends TargetRule {
    private Coordinate[] targets;
    private BehaviorCell lastCaller;

    public MockTargetRule() {
        super(null, null, -1, null);
    }

    public void setTargets(Coordinate[] targets) {
        this.targets = targets;
    }

    public BehaviorCell getLastCaller() {
        return lastCaller;
    }

    @Override
    protected Coordinate[] getCandidates(BehaviorCell caller) {
        lastCaller = caller;
        return targets;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new MockTargetRule();
    }
}
