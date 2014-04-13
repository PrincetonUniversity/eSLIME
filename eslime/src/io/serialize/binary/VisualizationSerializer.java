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

package io.serialize.binary;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.deserialize.SystemStateReader;
import io.serialize.Serializer;
import io.visual.Visualization;
import layers.SystemState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by dbborens on 4/2/14.
 */
public class VisualizationSerializer extends Serializer {

    // The visualization to render
    private Visualization visualization;

    // Leading part of the file name (after the instance path)
    private String prefix;

    // Output mode. Supported JPEG, PNG, BMP, WBMP and and GIF.
    // See Java's ImageIO class for more information.
    private String mode;

    public VisualizationSerializer(GeneralParameters p,
                                   Visualization visualization,
                                   String prefix, String mode) {
        super(p);
        this.visualization = visualization;
        this.prefix = prefix;
        this.mode = mode;
    }

    public VisualizationSerializer(GeneralParameters p,
                                   Visualization visualization,
                                   String prefix, String mode,
                                   String outputPath) {
        super(p);
        this.visualization = visualization;
        this.prefix = prefix;
        this.mode = mode;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // Initialize the visualization to this simulation.
        visualization.init(layerManager.getCellLayer().getGeometry());

        // Get expected fields.
        String[] soluteIds = visualization.getSoluteIds();
        int[] highlightChannels = visualization.getHighlightChannels();

        // Create a SystemStateReader.
        SystemStateReader reader = new SystemStateReader(soluteIds,
                highlightChannels, p.getInstancePath());

        // Scan through the frames...
        for (SystemState systemState : reader) {
            // Render the frame.
            BufferedImage image = visualization.render(systemState);

            // Export the frame to the disk.
            generateFile(systemState, image);
        }

        visualization.conclude();
    }

    private void generateFile(SystemState systemState, BufferedImage image) {
        String fileName = buildFileName(systemState.getTime());
        File file = new File(fileName);
        try {
            ImageIO.write(image, mode, file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String buildFileName(double time) {
        StringBuilder builder = new StringBuilder();
        builder.append(p.getInstancePath());
        builder.append(prefix);
        builder.append(time);
        builder.append(".");
        builder.append(mode.toLowerCase());
        return builder.toString();
    }

    @Override
    public void close() {
        // Doesn't do anything
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        // Doesn't do anything
    }
}
