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

package layers.cell;

import cells.Cell;
import control.identifiers.Coordinate;

/**
 * @author David Bruce Borenstein
 */
public class CellUpdateManager {
    protected CellLayerContent content;

    public CellUpdateManager(CellLayerContent content) {
        this.content = content;
    }

    /**
     * Instructs the specified cell to calculate its next state. Returns number
     * of calls to consider since last call to apply (including this one). This
     * should not change the apparent state of the cell. Therefore no indices
     * are updated.
     *
     * @param coord
     * @return
     */
    public int consider(Coordinate coord) {
        Cell cell = content.get(coord);
        int res = cell.consider();
        return res;
    }

    /**
     * Instructs the specified cell to update its state. Should blow up if
     * the cell has not called consider since last apply call. You should
     * USE THIS METHOD for updating cells, rather than doing so directly.
     *
     * @param coord
     */
    public void apply(Coordinate coord) {
        content.sanityCheck(coord);
        Cell cell = content.get(coord);
        content.remove(coord);

        cell.apply();
        content.put(coord, cell);
    }

    /**
     * Request a daughter cell from the specified cell, and place it at the
     * specified location. divideTo(...) may trigger physical state changes
     * on the part of the parent cell.
     * <p/>
     * Note: the following methods return false if unsuccessful for expected
     * reasons (e.g., lattice full). The definiton of an "expected reason"
     * depends on the cell type. Such events typically are used to signal
     * that the simulation is over. However, they are designed to throw
     * exceptions if something erroneous occurs, such as a call to a null
     * cell. Note however that "position occupied" is a handled condition.
     * If this should be considered erroneous, an exception should be thrown
     * by the calling function.
     *
     * @param pCoord
     * @param cCoord
     */
    public void divideTo(Coordinate pCoord, Coordinate cCoord) {
        content.sanityCheck(pCoord);
        content.sanityCheck(cCoord);

        // Note: divide(...) updates state index for parent
        Cell child = divide(pCoord);

        // Attempt to place child
        // Note: place(...) updates state index for child
        place(child, cCoord);

    }

    // TODO: The exposure of this method is a bit of cloodge for the shoving
    // method. There's no obvious way around it as things stand, but it does
    // suggest that a refactor may soon be necessary.
    public Cell divide(Coordinate pCoord) {
        content.sanityCheck(pCoord);

        // Divide parent
        Cell parent = content.get(pCoord);

        // Remove the parent from the map. After it divides, we will place
        // it again. This will update the indices.
        content.remove(pCoord);

        // Perform the division.
        Cell child = parent.divide();

        // Place the parent, whose state may have changed as a result of the
        // division event.
        content.put(pCoord, parent);

        // Return the child.
        return child;
    }

    /**
     * Place the specified cell at the specified coordinate.
     *
     * @param cell
     * @param coord
     */
    public void place(Cell cell, Coordinate coord) {
        content.sanityCheck(coord);

        if (content.has(coord)) {
            throw new IllegalStateException("Attempting to place a cell into " +
                    "an occupied site at " + coord.toString() + ".");
        }

        // Place cell in cell lattice
        content.put(coord, cell);
    }

    /**
     * Remove the specified cell from the lattice.
     *
     * @param coord
     */
    public void banish(Coordinate coord) {
        content.sanityCheck(coord);

        if (!content.has(coord)) {
            throw new IllegalStateException("Tried to banish non-existent cell.");
        }

        content.remove(coord);
    }

    /**
     * Relocate the cell at the first coordinate to the second coordinate.
     * This method blows up if the target site is occupied.
     *
     * @param pCoord
     * @param qCoord
     */
    public void move(Coordinate pCoord, Coordinate qCoord) {

        if (content.has(qCoord)) {
            throw new IllegalStateException("Attempted to move cell to an " +
                    "occupied site. Origin: " + pCoord + "; destination: "
                    + qCoord);
        }
        content.sanityCheck(pCoord);
        content.sanityCheck(qCoord);

        Cell cell = content.get(pCoord);

        content.remove(pCoord);
        content.put(qCoord, cell);
    }

    /**
     * Swap the cells at the specified locations.
     *
     * @param pCoord
     * @param qCoord
     */
    public void swap(Coordinate pCoord, Coordinate qCoord) {
        content.sanityCheck(pCoord);
        content.sanityCheck(qCoord);

        // Identify cells
        Cell p = content.get(pCoord);
        Cell q = content.get(qCoord);

        // Clear both sites
        content.remove(pCoord);
        content.remove(qCoord);

        // Place each cell in the other's site
        content.put(pCoord, q);
        content.put(qCoord, p);
    }

}
