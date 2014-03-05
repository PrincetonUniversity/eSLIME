/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package layers.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import cells.Cell;
import layers.Layer;
import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.Geometry;

/**
 * 
 * @test CellIntegrationTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayer extends Layer {

	private CellLayerIndices indices;
	private CellLayerContent content;

    private String id;

	public CellLayer(Geometry geom, int id) {
        geometry = geom;
        this.id = Integer.toString(id);
		indices = new CellLayerIndices();
		content = new CellLayerContent(geometry, indices);
	}

    @Override
    public String getId() {
        return id;
    }

    public CellLookupManager getLookupManager() {
		return new CellLookupManager(geometry, content, indices);
	}
	
	public CellUpdateManager getUpdateManager() {
		return new CellUpdateManager(content, indices);
	}
	
	public CellLayerViewer getViewer() {
		return new CellLayerViewer(this, content, indices);
	}

}
