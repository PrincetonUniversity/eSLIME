/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;

/**
 * Created by dbborens on 5/29/14.
 */
public class DominationEvent extends HaltCondition {

    private Integer cellState;

    public DominationEvent(double gillespie, Integer cellState) {
        super(gillespie);
        this.cellState = cellState;
    }

    public Integer getCellState() {
        return cellState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DominationEvent (");
        sb.append(cellState);
        sb.append(")");
        return sb.toString();
    }
}
