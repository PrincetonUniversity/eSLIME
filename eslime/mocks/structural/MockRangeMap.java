package structural;

/**
 * Created by dbborens on 3/18/14.
 */
public class MockRangeMap<T> extends RangeMap<T> {

    public MockRangeMap() {
    }

    private boolean reportEquality;
    private T nextTarget;

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
    public void setNextTarget(T nextTarget) {
        this.nextTarget = nextTarget;
    }

    private int timesCloned;

    @Override
    public RangeMap<T> clone() {
        timesCloned++;

        MockRangeMap<T> ret = new MockRangeMap<>();
        ret.setReportEquality(reportEquality);
        ret.setNextTarget(nextTarget);
        return ret;
    }

    @Override
    public T selectTarget(double x) {
        return nextTarget;
    }

    public int getTimesCloned() {
        return timesCloned;
    }
}
