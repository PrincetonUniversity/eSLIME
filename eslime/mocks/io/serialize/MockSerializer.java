/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.serialize;

import control.halt.HaltCondition;
import layers.LayerManager;
import processes.StepState;

/**
 * Created by dbborens on 5/15/14.
 */
public class MockSerializer extends Serializer {
    private boolean isDispatchHalt;
    private boolean isClose;
    private boolean isInit;
    private boolean isFlush;

    public MockSerializer() {
        super(null);

        isDispatchHalt = false;
        isClose = false;
        isInit = false;
        isFlush = false;
    }

    public boolean isDispatchHalt() {
        return isDispatchHalt;
    }

    public boolean isClose() {
        return isClose;
    }

    public boolean isInit() {
        return isInit;
    }

    public boolean isFlush() {
        return isFlush;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        isDispatchHalt = true;
    }

    @Override
    public void close() {
        isClose = true;
    }

    @Override
    public void init(LayerManager layerManager) {
        isInit = true;
    }


    @Override
    public void flush(StepState stepState) {
        isFlush = true;
    }
}
