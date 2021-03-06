/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.lattice;

import control.identifiers.Coordinate;

public class MockLattice extends Lattice {

    private Coordinate zeroVector;

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

    @Override
    public Coordinate getZeroVector() {
        return zeroVector;
    }

    public void setZeroVector(Coordinate zeroVector) {
        this.zeroVector = zeroVector;
    }
}
