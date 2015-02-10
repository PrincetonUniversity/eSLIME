/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import cells.BehaviorCell;
import cells.MockCell;
import control.halt.HaltCondition;

/**
 * Created by dbborens on 12/1/14.
 */
public class MockCellDescriptor extends CellDescriptor {
    private int state = 1;

    public MockCellDescriptor() {
        super(null);
    }

    public MockCellDescriptor(int state) {
        super(null);
        this.state = state;
    }

    @Override
    public BehaviorCell next() throws HaltCondition {
        return new MockCell(state);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MockCellDescriptor);
    }

    public void setState(int state) {
        this.state = state;
    }
}
