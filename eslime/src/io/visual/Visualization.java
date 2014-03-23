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

package io.visual;

import geometry.Geometry;
import layers.SystemState;

import java.awt.image.BufferedImage;

/**
 * Superclass for defining visualizations based on system state data.
 * The Visualization returns an Image object upon render. Depending on
 * user needs, this visualization may then either be written to disk or
 * to the screen.
 *
 * Created by David B Borenstein on 3/23/14.
 */
public interface Visualization {

    /**
     * Perform any actions necessary before frames may be written, but
     * which have to be called after the constructor.
     *
     * @param geometry The Geometry object from which the lattice
     *                 connectivity, size and shape will be drawn.
     *                 Boundary conditions should be ignored.
     */
    public abstract void init(Geometry geometry);

    /**
     * Render a frame.
     *
     * @param systemState
     */
    public abstract BufferedImage render(SystemState systemState);

    /**
     * Perform any actions necessary for finalizing the process.
     */
    public abstract void conclude();
}
