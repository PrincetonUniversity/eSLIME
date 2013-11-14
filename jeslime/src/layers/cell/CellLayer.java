package layers.cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import cells.Cell;
import structural.Flags;
import structural.identifiers.Coordinate;
import geometry.Geometry;

/**
 * 
 * @test CellIntegrationTest
 * @author David Bruce Borenstein
 *
 */
public class CellLayer {

	private CellLayerIndices indices;
	private CellLayerContent content;
	
	// Geometry (passed to Lattice at construction time)
    private Geometry geom;

	public CellLayer(Geometry geom, Integer id) {
		indices = new CellLayerIndices();
		content = new CellLayerContent(geom, indices);
		
		this.geom = geom;
	}

	public CellLookupManager getLookupManager() {
		return new CellLookupManager(geom, content, indices);
	}
	
	public CellUpdateManager getUpdateManager() {
		return new CellUpdateManager(content, indices);
	}
	
	public CellLayerViewer getViewer() {
		return new CellLayerViewer(this, content, indices);
	}
}
