/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual;

import geometry.Geometry;
import layers.SystemState;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Superclass for defining visualizations based on system state data.
 * The Visualization returns an Image object upon render. Depending on
 * user needs, this visualization may then either be written to disk or
 * to the screen. A single visualization object may be used to generate
 * visualizations from several simulations.
 *
 * Created by David B Borenstein on 3/23/14.
 */
public abstract class Visualization {

    protected VisualizationProperties properties;

    /**
     * Perform any actions necessary before frames may be written, but
     * which have to be called after the constructor.
     *
     * @param geometry The Geometry object from which the lattice
     *                 connectivity, size and shape will be drawn.
     *                 Boundary conditions should be ignored.
     */
    public abstract void init(Geometry geometry, double[] times, int[] frames);

    /**
     * Render a frame.
     *
     * @param systemState
     */
    public abstract Image render(SystemState systemState);

    /**
     * Perform any actions necessary for finalizing the current visualization.
     */
    public abstract void conclude();

    /**
     * Get a list of solute field IDs that are expected for this visualization.
     */
    public abstract String[] getSoluteIds();

    /**
     * Get a list of highlight channels that are expected for this visualization.
     */
    public abstract int[] getHighlightChannels();
}
