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

package io.serialize.text;

import control.GeneralParameters;
import control.arguments.Argument;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import io.serialize.Serializer;
import layers.LayerManager;
import layers.cell.CellLayer;
import processes.StepState;
import structural.utilities.EpsilonUtil;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Calculate the pairwise correlation between individuals at every distance.
 * This calculation is used to measure spatial structure. The results are
 * measured at a specified time point, and aggregated over all simulations.
 *
 * Created by dbborens on 4/22/14.
 */
public class CorrelationWriter extends Serializer {

    private double triggerTime;

    //The number of identity observations as a function of L1 distance.
    private HashMap<Integer, Double> identity;

    //The total number of number of observations at each L1 distance.
    private HashMap<Integer, Double> observations;

    // Indicates whether the analysis has occurred yet for this simulation.
    private boolean fired;

    private String filename;
    /**
     *
     * @param p
     * @param triggerTimeArg The minimum time at which the RDF should be run.
     *                    Once it is run, it will not be run a second time
     *                    for the same simulation. Multiple RDF serializers
     *                    can be included for the same model!
     */
    public CorrelationWriter(GeneralParameters p, String filename, Argument<Double> triggerTimeArg) {
        super(p);
        identity = new HashMap<>();
        observations = new HashMap<>();
        this.triggerTime = triggerTimeArg.next();
        this.filename = filename;
    }

    @Override
    public void init(LayerManager layerManager) {
        super.init(layerManager);
        fired = false;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
    }

    private TreeMap<Integer, Double> calcCorrelations() {
        TreeMap<Integer, Double> corMap = new TreeMap<>();

        for (Integer r : observations.keySet()) {
            double x = getIdentities(r);
            double n = observations.get(r);

            double p = x / n;
            double cor = (p * 2.0) - 1.0;
            corMap.put(r, cor);
        }
        return corMap;
    }

    private double getIdentities(Integer r) {
        double x;
        if (!identity.containsKey(r)) {
            x = 0.0;
        } else {
            x = identity.get(r);
        }

        return x;
    }

    @Override
    public void close() {

        TreeMap<Integer, Double> correlations = calcCorrelations();

        StringBuilder sb = new StringBuilder();
        // Write a header row
        for (Integer r : correlations.keySet()) {
            sb.append("\t");
            sb.append(r);
        }
        sb.append("\n");
        sb.append(triggerTime);

        // Write each entry
        for (Integer r : correlations.keySet()) {
            sb.append("\t");
            sb.append(correlations.get(r));
        }

        // Write the output to a file
        String path = p.getInstancePath() + '/' + filename;
        mkDir(p.getInstancePath(), true);
        BufferedWriter writer = makeBufferedWriter(path);
        hAppend(writer, sb);
        hClose(writer);
    }

    @Override
    public void flush(StepState stepState) {
        // Has the analysis fired yet? If so, return.
        if (fired) {
            return;
        }

        // Is it time to fire yet? If not, return.
        if (stepState.getTime() < triggerTime) {
            return;
        }

        CellLayer layer = stepState.getRecordedCellLayer();
        Geometry geom = layer.getGeometry();
        Coordinate[] cc = geom.getCanonicalSites();
        // Iterate over all canonical sites.
        for (Coordinate i : cc) {
            // For each canonical site, iterate over all canonical sites.
            for (Coordinate j : cc) {
                recordObservation(i, j, layer);
            }
        }
        // Mark the analysis event as having fired.
        fired = true;
    }

    private void recordObservation(Coordinate i, Coordinate j, CellLayer l) {

        // Calculate L1 distance r.
        int r = l.getGeometry().getL1Distance(i, j, Geometry.IGNORE_BOUNDARIES);

        int iState = l.getViewer().getState(i);
        int jState = l.getViewer().getState(j);

        // If identical, record an identity at distance r.
        if (iState == jState) {
            increment(identity, r);
        }

        // Record that an observation occurred.
        increment(observations, r);

    }

    private void increment(HashMap<Integer, Double> map, Integer key) {
        if (!map.containsKey(key)) {
            map.put(key, 0.0);
        }

        Double current = map.get(key);

        map.put(key, current + 1.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CorrelationWriter)) {
            return false;
        }

        CorrelationWriter other = (CorrelationWriter) obj;
        if (!EpsilonUtil.epsilonEquals(triggerTime, other.triggerTime)) {
            return false;
        }

        if (!filename.equals(other.filename)) {
            return false;
        }

        return true;
    }
}
