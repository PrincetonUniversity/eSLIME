/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry;

import factory.geometry.GeometryFactory;
import org.dom4j.Element;

/**
 * Created by David B Borenstein on 1/11/14.
 */
public class MockGeometryFactory extends GeometryFactory {

    private Geometry geometry;

    public MockGeometryFactory() {
        super();
    }

    /**
     * MockGeometryFactory will always return this geometry
     * on calls to make().
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    /**
     * Calls to MockGeometryFactory::make() will always
     * return the geometry assigned by the user, no matter
     * what the argument to Element.
     */
    public Geometry make(Element layerRoot) {
        return geometry;
    }
}
