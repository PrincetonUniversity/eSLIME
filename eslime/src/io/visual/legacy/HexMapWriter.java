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

package io.visual.legacy;

import geometry.Geometry;
import io.deserialize.ConditionViewer;
import structural.GeneralParameters;
import structural.identifiers.Coordinate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;


@Deprecated
public class HexMapWriter implements LegacyVisualization {

    private static final int EDGE = 10;

    private Point origin = new Point();
    private Point limits = new Point();
    private Point pixels = new Point();

    private Geometry geom;
    private LegacyColorManager colorManager;

    // For now, this hidden feature is for debug only
    private boolean showNumbers = false;

    // The path may not match any in the parameters passed to this
    // object, so we just track one.
    private String path;

    public HexMapWriter(GeneralParameters p, String path, Geometry geom) {
        this.path = path;
        this.geom = geom;

        colorManager = new LegacyColorManager(p);
        init();
    }

    public void renderImage(ConditionViewer condition, String filename) {
        BufferedImage img = buildImage(condition);
        export(img, filename);
    }

    private void export(BufferedImage img, String filename) {
        File f = new File(path + filename);

        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Initialize graphics state
    private void init() {

        calcDimensions();

        origin.x = Math.round(EDGE);
        origin.y = (int) Math.round(EDGE * Math.sqrt(3.0f));

    }

    private BufferedImage buildImage(ConditionViewer condition) {
        BufferedImage img = new BufferedImage(pixels.x, pixels.y, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics gfx = img.getGraphics();

        for (Coordinate c : geom.getCanonicalSites()) {
            render(gfx, c, condition);
        }

        return img;
    }

    private void render(Graphics gfx, Coordinate c, ConditionViewer condition) {
        Point center = indexToPixels(c);

        boolean isHighlight = checkHighlight(c, condition);
        Color color = getColor(condition.getState(c), isHighlight);

        if (condition.isVacant(c)) {
            drawOutlineHexagon(gfx, center);
        } else {
            drawFilledHexagon(gfx, center, color);
        }

        if (showNumbers) {
            makeLabel(c, gfx);
        }
    }

    private boolean checkHighlight(Coordinate c, ConditionViewer condition) {
        HashSet<Coordinate> highlights = condition.getHighlights();
        return highlights.contains(c);
    }

    private void drawOutlineHexagon(Graphics g, Point center) {

        Polygon p = makeHexagon(center);

        // Provide outline
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(p);

    }

    private void makeLabel(Coordinate coord, Graphics g) {
        Point c = indexToPixels(coord);

        String label = "(" + coord.x() + ", " + coord.y() + ")";
        char[] labelArr = label.toCharArray();


        int wAdj = g.getFontMetrics().charsWidth(labelArr, 0, labelArr.length) / 2;
        int hAdj = g.getFontMetrics().getHeight() / 2;

        g.setColor(Color.DARK_GRAY);
        g.drawChars(labelArr, 0, labelArr.length, c.x - wAdj, c.y + hAdj);
    }

    void drawFilledHexagon(Graphics g, Point center, Color color) {

        Polygon p = makeHexagon(center);

        // Flood region with background color
        g.setColor(color);
        g.fillPolygon(p);

        // Provide outline
        g.setColor(Color.DARK_GRAY);
        g.drawPolygon(p);

    }

    private Polygon makeHexagon(Point c) {
        Polygon p = new Polygon();
        for (double theta = 0d; theta < 360d; theta += 60d) {
            double rad = (theta / 180) * Math.PI;
            int x1 = (int) Math.round(c.x + EDGE * Math.cos(rad));
            int y1 = (int) Math.round(c.y + EDGE * Math.sin(rad));
            p.addPoint(x1, y1);
        }

        return p;

    }

    private Point indexToPixels(Coordinate c) {
        Point target = new Point();
        ;

        int x = c.x();
        int y = c.y();

        Point center = indexToOffset(x, y);

        target.x = origin.x + center.x;
        target.y = pixels.y - origin.y - center.y;

        return (target);
    }

    private Point indexToOffset(int x, int y) {
        Point target = new Point();

        float xOffsetFP = EDGE * (float) x * 1.5f;
        target.x = Math.round(xOffsetFP);

        float yOffsetFP = (-0.5f * (float) Math.sqrt(3.0f) * EDGE * (float) x)
                + ((float) Math.sqrt(3.0f) * EDGE * (float) y);
        target.y = Math.round(yOffsetFP);

        return (target);
    }

    private Color getColor(int state, boolean isHighlight) {
        if (isHighlight) {
            return colorManager.highlightColor(state);
        } else {
            return colorManager.basicColor(state);
        }
    }

    private void calcDimensions() {
        // Find limits of coordinate range
        Coordinate[] sites = geom.getCanonicalSites();

        int xMin = 2147483647;
        int xMax = -2147483648;

        int yMin = 2147483647;
        int yMax = -2147483648;

        for (int i = 0; i < sites.length; i++) {
            Coordinate coord = sites[i];

            int x = coord.x();
            int y = coord.y();

            if (x < xMin) {
                xMin = x;
            }

            if (x > xMax) {
                xMax = x;
            }

            if (y < yMin) {
                yMin = y;
            }

            if (y > yMax) {
                yMax = y;
            }
        }

        // We expect only positive or zero coordinates.
        if ((xMin != 0) || (yMin != 0)) {
            throw new IllegalStateException("Negative coordinate or no coordinates loaded.");
        }

        // Hexagonal lattice: height value depends on width.
        limits.x = xMax - xMin;
        limits.y = (yMax - yMin) - (limits.x / 2);

        // Calculate pixel bounds.
        float heightFP = (float) Math.sqrt(3.0f) * ((float) (limits.y) + 1.5f) * EDGE;
        float widthFP = 1.5f * ((float) (limits.x) + 1.5f) * EDGE;

        pixels.x = (int) Math.ceil(widthFP);
        pixels.y = (int) Math.ceil(heightFP);
    }

    protected class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point() {
        }
    }

}