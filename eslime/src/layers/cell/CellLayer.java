/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.cell;

import geometry.Geometry;
import geometry.boundaries.HaltBoundary;
import layers.Layer;

/**
 * @author David Bruce Borenstein
 * @test CellIntegrationTest
 */
public class CellLayer extends Layer {

    protected CellLayerContent content;
    public CellLayer(Geometry geom) {
        geometry = geom;
        reset();
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

    @Override
    public void reset() {
        CellLayerIndices indices = new CellLayerIndices();

        // Oh man, do I hate the following two lines and the
        // "getComponentClasses" cloodge that makes them possible
        Class boundaryClass = geometry.getComponentClasses()[2];
        if (HaltBoundary.class.isAssignableFrom(boundaryClass)) {
            content = new HaltCellLayerContent(geometry, indices);
        } else if (geometry.isInfinite()) {
            content = new InfiniteCellLayerContent(geometry, indices);
        } else {
            content = new FiniteCellLayerContent(geometry, indices);
        }
    }
}
