/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package structural;

import control.GeneralParameters;

import java.util.Random;

/**
 * Created by dbborens on 12/10/13.
 */
public class MockGeneralParameters extends GeneralParameters {

    private boolean isFrameValue;
    private String instancePath;
    private String path;
    private int T;
    private int numInstances;
    private int instance;

    public MockGeneralParameters() {
        super();
    }

    public String getInstancePath() {
        return instancePath;
    }

    public void setInstancePath(String instancePath) {
        this.instancePath = instancePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int T() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    @Override
    public String getProjectXML() {
        return "";
    }

    public void initializeRandom(long randomSeed) {
        random = new Random(randomSeed);
        this.randomSeed = randomSeed;
    }

    public void setIsFrameValue(boolean isFrameValue) {
        this.isFrameValue = isFrameValue;
    }

    @Override
    public int getNumInstances() {
        return numInstances;
    }

    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
    }

    @Override
    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public void setFrameValue(boolean isFrameValue) {
        this.isFrameValue = isFrameValue;
    }

    public void setRandom(MockRandom random) {
        this.random = random;
    }
}
