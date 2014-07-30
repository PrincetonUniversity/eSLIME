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

import control.GeneralParameters;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Deprecated
public class LegacyColorManager {

    private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

    private Random random;

    public LegacyColorManager(GeneralParameters p) {
        random = p.getRandom();
    }

    public Color basicColor(int state) {

        if (!colorMap.containsKey(state)) {
            makeColor(state);
        }

        return colorMap.get(state);

    }

    private void makeColor(int state) {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        Color c = new Color(r, g, b);

        colorMap.put(state, c);
    }

    public Color highlightColor(int state) {
        Color basic = basicColor(state);

        int r0 = basic.getRed();
        int g0 = basic.getGreen();
        int b0 = basic.getBlue();

        int r1 = ((255 - r0) * 3 / 4) + r0;
        int g1 = ((255 - g0) * 3 / 4) + g0;
        int b1 = ((255 - b0) * 3 / 4) + b0;

        Color highlight = new Color(r1, g1, b1);

        return highlight;
    }
}
