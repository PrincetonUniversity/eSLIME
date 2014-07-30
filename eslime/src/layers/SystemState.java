/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import control.identifiers.Coordinate;

/**
 * Parent class for objects that can report read-only information on a system
 * state. This is not to be confused with LayerManager, which gives read-write
 * access to the system state. This could be used either for enforcing scope,
 * or for reading final simulation states (eg, from a serialized data structure).
 *
 * @see layers.LayerManager
 * <p/>
 * Created by David B Borenstein on 3/23/14.
 */
public abstract class SystemState {

    /**
     * Retrieves a reconstructed LayerManager object, through which the state of
     * cell layers and solute layers can be accessed. Note that mutating this
     * CellLayer object will have undefined results.
     * @return
     */
    public abstract LayerManager getLayerManager();

    /**
     * Returns the system time associated with this state.
     */
    public abstract double getTime();

    /**
     * Returns the frame number associated with this state.
     *
     * @return
     */
    public abstract int getFrame();

    /**
     * Specifies whether a given site is highlighted on a given highlight
     * channel. (Highlight channels may specify different visual effects
     * in order to distinguish between events that took place simultaneously.)
     *
     * @param channel The highlight channel ID.
     * @param coord   The coordinate whose highlight status is to be checked.
     */
    public abstract boolean isHighlighted(int channel, Coordinate coord);

}
