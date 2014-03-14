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

package layers.solute;

import geometry.Geometry;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;

public class SoluteViewer {

    private SoluteLayer layer;
    private Geometry geom;

    public SoluteViewer(SoluteLayer layer, Geometry geom) {
        this.layer = layer;
        this.geom = geom;
    }

    public double get(Coordinate coord, boolean relative) {
        if (relative) {
            return layer.getState().getRelative(coord);
        } else {
            return layer.getState().getAbsolute(coord);
        }
    }

    public void push(SolutionViewer state) {
        layer.push(state);
    }
}
