/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;//import junit.framework.TestCase;

import control.halt.HaltCondition;
import control.halt.ManualHaltEvent;
import layers.MockLayerManager;
import processes.BaseProcessArguments;
import test.EslimeTestCase;

public class ManualHaltTest extends EslimeTestCase {

    public void testLifeCycle() {
        MockLayerManager layerManager = new MockLayerManager();
        BaseProcessArguments arguments = makeBaseProcessArguments(layerManager, null);
        CellProcessArguments cpArguments = new CellProcessArguments(null, null);
        ManualHalt halt = new ManualHalt(arguments, cpArguments, "message");
        boolean thrown = false;
        try {
            halt.fire(null);
        } catch (ManualHaltEvent mh) {
            thrown = true;
            assertEquals("message", mh.toString());
        } catch (HaltCondition hc) {
            fail();
        }
        assertTrue(thrown);
    }
}