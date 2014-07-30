/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import java.awt.*;

/**
 * Integration test of the dot glyph (a filled dot at the center of the cell).
 * <p/>
 * Created by dbborens on 4/3/14.
 */
public class DotGlyphTest extends GlyphTest {

    @Override
    protected Glyph makeGlyph() {
        return new DotGlyph(Color.YELLOW, 0.1);
    }

    @Override
    protected String getFileName() {
        return "dotGlyph.png";
    }
}
