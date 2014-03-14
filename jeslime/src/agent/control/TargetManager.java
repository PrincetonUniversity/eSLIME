/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package agent.control;

import cells.Cell;
import layers.LayerManager;
import org.dom4j.Element;

/**
 * Resolves a target descriptor at construction time and returns
 * a list of target Cells on demand.
 * <p/>
 * Created by dbborens on 2/7/14.
 */
public class TargetManager {

    private Cell callback;
    private LayerManager layerManager;

    public TargetManager(Cell callback, LayerManager layerManager, Element descriptor) {
        this.callback = callback;
        this.layerManager = layerManager;


        // Load a targetter

    }

    // Create a passthrough for target.report()
}
