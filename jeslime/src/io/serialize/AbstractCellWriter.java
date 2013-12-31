package io.serialize;

import geometry.Geometry;

import io.project.GeometryManager;
import layers.cell.CellLayer;
import structural.GeneralParameters;
import structural.identifiers.Coordinate;

/**
 * Contains convenience methods for 
 * @author dbborens
 *
 */
public abstract class AbstractCellWriter extends Writer {
	protected CellLayer layer;

    public AbstractCellWriter(GeneralParameters, GeometryManager gm) {
        super(gm, p);
    }
	
	/**
	 * Initialize data structures for an instance.
	 * @param l 
	 */
	public abstract void init(CellLayer l);

    /**
     * Step to the next frame of the current instance.
     */
    public abstract void step(Coordinate[] highlights, double gillespie, int frame);
}
