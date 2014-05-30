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

package io.serialize.text;

import cells.Cell;
import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.serialize.Serializer;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.StateMapViewer;
import processes.StepState;
import structural.utilities.FileConventions;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Writes out the number of interfaces that some focal cell type has with
 * each other cell type as a function of time.
 *
 * @author dbborens
 */
public class InterfaceCountWriter extends Serializer {


    private Integer focalState;

    // It is necessary to flush all data at the end of each iteration, rather
    // than after each flush event, because a state may appear for the first
    // time in the middle of the simulation, and we want an accurate column
    // for every observed interface type in the census table.
    ArrayList<Integer> frames;

    // The keys to this map are FRAMES. The values are a mapping from STATE
    // number to count. If a state number does not appear, that means the
    // count was zero at that time.
    HashMap<Integer, HashMap<Integer, Integer>> histo;

    HashSet<Integer> observedInterfaceTypes;

    private BufferedWriter bw;

    public InterfaceCountWriter(GeneralParameters p, Integer focalState) {
        super(p);
        this.focalState = focalState;
    }

    @Override
    public void init(LayerManager lm) {
        super.init(lm);
        histo = new HashMap<>();
        frames = new ArrayList<>();
        observedInterfaceTypes = new HashSet<>();

        String filename = FileConventions.makeInterfaceFilename(focalState);
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);
    }

    @Override
    public void flush(StepState stepState) {
        CellLayer layer = stepState.getRecordedCellLayer();
        frames.add(stepState.getFrame());

        // Create a bucket for this frame.
        HashMap<Integer, Integer> observations = new HashMap<>();
        histo.put(stepState.getFrame(), observations);

        // Iterate over all cells from this frame.
        HashSet<Coordinate> sites = layer.getViewer().getOccupiedSites();

        for (Coordinate c : sites) {
            Cell focus = layer.getViewer().getCell(c);

            // Focal cell type? If not, continue.
            if (focus.getState() != focalState)  {
                continue;
            }

            // Iterate through other neighbors.
            int[] neighborStates = layer.getLookupManager().getNeighborStates(c);

            // We only want to count each type once, so convert to a set.
            HashSet<Integer> observedInterfaces = new HashSet<>();
            for (int neighborState : neighborStates) {
                observedInterfaces.add(neighborState);
            }

            // Look at each type of observed interface...
            for (Integer observedInterface : observedInterfaces) {
                increment(observations, observedInterface);
            }
        }
    }

    private void increment(HashMap<Integer, Integer> observations, Integer observedInterface) {
        if (!observations.containsKey(observedInterface)) {
            observations.put(observedInterface, 0);
        }

        int count = observations.get(observedInterface);

        observations.put(observedInterface, count + 1);
    }

    // !!! CONTINUE CONVERTING FROM HERE

    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

    private void conclude() {
        // Sort the states numerically
        TreeSet<Integer> sortedStates = new TreeSet<>(observedInterfaceTypes);

        // Write out the header
        StringBuilder line = new StringBuilder();
        line.append("frame");

        for (Integer state : sortedStates) {
            line.append("\t");
            line.append(state);
        }

        line.append("\n");

        hAppend(bw, line);

        TreeSet<Integer> sortedFrames = new TreeSet<>(histo.keySet());
        for (Integer frame : sortedFrames) {
            HashMap<Integer, Integer> observations = histo.get(frame);

            line = new StringBuilder();
            line.append(frame);

            for (Integer state : sortedStates) {
                line.append("\t");

                if (observations.containsKey(state)) {
                    line.append(observations.get(state));
                } else {
                    line.append("0");
                }
            }

            line.append("\n");
            hAppend(bw, line);

        }
        hClose(bw);

    }

    public void close() {
        // Doesn't do anything.
    }
}
