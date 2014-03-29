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

package structural.postprocess;

import geometry.Geometry;
import io.deserialize.ConditionViewer;
import io.deserialize.LegacyCellStateReader;
import io.visual.legacy.HexMapWriter;
import structural.GeneralParameters;

public class ImageSequence {

    private Geometry geom;
    private GeneralParameters p;

    private HexMapWriter mapWriter;

    // The simulation being visualized may be a single instance of a multi-
    // replicate project. The ImageSequence is currently agnostic to that fact,
    // so we just pass in a path.
    private String path;

    public ImageSequence(String path, Geometry geom, GeneralParameters p) {
        System.out.print("Initializing image sequence writer...");

        this.geom = geom;
        this.p = p;
        this.path = path;

        mapWriter = new HexMapWriter(p, path, geom);

        System.out.println("done.");
    }

    public void generate() {
        System.out.println("Generating image sequence.");
        LegacyCellStateReader reader = new LegacyCellStateReader(path, geom);

        ConditionViewer condition = reader.next();

        int frame;

        while (condition != null) {
            frame = condition.getFrame();

            if (p.isFrame(frame)) {
                mapWriter.renderImage(condition, frame + ".png");
            }

            condition = reader.next();
        }

    }

}
