package io.serialize;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.LayerManager;
import layers.solute.SoluteLayer;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Created by dbborens on 12/11/13.
 */
public abstract class AbstractContinuumWriter extends Writer {

    public AbstractContinuumWriter(LayerManager lm, GeneralParameters p) {
        super(lm, p);
    }

    /**
     * Initialize data structures for an instance.
     * @param l
     */
    public abstract void init(SoluteLayer l);

    /**
     * Indicates to the writer that the simulation has advanced and
     * provides model state for this step.
     *
     * @param gillespie Simulation time.
     *
     * @param frame Logical time (number of time steps).
     */
    public abstract void step(double gillespie, int frame);

    @Override
    public abstract void dispatchHalt(HaltCondition ex);

    @Override
    public abstract void close();

}
