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

package io.visual.glyph;

import control.identifiers.Coordinate;
import io.visual.map.PixelTranslator;

import java.awt.*;

/**
 * A glyph is an overlay onto a lattice cell. It is used for highlighting in
 * the context of eSLIME visualizations. Each subclass defines a glyph that will
 * be used in a particular visualization. The overlay() method then puts this
 * glyph onto a particular site.
 * <p/>
 * Created by dbborens on 4/1/14.
 */
public abstract class Glyph {

    // The pixel translator converts coordinates to pixels.
    protected PixelTranslator translator;

    protected Graphics2D graphics;

    public void init(PixelTranslator translator) {
        this.translator = translator;
        internalInit();
    }

    /**
     * Set the Graphics object to which the glyphs should be written.
     */
    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }

    /**
     * Draw the glyph at the specified site.
     *
     * @param c Coordinate (in units of cells) of the site to be overlaid
     *          with the image.
     */
    public abstract void overlay(Coordinate c, int frame, double time);

    @Override
    public abstract boolean equals(Object obj);

    protected abstract void internalInit();

    protected int calcProportionalSize(double size) {
        // The length of a diagonal of the cell polygon.
        double diagonal = translator.getDiagonal();

        double scaled = diagonal * size / 2.0;
        return (int) scaled;
    }
}
