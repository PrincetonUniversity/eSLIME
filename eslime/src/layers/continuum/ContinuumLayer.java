/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import control.identifiers.Coordinate;
import layers.Layer;
import no.uib.cipr.matrix.Vector;

import java.util.function.Function;

/**
 * Created by dbborens on 12/11/14.
 */
public class ContinuumLayer extends Layer {

    private ContinuumLayerScheduler scheduler;
    private ContinuumLayerContent content;

    private String id;

    public ContinuumLayer(ContinuumLayerScheduler scheduler, ContinuumLayerContent content, String id) {
        this.scheduler = scheduler;
        this.content = content;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void reset() {
        scheduler.reset();
        content.reset();
    }

    public double[] getState() {
        Vector stateVector = content.getState();

        int n = stateVector.size();
        double[] stateArray = new double[n];

        for (int i = 0; i < n; i++) {
            stateArray[i] = stateVector.get(i);
        }

        return stateArray;
    }

    public ContinuumLayerScheduler getScheduler() {
        return scheduler;
    }

    public ContinuumAgentLinker getLinker() {
        Function<Coordinate, Double> stateLookup = c -> content.get(c);
        return scheduler.getLinker(stateLookup);
    }
}
