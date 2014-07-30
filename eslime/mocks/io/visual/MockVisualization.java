/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
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
