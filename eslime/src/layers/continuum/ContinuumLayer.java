/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.Layer;

import java.util.function.Function;

/**
 * Created by dbborens on 12/11/14.
 */
public class ContinuumLayer extends Layer {

    private ContinuumLayerScheduler scheduler;
    private ContinuumLayerContent content;

    public ContinuumLayer(ContinuumLayerScheduler scheduler, ContinuumLayerContent content, Geometry geometry) {
        this.scheduler = scheduler;
        this.content = content;
        this.geometry = geometry;
    }

    @Override
    public String getId() {
        return scheduler.getId();
    }

    @Override
    public void reset() {
        scheduler.reset();
        content.reset();
    }

    public ContinuumLayerScheduler getScheduler() {
        return scheduler;
    }

    public ContinuumAgentLinker getLinker() {
        Function<Coordinate, Double> stateLookup = c -> content.get(c);
        return scheduler.getLinker(stateLookup);
    }
}
