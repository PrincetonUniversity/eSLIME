/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import org.dom4j.tree.BaseElement;
import processes.StepState;

import java.util.ArrayList;

/**
 * Created by David B Borenstein on 1/12/14.
 */
public class MockSerializationManager extends SerializationManager {

    private StepState stepState;

    public MockSerializationManager() {
        super(null, new ArrayList<Serializer>());
    }

    public StepState getStepState() {
        return stepState;
    }

    @Override
    public void flush(StepState stepState) {
        this.stepState = stepState;
    }

}
