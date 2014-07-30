/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;


public class FixationEvent extends HaltCondition {

    private int fixationState;

    public FixationEvent(double time, int fixationState) {
        super(time);
        this.fixationState = fixationState;
    }

    public int getFixationState() {
        return fixationState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FixationEvent (");
        sb.append(fixationState);
        sb.append(")");
        return sb.toString();
    }
}
