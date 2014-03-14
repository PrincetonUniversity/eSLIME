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

package geometry;

import io.project.GeometryManager;
import org.dom4j.Element;

/**
 * Created by David B Borenstein on 1/11/14.
 */
public class MockGeometryManager extends GeometryManager {

    private Geometry geometry;

    public MockGeometryManager() {
        super();
    }

    /**
     * MockGeometryManager will always return this geometry
     * on calls to make().
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    /**
     * Calls to MockGeometryManager::make() will always
     * return the geometry assigned by the user, no matter
     * what the argument to Element.
     */
    public Geometry make(Element layerRoot) {
        return geometry;
    }
}
