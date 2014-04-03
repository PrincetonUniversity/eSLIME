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

package io.visual.highlight;

import io.visual.glyph.Glyph;
import io.visual.glyph.GlyphFactory;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class HighlightManagerFactory {
    public static HighlightManager instantiate(Element highlightRoot) {
        HighlightManager renderer = new HighlightManager();
        for (Object o : highlightRoot.elements()) {
            loadGlyph(renderer, (Element) o);
        }
        return renderer;
    }

    private static void loadGlyph(HighlightManager renderer, Element node) {
        Integer channel = getChannel(node);
        Element glyphNode = node.element("glyph");
        Glyph glyph = GlyphFactory.instantiate(glyphNode);
        renderer.setGlyph(channel, glyph);
    }

    private static Integer getChannel(Element node) {
        Attribute indexAttrib = node.attribute("index");
        String indexText = indexAttrib.getText();
        Integer channel = Integer.valueOf(indexText);
        return channel;
    }

}
