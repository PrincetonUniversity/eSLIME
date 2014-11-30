/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package control.arguments;

import layers.LayerManager;

/**
 * Defines the properties of the layers for the simulation. Can
 * generate LayerManager objects.
 * <p/>
 * Created by dbborens on 11/24/14.
 */
public class LayersDescriptor extends Argument<LayerManager> {


    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public LayerManager next() {
        return null;
    }
}