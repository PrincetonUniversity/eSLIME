/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 3/23/14.
 */
public abstract class SystemStateTest extends EslimeTestCase {
    public abstract void testGetHealth() throws Exception;

    public abstract void testGetState() throws Exception;

//    public abstract void testGetValue() throws Exception;

    public abstract void testGetTime() throws Exception;

    public abstract void testGetFrame() throws Exception;

    public abstract void testIsHighlighted() throws Exception;
}
