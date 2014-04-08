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

package geometry.lattice;

import control.identifiers.Coordinate;

public class MockLattice extends Lattice {

    @Override
    protected void defineBasis() {
        // TODO Auto-generated method stub

    }

    private int connectivity;

    public void setConnectivity(int c) {
        connectivity = c;
    }

    @Override
    public int getConnectivity() {
        return connectivity;
    }

    private int dimensionality;

    public void setDimensionality(int d) {
        dimensionality = d;
    }

    @Override
    public int getDimensionality() {
        return dimensionality;
    }

    @Override
    public Coordinate adjust(Coordinate toAdjust) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate invAdjust(Coordinate toAdjust) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate[] getAnnulus(Coordinate coord, int r) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate getDisplacement(Coordinate pCoord, Coordinate qCoord) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate getOrthoDisplacement(Coordinate pCoord, Coordinate qCoord) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate rel2abs(Coordinate coord, Coordinate displacement) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Lattice clone() {
        return new MockLattice();
    }
}
