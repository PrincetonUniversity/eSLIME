/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes;

import control.identifiers.Coordinate;
import layers.cell.CellLayer;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 4/20/14.
 */
public class MockStepState extends StepState {

    public MockStepState() {
        this(0.0);
    }

    public MockStepState(double startTime) {
        this(startTime, 0);
    }

    private HashMap<Integer, Coordinate[]> highlightMap;

    public MockStepState(double startTime, int frame) {
        super(startTime, frame);
        highlightMap = new HashMap<>();
    }

    @Override
    public Coordinate[] getHighlights(Integer channel) {
        return highlightMap.get(channel);
    }

    boolean record = false;

    @Override
    public void record(CellLayer cellLayer) {
        super.record(cellLayer);
        record = true;
    }

    public boolean isRecorded() {
        return record;
    }

    public void setHighlights(Integer channel, Coordinate[] highlights) {
        highlightMap.put(channel, highlights);
    }

    public void setRecord(boolean record) {
        this.record = record;
    }
}
