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

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.factory.DoubleArgumentFactory;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.utilities.XmlUtil;

/**
 * Adds a fixed amount of biomass to every cell.
 *
 * @author dbborens
 */
public class UniformBiomassGrowth extends CellProcess {

    // How much biomass to accumulate per time step
    private Argument<Double> delta;

    // If false, apply() will be called on cells after their
    // biomass is updated. If true, you must call apply() on
    // a cell before the new biomass accumulates.
    private boolean defer;

    public UniformBiomassGrowth(ProcessLoader loader, LayerManager layerManager, int id,
                                GeneralParameters p) {
        super(loader, layerManager, id, p);

//        delta = Double.valueOf(get("delta"));
        delta = DoubleArgumentFactory.instantiate(e, "delta", p.getRandom());
        defer = XmlUtil.getBoolean(e, "defer");

        if (defer) {
            throw new UnsupportedOperationException("Deferred actions are currently disabled.");
        }
    }

    public UniformBiomassGrowth(LayerManager layerManager,
                                double delta, boolean defer) {
        super(null, layerManager, 0, null);

        this.delta = new ConstantDouble(delta);
        this.defer = defer;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        // Feed the cells.
        for (Coordinate site : activeSites) {
            if (layer.getViewer().isOccupied(site)) {
                Cell cell = layer.getViewer().getCell(site);
                layer.getViewer().getCell(site).adjustHealth(delta.next());

            }
        }

        // If we're not defering updates, tell the cells to use the
        // new data right away. Note that we want to do this after
        // every cell has been "fed," in case there are non-local
        // interactions.
        if (!defer) {
            for (Coordinate site : activeSites) {
                if (layer.getViewer().isOccupied(site)) {
                    layer.getUpdateManager().apply(site);
                }
            }
        }
    }

}
