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

package structural;

import java.util.Random;

/**
 * Created by dbborens on 12/10/13.
 */
public class MockGeneralParameters extends GeneralParameters {

    public MockGeneralParameters() {
        super();
    }

    /* Instance path */

    private String instancePath;

    public String getInstancePath() {
        return instancePath;
    }

    public void setInstancePath(String instancePath) {
        this.instancePath = instancePath;
    }

    /* Path */

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int T() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    private int T;

    @Override
    public String getProjectXML() {
        return "";
    }

    public void initializeRandom(long randomSeed) {
        random = new Random(randomSeed);
    }
}
