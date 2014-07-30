/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import control.identifiers.Coordinate;

/**
 * Created by dbborens on 4/2/14.
 */
public class MockSystemState extends SystemState {

    private boolean highlighted;

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public void setLayerManager(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    private LayerManager layerManager;

    public MockSystemState() {
    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public int getFrame() {
        return 0;
    }

    public void setHighlighted(boolean value) {
        highlighted = value;
    }

    @Override
    public boolean isHighlighted(int channel, Coordinate coord) {
        return highlighted;
    }

}
