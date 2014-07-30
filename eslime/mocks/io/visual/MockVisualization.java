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
 * Created by David B Borenstein on 4/9/14.
 */
public class MockVisualization extends Visualization {
    private boolean isRender;
    private boolean isConclude;
    private boolean isInit;

    public boolean isRender() {
        return isRender;
    }

    public boolean isConclude() {
        return isConclude;
    }

    public boolean isInit() {
        return isInit;
    }

    public MockVisualization() {
        isRender = false;
        isConclude = false;
        isInit = false;
    }

    @Override
    public void init(Geometry geometry, double[] time, int[] frames) {
        isInit = true;
    }

    @Override
    public BufferedImage render(SystemState systemState) {
        isRender = true;
        return new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
    }

    @Override
    public void conclude() {
        isConclude = true;
    }

    @Override
    public String[] getSoluteIds() {
        return new String[0];
    }

    @Override
    public int[] getHighlightChannels() {
        return new int[0];
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockVisualization);
    }
}
