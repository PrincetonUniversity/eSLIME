/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.action;

import cells.BehaviorCell;

/**
 * Created by dbborens on 4/27/14.
 */
public class MockActionRangeMap extends ActionRangeMap {
    public MockActionRangeMap() {
    }

    private boolean reportEquality;
    private Action nextTarget;

    /**
     * Specifies whether this object should report itself as equal
     * to whatever it happens to be compared to.
     */
    public void setReportEquality(boolean reportEquality) {
        this.reportEquality = reportEquality;
        timesCloned = 0;
    }

    /**
     * Returns true if and only if reportEquality is set to
     * true.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return reportEquality;
    }

    /**
     * Specifies the next target to return, regardless
     * of what "x" is.
     *
     * @param nextTarget
     */
    public void setNextTarget(Action nextTarget) {
        this.nextTarget = nextTarget;
    }

    private int timesCloned;

    @Override
    public MockActionRangeMap clone(BehaviorCell child) {
        timesCloned++;

        MockActionRangeMap ret = new MockActionRangeMap();
        ret.setReportEquality(reportEquality);
        ret.setNextTarget(nextTarget);
        return ret;
    }

    @Override
    public Action selectTarget(double x) {
        return nextTarget;
    }

    public int getTimesCloned() {
        return timesCloned;
    }
}
