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

package factory.continuum.operations;

import continuum.operations.*;
import control.identifiers.Coordinate;
import factory.control.identifiers.CoordinateFactory;
import geometry.Geometry;
import org.dom4j.Element;

/**
 * @author dbborens
 * @test OperatorFactoryTest
 */
public class OperatorFactory {

    private Geometry geometry;
    private boolean useBoundaries;

    public OperatorFactory(Geometry geometry, boolean useBoundaries) {
        this.useBoundaries = useBoundaries;
        this.geometry = geometry;
    }

    public Operator instantiate(Element e) {

        String processClass = e.getName();

        if (processClass.equalsIgnoreCase("diffusion")) {
            return makeDiffusion(e);

        } else if (processClass.equalsIgnoreCase("advection")) {
            return makeAdvection(e);

        } else if (processClass.equalsIgnoreCase("scaling")) {
            return makeScaling(e);

        } else if (processClass.equalsIgnoreCase("compound")) {
            return makeCompound(e);

        } else {
            String msg = "Unrecognized operator '" +
                    processClass + ".'";

            throw new IllegalArgumentException(msg);
        }
    }

    private Operator makeCompound(Element e) {
        Element children = e.element("children");
        Operator compound = new CompoundOperator(geometry, useBoundaries, children);
        compound.init();
        return compound;
    }

    protected Operator makeScaling(Element e) {
        double lambda = Double.valueOf(get(e, "lambda"));
        Scaling scaling = new Scaling(geometry, useBoundaries, lambda);
        scaling.init();
        return scaling;
    }

    protected Operator makeAdvection(Element e) {
        Coordinate displacement = makeDisplacementVector(e);
        double r = Double.valueOf(get(e, "r"));
        Advection advection = new Advection(geometry, useBoundaries, displacement, r);
        advection.init();
        return advection;
    }

    protected Operator makeDiffusion(Element e) {
        double r = Double.valueOf(get(e, "r"));
        Diffusion diffusion = new Diffusion(geometry, useBoundaries, r);
        diffusion.init();
        return diffusion;
    }

    /**
     * Construct a displacement vector from a displacement tag
     *
     * @param e
     * @return
     */
    private Coordinate makeDisplacementVector(Element e) {
        Element dispElem = e.element("displacement");
        if (dispElem == null) {
            throw new IllegalArgumentException("No displacement vector provided for advection operator.");
        }
        Coordinate displacement = CoordinateFactory.instantiate(dispElem);
        return displacement;
    }


    /**
     * Pull in a single-datum element
     *
     * @param g
     * @param key
     * @return
     */
    protected String get(Element g, String key) {
        Element vElem = g.element(key);
        if (vElem == null) {
            throw new IllegalArgumentException("Required parameter '" +
                    key + "' not defined for operator " +
                    g.getName() + ".\nXML:\n\n" + g.asXML());
        }

        Object value = vElem.getData();

        return value.toString();
    }
}
