/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.io.visual.highlight;

import factory.io.visual.glyph.GlyphFactory;
import io.visual.glyph.Glyph;
import io.visual.highlight.HighlightManager;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Created by dbborens on 4/3/14.
 */
public abstract class HighlightManagerFactory {
    public static HighlightManager instantiate(Element highlightRoot) {
        HighlightManager renderer = new HighlightManager();

        // No highlight tag? Fine. Return the empty manager.
        if (highlightRoot == null) {
            return renderer;
        }

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
