package io;

import geometries.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import structural.identifiers.Coordinate;

import control.Parameters;

/**
 * 
 * Copyright (c) 2013, David Bruce Borenstein.
 * 
 * This work is licensed under the Creative Commons 2.0 BY-NC license.
 * 
 * Attribute (BY) -- You must attribute the work in the manner specified 
 * by the author or licensor (but not in any way that suggests that they 
 * endorse you or your use of the work).
 * 
 * Noncommercial (NC) -- You may not use this work for commercial purposes.
 * 
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc/3.0/legalcode
 * 
 * 
 * Displays the state of up to 3 scalar fields in a composite heat map.
 * Results are written to file. 
 * 
 * @author dbborens@princeton.edu
 *
 */
public class HexMapWriter {
	
	private static final int EDGE = 6;
	
	private Point origin = new Point();
	private Point limits = new Point();
	private Point pixels = new Point();
	
	Parameters p;
	Geometry geom;
	
	public HexMapWriter(Parameters p) {
		this.p = p;
		geom = new HexRing(p.W(), p.W());
		init();
	}
	
	public void renderImage(ConditionViewer condition, String filename) {
		BufferedImage img = buildImage(condition);
		export(img, filename);
	}
	
	private void export(BufferedImage img, String filename) {
		File f = new File(p.getPath() + filename);
		
		try {
			ImageIO.write(img,  "png", f);
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
		BufferedImage img = new BufferedImage(pixels.x, pixels.y, BufferedImage.TYPE_INT_RGB);
		Graphics gfx = img.getGraphics();
				
		for (Coordinate c : geom.getCanonicalSites()) {
			render(gfx, c, condition);
		}
		
		return img;
	}
	
    private void render(Graphics gfx, Coordinate c, ConditionViewer condition) {
		Point center = indexToPixels(c);
		Color color = getColor(condition.getState(c));
		
		if (condition.isVacant(c)) {
			drawOutlineHexagon(gfx, center);
		} else {
			drawFilledHexagon(gfx, center, color);
		}
	}
    
    private void drawOutlineHexagon(Graphics g, Point center) {
    	
		Polygon p = makeHexagon(center);
		
		// Provide outline
		g.setColor(Color.DARK_GRAY);
		g.drawPolygon(p);		
		
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
        Point target = new Point();;

    	int x = c.x();
    	int y = c.y();

        Point center = indexToOffset(x, y);

        target.x = origin.x + center.x;
        target.y = pixels.y - origin.y - center.y;

        return(target);
	}

	private Point indexToOffset(int x, int y) {
	    Point target = new Point();

	    float xOffsetFP = EDGE * (float) x * 1.5f;
	    target.x = Math.round(xOffsetFP);

	    float yOffsetFP = (-0.5f * (float) Math.sqrt(3.0f) * EDGE * (float) x)
	        + ((float) Math.sqrt(3.0f) * EDGE * (float) y);
	    target.y = Math.round(yOffsetFP);

	    return(target);
	}

	private Color getColor(int state) {
		return Color.WHITE;
    }
    
	private void calcDimensions() {
    // Find limits of coordinate range
	Coordinate[] sites = geom.getCanonicalSites();

	int xMin = 2147483647;
	int xMax = -2147483648;
	
	int yMin = 2147483647;
	int yMax = -2147483648;

    for (int i = 0; i <	sites.length; i++) {
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
	if ((xMin != 0)||(yMin != 0)) {
		throw new IllegalStateException("Negative coordinate or no coordinates loaded.");
	}
	
	// Hexagonal lattice: height value depends on width.
    limits.x = xMax - xMin;
    limits.y = (yMax - yMin) - (limits.x / 2);

	// Calculate pixel bounds.
    float heightFP = (float) Math.sqrt(3.0f) * ((float) (limits.y) + 0.5f) * EDGE;
    float widthFP  = 1.5f * ((float) (limits.x) + 0.5f) * EDGE;

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

		public Point() {}
	}
	
}