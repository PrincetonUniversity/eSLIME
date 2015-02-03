/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.layers.continuum;

import control.arguments.GeometryDescriptor;
import control.identifiers.Coordinate;
import factory.geometry.boundaries.BoundaryFactory;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import layers.continuum.ContinuumLayer;
import layers.continuum.ContinuumLayerContent;
import layers.continuum.ContinuumLayerScheduler;
import org.dom4j.Element;

import java.util.function.Function;

/**
 * Created by dbborens on 1/8/15.
 */
public abstract class ContinuumLayerFactory {

    public static ContinuumLayer instantiate(Element root, GeometryDescriptor geometryDescriptor) {
        Geometry geom = makeGeometry(root, geometryDescriptor);
        int n = geom.getCanonicalSites().length;
        Function<Coordinate, Integer> indexer = geom.getIndexer();
        String id = root.element("id").getText();

        ContinuumLayerContent content = new ContinuumLayerContent(indexer, geom.getCanonicalSites().length);
        ContinuumLayerScheduler scheduler = ContinuumLayerSchedulerFactory.instantiate(content, indexer, n, id);

        return new ContinuumLayer(scheduler, content, geom);
    }

    private static Geometry makeGeometry(Element root, GeometryDescriptor geometryDescriptor) {
        Boundary boundary = makeBoundary(root, geometryDescriptor);
        Geometry geom = geometryDescriptor.make(boundary);
        return geom;
    }

    private static Boundary makeBoundary(Element root, GeometryDescriptor geometryDescriptor) {
        Element boundaryElem = root.element("boundary");
        Boundary boundary = BoundaryFactory.instantiate(boundaryElem, geometryDescriptor);
        return boundary;
    }
}
