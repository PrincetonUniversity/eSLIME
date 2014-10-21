/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
    private int edge;

    // How wide should any outlines be?
    private int outline;

    // Temporal information
    private int[] frames;
    private double[] times;

    public VisualizationProperties(ColorManager colorManager, int edge,
                                   int outline) {
        if (outline > 1) {
            throw new UnsupportedOperationException("Thick outlines not " +
                    "yet supported");
        }
        this.colorManager = colorManager;
        this.edge = edge;
        this.outline = outline;
    }

    public int[] getFrames() {
        return frames;
    }

    public void setFrames(int[] frames) {
        this.frames = frames;
    }

    public double[] getTimes() {
        return times;
    }

    public void setTimes(double[] times) {
        this.times = times;
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

    public int getEdge() {
        return edge;
    }

    public int[] getChannels() {
        return new int[0];
    }

    public int getOutline() {
        return outline;
    }

}
