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

package io.visual.highlight;

import test.EslimeTestCase;

/**
 * eSLIME supports the ability to create visualizations that reflect the state
 * of the system at any point within an integration cycle, while also capturing
 * event data that takes place after the recording time point. This means that
 * a visualization can show both how a system looks before a set of events take
 * place and where those events will take place.
 *
 * This test verifies that the deferred state recording system works correctly
 * by doing the following:
 *
 *    (1) Create an initial system state.
 *    (2) Record the system state.
 *    (3) Modify the system state.
 *    (4) Record a highlight.
 *    (5) Flush the system state to a visualization.
 *
 * The result should be a visualization that reflects (2) and (4), but not (3).
 *
 * Created by dbborens on 4/26/14.
 */
public class DeferredRecordingHighlightTest extends EslimeTestCase {

    public void testDeferredRecording() {
        fail("See spec in JavaDoc.");
    }
}
