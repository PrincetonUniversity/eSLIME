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

package layers;

import test.EslimeTestCase;

/**
 * Created by David B Borenstein on 3/23/14.
 */
public abstract class SystemStateTest extends EslimeTestCase {
    public abstract void testGetHealth() throws Exception;

    public abstract void testGetState() throws Exception;

    public abstract void testGetValue() throws Exception;

    public abstract void testGetTime() throws Exception;

    public abstract void testGetFrame() throws Exception;

    public abstract void testIsHighlighted() throws Exception;
}
