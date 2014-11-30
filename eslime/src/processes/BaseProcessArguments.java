/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes;

import control.GeneralParameters;
import control.arguments.Argument;
import layers.LayerManager;

/**
 * Created by dbborens on 11/23/14.
 */
public class BaseProcessArguments {
    private GeneralParameters generalParameters;
    private int id;
    private Argument<Integer> start;
    private Argument<Integer> period;

    private LayerManager layerManager;

    public BaseProcessArguments(LayerManager layerManager, GeneralParameters generalParameters, int id, Argument<Integer> start, Argument<Integer> period) {
        this.generalParameters = generalParameters;
        this.id = id;
        this.start = start;
        this.period = period;
        this.layerManager = layerManager;
    }

    public Argument<Integer> getPeriod() {
        return period;
    }

    public Argument<Integer> getStart() {
        return start;
    }

    public int getId() {
        return id;
    }

    public GeneralParameters getGeneralParameters() {
        return generalParameters;
    }

    public LayerManager getLayerManager() {
        return layerManager;
    }


}
