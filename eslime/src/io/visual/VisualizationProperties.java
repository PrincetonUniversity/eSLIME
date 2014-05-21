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

import control.identifiers.Coordinate;
import io.visual.color.ColorManager;
import io.visual.highlight.HighlightManager;
import structural.utilities.EpsilonUtil;

import java.util.Arrays;

/**
 * Container for all state members associated with a map visualization.
 * <p/>
 * Created by dbborens on 4/1/14.
 */
public class VisualizationProperties {

    // Set of all coordinates to be considered.
    private Coordinate[] coordinates;

    // Helper object for setting colors.
    private ColorManager colorManager;

    // Imposes highlights on the map
    private HighlightManager highlightManager;

    // How long each edge should be. Sets visual scale.
    private double edge;

    public void setFrames(int[] frames) {
        this.frames = frames;
    }

    public void setTimes(double[] times) {
        this.times = times;
    }

    public int[] getFrames() {
        return frames;
    }

    public double[] getTimes() {
        return times;
    }

    // Temporal information
    private int[] frames;
    private double[] times;

    public VisualizationProperties(ColorManager colorManager, double edge) {
        this.colorManager = colorManager;
        this.edge = edge;
    }

    public HighlightManager getHighlightManager() {
        return highlightManager;
    }

    public void setHighlightManager(HighlightManager highlightManager) {
        this.highlightManager = highlightManager;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VisualizationProperties)) {
            return false;
        }

        VisualizationProperties other = (VisualizationProperties) obj;

        if (!Arrays.equals(coordinates, other.coordinates)) {
            return false;
        }

        if (!colorManager.equals(other.colorManager)) {
            return false;
        }

        if (!highlightManager.equals(other.highlightManager)) {
            return false;
        }

        if (!EpsilonUtil.epsilonEquals(edge, other.edge)) {
            return false;
        }

        return true;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public double getEdge() {
        return edge;
    }

    public int[] getChannels() {
        return new int[0];
    }
}
