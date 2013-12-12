package io.serialize;

import geometry.Geometry;
import layers.solute.SoluteLayer;
import structural.GeneralParameters;
import structural.halt.HaltCondition;

/**
 * Created by dbborens on 12/11/13.
 */
public abstract class AbstractContinuumWriter extends Writer {

    public AbstractContinuumWriter(Geometry geometry, GeneralParameters p) {
        super(geometry, p);
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
     * @param values The concentration of the solute at every coordinate, in
     *               canonical site order.
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
