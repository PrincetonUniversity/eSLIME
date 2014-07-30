/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import junit.framework.TestCase;
import processes.MockStepState;
import processes.StepState;

import java.util.ArrayList;

public class SerializationManagerTest extends TestCase {

    private MockSerializer serializer;
    private SerializationManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Create a SM with a mock serializer child. The tests just need to
        // make sure that the child's methods each get called.
        ArrayList<Serializer> writers = new ArrayList<>(1);

        serializer = new MockSerializer();
        writers.add(serializer);
        query = new SerializationManager(null, writers);
    }

    public void testFlushRecorded() {
        MockStepState mss = new MockStepState();
        mss.setRecord(true);
        query.flush(mss);
        assertTrue(serializer.isFlush());
    }

    public void testFlushNotRecorded() {
        MockStepState mss = new MockStepState();
        mss.setRecord(false);
        query.flush(mss);
        assertFalse(serializer.isFlush());

    }
    public void testInit() {
        query.init(null);
        assertTrue(serializer.isInit());
    }

    public void testClose() {
        query.close();
        assertTrue(serializer.isClose());
    }

    public void testDispatchHalt() {
        query.dispatchHalt(null);
        assertTrue(serializer.isDispatchHalt());
    }
}