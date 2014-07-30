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
