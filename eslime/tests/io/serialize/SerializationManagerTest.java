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