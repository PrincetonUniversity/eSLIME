/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.halt;

/**
 * Created by dbborens on 7/28/14.
 */
public class ManualHaltEvent extends HaltCondition {
    private String message;

    public ManualHaltEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
