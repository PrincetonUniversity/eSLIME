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

package layers.cell;

import geometry.Geometry;
import layers.Layer;

/**
 * @author David Bruce Borenstein
 * @test CellIntegrationTest
 */
public class CellLayer extends Layer {

    private CellLayerContent content;

    public CellLayer(Geometry geom) {
        geometry = geom;

        CellLayerIndices indices = new CellLayerIndices();
        if (geometry.isInfinite()) {
            content = new InfiniteCellLayerContent(geometry, indices);
        } else {
            content = new FiniteCellLayerContent(geometry, indices);
        }
    }

    public CellLookupManager getLookupManager() {
        return new CellLookupManager(geometry, content);
    }

    public CellUpdateManager getUpdateManager() {
        return new CellUpdateManager(content);
    }

    public CellLayerViewer getViewer() {
        return new CellLayerViewer(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellLayer cellLayer = (CellLayer) o;

        if (content != null ? !content.equals(cellLayer.content) : cellLayer.content != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public CellLayer clone() {
        CellLayerContent contentClone = content.clone();
        CellLayer clone = new CellLayer(geometry);
        clone.content = contentClone;
        return clone;
    }

    @Override
    public String getId() {
        return "0";
    }
}
